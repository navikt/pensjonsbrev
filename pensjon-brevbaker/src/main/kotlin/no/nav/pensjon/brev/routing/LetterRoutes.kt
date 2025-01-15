package no.nav.pensjon.brev.routing

import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.api.TemplateResource
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

fun Route.letterRoutes(
    autobrev: TemplateResource<Brevkode.Automatisk, AutobrevTemplate<*>>,
    redigerbareBrev: TemplateResource<Brevkode.Redigerbart, RedigerbarTemplate<*>>,
) {
    route("/${autobrev.name}") {
        post<BestillBrevRequest<Brevkode.Automatisk>>("/pdf") { brevbestilling ->
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