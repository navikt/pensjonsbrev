package no.nav.pensjon.brev.aldersovergang.fraser

import no.nav.pensjon.brev.alder.maler.felles.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.DIN_PENSJON_URL_INNLOGGET
import no.nav.pensjon.brev.alder.maler.felles.Constants.FORSOERGINGSTILLEGG_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.KONTAKT_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.alder.model.YtelseForAldersovergangKode
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text

// infoGjIkkeInntektsavkorted_002, infoGjInntektsavkortet_001, infoUTAPinnledn_001, infoUTGradertAPinnledn_001, infoUTogAPinnledn_001, infoFamPleierAPinnledn_001, infoAPinnledn_001
data class InnledningInfoYtelse(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.GJP_FULL)) {
            paragraph {
                text(
                    bokmal { + "Gjenlevendepensjonen din opphører fra og med måneden etter at du fyller 67 år." },
                    nynorsk { + "Attlevandepensjonen din opphøyrer frå og med månaden etter at du fyller 67 år." },
                    english { + "Your survivor's pension will be terminated from the month after you turn 67." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Gjenlevendepensjon er en pensjon beregnet med avdødes pensjonsrettigheter. " +
                        "Nav har ikke nok informasjon til å vurdere din rett til alderspensjon. Du må derfor søke om alderspensjon." },
                    nynorsk { + "Attlevandepensjonen er ein pensjon som blir berekna med avdødes pensjonsrettar. " +
                        "Nav har ikkje nok informasjon til å vurdere din rett til alderspensjon. Du må derfor søkje om alderspensjon." },
                    english { + "The survivor's pension is a pension which is based on the deceased's pension rights. " +
                        "Nav does not have sufficient information to assess your eligibility for retirement pension. " +
                        "Therefore, you will need to submit an application." },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Du kan søke elektronisk via $DIN_PENSJON_URL. Hvis du ikke kan benytte denne løsningen, " +
                        "så finner du søknadsskjema for alderspensjon på $NAV_URL." },
                    nynorsk { + 
                        "Du kan søkje elektronisk via $DIN_PENSJON_URL. Dersom du ikkje kan bruke denne løysinga, " +
                        "så finn du søknadsskjema  for alderspensjon på $NAV_URL." },
                    english { + 
                        "You can apply electronically by logging on to $DIN_PENSJON_URL, or use the application form " +
                        "for retirement pension, which is also found at $NAV_URL." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi kan tidligst innvilge søknaden din fra og med måneden etter at du søkte. " },
                    nynorsk { + "Vi kan tidlegast innvilge søknaden din frå og med månaden etter at du søkte." },
                    english { + "We can at the earliest grant your application the month after you applied." },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Du kan velge å ta ut hele eller deler av alderspensjonen din (20, 40, 50, 60, 80 eller 100 prosent). " +
                        "Du kan også vente med å ta ut alderspensjon." },
                    nynorsk { + 
                        "Du kan  velje å ta ut heile eller delar av alderspensjonen din (20, 40, 50, 60, 80 eller 100 prosent). " +
                        "Du kan også vente med å ta ut alderspensjon." },
                    english { + 
                        "You can start drawing all or a part of your retirement pension at a rate of 20, 40, 50, 60, 80 or 100 percent. " +
                        "You can also postpone drawing your retirement pension." },
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.GJP_AVKORT)) {
            paragraph {
                text(
                    bokmal { + 
                        "Gjenlevendepensjonen din opphører fra og med måneden etter at du fyller 67 år. " +
                        "Fra samme tidspunkt har alle som hovedregel rett til å ta ut alderspensjon. " +
                        "Dersom du velger å ta ut alderspensjon vil den bli beregnet med dine gjenlevenderettigheter." },
                    nynorsk { + 
                        "Attlevandepensjonen din blir avslutta frå og med månaden etter at du fyller 67 år. " +
                        "Frå same tidspunkt har alle som hovudregel rett til å ta ut alderspensjon. " +
                        "Dersom du vel å ta ut alderspensjon vil den bli berekna med dine attlevanderettar." },
                    english { + "Your survivor's pension will cease the month after you turn 67. " +
                        "Generally, everyone is entitled to draw a retirement pension from the National " +
                        "Insurance Scheme starting the month after they reach the age of 67. " +
                        "If you decide to draw retirement pension, your survivor’s rights will be included." },
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.UT)) {
            paragraph {
                text(
                    bokmal { + 
                        "Uføretrygden din opphører og regnes om til 100 prosent alderspensjon måneden etter at " +
                        "du fyller 67 år. Du trenger ikke søke om dette." },
                    nynorsk { + 
                        "Uføretrygda di blir avslutta og rekna om til 100 prosent alderspensjon månaden etter at " +
                        "du fyller 67 år. Du treng ikkje søkje om dette." },
                    english { + 
                        "The month after you turn 67 years of age, your disability benefit will cease and you " +
                        "will instead receive full retirement pension. You do not need to apply for this." },
                )
            }
        }.orShowIf(
            ytelseForAldersovergangKode.isOneOf(
                YtelseForAldersovergangKode.UT_GRAD,
                YtelseForAldersovergangKode.UT_AP_GRAD,
            ),
        ) {
            showIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.UT_GRAD)) {
                paragraph {
                    text(
                        bokmal { + 
                            "Uføretrygden din stopper og regnes om til alderspensjon måneden etter at du " +
                            "fyller 67 år. Hvis du ikke melder fra om noe annet, får du automatisk alderspensjon " +
                            "etter en uttaksgrad som tilsvarer det du får i uføretrygd i dag." },
                        nynorsk { + "Uføretrygda di stoppar og blir rekna om til alderspensjon månaden etter at du " +
                            "fyller 67 år. Om du ikkje melder frå om noko anna, får du automatisk alderspensjon " +
                            "etter ein uttaksgrad som svarer til det du får i uføretrygd i dag." },
                        english { + 
                            "Your disability benefit will cease and be converted into retirement pension the month " +
                            "after you turn 67. Unless we receive further information from you, you will " +
                            "automatically receive retirement pension at a withdrawal rate corresponding to " +
                            "what you currently receive in disability benefit. " },
                    )
                }
            }.orShowIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.UT_AP_GRAD)) {
                paragraph {
                    text(
                        bokmal { + 
                            "Uføretrygden din stopper og regnes om til alderspensjon måneden etter at du fyller 67 år. " +
                            "Hvis du ikke melder fra om noe annet, får du automatisk alderspensjon etter en uttaksgrad som tilsvarer " +
                            "det du får i uføretrygd og alderspensjon i dag." },
                        nynorsk { + "Uføretrygda di stoppar og blir rekna om til alderspensjon månaden etter at du " +
                            "fyller 67 år. Om du ikke melder fra om noko anna, får du automatisk alderspensjon " +
                            "etter ein uttaksgrad som svarer til det du får i uføretrygd og alderspensjoni dag." },
                        english { + 
                            "Your disability benefit will cease and be converted into retirement pension the month after " +
                            "you turn 67. Unless we receive further information from you, you will automatically receive " +
                            "retirement pension at a withdrawal rate corresponding to what you currently receive in " +
                            "disability benefit end retirement pension. " },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { + 
                        "Alle som har rett til alderspensjon, kan velge å ta ut hel (100 prosent) " +
                        "alderspensjon fra måneden etter fylte 67 år." },
                    nynorsk { + 
                        "Alle som har rett til alderspensjon, kan velje å ta ut heil (100 prosent) " +
                        "alderspensjon frå månaden etter fylte 67 år." },
                    english { + 
                        "Everyone entitled to retirement pension can choose to withdraw a full (100 percent) " +
                        "retirement pension from the month after turning 67." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du ønsker hel alderspensjon fra måneden etter at uføretrygden stopper, " +
                        "må du melde fra senest den måneden du fyller 67 år. Du melder fra til Nav ved å " +
                        "fylle ut elektronisk søknad/krav på $DIN_PENSJON_URL_INNLOGGET. Hvis du ikke kan bruke " +
                        "denne løsningen, finner du søknadsskjema for alderspensjon på nav.no." },
                    nynorsk { + "Om du ønskjer heil alderspensjon frå månaden etter at uføretrygda stoppar, " +
                        "må du melde frå seinast den månaden du fyller 67 år. Du melder frå til Nav ved å " +
                        "fylle ut elektronisk søknad/krav på $DIN_PENSJON_URL_INNLOGGET. Om du ikkje kan bruke " +
                        "denne løysinga, finn du søknadsskjema for alderspensjon på nav.no." },
                    english { + "If you wish to receive full retirement pension from the month after your " +
                        "disability benefit ends, you must notify Nav no later than the month you turn " +
                        "67. You can notify Nav by submitting an electronic application at " +
                        "$DIN_PENSJON_URL_INNLOGGET. If you are unable to use this solution, you " +
                        "can find the retirement pension application form at nav.no." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan også velge å ta ut alderspensjonen din med en annen uttaksgrad. " +
                        "Uttaksgrad er hvor stor andel av pensjonen du tar ut. Du kan ta ut " +
                        "20, 40, 50, 60, 80 eller 100 prosent alderspensjon." },
                    nynorsk { + "Du kan også velje å ta ut alderspensjonen din med ein annan uttaksgrad. " +
                        "Uttaksgrad er kor stor del av pensjonen du tar ut. Du kan ta ut " +
                        "20, 40, 50, 60, 80 eller 100 prosent alderspensjon." },
                    english { + "You may also choose to withdraw your retirement pension at a different " +
                        "withdrawal rate. The withdrawal rate is the proportion of the pension " +
                        "you choose to take out. You can withdraw 20, 40, 50, 60, 80, or 100 " +
                        "percent retirement pension." },
                )
            }
            paragraph {
                text(
                    bokmal { + "I pensjonskalkulatoren på nav.no kan du sjekke hvor høy pensjonen blir " +
                        "med ulike uttaksgrader. Hvis du trenger hjelp til beregningen eller søknaden, " +
                        "kan du kontakte oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON." },
                    nynorsk { + "I pensjonskalkulatoren på nav.no kan du sjekke kor høg pensjonen blir " +
                        "med ulike uttaksgradar. Om du treng hjelp til berekninga eller søknaden, " +
                        "kan du kontakte oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON." },
                    english { + "You can use the pension calculator at nav.no to check how much your pension " +
                        "would be for different withdrawal rates. If you need help with the calculation or " +
                        "the application, you can contact us by phone on $NAV_KONTAKTSENTER_TELEFON_PENSJON." },
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.FAM_PL)) {
            paragraph {
                text(
                    bokmal { + 
                        "Din pensjon som tidligere familiepleier opphører og regnes om til 100 prosent " +
                        "alderspensjon måneden etter at du fyller 67 år. " +
                        "Du trenger ikke søke om dette." },
                    nynorsk { + 
                        "Pensjonen din som tidlegare familiepleiar blir avslutta og rekna om til 100 prosent " +
                        "alderspensjon månaden etter at du fyller 67 år. " +
                        "Du treng ikkje å søkje om dette." },
                    english { + 
                        "The month after you turn 67, the pension you currently receive as a former family " +
                        "carer will cease, and you will instead receive a full retirement pension. " +
                        "You do not need to apply for this." },
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.INGEN_YT)) {
            paragraph {
                text(
                    bokmal { + "Du har frem til nå ikke tatt ut alderspensjon. " +
                        "Vi ønsker derfor å gjøre deg oppmerksom på at alle som hovedregel har rett til å " +
                        "ta ut alderspensjon fra folketrygden fra og med måneden etter fylte 67 år." },
                    nynorsk { + "Du har fram til no ikkje teke ut alderspensjon. " +
                        "Vi ønskjer derfor å gjere deg merksam på at alle som hovudregel har rett til å ta " +
                        "ut alderspensjon frå folketrygda frå og med månaden etter fylte 67 år." },
                    english { + "To date, you have not yet drawn any retirement pension. " +
                        "We want to make you aware that, as a general rule, everyone is entitled to draw " +
                        "retirement pension from the National Insurance Scheme starting the month after they turn 67." },
                )
            }
        }
    }
}

