package ollendev.aoc

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class IntComputerTest {
    private val sysOut = System.out
    private lateinit var outContent: ByteArrayOutputStream

    @Before
    fun setup() {
        outContent = ByteArrayOutputStream()
        val outStream = PrintStream(outContent)
        System.setOut(outStream)
    }

    @After
    fun tearDown() {
        System.setOut(sysOut)
    }

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
        val ram = mutableListOf(4,4,99,0,55)
        runTest(ram)
        assertEquals("55\n", outContent.toString())
    }

    @Test
    fun `test program input == 8 address mode`() {
        val ram = mutableListOf(3,9,8,9,10,9,4,9,99,-1,8)
        runTest(ram, 8)
        assertEquals("1\n", outContent.toString())
    }

    @Test
    fun `test program input != 8 address mode`() {
        val ram = mutableListOf(3,9,8,9,10,9,4,9,99,-1,8)
        runTest(ram, 5)
        assertEquals("0\n", outContent.toString())
    }

    @Test
    fun `test program input less than 8 address mode`() {
        val ram = mutableListOf(3,9,7,9,10,9,4,9,99,-1,8)
        runTest(ram, 6)
        assertEquals("1\n", outContent.toString())
    }

    @Test
    fun `test program input greater than or equal 8 address mode`() {
        val ram = mutableListOf(3,9,7,9,10,9,4,9,99,-1,8)
        runTest(ram, 8)
        assertEquals("0\n", outContent.toString())
    }

    @Test
    fun `test program input equal to 8 immediate mode`() {
        val ram = mutableListOf(3,3,1108,-1,8,3,4,3,99)
        runTest(ram, 8)
        assertEquals("1\n", outContent.toString())
    }

    @Test
    fun `test program input not equal to 8 immediate mode`() {
        val ram = mutableListOf(3,3,1108,-1,8,3,4,3,99)
        runTest(ram, 9)
        assertEquals("0\n", outContent.toString())
    }

    @Test
    fun `test input less than 8 immediate mode`() {
        val ram = mutableListOf(3,3,1107,-1,8,3,4,3,99)
        runTest(ram, 5)
        assertEquals("1\n", outContent.toString())
    }

    @Test
    fun `test input not less than 8 immediate mode`() {
        val ram = mutableListOf(3,3,1107,-1,8,3,4,3,99)
        runTest(ram, 8)
        assertEquals("0\n", outContent.toString())
    }

    @Test
    fun `jump test 0, output 1 - address mode`() {
        val ram = mutableListOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9)
        runTest(ram, 20)
        assertEquals("1\n", outContent.toString())
    }

    @Test
    fun `jump test 0, output 0 - address mode`() {
        val ram = mutableListOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9)
        runTest(ram, 0)
        assertEquals("0\n", outContent.toString())
    }

    @Test
    fun `jump test 1, output 1 - immediate mode`() {
        val ram = mutableListOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1)
        runTest(ram, 20)
        assertEquals("1\n", outContent.toString())
    }

    @Test
    fun `jump test 1, output 0 - immediate mode`() {
        val ram = mutableListOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1)
        runTest(ram, 0)
        assertEquals("0\n", outContent.toString())
    }

    @Test
    fun `input less than 8`() {
        val ram = mutableListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)
        runTest(ram, -1)
        assertEquals("999\n", outContent.toString())
    }

    @Test
    fun `input equals 8`() {
        val ram = mutableListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)
        runTest(ram, 8)
        assertEquals("1000\n", outContent.toString())
    }

    @Test
    fun `input greater than 8`() {
        val ram = mutableListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)
        runTest(ram, 200)
        assertEquals("1001\n", outContent.toString())
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