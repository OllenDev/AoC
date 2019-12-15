package ollendev.aoc

fun main() {
    println("Fix Computer\n-------------")
    fixComputer()
    println("\nFind Combination\n-------------")
    findPossibleCombinations()
    println("\nRun TEST Diag\n-------------")
    runTESTDiagnostic()
    println("\nRun Radiator Diag\n-------------")
    runRadiatorTestDiagnostic()
}

// Day 5 Part 1 -  Thermal Environment Supervision Terminal (TEST)
fun runTESTDiagnostic() {
    val ram = initRam("TESTDiagnosticProgram.txt")
    val computer = IntComputer(ram)
    var code = computer.run()
    var input = 1
    while (code != Halt) {
        if (code == RequestInput) {
            code = computer.runWithInput(input)
        } else if (code is Output) {
            input = code.value
            code = computer.run()
        }
    }
    println("Diagnostic code: $input")
}

// Day 5 Part 2 -  Thermal Radiator
fun runRadiatorTestDiagnostic() {
    val ram = initRam("TESTDiagnosticProgram.txt")
    val computer = IntComputer(ram)
    var code = computer.run()
    var input = 5
    while (code != Halt) {
        if (code == RequestInput) {
            code = computer.runWithInput(input)
        } else if (code is Output) {
            input = code.value
            code = computer.run()
        }
    }
    println("Diagnostic code: $input")
}

// Day 1 Part 1 "1202 error"
fun fixComputer() {
    val result = day1Driver(12, 2)
    println("$result")
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

    val computer = IntComputer(ram)
    computer.run()

    return ram[0]
}

fun initRam(programFile: String): MutableList<Int> {
    val ram = mutableListOf<Int>()

    FileLoader.getFile(programFile).readLines().forEach { line ->
        line.split(",").forEach { s ->
            ram.add(s.toInt())
        }
    }

    return ram
}

sealed class ReturnCode
data class Error(val message: String) : ReturnCode()
object RequestInput : ReturnCode()
data class Output(val value: Int) : ReturnCode()
object Halt : ReturnCode()

class IntComputer(
    private val ram: MutableList<Int>,
    private val debug: Boolean = false
) {

    val output = mutableListOf<Int>()

    private var programPointer = 0

    fun runWithInput(input: Int): ReturnCode {
        val saveAddress = ram[programPointer + 1]
        ram[saveAddress] = input
        programPointer += 2
        return run()
    }

    fun run(): ReturnCode {
        var instruction = getInstruction(programPointer)

        runloop@ while (instruction.opCode != OpCode.Halt) {
            var jumpPointer: Int? = null
            when (instruction.opCode) {
                OpCode.Add -> add(instruction)
                OpCode.Multiply -> multiply(instruction)
                OpCode.Input -> return input()
                OpCode.Output -> return output(instruction)
                OpCode.JumpIfTrue -> jumpPointer = jumpIfTrue(instruction)
                OpCode.JumpIfFalse -> jumpPointer = jumpIfFalse(instruction)
                OpCode.LessThan -> lessThan(instruction)
                OpCode.Equals -> isEqual(instruction)
                OpCode.Halt -> break@runloop
            }
            programPointer = jumpPointer ?: programPointer + instruction.opCode.size
            instruction = getInstruction(programPointer)
        }

        return Halt
    }

    private fun log(message: String) {
        if (debug) {
            println(message)
        }
    }

    private fun add(instruction: Instruction) {
        val left = readParamValue(instruction, 0)
        val right = readParamValue(instruction, 1)
        val saveAddress = ram[programPointer + 3]
        ram[saveAddress] = left + right
        log("Add ram[${programPointer + 3}] = $left + $right")
    }

    private fun multiply(instruction: Instruction) {
        val left = readParamValue(instruction, 0)
        val right = readParamValue(instruction, 1)
        val saveAddress = ram[programPointer + 3]
        ram[saveAddress] = left * right
        log("Multiply ram[${programPointer + 3}] = $left * $right")
    }

    private fun input(): RequestInput {
        log("Request Input")
        return RequestInput
    }

    private fun output(instruction: Instruction): Output {
        val value = readParamValue(instruction, 0)
        log("Output $value")
        programPointer += 2
        return Output(value)
    }

    // if the first parameter is non-zero, it sets the instruction pointer to the value from the second parameter.
    // Otherwise, it does nothing.
    private fun jumpIfTrue(instruction: Instruction): Int? {
        val value = readParamValue(instruction, 0)
        val address = readParamValue(instruction, 1)
        log("jumpIfTrue $value != 0 address = $address")
        if (value != 0) {
            return address
        }
        return null
    }

    // if the first parameter is zero, it sets the instruction pointer to the value from the second parameter.
    // Otherwise, it does nothing
    private fun jumpIfFalse(instruction: Instruction): Int? {
        val value = readParamValue(instruction, 0)
        val address = readParamValue(instruction, 1)
        log("jumpIfFalse $value != 0 address = $address")
        if (value == 0) {
            return address
        }
        return null
    }

    // if the first parameter is less than the second parameter, it stores 1 in the position given by the third
    // parameter. Otherwise, it stores 0.
    private fun lessThan(instruction: Instruction) {
        val saveAddress = ram[programPointer + 3]
        val left = readParamValue(instruction, 0)
        val right = readParamValue(instruction, 1)
        log("lessThan ram[$saveAddress] = $left < $right ")
        ram[saveAddress] = if (left < right) { 1 } else { 0 }
    }

    // if the first parameter is equal to the second parameter, it stores 1 in the position given by the third
    // parameter. Otherwise, it stores 0.
    private fun isEqual(instruction: Instruction) {
        val saveAddress = ram[programPointer + 3]
        val left = readParamValue(instruction, 0)
        val right = readParamValue(instruction, 1)
        log("lessThan ram[$saveAddress] = $left == $right ")
        ram[saveAddress] = if (left == right) { 1 } else { 0 }
    }

    private fun readParamValue(instruction: Instruction, paramIndex: Int): Int {
        return if (instruction.getParamMode(paramIndex) == ParamMode.Immediate) {
            ram[programPointer + paramIndex + 1]
        } else {
            ram[ram[programPointer + paramIndex + 1]]
        }
    }

    private fun getInstruction(index: Int): Instruction {
        return Instruction(ram[index].toString())
    }
}

enum class OpCode(val size: Int) {
    Add(4),
    Multiply(4),
    Input(2),
    Output(2),
    JumpIfTrue(3),
    JumpIfFalse(3),
    LessThan(4),
    Equals(4),
    Halt(1)
}

fun Int?.toOpCode() = when (this) {
    1 -> OpCode.Add
    2 -> OpCode.Multiply
    3 -> OpCode.Input
    4 -> OpCode.Output
    5 -> OpCode.JumpIfTrue
    6 -> OpCode.JumpIfFalse
    7 -> OpCode.LessThan
    8 -> OpCode.Equals
    99 -> OpCode.Halt
    else -> throw IllegalStateException("Unknown op code instruction: $this!")
}

enum class ParamMode {
    Address,
    Immediate
}

data class Instruction(val value: String) {
    val opCode: OpCode
    private val paramModes: List<ParamMode>

    init {
        opCode = parseOpCode()
        paramModes = parseParamModes()
    }

    fun getParamMode(index: Int): ParamMode {
        return paramModes.getOrElse(index) { ParamMode.Address }
    }

    private fun parseOpCode(): OpCode {
        return if (value.length == 1) {
            value.toInt().toOpCode()
        } else {
            value.substring(value.length-2).toInt().toOpCode()
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
