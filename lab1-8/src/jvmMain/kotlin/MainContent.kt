import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = MainViewModel()
) {
    val mainUiState by mainViewModel.mainUiState.collectAsState()

    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 75.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Button(
                    onClick = {
                        mainViewModel.onGetFile()
                    }
                ) {
                    Text(text = "Open file")
                }
            }
            Column {
                Button(
                    onClick = {
                        mainViewModel.onGetResultPressed()
                    }
                ) {
                    Text(text = "get result")
                }
            }
            Column {
                TextField(
                    value = mainUiState.key,
                    label = { Text(text = "key input") },
                    onValueChange = {
                        mainViewModel.onKeyChange(it)
                    }
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.Gray)
                .align(alignment = Alignment.CenterEnd)
                .width(75.dp)
        ) {
            Text(text = "encrypt|decrypt", overflow = TextOverflow.Clip)
            Row {
                //Encryption - decryption checkbox
                Checkbox(checked = mainUiState.encryption, onCheckedChange = { mainViewModel.onChangeEncryptDecrypt() })
            }
            Text(text = "dec|vig", overflow = TextOverflow.Clip)
            Row {
                //Algorithms switch
                Switch(checked = mainUiState.algorithm == 0, onCheckedChange = {
                    mainViewModel.onChangeAlgorithm(if (it) 0 else 1)
                })
            }
        }
        Row(modifier = modifier.padding(top = 55.dp)) {
            Text(
                modifier = modifier,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                text = if (mainUiState.getResult) {
                    mainUiState.getChipheredText(mainViewModel)
                } else {
                    ""
                }
            )
        }
    }
}