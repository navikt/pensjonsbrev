package no.nav.pensjon.brev.maler.fraser.alderspensjon


import no.nav.pensjon.brev.api.model.YtelseForAldersovergangKode
import no.nav.pensjon.brev.maler.fraser.common.Constants.ALDERSPENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


// infoGjIkkeInntektsavkorted_002, infoGjInntektsavkortet_001, infoUTAPinnledn_001, infoUTGradertAPinnledn_001, infoUTogAPinnledn_001, infoFamPleierAPinnledn_001, infoAPinnledn_001
data class InnledningInfoYtelse(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>

) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.GJP_FULL)) {
            paragraph {
                text(
                    Bokmal to "Gjenlevendepensjonen din opphører fra og med måneden etter at du fyller 67 år.",
                    Nynorsk to "Attlevandepensjonen din opphøyrar frå og med månaden etter at du fyller 67 år.",
                    English to "Your survivor`s pension will be terminated from the month after you turn 67."
                )
            }
            paragraph {
                text(
                    Bokmal to "Gjenlevendepensjon er en pensjon beregnet med avdødes pensjonsrettigheter. " +
                            "Nav har ikke nok informasjon til å vurdere din rett til alderspensjon. Du må derfor søke om alderspensjon.",
                    Nynorsk to "Attlevandepensjonen er ein pensjon som blir berekna med avdødes pensjonsrettar. " +
                            "Nav har ikkje nok informasjon til å vurdere din rett til alderspensjon. Du må derfor søkje om alderspensjon.",
                    English to "The survivor`s pension is a pension which is based on the deceased's pension rights. " +
                            "Nav does not have sufficient information to assess your eligibility for retirement pension. Therefore, you will need to submit an application."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan søke elektronisk via $DIN_PENSJON_URL. Hvis du ikke kan benytte denne løsningen, så finner du søknadsskjema for alderspensjon på $NAV_URL.",
                    Nynorsk to "Du kan søkje elektronisk via $DIN_PENSJON_URL. Dersom du ikkje kan bruke denne løsninga, så finn du søknadsskjema  for alderspensjon på $NAV_URL.",
                    English to "You can apply electronically by logging on to $DIN_PENSJON_URL, or use the application form for retirement pension, which is also found at $NAV_URL."
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi kan tidligst innvilge søknaden din fra og med måneden etter at du søkte. ",
                    Nynorsk to "Vi kan tidlegast innvilge søknaden din frå og med månaden etter at du søkte.",
                    English to "We can at the earliest grant your application the month after you applied."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan velge å ta ut hele eller deler av alderspensjonen din (20, 40, 50, 60, 80 eller 100 prosent). Du kan også vente med å ta ut alderspensjon.",
                    Nynorsk to "Du kan  velje å ta ut heile eller delar av alderspensjonen din (20, 40, 50, 60, 80 eller 100 prosent). Du kan også vente med å ta ut alderspensjon.",
                    English to "You can start drawing all or a part of your retirement pension at a rate of 20, 40, 50, 60, 80 or 100 percent. You can also postpone drawing your retirement pension."
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.GJP_AVKORT)) {
            paragraph {
                text(
                    Bokmal to "Gjenlevendepensjonen din opphører fra og med måneden etter at du fyller 67 år. Fra samme tidspunkt har alle som hovedregel rett til å ta ut alderspensjon. " +
                            "Dersom du velger å ta ut alderspensjon vil den bli beregnet med dine gjenlevenderettigheter.",
                    Nynorsk to "Attlevandepensjonen din blir avslutta frå og med månaden etter at du fyller 67 år. Frå same tidspunkt har alle som hovudregel rett til å ta ut alderspensjon. " +
                            "Dersom du vel å ta ut alderspensjon vil den bli berekna med dine attlevanderettar.",
                    English to "The month after you turn 67 years of age, your survivor´s pension will cease. " +
                            "As a general rule everyone is entitled to draw retirement pension from the National Insurance Scheme from the month after they reach the age of 67. " +
                            "If you decide to draw retirement pension, your survivor’s rights will be included."
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.UT)) {
            paragraph {
                text(
                    Bokmal to "Uføretrygden din opphører og regnes om til 100 prosent alderspensjon måneden etter at du fyller 67 år. Du trenger ikke søke om dette.",
                    Nynorsk to "Uføretrygda di blir avslutta og rekna om til 100 prosent alderspensjon månaden etter at du fyller 67 år. Du treng ikkje søkje om dette.",
                    English to "The month after you turn 67 years of age, your disability benefit will cease and you will instead receive full retirement pension. " +
                            "You do not need to apply for this."
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.UT_GRAD)) {
            paragraph {
                text(
                    Bokmal to "Uføretrygden din opphører og regnes om til alderspensjon måneden etter at du fyller 67 år. " +
                            "Du får da alderspensjon etter en uttaksgrad som tilsvarer det du får i uføretrygd i dag.",
                    Nynorsk to "Uføretrygda di blir avslutta og rekna om til alderspensjon månaden etter at du fyller 67 år. " +
                            "Du får då alderspensjon etter ein uttaksgrad som svarer til det du får i uføretrygd i dag.",
                    English to "The month after you turn 67 years of age, your disability benefit will cease and you will instead receive retirement pension. " +
                            "Your retirement pension will be paid out at the same percentage rate as your current disability benefit."
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.UT_AP_GRAD)) {
            paragraph {
                text(
                    Bokmal to "Uføretrygden din opphører og regnes om til alderspensjon måneden etter at du fyller 67 år. " +
                            "Du får da alderspensjon etter den uttaksgraden som tilsvarer det du får i uføretrygd og alderspensjon i dag.",
                    Nynorsk to "Uføretrygda di blir avslutta og rekna om til alderspensjon månaden etter at du fyller 67 år. " +
                            "Du får då alderspensjon etter den uttaksgraden som svarer til det du får i uføretrygd og alderspensjon i dag.",
                    English to "The month after you turn 67 years of age, your disability benefit will cease and you will instead receive retirement pension. " +
                            "Your retirement pension will be paid out at the same percentage rate as your current disability benefit and retirement pension in total."
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.FAM_PL)) {
            paragraph {
                text(
                    Bokmal to "Din pensjon som tidligere familiepleier opphører og regnes om til 100 prosent alderspensjon måneden etter at du fyller 67 år. " +
                            "Du trenger ikke søke om dette.",
                    Nynorsk to "Pensjonen din som tidlegare familiepleiar blir avslutta og rekna om til 100 prosent alderspensjon månaden etter at du fyller 67 år. " +
                            "Du treng ikkje å søkje om dette.",
                    English to "The month after you turn 67 years of age, the pension you currently receive as a former family carer will cease and you will instead receive full retirement pension. " +
                            "You do not need to apply for this."
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.INGEN_YT)) {
            paragraph {
                text(
                    Bokmal to "Du har frem til nå ikke tatt ut alderspensjon. " +
                            "Vi ønsker derfor å gjøre deg oppmerksom på at alle som hovedregel har rett til å ta ut alderspensjon fra folketrygden fra og med måneden etter fylte 67 år.",
                    Nynorsk to "Du har fram til no ikkje teke ut alderspensjon. " +
                            "Vi ønskjer derfor å gjere deg merksam på at alle som hovudregel har rett til å ta ut alderspensjon frå folketrygda frå og med månaden etter fylte 67 år",
                    English to "To date you have not drawn any retirement pension. " +
                            "We therefore want to make you aware of the fact that as a general rule everyone is entitled to draw retirement pension from the National Insurance Scheme from the month after they reach the age of 67."
                )
            }
        }
    }
}

// infoAPVelge1_001, infoAPVelge2_001
data class InfoVelgeAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            ytelseForAldersovergangKode.isOneOf(
                YtelseForAldersovergangKode.GJP_AVKORT,
                YtelseForAldersovergangKode.INGEN_YT,
                YtelseForAldersovergangKode.FAM_PL,
                YtelseForAldersovergangKode.UT_GRAD
            )
        ) {
            paragraph {
                text(
                    Bokmal to "Du kan velge",
                    Nynorsk to "Du kan velje",
                    English to "You can select one of the following options:"
                )
                list {
                    item {
                        text(
                            Bokmal to "å vente med å ta ut alderspensjonen",
                            Nynorsk to "å vente med å ta ut alderspensjon",
                            English to "Postpone starting to draw your retirement pension."
                        )
                    }
                    item {
                        text(
                            Bokmal to "å ta ut hele eller deler av pensjonen din (20, 40, 50, 60, 80 eller 100 prosent)",
                            Nynorsk to "å ta ut heile eller delar av pensjonen din (20, 40, 50, 60, 80 eller 100 prosent)",
                            English to "Start drawing all or a part of your pension (at a rate of 20, 40, 50, 60, 80 or 100 percent)."
                        )
                    }
                    showIf(
                        ytelseForAldersovergangKode.isOneOf(
                            YtelseForAldersovergangKode.FAM_PL,
                            YtelseForAldersovergangKode.UT_GRAD
                        )
                    ) {
                        item {
                            text(
                                Bokmal to "å stanse eller endre alderspensjonen på et senere tidspunkt",
                                Nynorsk to "å stanse eller endre alderspensjonen på eit seinare tidspunkt",
                                English to "Stop or change your retirement pension at a later date."
                            )
                        }
                    }
                }
            }
        }
    }
}

// infoAPSoke1_002, infoAPSoke2_002
data class InfoOenskeSokeAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.GJP_AVKORT, YtelseForAldersovergangKode.INGEN_YT)) {
            paragraph {
                text(
                    Bokmal to "Ønsker du å vente med å ta ut alderspensjonen eller en annen uttaksgrad enn hva du vil få etter omregning, må du søke om dette." +
                            "Du kan søke elektronisk via $DIN_PENSJON_URL. Hvis du ikke kan benytte denne løsningen, så finner du søknadsskjema for alderspensjon på $NAV_URL",
                    Nynorsk to "Ønskjer du å vente med å ta ut alderspensjonen, eller ønskjer du ein annan uttaksgrad enn det du får etter omrekninga, må du søkje om dette. " +
                            "Du kan søkje elektronisk via $DIN_PENSJON_URL. Dersom du ikkje kan bruke denne løsninga, så finn du søknadsskjema  for alderspensjon på $NAV_URL",
                    English to "If you do not want to start drawing your retirement pension yet or want to draw your pension at a different rate than that allocated after recalculation, " +
                            "you must submit an application. You can apply electronically by logging on to $DIN_PENSJON_URL, or use the application form for retirement pension, which is also found at $NAV_URL."
                )
            }
            paragraph {
                text(
                    Bokmal to "Ønsker du å endre alderspensjonen, må vi ha mottatt søknaden senest måneden før den måneden du søker endring fra.",
                    Nynorsk to "Ønskjer du å endre alderspensjonen, må vi få søknaden seinast månaden før du ønskjer endringa.",
                    English to "Any change will be implemented at the earliest the month after we have received the application."
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.UT_AP_GRAD)) {
            paragraph {
                text(
                    Bokmal to "Ønsker du en annen uttaksgrad enn hva du vil få etter omregning, må du søke om dette. " +
                            "Mulige uttaksgrader for alderspensjon er 20, 40, 50, 60, 80 eller 100 prosent.",
                    Nynorsk to "Ønskjer du ein annan uttaksgrad enn det du får etter omrekninga, må du søkje om dette. " +
                            "Moglege uttaksgradar for alderspensjon er 20, 40, 50, 60, 80 eller 100 prosent.",
                    English to "If you want to draw your pension at a different rate than that allocated after recalculation, you must submit an application. " +
                            "You can draw your retirement pension at a rate of 20, 40, 50, 60, 80 or 100 percent."
                )
            }
        }
    }
}

