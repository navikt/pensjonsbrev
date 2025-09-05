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
data class EndringAvAlderspensjonSivilstandDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<EndringAvAlderspensjonSivilstandDto.SaksbehandlerValg, EndringAvAlderspensjonSivilstandDto.PesysData> {

    data class SaksbehandlerValg(
        // Samboer § 1-5:
        @DisplayText("Samboere med felles barn")
        val samboereMedFellesBarn: Boolean,
        @DisplayText("Samboere som tidligere har vært gift")
        val samboereTidligereGift: Boolean,

        // Endring i EPS-inntekt:
        @DisplayText("Endring i EPS inntekt - Økning/Reduksjon")
        val epsInntektOekningReduksjon: Boolean,

        // Årsak til sivilstandsendringen:
        @DisplayText("Sivilstandsendring årsak - Fraflyting")
        val fraFlyttet: Boolean,
        @DisplayText("Sivilstandsendring årsak - inngått ekteskap men bor ikke sammen")
        val giftBorIkkeSammen: Boolean,

        // Alders-og sykehjem eller EPS på annen institusjon:
        val institusjonsopphold: Boolean,

        // Betydning for pensjons utbetaling?
        @DisplayText("Er beløpet endret?")
        val beloepEndring: BeloepEndring,
        @DisplayText("Hvis reduksjon tilbake i tid")
        val feilutbetaling: Boolean,
        @DisplayText("Hvis endring i pensjonen")
        val endringPensjon: Boolean,
        @DisplayText("Hvis etterbetaling")
        val etterbetaling: Boolean,
    ) : BrevbakerBrevdata

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val epsVedVirk: EpsVedVirk,
        val kravAarsak: KravArsakType,  //v3.Krav
        val kravVirkDatoFom: LocalDate,  //v3.Krav
        val regelverkType: AlderspensjonRegelverkType,
        val saerskiltSatsErBrukt: Boolean,  //saerskiltSatsVedVirk
        val sivilstand: MetaforceSivilstand,
        val vedtakEtterbetaling: Boolean,  //v1.Vedtak
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto
    ) : BrevbakerBrevdata

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