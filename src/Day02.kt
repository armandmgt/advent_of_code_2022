fun main() {
    val scores = mapOf(
        Pair("A", "X") to 4, Pair("A", "Y") to 8, Pair("A", "Z") to 3,
        Pair("B", "X") to 1, Pair("B", "Y") to 5, Pair("B", "Z") to 9,
        Pair("C", "X") to 7, Pair("C", "Y") to 2, Pair("C", "Z") to 6,
    )

    fun part1(input: List<String>): Int {
        return input.sumOf {
            val shapes = it.split(' ')
            scores[Pair(shapes[0], shapes[1])]!!
        }
    }

    val choices = mapOf(
        Pair("A", "X") to "Z", Pair("A", "Y") to "X", Pair("A", "Z") to "Y",
        Pair("B", "X") to "X", Pair("B", "Y") to "Y", Pair("B", "Z") to "Z",
        Pair("C", "X") to "Y", Pair("C", "Y") to "Z", Pair("C", "Z") to "X",
    )

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val shapeAndOutcome = it.split(' ')
            scores[Pair(shapeAndOutcome[0], choices[Pair(shapeAndOutcome[0], shapeAndOutcome[1])]!!)]!!
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
