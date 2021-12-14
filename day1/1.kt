import java.io.*

fun List<Int>.countIncreases(windowSize: Int): Int =
    this.windowed(windowSize) { it.last() - it.first() }.count { it > 0 }

fun main() {
    val input = File("input.txt").readLines().map { it.trim().toInt() }

    val part1 = input.countIncreases(2)
    println("$part1")

    // Small hack. When comparing input[x]+input[x+1]+input[x+2] to the next
    // window, two elements are shared so we don't need to consider them.
    val part2 = input.countIncreases(4)
    println("$part2")
}

