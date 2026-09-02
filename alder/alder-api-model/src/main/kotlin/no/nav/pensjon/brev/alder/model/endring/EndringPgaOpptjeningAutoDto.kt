package no.nav.pensjon.brev.alder.model.endring

import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.alder.model.BeloepEndring
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.model.vedlegg.OrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate


@Suppress("unused")
data class EndringPgaOpptjeningAutoDto(
    val virkFom: LocalDate,
    val opptjeningType: OpptjeningType,
    val opptjening: Opptjening,
    val belopEndring: BeloepEndring,
    val uforeKombinertMedAlder: Boolean,
    val beregnetPensjonPerMaanedGjeldende: BeregnetPensjonPerMaanedGjeldende,
    val beregnetPensjonPerMaaned: BeregnetPensjonPerMaaned,
    val beregnetPensjonPerMaanedVedVirk: BeregnetPensjonPerMaanedVedVirk,
    val regelverkType: AlderspensjonRegelverkType,
    val erFoerstegangsbehandling: Boolean,
    val borINorge: Boolean,

    val orienteringOmRettigheterOgPlikter: OrienteringOmRettigheterOgPlikterDto,
    val maanedligPensjonFoerSkatt: MaanedligPensjonFoerSkattDto?,
    val maanedligPensjonFoerSkattAP2025: MaanedligPensjonFoerSkattAP2025Dto?,
    val opplysningerBruktIBeregningenAlder: OpplysningerBruktIBeregningenAlderDto?,
    val opplysningerBruktIBeregningenAlderAP2025: OpplysningerBruktIBeregningenAlderAP2025Dto?,
    val opplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjening: OpplysningerBruktIBeregningenAlderAP2025EndringPgaOpptjeningDto?,
    val opplysningerOmAvdoedBruktIBeregning: OpplysningerOmAvdoedBruktIBeregningDto?,
) : AutobrevData

enum class OpptjeningType {
    TILVEKST,
    KORRIGERING,
}

data class Opptjening(
    val sisteGyldigeOpptjeningsAar: Int?,
    val antallAarEndretOpptjening: Int,
    val endretOpptjeningsAar: Set<Int>,
)

data class BeregnetPensjonPerMaaned(
    val antallBeregningsperioderPensjon: Int,
)

data class BeregnetPensjonPerMaanedGjeldende(
    val totalPensjon: Kroner,
    val virkFom: LocalDate,
)

data class BeregnetPensjonPerMaanedVedVirk(
    val totalPensjon: Kroner,
    val virkFom: LocalDate,
    val pensjonstilleggInnvilget: Boolean,
    val minstenivaPensjonistParInnvilget: Boolean,
    val minstenivaIndividuellInnvilget: Boolean,
    val gjenlevenderettAnvendt: Boolean,
    val garantipensjonInnvilget: Boolean,
    val uttaksgrad: Int,
)