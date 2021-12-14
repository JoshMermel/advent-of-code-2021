import java.io.File

fun String.isLowerCase(): Boolean = this.toList().all { it.isLowerCase() }

class Graph(edges: List<List<String>>) {
    private val neighbors = mutableMapOf<String, MutableList<String>>()

    init {
        edges.forEach {
            addEdge(it[0], it[1])
            addEdge(it[1], it[0])
        }
    }

    private fun addEdge(src: String, dst: String) {
        if (neighbors.containsKey(src)) {
            neighbors[src]!!.add(dst)
        } else {
            neighbors[src] = mutableListOf(dst)
        }
    }

    fun neighbors(s: String): List<String> = neighbors[s] ?: listOf()
}

fun explore(
    graph: Graph,
    currentNode: String,
    visitedCaves: Set<String>,
    hasRevisited: Boolean
): Int {
    if (currentNode == "end") {
        return 1
    }

    return graph.neighbors(currentNode).map { neighbor ->
        if (neighbor == "start") {
            0
        } else if (neighbor in visitedCaves && hasRevisited) {
            0
        } else if (neighbor in visitedCaves && !hasRevisited) {
            explore(graph, neighbor, visitedCaves, true)
        } else if (neighbor.isLowerCase()) {
            explore(
                graph, neighbor,
                visitedCaves.toMutableSet().also { it.add(neighbor) },
                hasRevisited
            )
        } else {
            explore(graph, neighbor, visitedCaves, hasRevisited)
        }
    }.sum()
}

fun main() {
    val input = File("input.txt").readLines().map { it.split('-') }
    val graph = Graph(input)

    val part1 = explore(graph, "start", setOf(), true)
    println("$part1")

    val part2 = explore(graph, "start", setOf(), false)
    println("$part2")
}
