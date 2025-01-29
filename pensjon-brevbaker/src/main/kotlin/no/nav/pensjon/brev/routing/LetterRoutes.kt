package no.nav.pensjon.brev.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

private val BREV_KODE = AttributeKey<String>("CallStartTime")
fun <T : Brevkode<T>> RoutingContext.installBrevkodeInCallContext(kode: Brevkode<T>) {
    call.attributes.put(BREV_KODE, kode.kode())
}

fun ApplicationCall.useBrevkodeFromCallContext(): String? = attributes.getOrNull(BREV_KODE)

fun Route.letterRoutes(
    autobrev: TemplateResource<Brevkode.Automatisk, AutobrevTemplate<*>>,
    redigerbareBrev: TemplateResource<Brevkode.Redigerbart, RedigerbarTemplate<*>>,
) {
    route("/${autobrev.name}") {
        post<BestillBrevRequest<Brevkode.Automatisk>>("/pdf") { brevbestilling ->
            installBrevkodeInCallContext(brevbestilling.kode)
            call.respond(autobrev.renderPDF(brevbestilling))
            autobrev.countLetter(brevbestilling.kode)
        }

        post<BestillBrevRequest<Brevkode.Automatisk>>("/pdfQueued") { brevbestilling ->
            installBrevkodeInCallContext(brevbestilling.kode)// TODO trenger vi denne?
            // TODO flytt inn i request?
            val orderId = call.request.headers["orderId"] ?: throw IllegalArgumentException("orderId er påkrevd")
            autobrev.renderPdfAsync(orderId, brevbestilling)
            autobrev.countLetter(brevbestilling.kode)
            call.respond(HttpStatusCode.OK)
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
            installBrevkodeInCallContext(brevbestilling.kode)
            call.respond(redigerbareBrev.renderPDF(brevbestilling))
            redigerbareBrev.countLetter(brevbestilling.kode)
        }

        post<BestillRedigertBrevRequest<Brevkode.Redigerbart>>("/html") { brevbestilling ->
            call.respond(redigerbareBrev.renderHTML(brevbestilling))
            redigerbareBrev.countLetter(brevbestilling.kode)
        }
    }
}