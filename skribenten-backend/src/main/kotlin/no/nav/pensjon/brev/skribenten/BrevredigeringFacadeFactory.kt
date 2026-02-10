package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.skribenten.domain.*
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.*

object BrevredigeringFacadeFactory {

    fun create(
        brevbakerService: BrevbakerService,
        penService: PenService,
        samhandlerService: SamhandlerService,
        navansattService: NavansattService,
    ): BrevredigeringFacade {
        val renderService = RenderService(brevbakerService)
        val brevdataService = BrevdataService(penService, samhandlerService)

        val redigerBrevPolicy = RedigerBrevPolicy()
        val opprettBrevPolicy = OpprettBrevPolicy(brevbakerService, navansattService)
        val brevreservasjonPolicy = BrevreservasjonPolicy()
        val attesterBrevPolicy = AttesterBrevPolicy()
        val klarTilSendingPolicy = KlarTilSendingPolicy()

        return BrevredigeringFacade(
            opprettBrev = OpprettBrevHandlerImpl(
                opprettBrevPolicy = opprettBrevPolicy,
                brevreservasjonPolicy = brevreservasjonPolicy,
                renderService = renderService,
                brevdataService = brevdataService,
                navansattService = navansattService,
            ),
            oppdaterBrev = OppdaterBrevHandler(
                redigerBrevPolicy = redigerBrevPolicy,
                renderService = renderService,
                brevdataService = brevdataService
            ),
            hentBrev = HentBrevHandler(
                redigerBrevPolicy = redigerBrevPolicy,
                renderService = renderService,
                brevdataService = brevdataService,
            ),
            hentBrevAttestering = HentBrevAttesteringHandler(
                attesterBrevPolicy = attesterBrevPolicy,
                redigerBrevPolicy = redigerBrevPolicy,
                renderService = renderService,
                brevdataService = brevdataService,
                navansattService = navansattService,
            ),
            veksleKlarStatus = VeksleKlarStatusHandler(
                klarTilSendingPolicy = klarTilSendingPolicy,
                redigerBrevPolicy = redigerBrevPolicy
            ),
            endreDistribusjonstype = EndreDistribusjonstypeHandler(
                redigerBrevPolicy = redigerBrevPolicy
            ),
            endreMottaker = EndreMottakerHandler(
                redigerBrevPolicy = redigerBrevPolicy,
                brevdataService = brevdataService
            ),
            reserverBrev = ReserverBrevHandler(
                brevreservasjonPolicy = brevreservasjonPolicy
            )
        )
    }

}