import algorithms.EncryptDecryptAlgorithms
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

class MainViewModel {

    private val _mainUiState = MutableStateFlow(EncryptDecryptAlgorithms())
    val mainUiState = _mainUiState.asStateFlow()

    fun applyMainUiState(newValue: EncryptDecryptAlgorithms) {
        _mainUiState.update { newValue }
    }

    fun onKeyChange(newValue: String) {
        _mainUiState.update { it.copy(key = newValue, getResult = false) }
    }

    private fun putFilePath(path: String) {
        _mainUiState.update { it.copy(input = File(path)) }
    }

    fun onGetResultPressed() {
        _mainUiState.update { it.copy(getResult = !it.getResult) }
    }

    fun onGetFile() {
        _mainUiState.update { it.copy(getFile = !it.getFile) }
        if (_mainUiState.value.getFile) {
            val dialog = FileDialog(Frame(), "Select File", FileDialog.LOAD)
            dialog.show()
            if (dialog.file != null) {
                putFilePath(dialog.directory + dialog.file)
            }
        }
    }

    fun onChangeEncryptDecrypt() {
        _mainUiState.update { it.copy(encryption = !it.encryption) }
    }

    fun onChangeAlgorithm(newValue: Int) {
        _mainUiState.update { it.copy(algorithm = newValue) }
    }

    fun removeKey() {
        _mainUiState.update { it.copy(key = "") }
    }
}