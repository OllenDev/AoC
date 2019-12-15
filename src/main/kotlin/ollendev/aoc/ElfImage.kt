package ollendev.aoc

import java.lang.IllegalStateException

fun main() {
    val text = FileLoader.getFile("ImageInput.txt").readText()
    val rawImage = text.map { it.toInt() - 48 }
    val image = ElfImage(rawImage, 25, 6)
    println("CheckSum = ${image.checkSum()}")
    println("--------------------")
    println(image.print())
    println("--------------------")
}

class ElfImage(private val raw: List<Int>, private val width: Int, private val height: Int) {

    private val size = width * height

    fun print() {
        val composite = compose()
        composite.chunked(width).forEach { println(it.joinToString("")) }
    }

    private fun compose(): List<Char> {
        val composite = MutableList(size) { '+' }
        val layers = raw.chunked(size)
        for (i in 0 until size) {
            layers.reversed().forEach { layer ->
                val color = layer[i]
                if (color == 0) {
                    composite[i] = ' '
                } else if (color == 1) {
                    composite[i] = '*'
                }
            }
        }
        return composite
    }

    fun checkSum(): Int {
        val countMap = mutableMapOf<Int, Pair<Int, Int>>()
        raw.chunked(size).forEach { layer ->
            val zeros = layer.joinToString("").count { it == '0' }
            val ones = layer.joinToString("").count { it == '1' }
            val twos = layer.joinToString("").count { it == '2' }
            countMap[zeros] = ones to twos
        }
        val min = countMap.minBy { it.key }?.let { it.value.first * it.value.second }
        return min ?: throw IllegalStateException("Error calculating min")
    }
}