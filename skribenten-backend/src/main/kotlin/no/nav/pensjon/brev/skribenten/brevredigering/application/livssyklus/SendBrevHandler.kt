package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.brevredigering.application.tilgang.Brevtilgang
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevmalFinnesIkke
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MottakerType
import no.nav.pensjon.brev.skribenten.brevredigering.domain.TssId
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
import no.nav.pensjon.brev.skribenten.services.ServiceException
import no.nav.pensjon.brev.skribenten.services.SamhandlerService
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger(SendBrevHandler::class.java)

class SendBrevHandler(
    private val brevtilgang: Brevtilgang,
    private val brevService: BrevService,
    private val brevmalService: BrevmalService,
    private val sendtBrevMetrikker: SendtBrevMetrikker,
    private val samhandlerService: SamhandlerService,
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
                    sendtBrevMetrikker.tellSendtBrev(brev)
                    brev.delete()
                } else {
                    brev.markerSomArkivert(response.journalpostId)
                }
            }

            success(Dto.SendBrevResult(journalpostId = response.journalpostId, error = response.error))
        }

    private suspend fun Dto.Mottaker.toPen(): Pen.SendRedigerbartBrevRequest.Mottaker {
        return when (type) {
            MottakerType.SAMHANDLER -> {
                val tssId = tssId!!
                hentSamhandlerOrgMottaker(tssId) ?: Pen.SendRedigerbartBrevRequest.Mottaker(
                    type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.TSS_ID,
                    tssId = tssId,
                )
            }

            MottakerType.NORSK_ADRESSE -> Pen.SendRedigerbartBrevRequest.Mottaker(
                type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.NORSK_ADRESSE,
                norskAdresse = Pen.SendRedigerbartBrevRequest.Mottaker.NorskAdresse(
                    navn = navn!!,
                    postnummer = postnummer!!,
                    poststed = poststed!!,
                    adresselinje1 = adresselinje1,
                    adresselinje2 = adresselinje2,
                    adresselinje3 = adresselinje3,
                ),
            )

            MottakerType.UTENLANDSK_ADRESSE -> Pen.SendRedigerbartBrevRequest.Mottaker(
                type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.UTENLANDSK_ADRESSE,
                utenlandskAdresse = Pen.SendRedigerbartBrevRequest.Mottaker.UtenlandsAdresse(
                    navn = navn!!,
                    landkode = landkode!!,
                    adresselinje1 = adresselinje1!!,
                    adresselinje2 = adresselinje2,
                    adresselinje3 = adresselinje3,
                ),
            )
        }
    }

    private suspend fun hentSamhandlerOrgMottaker(tssId: TssId): Pen.SendRedigerbartBrevRequest.Mottaker? {
        return if (Features.samhandlerOrgnummer.isEnabled()) {
            val response = samhandlerService.hentSamhandler(tssId)
            val samhandler = response.success

            if (samhandler == null) {
                logger.warn("Klarte ikke å hente samhandler for sending ({}). Bruker TSS_ID som mottaker.", response.failure)
                null
            } else {
                samhandler.offentligId
                    .takeIf { samhandler.idType == "ORG" && it.isNotBlank() }
                    ?.let { orgNr ->
                        Pen.SendRedigerbartBrevRequest.Mottaker(
                            type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.ORGNR,
                            organisasjon = Pen.SendRedigerbartBrevRequest.Mottaker.Organisasjon(orgNr),
                        )
                    }
            }
        } else null
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
