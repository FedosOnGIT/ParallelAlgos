package bfs

/**
 * @author nadutkinfedor
 */

abstract class Graph() {
    abstract val size: Int
    abstract operator fun get(index: Int): Node
}
class CubeGraph(private val side: Int) : Graph() {
    override val size = side * side * side

    private fun createIndex(x: Int, y: Int, z: Int, limit: Int): Int {
        if (x < 0 || x >= limit) {
            return -1
        }
        if (y < 0 || y >= limit) {
            return -1
        }
        if (z < 0 || z >= limit) {
            return -1
        }
        return x * limit * limit + y * limit + z
    }

    override fun get(index: Int): Node {
        val x = index / (side * side)
        val y = index / side % side
        val z = index % side
        val candidates = listOf(
            createIndex(x - 1, y, z, side),
            createIndex(x + 1, y, z, side),
            createIndex(x, y - 1, z, side),
            createIndex(x, y + 1, z, side),
            createIndex(x, y, z - 1, side),
            createIndex(x, y, z + 1, side)
        )
        val neighbours = mutableListOf<Int>()
        for (candidate in candidates) {
            if (candidate != -1) {
                neighbours.add(candidate)
            }
        }
        return Node(
            x = x,
            y = y,
            z = z,
            neighbours = neighbours
        )
    }
}