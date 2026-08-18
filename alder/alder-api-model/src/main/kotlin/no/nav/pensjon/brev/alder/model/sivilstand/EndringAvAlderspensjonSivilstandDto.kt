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
data class EndringAvAlderspensjonSivilstandDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : RedigerbarBrevdataMedSaksbehandlerValg<EndringAvAlderspensjonSivilstandDto.PesysData> {

    enum class Sivilstandsendringsaarsak(override val displayText: String) : SaksbehandlerValgEnum {
        fraFlyttet("Fraflytting"),
        giftBorIkkeSammen("Inngått ekteskap, men bor ikke sammen"),
        annet("Annet eller ingen")
    }

    data class PesysData(
        val alderspensjonVedVirk: AlderspensjonVedVirk,
        val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
        val epsVedVirk: EpsVedVirk?,
        val kravAarsak: KravArsakType, //v3.Krav
        val kravVirkDatoFom: LocalDate, //v3.Krav
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
        val grunnpensjon: Kroner?, //beregnetPensjonPerManedVedVirk
        val totalPensjon: Kroner, //beregnetPensjonPerManedVedVirk
    )
}