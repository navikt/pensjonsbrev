package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.*
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import no.nav.pensjon.brev.template.HasModel

val ANNOTATION_NAME = TemplateModelHelpers::class.qualifiedName ?: throw InitializationError("Couldn't find qualified name of: ${TemplateModelHelpers::class.simpleName}")
val HAS_MODEL_INTERFACE_NAME = HasModel::class.qualifiedName ?: throw InitializationError("Couldn't find qualified name of: ${HasModel::class.simpleName}")
const val HAS_MODEL_TYPE_PARAMETER_NAME = "Model"

internal class TemplateModelHelpersAnnotationProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        TemplateModelHelpersAnnotationProcessor(environment.codeGenerator, environment.logger)
}

internal fun <T : KSNode, R> Iterable<T>.foldAccept(initial: R, visitor: KSVisitor<R, R>) =
    fold(initial) { acc, node ->
        node.accept(visitor, acc)
    }

internal class TemplateModelHelpersAnnotationProcessor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val hasModelDeclaration = resolver.getClassDeclarationByName<HasModel<*>>()?.asStarProjectedType()
            ?: throw InitializationError("Couldn't resolve $HAS_MODEL_INTERFACE_NAME")

        val iterableDeclaration = resolver.getClassDeclarationByName<Iterable<*>>()?.asStarProjectedType()
            ?: throw InitializationError("Couldn't resove Iterable<*>")


        return try {
            val (validSymbols, invalidSymbols) = resolver.getSymbolsWithAnnotation(ANNOTATION_NAME).toList()
                .also { logger.info("Processing annotated symbols: $it") }
                .partition { it.validate() }

            val selectors = validSymbols.foldAccept(SelectorModels(), TemplateModelHelpersTargetVisitor(resolver, hasModelDeclaration, iterableDeclaration))

            SelectorCodeGenerator(selectors.needed).generateCode(codeGenerator)

            if (invalidSymbols.isNotEmpty()) {
                logger.warn("Some annotated symbols does not validate: $invalidSymbols")
            }

            invalidSymbols
        } catch (e: TemplateModelGeneratorException) {
            logger.error(e.msg, e.symbol)
            throw e
        }
    }

    inner class TemplateModelHelpersTargetVisitor(
        private val resolver: Resolver,
        private val hasModelType: KSType,
        private val iterableDeclaration: KSType,
    ) : KSDefaultVisitor<SelectorModels, SelectorModels>() {
        private val hasModelTypeParameter = hasModelType.declaration.typeParameters.first { it.simpleName.asString() == HAS_MODEL_TYPE_PARAMETER_NAME }

        override fun defaultHandler(node: KSNode, data: SelectorModels): SelectorModels {
            throw UnsupportedAnnotationTarget("Annotation $ANNOTATION_NAME does not support target $node at: ${node.location}", node)
        }

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: SelectorModels): SelectorModels {
            if (classDeclaration.containingFile == null) {
                logger.warn("Cannot determine source file for @$ANNOTATION_NAME annotated class: generated selectors will not have dependencies", classDeclaration)
            }

            val modelVisitor = TemplateModelVisitor(iterableDeclaration, logger, classDeclaration.containingFile)

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
                logger.info("@$ANNOTATION_NAME does not support target class kind ${classDeclaration.classKind}: skipping it, but will process additionalModels $additionalModels", classDeclaration)
                fromAdditionalModels
            } else {
                throw UnsupportedAnnotationTarget("@$ANNOTATION_NAME does not support target class kind ${classDeclaration.classKind} (only supports ${ClassKind.OBJECT}): $className", classDeclaration)
            }
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: SelectorModels): SelectorModels {
            if (property.containingFile == null) {
                logger.warn("Cannot determine source file for @$ANNOTATION_NAME annotated property: generated selectors will not have dependencies", property)
            }

            if (property.getAdditionalModelsFromAnnotation().any()) {
                throw MissingImplementation("@$ANNOTATION_NAME does not support additionalModels for properties", property)
            }

            val propertyType = property.type.resolve()
            if (!hasModelType.isAssignableFrom(propertyType)) {
                throw InvalidTargetException("$ANNOTATION_NAME annotated target property must have a value that extends $HAS_MODEL_INTERFACE_NAME", property)
            }
            return searchTypeHierarchyForModelType(propertyType.toString(), propertyType)
                .declaration
                .accept(TemplateModelVisitor(iterableDeclaration, logger, property.containingFile), data)
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
                throw InvalidObjectTarget("@$ANNOTATION_NAME annotated target $classKind must extend $HAS_MODEL_INTERFACE_NAME", this)
            }
            if (typeParameters.isNotEmpty()) {
                throw MissingImplementation("@$ANNOTATION_NAME annotated target $classKind cannot have generic type parameters: $typeParameters", this)
            }

            return searchTypeHierarchyForModelType(simpleName.asString(), superTypeThatExtendsHasModel().resolve())
        }

        /**
         * Walks up the `HasModel<Model>` hierarchy starting from [type], resolving what concrete type the `Model`
         * type parameter of [HasModel] ultimately refers to.
         *
         * [type] is always expressed "as seen from its own use site": the outermost call passes the type as
         * declared on the annotated object/property, while each recursive call passes the supertype-clause as
         * literally declared on [type]'s own declaration - which is why the recursive call can still refer to that
         * declaration's own formal type parameters (they are not yet substituted at that point).
         *
         * Because of this, each recursion level must substitute its own declaration's type parameters - wherever
         * they occur in the result of the recursive call, whether as a bare type parameter or nested inside another
         * generic type argument (e.g. `RedigerbarBrevdata<FagData>`) - with the concrete arguments carried by [type],
         * before returning. This is what [substituteTypeParameters] does; without it, a type parameter nested inside
         * another generic would be returned unsubstituted (and later crash when visited as if it were a real model).
         */
        private fun searchTypeHierarchyForModelType(targetName: String, type: KSType): KSType {
            val typeDeclaration = type.declaration
            return if (typeDeclaration == hasModelType.declaration) {
                type.arguments.getOrNull(typeDeclaration.indexOfTypeParameter(hasModelTypeParameter.simpleName))?.type?.resolve()
                    ?: throw InvalidObjectTarget("Could not resolve type argument of $HAS_MODEL_TYPE_PARAMETER_NAME type parameter for $hasModelType in declaration of $targetName", typeDeclaration)
            } else {
                when (typeDeclaration) {
                    is KSClassDeclaration -> {
                        val superTypeModel = searchTypeHierarchyForModelType(targetName, typeDeclaration.superTypeThatExtendsHasModel().resolve())

                        val substitution = typeDeclaration.typeParameters
                            .mapIndexedNotNull { index, typeParameter ->
                                type.arguments.getOrNull(index)?.type?.resolve()?.let { typeParameter.simpleName.asString() to it }
                            }
                            .toMap()

                        substituteTypeParameters(targetName, superTypeModel, substitution)
                    }

                    else -> throw MissingImplementation("Don't know how to handle a model of type ${typeDeclaration::class}: $type", typeDeclaration)
                }
            }
        }

        /**
         * Recursively replaces any type parameter found (by simple name) in [substitution] within [type] - whether
         * [type] itself is a bare type parameter, or the type parameter occurs nested inside one of [type]'s own
         * type arguments - with its substituted type.
         */
        private fun substituteTypeParameters(targetName: String, type: KSType, substitution: Map<String, KSType>): KSType {
            val declaration = type.declaration
            if (declaration is KSTypeParameter) {
                return substitution[declaration.simpleName.asString()]
                    ?: throw InvalidTargetException("Could not resolve type argument of ${declaration.simpleName.asString()} type parameter in declaration of $targetName", declaration)
            }

            if (type.arguments.isEmpty()) {
                return type
            }

            val newArguments = type.arguments.map { argument ->
                val argumentType = argument.type?.resolve()
                    ?: throw MissingImplementation("Couldn't determine type of type argument $argument for $type", declaration)
                val substituted = substituteTypeParameters(targetName, argumentType, substitution)
                resolver.getTypeArgument(resolver.createKSTypeReferenceFromKSType(substituted), argument.variance)
            }

            return type.replace(newArguments)
        }

        private fun KSDeclaration.indexOfTypeParameter(argumentName: KSName): Int {
            val index = typeParameters.indexOfFirst { it.simpleName == argumentName }
            return if (index >= 0) {
                index
            } else {
                throw InvalidObjectTarget("Could not find index of type argument '${argumentName.asString()}' for $this", this)
            }
        }

        private fun KSClassDeclaration.superTypeThatExtendsHasModel(): KSTypeReference =
            superTypes.firstOrNull { hasModelType.isAssignableFrom(it.resolve()) }
                ?: throw InvalidTargetException("Could not find a superType of $this that extends $hasModelType", this)
    }
}