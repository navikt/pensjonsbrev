package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Felles
import no.nav.pensjon.brev.api.model.NAVEnhet
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr


val vedleggPlikter_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Plikt til å opplyse om endringer - folketrygdloven § 21-3",
            Nynorsk to "Plikt til å opplyse om endringar  - folketrygdlova § 21-3",
            English to "Duty to inform of changes  - Section 21-3 of the National Insurance Act"
        )
    }
    paragraph {
        text(
            Bokmal to "Du må gi oss beskjed hvis",
            Nynorsk to "Du må gi oss beskjed hvis",
            English to "You must inform us if"
        )
    }
}

val vedleggPlikterAP2_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du skal oppholde deg utenfor Norge i en lengre periode eller skal flytte til et annet land",
        Nynorsk to "du skal opphalde deg utanfor Noreg i ein lengre periode eller skal flytte til eit anna land",
        English to "you intend to stay in a foreign country for an extended period of time or intend to move to another country"
    )
}

val vedleggPlikterAP3_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du flytter til et annet land, tilbake til Norge eller du endrer adresse i ditt nåværende bostedsland",
        Nynorsk to "du flyttar til eit anna land, tilbake til Noreg eller du endrar adresse i landet kor du bur no",
        English to "you move to another country, move back to Norway or if you change address in your country of residence"
    )
}

val vedleggPlikterAP1_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du gifter deg eller inngår samboerskap",
        Nynorsk to "du giftar deg eller inngår sambuarskap",
        English to "you marry or get a cohabiting partner"
    )
}

val vedleggPlikterAP4_002 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for ektefellen din",
        Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til ektefellen din endrar seg",
        English to "the employment income, pension, disability benefit or investment income changes for your spouse"
    )
}

val vedleggPlikterAP13_002 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for partneren din",
        Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til partnaren din endrar seg",
        English to "the employment income, pension, disability benefit or investment income changes for your partner"
    )
}

val vedleggPlikterAP15_002 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for samboeren din",
        Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til sambuaren din endrar seg",
        English to "the employment income, pension, disability benefit or investment income changes for your cohabiting partner"
    )
}

//vedleggPlikterAP6_002, vedleggPlikterAP14_002, vedleggPlikterAP18_001
val vedleggPlikterAPFlytterFraHverandre = TextOnlyPhrase<LangBokmalNynorskEnglish, Sivilstand> { sivilstand ->
    text(
        Bokmal to "du og ",
        Nynorsk to "du og ",
        English to "you and your "
    )
    showIf(sivilstand.isOneOf(Sivilstand.GIFT)){
        text(Bokmal to "ektefellen",Nynorsk to "ektefellen",English to "spouse")
    }.orShowIf(sivilstand.isOneOf(Sivilstand.PARTNER)){
        text(Bokmal to "partneren",Nynorsk to "partnaren",English to "partner")
    }.orShowIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)){
        text(Bokmal to "samboeren",Nynorsk to "sambuaren",English to "cohabiting partner")
    }
    text(
        Bokmal to " din flytter fra hverandre",
        Nynorsk to " din flyttar frå kvarandre",
        English to " separate"
    )
}

val vedleggPlikterAP16_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(Bokmal to "du gifter deg", Nynorsk to "du giftar deg", English to "you marry")
}

val vedleggPlikterAP17_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du får barn med samboeren din",
        Nynorsk to "du får barn med sambuaren din",
        English to "you and your cohabiting partner have a child together"
    )
}

val vedleggPlikterAP19_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(Bokmal to "samboeren din dør", Nynorsk to "sambuaren din døyr", English to "your cohabiting partner dies")
}

val vedleggPlikterAP8_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du og ektefellen din flytter sammen igjen",
        Nynorsk to "du og ektefellen din flyttar saman igjen",
        English to "you and your spouse move back together"
    )
}

val vedleggPlikterAP11_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du og partneren din flytter sammen igjen",
        Nynorsk to "du og partnaren din flyttar saman igjen",
        English to "you and your partner move back together"
    )
}

val vedleggPlikterAP9_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(Bokmal to "du blir skilt", Nynorsk to "du blir skild", English to "you divorce")
}

val vedleggPlikterAP7_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(Bokmal to "ektefellen din dør", Nynorsk to "ektefellen din døyr", English to "your spouse dies")
}

val vedleggPlikterAP12_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(Bokmal to "partneren din dør", Nynorsk to "partnaren din døyr", English to "your partner dies")
}

val vedleggPlikterAP10_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(Bokmal to "du får ny samboer", Nynorsk to "du får ny sambuar", English to "you get a new cohabiting partner")
}

