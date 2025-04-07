package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.api.model.maler.alderApi.AvslagUttakFoerNormertPensjonsalderAP2016AutoDtoSelectors.borINorge
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
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV

@TemplateModelHelpers
object AvslagUttakFoerNormertPensjonsalderAP2016 : RedigerbarTemplate<AvslagUttakFoerNormertPensjonsalderAP2016Dto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_UTTAK_FOER_NORMERT_PENSJONSALDER_AP2016

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagUttakFoerNormertPensjonsalderAP2016Dto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag tidlig uttak av alderspensjon - AP2016",
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
                    uttaksgrad = pesysData.opplysningerBruktIBeregningen.uttaksgrad,
                    prorataBruktIBeregningen = pesysData.opplysningerBruktIBeregningen.prorataBruktIBeregningen,
                    virkFom = pesysData.virkFom,
                    minstePensjonssats = pesysData.minstePensjonssats,
                    totalPensjon = pesysData.totalPensjon,
                    borINorge = pesysData.borINorge,
                    regelverkType = pesysData.regelverkType,
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
