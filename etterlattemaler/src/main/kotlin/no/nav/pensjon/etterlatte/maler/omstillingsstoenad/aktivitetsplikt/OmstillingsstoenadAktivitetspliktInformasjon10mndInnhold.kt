package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadAktivitetspliktFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTOSelectors.aktivitetsgrad
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTOSelectors.halvtGrunnbeloep
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTOSelectors.nasjonalEllerUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTOSelectors.redusertEtterInntekt
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTOSelectors.utbetaling

data class OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO(
    val aktivitetsgrad: Aktivitetsgrad,
    val utbetaling: Boolean,
    val redusertEtterInntekt: Boolean,
    val nasjonalEllerUtland: NasjonalEllerUtland,
    val halvtGrunnbeloep: Kroner
) : RedigerbartUtfallBrevDTO


@TemplateModelHelpers
object OmstillingsstoenadAktivitetspliktInformasjon10mndInnhold :
    EtterlatteTemplate<OmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO> {
    override val kode: EtterlatteBrevKode =
        EtterlatteBrevKode.AKTIVITETSPLIKT_INFORMASJON_10MND_INNHOLD

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata =
        LetterMetadata(
            displayTitle = "Informasjon om omstillingsstønaden din",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Informasjon om omstillingsstønaden din" },
                nynorsk { +"Informasjon om omstillingsstønaden din" },
                english { +"Information about your adjustment allowance" },
            )
        }

        outline {
            showIf(not(utbetaling)) {
                paragraph {
                    text(
                        bokmal { +"Du er innvilget omstillingsstønad. Denne er i dag redusert etter en forventet " +
                                "inntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET> kroner. Du får derfor ikke utbetalt omstillingsstønad." },
                        nynorsk { +"Du er innvilga omstillingsstønad. Denne har i dag blitt redusert ut frå ei " +
                                "forventa inntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET> kroner. Du får difor ikkje utbetalt omstillingsstønad." },
                        english { +"You are granted adjustment allowance. Your adjustment allowance is reduced based " +
                                "on your income from employment of NOK <FORVENTET INNTEKT TOTALT, AVRUNDET> per year. " +
                                "You will therefore not receive any adjustment allowance." },
                    )
                }
            } orShow {
                paragraph {
                    text(
                        bokmal { +"Du mottar omstillingsstønad. Du får utbetalt <BELØP> kroner per måned før skatt." },
                        nynorsk { +"Du mottar omstillingsstønad. Du får utbetalt <BELØP> kroner i stønad kvar månad før skatt." },
                        english { +"You are receiving an adjustment allowance.  You will receive NOK <AMOUNT> each month before tax." },
                    )
                }
            }

            showIf(redusertEtterInntekt.and(utbetaling)) {
                paragraph {
                    text(
                        bokmal { +"Omstillingsstønaden din er redusert etter en forventet arbeidsinntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET> kroner i år. LEGG TIL ETTER KRONER HVIS INNVILGET FRA FEBRUAR-AUGUST: fra <måned> og ut året." },
                        nynorsk { +"Omstillingsstønaden din har blitt redusert ut frå ei forventa arbeidsinntekt på <FORVENTET INNTEKT TOTALT, AVRUNDET> kroner i år. LEGG TIL ETTER KRONER HVIS INNVILGET FRA FEBRUAR-AUGUST: frå <månad> og ut året." },
                        english { +"Your adjustment allowance is reduced based on your income from employment of NOK <FORVENTET INNTEKT TOTALT, AVRUNDET> this year. LEGG TIL ETTER BELØP HVIS INNVILGET FRA FEBRUAR-AUGUST: from <måned> until the end of this year." },
                    )
                }
            }.orShowIf(utbetaling) {
                paragraph {
                    text(
                        bokmal { +"Omstillingsstønaden din er i dag ikke redusert etter arbeidsinntekt eller annen inntekt som er likestilt med arbeidsinntekt." },
                        nynorsk { +"Omstillingsstønaden din er i dag ikkje redusert ut frå arbeidsinntekt eller anna inntekt som er likestilt med arbeidsinntekt." },
                        english { +"Your current adjustment allowance is not reduced based on income from employment or other income that is equivalent to income from employment." },
                    )
                }
            }

            title2 {
                text(
                    bokmal { +"Krav til aktivitet ved mottak av omstillingsstønad" },
                    nynorsk { +"Krav til aktivitet ved mottak av omstillingsstønad" },
                    english { +"Activity requirement upon receiving an adjustment allowance" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Når det er gått 12 måneder etter dødsfallet, kan det kreves at du er i 100 prosent arbeid eller annen aktivitet med sikte på å komme i arbeid for å motta omstillingsstønad." },
                    nynorsk { +"For at du framleis skal kunne få omstillingsstønad når det har gått 12 månader sidan dødsfallet, må du vere i 100 prosent arbeid eller annan aktivitet med sikte på å kome i arbeid." },
                    english { +"When 12 months have passed since the death, you may be required to work full time, or participate in other activity with the goal of returning to work, in order to receive adjustment allowance." },
                )
            }

            showIf(not(utbetaling)) {
                paragraph {
                    text(
                        bokmal { +"Siden du ikke mottar omstillingsstønad i dag, kan du se bort fra kravet om aktivitet. Det er imidlertid viktig at du melder fra hvis situasjonen din endrer seg. Aktivitetsplikt og din mulighet for å motta omstillingsstønad kan da vurderes." },
                        nynorsk { +"Ettersom du ikkje får omstillingsstønad per i dag, kan du sjå vekk frå kravet om aktivitet. Pass derimot på å melde frå dersom situasjonen din skulle endre seg. Aktivitetsplikt og eventuell rett på omstillingsstønad kan då vurderast." },
                        english { +"Since you are not currently receiving an adjustment allowance, you can disregard the activity requirement. It is, however, important that you notify us, should your situation change. We can then assess your activity requirement and your potential right to adjustment allowance." },
                    )
                }
            }

            showIf(utbetaling) {
                paragraph {
                    text(
                        bokmal { +"Det er registrert i omstillingsstønaden din at du FYLL INN OM SITUASJONEN TIL BRUKER, F.EKS. er i 80 prosent arbeid/er under utdanning/er registrert hos Nav som reell arbeidssøker." },
                        nynorsk { +"Det er registrert i omstillingsstønaden din at du FYLL INN OM SITUASJONEN TIL BRUKER, F.EKS. er i 80 prosent arbeid/er under utdanning/er registrert hos Nav som reell arbeidssøker." },
                        english { +"It is registered in your adjustment allowance that you FYLL INN OM SITUASJONEN TIL BRUKER, F.EKS. er i 80 prosent arbeid/er under utdanning/er registrert hos Nav som reell arbeidssøker." },
                    )
                }
            }

            showIf(aktivitetsgrad.notEqualTo(Aktivitetsgrad.AKKURAT_100_PROSENT) and utbetaling) {
                paragraph {
                    text(
                        bokmal { +"For å motta omstillingsstønad videre bør du øke aktiviteten din. Se “Hvordan oppfylle aktivitetsplikten?”.  Hvis du ikke foretar deg noen av de andre aktivitetene som er nevnt, bør du melde deg som reell arbeidssøker hos Nav. Dette innebærer at du bekrefter at du vil stå som arbeidssøker, er aktivt med å søke jobber, samt deltar på de kurs som Nav tilbyr. Du kan lese mer om å være arbeidssøker på ${Constants.REGISTRER_ARBEIDSSOKER}." },
                        nynorsk { +"For å kunne få omstillingsstønad vidare bør du auke aktiviteten din. Sjå «Korleis oppfyller du aktivitetsplikta?».  Dersom du ikkje gjer nokon av dei andre aktivitetane som er nemnde, bør du melde deg som reell arbeidssøkjar hos Nav. Dette inneber at du må bekrefte at du vil stå som arbeidssøkjar, vere aktiv med å søkje jobbar, og delta på kursa som Nav tilbyr. Du kan lese meir om å være arbeidssøkjar på ${Constants.REGISTRER_ARBEIDSSOKER}." },
                        english { +"To receive an adjustment allowance in the future, you should increase your level of activity. See “How do I comply with the activity obligation?”.  If you are not doing any of the other activities mentioned, you must register as a genuine job seeker with Nav. This means that you confirm that you want to be a job seeker, actively be looking for work, and participate in the courses offered by Nav. You can read more about being a job seeker at ${Constants.Engelsk.REGISTRER_ARBEIDSSOKER}." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Hvis du ikke kan registrere deg elektronisk må du møte opp på lokalkontoret ditt for å registrere deg som reell arbeidssøker." },
                        nynorsk { +"Dersom du ikkje kan registrere deg elektronisk, møter du opp på lokalkontoret ditt for å registrere deg som reell arbeidssøkjar." },
                        english { +"If there is any reason why you are unable to be a genuine job seeker or do something else that fulfils the activity obligation of 100 percent, you must send us documentation of this as soon as possible and no later than three weeks from the date of this letter. See “Exemption from the activity obligation” below." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Er det en grunn til at du ikke kan være reell arbeidssøker eller gjøre annet som oppfyller aktivitetsplikten på 100 prosent, må du sende oss dokumentasjon på dette snarest mulig og senest innen tre uker fra datoen på dette brevet. Se “Unntak for aktivitetsplikten” under." },
                        nynorsk { +"Viss det er ein grunn til at du ikkje kan vere reell arbeidssøkjar eller gjere anna som oppfyller aktivitetsplikta på 100 prosent, må du sende oss dokumentasjon på dette snarast mogleg og seinast innan tre veker frå datoen på dette brevet. Sjå «Unntak frå aktivitetsplikta» under." },
                        english { +"" }
                    )
                }

                showIf(aktivitetsgrad.equalTo(Aktivitetsgrad.OVER_50_PROSENT)) {
                    paragraph {
                        text(
                            bokmal { +"Hvis du jobber minst 50 prosent eller deltar i annen aktivitet med mål om å komme i arbeid, og mener du kan forsørge deg selv med den inntekten du har i nåværende stillingsprosent (uten å regne med omstillingsstønad), må du sende oss informasjon om dette." },
                            nynorsk { +"Dersom du jobbar minst 50 prosent, eller deltek i annan aktivitet med mål om å kome i arbeid, og meiner du kan forsørgje deg sjølv med inntekta du har i noverande stillingsprosent (utan å rekne med omstillingsstønad), må du sende oss informasjon om dette." },
                            english { +"If you work at least 50 percent or participate in another activity with the goal of obtaining employment, and believe you can support yourself with the income you have in your current position (without considering adjustment allowance), you must send us information about this." },
                        )
                    }
                }
            }

            showIf(aktivitetsgrad.equalTo(Aktivitetsgrad.AKKURAT_100_PROSENT) and utbetaling) {
                paragraph {
                    text(
                        bokmal { +"Er du fortsatt i full jobb eller annen aktivitet med sikte på å komme i arbeid, fyller du aktivitetskravet og vil få omstillingsstønad som før." },
                        nynorsk { +"Viss du framleis er i full jobb eller annan aktivitet med sikte på å kome i arbeid, oppfyller du aktivitetskravet og vil få omstillingsstønad som før." },
                        english { +"If you are still working full time or participating in other activity with the goal of returning to work, you are meeting the activity requirement and will continue to receive the adjustment allowance." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Hvis situasjonen din er endret, må du gi oss informasjon om din nye situasjon snarest mulig og senest innen tre uker fra datoen på dette brevet. Les mer om hvordan du kan fylle aktivitetsplikten og om unntak fra aktivitetsplikten lenger ned i brevet." },
                        nynorsk { +"Dersom situasjonen din har endra seg, må du gi oss informasjon om den nye situasjonen snarast mogleg og seinast innan tre veker frå datoen på dette brevet. Lenger ned i brevet kan du lese meir om korleis du kan oppfylle aktivitetsplikta, og kva som er unntaka frå aktivitetsplikta." },
                        english { +"If your situation has changed, you must provide us with information about your new situation as soon as possible and no later than three weeks from the date of this letter. Read more about how you can comply with the activity obligation and exemption from the activity obligation farther down in this letter." },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Du finner informasjon om hvordan du melder fra under “Du må melde fra om endringer”." },
                    nynorsk { +"Under «Du må melde frå om endringar» finn du meir informasjon om korleis du melder frå." },
                    english { +"You will find more information about how to notify us in the section “You must notify us about any changes”." },
                )
            }
            showIf(utbetaling) {
                paragraph {
                    text(
                        bokmal {
                            +"Hvis du trenger mer tid for å innhente dokumentasjon, må du kontakte oss snarest mulig " +
                                    "og senest innen tre uker fra datoen på dette brevet."
                        },
                        nynorsk {
                            +"Viss du treng meir tid for å innhente dokumentasjon, må du kontakte oss snarast mogleg " +
                                    "og seinast innan tre veker frå datoen på dette brevet."
                        },
                        english {
                            +"If you need more time to obtain documentation, you must contact us as soon as possible " +
                                    "and no later than three weeks from the date of this letter."
                        },
                    )
                }
            }

            title2 {
                text(
                    bokmal { +"Omstillingsstønaden skal reduseres etter inntekten din" },
                    nynorsk { +"Omstillingsstønaden skal reduserast ut frå inntekta di" },
                    english { +"The adjustment allowance is reduced according to your income" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Stønaden skal reduseres med 45 prosent av arbeidsinntekten din og inntekt som er " +
                            "likestilt med arbeidsinntekt, som er over halvparten av grunnbeløpet i folketrygden (G). " +
                            "Stønaden blir redusert ut fra det du oppgir som forventet inntekt for gjeldende år." },
                    nynorsk { +"Stønaden skal reduserast med 45 prosent av arbeidsinntekta di og inntekt som er " +
                            "likestilt med arbeidsinntekt, som er over halvparten av grunnbeløpet i folketrygda (G). " +
                            "Stønaden blir redusert ut frå det du oppgir som forventa inntekt for gjeldande år." },
                    english { +"The allowance will be reduced by 45 percent of your income from employment or other " +
                            "income that is equivalent to income from employment, if this is more than half of the basic " +
                            "amount in the national insurance (G). The allowance will be reduced based on what you declare " +
                            "as anticipated income for the current year." },
                )
            }

            includePhrase(OmstillingsstoenadAktivitetspliktFraser.FellesInfoOmInntektsendring(redusertEtterInntekt, halvtGrunnbeloep))
            includePhrase(OmstillingsstoenadAktivitetspliktFraser.FellesOppfyllelseAktivitetsplikt(nasjonalEllerUtland, true.expr()))
            includePhrase(OmstillingsstoenadAktivitetspliktFraser.FellesOppfyllelseUnntakFraAktivitetsplikt)
            includePhrase(OmstillingsstoenadAktivitetspliktFraser.TrengerDuHjelpTilAaFaaNyJobb)
            includePhrase(OmstillingsstoenadAktivitetspliktFraser.HarDuHelseutfordringer)
            includePhrase(OmstillingsstoenadAktivitetspliktFraser.DuMaaMeldeFraOmEndringer(nasjonalEllerUtland))
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }
    }
}
