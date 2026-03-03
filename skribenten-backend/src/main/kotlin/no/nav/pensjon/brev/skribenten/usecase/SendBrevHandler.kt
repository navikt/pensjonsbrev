package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.domain.BrevmalFinnesIkke
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.SendBrevPolicy
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.toPen
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import java.sql.Connection

class SendBrevHandler(
    private val sendBrevPolicy: SendBrevPolicy,
    private val penService: PenService,
    private val brevbakerService: BrevbakerService,
) : BrevredigeringHandler<SendBrevHandler.Request, Dto.SendBrevResult> {

    data class Request(
        override val brevId: BrevId,
    ) : BrevredigeringRequest

    override suspend fun handle(request: Request): Outcome<Dto.SendBrevResult, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val document = brev.document ?: return null

        sendBrevPolicy.kanSende(brev, document).onError { return failure(it) }
        val template = brevbakerService.getRedigerbarTemplate(brev.brevkode)
            ?: return failure(BrevmalFinnesIkke(brev.brevkode))

        val response = penService.sendbrev(
            sendRedigerbartBrevRequest = Pen.SendRedigerbartBrevRequest(
                dokumentDato = document.dokumentDato,
                saksId = brev.saksId,
                enhetId = brev.avsenderEnhetId,
                templateDescription = template,
                brevkode = brev.brevkode,
                pdf = document.pdf,
                eksternReferanseId = "skribenten:${brev.id.value.id}",
                mottaker = brev.mottaker?.toPen(),
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

    override fun requiresReservasjon(request: Request): Boolean = true
    override fun transactionIsolation(): Int = Connection.TRANSACTION_REPEATABLE_READ
}


