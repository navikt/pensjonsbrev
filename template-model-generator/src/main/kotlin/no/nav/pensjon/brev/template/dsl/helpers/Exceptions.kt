package no.nav.pensjon.brev.template.dsl.helpers

abstract class TemplateModelGeneratorException(msg: String, cause: Throwable? = null): Exception(msg, cause)
class InitializationError(msg: String): TemplateModelGeneratorException(msg)
class UnsupportedAnnotationTarget(msg: String): TemplateModelGeneratorException(msg)
class InvalidObjectTarget(msg: String): TemplateModelGeneratorException(msg)
class InvalidVisitorState(msg: String): TemplateModelGeneratorException(msg)
class InvalidModel(msg: String): TemplateModelGeneratorException(msg)
class MissingImplementation(msg: String): TemplateModelGeneratorException(msg)