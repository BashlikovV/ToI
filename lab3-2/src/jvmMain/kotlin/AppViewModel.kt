import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.JOptionPane

class AppViewModel {

    private val _appUiState = MutableStateFlow(MainUiState())
    val appUiState = _appUiState.asStateFlow()

    private var roots = listOf<Int>()

    private val encoder = Encoder()

    companion object {
        const val POLYNOMIAL_POWER = 38
        var bytesForInt = 1
    }

    var bufSrcFile = ""
    var bufResFile = ""
    var bufGenkey = ""

    fun onProcessClicked(p: String, ed: Boolean, x: String, k: String) {
        val outputPath = _appUiState.value.outputFile.absolutePath
        val inputPath = _appUiState.value.inputFile.absolutePath

        if (inputPath.isEmpty() || outputPath.isEmpty()) {
            showMessage("Please fill in all the fields")
            return
        }

        val tmpP: Int
        try {
            tmpP = p.toInt()
        } catch (e: Exception) {
            showMessage("Error: ${e.message}")
            return
        }

        if (ed) {
            encode(tmpP, roots.first(), x.toInt(), k.toInt())
        } else {
            decode(tmpP, x.toInt())
        }
        showMessage("Success")
    }

    private fun encode(p: Int, g: Int, x: Int, k: Int) {
        val resultText: StringBuilder
        val cipherText = StringBuilder()
        val y = Utils.powMod(g, x, p)

        val bytes = _appUiState.value.inputFile.inputStream().readBytes()
        for (byte in bytes) {
            if (p < byte) {
                showMessage("M must be less than p")
                return
            }
            val cipherPare = encoder.encrypt(byte.toInt(), p, g, y, k)
            if (cipherPare.first() >= 256 || cipherPare[1] >= 256) {
                bytesForInt = 2
            }
            cipherText.append(cipherPare)
        }
        resultText = cipherText
        bufResFile = resultText.toString()
        _appUiState.value.outputFile.writeText(bufResFile)
    }

    private fun decode(p: Int, x: Int) {
        val bytes = _appUiState.value.inputFile.inputStream().readBytes()
        val decipherText = StringBuilder()
        bytesForInt = bytes.first().toInt()
        var aFlag = false
        var bFlag = false
        var a: Byte? = null
        var b: Byte? = null
        for (i in bytes.indices) {
            if (i == 0 || i % 2 == 0) {
                a = bytes[i]
                aFlag = true
            }
            if (i == 1 || i % 3 == 0) {
                b = bytes[i]
                bFlag = true
            }
            if (aFlag && bFlag) {
                decipherText.append(encoder.decrypt(a!!.toInt(), b!!.toInt(), x, p))
                aFlag = false
                bFlag = false
            }
        }
        bufResFile = decipherText.toString()
        _appUiState.value.outputFile.writeText(decipherText.toString())
    }

    fun onOpenKeyClicked() {
        _appUiState.update { it.copy(keyVisibility = !it.keyVisibility) }
    }

    fun onSelectFileClicked(field: Int) {
        val dialog = FileDialog(
            Frame(),
            if (field == 0) "Select input file" else "Select output file",
            if (field == 0) FileDialog.LOAD else FileDialog.SAVE
        )
        dialog.isVisible = true
        val path = dialog.directory + dialog.file

        when (field) {
            0 -> {
                val file = File(path)

                if (!file.exists()) {
                    showMessage("Input file does not exists")
                } else {
                    _appUiState.update { it.copy(inputFile = file) }
                }
            }
            1 -> {
                val file = File(path)

                if (!file.exists()) {
                    file.createNewFile()
                }

                _appUiState.update { it.copy(outputFile = file) }
            }
        }
    }

    private fun showMessage(message: String) {
        JOptionPane.showMessageDialog(Frame(), message)
    }

    fun generateRoots(p: String, onGChange: (String) -> Unit = {_ ->}) {
        if (p.isEmpty()) {
            showMessage("Fill the p field")
        } else {
            try {
                roots = Utils.getPrimitiveRoots(p.toInt())
                onGChange(roots.joinToString())
            } catch (e: Exception) {
                showMessage("Error: ${e.message}")
            }
        }
    }

    fun chooseRandomK(p: String, onKChange: (String) -> Unit = { _ -> }) {
        if (p.isEmpty()) {
            showMessage("Fill the p field")
        } else {
            try {
                p.toInt()
            } catch (e: Exception) {
                showMessage("Error: ${e.message}")
                return
            }
            val used = mutableSetOf<Int>()
            while (true) {
                val k = (1..p.toInt()).random()
                val gcd = Utils.getGcd(k, p.toInt() - 1).first()
                if (gcd !in used && gcd == 1) {
                    onKChange(k.toString())
                    break
                } else {
                    used.add(k)
                }
            }
        }
    }

    fun chooseRandomX(p: String, onXChange: (String) -> Unit = { _ -> }) {
        if (p.isEmpty()) {
            showMessage("Fill the p field")
        } else {
            try {
                p.toInt()
            } catch (e: Exception) {
                showMessage("Error: ${e.message}")
                return
            }
            onXChange((1..p.toInt()).random().toString())
        }
    }
}