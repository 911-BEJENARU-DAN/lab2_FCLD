package FA

import java.io.File

class FA (filename: String) {
    companion object {
        private fun charsToString(chars: List<String>?) = chars?.joinToString("")
        private fun stringToChars(word: String) = word.map { it.toString() }
    }

    private lateinit var states: List<String>
    private lateinit var initState: String
    private lateinit var outStates: List<String>
    private lateinit var transitions: List<Transition>
    private lateinit var alphabet: List<String>

    init {
        for (line in File(filename).readLines()) {
            when (Regex("^([a-z_]*)=").find(line)?.groups?.get(1)?.value) {
                "states" -> this.states = parseStates(line)
                "alphabet" -> this.alphabet = parseAlphabet(line)
                "out_states" -> this.outStates = parseOutStates(line)
                "in_state" -> this.initState = parseInitState(line)
                "transitions" -> this.transitions = parseTransitions(line)
                null -> throw Exception("Invalid line in finite automaton")
                else -> throw Exception("Invalid line in finite automaton")
            }
        }
    }

    fun parseStates(line: String): List<String> {
        val curlyParenthesesStates = line.substringAfter('=')
        val states = curlyParenthesesStates.trim().slice(1 until curlyParenthesesStates.length - 1).trim()
        return states.split(Regex(", *"))
    }

    fun parseAlphabet(line: String): List<String> {
        val curlyParenthesesAlphabet = line.substringAfter('=')
        val alphabet = curlyParenthesesAlphabet.trim().slice(1 until curlyParenthesesAlphabet.length - 1).trim()
        return alphabet.split(Regex(", *"))
    }

    fun parseOutStates(line: String): List<String> {
        val curlyParenthesesStates = line.substringAfter('=')
        val outStates = curlyParenthesesStates.trim().slice(1 until curlyParenthesesStates.length - 1).trim()
        return outStates.split(Regex(", *"))
    }

    fun parseInitState(line: String): String {
        return line.substringAfter('=').trim()
    }

    fun parseTransitions(line: String): List<Transition> {
        val parenthesesTransitions = line.substringAfter('=')
        val transitions = parenthesesTransitions.trim().slice(1 until parenthesesTransitions.length - 1).trim()
        val splitTransitions = transitions.split(Regex("; *"))
        val determinedTransitions = mutableListOf<Transition>()
        for (transition in splitTransitions) {
            val transitionWithoutParentheses = transition.slice(1 until transition.length - 1).trim()
            val independentValues = transitionWithoutParentheses.split(Regex(", *"))
            determinedTransitions.add(
                Transition(
                    independentValues[0],
                    independentValues[1],
                    independentValues[2]
                )
            )
        }
        return determinedTransitions
    }

    private fun displayStrings(nameOfList: String, strings: List<String>) {
        print("list = {")
        for ((index, state) in strings.withIndex()) {
            if (index + 1 == strings.size) {
                print(state);
            }
            else {
                print("$state, ")
            }
        }
        print("}")
        println()
    }

    fun displayStates() {
        displayStrings("states", states)
        println()
    }

    fun displayAlphabet() {
        displayStrings("alphabet", alphabet)
        println()
    }

    fun displayOutStates() {
        displayStrings("outStates", outStates)
        println()
    }

    fun displayInStates() {
        println("inState=$initState")
        println()
    }

    fun displayTransitions() {
        print("transitions = { ")
        for ((index, transition) in transitions.withIndex()) {
            print("(${transition.from}, ${transition.to}, ${transition.tag})")
            if (index + 1 != transitions.size) {
                print("; ")
            }
        }
        println()
    }

    fun isValid(word: List<String>): Boolean {
        var state = initState
        for (character in word) {
            var newState: String? = null
            for (transition in transitions) {
                if (transition.from == state && transition.tag == character) {
                    newState = transition.to
                    break
                }
            }

            if (newState == null) {
                return false;
            }
            state = newState
        }
        return state in outStates;
    }

    fun isValid(word: String): Boolean = isValid(stringToChars(word))

    fun nextValid(word: List<String>): List<String>? {
        var state = initState
        val validWord = mutableListOf<String>()
        for (character in word) {
            var newState: String? = null
            for (transition in transitions) {
                if (transition.from == state && transition.tag == character) {
                    newState = transition.to
                    break
                }
            }

            if (newState == null) {
                break
            }

            state = newState
            validWord.add(character)
        }

        if (state !in outStates) {
            return null
        }

        return validWord
    }

    fun nextValid(word: String): String? = charsToString(nextValid(stringToChars(word)))
}