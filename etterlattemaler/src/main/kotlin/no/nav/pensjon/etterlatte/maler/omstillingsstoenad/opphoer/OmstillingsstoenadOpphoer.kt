package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
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
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.forhaandsvarselFeilutbetalingOmstillingsstoenadOpphoer
import java.time.LocalDate

data class OmstillingsstoenadOpphoerDTO(
    override val innhold: List<Element>,
    val innholdForhaandsvarsel: List<Element>,
    val virkningsdato: LocalDate,
    val bosattUtland: Boolean,
    val feilutbetaling: FeilutbetalingType
): FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadOpphoer : EtterlatteTemplate<OmstillingsstoenadOpphoerDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_OPPHOER

    override val template = createTemplate(
        letterDataType = OmstillingsstoenadOpphoerDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Opphør av omstillingsstønad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Vi har opphørt omstillingsstønaden din" },
                nynorsk { +"Vi har avvikla omstillingsstønaden din" },
                english { +"We have terminated your adjustment allowance" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Omstillingsstønaden din opphører fra " + virkningsdato.format() + "." },
                    nynorsk { +"Omstillingsstønaden din fell bort frå og med " + virkningsdato.format() + "." },
                    english { +"Your adjustment allowance will terminate on: " + virkningsdato.format() + "." },
                )
            }
            konverterElementerTilBrevbakerformat(innhold)
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL)) {
                includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingMedVarselOpphoer)
            }
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingUtenVarselOpphoer)
            }
            includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlageAvslagOpphoer)
            includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilInnsyn)
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }

        // Nasjonal
        includeAttachment(klageOgAnke(bosattUtland = false), innhold, bosattUtland.not())

        // Bosatt utland
        includeAttachment(klageOgAnke(bosattUtland = true), innhold, bosattUtland)

        includeAttachment(forhaandsvarselFeilutbetalingOmstillingsstoenadOpphoer, this.argument, feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL))
    }
}