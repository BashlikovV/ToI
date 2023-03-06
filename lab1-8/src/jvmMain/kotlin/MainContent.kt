import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Preview
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = MainViewModel()
) {
    val mainUiState by mainViewModel.mainUiState.collectAsState()

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column {
                Row {
                    Text(text = if (mainUiState.encryption) "Encryption" else "Decryption")
                }
                Row {
                    Switch(
                        checked = mainUiState.encryption,
                        onCheckedChange = { mainViewModel.onChangeEncryptDecrypt() }
                    )
                }
            } // Encryption / decryption choosing section
            Column {
                Row {
                    Text(text = if (mainUiState.algorithm) "Vizhiner" else "Decimation")
                }
                Row {
                    Switch(
                        checked = mainUiState.algorithm,
                        onCheckedChange = { mainViewModel.onChangeAlgorithm() }
                    )
                }
            } // Algorithm choosing section
            Column {
                TextField(
                    value = mainUiState.key,
                    label = { Text(text = "key input") },
                    onValueChange = {
                        mainViewModel.onKeyChange(it)
                    }
                )
            } // Key input section
        }
        Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceAround) {
            Column {
                Button(
                    onClick = {
                        mainViewModel.onGetFile()
                    }
                ) {
                    Text(text = "Open file")
                }
            } // Open file section
            Column {
                Button(
                    onClick = {
                        mainViewModel.onGetResultPressed()
                    }
                ) {
                    Text(text = "Write into file")
                }
            } // Write into file section
        }
    }
}