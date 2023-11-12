import java.io.File;

fun getBackCode(scanner: Scanner, tokens: List<String>) {
    for (pair in scanner.pif) {
        if (pair.first !in listOf("stringConstant", "intConstant", "identifier")) {
            print("${pair.first} ");
        }
        else if (pair.first == "identifier") {
            print("${scanner.symTable.getId(pair.second.first, pair.second.second)} ");
        }
        else if (pair.first == "intConstant") {
            print("${scanner.symTable.getIntConst(pair.second.first, pair.second.second)} ");
        }
        else if (pair.first == "stringConstant") {
            print("\"${scanner.symTable.getStringConst(pair.second.first, pair.second.second)}\" ");
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
            val token = pair.first
            val position = pair.second
            w.println("$token (${position.first}, ${position.second})")
        }
    }

    File("src/main/resources/st.out").printWriter().use { w ->
        w.println("Symbol table size: ${scanner.symTable.size}\n");
        w.println("Identifiers: ");
        for (i in 0 until scanner.symTable.size) {
            for (j in 0 until scanner.symTable.idHashTable.containers[i].size) {
                w.println("$i, $j: ${scanner.symTable.idHashTable.containers[i][j]}");
            }
        }

        w.println();
        w.println("Integer constants: ");
        for (i in 0 until scanner.symTable.size) {
            for (j in 0 until scanner.symTable.intConstHashTable.containers[i].size) {
                w.println("$i, $j: ${scanner.symTable.intConstHashTable.containers[i][j]}");
            }
        }

        w.println();
        w.println("String constants: ");
        for (i in 0 until scanner.symTable.size) {
            for (j in 0 until scanner.symTable.stringConstHashTable.containers[i].size) {
                w.println("$i, $j: ${scanner.symTable.stringConstHashTable.containers[i][j]}");
            }
        }
    }
}