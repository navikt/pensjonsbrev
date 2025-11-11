package no.nav.pensjon.brev.maler.alder

import dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDto
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.avtaleland
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.borINorge
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.harEOSLand
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.normertPensjonsalder
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.opplysningerBruktIBeregningen
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.model.alder.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.prorataBruktIBeregningen
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP2025Vedlegg
import no.nav.pensjon.brev.model.alder.Aldersbrevkoder
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

@TemplateModelHelpers
object AvslagUttakFoerNormertPensjonsalderAuto : AutobrevTemplate<AvslagUttakFoerNormertPensjonsalderAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag tidlig uttak av alderspensjon",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Nav har avslått søknaden din om alderspensjon fra " + virkFom.format() },
                nynorsk { + "Nav har avslått søknaden din om alderspensjon frå " + virkFom.format() },
                english { + "Nav has declined your application for retirement pension from " + virkFom.format() },
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
                    avtaleland = avtaleland,
                    visInfoOmUttakFoer67 = false.expr(),
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachment(opplysningerBruktIBeregningenAP2025Vedlegg, opplysningerBruktIBeregningen)
    }
}
