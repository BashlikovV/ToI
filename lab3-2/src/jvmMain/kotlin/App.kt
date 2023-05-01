import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState

@Composable
@Preview
fun App(appViewModel: AppViewModel) {
    val appUiState by appViewModel.appUiState.collectAsState()
    var pInputState by rememberSaveable { mutableStateOf("") }
    var kInputState by rememberSaveable { mutableStateOf("") }
    var xInputState by rememberSaveable { mutableStateOf("") }
    var gInputState by rememberSaveable { mutableStateOf("") }
    var edState by rememberSaveable { mutableStateOf(true) }

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
                        value = appUiState.inputFile.absolutePath,
                        onValueChange = { },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.weight(0.7f)
                    )
                    Button(
                        onClick = { appViewModel.onSelectFileClicked(0) },
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
                        value = appUiState.outputFile.absolutePath,
                        onValueChange = { },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.weight(0.7f)
                    )
                    Button(
                        onClick = { appViewModel.onSelectFileClicked(1) },
                        content = { Text("Select file") },
                        modifier = Modifier.weight(0.2f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(0.5f)) {
                        Text(
                            text = "P value",
                            modifier = Modifier.weight(0.1f)
                        )
                        TextField(
                            value = pInputState,
                            onValueChange = {
                                pInputState = it
                            },
                            singleLine = true,
                            modifier = Modifier.weight(0.7f),
                            placeholder = { Text("Prime number") }
                        )
                    }
                    Row(modifier = Modifier.weight(0.5f)) {
                        Text(
                            text = "G value",
                            modifier = Modifier.weight(0.1f)
                        )
                        TextField(
                            value = gInputState,
                            onValueChange = {
                                gInputState = it
                            },
                            singleLine = true,
                            modifier = Modifier.weight(0.7f)
                        )
                        Button(
                            onClick = {
                                appViewModel.generateRoots(pInputState) { newG ->
                                    gInputState = newG
                                }
                            },
                            content = { Text("Generate") },
                            modifier = Modifier.weight(0.2f)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(0.5f)) {
                        Text(
                            text = "X value",
                            modifier = Modifier.weight(0.1f)
                        )
                        TextField(
                            value = xInputState,
                            onValueChange = {
                                xInputState = it
                            },
                            singleLine = true,
                            modifier = Modifier.weight(0.7f),
                            placeholder = { Text("x in {1,..., p - 1}") }
                        )
                        Button(
                            onClick = {
                                appViewModel.chooseRandomX(pInputState) { newX ->
                                    xInputState = newX
                                }
                            },
                            content = { Text("Choose randomly") },
                            modifier = Modifier.weight(0.2f)
                        )
                    }
                    Row(modifier = Modifier.weight(0.5f)) {
                        Text(
                            text = "K value",
                            modifier = Modifier.weight(0.1f)
                        )
                        TextField(
                            value = kInputState,
                            onValueChange = {
                                kInputState = it
                            },
                            singleLine = true,
                            modifier = Modifier.weight(0.7f),
                            placeholder = { Text("k in {1,..., p - 1}, (k, p - 1) = 1") }
                        )
                        Button(
                            onClick = {
                                appViewModel.chooseRandomK(pInputState) { newK ->
                                    kInputState = newK
                                }
                            },
                            content = { Text("Choose randomly") },
                            modifier = Modifier.weight(0.2f)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = edState,
                        onCheckedChange = { edState = !edState }
                    )
                    Button(
                        onClick = {
                            appViewModel.onProcessClicked(
                                pInputState,
                                edState,
                                xInputState,
                                kInputState
                            )
                        },
                        content = {
                            Text(if (edState) "encryption" else "decryption")
                        }
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            appViewModel.onOpenKeyClicked()
                        },
                        content = { Text("Open generated values") }
                    )
                }
            }
            KeyWindow(
                initialState = appUiState.keyVisibility,
                key = "",
                srcBytes = "",
                resBytes = ""
            ) {
                appViewModel.onOpenKeyClicked()
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