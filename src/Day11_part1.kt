fun main() {
    data class Monkey(
        val id: Int,
        var inspections: Int = 0,
        val items: MutableList<Int>,
        val operation: (Int) -> Int,
        val destination: (Int) -> Int
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
    fun makeOperation(operation: String): (Int) -> Int {
        when {
            multiplication.matches(operation) -> {
                val (factor) = multiplication.find(operation)!!.destructured
                return fun(old: Int): Int { return old * factor.toInt() }
            }

            square.matches(operation) -> return fun(old: Int): Int {
                return old * old
            }

            addition.matches(operation) -> {
                val (operand) = addition.find(operation)!!.destructured
                return fun(old: Int): Int {
                    return old + operand.toInt()
                }
            }
        }
        throw IllegalArgumentException("operation non matched: $operation")
    }

    fun makeDestination(divisor: Int, destTrueId: Int, destFalseId: Int): (Int) -> Int {
        return fun(worriness: Int): Int =
            if (worriness % divisor == 0) destTrueId else destFalseId
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
                    items = items.split(", ").map { it.toInt() }.toMutableList(),
                    operation = makeOperation(operation),
                    destination = makeDestination(divisor.toInt(), destTrue.toInt(), destFalse.toInt())
                )
            )
        }
        return monkeys
    }

    fun part1(input: List<String>): Int {
        val monkeys = parseMonkeyList(input)
        repeat(20) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    val newVal = monkey.operation(item) / 3
                    monkey.inspections = Math.addExact(monkey.inspections, 1)
                    val destMonkey = monkey.destination(newVal)
                    monkeys[destMonkey].items.add(newVal)
                }
                monkey.items.clear()
            }
        }
        return top(2, monkeys.map { it.inspections }).fold(1) { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day11_test")
    check(part1(testInput) == 10605)

    val input = readInput("resources/Day11")
    println(part1(input))
}
