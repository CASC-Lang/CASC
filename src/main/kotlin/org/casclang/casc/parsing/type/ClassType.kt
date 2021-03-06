package org.casclang.casc.parsing.type

import jdk.internal.org.objectweb.asm.Opcodes.*
import org.casclang.casc.compilation.Compiler
import org.casclang.casc.compilation.PackageTree
import org.casclang.casc.compilation.Parser
import java.io.File
import java.net.URLClassLoader

class ClassType(override val typeName: String) : Type {
    override fun classType(): Class<*> {
        val classLoader = URLClassLoader(arrayOf(Compiler.compilation.outputDirectory.toURI().toURL()))

        return Class.forName(typeName, false, classLoader)
    }

    fun isClassExists() =
        try {
            val classLoader = URLClassLoader(arrayOf(Compiler.compilation.outputDirectory.toURI().toURL()))

            val clazz = Class.forName(typeName, false, classLoader)

            true
        } catch (e: Exception) {
            false
        }

    fun isCached() =
        PackageTree.classes.containsKey(internalName) && !PackageTree.classes[internalName]!!.isCompiled

    fun tryInitClass(): Class<*> {
        val selfFile = Compiler.compilation.currentFile

        if (isCached()) {
            Parser("${Compiler.compilation.source.path}/${PackageTree.classes[internalName]!!.relativeFilePath}")
                .parseFile()
                .emitBytecode()
            PackageTree.classes[internalName]!!.isCompiled = true
        }

        Compiler.compilation.currentFile = selfFile

        return classType()
    }

    override val internalName: String = typeName.replace('.', '/')
    override val descriptor: String = "L$internalName;"
    override val loadVariableOpcode: Int = ALOAD
    override val storeVariableOpcode: Int = ASTORE
    override val returnOpcode: Int = ARETURN
    override val addOpcode: Int
        get() = throw RuntimeException("Addition operation is not supported for custom objects")
    override val subtractOpcode: Int
        get() = throw RuntimeException("Subtraction operation is not supported for custom objects")
    override val multiplyOpcode: Int
        get() = throw RuntimeException("Multiplication operation is not supported for custom objects")
    override val divideOpcode: Int
        get() = throw RuntimeException("Division operation is not supported for custom objects")
    override val negativeOpcode: Int
        get() = throw RuntimeException("Negate operation is not supported for custom objects")
    override val typeOpcode: Int
        get() = throw RuntimeException("Type operation is not supported for custom objects")
    override val arrayLoadOpcode: Int = AALOAD
    override val arrayStoreOpcode: Int = AASTORE

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val classType = other as ClassType
        return typeName == classType.typeName
    }

    override fun hashCode() =
        typeName.hashCode()

    override fun toString() =
        typeName
}