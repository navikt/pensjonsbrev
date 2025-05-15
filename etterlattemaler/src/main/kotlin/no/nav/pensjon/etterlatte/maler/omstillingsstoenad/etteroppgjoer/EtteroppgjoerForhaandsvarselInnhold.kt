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
            displayTitle = "Etteroppgjør Forhåndsvarsel Innhold",
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

            showIf(
                data.bosattUtland.not() and data.norskInntekt.not()) {
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
                    textExpr(Language.Bokmal to "Vår beregning viser at du har fått ".expr() + data.avviksBeloep.absoluteValue().format() +" kroner for mye omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger ett rettsgebyr, som betyr at du må betale tilbake det feilutbetalte beløpet.",
                        Language.Nynorsk to "".expr(),
                        Language.English to "".expr())
                }
            }

            // dersom etterbetaling
            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.ETTERBETALING)){
                paragraph {
                    textExpr(
                        Language.Bokmal to "Vår beregning viser at du har fått utbetalt ".expr() + data.avviksBeloep.absoluteValue().format() +" kroner for lite omstillingsstønad i " + data.etteroppgjoersAar.format() + ". Dette overstiger 25 prosent av rettsgebyret.",
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
                showIf(data.avviksBeloep.equalTo(0)){
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
                            Language.Bokmal to "Vår beregning viser at du har fått utbetalt ".expr() + data.avviksBeloep.absoluteValue().format() +" kroner "+ ifElse(data.avviksBeloep.greaterThan(0), "for lite", "for mye") +" i omstillingsstønad for "+data.etteroppgjoersAar.format()+". Dette er innenfor toleransegrensen, og det vil derfor ikke bli "+ ifElse(data.avviksBeloep.greaterThan(0),"tilbakekrevd","etterbetalt") +" omstillingsstønad for "+data.etteroppgjoersAar.format()+".",
                            Language.Nynorsk to "".expr(),
                            Language.English to "".expr()
                        )
                    }
                }
            }
        }
    }
}
