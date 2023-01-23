package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Year
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate


object UfoeretrygdEndretPgaInntekt {
    data class Tittel(
        val harEndretUfoeretrygd: Expression<Boolean>,
        val harEndretBarnetillegg: Expression<Boolean>,
        val harFlereBarnetillegg: Expression<Boolean>,
    ) : TextOnlyPhrase<LangBokmalNynorsk>() {

        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(harEndretUfoeretrygd and not(harEndretBarnetillegg)) {
                text(
                    Bokmal to "NAV har endret utbetalingen av uføretrygden din",
                    Nynorsk to "NAV har endra utbetalinga av uføretrygda di"
                )
            }

            showIf(harEndretUfoeretrygd and harEndretBarnetillegg) {
                textExpr(
                    Bokmal to "NAV har endret utbetalingen av uføretrygden og ".expr()
                            + ifElse(harFlereBarnetillegg, "barnetilleggene dine", "barnetillegget ditt"),
                    Nynorsk to "NAV har endra utbetalinga av uføretrygda di og ".expr()
                            + ifElse(harFlereBarnetillegg, "barnetillegga di", "barnetillegget ditt"),
                )
            }

            showIf(not(harEndretUfoeretrygd) and harEndretBarnetillegg) {
                textExpr(
                    Bokmal to "NAV har endret utbetalingen av ".expr() +
                            ifElse(harFlereBarnetillegg,"barnetilleggene","barnetillegget")
                            + " i uføretrygden din".expr(),
                    Nynorsk to "NAV har endra utbetalinga av ".expr() +
                            ifElse(harFlereBarnetillegg,"barnetillegga","barnetillegget")
                            + " i uføretrygda di".expr()
                )
            }
        }
    }

    // TBU2249
    data class InnledningInntektsendring(
        val harFlereFellesbarn: Expression<Boolean>,
        val harEndretBarnetillegg: Expression<Boolean>,
        val harEndretUfoeretrygd: Expression<Boolean>,
        val harBarnetilleggForSaerkullOgFellesbarn: Expression<Boolean>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val brukersSivilstand: Expression<Sivilstand>,
        val virkningsdatoFraOgMed: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vi har mottatt nye opplysninger om inntekten ",
                    Nynorsk to "Vi har fått nye opplysningar om inntekta ",
                )

                showIf(not(harBarnetilleggFellesbarn)) {
                    text(
                        Bokmal to "din",
                        Nynorsk to "di",
                    )
                }.orShow {
                    text(
                        Bokmal to "til deg eller din ",
                        Nynorsk to "til deg eller ",
                    )

                    includePhrase(Felles.SivilstandEPSBestemtForm(brukersSivilstand))

                    text(
                        Bokmal to ". Inntekten til din ",
                        Nynorsk to " din. Inntekta til ",
                    )

                    includePhrase(Felles.SivilstandEPSBestemtForm(brukersSivilstand))

                    text(
                        Bokmal to " har kun betydning for størrelsen på barnetillegget ",
                        Nynorsk to " din har berre betydning for storleiken på barnetillegget ",

                        )
                    showIf(harBarnetilleggForSaerkullOgFellesbarn) {
                        textExpr(
                            Bokmal to "for ".expr() + ifElse(
                                harFlereFellesbarn,
                                "barna",
                                "barnet"
                            ) + " som bor med begge sine foreldre",
                            Nynorsk to "for ".expr() + ifElse(
                                harFlereFellesbarn,
                                "barna",
                                "barnet"
                            ) + " som bur saman med begge foreldra sine",
                        )
                    }.orShow {
                        text(
                            Bokmal to "ditt",
                            Nynorsk to "ditt",
                        )
                    }
                }

                text(
                    Bokmal to ". Utbetalingen av ",
                    Nynorsk to ". Utbetalinga av ",
                )

                showIf(harEndretUfoeretrygd) {
                    text(
                        Bokmal to "uføretrygden din " + ifElse(harEndretBarnetillegg, "og ", ""),
                        Nynorsk to "uføretrygda di " + ifElse(harEndretBarnetillegg, "og ", ""),
                    )
                }

                showIf(harEndretBarnetillegg) {
                    showIf(harBarnetilleggForSaerkullOgFellesbarn) {
                        text(
                            Bokmal to "barnetilleggene dine",
                            Nynorsk to "barnetillegga dine",
                        )
                    }.orShow {
                        text(
                            Bokmal to "barnetillegget ditt",
                            Nynorsk to "barnetillegget ditt",
                        )
                    }
                }
                textExpr(
                    Bokmal to " er derfor endret fra ".expr() + virkningsdatoFraOgMed.format() + ".",
                    Nynorsk to " er derfor endra frå ".expr() + virkningsdatoFraOgMed.format() + ".",
                )
            }
        }
    }

    // TBU3403
    data class InnledningReduksjonUfoeretrygd(
        val forventetInntektUfoeretrygd: Expression<Kroner>,
        val harEndretUfoeretrygd: Expression<Boolean>,
        val virkningsaar: Expression<Int>,
        val aarFoerVirkningsAar: Expression<Year>,
        val ufoeregrad: Expression<Double>,
        val utbetalingsgrad: Expression<Double>,
        val fyller67IVirkningsAar: Expression<Boolean>
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(harEndretUfoeretrygd) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + forventetInntektUfoeretrygd.format() + " kroner når vi reduserer uføretrygden din for ".expr() + virkningsaar.format() + ". Har du ikke meldt inn ny inntekt for ".expr() + virkningsaar.format() + ", er inntekten justert opp til dagens verdi.",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + forventetInntektUfoeretrygd.format() + " kroner når vi reduserer uføretrygda di for ".expr() + virkningsaar.format() + ". Har du ikkje meldt inn ny inntekt for ".expr() + virkningsaar.format() + ", er inntekta justert opp til dagens verdi.",
                    )
                }

                showIf(utbetalingsgrad.notEqualTo(ufoeregrad)){
                    paragraph {
                        textExpr(
                            Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + aarFoerVirkningsAar.format() + ", er inntekten justert opp slik at den gjelder for hele ".expr() + virkningsaar.format() + ".",
                            Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + aarFoerVirkningsAar.format() + ", er inntekta også justert opp slik at den gjeld for heile ".expr() + virkningsaar.format() + ".",
                        )
                    }
                }
                showIf(fyller67IVirkningsAar) {
                    paragraph {
                        textExpr(
                            Bokmal to "Fordi du fyller 67 år i ".expr() + virkningsaar.format() + ", er inntekten justert i forhold til antall måneder du mottar uføretrygd.",
                            Nynorsk to "Fordi du fyljer 67 år i ".expr() + virkningsaar.format() + ", er inntekta justert ut frå talet på månadar du får uføretrygd.",
                        )
                    }
                }
            }
        }
    }

    /**TBU4016, TBU4001, TBU4002, TBU4017*/
    data class InnledningReduksjonBarnetillegg(
        val harEndretUfoeretrygd: Expression<Boolean>,
        val harEndretBarnetillegg: Expression<Boolean>,
        val harBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val harBarnetilleggFellesbarn: Expression<Boolean>,
        val harFlereFellesbarn: Expression<Boolean>,
        val harFlereSaerkullsbarn: Expression<Boolean>,
        val harEndretBarnetilleggFellesbarn: Expression<Boolean>,
        val harEndretBarnetilleggSaerkullsbarn: Expression<Boolean>,
        val inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn: Expression<Kroner>,
        val inntektBruktIAvkortningAvBarnetilleggForFellesbarn: Expression<Kroner>,
        val virkningsaar: Expression<Int>,
    ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            /**TBU4016*/
            showIf(
                harEndretUfoeretrygd
                        and harEndretBarnetillegg
                        and harBarnetilleggSaerkullsbarn
                        and not(harBarnetilleggFellesbarn)
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr()
                                + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn.format() + " kroner.",
                        Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr()
                                + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn.format() + " kroner.",
                    )
                }
            }


            /**TBU4001*/
            showIf(
                harEndretBarnetillegg
                        and harEndretUfoeretrygd
                        and harBarnetilleggFellesbarn
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr()
                                + inntektBruktIAvkortningAvBarnetilleggForFellesbarn.format() + " kroner",
                        Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr()
                                + inntektBruktIAvkortningAvBarnetilleggForFellesbarn.format() + " kroner",
                    )

                    showIf(harBarnetilleggSaerkullsbarn) {
                        textExpr(
                            Bokmal to " for ".expr()
                                    + ifElse(harFlereFellesbarn, "barna", "barnet")
                                    + " som bor med begge sine foreldre. For ".expr()
                                    + ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                    + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på ".expr()
                                    + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn.format() + " kroner",
                            Nynorsk to "for ".expr()
                                    + ifElse(harFlereFellesbarn, "barna", "barnet")
                                    + " som bur med begge sine foreldra. For ".expr()
                                    + ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                    + " som ikkje bur saman med begge foreldrea vil vi bruke ei inntekt på ".expr()
                                    + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn.format() + " kroner",
                        )
                    }
                    text(
                        Bokmal to ".",
                        Nynorsk to ".",
                    )
                }
            }

            /**TBU4002*/
            showIf(not(harEndretUfoeretrygd) and harEndretBarnetilleggFellesbarn) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi vil bruke en inntekt på ".expr()
                                + inntektBruktIAvkortningAvBarnetilleggForFellesbarn.format()
                                + " kroner når vi reduserer barnetillegget ditt",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr()
                                + inntektBruktIAvkortningAvBarnetilleggForFellesbarn.format()
                                + " kroner når vi reduserer barnetillegget ditt",
                    )

                    showIf(harBarnetilleggSaerkullsbarn) {
                        textExpr(
                            Bokmal to " for ".expr()
                                    + ifElse(harFlereFellesbarn, "barna", "barnet")
                                    + " som bor med begge sine foreldre for ".expr() + virkningsaar.format()
                                    + ". For ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                    + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på ".expr()
                                    + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn.format()
                                    + " kroner",
                            Nynorsk to " for ".expr()
                                    + ifElse(harFlereFellesbarn, "barna", "barnet")
                                    + " som bur saman med begge foreldra sine for ".expr() + virkningsaar.format()
                                    + ". For ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                    + " som ikkje bur saman med begge foreldra vil vi bruke ei inntekt på ".expr()
                                    + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn.format()
                                    + " kroner",
                        )
                    }

                    textExpr(
                        Bokmal to ". Har du ikke meldt inn nye inntekter for ".expr()
                                + virkningsaar.format() + ", er inntektene justert opp til dagens verdi.",
                        Nynorsk to ". Har du ikkje meldt inn ei nye inntekter for ".expr()
                                + virkningsaar.format() + ", er inntektainntektene justert opp til dagens verdi.",
                    )
                }
            }

            /**TBU4017*/
            showIf(not(harEndretUfoeretrygd)
                    and not(harEndretBarnetilleggFellesbarn)
                    and harEndretBarnetilleggSaerkullsbarn)
            {
                paragraph {
                    textExpr(
                        Bokmal to "Vi vil bruke en inntekt på ".expr()
                                + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn.format()
                                + " kroner når vi reduserer barnetillegget ditt. Har du ikke meldt inn ny inntekt for ".expr()
                                + virkningsaar.format() + ", er inntekten justert opp til dagens verdi.",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr()
                                + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn.format()
                                + " kroner når vi reduserer barnetillegget ditt. Har du ikkje meldt inn ny inntekt for ".expr()
                                + virkningsaar.format() + ", er inntekta justert opp til dagens verdi.",
                    )
                }
            }
        }
    }


    /**TBU4013*/
    data class InnledningInntektsjusteringUfoeretrygd(
        val virkningsaar: Expression<Int>,
        val aarFoerVirkningsAar: Expression<Int>,
    ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr()
                            + aarFoerVirkningsAar.format()
                            + ", er inntekten justert opp slik at den gjelder for hele ".expr()
                            + virkningsaar.format() + ".",
                    Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr()
                            + aarFoerVirkningsAar.format()
                            + ", er inntekta justert opp slik at den gjeld for heile ".expr()
                            + virkningsaar.format() + ".",
                )
            }
        }
    }

    /**TBU4004*/
    data class MeldFraOmInntektsEndringerUfoer(
        val barnetilleggFellesbarn_harBarnetillegg : Expression<Boolean>,
        val virkningsaar: Expression<Int>,
        val brukersSivilstand: Expression<Sivilstand>,
    ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(Bokmal to "Forventer du",Nynorsk to "Forventar du",)

                showIf(barnetilleggFellesbarn_harBarnetillegg){
                    text(Bokmal to " og din ",Nynorsk to " og ")
                    includePhrase(Felles.SivilstandEPSBestemtForm(brukersSivilstand))
                    text(Bokmal to "",Nynorsk to " din",)
                }

                textExpr(
                    Bokmal to " å tjene noe annet i ".expr() + virkningsaar.format() + " er det viktig at du melder inn ny forventet inntekt. Dette kan du gjøre på nav.no.",
                    Nynorsk to " å tene noko anna i ".expr() + virkningsaar.format() + " er det viktig at du melder inn ei ny forventa inntekt. Dette kan du gjere på nav.no.",
                )
            }
        }

    }

    /**TBU4003*/
     data class BarnetilleggInntektEllerFribeloepPeriodisert(
         val barnetilleggFellesbarn_fribeloepErPeriodisert: Expression<Boolean>,
         val barnetilleggFellesbarn_harBarnetillegg: Expression<Boolean>,
         val barnetilleggFellesbarn_inntektErPeriodisert: Expression<Boolean>,
         val barnetilleggSaerkullsbarn_fribeloepErPeriodisert: Expression<Boolean>,
         val barnetilleggSaerkullsbarn_harBarnetillegg: Expression<Boolean>,
         val barnetilleggSaerkullsbarn_inntektErPeriodisert: Expression<Boolean>,
         val harFlereFellesbarn: Expression<Boolean>,
         val harFlereSaerkullsbarn: Expression<Boolean>,
     ): OutlinePhrase<LangBokmalNynorsk>(){
         override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
             paragraph{
                 text(
                     Bokmal to "Fordi du ikke har barnetillegg ",
                     Nynorsk to "Fordi du ikkje har barnetillegg ",
                 )

                 showIf(barnetilleggSaerkullsbarn_harBarnetillegg and barnetilleggFellesbarn_harBarnetillegg) {

                     showIf(
                         (barnetilleggFellesbarn_inntektErPeriodisert and not(barnetilleggSaerkullsbarn_inntektErPeriodisert))
                                 or (barnetilleggFellesbarn_fribeloepErPeriodisert and not(barnetilleggSaerkullsbarn_fribeloepErPeriodisert))
                     ) {
                         textExpr(
                             Bokmal to "for ".expr() +
                                     ifElse(
                                         harFlereFellesbarn,
                                         "barna",
                                         "barnet"
                                     ) + " som bor med begge sine foreldre ",
                             Nynorsk to "for ".expr() +
                                     ifElse(
                                         harFlereFellesbarn,
                                         "barna",
                                         "barnet"
                                     ) + " som bur saman med begge foreldra ",
                         )
                     }

                     showIf(
                         (not(barnetilleggFellesbarn_inntektErPeriodisert) and barnetilleggSaerkullsbarn_inntektErPeriodisert)
                                 or (not(barnetilleggFellesbarn_fribeloepErPeriodisert) and barnetilleggSaerkullsbarn_fribeloepErPeriodisert)
                     ) {
                         textExpr(
                             Bokmal to "for ".expr()
                                     + ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                     + " som ikke bor sammen med begge foreldrene ",
                             Nynorsk to "for ".expr()
                                     + ifElse(harFlereSaerkullsbarn, "barna", "barnet")
                                     + " som ikkje bur saman med begge foreldra ",
                         )
                     }
                 }

                 val fribeloepErPeriodisert =
                     barnetilleggFellesbarn_fribeloepErPeriodisert or barnetilleggSaerkullsbarn_fribeloepErPeriodisert
                 val inntektErPeriodisert =
                     barnetilleggFellesbarn_inntektErPeriodisert or barnetilleggSaerkullsbarn_inntektErPeriodisert

                 text(Bokmal to "hele året er ", Nynorsk to "heile året er ")

                 showIf(inntektErPeriodisert) {
                     text(Bokmal to "inntektene ", Nynorsk to "inntektene ")
                     showIf(fribeloepErPeriodisert) {
                         text(Bokmal to "og ", Nynorsk to "og ")
                     }
                 }

                 showIf(fribeloepErPeriodisert) {
                     text(Bokmal to "fribeløpet ", Nynorsk to "fribeløpet ")
                 }

                 textExpr(
                     Bokmal to "justert slik at ".expr()
                             + ifElse(
                         fribeloepErPeriodisert and not(inntektErPeriodisert),
                         "det",
                         "de"
                     ) + " kun gjelder for den perioden du mottar barnetillegg.",
                     Nynorsk to "justert slik at ".expr()
                             + ifElse(
                         fribeloepErPeriodisert and not(inntektErPeriodisert),
                         "det",
                         "dei"
                     ) + " berre gjelde for den perioden du får barnetillegg.",
                 )
             }
         }
     }


    /**TBU2253*/
    data class EndringUfoeretrygdBegrunnelseInntektstak(
        val inntektsgrenseUfoeretrygd: Expression<Kroner>,
        val inntektstakUfoeretrygd: Expression<Kroner>,
        val nyttUfoeretrygdBeloep: Expression<Kroner>,
        val forventetInntektUfoeretrygd: Expression<Kroner>,
        val gammeltUfoeretrygdBeloep: Expression<Kroner>,
        val utbetalingsgrad: Expression<Double>,
        val ufoeregrad: Expression<Double>,
    ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            val faarUtbetaltUfoeretrygd = nyttUfoeretrygdBeloep.greaterThan(0)
            showIf(inntektsgrenseUfoeretrygd.lessThan(inntektstakUfoeretrygd)) {
                paragraph {
                    text(
                        Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden. Det er bare den delen av inntekten din som overstiger inntektsgrensen som vil gi en reduksjon av uføretrygden.",
                        Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda. Det er berre den delen av inntekta di som kjem over inntektsgrensa som gir ein reduksjon av uføretrygda di.",
                    )
                }

                paragraph {
                    showIf(faarUtbetaltUfoeretrygd) {
                        showIf(
                            forventetInntektUfoeretrygd.greaterThan(inntektsgrenseUfoeretrygd)
                                    and gammeltUfoeretrygdBeloep.greaterThan(nyttUfoeretrygdBeloep)
                        ) {
                            text(
                                Bokmal to "Uføretrygden din reduseres fordi du tjener over inntektsgrensen din. Selv om du får en reduksjon lønner det seg likevel å jobbe ved siden av uføretrygden.",
                                Nynorsk to "Uføretrygda di blir redusert fordi du tener over inntektsgrensa di. Sjølv om du får ein reduksjon, lønner det seg likevel å jobbe ved sida av uføretrygda.",
                            )
                        }.orShowIf(forventetInntektUfoeretrygd.lessThan(inntektsgrenseUfoeretrygd)) {
                            showIf(
                                utbetalingsgrad.equalTo(ufoeregrad)
                                        and gammeltUfoeretrygdBeloep.lessThan(nyttUfoeretrygdBeloep)
                            ) {
                                text(
                                    Bokmal to "Endringen i inntekten din gjør at uføretrygden ikke lenger er redusert. ",
                                    Nynorsk to "Endringa i inntekta di gjer at uføretrygda ikkje lenger er redusert. "
                                )
                            }
                            text(
                                Bokmal to "Utbetalingen av uføretrygden din økes fordi du tjener under inntektsgrensen din. Det betyr at uføretrygden ikke lenger er redusert.",
                                Nynorsk to "Utbetalinga av uføretrygda aukar fordi du tener under inntektsgrensa di. Det betyr at uføretrygda ikkje lenger er redusert."
                            )
                        }
                    }.orShowIf(forventetInntektUfoeretrygd.lessThanOrEqual(inntektsgrenseUfoeretrygd)){
                        text(
                            Bokmal to "Endring i inntekten din gjør at du ikke får utbetalt uføretrygd for resten av året.",
                            Nynorsk to "Endring i inntekta di gjer at du ikkje får utbetalt uføretrygd for resten av året.",
                        )
                    }
                }
            }
        }
    }

    data class InntektVedSidenAvUfoeretrygd(
        val inntektsgrenseUfoeretrygd: Expression<Kroner>,
        val ufoeretrygdBeloepErRedusert: Expression<Boolean>,
        val forventetInntektUfoeretrygd: Expression<Kroner>,
    ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektsgrenseUfoeretrygd.lessThan(inntektsgrenseUfoeretrygd)) {
                paragraph {
                    text(
                        Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden.",
                        Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda.",
                    )

                    showIf(
                        ufoeretrygdBeloepErRedusert
                                and forventetInntektUfoeretrygd.greaterThan(inntektsgrenseUfoeretrygd)
                    ) {
                        text(
                            Bokmal to " Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
                            Nynorsk to " Det lønner seg likevel å jobbe fordi inntekt og uføretrygd vil alltid vere høgare enn uføretrygd åleine.",
                        )
                    }
                }
            }
        }
    }

    /**TBU4005*/
    data class UfoeretrygdTrekkPgaInntekt(
        val forventetInntektUfoeretrygd: Expression<Kroner>,
        val inntektsgrenseUfoeretrygd: Expression<Kroner>,
        val nyttUfoeretrygdBeloep: Expression<Kroner>,
        val ufoeretrygdAvkortningsbeloepPerAar: Expression<Kroner>,
        val virkningsdatoErFoersteJanuar: Expression<Boolean>,

        ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(forventetInntektUfoeretrygd.greaterThan(inntektsgrenseUfoeretrygd) and nyttUfoeretrygdBeloep.greaterThan(0)){
                paragraph {
                    textExpr(
                        Bokmal to "Siden du har en inntekt på ".expr() + forventetInntektUfoeretrygd.format()
                                + " kroner trekker vi ".expr() + ufoeretrygdAvkortningsbeloepPerAar.format()
                                + " kroner fra uføretrygden "
                                + ifElse(virkningsdatoErFoersteJanuar, "for neste år.","i år."),
                        Nynorsk to "Fordi du har ei inntekt på ".expr() + forventetInntektUfoeretrygd.format()
                                + " kroner trekkjer vi ".expr() + ufoeretrygdAvkortningsbeloepPerAar.format()
                                + " kroner frå uføretrygda "
                                + ifElse(virkningsdatoErFoersteJanuar, "for neste år.","i år."),
                    )
                }
            }
        }
    }

    data class IkkeUtbetaltUfoeretrygdPaaGrunnAvInntekt(
        val forventetInntektUfoeretrygd: Expression<Kroner>,
        val inntektstakUfoeretrygd: Expression<Kroner>,
        val nyttUfoeretrygdBeloep: Expression<Kroner>,
        val inntektsgrenseUfoeretrygd: Expression<Kroner>,
    ): OutlinePhrase<LangBokmalNynorsk>(){
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(forventetInntektUfoeretrygd.greaterThan(inntektstakUfoeretrygd)
                    and inntektsgrenseUfoeretrygd.notEqualTo(inntektstakUfoeretrygd)
                    and nyttUfoeretrygdBeloep.equalTo(0)
            ){
                paragraph {
                    textExpr(
                        Bokmal to "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr()
                                + inntektstakUfoeretrygd.format() + " kroner. Inntekten vi har brukt er ".expr()
                                + forventetInntektUfoeretrygd.format() + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året.",
                        Nynorsk to "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie ".expr()
                                + inntektstakUfoeretrygd.format() + " kroner. Inntekta vi har brukt er ".expr()
                                + forventetInntektUfoeretrygd.format() + " kroner og du vil ikkje få utbetalt uføretrygd resten av året.",
                    )
                }
            }
        }

    }
}