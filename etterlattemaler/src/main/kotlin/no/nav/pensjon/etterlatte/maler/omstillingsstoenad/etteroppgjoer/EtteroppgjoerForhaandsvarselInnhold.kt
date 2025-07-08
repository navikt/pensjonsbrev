package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.absoluteValue
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselInnholfDTOSelectors.avviksBeloep
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselInnholfDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselInnholfDTOSelectors.dagensDato
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselInnholfDTOSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselInnholfDTOSelectors.norskInntekt
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselInnholfDTOSelectors.resultatType
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselInnholfDTOSelectors.rettsgebyrBeloep
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselRedigerbartBrevDTOSelectors.data
import java.time.LocalDate

data class EtteroppgjoerForhaandsvarselInnholfDTO(
    val bosattUtland: Boolean,
    val norskInntekt: Boolean,
    val etteroppgjoersAar: Int,
    val rettsgebyrBeloep: Kroner,
    val resultatType: EtteroppgjoerResultatType,
    val avviksBeloep: Kroner,
){
    val dagensDato: LocalDate = LocalDate.now()
}

data class EtteroppgjoerForhaandsvarselRedigerbartBrevDTO(
    val data: EtteroppgjoerForhaandsvarselInnholfDTO
) : RedigerbartUtfallBrevDTO


