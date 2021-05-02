package io.github.chaosunity.casc.parsing.node.expression

import io.github.chaosunity.casc.parsing.type.Type

data class Argument(val argumentName: String?, val expression: Expression<*>) : Expression<Argument> {
    override val type: Type = expression.type
}