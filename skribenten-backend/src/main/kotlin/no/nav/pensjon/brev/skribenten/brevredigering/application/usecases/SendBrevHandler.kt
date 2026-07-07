package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.*
import no.nav.pensjon.brev.skribenten.model.*
import org.jetbrains.exposed.v1.jdbc.Database
import java.sql.Connection

class SendBrevHandler(
    private val sendBrevPolicy: SendBrevPolicy,
    private val brevService: BrevService,
    private val brevmalService: BrevmalService,
    reserverBrevHandler: ReserverBrevHandler,
    database: Database,
) : ReservertBrevHandler<SendBrevHandler.Request, Dto.SendBrevResult>(database, reserverBrevHandler) {

    override fun transactionIsolation(): Int = Connection.TRANSACTION_REPEATABLE_READ

    data class Request(
        override val brevId: BrevId,
    ) : BrevredigeringRequest

    override suspend fun execute(request: Request): Outcome<Dto.SendBrevResult, BrevredigeringError>? {
        val brev = BrevredigeringEntity.findById(request.brevId) ?: return null
        val document = brev.document ?: return null

        sendBrevPolicy.kanSende(brev, document).onError { return failure(it) }
        val template = brevmalService.getRedigerbarTemplate(brev.brevkode)
            ?: return failure(BrevmalFinnesIkke(brev.brevkode))

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

        return success(Dto.SendBrevResult(journalpostId = response.journalpostId, error = response.error))
    }
}


