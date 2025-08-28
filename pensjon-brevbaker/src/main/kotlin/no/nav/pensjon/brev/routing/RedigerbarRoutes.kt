package no.nav.pensjon.brev.routing

import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.RedigerbarTemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.RedigerbarTemplate


fun Route.redigerbarRoutes(
    redigerbareBrev: RedigerbarTemplateResource<Brevkode.Redigerbart, RedigerbarTemplate<*>>,
) {
    route("/${redigerbareBrev.name}") {
        post<BestillBrevRequest<Brevkode.Redigerbart>>("/markup") { brevbestilling ->
            val markup = redigerbareBrev.renderLetterMarkup(brevbestilling)
            call.respond(markup)
        }

        post<BestillBrevRequest<Brevkode.Redigerbart>>("/markup-usage") { brevbestilling ->
            val markup = redigerbareBrev.renderLetterMarkupWithDataUsage(brevbestilling)
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