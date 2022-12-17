import java.util.*
import kotlin.math.abs

fun main() {
    data class Node(val x: Int, val y: Int, val height: Int)

    data class Puzzle(val start: Node, val end: Node, val nodes: List<List<Node>>)

    fun parse(input: List<String>): Puzzle {
        var start: Node? = null
        var end: Node? = null
        val nodes = mutableListOf<List<Node>>()
        input.forEachIndexed { y, line ->
            val lineNodes = mutableListOf<Node>()
            line.forEachIndexed { x, c ->
                when (c) {
                    'S' -> {
                        start = Node(x, y, 0)
                        lineNodes.add(start!!)
                    }

                    'E' -> {
                        end = Node(x, y, 25)
                        lineNodes.add(end!!)
                    }

                    else -> lineNodes.add(Node(x, y, c - 'a'))
                }
            }
            nodes.add(lineNodes)
        }
        return Puzzle(start!!, end!!, nodes)
    }

    fun manhattanDist(n1: Node, end: Node): Int {
        return abs(end.x - n1.x) + abs(end.y - n1.y)
    }

    fun reconstructPath(cameFrom: Map<Node, Node>, current: Node): List<Node> {
        var cur = current
        val totalPath = mutableListOf(cur)
        while (cameFrom.containsKey(cur)) {
            cur = cameFrom[cur]!!
            totalPath.add(cur)
        }
        return totalPath
    }

    fun validNeighbours(current: Node, puzzle: Puzzle): List<Node> {
        return arrayOf(
            Pair(current.x, current.y - 1),
            Pair(current.x + 1, current.y),
            Pair(current.x, current.y + 1),
            Pair(current.x - 1, current.y)
        ).mapNotNull { (x, y) ->
            if (y < 0 || y > puzzle.nodes.size - 1) return@mapNotNull null
            if (x < 0 || x > puzzle.nodes[0].size - 1) return@mapNotNull null
            if (puzzle.nodes[y][x].height <= current.height + 1) puzzle.nodes[y][x] else null
        }
    }

    fun aStar(puzzle: Puzzle): List<Node> {
        val cameFrom = mutableMapOf<Node, Node>()

        val gScore = mutableMapOf<Node, Int>()
        gScore[puzzle.start] = 0

        val fScore = mutableMapOf<Node, Int>()
        fScore[puzzle.start] = manhattanDist(puzzle.start, puzzle.end)

        val openSet = PriorityQueue<Node>(1) { n1, n2 ->
            compareValuesBy(n1, n2) { fScore.getOrDefault(it, Int.MAX_VALUE) }
        }
        openSet.add(puzzle.start)

        while (!openSet.isEmpty()) {
            val current = openSet.remove()

            if (current.equals(puzzle.end)) {
                return reconstructPath(cameFrom, current)
            }

            for (neighbour in validNeighbours(current, puzzle)) {
                val tentativeGScore = gScore.getOrDefault(current, Int.MAX_VALUE) + 1
                if (tentativeGScore < gScore.getOrDefault(neighbour, Int.MAX_VALUE)) {
                    cameFrom[neighbour] = current
                    gScore[neighbour] = tentativeGScore
                    fScore[neighbour] = tentativeGScore + manhattanDist(neighbour, puzzle.end)
                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour)
                    }
                }
            }
        }
        return emptyList()
    }

    fun part1(input: List<String>): Int {
        val puzzle = parse(input)
        return aStar(puzzle).size - 1
    }

    fun part2(input: List<String>): Int {
        val puzzle = parse(input)
        return puzzle.nodes.fold(emptyList<Node>()) { acc, nodes ->
            acc + nodes.filter { it.height == 0 }
        }.minOf { start ->
            aStar(puzzle.copy(start = start)).let {
                if (it.isEmpty()) Int.MAX_VALUE else it.size - 1
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day12_test")
    check(part1(testInput) == 31)

    val input = readInput("resources/Day12")
    println(part1(input))

    check(part2(testInput) == 29)
    println(part2(input))
}
