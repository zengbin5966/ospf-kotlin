package fuookami.ospf.kotlin.utils.physics.unit

import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.physics.dimension.*

data object Kilogram : Unit {
    override val name: String = "kilogram"
    override val symbol: String = "kg"

    override val quantity = DerivedQuantity(FundamentalQuantityDimension.Mass)
    override val system = SI
    override val scale = Scale()
}
