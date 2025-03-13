package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.normertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.opplysningerBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderDtoSelectors.PesysDataSelectors.avslagUttakData
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object AvslagUttakFoerNormertPensjonsalder : RedigerbarTemplate<AvslagUttakFoerNormertPensjonsalderDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_UTTAK_FOER_NORMERT_PENSJONSALDER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagUttakFoerNormertPensjonsalderDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har avslått søknaden din om alderspensjon før normert pensjonsalder",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = VEDTAKSBREV,
        )
    ) {
        val avslagData = pesysData.avslagUttakData

        title {
            textExpr(
                Bokmal to "Nav har avslått søknaden din om alderspensjon fra ".expr() + avslagData.virkFom.format(),
                Nynorsk to "Nav har avslått søknaden din om alderspensjon frå ".expr() + avslagData.virkFom.format(),
                English to "Nav has declined your application for retirement pension from ".expr() + avslagData.virkFom.format(),
            )
        }

        outline {
            includePhrase(
                AvslagUttakFoerNormertPensjonsalderFelles(
                    afpBruktIBeregning = avslagData.afpBruktIBeregning,
                    normertPensjonsalder = avslagData.normertPensjonsalder,
                    opplysningerBruktIBeregningen = avslagData.opplysningerBruktIBeregningen,
                    virkFom = avslagData.virkFom,
                    minstePensjonssats = avslagData.minstePensjonssats,
                    totalPensjon = avslagData.totalPensjon
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachment(opplysningerBruktIBeregningenAP, avslagData.opplysningerBruktIBeregningen)
    }

    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
}
