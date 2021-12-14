import java.io.*

fun part1() {
  var depth = 0
  var forward = 0
  File("input.txt").readLines().forEach {
      val parts = it.split(" ")
      val num = parts[1].toInt()
      when (parts[0]) {
        "forward" -> forward += num
        "down" -> depth += num
        "up" -> depth -= num
    }
  }
 
  println("$depth, $forward, ${depth * forward}")
}

fun part2() {
  var depth = 0
  var forward = 0
  var aim = 0
  File("input.txt").readLines().forEach {
      val parts = it.split(" ")
      val num = parts[1].toInt()
      when (parts[0]) {
        "forward" -> {
          forward += num
          depth += (aim * num)
        }
        "down" -> aim += num
        "up" -> aim -= num
    }
  }
 
  println("$depth, $forward, ${depth * forward}")
}

fun main() {
  part1()
  part2()
}

