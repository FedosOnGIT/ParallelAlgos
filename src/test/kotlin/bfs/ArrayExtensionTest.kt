package bfs

import bfs.ArrayExtensions.parallelFilter
import bfs.ArrayExtensions.parallelFor
import bfs.ArrayExtensions.parallelMap
import bfs.ArrayExtensions.parallelScan
import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author nadutkinfedor
 */
class ArrayExtensionTest {
    @Test
    fun parallelForTest() {
        val executors = Executors.newFixedThreadPool(4)
        val dispatcher = executors.asCoroutineDispatcher()
        for (block in arrayOf(10, 100, 1000)) {
            val array = IntArray(1000000) { i -> i }
            val result = IntArray(1000000) { 0 }
            runBlocking {
                withContext(dispatcher) {
                    parallelFor(
                        start = 0,
                        end = 1000000,
                        action = {
                            result[it] = -array[it]
                        },
                        block = block
                    )
                }
            }
            for (index in 0..<1000000) {
                assertEquals(-array[index], result[index])
            }
        }
        executors.shutdown()
    }

    @Test
    fun parallelScanTest() {
        val executors = Executors.newFixedThreadPool(4)
        val dispatcher = executors.asCoroutineDispatcher()
        for (block in arrayOf(10, 100, 1000)) {
            val array = IntArray(1000000) { 1 }
            var result: IntArray
            runBlocking {
                withContext(dispatcher) {
                    result = array.parallelScan(
                        start = 0,
                        end = 1000000,
                        block = block
                    )
                }
            }
            assertEquals(1000001, result.size)
            for (index in 0..1000000) {
                assertEquals(index, result[index])
            }
        }
        executors.shutdown()
    }

    @Test
    fun parallelMapTest() {
        val executors = Executors.newFixedThreadPool(4)
        val dispatcher = executors.asCoroutineDispatcher()
        for (block in arrayOf(10, 100, 1000)) {
            val array = IntArray(1000000) { 1 }
            var result: IntArray
            runBlocking {
                withContext(dispatcher) {
                    result = array.parallelMap(
                        start = 0,
                        end = 1000000,
                        default = 0,
                        map = { it * -1 },
                        block = block
                    )
                }
            }
            assertEquals(1000000, result.size)
            for (index in 0..<1000000) {
                assertEquals(array[index] * -1, result[index])
            }
        }
        executors.shutdown()
    }

    @Test
    fun parallelFilterTest() {
        val executors = Executors.newFixedThreadPool(4)
        val dispatcher = executors.asCoroutineDispatcher()
        for (block in arrayOf(10, 100, 1000)) {
            val array = IntArray(1000000) { i -> i }
            var result: IntArray
            runBlocking {
                withContext(dispatcher) {
                    result = array.parallelFilter(
                        start = 0,
                        end = 1000000,
                        predicate = { it % 2 == 0 },
                        block = block
                    )
                }
            }
            val filtered: Set<Int> = result.toHashSet()
            for (value in 0..<1000000) {
                if (value % 2 == 0) {
                    assertTrue(filtered.contains(value))
                } else {
                    assertFalse(filtered.contains(value))
                }
            }
        }
        executors.shutdown()
    }
}