class Encoder {

    fun encrypt(m: Int, p: Int, g: Int, y: Int, k: Int): List<Int> {
        val a = Utils.powMod(g, k, p)
        val b = Utils.powMod(y, k, p) * m % p

        return listOf(a, b)
    }

    fun decrypt(a: Int, b: Int, x: Int, p: Int): Int {
        return Utils.powMod(a, p - 1 - x, p) * b % p
    }
}