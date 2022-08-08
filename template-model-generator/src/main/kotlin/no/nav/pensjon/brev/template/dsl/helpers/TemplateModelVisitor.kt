package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelVisitor.Data
import no.nav.pensjon.brev.template.TemplateModelSelector
import org.slf4j.LoggerFactory
import java.io.PrintWriter

private val templateModelSelectorName = TemplateModelSelector::class.qualifiedName
    ?: throw InitializationError("Couldn't determine qualified name of ${TemplateModelSelector::class.simpleName}")

private val logger = LoggerFactory.getLogger(TemplateModelVisitor::class.java)
private const val INDENT = "    "

internal class TemplateModelVisitor(private val codeGenerator: CodeGenerator) : KSDefaultVisitor<Data, List<KSClassDeclaration>>() {

    data class Data(val indent: String, val writer: PrintWriter? = null)

    private fun <T> createFile(classDeclaration: KSClassDeclaration, useBlock: (PrintWriter) -> T): T {
        val className = classDeclaration.simpleName.asString()
        val symbolFile = classDeclaration.containingFile //?: throw OurProcessorException("Couldn't find containingFile of symbol: $className")
        val pkg = classDeclaration.packageName.asString()

        return PrintWriter(codeGenerator.createNewFile(Dependencies(true, *listOfNotNull(symbolFile).toTypedArray()), pkg, "${className}Selectors")).use { writer ->
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

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Data): List<KSClassDeclaration> {
        if (classDeclaration.classKind == ClassKind.CLASS && classDeclaration.modifiers.contains(Modifier.DATA)) {
            val className = classDeclaration.simpleName.asString()

            if (data.writer != null) {
                throw InvalidVisitorState("Cannot generate selector for $className: already have a file writer (probably recursion error)")
            }

            return createFile(classDeclaration) { writer ->
                val properties = classDeclaration.getAllProperties().toList()
                logger.info("Generating selector for $className with properties: $properties")

                writer.println("object ${className}Selectors {")
                val nextClasses = properties.flatMap { it.accept(this, Data(data.indent + INDENT, writer)) }
                writer.println("}")
                return@createFile nextClasses
            }
        } else {
            logger.warn("Skipping $classDeclaration: can only generate helpers for data classes")
            return emptyList()
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

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Data): List<KSClassDeclaration> {
        val propertyName = property.simpleName.asString()
        val selectorName = "${propertyName}Selector"
        val declaringClass = property.closestClassDeclaration() ?: throw InvalidVisitorState("Couldn't find class of $propertyName: but we came here from a class...")
        val dataClassName = declaringClass.qualifiedName?.asString() ?: throw InvalidModel("Couldn't find qualified name of class: $declaringClass")
        val resolvedType = property.type.resolve()
        val type = property.type.resolveWithTypeParameters()

        logger.info("Generating $selectorName for class $dataClassName with type $type")

        val writer = data.writer ?: throw InvalidVisitorState("Couldn't generate selector for $dataClassName.$propertyName: missing file writer")

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
            |       UnaryOperation.Select2($selectorName)
            |   )
            |
            |val Expression<$dataClassName>.$propertyName: Expression<$type>
            |   get() = Expression.UnaryInvoke(
            |       this,
            |       UnaryOperation.Select2($selectorName)
            |   )
            |
            """.replaceIndentByMargin(data.indent)
        )

        return when (val typeDeclaration = resolvedType.declaration) {
            is KSClassDeclaration -> {
                if (typeDeclaration.typeParameters.isEmpty()) {
                    logger.info("Type of $dataClassName.$propertyName is a class, we may need a generator for it: $typeDeclaration")
                    listOf(typeDeclaration)
                } else {
                    logger.warn("Type of $dataClassName.$propertyName is a class that has type parameter(s): selectors with type parameters is not supported ")
                    emptyList()
                }
            }

            is KSTypeParameter -> {
                logger.warn("Cannot generate helpers for property type $typeDeclaration of $dataClassName.$propertyName: generic type parameters not supported")
                emptyList()
            }

            else -> {
                throw MissingImplementation("Unsupported property type kind ${typeDeclaration::class}: for $dataClassName.$property")
            }
        }
    }

    override fun defaultHandler(node: KSNode, data: Data): List<KSClassDeclaration> {
        throw MissingImplementation("Couldn't process node $node at: ${node.location}")
    }

}