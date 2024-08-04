package fuookami.ospf.kotlin.utils.physics.dimension

data class DerivedQuantity(val quantities: List<FundamentalQuantity>) {
    constructor(dimension: FundamentalQuantityDimension) : this(listOf(FundamentalQuantity(dimension)))
    constructor(quantity: FundamentalQuantity) : this(listOf(quantity))
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
