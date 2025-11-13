package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class OmregningAlderUfore2016Dto(
    val virkFom: LocalDate,
    val uttaksgrad: Int,
    val totalPensjon: Kroner,
    val antallBeregningsperioder: Int,
    val gjenlevendetilleggKap19Innvilget: Boolean,
    val gjenlevenderettAnvendt: Boolean,
    val inngangOgEksportVurdering: InngangOgEksportVurdering,
    val pensjonstilleggInnvilget: Boolean,
    val garantipensjonInnvilget: Boolean,
    val godkjentYrkesskade: Boolean,
    val skjermingstilleggInnvilget: Boolean,
    val garantitilleggInnvilget: Boolean,
    val innvilgetFor67: Boolean,
    val fullTrygdetid: Boolean,
    val persongrunnlagAvdod: PersongrunnlagAvdod,
    val faktiskBostedsland: String?,
    val informasjonOmMedlemskap: InformasjonOmMedlemskap? = null,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val opplysningerBruktIBeregningenAlderDto: OpplysningerBruktIBeregningenAlderDto?,
    val opplysningerOmAvdoedBruktIBeregningDto: OpplysningerOmAvdoedBruktIBeregningDto?,
    val maanedligPensjonFoerSkattAlderspensjonDto: MaanedligPensjonFoerSkattAlderspensjonDto?,
    val opplysningerBruktIBeregningenAlderAP2025Dto: OpplysningerBruktIBeregningenAlderAP2025Dto?,
    val orienteringOmRettigheterOgPlikterDto: OrienteringOmRettigheterOgPlikterDto?,
    val borMedSivilstand: BorMedSivilstand?,
    val over2G: Boolean?,
) : BrevbakerBrevdata

data class PersongrunnlagAvdod(
    val avdodNavn: String?,
    val avdodFnr: String?,
)

data class InngangOgEksportVurdering(
    val eksportTrygdeavtaleAvtaleland: Boolean,
    val erEksportberegnet: Boolean,
    val eksportberegnetUtenGarantipensjon: Boolean,
    val borINorge: Boolean,
    val erEOSLand: Boolean,
    val eksportTrygdeavtaleEOS: Boolean,
    val avtaleland: String?,
    val oppfyltVedSammenleggingKap19: Boolean,
    val oppfyltVedSammenleggingKap20: Boolean,
    val oppfyltVedSammenleggingFemArKap19: Boolean,
    val oppfyltVedSammenleggingFemArKap20: Boolean,
)

data class Sivilstand(
    val borMedSivilstand: BorMedSivilstand?,
    val over2G: Boolean?,
)








