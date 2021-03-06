package org.casclang.casc.bytecode.statement

import jdk.internal.org.objectweb.asm.MethodVisitor
import jdk.internal.org.objectweb.asm.Opcodes.GETSTATIC
import jdk.internal.org.objectweb.asm.Opcodes.INVOKEVIRTUAL
import org.casclang.casc.bytecode.expression.ExpressionFactory
import org.casclang.casc.parsing.node.statement.OutputStatement
import org.casclang.casc.parsing.node.statement.PrintStatement
import org.casclang.casc.parsing.node.statement.PrintlnStatement
import org.casclang.casc.parsing.type.ClassType


class PrintFactory(private val ef: ExpressionFactory, private val mv: MethodVisitor) {
    fun generate(printStatement: PrintStatement) =
        preGenerate(printStatement, "print")

    fun generate(printlnStatement: PrintlnStatement) =
        preGenerate(printlnStatement, "println")

    private fun preGenerate(printable: OutputStatement<*>, methodName: String) {
        val expression = printable.expression
        val type = expression.type

        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        expression.accept(ef)

        val descriptor = if (type.isBuiltInType()) "(${type.descriptor})V" else "(Ljava/lang/Object;)V"
        val owner = ClassType("java.io.PrintStream")
        val fieldDescriptor = owner.internalName

        mv.visitMethodInsn(INVOKEVIRTUAL, fieldDescriptor, methodName, descriptor, false)
    }
}