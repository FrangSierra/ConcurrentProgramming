/**
 * Print all the possibilities that the next two processes compounded by three instructions each can
 * be executed if both process are executed at the same time.
 * -------------------------------
 * [Process 1] => x = 2 * x
 * => LOAD R1, X
 * => MULT R1, #2
 * => STORE R1, X
 * -------------------------------
 * [Process 2] => x = x + 1
 * => LOAD R2, X
 * => ADD R2, #1
 * => STORE R2, X
 * --------------------------------
 * @see https://en.wikipedia.org/wiki/Permutation
 * @see https://en.wikipedia.org/wiki/Combination
 */
fun main(args: Array<String>) {

    val p1: Process = mutableListOf(
            Instruction("LOAD R1, X"),
            Instruction("MULT R1, #2"),
            Instruction("STORE R1, X")
    )
    val p2: Process = mutableListOf(
            Instruction("LOAD R2, X"),
            Instruction("ADD R2, #1"),
            Instruction("STORE R2, X")
    )

    val totalItems = p1.size + p2.size
    val selection = p1.size

    print(combination(totalItems,selection))
    println()
    printPermutation(p1, p2)
}

/**
 * Calculates and print the permutation of two given lists of instructions.
 *
 * Permutation relates to the act of arranging all the members of a set into some
 * sequence or order, or if the set is already ordered, rearranging (reordering) its elements,
 * a process called permuting.
 * These differ from combinations, which are selections of some members of a set where order is disregarded
 *
 * @param p1 First list of instructions.
 * @param p2 Second list of instructions.
 */
private fun printPermutation(p1: Process, p2: Process, accumulator: Int = 0) {
    if (accumulator == p1.size) {  //stop condition for the recursion
        println("$p1")
        return
    }
    p2.forEach { ins ->
        p1[accumulator] = ins
        printPermutation(p1, p2, accumulator.plus(1))
    }
}

/**
 * Calculates the factorial of a number.
 * The factorial of a non-negative integer n, denoted by n!,
 * is the product of the multiplication all positive integers less than or equal to n.
 *
 * Formula:
 * n! = n × (n-1)!
 * @param n The number to calculate the factorial.
 */
private tailrec fun factorial(n: Int, accumulator: Int = 1): Int {
    if (n < 0) throw Exception("No negative numbers pls")
    return if (n == 1) accumulator else factorial(n-1, accumulator *n)
}

/**
 * Calculates the combinatorial for a selection of items from a collection.
 * Combination is the number of ways of picking m unordered outcomes from n possibilities(Binomial coefficient)
 * Formula:
 * n!/(n-m)!*n!
 * @param n The number of total items from a collection.
 * @param m The selection of items from the collection.
 */
fun combination(n: Int, m: Int): Int {
    if (n < 0 || m < 0) throw Exception("No negative numbers pls")
    return if (m == 0 || n == m) 1
    else if (m == 1) n
    else {
        val a = factorial(n)
        val b = factorial(n - m) * factorial(m)
        a / b
    }
}

private typealias Process = MutableList<Instruction>
private data class Instruction(private val instruction: String){
    override fun toString(): String = instruction
}