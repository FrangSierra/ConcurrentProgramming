/**
 * Implement the Lamport's bakery algorithm without use any atomic hardware operation(locks, synchronized...)
 *
 * [Lamport Algorithm]
 * Lamport envisioned a bakery with a currentCustomering machine at its entrance so each customer is given a unique ticket.
 * currentCustomers increase by one as customers enter the store. A global counter displays the ticket of the customer that is currently being served.
 * All other customers must wait in a queue until the baker finishes serving the current customer and the next ticket is displayed.
 * When the customer is done shopping and has disposed of his or her ticket, the clerk increments the ticket, allowing the next customer to be served.
 * That customer must draw another ticket from the currentCustomering machine in order to shop again.
 *
 * @see https://en.wikipedia.org/wiki/Lamport's_bakery_algorithm
 * @see https://en.wikipedia.org/wiki/Mutual_exclusion
 */
fun main(args: Array<String>) {
    val numberOfClients = 10
    (0 until numberOfClients).mapTo(clients) { Customer(it) }
    clients.forEach { it.shopping() }
}

val clients = mutableListOf<Customer>()
var currentNumberMachine = 0

data class Customer(private val id: Int,
                    private var ticket: Int = 0,
                    private var pickingNumber: Boolean = false) : Thread() {

    fun shopping() {
        start()
    }

    private fun pickNumber() {
        currentNumberMachine += 1
        ticket = currentNumberMachine
        println("Customer $id  took the number $ticket")
    }

    override fun run() {
        //Pick the number
        pickingNumber = true
        pickNumber()
        pickingNumber = false

        //Check the turn with every customer
        clients.forEach {
            while (it.pickingNumber); //Wait if the user is picking a number

            while (it.ticket != 0  //Customer didn't finished buying. Ticket = 0
                    && (it.ticket < ticket  //The customer priority is bigger than ours
                    || (it.ticket == ticket && it.id < id))); //The priority is the same but our identifier is bigger
        }
        //Critical section
        println("Customer $id with number $ticket -> Good afternoon Baker, I would like a load of bread")
        sleep(1000)
        println("Baker -> There you go")
        println("Baker -> NEXT")
        ticket = 0
    }
}