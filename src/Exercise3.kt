import java.util.*
import java.util.concurrent.Semaphore

/**
 * Implement a cyclic barrier just using semaphores.
 *
 * @see https://en.wikipedia.org/wiki/Barrier_(computer_science)
 */
fun main(args: Array<String>) {
    val threads = mutableListOf<Thread>()
    for (i in 1..THREAD_NUMBER) {
        threads.add(WorkThread(i))
    }
    threads.forEach { it.start() }
}

const val THREAD_NUMBER = 100
private val random = Random()
private val barrier = SemaphoreCyclicBarrier(THREAD_NUMBER)
private val threadExecutions = Vector<Int>()

private data class WorkThread(private val id: Int) : Thread(id.toString()) {
    private val repeat = 1000

    override fun run() {
        repeat(repeat) { work(); barrier.await() }
        //Wait for the last threads to finish
        Thread.sleep(100)
        checkChunkedData()
    }

    private fun work() {
        println("Thread $id Start")
        Thread.sleep(random.nextInt(1).toLong())
        threadExecutions.add(id)
    }
}

private data class SemaphoreCyclicBarrier(private val expectedThreads: Int) {
    private val awaitSemaphore = Semaphore(1)
    private var barrierSemaphore = mutableListOf<Semaphore>()
            .apply { repeat(expectedThreads) { add(Semaphore(0)) } }
    private var threadsWaiting = 0

    fun await() {
        awaitSemaphore.acquire()
        val index = threadsWaiting
        threadsWaiting += 1
        val shouldReleaseBarrier = threadsWaiting == expectedThreads
        if (shouldReleaseBarrier) threadsWaiting = 0
        awaitSemaphore.release()

        if (shouldReleaseBarrier) {
            barrierSemaphore.forEach { it.release() }
        }
        barrierSemaphore[index].acquire()
    }
}

private fun checkChunkedData() {
    //Each execution should generate an iteration of threads from 1 to N
    val expectedNumberPerChunk = mutableListOf<Int>()
    for (i in 1..THREAD_NUMBER) {
        expectedNumberPerChunk.add(i)
    }
    threadExecutions.windowed(THREAD_NUMBER, THREAD_NUMBER)
            .forEach { check(it.containsAll(expectedNumberPerChunk)) }
}