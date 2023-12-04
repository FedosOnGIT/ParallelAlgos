package quicksort

import quicksort.ArrayExtensions.partition
import kotlin.random.Random


/**
 * @author nadutkinfedor
 */
open class QuickSort(val random: Random) {
    open fun sort(array: IntArray, left: Int, right: Int) {
        if (right - left <= 1) {
            return
        }
        val middle = array.partition(random.nextInt(left, right), left, right)
        this.sort(array = array, left = left, right = middle)
        this.sort(array = array, left = middle, right = right)
    }
}