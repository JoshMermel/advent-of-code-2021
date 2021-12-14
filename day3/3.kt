import java.io.*

fun mostCommonBit(input: List<String>, offset: Int): Char {
    val count = input.map { it[offset] }.groupingBy { it }.eachCount()
    return if (count.getOrDefault('0', 0) > count.getOrDefault('1', 0)) '0' else '1'
}

fun leastCommonBit(input: List<String>, offset: Int): Char {
    return if (mostCommonBit(input, offset) == '1') '0' else '1'
}

// Parses two strings made up of '0' and '1' as binary and then returns the
// product.
fun multiplyBinaryString(first: String, second: String): Int =
    Integer.parseInt(first, 2) * Integer.parseInt(second, 2)

fun part1() {
    val input = File("input.txt").readLines()
    var most = ""
    var least = ""
    for (i in input[0].indices) {
        most += mostCommonBit(input, i)
        least += leastCommonBit(input, i)
    }

    println(multiplyBinaryString(most, least).toString())
}

fun filterBitwise(
    lst: List<String>, offset: Int,
    filterFun: (List<String>, Int) -> Char?
): String {
    if (lst.size == 1) {
        return lst[0]
    }

    val filterVal = filterFun(lst, offset)
    return filterBitwise(lst.filter { it[offset] == filterVal }, offset + 1, filterFun)
}

fun part2() {
    val input = File("input.txt").readLines()
    val oxygen = filterBitwise(input, 0, ::mostCommonBit)
    val co2 = filterBitwise(input, 0, ::leastCommonBit)

    println(multiplyBinaryString(oxygen, co2).toString())
}

fun main() {
    part1()
    part2()
}
