package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.maler.OpphoererBarnetilleggAuto
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.time.LocalDate
import java.util.Date


data class OpphoererBarnetilleggAutoDto(
    val oensketVirkningsDato: LocalDate,
    val fdatoPaaBarnetilleggOpphoert: Number,
    val totalNettoMaanedligUfoertrygdUtbetalt: Kroner,
)

object OpphoerBarnetilleggAuto {
    val totalNettoMaanedligUfoertrygdUtbetalt = it.select(OpphoererBarnetilleggAutoDto::totalNettoMaanedligUfoertrygdUtbetalt)
    val oensketVirkningsDato = it.select(OpphoererBarnetilleggAutoDto::oensketVirkningsDato)
    val fdatoPaaBarnetilleggOpphoert = it.select(OpphoererBarnetilleggAutoDto::fdatoPaaBarnetilleggOpphoert)

    val TBU2290 = OutlinePhrase<LangBokmalNynorskEnglish, OpphoererBarnetilleggAutoDto> {
        paragraph {
            textExpr(
                Bokmal to "Vi har vedtatt at barnetillegget i uføretrygden din opphører fra ".expr() + oensketVirkningsDato.format() + " for barn født ".expr() + fdatoPaaBarnetilleggOpphoert.format() + ".".expr(),
                Nynorsk to "Vi har stansa barnetillegget i uføretrygda di frå ".expr() + oensketVirkningsDato.format() + " for barn fødd ".expr() + fdatoPaaBarnetilleggOpphoert.format() + ".".expr(),
                English to "The child supplement in your disability benefit has been discontinued, effective as of ".expr() + oensketVirkningsDato.fomat() + ", for child born ".expr() + fdatoPaaBarnetilleggOpphoert.format() + ".".expr()
            )
        }
    }

