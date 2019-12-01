package ollendev.aoc

import java.io.File

fun main() {
    val fuelCalculator = FuelCalculator()
    val fileUrl = fuelCalculator::class.java.classLoader.getResource("modulemasses.txt")
    val file = File(fileUrl.toURI())
    val moduleMasses = readInput(file)
    val fuelRequirements = fuelCalculator.moduleMassListFuelRequirement(moduleMasses)
    println(fuelRequirements)
}

fun readInput(file: File): List<Int> {
    val moduleMasses = mutableListOf<Int>()
    file.forEachLine { line ->
        moduleMasses.add(line.toInt())
    }
    return moduleMasses
}

class FuelCalculator {

    fun calculateModuleFuelRequirements(mass: Int) = mass / 3 - 2

    fun moduleMassListFuelRequirement(moduleMasses: List<Int>): Int = moduleMasses.fold(0) { acc, mass ->
            acc + calculateModuleFuelRequirements(mass)
        }
}