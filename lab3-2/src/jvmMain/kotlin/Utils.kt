import kotlin.math.floor
import kotlin.math.sqrt

object Utils {

    fun powMod(a: Int, z: Int, m: Int): Int {
        var result = 1
        var tmpZ = z
        var tmpA = a

        while (tmpZ != 0) {
            while (tmpZ % 2 == 0) {
                tmpZ = tmpZ.div(2)
                tmpA = (tmpA * tmpA) % m
            }
            tmpZ -= 1
            result = (result * tmpA) % m
        }

        return result
    }

    fun isPrime(n: Int): Boolean {
        var a: Int

        when(n) {
            1 -> return false
            2, 3 -> return true
            else -> {
                val used = mutableListOf<Int>()
                for (i in 0..3) {
                    while (true) {
                        a = (0 until n).random()
                        if (a !in used) {
                            break
                        }
                    }
                    if (powMod(a, n - 1, n) != 1) {
                        return false
                    }
                    used.add(a)
                }
            }
        }

        return true
    }

    fun factorize(n: Int): Set<Int> {
        val divisors = mutableSetOf<Int>()
        var i = 2
        var tmpN = n

        while (i * i < tmpN) {
            while (tmpN % i == 0) {
                divisors.add(i)
                tmpN = tmpN.div(i)
            }
            i += 1
        }
        if (tmpN != 1) {
            divisors.add(n)
        }

        return divisors
    }

    fun getPrimitiveRoots(n: Int): List<Int> {
        val primitiveRoots = mutableListOf<Int>()
        val phi = n - 1
        val divisors = factorize(phi)

        for (i in 1..n) {
            var isRoot = true
            if (powMod(i, phi, n) != 1) {
                continue
            }
            divisors.forEach { divisor ->
                if (powMod(i, phi.div(divisor), n) == 1) {
                    isRoot = false
                }
            }
            if (isRoot) {
                primitiveRoots.add(i)
            }
        }

        return primitiveRoots
    }

    fun getGcd(a: Int, b: Int): List<Int> {
        if (a == 0) {
            return listOf(b, 0, 1)
        }
        val result = getGcd(b % a, a)
        val x = result.last() - (b.div(a)) * result[1]
        val y = result[1]

        return listOf(result.first(), x, y)
    }

    fun describeLogarithm(y: Int, g: Int, p: Float) : Int? {
        val m = floor(sqrt(p)) + 1
        val a = powMod(g, m.toInt(), p.toInt())
        var gamma = a
        val powers = mutableMapOf<Int, Int>()
        var result: Int? = null

        for (i in 1..(m + 1).toInt()) {
            powers[gamma] = i
            gamma = (gamma * a % p).toInt()
        }
        for (i in 1..m.toInt()) {
            if (powers.containsValue((y * powMod(g, i, p.toInt()) % p).toInt())) {
                val j = powers[(y * powMod(g, i, p.toInt()) % p).toInt()]
                if (j != null) {
                    result = (i * m - j).toInt()
                }
            }
        }

        return result
    }
}