package no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.BarnepensjonEtterbetaling
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.bareEnPeriode
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.flerePerioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.ingenUtbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.kunNyttRegelverk
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.vedtattIPesys
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonForeldreloesFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.beregningAvBarnepensjonNyttRegelverk
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikterBosattUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikterNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomHandlerPaaVegneAvBarnetUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjonNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjonUtland

data class BarnepensjonForeldreloesDTO(
    override val innhold: List<Element>,
    val beregning: BarnepensjonBeregning,
    val etterbetaling: BarnepensjonEtterbetaling?,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
    val kunNyttRegelverk: Boolean,
    val bareEnPeriode: Boolean,
    val flerePerioder: Boolean,
    val ingenUtbetaling: Boolean,
    val vedtattIPesys: Boolean
) : BrevDTO


@TemplateModelHelpers
object BarnepensjonInnvilgelseForeldreloes : EtterlatteTemplate<BarnepensjonForeldreloesDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INNVILGELSE_FORELDRELOES

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonForeldreloesDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilget søknad om barnepensjon - foreldreløs",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            ifElse(
                vedtattIPesys,
                text(
                    Bokmal to "Barnepensjonen er endret fra 1. januar 2024",
                    Nynorsk to "",
                    English to "",
                ),
                text(
                    Bokmal to "Vi har innvilget søknaden din om barnepensjon",
                    Nynorsk to "Vi har innvilga søknaden din om barnepensjon",
                    English to "We have granted your application for a children's pension",
                )
            )
        }

        outline {

            includePhrase(
                BarnepensjonForeldreloesFraser.Vedtak(
                    virkningstidspunkt = beregning.virkningsdato,
                    sistePeriodeBeloep = beregning.sisteBeregningsperiode.utbetaltBeloep,
                    sistePeriodeFom = beregning.sisteBeregningsperiode.datoFOM,
                    bareEnPeriode = bareEnPeriode,
                    flerePerioder = flerePerioder,
                    ingenUtbetaling = ingenUtbetaling,
                    vedtattIPesys = vedtattIPesys
                )
            )

            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(
                BarnepensjonForeldreloesFraser.UtbetalingAvBarnepensjon(
                    beregning.beregningsperioder,
                    etterbetaling
                )
            )

            includePhrase(BarnepensjonForeldreloesFraser.HvorLengeKanDuFaaBarnepensjon)

            includePhrase(BarnepensjonInnvilgelseFraser.MeldFraOmEndringer)
            includePhrase(BarnepensjonInnvilgelseFraser.DuHarRettTilAaKlage)
            includePhrase(
                BarnepensjonInnvilgelseFraser.HarDuSpoersmaal(
                    brukerUnder18Aar,
                    bosattUtland
                )
            )
        }

        // Beregning av barnepensjon nytt regelverk
        includeAttachment(beregningAvBarnepensjonNyttRegelverk, beregning, kunNyttRegelverk)

        // Vedlegg under 18 år
        includeAttachment(
            informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal,
            innhold,
            brukerUnder18Aar.and(bosattUtland.not())
        )
        includeAttachment(
            informasjonTilDegSomHandlerPaaVegneAvBarnetUtland,
            innhold,
            brukerUnder18Aar.and(bosattUtland)
        )

        // Vedlegg over 18 år
        includeAttachment(
            informasjonTilDegSomMottarBarnepensjonNasjonal,
            innhold,
            brukerUnder18Aar.not().and(bosattUtland.not())
        )
        includeAttachment(
            informasjonTilDegSomMottarBarnepensjonUtland,
            innhold,
            brukerUnder18Aar.not().and(bosattUtland)
        )

        includeAttachment(dineRettigheterOgPlikterBosattUtland, innhold, bosattUtland)
        includeAttachment(dineRettigheterOgPlikterNasjonal, innhold, bosattUtland.not())
    }
}