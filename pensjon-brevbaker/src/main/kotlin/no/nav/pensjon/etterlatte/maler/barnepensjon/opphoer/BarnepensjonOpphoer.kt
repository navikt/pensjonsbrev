package no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.forhaandsvarselFeilutbetalingBarnepensjonOpphoer
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnkeNasjonal
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnkeUtland

data class BarnepensjonOpphoerDTO(
    override val innhold: List<Element>,
    val innholdForhaandsvarsel: List<Element>,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
    val feilutbetaling: FeilutbetalingType
) : BrevDTO
@TemplateModelHelpers
object BarnepensjonOpphoer : EtterlatteTemplate<BarnepensjonOpphoerDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_OPPHOER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonOpphoerDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Vi har opphørt barnepensjonen din",
                Nynorsk to "Vi har avvikla barnepensjonen din",
                English to "We have terminated your application for a children's pension",
            )
        }
        outline {
            konverterElementerTilBrevbakerformat(innhold)
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL)) {
                includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingMedVarselOpphoer)
            }
            includePhrase(BarnepensjonFellesFraser.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonFellesFraser.DuHarRettTilInnsyn)
            includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaalNy(brukerUnder18Aar, bosattUtland))
        }

        // Nasjonal
        includeAttachment(klageOgAnkeNasjonal, innhold, bosattUtland.not())

        // Bosatt utland
        includeAttachment(klageOgAnkeUtland, innhold, bosattUtland)

        includeAttachment(forhaandsvarselFeilutbetalingBarnepensjonOpphoer, this.argument, feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL))
    }
}
