package bfs

import kotlin.system.measureTimeMillis

/**
 * @author nadutkinfedor
 */
class Benchmark {
    companion object {
        fun benchmark(block: Int) {
            val graph = CubeGraph(500)
            var sequentialTime = 0L
            var parallelTime = 0L
            val experiments = 5
            for (experiment in 1..experiments) {
                println()
                println("---------------------------------")
                println()

                val sequential = BFS()
                val sequentialExecutionTime = measureTimeMillis {
                    sequential.bfs(graph, 0)
                }
                println("*Experiment number $experiment*. Sequential time = ${sequentialExecutionTime.toDouble() / 1000}s")
                val parallel = ParallelBFS(block = block)
                val parallelExecutionTime = measureTimeMillis {
                    parallel.bfs(graph, 0)
                }
                parallel.shutdown()
                println("*Experiment number $experiment*. Parallel time = ${parallelExecutionTime.toDouble() / 1000}s")
                sequentialTime += sequentialExecutionTime
                parallelTime += parallelExecutionTime
            }
            println()
            println("---------------------------------")
            println()
            println("**Average sequential time = ${sequentialTime.toDouble() / experiments / 1000}s, " +
                    "average parallel time = ${parallelTime.toDouble() / experiments / 1000}s**")
            println()
            println("---------------------------------")
            println()
        }

    }
}