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
    fun `module mass list of 12, 14, 1969, 100756 equals 51,316`() {
        val fuel = fuelCalculator.moduleMassListFuelRequirement(listOf(12, 14, 1969, 100756))

        assertEquals(51316, fuel)
    }

    @Test
    fun `for 2 fuel with additional fuel mass is 2`() {
        val totalFuel = fuelCalculator.calculateTotalFuelMass(2)

        assertEquals(2, totalFuel)
    }

    @Test
    fun `for 654 fuel total fuel needs is 966`() {
        val totalFuel = fuelCalculator.calculateTotalFuelMass(654)

        assertEquals(966, totalFuel)
    }

    @Test
    fun `for 33583 fuel total fuel needs is 50346`() {
        val totalFuel = fuelCalculator.calculateTotalFuelMass(33583)

        assertEquals(50346, totalFuel)
    }
}