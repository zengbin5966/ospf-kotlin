package fuookami.ospf.kotlin.utils.physics.dimension

class DerivedQuantity(
    quantities: List<FundamentalQuantity>,
    val name: String? = null
) {
    val quantities = quantities.sortedBy { it.dimension }

    constructor(dimension: FundamentalQuantityDimension, name: String? = null) : this(listOf(FundamentalQuantity(dimension)), name)
    constructor(quantity: FundamentalQuantity, name: String? = null) : this(listOf(quantity), name)

    override fun toString(): String {
        return name ?: quantities.joinToString(separator = " * ") { "${it.dimension}^${it.index}" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DerivedQuantity

        return quantities == other.quantities
    }

    override fun hashCode(): Int {
        return quantities.hashCode()
    }
}

operator fun FundamentalQuantity.times(other: FundamentalQuantity): DerivedQuantity {
    val indexes = mutableMapOf<FundamentalQuantityDimension, Int>()
    indexes[this.dimension] = indexes.getOrDefault(this.dimension, 0) + this.index
    indexes[other.dimension] = indexes.getOrDefault(other.dimension, 0) + other.index
    return DerivedQuantity(indexes.map { FundamentalQuantity(it.key, it.value) })
}

operator fun FundamentalQuantity.div(other: FundamentalQuantity): DerivedQuantity {
    val indexes = mutableMapOf<FundamentalQuantityDimension, Int>()
    indexes[this.dimension] = indexes.getOrDefault(this.dimension, 0) + this.index
    indexes[other.dimension] = indexes.getOrDefault(other.dimension, 0) - other.index
    return DerivedQuantity(indexes.map { FundamentalQuantity(it.key, it.value) })
}

operator fun FundamentalQuantity.times(other: DerivedQuantity): DerivedQuantity {
    val indexes = mutableMapOf<FundamentalQuantityDimension, Int>()
    for (quantity in other.quantities) {
        indexes[quantity.dimension] = indexes.getOrDefault(quantity.dimension, 0) + quantity.index
    }
    indexes[this.dimension] = indexes.getOrDefault(this.dimension, 0) + this.index
    return DerivedQuantity(indexes.map { FundamentalQuantity(it.key, it.value) })
}

operator fun FundamentalQuantity.div(other: DerivedQuantity): DerivedQuantity {
    val indexes = mutableMapOf<FundamentalQuantityDimension, Int>()
    for (quantity in other.quantities) {
        indexes[quantity.dimension] = indexes.getOrDefault(quantity.dimension, 0) - quantity.index
    }
    indexes[this.dimension] = indexes.getOrDefault(this.dimension, 0) + this.index
    return DerivedQuantity(indexes.map { FundamentalQuantity(it.key, it.value) })
}

operator fun DerivedQuantity.times(other: DerivedQuantity): DerivedQuantity {
    val indexes = mutableMapOf<FundamentalQuantityDimension, Int>()
    for (quantity in this.quantities) {
        indexes[quantity.dimension] = indexes.getOrDefault(quantity.dimension, 0) + quantity.index
    }
    for (quantity in other.quantities) {
        indexes[quantity.dimension] = indexes.getOrDefault(quantity.dimension, 0) + quantity.index
    }
    return DerivedQuantity(indexes.map { FundamentalQuantity(it.key, it.value) })
}

operator fun DerivedQuantity.div(other: DerivedQuantity): DerivedQuantity {
    val indexes = mutableMapOf<FundamentalQuantityDimension, Int>()
    for (quantity in this.quantities) {
        indexes[quantity.dimension] = indexes.getOrDefault(quantity.dimension, 0) + quantity.index
    }
    for (quantity in other.quantities) {
        indexes[quantity.dimension] = indexes.getOrDefault(quantity.dimension, 0) - quantity.index
    }
    return DerivedQuantity(indexes.map { FundamentalQuantity(it.key, it.value) })
}
