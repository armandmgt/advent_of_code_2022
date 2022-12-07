fun main() {
    class Node(val name: String, val size: Int, val leafs: List<Node>)

    val cdPattern = Regex("\\$ cd ([/\\w]+)")
    val filePattern = Regex("(\\d+) .*")
    val exitPattern = Regex("\\$ cd \\.\\.")

    var matchingDirSum = 0
    fun enter(iterator: Iterator<String>): Int {
        var currentDirSum = 0
        iterator.next() // ignore ls command
        while (iterator.hasNext()) {
            val it = iterator.next()
            when {
                cdPattern.matches(it) -> {
                    val subdirSize = enter(iterator)
                    currentDirSum += subdirSize
                }

                exitPattern.matches(it) -> {
                    if (currentDirSum <= 100000) matchingDirSum += currentDirSum
                    return currentDirSum
                }

                filePattern.matches(it) -> {
                    val (fileSize) = filePattern.find(it)!!.destructured
                    currentDirSum += fileSize.toInt()
                }
            }
        }
        return currentDirSum
    }

    fun part1(input: List<String>): Int {
        matchingDirSum = 0
        val iterator = input.iterator()
        iterator.next() // ignore first cd command
        enter(iterator)
        return matchingDirSum
    }

    fun build(command: String, iterator: Iterator<String>): Node {
        val (dirname) = cdPattern.find(command)!!.destructured
        val leafs = mutableListOf<Node>()
        var size = 0
        iterator.next() // ignore ls command
        while (iterator.hasNext()) {
            val it = iterator.next()
            when {
                cdPattern.matches(it) -> {
                    val node = build(it, iterator)
                    size += node.size
                    leafs.add(node)
                }

                exitPattern.matches(it) -> {
                    break
                }

                filePattern.matches(it) -> {
                    val (fileSize) = filePattern.find(it)!!.destructured
                    size += fileSize.toInt()
                }
            }
        }
        return Node(dirname, size, leafs)
    }

    fun dfs(node: Node, necessarySpace: Int): Node {
        return node.leafs.filter {
            it.size >= necessarySpace
        }.map {
            dfs(it, necessarySpace)
        }.minByOrNull {
            it.size
        } ?: node
    }

    fun part2(input: List<String>): Int {
        val iterator = input.iterator()
        val command = iterator.next()
        val root = build(command, iterator)
        val necessarySpace = 30000000 - (70000000 - root.size)
        return dfs(root, necessarySpace).size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day07_test")
    check(part1(testInput) == 95437)

    val input = readInput("resources/Day07")
    println(part1(input))

    check(part2(testInput) == 24933642)
    println(part2(input))
}
