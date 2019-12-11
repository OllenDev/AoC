package ollendev.aoc

import java.lang.IllegalStateException

fun main() {
    val orbits = mutableListOf<Orbit>()
    FileLoader.getFile("OrbitMap.txt").forEachLine { line ->
        orbits.add(line.toOrbit())
    }
    val orbitMapper = OrbitMapper(orbits)
    orbitMapper.buildMap()
    val totalOrbits = orbitMapper.calculateTotalOrbits()
    println("Total Orbits = $totalOrbits")
}

class OrbitMapper(
    private val orbits: List<Orbit>
) {

    private val orbitHashMap = hashMapOf<String, OrbitNode>()

    fun buildMap() {
        orbits.forEach { orbit ->
            val parent = getOrbitNode(orbit.parent)
            val orbiter = getOrbitNode(orbit.orbiter)
            parent.orbiters.add(orbiter)
        }
    }

    fun calculateTotalOrbits(): Int {
        val com = orbitHashMap["COM"] ?: throw IllegalStateException("Couldn't find COM")
        return orbits(com, 0)
    }

    private fun orbits(node: OrbitNode, numParents: Int): Int {
        return if (node.orbiters.size < 1) {
            numParents
        } else {
            var sum = 0
            node.orbiters.forEach {
                sum += orbits(it, numParents + 1)
            }
            numParents + sum
        }
    }

    private fun getOrbitNode(name: String): OrbitNode {
        var node = orbitHashMap[name]
        if (node == null) {
            val newOrbitNode = OrbitNode(name)
            orbitHashMap[name] = newOrbitNode
            node = newOrbitNode
        }
        return node
    }
}

data class Orbit(val parent: String, val orbiter: String)

fun String.toOrbit() = this.split(')').let { Orbit(it[0], it[1]) }

data class OrbitNode(val name: String) {
    val orbiters = mutableListOf<OrbitNode>()
}