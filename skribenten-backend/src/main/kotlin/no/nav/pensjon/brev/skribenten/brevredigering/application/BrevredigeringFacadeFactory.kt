package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.fagsystem.BrevService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.services.NavansattService

object BrevredigeringFacadeFactory {

    fun create(
        brevService: BrevService,
        brevdataService: BrevdataService,
        brevmalService: BrevmalService,
        navansattService: NavansattService,
    ): BrevredigeringFacade {
        val opprettBrevPolicy = OpprettBrevPolicy(brevmalService, navansattService)
        val brevreservasjonPolicy = BrevreservasjonPolicy()
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