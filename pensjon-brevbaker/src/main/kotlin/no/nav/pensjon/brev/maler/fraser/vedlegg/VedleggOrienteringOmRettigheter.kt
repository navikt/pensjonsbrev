package no.nav.pensjon.brev.maler.fraser.vedlegg

import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.maler.fraser.common.Constants.BESKJED_TIL_NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.ETTERSENDELSE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.FULLMAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.telefonnummer


// VedleggPlikter_001, VedleggPlikterUT_001
object VedleggPlikter : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Plikt til å opplyse om endringer - folketrygdloven § 21-3",
                Nynorsk to "Plikt til å opplyse om endringar - folketrygdlova § 21-3",
                English to "Duty to inform of changes - Section 21-3 of the National Insurance Act"
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
}

//VedleggPlikterAP2_001
object VedleggPlikterAP2 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du skal oppholde deg utenfor Norge i en lengre periode eller skal flytte til et annet land",
            Nynorsk to "du skal opphalde deg utanfor Noreg i ein lengre periode eller skal flytte til eit anna land",
            English to "you intend to stay in a foreign country for an extended period of time or intend to move to another country"
        )
}

// VedleggPlikterAP3_001
object VedleggPlikterAP3 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du flytter til et annet land, tilbake til Norge eller du endrer adresse i ditt nåværende bostedsland",
            Nynorsk to "du flyttar til eit anna land, tilbake til Noreg eller du endrar adresse i landet kor du bur no",
            English to "you move to another country, move back to Norway or if you change address in your country of residence"
        )
}

// VedleggPlikterAP1_001
object VedleggPlikterAP1 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du gifter deg eller inngår samboerskap",
            Nynorsk to "du giftar deg eller inngår sambuarskap",
            English to "you marry or get a cohabiting partner"
        )
}

object VedleggPlikterAP4_002 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for ektefellen din",
            Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til ektefellen din endrar seg",
            English to "the employment income, pension, disability benefit or investment income changes for your spouse"
        )
}

object VedleggPlikterAP13_002 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for partneren din",
            Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til partnaren din endrar seg",
            English to "the employment income, pension, disability benefit or investment income changes for your partner"
        )
}

object VedleggPlikterAP15_002 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "arbeidsinntekten, pensjonsinntekten, uføreinntekten eller kapitalinntekten endrer seg for samboeren din",
            Nynorsk to "arbeidsinntekta, pensjonsinntekta, uføreinntekta eller kapitalinntekta til sambuaren din endrar seg",
            English to "the employment income, pension, disability benefit or investment income changes for your cohabiting partner"
        )
}

//vedleggPlikterAP6_002, vedleggPlikterAP14_002, vedleggPlikterAP18_001
data class VedleggPlikterAPFlytterFraHverandre(val sivilstand: Expression<Sivilstand>) :
    TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "du og ",
            Nynorsk to "du og ",
            English to "you and your "
        )
        showIf(sivilstand.isOneOf(Sivilstand.GIFT)) {
            text(Bokmal to "ektefellen", Nynorsk to "ektefellen", English to "spouse")
        }.orShowIf(sivilstand.isOneOf(Sivilstand.PARTNER)) {
            text(Bokmal to "partneren", Nynorsk to "partnaren", English to "partner")
        }.orShowIf(sivilstand.isOneOf(Sivilstand.SAMBOER1_5, Sivilstand.SAMBOER3_2)) {
            text(Bokmal to "samboeren", Nynorsk to "sambuaren", English to "cohabiting partner")
        }
        text(
            Bokmal to " din flytter fra hverandre",
            Nynorsk to " din flyttar frå kvarandre",
            English to " separate"
        )
    }
}

// VedleggPlikterAP16_001
object VedleggPlikterAP16 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(Bokmal to "du gifter deg", Nynorsk to "du giftar deg", English to "you marry")
}

// VedleggPlikterAP17_001
object VedleggPlikterAP17 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du får barn med samboeren din",
            Nynorsk to "du får barn med sambuaren din",
            English to "you and your cohabiting partner have a child together"
        )
}

