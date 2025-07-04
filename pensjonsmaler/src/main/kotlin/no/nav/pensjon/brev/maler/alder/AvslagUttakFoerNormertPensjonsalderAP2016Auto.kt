package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.avtaleland
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.harEOSLand
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.normertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.opplysningerBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.virkFom
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.prorataBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP2016Vedlegg
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
object AvslagUttakFoerNormertPensjonsalderAP2016Auto : AutobrevTemplate<AvslagUttakFoerNormertPensjonsalderAP2016AutoDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER_AP2016_AUTO

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagUttakFoerNormertPensjonsalderAP2016AutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag tidlig uttak av alderspensjon",
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
            includePhrase(
                AvslagUttakFoerNormertPensjonsalderFelles(
                    afpBruktIBeregning = afpBruktIBeregning,
                    normertPensjonsalder = normertPensjonsalder,
                    uttaksgrad = opplysningerBruktIBeregningen.uttaksgrad,
                    prorataBruktIBeregningen = opplysningerBruktIBeregningen.prorataBruktIBeregningen,
                    virkFom = virkFom,
                    minstePensjonssats = minstePensjonssats,
                    totalPensjon = totalPensjon,
                    borINorge = borINorge,
                    regelverkType = regelverkType,
                    harEOSLand = harEOSLand,
                    avtaleland = avtaleland
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachment(opplysningerBruktIBeregningenAP2016Vedlegg, opplysningerBruktIBeregningen)
    }
}