// infoAPVelge1_001, infoAPVelge2_001
data class InfoVelgeAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            ytelseForAldersovergangKode.isOneOf(
                YtelseForAldersovergangKode.GJP_AVKORT,
                YtelseForAldersovergangKode.INGEN_YT,
                YtelseForAldersovergangKode.FAM_PL,
            ),
        ) {
            paragraph {
                val likevel =
                    ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.FAM_PL)
                text(
                    bokmal { + "Du kan " +
                        ifElse(likevel, ifTrue = "likevel", ifFalse = "") + " velge" },
                    nynorsk { + "Du kan " +
                        ifElse(likevel, ifTrue = "likevel", ifFalse = "") + " velje" },
                    english { + "You can " +
                        ifElse(
                            likevel,
                            ifTrue = "nevertheless",
                            ifFalse = "",
                        ) + " select one of the following options:" },
                )
                list {
                    item {
                        text(
                            bokmal { + "å vente med å ta ut alderspensjonen" },
                            nynorsk { + "å vente med å ta ut alderspensjon" },
                            english { + "Postpone starting to draw your retirement pension." },
                        )
                    }
                    item {
                        text(
                            bokmal { + "å ta ut hele eller deler av pensjonen din (20, 40, 50, 60, 80 eller 100 prosent)" },
                            nynorsk { + "å ta ut heile eller delar av pensjonen din (20, 40, 50, 60, 80 eller 100 prosent)" },
                            english { + "Start drawing all or a part of your pension at a rate of 20, 40, 50, 60, 80 or 100 percent." },
                        )
                    }
                    showIf(
                        ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.FAM_PL),
                    ) {
                        item {
                            text(
                                bokmal { + "å stanse eller endre alderspensjonen på et senere tidspunkt" },
                                nynorsk { + "å stanse eller endre alderspensjonen på eit seinare tidspunkt" },
                                english { + "Stop or change your retirement pension at a later date." },
                            )
                        }
                    }
                }
            }
        }
    }
}