object VedleggPlikterEndretSivilstatus : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "sivilstanden din endrer seg",
            Nynorsk to "sivilstanden din endrar seg",
            English to "your civil status changes",
        )
    }
}

// VedleggPlikterAP19_001
object VedleggPlikterAP19 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(Bokmal to "samboeren din dør", Nynorsk to "sambuaren din døyr", English to "your cohabiting partner dies")
}

// VedleggPlikterAP8_001
object VedleggPlikterAP8 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du og ektefellen din flytter sammen igjen",
            Nynorsk to "du og ektefellen din flyttar saman igjen",
            English to "you and your spouse move back together"
        )
}

//VedleggPlikterAP11_001
object VedleggPlikterAP11 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du og partneren din flytter sammen igjen",
            Nynorsk to "du og partnaren din flyttar saman igjen",
            English to "you and your partner move back together"
        )
}

//VedleggPlikterAP9_001
object VedleggPlikterAP9 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(Bokmal to "du blir skilt", Nynorsk to "du blir skild", English to "you divorce")
}

//VedleggPlikterAP7_001
object VedleggPlikterAP7 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(Bokmal to "ektefellen din dør", Nynorsk to "ektefellen din døyr", English to "your spouse dies")
}

//VedleggPlikterAP12_001
object VedleggPlikterAP12 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(Bokmal to "partneren din dør", Nynorsk to "partnaren din døyr", English to "your partner dies")
}

//VedleggPlikterAP10_001
object VedleggPlikterAP10 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du får ny samboer",
            Nynorsk to "du får ny sambuar",
            English to "you get a new cohabiting partner"
        )
}

// VedleggPlikterAP5_001
data class VedleggPlikterAP5(val sivilstand: Expression<Sivilstand>) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
}

// VedleggPlikterAP26_001
object VedleggPlikterAP26 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du får et varig opphold i institusjon",
            Nynorsk to "du blir innlagd på institusjon",
            English to "you get permanent residency in an institution"
        )
}

// VedleggPlikterAP27_001
object VedleggPlikterAP27 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du sitter i varetekt, soner straff eller er under forvaring",
            Nynorsk to "du sit i varetekt, sonar straff eller er under forvaring",
            English to "you are held in detention, incarcerated or in custody"
        )
}

// VedleggPlikterHvorforMeldeAP_001
object VedleggPlikterHvorforMeldeAlderspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Skjer det endringer, kan det få betydning for hvor mye du kan få utbetalt i alderspensjon. Derfor er det viktig at du gir oss beskjed så raskt som mulig.",
                Nynorsk to "Skjer det endringar, kan det få betydning for kor mykje du kan få utbetalt i alderspensjon. Derfor er det viktig at du gir oss beskjed så raskt som mogleg.",
                English to "To make sure you get the right amount of retirement pension, you need to report any changes in your circumstances that can influence the assessment of the supplement you receive. It is important that you notify any change to us as soon as possible."
            )
        }
}

// VedleggPlikterRettTilBarnetilleggAP_001
data class VedleggPlikterRettTilBarnetilleggAP(
    val harTilleggForFlereBarn: Expression<Boolean>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val barnFlertall = harTilleggForFlereBarn
        paragraph {
            text(
                Bokmal to "Fordi du får barnetillegg må du også melde fra om endringer som kan ha betydning for dette tillegget.",
                Nynorsk to "Fordi du får barnetillegg må du også melde frå om endringar som kan ha betydning for dette tillegget.",
                English to "Because you receive child supplement, you must notify us if there are any changes in circumstances that can have an influence on the assessment of the supplement."
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Du må gi oss beskjed hvis ".expr() + ifElse(
                    barnFlertall,
                    "barna",
                    "barnet"
                ) + " du forsørger".expr(),
                Nynorsk to "Du må gi oss beskjed om ".expr() + ifElse(
                    barnFlertall,
                    "barna",
                    "barnet"
                ) + " du forsørgjer".expr(),
                English to "You must notify us if the ".expr() + ifElse(
                    barnFlertall,
                    "children",
                    "child"
                ) + " you provide for will".expr()
            )
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
    }
}

