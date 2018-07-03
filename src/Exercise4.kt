import java.util.concurrent.Semaphore

/**
 * Implement a read-write lock using semaphores.
 *
 * @see https://en.wikipedia.org/wiki/Readers%E2%80%93writer_lock
 */

fun main(args: Array<String>) {
    val numberOfReadersAndWriters = 5
    repeat(1000000) {
        val people = mutableListOf<Thread>()
        (0 until numberOfReadersAndWriters).forEach {
            people.addAll(listOf(Writer(), Reader()))
        }
        //people.shuffled(random).forEach { it.start() }
        people.forEach { it.start() }
    }
}

private var readingVariable = 5
private val rwLock = RWLock()

private class Reader : Thread() {
    override fun run() {
        rwLock.readLock()

        println("Reader $id start reading")
        //Thread.sleep(random.nextInt(500).toLong())
        println("Reader $id check $readingVariable and end reading")

        rwLock.readUnlock()
    }
}

private class Writer : Thread() {
    override fun run() {
        rwLock.writeLock()

        println("Writer $id start to write")
        //Thread.sleep(random.nextInt(500).toLong())
        readingVariable++
        println("Writer $id end writing and the new value is $readingVariable")

        rwLock.writeUnlock()
    }
}

private class RWLock {
    private val readSemaphore = Semaphore(1)
    private val writeSemaphore = Semaphore(1)
    private var currentlyReading = 0
    private var isWriting = false

    fun readLock() {
        readSemaphore.acquire()
        currentlyReading++
        if (currentlyReading == 1)
            writeSemaphore.acquire()
        readSemaphore.release()
    }

    fun readUnlock() {
        readSemaphore.acquire()
        currentlyReading--
        if (currentlyReading == 0)
            writeSemaphore.release()
        readSemaphore.release()
    }

    fun writeLock() {
        writeSemaphore.acquire()
        isWriting = true
    }

    fun writeUnlock() {
        isWriting = false
        writeSemaphore.release()
    }
}
