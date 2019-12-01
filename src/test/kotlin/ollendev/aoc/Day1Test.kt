package ollendev.aoc

import org.junit.Assert.assertEquals
import org.junit.Test

internal class FuelCalculatorTest {

    val fuelCalculator: FuelCalculator = FuelCalculator()

    @Test
    fun `mass of 12 needs 2`() {
        val fuel = fuelCalculator.calculateModuleFuelRequirements(12)

        assertEquals(2, fuel)
    }

    @Test
    fun `mass of 14 needs 2`() {
        val fuel = fuelCalculator.calculateModuleFuelRequirements(14)

        assertEquals(2, fuel)
    }

    @Test
    fun `mass of 1969 needs 654`() {
        val fuel = fuelCalculator.calculateModuleFuelRequirements(1969)

        assertEquals(654, fuel)
    }

    @Test
    fun `mass of 100756 needs 33583`() {
        val fuel = fuelCalculator.calculateModuleFuelRequirements(100756)

        assertEquals(33583, fuel)
    }

    @Test
    fun `module mass list of 12, 14, 1969, 100756 equals 34,241`() {
        val fuel = fuelCalculator.moduleMassListFuelRequirement(listOf(12, 14, 1969, 100756))

        assertEquals(34241, fuel)
    }
}