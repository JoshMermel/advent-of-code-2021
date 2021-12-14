import java.io.File

fun Int?.nullOrGT(other: Int) = this == null || this > other

class HeightMap(private val heights: List<List<Int>>) {
    private val numRows = heights.size
    private val numCols = heights[0].size
    private var basinId: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    init {
        var id = 0
        for (row in heights.indices) {
            for (col in heights[row].indices) {
                maybeAddToBasin(row, col, id)
                id++
            }
        }
    }

    private fun isLowPoint(row: Int, col: Int): Boolean {
        val height = heights[row][col]
        return heights.getOrNull(row + 1)?.getOrNull(col).nullOrGT(height) &&
                heights.getOrNull(row - 1)?.getOrNull(col).nullOrGT(height) &&
                heights.getOrNull(row)?.getOrNull(col + 1).nullOrGT(height) &&
                heights.getOrNull(row)?.getOrNull(col - 1).nullOrGT(height)
    }

    fun lowPointSum(): Int {
        var sum = 0
        for (row in heights.indices) {
            for (col in heights[row].indices) {
                if (isLowPoint(row, col)) {
                    sum += (heights[row][col] + 1)
                }
            }
        }
        return sum
    }

    // flood fills coordinates with |id| but is stopped by the edge of the board
    // or by height 9.
    private fun maybeAddToBasin(row: Int, col: Int, id: Int) {
        if (basinId.containsKey(Pair(row, col))) {
            return
        }
        if (row !in (0 until numRows) || col !in (0 until numCols)) {
            return
        }
        if (heights[row][col] == 9) {
            return
        }
        basinId[Pair(row, col)] = id
        maybeAddToBasin(row + 1, col, id)
        maybeAddToBasin(row - 1, col, id)
        maybeAddToBasin(row, col + 1, id)
        maybeAddToBasin(row, col - 1, id)
    }

    fun basinSizes(): Collection<Int> =
        basinId.values.groupingBy { it }.eachCount().values
}

fun main() {
    val input = File("input.txt").readLines().map { line ->
        line.toList().map { Character.getNumericValue(it) }
    }
    val heightmap = HeightMap(input)
    println("${heightmap.lowPointSum()}")

    val biggestBasins = heightmap.basinSizes().sorted()
    println("${biggestBasins.takeLast(3).reduce(Int::times)}")
}
