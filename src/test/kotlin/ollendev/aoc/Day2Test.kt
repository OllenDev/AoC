package ollendev.aoc

import org.junit.Assert.assertEquals
import org.junit.Test

internal class Day2Test {

    @Test
    fun `Compute sample 1`() {
        val ram = mutableListOf(1,9,10,3,2,3,11,0,99,30,40,50)
        runTest(ram)
        assertEquals(3500, ram[0])
    }

    @Test
    fun `Compute sample 2`() {
        val ram = mutableListOf(1,0,0,0,99)
        runTest(ram)
        assertEquals(2, ram[0])
    }

    @Test
    fun `Compute sample 3`() {
        val ram = mutableListOf(2,3,0,3,99)
        runTest(ram)
        assertEquals(mutableListOf(2,3,0,6,99), ram)
    }

    @Test
    fun `Compute sample 4`() {
        val ram = mutableListOf(2,4,4,5,99,0)
        runTest(ram)
        assertEquals(mutableListOf(2,4,4,5,99,9801), ram)
    }

    @Test
    fun `Compute sample 5`() {
        val ram = mutableListOf(1,1,1,4,99,5,6,0,99)
        runTest(ram)
        assertEquals(mutableListOf(30,1,1,4,2,5,6,0,99), ram)
    }

    private fun runTest(ram: MutableList<Int>) {
        val computer = Computer(ram)
        computer.run()
    }
}