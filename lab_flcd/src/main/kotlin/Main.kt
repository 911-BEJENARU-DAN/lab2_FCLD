import java.io.File;

fun getBackCode(scanner: Scanner, tokens: List<String>) {
    for (pair in scanner.pif) {
        if (pair.first >= 0) {
            print("${tokens[pair.first]} ");
        }
        else if (pair.first == -1) {
            print("${scanner.symbolTable.getId(pair.second.first, pair.second.second)} ");
        }
        else if (pair.first == -2) {
            print("${scanner.symbolTable.getIntConst(pair.second.first, pair.second.second)} ");
        }
        else if (pair.first == -3) {
            print("\"${scanner.symbolTable.getStringConst(pair.second.first, pair.second.second)}\" ");
        }
    }
    println();
}

fun main(args: Array<String>) {
    val program = File("src/main/resources/p1err.in").readText();
    val tokens = File("src/main/resources/token.in").readLines();
    val scanner = Scanner(program, tokens);
    try{
        scanner.scan();
    }
    catch(exception: ScannerException) {
        println("${exception.message}\nline: ${exception.errorCode}");
        return;
    }
    getBackCode(scanner, tokens);
    File("src/main/resources/pif.out").printWriter().use { w ->
        for (pair in scanner.pif) {
            w.println("${pair.first} (${pair.second.first}, ${pair.second.second})");
        }
    }

    File("src/main/resources/st.out").printWriter().use { w ->
        w.println("Symbol table size: ${scanner.symbolTable.size}\n");
        w.println("Identifiers: ");
        for (i in 0 until scanner.symbolTable.size) {
            for (j in 0 until scanner.symbolTable.idHashTable.containers[i].size) {
                w.println("$i, $j: ${scanner.symbolTable.idHashTable.containers[i][j]}");
            }
        }

        w.println();
        w.println("Integer constants: ");
        for (i in 0 until scanner.symbolTable.size) {
            for (j in 0 until scanner.symbolTable.intConstHashTable.containers[i].size) {
                w.println("$i, $j: ${scanner.symbolTable.intConstHashTable.containers[i][j]}");
            }
        }

        w.println();
        w.println("String constants: ");
        for (i in 0 until scanner.symbolTable.size) {
            for (j in 0 until scanner.symbolTable.stringConstHashTable.containers[i].size) {
                w.println("$i, $j: ${scanner.symbolTable.stringConstHashTable.containers[i][j]}");
            }
        }
    }
}