// VedleggPlikterRettTilEktefelletilleggAP_001
data class VedleggPlikterRettTilEktefelletilleggAP(
    val sivilstand: Expression<Sivilstand>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Fordi du får ektefelletillegg må du også melde fra om endringer som kan ha betydning for dette tillegget.",
                Nynorsk to "Fordi du får ektefelletillegg må du også melde frå om endringar som kan ha betydning for dette tillegget.",
                English to "Because you receive spouse supplement, you must notify us if there are any changes in circumstances that can have an influence on the assessment of the supplement.",
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Du må gi oss beskjed hvis ".expr() + sivilstand.bestemtForm() + " du forsørger",
                Nynorsk to "Du må gi oss beskjed hvis ".expr() + sivilstand.bestemtForm() + " du forsørgjar",
                English to "You must notify us if the ".expr() + sivilstand.bestemtForm() + " you provide for will",
            )
            list {
                item {
                    text(
                        Bokmal to "får egen inntekt som er mer enn folketrygden grunnbeløp",
                        Nynorsk to "får eiga inntekt som er meir enn grunnbeløpet i folketrygda",
                        English to "earn an income exceeding the National Insurance basic amount",
                    )
                    text(
                        Bokmal to "skal flytte til et annet land",
                        Nynorsk to "skal flytte til eit anna land",
                        English to "move to another country",
                    )
                    text(
                        Bokmal to "skal oppholde seg i et annet land i mer enn 90 dager i løpet av en tolvmånedersperiode",
                        Nynorsk to "skal opphalde seg i eit anna land i meir enn 90 dager i løpet av ein tolv månedars periode",
                        English to "stay in another country for more than 90 days in a 12 month period",
                    )
                }
            }

        }
    }
}

// VedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP_001
data class VedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP(
    val sivilstand: Expression<Sivilstand>,
    val harTilleggForFlereBarn: Expression<Boolean>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val barnFlertall = harTilleggForFlereBarn
        paragraph {
            text(
                Bokmal to "Fordi du får ektefelletillegg og barnetillegg må du også melde fra om endringer som kan ha betydning for disse tilleggene.",
                Nynorsk to "Fordi du får ektefelletillegg og barnetillegg må du også melde frå om endringar som kan ha betydning for desse tillegga.",
                English to "Because you receive both spouse supplement and child supplement, you must notify us if there are any changes in circumstances that can have an influence on the assessment of these supplements."
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Du må gi oss beskjed hvis ".expr() +
                        ifElse(barnFlertall, "barna", "barnet") + " eller " + sivilstand.bestemtForm() +
                        " du forsørger".expr(),
                Nynorsk to "Du må gi oss beskjed om ".expr() +
                        ifElse(barnFlertall, "barna", "barnet") + " eller " + sivilstand.bestemtForm() +
                        " du forsørgjer".expr(),
                English to "You must notify us if the ".expr() +
                        ifElse(barnFlertall, "children", "child") + " or " + sivilstand.bestemtForm() +
                        " you provide for will".expr()
            )
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
    }
}

object InntektsProevingPliktListe : ParagraphPhrase<LangBokmalNynorskEnglish>() {
    override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
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

// VedleggPlikterinntektsprovingBTFellesBarnSaerkullsbarnAP_001
object VedleggPlikterinntektsprovingBTFellesBarnSaerkullsbarnAP :
    OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Hvor mye du får utbetalt i barnetillegg avhenger av den samlede inntekten du og ektefellen har. Du må derfor også gi beskjed hvis",
                Nynorsk to "Kor mykje du får utbetalt i barnetillegg er avhengig av den samla inntekta du og ektefellen har. Du må derfor også gi beskjed om",
                English to "How much you receive in child supplement depends on the total combined income of you and your spouse. You must therefore also notify us if your"
            )
            includePhrase(InntektsProevingPliktListe)
        }
}

