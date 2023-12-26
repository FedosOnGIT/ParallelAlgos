fun main() {
    runQuicksort()
    runBFS()
}

fun runQuicksort() {
    println("Working with block 1_000")
    quicksort.Benchmark.benchmark(block = 1_000)
    println("---------------------------------")
    println("Working with block 10_000")
    quicksort.Benchmark.benchmark(block = 10_000)
    println("---------------------------------")
    println("Working with block 100_000")
    quicksort.Benchmark.benchmark(block = 100_000)
    println("---------------------------------")
    println("Working with block 1_000_000")
    quicksort.Benchmark.benchmark(block = 1_000_000)
    println("---------------------------------")
}

fun runBFS() {
    println("# Working with block 100")
    bfs.Benchmark.benchmark(block = 100)
    println("# Working with block 1_000")
    bfs.Benchmark.benchmark(block = 1000)
    println("# Working with block 10_000")
    bfs.Benchmark.benchmark(block = 10000)
}