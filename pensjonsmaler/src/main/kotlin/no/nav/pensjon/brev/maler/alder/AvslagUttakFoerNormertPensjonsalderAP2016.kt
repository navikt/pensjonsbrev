package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
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
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016Dto
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016DtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.prorataBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP2016Vedlegg
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
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AvslagUttakFoerNormertPensjonsalderAP2016 : RedigerbarTemplate<AvslagUttakFoerNormertPensjonsalderAP2016Dto> {

    override val featureToggle = FeatureToggles.apAvslagNormertPensjonsalderAP2016.toggle

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER_AP2016

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagUttakFoerNormertPensjonsalderAP2016Dto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag tidlig uttak av alderspensjon",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { + "Nav har avslått søknaden din om alderspensjon fra " + pesysData.virkFom.format() },
                nynorsk { + "Nav har avslått søknaden din om alderspensjon frå " + pesysData.virkFom.format() },
                english { + "Nav has declined your application for retirement pension from " + pesysData.virkFom.format() },
            )
        }

        outline {
            includePhrase(
                AvslagUttakFoerNormertPensjonsalderFelles(
                    afpBruktIBeregning = pesysData.afpBruktIBeregning,
                    normertPensjonsalder = pesysData.normertPensjonsalder,
                    uttaksgrad = pesysData.opplysningerBruktIBeregningen.uttaksgrad,
                    prorataBruktIBeregningen = pesysData.opplysningerBruktIBeregningen.prorataBruktIBeregningen,
                    virkFom = pesysData.virkFom,
                    minstePensjonssats = pesysData.minstePensjonssats,
                    totalPensjon = pesysData.totalPensjon,
                    borINorge = pesysData.borINorge,
                    regelverkType = pesysData.regelverkType,
                    harEOSLand = pesysData.harEOSLand,
                    avtaleland = pesysData.avtaleland,
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachment(opplysningerBruktIBeregningenAP2016Vedlegg, pesysData.opplysningerBruktIBeregningen)
    }

    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
}