// VedleggPlikterinntektsprovingBTOgETAP_001
object VedleggPlikterinntektsprovingBTOgETAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Hvor mye du får utbetalt i barnetillegg og ektefelletillegg avhenger av den samlede inntekten du har. Du må derfor også gi beskjed hvis",
                Nynorsk to "Kor mykje du får utbetalt i barnetillegg og ektefelletillegg er avhengig av den samla inntekta du har. Du må derfor også gi beskjed om",
                English to "How much you receive in child and spouse supplements depends on your total income. You must therefore notify us if your"
            )
            includePhrase(InntektsProevingPliktListe)
        }
}

// VedleggPlikterinntektsprovingETAP_001
object VedleggPlikterinntektsprovingETAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Hvor mye du får utbetalt i ektefelletillegg avhenger av den samlede inntekten du har. Du må derfor også gi beskjed hvis",
                Nynorsk to "Kor mykje du får utbetalt i ektefelletillegg avhenger av den samla inntekta du har. Du må derfor også gi beskjed om",
                English to "How much you receive in spouse supplement depends on your total income. You must therefore notify us if your"
            )
            includePhrase(InntektsProevingPliktListe)
        }
}

// VedleggPlikterUT_001
object VedleggPlikterUT1 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "inntekten din endrer seg. Du kan informere NAV om endret inntekt ved å bruke selvbetjeningsløsningen på $NAV_URL",
            Nynorsk to "inntekta di endrar seg. Du kan informere NAV om endra inntekt ved å bruke sjølvbeteningsløysninga på $NAV_URL",
            English to "your income changes. You can notify NAV of changes in your income by using the online service at $NAV_URL"
        )
}

// VedleggPlikterUT2_001
object VedleggPlikterUT2 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(Bokmal to "du endrer adresse", Nynorsk to "du endrar adresse", English to "you change address")
}

// VedleggPlikterUT3_001
object VedleggPlikterUT3 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du skal begynne å arbeide i utlandet",
            Nynorsk to "du skal begynne å arbeide i utlandet",
            English to "you will start working abroad"
        )
}

// VedleggPlikterUT4_001
object VedleggPlikterUT4 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du skal oppholde deg utenfor Norge lengre enn seks måneder",
            Nynorsk to "du skal opphalde deg utanfor Noreg lengre enn seks månader",
            English to "you intend to stay outside Norway for more than six months"
        )
}

// VedleggPlikterUT5_001
object VedleggPlikterUT5 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du skal flytte til et annet land",
            Nynorsk to "du flyttar til eit anna land",
            English to "you are moving to another country"
        )
}

object VedleggPlikterEndretInntektBarnetillegg : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            Bokmal to "din ektefelle, partner eller samboers inntekt endrer seg og du har barnetillegg for felles barn",
            Nynorsk to "inntekta til ektefellen, partnaren eller sambuaren din endrar seg, og du har barnetillegg for felles barn",
            English to "the income of your spouse, partner or cohabitant changes, and you are receiving a child supplement for your joint child(ren)",
        )
    }
}

// VedleggPlikterUT7_001
data class VedleggPlikterUT7(
    val harTilleggForFlereBarn: Expression<Boolean>
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val barnFlertall = harTilleggForFlereBarn
        textExpr(
            Bokmal to ifElse(barnFlertall, "barna", "barnet") +
                    " du forsørger får en inntekt over folketrygdens grunnbeløp, eller det skjer endringer i omsorgsituasjonen".expr(),
            Nynorsk to ifElse(barnFlertall, "barna", "barnet") +
                    " du forsørgjer får ei samla inntekt over grunnbeløpet i folketrygda, eller det skjer endringar av omsorgsituasjonen".expr(),
            English to ifElse(barnFlertall, "children in your care earn", "the child in your care earns") +
                    " an income exceeding the National Insurance basic amount or there are changes in the care situation".expr()
        )
    }
}

