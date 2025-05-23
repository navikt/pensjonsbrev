package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

@TemplateModelHelpers
val praktiskInformasjonOmEtteroppgjoeret: AttachmentTemplate<LangBokmalNynorskEnglish, Unit> =
    createAttachment(
        title =
        newText(
            Bokmal to "Praktisk informasjon om etteroppgjøret",
            Nynorsk to "",
            English to "",
        ),
        includeSakspart = false,
    ) {

        paragraph {

            text(
                Bokmal to "Du vil motta betalingsinformasjon fra Skatteetaten på én av følgende måter:",
                Nynorsk to "",
                English to "",
            )

            list {
                item {
                    text(
                        Bokmal to "Du kan få faktura rett i nettbanken. Da må du åpne fakturaen i nettbanken for å lese informasjon om tilbakebetaling.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                item {
                    text(
                        Bokmal to "Hvis du ikke har takket ja til eFaktura i nettbanken din, får du fakturaen med betalingsinformasjon i Altinn.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                item {
                    text(
                        Bokmal to "Har du reservert deg mot digital post, får du fakturaen i posten.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }

        title2 {
            text(
                Bokmal to "Slik betaler du tilbake",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
            Bokmal to "Du kan betale tilbake hele beløpet med en gang, eller du kan be om å få dele opp beløpet. Husk at du ikke kan betale tilbake før du har fått brevet fra Skatteetaten. Hvis du ikke betaler, har Skatteetaten mulighet til å trekke deg i lønn etter bidragsinnkrevingsloven § 11.",
            Nynorsk to "",
            English to ""
            )
        }
        paragraph {
            list {
                item {
                    text(
                        Bokmal to "Du kan betale hele beløpet med en gang Du betaler hele beløpet i faktura fra Skatteetaten.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                item {
                    text(
                        Bokmal to "Du kan betale i avdrag Du kan avtale å betale tilbake beløpet i avdrag. Ta kontakt med Skatteetaten hvis du ønsker å betale i avdrag, eller hvis du ønsker å øke eller redusere trekkprosenten.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                item {
                    text(
                        Bokmal to "Du kan få trekk i støtte fra Nav Betaler du ikke hele beløpet, og du fortsatt får uføretrygd eller annen pengestøtte fra Nav, kan Skatteetaten automatisk trekke fra denne støtten. Normalt trekker vi 10 prosent av innvilget uføretrygd frem til hele beløpet er betalt. Vil du betale ned raskere, kan du be oss om å trekke mer enn 10 prosent.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
        paragraph {
            text(
                Bokmal to "Trekket blir gjort etter at skatten er trukket fra, men før eventuelle andre trekk.",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Har du allerede trekk fra forrige etteroppgjør, kan du få to trekk.",
                Nynorsk to "",
                English to ""
            )
        }

        title2 {
            text(
                Bokmal to "Inntekter som kan holdes utenfor etteroppgjøret",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Har du inntekter som kan holdes utenfor etteroppgjøret som ikke allerede er tatt med i beregningen, må du sende oss dokumentasjon på det i posten innen 3 uker.",
                Nynorsk to "",
                English to ""
            )
        }

        paragraph {
            text(
                Bokmal to "Eksempler på dette er:",
                Nynorsk to "",
                English to ""
            )

            list {
                item {
                    text(
                        Bokmal to "Inntekt fra arbeid eller utbetaling fra en virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel feriepenger.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                item {
                    text(
                        Bokmal to "Erstatningsoppgjør etter skadeerstatningsloven § 3‑1 (også voldsoffererstatning), yrkesskadeloven § 13, og pasientskadeloven § 4 første ledd.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }

        paragraph {
            text(
                Bokmal to "Eksempler på dokumentasjon for inntekter som kan holdes utenfor etteroppgjøret:",
                Nynorsk to "",
                English to ""
            )
            list {
                item {
                    text(
                        Bokmal to "lønnsslipp",
                        Nynorsk to "",
                        English to "",
                    )
                }
                item {
                    text(
                        Bokmal to "dokumentasjon fra regnskapsfører",
                        Nynorsk to "",
                        English to "",
                    )
                }
                item {
                    text(
                        Bokmal to "kopi av vedtak for erstatning",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
        paragraph {
            text(
                Bokmal to "Du trenger ikke å klage på vedtaket hvis du kun skal be om at inntekt skal holdes utenfor etteroppgjøret.",
                Nynorsk to "",
                English to ""
            )
        }

        title2 {
            text(
                Bokmal to "Hvordan ettersende dokumentasjon",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan sende oss dokumentasjon både digitalt og på papir:",
                Nynorsk to "",
                English to "",
            )
        }

        paragraph {
            list {
                item {
                    text(
                        Bokmal to "For å sende dokumentasjon digitalt, gå inn på nav.no/uføre‑ettersende.",
                        Nynorsk to "",
                        English to "",
                    )
                }

                item {
                    text(
                        Bokmal to "Når du sender inn dokumenter i posten, må du sende med en førsteside for innsending. Den finner du ved å gå inn på nav.no/uføre‑ettersende‑post. Når vi har mottatt dokumentasjon fra deg, vil vi vurdere om inntekten skal holdes utenfor.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }

        paragraph {
            text(
                Bokmal to "Du får et nytt brev når vi har gjort et nytt etteroppgjør. Du kan lese mer om etteroppgjør på nav.no/etteroppgjor. Saksnummer: 22983694 side 1 av 2",
                Nynorsk to "",
                English to "",
            )
        }


    }