// omregningUTAPtilAPEksempel_001
data class InfoOmregningUTtilAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.UT_AP_GRAD)) {
            paragraph {
                text(
                    Bokmal to "Eksempel: Hvis du mottar 60 prosent uføretrygd og 20 prosent alderspensjon, vil dette bli regnet om til 80 prosent alderspensjon. " +
                            "Hvis du ønsker 100 prosent alderspensjon, må du selv endre til dette.",
                    Nynorsk to "Eksempel: Dersom du mottar 60 prosent uføretryd og 20 prosent alderspensjon, vil dette bli regna om til 80 prosent alderspensjon. " +
                            "Dersom du ønskjer 100 prosent alderspensjon, må du sjølv endre til dette.",
                    English to "Example: If you are receiving a 60 percent disability benefit and a 20 percent retirement pension, this will be converted into a 80 percent retirement pension. " +
                            "If you want to draw a 100 percent retirement pension, you must submit an application."
                )
            }
        }
    }
}


// infoAPSivilstand_001
data class InfoSivilstandAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.UT, YtelseForAldersovergangKode.UT_GRAD)) {
            title1 {
                text(
                    Bokmal to "For deg som har ektefelle/partner/samboer",
                    Nynorsk to "For deg som har ektefelle/partnar/sambuar",
                    English to "For people who have a spouse/partner/cohabitant"
                )
            }
            paragraph {
                text(
                    Bokmal to "Din ektefelles, samboers eller partners inntekt har betydning for beregningen av alderspensjon.",
                    Nynorsk to "Inntekta til ektefellen, sambuaren eller partnaren din påverkar berekninga av alderspensjonen.",
                    English to "Your spouse’s/partner’s/cohabitant’s income affects the calculation of your retirement pension."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis ektefelle, partner eller samboer ikke mottar egen pensjon eller uføretrygd, vil vi i de fleste saker ikke ha korrekte inntektsopplysninger. " +
                            "Vi vil derfor legge til grunn en inntekt på to ganger folketrygdens grunnbeløp når alderspensjonen beregnes.",
                    Nynorsk to "Dersom ektefellen, partnaren eller sambuaren din ikkje får eigen pensjon eller eiga uføretrygd, vil vi i de fleste sakene ikkje ha korrekte inntektsopplysningar. " +
                            "Vi vil derfor leggje til grunn ei inntekt på to gonger grunnbeløpet i folketrygda når vi bereknar alderspensjonen.",
                    English to "If your spouse/partner/cohabitant does not receive a separate pension or disability benefit, in most cases we will not have the correct income information. " +
                            "We will therefore use an income of two times the National Insurance basic amount (“G”) as the starting point when calculating retirement pension. ",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du har en ektefelle, partner eller samboer som har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp er det viktig at du kontakter $NAV_URL. " +
                            "Da kan du ha rett på høyere alderspensjon.",
                    Nynorsk to "Dersom du har ein ektefelle, partnar eller sambuar som har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda, er det viktig at du kontaktar $NAV_URL. " +
                            "Då kan du ha rett på høgare alderspensjon.",
                    English to "If you have a spouse/partner/cohabitant who has an income that is lower than two times the National Insurance basic amount, it is important that you contact $NAV_URL, " +
                            "as you may be entitled to a higher retirement pension."
                )
            }
        }
    }
}

