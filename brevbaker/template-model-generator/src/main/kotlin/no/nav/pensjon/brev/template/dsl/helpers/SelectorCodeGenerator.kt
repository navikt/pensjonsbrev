package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import no.nav.pensjon.brevbaker.api.model.Redigerbar
import java.io.PrintWriter

private const val INDENT = "    "

internal class SelectorCodeGenerator(needed: Map<KSClassDeclaration, Set<KSFile>>) {
    private data class Node(val decl: KSClassDeclaration, var include: Boolean, val children: MutableMap<KSClassDeclaration, Node> = mutableMapOf())
    private data class Root(val node: Node, val dependencies: MutableSet<KSFile>)

    private val roots: MutableMap<KSClassDeclaration, Root> = mutableMapOf()

    init {
        needed.forEach { (decl, dependencies) ->
            findOrCreateBranches(decl, dependencies).include = true
        }
    }

    private fun findOrCreateBranches(branch: KSClassDeclaration, dependencies: Set<KSFile>): Node =
        when (val parent = branch.parentDeclaration) {
            null -> {
                roots[branch]?.apply { this.dependencies += dependencies }?.node
                    ?: Root(Node(branch, false), dependencies.toMutableSet()).also { roots[branch] = it }.node
            }

            is KSClassDeclaration -> findOrCreateBranches(parent, dependencies).children.computeIfAbsent(branch) { Node(branch, false) }

            else -> throw InvalidModel("Cannot build SelectorTree for $branch: parent is not a class $parent")
        }

    fun generateCode(codeGenerator: CodeGenerator) {
        roots.values.forEach { root ->
            createFile(codeGenerator, root) { writer ->
                generateSelectors(root.node, "", writer)
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

        private fun generateSelectors(
            property: KSPropertyDeclaration,
            indent: String,
            writer: PrintWriter
        ) {
            val propertyName = property.simpleName.asString()
            val selectorName = "${propertyName}Selector"

            val declaringClass =
                property.closestClassDeclaration()
                    ?: throw InvalidVisitorState("Couldn't find class of $propertyName")

            val dataClassName =
                declaringClass.qualifiedName?.asString()
                    ?: throw InvalidModel("Couldn't find qualified name of $declaringClass")

            val type = property.type.resolveWithTypeParameters()

            val redigerbar = property.annotations.map { it.annotationType.resolveWithTypeParameters() }.any { it == Redigerbar::class.qualifiedName }

            writer.println(
                """
                |@JvmField
                |val $selectorName =
                |    SimpleSelector<$dataClassName, $type>(
                |        className = "$dataClassName",
                |        propertyName = "$propertyName",
                |        propertyType = "$type",
                |        selector = $dataClassName::$propertyName,
                |        redigerbar = $redigerbar
                |    )
                |
                |val TemplateGlobalScope<$dataClassName>.$propertyName: Expression<$type>
                |    get() = Expression.UnaryInvoke(
                |        Expression.FromScope.Argument(),
                |        UnaryOperation.Select($selectorName)
                |    )
                |
                |val Expression<$dataClassName>.$propertyName: Expression<$type>
                |    get() = Expression.UnaryInvoke(
                |        this,
                |        UnaryOperation.Select($selectorName)
                |    )
                |
                """.trimMargin().prependIndent(indent)
            )
        }

        private fun <T> createFile(codeGenerator: CodeGenerator, root: Root, useBlock: (PrintWriter) -> T): T {
            val className = root.node.decl.simpleName.asString()
            val pkg = root.node.decl.packageName.asString()

            return PrintWriter(codeGenerator.createNewFile(Dependencies(true, *root.dependencies.toTypedArray()), pkg, "${className}Selectors")).use { writer ->
                writer.println(
                    """
                ${if (pkg.isNotBlank()) "package $pkg" else ""}

                import no.nav.pensjon.brev.template.Expression
                import no.nav.pensjon.brev.template.UnaryOperation
                import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
                import no.nav.brev.InternKonstruktoer
                import no.nav.pensjon.brev.template.SimpleSelector

                @OptIn(InternKonstruktoer::class)
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

    }
}