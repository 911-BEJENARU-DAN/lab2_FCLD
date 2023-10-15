import java.lang.Exception
import kotlin.math.abs;


class HashTable<T>(private var size: Int = 10){
    var containers: Array<MutableList<T>> = Array(size) { mutableListOf() }

    private fun hash(elem: T): Int {
        val value = elem.hashCode() % size;

        return abs(value);
    }

    fun get(elem: T): Pair<Int, Int>? {
        val containerIdx = hash(elem);
        containers[containerIdx].forEachIndexed{ currContainerElemIdx, currContainerElemValue ->
            if (currContainerElemValue == elem) {
                return Pair(containerIdx, currContainerElemIdx);
            }
        }

        return null;
    }

    fun getByIndex(containerPos: Int, containerElemIdx: Int): T {
        if (containerPos !in 0 until size) {
            throw Exception("Invalid container position!");
        }

        if (containerElemIdx !in 0 until containers[containerPos].size) {
            throw Exception("Invalid position within the container!");
        }

        return containers[containerPos][containerElemIdx];
    }


    fun add(elem: T): Pair<Int, Int> {
        val item = get(elem);
        if (item != null) {
            return item;
        }

        val containerPos = hash(elem);
        val containerElemIdx = containers[containerPos].size;
        containers[containerPos].add(elem);

        return Pair(containerPos, containerElemIdx);
    }
}