// VedleggPlikterUT8_001
object VedleggPlikterUT8 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "tjenestepensjon fra offentlig eller private ordninger endrer seg",
            Nynorsk to "tenestepensjon frå offentlege eller private ordningar endrar seg",
            English to "your occupational pension from public or private schemes changes"
        )
}

// VedleggPlikterUT9_001
object VedleggPlikterUT9 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "individuelle pensjonsordninger, livrente og gavepensjon endrer seg",
            Nynorsk to "individuelle pensjonsordningar, livrente og gåvepensjon endrar seg",
            English to "your individual pension scheme, annuity or gratuity pension changes"
        )
}

// VedleggPlikterUT10_001
object VedleggPlikterUT10 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "ytelser og pensjon fra andre land endrer seg",
            Nynorsk to "ytingar og pensjon frå andre land endrar seg",
            English to "benefits and pensions from other countries change"
        )
}

// VedleggPlikterUT11_001
object VedleggPlikterUT11 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du blir innlagt på institusjon",
            Nynorsk to "du blir innlagd på institusjon",
            English to "you are admitted to an institution"
        )
}

// VedleggPlikterUT12_001
object VedleggPlikterUT12 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du sitter i varetekt, soner straff eller er under forvaring",
            Nynorsk to "du sit i varetekt, sonar straff eller er under forvaring",
            English to "you are remanded in custody or serving time in prison or preventive custody"
        )
}

// VedleggPlikterUT13_001
data class VedleggPlikterUT13(
    val harTilleggForFlereBarn: Expression<Boolean>
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val barnFlertall = harTilleggForFlereBarn
        textExpr(
            Bokmal to ifElse(
                barnFlertall,
                "barna",
                "barnet"
            ) + " du forsørger skal flytte til et annet land".expr(),
            Nynorsk to ifElse(
                barnFlertall,
                "barna",
                "barnet"
            ) + " du forsørgjer skal flytte til eit anna land".expr(),
            English to ifElse(
                barnFlertall,
                "children in your care move",
                "the child in your care moves"
            ) + " to another country".expr()
        )
    }
}

// VedleggPlikterUT14_001
data class VedleggPlikterUT14(
    val harTilleggForFlereBarn: Expression<Boolean>
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val barnFlertall = harTilleggForFlereBarn
        textExpr(
            Bokmal to ifElse(
                barnFlertall,
                "barna",
                "barnet"
            ) + " du forsørger skal oppholde seg i et annet land mer enn 90 dager i løpet av en tolvmånedersperiode".expr(),
            Nynorsk to ifElse(
                barnFlertall,
                "barna",
                "barnet"
            ) + " du forsørgjer skal opphalde seg i eit anna land i meir enn 90 dagar i løpet av ein tolv månedars periode".expr(),
            English to ifElse(
                barnFlertall,
                "children in your care stay",
                "the child in your care stays"
            ) + " in another country for more than 90 days in a 12 month period".expr()
        )
    }
}

// VedleggPlikterAFP_001
object VedleggPlikterAFP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
}

// VedleggPlikterAFP1_001
object VedleggPlikterAFP1 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "inntekten din endrer seg",
            Nynorsk to "inntekta di endrar seg",
            English to "your income changes"
        )
}

// VedleggPlikterAFP2_001
object VedleggPlikterAFP2 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du gifter deg eller inngår samboerskap",
            Nynorsk to "du giftar deg eller inngår sambuarskap",
            English to "you get married or get a cohabitant"
        )
}

// VedleggPlikterAFP3_001
object VedleggPlikterAFP3 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du skal oppholde deg utenfor Norge i en lengre periode eller skal flytte til et annet land",
            Nynorsk to "du skal opphalde deg utanfor Noreg i ein lengre periode eller skal flytte til eit anna land",
            English to "you intend to stay outside Norway for a long period or intend to move to another country"
        )
}

// VedleggPlikterAFP4_001
object VedleggPlikterAFP4 : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        text(
            Bokmal to "du flytter til et annet land, tilbake til Norge eller du endrer adresse i ditt nåværende bostedsland",
            Nynorsk to "du flyttar til eit anna land, tilbake til Noreg eller du endrar adresse i landet kor du bur no",
            English to "you move to another country, move back to Norway, or you change address in your current country of residence"
        )
}

