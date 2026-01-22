package no.nav.pensjon.brev.skribenten.services

import no.nav.pensjon.brev.skribenten.usecase.BrevredigeringHandler
import no.nav.pensjon.brev.skribenten.usecase.BrevredigeringRequest
import no.nav.pensjon.brev.skribenten.usecase.BrevredigeringTest
import no.nav.pensjon.brev.skribenten.usecase.EndreDistribusjonstypeHandler
import no.nav.pensjon.brev.skribenten.usecase.EndreMottakerHandler
import no.nav.pensjon.brev.skribenten.usecase.HentBrevAttesteringHandler
import no.nav.pensjon.brev.skribenten.usecase.HentBrevHandler
import no.nav.pensjon.brev.skribenten.usecase.OppdaterBrevHandler
import no.nav.pensjon.brev.skribenten.usecase.OpprettBrevHandler
import no.nav.pensjon.brev.skribenten.usecase.OpprettBrevHandlerImpl
import no.nav.pensjon.brev.skribenten.usecase.VeksleKlarStatusHandler
import org.junit.jupiter.api.Test

class BrevredigeringFacadeTest : BrevredigeringTest() {
    private val facade = BrevredigeringFacade(
        opprettBrev = object : OpprettBrevHandler {
            override suspend fun handle(request: OpprettBrevHandlerImpl.Request) = notYetStubbed()
        },
        oppdaterBrev = handlerStub(),
        hentBrev = handlerStub(),
        hentBrevAttestering = handlerStub(),
        veksleKlarStatus = handlerStub(),
        endreDistribusjonstype = handlerStub(),
        endreMottaker = handlerStub()
    )

    @Test
    suspend fun `reserverer brev utfra hva handler requiresReservasjon svarer`() {
        val brev = opprettBrev().resultOrFail()

    }
}

private inline fun <reified T : BrevredigeringRequest> handlerStub() = object : BrevredigeringHandler<T> {
    override suspend fun handle(request: T) = notYetStubbed()
}

