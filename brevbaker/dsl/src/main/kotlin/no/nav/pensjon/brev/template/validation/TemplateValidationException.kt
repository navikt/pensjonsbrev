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
        if (internalFrames.size < allFrames.size) {
            stackTrace = allFrames.drop(internalFrames.size).toTypedArray()
        }
    }
}
class MissingScopeForNextItemEvaluationException(msg: String) : TemplateValidationException(msg)
class InvalidScopeTypeException(msg: String) : TemplateValidationException(msg)
class InvalidTableDeclarationException(msg: String) : TemplateValidationException(msg)
class InvalidListDeclarationException(msg: String) : TemplateValidationException(msg)
class MissingTitleInTemplateException(msg: String) : TemplateValidationException(msg)

/**
 * Indikerer at malen allerede inneholder en blokk (title1, title2, title3, paragraph) som har samme tekstlig innhold (stableHashCode identitet).
 * Dette er et problem for redigerbare maler fordi det brukes av algoritmen som oppdaterer det redigerte brevet mot malen.
 * Om unntaket blir kastet på en `includePhrase` så betyr det frasen inneholder en blokk som også eksisterer der hvor du inkluderer frasen,
 * husk at dette kan bety at to fraser som brukes i brevet har samme blokk.
 *
 * Man kan komme seg rundt problemet ved å angi argumentet `uniqueness` til blokken det gjelder.
 */
class DuplicateBlockIdentity(msg: String) : TemplateValidationException(msg)