package io.github.chaosunity.casc.visitor.statement

import io.github.chaosunity.casc.CASCBaseVisitor
import io.github.chaosunity.casc.CASCParser
import io.github.chaosunity.casc.parsing.node.expression.Expression
import io.github.chaosunity.casc.parsing.node.statement.Statement
import io.github.chaosunity.casc.parsing.scope.Scope
import io.github.chaosunity.casc.visitor.expression.ExpressionVisitor

class StatementVisitor(scope: Scope) : CASCBaseVisitor<Statement<*>>() {
    private val ev = ExpressionVisitor(scope)
    private val pv = PrintVisitor(ev)
    private val vdv = VariableDeclarationVisitor(ev, scope)
    private val rv = ReturnVisitor(ev)
    private val fv = ForVisitor(scope)
    private val bv = BlockVisitor(scope)
    private val iv = IfStatementVisitor(this, ev)
    private val av = AssignmentVisitor(ev, scope)

    override fun visitPrintStatement(ctx: CASCParser.PrintStatementContext): Statement<*> =
        pv.visitPrintStatement(ctx)

    override fun visitPrintlnStatement(ctx: CASCParser.PrintlnStatementContext): Statement<*> =
        pv.visitPrintlnStatement(ctx)

    override fun visitReturnVoid(ctx: CASCParser.ReturnVoidContext): Statement<*> =
        rv.visitReturnVoid(ctx)

    override fun visitReturnWithValue(ctx: CASCParser.ReturnWithValueContext): Statement<*> =
        rv.visitReturnWithValue(ctx)

    override fun visitForStatement(ctx: CASCParser.ForStatementContext): Statement<*> =
        fv.visitForStatement(ctx)

    override fun visitBlock(ctx: CASCParser.BlockContext): Statement<*> =
        bv.visitBlock(ctx)

    override fun visitIfStatement(ctx: CASCParser.IfStatementContext): Statement<*> =
        iv.visitIfStatement(ctx)

    override fun visitVarRef(ctx: CASCParser.VarRefContext): Expression<*> =
        ev.visitVarRef(ctx)

    override fun visitVarReference(ctx: CASCParser.VarReferenceContext): Expression<*> =
        ev.visitVarReference(ctx)

    override fun visitIndexEpxression(ctx: CASCParser.IndexEpxressionContext): Statement<*> =
        ev.visitIndexEpxression(ctx)

    override fun visitVariableDeclaration(ctx: CASCParser.VariableDeclarationContext): Statement<*> =
        vdv.visitVariableDeclaration(ctx)

    override fun visitFieldCall(ctx: CASCParser.FieldCallContext): Statement<*> =
        ev.visitFieldCall(ctx)

    override fun visitFunctionCall(ctx: CASCParser.FunctionCallContext): Expression<*> =
        ev.visitFunctionCall(ctx)

    override fun visitConstructorCall(ctx: CASCParser.ConstructorCallContext): Expression<*> =
        ev.visitConstructorCall(ctx)

    override fun visitSuperCall(ctx: CASCParser.SuperCallContext): Expression<*> =
        ev.visitSuperCall(ctx)

    override fun visitArrayDeclaration(ctx: CASCParser.ArrayDeclarationContext): Statement<*> =
        ev.visitArrayDeclaration(ctx)

    override fun visitArrayInitialization(ctx: CASCParser.ArrayInitializationContext): Statement<*> =
        ev.visitArrayInitialization(ctx)

    override fun visitValue(ctx: CASCParser.ValueContext): Expression<*> =
        ev.visitValue(ctx)

    override fun visitAdd(ctx: CASCParser.AddContext): Expression<*> =
        ev.visitAdd(ctx)

    override fun visitSubtract(ctx: CASCParser.SubtractContext): Expression<*> =
        ev.visitSubtract(ctx)

    override fun visitMultiply(ctx: CASCParser.MultiplyContext): Expression<*> =
        ev.visitMultiply(ctx)

    override fun visitDivide(ctx: CASCParser.DivideContext): Expression<*> =
        ev.visitDivide(ctx)

    override fun visitConditionalExpression(ctx: CASCParser.ConditionalExpressionContext): Expression<*> =
        ev.visitConditionalExpression(ctx)

    override fun visitIfExpression(ctx: CASCParser.IfExpressionContext): Expression<*> =
        ev.visitIfExpression(ctx)

    override fun visitAssignment(ctx: CASCParser.AssignmentContext): Statement<*> =
        av.visitAssignment(ctx)

    override fun visitNegativeExpression(ctx: CASCParser.NegativeExpressionContext): Expression<*> =
        ev.visitNegativeExpression(ctx)

    override fun visitWrappedExpression(ctx: CASCParser.WrappedExpressionContext): Expression<*> =
        ev.visitWrappedExpression(ctx)
}