// infoAPOnsketUttak_001
data class InfoOnsketUttakAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            ytelseForAldersovergangKode.isOneOf(
                YtelseForAldersovergangKode.GJP_AVKORT,
                YtelseForAldersovergangKode.INGEN_YT,
            ),
        ) {
            paragraph {
                text(
                    bokmal { + "Når du ønsker å ta ut alderspensjon, må du søke om dette." },
                    nynorsk { + "Når du ønskjer å ta ut alderspensjon, må du søkje om dette." },
                    english { + "When you want to start receiving your retirement pension, you will need to submit an application." },
                )
            }
        }
    }
}

// infoAPSoke1_002, infoAPSoke2_002
data class InfoOenskeSokeAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.FAM_PL),
        ) {
            paragraph {
                text(
                    bokmal { + 
                        "Ønsker du å vente med å ta ut alderspensjonen eller en annen uttaksgrad enn hva " +
                        "du vil få etter omregning, må du søke om dette. " +
                        "Du kan søke elektronisk via $DIN_PENSJON_URL. Hvis du ikke kan benytte denne løsningen, " +
                        "så finner du søknadsskjema for alderspensjon på $NAV_URL." },
                    nynorsk { + 
                        "Ønskjer du å vente med å ta ut alderspensjonen, eller ønskjer du ein annan " +
                        "uttaksgrad enn det du får etter omrekninga, må du søkje om dette. " +
                        "Du kan søkje elektronisk via $DIN_PENSJON_URL. Dersom du ikkje kan bruke denne løysinga, " +
                        "så finn du søknadsskjema for alderspensjon på $NAV_URL." },
                    english { + 
                        "If you do not want to start drawing your retirement pension yet, or if you want " +
                        "to draw your pension at a different rate than the one allocated after recalculation, " +
                        "you must submit an application. You can apply electronically by logging on to " +
                        "$DIN_PENSJON_URL, or use the application form for retirement pension, which " +
                        "is also found at $NAV_URL." },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Ønsker du å endre alderspensjonen, må vi ha mottatt søknaden senest måneden før den måneden du søker endring fra." },
                    nynorsk { + "Ønskjer du å endre alderspensjonen, må vi få søknaden seinast månaden før du ønskjer endringa." },
                    english { + "Any change will be implemented at the earliest the month after we have received the application." },
                )
            }
        }
    }
}

