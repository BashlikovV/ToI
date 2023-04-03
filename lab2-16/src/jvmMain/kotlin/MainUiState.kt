import java.io.File

data class MainUiState(
    val inputFile: File = File(""),
    val outputFile: File = File(""),
    val startValue: String = "",
    val keyVisibility: Boolean = false
)
