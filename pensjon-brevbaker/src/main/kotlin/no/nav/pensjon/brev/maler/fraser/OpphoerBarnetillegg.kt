package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.time.LocalDate


data class BToensketVirkningsDatoOgFdatoPaaBTopphoert(
    val oensketVirkningsDato: Expression<LocalDate>,
    val foedselsdatoPaaBarnetilleggOpphoert: Expression<LocalDate>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val virkningsDato = oensketVirkningsDato.format()
            val foedselsdato = foedselsdatoPaaBarnetilleggOpphoert.format()
            textExpr(
                Bokmal to "Vi har vedtatt at barnetillegget i uføretrygden din opphører fra ".expr() + virkningsDato + " for barn født ".expr() + foedselsdato + ".".expr(),
                Nynorsk to "Vi har stansa barnetillegget i uføretrygda di frå ".expr() + virkningsDato + " for barn fødd ".expr() + foedselsdato + ".".expr(),
                English to "The child supplement in your disability benefit has been discontinued, effective as of ".expr() + virkningsDato + ", for child born ".expr() + foedselsdato + ".".expr()
            )
        }
    }
}

data class Beloep(
    val perMaaned: Expression<Kroner>,
    val ektefelle: Expression<Boolean>,
    val gjenlevende: Expression<Boolean>,
    val fellesbarn: Expression<Boolean>,
    val saerkullsbarn: Expression<Boolean>,
    val ufoertrygd: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val kroner = perMaaned.format()
            // TBU1120, TBU1121, TBU1122, TBU1123, TBU1253, TBU1254 is found in <maler/fraser/omregning/ufoeretrygd/Ufoeretrygd.kt
            showIf(not(ufoertrygd) and (fellesbarn or saerkullsbarn) and not(ektefelle) and not(gjenlevende)) {
                // TBU4082
                textExpr(
                    Bokmal to "Du får ".expr() + kroner + " kroner i barnetillegg per måned før skatt.".expr(),
                    Nynorsk to "Du får ".expr() + kroner + " kroner i barnetillegg per månad før skatt.".expr(),
                    English to "Your monthly child supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                )

            }.orShowIf(not(ufoertrygd) and (fellesbarn or saerkullsbarn) and ektefelle and not(gjenlevende)) {
                // TBU4083
                textExpr(
                    Bokmal to "Du får ".expr() + kroner + " kroner i barne- og ektefelletillegg per måned før skatt.".expr(),
                    Nynorsk to "Du får ".expr() + kroner + " kroner i barne- og ektefelletillegg per månad før skatt.".expr(),
                    English to "Your monthly child supplement and spouse supplement  payment will be NOK ".expr() + kroner + " before tax.".expr()
                )

            }.orShowIf(not(ufoertrygd) and not(fellesbarn) and not(saerkullsbarn) and ektefelle and not(gjenlevende)) {
                // TBU4084
                textExpr(
                    Bokmal to "Du får ".expr() + kroner + " kroner i ektefelletillegg per måned før skatt.".expr(),
                    Nynorsk to "Du får ".expr() + kroner + " kroner i ektefelletillegg per månad før skatt.".expr(),
                    English to "Your monthly spouse supplement payment will be NOK ".expr() + kroner + " before tax.".expr()
                )

            }
        }
    }
}

object TBU2223 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned.",
                Nynorsk to "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad.",
                English to "Your disability benefit will still be paid no later than the 20th of every month."
            )
        }
    }
}

object TBU1128 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet.",
                Nynorsk to "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet.",
                English to "In this letter we will explain your rights and obligations. Therefore, it is important that you read the whole letter."
            )
        }
    }
}

// TODO use existing TBU1092 in <maler/fraser/vedtak/Vedtak.kt
object TBU3920 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "For å ha rett til barnetillegg må du forsørge barn under 18 år. Vi har vedtatt at barnetillegget i uføretrygden opphører fordi barnetbarna har fylt 18 år.",
                Nynorsk to "For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi barnetbarna har fylt 18 år.",
                English to "To be eligible for child supplement, you must support children under 18 years of age. The child supplement in your disability benefit has been discontinued because your  childchildren has(have) turned 18 years of age."
            )
        }
    }
}

