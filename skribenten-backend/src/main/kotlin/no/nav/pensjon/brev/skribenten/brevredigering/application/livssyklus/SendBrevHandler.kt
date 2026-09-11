package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevmalFinnesIkke
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.HarJournalpostId
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Distribusjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.JournalpostId
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.toPen
import no.nav.pensjon.brev.skribenten.services.ServiceException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(SendBrevHandler::class.java)

class SendBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val brevService: BrevService,
    private val brevmalService: BrevmalService,
) {

    data class Request(val brevId: BrevId, val saksId: SaksId)

    suspend operator fun invoke(request: Request): Outcome<Dto.SendBrevResult, BrevredigeringError>? =
        try {
            sendBrev(request)
        } catch (e: ServiceException) {
            (e as? HarJournalpostId)?.journalpostId?.also { markerSomArkivert(request, it, e) }
            throw e
        }

    private suspend fun sendBrev(request: Request): Outcome<Dto.SendBrevResult, BrevredigeringError>? =
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
                    brev.markerSomArkivert(response.journalpostId)
                }
            }

            success(Dto.SendBrevResult(journalpostId = response.journalpostId, error = response.error))
        }

    /**
     * PEN kan ha journalført brevet selv om sendingen feilet. Transaksjonen i [Brevtilgang.forSending] er
     * rullet tilbake når vi kommer hit, så journalposten må registreres i en egen transaksjon for ikke å gå
     * tapt. Feiler også det, logges det uten å maskere den opprinnelige feilen fra PEN.
     */
    private suspend fun markerSomArkivert(request: Request, journalpostId: JournalpostId, aarsak: ServiceException) {
        logger.warn(
            "Sending av brev ${request.brevId.id} feilet, men PEN har journalført brevet med journalpostId ${journalpostId.id}. Registrerer journalposten på brevet.",
            aarsak,
        )

        try {
            brevtilgang.forSystemendring(request.brevId, request.saksId) {
                brev.markerSomArkivert(journalpostId)
                success(Unit)
            }
        } catch (e: Exception) {
            logger.error("Klarte ikke å registrere journalpostId ${journalpostId.id} på brev ${request.brevId.id}", e)
            currentCoroutineContext().ensureActive()
        }
    }
}
