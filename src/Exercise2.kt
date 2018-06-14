/**
 * Implement the Lamport's bakery algorithm without use any atomic hardware operation(locks, synchronized...)
 *
 * [Lamport Algorithm]
 * Lamport envisioned a bakery with a numbering machine at its entrance so each customer is given a unique number.
 * Numbers increase by one as customers enter the store. A global counter displays the number of the customer that is currently being served.
 * All other customers must wait in a queue until the baker finishes serving the current customer and the next number is displayed.
 * When the customer is done shopping and has disposed of his or her number, the clerk increments the number, allowing the next customer to be served.
 * That customer must draw another number from the numbering machine in order to shop again.
 *
 * @see https://en.wikipedia.org/wiki/Lamport's_bakery_algorithm
 * @see https://en.wikipedia.org/wiki/Mutual_exclusion
 */
fun main(args: Array<String>) {

}