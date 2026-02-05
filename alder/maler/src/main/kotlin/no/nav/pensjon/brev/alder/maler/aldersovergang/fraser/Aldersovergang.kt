package no.nav.pensjon.brev.aldersovergang.fraser

import no.nav.pensjon.brev.alder.maler.felles.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.DIN_PENSJON_URL_INNLOGGET
import no.nav.pensjon.brev.alder.maler.felles.Constants.FORSOERGINGSTILLEGG_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.alder.maler.felles.bestemtForm
import no.nav.pensjon.brev.alder.maler.felles.ubestemtForm
import no.nav.pensjon.brev.alder.model.BorMedSivilstand
import no.nav.pensjon.brev.alder.model.YtelseForAldersovergangKode
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner

// infoGjIkkeInntektsavkorted_002, infoGjInntektsavkortet_001, infoUTAPinnledn_001, infoUTGradertAPinnledn_001, infoUTogAPinnledn_001, infoFamPleierAPinnledn_001, infoAPinnledn_001
data class InnledningInfoYtelse(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
    val uforegrad: Expression<Int?>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.GJP_FULL)) {
            paragraph {
                text(
                    bokmal { +"Gjenlevendepensjonen din opphører fra og med måneden etter at du fyller 67 år." },
                    nynorsk { +"Attlevandepensjonen din opphøyrer frå og med månaden etter at du fyller 67 år." },
                    english { +"Your survivor's pension will be terminated from the month after you turn 67." },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Gjenlevendepensjon er en pensjon beregnet med avdødes pensjonsrettigheter. " +
                                "Nav har ikke nok informasjon til å vurdere din rett til alderspensjon. Du må derfor søke om alderspensjon."
                    },
                    nynorsk {
                        +"Attlevandepensjonen er ein pensjon som blir berekna med avdødes pensjonsrettar. " +
                                "Nav har ikkje nok informasjon til å vurdere din rett til alderspensjon. Du må derfor søkje om alderspensjon."
                    },
                    english {
                        +"The survivor's pension is a pension which is based on the deceased's pension rights. " +
                                "Nav does not have sufficient information to assess your eligibility for retirement pension. " +
                                "Therefore, you will need to submit an application."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +
                        "Du kan søke elektronisk via $DIN_PENSJON_URL. Hvis du ikke kan benytte denne løsningen, " +
                                "så finner du søknadsskjema for alderspensjon på $NAV_URL."
                    },
                    nynorsk {
                        +
                        "Du kan søkje elektronisk via $DIN_PENSJON_URL. Dersom du ikkje kan bruke denne løysinga, " +
                                "så finn du søknadsskjema  for alderspensjon på $NAV_URL."
                    },
                    english {
                        +
                        "You can apply electronically by logging on to $DIN_PENSJON_URL, or use the application form " +
                                "for retirement pension, which is also found at $NAV_URL."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi kan tidligst innvilge søknaden din fra og med måneden etter at du søkte. " },
                    nynorsk { +"Vi kan tidlegast innvilge søknaden din frå og med månaden etter at du søkte." },
                    english { +"We can at the earliest grant your application the month after you applied." },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +
                        "Du kan velge å ta ut hele eller deler av alderspensjonen din (20, 40, 50, 60, 80 eller 100 prosent). " +
                                "Du kan også vente med å ta ut alderspensjon."
                    },
                    nynorsk {
                        +
                        "Du kan  velje å ta ut heile eller delar av alderspensjonen din (20, 40, 50, 60, 80 eller 100 prosent). " +
                                "Du kan også vente med å ta ut alderspensjon."
                    },
                    english {
                        +
                        "You can start drawing all or a part of your retirement pension at a rate of 20, 40, 50, 60, 80 or 100 percent. " +
                                "You can also postpone drawing your retirement pension."
                    },
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.GJP_AVKORT)) {
            paragraph {
                text(
                    bokmal {
                        +
                        "Gjenlevendepensjonen din opphører fra og med måneden etter at du fyller 67 år. " +
                                "Fra samme tidspunkt har alle som hovedregel rett til å ta ut alderspensjon. " +
                                "Dersom du velger å ta ut alderspensjon vil den bli beregnet med dine gjenlevenderettigheter."
                    },
                    nynorsk {
                        +
                        "Attlevandepensjonen din blir avslutta frå og med månaden etter at du fyller 67 år. " +
                                "Frå same tidspunkt har alle som hovudregel rett til å ta ut alderspensjon. " +
                                "Dersom du vel å ta ut alderspensjon vil den bli berekna med dine attlevanderettar."
                    },
                    english {
                        +"Your survivor's pension will cease the month after you turn 67. " +
                                "Generally, everyone is entitled to draw a retirement pension from the National " +
                                "Insurance Scheme starting the month after they reach the age of 67. " +
                                "If you decide to draw retirement pension, your survivor’s rights will be included."
                    },
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.FAM_PL)) {
            paragraph {
                text(
                    bokmal {
                        +
                        "Din pensjon som tidligere familiepleier opphører og regnes om til 100 prosent " +
                                "alderspensjon måneden etter at du fyller 67 år. " +
                                "Du trenger ikke søke om dette."
                    },
                    nynorsk {
                        +
                        "Pensjonen din som tidlegare familiepleiar blir avslutta og rekna om til 100 prosent " +
                                "alderspensjon månaden etter at du fyller 67 år. " +
                                "Du treng ikkje å søkje om dette."
                    },
                    english {
                        +
                        "The month after you turn 67, the pension you currently receive as a former family " +
                                "carer will cease, and you will instead receive a full retirement pension. " +
                                "You do not need to apply for this."
                    },
                )
            }
        }.orShowIf(ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.INGEN_YT)) {
            paragraph {
                text(
                    bokmal {
                        +"Du har frem til nå ikke tatt ut alderspensjon. " +
                                "Vi ønsker derfor å gjøre deg oppmerksom på at alle som hovedregel har rett til å " +
                                "ta ut alderspensjon fra folketrygden fra og med måneden etter fylte 67 år."
                    },
                    nynorsk {
                        +"Du har fram til no ikkje teke ut alderspensjon. " +
                                "Vi ønskjer derfor å gjere deg merksam på at alle som hovudregel har rett til å ta " +
                                "ut alderspensjon frå folketrygda frå og med månaden etter fylte 67 år."
                    },
                    english {
                        +"To date, you have not yet drawn any retirement pension. " +
                                "We want to make you aware that, as a general rule, everyone is entitled to draw " +
                                "retirement pension from the National Insurance Scheme starting the month after they turn 67."
                    },
                )
            }
        }
        ifNotNull(uforegrad) { uforegrad ->
            showIf(uforegrad.equalTo(100)) {
                paragraph {
                    text(
                        bokmal {
                            +
                            "Uføretrygden din stopper måneden etter at du fyller 67 år. " +
                                    "Da får du automatisk 100 prosent alderspensjon. Du trenger ikke søke om dette."
                        },
                        nynorsk {
                            +
                            "Uføretrygda di stoppar månaden etter at du fyller 67 år. " +
                                    "Då får du automatisk 100 prosent alderspensjon. Du treng ikkje søkje om dette."
                        },
                        english {
                            +
                            "Your disability benefit will cease the month after you turn 67. " +
                                    "You will then automatically receive 100 percent retirement pension. You do not need to apply for this."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Alderspensjon beregnes etter andre regler enn uføretrygd. " +
                                    "Derfor får du ikke det samme i alderspensjon som du har hatt i uføretrygd."
                        },
                        nynorsk {
                            +"Alderspensjon blir berekna etter andre reglar enn uføretrygd. " +
                                    "Derfor får du ikkje det same i alderspensjon som du har hatt i uføretrygd."
                        },
                        english {
                            +"Retirement pension is calculated according to different rules than disability benefit. " +
                                    "Therefore, you will not receive the same amount in retirement pension as you did in disability benefit."
                        },
                    )
                }
            }.orShowIf( uforegrad.greaterThan(0) and uforegrad.notEqualTo(100)) {
                    paragraph {
                        text(
                            bokmal {
                                +"Uføretrygden din stopper fra måneden etter at du blir 67 år. " +
                                        "Hvis du ikke melder fra om noe annet, får du automatisk alderspensjon med en uttaksgrad nærmest mulig den du har i uføretrygd og eventuelt alderspensjon i dag."
                            },
                            nynorsk {
                                +"Uføretrygda di stoppar frå månaden etter at du blir 67 år. " +
                                        "Om du ikkje melder frå om noko anna, får du automatisk alderspensjon med ein uttaksgrad nærast mogleg den du har i uføretrygd og eventuelt alderspensjon i dag."
                            },
                            english {
                                +"Your disability benefit will cease from the month after you turn 67. " +
                                        "Unless we receive further information from you, " +
                                        "you will automatically receive a retirement pension with a withdrawal rate as close as possible to your current disability benefit rate and any retirement pension you may receive today."
                            },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Alderspensjon beregnes etter andre regler enn uføretrygd. " +
                                        "Derfor får du ikke det samme i alderspensjon som du har hatt i uføretrygd."
                            },
                            nynorsk {
                                +"Alderspensjon blir berekna etter andre reglar enn uføretrygd. " +
                                        "Derfor får du ikkje det same i alderspensjon som du har hatt i uføretrygd."
                            },
                            english {
                                +"Retirement pension is calculated according to different rules than disability benefit. " +
                                        "Therefore, you will not receive the same amount in retirement pension as you did in disability benefit."
                            },
                        )
                    }
                }
                title2 {
                    text(
                        bokmal { +"Du har rett til hel (100 prosent) alderspensjon" },
                        nynorsk { +"Du har rett til heil (100 prosent) alderspensjon" },
                        english { +"You are entitled to full (100 percent) retirement pension" },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Hvis du ønsker hel alderspensjon fra måneden etter at uføretrygden stopper, må du melde fra senest den måneden du fyller 67 år. " +
                                    "Du kan endre pensjonen på $DIN_PENSJON_URL_INNLOGGET. Hvis du ikke kan logge inn, finner du søknadsskjema for alderspensjon på $NAV_URL."
                        },
                        nynorsk {
                            +"Om du ønskjer heil alderspensjon frå månaden etter at uføretrygda stoppar, må du melde frå seinast den månaden du fyller 67 år. " +
                                    "Du kan endre pensjonen på $DIN_PENSJON_URL_INNLOGGET. Om du ikkje kan logge inn, finn du søknadsskjema for alderspensjon på $NAV_URL."
                        },
                        english {
                            +"If you wish to receive full retirement pension from the month after your disability benefit ends, you must notify Nav no later than the month you turn 67. " +
                                    "You can change your pension at $DIN_PENSJON_URL_INNLOGGET. If you are unable to log in, you will find the application form for retirement pension at $NAV_URL."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Du kan også velge å ta ut alderspensjonen din med en annen uttaksgrad. " +
                                    "Uttaksgrad er hvor stor andel av pensjonen du tar ut. Du kan ta ut 20, 40, 50, 60, 80 eller 100 prosent alderspensjon."
                        },
                        nynorsk {
                            +"Du kan også velje å ta ut alderspensjonen din med ein annan uttaksgrad. " +
                                    "Uttaksgrad er kor stor del av pensjonen du tar ut. Du kan ta ut 20, 40, 50, 60, 80 eller 100 prosent alderspensjon."
                        },
                        english {
                            +"You may also choose to withdraw your retirement pension at a different withdrawal rate. " +
                                    "The withdrawal rate is the proportion of the pension you choose to take out. You can withdraw 20, 40, 50, 60, 80, or 100 percent retirement pension."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"I pensjonskalkulatoren på $NAV_URL kan du sjekke hvor høy pensjonen blir med ulike uttaksgrader. " +
                                    "Hvis du trenger hjelp til beregningen eller søknaden, kan du kontakte oss på $NAV_KONTAKTSENTER_TELEFON_PENSJON."
                        },
                        nynorsk {
                            +"I pensjonskalkulatoren på $NAV_URL kan du sjekke kor høg pensjonen blir med ulike uttaksgradar. " +
                                    "Om du treng hjelp til berekninga eller søknaden, kan du kontakte oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON."
                        },
                        english {
                            +"You can use the pension calculator at $NAV_URL to check how much your pension will be at different withdrawal rates. " +
                                    "If you need help with the calculation or the application, you can contact us by phone at $NAV_KONTAKTSENTER_TELEFON_PENSJON."
                        },
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
                    bokmal {
                        +"Du kan " +
                                ifElse(likevel, ifTrue = "likevel", ifFalse = "") + " velge"
                    },
                    nynorsk {
                        +"Du kan " +
                                ifElse(likevel, ifTrue = "likevel", ifFalse = "") + " velje"
                    },
                    english {
                        +"You can " +
                                ifElse(
                                    likevel,
                                    ifTrue = "nevertheless",
                                    ifFalse = "",
                                ) + " select one of the following options:"
                    },
                )
                list {
                    item {
                        text(
                            bokmal { +"å vente med å ta ut alderspensjonen" },
                            nynorsk { +"å vente med å ta ut alderspensjon" },
                            english { +"Postpone starting to draw your retirement pension." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"å ta ut hele eller deler av pensjonen din (20, 40, 50, 60, 80 eller 100 prosent)" },
                            nynorsk { +"å ta ut heile eller delar av pensjonen din (20, 40, 50, 60, 80 eller 100 prosent)" },
                            english { +"Start drawing all or a part of your pension at a rate of 20, 40, 50, 60, 80 or 100 percent." },
                        )
                    }
                    showIf(
                        ytelseForAldersovergangKode.equalTo(YtelseForAldersovergangKode.FAM_PL),
                    ) {
                        item {
                            text(
                                bokmal { +"å stanse eller endre alderspensjonen på et senere tidspunkt" },
                                nynorsk { +"å stanse eller endre alderspensjonen på eit seinare tidspunkt" },
                                english { +"Stop or change your retirement pension at a later date." },
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
                    bokmal { +"Når du ønsker å ta ut alderspensjon, må du søke om dette." },
                    nynorsk { +"Når du ønskjer å ta ut alderspensjon, må du søkje om dette." },
                    english { +"When you want to start receiving your retirement pension, you will need to submit an application." },
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
                    bokmal {
                        +
                        "Ønsker du å vente med å ta ut alderspensjonen eller en annen uttaksgrad enn hva " +
                                "du vil få etter omregning, må du søke om dette. " +
                                "Du kan søke elektronisk via $DIN_PENSJON_URL. Hvis du ikke kan benytte denne løsningen, " +
                                "så finner du søknadsskjema for alderspensjon på $NAV_URL."
                    },
                    nynorsk {
                        +
                        "Ønskjer du å vente med å ta ut alderspensjonen, eller ønskjer du ein annan " +
                                "uttaksgrad enn det du får etter omrekninga, må du søkje om dette. " +
                                "Du kan søkje elektronisk via $DIN_PENSJON_URL. Dersom du ikkje kan bruke denne løysinga, " +
                                "så finn du søknadsskjema for alderspensjon på $NAV_URL."
                    },
                    english {
                        +
                        "If you do not want to start drawing your retirement pension yet, or if you want " +
                                "to draw your pension at a different rate than the one allocated after recalculation, " +
                                "you must submit an application. You can apply electronically by logging on to " +
                                "$DIN_PENSJON_URL, or use the application form for retirement pension, which " +
                                "is also found at $NAV_URL."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +
                        "Ønsker du å endre alderspensjonen, må vi ha mottatt søknaden senest måneden før den måneden du søker endring fra."
                    },
                    nynorsk { +"Ønskjer du å endre alderspensjonen, må vi få søknaden seinast månaden før du ønskjer endringa." },
                    english { +"Any change will be implemented at the earliest the month after we have received the application." },
                )
            }
        }
    }
}

