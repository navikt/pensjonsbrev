package no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.brukerUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.forhaandsvarselFeilutbetalingBarnepensjonOpphoer
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke
import java.time.LocalDate

data class BarnepensjonOpphoerDTO(
    override val innhold: List<Element>,
    val innholdForhaandsvarsel: List<Element>,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
    val virkningsdato: LocalDate,
    val feilutbetaling: FeilutbetalingType,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object BarnepensjonOpphoer : EtterlatteTemplate<BarnepensjonOpphoerDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_OPPHOER

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = BarnepensjonOpphoerDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
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
                    English to "We have terminated your children's pension",
                )
            }
            outline {
                paragraph {
                    textExpr(
                        Bokmal to "Barnepensjonen din opphører fra ".expr() + virkningsdato.format() + ".",
                        Nynorsk to "Barnepensjonen din fell bort frå og med ".expr() + virkningsdato.format() + ".",
                        English to "Your children's pension will terminate on ".expr() + virkningsdato.format() + ".",
                    )
                }
                konverterElementerTilBrevbakerformat(innhold)
                showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL)) {
                    includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingMedVarselOpphoer)
                }
                showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                    includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingUtenVarselOpphoer)
                }
                includePhrase(BarnepensjonFellesFraser.DuHarRettTilAaKlage)
                includePhrase(BarnepensjonFellesFraser.DuHarRettTilInnsyn)
                includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(brukerUnder18Aar, bosattUtland))
            }

            // Nasjonal
            includeAttachment(klageOgAnke(bosattUtland = false), innhold, bosattUtland.not())

            // Bosatt utland
            includeAttachment(klageOgAnke(bosattUtland = true), innhold, bosattUtland)

            includeAttachment(forhaandsvarselFeilutbetalingBarnepensjonOpphoer, this.argument, feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL))
        }
}
