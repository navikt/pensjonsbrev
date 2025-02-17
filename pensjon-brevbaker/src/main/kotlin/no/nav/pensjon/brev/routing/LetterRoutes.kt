package no.nav.pensjon.brev.routing

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.AttributeKey
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.AutobrevTemplate

private val BREV_KODE = AttributeKey<String>("CallStartTime")
fun <T : Brevkode<T>> RoutingContext.installBrevkodeInCallContext(kode: Brevkode<T>) {
    call.attributes.put(BREV_KODE, kode.kode())
}
fun ApplicationCall.useBrevkodeFromCallContext(): String? = attributes.getOrNull(BREV_KODE)

fun Route.autobrevRoutes(
    autobrev: TemplateResource<Brevkode.Automatisk, AutobrevTemplate<*>>,
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

        post<BestillBrevRequest<Brevkode.Automatisk>>("/json") {
            call.respond(autobrev.renderJSON(it))
        }
    }
}