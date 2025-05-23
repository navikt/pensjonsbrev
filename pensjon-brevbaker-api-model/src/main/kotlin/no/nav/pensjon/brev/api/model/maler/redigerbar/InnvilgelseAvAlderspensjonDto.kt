package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


@Suppress("unused")
data class InnvilgelseAvAlderspensjonDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<InnvilgelseAvAlderspensjonDto.SaksbehandlerValg, InnvilgelseAvAlderspensjonDto.PesysData> {

    data class SaksbehandlerValg(
        val abc: String,
    ): BrevbakerBrevdata

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val barnetilleggVedVirk: BarnetilleggVedVirk,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val ektefelletilleggVedVirk: EktefelletilleggVedVirk,
        val kravVirkDatoFom: LocalDate,
        val regelverkType: AlderspensjonRegelverkType,
        val sivilstand: MetaforceSivilstand,
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    ): BrevbakerBrevdata

    data class AlderspensjonVedVirk(
        val uttaksgrad: Int,  // NB! For AP2025 hentes uttaksgrad fra fra alderspensjonVedVirkKap20
        val totalPensjon: Kroner,
        val uforeKombinertMedAlder: Boolean,
        val innvilgetFor67: Boolean,
        val gjenlevendetilleggInnvilget: Boolean,
        val gjenlevendetilleggKap19Innvilget: Boolean,
    )

    data class BarnetilleggVedVirk(
        val innvilgetFellesbarn: Boolean,
        val innvilgetSaerkullsbarn: Boolean,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val barnetilleggFellesbarn: Kroner,
        val barnetilleggSaerkullsbarn: Kroner,
        val ektefelletillegg: Kroner,
        val gjenlevendetilleggKap19: Kroner,
    )

    data class EktefelletilleggVedVirk(
        val innvilgetEktefelletillegg: Boolean,

    )
}
