package no.nav.pensjon.brev.converters

import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerImpl
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlereImpl

@OptIn(InterneDataklasser::class)
object FellesModule : SimpleModule() {
    private fun readResolve(): Any = FellesModule

    init {
        addInterfaceDeserializer<Foedselsnummer, FoedselsnummerImpl>()
        addInterfaceDeserializer<SignerendeSaksbehandlere, SignerendeSaksbehandlereImpl>()
    }
}