object TBU4085 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12 og forskrift om overgangsreglar for barnetillegg i uføretrygda.",
                English to "The decision has been made pursuant to Section 12-15, 12-16 and 22-12 of the Norwegian National Insurance Act and the transitional provisions for the child supplement in your disability benefit."
            )
        }
    }
}

// TBU1174 in <maler/fraser/omregning/ufoeretrygd/Ufoeretrygd.kt
// TBU4086
data class OensketVirkningsDato(
    val oensketVirkningsDato: Expression<LocalDate>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val dato = oensketVirkningsDato.format()
            textExpr(
                Bokmal to "Barnetillegget ditt har opphørter blitt endret fra ".expr() + dato + ". Dette er måneden etter at barnet barna har fylt 18 år. Dette kaller vi virkningstidspunktet.".expr(),
                Nynorsk to "Barnetillegget ditt er stansaendra frå ".expr() + dato + ". Dette er månaden etter at barnet barna  har fylt 18 år. Dette kallar vi verknadstidspunktet.".expr(),
                English to "Your child supplement has been discontinued changed from ".expr() + dato + ". This is the month after the child children has have turned 18. This is called the effective date.".expr()
            )
        }
    }
}

object TBU3800 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Slik påvirker inntekt barnetillegget ditt",
                Nynorsk to "Slik verkar inntekt inn på barnetillegget ditt",
                English to "Income will affect your child supplement"
            )
        }
    }
}

object TBU2338 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensen kaller vi for fribeløp. Inntekten til ektefellenpartnerensamboeren din har ikke betydning for størrelsen på barnetillegget.",
                Nynorsk to "Inntekta di har noko å seie for kva du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensa kallar vi for fribeløp. Inntekta til ektefellen/partnaren/sambuaren din har ikkje noko å seie for storleiken på barnetillegget.",
                English to "The incomes of you and your spouse/partner/cohabiting partner affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced. We call this limit the exemption amount. The income of your spouse/partner/cohabiting partner only affects the size of the child supplement for the child/children who lives together with both parents."
            )
        }
    }
}

object TBU2339 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Inntekten til deg og ektefellenpartnerensamboeren din har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensen kaller vi for fribeløp. Inntekten til ektefellenpartnerensamboeren din har kun betydning for størrelsen på barnetillegget til barnetbarna som bor sammen med begge sine foreldre.",
                Nynorsk to "Inntekta til deg og ektefellen/partnaren/sambuaren din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert. Denne grensa kallar vi for fribeløp. Inntekta til ektefellen/partnaren/sambuaren din har berre betydning for storleiken på barnetillegget til barnet/barna som bur saman med begge foreldra sine.",
                English to "Your income affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced. We call this limit the exemption amount. The income of your spouse/partner/cohabiting partner does not affect the size of your child supplement."
            )
        }
    }
}

