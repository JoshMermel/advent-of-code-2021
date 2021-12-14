import java.io.File

fun String.hasUniqueSegmentCount(): Boolean = this.length in listOf(2, 3, 4, 7)

fun part1() {
    val input = File("input.txt").readLines().map { line ->
        val parts = line.split("|")
        val part2 = parts[1].split(" ").filter { line.isNotEmpty() }
        part2.count { it.hasUniqueSegmentCount() }
    }
    println("${input.sum()}")
}

// Taken from stack overflow
fun <T> allPermutations(set: Set<T>): Set<List<T>> {
    if (set.isEmpty()) return emptySet()

    fun <T> _allPermutations(list: List<T>): Set<List<T>> {
        if (list.isEmpty()) return setOf(emptyList())

        val result: MutableSet<List<T>> = mutableSetOf()
        for (i in list.indices) {
            _allPermutations(list - list[i]).forEach { item ->
                result.add(item + list[i])
            }
        }
        return result
    }

    return _allPermutations(set.toList())
}

// Translates from a set of segments to the int they "spell". Returns null if
// the set is invalid.
fun segmentToInt(segments: Set<Int>): Int? {
    return when (segments) {
        setOf(0, 1, 2, 4, 5, 6) -> 0
        setOf(2, 5) -> 1
        setOf(0, 2, 3, 4, 6) -> 2
        setOf(0, 2, 3, 5, 6) -> 3
        setOf(1, 2, 3, 5) -> 4
        setOf(0, 1, 3, 5, 6) -> 5
        setOf(0, 1, 3, 4, 5, 6) -> 6
        setOf(0, 2, 5) -> 7
        setOf(0, 1, 2, 3, 4, 5, 6) -> 8
        setOf(0, 1, 2, 3, 5, 6) -> 9
        else -> null
    }
}

// Decodes a word into a list of segments with a given mapping
fun stringToSegments(word: String, mapping: List<Int>): Set<Int> {
    return word.toList().map { it.code - 'a'.code }.map { mapping[it] }.toSet()
}

// Returns whether all strings in the line decode to valid 7-segment digits with
// the given mapping.
fun allValid(line: List<String>, mapping: List<Int>): Boolean =
    line.map { segmentToInt(stringToSegments(it, mapping)) }.all { it != null }

// Checks all mappings against the line and returns one where all words decode
// to valid 7-segment numerals
fun getPermutation(line: List<String>): List<Int> =
    allPermutations(setOf(0, 1, 2, 3, 4, 5, 6)).first { allValid(line, it) }

// Applys a mapping to a word and returns the int it maps to. Assumes the
// mapping is valid.
fun applyPermutation(line: List<String>, mapping: List<Int>): Int =
    line.map { segmentToInt(stringToSegments(it, mapping))!! }.joinToString("").toInt()

fun part2() {
    // This approach was pretty easy to write and I think it's pretty easy to
    // read but it's quite slow.
    val solution = File("input.txt").readLines().map { line ->
        val parts = line.split("|")
        
        // find a permutation where the whole left half can be decoded
        val part1 = parts[0].split(" ").filter { line.isNotEmpty() }
        val perm = getPermutation(part1)

        // apply it to the right half
        val part2 = parts[1].split(" ").filter { line.isNotEmpty() }
        applyPermutation(part2, perm)
    }.sum()

    println("$solution")
}

// This was a fun one to write but is horrible to read and debug.
// It figures out the meaning of each segment one-by-one.
fun decode(part1: List<String>, part2: List<String>): Int {
    val sets: Map<Int, List<Set<Char>>> = part1.map { it.toList().toSet() }.groupBy { it.size }
    // TODO(jmerm): assert right sizes?

    val charToSegment: MutableMap<Char, Int> = mutableMapOf()

    val segment0 = (sets[3]!!.single() - sets[2]!!.single()).single()
    charToSegment[segment0] = 0

    val one_three = (sets[4]!!.single() - sets[2]!!.single()).toMutableSet()
    sets[5]!!.forEach { one_three.retainAll(it) }
    val segment3 = one_three.single()
    charToSegment[segment3] = 3

    val segment6 =
        (sets[5]!!.single { it.containsAll(sets[3]!!.single()) } - sets[3]!!.single() - setOf(
            segment3
        )).single()
    charToSegment[segment6] = 6

    val segment2 =
        sets[6]!!.map { sets[3]!!.single() - it }.single { it.size == 1 }.single()
    charToSegment[segment2] = 2

    val segment5 = (sets[2]!!.single() - setOf(segment2)).single()
    charToSegment[segment5] = 5

    val segment1 = (sets[4]!!.single() - setOf(segment2, segment3, segment5)).single()
    charToSegment[segment1] = 1

    val segment4 = (sets[7]!!.single() - setOf(
        segment0,
        segment1,
        segment2,
        segment3,
        segment5,
        segment6
    )).single()
    charToSegment[segment4] = 4

    return part2.map { word ->
        segmentToInt(word.toList().map { charToSegment[it]!! }.toSet())
    }.joinToString("").toInt()
}

fun part2b() {
    val solution = File("input.txt").readLines().map { line ->
        val parts = line.split("|")
        val part1 = parts[0].split(" ").filter { line.isNotEmpty() }
        val part2 = parts[1].split(" ").filter { line.isNotEmpty() }
        decode(part1, part2)
    }.sum()

    println("$solution")
}

// My third idea and probably how you're supposed to do it. I don't decode
// segments and just figure out which digit each word is by checking overlap
// with known digits.
fun decodeC(part1: List<Set<Char>>, part2: String): Int {
    val len2 = part1.single { it.size == 2 }
    val len3 = part1.single { it.size == 3 }
    val len4 = part1.single { it.size == 4 }

    return when (part2.length) {
        2 -> 1
        3 -> 7
        4 -> 4
        7 -> 8
        5 -> {
            val s = part2.toList().toSet()
            if (s.containsAll(len2)) {
                3
            } else if ((s intersect len4).size == 2) {
                2
            } else {
                5
            }
        }
        6 -> {
            val s = part2.toList().toSet()
            if (s.containsAll(len3) && s.containsAll(len4)) {
                9
            } else if (s.containsAll(len3) && !s.containsAll(len4)) {
                0
            } else {
                6
            }
        }
        else -> -1
    }
}

fun part2c() {
    val solution = File("input.txt").readLines().map { line ->
        val parts = line.split("|")
        val part1 = parts[0].split(" ").filter { line.isNotEmpty() }.map { it.toList().toSet() }
        val part2 = parts[1].split(" ").filter { line.isNotEmpty() }
        part2.joinToString("") { decodeC(part1, line).toString() }.toInt()
    }.sum()

    println("$solution")
}

fun main() {
    part1()
    part2()
    part2b()
    part2c()
}
