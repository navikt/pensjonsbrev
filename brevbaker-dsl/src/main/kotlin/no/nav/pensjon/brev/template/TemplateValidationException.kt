package no.nav.pensjon.brev.template

abstract class TemplateValidationException(msg: String) : Exception(msg)
class MissingScopeForNextItemEvaluationException(msg: String) : TemplateValidationException(msg)
class InvalidScopeTypeException(msg: String) : TemplateValidationException(msg)
class InvalidTableDeclarationException(msg: String) : TemplateValidationException(msg)
class InvalidListDeclarationException(msg: String) : TemplateValidationException(msg)
class MissingTitleInTemplateException(msg: String) : TemplateValidationException(msg)
