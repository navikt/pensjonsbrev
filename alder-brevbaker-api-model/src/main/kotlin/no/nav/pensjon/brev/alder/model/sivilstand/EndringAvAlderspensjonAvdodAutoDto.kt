package no.nav.pensjon.brev.alder.model.sivilstand

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.BorMedSivilstand
import no.nav.pensjon.brev.alder.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.alder.model.Institusjon
import no.nav.pensjon.brev.alder.model.SivilstandAvdoed
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class EndringAvAlderspensjonAvdodAutoDto (
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val beregnetPensjonPerManed: BeregnetPensjonPerManed,
    val avdodInformasjon: AvdodInformasjon,
    val institusjonsoppholdVedVirk: Institusjon,
    val institusjonsoppholdGjeldende: Institusjon,
    val sivilstand: BorMedSivilstand,
    val virkFom: LocalDate,
    val harBarnUnder18: Boolean,
    val etterBetaling: Boolean,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val maanedligPensjonFoerSkattAP2025Dto: MaanedligPensjonFoerSkattAP2025Dto?,
    val opplysningerBruktIBeregningenAlderDto: OpplysningerBruktIBeregningenAlderDto?,
    val opplysningerOmAvdoedBruktIBeregningDto: OpplysningerOmAvdoedBruktIBeregningDto?,
    val maanedligPensjonFoerSkattAFPDto: MaanedligPensjonFoerSkattAFPDto?,
    val informasjonOmMedlemskap: InformasjonOmMedlemskap? = null,

    ): AutobrevData {

    data class AlderspensjonVedVirk (
        val harEndretPensjon: Boolean,
        val totalPensjon: Kroner,
        val regelverkType: AlderspensjonRegelverkType,
        val uttaksgrad: Int,
        val minstenivaIndividuellInnvilget: Boolean,
    )

    data class BeregnetPensjonPerManed(
        val antallBeregningsperioderPensjon: Int,
        val erPerioderMedUttak: Boolean,
        val garantiPensjon: Kroner?,

    )

    data class AvdodInformasjon (
        val sivilstandAvdoed: SivilstandAvdoed,
        val ektefelletilleggOpphort: Boolean,
        val gjenlevendesAlder: Int,
        val avdodNavn: String,
    )
}