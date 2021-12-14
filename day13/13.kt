import java.io.File

class OrigamiPaper(var points: Set<Pair<Int, Int>>) {
    override fun toString(): String {
        return "$points"
    }

    fun foldX(x: Int) {
        points = points.map {
            if (it.first <= x) {
                it
            } else {
                Pair(2 * x - it.first, it.second)
            }
        }.toSet()
    }

    fun foldY(y: Int) {
        points = points.map {
            if (it.second <= y) {
                it
            } else {
                Pair(it.first, 2 * y - it.second)
            }
        }.toSet()
    }

    fun numDots(): Int = points.size
}

fun main() {
    val coordinateRegex = Regex("(\\d+),(\\d+)")
    val coordinates = mutableSetOf<Pair<Int, Int>>()
    val input = File("input.txt").bufferedReader()

    while (true) {
        val line = input.readLine()
        if (line.isNullOrEmpty()) break
        val match = coordinateRegex.find(line)!!
        val (x, y) = match.destructured
        coordinates.add(Pair(x.toInt(), y.toInt()))
    }

    val paper = OrigamiPaper(coordinates)

    val foldRegex = Regex("fold along (.)=(\\d+)")
    val numPoints = mutableListOf<Int>()
    while (true) {
        val line = input.readLine() ?: break
        val match = foldRegex.find(line)!!
        val (axis, offset) = match.destructured
        when (axis) {
            "x" -> paper.foldX(offset.toInt())
            "y" -> paper.foldY(offset.toInt())
        }
        numPoints.add(paper.numDots())
    }
    println(numPoints[0])

    val numRows = 1 + paper.points.maxByOrNull { it.second }!!.second
    val numCols = 1 + paper.points.maxByOrNull { it.first }!!.first
    val out = MutableList(numRows) { MutableList(numCols) { " " } }
    for ((x, y) in paper.points) {
        out[y][x] = "x"
    }
    println(out.joinToString("\n") { it.joinToString("") })
}
