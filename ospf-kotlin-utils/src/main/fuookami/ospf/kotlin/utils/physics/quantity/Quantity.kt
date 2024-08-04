package fuookami.ospf.kotlin.utils.physics.quantity

import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.physics.unit.*

data class Quantity<V: Arithmetic<V>>(
    val value: V,
    val unit: Unit
) {
    fun to(unit: Unit): Quantity<V> {
        TODO("not implemented yet")
    }
}
