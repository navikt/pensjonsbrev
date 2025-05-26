package no.nav.pensjon.etterlatte.maler.barnepensjon.varsel

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregning
import no.nav.pensjon.etterlatte.maler.ElementListe
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselDTOSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselDTOSelectors.erUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.beregningAvBarnepensjonNyttRegelverk


data class BarnepensjonVarselDTO(
    override val innhold: ElementListe,
    val beregning: BarnepensjonBeregning?,
    val erUnder18Aar: Boolean,
    val erBosattUtlandet: Boolean,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object BarnepensjonVarsel : EtterlatteTemplate<BarnepensjonVarselDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VARSEL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonVarselDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varselbrev barnepensjon",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Forhåndsvarsel om ny barnepensjon fra 1. januar 2024",
                Language.Nynorsk to "Førehandsvarsel om ny barnepensjon frå 1. januar 2024",
                Language.English to "Advance notice of new children’s pension from 1 January",
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(erUnder18Aar, erBosattUtlandet))
        }

        includeAttachmentIfNotNull(beregningAvBarnepensjonNyttRegelverk, beregning)
    }
}