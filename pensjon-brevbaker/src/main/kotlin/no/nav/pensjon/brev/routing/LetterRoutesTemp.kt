package no.nav.pensjon.brev.routing

import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@Suppress("unused")
data class LetterResponse(val base64pdf: ByteArray, val contentType: String, val letterMetadata: LetterMetadata) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LetterResponse

        if (!base64pdf.contentEquals(other.base64pdf)) return false
        if (contentType != other.contentType) return false
        if (letterMetadata != other.letterMetadata) return false

        return true
    }

    override fun hashCode(): Int {
        var result = base64pdf.contentHashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + letterMetadata.hashCode()
        return result
    }
}

fun Route.letterRoutesTemp(
    autobrev: TemplateResource<Brevkode.Automatisk, AutobrevTemplate<*>>,
    redigerbareBrev: TemplateResource<Brevkode.Redigerbart, RedigerbarTemplate<*>>,
) {
    route("/${autobrev.name}") {
        post<BestillBrevRequest<Brevkode.Automatisk>>("/pdf") { brevbestilling ->
            call.respond(autobrev.renderPDF(brevbestilling).let {
                LetterResponse(
                    base64pdf = it.file,
                    contentType = it.contentType,
                    letterMetadata = it.letterMetadata
                )
            })
            autobrev.countLetter(brevbestilling.kode)
        }

        post<BestillBrevRequest<Brevkode.Automatisk>>("/html") { brevbestilling ->
            call.respond(autobrev.renderHTML(brevbestilling))
            autobrev.countLetter(brevbestilling.kode)
        }

        post<BestillBrevRequest<Brevkode.Automatisk>>("/json") {
            call.respond(autobrev.renderJSON(it))
        }
    }
    route("/${redigerbareBrev.name}") {
        post<BestillBrevRequest<Brevkode.Redigerbart>>("/markup") { brevbestilling ->
            val markup = redigerbareBrev.renderLetterMarkup(brevbestilling)
            call.respond(markup)
        }
        post<BestillRedigertBrevRequest<Brevkode.Redigerbart>>("/pdf") { brevbestilling ->
            call.respond(redigerbareBrev.renderPDF(brevbestilling))
            redigerbareBrev.countLetter(brevbestilling.kode)
        }

        post<BestillRedigertBrevRequest<Brevkode.Redigerbart>>("/html") { brevbestilling ->
            call.respond(redigerbareBrev.renderHTML(brevbestilling))
            redigerbareBrev.countLetter(brevbestilling.kode)
        }
    }
}