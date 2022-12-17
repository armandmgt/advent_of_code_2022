fun main() {
    data class Monkey(
        val id: Int,
        var inspections: Long = 0,
        val divisor: Long,
        val items: MutableList<Long>,
        val operation: (Long, Long) -> Long,
        val destination: (Long) -> Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Monkey

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id
        }
    }

    val multiplication = Regex("new = old \\* (\\d+)")
    val square = Regex("new = old \\* old")
    val addition = Regex("new = old \\+ (\\d+)")
    fun makeOperation(operation: String): (Long, Long) -> Long {
        when {
            multiplication.matches(operation) -> {
                val (factor) = multiplication.find(operation)!!.destructured
                return fun(modulus: Long, old: Long): Long { return (old * factor.toLong()) % modulus }
            }

            square.matches(operation) -> return fun(modulus: Long, old: Long): Long {
                return (old * old) % modulus
            }

            addition.matches(operation) -> {
                val (operand) = addition.find(operation)!!.destructured
                return fun(modulus: Long, old: Long): Long {
                    return (old + operand.toLong()) % modulus
                }
            }
        }
        throw IllegalArgumentException("operation non matched: $operation")
    }

    fun makeDestination(divisor: Long, destTrueId: Int, destFalseId: Int): (Long) -> Int {
        return fun(worriness: Long): Int =
            if (worriness % divisor == 0L) destTrueId else destFalseId
    }

    fun parseMonkeyList(input: List<String>): List<Monkey> {
        val monkeys = mutableListOf<Monkey>()
        input.chunked(7).forEach { monkeyDef ->
            val (id) = Regex("Monkey (\\d+):").find(monkeyDef[0])!!.destructured
            val (items) = Regex("Starting items: (.*)").find(monkeyDef[1])!!.destructured
            val (operation) = Regex("Operation: (.*)").find(monkeyDef[2])!!.destructured
            val (divisor) = Regex("Test: divisible by (.*)").find(monkeyDef[3])!!.destructured
            val (destTrue) = Regex("If true: throw to monkey (.*)").find(monkeyDef[4])!!.destructured
            val (destFalse) = Regex("If false: throw to monkey (.*)").find(monkeyDef[5])!!.destructured
            monkeys.add(
                Monkey(
                    id.toInt(),
                    divisor = divisor.toLong(),
                    items = items.split(", ").map { it.toLong() }.toMutableList(),
                    operation = makeOperation(operation),
                    destination = makeDestination(divisor.toLong(), destTrue.toInt(), destFalse.toInt())
                )
            )
        }
        return monkeys
    }

    fun part2(input: List<String>): Long {
        val monkeys = parseMonkeyList(input)
        val modulus = monkeys.fold(1L) { acc, m -> acc * m.divisor }
        repeat(10000) {
            monkeys.forEach { monkey ->
//                println("Monkey ${monkey.id}:")
                monkey.items.forEach { item ->
//                    println("  Monkey inspects an item with a worry level of $item.")
                    val newVal = monkey.operation(modulus, item)
//                    println("    Worry level after is $newVal.")
                    monkey.inspections += 1
                    val destMonkey = monkey.destination(newVal)
//                    println("    Item with worry level $newVal is thrown to monkey $destMonkey.")
                    monkeys[destMonkey].items.add(newVal)
                }
                monkey.items.clear()
            }
            if (it + 1 == 1 || it + 1 == 20 || (it + 1) % 1000 == 0) {
                println("== After round ${it + 1} ==")
                monkeys.forEach { m ->
                    println("Monkey ${m.id} inspected items ${m.inspections} times.")
                }
            }
        }
        return top(2, monkeys.map { it.inspections }).fold(1) { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day11_test")
    check(part2(testInput) == 2713310158)

    val input = readInput("resources/Day11")
    println(part2(input))
}
