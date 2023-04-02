import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

@Composable
@Preview
fun App(mainViewModel: MainViewModel) {
    val mainUiState by mainViewModel.mainUiState.collectAsState()

    MaterialTheme {
        Scaffold {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Input path",
                        modifier = Modifier.weight(0.1f)
                    )
                    TextField(
                        value = mainUiState.inputFile.absolutePath,
                        onValueChange = { },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.weight(0.7f)
                    )
                    Button(
                        onClick = { mainViewModel.onSelectFileClicked(0) },
                        content = { Text("Select file") },
                        modifier = Modifier.weight(0.2f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Output path",
                        modifier = Modifier.weight(0.1f)
                    )
                    TextField(
                        value = mainUiState.outputFile.absolutePath,
                        onValueChange = { },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.weight(0.7f)
                    )
                    Button(
                        onClick = { mainViewModel.onSelectFileClicked(1) },
                        content = { Text("Select file") },
                        modifier = Modifier.weight(0.2f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Start value",
                        modifier = Modifier.weight(0.1f)
                    )
                    TextField(
                        value = mainUiState.startValue,
                        onValueChange = {
                            mainViewModel.onStartValueChange(it)
                        },
                        singleLine = true,
                        modifier = Modifier.weight(0.7f)
                    )
                    Button(
                        onClick = { mainViewModel.onProcessClicked() },
                        content = { Text("Process") },
                        modifier = Modifier.weight(0.2f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { mainViewModel.onOpenKeyClicked() },
                        content = { Text("Open generated key") }
                    )
                }
            }
        }
    }
}

fun main() = application {
    val mainViewModel = MainViewModel()

    Window(onCloseRequest = ::exitApplication, state = WindowState(
        size = DpSize(600.dp, 300.dp)
    )) {
        App(mainViewModel = mainViewModel)
    }
}