    val TBU1120 = OutlinePhrase<LangBokmalNynorskEnglish, OpphoererBarnetilleggAutoDto> {
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd per måned før skatt.".expr(),
                Nynorsk to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd per månad før skatt.".expr(),
                English to "Your monthly disability benefit payment will be NOK ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " before tax.".expr()
            )
        }
    }

    val TBU1121 = OutlinePhrase<LangBokmalNynorskEnglish, OpphoererBarnetilleggAutoDto> {
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd og barnetillegg per måned før skatt.".expr(),
                Nynorsk to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd og barnetillegg per månad før skatt.".expr(),
                English to "Your monthly disability benefit and child supplement payment will be NOK ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " before tax.".expr()
            )
        }
    }

    val TBU1122 = OutlinePhrase<LangBokmalNynorskEnglish, OpphoererBarnetilleggAutoDto> {
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt.".expr(),
                Nynorsk to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd og attlevandetillegg per månad før skatt.".expr(),
                English to "Your monthly disability benefit and survivor's supplement payment will be NOK ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " before tax.".expr()
            )
        }
    }

    val TBU1123 = OutlinePhrase<LangBokmalNynorskEnglish, OpphoererBarnetilleggAutoDto> {
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt.".expr(),
                Nynorsk to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt.".expr(),
                English to "Your monthly disability benefit, child supplement and survivor's supplement payment will be NOK ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " before tax.".expr()
            )
        }
    }

    val TBU1253 = OutlinePhrase<LangBokmalNynorskEnglish, OpphoererBarnetilleggAutoDto> {
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd og ektefelletillegg per måned før skatt.".expr(),
                Nynorsk to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd og ektefelletillegg per månad før skatt.".expr(),
                English to "Your monthly disability benefit and spouse supplement payment will be NOK ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " before tax.".expr()
            )
        }
    }

    val TBU1254 = OutlinePhrase<LangBokmalNynorskEnglish, OpphoererBarnetilleggAutoDto> {
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd, barne- og ektefelletillegg per måned før skatt.".expr(),
                Nynorsk to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i uføretrygd, barne- og ektefelletillegg per månad før skatt.".expr(),
                English to "Your monthly disability benefit, child supplement and spouse supplement payment will be NOK ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " before tax.".expr()
            )
        }
    }

    val TBU4082 = OutlinePhrase<LangBokmalNynorskEnglish, OpphoererBarnetilleggAutoDto> {
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i barnetillegg per måned før skatt.".expr(),
                Nynorsk to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i barnetillegg per månad før skatt.".expr(),
                English to "Your monthly child supplement payment will be NOK ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " before tax.".expr()
            )
        }
    }

    val TBU4083 = OutlinePhrase<LangBokmalNynorskEnglish, OpphoererBarnetilleggAutoDto> {
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i barne- og ektefelletillegg per måned før skatt.".expr(),
                Nynorsk to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i barne- og ektefelletillegg per månad før skatt.".expr(),
                English to "Your monthly child supplement and spouse supplement  payment will be NOK ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " before tax.".expr()
            )
        }
    }

    val TBU4084 = OutlinePhrase<LangBokmalNynorskEnglish, OpphoererBarnetilleggAutoDto> {
        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i ektefelletillegg per måned før skatt.".expr(),
                Nynorsk to "Du får ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " kroner i ektefelletillegg per månad før skatt.".expr(),
                English to "Your monthly spouse supplement payment will be NOK ".expr() + totalNettoMaanedligUfoertrygdUtbetalt.format() + " before tax.".expr()
            )
        }
    }

    val TBU2223 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned.",
                Nynorsk to "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad.",
                English to "Your disability benefit will still be paid no later than the 20th of every month."
            )
        }
    }

    val TBU1128 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet.",
                Nynorsk to "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet.",
                English to "In this letter we will explain your rights and obligations. Therefore, it is important that you read the whole letter."
            )
        }
    }

    val TBU1092 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Begrunnelse for vedtaket",
                Nynorsk to "Grunngiving for vedtaket",
                English to "Grounds for the decision"
            )
        }
    }
    val TBU3920 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "For å ha rett til barnetillegg må du forsørge barn under 18 år. Vi har vedtatt at barnetillegget i uføretrygden opphører fordi barnetbarna har fylt 18 år. ",
                Nynorsk to "For å ha rett til barnetillegg må du forsørgje barn under 18 år. Vi har stansa barnetillegget i uføretrygda fordi barnetbarna har fylt 18 år.",
                English to "To be eligible for child supplement, you must support children under 18 years of age. The child supplement in your disability benefit has been discontinued because your  childchildren has(have) turned 18 years of age."
            )
        }
    }
    val TBU4085 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12 og forskrift om overgangsreglar for barnetillegg i uføretrygda.",
                English to "The decision has been made pursuant to Section 12-15, 12-16 and 22-12 of the Norwegian National Insurance Act and the transitional provisions for the child supplement in your disability benefit."
            )
        }
    }
    val TBU1174 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Dette er virkningstidspunktet ditt",
                Nynorsk to "Dette er verknadstidspunktet ditt",
                English to "This is your effective date"
            )
        }
    }
    val TBU4086 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Barnetillegget ditt har opphørter blitt endret fra <OnsketVirkningsDato>. Dette er måneden etter at barnet barna har fylt 18 år. Dette kaller vi virkningstidspunktet.",
                Nynorsk to "Barnetillegget ditt er stansaendra frå <OnsketVirkningsDato>. Dette er månaden etter at barnet barna  har fylt 18 år. Dette kallar vi verknadstidspunktet.",
                English to "Your child supplement has been discontinued changed from <OnsketVirkningsDato>. This is the month after the child children has have turned 18. This is called the effective date."
            )
        }
    }
    val TBU3800 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Slik påvirker inntekt barnetillegget ditt",
                Nynorsk to "Slik verkar inntekt inn på barnetillegget ditt",
                English to "Income will affect your child supplement "
            )
        }
    }
    val TBU1278 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn 95 prosent av inntekten din før du ble ufør. 95 prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på <GradertOppjustertIFU> kroner. Uføretrygden og barnetillegget ditt er til sammen lavere enn dette. Barnetillegget er derfor fastsatt til 40 prosent av folketrygdens grunnbeløp for hvert barn. Har du inntekt ved siden av uføretrygden vil dette kunne ha betydning for størrelsen på barnetillegget ditt.",
                Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn 95 prosent av inntekta di før du blei ufør. 95 prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på <GradertOppjustertIFU> kroner. Uføretrygda di og barnetillegget ditt er til saman lågare enn dette. Barnetillegget er derfor fastsett til 40 prosent av grunnbeløpet i folketrygda for kvart barn. Har du inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt.",
                English to "Your disability benefit and child supplement together cannot exceed more than 95 percent of your income before you became disabled. 95 percent of the income you had before you became disabled is equivalent today to an income of NOK <GradertOppjustertIFU>. Your disability benefit and child supplement together are lower than this. Therefore, your child supplement has been determined to be 40 percent of the national insurance basic amount for each child. If you have an income in addition to your disability benefit, this could also affect the size of your child supplement."
            )
        }
    }
    val TBU3803 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Uføretrygden og barnetillegget ditt kan til sammen ikke utgjøre mer enn 95 prosent av inntekten din før du ble ufør. 95 prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på <GradertOppjustertIFU> kroner. Uføretrygden og barnetillegget ditt er til sammen høyere enn dette. Årlig barnetillegg før reduksjon ut fra inntekt blir ikke utbetalt. derfor redusert til <SumBruttoEtterReduksjonBT> kroner. Har du inntekt ved siden av uføretrygden vil dette også kunne ha betydning for størrelsen på barnetillegget ditt.",
                Nynorsk to "Uføretrygda di og barnetillegget ditt kan til saman ikkje utgjere meir enn 95 prosent av inntekta di før du blei ufør. 95 prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på <GradertOppjustertIFU> kroner. Uføretrygda di og barnetillegget ditt er til saman høgare enn dette. Brutto årleg barnetillegg før reduksjon ut frå inntekt blir ikkje utbetalt.derfor redusert til <SumBruttoEtterReduksjonBT> kroner. Har du inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt.",
                English to "Your disability benefit and child supplement together cannot exceed more than 95 percent of your income before you became disabled. 95 percent of the income you had before you became disabled is equivalent today to an income of NOK <GradertOppjustertIFU>. Your disability benefit and child supplement together are higher than this. Therefore you will not receive child supplement. Your annual child supplement before income reduction will therefore be reduced to NOK <SumBruttoEtterReduksjonBT>. If you have an income in addition to your disability benefit, this could also affect the size of your child supplement."
            )
        }
    }
    val TBU3802 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "I perioden fra 1. januar 2016 til 31. desember 2020 er det egne regler for hvordan barnetillegget ditt blir justert.",
                Nynorsk to "I perioden frå 1. januar 2016 til 31. desember 2020 er det eigne reglar for korleis barnetillegget ditt blir justert.",
                English to "In the period from 1 January 2016 to 31 December 2020, there will be special rules for how your child supplement is adjusted."
            )
            text(
                Bokmal to "Fram til 31. desember (2016)(2017)(2018)(2019)(2020) kan ikke uføretrygden og barnetillegget ditt til sammen være høyere enn 11010710410198 prosent av inntekten din før du ble ufør. Uføretrygden og barnetillegget ditt utgjør <AndelYtelseAvOIFU> prosent av det som var inntekten din før du ble ufør.",
                Nynorsk to "Fram til 31. desember (2016)(2017)(2018)(2019(2020) kan ikkje uføretrygda di og barnetillegget ditt til saman vere høgare enn 110 107 104 101 98 prosent av inntekta di før du blei ufør. Uføretrygda og barnetillegget ditt utgjer <AndelYtelseAvOIFU> prosent av det som var inntekta di før du blei ufør.",
                English to "Up to 31 December (2016)(2017)(2018)(2019)(2020), your disability benefit and child supplement cannot be higher than 110/107/104/101/98 percent of your income before you became disabled. Your disability benefit and child supplement exceeds <AndelYtelseAvOIFU> percent of your income before you became disabled."
            )
            text(
                Bokmal to "11010710410198 prosent av den inntekten du hadde før du ble ufør tilsvarer i dag en inntekt på <GradertOppjustertIFU> kroner. Uføretrygden og barnetillegget ditt er til sammen høyerelavere enn dette. Barnetillegget blir derfor ikke utbetalt. Årlig barnetillegg før reduksjon ut fra inntekt er derfor redusert til <SumBruttoEtterReduksjonBT> kroner.  Barnetillegget er derfor fastsatt ut fra 40 prosent av folketrygdens grunnbeløp for hvert barn du får barnetillegg for. Fordi du har mindre enn 40 års trygdetid er barnetillegget ditt justert ned. Har du og ektefellenpartnerensamboeren din inntekt ved siden av uføretrygden vil dette også kunne ha betydning for størrelsen på barnetillegget ditt. ",
                Nynorsk to "110 107 104 101 98 prosent av den inntekta du hadde før du blei ufør, tilsvarer i dag ei inntekt på <GradertOppjustertIFU> kroner. Uføretrygda di og barnetillegget ditt er til saman høgare/lågare enn dette. Barnetillegget blir derfor ikkje utbetalt. Brutto årleg barnetillegg før reduksjon ut frå inntekt er derfor redusert til <SumBruttoEtterReduksjonBT> kroner. Barnetillegget er derfor fastsett ut frå 40 prosent av grunnbeløpet i folketrygda for kvart barn du får barnetillegg for. Fordi du har mindre enn 40 års trygdetid, er barnetillegget ditt justert ned. Har du og ektefellen/partnaren/sambuaren din inntekt ved sida av uføretrygda, kan dette også ha noko å seie for storleiken på barnetillegget ditt. ",
                English to "11010710410198 percent of the income you had before you became disabled is equivalent today to an income of NOK <GradertOppjustertIFU>. Your disability benefit and child supplement together are higher/lower than this. Therefore you will not receive child supplement. Your annual child supplement before income reduction will therefore be reduced to NOK <SumBruttoEtterReduksjonBT>. Your child supplement has therefore been determined on the basis of 40 percent of the national insurance basic amount for each child for whom you receive child supplement. As you have a period of insurance cover of less than 40 years, your child supplement has been adjusted downward. If you and your spouse/partner/cohabiting partner have an income in addition to disability benefit, this could also affect the size of your child supplement."
            )
        }
    }
    val TBU2338 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Inntekten din har betydning for hva du får i barnetillegg. Er inntekten din over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensen kaller vi for fribeløp. Inntekten til ektefellenpartnerensamboeren din har ikke betydning for størrelsen på barnetillegget.",
                Nynorsk to "Inntekta di har noko å seie for kva du får i barnetillegg. Er inntekta di over grensa for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensa kallar vi for fribeløp. Inntekta til ektefellen/partnaren/sambuaren din har ikkje noko å seie for storleiken på barnetillegget.",
                English to "The incomes of you and your spouse/partner/cohabiting partner affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced. We call this limit the exemption amount. The income of your spouse/partner/cohabiting partner only affects the size of the child supplement for the child/children who lives together with both parents."
            )
        }
    }
    val TBU2339 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Inntekten til deg og ektefellenpartnerensamboeren din har betydning for hva du får i barnetillegg. Er inntektene over grensen for å få utbetalt fullt barnetillegg, blir tillegget redusert. Denne grensen kaller vi for fribeløp. Inntekten til ektefellenpartnerensamboeren din har kun betydning for størrelsen på barnetillegget til barnetbarna som bor sammen med begge sine foreldre.",
                Nynorsk to "Inntekta til deg og ektefellen/partnaren/sambuaren din har noko å seie for kva du får i barnetillegg. Er den samla inntekta over grensa for å få utbetalt fullt barnetillegg, blir tillegget ditt redusert. Denne grensa kallar vi for fribeløp. Inntekta til ektefellen/partnaren/sambuaren din har berre betydning for storleiken på barnetillegget til barnet/barna som bur saman med begge foreldra sine.",
                English to "Your income affects how much you receive in child supplement. If your income exceeds the limit for receiving full child supplement, your child supplement will be reduced. We call this limit the exemption amount. The income of your spouse/partner/cohabiting partner does not affect the size of your child supplement."
            )
        }
    }
    val TBU3801 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Endringer i inntektene til deg og ektefellenpartnerensamboeren dininntekten din kan ha betydning for barnetillegget ditt. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» på nav.no.",
                Nynorsk to "Endringar i inntektene til deg og ektefellen/partnaren/sambuaren din inntekta di kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på nav.no.",
                English to "Changes in your and your spouse/partner/cohabiting partner's income may affect your child supplement. You can easily report income changes under the menu option \"disability benefit\" at nav.no."
            )
        }
    }
    val TBU1284 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Inntekten din er <BTFBBrukersInntektTilAvkortning> kroner og inntekten til ektefellenpartnerensamboeren din er <BTFBinntektAnnenForelder> kroner. Folketrygdens grunnbeløp på inntil <grunnbelop> kroner er holdt utenfor inntekten til ektefellenpartnerensamboeren din. Til sammen er inntektene høyere lavere enn fribeløpet ditt på <BTFBfribelop> kroner. Barnetillegget ditt er derfor ikke redusert ut fra inntekt.  Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget.  Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                Nynorsk to "Endringar i inntektene til deg og ektefellen/partnaren/sambuaren din inntekta di kan ha betydning for barnetillegget ditt. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» på nav.no.",
                English to "Your income is NOK <BTFBBrukersInntektTilAvkortning> and your spouse/partner/cohabiting partner's income is NOK <BTFBinntektAnnenForelder>. The national insurance basic amount of up to NOK <grunnbelop> has not been included in your spouse/partner/cohabiting partner's income. Together, the incomes are higher/lower than your exemption amount of NOK <BTFBfribelop>. Therefore your child supplement has not been reduced on the basis of your income. What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took in to account when we changed your child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
            )
        }
    }
    val TBU1285 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Inntekten din på <BTSBInntektBruktiAvkortning> kroner er høyere lavere enn fribeløpet ditt på <BTSBfribelop> kroner. Barnetillegget ditt er derfor ikke redusert ut fra inntekt. Inntekten din er <BTSBInntektBruktiAvkortning> kroner.",
                Nynorsk to "Inntekta di på <BTSBInntektBruktiAvkortning> kroner er høgare/lågare enn fribeløpet ditt på <BTSBfribelop> kroner. Barnetillegget ditt er derfor ikkje redusert ut frå inntekt. Inntekta di er <BTSBInntektBruktiAvkortning> kroner.",
                English to "Your income is NOK <BTSBInntektBruktiAvkortning>. What you have received in child supplement earlier this year, affects what you will receive in child supplement in the future. This we took in to account when we changed your child supplement. You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
            )
            text(
                Bokmal to "Inntekten din er <BTSBInntektBruktiAvkortning> kroner. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                Nynorsk to "Inntekta di er <BTSBInntektBruktiAvkortning> kroner. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                English to "Your income of NOK <BTSBInntektBruktiAvkortning> is higher lower than your exemption amount of NOK <BTSBfribelop>. Therefore your child supplement has not been reduced on the basis of your income. Your income is NOK <BTSBInntektBruktiAvkortning>."
            )
        }
    }
    val TBU1286 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        paragraph {
            text(
                Bokmal to "Inntekten din er høyerelavere enn <BTSBfribelop> kroner, som er fribeløpet for barnetillegget til barnetbarna som ikke bor sammen med begge foreldrene. Dette barnetillegget er derfor ikke redusert ut fra inntekt. Til sammen er også inntektene til deg og ektefellenpartnerensamboeren din høyerelavere enn <BTFBfribelop> kroner, som er fribeløpet for barnetillegget til barnetbarna som bor med begge sine foreldre. Dette barnetillegget er derfor ikke redusert ut fra inntekt. Barnetilleggene er derfor ikke redusert ut fra inntekt.",
                Nynorsk to "Inntekta di er høgare/lågare enn <BTSBfribelop> kroner, som er fribeløpet for barnetillegget til barnet/barna som ikkje bur saman med begge foreldra. Dette barnetillegget er derfor ikkje redusert ut frå inntekt. Til saman er også inntektene til deg og ektefellen/partnaren/sambuaren din høgare/lågare enn <BTFBfribelop> kroner, som er fribeløpet for barnetillegget til barnet/barna som bur saman med begge foreldra sine. Dette barnetillegget er derfor ikkje redusert ut frå inntekt. Desse barnetillegga er derfor ikkje redusert ut frå inntekt.",
                English to "Your income is higher/lower than NOK <BTSBfribelop>, which is the exemption amount for the child supplement for the child/children who do not live together with both parents. Therefore your child supplement has not been reduced on the basis of your income. Together, your income and your spouse/partner/cohabiting partner's income is higher/lower than NOK<BTFBfribelop>, which is the exemption amount for the child supplement for the child/children who lives together with both parents. Therefore your child supplement has not been reduced on the basis of your income. Therefore your child supplements have not been reduced on the basis of your income."
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