val vedleggPlikterAP5_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Sivilstand> { sivilstand ->
    showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
        text(
            Bokmal to "en av dere får et varig opphold i institusjon",
            Nynorsk to "ein av dykk får et varig opphald i institusjon",
            English to "either you or your spouse get permanent residency in an institution"
        )
    }
    showIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.PARTNER_LEVER_ADSKILT)) {
        text(
            Bokmal to "en av dere får et varig opphold i institusjon",
            Nynorsk to "ein av dykk får et varig opphald i institusjon",
            English to "either you or your partner get permanent residency in an institution"
        )
    }
    showIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
        text(
            Bokmal to "en av dere får et varig opphold i institusjon",
            Nynorsk to "ein av dykk får et varig opphald i institusjon",
            English to "either you or your partner get permanent residency in an institutio"
        )
    }
}

val vedleggPlikterAP26_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du får et varig opphold i institusjon",
        Nynorsk to "du blir innlagd på institusjon",
        English to "you get permanent residency in an institution"
    )
}

val vedleggPlikterAP27_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du sitter i varetekt, soner straff eller er under forvaring",
        Nynorsk to "du sit i varetekt, sonar straff eller er under forvaring",
        English to "you are held in detention, incarcerated or in custody"
    )
}

val vedleggPlikterHvorforMeldeAP_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Skjer det endringer, kan det få betydning for hvor mye du kan få utbetalt i alderspensjon. Derfor er det viktig at du gir oss beskjed så raskt som mulig.",
            Nynorsk to "Skjer det endringar, kan det få betydning for kor mykje du kan få utbetalt i alderspensjon. Derfor er det viktig at du gir oss beskjed så raskt som mogleg.",
            English to "To make sure you get the right amount of retirement pension, you need to report any changes in your circumstances that can influence the assessment of the supplement you receive. It is important that you notify any change to us as soon as possible."
        )
    }
}

val vedleggPlikterRettTilBarnetilleggAP_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Fordi du får barnetillegg må du også melde fra om endringer som kan ha betydning for dette tillegget.",
            Nynorsk to "Fordi du får barnetillegg må du også melde frå om endringar som kan ha betydning for dette tillegget.",
            English to "Because you receive child supplement, you must notify us if there are any changes in circumstances that can have an influence on the assessment of the supplement."
        )
    }
    paragraph {
        text(
            Bokmal to "Du må gi oss beskjed hvis barn du forsørger",
            Nynorsk to "Du må gi oss beskjed om barn du forsørgjer",
            English to "You must notify us if the child(ren) you provide for will"
        )
    }
    list {
        item {
            text(
                Bokmal to "får egen inntekt som er mer enn folketrygden grunnbeløp omsorgssituasjonen for barnet endrer seg",
                Nynorsk to "får eiga inntekt som er meir enn grunnbeløpet i folketrygda skjer endringar av omsorgsituasjonen",
                English to "earn an income exceeding the National Insurance basic amount have parental care provision changes"
            )
            text(
                Bokmal to "skal flytte til et annet land",
                Nynorsk to "skal flytte til eit anna land",
                English to "move to another country"
            )
            text(
                Bokmal to "skal oppholde seg i et annet land i mer enn 90 dager i løpet av en tolvmånedersperiode",
                Nynorsk to "skal opphalde seg i eit anna land i meir enn 90 dagar i løpet av ein tolv månedars periode",
                English to "stay in another country for more than 90 days in a 12 month period"
            )
        }
    }
}

val vedleggPlikterRettTilEktefelletilleggAP_001 =
    OutlinePhrase<LangBokmalNynorskEnglish, Sivilstand> {
        val sivilstand = it
        paragraph {
            text(
                Bokmal to "Fordi du får ektefelletillegg må du også melde fra om endringer som kan ha betydning for dette tillegget.",
                Nynorsk to "Fordi du får ektefelletillegg må du også melde frå om endringar som kan ha betydning for dette tillegget.",
                English to "Because you receive spouse supplement, you must notify us if there are any changes in circumstances that can have an influence on the assessment of the supplement."
            )
        }

        paragraph {
            showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
                text(
                    Bokmal to "Du må gi oss beskjed hvis ektefellen du forsørger",
                    Nynorsk to "Du må gi oss beskjed hvis ektefellen du forsørgjar",
                    English to "You must notify us if the spouse you provide for will"
                )
            }
        }

        paragraph {
            showIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.GIFT_LEVER_ADSKILT)) {
                text(
                    Bokmal to "Du må gi oss beskjed hvis partneren du forsørger",
                    Nynorsk to "Du må gi oss beskjed hvis partaren du forsørgjar",
                    English to "You must notify us if the partner you provide for will"
                )
            }
        }

        paragraph {
            showIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
                text(
                    Bokmal to "Du må gi oss beskjed hvis saboeren du forsørger",
                    Nynorsk to "Du må gi oss beskjed hvis sambuaren du forsørgjar",
                    English to "You must notify us if the cohabitant you provide for will"
                )
            }
        }
        list {
            item {
                text(
                    Bokmal to "får egen inntekt som er mer enn  folketrygden grunnbeløp",
                    Nynorsk to "får eiga inntekt som er meir enn grunnbeløpet i folketrygda",
                    English to "earn an income exceeding the National Insurance basic amount"
                )
                text(
                    Bokmal to "skal flytte til et annet land",
                    Nynorsk to "skal flytte til eit anna land",
                    English to "move to another country"
                )
                text(
                    Bokmal to "skal oppholde seg i et annet land i mer enn 90 dager i løpet av en tolvmånedersperiode",
                    Nynorsk to "skal opphalde seg i eit anna land i meir enn 90 dager i løpet av ein tolv månedars periode",
                    English to "stay in another country for more than 90 days in a 12 month period"
                )
            }
        }
    }

val vedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP_001 = OutlinePhrase<LangBokmalNynorskEnglish, Sivilstand> {
    val sivilstand = it
    paragraph {
        text(
            Bokmal to "Fordi du får ektefelletillegg og barnetillegg må du også melde fra om endringer som kan ha betydning for disse tilleggene.",
            Nynorsk to "Fordi du får ektefelletillegg og barnetillegg må du også melde frå om endringar som kan ha betydning for desse tillegga.",
            English to "Because you receive both spouse supplement and child supplement, you must notify us if there are any changes in circumstances that can have an influence on the assessment of these supplements."
        )
    }

    paragraph {
        showIf(sivilstand.isOneOf(Sivilstand.GIFT, Sivilstand.GIFT_LEVER_ADSKILT)) {
            text(
                Bokmal to "Du må gi oss beskjed hvis barn eller ektefellen du forsørger",
                Nynorsk to "Du må gi oss beskjed om barn eller ektefellen du forsørgjer",
                English to "You must notify us if the child/children or spouse you provide for will"
            )
        }
        showIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
            text(
                Bokmal to "Du må gi oss beskjed hvis barn eller samboeren du forsørger",
                Nynorsk to "Du må gi oss beskjed om barn eller samboeren du forsørgjer",
                English to "You must notify us if the child/children or partner you provide for will"
            )
        }
        showIf(sivilstand.isOneOf(Sivilstand.PARTNER, Sivilstand.PARTNER_LEVER_ADSKILT)) {
            text(
                Bokmal to "Du må gi oss beskjed hvis barn eller partneren du forsørger",
                Nynorsk to "Du må gi oss beskjed om barn eller partneren du forsørgjer",
                English to "You must notify us if the child/children or partner you provide for will"
            )
        }
    }

    list {
        item {
            text(
                Bokmal to "får egen inntekt som er mer enn  folketrygden grunnbeløp",
                Nynorsk to "får eiga inntekt som er meir enn grunnbeløpet i folketrygda",
                English to "earn an income exceeding the National Insurance basic amount"
            )
            text(
                Bokmal to "omsorgssituasjonen for barnet endrer seg",
                Nynorsk to "omsorgssituasjonen for barnet endrar seg",
                English to "have parental care provision changes"
            )
            text(
                Bokmal to "skal flytte til et annet land",
                Nynorsk to "skal flytte til eit anna land",
                English to "move to another country"
            )
            text(
                Bokmal to "skal oppholde seg i et annet land i mer enn 90 dager i løpet av en tolvmånedersperiode",
                Nynorsk to "skal opphalde seg i eit anna land i meir enn 90 dager i løpet av ein tolv månedars periode",
                English to "stay in another country for more than 90 days in a 12 month period"
            )
        }
    }
}

val vedleggPlikterinntektsprovingBTFellesBarnSaerkullsbarnAP_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Hvor mye du får utbetalt i barnetillegg avhenger av den samlede inntekten du og ektefellen har. Du må derfor også gi beskjed hvis",
            Nynorsk to "Kor mykje du får utbetalt i barnetillegg er avhengig av den samla inntekta du og ektefellen har. Du må derfor også gi beskjed om",
            English to "How much you receive in child supplement depends on the total combined income of you and your spouse. You must therefore also notify us if your"
        )
        list {
            item {
                text(
                    Bokmal to "arbeidsinntekten endres",
                    Nynorsk to "arbeidsinntekta di endrer seg",
                    English to "employment income changes"
                )
                text(
                    Bokmal to "tjenestepensjonen fra offentlig eller private ordninger endres",
                    Nynorsk to "tenestepensjon frå offentlege eller private ordningar endrar seg",
                    English to "occupational pension from public or private schemes changes"
                )
                text(
                    Bokmal to "den individuelle pensjonsordningen, livrente og gavepensjonen endres",
                    Nynorsk to "den individuelle pensjonsordningen, livrente og gavepensjonen endres",
                    English to "individual pension scheme, annuity and gratuity payments changes"
                )
                text(
                    Bokmal to "andre ytelser fra folketrygden endres",
                    Nynorsk to "andre ytingar frå folketrygda endrar seg",
                    English to "other income supplements from the National Insurance Scheme changes"
                )
                text(
                    Bokmal to "ytelser og pensjon fra andre land endres",
                    Nynorsk to "ytingar og pensjon frå andre land endrar seg",
                    English to "other income supplements and pension from other countries changes"
                )
            }
        }
    }
}