// infoAPft_002
data class InfoFTAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.UT, YtelseForAldersovergangKode.UT_GRAD, YtelseForAldersovergangKode.UT_AP_GRAD)) {
            title1 {
                text(
                    Bokmal to "For deg som har forsørgingstillegg",
                    Nynorsk to "Forsørgingstillegg",
                    English to "If you have dependant supplement"
                )
            }
            paragraph {
                text(
                    Bokmal to "Fra 1. januar 2022 er regelverket endret og det blir ikke lenger innvilget nye forsørgingstillegg til alderspensjon. " +
                            "Derfor vil du ikke få ektefelletillegg og/eller barnetillegg i alderspensjonen din.",
                    Nynorsk to "Frå 1. januar 2022 er regelverket endra og det blir ikkje lenger innvilga nye forsørgingstillegg til alderspensjon. " +
                            "Derfor vil du ikkje få ektefelletillegg og/eller barnetillegg i alderspensjonen din.",
                    English to "From 1 January 2022 the regulations change and new dependant supplement will no longer be granted in a retirement pension. " +
                            "Therefore, you will not be granted dependant supplement for your spouse or child in your retirement pension."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon om dette på $ALDERSPENSJON.",
                    Nynorsk to "Du finn meir informasjon om dette på $ALDERSPENSJON.",
                    English to "You will find more information at $ALDERSPENSJON."
                )
            }
        }
    }
}


