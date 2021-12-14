import java.io.File

// Makes a range between |first| and |second| in the correct direction.
fun makeRange(first: Int, second: Int): IntProgression =
    if (first < second) {
        (first..second)
    } else {
        (first downTo second)
    }

class Grid() {
    // Maps from (x,y) coordinate to a count for that coordinate.
    var contents: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    private fun increment(x: Int, y: Int) {
        val key = Pair(x, y)
        when (val count = contents[key]) {
            null -> contents[key] = 1
            else -> contents[key] = count + 1
        }
    }

    fun addLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        when {
            x1 == x2 -> makeRange(y1, y2).forEach { row -> increment(x1, row) }
            y1 == y2 -> makeRange(x1, x2).forEach { col -> increment(col, y1) }
            else -> makeRange(x1, x2).zip(makeRange(y1, y2)).forEach { (row, col) ->
                increment(row, col)
            }
        }
    }

    fun score(): Int = contents.values.count { it > 1 }
}

fun main() {
    val grid1 = Grid()
    val grid2 = Grid()
    val lineRegex = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)")
    File("input.txt").readLines().forEach {
        val match = lineRegex.find(it)!!
        val (x1, y1, x2, y2) = match.destructured
        if (x1 == x2 || y1 == y2) {
            grid1.addLine(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
        }
        grid2.addLine(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
    }

    println("${grid1.score()}")
    println("${grid2.score()}")
}