val vedleggPlikterinntektsprovingBTOgETAP_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Hvor mye du får utbetalt i barnetillegg og ektefelletillegg avhenger av den samlede inntekten du har. Du må derfor også gi beskjed hvis",
            Nynorsk to "Kor mykje du får utbetalt i barnetillegg og ektefelletillegg er avhengig av den samla inntekta du har. Du må derfor også gi beskjed om",
            English to "How much you receive in child and spouse supplements depends on your total income. You must therefore notify us if your"
        )
        list {
            item {
                text(
                    Bokmal to "arbeidsinntekten endres",
                    Nynorsk to "arbeidsinntekta di endrer seg",
                    English to "employment income changes"
                )
                text(
                    Bokmal to "tjenestepensjonen fra offentlig eller private ordninger endre",
                    Nynorsk to "tenestepensjon frå offentlege eller private ordningar endrar seg",
                    English to "occupational pension from public or private schemes changes"
                )
                text(
                    Bokmal to "den individuelle pensjonsordningen, livrente og gavepensjonen endres",
                    Nynorsk to "individuelle pensjonsordningar, livrente og gåvepensjon endrar seg",
                    English to "individual pension scheme, annuity and gratuity payments changes"
                )
                text(
                    Bokmal to "andre ytelser fra folketrygden endres",
                    Nynorsk to "andre ytingar frå folketrygda endrar seg",
                    English to "other income supplements from the National Insurance Scheme changes"
                )
                text(
                    Bokmal to "ytelser og pensjon fra andre land endres",
                    Nynorsk to "ytingar og pensjon frå andre land endrar seg",
                    English to "other income supplements and pension from other countries changes"
                )
            }
        }
    }
}

val vedleggPlikterinntektsprovingETAP_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Hvor mye du får utbetalt i ektefelletillegg avhenger av den samlede inntekten du har. Du må derfor også gi beskjed hvis",
            Nynorsk to "Kor mykje du får utbetalt i ektefelletillegg avhenger av den samla inntekta du har. Du må derfor også gi beskjed om",
            English to "How much you receive in spouse supplement depends on your total income. You must therefore notify us if your"
        )
        list {
            item {
                text(
                    Bokmal to "arbeidsinntekten endres",
                    Nynorsk to "arbeidsinntekta di endrer seg",
                    English to "employment income changes"
                )
                text(
                    Bokmal to "tjenestepensjonen fra offentlig eller private ordninger endres",
                    Nynorsk to "tenestepensjon frå offentlege eller private ordningar endrar seg",
                    English to "occupational pension from public or private schemes changes"
                )
                text(
                    Bokmal to "den individuelle pensjonsordningen, livrente og gavepensjonen endres",
                    Nynorsk to "individuelle pensjonsordningar, livrente og gåvepensjon endrar seg",
                    English to "individual pension scheme, annuity and gratuity payments changes"
                )
                text(
                    Bokmal to "andre ytelser fra folketrygden endres",
                    Nynorsk to "andre ytingar frå folketrygda endrar seg",
                    English to "other income supplements from the National Insurance Scheme changes"
                )
                text(
                    Bokmal to "ytelser og pensjon fra andre land endres",
                    Nynorsk to "ytingar og pensjon frå andre land endrar seg",
                    English to "other income supplements and pension from other countries changes"
                )
            }
        }
    }
}

val vedleggPlikterUT_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Plikt til å opplyse om endringer - folketrygdloven § 21-3",
            Nynorsk to "Plikt til å opplyse om endringar - folketrygdlova § 21-3",
            English to "Duty to inform of changes - Section 21-3 of the National Insurance Act"
        )
    }
    paragraph {
        text(
            Bokmal to "Du må melde fra til NAV hvis",
            Nynorsk to "Du må melde frå til NAV om",
            English to "You must notify NAV if"
        )
    }
}