object TBU3801 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Endringer i inntektene til deg og ektefellenpartnerensamboeren dininntekten din kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på nav.no.",
                Nynorsk to "Endringar i inntektene til deg og ektefellen/partnaren/sambuaren din inntekta di kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på nav.no.",
                English to "Changes in your and your spouse/partner/cohabiting partner's income may affect your child supplement. You can easily report income changes under the menu option «disability benefit» at nav.no."
            )
        }
    }
}
// TBU1284
data class BTFBinntektBruktiAvkortningOgFribeloepOgGrunnbeloep(
    val fellesbarnFribeloep: Expression<Kroner>,
    val fellesbarnInntektAnnenForelder: Expression<Kroner>,
    val fellesbarnInntektBruktiAvkortning: Expression<Kroner>,
    val grunnbeloep: Expression<Kroner>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val fbFribeloep = fellesbarnFribeloep.format()
                val fbInntektAnnenForelder = fellesbarnInntektAnnenForelder.format()
                val fbInntektBruktiAvkortning = fellesbarnInntektBruktiAvkortning.format()
                val grunnbeloep = grunnbeloep.format()

                textExpr(
                    Bokmal to "Inntekten din er ".expr() + fbInntektBruktiAvkortning + " kroner og inntekten til ektefellenpartnerensamboeren din er ".expr() + fbInntektAnnenForelder + " kroner. Folketrygdens grunnbeløp på inntil ".expr() + grunnbeloep + " kroner er holdt utenfor inntekten til ektefellenpartnerensamboeren din. Til sammen er inntektene høyere lavere enn fribeløpet ditt på ".expr() + fbFribeloep + " kroner. Barnetillegget ditt er derfor ikke redusert ut fra inntekt.  Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.  Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.".expr(),
                    Nynorsk to "Inntekta di er ".expr() + fbInntektBruktiAvkortning + " kroner, og inntekta til ektefellen/partnaren/sambuaren din er ".expr() + fbInntektAnnenForelder + " kroner. Grunnbeløpet i folketrygda på inntil <grunnbelop> kroner er halde utanfor inntekta til ektefellen/partnaren/sambuaren din. Til saman er inntektene høgare/lågare enn fribeløpet ditt på ".expr() + fbFribeloep + " kroner. Barnetillegget ditt er derfor ikkje redusert ut frå inntekt. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.".expr(),
                    English to "Your income is NOK ".expr() + fbInntektBruktiAvkortning + " and your spouse/partner/cohabiting partner's income is NOK ".expr() + fbInntektAnnenForelder + ". The national insurance basic amount of up to NOK <grunnbelop> has not been included in your spouse/partner/cohabiting partner's income. Together, the incomes are higher/lower than your exemption amount of NOK ".expr() + fbFribeloep + ". Therefore, your child supplement has not been reduced on the basis of your income. What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took into account when we changed your child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.".expr()
                )
            }
        }
    }

// TBU1285
data class BTSBinntektBruktiAvkortningOgBTfribeleop(
    val saerkullsbarnInntektBruktiAvkortning: Expression<Kroner>,
    val saerkullsbarnFribeloep: Expression<Kroner>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val sbInntektBruktiAvkortning = saerkullsbarnInntektBruktiAvkortning.format()
            val sbFribeloep = saerkullsbarnFribeloep.format()
            textExpr(
                Bokmal to "Inntekten din på ".expr() + sbInntektBruktiAvkortning + " kroner er høyere lavere enn fribeløpet ditt på ".expr() + sbFribeloep + " kroner. Barnetillegget ditt er derfor ikke redusert ut fra inntekt. Inntekten din er ".expr() + sbInntektBruktiAvkortning + " kroner.".expr(),
                Nynorsk to "Inntekta di på ".expr() + sbInntektBruktiAvkortning + " kroner er høgare/lågare enn fribeløpet ditt på ".expr() + sbFribeloep + " kroner. Barnetillegget ditt er derfor ikkje redusert ut frå inntekt. Inntekta di er ".expr() + sbInntektBruktiAvkortning + " kroner.".expr(),
                English to "Your income of NOK ".expr() + sbInntektBruktiAvkortning + " is higher lower than your exemption amount of NOK ".expr() + sbFribeloep + ". Therefore your child supplement has not been reduced on the basis of your income. Your income is NOK ".expr() + sbInntektBruktiAvkortning + ".".expr()
            )
            textExpr(
                Bokmal to "Inntekten din er ".expr() + sbInntektBruktiAvkortning + " kroner. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.".expr(),
                Nynorsk to "Inntekta di er ".expr() + sbInntektBruktiAvkortning + " kroner. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.".expr(),
                English to "Your income is NOK ".expr() + sbInntektBruktiAvkortning + ". What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took in to account when we changed your child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year.".expr()
            )
        }
    }
}

