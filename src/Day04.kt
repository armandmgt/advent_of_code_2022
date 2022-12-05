import java.util.Scanner
import java.util.regex.Pattern

fun main() {
    val pairDescPattern = Regex("(\\d+)-(\\d+),(\\d+)-(\\d+)")
    fun toPair(pairDesc: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val m = pairDescPattern.matchEntire(pairDesc)!!
        return Pair(
            Pair(m.groups[1]!!.value.toInt(), m.groups[2]!!.value.toInt()),
            Pair(m.groups[3]!!.value.toInt(), m.groups[4]!!.value.toInt())
        )
    }

    fun contains(first: Pair<Int, Int>, second: Pair<Int, Int>): Boolean {
        return first.first <= second.first && first.second >= second.second
    }

    fun part1(input: List<String>): Int {
        return input.count { pairDesc ->
            val pair = toPair(pairDesc)
            contains(pair.first, pair.second) || contains(pair.second, pair.first)
        }
    }

    fun overlaps(first: Pair<Int, Int>, second: Pair<Int, Int>): Boolean {
        return (first.first <= second.first && first.second >= second.first) ||
                (first.first <= second.second && first.second >= second.second)
    }

    fun part2(input: List<String>): Int {
        return input.count { pairDesc ->
            val pair = toPair(pairDesc)
            contains(pair.first, pair.second) || contains(pair.second, pair.first) || overlaps(pair.first, pair.second)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    val input = readInput("Day04")
    println(part1(input))

    check(part2(testInput) == 4)
    println(part2(input))
}
