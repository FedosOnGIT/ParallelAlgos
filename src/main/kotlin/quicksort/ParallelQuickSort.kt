package quicksort

import quicksort.ArrayExtensions.partition
import java.util.concurrent.ExecutorService
import kotlin.random.Random
import kotlinx.coroutines.*
import java.util.concurrent.Executors

/**
 * @author nadutkinfedor
 */
class ParallelQuickSort(random: Random, private val block: Int = 1_000): QuickSort(random = random) {
    private val dispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()

    fun parallelSort(array: IntArray, left: Int, right: Int) {
        runBlocking {
            withContext(dispatcher) {
                sortParallel(array, left, right)
            }
        }
    }

    private suspend fun sortParallel(array: IntArray, left: Int, right: Int) {
        if (right - left < block) {
            super.sort(array = array, left = left, right = right)
            return
        }
        val middle = array.partition(
            index = random.nextInt(left, right),
            left = left,
            right = right
        )
        withContext(dispatcher) {
            launch { sortParallel(array = array, left = left, right = middle) }
            sortParallel(array = array, left = middle, right = right)
        }
    }

    fun shutdown() {
        (dispatcher.executor as? ExecutorService)?.shutdown()
    }
}