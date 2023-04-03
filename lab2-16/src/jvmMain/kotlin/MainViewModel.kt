import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.util.*
import javax.swing.JOptionPane

class MainViewModel {

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    companion object {
        const val POLYNOMIAL_POWER = 38
    }

    private val encoder = Encoder()
    val key = encoder.bufGenkey

    fun onStartValueChange(newValue: String) {
        _mainUiState.update { it.copy(startValue = newValue) }
    }

    private fun preprocess(str: String): String {
        val upper = str.uppercase(Locale.getDefault())
        return upper.filter { it in "01" }
    }

    fun onProcessClicked() {
        val outputPath = _mainUiState.value.outputFile.absolutePath
        val inputPath = _mainUiState.value.inputFile.absolutePath
        val key = preprocess(_mainUiState.value.startValue)

        if (inputPath.isEmpty() || outputPath.isEmpty() || key.length != POLYNOMIAL_POWER) {
            showMessage("Please fill in all the fields")
            return
        }

        encoder.encode(
            polynomialPowers = intArrayOf(POLYNOMIAL_POWER, 6, 5, 1),
            initialKey = key,
            pathToSrcFile = _mainUiState.value.inputFile.absolutePath,
            pathToResFile = _mainUiState.value.outputFile.absolutePath
        )

        showMessage("Success")
    }

    fun onOpenKeyClicked() {
        _mainUiState.update { it.copy(keyVisibility = !it.keyVisibility) }
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
                    _mainUiState.update { it.copy(inputFile = file) }
                }
            }
            1 -> {
                val file = File(path)

                if (!file.exists()) {
                    file.createNewFile()
                }

                _mainUiState.update { it.copy(outputFile = file) }
            }
        }
    }

    private fun showMessage(message: String) {
        JOptionPane.showMessageDialog(Frame(), message)
    }
}