@TemplateModelHelpers
object EtteroppgjoerForhaandsvarselInnhold : EtterlatteTemplate<EtteroppgjoerForhaandsvarselRedigerbartBrevDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_FORHAANDSVARSEL_INNHOLD

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EtteroppgjoerForhaandsvarselRedigerbartBrevDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - Etteroppgjør Innhold",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "",
                Language.Nynorsk to "",
                Language.English to "",
            )
        }

        outline {

            // alle med norsk inntekt
            showIf(data.norskInntekt) {
                paragraph {
                    text(
                        Language.Bokmal to "Hvert år sjekker Nav inntekten din for å se om du har fått utbetalt riktig beløp i omstillingsstønad året før. Omstillingsstønaden din er beregnet etter opplysninger om inntekt fra Skatteetaten og a-ordningen.",
                        Language.Nynorsk to "Kvar haust sjekkar Nav inntekta di for å sjå om du fekk utbetalt rett beløp i omstillingsstønad året før. Omstillingsstønaden din er rekna ut etter opplysningar om inntekt frå Skatteetaten og a-ordninga.",
                        Language.English to "Each year, Nav checks your income to see if you have been paid the correct amount of adjustment allowance in the previous year. Your adjustment allowance is calculated based on information about your income obtained from the Tax Administration and A-scheme.",
                    )
                }
            }

            // hvis bosatt utland
            showIf(data.bosattUtland){
                paragraph {
                    textExpr(
                        Language.Bokmal to "Inntektsopplysninger innhentet fra Skatteetaten og a-ordningen, viser kun norsk inntekt. Siden vi ikke mottar opplysninger om utenlandsk inntekt, har vi lagt til grunn det du tidligere oppga som forventet utenlandsk inntekt. Hvis disse opplysningene ikke stemmer, må du sende oss dokumentasjon på din faktiske inntekt fra utlandet i ".expr() +  data.etteroppgjoersAar.format() + ".",
                        Language.Nynorsk to "Inntektsopplysningar som er henta inn frå Skatteetaten og a-ordninga, viser berre norsk inntekt. Ettersom vi ikkje får opplysningar om utanlandsk inntekt, har vi lagt til grunn det du tidlegare har oppgitt som forventa utanlandsk inntekt. Dersom desse opplysningane ikkje stemmer, må du sende oss dokumentasjon på den faktiske inntekta di frå utlandet i ".expr() + data.etteroppgjoersAar.format() + ".",
                        Language.English to "Income information obtained from the Tax Administration and A-scheme only shows Norwegian income. As we do not receive information about foreign income, we have used the information you previously provided about expected foreign income as a basis. If this information is not correct, you must send us documentation of your actual income from other countries in ".expr() + data.etteroppgjoersAar.format() + ".",
                    )
                }
            }

            // alle
            paragraph {
                text(
                    Language.Bokmal to "Hvis etteroppgjøret viser at for lite utbetalt er mindre enn 25 prosent av rettsgebyret vil du ikke få utbetalt differansen.",
                    Language.Nynorsk to "Dersom etteroppgjeret viser at for lite utbetalt er mindre enn 25 prosent av rettsgebyret, vil du ikkje få utbetalt differansen.",
                    Language.English to "If the final settlement shows that the amount paid too little is less than 25 percent of a standard court fee, you will not be paid the difference.",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis etteroppgjøret viser at for mye utbetalt er mindre enn ett rettsgebyr, vil du ikke få krav om tilbakebetaling.",
                    Language.Nynorsk to "Dersom etteroppgjeret viser at for mykje utbetalt er mindre enn eitt rettsgebyr, vil du ikkje få krav om tilbakebetaling.",
                    Language.English to "If the final settlement shows that the amount overpaid is less than a standard court fee, you will not receive a demand for repayment.",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Per 31. Desember ".expr() + data.etteroppgjoersAar.format() + " er ett rettsgebyr " + data.rettsgebyrBeloep.format() + " kroner.",
                    Language.Nynorsk to "Per 31. desember ".expr() + data.etteroppgjoersAar.format() + " er eitt rettsgebyr " + data.rettsgebyrBeloep.format() + " kroner.",
                    Language.English to "As of 31 December ".expr() + data.etteroppgjoersAar.format() + " a standard court fee is NOK " + data.rettsgebyrBeloep.format() + ".",
                )
            }

            // dersom feilutbetalt beløp (tilbakekreving)
            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)){
                paragraph {
                    textExpr(Language.Bokmal to "Vår beregning viser at du har fått ".expr() + data.avviksBeloep.absoluteValue().format() +" kroner for mye omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger ett rettsgebyr, som betyr at du må betale tilbake det feilutbetalte beløpet.",
                        Language.Nynorsk to "Utrekninga vår viser at du har fått ".expr() + data.avviksBeloep.absoluteValue().format() +" kroner for mykje i omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstig eitt rettsgebyr, og du må difor betale tilbake det feilutbetalte beløpet.",
                        Language.English to "Our calculations show that you have been overpaid NOK ".expr() + data.avviksBeloep.absoluteValue().format() +" in adjustment allowance in " + data.etteroppgjoersAar.format() + ". This exceeds a standard court fee, which means that you must repay the incorrect amount paid to you.",
                    )
                }
            }

            // dersom etterbetaling
            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.ETTERBETALING)){
                paragraph {
                    textExpr(
                        Language.Bokmal to "Vår beregning viser at du har fått utbetalt ".expr() + data.avviksBeloep.absoluteValue().format() +" kroner for lite omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger 25 prosent av rettsgebyret.",
                        Language.Nynorsk to "Utrekninga vår viser at du har fått utbetalt ".expr() + data.avviksBeloep.absoluteValue().format() +" kroner for lite i omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstig 25 prosent av rettsgebyret",
                        Language.English to "Our calculations show that you have been paid NOK ".expr() + data.avviksBeloep.absoluteValue().format() +" too little in adjustment allowance in " + data.etteroppgjoersAar.format() + ". This exceeds 25 percent of a standard court fee."
                    )
                }

                paragraph {
                    text(
                        Language.Bokmal to "Du kan få etterbetalt omstillingsstønad hvis du har vært i minst 50 prosent aktivitet eller har fått unntak for aktivitetsplikten. Du kan lese om aktivitetsplikten på nav.no/omstillingsstonad#aktivitet.",
                        Language.Nynorsk to "Du kan få etterbetalt omstillingsstønad dersom du har vore i minst 50 prosent aktivitet eller har fått unntak for aktivitetsplikta. Du kan lese meir om aktivitetsplikta på nav.no/omstillingsstonad#aktivitet.",
                        Language.English to "You can receive post-payment of adjustment allowance if you have had a minimum of 50 percent activity or if you have been granted an exemption from the activity obligation. You can read about the activity obligation at: nav.no/omstillingsstonad#aktivitet."
                    )
                }
            }

            // dersom ingen endring
            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.INGEN_ENDRING)){
                showIf(data.avviksBeloep.equalTo(0)){
                    paragraph {
                        textExpr(
                            Language.Bokmal to "Vår beregning viser at utbetalt omstillingsstønad i ".expr() + data.etteroppgjoersAar.format()+ " er lik det du skulle ha fått. Du har fått utbetalt riktig stønad.",
                            Language.Nynorsk to "Utrekninga vår viser at utbetalt omstillingsstønad i ".expr() + data.etteroppgjoersAar.format()+ " er lik det du skulle ha fått. Du har fått utbetalt rett stønad.",
                            Language.English to "Our calculations show that the amount of adjustment allowance you have been paid in ".expr() + data.etteroppgjoersAar.format()+ " is equal to the amount you should have received. You have been paid the correct allowance."
                        )
                    }
                }.orShow {
                    // for lite utbetalt mindre en 0,25 RG eller for mye utbetalt mindre en 1 RG
                    paragraph {
                        textExpr(
                            Language.Bokmal to "Vår beregning viser at du har fått utbetalt ".expr() + data.avviksBeloep.absoluteValue().format() +" kroner "+ ifElse(data.avviksBeloep.greaterThan(0), "for mye", "for lite") +" i omstillingsstønad for "+data.etteroppgjoersAar.format()+". Dette er innenfor toleransegrensen, og det vil derfor ikke bli "+ ifElse(data.avviksBeloep.greaterThan(0),"tilbakekrevd","etterbetalt") +" omstillingsstønad for "+data.etteroppgjoersAar.format()+".",
                            Language.Nynorsk to "Utrekninga vår viser at du har fått utbetalt ".expr() + data.avviksBeloep.absoluteValue().format() +" kroner "+ ifElse(data.avviksBeloep.greaterThan(0), "for mykje", "for lite") +" i omstillingsstønad for "+data.etteroppgjoersAar.format()+". Dette er innanfor toleransegrensa, og det vil difor ikkje bli "+ ifElse(data.avviksBeloep.greaterThan(0),"kravd tilbake","etterbetalt") +" omstillingsstønad for "+data.etteroppgjoersAar.format()+".",
                            Language.English to "Our calculations show that you have been paid NOK ".expr() + data.avviksBeloep.absoluteValue().format() +" "+ ifElse(data.avviksBeloep.greaterThan(0), "too much", "too little") +" in adjustment allowance for "+data.etteroppgjoersAar.format()+". This is within tolerance limits - therefore there will be no "+ ifElse(data.avviksBeloep.greaterThan(0),"demand for repayment","post-payment") +" of adjustment allowance for "+data.etteroppgjoersAar.format()+"."
                        )
                    }
                }
            }
        }
    }
}
