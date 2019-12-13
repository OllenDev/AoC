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
    println("Distance = ${orbitMapper.getDistance("YOU", "SAN")}")
}

class OrbitMapper(
    private val orbits: List<Orbit>
) {

    private val orbitHashMap = hashMapOf<String, OrbitNode>()

    fun buildMap() {
        orbits.forEach { orbit ->
            val parent = getOrbitNode(orbit.parent)
            val orbiter = getOrbitNode(orbit.orbiter)
            orbiter.parent = parent
            parent.orbiters.add(orbiter)
        }
    }

    fun calculateTotalOrbits(): Int {
        val com = orbitHashMap["COM"] ?: throw IllegalStateException("Couldn't find COM")
        return orbits(com, 0)
    }

    fun getDistance(name1: String, name2: String): Int {
        val node1 = getOrbitNode(name1)
        val node2 = getOrbitNode(name2)
        val path1 = getOrbitPath(node1)
        val path2 = getOrbitPath(node2)
        var count = 0
        var commonNode: OrbitNode? = null
        for (node in path1) {
            count++
            if (path2.contains(node)) {
                commonNode = node
                break
            }
        }
        for (node in path2) {
            if (node == commonNode) {
                break
            }
            count++
        }
        return count - 3 // Subtract 3 for the 2 nodes and the target node
    }

    private fun getOrbitPath(node: OrbitNode): List<OrbitNode> {
        val path = mutableListOf(node)
        var parent: OrbitNode? = node.parent
        while (parent != null) {
            path.add(parent)
            parent = parent.parent
        }
        return path
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
    var parent: OrbitNode? = null
    val orbiters = mutableListOf<OrbitNode>()
}