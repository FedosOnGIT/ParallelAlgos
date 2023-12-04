import quicksort.Benchmark

fun main() {
    println("Working with block 1_000")
    Benchmark.benchmark(block = 1_000)
    println("---------------------------------")
    println("Working with block 10_000")
    Benchmark.benchmark(block = 10_000)
    println("---------------------------------")
    println("Working with block 100_000")
    Benchmark.benchmark(block = 100_000)
    println("---------------------------------")
    println("Working with block 1_000_000")
    Benchmark.benchmark(block = 1_000_000)
    println("---------------------------------")
}