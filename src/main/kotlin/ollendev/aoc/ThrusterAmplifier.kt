package ollendev.aoc

fun main() {
    val generator = PermutationGenerator(mutableListOf(0,1,2,3,4))
    val permutations = generator.generate()
    val solutions = mutableMapOf<Int, List<Int>>()
    for (permutation in permutations) {
        println("Input = $permutation")

        val ramA = initRam("AmplifierController.txt")
        val ampA = IntComputer(ramA, mutableListOf(permutation[0], 0))
        ampA.run()
        println("A output: ${ampA.input.last()}")

        val ramB = initRam("AmplifierController.txt")
        val ampB = IntComputer(ramB, mutableListOf(permutation[1], ampA.input.last()))
        ampB.run()
        println("B output: ${ampB.input.last()}")

        val ramC = initRam("AmplifierController.txt")
        val ampC = IntComputer(ramC, mutableListOf(permutation[2], ampB.input.last()))
        ampC.run()
        println("C output: ${ampC.input.last()}")

        val ramD = initRam("AmplifierController.txt")
        val ampD = IntComputer(ramD, mutableListOf(permutation[3], ampC.input.last()))
        ampD.run()
        println("D output: ${ampD.input.last()}")

        val ramE = initRam("AmplifierController.txt")
        val ampE = IntComputer(ramE, mutableListOf(permutation[4], ampD.input.last()))
        ampE.run()
        println("E output: ${ampE.input.last()}")

        solutions[ampE.input.last()] = permutation
    }
    val bestSolution = solutions.maxBy { it.key }
    println("Max value ${bestSolution?.key} for inputs ${bestSolution?.value}")
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