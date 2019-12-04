package ollendev.aoc

import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

internal class Day4Test {

    @Test
    fun `get digits 123456`() {
        assertEquals(listOf(1,2,3,4,5,6), getDigits(123456))
    }

    @Test
    fun `123456 does not meet criteria`() {
        assertFalse(meetsCriteria(listOf(1,2,3,4,5,6)))
    }

    @Test
    fun `311111 does not meet criteria`() {
        assertFalse(meetsCriteria(listOf(3,1,1,1,1,1)))
    }

    @Test
    fun `123454 does not meet criteria`() {
        assertFalse(meetsCriteria(listOf(1,2,3,4,5,4)))
    }

    @Test
    fun `123444 does not meet criteria`() {
        assertFalse(meetsCriteria(listOf(1, 2, 3, 4, 4, 4)))
    }

    @Test
    fun `111111 does not meet criteria`() {
        assertFalse(meetsCriteria(listOf(1,1,1,1,1,1)))
    }

    @Test
    fun `111122 meets criteria`() {
        assertTrue(meetsCriteria(listOf(1,1,1,1,2,2)))
    }

    @Test
    fun `112233 meets criteria`() {
        assertTrue(meetsCriteria(listOf(1,1,2,2,3,3)))
    }

    @Test
    fun `122345 meets criteria`() {
        assertTrue(meetsCriteria(listOf(1,2,2,3,4,5)))
    }
}