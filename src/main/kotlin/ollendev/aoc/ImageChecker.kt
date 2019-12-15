package ollendev.aoc

import java.lang.IllegalStateException

fun main() {
    val text = FileLoader.getFile("ImageInput.txt").readText()
    val image = text.map { it.toInt() - 48 }
    val checker = ImageChecker(image, 25, 6)
    print("CheckSum = ${checker.checkSum()}")
}

class ImageChecker(val image: List<Int>, val width: Int, val height: Int) {

    fun checkSum(): Int {
        val size = width * height
        val countMap = mutableMapOf<Int, Pair<Int, Int>>()
        image.chunked(size).forEach { layer ->
            val zeros = layer.joinToString("").count { it == '0' }
            val ones = layer.joinToString("").count { it == '1' }
            val twos = layer.joinToString("").count { it == '2' }
            countMap[zeros] = ones to twos
        }
        val min = countMap.minBy { it.key }?.let { it.value.first * it.value.second }
        return min ?: throw IllegalStateException("Error calculating min")
    }
}