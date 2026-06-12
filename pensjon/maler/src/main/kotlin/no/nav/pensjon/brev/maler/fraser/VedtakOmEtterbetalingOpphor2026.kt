package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.maler.SamletMeldingOmPensjonsvedtak.fritekst
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.legacy.HjemmelFormatter
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

class VedtakOmEtterbetalingOpphor2026 {
    data class Outline(private val etterbetaling: Expression<Kroner>, private val hjemler: Expression<Collection<String>>, private val reduksjonsprosent: Expression<Double>, private val erRedigerbar: Expression<Boolean> = false.expr()) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(etterbetaling.greaterThan(0)) {
                paragraph {
                    text(
                        bokmal { +"Du får en etterbetaling fra oss på " + etterbetaling.format() + ". Den utbetales senest 20. juli." },
                        nynorsk { +"Du får ei etterbetaling frå oss på " + etterbetaling.format() + ". Den blir utbetalt seinast 20. juli." },
                    )
                }
                title1 {
                    text(
                        bokmal { +"Derfor får du en etterbetaling" },
                        nynorsk { +"Derfor får du ei etterbetaling" },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Stortinget har vedtatt en lovendring som gir deg en lavere reduksjonsprosent. Dette er til fordel for deg. Din nye reduksjonsprosent, som er " + reduksjonsprosent.format() + " skal gjelde fra 1. januar 2026. Siden du har hatt inntekt over inntektsgrensen i perioden etter 1. januar, fører det til at du får en etterbetaling. "
                        },
                        nynorsk {
                            +"Stortinget har vedteke ei lovendring som gir deg ein lågare reduksjonsprosent. Dette er til fordel for deg. Din nye reduksjonsprosent, som er " + reduksjonsprosent.format() + " skal gjelde frå 1. januar 2026. Sidan du har hatt inntekt over inntektsgrensa i perioden etter 1. januar, fører det til at du får ei etterbetaling. "
                        },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal {
                            +"Stortinget har vedtatt en lovendring som gir deg en lavere reduksjonsprosent. Den nye reduksjonsprosenten skal gjelde fra 1. januar 2026. "
                        },
                        nynorsk {
                            +"Stortinget har vedteke ei lovendring som gir deg ein lågare reduksjonsprosent. Den nye reduksjonsprosenten skal gjelde frå 1. januar 2026. "
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Dersom du har hatt inntekt over inntektsgrensen, vil dette være til fordel for deg, fordi du vil få en mindre avkortning av uføretrygden i etteroppgjøret. "
                        },
                        nynorsk {
                            +"Dersom du har hatt inntekt over inntektsgrensa, vil dette vere til fordel for deg, fordi du vil få ei mindre avkorting av uføretrygda i etteroppgjeret. "
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Dersom du ikke har hatt inntekt over inntektsgrensen, vil ikke endringen ha noen betydning for utbetalingen av uføretrygden din."
                        },
                        nynorsk {
                            +"Dersom du ikkje har hatt inntekt over inntektsgrensa, vil ikkje endringa ha nokon betydning for utbetalinga av uføretrygda di."
                        },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Vedtaket har vi gjort etter " },
                    nynorsk { +"Vedtaket har vi gjort etter " },
                )
                showIf(erRedigerbar) {
                    text(
                        bokmal { +fritekst("Evt legg inn 12-9(IFU)") + " " },
                        nynorsk { +fritekst("Evt legg inn 12-9(IFU)") + " " },
                    )

                }
                text(
                    bokmal { +hjemler.format(HjemmelFormatter(true)) + "." },
                    nynorsk { +hjemler.format(HjemmelFormatter(true)) + "." },
                )
            }
            showIf(etterbetaling.greaterThan(0)) {
                title1 {
                    text(
                        bokmal { +"Informasjon om etterbetaling" },
                        nynorsk { +"Informasjon om etterbetaling" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du får ikke renter på etterbetalingen. Informasjon om skattetrekk på etterbetalingen finner du hos Skatteetaten." },
                        nynorsk { +"Du får ikkje renter på etterbetalinga. Informasjon om skattetrekk på etterbetalinga finn du hos Skatteetaten." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Har du gjeld som Skatteetaten krever inn, kan pengene fra etterbetalingen gå til å dekke gjelden. Eksempler på gjeld kan være bidrags- eller feilutbetalingsgjeld hos Nav og refusjonskrav hos tjenestepensjonsordning." },
                        nynorsk { +"Har du gjeld som Skatteetaten krev inn, kan pengane frå etterbetalinga gå til å dekke gjelda. Eksempel på gjeld kan vere bidrags- eller feilutbetalingsgjeld hos Nav og refusjonskrav hos tenestepensjonsordning." },
                    )
                }
            }
        }
    }

    object RettTilAAKlage : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen vedtaket har kommet fram til deg. Du finner skjema og informasjon på " +
                                "${Constants.KLAGE_URL}."
                    },
                    nynorsk {
                        +"Om du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen vedtaket har kome fram til deg. Du finn skjema og informasjon på " +
                                "${Constants.KLAGE_URL}."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"I vedlegget " },
                    nynorsk { +"I vedlegget " },
                )
                namedReference(vedleggDineRettigheterOgPlikterUfore)
                text(
                    bokmal { +" får du vite mer om hvordan du går fram for å klage." },
                    nynorsk { +" får du vite meir om korleis du går fram for å klage." },
                )
            }
        }
    }
}