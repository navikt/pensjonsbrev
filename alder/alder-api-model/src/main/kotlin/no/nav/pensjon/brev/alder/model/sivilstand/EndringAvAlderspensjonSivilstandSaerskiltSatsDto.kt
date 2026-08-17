package no.nav.pensjon.brev.alder.model.sivilstand

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.KravArsakType
import no.nav.pensjon.brev.alder.model.MetaforceSivilstand
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringAvAlderspensjonSivilstandSaerskiltSatsDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : RedigerbarBrevdataMedSaksbehandlerValg<EndringAvAlderspensjonSivilstandSaerskiltSatsDto.PesysData> {

    enum class EPS(override val displayText: String) : SaksbehandlerValgEnum {
        epsIkkeFylt62Aar("Brukt i beregningen. EPS ikke fylt 62 år"),
        epsIkkeRettTilFullAlderspensjon("Brukt i beregningen. EPS har ikke rett til å ta ut full alderspensjon"),
        epsAvkallPaaEgenAlderspenspensjon("Ikke brukt i beregningen. EPS gir avkall på egen alderspensjon"),
        epsAvkallPaaEgenUfoeretrygd("Ikke brukt i beregningen. EPS git avkall på egen uføretrygd"),
        epsHarInntektOver1G("Ikke brukt i beregningen. EPS har inntekt over 1 G"),
        epsHarRettTilFullAlderspensjon("Ikke brukt i beregningen. EPS har rett til full alderspensjon"),
        epsTarUtAlderspensjon("Ikke brukt i beregningen. EPS tar ut alderspensjon"),
        epsTarUtAlderspensjonIStatligSektor("Ikke brukt i beregningen. EPS tar ut AFP i statlig sektor"),
        epsTarUtUfoeretrygd("Ikke brukt i beregningen. EPS tar ut uføretrygd"),
    }

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val kravAarsak: KravArsakType, //v3.Krav
        val kravVirkDatoFom: LocalDate, //v3.Krav
        val regelverkType: AlderspensjonRegelverkType,
        val saerskiltSatsErBrukt: Boolean, //saerskiltSatsVedVirk
        val sivilstand: MetaforceSivilstand,
        val beloepEndring: BeloepEndring,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto
    ) : FagsystemBrevdata

    data class AlderspensjonVedVirk(
        val innvilgetFor67: Boolean,
        val minstenivaaIndividuellInnvilget: Boolean,
        val saertilleggInnvilget: Boolean,
        val ufoereKombinertMedAlder: Boolean,
        val uttaksgrad: Int,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val grunnbelop: Kroner, // beregnetPensjonPerManedVedVirk
        val totalPensjon: Kroner, //beregnetPensjonPerManedVedVirk
    )
}