package io.github.chaosunity.casc.visitor

import io.github.chaosunity.antlr.CASCBaseVisitor
import io.github.chaosunity.antlr.CASCParser
import io.github.chaosunity.casc.parsing.expression.Expression
import io.github.chaosunity.casc.parsing.expression.FunctionCall
import io.github.chaosunity.casc.parsing.expression.Value
import io.github.chaosunity.casc.parsing.expression.VarReference
import io.github.chaosunity.casc.parsing.math.Addition
import io.github.chaosunity.casc.parsing.math.Division
import io.github.chaosunity.casc.parsing.math.Multiplication
import io.github.chaosunity.casc.parsing.math.Subtraction
import io.github.chaosunity.casc.parsing.scope.Scope
import io.github.chaosunity.casc.util.TypeResolver

class ExpressionVisitor(private val scope: Scope) : CASCBaseVisitor<Expression>() {
    override fun visitVarReference(ctx: CASCParser.VarReferenceContext?): Expression {
        val variableName = ctx?.text
        val localVariableDeclaration =  scope.getLocalVariable(variableName)

        return VarReference(localVariableDeclaration.type(), variableName)
    }

    override fun visitValue(ctx: CASCParser.ValueContext?): Expression {
        val value = ctx?.text
        val type = TypeResolver.getFromValue(value)

        return Value(type, value)
    }

    override fun visitFunctionCall(ctx: CASCParser.FunctionCallContext?): Expression {
        val functionName = ctx?.functionName()?.text
        val signature = scope.getSignature(functionName)
        val signatureParameters = signature.parameters()
        val calledParameters = ctx?.expressionList()?.expression() ?: mutableListOf()
        val args = calledParameters.map { it.accept(ExpressionVisitor(scope)) }
        val returnType = signature.returnType()

        return FunctionCall(signature, args, returnType)
    }

    override fun visitAdd(ctx: CASCParser.AddContext?): Expression {
        val left = ctx?.expression(0)?.accept(this)
        val right = ctx?.expression(1)?.accept(this)

        return Addition(left, right)
    }

    override fun visitSubtract(ctx: CASCParser.SubtractContext?): Expression {
        val left = ctx?.expression(0)?.accept(this)
        val right = ctx?.expression(1)?.accept(this)

        return Subtraction(left, right)
    }

    override fun visitMultiply(ctx: CASCParser.MultiplyContext?): Expression {
        val left = ctx?.expression(0)?.accept(this)
        val right = ctx?.expression(1)?.accept(this)

        return Multiplication(left, right)
    }

    override fun visitDivide(ctx: CASCParser.DivideContext?): Expression {
        val left = ctx?.expression(0)?.accept(this)
        val right = ctx?.expression(1)?.accept(this)

        return Division(left, right)
    }
}