import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    var mainViewModel: MainViewModel? = null
    LaunchedEffect(Unit) {
        mainViewModel = MainViewModel()
    }

    Window(onCloseRequest = ::exitApplication) {
        MainContent(modifier = Modifier.fillMaxSize(), mainViewModel = mainViewModel ?: MainViewModel())
    }
}
