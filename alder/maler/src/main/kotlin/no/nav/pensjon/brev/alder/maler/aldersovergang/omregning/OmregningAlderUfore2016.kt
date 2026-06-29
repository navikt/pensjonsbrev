package no.nav.pensjon.brev.alder.maler.aldersovergang.omregning

import no.nav.pensjon.brev.alder.maler.Brevkategori
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
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.selectors.inngangOgEksportVurdering.*
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.selectors.omregningAlderUfore2016Dto.*
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.selectors.omregningAlderUfore2016RedigerbarDto.*
import no.nav.pensjon.brev.alder.model.aldersovergang.omregning.selectors.persongrunnlagAvdod.*
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
                    avdodNavn = pesysData.persongrunnlagAvdod.avdodNavn,
                    avdodFnr = pesysData.persongrunnlagAvdod.avdodFnr,
                    gjenlevenderettAnvendt = pesysData.gjenlevenderettAnvendt,
                    gjenlevenderettInnvilget = pesysData.gjenlevenderettInnvilget,
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

    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper = setOf(Sakstype.ALDER)
}

