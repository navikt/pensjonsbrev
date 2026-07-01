package no.nav.pensjon.brev.routing

import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.AutobrevTemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.AutobrevTemplate

fun Route.autobrevRoutesV2(
    autobrev: AutobrevTemplateResource<Brevkode.Automatisk, AutobrevTemplate<*>>,
) {
    route("/${autobrev.name}") {
        post<BestillBrevRequest<Brevkode.Automatisk>>("/json") { brevbestilling ->
            call.respond(autobrev.renderLetterMarkupV2(brevbestilling))
        }
    }
}
