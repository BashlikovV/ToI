import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                        onClick = {
                            mainViewModel.onOpenKeyClicked()
                        },
                        content = { Text("Open generated key") }
                    )
                }
            }
            KeyWindow(
                initialState = mainUiState.keyVisibility,
                key = mainViewModel.bufGenkey,
                srcBytes = mainViewModel.bufSrcFile,
                resBytes = mainViewModel.bufResFile
            ) {
                mainViewModel.onOpenKeyClicked()
            }
        }
    }
}

@Composable
fun KeyWindow(
    initialState: Boolean,
    key: String,
    srcBytes: String,
    resBytes: String,
    onCloseRequest: () -> Unit
) {
    val modifier = Modifier.fillMaxSize()

    Window(
        visible = initialState,
        onCloseRequest = onCloseRequest,
        state = WindowState(
            size = DpSize(600.dp, 600.dp)
        )
    ) {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onCloseRequest,
                        content = { Text("Close") }
                    )
                }
            }
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.weight(0.33f)
                ) {
                    LazyColView(srcBytes, "Source")
                }
                Column(
                    modifier = Modifier.weight(0.33f)
                ) {
                    LazyColView(key, "Key")
                }
                Column(
                    modifier = Modifier.weight(0.33f)
                ) {
                    LazyColView(resBytes, "Result")
                }
            }
        }
    }
}

@Composable
fun LazyColView(data: String, name: String) {
    LazyColumn (
        modifier = Modifier
            .scrollable(
                state = rememberScrollableState{ 0f },
                orientation = Orientation.Vertical
            )
            .border(width = 1.dp, color = Color.Black)
            .padding(5.dp),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(name)
        }
        for (i in 8..data.length step 8) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = i.toString() + " -> [" + data.subSequence(i - 8, i) + "]",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

fun main() = application {
    val mainViewModel = MainViewModel()

    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(600.dp, 300.dp)
        )
    ) {
        App(mainViewModel = mainViewModel)
    }
    //11111111111111111111111111111111111111
    //00000000111111111111111111111111111111

}