fun main() {
    fun countInDir(input: List<String>, dir: Direction): MutableList<Pair<Int, Int>> {
        val visibleTrees = mutableListOf<Pair<Int, Int>>()
        var currentSize = -1

        val range = when (dir) {
            Direction.NORTH, Direction.WEST -> input.indices.reversed()
            Direction.SOUTH, Direction.EAST -> input.indices
        }
        for (c in input.indices) {
            for (m in range) {
                val pos = when (dir) {
                    Direction.NORTH, Direction.SOUTH -> Pair(c, m)
                    Direction.WEST, Direction.EAST -> Pair(m, c)
                }
                if ((input[pos.first][pos.second] - '0') > currentSize) {
                    currentSize = input[pos.first][pos.second] - '0'
                    visibleTrees.add(pos)
                }
            }
            currentSize = -1
        }
        return visibleTrees;
    }

    fun part1(input: List<String>): Int {
        return (countInDir(input, Direction.NORTH) + countInDir(input, Direction.SOUTH) +
                countInDir(input, Direction.EAST) + countInDir(input, Direction.WEST)).toSet().size
    }

    fun viewFrom(input: List<String>, x: Int, y: Int, dir: Direction): Int {
        var count = 0
        val range = when (dir) {
            Direction.NORTH -> (0 until y).reversed()
            Direction.SOUTH -> (y + 1..input.lastIndex)
            Direction.EAST -> (x + 1..input.lastIndex)
            Direction.WEST -> (0 until x).reversed()
        }
        range.forEach {
            count += 1
            when (dir) {
                Direction.NORTH, Direction.SOUTH ->
                    if (input[it][x] >= input[y][x]) return count

                Direction.EAST, Direction.WEST ->
                    if (input[y][it] >= input[y][x]) return count
            }
        }
        return count
    }

    fun scenicScore(input: List<String>, x: Int, y: Int): Int {
        return viewFrom(input, x, y, Direction.NORTH) *
                viewFrom(input, x, y, Direction.SOUTH) *
                viewFrom(input, x, y, Direction.EAST) *
                viewFrom(input, x, y, Direction.WEST)
    }

    fun part2(input: List<String>): Int {
        return input.indices.maxOf { y ->
            input.indices.maxOf { x ->
                scenicScore(input, x, y)
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day08_test")
    check(part1(testInput) == 21)

    val input = readInput("resources/Day08")
    println(part1(input))

    check(part2(testInput) == 8)
    println(part2(input))
}
