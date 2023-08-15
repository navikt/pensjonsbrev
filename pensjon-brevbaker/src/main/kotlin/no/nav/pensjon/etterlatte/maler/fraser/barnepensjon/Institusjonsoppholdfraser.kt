package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object Institusjonsoppholdfraser {
    object Innvilgelse : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Innlagt på institusjon – ingen reduksjon pga dokumenterte utgifter",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du er innlagt i helseinstitusjon og barnepensjonen skal etter hovedregelen reduseres som følge av dette. Du har dokumentert nødvendige utgifter til bolig og barnepensjonen din vil derfor ikke være redusert når du er innlagt i institusjon.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class Lover(val erEtterbetaling: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(erEtterbetaling) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5, 18-8 og § 22-12.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5, 18-8 § 22-12 og § 22-13.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }

    object LoverEndring : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § 18-8 og § 22-12.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class HarDokumentertUtgiftBarnepensjonBlirRedusertMedMindreEnn90Prosent(
        val virkningsdato: Expression<LocalDate>,
        val prosent: Expression<Int>,
        val kronebeloep: Expression<Kroner>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertVirkningsdato = virkningsdato.format()
            title1 {
                text(
                    Bokmal to "Innlagt – har dokumenterte utgift – barnepensjon blir redusert med mindre enn 90% ",
                    Nynorsk to "",
                    English to "",
                )
            }
            val prosent = prosent.format()
            paragraph {
                textExpr(
                    Bokmal to "Barnepensjonen din endres fra ".expr() + formatertVirkningsdato + " fordi du er blitt innlagt i helseinstitusjon. " +
                        "Du har dokumentert nødvendige utgifter til bolig og barnepensjonen din vil bli redusert til " + prosent + " av grunnbeløpet (G). Du får " + kronebeloep.format() + " kroner hver måned.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            includePhrase(LoverEndring)
            includePhrase(Barnepensjon.SlikHarViBeregnetPensjonenDinTittel)
            paragraph {
                text(
                    Bokmal to "Når du blir innlagt i institusjon skal barnepensjonen som hovedregel utbetales med 10 prosent av grunnbeløpet (G). Siden du har dokumentert nødvendige utgifter til bolig vil pensjonen din utbetales med " + prosent + " av grunnbeløpet.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class InnlagtVanligSats(
        val virkningsdato: Expression<LocalDate>,
        val kronebeloep: Expression<Kroner>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertVirkningsdato = virkningsdato.format()
            title1 {
                text(
                    Bokmal to "Innlagt – vanlig sats",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Barnepensjonen din er redusert fra ".expr() + formatertVirkningsdato + " fordi du er blitt innlagt i helseinstitusjon. " +
                        "Du får " + kronebeloep.format() + " kroner hver måned.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi har vurdert mottatt dokumentasjon på utgifter til bolig. [fritekst med begrunnelse på hvorfor dokumentasjon ikke er hensyntatt].",
                    Nynorsk to "",
                    English to "",
                )
            }

            includePhrase(LoverEndring)
            includePhrase(Barnepensjon.SlikHarViBeregnetPensjonenDinTittel)
            paragraph {
                text(
                    Bokmal to "Når du blir innlagt i institusjon reduseres barnepensjonen din fra den fjerde måneden etter innleggelse. Barnepensjonen utgjør 10 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class InnlagtPaaNyttInnen3Maaneder(
        val innlagtdato: Expression<LocalDate>,
        val virkningsdato: Expression<LocalDate>,
        val kronebeloep: Expression<Kroner>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertInnlagtdato = innlagtdato.format()
            val formatertVirkningsdato = virkningsdato.format()
            title1 {
                text(
                    Bokmal to "Innlagt – innlagt på nytt innen 3 mnd",
                    Nynorsk to "",
                    English to "",
                )
            }
            title2 {
                text(
                    Bokmal to "Alternativ 1 (behandles som et nytt beh.opp (4. mnd)",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du er blitt innlagt i helseinstitusjon på nytt fra ".expr() + formatertInnlagtdato + " og barnepensjonen skal etter hovedregelen reduseres fra " +
                        formatertVirkningsdato + ". Du får " + kronebeloep.format() + " kroner hver måned.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjonen reduseres fra og med fjerde måned etter innleggelsesmåneden når forrige institusjonsopphold ikke førte til reduksjon i barnepensjonen. Barnepensjonen utgjør 10 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Alternativ 2 (behandles som fortsettelse av innleggelse (fra mnd etter ny innleggelse)",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du har på nytt blitt innlagt i helseinstitusjon fra ".expr() + formatertInnlagtdato + " og barnepensjonen skal etter hovedregel reduseres fra " +
                        formatertVirkningsdato + ". Du får " + kronebeloep.format() + " kroner hver måned.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjonen reduseres fra og med måneden etter ny innleggelsesdato fordi du er innlagt innen tre måneder etter forrige opphold i helseinstitusjon som ga reduksjon i barnepensjonen. Barnepensjonen utgjør 10 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året.",
                    Nynorsk to "",
                    English to "",
                )
            }

            includePhrase(LoverEndring)

            includePhrase(Barnepensjon.SlikHarViBeregnetPensjonenDinTittel)
            title2 {
                text(
                    Bokmal to "Alternativ 1",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Når du blir innlagt i institusjon reduseres barnepensjonen din fra den fjerde måneden etter innleggelse. Barnepensjonen utgjør 10 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året.",
                    Nynorsk to "",
                    English to "",
                )
            }
            title2 {
                text(
                    Bokmal to "Alternativ 2",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Når du blir innlagt i institusjon på nytt reduseres barnepensjonen din fra måneden etter ny innleggelse. Barnepensjonen utgjør 10 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class HarDokumentertUtgiftIngenReduksjonVanligUtbetaling(
        val innlagtdato: Expression<LocalDate>,
        val virkningsdato: Expression<LocalDate>,
        val kronebeloep: Expression<Kroner>,
        val antallBarnSomOppdrasSammen: Expression<Int>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertInnlagtdato = innlagtdato.format()
            val formatertVirkningsdato = virkningsdato.format()
            title1 {
                text(
                    Bokmal to "Innlagt – har dokumenterte utgift - ingen reduksjon (vanlig utbetaling)",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du er blitt innlagt i helseinstitusjon  fra ".expr() + formatertInnlagtdato + " og barnepensjonen skal etter hovedregelen reduseres fra " +
                        formatertVirkningsdato + ". Du har dokumentert nødvendige utgifter til bolig og barnepensjonen din vil derfor ikke reduseres på grunn av oppholdet. Du får fortsatt " + kronebeloep.format() + " kroner hver måned.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            includePhrase(LoverEndring)
            includePhrase(Barnepensjon.SlikHarViBeregnetPensjonenDinTittel)
            showIf(antallBarnSomOppdrasSammen.equalTo(1)) {
                paragraph {
                    text(
                        Bokmal to "Barnepensjonen beregnes for ett barn og utgjør 40 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }.orShowIf(antallBarnSomOppdrasSammen.greaterThanOrEqual(2)) {
                paragraph {
                    textExpr(
                        Bokmal to "Det gjøres en samlet beregning av pensjon for barn som oppdras sammen. For denne beregningen har vi lagt til grunn at dere er ".expr() + antallBarnSomOppdrasSammen.toString() + " barn som oppdras sammen.",
                        Nynorsk to "".expr(),
                        English to "".expr(),
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Barnepensjon utgjør 40 prosent av folketrygdens grunnbeløp (G) for det første barnet i søskenflokken. For hvert av de øvrige barna legges det til 25 prosent av G. Summen deles på antall barn, og pensjonen utbetales med likt beløp til hvert av barna. Pensjonen fordeles på 12 utbetalinger i året.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }

    data class UtskrevetVanligSats(
        val virkningsdato: Expression<LocalDate>,
        val kronebeloep: Expression<Kroner>,
        val antallBarnSomOppdrasSammen: Expression<Int>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertVirkningsdato = virkningsdato.format()
            title1 {
                text(
                    Bokmal to "Utskrevet - vanlig sats",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Barnepensjonen din øker fra ".expr() + formatertVirkningsdato + " fordi du er kommet hjem fra helseinstitusjon. Barnepensjonen øker fra og med utskrivingsmåneden. Du får " + kronebeloep.format() + " kroner hver måned.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            includePhrase(LoverEndring)
            includePhrase(Barnepensjon.SlikHarViBeregnetPensjonenDinTittel)
            showIf(antallBarnSomOppdrasSammen.equalTo(1)) {
                paragraph {
                    text(
                        Bokmal to "Barnepensjonen beregnes for ett barn og utgjør 40 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }.orShowIf(antallBarnSomOppdrasSammen.greaterThanOrEqual(2)) {
                paragraph {
                    text(
                        Bokmal to "Det gjøres en samlet beregning av pensjon for barn som oppdras sammen. For denne beregningen har vi lagt til grunn at dere er $antallBarnSomOppdrasSammen barn som oppdras sammen.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Barnepensjon utgjør 40 prosent av folketrygdens grunnbeløp (G) for det første barnet i søskenflokken. For hvert av de øvrige barna legges det til 25 prosent av G. Summen deles på antall barn, og pensjonen utbetales med likt beløp til hvert av barna. Pensjonen fordeles på 12 utbetalinger i året.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }

    data class UtskrevetHarDokumenterteUtgiftIngenReduksjonHarVaertVanligUtbetaling(
        val utskrevetdato: Expression<LocalDate>,
        val virkningsdato: Expression<LocalDate>,
        val kronebeloep: Expression<Kroner>,
        val antallBarnSomOppdrasSammen: Expression<Int>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertUtskrevetdato = utskrevetdato.format()
            val formatertVirkningsdato = virkningsdato.format()
            title1 {
                text(
                    Bokmal to "Utskrevet – har dokumenterte utgift - ingen reduksjon (har vært vanlig utbetaling)",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du er kommet hjem fra opphold i helseinstitusjon ".expr() + formatertUtskrevetdato + " og barnepensjonen skal etter hovedregelen økes fra " +
                        formatertVirkningsdato + ". Du dokumenterte nødvendige utgifter til bolig under institusjonsoppholdet og barnepensjonen har ikke vært redusert i perioden. Barnepensjonen endres dermed ikke når du er kommet hjem. Du får fortsatt " + kronebeloep.format() + " kroner hver måned.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            includePhrase(LoverEndring)
            includePhrase(Barnepensjon.SlikHarViBeregnetPensjonenDinTittel)
            showIf(antallBarnSomOppdrasSammen.equalTo(1)) {
                paragraph {
                    text(
                        Bokmal to "Barnepensjonen beregnes for ett barn og utgjør 40 prosent av folketrygdens grunnbeløp (G) og fordeles på 12 utbetalinger i året.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }.orShowIf(antallBarnSomOppdrasSammen.greaterThanOrEqual(2)) {
                paragraph {
                    text(
                        Bokmal to "Det gjøres en samlet beregning av pensjon for barn som oppdras sammen. For denne beregningen har vi lagt til grunn at dere er $antallBarnSomOppdrasSammen barn som oppdras sammen.",
                        Nynorsk to "",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Barnepensjon utgjør 40 prosent av folketrygdens grunnbeløp (G) for det første barnet i søskenflokken. For hvert av de øvrige barna legges det til 25 prosent av G. Summen deles på antall barn, og pensjonen utbetales med likt beløp til hvert av barna. Pensjonen fordeles på 12 utbetalinger i året.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }
}
