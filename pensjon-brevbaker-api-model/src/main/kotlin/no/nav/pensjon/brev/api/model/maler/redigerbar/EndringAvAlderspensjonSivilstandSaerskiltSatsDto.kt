package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringAvAlderspensjonSivilstandSaerskiltSatsDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<EndringAvAlderspensjonSivilstandSaerskiltSatsDto.SaksbehandlerValg, EndringAvAlderspensjonSivilstandSaerskiltSatsDto.PesysData> {

    data class SaksbehandlerValg(
        @DisplayText("Forsørger EPS over 60 år. Særskilt sats for minste pensjonsnivå")
        val eps: EPS?,
        @DisplayText("Informasjon om årlig kontroll til 67 år")
        val aarligKontrollEPS: Boolean,
        @DisplayText("Hvis reduksjon tilbake i tid")
        val feilutbetaling: Boolean,
        @DisplayText("Hvis etterbetaling")
        val etterbetaling: Boolean?,
    ) : BrevbakerBrevdata {
        enum class EPS {
            @DisplayText("Brukt i beregningen. EPS ikke fylt 62 år")
            epsIkkeFylt62Aar,
            @DisplayText("Brukt i beregningen. EPS har ikke rett til å ta ut full alderspensjon")
            epsIkkeRettTilFullAlderspensjon,
            @DisplayText("Ikke brukt i beregningen. EPS gir avkall på egen alderspensjon")
            epsAvkallPaaEgenAlderspenspensjon,
            @DisplayText("Ikke brukt i beregningen. EPS git avkall på egen uføretrygd")
            epsAvkallPaaEgenUfoeretrygd,
            @DisplayText("Ikke brukt i beregningen. EPS har inntekt over 1 G")
            epsHarInntektOver1G,
            @DisplayText("Ikke brukt i beregningen. EPS har rett til full alderspensjon")
            epsHarRettTilFullAlderspensjon,
            @DisplayText("Ikke brukt i beregningen. EPS tar ut alderspensjon")
            epsTarUtAlderspensjon,
            @DisplayText("Ikke brukt i beregningen. EPS tar ut AFP i statlig sektor")
            epsTarUtAlderspensjonIStatligSektor,
            @DisplayText("Ikke brukt i beregningen. EPS tar ut uføretrygd")
            epsTarUtUfoeretrygd,
        }
    }

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val kravAarsak: KravArsakType,  //v3.Krav
        val kravVirkDatoFom: LocalDate,  //v3.Krav
        val regelverkType: AlderspensjonRegelverkType,
        val saerskiltSatsErBrukt: Boolean,  //saerskiltSatsVedVirk
        val sivilstand: MetaforceSivilstand,
        val beloepEndring: BeloepEndring,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto
    ) : BrevbakerBrevdata

    data class AlderspensjonVedVirk(
        val innvilgetFor67: Boolean,
        val minstenivaaIndividuellInnvilget: Boolean,
        val saertilleggInnvilget: Boolean,
        val ufoereKombinertMedAlder: Boolean,
        val uttaksgrad: Int,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val grunnbelop: Kroner,  // beregnetPensjonPerManedVedVirk
        val totalPensjon: Kroner,  //beregnetPensjonPerManedVedVirk
    )
}