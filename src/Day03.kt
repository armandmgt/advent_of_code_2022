fun main() {
    fun compartments(rucksack: String): Pair<CharArray, CharArray> {
        return Pair(
            rucksack.substring(0, rucksack.length / 2).toCharArray(),
            rucksack.substring(rucksack.length / 2).toCharArray()
        )
    }

    fun toPriority(char: Char): Int {
        return if (char <= 'Z') char.code - 64 + 26 else char.code - 96
    }


    fun part1(input: List<String>): Int {
        return input.sumOf { rucksack ->
            val pair = compartments(rucksack)
            val intersection = pair.first.toSet().intersect(pair.second.toSet())
            intersection.sumOf(::toPriority)
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { group ->
            val intersection = group[0].toSet().intersect(group[1].toSet().intersect(group[2].toSet()))
            intersection.sumOf(::toPriority)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)

    val input = readInput("Day03")
    println(part1(input))

    check(part2(testInput) == 70)
    println(part2(input))
}
