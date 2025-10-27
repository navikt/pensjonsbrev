package no.nav.pensjon.brev.model.alder.sivilstand

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.BeloepEndring
import no.nav.pensjon.brev.model.alder.KravArsakType
import no.nav.pensjon.brev.model.alder.MetaforceSivilstand
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.model.alder.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringAvAlderspensjonSivilstandAutoDto(
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val epsVedVirk: EpsVedVirk,
    val kravAarsak: KravArsakType,
    val kravVirkDatoFom: LocalDate,
    val regelverkType: AlderspensjonRegelverkType,
    val sivilstand: MetaforceSivilstand,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
    val beloepEndring: BeloepEndring,
) : BrevbakerBrevdata {
    data class EpsVedVirk(
        val harInntektOver2G: Boolean,
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
        val grunnpensjon: Kroner?,
        val totalPensjon: Kroner,
    )
}
