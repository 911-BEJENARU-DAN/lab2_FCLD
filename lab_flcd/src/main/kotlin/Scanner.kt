class Scanner(private val program: String, private val tokens: List<String>) {
    val symbolTable = SymbolTable();
    val pif = mutableListOf<Pair<Int, Pair<Int, Int>>>();
    private var index = 0;
    private var currentLine = 1;

    private fun skipWhiteSpaces() {
        while (index < program.length && program[index].isWhitespace()) {
            if (program[index] == '\n') {
                currentLine++;
            }
            index++;
        }
    }

    private fun skipComments() {
        if (program.startsWith("//", index)) {
            while (index < program.length && program[index] != '\n'){
                index++;
            }
            return
        }
    }

    private fun treatStringConstant(): Boolean {
        val regex = Regex("^\"([a-zA-z0-9_ ]*)\"").find(program.substring(index));
        if (regex == null) {
            if (Regex("^\"").containsMatchIn(program.substring(index)))
                throw ScannerException("Lexical error. Unclosed quotes. ", currentLine);
            if (Regex("^\"[^\"]\"").containsMatchIn(program.substring(index)))
                throw ScannerException("Lexical error. Invalid characters inside string.  ", currentLine);
            return false;
        }

        val stringConstant = regex.groups[1]!!.value;
        index += stringConstant.length + 2;
        val position = symbolTable.addStringConst(stringConstant);
        pif.add(Pair(position.positionType.code, position.pair));
        return true;
    }

    private fun treatIntConstant(): Boolean {
        val regex = Regex("^([+-]?[1-9][0-9]*|0)").find(program.substring(index)) ?: return false
        val intConstant = regex.groups[1]!!.value;
        index += intConstant.length;
        val parsedIntConstant = intConstant.toInt();
        val position = symbolTable.addIntConst(parsedIntConstant);
        pif.add(Pair(position.positionType.code, position.pair));
        return true;
    }

    private fun treatFromTokenList(): Boolean {
        for ((tokenIndex, token) in tokens.withIndex()) {
            if (program.startsWith(token, index)) {
                pif.add(Pair(tokenIndex, Position.nullPosition.pair));
                index += token.length;
                return true;
            }
        }
        return false;
    }

    private fun treatIdentifier(): Boolean {
        val regex = Regex("^([a-zA-Z_][a-zA-Z0-9_]*)").find(program.substring(index)) ?: return false;
        val identifier = regex.groups[1]!!.value;

        if (identifier[0].isDigit()) {
            throw ScannerException("Lexical error. Invalid identifier.", currentLine);
        }

        index += identifier.length;
        val position = symbolTable.addId(identifier);
        pif.add(Pair(position.positionType.code, position.pair));
        return true;
    }

    private fun nextToken() {
        skipWhiteSpaces();
        skipComments();
        if (index == program.length) {
            return;
        }

        for (function in listOf(Scanner::treatIntConstant, Scanner::treatStringConstant,
            Scanner::treatFromTokenList, Scanner::treatIdentifier)) {
            if (function(this)) {
                return
            }
        }
        throw ScannerException("Lexical error: Token cannot be classified ", currentLine);
    }

    fun scan () {
        while (index in program.indices){
            nextToken();
        }
    }

}
