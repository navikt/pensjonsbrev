package no.nav.pensjon.brev.alder.maler

import no.nav.pensjon.brev.alder.maler.avslag.uttak.AvslagUttakFoerNormertPensjonsalderFelles
import no.nav.pensjon.brev.alder.maler.felles.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.afpBruktIBeregning
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.avtaleland
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.borINorge
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.harEOSLand
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.minstePensjonssats
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.normertPensjonsalder
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.opplysningerBruktIBeregningen
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.regelverkType
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.totalPensjon
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderAutoDtoSelectors.virkFom
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderDto
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderDtoSelectors.SaksbehandlerValgSelectors.visInfoOmUttakFoer67
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderDtoSelectors.pesysData
import no.nav.pensjon.brev.alder.model.avslag.AvslagUttakFoerNormertPensjonsalderDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.alder.model.avslag.OpplysningerBruktIBeregningenSelectors.prorataBruktIBeregningen
import no.nav.pensjon.brev.alder.model.avslag.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerBruktIBeregningenAP2025Vedlegg
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AvslagUttakFoerNormertPensjonsalder : RedigerbarTemplate<AvslagUttakFoerNormertPensjonsalderDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AP_AVSLAG_UTTAK_FOER_NORM_PEN_ALDER

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag tidlig uttak av alderspensjon",
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
                    visInfoOmUttakFoer67 = saksbehandlerValg.visInfoOmUttakFoer67.ifNull(false)
                )
            )
        }

        includeAttachment(dineRettigheterOgMulighetTilAaKlagePensjonStatisk)
        includeAttachment(opplysningerBruktIBeregningenAP2025Vedlegg, pesysData.opplysningerBruktIBeregningen)
    }

    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
}
