package ollendev.aoc

import org.junit.Assert.assertEquals
import org.junit.Test

internal class Day3Test {

    @Test
    fun `input 1 closest manhattan distance`() {
        val wire1Path = "R8,U5,L5,D3".toMoveList()
        val wire2Path = "U7,R6,D4,L4".toMoveList()
        val wireMapper = WireMapper(wire1Path, wire2Path)

        wireMapper.mapWires()
        val distance = wireMapper.findClosestIntersectionDistance()

        assertEquals(6, distance)
    }

    @Test
    fun `input 2 closest manhattan distance`() {
        val wire1Path = "R75,D30,R83,U83,L12,D49,R71,U7,L72".toMoveList()
        val wire2Path = "U62,R66,U55,R34,D71,R55,D58,R83".toMoveList()
        val wireMapper = WireMapper(wire1Path, wire2Path)

        wireMapper.mapWires()
        val distance = wireMapper.findClosestIntersectionDistance()

        assertEquals(159, distance)
    }

    @Test
    fun `input 3 closest manhattan distance`() {
        val wire1Path = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51".toMoveList()
        val wire2Path = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7".toMoveList()
        val wireMapper = WireMapper(wire1Path, wire2Path)

        wireMapper.mapWires()
        val distance = wireMapper.findClosestIntersectionDistance()

        assertEquals(135, distance)
    }

    @Test
    fun `input 1 closest steps`() {
        val wire1Path = "R8,U5,L5,D3".toMoveList()
        val wire2Path = "U7,R6,D4,L4".toMoveList()
        val wireMapper = WireMapper(wire1Path, wire2Path)

        wireMapper.mapWires()
        val steps = wireMapper.findClosestSteps()

        assertEquals(30, steps)
    }

    @Test
    fun `input 2 closest steps`() {
        val wire1Path = "R75,D30,R83,U83,L12,D49,R71,U7,L72".toMoveList()
        val wire2Path = "U62,R66,U55,R34,D71,R55,D58,R83".toMoveList()
        val wireMapper = WireMapper(wire1Path, wire2Path)

        wireMapper.mapWires()
        val steps = wireMapper.findClosestSteps()

        assertEquals(610, steps)
    }

    @Test
    fun `input 3 closest steps`() {
        val wire1Path = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51".toMoveList()
        val wire2Path = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7".toMoveList()
        val wireMapper = WireMapper(wire1Path, wire2Path)

        wireMapper.mapWires()
        val steps = wireMapper.findClosestSteps()

        assertEquals(410, steps)
    }
}