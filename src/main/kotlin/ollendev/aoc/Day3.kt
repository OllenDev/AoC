package ollendev.aoc

import java.lang.Math.abs

fun main() {
    val wirePaths = FileLoader.getFile("wires.txt").readLines().map { it.toMoveList() }
    val mapper = WireMapper(wirePaths[0], wirePaths[1])
    mapper.mapWires()
    val distance = mapper.findClosestIntersectionDistance()
    println("Closest intersection by manhattan distance $distance")
    val steps = mapper.findClosestSteps()
    println("Closest intersection by number of steps is $steps")
}

@Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")
class WireMapper(
    private val wire1Path: List<Move>,
    private val wire2Path: List<Move>
) {

    private val intersectionMarker = '+'
    private val w1Marker = 'a'
    private val w2Marker = 'b'

    // y then x
    private val wireMap = mutableMapOf<Int, MutableMap<Int, Node>>()
    private val intersections = mutableSetOf<Pair<Int, Int>>()

    fun mapWires()  {
        mapWire(wire1Path, w1Marker, w2Marker)
        mapWire(wire2Path, w2Marker, w1Marker)
    }

    private fun mapWire(path: List<Move>, currentMarker: Char, otherMarker: Char) {
        var x = 0
        var y = 0
        var totalDistance = 0
        for (move in path) {
            val (newX, newY) = when (move.direction) {
                Direction.LEFT -> moveLeft(x, y, move.distance, totalDistance, currentMarker, otherMarker)
                Direction.UP -> moveUp(x, y, move.distance, totalDistance, currentMarker, otherMarker)
                Direction.RIGHT -> moveRight(x, y, move.distance, totalDistance, currentMarker, otherMarker)
                Direction.DOWN -> moveDown(x, y, move.distance, totalDistance, currentMarker, otherMarker)
            }
            totalDistance += move.distance
            x = newX
            y = newY
        }
    }

    private fun moveLeft(x: Int, y: Int, distance: Int, currentTotalDistance: Int, currentMarker: Char, otherMarker: Char): Pair<Int, Int> {
        val row = wireMap[y] ?: mutableMapOf()
        for (i in 1..distance) {
            val xPos = x-i
            updateRow(row, xPos, y, currentTotalDistance+i, currentMarker, otherMarker)
        }
        wireMap[y] = row
        return (x-distance to y)
    }

    private fun moveUp(x: Int,y: Int, distance: Int, currentTotalDistance: Int, currentMarker: Char, otherMarker: Char): Pair<Int, Int> {
        for (i in 1..distance) {
            val yPos = y+i
            val row = wireMap[yPos] ?: mutableMapOf()
            updateRow(row, x, yPos, currentTotalDistance+i, currentMarker, otherMarker)
            wireMap[yPos] = row
        }
        return (x to y+distance)
    }

    private fun moveRight(x: Int, y: Int, distance: Int, currentTotalDistance: Int,  currentMarker: Char, otherMarker: Char): Pair<Int, Int> {
        val row = wireMap[y] ?: mutableMapOf()
        for (i in 1..distance) {
            val xPos = x+i
            updateRow(row, xPos, y, currentTotalDistance+i, currentMarker, otherMarker)
        }
        wireMap[y] = row
        return (x+distance to y)
    }

    private fun moveDown(x: Int,y: Int, distance: Int, currentTotalDistance: Int,  currentMarker: Char, otherMarker: Char): Pair<Int, Int> {
        for (i in 1..distance) {
            val yPos = y-i
            val row = wireMap[yPos] ?: mutableMapOf()
            updateRow(row, x, yPos, currentTotalDistance+i, currentMarker, otherMarker)
            wireMap[yPos] = row
        }
        return (x to y-distance)
    }

    private fun updateRow(row: MutableMap<Int, Node>, x: Int, y: Int, totalDistance: Int, currentMarker: Char, otherMarker: Char) {
        when (row[x]?.symbol) {
            intersectionMarker -> {
                // do nothing
            }
            otherMarker -> {
                // we found a match mark as so
                val firstWire = row[x] as Wire
                row[x] = Intersection(mutableMapOf(
                    firstWire.wireSymbol to firstWire.steps,
                    currentMarker to totalDistance
                ))
                intersections.add(x to y)
            }
            else -> {
                row[x] = Wire(currentMarker, totalDistance)
            }
        }
    }

    fun findClosestIntersectionDistance(): Int = intersections.map { abs(it.first) + abs(it.second) }.min() ?: 0

    fun findClosestSteps(): Int {
        return intersections.map {
            val intersection = wireMap[it.second]?.get(it.first) as Intersection
            intersection.steps['a']!! + intersection.steps['b']!!
        }.min()!!
    }
}

sealed class Node(val symbol: Char)
data class Wire(val wireSymbol: Char, val steps: Int) : Node(wireSymbol)
data class Intersection(val steps: Map<Char, Int>) : Node('+')

data class Move(
    val direction: Direction,
    val distance: Int
)

enum class Direction {
    LEFT,
    UP,
    RIGHT,
    DOWN
}

fun Char.toDirection(): Direction =
    when (this) {
        'L' -> Direction.LEFT
        'U' -> Direction.UP
        'R' -> Direction.RIGHT
        else -> Direction.DOWN
    }

fun String.toMoveList(): List<Move> = this.split(',')
    .map { Move(it[0].toDirection(), it.substring(1 until it.length).toInt()) }