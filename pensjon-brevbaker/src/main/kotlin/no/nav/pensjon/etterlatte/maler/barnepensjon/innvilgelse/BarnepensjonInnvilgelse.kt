package no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetaling
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTOSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTOSelectors.kunNyttRegelverk
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.beregningAvBarnepensjonGammeltOgNyttRegelverk
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.beregningAvBarnepensjonNyttRegelverk
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.etterbetalingAvBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.informasjonTilDegSomHandlerPaaVegneAvBarnetUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.informasjonTilDegSomMottarBarnepensjonNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.informasjonTilDegSomMottarBarnepensjonUtland

data class BarnepensjonInnvilgelseDTO(
    override val innhold: List<Element>,
    val beregning: BarnepensjonBeregning,
    val etterbetaling: BarnepensjonEtterbetaling?,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
    val kunNyttRegelverk: Boolean,
) : BrevDTO

@TemplateModelHelpers
object BarnepensjonInnvilgelse : EtterlatteTemplate<BarnepensjonInnvilgelseDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INNVILGELSE

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonInnvilgelseDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilget søknad om barnepensjon",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi har innvilget søknaden din om barnepensjon",
                Nynorsk to "Vi har innvilga søknaden din om barnepensjon",
                English to "We have granted your application for a children's pension",
            )
        }

        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(
                BarnepensjonInnvilgelseFraser.UtbetalingAvBarnepensjon(
                    beregning.beregningsperioder,
                    etterbetaling
                )
            )
            includePhrase(BarnepensjonInnvilgelseFraser.MeldFraOmEndringer)
            includePhrase(BarnepensjonInnvilgelseFraser.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonInnvilgelseFraser.HarDuSpoersmaal(brukerUnder18Aar, bosattUtland))
        }

        // Beregning av barnepensjon nytt og gammelt regelverk
        includeAttachment(beregningAvBarnepensjonGammeltOgNyttRegelverk, beregning, kunNyttRegelverk.not())

        // Beregning av barnepensjon nytt regelverk
        includeAttachment(beregningAvBarnepensjonNyttRegelverk, beregning, kunNyttRegelverk)

        includeAttachmentIfNotNull(etterbetalingAvBarnepensjon, etterbetaling)

        // Vedlegg under 18 år
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal, innhold, brukerUnder18Aar.and(bosattUtland.not()))
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnetUtland, innhold, brukerUnder18Aar.and(bosattUtland))

        // Vedlegg over 18 år
        includeAttachment(informasjonTilDegSomMottarBarnepensjonNasjonal, innhold, brukerUnder18Aar.not().and(bosattUtland.not()))
        includeAttachment(informasjonTilDegSomMottarBarnepensjonUtland, innhold, brukerUnder18Aar.not().and(bosattUtland))

        includeAttachment(dineRettigheterOgPlikter, this.argument, true.expr())
    }
}