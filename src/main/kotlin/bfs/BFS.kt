package bfs

/**
 * @author nadutkinfedor
 */
open class BFS {
    open fun bfs(graph: Graph, start: Int): IntArray {
        val used = BooleanArray(graph.size) { false }
        val depth = IntArray(graph.size) { 0 }
        used[start] = true
        val queue = ArrayDeque<Int>()
        queue.add(start)
        while (!queue.isEmpty()) {
            val index = queue.removeFirst()
            val node = graph[index]
            for (neighbour in node.neighbours) {
                if (!used[neighbour]) {
                    used[neighbour] = true
                    depth[neighbour] = depth[index] + 1
                    queue.add(neighbour)
                }
            }
        }
        return depth
    }
}