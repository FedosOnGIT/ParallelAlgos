package bfs

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author nadutkinfedor
 */
class BFSTest {
    @Test
    fun sequentialBFSTest() {
        val graph = CubeGraph(100)
        val depths = BFS().bfs(graph = graph, start = 0)
        for (index in 0..<graph.size) {
            val node = graph.get(index)
            assertEquals(node.x + node.y + node.z, depths[index])
        }
    }

    @Test
    fun parallelBFSTest() {
        val graph = CubeGraph(100)
        val depths = ParallelBFS().bfs(graph = graph, start = 0)
        for (index in 0..<graph.size) {
            val node = graph.get(index)
            assertEquals(node.x + node.y + node.z, depths[index])
        }
    }
}