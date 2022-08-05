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
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return TemplateModelHelpersAnnotationProcessor(environment.codeGenerator)
    }
}

internal class TemplateModelHelpersAnnotationProcessor(private val codeGenerator: CodeGenerator) : SymbolProcessor {
    private val visitedModels = mutableSetOf<KSDeclaration>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val hasModelDeclaration = resolver.getClassDeclarationByName<HasModel<*>>()?.asStarProjectedType()
            ?: throw InitializationError("Couldn't resolve $HAS_MODEL_INTERFACE_NAME")

        val symbols = resolver.getSymbolsWithAnnotation(ANNOTATION_NAME)
        logger.info("Processing annotated symbols: ${symbols.toList()}")

        try {
            symbols.filter { it.validate() }
                .forEach { it.accept(TemplateModelHelpersVisitor(hasModelDeclaration), Unit) }
        } catch (e: TemplateModelGeneratorException) {
            logger.error(e.message, e)
            throw e
        }


        val invalidSymbols = symbols.filter { !it.validate() }.toList()
        if (invalidSymbols.isNotEmpty()) {
            logger.info("Some annotated symbols does not validate: $invalidSymbols")
        }
        return invalidSymbols
    }

    inner class TemplateModelHelpersVisitor(private val hasModelType: KSType) : KSDefaultVisitor<Unit, Unit>() {
        private val hasModelTypeParameter = hasModelType.declaration.typeParameters.first { it.simpleName.asString() == HAS_MODEL_TYPE_PARAMETER_NAME }

        override fun defaultHandler(node: KSNode, data: Unit) {
            throw UnsupportedAnnotationTarget("Annotation $ANNOTATION_NAME does not support target $node at: ${node.location}")
        }

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val className = classDeclaration.simpleName.asString()
            if (classDeclaration.classKind == ClassKind.OBJECT) {
                classDeclaration.findModelTypeFromHasModelInterface().generateModels()
            } else {
                throw UnsupportedAnnotationTarget("Annotation $ANNOTATION_NAME does not support target class kind ${classDeclaration.classKind} (only supports ${ClassKind.OBJECT}): $className")
            }
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            val propertyType = property.type.resolve()
            if (!hasModelType.isAssignableFrom(propertyType)) {
                throw InvalidTargetException("$ANNOTATION_NAME annotated target property must have a value that extends $HAS_MODEL_INTERFACE_NAME: $property at ${property.location}")
            }
            property.findModelTypeFromHasModelInterface().generateModels()
        }

        private fun KSType.generateModels() {
            visitModel(declaration).forEach { visitModel(it) }
        }

        private fun visitModel(model: KSDeclaration): List<KSClassDeclaration> =
            if (!visitedModels.contains(model)) {
                visitedModels.add(model)
                model.accept(TemplateModelVisitor(codeGenerator), TemplateModelVisitor.Data(""))
            } else {
                emptyList()
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

        private fun KSPropertyDeclaration.findModelTypeFromHasModelInterface(): KSType {
            val propertyType = type.resolve()
            if (!hasModelType.isAssignableFrom(propertyType)) {
                throw InvalidTargetException("$ANNOTATION_NAME annotated target property must have a type that extends $HAS_MODEL_INTERFACE_NAME: $this")
            }
            return searchTypeHierarchyForModelType(propertyType.toString(), type).resolve()
        }

        private fun searchTypeHierarchyForModelType(targetName: String, type: KSTypeReference): KSTypeReference {
            return if (type.resolve().declaration == hasModelType.declaration) {
                type.getTypeArgument(hasModelTypeParameter)?.type
                    ?: throw InvalidObjectTarget("Could not resolve type argument of $HAS_MODEL_TYPE_PARAMETER_NAME type parameter for $hasModelType in declaration of $targetName")
            } else {
                when(val typeDeclaration = type.resolve().declaration) {
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

        private fun KSTypeReference.getTypeArgument(typeParameter: KSTypeParameter): KSTypeArgument? {
            return resolve().let {
                it.arguments.getOrNull(it.declaration.indexOfTypeParameter(typeParameter.simpleName))
            }
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