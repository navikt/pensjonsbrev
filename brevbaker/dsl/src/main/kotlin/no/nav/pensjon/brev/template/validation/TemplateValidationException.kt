package no.nav.pensjon.brev.template.validation

abstract class TemplateValidationException(msg: String) : Exception(msg) {
    init {
        val allFrames = stackTrace
        val internalFrames = allFrames.takeWhile { frame ->
            frame.className.startsWith("no.nav.pensjon.brev.template")
        }
        if (internalFrames.isNotEmpty()) {
            addSuppressed(IllegalArgumentException("DSL-interne frames").apply {
                stackTrace = internalFrames.toTypedArray()
            })
        }
        stackTrace = allFrames.drop(internalFrames.size).toTypedArray()
    }
}
class MissingScopeForNextItemEvaluationException(msg: String) : TemplateValidationException(msg)
class InvalidScopeTypeException(msg: String) : TemplateValidationException(msg)
class InvalidTableDeclarationException(msg: String) : TemplateValidationException(msg)
class InvalidListDeclarationException(msg: String) : TemplateValidationException(msg)
class MissingTitleInTemplateException(msg: String) : TemplateValidationException(msg)
