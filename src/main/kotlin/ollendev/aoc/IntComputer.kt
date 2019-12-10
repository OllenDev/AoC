package ollendev.aoc

fun main() {
//    fixComputer()
//    findPossibleCombinations()
    runTESTDiagnostic()
}

// Day 5 Part 1 -  Thermal Environment Supervision Terminal (TEST)
fun runTESTDiagnostic() {
    val ram = initRam("TESTDiagnosticProgram.txt")
    val computer = IntComputer(ram, 1)
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

    fun run() {
        if (ram.size < 4) return

        var programPointer = 0
        var code = getCode(programPointer)

        runloop@ while (code.instruction != Instruction.Halt) {
            when (code.instruction) {
                Instruction.Add -> add(programPointer, code)
                Instruction.Multiply -> multiply(programPointer, code)
                Instruction.WriteInput -> writeInput(programPointer, code)
                Instruction.PrintOutput -> printOutput(programPointer, code)
                Instruction.Halt -> break@runloop
            }
            programPointer += code.instruction.size
            code = getCode(programPointer)
        }
    }

    private fun add(pointer: Int, code: Code) {
        val saveAddress = ram[pointer + 3]
        val left = if (code.getParamMode(0) == ParamMode.Immediate) {
            ram[pointer + 1]
        } else {
            ram[ram[pointer + 1]]
        }
        val right = if (code.getParamMode(1) == ParamMode.Immediate) {
            ram[pointer + 2]
        } else {
            ram[ram[pointer +2]]
        }
        ram[saveAddress] = left + right
    }

    private fun multiply(pointer: Int, code: Code) {
        val saveAddress = ram[pointer + 3]
        val left = if (code.getParamMode(0) == ParamMode.Immediate) {
            ram[pointer + 1]
        } else {
            ram[ram[pointer + 1]]
        }
        val right = if (code.getParamMode(1) == ParamMode.Immediate) {
            ram[pointer + 2]
        } else {
            ram[ram[pointer +2]]
        }
        ram[saveAddress] = left * right
    }

    private fun writeInput(pointer: Int, code: Code) {
        val saveAddress = ram[pointer + 1]
        ram[saveAddress] = input
    }

    private fun printOutput(pointer: Int, code: Code) {
        val output = if (code.getParamMode(0) == ParamMode.Immediate) {
            ram[pointer + 1]
        } else {
            ram[ram[pointer + 1]]
        }
        println("$output")
    }

    private fun getCode(index: Int): Code {
        return Code(ram[index].toString())
    }
}

enum class Instruction(val size: Int) {
    Add(4),
    Multiply(4),
    WriteInput(2),
    PrintOutput(2),
    Halt(1)
}

fun Int?.toInstruction() = when (this) {
    1 -> Instruction.Add
    2 -> Instruction.Multiply
    3 -> Instruction.WriteInput
    4 -> Instruction.PrintOutput
    99 -> Instruction.Halt
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