// InfoAPBeskjed_001
object InfoAlderspensjonGiBeskjed : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Slik gir du oss beskjed",
                Nynorsk to "Slik gir du oss beskjed",
                English to "How to contact us"
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan logge deg inn på Din Pensjon og velge «Kontakt NAV om pensjon», eller på $BESKJED_TIL_NAV_URL og velge «Send beskjed til NAV». Du kan også sende beskjed til oss i posten. Adressen finner du på $ETTERSENDELSE_URL.",
                Nynorsk to "Du kan logge deg inn på Din Pensjon og velje «Kontakt NAV om pensjon», eller på $BESKJED_TIL_NAV_URL og velje «Send beskjed til NAV». Du kan også gi melding til oss i posten. Adressa finner du på $ETTERSENDELSE_URL.",
                English to "You can contact us by logging in to your personal Din Pensjon pension page and selecting “Kontakt NAV”, or by logging in to $BESKJED_TIL_NAV_URL and selecting “Send beskjed til NAV”. You can also contact us by post. You can find the address at $ETTERSENDELSE_URL."
            )
        }
    }
}

// VedleggVeiledning_001
object VedleggVeiledning : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
}


// VedleggInnsynSakPensjon_001
object VedleggInnsynSakPensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val telefonNummer = felles.avsenderEnhet.telefonnummer
        title1 {
            text(
                Bokmal to "Innsyn i saken din - forvaltningsloven § 18",
                Nynorsk to "Innsyn i saka di - forvaltningslova § 18",
                English to "Access to your case - Section 18 of the Public Administration Act"
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Med få unntak har du rett til å se dokumentene i saken din. Du kan logge deg inn på $NAV_URL for å se all kommunikasjon som har vært mellom deg og NAV i saken din. Du kan også ringe oss på telefon ".expr() + telefonNummer.format() + ".".expr(),
                Nynorsk to "Med få unntak har du rett til å sjå dokumenta i saka di. Du kan logge deg inn på $NAV_URL for å sjå all kommunikasjon som har vore mellom deg og NAV i saka di. Du kan også ringje oss på telefon ".expr() + telefonNummer.format() + ".".expr(),
                English to "With some exceptions, you are entitled to access all the documents relating to your case. Log on to $NAV_URL to review the communication between you and NAV in connection with your case. You can also call us at tel.: ".expr() + telefonNummer.format() + ".".expr()
            )
        }
    }
}

// VedleggInnsynSakUTPesys_001
object VedleggInnsynSakUfoeretrygdPesys : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Innsyn i saken din - forvaltningsloven § 18",
                Nynorsk to "Innsyn i saka di - forvaltningslova § 18",
                English to "Access to your case - Section 18 of the Public Administration Act"
            )
        }
        paragraph {
            text(
                Bokmal to "Med få unntak har du rett til å se dokumentene i saken din. Du kan logge deg inn via $NAV_URL for å se dokumenter i saken din. Du kan også ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON.",
                Nynorsk to "Med få unntak har du rett til å sjå dokumenta i saka di. Du kan logge deg inn via $NAV_URL for å sjå dokumenter i saka di. Du kan også ringje oss på telefon $NAV_KONTAKTSENTER_TELEFON.",
                English to "With some exceptions, you are entitled to access the documents relating to your case. Log on to $NAV_URL to review the documents in connection with your case. You can also call us at telephone +47 $NAV_KONTAKTSENTER_TELEFON.",
            )
        }
    }
}

