package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.symbol.KSNode

internal abstract class TemplateModelGeneratorException(val msg: String, val symbol: KSNode? = null): Exception(msg)
internal class InitializationError(msg: String): TemplateModelGeneratorException(msg)
internal class UnsupportedAnnotationTarget(msg: String, symbol: KSNode): TemplateModelGeneratorException(msg, symbol)
internal class InvalidVisitorState(msg: String): TemplateModelGeneratorException(msg)
internal class InvalidModel(msg: String): TemplateModelGeneratorException(msg)
internal class MissingImplementation(msg: String, symbol: KSNode): TemplateModelGeneratorException(msg, symbol)

internal open class InvalidTargetException(msg: String, symbol: KSNode): TemplateModelGeneratorException(msg, symbol)
internal class InvalidObjectTarget(msg: String, symbol: KSNode): InvalidTargetException(msg, symbol)
