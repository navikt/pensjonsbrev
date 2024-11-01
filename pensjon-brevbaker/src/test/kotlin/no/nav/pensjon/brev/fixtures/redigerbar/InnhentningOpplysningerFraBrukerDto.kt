package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentningOpplysningerFraBrukerDto

fun createInnhentningOpplysningerFraBrukerDto() = InnhentningOpplysningerFraBrukerDto(
    saksbehandlerValg = InnhentningOpplysningerFraBrukerDto.SaksbehandlerValg(
        innhentingAvInntekt = true
    ),
    pesysData = InnhentningOpplysningerFraBrukerDto.BrevData(
        avsenderEnhetNavn = "Nav Avd 1",
        avsenderEnhetAdresselinje1 = "Adressegate 1",
        avsenderEnhetAdresselinje2 = "0001 By",
        avsenderEnhetLand = "Norge"
    )
)