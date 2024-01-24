package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Etterbetaling
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.Utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BeregningsinfoBP
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.beregningsinfo
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.erEndret
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.harFlereUtbetalingsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.kunNyttRegelverk
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.sisteUtbetalingsperiodeDatoFom
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.revurdering.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.etterbetalingAvBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.beregningAvBarnepensjonGammeltOgNyttRegelverk
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.beregningAvBarnepensjonNyttRegelverk
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.informasjonTilDegSomHandlerPaaVegneAvBarnetUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.informasjonTilDegSomMottarBarnepensjonNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.innvilgelse.informasjonTilDegSomMottarBarnepensjonUtland
import java.time.LocalDate

data class BarnepensjonRevurderingDTO(
    override val innhold: List<Element>,
    val erEndret: Boolean,
    val utbetalingsinfo: Utbetalingsinfo,
    val beregningsinfo: BeregningsinfoBP,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
    val kunNyttRegelverk: Boolean,
    val harFlereUtbetalingsperioder: Boolean,
    val sisteUtbetalingsperiodeDatoFom: LocalDate,
    val etterbetaling: Etterbetaling? = null,
) : BrevDTO

@TemplateModelHelpers
object BarnepensjonRevurdering : EtterlatteTemplate<BarnepensjonRevurderingDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonRevurderingDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - revurdering",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi har ",
                Nynorsk to "Vi har ",
                English to "We have ",
            )
            showIf(erEndret) {
                text(
                    Bokmal to "endret",
                    Nynorsk to "endra",
                    English to "changed",
                )
            } orShow {
                text(
                    Bokmal to "vurdert",
                    Nynorsk to "vurdert",
                    English to "evaluated",
                )
            }
            text(
                Bokmal to " barnepensjonen din",
                Nynorsk to " barnepensjonen din",
                English to " your children's pension",
            )
        }
        outline {
            includePhrase(BarnepensjonRevurderingFraser.RevurderingVedtak(
                erEndret,
                utbetalingsinfo,
                etterbetaling.notNull(),
                sisteUtbetalingsperiodeDatoFom,
                harFlereUtbetalingsperioder
            ))

            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(
                BarnepensjonInnvilgelseFraser.UtbetalingAvBarnepensjon(
                    utbetalingsinfo.beregningsperioder,
                    etterbetaling
                )
            )
            includePhrase(BarnepensjonInnvilgelseFraser.MeldFraOmEndringer)
            includePhrase(BarnepensjonInnvilgelseFraser.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonInnvilgelseFraser.HarDuSpoersmaal(brukerUnder18Aar, bosattUtland))
        }

        // Beregning av barnepensjon nytt og gammelt regelverk
        includeAttachment(beregningAvBarnepensjonGammeltOgNyttRegelverk, beregningsinfo, kunNyttRegelverk.not())

        // Beregning av barnepensjon nytt regelverk
        includeAttachment(beregningAvBarnepensjonNyttRegelverk, beregningsinfo, kunNyttRegelverk)

        includeAttachmentIfNotNull(etterbetalingAvBarnepensjon, etterbetaling)

        // Vedlegg under 18 år
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal, innhold, brukerUnder18Aar.and(bosattUtland.not()))
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnetUtland, innhold, brukerUnder18Aar.and(bosattUtland))

        // Vedlegg over 18 år
        includeAttachment(informasjonTilDegSomMottarBarnepensjonNasjonal, innhold, brukerUnder18Aar.not().and(bosattUtland.not()))
        includeAttachment(informasjonTilDegSomMottarBarnepensjonUtland, innhold, brukerUnder18Aar.not().and(bosattUtland))
    }
}
