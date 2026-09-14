package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.SlettBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.Behandlingsnummer
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType

class SlettBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val slettBrevPolicy: SlettBrevPolicy,
) {

    data class Request(
        val brevId: BrevId,
        val saksId: SaksId,
        val pid: BrevbakerType.Pid,
        val behandlingsnumre: List<Behandlingsnummer>,
    )

    suspend operator fun invoke(request: Request): Outcome<Unit, BrevredigeringError>? =
        brevtilgang.forSletting(request.brevId, request.saksId) {
            slettBrevPolicy.kanSlette(brev, request.pid, request.behandlingsnumre)
                .onError { return@forSletting failure(it) }

            brev.delete()
            success(Unit)
        }
}