// infoAPAfpPrivat_001
data class InfoAFPprivatAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.GJP_AVKORT, YtelseForAldersovergangKode.INGEN_YT)) {
            title1 {
                text(
                    Bokmal to "Har du rett til AFP i privat sektor?",
                    Nynorsk to "Har du rett til AFP i privat sektor?",
                    English to "Are you entitled to a contractual pension in the private sector (AFP)?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du er ansatt i en privat virksomhet og har rett til AFP, må du søke om dette samtidig med alderspensjonen fra folketrygden.",
                    Nynorsk to "Dersom du er tilsett i ei privat verksemd og har rett til AFP, må du søkje om dette samtidig med alderspensjonen frå folketrygda.",
                    English to "If you are employed in a private business and are entitled to a contractual early retirement pension (AFP), " +
                            "you must apply for it at the same time as you apply for retirement pension from the National Insurance Scheme."
                )
            }
            paragraph {
                text(
                    Bokmal to "AFP for ansatte i en privat virksomhet blir ikke redusert hvis du har inntekt. " +
                            "Du kan bruke pensjonskalkulatoren i nettjenesten Din pensjon på $DIN_PENSJON_URL for å se hvor mye du får i AFP, sammen med alderspensjonen.",
                    Nynorsk to "AFP for tilsette i ei privat verksemd blir ikkje redusert dersom du har inntekt. " +
                            "Du kan bruke pensjonskalkulatoren i nettenesta Din pensjon på $DIN_PENSJON_URL for å sjå kor mykje du får i AFP, saman med alderspensjonen.",
                    English to "AFP for employees in a private business is not reduced if you have income. " +
                            "You can use the pension calculator at the online pension service at $DIN_PENSJON_URL to see how much AFP you will receive in addition to your retirement pension."
                )
            }
        }
    }
}

