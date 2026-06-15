package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

/**
 * Felles innhold for AFP etteroppgjør-vedtaksbrev som ender i tilbakekreving
 * (offentlig sektor / Statens pensjonskasse). Deles mellom
 * [no.nav.pensjon.brev.alder.maler.afp.VedtakAfpEtteroppgjoerTilbakekrevingAuto]
 * (PE_AF_04_107) og PE_AF_04_104.
 *
 * «Ny pensjonsberegning» og «AFP som er betalt ut for mye» er i originalen
 * formatert som ligninger (A − B = C). Med kun to kolonner (etikett + beløp)
 * ble en ekte `table` med tom header for smal og lite leselig; gjengis derfor
 * som tre linjer i én paragraf (newline mellom linjene).
 */
object AfpTilbakekrevingBody {

    /**
     * Paragraf som forklarer at avviket er større enn toleransebeløpet, og
     * at pensjonen er beregnet på ny og avregnet mot tidligere utbetalt
     * pensjon. Identisk i PE_AF_04_104 og PE_AF_04_107.
     */
    data class ToleransebeloepOverskrider(
        val avvik: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
        val toleranseBeloep: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Den arbeidsinntekten du har hatt i perioden med AFP er " + avvik.format() +
                                " høyere enn den forventede arbeidsinntekten som ble lagt til grunn ved " +
                                "utbetalingen av pensjonen din i det aktuelle tidsrommet. Fordi dette er mer " +
                                "enn toleransebeløpet som i " + oppgjoersAar.format() + " var " + toleranseBeloep.format() + ", " +
                                "er pensjonen din beregnet på nytt og avregnet mot den pensjonen du allerede " +
                                "har fått utbetalt i perioden."
                    },
                    nynorsk {
                        +"Den arbeidsinntekta du har hatt i perioden med AFP, er " + avvik.format() +
                                " høgare enn den forventa arbeidsinntekta som blei lagd til grunn ved " +
                                "utbetalinga av pensjonen din i det aktuelle tidsrommet. Fordi dette er meir " +
                                "enn toleransebeløpet som i " + oppgjoersAar.format() + " var " + toleranseBeloep.format() + ", " +
                                "er pensjonen din berekna på nytt og avrekna mot den pensjonen du allereie " +
                                "har fått utbetalt i perioden."
                    },
                )
            }
        }
    }

    /**
     * Title1 «Ny pensjonsberegning» + likning (full AFP − fradrag = AFP etter
     * fradrag for ny inntekt) gjengitt som tre linjer i én paragraf.
     */
    data class NyPensjonsberegningEquation(
        val fullAfp: Expression<Kroner>,
        val fradragBeregnetArbeidsInntekt: Expression<Kroner>,
        val korrigertAfp: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Ny pensjonsberegning" },
                    nynorsk { +"Ny pensjonsberekning" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Full AFP (uten fradrag for inntekt): " + fullAfp.format(denominator = false) + " kr" },
                    nynorsk { +"Full AFP (utan frådrag for inntekt): " + fullAfp.format(denominator = false) + " kr" },
                )
                newline()
                text(
                    bokmal { +"− Nytt beregnet inntektsfradrag: " + fradragBeregnetArbeidsInntekt.format(denominator = false) + " kr" },
                    nynorsk { +"− Nytt berekna inntektsfrådrag: " + fradragBeregnetArbeidsInntekt.format(denominator = false) + " kr" },
                )
                newline()
                text(
                    bokmal { +"= AFP etter fradrag for den nye inntekten: " + korrigertAfp.format(denominator = false) + " kr" },
                    nynorsk { +"= AFP etter frådrag for den nye inntekta: " + korrigertAfp.format(denominator = false) + " kr" },
                )
            }
        }
    }

    /**
     * Formelforklaring for inntektsfradraget: ny inntekt / tidligere
     * arbeidsinntekt × full AFP. Brukes rett etter [NyPensjonsberegningEquation].
     */
    data class InntektsfradragetFormel(
        val fradragBeregnetArbeidsInntekt: Expression<Kroner>,
        val inntektIAfpPerioden: Expression<Kroner>,
        val tidligereArbeidsInntektBeregnet: Expression<Kroner>,
        val fullAfp: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Inntektsfradraget i AFP for den nye inntekten på " + fradragBeregnetArbeidsInntekt.format(denominator = false) +
                                " kr beregnes slik: " + inntektIAfpPerioden.format(denominator = false) + " kr (ny beregnet inntekt) / " +
                                tidligereArbeidsInntektBeregnet.format(denominator = false) + " kr (tidligere arbeidsinntekt*) x " +
                                fullAfp.format(denominator = false) + " kr (full AFP)."
                    },
                    nynorsk {
                        +"Inntektsfrådraget i AFP for den nye inntekta på " + fradragBeregnetArbeidsInntekt.format(denominator = false) +
                                " kr blir berekna slik: " + inntektIAfpPerioden.format(denominator = false) + " kr (ny berekna inntekt) / " +
                                tidligereArbeidsInntektBeregnet.format(denominator = false) + " kr (tidlegare arbeidsinntekt*) x " +
                                fullAfp.format(denominator = false) + " kr (full AFP)."
                    },
                )
                newline()
                text(
                    bokmal { +"*Tidligere arbeidsinntekt er beregnet ut fra inntekten din i årene før du tok ut AFP." },
                    nynorsk { +"*Tidlegare arbeidsinntekt er berekna ut frå inntekta di i åra før du tok ut AFP." },
                )
            }
        }
    }

    /**
     * Title1 «AFP som er betalt ut for mye» + forklarende paragraf + likning
     * (tidligere utbetalt − korrigert AFP = for mye utbetalt) som tre linjer
     * i én paragraf.
     */
    data class AfpForMyeEquation(
        val utbetaltAfp: Expression<Kroner>,
        val korrigertAfp: Expression<Kroner>,
        val formyebetalt: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"AFP som er betalt ut for mye" },
                    nynorsk { +"AFP som er betalt ut for mykje" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Dette beløpet er differansen mellom tidligere utbetalt AFP og AFP etter fradrag " +
                                "for den nye inntekten."
                    },
                    nynorsk {
                        +"Dette beløpet er differansen mellom tidlegare utbetalt AFP og AFP etter frådrag " +
                                "for den nye inntekta."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Tidligere utbetalt AFP: " + utbetaltAfp.format(denominator = false) + " kr" },
                    nynorsk { +"Tidlegare utbetalt AFP: " + utbetaltAfp.format(denominator = false) + " kr" },
                )
                newline()
                text(
                    bokmal { +"− AFP fratrukket nytt beregnet inntektsfradrag: " + korrigertAfp.format(denominator = false) + " kr" },
                    nynorsk { +"− AFP fråtrekt nytt berekna inntektsfrådrag: " + korrigertAfp.format(denominator = false) + " kr" },
                )
                newline()
                text(
                    bokmal { +"= For mye utbetalt AFP: " + formyebetalt.format(denominator = false) + " kr" },
                    nynorsk { +"= For mykje utbetalt AFP: " + formyebetalt.format(denominator = false) + " kr" },
                )
            }
        }
    }

    /** Title1 «Tilbakebetaling» + 3 forklarende paragrafer (ingen parametere). */
    object TilbakebetalingSection : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Tilbakebetaling" },
                    nynorsk { +"Tilbakebetaling" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Nav sender kravet til Skatteetaten for videre oppfølging. Dette skjer først når " +
                                "klagefristen på 6 uker er gått ut og vanligvis innen 12 uker."
                    },
                    nynorsk {
                        +"Nav sender kravet til Skatteetaten for vidare oppfølging. Dette skjer først når " +
                                "klagefristen på 6 veker er gått ut og vanlegvis innan 12 veker."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du vil få et eget brev med betalingsinformasjon. Beløpet skal som hovedregel " +
                                "betales tilbake i løpet av 12 kalendermåneder, og vil trekkes i den månedlige " +
                                "utbetalingen din av AFP eller alderspensjon fra folketrygden."
                    },
                    nynorsk {
                        +"Du vil få eit eige brev frå Skatteetaten med betalingsinformasjon. Beløpet skal " +
                                "som hovudregel betalast tilbake i løpet av 12 kalendermånader, og vil " +
                                "bli trekt i den månadlege utbetalinga di av AFP eller alderspensjon frå " +
                                "folketrygda."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Etter at du har fått brevet fra Skatteetaten, kan du ta kontakt med dem for å " +
                                "endre trekkbeløpet."
                    },
                    nynorsk {
                        +"Etter at du har fått brevet frå Skatteetaten kan du ta kontakt med dei for å endre " +
                                "trekkbeløpet."
                    },
                )
            }
        }
    }

    /** Paragraf som forklarer korrigeringen til Skatteetaten. */
    data class SkatteoppgjorParagraph(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Nav rapporterer endringen til Skatteetaten. De vil foreta en korrigering av " +
                                "skatteoppgjøret ditt for " + oppgjoersAar.format() + " basert på denne " +
                                "endringen av utbetalt AFP."
                    },
                    nynorsk {
                        +"Nav rapporterer endringa til Skatteetaten. Dei vil gjere ei korrigering av " +
                                "skatteoppgjeret ditt for " + oppgjoersAar.format() + " basert på denne " +
                                "endringa."
                    },
                )
            }
        }
    }
}