// infoAPSivilstand_001
data class InfoSivilstandAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            ytelseForAldersovergangKode.isOneOf(
                YtelseForAldersovergangKode.UT,
                YtelseForAldersovergangKode.UT_GRAD,
            ),
        ) {
            title1 {
                text(
                    bokmal { + "For deg som har ektefelle/partner/samboer" },
                    nynorsk { + "For deg som har ektefelle/partnar/sambuar" },
                    english { + "For people who have a spouse/partner/cohabitant" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Din ektefelles, samboers eller partners inntekt har betydning for beregningen av alderspensjon." },
                    nynorsk { + "Inntekta til ektefellen, sambuaren eller partnaren din påverkar berekninga av alderspensjonen." },
                    english { + "Your spouse’s/partner’s/cohabitant’s income affects the calculation of your retirement pension." },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Hvis din ektefelle, partner eller samboer ikke har egen pensjon eller uføretrygd, " +
                        "vil vi i de fleste saker ikke ha korrekte inntektsopplysninger. " +
                        "Vi vil derfor legge til grunn en inntekt på 2 ganger folketrygdens grunnbeløp " +
                        "når vi beregner alderspensjonen." },
                    nynorsk { + 
                        "Dersom ektefellen, partnaren eller sambuaren din ikkje har eigen pensjon eller " +
                        "eiga uføretrygd, vil vi i dei fleste sakene ikkje ha korrekte inntektsopplysningar. " +
                        "Vi vil derfor leggje til grunn ei inntekt på 2 gonger grunnbeløpet i folketrygda " +
                        "når vi bereknar alderspensjonen." },
                    english { + 
                        "If your spouse/partner/cohabitant does not receive a separate pension or disability " +
                        "benefit, in most cases we will not have the correct income information. " +
                        "We will therefore use an income of two times the National Insurance basic amount (" },
                )
                text(
                    bokmal { + "" },
                    nynorsk { + "" },
                    english { + quoted("G") + ") as the starting point when calculating retirement pension." },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Hvis du har en ektefelle, partner eller samboer som har en inntekt som er lavere " +
                        "enn 2 ganger folketrygdens grunnbeløp, er det viktig at du gir beskjed om beløpet på $KONTAKT_URL. " +
                        "Da kan du ha rett til høyere alderspensjon." },
                    nynorsk { + 
                        "Dersom du har ein ektefelle, partnar eller sambuar som har ei inntekt som er " +
                        "lågare enn 2 gonger grunnbeløpet i folketrygda, er det viktig at du gir beskjed om beløpet på $KONTAKT_URL. " +
                        "Då kan du ha rett til høgare alderspensjon." },
                    english { + 
                        "If you have a spouse, partner, or cohabitant whose income is lower than twice " +
                        "the National Insurance basic amount, it is important that you report the amount " +
                        "at $KONTAKT_URL as you may be entitled to a higher retirement pension." },
                )
            }
        }
    }
}

