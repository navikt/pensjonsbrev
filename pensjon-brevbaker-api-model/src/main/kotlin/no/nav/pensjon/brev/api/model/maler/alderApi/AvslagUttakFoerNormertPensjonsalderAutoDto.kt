package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate

@Suppress("unused")
data class AvslagUttakFoerNormertPensjonsalderAutoDto(
    val minstePensjonssats: Kroner,
    val normertPensjonsalder: NormertPensjonsalder,
    val virkFom: LocalDate,
    val totalPensjon: Kroner,
    val afpBruktIBeregning: Boolean,
    val opplysningerBruktIBeregningen: OpplysningerBruktIBeregningen,
    val borINorge: Boolean,
    val harEOSLand: Boolean,
    val vedtakBegrunnelseLavOpptjening: Boolean,
    val regelverkType: AlderspensjonRegelverkType = AlderspensjonRegelverkType.AP2025
) : BrevbakerBrevdata

@Suppress("unused")
data class AvslagUttakFoerNormertPensjonsalderAP2016AutoDto(
    val minstePensjonssats: Kroner,
    val normertPensjonsalder: NormertPensjonsalder,
    val virkFom: LocalDate,
    val totalPensjon: Kroner,
    val afpBruktIBeregning: Boolean,
    val opplysningerBruktIBeregningen: OpplysningerBruktIBeregningen,
    val borINorge: Boolean,
    val harEOSLand: Boolean,
    val vedtakBegrunnelseLavOpptjening: Boolean,
    val regelverkType: AlderspensjonRegelverkType = AlderspensjonRegelverkType.AP2016
) : BrevbakerBrevdata

data class NormertPensjonsalder(
    val aar: Int,
    val maaneder: Int,
)

data class TrygdeperiodeNorge(
    val fom: LocalDate,
    val tom: LocalDate
)

data class TrygdeperiodeUtland(
    val fom: LocalDate,
    val tom: LocalDate,
    val land: String
)

data class OpplysningerBruktIBeregningen(
    val virkFom: LocalDate,
    val uttaksgrad: Int,
    val trygdetid: Int,
    val pensjonsbeholdning: Kroner,
    val delingstallVedUttak: Double,
    val delingstallVedNormertPensjonsalder: Double?,
    val normertPensjonsalder: NormertPensjonsalder,
    val sisteOpptjeningsAar: Int?,
    val prorataBruktIBeregningen: Boolean,
    val kravAarsak: String?,
    val trygdeperioderNorge: List<TrygdeperiodeNorge>,
    val trygdeperioderUtland: List<TrygdeperiodeUtland>,
    val opplysningerKap19: OpplysningerBruktIBeregningenKap19?,
    val opplysningerKap20: OpplysningerBruktIBeregningenKap20,
)

data class OpplysningerBruktIBeregningenKap20(
    val redusertTrygdetidKap20: Boolean,
)

data class OpplysningerBruktIBeregningenKap19(
    val fodselsAar: Year,
    val andelNyttRegelverk: Int,
    val andelGammeltRegelverk: Int,
    val avslattKap19: Boolean,
    val sluttpoengTall: Int?,
    val innvilgetTillegspensjon: Boolean?,
    val poengAar: Int?,
    val poengAarF92: Int?,
    val poengAarE91: Int?,
    val forholdstall: Double,
    val forholdstallVed67: Double,
    val redusertTrygdetidKap19: Boolean,
    val trygdetidKap19: Int
)