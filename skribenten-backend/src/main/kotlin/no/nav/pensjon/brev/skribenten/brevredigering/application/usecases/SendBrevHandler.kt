package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevmalFinnesIkke
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MottakerType
import no.nav.pensjon.brev.skribenten.brevredigering.domain.SendBrevPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.BrevService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.model.Pen.SendRedigerbartBrevRequest.Adresse
import java.sql.Connection

class SendBrevHandler(
    private val sendBrevPolicy: SendBrevPolicy,
    private val brevService: BrevService,
    private val brevmalService: BrevmalService,
) : BrevredigeringHandler<SendBrevHandler.Request, Dto.SendBrevResult> {

    data class Request(
        override val brevId: BrevId,
        val adresse: Adresse?
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.SendBrevResult, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val document = brev.document ?: return null

        sendBrevPolicy.kanSende(brev, document).onError { return failure(it) }
        val template = brevmalService.getRedigerbarTemplate(brev.brevkode)
            ?: return failure(BrevmalFinnesIkke(brev.brevkode))

        val adresse = request.adresse ?: return failure(SendBrevPolicy.KanIkkeSende.ManglerAdresse(request.brevId))

        val response = brevService.sendbrev(
            sendRedigerbartBrevRequest = Pen.SendRedigerbartBrevRequest(
                dokumentDato = document.dokumentDato,
                saksId = brev.saksId,
                enhetId = brev.avsenderEnhetId,
                templateDescription = template,
                brevkode = brev.brevkode,
                pdf = document.pdf,
                eksternReferanseId = "skribenten:${brev.id.value.id}",
                mottaker = brev.mottaker?.toPen(),
                adresse = adresse
            ),
            distribuer = brev.distribusjonstype == Distribusjonstype.SENTRALPRINT,
        )

        if (response.journalpostId != null) {
            if (response.error == null) {
                brev.delete()
            } else {
                brev.journalpostId = response.journalpostId
            }
        }

        return success(Dto.SendBrevResult(journalpostId = response.journalpostId, error = response.error))
    }

    fun Dto.Mottaker.toPen(): Pen.SendRedigerbartBrevRequest.Mottaker = when (type) {
        MottakerType.SAMHANDLER -> Pen.SendRedigerbartBrevRequest.Mottaker(type = Pen.SendRedigerbartBrevRequest.Mottaker.Type.TSS_ID, tssId = tssId!!)
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

    override fun requiresReservasjon(request: Request): Boolean = true
    override fun transactionIsolation(): Int = Connection.TRANSACTION_REPEATABLE_READ
}


