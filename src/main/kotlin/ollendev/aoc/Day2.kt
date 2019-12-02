package ollendev.aoc

import java.io.File

fun main() {
    val ram = initRam()

    // replace position 1 with the value 12
    ram[1] = 12
    // replace position 2 with the value 2
    ram[2] = 2

    val computer = Computer(ram)
    computer.run()

    println("Ship Computer Calculation ${ram[0]}")
}

fun initRam(): MutableList<Int> {
    val ram = mutableListOf<Int>()

    FileLoader.getFile("ram.txt").readLines().forEach { line ->
        line.split(",").forEach { s ->
            ram.add(s.toInt())
        }
    }

    return ram
}

class Computer(
    private val ram: MutableList<Int>
) {

    fun run() {
        if (ram.size < 4) return

        var opCodePos = 0
        var opCode = getOpcode(opCodePos)

        runloop@ while (opCode.instruction != Instruction.Halt) {
            when (opCode.instruction) {
                Instruction.Add -> add(opCode)
                Instruction.Multiply -> multiply(opCode)
                Instruction.Halt -> break@runloop
            }
            opCodePos += 4
            opCode = getOpcode(opCodePos)
        }
    }

    private fun add(opCode: OpCode) {
        println("Adding $opCode")
        ram[opCode.saveAddress] = ram[opCode.leftAddress] + ram[opCode.rightAddress]
    }

    private fun multiply(opCode: OpCode) {
        println("Multiplying $opCode")
        ram[opCode.saveAddress] = ram[opCode.leftAddress] * ram[opCode.rightAddress]
    }

    private fun getOpcode(index: Int): OpCode {
        return OpCode(
            ram[index].toInstruction(),
            ram.getOrNull(index + 1) ?: 0,
            ram.getOrNull(index + 2) ?: 0,
            ram.getOrNull(index+3) ?: 0
        )
    }
}

enum class Instruction {
    Add,
    Multiply,
    Halt
}

fun Int?.toInstruction() = when (this) {
    1 -> Instruction.Add
    2 -> Instruction.Multiply
    99 -> Instruction.Halt
    else -> throw IllegalStateException("Unknown op code instruction!")
}

data class OpCode(
    val instruction: Instruction,
    val leftAddress: Int,
    val rightAddress: Int,
    val saveAddress: Int
)
