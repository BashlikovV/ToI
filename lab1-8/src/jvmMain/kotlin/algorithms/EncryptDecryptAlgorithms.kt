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
            val keys = CharArray(msg.size)
            val encryptedMessage = CharArray(msg.size)

            var j = 0
            msg.forEachIndexed { index, _ ->
                if (j == key.length) {
                    j = 0
                }
                keys[index] = key[j]
                j++
            }

            for (idx in msg.indices step 1) {
                val tmp = ((alphabet.indexOf(msg[idx]) + alphabet.indexOf(keys[idx])) % count)
                val index = tmp + alphabet.indexOf('а')
                encryptedMessage[idx] = alphabet[index]
            }
            tmpRes = encryptedMessage.concatToString()
        } else {
            val msg = text.filter { it in alphabet }
            if (isMutuallySimple(key.toInt())) {
                msg.forEachIndexed { idx, _ ->
                    tmpRes += alphabet[alphabet.indexOf(msg[idx]) * key.toInt() % count]
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
            val msg = text.toCharArray().filter { it in alphabet }
            val keys = CharArray(msg.size)
            val decryptedMessage = CharArray(msg.size)

            var j = 0
            msg.forEachIndexed { index, _ ->
                if (j == key.length) {
                    j = 0
                }
                keys[index] = key[j]
                j++
            }

            for (idx in msg.indices step 1) {
                val tmp = (((alphabet.indexOf(msg[idx]) - alphabet.indexOf(keys[idx])) + count) % count)
                val index = tmp + alphabet.indexOf('а')
                decryptedMessage[idx] = alphabet[index]
            }
            tmpRes = decryptedMessage.concatToString()
        } else {
            val msg = text.filter { it in alphabet }
            if (isMutuallySimple(key.toInt())) {
                msg.forEachIndexed { i, _ ->
                    val inverse = mmi(key.toInt())
                    tmpRes += alphabet[(alphabet.indexOf(msg[i]) * inverse) % count]
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

    private fun mmi(a: Int): Int {
        var tmpA = a

        tmpA %= count
            for (i in 1 until count step 1) {
                if ((tmpA * i) % count == 1) {
                    return i
                }
            }
        return -1
    }
}