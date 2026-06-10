package no.nav.pensjon.brev.routing

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.RedigerbarTemplateResource
import no.nav.pensjon.brev.api.countLetter
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

        post<BestillBrevRequest<Brevkode.Redigerbart>>("/redigerbare-vedlegg/titler") { brevbestilling ->
            val titler = redigerbareBrev.renderRedigerbartVedleggTitler(brevbestilling)
            call.respond(titler)
        }

        post<BestillBrevRequest<Brevkode.Redigerbart>>("/redigerbare-vedlegg/{vedleggId}") { brevbestilling ->
            val vedleggId = call.parameters.getOrFail("vedleggId")
            when (val vedlegg = redigerbareBrev.renderRedigerbartVedlegg(brevbestilling, vedleggId)) {
                null -> call.respond(HttpStatusCode.NotFound)
                else -> call.respond(vedlegg)
            }
        }

        post<BestillRedigertBrevRequest<Brevkode.Redigerbart>>("/pdf") { brevbestilling ->
            installBrevkodeInCallContext(brevbestilling.kode)
            call.respond(redigerbareBrev.renderPDF(brevbestilling))
            countLetter(brevbestilling.kode)
        }

        post<BestillRedigertBrevRequest<Brevkode.Redigerbart>>("/html") { brevbestilling ->
            call.respond(redigerbareBrev.renderHTML(brevbestilling))
            countLetter(brevbestilling.kode)
        }

        get("/alltidValgbareVedlegg") {
            call.respond(redigerbareBrev.alltidValgbareVedlegg.map { it.kode })
        }
    }
}