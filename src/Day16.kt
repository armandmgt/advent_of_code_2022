fun main() {
    data class Valve(val key: String, val rate: Int, val tunnels: List<String>)

    val valvePattern = Regex("Valve (\\w+) has flow rate=(\\d+);")
    val tunnelsPattern = Regex("tunnels? leads? to valves? ([\\w, ]+)")
    fun parseGraph(input: List<String>): Map<String, Valve> {
        val graph = mutableMapOf<String, Valve>()
        input.forEach {
            val (key, rate) = valvePattern.find(it)!!.destructured
            val tunnels = tunnelsPattern.find(it)!!.groupValues[1].split(", ")
            graph[key] = Valve(key, rate.toInt(), tunnels)
        }
        return graph
    }

    var callCount = 0
    fun bfs(graph: Map<String, Valve>, minutesLeft: Int, currentValveKey: String, openedValves: Set<String>): Int {
        callCount += 1
        println("analyzing $currentValveKey ; call count $callCount")
//        println("== Minute ${30 - minutesLeft} ==")
        val currentPressurePerMinute = openedValves.sumOf { graph[it]!!.rate }
//        println("Opened valves are ${openedValves.joinToString()} releasing $currentPressurePerMinute pressure.")
        if (minutesLeft == 0) return currentPressurePerMinute

        val currentValve = graph[currentValveKey]!!
//        println("currentValve rate=${currentValve.rate} neighbor=${currentValve.tunnels.first()}")
        val scoreIfOpening = if (currentValve.rate != 0 && !openedValves.contains(currentValve.key)) {
            bfs(graph, minutesLeft - 1, currentValve.key, openedValves + setOf(currentValve.key))
        } else { 0 }
        val scoreOfNeighbors = currentValve.tunnels.map { neighbor ->
            bfs(graph, minutesLeft - 1, neighbor, openedValves)
        }
        return currentPressurePerMinute + (arrayOf(scoreIfOpening) + scoreOfNeighbors).max()
    }

    fun part1(input: List<String>): Int {
        val graph = parseGraph(input)
        val openedValves = setOf<String>()
        return bfs(graph, 30, "AA", openedValves).also { println(it) }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day16_test")
    check(part1(testInput) == 1651)

    val input = readInput("resources/Day16")
    println(part1(input))
    println(part2(input))
}

//val testResult = arrayListOf(
//    Pair(graph["AA"]!!, false),
//    Pair(graph["DD"]!!, false),
//    Pair(graph["DD"]!!, true),
//    Pair(graph["CC"]!!, false),
//    Pair(graph["BB"]!!, false),
//    Pair(graph["BB"]!!, true),
//    Pair(graph["AA"]!!, false),
//    Pair(graph["II"]!!, false),
//    Pair(graph["JJ"]!!, false),
//    Pair(graph["JJ"]!!, true),
//    Pair(graph["II"]!!, false),
//    Pair(graph["AA"]!!, false),
//    Pair(graph["DD"]!!, false),
//    Pair(graph["EE"]!!, false),
//    Pair(graph["FF"]!!, false),
//    Pair(graph["GG"]!!, false),
//    Pair(graph["HH"]!!, false),
//    Pair(graph["HH"]!!, true),
//    Pair(graph["GG"]!!, false),
//    Pair(graph["FF"]!!, false),
//    Pair(graph["EE"]!!, false),
//    Pair(graph["EE"]!!, true),
//    Pair(graph["DD"]!!, false),
//    Pair(graph["CC"]!!, false),
//    Pair(graph["CC"]!!, true),
//    Pair(graph["CC"]!!, false),
//    Pair(graph["CC"]!!, false),
//    Pair(graph["CC"]!!, false),
//    Pair(graph["CC"]!!, false),
//    Pair(graph["CC"]!!, false),
//    Pair(graph["CC"]!!, false),
//)