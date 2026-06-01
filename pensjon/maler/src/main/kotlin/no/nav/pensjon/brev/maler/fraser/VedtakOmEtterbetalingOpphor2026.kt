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
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

class VedtakOmEtterbetalingOpphor2026 {
    data class Outline(private val etterbetaling: Expression<Kroner>, private val hjemler: Expression<Collection<String>>, private val erRedigerbar: Expression<Boolean> = false.expr()) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

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
                        +"Stortinget har vedtatt en lovendring som gir deg en lavere reduksjonsprosent. Dette er til fordel for deg. Den nye reduksjonsprosenten skal gjelde fra 1. januar 2026. Siden du har hatt inntekt over inntektsgrensen i perioden etterpå, fører det til at du får en etterbetaling. "
                    },
                    nynorsk {
                        +"Stortinget har vedteke ei lovendring som gir deg ein lågare reduksjonsprosent. Dette er til fordel for deg. Den nye reduksjonsprosenten skal gjelde frå 1. januar 2026. Sidan du har hatt inntekt over inntektsgrensa i perioden etterpå, fører det til at du får ei etterbetaling. "
                    },
                )
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