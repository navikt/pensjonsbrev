package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.harEOSLand
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.normertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.opplysningerBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderDto
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
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV

@TemplateModelHelpers
object AvslagUttakFoerNormertPensjonsalder : RedigerbarTemplate<AvslagUttakFoerNormertPensjonsalderDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_UTTAK_FOER_NORMERT_PENSJONSALDER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagUttakFoerNormertPensjonsalderDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag tidlig uttak av alderspensjon - AP2025",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = INFORMASJONSBREV, // todo: skal være vedtaksbrev (når attestering er ferdig)
        )
    ) {
        title {
            textExpr(
                Bokmal to "Nav har avslått søknaden din om alderspensjon fra ".expr() + pesysData.virkFom.format(),
                Nynorsk to "Nav har avslått søknaden din om alderspensjon frå ".expr() + pesysData.virkFom.format(),
                English to "Nav has declined your application for retirement pension from ".expr() + pesysData.virkFom.format(),
            )
        }

        outline {
            includePhrase(
                AvslagUttakFoerNormertPensjonsalderFelles(
                    afpBruktIBeregning = pesysData.afpBruktIBeregning,
                    normertPensjonsalder = pesysData.normertPensjonsalder,
                    opplysningerBruktIBeregningen = pesysData.opplysningerBruktIBeregningen,
                    virkFom = pesysData.virkFom,
                    minstePensjonssats = pesysData.minstePensjonssats,
                    totalPensjon = pesysData.totalPensjon,
                    borINorge = pesysData.borINorge,
                    harEOSLand = pesysData.harEOSLand,
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachment(opplysningerBruktIBeregningenAP, pesysData.opplysningerBruktIBeregningen)
    }

    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
}
