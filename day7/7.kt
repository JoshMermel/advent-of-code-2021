import java.io.File
import kotlin.math.absoluteValue

fun List<Int>.constantCostCumulative(target: Int): Int =
    this.map { (target - it).absoluteValue }.sum()

fun List<Int>.increasingCostCumulative(target: Int): Int =
    this.map {
        val d = (target - it).absoluteValue
        d * (d + 1) / 2
    }.sum()

fun main() {
    val input: List<Int> = File("input.txt").bufferedReader().readLine()
        .split(",").map { it.trim().toInt() }
    val max = input.maxOrNull()!!
    val min = input.minOrNull()!!

    val best = (min..max).map { input.constantCostCumulative(it) }.minOrNull()!!
    println("$best")

    val best2 = (min..max).map { input.increasingCostCumulative(it) }.minOrNull()!!
    println("$best2")
}
