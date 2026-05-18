package no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
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
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDataSelectors.beregning
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDataSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDataSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDataSelectors.datoVedtakOmgjoering
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDataSelectors.erEtterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDataSelectors.erGjenoppretting
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDataSelectors.erMigrertYrkesskade
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDataSelectors.frivilligSkattetrekk
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDataSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseDataSelectors.kunNyttRegelverk
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.beregningAvBarnepensjonGammeltOgNyttRegelverk
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.beregningAvBarnepensjonNyttRegelverk
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikterBosattUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikterNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomHandlerPaaVegneAvBarnetUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjonNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjonUtland
import java.time.LocalDate

data class BarnepensjonInnvilgelseData(
    val beregning: BarnepensjonBeregning,
    val bosattUtland: Boolean,
    val brukerUnder18Aar: Boolean,
    val erGjenoppretting: Boolean,
    val erMigrertYrkesskade: Boolean,
    val erEtterbetaling: Boolean,
    val frivilligSkattetrekk: Boolean,
    val harUtbetaling: Boolean,
    val kunNyttRegelverk: Boolean,
    val erSluttbehandling: Boolean = false,
    val datoVedtakOmgjoering: LocalDate? = null,
)

data class BarnepensjonInnvilgelseDTO(
    override val innhold: List<Element>,
    override val data: BarnepensjonInnvilgelseData,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object BarnepensjonInnvilgelse : EtterlatteTemplate<BarnepensjonInnvilgelseDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INNVILGELSE

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilget søknad om barnepensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {

            ifNotNull(data.datoVedtakOmgjoering) {
                text(
                    bokmal { +"Vi har omgjort vedtaket om omstillingsstønad av " + it.format() },
                    nynorsk { +"Vi har gjort om vedtaket om omstillingsstønad av " + it.format() },
                    english { +"We have reversed our decision regarding the adjustment allowance on " + it.format() },
                )
            }
            .orShowIf(data.erGjenoppretting) {
                text(
                    bokmal { +"Du er innvilget barnepensjon på nytt" },
                    nynorsk { +"Du er innvilga barnepensjon på ny" },
                    english { +"You have been granted children’s pension again" },
                )
            }.orShow {
                text(
                    bokmal { +"Vi har innvilget søknaden din om barnepensjon" },
                    nynorsk { +"Vi har innvilga søknaden din om barnepensjon" },
                    english { +"We have granted your application for a children's pension" },
                )
            }
        }

        outline {
            konverterElementerTilBrevbakerformat(innhold)

            showIf(data.harUtbetaling) {
                includePhrase(BarnepensjonFellesFraser.UtbetalingAvBarnepensjon(data.erEtterbetaling, data.bosattUtland, data.frivilligSkattetrekk))
            }
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
    }
}