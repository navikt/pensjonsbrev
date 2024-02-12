package no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattBarnepensjonDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattBarnepensjonDTOSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattBarnepensjonDTOSelectors.erUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattBarnepensjonDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.beregningAvBarnepensjonGammeltOgNyttRegelverk

data class ForhaandsvarselGjenoppstaattBarnepensjonDTO(
    override val innhold: List<Element>,
    val beregning: BarnepensjonBeregning,
    val erUnder18Aar: Boolean,
    val erBosattUtlandet: Boolean,
) : BrevDTO

@TemplateModelHelpers
object ForhaandsvarselGjenoppstaattBarnepensjon : EtterlatteTemplate<ForhaandsvarselGjenoppstaattBarnepensjonDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_FORHAANDSVARSEL_GJENOPPSTAATT
    override val template: LetterTemplate<*, ForhaandsvarselGjenoppstaattBarnepensjonDTO> = createTemplate(
        name = kode.name,
        letterDataType = ForhaandsvarselGjenoppstaattBarnepensjonDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Endring av barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Language.Bokmal to "Forhåndsvarsel om ny barnepensjon fra 1. januar 2024",
                Language.Nynorsk to "",
                Language.English to "",
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(BarnepensjonInnvilgelseFraser.HarDuSpoersmaal(erUnder18Aar, erBosattUtlandet))
        }

        // Beregning av barnepensjon nytt og gammelt regelverk
        includeAttachment(beregningAvBarnepensjonGammeltOgNyttRegelverk, beregning)
    }
}