// infoAPNorSoke_002, infoAPHvordanSoke_002
data class InfoSoekeAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.isNotAnyOf(YtelseForAldersovergangKode.GJP_FULL, YtelseForAldersovergangKode.UT, YtelseForAldersovergangKode.UT_GRAD)) {
            title1 {
                text(
                    Bokmal to "Når må du søke?",
                    Nynorsk to "Når må du søkje?",
                    English to "When to apply?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi kan tidligst innvilge søknaden din fra og med måneden etter at du søkte. Du bør søke i god tid, men tidligst fire måneder før du ønsker at utbetalingen skal starte.",
                    Nynorsk to "Vi kan tidlegast innvilge søknaden din frå og med månaden etter at du søkte. Du bør derfor søkje i god tid, men tidlegast fire månader før du ønskjer at utbetalinga skal starte.",
                    English to "Retirement pension from the National Insurance Scheme can, at the earliest, be paid out the month after we receive your application. " +
                            "Therefore, you should apply well in advance of the month that you want to start receiving your pension, but at the earliest four months in advance."
                )
            }
            paragraph {
                text(
                    Bokmal to "Eksempel: Hvis du ønsker første utbetaling av alderspensjon i august, kan du tidligst sende søknaden i april.",
                    Nynorsk to "Eksempel: Dersom du ønskjer første utbetaling av alderspensjon i august, kan du tidlegast sende søknaden i april.",
                    English to "Example: If you want to start receiving your retirement pension in August you can at the earliest send your application in April."
                )
            }
            title1 {
                text(
                    Bokmal to "Hvordan søker du om alderspensjon?",
                    Nynorsk to "Korleis søkjer du om alderspensjon?",
                    English to "How do you apply for retirement pension?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan søke elektronisk via $DIN_PENSJON_URL eller bruke søknadsskjema for alderspensjon som du også finner på $NAV_URL.",
                    Nynorsk to "Du kan søkje elektronisk via $DIN_PENSJON_URL eller bruke søknadsskjemaet om alderspensjon som du også finn på $NAV_URL.",
                    English to "You can apply electronically by logging on to $DIN_PENSJON_URL, or use the application form for retirement pension, which is also found at $NAV_URL."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan bruke pensjonskalkulatoren på $DIN_PENSJON_URL til å beregne alderspensjonen din.",
                    Nynorsk to "Du kan bruke pensjonskalkulatoren på $DIN_PENSJON_URL til å berekne alderspensjon din.",
                    English to "You can use the pension calculator at $DIN_PENSJON_URL to calculate your future retirement pension."
                )
            }
        }
    }
}

