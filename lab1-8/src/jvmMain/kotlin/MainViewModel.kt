import algorithms.EncryptDecryptAlgorithms
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.JOptionPane

class MainViewModel {

    private val _mainUiState = MutableStateFlow(EncryptDecryptAlgorithms())
    val mainUiState = _mainUiState.asStateFlow()

    fun onKeyChange(newValue: String) {
        _mainUiState.update { it.copy(key = newValue, getResult = false) }
    }

    private fun putFilePath(path: String) {
        _mainUiState.update { it.copy(input = File(path)) }
    }

    fun onGetResultPressed() {
        val dialog = FileDialog(Frame(), "Select File", FileDialog.SAVE)
        dialog.isVisible = true
        val path = dialog.directory + dialog.file

        keyToLoweCase()

        try {
            if (_mainUiState.value.key.isBlank()) {
                JOptionPane.showMessageDialog(Frame(), "Error. Key is empty.")
            } else {
                if (_mainUiState.value.algorithm) {
                    _mainUiState.update { currentState ->
                        currentState.copy(key = currentState.key.filter { it in KeysVValues })
                    }
                } else {
                    _mainUiState.update { currentState ->
                        currentState.copy(key = currentState.key.filter { it in KeysDValues.toString() })
                    }
                }
                if (_mainUiState.value.key.isBlank()) {
                    JOptionPane.showMessageDialog(Frame(), "Error. Key is blank.")
                    throw IllegalArgumentException()
                }
                if (_mainUiState.value.encryption) {
                    _mainUiState.value.encrypt(
                        key = _mainUiState.value.key,
                        resource = _mainUiState.value.input,
                        path = path
                    )
                } else {
                    _mainUiState.value.decrypt(
                        key = _mainUiState.value.key,
                        resource = _mainUiState.value.input,
                        path = path
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onGetFile() {
        _mainUiState.update { it.copy(getFile = !it.getFile) }
        if (_mainUiState.value.getFile) {
            val dialog = FileDialog(Frame(), "Select File", FileDialog.LOAD)
            dialog.isVisible = true
            if (dialog.file != null) {
                putFilePath(dialog.directory + dialog.file)
            }
        }
    }

    fun onChangeEncryptDecrypt() {
        _mainUiState.update { it.copy(encryption = !it.encryption) }
    }

    fun onChangeAlgorithm() {
        _mainUiState.update { it.copy(algorithm = !it.algorithm) }
    }

    private fun keyToLoweCase() {
        _mainUiState.update { it.copy(key = it.key.lowercase()) }
    }

    companion object {
        const val KeysVValues = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
        val KeysDValues = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    }
}