// infoAPSivilstand_001
data class InfoSivilstandAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
    val borMedSivilstand: Expression<BorMedSivilstand?>,
    val over2G: Expression<Boolean?>,
    val kronebelop2G: Expression<Kroner?>,
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
                    bokmal { +"Sivilstanden har betydning for pensjonen din" },
                    nynorsk { +"Sivilstanden har betydning for pensjonen din" },
                    english { +"Marital status affects your pension" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du har ektefelle, samboer eller partner, skal pensjonen din kontrolleres mot den andre partens inntekt." },
                    nynorsk { +"Om du har ektefelle, sambuar eller partnar, blir pensjonen din kontrollert mot inntekta til den andre parten." },
                    english { +"If you have a spouse or partner, your pension will be checked against their income." },
                )
            }

            ifNotNull(borMedSivilstand) { borMedSivilstand ->
                paragraph {
                    showIf(borMedSivilstand.isOneOf(BorMedSivilstand.GIFT_LEVER_ADSKILT, BorMedSivilstand.EKTEFELLE)) {
                        text(
                            bokmal { +"Du er registrert som gift." },
                            nynorsk { +"Du er registrert som gift." },
                            english { +" You are registered as married." }
                        )
                    }.orShow {
                        text(
                            bokmal { +"Du er registrert som " + borMedSivilstand.ubestemtForm() + "." },
                            nynorsk { +"Du er registrert som " + borMedSivilstand.ubestemtForm() + "." },
                            english { +" You are registered as " + borMedSivilstand.ubestemtForm() + "." }
                        )
                    }
                }
                showIf(borMedSivilstand.isNotAnyOf(BorMedSivilstand.GIFT_LEVER_ADSKILT)) {
                    paragraph {
                        text(
                            bokmal { +"Vi har registrert at " + borMedSivilstand.bestemtForm() + " " },
                            nynorsk { +"Vi har registrert at " + borMedSivilstand.bestemtForm() + " " },
                            english { +"We have registered that your " + borMedSivilstand.bestemtForm() + " " }
                        )
                        ifNotNull(over2G) { over2G ->
                            ifNotNull(kronebelop2G) { kronebelop2G ->
                                showIf(over2G) {
                                    text(
                                        bokmal { +"har inntekt over " + kronebelop2G.format() + " eller egen pensjon, uføretrygd eller omstillingsstønad." },
                                        nynorsk { +"har inntekt over " + kronebelop2G.format() + " eller eigen pensjon, uføretrygd eller omstillingsstønad." },
                                        english { +"has an income of over " + kronebelop2G.format() + " or their own pension, disability benefit or adjustment allowance." })

                                }.orShow {
                                    text(
                                        bokmal { +"ikke har egen pensjon, uføretrygd eller omstillingsstønad og heller ikke inntekt over  " + kronebelop2G.format() + "." },
                                        nynorsk { +"ikkje har eigen pensjon, uføretrygd eller omstillingsstønad og heller ikkje inntekt over " + kronebelop2G.format() + "." },
                                        english { +"does not have their own pension, disability benefit or adjustment allowance nor an income of over " + kronebelop2G.format() + "." })

                                }
                            }
                        }
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Vi har registrert at du og ektefellen din er registrert med forskjellig bosted, eller en av dere bor på institusjon." },
                            nynorsk { +"Vi har registrert at du og ektefellen din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon." },
                            english { +"We have registered that you and your spouse are registered at different residences, or that one of you is living in an institution." }
                        )
                    }
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Du er registrert som enslig." },
                        nynorsk { +"Du er registrert som einsleg." },
                        english { +"You are registered as single." }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Hvis dette ikke er riktig, må du kontakte oss." },
                    nynorsk { +"Om dette ikkje er rett, må du kontakte oss." },
                    english { +"If this is incorrect, please contact us. " })
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
                    bokmal { +"For deg som har forsørgingstillegg" },
                    nynorsk { +"Forsørgingstillegg" },
                    english { +"If you have dependant supplement" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Reglene for forsørgingstillegg er endret, og du vil derfor ikke få forsørgingstillegg/ barnetillegg i alderspensjonen din." },
                    nynorsk { +"Reglande for forsørgingstillegg er endra, og du vil derfor ikkje få forsørgingstillegg/ barnetillegg i alderspensjonen din." },
                    english { +"The regulations regarding dependant supplement are changed. Therefore, you will not receive a dependant or child supplement in your retirement pension." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du finner mer informasjon om dette på $FORSOERGINGSTILLEGG_URL." },
                    nynorsk { +"Du finn meir informasjon om dette på $FORSOERGINGSTILLEGG_URL." },
                    english { +"You can find more information at $FORSOERGINGSTILLEGG_URL." },
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
                    bokmal { +"Har du rett til AFP i privat sektor?" },
                    nynorsk { +"Har du rett til AFP i privat sektor?" },
                    english { +"Are you entitled to a contractual pension in the private sector (AFP)?" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +
                        "Hvis du er ansatt i en privat virksomhet og har rett til AFP, må du søke om " +
                                "dette samtidig med alderspensjonen fra folketrygden."
                    },
                    nynorsk {
                        +
                        "Dersom du er tilsett i ei privat verksemd og har rett til AFP, må du søkje om " +
                                "dette samtidig med alderspensjonen frå folketrygda."
                    },
                    english {
                        +
                        "If you are employed in a private business and are entitled to a contractual early " +
                                "retirement pension (AFP), you must apply for it at the same time as you apply " +
                                "for retirement pension from the National Insurance Scheme."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"AFP for ansatte i en privat virksomhet blir ikke redusert hvis du har inntekt. " +
                                "Du kan bruke pensjonskalkulatoren på $NAV_URL " +
                                "for å se hvor mye du får i AFP, sammen med alderspensjonen."
                    },
                    nynorsk {
                        +"AFP for tilsette i ei privat verksemd blir ikkje redusert dersom du har inntekt. " +
                                "Du kan bruke pensjonskalkulatoren på $NAV_URL " +
                                "for å sjå kor mykje du får i AFP, saman med alderspensjonen."
                    },
                    english {
                        +"AFP for employees in a private business is not reduced if you have income. " +
                                "You can use the pension calculator at $NAV_URL " +
                                "to see how much AFP you will receive in addition to your retirement pension."
                    },
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
                    bokmal { +"Når må du søke?" },
                    nynorsk { +"Når må du søkje?" },
                    english { +"When to apply?" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +
                        "Vi kan tidligst innvilge søknaden din fra og med måneden etter at du søkte. " +
                                "Du bør søke i god tid, men tidligst fire måneder før du ønsker at utbetalingen skal starte."
                    },
                    nynorsk {
                        +
                        "Vi kan tidlegast innvilge søknaden din frå og med månaden etter at du søkte. " +
                                "Du bør derfor søkje i god tid, men tidlegast fire månader før du ønskjer at utbetalinga skal starte."
                    },
                    english {
                        +
                        "Retirement pension from the National Insurance Scheme can, at the earliest, " +
                                "be paid out the month after we receive your application. " +
                                "Therefore, you should apply well in advance of the month that you want to start " +
                                "receiving your pension, but at the earliest four months in advance."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +
                        "Eksempel: Hvis du ønsker første utbetaling av alderspensjon i august, kan du " +
                                "tidligst sende søknaden i april."
                    },
                    nynorsk {
                        +
                        "Eksempel: Dersom du ønskjer første utbetaling av alderspensjon i august, kan du " +
                                "tidlegast sende søknaden i april."
                    },
                    english {
                        +
                        "Example: If you want to start receiving your retirement pension in August you " +
                                "can at the earliest send your application in April."
                    },
                )
            }
            title1 {
                text(
                    bokmal { +"Hvordan søker du om alderspensjon?" },
                    nynorsk { +"Korleis søkjer du om alderspensjon?" },
                    english { +"How do you apply for retirement pension?" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +
                        "Du kan kan søke på $DIN_PENSJON_URL_INNLOGGET eller bruke søknadsskjema for " +
                                "alderspensjon som du også finner på $NAV_URL."
                    },
                    nynorsk {
                        +
                        "Du kan søkje på $DIN_PENSJON_URL_INNLOGGET eller bruke søknadsskjemaet om " +
                                "alderspensjon som du også finn på $NAV_URL."
                    },
                    english {
                        +
                        "You can apply at $DIN_PENSJON_URL_INNLOGGET, or use the " +
                                "application form for retirement pension, which is also found at $NAV_URL."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan bruke pensjonskalkulatoren på $NAV_URL til å beregne alderspensjonen din." },
                    nynorsk { +"Du kan bruke pensjonskalkulatoren på $NAV_URL til å berekne alderspensjon din." },
                    english {
                        +
                        "You can use the pension calculator at $NAV_URL to calculate your future retirement pension."
                    },
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
                    bokmal { +"Ønsker du å ta ut en annen grad enn 100 prosent alderspensjon?" },
                    nynorsk { +"Ønskjer du å ta ut ein annan grad enn 100 prosent alderspensjon?" },
                    english { +"Do you want to draw retirement pension at another rate than 100 percent?" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Vi regner om uføretrygden din til 100 prosent alderspensjon. Du kan likevel velge" },
                    nynorsk { +"Vi reknar om uføretrygda di til 100 prosent alderspensjon. Du kan likevel velje" },
                    english {
                        +
                        "Your disability benefit will be converted into a 100 percent retirement " +
                                "pension. You can nevertheless select one of the following options:"
                    },
                )
                list {
                    item {
                        text(
                            bokmal { +"å ta ut deler av pensjonen din (20, 40 ,50, 60 eller 80 prosent)" },
                            nynorsk { +"å ta ut delar av pensjonen din (20, 40, 50, 60 eller 80 prosent)" },
                            english { +"Start drawing a part of your pension at a rate of 20, 40, 50, 60 or 80 percent." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"å stanse eller endre pensjonen din på et senere tidspunkt" },
                            nynorsk { +"å stanse eller endre pensjonen din på eit seinare tidspunkt" },
                            english { +"Stop or change your retirement pension at a later date." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"å vente med å ta ut alderspensjonen" },
                            nynorsk { +"å vente med å ta ut alderspensjonen" },
                            english { +"Postpone drawing your retirement pension." },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal {
                        +
                        "Hvis du ønsker en annen uttaksgrad enn 100 prosent alderspensjon, kan du søke " +
                                "om dette elektronisk via $DIN_PENSJON_URL_INNLOGGET. " +
                                "Hvis du ikke kan benytte denne løsningen, finner du søknadsskjema for alderspensjon på $NAV_URL."
                    },
                    nynorsk {
                        +
                        "Dersom du ønskjer ein annan uttaksgrad enn 100 prosent alderspensjon, kan du " +
                                "søkje om dette elektronisk via $DIN_PENSJON_URL_INNLOGGET. " +
                                "Dersom du ikkje kan bruke denne løysinga, så finn du søknadsskjema  for alderspensjon på $NAV_URL."
                    },
                    english {
                        +
                        "If you want to draw retirement pension at another rate than 100 percent, you " +
                                "can apply electronically by logging on to $DIN_PENSJON_URL_INNLOGGET. " +
                                "Or you can use the application form for retirement pension, which is also found at $NAV_URL."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Ønsker du å endre alderspensjonen, må vi få søknaden senest måneden før du ønsker endringen." },
                    nynorsk { +"Ønskjer du å endre alderspensjonen, må vi få søknaden seinast månaden før du ønskjer endringa." },
                    english { +"Any change will be implemented at the earliest the month after we have received the application." },
                )
            }
        }
    }
}

// skattAPInformasjonsbrev_001
data class InfoSkattAP(
    val ytelseForAldersovergangKode: Expression<YtelseForAldersovergangKode>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Husk å endre skattekortet ditt" },
                nynorsk { +"Hugs å endre skattekortet ditt" },
                english { +"Remember to change your tax card" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +
                    "Du bør endre skattekortet når du begynner å ta ut alderspensjon. Ellers trekkes det " +
                            "30 prosent skatt av alderspensjonen."
                },
                nynorsk {
                    +
                    "Du bør endre skattekortet når du byrjar å ta ut alderspensjon. Elles blir det " +
                            "trekt 30 prosent skatt av alderspensjonen."
                },
                english {
                    +
                    "When you start drawing retirement pension, you should change your tax deduction card. " +
                            "Otherwise, 30 percent of the pension will be deducted from your pension."
                },
            )
        }
        showIf(ytelseForAldersovergangKode.isOneOf(YtelseForAldersovergangKode.UT, YtelseForAldersovergangKode.UT_GRAD, YtelseForAldersovergangKode.UT_AP_GRAD)) {
            paragraph {
                text(
                    bokmal { +"Du bør endre det etter siste utbetaling av uføretrygd, men ikke før." },
                    nynorsk { +"Du bør endre det etter siste utbetaling av uføretrygd, men ikkje før. " },
                    english { +"You should make the update after the last payment of disability benefit, but not before." },
                )
            }
        }

        paragraph {
            text(
                bokmal {
                    +"Du endrer skattekortet på $SKATTEETATEN_PENSJONIST_URL. " +
                            "Nav får skattekortet elektronisk. Du skal derfor ikke sende det til oss."
                },
                nynorsk {
                    +"Du endrar skattekortet på  $SKATTEETATEN_PENSJONIST_URL. " +
                            "Nav får skattekortet elektronisk. Du skal derfor ikkje sende det til oss."
                },
                english {
                    +"You can change your tax card at $SKATTEETATEN_PENSJONIST_URL. " +
                            "Nav receives your tax card electronically. Therefore, you should not send it to us."
                }
            )
        }


        paragraph {
            text(
                bokmal {
                    +"På $SKATTEETATEN_PENSJONIST_URL finner du også informasjon om skatt når du bor utenfor Norge. " +
                            "Du må selv avklare spørsmål om skatteplikt til det landet du bor i med skattemyndighetene der."
                },
                nynorsk {
                    +"På $SKATTEETATEN_PENSJONIST_URL finn du også informasjon om skatt når du bur utanfor Noreg. " +
                            "Du må sjølv avklare spørsmål om skatteplikt til det landet du bur i med skatteorgana der."
                },
                english {
                    +
                    "At $SKATTEETATEN_PENSJONIST_URL, you will also find information about tax liability to Norway after moving abroad. " +
                            "You must clarify questions about tax liability to your country of residence with the local tax authorities."
                },
            )
        }
    }
}