val vedleggPlikterUT1_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "inntekten din endrer seg. Du kan informere NAV om endret inntekt ved å bruke selvbetjeningsløsningen på nav.no",
        Nynorsk to "inntekta di endrar seg. Du kan informere NAV om endra inntekt ved å bruke sjølvbeteningsløysninga på nav.no",
        English to "your income changes. You can notify NAV of changes in your income by using the online service at nav.no"
    )
}

val vedleggPlikterUT2_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(Bokmal to "du endrer adresse", Nynorsk to "du endrar adresse", English to "you change address")
}

val vedleggPlikterUT3_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du skal begynne å arbeide i utlandet",
        Nynorsk to "du skal begynne å arbeide i utlandet",
        English to "you will start working abroad"
    )
}

val vedleggPlikterUT4_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du skal oppholde deg utenfor Norge lengre enn seks måneder",
        Nynorsk to "du skal opphalde deg utanfor Noreg lengre enn seks månader",
        English to "you intend to stay outside Norway for more than six months"
    )
}

val vedleggPlikterUT5_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du skal flytte til et annet land",
        Nynorsk to "du flyttar til eit anna land",
        English to "you are moving to another country"
    )
}

val vedleggPlikterUT6_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du gifter deg eller inngår samboerskap",
        Nynorsk to "du giftar deg eller inngår sambuarskap",
        English to "you get married or get a cohabitant"
    )
}

val vedleggPlikterUT7_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "barn du forsørger får en inntekt over folketrygdens grunnbeløp, eller det skjer endringer i omsorgsituasjonen",
        Nynorsk to "barn du forsørgjer får ei samla inntekt over grunnbeløpet i folketrygda, eller det skjer endringar av omsorgsituasjonen",
        English to "the child(ren) in your care earn an income exceeding the National Insurance basic amount or there are changes in the care situation"
    )
}

val vedleggPlikterUT8_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "tjenestepensjon fra offentlig eller private ordninger endrer seg",
        Nynorsk to "tenestepensjon frå offentlege eller private ordningar endrar seg",
        English to "your occupational pension from public or private schemes changes"
    )
}

val vedleggPlikterUT9_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "individuelle pensjonsordninger, livrente og gavepensjon endrer seg",
        Nynorsk to "individuelle pensjonsordningar, livrente og gåvepensjon endrar seg",
        English to "your individual pension scheme, annuity or gratuity pension changes"
    )
}

val vedleggPlikterUT10_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "ytelser og pensjon fra andre land endrer seg",
        Nynorsk to "ytingar og pensjon frå andre land endrar seg",
        English to "benefits and pensions from other countries change"
    )
}

val vedleggPlikterUT11_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du blir innlagt på institusjon",
        Nynorsk to "du blir innlagd på institusjon",
        English to "you are admitted to an institution"
    )
}

val vedleggPlikterUT12_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du sitter i varetekt, soner straff eller er under forvaring",
        Nynorsk to "du sit i varetekt, sonar straff eller er under forvaring",
        English to "you are remanded in custody or serving time in prison or preventive custody"
    )
}

val vedleggPlikterAFP_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Plikt til å opplyse om endringer",
            Nynorsk to "Plikt til å opplyse om endringar",
            English to "Duty to inform of changes"
        )
    }
    paragraph {
        text(
            Bokmal to "Du må melde fra til NAV om endringer som har betydning for størrelsen på pensjonen din. Du må alltid melde fra dersom",
            Nynorsk to "Du må melde frå til NAV om endringar som har noko å seie for storleiken på pensjonen din. Du må alltid melde frå dersom",
            English to "You must notify NAV of changes that may be important for your pension. You must always notify us if"
        )
    }
}

val vedleggPlikterAFP1_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "inntekten din endrer seg",
        Nynorsk to "inntekta di endrar seg",
        English to "your income changes"
    )
}

val vedleggPlikterAFP2_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du gifter deg eller inngår samboerskap",
        Nynorsk to "du giftar deg eller inngår sambuarskap",
        English to "you get married or get a cohabitant"
    )
}

val vedleggPlikterAFP3_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du skal oppholde deg utenfor Norge i en lengre periode eller skal flytte til et annet land",
        Nynorsk to "du skal opphalde deg utanfor Noreg i ein lengre periode eller skal flytte til eit anna land",
        English to "you intend to stay outside Norway for a long period or intend to move to another country "
    )
}

val vedleggPlikterAFP4_001 = TextOnlyPhrase<LangBokmalNynorskEnglish, Unit> {
    text(
        Bokmal to "du flytter til et annet land, tilbake til Norge eller du endrer adresse i ditt nåværende bostedsland",
        Nynorsk to "du flyttar til eit anna land, tilbake til Noreg eller du endrar adresse i landet kor du bur no",
        English to "you move to another country, move back to Norway, or you change address in your current country of residence"
    )
}

