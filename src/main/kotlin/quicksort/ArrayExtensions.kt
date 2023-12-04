package quicksort

/**
 * @author nadutkinfedor
 */
object ArrayExtensions {
    fun IntArray.swap(firstIndex: Int, secondIndex: Int) {
        val temporary = this[firstIndex]
        this[firstIndex] = this[secondIndex]
        this[secondIndex] = temporary
    }

    fun IntArray.partition(index: Int, left: Int, right: Int): Int {
        val value = this[index]
        var start = left
        var end = right - 1
        while (start <= end) {
            while (this[start] < value) {
                start++
            }
            while (this[end] > value) {
                end--
            }
            if (start == end) {
                break
            }
            if (start < end) {
                this.swap(start++, end--)
            }
        }
        return start
    }
}