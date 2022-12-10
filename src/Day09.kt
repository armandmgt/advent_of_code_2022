import kotlin.math.abs
import kotlin.math.sign

fun main() {
    data class State(val headPos: Pair<Int, Int>, val tailPos: Pair<Int, Int>)

    fun follow(head: Int, tail: Int, headOther: Int, tailOther: Int): Pair<Int, Int> {
        // If tail is 2 squares away from head, move in direction of head
        if (abs(head - tail) >= 2) {
            val mainChange = (head - tail).sign

            // Move along other axis too if not same position on main axis
            val otherChange = if (abs(headOther - tailOther) == 1) {
                headOther - tailOther
            } else 0
            return Pair(mainChange, otherChange)
        }
        return Pair(0, 0)
    }

    fun move(state: State, visited: MutableSet<Pair<Int, Int>>, dir: Direction, steps: Int): State {
        return (0 until steps).fold(state) { s, _ ->
            val newHeadPos = when (dir) {
                Direction.NORTH -> Pair(s.headPos.first, s.headPos.second + 1)
                Direction.SOUTH -> Pair(s.headPos.first, s.headPos.second - 1)
                Direction.WEST -> Pair(s.headPos.first - 1, s.headPos.second)
                Direction.EAST -> Pair(s.headPos.first + 1, s.headPos.second)
            }
            var newTailPosX = s.tailPos.first
            var newTailPosY = s.tailPos.second

            follow(newHeadPos.first, s.tailPos.first, newHeadPos.second, s.tailPos.second).also {
                newTailPosX += it.first
                newTailPosY += it.second
            }
            follow(newHeadPos.second, s.tailPos.second, newHeadPos.first, s.tailPos.first).also {
                newTailPosY += it.first
                newTailPosX += it.second
            }

            val newTailPos = Pair(newTailPosX, newTailPosY)
            visited.add(newTailPos)
            State(newHeadPos, newTailPos)
        }
    }

    fun part1(input: List<String>): Int {
        val state = State(Pair(0, 0), Pair(0, 0))
        val visited = mutableSetOf<Pair<Int, Int>>()
        input.fold(state) { s, motion ->
            val dir = Direction.from(motion[0])
            val steps = motion.substringAfter(" ").toInt()
            move(s, visited, dir, steps)
        }
        return visited.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day09_test")
    check(part1(testInput) == 13)

    val input = readInput("resources/Day09")
    println(part1(input))

    val testInput2 = readInput("resources/Day09_test2")
    check(part2(testInput) == 36)
    println(part2(input))
}
