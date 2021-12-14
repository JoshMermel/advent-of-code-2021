import java.io.*

class BingoBoard(contents: List<String>) {
    val board = contents.take(5).flatMap {
        it.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    }.toMutableList()

    fun markNumber(number: Int) {
        val idx = board.indexOfFirst { it == number }
        if (idx != -1) {
            board[idx] = 0
        }
    }

    fun isComplete(): Boolean = hasCompleteRow() || hasCompleteCol()

    private fun hasCompleteRow(): Boolean =
        board.chunked(5).any { row -> row.all { it == 0 } }

    private fun hasCompleteCol(): Boolean {
        for (col in (0..4)) {
            if (board[col] + board[col + 5] + board[col + 10] + board[col + 15] + board[col + 20] == 0) {
                return true
            }
        }
        return false
    }

    fun score(): Int = board.sum()
}

fun part1() {
    val input = File("input.txt").readLines()
    val orders = input[0].split(",").map { it.toInt() }
    val boards = input.drop(2).chunked(6).map { BingoBoard(it) }

    for (order in orders) {
        boards.forEach { it.markNumber(order) }
        val complete = boards.firstOrNull { it.isComplete() }
        if (complete != null) {
            println((complete.score() * order).toString())
            return
        }
    }
}

fun part2() {
    val input = File("input.txt").readLines()
    val orders = input[0].split(",").map { it.toInt() }
    var boards = input.drop(2).chunked(6).map { BingoBoard(it) }

    for (order in orders) {
        boards.forEach { it.markNumber(order) }
        if (boards.size == 1 && boards[0].isComplete()) {
            println((boards[0].score() * order).toString())
            return
        }
        boards = boards.filter { !it.isComplete() }
    }
}

fun main() {
    part1()
    part2()
}
