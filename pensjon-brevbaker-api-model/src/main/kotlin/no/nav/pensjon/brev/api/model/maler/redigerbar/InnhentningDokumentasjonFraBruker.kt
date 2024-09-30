package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import java.time.LocalDate

@Suppress("unused")
data class InnhentningDokumentasjonFraBrukerDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: BrevData
) : RedigerbarBrevdata<InnhentningDokumentasjonFraBrukerDto.SaksbehandlerValg, InnhentningDokumentasjonFraBrukerDto.BrevData> {

    data class SaksbehandlerValg(
        val inkluderInnhentingInntekt: Boolean,
    ) : BrevbakerBrevdata

    data class BrevData(
        val avsenderEnhetNavn: String,
        val avsenderEnhetAdresselinje1: String,
        val avsenderEnhetAdresselinje2: String,
        val avsenderEnhetLand: String?,
    ): BrevbakerBrevdata
}