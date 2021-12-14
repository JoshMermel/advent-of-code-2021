import java.io.File

class OctoGrid(heights: List<String>) {
    private var energy: MutableList<MutableList<Int>> = heights.map { line ->
        line.toList().map { Character.getNumericValue(it) }.toMutableList()
    }.toMutableList()
    private val numRows = energy.size
    private val numCols = energy[0].size

    private fun increment(minRow: Int, maxRow: Int, minCol: Int, maxCol: Int) {
        for (row in (minRow..maxRow)) {
            for (col in (minCol..maxCol)) {
                if (row in (0 until numRows) && col in (0 until numCols)) {
                    energy[row][col] += 1
                }
            }
        }
    }

    // updates the grid and a set of who flashed
    // returns the delta in how many flashed
    private fun flashOverNine(flashedSoFar: MutableSet<Pair<Int, Int>>): Int {
        var ret = 0
        for (row in energy.indices) {
            for (col in energy[row].indices) {
                if (!flashedSoFar.contains(Pair(row, col)) && energy[row][col] > 9) {
                    ret++
                    flashedSoFar.add(Pair(row, col))
                    increment(row - 1, row + 1, col - 1, col + 1)
                }
            }
        }
        return ret
    }

    private fun cleanup() {
        for (row in energy.indices) {
            for (col in energy[row].indices) {
                if (energy[row][col] > 9) {
                    energy[row][col] = 0
                }
            }
        }

    }

    fun step(): Int {
        var ret = 0
        increment(0, numRows - 1, 0, numCols - 1)
        var flashedSoFar = mutableSetOf<Pair<Int, Int>>()
        var numFlashed = flashOverNine(flashedSoFar)
        while (numFlashed != 0) {
            ret += numFlashed
            numFlashed = flashOverNine(flashedSoFar)
        }
        cleanup()
        return ret
    }

    fun isAllZero(): Boolean = energy.all { row -> row.all { it == 0 } }

    override fun toString(): String {
        return energy.joinToString("\n") { it.joinToString(" ") }
    }
}

fun part1() {
    val input = OctoGrid(File("input.txt").readLines())
    var flashes = 0
    repeat(100) {
        flashes += input.step()
    }
    println("$flashes")
}

fun part2() {
    val input = OctoGrid(File("input.txt").readLines())
    var steps = 0
    while (!input.isAllZero()) {
        input.step()
        steps += 1
    }
    println("$steps")
}

fun main() {
    part1()
    part2()
}

