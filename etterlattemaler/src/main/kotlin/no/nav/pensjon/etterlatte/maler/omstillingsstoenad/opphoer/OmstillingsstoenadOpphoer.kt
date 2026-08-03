package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer

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
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.selectors.omstillingsstoenadOpphoerDTO.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.selectors.omstillingsstoenadOpphoerData.*
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.forhaandsvarselFeilutbetalingOmstillingsstoenadOpphoer
import java.time.LocalDate

data class OmstillingsstoenadOpphoerData(
    val innholdForhaandsvarsel: List<Element>,
    val virkningsdato: LocalDate,
    val bosattUtland: Boolean,
    val feilutbetaling: FeilutbetalingType,
)

data class OmstillingsstoenadOpphoerDTO(
    override val innhold: List<Element>,
    override val data: OmstillingsstoenadOpphoerData,
) : VedleggData, FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadOpphoer : EtterlatteTemplate<OmstillingsstoenadOpphoerDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_OPPHOER

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Opphør av omstillingsstønad",
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
                    bokmal { +"Omstillingsstønaden din opphører fra " + data.virkningsdato.format() + "." },
                    nynorsk { +"Omstillingsstønaden din fell bort frå og med " + data.virkningsdato.format() + "." },
                    english { +"Your adjustment allowance will terminate on: " + data.virkningsdato.format() + "." },
                )
            }
            konverterElementerTilBrevbakerformat(innhold)
            showIf(data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL)) {
                includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingMedVarselOpphoer)
            }
            showIf(data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingUtenVarselOpphoer)
            }
            includePhrase(Felles.DuHarRettTilAaKlage)
            includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilInnsyn)
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }

        // Nasjonal
        includeAttachment(klageOgAnke(bosattUtland = false), data.bosattUtland.not())

        // Bosatt utland
        includeAttachment(klageOgAnke(bosattUtland = true), data.bosattUtland)

        includeAttachment(forhaandsvarselFeilutbetalingOmstillingsstoenadOpphoer, this.argument, data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL))
    }
}