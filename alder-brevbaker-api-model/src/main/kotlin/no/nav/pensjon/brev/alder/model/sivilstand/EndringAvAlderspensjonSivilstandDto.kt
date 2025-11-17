package no.nav.pensjon.brev.alder.model.sivilstand

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.KravArsakType
import no.nav.pensjon.brev.alder.model.MetaforceSivilstand
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringAvAlderspensjonSivilstandDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<EndringAvAlderspensjonSivilstandDto.SaksbehandlerValg, EndringAvAlderspensjonSivilstandDto.PesysData> {

    data class SaksbehandlerValg(
        @DisplayText("Årsak til sivilstandsendringen")
        val sivilstandsendringsaarsak: Sivilstandsendringsaarsak?,
        @DisplayText("Hvis reduksjon tilbake i tid")
        val feilutbetaling: Boolean,
        @DisplayText("Hvis etterbetaling")
        val etterbetaling: Boolean?,
    ) : SaksbehandlerValgBrevdata {
        enum class Sivilstandsendringsaarsak {
            @DisplayText("Fraflytting")
            fraFlyttet,
            @DisplayText("Inngått ekteskap, men bor ikke sammen")
            giftBorIkkeSammen,
            @DisplayText("Annet eller ingen")
            annet
        }
    }

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val epsVedVirk: EpsVedVirk?,
        val kravAarsak: KravArsakType,  //v3.Krav
        val kravVirkDatoFom: LocalDate,  //v3.Krav
        val regelverkType: AlderspensjonRegelverkType,
        val sivilstand: MetaforceSivilstand,
        val beloepEndring: BeloepEndring,
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto
    ) : FagsystemBrevdata

    data class EpsVedVirk(
        val borSammenMedBruker: Boolean,
        val harInntektOver2G: Boolean,
        val mottarOmstillingsstonad: Boolean,
        val mottarPensjon: Boolean,
    )

    data class AlderspensjonVedVirk(
        val garantipensjonInnvilget: Boolean,
        val innvilgetFor67: Boolean,
        val minstenivaaIndividuellInnvilget: Boolean,
        val minstenivaaPensjonsistParInnvilget: Boolean,
        val pensjonstilleggInnvilget: Boolean,
        val saertilleggInnvilget: Boolean,
        val ufoereKombinertMedAlder: Boolean,
        val uttaksgrad: Int,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val grunnpensjon: Kroner?,  //beregnetPensjonPerManedVedVirk
        val totalPensjon: Kroner,  //beregnetPensjonPerManedVedVirk
    )
}