val infoAPBeskjed_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(Bokmal to "Slik gir du oss beskjed", Nynorsk to "Slik gir du oss beskjed", English to "How to contact us")
    }
    paragraph {
        text(
            Bokmal to "Du kan logge deg inn på Din Pensjon og velge «Kontakt NAV om pensjon», eller på nav.no/beskjedtilnav og velge «Send beskjed til NAV». Du kan også sende beskjed til oss i posten. Adressen finner du på nav.no/ettersendelse.",
            Nynorsk to "Du kan logge deg inn på Din Pensjon og velje «Kontakt NAV om pensjon», eller på nav.no/beskjedtilnav og velje «Send beskjed til NAV». Du kan også gi melding til oss i posten. Adressa finner du på nav.no/ettersendelse.",
            English to "You can contact us by logging in to your personal Din Pensjon pension page and selecting “Kontakt NAV”, or by logging in to nav.no/beskjedtilnav and selecting “Send beskjed til NAV”. You can also contact us by post. You can find the address at nav.no/ettersendelser."
        )
    }
}

val vedleggVeiledning_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Veiledning fra NAV - forvaltningsloven § 11",
            Nynorsk to "Rettleiing frå NAV - forvaltningslova § 11",
            English to "Guidance from NAV - Section 11 of the Public Administration Act"
        )
    }
    paragraph {
        text(
            Bokmal to "Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre vårt beste for å hjelpe deg.",
            Nynorsk to "Vi har plikt til å rettleie deg om rettane og pliktene dine i saka di, både før, under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, vil vi gjere vårt beste for å hjelpe deg.",
            English to "We have a duty to inform you of your rights and obligations in connection with your case, both before, during and after the administrative process. If you have any questions or are uncertain about something, we will do our best to help you."
        )
    }
}


val vedleggInnsynSakPensjon_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    val telefonNummer = felles().select(Felles::avsenderEnhet).select(NAVEnhet::telefonnummer)
    val kontaktinformasjonNettsted = felles().select(Felles::avsenderEnhet).select(NAVEnhet::nettside)
    title1 {
        text(
            Bokmal to "Innsyn i saken din - forvaltningsloven § 18",
            Nynorsk to "Innsyn i saka di - forvaltningslova § 18",
            English to "Access to your case - Section 18 of the Public Administration Act"
        )
    }
    paragraph {
        textExpr(
            Bokmal to "Med få unntak har du rett til å se dokumentene i saken din. Du kan logge deg inn på ".expr() + kontaktinformasjonNettsted + " for å se all kommunikasjon som har vært mellom deg og NAV i saken din. Du kan også ringe oss på telefon ".expr() + telefonNummer.format() + ".".expr(),
            Nynorsk to "Med få unntak har du rett til å sjå dokumenta i saka di. Du kan logge deg inn på ".expr() + kontaktinformasjonNettsted + " for å sjå all kommunikasjon som har vore mellom deg og NAV i saka di. Du kan også ringje oss på telefon ".expr() + telefonNummer.format() + ".".expr(),
            English to "With some exceptions, you are entitled to access all the documents relating to your case. Log on to ".expr() + kontaktinformasjonNettsted + " to review the communication between you and NAV in connection with your case. You can also call us at tel.: ".expr() + telefonNummer.format() + ".".expr()
        )
    }
}


val vedleggInnsynSakUTPesys_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    val telefonNummer = felles().select(Felles::avsenderEnhet).select(NAVEnhet::telefonnummer)
    title1 {
        text(
            Bokmal to "Innsyn i saken din - forvaltningsloven § 18",
            Nynorsk to "Innsyn i saka di - forvaltningslova § 18",
            English to "Access to your case - Section 18 of the Public Administration Act"
        )
    }
    paragraph {
        textExpr(
            Bokmal to "Med få unntak har du rett til å se dokumentene i saken din. Du kan se noen dokumenter på nav.no/dittnav. Du kan også ringe oss på telefon ".expr() + telefonNummer.format() + ".".expr(),
            Nynorsk to "Med få unntak har du rett til å sjå dokumenta i saka di. Du kan sjå nokre dokument på nav.no/dittnav. Du kan også ringe oss på telefon ".expr() + telefonNummer.format() + ".".expr(),
            English to "With some exceptions, you are entitled to access all the documents pertaining to your case. You can read some of the documents at nav.no/dittnav. You can also call us at tel.: ".expr() + telefonNummer.format() + ".".expr()
        )
    }
}