// infoBoddArbeidetUtland_001
object InfoArbeidsinntekt : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Arbeidsinntekt ved siden av alderspensjonen kan gi høyere pensjon" },
                nynorsk { +"Arbeidsinntekt ved sida av alderspensjonen kan gi høgare pensjon" },
                english { +"Work income alongside retirement pension can increase your pension" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Du kan ha arbeidsinntekt uten at pensjonen din blir redusert. " +
                            "Fram til og med det året du fyller 75 år, kan arbeidsinntekt gjøre at pensjonen din øker."
                },
                nynorsk {
                    +"Du kan ha arbeidsinntekt utan at pensjonen din blir redusert. " +
                            "Fram til og med det året du fyller 75 år, kan arbeidsinntekt gjere at pensjonen din aukar."
                },
                english {
                    +"You can have work income without your pension being reduced. " +
                            "Up to and including the year you turn 75, work income can increase your pension."
                },
            )
        }
    }
}

// infoBoddArbeidetUtland_001
object InfoBoddArbeidetUtlandet : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Har du bodd eller arbeidet i utlandet?" },
                nynorsk { +"Har du budd eller arbeidd i utlandet?" },
                english { +"Have you lived or worked abroad?" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Hvis du har bodd eller arbeidet i utlandet, kan dette få betydning for hvor mye du får utbetalt i pensjon. " +
                            "Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. " +
                            "Derfor kan du også ha rett til pensjon fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med."
                },
                nynorsk {
                    +
                    "Dersom du har budd eller arbeidd i utlandet, kan dette få noko å seie for kor mykje du får utbetalt i pensjon. " +
                            "Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. " +
                            "Derfor kan du også ha rett til pensjon frå andre land. " +
                            "Vi kan hjelpe deg med å søkje i land Noreg har trygdeavtale med."
                },
                english {
                    +"If you have lived or worked abroad, this may affect the amount of your pension. " +
                            "Norway cooperates with a number of countries through the EEA Agreement and other social " +
                            "security agreements. You may therefore also be entitled to a pension from other countries. " +
                            "We can assist you with your application to countries with which Norway has a social security agreement."
                },
            )
        }
    }
}

// pensjonFraAndreInfoAP_001
object InfoPensjonFraAndreAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +"Andre pensjonsordninger" },
                nynorsk { +"Andre pensjonsordningar" },
                english { +"Other pension schemes" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Tjenestepensjon fra privat eller offentlig pensjonsordning kan komme i tillegg til pensjonen fra Nav. " +
                            "Kontakt pensjonsordningen din hvis du har spørsmål om andre pensjonsordninger. "
                },
                nynorsk {
                    +"Tenestepensjon frå privat eller offentleg pensjonsordning kan komme i tillegg til pensjonen frå Nav. " +
                            "Kontakt pensjonsordninga di om du har spørsmål om andre pensjonsordningar. "
                },
                english {
                    +"Occupational pensions from private or public schemes may supplement your Nav pension. " +
                            "Contact your pension provider if you have questions about other pension schemes."
                },
            )
        }
    }
}

