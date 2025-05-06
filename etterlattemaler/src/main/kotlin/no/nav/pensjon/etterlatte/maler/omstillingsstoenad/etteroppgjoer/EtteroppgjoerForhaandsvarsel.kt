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
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselBrevDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.dagensDato
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.norskInntekt
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.opplysningerOmEtteroppgjoeretData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.resultatType
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.rettsgebyrBeloep
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.utbetalingData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerUtbetalingDTOSelectors.avviksBeloep
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.OpplysningerOmEtteroppgjoeretData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.opplysningerOmEtteroppgjoeret
import java.time.LocalDate

data class EtteroppgjoerUtbetalingDTO(
    val inntekt: Kroner,
    val faktiskInntekt: Kroner,
    val avviksBeloep: Kroner
)

data class EtteroppgjoerForhaandsvarselBrevDTO(
    override val innhold: List<Element>,
    val data: EtteroppgjoerForhaandsvarselDTO
) : FerdigstillingBrevDTO

enum class EtteroppgjoerResultatType{
    TILBAKEKREVING,
    ETTERBETALING,
    IKKE_ETTEROPPGJOER
}

data class EtteroppgjoerForhaandsvarselDTO(
    val bosattUtland: Boolean,
    val norskInntekt: Boolean,
    val etteroppgjoersAar: Int,
    val rettsgebyrBeloep: Kroner,
    val resultatType: EtteroppgjoerResultatType,
    val inntekt: Kroner,
    val faktiskInntekt: Kroner,
    val avviksBeloep: Kroner
){
    val utbetalingData = EtteroppgjoerUtbetalingDTO(inntekt, faktiskInntekt, avviksBeloep)
    val opplysningerOmEtteroppgjoeretData = OpplysningerOmEtteroppgjoeretData(etteroppgjoersAar, utbetalingData)
    val dagensDato: LocalDate = LocalDate.now()
}

