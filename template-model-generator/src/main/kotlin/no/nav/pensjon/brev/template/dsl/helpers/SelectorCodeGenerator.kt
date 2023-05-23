package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import no.nav.pensjon.brev.template.TemplateModelSelector
import java.io.PrintWriter

private val templateModelSelectorName = TemplateModelSelector::class.qualifiedName
    ?: throw InitializationError("Couldn't determine qualified name of ${TemplateModelSelector::class.simpleName}")
private const val INDENT = "    "

internal class SelectorCodeGenerator(needed: Set<KSClassDeclaration>) {
    private data class Node(val decl: KSClassDeclaration, var include: Boolean, val children: MutableMap<KSClassDeclaration, Node> = mutableMapOf())

    private val roots: MutableMap<KSClassDeclaration, Node> = mutableMapOf()

    init {
        needed.forEach {
            findOrCreateBranches(it).include = true
        }
    }

    private fun findOrCreateBranches(branch: KSClassDeclaration): Node {
        val parentBranch = when (val parent = branch.parentDeclaration) {
            null -> roots.computeIfAbsent(branch) { Node(branch, false) }
            is KSClassDeclaration -> findOrCreateBranches(parent).children.computeIfAbsent(branch) { Node(branch, false) }

            else -> throw InvalidModel("Cannot build SelectorTree for $branch: parent is not a class $parent")
        }

        return parentBranch
    }

    fun generateCode(codeGenerator: CodeGenerator) {
        roots.values.forEach { root ->
            createFile(codeGenerator, root.decl) { writer ->
                generateSelectors(root, "", writer)
            }
        }
    }

    companion object {
        private fun generateSelectors(node: Node, indent: String, writer: PrintWriter) {
            val className = node.decl.simpleName.asString()
            writer.println("${indent}object ${className}Selectors {")

            if (node.include) {
                node.decl.getAllProperties().forEach { generateSelectors(it, indent + INDENT, writer) }
            }

            writer.println()
            node.children.values.forEach { generateSelectors(it, indent + INDENT, writer) }

            writer.println("${indent}}")
        }

        private fun generateSelectors(property: KSPropertyDeclaration, indent: String, writer: PrintWriter) {
            val propertyName = property.simpleName.asString()
            val selectorName = "${propertyName}Selector"
            val declaringClass = property.closestClassDeclaration() ?: throw InvalidVisitorState("Couldn't find class of $propertyName: but we came here from a class...")
            val dataClassName = declaringClass.qualifiedName?.asString() ?: throw InvalidModel("Couldn't find qualified name of class: $declaringClass")
            val type = property.type.resolveWithTypeParameters()

            writer.println(
                """
            |val $selectorName = object : TemplateModelSelector<$dataClassName, $type> {
            |   override val className: String = "$dataClassName"
            |   override val propertyName: String = "$propertyName"
            |   override val propertyType: String = "$type"
            |   override val selector = $dataClassName::$propertyName
            |}
            |
            |val TemplateGlobalScope<$dataClassName>.$propertyName: Expression<$type>
            |   get() = Expression.UnaryInvoke(
            |       Expression.FromScope(ExpressionScope<$dataClassName, *>::argument),
            |       UnaryOperation.Select($selectorName)
            |   )
            |
            |val Expression<$dataClassName>.$propertyName: Expression<$type>
            |   get() = Expression.UnaryInvoke(
            |       this,
            |       UnaryOperation.Select($selectorName)
            |   )
            |
            |val Expression<$dataClassName?>.${propertyName}_safe: Expression<${nullable(type)}>
            |   get() = Expression.UnaryInvoke(
            |       this,
            |       UnaryOperation.SafeCall($selectorName)
            |   )
            |
            """.replaceIndentByMargin(indent)
            )
        }

        private fun <T> createFile(codeGenerator: CodeGenerator, classDeclaration: KSClassDeclaration, useBlock: (PrintWriter) -> T): T {
            val className = classDeclaration.simpleName.asString()
            val pkg = classDeclaration.packageName.asString()

            // TODO: Sjekk at dependencies fungerer.
            return PrintWriter(codeGenerator.createNewFile(Dependencies(true), pkg, "${className}Selectors")).use { writer ->
                writer.println(
                    """
                ${if (pkg.isNotBlank()) "package $pkg" else ""}

                import $templateModelSelectorName
                import no.nav.pensjon.brev.template.Expression
                import no.nav.pensjon.brev.template.UnaryOperation
                import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
                import no.nav.pensjon.brev.template.ExpressionScope

                """.trimIndent()
                )
                useBlock(writer)
            }
        }

        private fun KSTypeReference.resolveWithTypeParameters(): String =
            resolve().let { resolved ->
                val typeName = resolved.declaration.qualifiedName?.asString() ?: throw InvalidModel("Couldn't determine qualified name for type: $this")

                val typeArgs: String = resolved.arguments.map { it.type ?: throw InvalidModel("Couldn't determine type of type argument $it for $this") }
                    .takeIf { it.isNotEmpty() }
                    ?.joinToString(", ", "<", ">") { it.resolveWithTypeParameters() }
                    ?: ""

                when (resolved.nullability) {
                    Nullability.NULLABLE -> "$typeName$typeArgs?"
                    Nullability.NOT_NULL -> "$typeName$typeArgs"
                    Nullability.PLATFORM -> throw InvalidModel("Don't know how to handle type with Nullability.PLATFORM of type: $typeName")
                }
            }

        private fun nullable(type: String): String = if (type.endsWith('?')) type else "$type?"
    }
}