package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*

object BrevredigeringFacadeFactory {

    fun create(): BrevredigeringFacade {
        val brevreservasjonPolicy = BrevreservasjonPolicy()

        return BrevredigeringFacade(
            frigiReservasjon = FrigiReservasjonHandler(
                brevreservasjonPolicy = brevreservasjonPolicy,
            ),
            brevreservasjonPolicy = brevreservasjonPolicy,
        )
    }
}