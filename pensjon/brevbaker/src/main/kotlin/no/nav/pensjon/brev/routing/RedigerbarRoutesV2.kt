package no.nav.pensjon.brev.routing

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.api.RedigerbarTemplateResource
import no.nav.pensjon.brev.api.countLetter
import no.nav.pensjon.brev.api.model.BestillBrevRequest
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequestV2
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.RedigerbarTemplate

fun Route.redigerbarRoutesV2(
    redigerbareBrev: RedigerbarTemplateResource<Brevkode.Redigerbart, RedigerbarTemplate<*>>,
) {
    route("/${redigerbareBrev.name}") {
        post<BestillBrevRequest<Brevkode.Redigerbart>>("/markup") { brevbestilling ->
            val markup = redigerbareBrev.renderLetterMarkupV2(brevbestilling)
            call.respond(markup)
        }

        post<BestillBrevRequest<Brevkode.Redigerbart>>("/markup-usage") { brevbestilling ->
            val markup = redigerbareBrev.renderLetterMarkupWithDataUsageV2(brevbestilling)
            call.respond(markup)
        }

        post<BestillRedigertBrevRequestV2<Brevkode.Redigerbart>>("/pdf") { brevbestilling ->
            installBrevkodeInCallContext(brevbestilling.kode)
            call.respond(redigerbareBrev.renderPDFV2(brevbestilling))
            countLetter(brevbestilling.kode)
        }

        route("/redigerbare-vedlegg") {

            post<BestillBrevRequest<Brevkode.Redigerbart>>("/titler") { brevbestilling ->
                val titler = redigerbareBrev.renderRedigerbareVedleggV2Titler(brevbestilling)
                call.respond(titler)
            }

            post<BestillBrevRequest<Brevkode.Redigerbart>>("/{vedleggId}") { brevbestilling ->
                val vedleggId = call.parameters.getOrFail("vedleggId")
                when (val vedlegg = redigerbareBrev.renderRedigerbartVedleggV2Markup(brevbestilling, vedleggId)) {
                    null -> call.respond(HttpStatusCode.NotFound)
                    else -> call.respond(vedlegg)
                }
            }
        }
    }
}
