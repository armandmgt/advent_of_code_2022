import java.awt.Point
import kotlin.math.abs

fun main() {
    data class Sensor(val pos: Point, val beacon: Point) {
        val xDistance = abs(beacon.x - pos.x)
        val yDistance = abs(beacon.y - pos.y)
        fun abscessesCoveredAt(y: Int): Set<Int> {
            val evaluationYDistance = abs(y - pos.y)
            val xSemiLength = xDistance + (yDistance - evaluationYDistance)
            return ((pos.x - xSemiLength)..(pos.x + xSemiLength)).toSet()
        }

        fun abscessesCoveredAt(y: Int, maxSearch: Int): IntRange? {
            val evaluationYDistance = abs(y - pos.y)
            val xSemiLength = xDistance + yDistance - evaluationYDistance
            if (xSemiLength < 0) return null
            return maxOf(0, pos.x - xSemiLength)..minOf(maxSearch, pos.x + xSemiLength)
        }
    }

    val sensorPattern = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
    fun part1(input: List<String>, y: Int): Int {
        return input.map {
            val (sensorX, sensorY, beaconX, beaconY) = sensorPattern.find(it)!!.destructured
            Sensor(Point(sensorX.toInt(), sensorY.toInt()), Point(beaconX.toInt(), beaconY.toInt()))
        }.fold(setOf<Int>()) { points, sensor ->
            points.plus(sensor.abscessesCoveredAt(y))
        }.size - 1
    }

    fun part2(input: List<String>, maxSearch: Int): Long {
        val sensors = input.map {
            val (sensorX, sensorY, beaconX, beaconY) = sensorPattern.find(it)!!.destructured
            Sensor(Point(sensorX.toInt(), sensorY.toInt()), Point(beaconX.toInt(), beaconY.toInt()))
        }
        return (0..maxSearch).asSequence().map { y ->
            sensors.fold(listOf<IntRange>()) { listOfRanges, sensor ->
                val range = sensor.abscessesCoveredAt(y, maxSearch) ?: return@fold listOfRanges
                val (includes, doesNotInclude) = listOfRanges.partition { r ->
                    r.contains(range.first) || r.contains(range.last) ||
                            range.contains(r.first) || range.contains(r.last)
                }
                doesNotInclude + listOf(includes.let { inc ->
                    if (inc.isEmpty()) range
                    else minOf(range.first, inc.minOf { it.first })..maxOf(range.last, inc.maxOf { it.last })
                })
            }
        }.withIndex().first { it.value.size > 1 }.let {
            val x = (if (it.value[0].first < it.value[1].first) it.value[0].last else it.value[1].last) + 1
            println("y=${it.index} + x=$x * 4000000 = ${it.index + Math.multiplyExact(x.toLong(), 4000000L)}")
            Math.addExact(it.index.toLong(), Math.multiplyExact(x.toLong(), 4000000L))
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("resources/Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}
