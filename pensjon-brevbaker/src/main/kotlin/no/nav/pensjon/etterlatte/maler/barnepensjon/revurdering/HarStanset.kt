package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.HarStansetDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.HarStansetDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.Barnepensjon
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.informasjonTilDegSomHandlerPaaVegneAvBarnet
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke

data class HarStansetDTO(val utbetalingsinfo: Utbetalingsinfo, override val innhold: List<Element>) : BrevDTO

@TemplateModelHelpers
object HarStanset : EtterlatteTemplate<HarStansetDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_HAR_STANSET

    override val template = createTemplate(
        name = kode.name,
        letterDataType = HarStansetDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - har staset",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Vi har stanset barnepensjonen din",
                Language.Nynorsk to "",
                Language.English to "",
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(
                Barnepensjon.BeregnetPensjonTabell(utbetalingsinfo.beregningsperioder),
            )
            includePhrase(Barnepensjon.DuMaaMeldeFraOmEndringer)
            includePhrase(Barnepensjon.DuHarRettTilAaKlage)
            includePhrase(Barnepensjon.HarDuSpoersmaal)
        }
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnet, innhold)
        includeAttachment(dineRettigheterOgPlikter, innhold)
        includeAttachment(klageOgAnke, innhold)
    }
}
