package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDto
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.DisplayText
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringAvAlderspensjonSivilstandDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<EndringAvAlderspensjonSivilstandDto.SaksbehandlerValg, EndringAvAlderspensjonSivilstandDto.PesysData> {

    data class SaksbehandlerValg(
        val samboer15: MetaforceSivilstand,
        val endringIEPSInntekt: KravArsakType,
        val institusjonsopphold: KravArsakType,
        val forsoergerEPSOver60AarBruktIBeregningen: KravArsakType,
        val forsoergerEPSOver60AarIkkeBruktIBeregningen: KravArsakType,
        @DisplayText("Betydning får omregulering for pensjon? Ingen")
        val ingenBetydning: Boolean,
        @DisplayText("Betydning får omregulering for pensjon? Pensjonen øker")
        val pensjonenOeker: Boolean,
        @DisplayText("Betydning får omregulering for pensjon? Pensjonen blir redusert")
        val pensjonenRedusert: Boolean,
        @DisplayText("Informasjon om årlig kontroll til 67 år")
        val aarligKontrollEPS: Boolean,
        @DisplayText("Hvis reduksjon tilbake i tid")
        val feilutbetaling: Boolean,
        @DisplayText("Hvis endring i pensjonen")
        val endringPensjon: Boolean,
        @DisplayText("Hvis etterbetaling")
        val etterbetaling: Boolean,
    ) : BrevbakerBrevdata

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val brukersSivilstand: MetaforceSivilstand,
        val epsVedVirk: EpsVedVirk,
        val garantitillegg: Kroner?,
        val grunnpensjon: Kroner,  //beregnetPensjonPerManedVedVirk
        val kravAarsak: KravArsakType,
        val kravVirkDatoFom: LocalDate,
        val regelverkType: AlderspensjonRegelverkType,
        val saerskiltSatsErBrukt: Boolean,  //saerskiltSatsVedVirk
        val totalPensjon: Kroner,  //beregnetPensjonPerManedVedVirk
        val ufoereKombinertMedAlder: Boolean,
        val vedtakEtterbetaling: Boolean,  //v1.Vedtak
        val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto,
        val orienteringOmRettigheterAlderDto: OrienteringOmRettigheterAlderDto
    ) : BrevbakerBrevdata

    data class EpsVedVirk(
        val borSammenMedBruker: Boolean,
        val harInntektOver2G: Boolean,
        val mottarOmstillingsstonad: Boolean,
        val mottarPensjon: Boolean,
    )

    data class AlderspensjonVedVirk(
        val garantipensjonInnvilget: Boolean,
        val minstenivaaIndividuellInnvilget: Boolean,
        val minstenivaaPensjonsistParInnvilget: Boolean,
        val pensjonstilleggInnvilget: Boolean,
        val uttaksgrad: Int,
        val saertilleggInnvilget: Boolean,
    )
}