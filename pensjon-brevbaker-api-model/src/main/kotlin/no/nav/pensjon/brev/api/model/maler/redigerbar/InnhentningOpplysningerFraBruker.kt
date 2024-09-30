package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

@Suppress("unused")
data class InnhentningOpplysningerFraBrukerDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: BrevData
) : RedigerbarBrevdata<InnhentningOpplysningerFraBrukerDto.SaksbehandlerValg, InnhentningOpplysningerFraBrukerDto.BrevData> {

    data class SaksbehandlerValg(
        val innhentingAvInntekt: Boolean,
    ) : BrevbakerBrevdata

    data class BrevData(
        val avsenderEnhetNavn: String,
        val avsenderEnhetAdresselinje1: String,
        val avsenderEnhetAdresselinje2: String,
        val avsenderEnhetLand: String?,
    ): BrevbakerBrevdata
}