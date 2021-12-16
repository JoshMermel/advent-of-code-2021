import java.io.File
import java.util.*

data class Cell (val row : Int, val col : Int, val danger : Int)

// Returns a 2D array showing the safest cost to reach each cell.
fun safestPath(grid: List<List<Int>>): Int {
    val numRows = grid.size
    val numCols = grid[0].size

    val Q = PriorityQueue<Cell>(compareBy(Cell::danger))
    val dist = MutableList(numRows) { MutableList(numCols) { Int.MAX_VALUE } }
    dist[0][0] = 0
    Q.add(Cell(0,0,0))

    while (Q.isNotEmpty()) {
        val u = Q.remove()
        for (neighbor in listOf(Pair(1,0), Pair(-1,0), Pair(0,1), Pair(0,-1))) {
          val newRow = u.row + neighbor.first
          val newCol = u.col + neighbor.second
          if (newRow in (0 until numRows) && newCol in (0 until numCols)) {
            val oldVal = dist[newRow][newCol]
            val maybeReplacement = u.danger + grid[newRow][newCol]
            if (maybeReplacement < oldVal) {
              dist[newRow][newCol] = maybeReplacement
              Q.add(Cell(newRow, newCol, maybeReplacement))
              if (newRow == numRows-1 && newCol == numCols -1) {
                return maybeReplacement
              }
            }
          }
        }
    }

  return -1
}

fun buildPartTwoInput(grid : List<List<Int>>) : List<List<Int>> {
    val numRows = grid.size
    val numCols = grid[0].size
    return List(numRows * 5) { row ->
      List(numCols * 5) { col ->
        1 + (grid[row % numRows][col % numCols] + (row / numRows) + (col / numCols) -1) % 9
      }
    }
}

fun main() {
    val input = File("input.txt").readLines().map { line ->
        line.toList().map { it.code - '0'.code }
    }

    val part1 = safestPath(input)
    println("$part1")

    val part2 = safestPath(buildPartTwoInput(input))
    println("$part2")
}