@TemplateModelHelpers
object EtteroppgjoerForhaandsvarsel : EtterlatteTemplate<EtteroppgjoerForhaandsvarselBrevDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_FORHAANDSVARSEL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EtteroppgjoerForhaandsvarselBrevDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Etteroppgjør Forhåndsvarsel",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {

            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.IKKE_ETTEROPPGJOER)){
                textExpr(
                    Language.Bokmal to "Informasjon om etteroppgjør av omstillingsstønad for ".expr() + data.etteroppgjoersAar.format(),
                    Language.Nynorsk to "".expr() + data.etteroppgjoersAar.format(),
                    Language.English to "".expr() + data.etteroppgjoersAar.format(),
                )
            }.orShow {
                textExpr(
                    Language.Bokmal to "Forhåndsvarsel om etteroppgjør av omstillingsstønad for ".expr() + data.etteroppgjoersAar.format(),
                    Language.Nynorsk to "".expr() + data.etteroppgjoersAar.format(),
                    Language.English to "".expr() + data.etteroppgjoersAar.format(),
                )
            }

        }

        outline {
            showIf(data.bosattUtland.not() and data.norskInntekt.not()) {
                // felles minus bosatt utland og uten norsk inntekt
                paragraph {
                    text(
                        Language.Bokmal to "Hvert år, når skatteoppgjøret er ferdig, sjekker Nav inntekten din for å se om du har fått utbetalt riktig beløp i omstillingsstønad året før. Omstillingsstønaden din er beregnet basert på nye opplysninger fra Skatteetaten.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }.orShow {
                // hvis bosatt utland med norsk inntekt
                paragraph {
                    textExpr(
                        Language.Bokmal to "Skatteoppgjøret viser kun norsk inntekt. Siden vi ikke mottar opplysninger om utenlandsk inntekt, har vi lagt til grunn det du tidligere oppga som forventet utenlandsk inntekt. Hvis disse opplysningene ikke stemmer, må du sende oss dokumentasjon på din faktiske inntekt fra utlandet i ".expr() +  data.etteroppgjoersAar.format() + ".",
                        Language.Nynorsk to "".expr(),
                        Language.English to "".expr(),
                    )
                }
            }

            // alle
            paragraph {
                textExpr(
                    Language.Bokmal to "Etteroppgjør skal ikke gjennomføres hvis for lite utbetalt er mindre enn 25 prosent av rettsgebyret, eller hvis for mye utbetalt er mindre enn ett rettsgebyr. Per ".expr() + data.dagensDato.format() + " er ett rettsgebyr " + data.rettsgebyrBeloep.format() + " kroner.",
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
            }

            // dersom feilutbetalt beløp
            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)){
                paragraph {
                    textExpr(Language.Bokmal to "Vår beregning viser at du har fått ".expr() + data.utbetalingData.avviksBeloep.absoluteValue().format() +" kroner for mye omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger ett rettsgebyr, som betyr at du må betale tilbake det feilutbetalte beløpet.",
                        Language.Nynorsk to "".expr(),
                        Language.English to "".expr())
                }
            }

            // dersom etterbetaling
            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.ETTERBETALING)){
                paragraph {
                    textExpr(
                        Language.Bokmal to "Vår beregning viser at du har fått utbetalt ".expr() + data.utbetalingData.avviksBeloep.absoluteValue().format() +" kroner for lite omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger 25 prosent av rettsgebyret.",
                        Language.Nynorsk to "".expr(),
                        Language.English to "".expr()
                    )
                }

                paragraph {
                    text(
                        Language.Bokmal to "Du kan få etterbetalt omstillingsstønad hvis du har vært i minst 50 prosent aktivitet eller har fått unntak for aktivitetsplikten. Du kan lese om aktivitetsplikten på nav.no/omstillingsstonad#aktivitet.",
                        Language.Nynorsk to "",
                        Language.English to ""
                    )
                }
            }

            // dersom ingen endring
            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.IKKE_ETTEROPPGJOER)){
                showIf(data.utbetalingData.avviksBeloep.equalTo(0)){
                    paragraph {
                        textExpr(
                            Language.Bokmal to "Vår beregning viser at utbetalt omstillingsstønad i ".expr() + data.etteroppgjoersAar.format()+ " er lik det du skulle ha fått. Etteroppgjør vil derfor ikke bli gjennomført.",
                            Language.Nynorsk to "".expr(),
                            Language.English to "".expr()
                        )
                    }
                }.orShow {
                    // for lite utbetalt mindre en 0,25 RG eller for mye utbetalt mindre en 1 RG
                    paragraph {
                        textExpr(
                            Language.Bokmal to "Vår beregning viser at du har fått utbetalt ".expr() + data.utbetalingData.avviksBeloep.absoluteValue().format() +" kroner "+ ifElse(data.utbetalingData.avviksBeloep.greaterThan(0), "for lite", "for mye") +" i omstillingsstønad for "+data.etteroppgjoersAar.format()+". Dette er innenfor toleransegrensen, og det vil derfor ikke bli "+ifElse(data.utbetalingData.avviksBeloep.greaterThan(0),"tilbakekrevd","etterbetalt") +" omstillingsstønad for "+data.etteroppgjoersAar.format()+".",
                            Language.Nynorsk to "".expr(),
                            Language.English to "".expr()
                        )
                    }
                }
            }

            title2 {
                text(
                    Language.Bokmal to "Sjekk beregningen og meld fra hvis noe er feil",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Se ny beregning av omstillingsstønaden din for inntektsåret ".expr() + data.etteroppgjoersAar.format() + " i vedlegget “Opplysninger om etteroppgjøret”. Du må kontrollere om inntektene som er oppgitt er riktig. ",
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis opplysningene er feil, må du gi beskjed innen tre uker. Hvis du ikke svarer, får du et vedtak basert på de opplysningene som står i vedlegget. Hvis du melder fra om feil, vil vi vurdere de nye opplysningene før du får et vedtak.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            showIf(data.resultatType.equalTo("TILBAKEKREVING")) {
                paragraph {
                    text(
                        Language.Bokmal to "I vedtaket får du informasjon om hvordan du kan betale tilbake for mye utbetalt omstillingsstønad og hvordan du kan klage på vedtaket.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }

            includePhrase(Felles.HvordanMelderDuFra)
            includePhrase(OmstillingsstoenadFellesFraser.HarDuIkkeBankID(data.bosattUtland))
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }

        includeAttachment(opplysningerOmEtteroppgjoeret(), data.opplysningerOmEtteroppgjoeretData)
    }
}
