package ollendev.aoc

import org.junit.Assert.assertEquals
import org.junit.Test

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
        runTest(ram, mutableListOf(55))
        assertEquals(mutableListOf(3,4,99,0,55), ram)
    }

    @Test
    fun `Print output address mode`() {
        val ram = mutableListOf(4,4,99,0,55)
        val output = runTest(ram)
        assertEquals(55, output)
    }

    @Test
    fun `test program input == 8 address mode`() {
        val ram = mutableListOf(3,9,8,9,10,9,4,9,99,-1,8)
        val output = runTest(ram, mutableListOf(8))
        assertEquals(1, output)
    }

    @Test
    fun `test program input != 8 address mode`() {
        val ram = mutableListOf(3,9,8,9,10,9,4,9,99,-1,8)
        val output = runTest(ram, mutableListOf(5))
        assertEquals(0, output)
    }

    @Test
    fun `test program input less than 8 address mode`() {
        val ram = mutableListOf(3,9,7,9,10,9,4,9,99,-1,8)
        val output = runTest(ram, mutableListOf(6))
        assertEquals(1, output)
    }

    @Test
    fun `test program input greater than or equal 8 address mode`() {
        val ram = mutableListOf(3,9,7,9,10,9,4,9,99,-1,8)
        val output = runTest(ram, mutableListOf(8))
        assertEquals(0, output)
    }

    @Test
    fun `test program input equal to 8 immediate mode`() {
        val ram = mutableListOf(3,3,1108,-1,8,3,4,3,99)
        val output = runTest(ram, mutableListOf(8))
        assertEquals(1, output)
    }

    @Test
    fun `test program input not equal to 8 immediate mode`() {
        val ram = mutableListOf(3,3,1108,-1,8,3,4,3,99)
        val output = runTest(ram, mutableListOf(9))
        assertEquals(0, output)
    }

    @Test
    fun `test input less than 8 immediate mode`() {
        val ram = mutableListOf(3,3,1107,-1,8,3,4,3,99)
        val output = runTest(ram, mutableListOf(5))
        assertEquals(1, output)
    }

    @Test
    fun `test input not less than 8 immediate mode`() {
        val ram = mutableListOf(3,3,1107,-1,8,3,4,3,99)
        val output = runTest(ram, mutableListOf(8))
        assertEquals(0, output)
    }

    @Test
    fun `jump test 0, output 1 - address mode`() {
        val ram = mutableListOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9)
        val output = runTest(ram, mutableListOf(20))
        assertEquals(1, output)
    }

    @Test
    fun `jump test 0, output 0 - address mode`() {
        val ram = mutableListOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9)
        val output = runTest(ram, mutableListOf(0))
        assertEquals(0, output)
    }

    @Test
    fun `jump test 1, output 1 - immediate mode`() {
        val ram = mutableListOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1)
        val output = runTest(ram, mutableListOf(20))
        assertEquals(1, output)
    }

    @Test
    fun `jump test 1, output 0 - immediate mode`() {
        val ram = mutableListOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1)
        val output = runTest(ram, mutableListOf(0))
        assertEquals(0, output)
    }

    @Test
    fun `input less than 8`() {
        val ram = mutableListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)
        val output = runTest(ram, mutableListOf(-1))
        assertEquals(999, output)
    }

    @Test
    fun `input equals 8`() {
        val ram = mutableListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)
        val output = runTest(ram, mutableListOf(8))
        assertEquals(1000, output)
    }

    @Test
    fun `input greater than 8`() {
        val ram = mutableListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)
        val output = runTest(ram, mutableListOf(200))
        assertEquals(1001, output)
    }

    private fun runTest(ram: MutableList<Int>, input: MutableList<Int> = mutableListOf(1)): Int? {
        val computer = IntComputer(ram)
        var rc: ReturnCode? = null
        var run = true
        while (run) {
            when (rc) {
                null -> rc = computer.run()
                is RequestInput -> rc = computer.runWithInput(input.removeAt(0))
                is Output -> return (rc as Output).value
                else -> run = false
            }
        }
        return null
    }
}

class InstructionTest {

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
        val code = Instruction("11101")
        assertEquals(ParamMode.Immediate, code.getParamMode(0))
        assertEquals(ParamMode.Immediate, code.getParamMode(1))
        assertEquals(ParamMode.Immediate, code.getParamMode(2))
    }

    @Test
    fun `missing codes default to address mode`() {
        val code = Instruction("02")
        assertEquals(ParamMode.Address, code.getParamMode(0))
        assertEquals(ParamMode.Address, code.getParamMode(1))
        assertEquals(ParamMode.Address, code.getParamMode(2))
    }

    @Test
    fun `partial codes parsed correctly`() {
        val code = Instruction("1002")
        assertEquals(ParamMode.Address, code.getParamMode(0))
        assertEquals(ParamMode.Immediate, code.getParamMode(1))
        assertEquals(ParamMode.Address, code.getParamMode(2))
    }

    private fun assertAddInstruction(input: String) {
        val code = Instruction(input)
        assertEquals(OpCode.Add, code.opCode)
    }

    private fun assertMultiplyInstruction(input: String) {
        val code = Instruction(input)
        assertEquals(OpCode.Multiply, code.opCode)
    }

    private fun assertHaltInstruction(input: String) {
        val code = Instruction(input)
        assertEquals(OpCode.Halt, code.opCode)
    }
}