val vedleggHjelpFraAndre_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Hjelp fra andre - forvaltningsloven § 12",
            Nynorsk to "Hjelp frå andre - forvaltningslova § 12",
            English to "Assistance from a third party - Section 12 of the Public Administration Act"
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne skjemaet du finner på nav.no/fullmakt.",
            Nynorsk to "Du kan be om hjelp frå andre under heile saksbehandlinga, for eksempel av advokat, rettshjelpar, ein organisasjon du er medlem av, eller ein annan myndig person. Dersom den som hjelper deg, ikkje er advokat, må du gi denne personen ei skriftleg fullmakt. Bruk gjerne skjemaet du finn på nav.no/fullmakt.",
            English to "You can use the assistance of a third party throughout the administrative process, e.g. from a lawyer, legal services provider, an organisation of which you are a member, or another competent person. If the person assisting you is not a lawyer, you must issue a written power of attorney authorising them to help you. You can use the form found on nav.no/fullmakt."
        )
    }
}


val vedleggKlagePensjon_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    val telefonNummer = felles().select(Felles::avsenderEnhet).select(NAVEnhet::telefonnummer)
    title1 {
        text(
            Bokmal to "Klage på vedtaket - folketrygdloven § 21-12",
            Nynorsk to "Klage på vedtaket - folketrygdlova § 21-12",
            English to "Appealing a decision - Section 21-12 of the National Insurance Act"
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket vil da vurdere saken din på nytt.",
            Nynorsk to "Du kan klage på vedtaket innan seks veker frå du fekk det. Kontoret som har gjort vedtaket, vurderer då saka di på nytt.",
            English to "You may appeal the decision within six weeks of receiving it. The department that made the decision will then review your case."
        )
    }
    paragraph {
        text(
            Bokmal to "Hvis du ikke får gjennomslag for klagen din, blir den sendt videre til NAV Klageinstans for ny vurdering og avgjørelse. Dersom du heller ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten.",
            Nynorsk to "Dersom du ikkje får gjennomslag for klaga di, blir ho send vidare til NAV Klageinstans for ny vurdering og avgjerd. Dersom du heller ikkje får gjennomslag hos klageinstansen, kan du anke saka inn for Trygderetten.",
            English to "If your complaint is declined, it will be forwarded to NAV Appeals for a new review and decision. If this review is also unsuccessful, you may appeal to The National Insurance Court."
        )
    }
    paragraph {
        textExpr(
            Bokmal to "Klagen må være skriftlig og inneholde navn, fødselsnummer og adresse. Bruk gjerne skjemaet som du finner på nav.no/klage. Trenger du hjelp, er du velkommen til å ringe oss på telefon ".expr() + telefonNummer.format() + ".".expr(),
            Nynorsk to "Klaga må vere skriftleg og innehalde namn, fødselsnummer og adresse. Bruk gjerne skjemaet som du finn på nav.no/klage. Treng du hjelp, er du velkomen til å ringje oss på telefon ".expr() + telefonNummer.format() + ".".expr(),
            English to "Your appeal must be made in writing and include your name, national identity number and address. Feel free to use the form found at nav.no/klage. Should you need assistance in writing the appeal, please call us at tel.: ".expr() + telefonNummer.format() + ".".expr()
        )
    }
    paragraph {
        text(Bokmal to "Du må skrive", Nynorsk to "Du må skrive", English to "You must specify")
        list {
            item {
                text(
                    Bokmal to "hvilket vedtak du klager på",
                    Nynorsk to "kva vedtak du klagar på",
                    English to "which decision you are appealing"
                )
                text(
                    Bokmal to "hvilken endring i vedtaket du ber om",
                    Nynorsk to "kva endring i vedtaket du ber om",
                    English to "how you believe the decision should be amended"
                )
            }
        }
    }
    paragraph {
        text(Bokmal to "Du bør også", Nynorsk to "Du bør også", English to "You should also")
        list {
            item {
                text(
                    Bokmal to "skrive hvorfor du mener vedtaket er feil",
                    Nynorsk to "skrive kvifor du meiner vedtaket er feil",
                    English to "specify why you believe the decision is wrong"
                )
                text(
                    Bokmal to "nevne erklæringer og andre dokumenter som du legger ved klagen",
                    Nynorsk to "nemne erklæringar og andre dokument som du legg ved klaga",
                    English to "list statements and other documents attached to the appeal"
                )
            }
        }
    }

    paragraph {
        text(
            Bokmal to "Husk å undertegne klagen, ellers må vi sende den i retur til deg.",
            Nynorsk to "Hugs å skrive under klaga, elles må vi sende henne i retur til deg.",
            English to "Please remember to sign the appeal, otherwise it will be returned to you."
        )
    }
    paragraph {
        text(
            Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos fylkesmannen, advokater eller NAV.",
            Nynorsk to "Får du medhald, kan du få dekt vesentlege utgifter som har vore nødvendige for å få endra vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelplova. Informasjon om denne ordninga kan du få hos fylkesmannen, advokatar eller NAV.",
            English to "If your appeal is successful, you may be eligible for compensation for costs incurred to have the decision overturned. You may also be eligible for free legal aid, pursuant to the Legal Aid Act. Information about the legal aid scheme can be obtained from the county governor, lawyers or NAV."
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan lese om saksomkostninger i forvaltningsloven § 36.",
            Nynorsk to "Du kan lese om saksomkostningar i forvaltningslova § 36.",
            English to "Read more about costs of action in Section 36 of the Public Administration Act."
        )
    }
}

