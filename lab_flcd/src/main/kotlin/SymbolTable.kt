class SymbolTable(val size: Int = 10) {
    val idHashTable = HashTable<String>(size)
    val intConstHashTable = HashTable<Int>(size)
    val stringConstHashTable = HashTable<String>(size)

    fun idExists(name: String): Pair<Int, Int>? = idHashTable.get(name)

    fun intConstExists(const: Int): Pair<Int, Int>? = intConstHashTable.get(const)

    fun stringConstExists(string: String): Pair<Int, Int>? = stringConstHashTable.get(string)

    fun getId(containerPos: Int, containerElemIdx: Int) = idHashTable.getByIndex(containerPos, containerElemIdx)

    fun getIntConst(containerPos: Int, containerElemIdx: Int) = intConstHashTable.getByIndex(containerPos, containerElemIdx)

    fun getStringConst(containerPos: Int, containerElemIdx: Int) = stringConstHashTable.getByIndex(containerPos, containerElemIdx)

    fun addId(name: String): Position = Position(PositionType.IDENTIFIER, idHashTable.add(name))

    fun addIntConst(constant: Int): Position = Position(PositionType.INT_CONSTANT, intConstHashTable.add(constant))

    fun addStringConst(string: String): Position = Position(PositionType.STRING_CONSTANT, stringConstHashTable.add(string))
}