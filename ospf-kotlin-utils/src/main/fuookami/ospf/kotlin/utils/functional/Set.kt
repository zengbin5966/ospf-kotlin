package fuookami.ospf.kotlin.utils.functional

import java.util.*

fun <T> Iterable<T>.toSortedSetWithComparator(comparator: Comparator<T>): SortedSet<T> {
    return this.toSortedSet { lhs, rhs ->
        if (comparator(lhs, rhs)) {
            -1
        } else if (comparator(rhs, lhs)) {
            1
        } else {
            0
        }
    }
}

fun <T> Iterable<T>.toSortedSetWithPartialComparator(comparator: PartialComparator<T>): SortedSet<T> {
    return this.toSortedSet { lhs, rhs ->
        if (comparator(lhs, rhs) == true) {
            -1
        } else if (comparator(rhs, lhs) == true) {
            1
        } else {
            0
        }
    }
}

fun <T> Iterable<T>.toSortedSetWithThreeWayComparator(comparator: ThreeWayComparator<T>): SortedSet<T> {
    return this.toSortedSet { lhs, rhs -> comparator(lhs, rhs).value }
}

fun <T> Iterable<T>.toSortedSetWithPartialThreeWayComparator(comparator: PartialThreeWayComparator<T>): SortedSet<T> {
    return this.toSortedSet { lhs, rhs -> comparator(lhs, rhs)?.value ?: 0 }
}
