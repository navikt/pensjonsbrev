package no.nav.pensjon.brev.api.model.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class VedtakEndringAvAlderspensjonGjenlevenderettigheterDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.SaksbehandlerValg, VedtakEndringAvAlderspensjonGjenlevenderettigheterDto.PesysData> {
    data class SaksbehandlerValg(
        val visTilleggspensjonavsnittAP1967: Boolean,
        val omregnetTilEnsligISammeVedtak: Boolean,
        val pensjonenOeker: Boolean,
        @DisplayText("Hvis bruker under 67 år og avdøde har redusert trygdetid/poengår")
        val brukerUnder67OgAvdoedeHarRedusertTrygdetidEllerPoengaar: Boolean,
        @DisplayText("Hvis avdøde har redusert trygdetid/poengår")
        val avdoedeHarRedusertTrygdetidEllerPoengaar: Boolean,
        @DisplayText("Hvis endring i pensjonsutbetaling")
        val endringIPensjonsutbetaling: Boolean,
        @DisplayText("Hvis etterbetaling av pensjon")
        val etterbetaling: Boolean,
    ) : BrevbakerBrevdata

    data class PesysData(
        val avdod: Avdod,
        val bruker: Bruker,
        val krav: Krav,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val ytelseskomponentInformasjon: YtelseskomponentInformasjon,
        val gjenlevendetilleggKapittel19VedVirk: GjenlevendetilleggKapittel19VedVirk,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto,
        val opplysningerOmAvdoedBruktIBeregningDto: OpplysningerOmAvdoedBruktIBeregningDto,
    ) : BrevbakerBrevdata

    data class Avdod(
        val navn: String
    )

    data class Bruker(
        val fodselsdato: LocalDate
    )

    data class Krav(
        val virkDatoFom: LocalDate,
        val kravInitiertAv: KravInitiertAv
    )

    data class AlderspensjonVedVirk(
        val totalPensjon: Kroner,
        val regelverkType: AlderspensjonRegelverkType,
        val uttaksgrad: Int,
        val gjenlevendetilleggKap19Innvilget: Boolean,
        val gjenlevenderettAnvendt: Boolean,
        val gjenlevendetilleggInnvilget: Boolean,
        val saertilleggInnvilget: Boolean,
        val minstenivaIndividuellInnvilget: Boolean,
        val pensjonstilleggInnvilget: Boolean,
        val garantipensjonInnvilget: Boolean,
        val harEndretPensjon: Boolean,
    )

    data class YtelseskomponentInformasjon(
        val beloepEndring: BeloepEndring,
    )

    data class GjenlevendetilleggKapittel19VedVirk(
        val apKap19utenGJR: Int
    )

    data class BeregnetPensjonPerManedVedVirk(
        val inntektspensjon: Int?,
        val gjenlevendetilleggKap19: Kroner?,
        val gjenlevendetillegg: Kroner?,
        val antallBeregningsperioderPensjon: Int,
    )

    @Suppress("EnumEntryName")
    enum class KravInitiertAv {
        BRUKER,
        NAV,
        VERGE,
    }

    @Suppress("EnumEntryName")
    enum class BeloepEndring {
        ENDR_OKT,
        ENDR_RED,
        UENDRET
    }
}