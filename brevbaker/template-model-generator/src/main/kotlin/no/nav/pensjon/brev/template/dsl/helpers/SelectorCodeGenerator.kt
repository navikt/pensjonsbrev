package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import java.io.PrintWriter

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
            val rootPkg = root.node.decl.packageName.asString()
            val rootSegment = root.node.decl.simpleName.asString().replaceFirstChar { it.lowercase() }
            // Default-package classes can't be referenced from sub-packages, so keep their selectors in the default package too
            val selectorsPkg = if (rootPkg.isNotBlank()) "$rootPkg.selectors.$rootSegment" else ""
            generateNodeCode(codeGenerator, root.node, selectorsPkg, root.dependencies)
        }
    }

    companion object {
        private fun generateNodeCode(codeGenerator: CodeGenerator, node: Node, pkg: String, dependencies: Set<KSFile>) {
            if (node.include) {
                val className = node.decl.simpleName.asString()
                createFile(codeGenerator, pkg, className, dependencies) { writer ->
                    node.decl.getAllProperties().forEach { generatePropertySelectors(it, writer) }
                }
            }
            node.children.values.forEach { child ->
                val childSegment = child.decl.simpleName.asString().replaceFirstChar { it.lowercase() }
                // If parent is in default package, keep children there too (can't reference default-package classes from sub-packages)
                val childPkg = if (pkg.isBlank()) "" else "$pkg.$childSegment"
                generateNodeCode(codeGenerator, child, childPkg, dependencies)
            }
        }

        private fun generatePropertySelectors(property: KSPropertyDeclaration, writer: PrintWriter) {
            val propertyName = property.simpleName.asString()
            val selectorName = "${propertyName}Selector"

            val declaringClass =
                property.closestClassDeclaration()
                    ?: throw InvalidVisitorState("Couldn't find class of $propertyName")

            val dataClassName =
                declaringClass.qualifiedName?.asString()
                    ?: throw InvalidModel("Couldn't find qualified name of $declaringClass")

            val type = property.type.resolveWithTypeParameters()

            writer.println(
                """
                |@JvmField
                |val $selectorName =
                |    SimpleSelector<$dataClassName, $type>(
                |        className = "$dataClassName",
                |        propertyName = "$propertyName",
                |        propertyType = "$type",
                |        selector = $dataClassName::$propertyName
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
                """.trimMargin()
            )
        }

        private fun <T> createFile(codeGenerator: CodeGenerator, pkg: String, className: String, dependencies: Set<KSFile>, useBlock: (PrintWriter) -> T): T =
            PrintWriter(codeGenerator.createNewFile(Dependencies(true, *dependencies.toTypedArray()), pkg, "${className}Selectors")).use { writer ->
                writer.println(
                    """
                    @file:OptIn(InternKonstruktoer::class)

                    ${if (pkg.isNotBlank()) "package $pkg" else ""}

                    import no.nav.pensjon.brev.template.Expression
                    import no.nav.pensjon.brev.template.UnaryOperation
                    import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
                    import no.nav.brev.InternKonstruktoer
                    import no.nav.pensjon.brev.template.SimpleSelector

                    """.trimIndent()
                )
                useBlock(writer)
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