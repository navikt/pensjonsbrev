package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
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

        symbols.filterIsInstance<KSClassDeclaration>()
            .filter { it.validate() }
            .forEach { it.accept(TemplateModelHelpersVisitor(hasModelDeclaration), Unit) }


        val invalidSymbols = symbols.filter { !it.validate() }.toList()
        if (invalidSymbols.isNotEmpty()) {
            logger.info("Some annotated symbols does not validate: $invalidSymbols")
        }
        return invalidSymbols
    }

    inner class TemplateModelHelpersVisitor(private val hasModelType: KSType) : KSVisitorVoid() {

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val className = classDeclaration.simpleName.asString()
            if (classDeclaration.classKind == ClassKind.OBJECT) {
                classDeclaration.findModelTypeFromHasModelInterface()
                    .declaration
                    .let { visitModel(it) }
                    .forEach { visitModel(it) }

            } else {
                throw UnsupportedAnnotationTarget("Annotation $ANNOTATION_NAME does not support target class kind ${classDeclaration.classKind} (only supports ${ClassKind.OBJECT}): $className")
            }
        }

        private fun visitModel(model: KSDeclaration): List<KSClassDeclaration> =
            if (!visitedModels.contains(model)) {
                visitedModels.add(model)
                model.accept(TemplateModelVisitor(codeGenerator), TemplateModelVisitor.Data(""))
            } else {
                emptyList()
            }

        private fun KSClassDeclaration.findModelTypeFromHasModelInterface(): KSType {
            if (!hasModelType.isAssignableFrom(this.asStarProjectedType())) {
                throw InvalidObjectTarget("$ANNOTATION_NAME annotated target $classKind must extend $HAS_MODEL_INTERFACE_NAME: $this")
            }

            return searchSuperTypeHierarchyForModelType(this.simpleName.asString(), superTypes).resolve()
        }

        private fun searchSuperTypeHierarchyForModelType(targetClassName: String, superTypes: Sequence<KSTypeReference>): KSTypeReference {
            val directSuperType = superTypes.firstOrNull { it.resolve().declaration == hasModelType.declaration }
            return if (directSuperType != null) {
                directSuperType.element?.typeArguments
                    ?.get(directSuperType.resolve().declaration.indexOfTypeParameter(HAS_MODEL_TYPE_PARAMETER_NAME))
                    ?.type
                    ?: throw InvalidObjectTarget("Could not resolve type argument of $HAS_MODEL_TYPE_PARAMETER_NAME type parameter for $hasModelType in declaration of $targetClassName")
            } else {
                val superTypeDescendantOfHasModel = superTypes.firstOrNull { hasModelType.isAssignableFrom(it.resolve()) }
                    ?: throw InvalidObjectTarget("Found no super types of $targetClassName that is descendant of $hasModelType")

                val declaration = superTypeDescendantOfHasModel.resolve().declaration

                if (declaration is KSClassDeclaration) {
                    val ascendantTypeArgName = searchSuperTypeHierarchyForModelType(targetClassName, declaration.superTypes).resolve().declaration.simpleName
                    val positionOfTypeArgToHasModel = declaration.typeParameters.indexOfFirst { it.simpleName == ascendantTypeArgName }

                    superTypeDescendantOfHasModel.element?.typeArguments?.getOrNull(positionOfTypeArgToHasModel)?.type
                        ?: throw InvalidObjectTarget("Could not resolve type argument '$ascendantTypeArgName' in $superTypeDescendantOfHasModel")
                } else {
                    throw MissingImplementation("Expected declaration of super type (that is descendant of $hasModelType) of $targetClassName to be KSClassDeclaration, but was ${declaration::class}")
                }
            }
        }

        private fun KSDeclaration.indexOfTypeParameter(argumentName: String): Int {
            val index = typeParameters.indexOfFirst { it.simpleName.asString() == argumentName }
            return if (index >= 0) {
                index
            } else {
                throw InvalidObjectTarget("Could not find index of type argument '$argumentName' for $this")
            }
        }
    }
}