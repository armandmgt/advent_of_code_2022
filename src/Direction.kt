enum class Direction {
    NORTH, SOUTH, WEST, EAST;

    companion object {
        fun from(c: Char): Direction {
            return when (c) {
                'U' -> NORTH
                'D' -> SOUTH
                'L' -> WEST
                'R' -> EAST
                else ->
                    throw IllegalArgumentException()
            }
        }
    }
}