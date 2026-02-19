package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.BeloepEndring
import no.nav.pensjon.brev.api.model.EksportForbudKode
import no.nav.pensjon.brev.api.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import java.time.LocalDate

data class VedtakEndringVedFlyttingMellomLandDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<VedtakEndringVedFlyttingMellomLandDto.SaksbehandlerValg, VedtakEndringVedFlyttingMellomLandDto.PesysData> {
    data class SaksbehandlerValg(
        @DisplayText("Innvandret")
        val innvandret: Boolean,
        @DisplayText("Reduksjon tilbake i tid")
        val reduksjonTilbakeITid: Boolean,
        @DisplayText("Endring i pensjon")
        val endringIPensjonen: Boolean,
        @DisplayText("Etterbetaling")
        val etterbetaling: Boolean,
        @DisplayText("Relevant hvis innvandret")
        val aarsakTilAtPensjonenOeker: AarsakTilAtPensjonenOeker,
    ) : SaksbehandlerValgBrevdata

    data class PesysData(
        val krav: Krav,
        val bruker: Bruker,
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val inngangOgEksportVurdering: InngangOgEksportVurdering,
        val inngangOgEksportVurderingAvdoed: InngangOgEksportVurderingAvdoed?,
        val opphoersbegrunnelseVedVirk: OpphoersbegrunnelseVedVirk,
        val ytelseskomponentInformasjon: YtelseskomponentInformasjon,
        val beregnetpensjonPerMaanedVedVirk: BeregnetPensjonPerMaanedVedVirk,
        val erEtterbetaling1Maaned: Boolean,
        val informasjonOmMedlemskap: InformasjonOmMedlemskap? = null,
        val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
        val maanedligPensjonFoerSkatt: MaanedligPensjonFoerSkattDto?,
        val maanedligPensjonFoerSkattAP2025: MaanedligPensjonFoerSkattAP2025Dto?,
        val opplysningerBruktIBeregningen: OpplysningerBruktIBeregningenAlderDto?,
        val opplysningerBruktIBeregningenAlderAP2025Dto: OpplysningerBruktIBeregningenAlderAP2025Dto?,
        val opplysningerOmAvdoedBruktIBeregning: OpplysningerOmAvdoedBruktIBeregningDto?,
    ) : FagsystemBrevdata {
        data class Krav(val virkDatoFom: LocalDate, val aarsak: KravArsakType)
        data class Bruker(val faktiskBostedsland: String?, val borIEOES: Boolean, val borIAvtaleland: Boolean)
        data class AlderspensjonVedVirk(
            val erEksportberegnet: Boolean,
            val garantipensjonInnvilget: Boolean,
            val pensjonstilleggInnvilget: Boolean,
            val minstenivaaIndividuellInnvilget: Boolean,
            val minstenivaaPensjonistParInnvilget: Boolean,
            val uforeKombinertMedAlder: Boolean,
            val totalPensjon: Kroner,
            val gjenlevenderettAnvendt: Boolean,
            val fullUttaksgrad: Boolean,
        )

        data class InngangOgEksportVurdering(
            val eksportForbudKode: EksportForbudKode?,
            val minst20AarTrygdetid: Boolean,
            val eksportTrygdeavtaleEOES: Boolean,
            val eksportTrygdeavtaleAvtaleland: Boolean,
        )

        data class InngangOgEksportVurderingAvdoed(
            val eksportForbudKode: EksportForbudKode?,
            val minst20ArTrygdetidKap20: Boolean,
            val minst20ArBotidKap19: Boolean,
        )

        data class OpphoersbegrunnelseVedVirk(
            val begrunnelseET: Opphoersbegrunnelse?,
            val begrunnelseBT: Opphoersbegrunnelse?,
        )

        data class YtelseskomponentInformasjon(val beloepEndring: BeloepEndring?)
        data class BeregnetPensjonPerMaanedVedVirk(val grunnpensjon: Kroner?)
    }

    enum class Opphoersbegrunnelse {
        BRUKER_FLYTTET_IKKE_AVT_LAND,
        ANNET
    }

    enum class AarsakTilAtPensjonenOeker {
        @DisplayText("Ingen")
        INGEN,
        @DisplayText("Eksportberegning med redusert trygdetid")
        EKSPORTBEREGNING_MED_REDUSERT_TRYGDETID,
        @DisplayText("Eksportberegning ung ufør")
        EKSPORTFORBUD_UNG_UFOER,
        @DisplayText("Eksportberegning flyktning")
        EKSPORTFORBUD_FLYKTNING
    }
}