package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDataSelectors.beregning
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDataSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDataSelectors.erEtterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDataSelectors.erUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDataSelectors.frivilligSkattetrekk
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.beregningAvBarnepensjonGammeltOgNyttRegelverk
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikterBosattUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikterNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomHandlerPaaVegneAvBarnetUtland
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjonNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjonUtland

@TemplateModelHelpers
object EnkeltVedtakOmregningNyttRegelverkFerdig : EtterlatteTemplate<BarnepensjonOmregnetNyttRegelverkFerdigDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VEDTAK_OMREGNING_FERDIG
    override val template: LetterTemplate<*, BarnepensjonOmregnetNyttRegelverkFerdigDTO> = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Endring av barnepensjon",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Vedtak - endring av barnepensjon" },
                nynorsk { +"Vi har endra barnepensjonen din" },
                english { +"Draft decision – adjustment of children's pension" },
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(BarnepensjonFellesFraser.UtbetalingAvBarnepensjon(data.erEtterbetaling, data.erBosattUtlandet, data.frivilligSkattetrekk))
            includePhrase(BarnepensjonFellesFraser.MeldFraOmEndringer)
            includePhrase(Felles.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(data.erUnder18Aar, data.erBosattUtlandet))
        }

        // Beregning av barnepensjon nytt og gammelt regelverk
        includeAttachment(beregningAvBarnepensjonGammeltOgNyttRegelverk, data.beregning)

        // Vedlegg under 18 år
        includeAttachment(
            informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal,
            data.erUnder18Aar.and(data.erBosattUtlandet.not())
        )
        includeAttachment(
            informasjonTilDegSomHandlerPaaVegneAvBarnetUtland,
            data.erUnder18Aar.and(data.erBosattUtlandet)
        )

        // Vedlegg over 18 år
        includeAttachment(
            informasjonTilDegSomMottarBarnepensjonNasjonal,
            data.erUnder18Aar.not().and(data.erBosattUtlandet.not())
        )
        includeAttachment(
            informasjonTilDegSomMottarBarnepensjonUtland,
            data.erUnder18Aar.not().and(data.erBosattUtlandet)
        )

        includeAttachment(dineRettigheterOgPlikterBosattUtland, data.erBosattUtlandet)
        includeAttachment(dineRettigheterOgPlikterNasjonal, data.erBosattUtlandet.not())
    }
}