import java.io.File
import java.math.BigInteger

// |Input| maps from Integer to how many of that day appear in the input file.
class Population(input: Map<Int, Int>) {
    // In retrospect, Long would probably be fine here. I know for sure that 32
    // bit int isn't enough.
    private var days = MutableList(9) { (input[it] ?: 0).toBigInteger() }

    fun update() {
        val first = days.removeFirst()
        days[6] += first
        days.add(first)
    }

    fun size(): BigInteger = days.fold(BigInteger.ZERO, BigInteger::add)
}

fun main() {
    val input: List<Int> = File("input.txt").bufferedReader().readLine()
        .split(",").map { it.trim().toInt() }
    val pop = Population(input.groupingBy { it }.eachCount())

    // Part 1
    repeat(80) { pop.update() }
    println("${pop.size()}")

    // Part 2
    repeat(256 - 80) { pop.update() }
    println("${pop.size()}")
}
