fun main() {
    fun part1(input: List<String>): Int {
        var maxCalories = 0
        var sum = 0
        input.forEach {
            if (it == "") {
                if (sum > maxCalories) maxCalories = sum
                sum = 0
            } else {
                sum += it.toInt()
            }
        }
        return maxCalories
    }

    fun part2(input: List<String>): Int {
        var top3 = arrayOf(0, 0, 0)
        var sum = 0
        input.forEach {
            if (it == "") {
                top3 = (arrayOf(sum) + top3).sortedArray().reversedArray().take(3).toTypedArray()
                sum = 0
            } else {
                sum += it.toInt()
            }
        }
        return top3.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("resources/Day01")
    println(part1(input))
    println(part2(input))
}
