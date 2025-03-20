package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.normertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.opplysningerBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
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
object AvslagUttakFoerNormertPensjonsalderAuto : AutobrevTemplate<AvslagUttakFoerNormertPensjonsalderAutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_AVSLAG_UTTAK_FOER_NORMERT_PENSJONSALDER_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagUttakFoerNormertPensjonsalderAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Nav har avslått søknaden din om alderspensjon før normert pensjonsalder",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = VEDTAKSBREV,
        )
    ) {
        title {
            textExpr(
                Bokmal to "Nav har avslått søknaden din om alderspensjon fra ".expr() + virkFom.format(),
                Nynorsk to "Nav har avslått søknaden din om alderspensjon frå ".expr() + virkFom.format(),
                English to "Nav has declined your application for retirement pension from ".expr() + virkFom.format(),
            )
        }

        outline {
            includePhrase(AvslagUttakFoerNormertPensjonsalderFelles(
                afpBruktIBeregning = afpBruktIBeregning,
                normertPensjonsalder = normertPensjonsalder,
                opplysningerBruktIBeregningen = opplysningerBruktIBeregningen,
                virkFom = virkFom,
                minstePensjonssats = minstePensjonssats,
                totalPensjon = totalPensjon,
                borINorge = borINorge,
            ))
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachment(opplysningerBruktIBeregningenAP, opplysningerBruktIBeregningen)
    }
}
