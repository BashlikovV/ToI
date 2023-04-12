import java.io.File
import kotlin.experimental.xor
import kotlin.math.pow

class Encoder {
    companion object {
        const val BITES = 8
    }
    private var bufSrcFile = StringBuilder()
    private var bufResFile = StringBuilder()
    private var bufGenkey = StringBuilder()

    fun encode(
        polynomialPowers: IntArray,
        initialKey: String,
        pathToSrcFile: String,
        pathToResFile: String,
        mainViewModel: MainViewModel
    ) {
        val reg = Register(polynomialPowers, initialKey)

        val srcBytes = File(pathToSrcFile).readBytes()

        for (i in srcBytes.indices) {
            println("Processing $i")
            val tmp = Integer.toBinaryString(srcBytes[i].toInt() and 0xFF).format("%8s", "0")
            if (tmp.length < 8) {
                repeat(8 - tmp.length) {
                    bufSrcFile.append("0")
                }
            }
            bufSrcFile.append("$tmp ")

            val currKey = StringBuilder()
            repeat(BITES) {
                currKey.append(reg.generateKeyBit())
            }

            var keyByte = 0.toByte()

            for (j in currKey.toString().indices) {
                val bp = (currKey.toString()[j].digitToInt()).toByte() * 2.0.pow((BITES - 1 - j)).toInt().toByte()
                keyByte = (keyByte + bp).toByte()
            }

            bufGenkey.append(currKey.toString())
            srcBytes[i] = srcBytes[i] xor keyByte
            val tmp2 = Integer.toBinaryString(srcBytes[i].toInt() and 0xFF).format("%8s", "0")
            if (tmp2.length < 8) {
                repeat(8 - tmp2.length) {
                    bufResFile.append("0")
                }
            }
            bufResFile.append("$tmp2 ")
        }
        File(pathToResFile).writeBytes(srcBytes)
        mainViewModel.bufGenkey = bufGenkey.toString().replace(" ", "")
        mainViewModel.bufSrcFile = bufSrcFile.toString().replace(" ", "")
        mainViewModel.bufResFile = bufResFile.toString().replace(" ", "")
    }
}