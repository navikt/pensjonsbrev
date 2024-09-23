package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTOSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTOSelectors.erUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTOSelectors.frivilligSkattetrekk
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkFerdigDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
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
        name = kode.name,
        letterDataType = BarnepensjonOmregnetNyttRegelverkFerdigDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Endring av barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Bokmal to "Vedtak - endring av barnepensjon",
                Nynorsk to "Vi har endra barnepensjonen din",
                English to "Draft decision – adjustment of children's pension",
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(BarnepensjonFellesFraser.UtbetalingAvBarnepensjon(etterbetaling, frivilligSkattetrekk, erBosattUtlandet))
            includePhrase(BarnepensjonFellesFraser.MeldFraOmEndringer)
            includePhrase(BarnepensjonFellesFraser.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(erUnder18Aar, erBosattUtlandet))
        }

        // Beregning av barnepensjon nytt og gammelt regelverk
        includeAttachment(beregningAvBarnepensjonGammeltOgNyttRegelverk, beregning)

        // Vedlegg under 18 år
        includeAttachment(
            informasjonTilDegSomHandlerPaaVegneAvBarnetNasjonal,
            innhold,
            erUnder18Aar.and(erBosattUtlandet.not())
        )
        includeAttachment(
            informasjonTilDegSomHandlerPaaVegneAvBarnetUtland,
            innhold,
            erUnder18Aar.and(erBosattUtlandet)
        )

        // Vedlegg over 18 år
        includeAttachment(
            informasjonTilDegSomMottarBarnepensjonNasjonal,
            innhold,
            erUnder18Aar.not().and(erBosattUtlandet.not())
        )
        includeAttachment(
            informasjonTilDegSomMottarBarnepensjonUtland,
            innhold,
            erUnder18Aar.not().and(erBosattUtlandet)
        )

        includeAttachment(dineRettigheterOgPlikterBosattUtland, innhold, erBosattUtlandet)
        includeAttachment(dineRettigheterOgPlikterNasjonal, innhold, erBosattUtlandet.not())
    }
}