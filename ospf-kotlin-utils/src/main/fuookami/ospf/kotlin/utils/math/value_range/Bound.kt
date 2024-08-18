package fuookami.ospf.kotlin.utils.math.value_range

import java.util.*
import kotlin.reflect.full.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import fuookami.ospf.kotlin.utils.concept.*
import fuookami.ospf.kotlin.utils.math.*
import fuookami.ospf.kotlin.utils.math.ordinary.*
import fuookami.ospf.kotlin.utils.operator.*

//enum class BoundSide {
//    Lower,
//    Upper
//}
//
//data class Bound<T>(
//    val value: ValueWrapper<T>,
//    val interval: Interval
//) : Cloneable, Copyable<ValueWrapper<T>>, Ord<ValueWrapper<T>>, Eq<ValueWrapper<T>>,
//    Plus<ValueWrapper<T>, ValueWrapper<T>>, Minus<ValueWrapper<T>, ValueWrapper<T>>,
//    Times<ValueWrapper<T>, ValueWrapper<T>>, Div<ValueWrapper<T>, ValueWrapper<T>>
//        where T : RealNumber<T>, T : NumberField<T>
