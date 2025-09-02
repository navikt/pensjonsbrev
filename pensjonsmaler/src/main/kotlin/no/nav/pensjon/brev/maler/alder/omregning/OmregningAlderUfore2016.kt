package no.nav.pensjon.brev.maler.alder.omregning

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.avtaleland
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleAvtaleland
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.eksportberegnetUtenGarantipensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.erEOSLand
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.erEksportberegnet
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingFemArKap19
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingFemArKap20
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingKap19
import no.nav.pensjon.brev.api.model.maler.alderApi.InngangOgEksportVurderingSelectors.oppfyltVedSammenleggingKap20
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.antallBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.faktiskBostedsland
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.fullTrygdetid
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.garantitilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.gjenlevendetilleggKap19Innvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.godkjentYrkesskade
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.informasjonOmMedlemskapOgHelserettigheterDto
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.maanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.opplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.opplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.opplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.persongrunnlagAvdod
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.skjermingstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016DtoSelectors.virkFom
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016RedigerbarDto
import no.nav.pensjon.brev.api.model.maler.alderApi.OmregningAlderUfore2016RedigerbarDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.alderApi.PersongrunnlagAvdodSelectors.avdodFnr
import no.nav.pensjon.brev.api.model.maler.alderApi.PersongrunnlagAvdodSelectors.avdodNavn
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.vedlegg.maanedligPensjonFoerSkattAlderspensjon
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerOmAvdoedBruktIBeregning
import no.nav.pensjon.brev.maler.vedlegg.vedleggInformasjonOmMedlemskapOgHelserettigheter
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerBruktIBeregningenAlderAP2025
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object OmregningAlderUfore2016 : RedigerbarTemplate<OmregningAlderUfore2016RedigerbarDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_OMREGNING_ALDER_UFORE_2016

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmregningAlderUfore2016RedigerbarDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Omregning fra Uføre til Alder",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Vi har regnet om uføretrygden din til alderspensjon" },
                nynorsk { + "Vi har rekna om uføretrygda di til alderspensjon" },
                english { + "We have converted your disability pension into retirement pension" },
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
                )
            )

        }
        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlder, pesysData.opplysningerBruktIBeregningenAlderDto)
        includeAttachmentIfNotNull(vedleggOpplysningerOmAvdoedBruktIBeregning,pesysData.opplysningerOmAvdoedBruktIBeregningDto)
        includeAttachmentIfNotNull(maanedligPensjonFoerSkattAlderspensjon,pesysData.maanedligPensjonFoerSkattAlderspensjonDto)
        includeAttachmentIfNotNull(vedleggInformasjonOmMedlemskapOgHelserettigheter,pesysData.informasjonOmMedlemskapOgHelserettigheterDto)
        includeAttachmentIfNotNull(vedleggOpplysningerBruktIBeregningenAlderAP2025,pesysData.opplysningerBruktIBeregningenAlderAP2025Dto)
    }

    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
}