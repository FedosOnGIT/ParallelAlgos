package bfs

import java.util.concurrent.atomic.AtomicIntegerArray
import bfs.ArrayExtensions.parallelFilter
import bfs.ArrayExtensions.parallelFor
import bfs.ArrayExtensions.parallelMap
import bfs.ArrayExtensions.parallelScan
import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * @author nadutkinfedor
 */
class ParallelBFS(
    private val block: Int = 100) : BFS() {
    private val executors = Executors.newFixedThreadPool(4)
    private val dispatcher = executors.asCoroutineDispatcher()
    override fun bfs(graph: Graph, start: Int): IntArray {
        var depth: IntArray

        runBlocking {
            withContext(dispatcher) {
                val result = async { parallelBFS(graph, start) }
                depth = result.await()
            }
        }
        return depth
    }

    private suspend fun parallelBFS(graph: Graph, start: Int): IntArray {
        val depth = IntArray(graph.size) { 0 }
        val used = AtomicIntegerArray(graph.size)
        used.set(start, 1)
        var processing = intArrayOf(start)
        while (processing.isNotEmpty()) {
            val processingNumber = processing.size
            var degrees = processing.parallelMap(
                start = 0,
                end = processingNumber,
                default = 0,
                map = { graph[it].neighbours.size },
                block = block
            )
            degrees = degrees.parallelScan(
                start = 0,
                end = processingNumber,
                block = block
            )
            val futureProcessingNumber = degrees.last()
            val futureProcessing = IntArray(futureProcessingNumber) { -1 }
            parallelFor(
                start = 0,
                end = processingNumber,
                action = {
                    val position = processing[it]
                    val node = graph[position]
                    val neighbours = node.neighbours
                    for (index in neighbours.indices) {
                        val neighbour = neighbours[index]
                        if (used.compareAndSet(neighbour, 0, 1)) {
                            futureProcessing[degrees[it] + index] = neighbour
                            depth[neighbour] = depth[position] + 1
                        }
                    }
                },
                block = block
            )
            processing = futureProcessing.parallelFilter(
                start = 0,
                end = futureProcessingNumber,
                predicate = {
                    it >= 0
                },
                block = block
            )
        }
        return depth
    }

    fun shutdown() {
        executors.shutdown()
    }
}