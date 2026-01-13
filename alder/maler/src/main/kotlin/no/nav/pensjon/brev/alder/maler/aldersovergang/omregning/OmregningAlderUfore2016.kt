package no.nav.pensjon.brev.alder.maler.aldersovergang.omregning

import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen.vedleggOpplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen.vedleggOpplysningerBruktIBeregningenAlderAP2025
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggInformasjonOmMedlemskapOgHelserettigheterEOES
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggInformasjonOmMedlemskapOgHelserettigheterUtenforEOES
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningeromavdodbruktiberegningen.vedleggOpplysningerOmAvdoedBruktIBeregning
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.InformasjonOmMedlemskap
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016RedigerbarDto
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.avtaleland
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.borINorge
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.eksportberegnetUtenGarantipensjon
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.erEOSLand
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.erEksportberegnet
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingFemArKap19
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingFemArKap20
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingKap19
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
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.innvilgetFor67
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.kronebelop2G
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.opplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.opplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.opplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.over2G
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.persongrunnlagAvdod
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.skjermingstilleggInnvilget
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.totalPensjon
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.uttaksgrad
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.virkFom
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016DtoSelectors.ytelseForAldersovergang
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.OmregningAlderUfore2016RedigerbarDtoSelectors.pesysData
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.PersongrunnlagAvdodSelectors.avdodFnr
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.PersongrunnlagAvdodSelectors.avdodNavn
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object OmregningAlderUfore2016 : RedigerbarTemplate<OmregningAlderUfore2016RedigerbarDto> {

    override val featureToggle = FeatureToggles.omregningAlderUfore2016.toggle

    override val kode = Aldersbrevkoder.Redigerbar.PE_AP_OMREGNING_ALDER_UFORE_2016

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
                    virkFom = pesysData.virkFom,
                    uttaksgrad = pesysData.uttaksgrad,
                    totalPensjon = pesysData.totalPensjon,
                    antallBeregningsperioder = pesysData.antallBeregningsperioder,
                    gjenlevendetilleggKap19Innvilget = pesysData.gjenlevendetilleggKap19Innvilget,
                    gjenlevenderettInnvilget = pesysData.gjenlevenderettInnvilget,
                    avdodNavn = pesysData.persongrunnlagAvdod.avdodNavn,
                    avdodFnr = pesysData.persongrunnlagAvdod.avdodFnr,
                    gjenlevenderettAnvendt = pesysData.gjenlevenderettAnvendt,
                    eksportTrygdeavtaleAvtaleland = pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleAvtaleland,
                    faktiskBostedsland = pesysData.faktiskBostedsland,
                    erEksportberegnet = pesysData.inngangOgEksportVurdering.erEksportberegnet,
                    eksportberegnetUtenGarantipensjon = pesysData.inngangOgEksportVurdering.eksportberegnetUtenGarantipensjon,
                    pensjonstilleggInnvilget = pesysData.pensjonstilleggInnvilget,
                    garantipensjonInnvilget = pesysData.garantipensjonInnvilget,
                    godkjentYrkesskade = pesysData.godkjentYrkesskade,
                    skjermingstilleggInnvilget = pesysData.skjermingstilleggInnvilget,
                    garantitilleggInnvilget = pesysData.garantitilleggInnvilget,
                    oppfyltVedSammenleggingKap19 = pesysData.inngangOgEksportVurdering.oppfyltVedSammenleggingKap19,
                    oppfyltVedSammenleggingKap20 = pesysData.inngangOgEksportVurdering.oppfyltVedSammenleggingKap20,
                    oppfyltVedSammenleggingFemArKap19 = pesysData.inngangOgEksportVurdering.oppfyltVedSammenleggingFemArKap19,
                    oppfyltVedSammenleggingFemArKap20 = pesysData.inngangOgEksportVurdering.oppfyltVedSammenleggingFemArKap20,
                    borINorge = pesysData.inngangOgEksportVurdering.borINorge,
                    erEOSLand = pesysData.inngangOgEksportVurdering.erEOSLand,
                    eksportTrygdeavtaleEOS = pesysData.inngangOgEksportVurdering.eksportTrygdeavtaleEOS,
                    avtaleland = pesysData.inngangOgEksportVurdering.avtaleland,
                    innvilgetFor67 = pesysData.innvilgetFor67,
                    fullTrygdetid = pesysData.fullTrygdetid,
                    brukersSivilstand = pesysData.brukersSivilstand,
                    borMedSivilstand = pesysData.borMedSivilstand,
                    over2G = pesysData.over2G,
                    kronebelop2G = pesysData.kronebelop2G,
                    ytelseForAldersovergang = pesysData.ytelseForAldersovergang,
                )
            )

        }
        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder, pesysData.opplysningerBruktIBeregningenAlderDto)
        includeAttachmentIfNotNull(vedleggOpplysningerOmAvdoedBruktIBeregning,pesysData.opplysningerOmAvdoedBruktIBeregningDto)
        includeAttachment(vedleggInformasjonOmMedlemskapOgHelserettigheterEOES, pesysData.informasjonOmMedlemskap.equalTo(InformasjonOmMedlemskap.EOES))
        includeAttachment(vedleggInformasjonOmMedlemskapOgHelserettigheterUtenforEOES, pesysData.informasjonOmMedlemskap.equalTo(InformasjonOmMedlemskap.UTENFOR_EOES))
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025,pesysData.opplysningerBruktIBeregningenAlderAP2025Dto)
    }

    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
}