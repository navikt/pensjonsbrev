package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.fagsystem.BrevdataService
import no.nav.pensjon.brev.skribenten.fagsystem.BrevmalService
import no.nav.pensjon.brev.skribenten.services.NavansattService

object BrevredigeringFacadeFactory {

    fun create(
        brevdataService: BrevdataService,
        brevmalService: BrevmalService,
        navansattService: NavansattService,
    ): BrevredigeringFacade {
        val opprettBrevPolicy = OpprettBrevPolicy(brevmalService, navansattService)
        val brevreservasjonPolicy = BrevreservasjonPolicy()

        return BrevredigeringFacade(
            opprettBrev = OpprettBrevHandlerImpl(
                opprettBrevPolicy = opprettBrevPolicy,
                brevreservasjonPolicy = brevreservasjonPolicy,
                brevmalService = brevmalService,
                brevdataService = brevdataService,
                navansattService = navansattService,
            ),
            frigiReservasjon = FrigiReservasjonHandler(
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            brevreservasjonPolicy = brevreservasjonPolicy,
        )
    }
}