package no.nav.pensjon.brev.skribenten.brevredigering.application

import no.nav.pensjon.brev.skribenten.brevredigering.domain.*

object BrevredigeringFacadeFactory {

    fun create(): BrevredigeringFacade {
        val brevreservasjonPolicy = BrevreservasjonPolicy()

        return BrevredigeringFacade(
            brevreservasjonPolicy = brevreservasjonPolicy,
        )
    }
}