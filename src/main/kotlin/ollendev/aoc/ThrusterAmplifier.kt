package ollendev.aoc

import java.lang.IllegalStateException

fun main() {
    val generator = PermutationGenerator(mutableListOf(5,6,7,8,9))
    val permutations = generator.generate()
    val solutions = mutableMapOf<Int, List<Int>>()

    for (permutation in permutations) {
        println("Input = $permutation")
        val computers = initComputers()
        var input = 0
        var returnCode: ReturnCode? = null
        var computerIndex = 0
        var computer = computers[computerIndex]
        var modeSetCount = 0
        while (returnCode != Halt) {
            when(returnCode) {
                null -> returnCode = computer.run()
                is Output -> {
                    input = returnCode.value
                    computerIndex = if (computerIndex < (computers.size - 1)) { computerIndex + 1 } else { 0 }
                    computer = computers[computerIndex]
                    returnCode = computer.run()
                }
                is RequestInput -> {
                    if (modeSetCount < computers.size) {
                        computer.runWithInput(permutation[computerIndex])
                        modeSetCount += 1
                    }
                    returnCode = computer.runWithInput(input)
                }
                else -> throw IllegalStateException("There was an error $returnCode")
            }
        }
        solutions[input] = permutation
    }
    val bestSolution = solutions.maxBy { it.key }
    println("Max value ${bestSolution?.key} for inputs ${bestSolution?.value}")
}

private fun initComputers(): MutableList<IntComputer> {
    val ramA = initRam("AmplifierController.txt")
    val ramB = initRam("AmplifierController.txt")
    val ramC = initRam("AmplifierController.txt")
    val ramD = initRam("AmplifierController.txt")
    val ramE = initRam("AmplifierController.txt")
    return mutableListOf<IntComputer>(
        IntComputer(ramA),
        IntComputer(ramB),
        IntComputer(ramC),
        IntComputer(ramD),
        IntComputer(ramE)
    )
}

class PermutationGenerator(private val list: MutableList<Int>) {

    private val permutations = mutableListOf<MutableList<Int>>()

    fun generate(): List<MutableList<Int>> {
        permutations.clear()
        permute(0, list.size)
        return permutations
    }

    private fun permute(left: Int, size: Int) {
        if (left == size) {
            permutations.add(list.toMutableList())
        } else {
            for (i in left until size) {
                swap(left, i)
                permute(left + 1, size)
                swap(left, i)
            }
        }
    }

    private fun swap(i: Int, j: Int) {
        val temp = list[i]
        list[i] = list[j]
        list[j] = temp
    }
}