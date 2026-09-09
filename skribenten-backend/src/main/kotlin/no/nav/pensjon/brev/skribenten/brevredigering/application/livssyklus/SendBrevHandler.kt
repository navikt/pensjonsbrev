package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevmalFinnesIkke
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Distribusjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.toPen

class SendBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val brevService: BrevService,
    private val brevmalService: BrevmalService,
) {

    data class Request(val brevId: BrevId, val saksId: SaksId)

    suspend operator fun invoke(request: Request): Outcome<Dto.SendBrevResult, BrevredigeringError>? =
        brevtilgang.forSending(request.brevId, request.saksId) { document ->
            val template = brevmalService.getRedigerbarTemplate(brev.brevkode)
                ?: return@forSending failure(BrevmalFinnesIkke(brev.brevkode))

            val response = brevService.sendbrev(
                sendRedigerbartBrevRequest = Pen.SendRedigerbartBrevRequest(
                    dokumentDato = document.dokumentDato,
                    saksId = brev.saksId,
                    enhetsId = brev.avsenderEnhetId,
                    templateDescription = template,
                    brevkode = brev.brevkode,
                    pdf = document.pdf,
                    eksternReferanseId = "skribenten:${brev.id.value.id}",
                    mottaker = brev.mottaker?.toPen(),
                ),
                distribuer = brev.distribusjonstype == Distribusjon.SENTRALPRINT,
            )

            if (response.journalpostId != null) {
                if (response.error == null) {
                    brev.delete()
                } else {
                    brev.journalpostId = response.journalpostId
                }
            }

            success(Dto.SendBrevResult(journalpostId = response.journalpostId, error = response.error))
        }
}
