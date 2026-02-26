package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.skribenten.brevbaker.RenderService
import no.nav.pensjon.brev.skribenten.domain.*
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.fagsystem.PenService
import no.nav.pensjon.brev.skribenten.services.BrevredigeringFacade
import no.nav.pensjon.brev.skribenten.services.NavansattService
import no.nav.pensjon.brev.skribenten.services.P1Service
import no.nav.pensjon.brev.skribenten.services.SamhandlerService
import no.nav.pensjon.brev.skribenten.usecase.*

object BrevredigeringFacadeFactory {

    fun create(
        brevmalService: BrevmalService,
        penService: PenService,
        samhandlerService: SamhandlerService,
        navansattService: NavansattService,
        p1Service: P1Service,
        renderService: RenderService,
    ): BrevredigeringFacade {
        val brevdataService = BrevdataService(penService, samhandlerService)

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
            oppdaterBrev = OppdaterBrevHandler(
                redigerBrevPolicy = redigerBrevPolicy,
                brevmalService = brevmalService,
                brevdataService = brevdataService,
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            hentBrev = HentBrevHandler(
                redigerBrevPolicy = redigerBrevPolicy,
                brevmalService = brevmalService,
                brevdataService = brevdataService,
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            hentBrevAttestering = HentBrevAttesteringHandler(
                attesterBrevPolicy = attesterBrevPolicy,
                redigerBrevPolicy = redigerBrevPolicy,
                brevmalService = brevmalService,
                brevdataService = brevdataService,
                navansattService = navansattService,
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            veksleKlarStatus = VeksleKlarStatusHandler(
                ferdigRedigertPolicy = ferdigRedigertPolicy,
                redigerBrevPolicy = redigerBrevPolicy,
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            endreDistribusjonstype = EndreDistribusjonstypeHandler(
                redigerBrevPolicy = redigerBrevPolicy,
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            endreMottaker = EndreMottakerHandler(
                redigerBrevPolicy = redigerBrevPolicy,
                brevdataService = brevdataService,
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            reserverBrev = ReserverBrevHandler(
                brevreservasjonPolicy = brevreservasjonPolicy
            ),
            hentEllerOpprettPdf = HentEllerOpprettPdfHandler(
                brevdataService = brevdataService,
                renderService = renderService,
                brevmalService = brevmalService,
                p1Service = p1Service,
            ),
            attesterBrev = AttesterBrevHandler(
                attesterBrevPolicy = attesterBrevPolicy,
                redigerBrevPolicy = redigerBrevPolicy,
                brevmalService = brevmalService,
                brevdataService = brevdataService,
                navansattService = navansattService,
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            tilbakestillBrev = TilbakestillBrevHandler(
                redigerBrevPolicy = redigerBrevPolicy,
                brevmalService = brevmalService,
                brevdataService = brevdataService,
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            endreValgteVedlegg = EndreValgteVedleggHandler(
                redigerBrevPolicy = redigerBrevPolicy,
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            sendBrev = SendBrevHandler(
                sendBrevPolicy = sendBrevPolicy,
                penService = penService,
                brevmalService = brevmalService,
            ),
            brevreservasjonPolicy = brevreservasjonPolicy,
        )
    }
}