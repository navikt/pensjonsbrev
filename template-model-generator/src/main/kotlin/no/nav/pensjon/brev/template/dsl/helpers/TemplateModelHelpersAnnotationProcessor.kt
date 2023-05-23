package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import no.nav.pensjon.brev.template.*
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(TemplateModelHelpersAnnotationProcessor::class.java)
private val ANNOTATION_NAME = TemplateModelHelpers::class.qualifiedName ?: throw InitializationError("Couldn't find qualified name of: ${TemplateModelHelpers::class.simpleName}")
private val HAS_MODEL_INTERFACE_NAME = HasModel::class.qualifiedName ?: throw InitializationError("Couldn't find qualified name of: ${HasModel::class.simpleName}")
private val HAS_MODEL_TYPE_PARAMETER_NAME = HasModel::class.typeParameters.first().name

internal class TemplateModelHelpersAnnotationProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        TemplateModelHelpersAnnotationProcessor(environment.codeGenerator)
}

internal fun <T : KSNode, R> Iterable<T>.foldAccept(initial: R, visitor: KSVisitor<R, R>) =
    fold(initial) { acc, node ->
        node.accept(visitor, acc)
    }

internal class TemplateModelHelpersAnnotationProcessor(private val codeGenerator: CodeGenerator) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val hasModelDeclaration = resolver.getClassDeclarationByName<HasModel<*>>()?.asStarProjectedType()
            ?: throw InitializationError("Couldn't resolve $HAS_MODEL_INTERFACE_NAME")

        val iterableDeclaration = resolver.getClassDeclarationByName<Iterable<*>>()?.asStarProjectedType()
            ?: throw InitializationError("Couldn't resove Iterable<*>")


        return try {
            val (validSymbols, invalidSymbols) = resolver.getSymbolsWithAnnotation(ANNOTATION_NAME).toList()
                .also { logger.info("Processing annotated symbols: $it") }
                .partition { it.validate() }

            val selectors = validSymbols.foldAccept(SelectorModels(), TemplateModelHelpersTargetVisitor(hasModelDeclaration, iterableDeclaration))

            SelectorCodeGenerator(selectors.needed).generateCode(codeGenerator)

            if (invalidSymbols.isNotEmpty()) {
                logger.info("Some annotated symbols does not validate: $invalidSymbols")
            }

            invalidSymbols
        } catch (e: TemplateModelGeneratorException) {
            logger.error(e.message, e)
            throw e
        }
    }

    inner class TemplateModelHelpersTargetVisitor(
        private val hasModelType: KSType,
        private val iterableDeclaration: KSType,
    ) : KSDefaultVisitor<SelectorModels, SelectorModels>() {
        private val hasModelTypeParameter = hasModelType.declaration.typeParameters.first { it.simpleName.asString() == HAS_MODEL_TYPE_PARAMETER_NAME }

        override fun defaultHandler(node: KSNode, data: SelectorModels): SelectorModels {
            throw UnsupportedAnnotationTarget("Annotation $ANNOTATION_NAME does not support target $node at: ${node.location}")
        }

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: SelectorModels): SelectorModels {
            val modelVisitor = TemplateModelVisitor(iterableDeclaration)

            val className = classDeclaration.simpleName.asString()
            val additionalModels = classDeclaration.getAdditionalModelsFromAnnotation().toList()
            val fromAdditionalModels = additionalModels.foldAccept(data, modelVisitor)

            return if (classDeclaration.classKind == ClassKind.OBJECT) {
                val modelType = classDeclaration.findModelTypeFromHasModelInterface()
                val fromDeclaration = modelType.declaration.accept(modelVisitor, fromAdditionalModels)

                modelType.arguments.mapNotNull { it.type?.resolve()?.declaration }
                    .foldAccept(fromDeclaration, modelVisitor)
            } else if (additionalModels.isNotEmpty()) {
                // Annotation target is not a class of type object, but we do have additionalModels, so it is not an error.
                logger.info("Annotation $ANNOTATION_NAME does not support target class kind ${classDeclaration.classKind} $className at ${classDeclaration.location}: skipping it")
                fromAdditionalModels
            } else {
                throw UnsupportedAnnotationTarget("Annotation $ANNOTATION_NAME does not support target class kind ${classDeclaration.classKind} (only supports ${ClassKind.OBJECT}): $className at ${classDeclaration.location}")
            }
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: SelectorModels): SelectorModels {
            if (property.getAdditionalModelsFromAnnotation().any()) {
                throw MissingImplementation("Annotation $ANNOTATION_NAME does not support additionalModels for target $property at: ${property.location}")
            }

            val propertyType = property.type.resolve()
            if (!hasModelType.isAssignableFrom(propertyType)) {
                throw InvalidTargetException("$ANNOTATION_NAME annotated target property must have a value that extends $HAS_MODEL_INTERFACE_NAME: $property at ${property.location}")
            }
            return searchTypeHierarchyForModelType(propertyType.toString(), property.type)
                .resolve()
                .declaration
                .accept(TemplateModelVisitor(iterableDeclaration), data)
        }

        private fun KSAnnotated.getAdditionalModelsFromAnnotation(): Sequence<KSClassDeclaration> {
            return annotations.filter {
                it.shortName.getShortName() == TemplateModelHelpers::class.simpleName &&
                        it.annotationType.resolve().declaration.qualifiedName?.asString() == TemplateModelHelpers::class.qualifiedName
            }.flatMap { it.arguments }
                .filter { it.name?.getShortName() == "additionalModels" }
                .map { it.value }
                .filterIsInstance<Collection<*>>()
                .flatMap { it.filterIsInstance<KSType>() }
                .map { it.declaration }
                .filterIsInstance<KSClassDeclaration>()
        }

        private fun KSClassDeclaration.findModelTypeFromHasModelInterface(): KSType {
            if (!hasModelType.isAssignableFrom(asStarProjectedType())) {
                throw InvalidObjectTarget("$ANNOTATION_NAME annotated target $classKind must extend $HAS_MODEL_INTERFACE_NAME: $this")
            }
            if (typeParameters.isNotEmpty()) {
                throw MissingImplementation("$ANNOTATION_NAME annotated target $classKind cannot have generic type parameters: $typeParameters")
            }

            return searchTypeHierarchyForModelType(simpleName.asString(), superTypeThatExtendsHasModel()).resolve()
        }

        private fun searchTypeHierarchyForModelType(targetName: String, type: KSTypeReference): KSTypeReference {
            return if (type.resolve().declaration == hasModelType.declaration) {
                type.getTypeArgument(hasModelTypeParameter)?.type
                    ?: throw InvalidObjectTarget("Could not resolve type argument of $HAS_MODEL_TYPE_PARAMETER_NAME type parameter for $hasModelType in declaration of $targetName")
            } else {
                when (val typeDeclaration = type.resolve().declaration) {
                    is KSClassDeclaration -> {
                        val superTypeModel = searchTypeHierarchyForModelType(targetName, typeDeclaration.superTypeThatExtendsHasModel())

                        when (val superTypeModelDeclaration = superTypeModel.resolve().declaration) {
                            is KSClassDeclaration -> {
                                superTypeModel
                            }

                            is KSTypeParameter -> {
                                type.getTypeArgument(superTypeModelDeclaration)?.type
                                    ?: throw InvalidTargetException("Could not resolve type argument of $superTypeModelDeclaration type parameter for $type in declaration of $targetName")
                            }

                            else -> throw MissingImplementation("Don't know how to handle a superType model of type ${superTypeModelDeclaration::class}: $superTypeModel")
                        }
                    }

                    else -> throw MissingImplementation("Don't know how to handle a model of type ${typeDeclaration::class}: $type")
                }
            }
        }

        private fun KSTypeReference.getTypeArgument(typeParameter: KSTypeParameter): KSTypeArgument? =
            resolve().let {
                it.arguments.getOrNull(it.declaration.indexOfTypeParameter(typeParameter.simpleName))
            }

        private fun KSDeclaration.indexOfTypeParameter(argumentName: KSName): Int {
            val index = typeParameters.indexOfFirst { it.simpleName == argumentName }
            return if (index >= 0) {
                index
            } else {
                throw InvalidObjectTarget("Could not find index of type argument '${argumentName.asString()}' for $this")
            }
        }

        private fun KSClassDeclaration.superTypeThatExtendsHasModel(): KSTypeReference =
            superTypes.firstOrNull { hasModelType.isAssignableFrom(it.resolve()) }
                ?: throw InvalidTargetException("Could not find a superType of $this that extends $hasModelType")
    }
}