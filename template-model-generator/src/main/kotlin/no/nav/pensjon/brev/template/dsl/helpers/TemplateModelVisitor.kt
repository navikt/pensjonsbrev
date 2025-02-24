package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.symbol.ClassKind.CLASS
import com.google.devtools.ksp.symbol.ClassKind.INTERFACE
import com.google.devtools.ksp.visitor.KSDefaultVisitor

private val SKIPPED_NO_WARN_PACKAGES: Set<String> = setOf("kotlin", "java.util", "java.time")
private val SKIPPED_NO_WARN_CLASSES: Set<String> = setOf(
    "no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata",
    "no.nav.pensjon.brev.api.model.maler.EmptyBrevdata",
)

private fun KSPLogger.logSkipped(classDeclaration: KSClassDeclaration) {
    val message = "Skipping $classDeclaration: can only generate helpers for classes and interfaces"

    if (classDeclaration.packageName.asString() in SKIPPED_NO_WARN_PACKAGES
        || classDeclaration.classKind == ClassKind.ENUM_CLASS
        || classDeclaration.qualifiedName?.asString() in SKIPPED_NO_WARN_CLASSES
    ) {
        info(message, classDeclaration)
    } else {
        warn(message, classDeclaration)
    }
}

internal class TemplateModelVisitor(
    private val iterableDeclaration: KSType,
    private val logger: KSPLogger,
    private val dependency: KSFile?,
) : KSDefaultVisitor<SelectorModels, SelectorModels>() {

    override fun defaultHandler(node: KSNode, data: SelectorModels): SelectorModels {
        throw MissingImplementation("Couldn't process node $node at: ${node.location}", node)
    }

    private fun KSClassDeclaration.notInPackage(name: String): Boolean {
        val pkg = this.packageName.asString()
        val nameWithSeparator = if (name.endsWith(".")) name else "$name."

        return pkg != name && !pkg.startsWith(nameWithSeparator)
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: SelectorModels): SelectorModels =
        if (data.isVisited(classDeclaration)) {
            data.withDependency(classDeclaration, dependency)
        } else if (classDeclaration.classKind in setOf(CLASS, INTERFACE) && classDeclaration.notInPackage("java") && classDeclaration.notInPackage("kotlin")) {
            classDeclaration.getAllProperties().toList().foldAccept(data.withNeeded(classDeclaration, dependency), this)
        } else {
            logger.logSkipped(classDeclaration)
            data.withVisited(classDeclaration)
        }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: SelectorModels): SelectorModels {
        val fromProp = findModel(property.type.resolve())?.accept(this, data)
        return if (fromProp != null) {
            fromProp
        } else {
            logger.warn("Failed to create selectors for type of property '$property' of '${property.parentDeclaration}': ${property.type}")
            data
        }
    }

    private fun findModel(resolvedType: KSType): KSClassDeclaration? {
        return when (val typeDeclaration = resolvedType.declaration) {
            is KSClassDeclaration -> {
                if (typeDeclaration.typeParameters.isEmpty()) {
                    logger.info("Processed type $typeDeclaration: it's a class, we may need a generator for it")
                    typeDeclaration
                } else if (iterableDeclaration.isAssignableFrom(resolvedType) || iterableDeclaration.isAssignableFrom(resolvedType.makeNotNullable())) {
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
                throw MissingImplementation("Processed type $typeDeclaration: property type kind ${typeDeclaration::class} is not supported", typeDeclaration)
            }
        }
    }
}

