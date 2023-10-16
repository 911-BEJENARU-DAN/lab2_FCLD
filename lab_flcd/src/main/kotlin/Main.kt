fun main(args: Array<String>) {
    println("Hello World!");
    val s = SymbolTable();
    s.addId("symbol");
    println(s.idExists("symbol"));
}