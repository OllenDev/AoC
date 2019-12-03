package ollendev.aoc

import java.lang.Math.abs

fun main() {
    val wirePaths = FileLoader.getFile("wires.txt").readLines().map { it.toMoveList() }
    val mapper = WireMapper(wirePaths[0], wirePaths[1])
    mapper.mapWires()
    mapper.printWireMap()
    val distance = mapper.findClosestIntersectionDistance()
    println("Closest Intersection is $distance")
}

@Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")
class WireMapper(
    private val wire1Path: List<Move>,
    private val wire2Path: List<Move>
) {

    private val intersectionMarker = '+'
    private val w1Marker = 'a'
    private val w2Marker = 'b'

    private val wireMap = mutableMapOf<Int, MutableMap<Int, Char>>()
    private val intersections = mutableSetOf<Pair<Int, Int>>()

    fun mapWires()  {
        mapWire(wire1Path, w1Marker, w2Marker)
        mapWire(wire2Path, w2Marker, w1Marker)
    }

    private fun mapWire(path: List<Move>, currentMarker: Char, otherMarker: Char) {
        var x = 0
        var y = 0
        for (move in path) {
            val (newX, newY) = when (move.direction) {
                Direction.LEFT -> moveLeft(x, y, move.distance, currentMarker, otherMarker)
                Direction.UP -> moveUp(x, y, move.distance, currentMarker, otherMarker)
                Direction.RIGHT -> moveRight(x, y, move.distance, currentMarker, otherMarker)
                Direction.DOWN -> moveDown(x, y, move.distance, currentMarker, otherMarker)
            }
            x = newX
            y = newY
        }
    }

    private fun moveLeft(x: Int, y: Int, distance: Int, currentMarker: Char, otherMarker: Char): Pair<Int, Int> {
        val row = wireMap[y] ?: mutableMapOf()
        for (i in 1..distance) {
            val pos = x-i
            updateRow(row, pos, pos, y, currentMarker, otherMarker)
        }
        wireMap[y] = row
        return (x-distance to y)
    }

    private fun moveUp(x: Int,y: Int, distance: Int, currentMarker: Char, otherMarker: Char): Pair<Int, Int> {
        for (i in 1..distance) {
            val pos = y+i
            val row = wireMap[pos] ?: mutableMapOf()
            updateRow(row, x, x, pos, currentMarker, otherMarker)
            wireMap[pos] = row
        }
        return (x to y+distance)
    }

    private fun moveRight(x: Int,y: Int, distance: Int,  currentMarker: Char, otherMarker: Char): Pair<Int, Int> {
        val row = wireMap[y] ?: mutableMapOf()
        for (i in 1..distance) {
            val pos = x+i
            updateRow(row, pos, pos, y, currentMarker, otherMarker)
        }
        wireMap[y] = row
        return (x+distance to y)
    }

    private fun moveDown(x: Int,y: Int, distance: Int,  currentMarker: Char, otherMarker: Char): Pair<Int, Int> {
        for (i in 1..distance) {
            val pos = y-i
            val row = wireMap[pos] ?: mutableMapOf()
            updateRow(row, x, x, pos, currentMarker, otherMarker)
            wireMap[pos] = row
        }
        return (x to y-distance)
    }

    private fun updateRow(row: MutableMap<Int, Char>, pos: Int, x: Int, y: Int, currentMarker: Char, otherMarker: Char) {
        when (row[pos]) {
            intersectionMarker -> {
                // do nothing
            }
            otherMarker -> {
                // we found a match mark as so
                row[pos] = intersectionMarker
                intersections.add(x to y)
            }
            else -> {
                row[pos] = currentMarker
            }
        }
    }

    fun findClosestIntersectionDistance(): Int = intersections.map { abs(it.first) + abs(it.second) }.min() ?: 0

    fun printWireMap() {
        val heightMin = wireMap.keys.min() ?: 0
        val heightMax = wireMap.keys.max() ?: 0
        val height = abs(heightMin) + abs(heightMax)
        val widthMin = wireMap.map { it.value.keys.min() ?: 0 }.min() ?: 0
        val widthMax = wireMap.map { it.value.keys.max() ?: 0 }.max() ?: 0
        val width = abs(widthMin) + abs(widthMax)

        val line = List(width + 4) { '-' }.joinToString("")
        println(line)
        for (i in 0..height) {
            val yPos = heightMin + i
            val row = Array(width+1) { '.' }
            for (j in 0..width) {
                val xPos = widthMin + j
                if (xPos == 0 && yPos == 0) {
                    row[j] = 'O'
                } else {
                    row[j] = wireMap[yPos]?.get(xPos) ?: '.'
                }
            }
            println("| ${row.joinToString("")} |")
        }
        println(line)
    }
}

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