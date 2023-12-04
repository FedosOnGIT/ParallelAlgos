package quicksort

import kotlin.random.Random
import kotlin.system.measureTimeMillis

/**
 * @author nadutkinfedor
 */
class Benchmark {
    companion object {
        fun benchmark(block: Int) {
            val size = 1e8.toInt()
            var sequentialTime = 0L
            var parallelTime = 0L
            val experiments = 5
            for (experiment in 1..experiments) {
                println("Starting experiment $experiment")
                println("---------------------------------")
                val sequentialArray = IntArray(size) { Random.nextInt() }
                val parallelArray = sequentialArray.clone()

                val sequential = QuickSort(Random(experiment))
                val sequentialExecutionTime = measureTimeMillis {
                    sequential.sort(sequentialArray, 0, size)
                }
                println("Experiment number $experiment. Sequential time = ${sequentialExecutionTime.toDouble() / 1000}s")
                val parallel = ParallelQuickSort(Random(experiment), block = block)
                val parallelExecutionTime = measureTimeMillis {
                    parallel.parallelSort(parallelArray, 0, size)
                }
                parallel.shutdown()
                println("Experiment number $experiment. Parallel time = ${parallelExecutionTime.toDouble() / 1000}s")
                sequentialTime += sequentialExecutionTime
                parallelTime += parallelExecutionTime
            }
            println("---------------------------------")
            println("Average sequential time = ${sequentialTime.toDouble() / experiments / 1000}s, " +
                    "average parallel time = ${parallelTime.toDouble() / experiments / 1000}s")
        }

    }
}