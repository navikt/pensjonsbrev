package no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.erEtterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.erGjenoppretting
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.erMigrertYrkesskade
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.frivilligSkattetrekk
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.kunNyttRegelverk
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonForeldreloesDTOSelectors.vedtattIPesys
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
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
    val bosattUtland: Boolean,
    val brukerUnder18Aar: Boolean,
    val erGjenoppretting: Boolean,
    val erMigrertYrkesskade: Boolean,
    val frivilligSkattetrekk: Boolean,
    val harUtbetaling: Boolean,
    val kunNyttRegelverk: Boolean,
    val vedtattIPesys: Boolean,
    val erEtterbetaling: Boolean
) : FerdigstillingBrevDTO


@TemplateModelHelpers
object BarnepensjonInnvilgelseForeldreloes : EtterlatteTemplate<BarnepensjonForeldreloesDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BP_INNVILGELSE_FORELDRELOES

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
            showIf(vedtattIPesys) {
                text(
                    Bokmal to "Barnepensjonen er endret fra 1. januar 2024",
                    Nynorsk to "Barnepensjonen er endra frå 1. januar 2024",
                    English to "Your children’s pension has been changed as of 1 January 2024",
                )
            }.orShowIf(erGjenoppretting) {
                text(
                    Bokmal to "Du er innvilget barnepensjon på nytt",
                    Nynorsk to "Du er innvilga barnepensjon på ny",
                    English to "You have been granted children’s pension again",
                )
            }.orShow {
                text(
                    Bokmal to "Vi har innvilget søknaden din om barnepensjon",
                    Nynorsk to "Vi har innvilga søknaden din om barnepensjon",
                    English to "We have granted your application for a children's pension",
                )
            }
        }

        outline {
            konverterElementerTilBrevbakerformat(innhold)

            showIf(harUtbetaling) {
                includePhrase(BarnepensjonFellesFraser.UtbetalingAvBarnepensjon(erEtterbetaling, bosattUtland, frivilligSkattetrekk))
            }
            includePhrase(BarnepensjonFellesFraser.HvorLengeKanDuFaaBarnepensjon(erMigrertYrkesskade))
            includePhrase(BarnepensjonFellesFraser.MeldFraOmEndringer)
            includePhrase(BarnepensjonFellesFraser.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(brukerUnder18Aar, bosattUtland))
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