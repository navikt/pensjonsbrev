package no.nav.pensjon.brev.alder.maler.aldersovergang.omregning

import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen.vedleggOpplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen.vedleggOpplysningerBruktIBeregningenAlderAP2025
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggInformasjonOmMedlemskapOgHelserettigheterEOES
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggInformasjonOmMedlemskapOgHelserettigheterUtenforEOES
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningeromavdodbruktiberegningen.vedleggOpplysningerOmAvdoedBruktIBeregning
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingKap19
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016Dto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.virkFom
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.avtaleland
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.borINorge
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.eksportberegnetUtenGarantipensjon
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.erEOSLand
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.erEksportberegnet
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingFemArKap19
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingFemArKap20
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingKap20
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.antallBeregningsperioder
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.borMedSivilstand
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.brukersSivilstand
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.faktiskBostedsland
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.fullTrygdetid
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.garantitilleggInnvilget
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.gjenlevenderettInnvilget
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.gjenlevendetilleggKap19Innvilget
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.godkjentYrkesskade
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.informasjonOmMedlemskap
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.innvilgetFor67
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.kronebelop2G
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.opplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.opplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.opplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.over2G
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.persongrunnlagAvdod
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.skjermingstilleggInnvilget
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.totalPensjon
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.uttaksgrad
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.PersongrunnlagAvdodSelectors.avdodFnr
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.PersongrunnlagAvdodSelectors.avdodNavn
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object OmregningAlderUfore2016Auto : AutobrevTemplate<OmregningAlderUfore2016Dto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_OMREGNING_ALDER_UFORE_2016_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak – Overgang fra uføretrygd til alderspensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Du er innvilget alderspensjon" },
                nynorsk { +"Du er innvilga alderspensjon" },
                english { +"You have been granted a retirement pension" },
            )
        }
        outline {
            includePhrase(
                OmregningAlderUfore2016Felles(
                    virkFom = virkFom,
                    uttaksgrad = uttaksgrad,
                    totalPensjon = totalPensjon,
                    antallBeregningsperioder = antallBeregningsperioder,
                    gjenlevendetilleggKap19Innvilget = gjenlevendetilleggKap19Innvilget,
                    avdodNavn = persongrunnlagAvdod.avdodNavn,
                    avdodFnr = persongrunnlagAvdod.avdodFnr,
                    gjenlevenderettAnvendt = gjenlevenderettAnvendt,
                    gjenlevenderettInnvilget = gjenlevenderettInnvilget,
                    eksportTrygdeavtaleAvtaleland = inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland,
                    faktiskBostedsland = faktiskBostedsland,
                    erEksportberegnet = inngangOgEksportVurdering.erEksportberegnet,
                    eksportberegnetUtenGarantipensjon = inngangOgEksportVurdering.eksportberegnetUtenGarantipensjon,
                    pensjonstilleggInnvilget = pensjonstilleggInnvilget,
                    garantipensjonInnvilget = garantipensjonInnvilget,
                    godkjentYrkesskade = godkjentYrkesskade,
                    skjermingstilleggInnvilget = skjermingstilleggInnvilget,
                    garantitilleggInnvilget = garantitilleggInnvilget,
                    oppfyltVedSammenleggingKap19 = inngangOgEksportVurdering.oppfyltVedSammenleggingKap19,
                    oppfyltVedSammenleggingKap20 = inngangOgEksportVurdering.oppfyltVedSammenleggingKap20,
                    oppfyltVedSammenleggingFemArKap19 = inngangOgEksportVurdering.oppfyltVedSammenleggingFemArKap19,
                    oppfyltVedSammenleggingFemArKap20 = inngangOgEksportVurdering.oppfyltVedSammenleggingFemArKap20,
                    borINorge = inngangOgEksportVurdering.borINorge,
                    erEOSLand = inngangOgEksportVurdering.erEOSLand,
                    eksportTrygdeavtaleEOS = inngangOgEksportVurdering.eksportTrygdeavtaleEOS,
                    avtaleland = inngangOgEksportVurdering.avtaleland,
                    innvilgetFor67 = innvilgetFor67,
                    fullTrygdetid = fullTrygdetid,
                    brukersSivilstand = brukersSivilstand,
                    borMedSivilstand = borMedSivilstand,
                    over2G = over2G,
                    kronebelop2G = kronebelop2G,
                )
            )
        }
        includeAttachmentIfNotNull(vedleggOrienteringOmRettigheterOgPlikter, orienteringOmRettigheterOgPlikterDto)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, maanedligPensjonFoerSkattDto)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder, opplysningerBruktIBeregningenAlderDto)
        includeAttachmentIfNotNull(vedleggOpplysningerOmAvdoedBruktIBeregning, opplysningerOmAvdoedBruktIBeregningDto)
        includeAttachment(vedleggInformasjonOmMedlemskapOgHelserettigheterEOES, informasjonOmMedlemskap.equalTo(InformasjonOmMedlemskap.EOES))
        includeAttachment(vedleggInformasjonOmMedlemskapOgHelserettigheterUtenforEOES, informasjonOmMedlemskap.equalTo(InformasjonOmMedlemskap.UTENFOR_EOES))
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025, opplysningerBruktIBeregningenAlderAP2025Dto)
    }
}