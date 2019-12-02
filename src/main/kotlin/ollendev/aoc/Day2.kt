package ollendev.aoc

import java.io.File

fun main() {
    // Day 1 Part 1 "1202 error"
    //val result = driver(12, 2)

    val target = 19690720
    var found = false

    for (n in 0..99) {
        for (v in 0..99) {
            val result = driver(n, v)
            if (result == target) {
                println("Found combination noun = $n verb = $v\n100 * $n + $v = ${100 * n + v}")
                found = true
                break
            }
        }
    }
    println("Found combination = $found")
}

fun driver(noun: Int, verb: Int): Int {
    val ram = initRam()

    // replace position 1 with the value 12
    ram[1] = noun
    // replace position 2 with the value 2
    ram[2] = verb

    val computer = Computer(ram)
    computer.run()

    return ram[0]
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
        ram[opCode.saveAddress] = ram[opCode.leftAddress] + ram[opCode.rightAddress]
    }

    private fun multiply(opCode: OpCode) {
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