// VedleggHjelpFraAndre_001
object VedleggHjelpFraAndre : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Hjelp fra andre - forvaltningsloven § 12",
                Nynorsk to "Hjelp frå andre - forvaltningslova § 12",
                English to "Assistance from a third party - Section 12 of the Public Administration Act"
            )
        }
        paragraph {
            text(
                Bokmal to "Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne skjemaet du finner på $FULLMAKT_URL.",
                Nynorsk to "Du kan be om hjelp frå andre under heile saksbehandlinga, for eksempel av advokat, rettshjelpar, ein organisasjon du er medlem av, eller ein annan myndig person. Dersom den som hjelper deg, ikkje er advokat, må du gi denne personen ei skriftleg fullmakt. Bruk gjerne skjemaet du finn på $FULLMAKT_URL.",
                English to "You can use the assistance of a third party throughout the administrative process, e.g. from a lawyer, legal services provider, an organisation of which you are a member, or another competent person. If the person assisting you is not a lawyer, you must issue a written power of attorney authorising them to help you. You can use the form found on $FULLMAKT_URL."
            )
        }
    }
}

// VedleggKlagePensjon_001
object VedleggKlagePensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val telefonNummer = felles.avsenderEnhet.telefonnummer
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
                Bokmal to "Klagen må være skriftlig og inneholde navn, fødselsnummer og adresse. Bruk gjerne skjemaet som du finner på $KLAGE_URL. Trenger du hjelp, er du velkommen til å ringe oss på telefon ".expr() + telefonNummer.format() + ".".expr(),
                Nynorsk to "Klaga må vere skriftleg og innehalde namn, fødselsnummer og adresse. Bruk gjerne skjemaet som du finn på $KLAGE_URL. Treng du hjelp, er du velkomen til å ringje oss på telefon ".expr() + telefonNummer.format() + ".".expr(),
                English to "Your appeal must be made in writing and include your name, national identity number and address. Feel free to use the form found at $KLAGE_URL. Should you need assistance in writing the appeal, please call us at tel.: ".expr() + telefonNummer.format() + ".".expr()
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
                }
                item {
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
                }
                item {
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
                Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller NAV.",
                Nynorsk to "Får du medhald, kan du få dekt vesentlege utgifter som har vore nødvendige for å få endra vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelplova. Informasjon om denne ordninga kan du få hos statsforvalteren, advokatar eller NAV.",
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
}

// VedleggKlagePesys_001
object VedleggKlagePesys : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
                }
                item {
                    text(
                        Bokmal to "hvilket vedtak du klager på",
                        Nynorsk to "kva vedtak du klagar på",
                        English to "which decision you are appealing"
                    )
                }
                item {
                    text(
                        Bokmal to "hvilken endring i vedtaket du ber om",
                        Nynorsk to "kva endring i vedtaket du ber om",
                        English to "how you believe the decision should be amended"
                    )
                }
                item {
                    text(
                        Bokmal to "din underskrift",
                        Nynorsk to "di underskrift",
                        English to "your signature"
                    )
                }
            }
        }
        paragraph {
            text(
                Bokmal to "Du bør også",
                Nynorsk to "Du bør også",
                English to "Your appeal should also spesify"
            )
        }
        paragraph {
            list {
                item {
                    text(
                        Bokmal to "skrive hvorfor du mener vedtaket er feil",
                        Nynorsk to "skrive kvifor du meiner vedtaket er feil",
                        English to "why you believe the decision is wrong"
                    )
                }
                item {
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
                Bokmal to "Bruk gjerne skjemaet du finner på $KLAGE_URL. NAV kan hjelpe deg med å skrive klagen.",
                Nynorsk to "Bruk gjerne skjemaet som du finn på $KLAGE_URL. NAV kan hjelpe deg med å skrive klaga.",
                English to "You can use the form found on $KLAGE_URL. NAV can assist you in writing the appeal."
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
                Bokmal to "Får du medhold, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelploven. Informasjon om denne ordningen kan du få hos statsforvalteren, advokater eller NAV.",
                Nynorsk to "Får du medhald, kan du få dekt vesentlege utgifter som har vore nødvendige for å få endra vedtaket. Du kan ha krav på fri rettshjelp etter rettshjelplova. Informasjon om denne ordninga kan du få hos Statsforvalteren, advokatar eller NAV.",
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
}


