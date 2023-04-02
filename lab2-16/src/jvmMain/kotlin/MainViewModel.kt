import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.swing.JOptionPane

class MainViewModel {

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    fun onStartValueChange(newValue: String) {
        _mainUiState.update { it.copy(startValue = newValue) }
    }

    fun onProcessClicked() {

    }

    fun onOpenKeyClicked() {

    }

    fun onSelectFileClicked(field: Int) {
        val dialog = FileDialog(
            Frame(),
            if (field == 0) "Select input file" else "Select output file",
            FileDialog.LOAD
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

    fun showMessage(message: String) {
        JOptionPane.showMessageDialog(Frame(), message)
    }
}