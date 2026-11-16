package no.nav.pensjon.brev.api.model.maler.ufoerApi.utland

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

data class EksportAvUforetrygdDto(
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
    override val pesysData: EksportAvUforetrygdDto.PesysData,
) : RedigerbarBrevdata<EksportAvUforetrygdDto.PesysData> {
    data class PesysData(
        val fullEksport: Boolean,
        val sumUtOgTillegg: Kroner,
        val virkningstidspunkt: LocalDate,
        val muligTilbakekrevingVarsel: Boolean,
        val ensligOgMinstesats: Boolean,
        val minstesatsTilBruker: Double,
        val barnetilleggAvtaleland: Boolean,
        val flereBarn: Boolean,
        val innvilgetBT: Boolean,
        val btInnvilgetMenIngenUtbetaling: Boolean,
        val gjtEndret: Boolean,
        val gjtUendret: Boolean,
        val gjtAvkortet: Int,
        val eosNordisk: Boolean,
    ) : FagsystemBrevdata
}
