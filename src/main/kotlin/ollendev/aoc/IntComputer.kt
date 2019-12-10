package ollendev.aoc

fun main() {
//    fixComputer()
//    findPossibleCombinations()
//    runTESTDiagnostic()
    runRadiatorTestDiagnostic()
}

// Day 5 Part 1 -  Thermal Environment Supervision Terminal (TEST)
fun runTESTDiagnostic() {
    val ram = initRam("TESTDiagnosticProgram.txt")
    val computer = IntComputer(ram, 1)
    computer.run()
}

// Day 5 Part 2 -  Thermal Radiator
fun runRadiatorTestDiagnostic() {
    val ram = initRam("TESTDiagnosticProgram.txt")
    val computer = IntComputer(ram, 5)
    computer.run()
}

// Day 1 Part 1 "1202 error"
fun fixComputer() {
    val result = day1Driver(12, 2)
    print("$result")
}

// Day 2 Part 2
fun findPossibleCombinations() {
    val target = 19690720
    var found = false

    for (n in 0..99) {
        for (v in 0..99) {
            val result = day1Driver(n, v)
            if (result == target) {
                println("Found combination noun = $n verb = $v\n100 * $n + $v = ${100 * n + v}")
                found = true
                break
            }
        }
    }
    println("Found combination = $found")
}

fun day1Driver(noun: Int, verb: Int): Int {
    val ram = initRam("ram.txt")

    // replace position 1 with the value 12
    ram[1] = noun
    // replace position 2 with the value 2
    ram[2] = verb

    val computer = IntComputer(ram, 1)
    computer.run()

    return ram[0]
}

fun initRam(programFile: String): MutableList<Int> {
    val ram = mutableListOf<Int>()

    FileLoader.getFile("$programFile").readLines().forEach { line ->
        line.split(",").forEach { s ->
            ram.add(s.toInt())
        }
    }

    return ram
}

class IntComputer(
    private val ram: MutableList<Int>,
    private val input: Int
) {

    private var programPointer = 0

    fun run() {
        if (ram.size < 4) return

        var code = getCode(programPointer)

        runloop@ while (code.instruction != Instruction.Halt) {
            var jumpPointer: Int? = null
            when (code.instruction) {
                Instruction.Add -> add(code)
                Instruction.Multiply -> multiply(code)
                Instruction.WriteInput -> writeInput()
                Instruction.PrintOutput -> printOutput(code)
                Instruction.JumpIfTrue -> jumpPointer = jumpIfTrue(code)
                Instruction.JumpIfFalse -> jumpPointer = jumpIfFalse(code)
                Instruction.LessThan -> lessThan(code)
                Instruction.Equals -> equals(code)
                Instruction.Halt -> break@runloop
            }
            if (jumpPointer != null) {
                programPointer = jumpPointer
            } else {
                programPointer += code.instruction.size
            }
            code = getCode(programPointer)
        }
    }

    private fun add(code: Code) {
        val left = readParamValue(code, 0)
        val right = readParamValue(code, 1)
        val saveAddress = ram[programPointer + 3]
        ram[saveAddress] = left + right
    }

    private fun multiply(code: Code) {
        val left = readParamValue(code, 0)
        val right = readParamValue(code, 1)
        val saveAddress = ram[programPointer + 3]
        ram[saveAddress] = left * right
    }

    private fun writeInput() {
        val saveAddress = ram[programPointer + 1]
        ram[saveAddress] = input
    }

    private fun printOutput(code: Code) {
        val output = readParamValue(code, 0)
        println("$output")
    }

    // if the first parameter is non-zero, it sets the instruction pointer to the value from the second parameter.
    // Otherwise, it does nothing.
    private fun jumpIfTrue(code: Code): Int? {
        val value = readParamValue(code, 0)
        if (value != 0) {
            return readParamValue(code, 1)
        }
        return null
    }

    // if the first parameter is zero, it sets the instruction pointer to the value from the second parameter.
    // Otherwise, it does nothing
    private fun jumpIfFalse(code: Code): Int? {
        val value = readParamValue(code, 0)
        if (value == 0) {
            return readParamValue(code, 1)
        }
        return null
    }

    // if the first parameter is less than the second parameter, it stores 1 in the position given by the third
    // parameter. Otherwise, it stores 0.
    private fun lessThan(code: Code) {
        val saveAddress = ram[programPointer + 3]
        val left = readParamValue(code, 0)
        val right = readParamValue(code, 1)
        ram[saveAddress] = if (left < right) { 1 } else { 0 }
    }

    // if the first parameter is equal to the second parameter, it stores 1 in the position given by the third
    // parameter. Otherwise, it stores 0.
    private fun equals(code: Code) {
        val saveAddress = ram[programPointer + 3]
        val left = readParamValue(code, 0)
        val right = readParamValue(code, 1)
        ram[saveAddress] = if (left == right) { 1 } else { 0 }
    }

    private fun readParamValue(code: Code, paramIndex: Int): Int {
        return if (code.getParamMode(paramIndex) == ParamMode.Immediate) {
            ram[programPointer + paramIndex + 1]
        } else {
            ram[ram[programPointer + paramIndex + 1]]
        }
    }

    private fun getCode(index: Int): Code {
        return Code(ram[index].toString())
    }
}

enum class Instruction(val value: Int, val size: Int) {
    Add(1, 4),
    Multiply(2, 4),
    WriteInput(3, 2),
    PrintOutput(4, 2),
    JumpIfTrue(5, 3),
    JumpIfFalse(6, 3),
    LessThan(7, 4),
    Equals(8, 4),
    Halt(99, 1)
}

fun Int?.toInstruction() = when (this) {
    Instruction.Add.value -> Instruction.Add
    Instruction.Multiply.value -> Instruction.Multiply
    Instruction.WriteInput.value -> Instruction.WriteInput
    Instruction.PrintOutput.value -> Instruction.PrintOutput
    Instruction.JumpIfTrue.value -> Instruction.JumpIfTrue
    Instruction.JumpIfFalse.value -> Instruction.JumpIfFalse
    Instruction.LessThan.value -> Instruction.LessThan
    Instruction.Equals.value -> Instruction.Equals
    Instruction.Halt.value -> Instruction.Halt
    else -> throw IllegalStateException("Unknown op code instruction!")
}

enum class ParamMode {
    Address,
    Immediate
}

data class Code(val value: String) {
    val instruction: Instruction
    private val paramModes: List<ParamMode>

    init {
        instruction = parseInstruction()
        paramModes = parseParamModes()
    }

    fun getParamMode(index: Int): ParamMode {
        return paramModes.getOrElse(index) { ParamMode.Address }
    }

    private fun parseInstruction(): Instruction {
        return if (value.length == 1) {
            value.toInt().toInstruction()
        } else {
            value.substring(value.length-2).toInt().toInstruction()
        }
    }

    private fun parseParamModes(): List<ParamMode> {
        return if (value.length <= 2) {
            listOf()
        } else {
            val end = value.length-3
            value.substring(0..end).reversed().map {
                if (it == '1') {
                    ParamMode.Immediate
                } else {
                    ParamMode.Address
                }
            }
        }
    }
}
