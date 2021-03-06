package org.casclang.casc.parsing.node.expression

import org.casclang.casc.parsing.type.ArrayType
import org.casclang.casc.parsing.type.Type

data class Index(val expression: Expression<*>, val indexExpression: Expression<*>) : Expression<Index> {
    override val type: Type
        get() {
            val type = expression.type

            if (type !is ArrayType)
                throw RuntimeException("Cannot index non-array type.")

            return if (type.dimension > 1) ArrayType(type.baseType, type.dimension - 1)
            else if (type.dimension == 1) type.baseType
            else throw RuntimeException("Index operator overflow.")
        }
}