// infoAPSokeAnnenGrad100_002
data class InfoSoekeAnnenGradAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.UT)) {
            title1 {
                text(
                    Bokmal to "Ønsker du å ta ut en annen grad enn 100 prosent alderspensjon?",
                    Nynorsk to "Ønskjer du å ta ut ein annan grad enn 100 prosent alderspensjon?",
                    English to "Do you want to draw retirement pension at another rate than 100 percent?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi regner om uføretrygden din til 100 prosent alderspensjon. Du kan likevel velge",
                    Nynorsk to "Vi reknar om uføretrygda di til 100 prosent alderspensjon. Du kan likevel velje",
                    English to "Your disability benefit will be converted into a 100 percent retirement pension. You can nevertheless select one of the following options"
                )
                list {
                    item {
                        text(
                            Bokmal to "å ta ut deler av pensjonen din (20, 40 ,50, 60 eller 80 prosent)",
                            Nynorsk to "å ta ut delar av pensjonen din (20, 40, 50, 60 eller 80 prosent)",
                            English to "start drawing a part of your pension at a rate of 20, 40, 50, 60 of 80 percent"
                        )
                    }
                    item {
                        text(
                            Bokmal to "å stanse eller endre pensjonen din på et senere tidspunkt",
                            Nynorsk to "å stanse eller endre pensjonen din på eit seinare tidspunkt",
                            English to "stop or change your retirement pension at a later date"
                        )
                    }
                    item {
                        text(
                            Bokmal to "å vente med å ta ut alderspensjonen",
                            Nynorsk to "å vente med å ta ut alderspensjonen",
                            English to "postpone drawing your retirement pension"
                        )
                    }
                }
            }
            paragraph {
                text(
                    Bokmal to "Hvis du ønsker en annen uttaksgrad enn 100 prosent alderspensjon, kan du søke om dette elektronisk via $DIN_PENSJON_URL. " +
                            "Hvis du ikke kan benytte denne løsningen, finner du søknadsskjema for alderspensjon på $NAV_URL.",
                    Nynorsk to "Dersom du ønskjer ein annan uttaksgrad enn 100 prosent alderspensjon, kan du søkje om dette elektronisk via $DIN_PENSJON_URL. " +
                            "Dersom du ikkje kan bruke denne løsninga, så finn du søknadsskjema  for alderspensjon på $NAV_URL.",
                    English to "If you want to draw retirement pension at another rate than 100 percent, you can apply electronically by logging on to $DIN_PENSJON_URL. " +
                            "Or you can use the application form for retirement pension, which is also found at $NAV_URL."
                )
            }
            paragraph {
                text(
                    Bokmal to "Ønsker du å endre alderspensjonen, må vi få søknaden senest måneden før du ønsker endringen.",
                    Nynorsk to "Ønskjer du å endre alderspensjonen, må vi få søknaden seinast månaden før du ønskjer endringa.",
                    English to "Any change will be implemented at the earliest the month after we have received the application."
                )
            }
        }
    }
}