// infoAPft_002
data class InfoFTAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            ytelseForAldersovergangKode.isOneOf(
                YtelseForAldersovergangKode.UT,
                YtelseForAldersovergangKode.UT_GRAD,
                YtelseForAldersovergangKode.UT_AP_GRAD,
            ),
        ) {
            title1 {
                text(
                    bokmal { + "For deg som har forsørgingstillegg" },
                    nynorsk { + "Forsørgingstillegg" },
                    english { + "If you have dependant supplement" },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Fra 1. januar 2022 er regelverket om forsørgingstillegg endret, og du vil derfor " +
                        "ikke få ektefelletillegg og/eller barnetillegg i alderspensjonen din." },
                    nynorsk { + 
                        "Frå 1. januar 2022 er regelverket om forsørgingstillegg endra, og du vil derfor " +
                        "ikkje få ektefelletillegg og/eller barnetillegg i alderspensjonen din." },
                    english { + 
                        "From 1 January 2022, the regulations regarding dependant supplement were changed. " +
                        "Therefore, you will no longer receive a dependant supplement for your spouse or child " +
                        "in your retirement pension." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du finner mer informasjon om dette på $FORSOERGINGSTILLEGG_URL." },
                    nynorsk { + "Du finn meir informasjon om dette på $FORSOERGINGSTILLEGG_URL." },
                    english { + "You can find more information at $FORSOERGINGSTILLEGG_URL." },
                )
            }
        }
    }
}

