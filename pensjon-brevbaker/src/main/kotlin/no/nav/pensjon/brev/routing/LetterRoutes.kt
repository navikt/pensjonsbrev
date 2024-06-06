package no.nav.pensjon.brev.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.TemplateResourceV2
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

fun Route.letterRoutes(
    autobrev: TemplateResourceV2<Brevkode.AutoBrev, AutobrevTemplate<*>>,
    redigerbareBrev: TemplateResourceV2<Brevkode.Redigerbar, RedigerbarTemplate<*>>,
) {
    route("/${autobrev.name}") {
        post<BestillBrevRequest<Brevkode.AutoBrev>>("/pdf") { brevbestilling ->
            call.respond(autobrev.renderPDF(call, brevbestilling))
            autobrev.countLetter(brevbestilling.kode)
        }

        post<BestillBrevRequest<Brevkode.AutoBrev>>("/html") { brevbestilling ->
            call.respond(autobrev.renderHTML(brevbestilling))
            autobrev.countLetter(brevbestilling.kode)
        }
    }
    route("/${redigerbareBrev.name}") {
        post<BestillBrevRequest<Brevkode.Redigerbar>>("/markup") { brevbestilling ->
            val markup = redigerbareBrev.renderLetterMarkup(brevbestilling)
            call.respond(markup)
        }

        post<BestillRedigertBrevRequest<Brevkode.Redigerbar>>("/pdf") { brevbestilling ->
            call.respond(redigerbareBrev.renderPDF(call, brevbestilling))
            redigerbareBrev.countLetter(brevbestilling.kode)
        }

        post<BestillRedigertBrevRequest<Brevkode.Redigerbar>>("/html") { brevbestilling ->
            call.respond(redigerbareBrev.renderHTML(brevbestilling))
            redigerbareBrev.countLetter(brevbestilling.kode)
        }
    }
}