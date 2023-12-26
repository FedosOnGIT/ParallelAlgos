package bfs

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.min

/**
 * @author nadutkinfedor
 */
object ArrayExtensions {
    suspend inline fun parallelFor(
        start: Int,
        end: Int,
        crossinline action: suspend (Int) -> Unit,
        block: Int
    ) {
        val size = end - start
        if (size <= block) {
            for (i in start..<end) {
                action(i)
            }
            return
        }

        coroutineScope {
            val step = size / 4
            for (part in start..<end step step) {
                launch {
                    for (i in part..<minOf(end, part + step)) {
                        action(i)
                    }
                }
            }
        }
    }

    suspend fun IntArray.parallelScan(
        start: Int,
        end: Int,
        block: Int = 100
    ): IntArray {
        val size = end - start
        val scan = IntArray(size + 1) { 0 }

        if (size < block) {
            for (index in 0..<size) {
                scan[index + 1] = scan[index] + get(start + index)
            }
            return scan
        }

        val plusesNumber = size / block + (if (size % block == 0) 0 else 1)
        val pluses = IntArray(plusesNumber) { 0 }
        parallelFor(
            start = 0,
            end = plusesNumber,
            action = {
                var plus = 0
                for (index in block * it..<min(block * (it + 1), size)) {
                    plus += get(index + start)
                    scan[index + 1] = plus
                }
                pluses[it] = plus
            },
            block = block
        )
        val scannedPluses = pluses.parallelScan(
            start = 0,
            end = plusesNumber,
            block = block
        )
        parallelFor(
            0,
            end = plusesNumber,
            action = {
                for (index in block * it..<min(block * (it + 1), size)) {
                    scan[index + 1] += scannedPluses[it]
                }
            },
            block = block
        )
        return scan
    }

    suspend inline fun IntArray.parallelMap(
        start: Int,
        end: Int,
        default: Int,
        crossinline map: (Int) -> Int,
        block: Int = 100
    ): IntArray {
        val mapped = IntArray(end - start) { default }
        parallelFor(
            start = start,
            end = end,
            action = { index -> mapped[index - start] = map(get(index)) },
            block = block
        )
        return mapped
    }

    suspend inline fun IntArray.parallelFilter(
        start: Int,
        end: Int,
        crossinline predicate: (Int) -> Boolean,
        block: Int = 100
    ): IntArray {
        val predicated = parallelMap(start = start, end = end, default = 0, map = {
            if (predicate(it)) {
                1
            } else {
                0
            }
        }, block = block)
        val finish = end - start
        val indexes = predicated.parallelScan(start = 0, end = finish, block = block)
        val filtered = IntArray(indexes.last()) { get(start) }
        parallelFor(
            start = 0, end = finish, action = {
                if (predicated[it] == 1) {
                    val index = indexes[it + 1]
                    filtered[index - 1] = get(it + start)
                }
            }, block = block
        )
        return filtered
    }
}