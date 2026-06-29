package no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.selectors.barnepensjonOpphoerDTO.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.selectors.barnepensjonOpphoerData.*
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.forhaandsvarselFeilutbetalingBarnepensjonOpphoer
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke
import java.time.LocalDate

data class BarnepensjonOpphoerData(
    val innholdForhaandsvarsel: List<Element>,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
    val virkningsdato: LocalDate,
    val feilutbetaling: FeilutbetalingType,
)

data class BarnepensjonOpphoerDTO(
    override val innhold: List<Element>,
    override val data: BarnepensjonOpphoerData,
) : VedleggData, FerdigstillingBrevDTO
@TemplateModelHelpers
object BarnepensjonOpphoer : EtterlatteTemplate<BarnepensjonOpphoerDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_OPPHOER

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - opphør",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Vi har opphørt barnepensjonen din" },
                nynorsk { +"Vi har avvikla barnepensjonen din" },
                english { +"We have terminated your children's pension" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Barnepensjonen din opphører fra " + data.virkningsdato.format() + "." },
                    nynorsk { +"Barnepensjonen din fell bort frå og med " + data.virkningsdato.format() + "." },
                    english { +"Your children's pension will terminate on " + data.virkningsdato.format() + "." },
                )
            }
            konverterElementerTilBrevbakerformat(innhold)
            showIf(data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL)) {
                includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingMedVarselOpphoer)
            }
            showIf(data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingUtenVarselOpphoer)
            }
            includePhrase(Felles.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonFellesFraser.DuHarRettTilInnsyn)
            includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(data.brukerUnder18Aar, data.bosattUtland))
        }

        // Nasjonal
        includeAttachment(klageOgAnke(bosattUtland = false), data.bosattUtland.not())

        // Bosatt utland
        includeAttachment(klageOgAnke(bosattUtland = true), data.bosattUtland)

        includeAttachment(forhaandsvarselFeilutbetalingBarnepensjonOpphoer, this.argument, data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL))
    }
}
