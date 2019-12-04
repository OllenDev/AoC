package ollendev.aoc

fun main() {
    val min = 307237
    val max = 769058
    var count = 0
    for (i in min..max) {
        val digits = getDigits(i)
        if (meetsCriteria(digits)) {
            count += 1
        }
    }
    println("Number of matches = $count")
}

fun meetsCriteria(digits: List<Int>): Boolean {
    var last = digits[0]
    val duplicateCounts = mutableMapOf<Int, Int>()

    for (i in 1 until digits.size) {
        val curr = digits[i]
        if (curr < last) {
            return false
        } else if (curr == last) {
            duplicateCounts[curr] = (duplicateCounts[curr] ?: 0) + 1
        }
        last = curr
    }

    return duplicateCounts.filterValues { it == 1 }.isNotEmpty()
}

fun getDigits(num: Int): List<Int> {
    var temp = num
    val digits = mutableListOf<Int>()
    while (temp > 0) {
        val digit = temp % 10
        digits.add(digit)
        temp /= 10
    }
    return digits.reversed()
}
