package FA

fun readCharacters(): List<String> {
    print("Characters length: ")
    val nr = readln().toInt()
    val characters = mutableListOf<String>()
    for (index in 1..nr){
        print("enter: ")
        val character = readlnOrNull()
        characters.add(character!!)
    }
    return characters
}

fun displayMenu() {
    println("1. Display alphabet.")
    println("2. Display states.")
    println("3. Display input state.")
    println("4. Display output states.")
    println("5. Display transitions.")
    println("6. Verify word of different dimensions.")
    println("7. Verify word of length 1 characters.")
    println("8. Get matching different dimension substring.")
    println("9. Get matching substring of length 1.")
    println("0. Exit.")
    println()
}

fun main() {
    val fa = FA("src/main/resources/fa.in")

    while(true) {
        displayMenu()
        when(readln().toInt()){
            0->break
            1->fa.displayAlphabet()
            2->fa.displayStates()
            3->fa.displayInStates()
            4->fa.displayOutStates()
            5->fa.displayTransitions()
            6->println(fa.isValid(readCharacters()))
            7->println(fa.isValid(readln()))
            8->{
                val characters = readCharacters()
                val validWord = fa.nextValid(characters)
                if (validWord != null) {
                    for(character in validWord) {
                        print(character)
                    }
                    println()
                }
                else {
                    print("Prefix not matching.\n")
                }
            }
            9->{
                val validWord = fa.nextValid(readln())
                if (validWord != null) {
                    for (character in validWord) {
                        print(character)
                    }
                    println()
                }
                else {
                    print("Prefix not matching.")
                    println()
                }
            }
            else->{println("The option doesn't exist.\n")}
        }

    }
}