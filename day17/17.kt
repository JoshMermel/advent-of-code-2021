import kotlin.math.sign

class Trajectory(private var dx: Int, private var dy: Int) {
    fun maxHeight(minX: Int, maxX: Int, minY: Int, maxY: Int): Int? {
        var x = 0
        var y = 0
        var maxHeight = 0
        while (x < maxX && y > maxY) {
            // update pos
            x += dx
            y += dy
            if (y > maxHeight) {
                maxHeight = y
            }

            // update velocity
            dx -= dx.sign
            dy -= 1

            if (x in (minX..maxX) && y in (maxY..minY)) {
                return maxHeight
            }
        }
        return null
    }
}

fun main() {
    // part 1
    var max = 0
    var total = 0
    for (dx in (1..1000)) {
        for (dy in (-1000..1000)) {
            val t = Trajectory(dx, dy)
            val m = t.maxHeight(70, 96, -124, -179)
            if (m != null) {
                total++
                if (m > max) {
                    max = m
                }
            }
        }
    }

    println("$max")
    println("$total")
}
