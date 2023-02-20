package algorithms

import MainViewModel
import java.io.File

class EncryptDecryptAlgorithms(
    val encryption: Boolean = true,
    val algorithm: Int = 0,
    private val input: File = File(""),
    val key: String = "",
    val getResult: Boolean = false,
    val getFile: Boolean = false
) {

    fun getChipheredText(mainViewModel: MainViewModel): String {
        val result = this.key
//        mainViewModel.removeKey()
        return result
    }

    fun copy(
        encryption: Boolean = this.encryption,
        algorithm: Int = this.algorithm,
        input: File = this.input,
        key: String = this.key,
        getResult: Boolean = this.getResult,
        getFile: Boolean = this.getFile
    ): EncryptDecryptAlgorithms {
        return EncryptDecryptAlgorithms(encryption, algorithm, input, key, getResult, getFile)
    }
}