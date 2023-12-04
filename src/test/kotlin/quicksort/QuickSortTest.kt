package quicksort


import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author nadutkinfedor
 */
class QuickSortTest {
    private fun assertSorted(
        initialArray: IntArray,
        sortedArray: IntArray,
        exceptionMessage: String?
    ) {
        assertEquals(
            expected = initialArray.size,
            actual = sortedArray.size,
            message = exceptionMessage
        )
        initialArray.sort()
        for (index in initialArray.indices) {
            assertEquals(
                expected = initialArray[index],
                actual = sortedArray[index],
                message = exceptionMessage
            )
        }
    }

    @Test
    fun quickSortTest() {
        val size = 10000
        val experiments = 1000
        for (experiment in 0..experiments) {
            val random = Random(experiment)
            val initialArray = IntArray(size) { random.nextInt() }
            val sortedArray = initialArray.clone()
            val quickSort = QuickSort(random = random)
            quickSort.sort(array = sortedArray, left = 0, right = size)
            assertSorted(
                initialArray = initialArray,
                sortedArray = sortedArray,
                exceptionMessage = "Experiment with seed $experiment failed"
            )
        }
    }

    @Test
    fun parallelQuickSortTest() {
        val size = 10000
        val experiments = 1000
        for (experiment in 0..experiments) {
            val random = Random(experiment)
            val initialArray = IntArray(size) { random.nextInt() }
            val sortedArray = initialArray.clone()
            val quickSort = ParallelQuickSort(random = random)
            quickSort.parallelSort(array = sortedArray, left = 0, right = size)
            assertSorted(
                initialArray = initialArray,
                sortedArray = sortedArray,
                exceptionMessage = "Experiment with seed $experiment failed"
            )
            quickSort.shutdown()
        }
    }
}