val vedleggKlagePesys_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Klage på vedtaket - folketrygdloven § 21-12",
            Nynorsk to "Klage på vedtaket - folketrygdlova § 21-12",
            English to "Appealing a decision - Section 21-12 of the National Insurance Act"
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt.",
            Nynorsk to "Du kan klage på vedtaket innan seks veker frå du fekk det. Kontoret som har gjort vedtaket, vurderer då saka di på nytt.",
            English to "You may appeal the decision within six weeks of receiving it. The NAV office that made the decision will reconsider your case."
        )
    }
    paragraph {
        text(
            Bokmal to "Klagen må være skriftlig og inneholde",
            Nynorsk to "Klaga må vere skriftleg og innehalde",
            English to "Your appeal must be made in writing and include"
        )
    }
    paragraph {
        list {
            item {
                text(
                    Bokmal to "navn, fødselsnummer og adresse",
                    Nynorsk to "namn, fødselsnummer og adresse",
                    English to "name, national identity number and address"
                )
                text(
                    Bokmal to "hvilket vedtak du klager på",
                    Nynorsk to "kva vedtak du klagar på",
                    English to "which decision you are appealing"
                )
                text(
                    Bokmal to "hvilken endring i vedtaket du ber om",
                    Nynorsk to "kva endring i vedtaket du ber om",
                    English to "how you believe the decision should be amended"
                )
                text(Bokmal to "din underskrift", Nynorsk to "di underskrift", English to "your signature")
            }
        }
    }
    paragraph {
        text(Bokmal to "Du bør også", Nynorsk to "Du bør også", English to "Your appeal should also spesify")
    }
    paragraph {
        list {
            item {
                text(
                    Bokmal to "skrive hvorfor du mener vedtaket er feil",
                    Nynorsk to "skrive kvifor du meiner vedtaket er feil",
                    English to "why you believe the decision is wrong"
                )
                text(
                    Bokmal to "nødvendige erklæringer og andre dokumenter",
                    Nynorsk to "nemne erklæringar og andre dokument som du legg ved klaga",
                    English to "a list of statements and other documents attached to the appeal"
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Bruk gjerne skjemaet du finner på nav.no/klage. NAV kan hjelpe deg med å skrive klagen.",
            Nynorsk to "Bruk gjerne skjemaet som du finn på nav.no/klage. NAV kan hjelpe deg med å skrive klaga.",
            English to "You can use the form found on nav.no/klage. NAV can assist you in writing the appeal."
        )
    }
    paragraph {
        text(
            Bokmal to "Hvis du ikke får gjennomslag for klagen din, blir den sendt videre til NAV Klageinstans for ny vurdering og avgjørelse. Dersom du heller ikke får gjennomslag hos klageinstansen, kan du anke saken inn for Trygderetten.",
            Nynorsk to "Dersom du ikkje får gjennomslag for klaga di, blir ho send vidare til NAV Klageinstans for ny vurdering og avgjerd. Dersom du heller ikkje får gjennomslag hos klageinstansen, kan du anke saka inn for Trygderetten.",
            English to "If your appeal is not successful, it will be sent to NAV Klageinstans for a new evaluation and final decision. If then you are not successful with your appeal, you can appeal the case in The National Insurance Court."
        )
    }
    paragraph {
        text(
            Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos fylkesmannen, advokater eller NAV.",
            Nynorsk to "Får du medhald, kan du få dekt vesentlege utgifter som har vore nødvendige for å få endra vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelplova. Informasjon om denne ordninga kan du få hos fylkesmannen, advokatar eller NAV.",
            English to "If your appeal is successful, you may be eligible for compensation for the costs incurred to have the decision overturned. You may also be eligible for free legal aid, pursuant to the Legal Aid Act. Information about the legal aid scheme can be obtained from the county governor, lawyers of NAV."
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan lese om saksomkostninger i forvaltningsloven § 36.",
            Nynorsk to "Du kan lese om saksomkostningar i forvaltningslova § 36.",
            English to "You can read more about the costs of appeal in section 36 of the Public Administration Act."
        )
    }
}
