data class PacketList(val items: List<Either<Int, PacketList>>) : Comparable<PacketList> {
    override fun toString(): String {
        return buildString {
            append("[")
            append(items.joinToString(", ") {
                when (it) {
                    is Either.Left<Int, PacketList> -> it.left.toString()
                    is Either.Right<Int, PacketList> -> it.right.toString()
                }
            })
            append("]")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PacketList) return false

        return compareTo(other) == 0
    }

    override fun hashCode(): Int {
        return items.hashCode()
    }

    companion object {
        fun from(iterator: CharIterator): PacketList {
            val items = mutableListOf<Either<Int, PacketList>>()
            var intStr = ""
            while (iterator.hasNext()) {
                val c = iterator.next()
                when {
                    c == '[' -> items.add(Either.Right(from(iterator)))
                    ('0'..'9').contains(c) -> intStr += c
                    c == ',' || c == ']' -> {
                        if (intStr.isNotEmpty()) {
                            items.add(Either.Left(intStr.toInt()))
                            intStr = ""
                        }
                        if (c == ']') {
                            return PacketList(items)
                        }
                    }
                }
            }
            throw IllegalArgumentException("Packet is not terminated correctly. Current items: `$items`")
        }
    }

    override fun compareTo(other: PacketList): Int {
        for (itemWithIndex in items.withIndex()) {
            val itemLeft = itemWithIndex.value
            if (itemWithIndex.index >= other.items.size) return 1
            val itemRight = other.items[itemWithIndex.index]
            if (itemLeft is Either.Left<Int, PacketList> && itemRight is Either.Left<Int, PacketList>) {
                if (itemLeft.left < itemRight.left) return -1
                if (itemLeft.left > itemRight.left) return 1
            } else if (itemLeft is Either.Right<Int, PacketList> && itemRight is Either.Right<Int, PacketList>) {
                if (itemLeft.right < itemRight.right) return -1
                if (itemLeft.right > itemRight.right) return 1
            } else if (itemLeft is Either.Left<Int, PacketList> && itemRight is Either.Right<Int, PacketList>) {
                val newLeft = PacketList(listOf(itemLeft))
                if (newLeft < itemRight.right) return -1
                if (newLeft > itemRight.right) return 1
            } else if (itemRight is Either.Left<Int, PacketList> && itemLeft is Either.Right<Int, PacketList>) {
                val newRight = PacketList(listOf(itemRight))
                if (itemLeft.right < newRight) return -1
                if (itemLeft.right > newRight) return 1
            }
        }
        if (items.size == other.items.size) return 0
        return -1
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.chunked(3).mapIndexed { index, (packet1, packet2) ->
            if (PacketList.from(packet1.iterator().apply { next() }) <= PacketList.from(
                    packet2.iterator().apply { next() })
            ) {
                index + 1
            } else 0
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return (input + listOf("", "[[2]]", "[[6]]")).chunked(3).flatMap { (packet1, packet2) ->
            listOf(
                PacketList.from(packet1.iterator().apply { next() }),
                PacketList.from(packet2.iterator().apply { next() })
            )
        }.sorted().let { res ->
            (res.indexOf(PacketList.from("[[2]]".iterator().apply { next() })) + 1) *
                    (res.indexOf(PacketList.from("[[6]]".iterator().apply { next() })) + 1)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("resources/Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("resources/Day13")
    println(part1(input))
    println(part2(input))
}
