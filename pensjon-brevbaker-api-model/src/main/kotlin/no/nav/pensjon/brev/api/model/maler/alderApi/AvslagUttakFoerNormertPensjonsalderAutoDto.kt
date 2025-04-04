package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
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
    val redusertTrygdetid: Boolean,
    val trygdeperioderNorge: List<TrygdeperiodeNorge>,
    val trygdeperioderUtland: List<TrygdeperiodeUtland>
)
