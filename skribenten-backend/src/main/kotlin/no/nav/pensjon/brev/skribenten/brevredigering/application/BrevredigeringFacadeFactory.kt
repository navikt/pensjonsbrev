package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevbaker.RenderService
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.fagsystem.BrevService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.P1Service
import no.nav.pensjon.brev.skribenten.services.NavansattService

object BrevredigeringFacadeFactory {

    fun create(
        brevService: BrevService,
        brevdataService: BrevdataService,
        brevmalService: BrevmalService,
        navansattService: NavansattService,
        p1Service: P1Service,
        renderService: RenderService,
    ): BrevredigeringFacade {
        val redigerBrevPolicy = RedigerBrevPolicy()
        val opprettBrevPolicy = OpprettBrevPolicy(brevmalService, navansattService)
        val brevreservasjonPolicy = BrevreservasjonPolicy()
        val attesterBrevPolicy = AttesterBrevPolicy()
        val ferdigRedigertPolicy = FerdigRedigertPolicy()
        val sendBrevPolicy = SendBrevPolicy(ferdigRedigertPolicy)

        return BrevredigeringFacade(
            opprettBrev = OpprettBrevHandlerImpl(
                opprettBrevPolicy = opprettBrevPolicy,
                brevreservasjonPolicy = brevreservasjonPolicy,
                brevmalService = brevmalService,
                brevdataService = brevdataService,
                navansattService = navansattService,
            ),
            reserverBrev = ReserverBrevHandler(
                brevreservasjonPolicy = brevreservasjonPolicy
            ),
            frigiReservasjon = FrigiReservasjonHandler(
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            hentEllerOpprettPdf = HentEllerOpprettPdfHandler(
                brevdataService = brevdataService,
                renderService = renderService,
                brevmalService = brevmalService,
                p1Service = p1Service,
            ),
            hentRedigertVedlegg = HentRedigertVedleggHandler(
                brevmalService = brevmalService,
                brevdataService = brevdataService,
            ),
            hentRedigerbareVedlegg = HentRedigerbareVedleggHandler(
                brevmalService = brevmalService,
                brevdataService = brevdataService,
            ),
            slettRedigertVedlegg = SlettRedigertVedleggHandler(
                redigerBrevPolicy = redigerBrevPolicy,
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            sendBrev = SendBrevHandler(
                sendBrevPolicy = sendBrevPolicy,
                brevService = brevService,
                brevmalService = brevmalService,
            ),
            slettBrev = SlettBrevHandler(),
            brevreservasjonPolicy = brevreservasjonPolicy,
            diffBrev = DiffBrevHandler(
                brevmalService = brevmalService,
                brevdataService = brevdataService,
            ),
        )
    }
}