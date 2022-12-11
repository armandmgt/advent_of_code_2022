fun main() {
    abstract class Triable {
        abstract fun tryObservation(cycle: Int, reg: Int): Triable
        abstract fun value(): Any
    }

    data class State(val cycle: Int, val reg: Int, val triable: Triable)

    data class SumSignalStrengths(val value: Int) : Triable() {
        override fun tryObservation(cycle: Int, reg: Int): SumSignalStrengths {
            if ((cycle - 20) % 40 != 0) return this

            return copy(value = value + reg * cycle).also {
//            println("At cycle $cycle reg = $reg, signal = ${reg * cycle}, sum = $it")
            }
        }

        override fun value(): Int = value
    }

    val addPattern = Regex("addx (-?\\d+)")
    fun execInstructions(input: List<String>, initial: State) = input.fold(initial) { state, instr ->
        var (cycle, reg, triable) = state
        triable = triable.tryObservation(cycle, reg)
        cycle += 1
        when {
            addPattern.matches(instr) -> {
                triable = triable.tryObservation(cycle, reg)
                val (value) = addPattern.find(instr)!!.destructured
                reg += value.toInt()
                cycle += 1
            }
        }
        State(cycle, reg, triable)
    }.triable.value()

    fun part1(input: List<String>): Int {
        val initial = State(1, 1, SumSignalStrengths(0))
        return execInstructions(input, initial) as Int
    }

    @Suppress("ArrayInDataClass")
    data class CRT(var value: Array<Char>) : Triable() {
        override fun tryObservation(cycle: Int, reg: Int): CRT {
            val crtCursorPos = (cycle - 1) % 40
            (reg - 1..reg + 1).forEach {
                if (crtCursorPos == it) {
                    value[cycle - 1] = '#'
                }
            }
            return this
        }

        override fun value(): String = value.asIterable().chunked(40).joinToString("\n") { it.joinToString("") }
//            .also(::println)
    }

    fun part2(input: List<String>): String {
        val initial = State(1, 1, CRT(Array(6 * 40) { '.' }))
        return execInstructions(input, initial) as String
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day10_test")
    check(part1(testInput) == 13140)

    val input = readInput("resources/Day10")
    println(part1(input))

    val result2 = readInput("resources/Day10_test2").joinToString("\n")
    check(part2(testInput) == result2)
    println(part2(input))
}