// skattAPInformasjonsbrev_001
object InfoSkattAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Det er egne skatteregler for alderspensjon",
                Nynorsk to "Det er eigne skattereglar for pensjon",
                English to "Pensions are subject to different tax rules"
            )
        }
        paragraph {
            text(
                Bokmal to "Du bør endre skattekortet når du begynner å ta ut alderspensjon. Dette kan du gjøre selv på $SKATTEETATEN_URL. " +
                        "Der får du også mer informasjon om skattekort for pensjonister. Vi får skattekortet elektronisk. Du skal derfor ikke sende det til oss.",
                Nynorsk to "Du bør endre skattekortet når du byrjar å ta ut alderspensjon. Dette kan du gjere sjølv på $SKATTEETATEN_URL. " +
                        "Der får du også meir informasjon om skattekort for pensjonistar. Vi får skattekortet elektronisk. Du skal derfor ikkje sende det til oss.",
                English to "When you start draw retirement pension, you should change your tax deduction card. You can change your tax card by logging on to $SKATTEETATEN_URL. " +
                        "There you will find more information regarding tax deduction card for pensioners. We will receive the tax card directly from the Norwegian Tax Administration, meaning you do not need to send it to us."
            )
        }
        paragraph {
            text(
                Bokmal to "På $SKATTEETATEN_URL finner du også informasjon om skatt når du bor utenfor Norge. " +
                        "Du må selv avklare spørsmål om skatteplikt til det landet du bor i med skattemyndighetene der.",
                Nynorsk to "På $SKATTEETATEN_URL finn du også informasjon om skatt når du bur utanfor Noreg. " +
                        "Du må sjølv avklare spørsmål om skatteplikt til det landet du bur i med skatteorgana der.",
                English to "At $SKATTEETATEN_URL you will also find information about tax liability to Norway after moving abroad. " +
                        "You must clarify questions about tax liability to your country of residence with the local tax authorities."
            )
        }
    }
}

// infoLevealder_001
object InfoLevealderAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Alderspensjonen blir levealdersjustert",
                Nynorsk to "Alderspensjonen blir levealdersjustert",
                English to "Your retirement pension is adjusted for life expectancy"
            )
        }
        paragraph {
            text(
                Bokmal to "Pensjonen blir justert ut i fra forventet levealder for ditt årskull. Du finner mer informasjon på $ALDERSPENSJON.",
                Nynorsk to "Pensjonen blir justert ut frå forventa levealder for årskullet ditt. Du finn meir informasjon på $ALDERSPENSJON.",
                English to "Your pension has been adjusted in accordance with the life expectancy for people born in the same year as you. You will find more information at $ALDERSPENSJON."
            )
        }
    }
}

// infoBoddArbeidetUtland_001
object InfoBoddArbeidetUtlandet : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Har du bodd eller arbeidet i utlandet?",
                Nynorsk to "Har du budd eller arbeidd i  utlandet?",
                English to "Have you lived or worked abroad?"
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du har bodd eller arbeidet i utlandet kan dette få betydning for hvor mye du får utbetalt i pensjon. " +
                        "Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. " +
                        "Derfor kan du også ha rett til pensjon fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med.",
                Nynorsk to "Dersom du har budd eller arbeidd i utlandet, kan dette få noko å seie for kor mykje du får utbetalt i pensjon. " +
                        "Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rett til pensjon frå andre land. " +
                        "Vi kan hjelpe deg med å søkje i land Noreg har trygdeavtale med.",
                English to "If you have lived or worked abroad, this may affect the amount of your pension. " +
                        "Norway cooperates with a number of countries through the EEA Agreement and other social security agreements. You may therefore also be entitled to a pension from other countries. " +
                        "We can assist you with your application to countries with which Norway has a social security agreement."
            )
        }
    }
}

// pensjonFraAndreInfoAP_001
object InfoPensjonFraAndreAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Andre pensjonsordninger",
                Nynorsk to "Andre pensjonsordningar",
                English to "Other pension schemes"
            )
        }
        paragraph {
            text(
                Bokmal to "Mange er tilknyttet en eller flere offentlige eller private pensjonsordninger som de har pensjonsrettigheter fra. " +
                        "Du bør kontakte de du har slike ordninger med for å undersøke hvilke rettigheter du kan ha. Du kan også undersøke med siste arbeidsgiver. ",
                Nynorsk to "Mange er knytte til ei eller fleire offentlege eller private pensjonsordningar som de har pensjonsrettar frå. " +
                        "Du bør kontakte dei du har slike ordningar med for å undersøke kva for rettar du har. Du kan også undersøkje med siste arbeidsgivar.",
                English to "Many people are also members of one or more public or private pension schemes where they also have pension rights. " +
                        "You must contact the company/ies you have pension arrangements with, if you have any questions about this. You can also contact your most recent employer."
            )
        }
    }
}