// infoAPAfpPrivat_001
data class InfoAFPprivatAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            ytelseForAldersovergangKode.isOneOf(
                YtelseForAldersovergangKode.GJP_AVKORT,
                YtelseForAldersovergangKode.INGEN_YT,
            ),
        ) {
            title1 {
                text(
                    bokmal { + "Har du rett til AFP i privat sektor?" },
                    nynorsk { + "Har du rett til AFP i privat sektor?" },
                    english { + "Are you entitled to a contractual pension in the private sector (AFP)?" },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Hvis du er ansatt i en privat virksomhet og har rett til AFP, må du søke om " +
                        "dette samtidig med alderspensjonen fra folketrygden." },
                    nynorsk { + 
                        "Dersom du er tilsett i ei privat verksemd og har rett til AFP, må du søkje om " +
                        "dette samtidig med alderspensjonen frå folketrygda." },
                    english { + 
                        "If you are employed in a private business and are entitled to a contractual early " +
                        "retirement pension (AFP), you must apply for it at the same time as you apply " +
                        "for retirement pension from the National Insurance Scheme." },
                )
            }
            paragraph {
                text(
                    bokmal { + "AFP for ansatte i en privat virksomhet blir ikke redusert hvis du har inntekt. " +
                        "Du kan bruke pensjonskalkulatoren på $DIN_PENSJON_URL_INNLOGGET " +
                        "for å se hvor mye du får i AFP, sammen med alderspensjonen." },
                    nynorsk { + "AFP for tilsette i ei privat verksemd blir ikkje redusert dersom du har inntekt. " +
                        "Du kan bruke pensjonskalkulatoren på $DIN_PENSJON_URL_INNLOGGET " +
                        "for å sjå kor mykje du får i AFP, saman med alderspensjonen." },
                    english { + "AFP for employees in a private business is not reduced if you have income. " +
                        "You can use the pension calculator at $DIN_PENSJON_URL_INNLOGGET " +
                        "to see how much AFP you will receive in addition to your retirement pension." },
                )
            }
        }
    }
}

// infoAPNorSoke_002, infoAPHvordanSoke_002
data class InfoSoekeAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            ytelseForAldersovergangKode.isNotAnyOf(
                YtelseForAldersovergangKode.GJP_FULL,
                YtelseForAldersovergangKode.UT,
            ),
        ) {
            title1 {
                text(
                    bokmal { + "Når må du søke?" },
                    nynorsk { + "Når må du søkje?" },
                    english { + "When to apply?" },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Vi kan tidligst innvilge søknaden din fra og med måneden etter at du søkte. " +
                        "Du bør søke i god tid, men tidligst fire måneder før du ønsker at utbetalingen skal starte." },
                    nynorsk { + 
                        "Vi kan tidlegast innvilge søknaden din frå og med månaden etter at du søkte. " +
                        "Du bør derfor søkje i god tid, men tidlegast fire månader før du ønskjer at utbetalinga skal starte." },
                    english { + 
                        "Retirement pension from the National Insurance Scheme can, at the earliest, " +
                        "be paid out the month after we receive your application. " +
                        "Therefore, you should apply well in advance of the month that you want to start " +
                        "receiving your pension, but at the earliest four months in advance." },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Eksempel: Hvis du ønsker første utbetaling av alderspensjon i august, kan du " +
                        "tidligst sende søknaden i april." },
                    nynorsk { + 
                        "Eksempel: Dersom du ønskjer første utbetaling av alderspensjon i august, kan du " +
                        "tidlegast sende søknaden i april." },
                    english { + 
                        "Example: If you want to start receiving your retirement pension in August you " +
                        "can at the earliest send your application in April." },
                )
            }
            title1 {
                text(
                    bokmal { + "Hvordan søker du om alderspensjon?" },
                    nynorsk { + "Korleis søkjer du om alderspensjon?" },
                    english { + "How do you apply for retirement pension?" },
                )
            }
            paragraph {
                text(
                    bokmal { + 
                        "Du kan søke elektronisk via $DIN_PENSJON_URL_INNLOGGET eller bruke søknadsskjema for " +
                        "alderspensjon som du også finner på $NAV_URL." },
                    nynorsk { + 
                        "Du kan søkje elektronisk via $DIN_PENSJON_URL_INNLOGGET eller bruke søknadsskjemaet om " +
                        "alderspensjon som du også finn på $NAV_URL." },
                    english { + 
                        "You can apply electronically by logging on to $DIN_PENSJON_URL_INNLOGGET, or use the " +
                        "application form for retirement pension, which is also found at $NAV_URL." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du kan bruke pensjonskalkulatoren på $DIN_PENSJON_URL_INNLOGGET til å beregne alderspensjonen din." },
                    nynorsk { + "Du kan bruke pensjonskalkulatoren på $DIN_PENSJON_URL_INNLOGGET til å berekne alderspensjon din." },
                    english { + 
                        "You can use the pension calculator at $DIN_PENSJON_URL_INNLOGGET to calculate your future retirement pension." },
                )
            }
        }
    }
}

