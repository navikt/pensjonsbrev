package no.nav.pensjon.brev.template.dsl.helpers

import com.google.devtools.ksp.symbol.KSNode

abstract class TemplateModelGeneratorException(val msg: String, val symbol: KSNode? = null) : Exception(msg)

class InitializationError(msg: String) : TemplateModelGeneratorException(msg)

class UnsupportedAnnotationTarget(msg: String, symbol: KSNode) : TemplateModelGeneratorException(msg, symbol)

class InvalidVisitorState(msg: String) : TemplateModelGeneratorException(msg)

class InvalidModel(msg: String) : TemplateModelGeneratorException(msg)

class MissingImplementation(msg: String, symbol: KSNode) : TemplateModelGeneratorException(msg, symbol)

open class InvalidTargetException(msg: String, symbol: KSNode) : TemplateModelGeneratorException(msg, symbol)

class InvalidObjectTarget(msg: String, symbol: KSNode) : InvalidTargetException(msg, symbol)
