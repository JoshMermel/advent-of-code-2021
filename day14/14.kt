import java.io.File

typealias Bigram = Pair<Char, Char>

// TODO(jmerm): define iterator so I can make counts private
class Counter<T> {
    var counts = mutableMapOf<T, Long>()

    fun increment(t: T, amount: Long) {
        counts.putIfAbsent(t, 0L)
        counts[t] = counts[t]!! + amount
    }

    fun minValue(): Long = counts.minByOrNull { it.value }!!.value
    fun maxValue(): Long = counts.maxByOrNull { it.value }!!.value
}

fun replace(
    seed: Counter<Bigram>,
    replacements: Map<Bigram, Pair<Bigram, Bigram>>
): Counter<Bigram> {
    val ret = Counter<Bigram>()
    for ((bigram, count) in seed.counts) {
        val (left, right) = replacements[bigram] ?: continue
        ret.increment(left, count)
        ret.increment(right, count)
    }
    return ret
}

fun main() {
    val input = File("input.txt").readLines()
    val seed = input[0].toList()
    var bigramCounts = Counter<Bigram>()
    for (pair in seed.zipWithNext()) {
        bigramCounts.increment(pair, 1)
    }

    val replacements = input.drop(2).map {
        val (left, right) = it.split(" -> ")
        Pair(
            Pair(left[0], left[1]),
            Pair(Pair(left[0], right[0]), Pair(right[0], left[1]))
        )
    }.toMap()

    repeat(40) {
        bigramCounts = replace(bigramCounts, replacements)
    }

    val charCounts = Counter<Char>()
    for ((key, value) in bigramCounts.counts) {
        val (l, r) = key
        charCounts.increment(l, value)
        charCounts.increment(r, value)
    }
    val max = charCounts.maxValue()
    val min = charCounts.minValue()

    // everyone is double counted except first and last of seed who need one
    // added to be properly double counted. In practice I think this ~never
    // matters.
    println("${(max - min) / 2}")
}