// infoAPSokeAnnenGrad100_002
data class InfoSoekeAnnenGradAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.UT)) {
            title1 {
                text(
                    bokmal { + "Ønsker du å ta ut en annen grad enn 100 prosent alderspensjon?" },
                    nynorsk { + "Ønskjer du å ta ut ein annan grad enn 100 prosent alderspensjon?" },
                    english { + "Do you want to draw retirement pension at another rate than 100 percent?" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi regner om uføretrygden din til 100 prosent alderspensjon. Du kan likevel velge" },
                    nynorsk { + "Vi reknar om uføretrygda di til 100 prosent alderspensjon. Du kan likevel velje" },
                    english { + 
                        "Your disability benefit will be converted into a 100 percent retirement " +
                        "pension. You can nevertheless select one of the following options:" },
                )
                list {
                    item {
                        text(
                            bokmal { + "å ta ut deler av pensjonen din (20, 40 ,50, 60 eller 80 prosent)" },
                            nynorsk { + "å ta ut delar av pensjonen din (20, 40, 50, 60 eller 80 prosent)" },
                            english { + "Start drawing a part of your pension at a rate of 20, 40, 50, 60 or 80 percent." },
                        )
                    }
                    item {
                        text(
                            bokmal { + "å stanse eller endre pensjonen din på et senere tidspunkt" },
                            nynorsk { + "å stanse eller endre pensjonen din på eit seinare tidspunkt" },
                            english { + "Stop or change your retirement pension at a later date." },
                        )
                    }
                    item {
                        text(
                            bokmal { + "å vente med å ta ut alderspensjonen" },
                            nynorsk { + "å vente med å ta ut alderspensjonen" },
                            english { + "Postpone drawing your retirement pension." },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { + 
                        "Hvis du ønsker en annen uttaksgrad enn 100 prosent alderspensjon, kan du søke " +
                        "om dette elektronisk via $DIN_PENSJON_URL_INNLOGGET. " +
                        "Hvis du ikke kan benytte denne løsningen, finner du søknadsskjema for alderspensjon på $NAV_URL." },
                    nynorsk { + 
                        "Dersom du ønskjer ein annan uttaksgrad enn 100 prosent alderspensjon, kan du " +
                        "søkje om dette elektronisk via $DIN_PENSJON_URL_INNLOGGET. " +
                        "Dersom du ikkje kan bruke denne løysinga, så finn du søknadsskjema  for alderspensjon på $NAV_URL." },
                    english { + 
                        "If you want to draw retirement pension at another rate than 100 percent, you " +
                        "can apply electronically by logging on to $DIN_PENSJON_URL_INNLOGGET. " +
                        "Or you can use the application form for retirement pension, which is also found at $NAV_URL." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Ønsker du å endre alderspensjonen, må vi få søknaden senest måneden før du ønsker endringen." },
                    nynorsk { + "Ønskjer du å endre alderspensjonen, må vi få søknaden seinast månaden før du ønskjer endringa." },
                    english { + "Any change will be implemented at the earliest the month after we have received the application." },
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
                bokmal { + "Det er egne skatteregler for alderspensjon" },
                nynorsk { + "Det er eigne skattereglar for pensjon" },
                english { + "Pensions are subject to different tax rules" },
            )
        }
        paragraph {
            text(
                bokmal { + 
                    "Du bør endre skattekortet når du begynner å ta ut alderspensjon. Ellers trekkes det " +
                    "30 prosent skatt av alderspensjonen. Du kan endre skattekortet og få mer informasjon " +
                    "på $SKATTEETATEN_PENSJONIST_URL. Der får du også mer informasjon om skattekort for pensjonister. " +
                    "Nav får skattekortet elektronisk. Du skal derfor ikke sende det til oss." },
                nynorsk { + 
                    "Du bør endre skattekortet når du byrjar å ta ut alderspensjon. Elles blir det " +
                    "trekt 30 prosent skatt av alderspensjonen. Du kan endre skattekortet og få meir informasjon " +
                    "på  $SKATTEETATEN_PENSJONIST_URL. Der får du også meir informasjon om skattekort for " +
                    "pensjonistar. Nav får skattekortet elektronisk. Du skal derfor ikkje sende det til oss." },
                english { + 
                    "When you start drawing retirement pension, you should change your tax deduction card. " +
                    "Otherwise, 30 percent of the pension will be deducted from your pension. You can change " +
                    "your tax card by logging on to $SKATTEETATEN_PENSJONIST_URL. " +
                    "Nav will receive the tax card directly from the Norwegian Tax Administration, meaning you do not need to send it to us." },
            )
        }
        paragraph {
            text(
                bokmal { + "På $SKATTEETATEN_PENSJONIST_URL finner du også informasjon om skatt når du bor utenfor Norge. " +
                    "Du må selv avklare spørsmål om skatteplikt til det landet du bor i med skattemyndighetene der." },
                nynorsk { + "På $SKATTEETATEN_PENSJONIST_URL finn du også informasjon om skatt når du bur utanfor Noreg. " +
                    "Du må sjølv avklare spørsmål om skatteplikt til det landet du bur i med skatteorgana der." },
                english { + 
                    "At $SKATTEETATEN_PENSJONIST_URL, you will also find information about tax liability to Norway after moving abroad. " +
                    "You must clarify questions about tax liability to your country of residence with the local tax authorities." },
            )
        }
    }
}

// infoBoddArbeidetUtland_001
object InfoBoddArbeidetUtlandet : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Har du bodd eller arbeidet i utlandet?" },
                nynorsk { + "Har du budd eller arbeidd i utlandet?" },
                english { + "Have you lived or worked abroad?" },
            )
        }
        paragraph {
            text(
                bokmal { + "Hvis du har bodd eller arbeidet i utlandet, kan dette få betydning for hvor mye du får utbetalt i pensjon. " +
                    "Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. " +
                    "Derfor kan du også ha rett til pensjon fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med." },
                nynorsk { + 
                    "Dersom du har budd eller arbeidd i utlandet, kan dette få noko å seie for kor mykje du får utbetalt i pensjon. " +
                    "Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. " +
                    "Derfor kan du også ha rett til pensjon frå andre land. " +
                    "Vi kan hjelpe deg med å søkje i land Noreg har trygdeavtale med." },
                english { + "If you have lived or worked abroad, this may affect the amount of your pension. " +
                    "Norway cooperates with a number of countries through the EEA Agreement and other social " +
                    "security agreements. You may therefore also be entitled to a pension from other countries. " +
                    "We can assist you with your application to countries with which Norway has a social security agreement." },
            )
        }
    }
}

// pensjonFraAndreInfoAP_001
object InfoPensjonFraAndreAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Andre pensjonsordninger" },
                nynorsk { + "Andre pensjonsordningar" },
                english { + "Other pension schemes" },
            )
        }
        paragraph {
            text(
                bokmal { + 
                    "Mange er tilknyttet en eller flere offentlige eller private pensjonsordninger som de har pensjonsrettigheter fra. " +
                    "Du bør kontakte dem du har slike ordninger med for å undersøke hvilke rettigheter du kan ha. Du kan også undersøke med siste arbeidsgiver. " },
                nynorsk { + 
                    "Mange er knytte til ei eller fleire offentlege eller private pensjonsordningar som de har pensjonsrettar frå. " +
                    "Du bør kontakte dei du har slike ordningar med for å undersøke kva for rettar du har. Du kan også undersøkje med siste arbeidsgivar." },
                english { + 
                    "Many people are also members of one or more public or private pension schemes where they also have pension rights. " +
                    "You must contact the company/ies you have pension arrangements with, if you have any questions about this. You can also contact your most recent employer." },
            )
        }
    }
}

