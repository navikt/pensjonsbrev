package no.nav.pensjon.brev.api.model.maler.alderApi

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.InformasjonOmMedlemskapOgHelserettigheterDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
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
    val faktiskBostedsland: String,
    val maanedligPensjonFoerSkattDto: MaanedligPensjonFoerSkattDto?,
    val opplysningerBruktIBeregningenAlderDto: OpplysningerBruktIBeregningenAlderDto?,
    val opplysningerOmAvdoedBruktIBeregningDto: OpplysningerOmAvdoedBruktIBeregningDto?,
    val maanedligPensjonFoerSkattAlderspensjonDto: MaanedligPensjonFoerSkattAlderspensjonDto?,
    val informasjonOmMedlemskapOgHelserettigheterDto: InformasjonOmMedlemskapOgHelserettigheterDto?,
    val opplysningerBruktIBeregningenAlderAP2025Dto: OpplysningerBruktIBeregningenAlderAP2025Dto?

    ):BrevbakerBrevdata

data class PersongrunnlagAvdod(
    val avdodNavn: String,
    val avdodFnr: String,
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








