package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetaling
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.datoVedtakOmgjoering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.erEndret
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.erMigrertYrkesskade
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.erOmgjoering
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.harFlereUtbetalingsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.kunNyttRegelverk
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.beregningAvBarnepensjonGammeltOgNyttRegelverk
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.beregningAvBarnepensjonNyttRegelverk
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikterBosattUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikterNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.forhaandsvarselFeilutbetalingBarnepensjonRevurdering
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomHandlerPaaVegneAvBarnetUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjonNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjonUtland
import java.time.LocalDate

data class BarnepensjonRevurderingDTO(
    override val innhold: List<Element>,
    val innholdForhaandsvarsel: List<Element>,
    val erEndret: Boolean,
    val erOmgjoering: Boolean,
    val datoVedtakOmgjoering: LocalDate?,
    val beregning: BarnepensjonBeregning,
    val etterbetaling: BarnepensjonEtterbetaling?,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
    val kunNyttRegelverk: Boolean,
    val harFlereUtbetalingsperioder: Boolean,
    val harUtbetaling: Boolean,
    val feilutbetaling: FeilutbetalingType,
    val erMigrertYrkesskade: Boolean,
) : FerdigstillingBrevDTO

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

            showIf(erOmgjoering) {
                ifNotNull(datoVedtakOmgjoering) {
                    textExpr(
                        Bokmal to "omgjort vedtaket om barnepensjon av ".expr() + it.format(),
                        Nynorsk to "gjort om vedtaket om barnepensjon av ".expr() + it.format(),
                        English to "reversed our decision regarding the  children's pension on ".expr() + it.format(),
                    )
                }
            }.orShow {
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
        }
        outline {
            includePhrase(
                BarnepensjonRevurderingFraser.RevurderingVedtak(
                erEndret,
                beregning,
                etterbetaling.notNull(),
                harFlereUtbetalingsperioder,
                harUtbetaling
            ))

            konverterElementerTilBrevbakerformat(innhold)

            showIf(harUtbetaling) {
                includePhrase(BarnepensjonFellesFraser.UtbetalingAvBarnepensjon(etterbetaling, bosattUtland))
            }
            includePhrase(BarnepensjonFellesFraser.HvorLengeKanDuFaaBarnepensjon(erMigrertYrkesskade))
            includePhrase(BarnepensjonFellesFraser.MeldFraOmEndringer)
            includePhrase(BarnepensjonFellesFraser.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(brukerUnder18Aar, bosattUtland))
        }

        // Beregning av barnepensjon nytt og gammelt regelverk
        includeAttachment(beregningAvBarnepensjonGammeltOgNyttRegelverk, beregning, kunNyttRegelverk.not())

        // Beregning av barnepensjon nytt regelverk
        includeAttachment(beregningAvBarnepensjonNyttRegelverk, beregning, kunNyttRegelverk)

        // Vedlegg under 18 år
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal, innhold, brukerUnder18Aar.and(bosattUtland.not()))
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnetUtland, innhold, brukerUnder18Aar.and(bosattUtland))

        // Vedlegg over 18 år
        includeAttachment(informasjonTilDegSomMottarBarnepensjonNasjonal, innhold, brukerUnder18Aar.not().and(bosattUtland.not()))
        includeAttachment(informasjonTilDegSomMottarBarnepensjonUtland, innhold, brukerUnder18Aar.not().and(bosattUtland))

        includeAttachment(dineRettigheterOgPlikterBosattUtland, innhold, bosattUtland)
        includeAttachment(dineRettigheterOgPlikterNasjonal, innhold, bosattUtland.not())

        includeAttachment(forhaandsvarselFeilutbetalingBarnepensjonRevurdering, this.argument, feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL))
    }
}