// TBU1286
data class BTfribeloep(
    val saerkullsbarnFribeloep: Expression<Kroner>,
    val fellesbarnFribeloep: Expression<Kroner>

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val sbFribeloep = saerkullsbarnFribeloep.format()
            val fbFribeloep = fellesbarnFribeloep.format()
            textExpr(
                Bokmal to "Inntekten din er høyerelavere enn ".expr() + sbFribeloep + " kroner, som er fribeløpet for barnetillegget til barnetbarna som ikke bor sammen med begge foreldrene. Dette barnetillegget er derfor ikke redusert ut fra inntekt. Til sammen er også inntektene til deg og ektefellenpartnerensamboeren din høyerelavere enn ".expr() +
                    fbFribeloep + " kroner, som er fribeløpet for barnetillegget til barnetbarna som bor med begge sine foreldre. Dette barnetillegget er derfor ikke redusert ut fra inntekt. Barnetilleggene er derfor ikke redusert ut fra inntekt.".expr(),
                Nynorsk to "Inntekta di er høgare/lågare enn ".expr() + sbFribeloep + " kroner, som er fribeløpet for barnetillegget til barnet/barna som ikkje bur saman med begge foreldra. Dette barnetillegget er derfor ikkje redusert ut frå inntekt. Til saman er også inntektene til deg og ektefellen/partnaren/sambuaren din høgare/lågare enn ".expr() +
                    fbFribeloep + " kroner, som er fribeløpet for barnetillegget til barnet/barna som bur saman med begge foreldra sine. Dette barnetillegget er derfor ikkje redusert ut frå inntekt. Desse barnetillegga er derfor ikkje redusert ut frå inntekt.".expr(),
                English to "Your income is higher/lower than NOK ".expr() + sbFribeloep + ", which is the exemption amount for the child supplement for the child/children who do not live together with both parents. Therefore your child supplement has not been reduced on the basis of your income. Together, your income and your spouse/partner/cohabiting partner's income is higher/lower than NOK ".expr() +
                    fbFribeloep + ", which is the exemption amount for the child supplement for the child/children who lives together with both parents. Therefore your child supplement has not been reduced on the basis of your income. Therefore your child supplements have not been reduced on the basis of your income.".expr()
            )
            text(
                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for barnetbarna som bor med begge sine foreldre. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for barnet/barna som bur saman med begge foreldra sine. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                English to "What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took in to account when we changed your child supplement for the child/children who live(s) together with both parents. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
            )
            text(
                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for barnetbarna som ikke bor sammen med begge foreldrene. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for barnet/barna som ikkje bur saman med begge foreldra. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                English to "What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took in to account when we changed your child supplement for the child/children who do not live together with both parents. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."

            )
        }
    }
}

// TBU1286.1 in <maler/fraser/omregning/ufoeretrygd/Ufoeretrygd.kt
// TBU1286.2 in <maler/fraser/omregning/ufoeretrygd/Ufoeretrygd.kt

// TBU2490
data class BTinntektstak(
    val fellesbarnInntektstak: Expression<Kroner>,
    val saerkullsbarnInntektstak: Expression<Kroner>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            val fbInntektstak = fellesbarnInntektstak.format()
            val sbInntektstak = saerkullsbarnInntektstak.format()
            textExpr(
                Bokmal to "Barnetillegget for barnetbarna som bor med begge sine foreldre, blir ikke utbetalt fordi de samlede inntektene er høyere enn ".expr() +
                    fbInntektstak + " kroner. Barnetillegget for barnetbarna som ikke bor sammen med begge foreldrene, blir heller ikke utbetalt fordi inntekten din alene er høyere enn <BarnetilleggSerkull.inntektstak> kroner. Inntektene er over grensen for å få utbetalt barnetillegg.".expr(),
                Nynorsk to "Barnetillegget for barnet/barna som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei samla inntektene er høgare enn ".expr() +
                    fbInntektstak + " kroner. Barnetillegget for barnet/barna som ikkje bur saman med begge foreldra, blir heller ikkje utbetalt fordi inntekta di åleine er høgare enn <BarnetilleggSerkull.inntektstak> kroner. Inntektene er over grensa for å få utbetalt barnetillegg.".expr(),
                English to "You will not receive child supplement for the child/children who lives together with both parents because your total income is higher than NOK ".expr() +
                    fbInntektstak + ". You will not receive child supplement for the child/children who do not live together with both parents because your income alone is higher than NOK <BarnetilleggSerkull.inntektstak>. You will not receive child supplement because your income exceeds the income limit.".expr()
            )
        }
    }
}

object TBU1288 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Du kan lese mer om beregningen av barnetillegg i vedlegget «Opplysninger om beregningen».",
                Nynorsk to "Du kan lese meir om berekninga av barnetillegg i vedlegget «Opplysningar om berekninga».",
                English to "Read more about how child supplements are calculated in the attachment called «Information about calculations»."
            )
        }
    }
}

