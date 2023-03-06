package algorithms

import java.awt.Dialog
import java.awt.Frame
import java.io.*
import java.util.*

class EncryptDecryptAlgorithms(
    val encryption: Boolean = true,
    val algorithm: Boolean = true,
    val input: File = File(""),
    val key: String = "",
    private val getResult: Boolean = false,
    val getFile: Boolean = false
) {

    companion object {
        const val alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
        const val count = 33
    }
    fun copy(
        encryption: Boolean = this.encryption,
        algorithm: Boolean = this.algorithm,
        input: File = this.input,
        key: String = this.key,
        getResult: Boolean = this.getResult,
        getFile: Boolean = this.getFile
    ): EncryptDecryptAlgorithms {
        return EncryptDecryptAlgorithms(encryption, algorithm, input, key, getResult, getFile)
    }

    fun encrypt(key: String, resource: File, path: String): File {
        val result = File(path)

        if (!result.exists()) {
            result.createNewFile()
        }
        val outputStream = BufferedWriter(FileWriter(result.absolutePath))
        val inputStream = BufferedReader(FileReader(resource.absolutePath))
        val text = inputStream.readText().lowercase(Locale.getDefault())
        var tmpRes = ""

        if (algorithm) {
            val msg = text.toCharArray().filter { it in alphabet }
            val msgLen = msg.size
            val keys = CharArray(msgLen)
            val encryptedMessage = CharArray(msgLen)

            var i = 0
            var j = 0
            while (i < msgLen) {
                if (j == key.length) {
                    j = 0
                }
                keys[i] = key[j]
                i++
                j++
            }

            for (idx in 0 until msgLen step 1) {
                val index = (((alphabet.indexOf(msg[idx]) + alphabet.indexOf(keys[idx])) % count) + alphabet.indexOf('а'))
                encryptedMessage[index] = alphabet[index]
            }
            tmpRes = encryptedMessage.concatToString()
        } else {
            if (isMutuallySimple(key.toInt())) {
                for (i in text.indices) {
                    if (text[i] in alphabet) {
                        tmpRes += alphabet[alphabet.indexOf(text[i]) * key.toInt() % count]
                    }
                }
            } else {
                val dlg = Dialog(Frame(), "Error")
                dlg.isVisible = true
            }
        }

        outputStream.write(tmpRes)
        outputStream.flush()
        outputStream.close()
        inputStream.close()
        return result
    }

    fun decrypt(key: String, resource: File, path: String): File {
        val result = File(path)

        if (!result.exists()) {
            result.createNewFile()
        }
        val outputStream = BufferedWriter(FileWriter(result.absolutePath))
        val inputStream = BufferedReader(FileReader(resource.absolutePath))
        val text = inputStream.readText().lowercase(Locale.getDefault())
        var tmpRes = ""

        if (algorithm) {

        } else {
            for (i in text.indices) {
                if (text[i] in alphabet) {
                    tmpRes += alphabet[(alphabet.indexOf(text[i]) * key.toInt()) % count]
                }
            }
        }

        outputStream.write(tmpRes)
        outputStream.flush()
        outputStream.close()
        inputStream.close()
        return result
    }

    private fun isMutuallySimple(key: Int): Boolean {
        var tmpKey = key
        var tmpCount = count
        while (tmpKey > 0 && tmpCount > 0) {
            if (tmpKey > tmpCount) {
                tmpKey %= tmpCount
            } else {
                tmpCount %= tmpKey
            }
        }

        return tmpKey + tmpCount == 1
    }
}