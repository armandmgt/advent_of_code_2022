fun main() {
    fun part1(input: List<String>): Int {
        return input.first().windowed(size = 4).indexOfFirst {
            it.toSet().size == it.length
        } + 4
    }

    fun part2(input: List<String>): Int {
        return input.first().windowed(size = 14).indexOfFirst {
            it.toSet().size == it.length
        } + 14
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)

    val input = readInput("Day06")
    println(part1(input))

    check(part2(testInput) == 19)
    println(part2(input))
}
