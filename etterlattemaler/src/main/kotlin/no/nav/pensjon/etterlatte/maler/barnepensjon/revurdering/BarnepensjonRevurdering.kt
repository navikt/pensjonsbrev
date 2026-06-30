package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.selectors.barnepensjonRevurderingDTO.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.selectors.barnepensjonRevurderingData.*
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
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

data class BarnepensjonRevurderingData(
    val beregning: BarnepensjonBeregning,
    val bosattUtland: Boolean,
    val brukerUnder18Aar: Boolean,
    val datoVedtakOmgjoering: LocalDate?,
    val erEndret: Boolean,
    val erMigrertYrkesskade: Boolean,
    val erOmgjoering: Boolean,
    val erEtterbetaling: Boolean,
    val feilutbetaling: FeilutbetalingType,
    val frivilligSkattetrekk: Boolean,
    val harFlereUtbetalingsperioder: Boolean,
    val harUtbetaling: Boolean,
    val innholdForhaandsvarsel: List<Element>,
    val kunNyttRegelverk: Boolean,
)

data class BarnepensjonRevurderingDTO(
    override val innhold: List<Element>,
    override val data: BarnepensjonRevurderingData,
) : VedleggData, FerdigstillingBrevDTO

@TemplateModelHelpers
object BarnepensjonRevurdering : EtterlatteTemplate<BarnepensjonRevurderingDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - revurdering",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Vi har " },
                nynorsk { +"Vi har " },
                english { +"We have " },
            )

            showIf(data.erOmgjoering) {
                ifNotNull(data.datoVedtakOmgjoering) {
                    text(
                        bokmal { +"omgjort vedtaket om barnepensjon av " + it.format() },
                        nynorsk { +"gjort om vedtaket om barnepensjon av " + it.format() },
                        english { +"reversed our decision regarding the  children's pension on " + it.format() },
                    )
                }
            }.orShow {
                showIf(data.erEndret) {
                    text(
                        bokmal { +"endret" },
                        nynorsk { +"endra" },
                        english { +"changed" },
                    )
                } orShow {
                    text(
                        bokmal { +"vurdert" },
                        nynorsk { +"vurdert" },
                        english { +"evaluated" },
                    )
                }
                text(
                    bokmal { +" barnepensjonen din" },
                    nynorsk { +" barnepensjonen din" },
                    english { +" your children's pension" },
                )
            }
        }
        outline {
            includePhrase(
                BarnepensjonRevurderingFraser.RevurderingVedtak(
                data.erEndret,
                data.beregning,
                data.erEtterbetaling,
                data.harFlereUtbetalingsperioder,
                data.harUtbetaling
            ))

            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(BarnepensjonFellesFraser.HvorLengeKanDuFaaBarnepensjon(data.erMigrertYrkesskade))
            includePhrase(BarnepensjonFellesFraser.MeldFraOmEndringer)
            includePhrase(Felles.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(data.brukerUnder18Aar, data.bosattUtland))
        }

        // Beregning av barnepensjon nytt og gammelt regelverk
        includeAttachment(beregningAvBarnepensjonGammeltOgNyttRegelverk, data.beregning, data.kunNyttRegelverk.not())

        // Beregning av barnepensjon nytt regelverk
        includeAttachment(beregningAvBarnepensjonNyttRegelverk, data.beregning, data.kunNyttRegelverk)

        // Vedlegg under 18 år
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal, data.brukerUnder18Aar.and(data.bosattUtland.not()))
        includeAttachment(informasjonTilDegSomHandlerPaaVegneAvBarnetUtland, data.brukerUnder18Aar.and(data.bosattUtland))

        // Vedlegg over 18 år
        includeAttachment(informasjonTilDegSomMottarBarnepensjonNasjonal, data.brukerUnder18Aar.not().and(data.bosattUtland.not()))
        includeAttachment(informasjonTilDegSomMottarBarnepensjonUtland, data.brukerUnder18Aar.not().and(data.bosattUtland))

        includeAttachment(dineRettigheterOgPlikterBosattUtland, data.bosattUtland)
        includeAttachment(dineRettigheterOgPlikterNasjonal, data.bosattUtland.not())

        includeAttachment(forhaandsvarselFeilutbetalingBarnepensjonRevurdering, this.argument, data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL))
    }
}
