package no.nav.pensjon.brev.alder.maler.aldersovergang.omregning

import no.nav.pensjon.brev.alder.maler.aldersovergang.omregning.fraser.Omregning2016Hjemler
import no.nav.pensjon.brev.alder.maler.felles.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.SUPPLERENDE_STOENAD_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaalAlder
import no.nav.pensjon.brev.alder.maler.felles.RettTilAAKlage
import no.nav.pensjon.brev.alder.maler.felles.bestemtForm
import no.nav.pensjon.brev.alder.maler.felles.ubestemtForm
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen.vedleggOpplysningerBruktIBeregningenAlder
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.model.BorMedSivilstand
import no.nav.pensjon.brev.alder.model.Sivilstand
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import java.time.LocalDate


data class OmregningAlderUfore2016Felles(
    val virkFom: Expression<LocalDate>,
    val uttaksgrad: Expression<Int>,
    val totalPensjon: Expression<Kroner>,
    val antallBeregningsperioder: Expression<Int>,
    val gjenlevendetilleggKap19Innvilget: Expression<Boolean>,
    val avdodNavn: Expression<String?>,
    val avdodFnr: Expression<String?>,
    val gjenlevenderettAnvendt: Expression<Boolean>,
    val gjenlevenderettInnvilget: Expression<Boolean>,
    val eksportTrygdeavtaleAvtaleland: Expression<Boolean>,
    val faktiskBostedsland: Expression<String?>,
    val erEksportberegnet: Expression<Boolean>,
    val eksportberegnetUtenGarantipensjon: Expression<Boolean>,
    val pensjonstilleggInnvilget: Expression<Boolean>,
    val garantipensjonInnvilget: Expression<Boolean>,
    val godkjentYrkesskade: Expression<Boolean>,
    val skjermingstilleggInnvilget: Expression<Boolean>,
    val garantitilleggInnvilget: Expression<Boolean>,
    val oppfyltVedSammenleggingKap19: Expression<Boolean>,
    val oppfyltVedSammenleggingKap20: Expression<Boolean>,
    val oppfyltVedSammenleggingFemArKap19: Expression<Boolean>,
    val oppfyltVedSammenleggingFemArKap20: Expression<Boolean>,
    val borINorge: Expression<Boolean>,
    val erEOSLand: Expression<Boolean>,
    val eksportTrygdeavtaleEOS: Expression<Boolean>,
    val avtaleland: Expression<String?>,
    val innvilgetFor67: Expression<Boolean>,
    val fullTrygdetid: Expression<Boolean>,
    val brukersSivilstand: Expression<Sivilstand>,
    val borMedSivilstand: Expression<BorMedSivilstand?>,
    val over2G: Expression<Boolean?>,
    val kronebelop2G: Expression<Kroner>,


    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                bokmal { +"Vedtak" },
                nynorsk { +"Vedtak" },
                english { +"Decision" }
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Fra " + virkFom.format() + " får du " + uttaksgrad.format() + " prosent alderspensjon. Du får " + totalPensjon.format() +
                            " i alderpensjon før skatt hver måned."
                },
                nynorsk {
                    +"Frå " + virkFom.format() + " får du " + uttaksgrad.format() + " prosent alderspensjon. Du får " + totalPensjon.format() +
                            " i alderspensjon før skatt kvar månad."
                },
                english {
                    +"From " + virkFom.format() + " you will receive " + uttaksgrad.format() + " percent retirement pension. You will receive " + totalPensjon.format() +
                            " in retirement pension before tax each month."
                }

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
                    +"Retirement pension is calculated according to different rules than the disability benefit. " +
                            "Therefore, the amount you receive in retirement pension may differ from what you received in disability benefit. "
                }

            )
        }

        paragraph {
            text(
                bokmal {
                    +"Du finner informasjon om hvordan alderspensjonen er satt sammen og beregnet " +
                            "i vedleggene "
                },
                nynorsk {
                    +"Du finn informasjon om korleis alderspensjonen er sett saman og berekna" +
                            " i vedlegga "
                },
                english {
                    +"You can find information about how your retirement pension " +
                            "is composed and calculated in the attachments "
                }
            )
            namedReference(vedleggMaanedligPensjonFoerSkatt)
            text(
                bokmal { +" og " },
                nynorsk { +" og " },
                english { +" and " }
            )
            namedReference(vedleggOpplysningerBruktIBeregningenAlder)
            text(
                bokmal { +"." },
                nynorsk { +"." },
                english { +"." }
            )
        }

        ifNotNull(avdodNavn) { avdodNavn ->
            showIf(gjenlevendetilleggKap19Innvilget) {
                title2 {
                    text(
                        bokmal { +"Gjenlevenderett i alderspensjon" },
                        nynorsk { +"Attlevenderett i alderspensjon" },
                        english { +"Survivor's rights in retirement pension" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du får et gjenlevendetillegg i alderspensjonen fordi du har pensjonsrettigheter etter " + avdodNavn + "."},
                        nynorsk { +"Du får eit attlevandetillegg i alderspensjonen fordi du har pensjonsrettar etter " + avdodNavn + "." },
                        english { +"You receive a survivor’s supplement in the retirement pension because you have pension rights after " + avdodNavn + "." }

                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Alderspensjonen er basert på din egen pensjonsopptjening. " +
                                    "Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, " +
                                    "og alderspensjon du har tjent opp selv."
                        },
                        nynorsk {
                            +"Alderspensjonen er basert på di eiga pensjonsopptening. " +
                                    "Attlevandetillegget er skilnaden mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, " +
                                    "og alderspensjon du har tent opp sjølv."
                        },
                        english {
                            +"The retirement pension is based on your own pension earnings. " +
                                    "The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, " +
                                    "and retirement pension you have earned yourself."
                        }
                    )
                }
            }
        }
        ifNotNull(avdodNavn) { avdodNavn ->
            showIf(gjenlevenderettAnvendt.not() and avdodFnr.notNull() and gjenlevenderettInnvilget) {
                title2 {
                    text(
                        bokmal { +"Gjenlevenderett i alderspensjon" },
                        nynorsk { +"Attlevenderett i alderspensjon" },
                        english { +"Survivor's rights in retirement pension" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"I beregningen vår har vi tatt utgangspunkt i din egen opptjening. Dette gir deg en høyere pensjon enn om vi hadde tatt utgangspunkt i pensjonsrettighetene du har etter " + avdodNavn + "." },
                        nynorsk { +"I vår berekning har vi teke utgangspunkt i di eiga opptening. Dette gir deg ein høgare pensjon enn om vi hadde teke utgangspunkt i pensjonsrettane du har etter " + avdodNavn + "."},
                        english { +"We have based our calculation on your own earnings. This gives you a higher pension than if we had based it on the pension rights you have after " + avdodNavn + "." }
                    )
                }
            }
        }

        ifNotNull(faktiskBostedsland) { faktiskBostedsland ->
            showIf(eksportTrygdeavtaleEOS) {
                paragraph {
                    text(
                        bokmal { +"Vi forutsetter at du bor i " + faktiskBostedsland + ". Hvis du skal flytte til et land utenfor EØS-området, må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon." },
                        nynorsk { +"Vi føreset at du bur i " + faktiskBostedsland + ". Dersom du skal flytte til eit land utanfor EØS-området, må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon." },
                        english { +"We presume that you live in " + faktiskBostedsland + ". If you are moving to a country outside the EEA region, it is important that you contact Nav We will then reassess your eligibility for retirement pension." }
                    )
                }
            }

            showIf(eksportTrygdeavtaleAvtaleland) {
                paragraph {
                    text(
                        bokmal { +"Vi forutsetter at du bor i " + faktiskBostedsland + ". Hvis du skal flytte til et annet land, må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon." },
                        nynorsk { +"Vi føreset at du bur i " + faktiskBostedsland + ". Dersom du skal flytte til eit anna land, må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon." },
                        english { +"We presume that you live in " + faktiskBostedsland + ". If you are moving to another country, it is important that you contact Nav We will then reassess your eligibility for retirement pension." }
                    )
                }
            }

            showIf(erEksportberegnet and eksportberegnetUtenGarantipensjon) {
                paragraph {
                    text(
                        bokmal {
                            +"For å ha rett til full alderspensjon når du bor i " + faktiskBostedsland + ", må du ha vært medlem i folketrygden i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikke rett til full pensjon. " +
                                    "I vedleggene finner du mer detaljerte opplysninger."
                        },
                        nynorsk {
                            +"For å ha rett til full alderspensjon når du bur i " + faktiskBostedsland + ", må du ha vore medlem i folketrygda i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikkje rett til full pensjon. " +
                                    "I vedlegga finn du meir detaljerte opplysningar."
                        },
                        english {
                            +"To be eligible for a full retirement pension while living in " + faktiskBostedsland + ", you must have been a member of the National Insurance scheme earning pension rights for at least 20 years. " +
                                    "You have been a member for less than 20 years, and are therefore not eligible for a full pension. " +
                                    "There is more detailed information in the attachments."
                        }
                    )
                }
            }
        }

        includePhrase(
            Omregning2016Hjemler(
                pensjonstilleggInnvilget,
                garantipensjonInnvilget,
                godkjentYrkesskade,
                oppfyltVedSammenleggingKap19,
                oppfyltVedSammenleggingKap20,
                oppfyltVedSammenleggingFemArKap19,
                oppfyltVedSammenleggingFemArKap20,
                eksportTrygdeavtaleEOS,
                borINorge,
                erEOSLand,
                skjermingstilleggInnvilget,
                gjenlevenderettAnvendt,
                garantitilleggInnvilget,
                eksportTrygdeavtaleAvtaleland,
                avtaleland,
            )
        )

        showIf(uttaksgrad.lessThan(100)) {

            title2 {
                text(
                    bokmal { +"Du har rett til hel (100 prosent) alderspensjon" },
                    nynorsk { +"Du har rett til heil (100 prosent) alderspensjon" },
                    english { +"You are entitled to a full (100 percent) retirement pension " }
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Du har fått alderspensjon med en uttaksgrad nærmest mulig uføregraden din. Hvis du ønsker hel alderspensjon, må du gi beskjed til Nav. " +
                                "Du kan endre pensjonen på $DIN_PENSJON_URL. " +
                                "Alderspensjonen kan tidligst endres fra måneden etter at du har søkt om endringen. "
                    },
                    nynorsk {
                        +"Du har fått alderspensjon med ein uttaksgrad nærast mogleg uføregraden din. Om du ønskjer heil alderspensjon, må du gi beskjed til Nav. " +
                                "Du kan endre pensjonen på $DIN_PENSJON_URL. " +
                                "Du kan tidlegast få endra alderspensjonen frå månaden etter at du har søkt om endringa. "
                    },
                    english {
                        +"You have been granted a retirement pension with a withdrawal rate as close as possible to your previous disability benefit rate. " +
                                "If you wish to receive a full retirement pension, you must notify Nav. " +
                                "You can change your pension at $DIN_PENSJON_URL. Changes can take effect from the month after you apply. "
                    }
                )
            }

            paragraph {
                text(
                    bokmal { +"I pensjonskalkulatoren på $NAV_URL kan du sjekke hvor mye du kan få i hel alderspensjon. " },
                    nynorsk { +"I pensjonskalkulatoren på $NAV_URL kan du sjekke kor mykje du kan få i heil alderspensjon. " },
                    english { +"You can use the pension calculator at $NAV_URL to check how much you can receive in full retirement pension. " }
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis du trenger hjelp til å beregne eller endre pensjonen, kan du ringe oss på telefon 55 55 33 34. " },
                    nynorsk { +"Om du treng hjelp til å berekne eller endre pensjonen, kan du ringe oss på telefon 55 55 33 34. " },
                    english { +"If you need help calculating or changing your pension, you can call us at 55 55 33 34. " }
                )
            }
        }

        title2 {
            text(
                bokmal { +"Sivilstanden har betydning for pensjonen din" },
                nynorsk { +"Sivilstanden har betydning for pensjonen din" },
                english { +"Marital status affects your pension " }
            )
        }

        paragraph {
            text(
                bokmal { +"Hvis du har ektefelle eller samboer, skal pensjonen din kontrolleres mot den andre partens inntekt. " },
                nynorsk { +"Om du har ektefelle eller sambuar, skal pensjonen din kontrollerast mot inntekta til den andre parten. " },
                english { +"If you have a spouse or partner, your pension will be assessed against their income." }
            )
        }


            ifNotNull(borMedSivilstand){ borMedSivilstand ->
                paragraph {
                    showIf(borMedSivilstand.isOneOf(BorMedSivilstand.GIFT_LEVER_ADSKILT, BorMedSivilstand.EKTEFELLE)) {
                        text(
                            bokmal { +"Du er registrert som gift." },
                            nynorsk { +"Du er registrert som gift." },
                            english { +" You are registered as married." }
                        )
                    }.orShow {
                        text(
                        bokmal { +"Du er registrert som " +borMedSivilstand.ubestemtForm()+"." },
                        nynorsk { +"Du er registrert som "+borMedSivilstand.ubestemtForm()+"." },
                        english { +" You are registered as "+borMedSivilstand.ubestemtForm()+"." }
                        )
                    }
                }
                showIf(borMedSivilstand.isNotAnyOf(BorMedSivilstand.GIFT_LEVER_ADSKILT)) {
                    paragraph {
                        text(
                            bokmal { +"Vi har beregnet alderspensjonen din ut ifra at " + borMedSivilstand.bestemtForm() + " " },
                            nynorsk { +"Vi har berekna alderspensjonen din ut ifrå at " + borMedSivilstand.bestemtForm() + " " },
                            english { +"We have calculated your retirement pension based on the assumption that your " + borMedSivilstand.bestemtForm() + " " }
                        )
                        ifNotNull(over2G) { over2G ->
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
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Vi har beregnet alderspensjonen din ut ifra at du og ektefellen din er registrert med forskjellig bosted, eller en av dere bor på institusjon." },
                            nynorsk { +"Vi har berekna alderspensjonen din ut ifra at du og ektefellen din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon." },
                            english { +"We have calculated your retirement pension based on the fact that you and your spouse are registered at different residences, or that one of you is living in an institution." }
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

        paragraph {
            text(
                bokmal { +"Du kan se mer om dette i vedlegget " },
                nynorsk { +"Du kan sjå meir om dette i vedlegget " },
                english { +"You can find more information in the attachment " })
            namedReference(vedleggOpplysningerBruktIBeregningenAlder)
            text(
                bokmal { +"." },
                nynorsk { +"." },
                english { +"." }
            )
        }

        title2 {
            text(
                bokmal { +"Du må melde fra om endringer" },
                nynorsk { +"Du må melde frå om endringar" },
                english { +"You must report changes" }
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Hvis du får endringer i familiesituasjon, planlegger opphold i utlandet, eller ektefellen eller samboeren din får endringer i inntekten, kan det påvirke hvor mye du får utbetalt fra Nav. " +
                            "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss riktige opplysninger, må du vanligvis betale tilbake pengene. " +
                            "Du kan se hvilke endringer du må si fra om i vedlegget om rettigheter."
                },
                nynorsk {
                    +"Om du får endringar i familiesituasjonen, planlegg opphald i utlandet, eller ektefellen eller sambuaren din får endra inntekt, kan det påverke kor mykje du får utbetalt frå Nav. " +
                            "Om du har fått utbetalt for mykje fordi du ikkje har gitt oss rette opplysningar, må du vanlegvis betale tilbake pengane. " +
                            "Du kan sjå kva endringar du må seie frå om i vedlegget om rettar."
                },
                english {
                    +"If your family situation changes, you plan to stay abroad, or your spouse/partner’s income changes, it may affect your pension payments from Nav. " +
                            "If you receive too much because you did not provide correct information, you may have to repay the excess amount. " +
                            "You can see which changes you are required to report in the attachment about your rights."
                }
            )
        }

        title2 {
            text(
                bokmal { +"Arbeidsinntekt ved siden av alderspensjonen kan gi høyere pensjon" },
                nynorsk { +"Arbeidsinntekt ved sida av alderspensjonen kan gi høgare pensjon" },
                english { +"Work income may increase your pension" }
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Du kan ha arbeidsinntekt uten at pensjonen din blir redusert. " +
                            "Fram til og med det året du fyller 75 år, kan arbeidsinntekt gjøre at pensjonen din øker. "
                },
                nynorsk {
                    +"Du kan ha arbeidsinntekt utan at pensjonen din blir redusert. " +
                            "Fram til og med det året du fyller 75 år, kan arbeidsinntekt gjere at pensjonen din aukar"
                },
                english {
                    +"You can have work income without your pension being reduced. " +
                            "Up to and including the year you turn 75, work income may increase your pension. "
                }
            )
        }

        title2 {
            text(
                bokmal { +"Du bør sjekke skattekortet ditt" },
                nynorsk { +"Du bør sjekke skattekortet ditt" },
                english { +"Remember to check your tax card" }
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Hvis du ikke har endret skattekortet ditt, trekkes det 30 prosent skatt av alderspensjonen. " +
                            "Du kan endre skattekortet og få mer informasjon på $SKATTEETATEN_PENSJONIST_URL. " +
                            "Nav får skattekortet elektronisk. Du skal derfor ikke sende det til oss."
                },
                nynorsk {
                    +"Om du ikkje har endra skattekortet ditt, blir det trekt 30 prosent skatt av alderspensjonen. " +
                            "Du kan endre skattekortet og få meir informasjon på $SKATTEETATEN_PENSJONIST_URL. " +
                            "Nav får skattekortet elektronisk. Du skal derfor ikkje sende det til oss."
                },
                english {
                    +"If you have not changed your tax card, 30 percent tax will be deducted from your retirement pension. " +
                            "You can change the tax card and find more information at $SKATTEETATEN_PENSJONIST_URL." +
                            " Nav receives your tax card electronically, so you do not need to send it to us."
                },
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
                            "Du må sjølv avklare spørsmål om skatteplikt til det landet du bur i med skattestyresmaktene der."
                },
                english {
                    +"If you live outside Norway, you must clarify tax obligations with the tax authorities in your country of residence."
                },
            )
        }

        title2 {
            text(
                bokmal { +"Utbetalinger" },
                nynorsk { +"Utbetalingar" },
                english { +"Payments" }
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL."
                },
                nynorsk {
                    +"Alderspensjonen din blir utbetalt innan den 20. kvar månad. Du finner oversikt over utbetalingane dine på $UTBETALINGER_URL."
                },
                english {
                    +"Your retirement pension is paid by the 20th of each month. You can find an overview of your payments at $UTBETALINGER_URL."
                },
            )
        }

        title2 {
            text(
                bokmal { +"Alderspensjonen din reguleres årlig" },
                nynorsk { +"Alderspensjonen din blir regulert årleg" },
                english { +"Your retirement pension will be adjusted annually" }
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Reguleringen skjer med virkning fra 1. mai og selve økningen blir vanligvis etterbetalt i juni. " +
                            "Du får informasjon om dette på utbetalingsmeldingen din. " +
                            "På $NAV_URL kan du lese mer om hvordan pensjonene reguleres."
                },
                nynorsk {
                    +"Reguleringa skjer med verknad frå 1. mai, og sjølve auken blir vanlegvis etterbetalt i juni. " +
                            "Du får informasjon om dette på utbetalingsmeldinga di. " +
                            "På $NAV_URL kan du lese meir om korleis pensjonane blir regulerte."
                },
                english {
                    +"Adjustments take effect from 1 May, and the increase is usually paid retroactively in June. " +
                            "You will receive information about this in your payout notice. " +
                            "You can read more about pension adjustments at $NAV_URL. "
                },
            )
        }

        showIf(gjenlevendetilleggKap19Innvilget) {
            paragraph {
                text(
                    bokmal { +"Gjenlevendetillegg skal ikke reguleres når pensjonen øker fra 1. mai hvert år." },
                    nynorsk { +"Attlevendetillegg skal ikkje regulerast når pensjonen aukar frå 1. mai kvart år." },
                    english { +"The survivor’s supplement will not be adjusted when the pension increases from May 1st each year." }
                )
            }

            showIf(uttaksgrad.equalTo(100)) {
                paragraph {
                    text(
                        bokmal { +"En eventuell økning vil gjelde fra 1. januar året etter at skatteoppgjøret ditt er ferdig." },
                        nynorsk { +"Ein eventuell auke vil gjelde frå 1. januar året etter at skatteoppgjeret ditt er ferdig." },
                        english { +"Any increase, if applicable, will take effect as of 1 January of the year after the tax assessment of your income is complete." }
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Den nye opptjeningen vil bli lagt til den utbetalte alderspensjonen din når du søker om endret uttaksgrad eller ny beregning av den uttaksgraden du har nå." },
                        nynorsk { +"Den nye oppteninga blir lagd til den utbetalte alderspensjonen din når du søkjer om endra uttaksgrad eller ny berekning av den uttaksgraden du har no." },
                        english { +"Any new accumulation of rights will be added to your pension payments when you apply to have your retirement percentage amended or apply to have your pension recalculated at your current retirement percentage." }
                    )
                }
            }
        }

        title2 {
            text(
                bokmal { +"Andre pensjonsordninger" },
                nynorsk { +"Andre pensjonsordningar" },
                english { +"Other pension schemes" }
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Tjenestepensjon fra privat eller offentlig pensjonsordning kan komme i tillegg til pensjonen fra Nav. " +
                            "Kontakt pensjonsordningen din hvis du har spørsmål om andre pensjonsordninger."
                },
                nynorsk {
                    +"Tenestepensjon frå privat eller offentleg pensjonsordning kan komme i tillegg til pensjonen frå Nav. " +
                            "Kontakt pensjonsordninga di om du har spørsmål om andre pensjonsordningar."
                },
                english {
                    +"Occupational pensions from private or public schemes may supplement your Nav pension. " +
                            "Contact your pension provider if you have questions about other pension schemes."
                }
            )
        }

        showIf(
            innvilgetFor67.not()
                    and uttaksgrad.equalTo(100)
                    and fullTrygdetid.not()
                    and borINorge
        ) {

            title2 {
                text(
                    bokmal { +"Supplerende stønad" },
                    nynorsk { +"Supplerande stønad" },
                    english { +"Supplementary benefit" }
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Hvis du har kort botid i Norge når du fyller 67 år, kan du søke om supplerende stønad. " +
                                "Stønaden er behovsprøvd og all inntekt fra Norge og utlandet blir regnet med. " +
                                "Inntekten til eventuell ektefelle, samboer eller registrert partner blir også regnet med. " +
                                "Du kan lese mer om supplerende stønad på $SUPPLERENDE_STOENAD_URL."
                    },
                    nynorsk {
                        +"Dersom du har kort butid i Noreg når du fyller 67 år, kan du søkje om supplerande stønad. " +
                                "Stønaden er behovsprøvd, og all inntekt frå Noreg og utlandet blir rekna med. " +
                                "Inntekta til eventuell ektefelle, sambuar eller registrert partnar skal også reknast med. " +
                                "Du kan lese meir om supplerande stønad på $SUPPLERENDE_STOENAD_URL."
                    },
                    english {
                        +"If you have only lived a short period in Norway before reaching 67 years of age, you can apply for supplementary benefit. " +
                                "The benefit is means-tested and your total income from Norway and abroad is taken into account. " +
                                "The income of any spouse, cohabitant or registered partner will also be taken into account. " +
                                "You can read more about supplementary benefit at $SUPPLERENDE_STOENAD_URL."
                    },
                )
            }
        }

        includePhrase(RettTilAAKlage)
        includePhrase(HarDuSpoersmaalAlder)

    }
}