object TBU2364 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Du må melde fra om eventuell inntekt",
                Nynorsk to "Du må melde frå om eventuell inntekt",
                English to "Report any income"
            )
        }
    }
}

object TBU2365 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på nav.no. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd ved siden av inntekten din.",
                Nynorsk to "Dersom du er i jobb eller har planar om å jobbe, må du melde frå om eventuelle endringar i inntekta di. Det er viktig at du melder frå så tidleg som råd, slik at du får rett utbetaling av uføretrygd. Dette kan du gjere under menyvalet «uføretrygd» når du logger deg inn på nav.no. Her kan du leggje inn kor mykje du forventar å tene i løpet av året. Du vil då kunne sjå kor mykje du kjem til å få betalt ut i uføretrygd ved sida av inntekta di.",
                English to "If you are working or are planning to work, you must report any changes in your income. It is important that you report this as soon as possible, so that you receive the correct disability benefit payments. You can register your change in income under the option “uføretrygd” at nav.no. You can register how much you expect to earn in the calendar year. You will then be able to see how much disability benefit you will receive in addition to your income."
            )
        }
    }
}

object TBU2212 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Du må melde fra om endringer",
                Nynorsk to "Du må melde frå om endringar",
                English to "You must notify any changes"
            )
        }
        paragraph {
            text(
                Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget «Orientering om rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                Nynorsk to "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget «Orientering om rettar og plikter» ser du kva endringar du må seie frå om.",
                English to "You must notify us immediately of any changes in your situation. In the attachment “Information about rights and obligations” you will see which changes you must report."
            )
        }
    }
}

object TBU2213 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Du har rett til å klage",
                Nynorsk to "Du har rett til å klage",
                English to "You have the right of appeal"
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «Orientering om rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                Nynorsk to "Viss du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget «Orientering om rettar og plikter» får du vite meir om korleis du går fram. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                English to "If you believe the decision is wrong, you may appeal. The deadline for appeal is six weeks from the date you received the decision. In the attachment “Information about rights and obligations”, you can find out more about how to proceed. You will find forms and information at ${Constants.KLAGE_URL}."

            )
        }
    }
}

// TBU1074 in <maler/fraser/common/Felles.kt
object TBU2242 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «Orientering om rettigheter og plikter» for informasjon om hvordan du går fram.",
                Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «Orientering om rettar og plikter» for informasjon om korleis du går fram.",
                English to "You are entitled to see your case documents. Refer to the attachment “Rights and obligations” for information about how to proceed."
            )
        }
    }
}

// TBU1227 in <maler/fraser/omregning/ufoeretrygd/Ufoeretrygd.kt

object TBU1228 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Skattekort",
                Nynorsk to "Skattekort",
                English to "Tax card"
            )

        }
        paragraph {
            text(
                Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til NAV fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten. Du bør likevel sjekke at du har riktig skattekort. Skattekortet kan du endre på skatteetaten.no. Under menyvalget «uføretrygd» når du logger deg inn på nav.no, kan du se hvilket skattetrekk som er registrert hos NAV.",
                Nynorsk to "Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet ditt til NAV, fordi skatteopplysningane dine blir sende elektronisk frå Skatteetaten. Du bør likevel sjekke at du har rett skattekort. Skattekortet kan du endre på skatteetaten.no. Under menyvalet «uføretrygd» når du logger deg inn på nav.no, kan du sjå kva skattetrekk som er registrert hos NAV.",
                English to "You do not need to submit your tax card to NAV because your tax details are sent electronically from the Norwegian Tax Administration. However, you should check that you have the correct tax card. You may change your tax card under skatteetaten.no. You may see your registered income tax rate under the option “uføretrygd” at nav.no."
            )
        }
    }
}

object TBU3730 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Skatt for deg som bor i utlandet",
                Nynorsk to "Skatt for deg som bur i utlandet",
                English to "Tax for people who live abroad"
            )
        }
        paragraph {
            text(
                Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
                Nynorsk to "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på skatteetaten.no. Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur.",
                English to "You can find more information about withholding tax to Norway at skatteetaten.no. For information about taxation from your country of residence, you can contact the locale tax authorities."
            )
        }
    }
}
