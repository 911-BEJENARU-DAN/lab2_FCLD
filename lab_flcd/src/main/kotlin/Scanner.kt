import FA.FA

class Scanner(private val program: String, private val tokens: List<String>) {
    val symTable = SymbolTable();
    val pif = mutableListOf<Pair<String, Pair<Int, Int>>>();
    private var currPos = 0;
    private var currLine = 1;

    private fun jumpSpaces() {
        while (currPos < program.length && program[currPos].isWhitespace()) {
            if (program[currPos] == '\n') {
                currLine++;
            }
            currPos++;
        }
    }

    private fun jumpComments() {
        if (program.startsWith("//", currPos)) {
            while (currPos < program.length && program[currPos] != '\n'){
                currPos++;
            }
            return
        }
    }

    private fun handleStringConstant(): Boolean {
        val regex = Regex("^\"([a-zA-z0-9_ ]*)\"").find(program.substring(currPos));
        if (regex == null) {
            if (Regex("^\"").containsMatchIn(program.substring(currPos)))
                throw ScannerException("Lexical error. Unclosed quotes. ", currLine);
            if (Regex("^\"[^\"]\"").containsMatchIn(program.substring(currPos)))
                throw ScannerException("Lexical error. Invalid characters inside string.  ", currLine);
            return false;
        }

        val stringConst = regex.groups[1]!!.value;
        currPos += stringConst.length + 2;
        val pos = symTable.addStringConst(stringConst);
        pif.add(Pair("stringConstant", pos.pair));
        return true;
    }

    private fun handleIntConstant(): Boolean {
        /*val regex = Regex("^([+-]?[1-9][0-9]*|0)").find(program.substring(index)) ?: return false
        val intConstant = regex.groups[1]!!.value;*/
        val finiteAutomaton = FA("src/main/resources/intConstant.in");
        val intConst = finiteAutomaton.nextValid(program.substring(currPos)) ?: return false;

        if (intConst[0] in listOf('-', '+')
            && pif.size > 0 &&
            pif.last().first in listOf(
                "intConstant",
                "stringConstant",
                "identifier")) {
            return false;
        }

        currPos += intConst.length;
        val intConstVal = intConst.toInt();
        val pos = symTable.addIntConst(intConstVal);
        pif.add(Pair("intConstant", pos.pair));
        return true;
    }

    private fun handleFromTokenList(): Boolean {
        for ((tokenIdx, token) in tokens.withIndex()) {
            if (program.startsWith(token, currPos)) {
                pif.add(Pair(token, Position.nullPosition.pair));
                currPos += token.length;
                return true;
            }
        }
        return false;
    }

    private fun handleIdentifier(): Boolean {
        /*val regex = Regex("^([a-zA-Z_][a-zA-Z0-9_]*)").find(program.substring(index)) ?: return false;
        val identifier = regex.groups[1]!!.value;*/

        val finiteAutomaton = FA("src/main/resources/identifier.in");
        val id = finiteAutomaton.nextValid(program.substring(currPos)) ?: return false

        if (id[0].isDigit()) {
            throw ScannerException("Lexical error. Invalid identifier.", currLine);
        }

        currPos += id.length;
        val pos = symTable.addId(id);
        pif.add(Pair("identifier", pos.pair));
        return true;
    }

    private fun nextToken() {
        jumpSpaces();
        jumpComments();
        if (currPos == program.length) {
            return;
        }

        for (f in listOf(Scanner::handleIntConstant, Scanner::handleStringConstant,
            Scanner::handleFromTokenList, Scanner::handleIdentifier)) {
            if (f(this)) {
                return
            }
        }
        throw ScannerException("Lexical error: Token cannot be classified ", currLine);
    }

    fun scan () {
        while (currPos in program.indices){
            nextToken();
        }
    }

}
