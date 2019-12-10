package ollendev.aoc

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class IntComputerTest {

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

    @Test
    fun `Add 1 + 1 = 2 address mode`() {
        val ram = mutableListOf(1,5,6,7,99,1,1,0)
        runTest(ram)
        assertEquals(mutableListOf(1,5,6,7,99,1,1,2), ram)
    }

    @Test
    fun `Add 2 + 3 = 5 immediate mode`() {
        val ram = mutableListOf(1101,2,3,5,99,0)
        runTest(ram)
        assertEquals(mutableListOf(1101,2,3,5,99,5), ram)
    }

    @Test
    fun `Multiply 2 * 3 = 6 address mode`() {
        val ram = mutableListOf(2,5,6,7,99,2,3,0)
        runTest(ram)
        assertEquals(mutableListOf(2,5,6,7,99,2,3,6), ram)
    }

    @Test
    fun `Multiply 2 * 3 = 6 immediate mode`() {
        val ram = mutableListOf(1102,2,3,5,99,0)
        runTest(ram)
        assertEquals(mutableListOf(1102,2,3,5,99,6), ram)
    }

    @Test
    fun `Write input`() {
        val ram = mutableListOf(3,4,99,0,0)
        runTest(ram, 55)
        assertEquals(mutableListOf(3,4,99,0,55), ram)
    }

    @Test
    fun `Print output address mode`() {
        val sysOut = System.out
        val outContent = ByteArrayOutputStream()
        val outStream = PrintStream(outContent)
        System.setOut(outStream)

        val ram = mutableListOf(4,4,99,0,55)
        runTest(ram)
        assertEquals("55\n", outContent.toString())

        System.setOut(sysOut)
    }

    private fun runTest(ram: MutableList<Int>, input: Int = 1) {
        val computer = IntComputer(ram, input)
        computer.run()
    }
}

class CodeTest {

    @Test
    fun `01001 is an add instruction`() {
        assertAddInstruction("01001")
    }

    @Test
    fun `01 is an add instruction`() {
        assertAddInstruction("01")
    }

    @Test
    fun `1 is an add instruction`() {
        assertAddInstruction("1")
    }

    @Test
    fun `002 is a multiply instruction`() {
        assertMultiplyInstruction("002")
    }

    @Test
    fun `02 is a multiply instruction`() {
        assertMultiplyInstruction("02")
    }

    @Test
    fun `2 is a multiply instruction`() {
        assertMultiplyInstruction("2")
    }

    @Test
    fun `99 is a halt instruction`() {
        assertHaltInstruction("99")
    }

    @Test
    fun `0099 is a halt instruction`() {
        assertHaltInstruction("0099")
    }

    @Test
    fun `11101 yields 3 immediate modes`() {
        val code = Code("11101")
        assertEquals(ParamMode.Immediate, code.getParamMode(0))
        assertEquals(ParamMode.Immediate, code.getParamMode(1))
        assertEquals(ParamMode.Immediate, code.getParamMode(2))
    }

    @Test
    fun `missing codes default to address mode`() {
        val code = Code("02")
        assertEquals(ParamMode.Address, code.getParamMode(0))
        assertEquals(ParamMode.Address, code.getParamMode(1))
        assertEquals(ParamMode.Address, code.getParamMode(2))
    }

    @Test
    fun `partial codes parsed correctly`() {
        val code = Code("1002")
        assertEquals(ParamMode.Address, code.getParamMode(0))
        assertEquals(ParamMode.Immediate, code.getParamMode(1))
        assertEquals(ParamMode.Address, code.getParamMode(2))
    }

    private fun assertAddInstruction(input: String) {
        val code = Code(input)
        assertEquals(Instruction.Add, code.instruction)
    }

    private fun assertMultiplyInstruction(input: String) {
        val code = Code(input)
        assertEquals(Instruction.Multiply, code.instruction)
    }

    private fun assertHaltInstruction(input: String) {
        val code = Code(input)
        assertEquals(Instruction.Halt, code.instruction)
    }
}