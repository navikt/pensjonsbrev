package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(TemplateModelVisitor::class.java)

internal class TemplateModelVisitor(
    private val iterableDeclaration: KSType,
) : KSDefaultVisitor<SelectorModels, SelectorModels>() {

    override fun defaultHandler(node: KSNode, data: SelectorModels): SelectorModels {
        throw MissingImplementation("Couldn't process node $node at: ${node.location}")
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: SelectorModels): SelectorModels =
        if (data.isVisited(classDeclaration)) {
            data
        } else if (classDeclaration.classKind == ClassKind.CLASS && classDeclaration.modifiers.contains(Modifier.DATA)) {
            classDeclaration.getAllProperties().toList().foldAccept(data.withNeeded(classDeclaration), this)
        } else {
            logger.warn("Skipping $classDeclaration: can only generate helpers for data classes")
            data.withVisited(classDeclaration)
        }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: SelectorModels): SelectorModels =
        findModel(property.type.resolve())
            ?.accept(this, data)
            ?: data

    private fun findModel(resolvedType: KSType): KSClassDeclaration? {
        return when (val typeDeclaration = resolvedType.declaration) {
            is KSClassDeclaration -> {
                if (typeDeclaration.typeParameters.isEmpty()) {
                    logger.info("Processed type $typeDeclaration: it's a class, we may need a generator for it")
                    typeDeclaration
                } else if (iterableDeclaration.isAssignableFrom(resolvedType)) {
                    val elementType = resolvedType.arguments.firstOrNull()?.type?.resolve()
                    logger.info("Processed type $typeDeclaration: it's an Iterable, we may need a generator for the element type $elementType")
                    elementType?.let { findModel(it) }
                } else {
                    logger.warn("Processed type $typeDeclaration: cannot generate selectors for class with type parameters")
                    null
                }
            }

            is KSTypeParameter -> {
                logger.warn("Processed type $typeDeclaration: cannot generate helpers for generic type parameters")
                null
            }

            else -> {
                throw MissingImplementation("Processed type $typeDeclaration: property type kind ${typeDeclaration::class} is not supported")
            }
        }
    }
}

