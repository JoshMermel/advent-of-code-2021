import java.io.File

fun Char.toBinary(): List<Int> = when (this) {
    '0' -> listOf(0, 0, 0, 0)
    '1' -> listOf(0, 0, 0, 1)
    '2' -> listOf(0, 0, 1, 0)
    '3' -> listOf(0, 0, 1, 1)
    '4' -> listOf(0, 1, 0, 0)
    '5' -> listOf(0, 1, 0, 1)
    '6' -> listOf(0, 1, 1, 0)
    '7' -> listOf(0, 1, 1, 1)
    '8' -> listOf(1, 0, 0, 0)
    '9' -> listOf(1, 0, 0, 1)
    'A' -> listOf(1, 0, 1, 0)
    'B' -> listOf(1, 0, 1, 1)
    'C' -> listOf(1, 1, 0, 0)
    'D' -> listOf(1, 1, 0, 1)
    'E' -> listOf(1, 1, 1, 0)
    'F' -> listOf(1, 1, 1, 1)
    else -> listOf()
}

fun List<Int>.toInt(): Int = this.joinToString("").toInt(2)
fun List<Int>.toLong(): Long = this.joinToString("").toLong(2)

abstract class Packet {
    abstract fun versionSum(): Int
    abstract fun evaluate(): Long
}

class LitPacket(private val version: Int, private val value: Long) : Packet() {
    override fun versionSum(): Int = version
    override fun evaluate(): Long = value
}

class OpPacket(
    private val version: Int,
    private val typeId: Int,
    private val packets: List<Packet>
) : Packet() {
    override fun versionSum(): Int {
        return version + packets.map { it.versionSum() }.sum()
    }

    override fun evaluate(): Long {
        return when (typeId) {
            0 -> packets.map { it.evaluate() }.sum()
            1 -> packets.map { it.evaluate() }.reduce { acc, i -> acc * i }
            2 -> packets.map { it.evaluate() }.minOrNull()!!
            3 -> packets.map { it.evaluate() }.maxOrNull()!!
            5 -> if (packets[0].evaluate() > packets[1].evaluate()) 1 else 0
            6 -> if (packets[0].evaluate() < packets[1].evaluate()) 1 else 0
            7 -> if (packets[0].evaluate() == packets[1].evaluate()) 1 else 0
            else -> -1
        }
    }
}

class DataStream(private val data: List<Int>) {
    private var cursor = 0

    fun getBits(n: Int): List<Int> {
        val ret = data.subList(cursor, cursor + n)
        cursor += n
        return ret
    }

    fun notEmpty(): Boolean = data.drop(cursor).any { it == 1 }
    override fun toString(): String = data.drop(cursor).joinToString("")
}

fun parseStream(stream: DataStream): List<Packet> {
    val ret = mutableListOf<Packet>()
    while (stream.notEmpty()) {
        ret.add(parsePacket(stream))
    }
    return ret
}

// parses one packet out of a stream
fun parsePacket(stream: DataStream): Packet {
    val version = stream.getBits(3).toInt()
    val typeId = stream.getBits(3).toInt()
    return when (typeId) {
        4 -> parseLiteral(stream, version)
        else -> parseOperator(stream, version, typeId)
    }
}

fun parseLiteral(stream: DataStream, version: Int): LitPacket {
    var chunk = stream.getBits(5)
    val ret = mutableListOf<List<Int>>()
    while (true) {
        if (chunk[0] == 0) break
        ret += chunk.drop(1)
        chunk = stream.getBits(5)
    }
    ret += chunk.drop(1)

    val value = ret.flatten().toLong()
    return LitPacket(version, value)
}

fun parseOperator(stream: DataStream, version: Int, typeId: Int): OpPacket {
    val type = stream.getBits(1).single()
    val packets = when (type) {
        0 -> parseStream(DataStream(stream.getBits( stream.getBits(15).toInt())))
        else -> buildList {
            val length = stream.getBits(11).toInt()
            repeat(length) {
                add(parsePacket(stream))
            }
        }
    }
    return OpPacket(version, typeId, packets)
}

fun main() {
    val input = File("input.txt").bufferedReader().readLine().toList().map {
        it.toBinary()
    }.flatten().toMutableList()

    val out = parsePacket(DataStream(input))

    println("${out.versionSum()}")
    println("${out.evaluate()}")
}



