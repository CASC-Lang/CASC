package io.github.chaosunity.casc.parsing.scope

import io.github.chaosunity.casc.parsing.node.expression.Argument
import io.github.chaosunity.casc.parsing.node.expression.Parameter
import io.github.chaosunity.casc.parsing.type.Type

data class FunctionSignature(val name: String, val parameters: List<Parameter>, val returnType: Type) {
    fun getParameterByName(name: String): Parameter =
        parameters.find { it.name == name }
            ?: throw RuntimeException("Function '${this.name}' has no parameter named '$name'")

    fun getIndexOfParameter(parameterName: String): Int =
        parameters.indexOf(getParameterByName(parameterName))

    fun matches(signatureName: String, arguments: List<Argument>): Boolean {
        if (name != signatureName) return false

        val nonDefaultParameterCount = parameters.filter { it.defaultValue == null }.count()

        if (nonDefaultParameterCount > arguments.size) return false

        if (arguments.any { it.argumentName != null }) {
            return arguments.all {
                val parameterName = it.argumentName!!
                return parameters.map(Parameter::name).any(parameterName::equals)
            }
        }

        return (arguments.indices).all {
            arguments[it].type == parameters[it].type
        }
    }
}