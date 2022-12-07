fun main() {
    val indexesPattern = Regex("(:? \\d ? ?)+")
    val cratePattern = Regex(".{3} ?")
    fun parseCrates(inputIterator: ListIterator<String>): Map<Int, ArrayDeque<Char>> {
        val stacks = mutableMapOf<Int, MutableList<Char>>()
        run breaking@ {
            inputIterator.forEach { line ->
                if (line.matches(indexesPattern)) return@breaking

                val m = cratePattern.findAll(line)
                m.forEachIndexed { idx, matchResult ->
                    if (matchResult.value[1] == ' ') return@forEachIndexed
                    if (stacks[idx + 1] == null) stacks[idx + 1] = mutableListOf()

                    stacks[idx + 1]!!.add(0, matchResult.value[1])
                }
            }
        }
        inputIterator.next()
        return stacks.mapValues { entry -> ArrayDeque(entry.value) }
    }

    val opPattern = Regex("move (\\d+) from (\\d+) to (\\d+)")
    fun part1(input: List<String>): String {
        val crane = ArrayDeque<Char>()
        val iterator = input.listIterator()
        val stacks = parseCrates(iterator)
        iterator.forEachRemaining { operation ->
            val (count, origin, dest) = opPattern.find(operation)!!.destructured.toList().map(String::toInt)
            repeat(count) { crane.addLast(stacks[origin]!!.removeLast()) }
            repeat(count) { stacks[dest]!!.addLast(crane.removeFirst()) }
        }
        return (1 until stacks.size + 1).map { stacks[it]!!.last() }.joinToString("")
    }

    fun part2(input: List<String>): String {
        val crane = ArrayDeque<Char>()
        val iterator = input.listIterator()
        val stacks = parseCrates(iterator)
        iterator.forEachRemaining { operation ->
            val (count, origin, dest) = opPattern.find(operation)!!.destructured.toList().map(String::toInt)
            repeat(count) { crane.addLast(stacks[origin]!!.removeLast()) }
            repeat(count) { stacks[dest]!!.addLast(crane.removeLast()) }
        }
        return (1 until stacks.size + 1).map { stacks[it]!!.last() }.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day05_test")
    check(part1(testInput) == "CMZ")

    val input = readInput("resources/Day05")
    println(part1(input))

    check(part2(testInput) == "MCD")
    println(part2(input))
}
