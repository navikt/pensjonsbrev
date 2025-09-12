package no.nav.pensjon.brev.routing

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.AttributeKey
import no.nav.brev.brevbaker.AutoMal
import no.nav.pensjon.brev.api.AutobrevTemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode

private val BREV_KODE = AttributeKey<String>("BrevKode")
fun <T : Brevkode<T>> RoutingContext.installBrevkodeInCallContext(kode: Brevkode<T>) {
    call.attributes.put(BREV_KODE, kode.kode())
}

fun ApplicationCall.useBrevkodeFromCallContext(): String? = attributes.getOrNull(BREV_KODE)

fun Route.autobrevRoutes(
    autobrev: AutobrevTemplateResource<Brevkode.Automatisk, AutoMal<*>>,
) {
    route("/${autobrev.name}") {
        post<BestillBrevRequest<Brevkode.Automatisk>>("/pdf") { brevbestilling ->
            installBrevkodeInCallContext(brevbestilling.kode)
            call.respond(autobrev.renderPDF(brevbestilling))
            autobrev.countLetter(brevbestilling.kode)
        }

        post<BestillBrevRequest<Brevkode.Automatisk>>("/html") { brevbestilling ->
            call.respond(autobrev.renderHTML(brevbestilling))
            autobrev.countLetter(brevbestilling.kode)
        }

        post<BestillBrevRequest<Brevkode.Automatisk>>("/json") { brevbestilling ->
            call.respond(autobrev.renderJSON(brevbestilling))
        }
    }
}