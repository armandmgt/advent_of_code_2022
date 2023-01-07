import java.awt.Point

fun main() {
    val pointPattern = Regex("(\\d+),(\\d+)")

    class Line(def: String) {
        val points: List<Point>

        init {
            this.points = def.split(" -> ").map {
                val (x, y) = pointPattern.find(it)!!.destructured
                Point(x.toInt(), y.toInt())
            }
        }

        fun contains(point: Point): Boolean {
            return points.windowed(2).any { (start, end) ->
                when {
                    start.y == end.y -> point.y == start.y && (minOf(start.x, end.x)..maxOf(start.x, end.x)).contains(point.x)
                    start.x == end.x -> point.x == start.x && (minOf(start.y, end.y)..maxOf(start.y, end.y)).contains(point.y)
                    else -> throw IllegalArgumentException("Line is neither vertical nor horizontal")
                }
            }
        }
    }

    fun newGrainPos(grain: Point, grains: MutableList<Point>, lines: List<Line>): Point {
        return listOf(Point(grain.x, grain.y + 1), Point(grain.x - 1, grain.y + 1), Point(grain.x + 1, grain.y + 1)).firstOrNull {
            !grains.contains(it) && lines.none { line -> line.contains(it) }
        } ?: grain
    }

    fun part1(input: List<String>): Int {
        val lines = input.map { Line(it) }
        val lowestY = lines.maxOf { it.points.maxOf { p -> p.y } }
        val grains = mutableListOf<Point>()
        while (true) {
            var grain = Point(500, 0)
            do {
                val prevPos = grain
                grain = newGrainPos(grain, grains, lines)
            } while (grain.y != prevPos.y && grain.y < lowestY)
            if (grain.y >= lowestY) return grains.size
            grains.add(grain)
        }
    }

    fun part2(input: List<String>): Int {
        val lines = input.map { Line(it) }
        val lowestY = lines.maxOf { it.points.maxOf { p -> p.y } }
        val grains = mutableListOf<Point>()
        while (true) {
            var grain = Point(500, 0)
            do {
                val prevPos = grain
                grain = newGrainPos(grain, grains, lines)
            } while (grain.y != prevPos.y && grain.y < lowestY + 1)
            if (grain == Point(500, 0)) return grains.size + 1
            grains.add(grain)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("resources/Day14")
    println(part1(input))
    println(part2(input))
}
