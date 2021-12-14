import java.io.File

fun firstCorrupted(s: String): Int {
    val stack: MutableList<Char> = mutableListOf()
    for (c in s.toList()) {
        when (c) {
            '(' -> stack.add(')')
            '[' -> stack.add(']')
            '{' -> stack.add('}')
            '<' -> stack.add('>')
            else -> if (c == stack.last()) {
                stack.removeLast()
            } else {
                return score(c)
            }
        }
    }
    return 0
}

fun score(c: Char): Int {
    return when (c) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> 0
    }
}

fun completion(s: String): Long {
    val stack: MutableList<Char> = mutableListOf()
    for (c in s.toList()) {
        when (c) {
            '(' -> stack.add(')')
            '[' -> stack.add(']')
            '{' -> stack.add('}')
            '<' -> stack.add('>')
            else -> stack.removeLast()
        }
    }
    return score2(stack.reversed())
}

fun score2(s: List<Char>): Long {
    var score = 0L
    for (c in s) {
        score *= 5
        when (c) {
            ')' -> score += 1
            ']' -> score += 2
            '}' -> score += 3
            '>' -> score += 4
        }
    }
    return score
}

fun part1() {
    val answer = File("input.txt").readLines().map { firstCorrupted(it) }.sum()
    println("$answer")
}

fun part2() {
    val scores = File("input.txt")
        .readLines()
        .filter { firstCorrupted(it) == 0 }
        .map { completion(it) }
    val answer = scores.sorted()[scores.size / 2]
    println("$answer")
}

fun main() {
    part1()
    part2()
}

