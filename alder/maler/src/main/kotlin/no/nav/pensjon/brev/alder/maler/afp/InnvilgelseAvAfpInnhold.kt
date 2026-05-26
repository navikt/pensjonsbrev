package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpPrivatFraser
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Felles innhold for innvilgelse av AFP i privat sektor.
 *
 * Brukes av både [InnvilgelseAvAfpAuto] (autobrev, PE_AF_04_115) og
 * den redigerbare malen `InnvilgelseAvAfp` (PE_AF_04_111). Inneholder hele
 * brevkroppen, men ikke avsluttende `Har du spørsmål?`-blokk — den
 * inkluderes separat i hver mal.
 */
data class InnvilgelseAvAfpInnhold(
    val kravMottattDato: Expression<LocalDate>,
    val virkningFom: Expression<LocalDate>,
    val totalPensjon: Expression<Kroner>,
    val livsvarigBrutto: Expression<Kroner?>,
    val kronetilleggBrutto: Expression<Kroner?>,
    val kompensasjonstilleggBrutto: Expression<Kroner?>,
    val brukerUnder70Aar: Expression<Boolean>,
    val bosattINorge: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Nav viser til søknaden din om AFP i privat sektor mottatt " +
                        kravMottattDato.format() +
                        ". Du har valgt å ta ut AFP fra " + virkningFom.format() +
                        ". Du får " + totalPensjon.format() +
                        " hver måned før skatt."
                },
                nynorsk {
                    +"Nav viser til søknaden din om AFP i privat sektor motteken " +
                        kravMottattDato.format() +
                        ". Du har valt å ta ut AFP frå " + virkningFom.format() +
                        ". Du får " + totalPensjon.format() +
                        " kvar månad før skatt."
                },
                english {
                    +"Nav has granted your application which we received on " +
                        kravMottattDato.format() +
                        " for contractual pension in the private sector. You have decided to draw " +
                        "contractual pension starting on " + virkningFom.format() +
                        ". You will receive " + totalPensjon.format() + " per month before tax."
                },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"AFP i privat sektor gis etter bestemmelsene i lov om statstilskott til arbeidstakere som tar " +
                        "ut avtalefestet pensjon i privat sektor (AFP-tilskottsloven). Fellesordningen for AFP har " +
                        "funnet at du oppfyller de avtalemessige vilkårene for rett til AFP. Nav har avgjort andre " +
                        "spørsmål om retten til pensjon, blant annet beregningen. Beregningsreglene står i " +
                        "paragrafene 6 til 11 i AFP-tilskottsloven."
                },
                nynorsk {
                    +"AFP i privat sektor blir gitt etter føresegnene i lov om statstilskot til arbeidstakarar som " +
                        "tek ut avtalefesta pensjon i privat sektor (AFP-tilskotslova). Fellesordninga for AFP har " +
                        "funne at du oppfyller dei avtalemessige vilkåra for rett til AFP. Nav har avgjort andre " +
                        "spørsmål om retten til pensjon, mellom anna berekninga. Berekningsreglane står i " +
                        "paragrafane 6 til 11 i AFP-tilskotslova."
                },
                english {
                    +"Private sector contractual pension is granted pursuant to the act relating to state subsidies " +
                        "to employees who draw an early retirement pension in the private sector (the Early " +
                        "Retirement Pension Subsidy Act - AFP-tilskottsloven). The Common Scheme for Contractual " +
                        "Pension has ascertained that you meet the contractual terms for the right to AFP. Nav has " +
                        "made a decision on other issues regarding the right to pension, including the calculation. " +
                        "The calculation rules are listed in sections 6 to 11 of the Early Retirement Pension " +
                        "Subsidy Act."
                },
            )
        }

        // Tabell: "Beløp per måned" — restaurert fra de flate text(...)-blokkene i konverteren
        // (Step 5 i convert-exstream-letter). Konvertert til ekte table med betinget rad per komponent.
        paragraph {
            text(
                bokmal { +"Din AFP per måned blir slik:" },
                nynorsk { +"AFP-en din per månad blir slik:" },
                english { +"Your monthly contractual pension will be:" },
            )
            table(
                header = {
                    column {
                        text(
                            bokmal { +"Beløp per måned" },
                            nynorsk { +"Beløp per månad" },
                            english { +"Amount per month" },
                        )
                    }
                    column(alignment = ColumnAlignment.RIGHT) {
                        text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
                    }
                },
            ) {
                ifNotNull(livsvarigBrutto) { brutto ->
                    row {
                        cell {
                            text(
                                bokmal { +"AFP livsvarig del" },
                                nynorsk { +"AFP livsvarig del" },
                                english { +"Contractual pension, lifelong amount" },
                            )
                        }
                        cell { includePhrase(KronerText(brutto)) }
                    }
                }
                ifNotNull(kronetilleggBrutto) { brutto ->
                    row {
                        cell {
                            text(
                                bokmal { +"AFP kronetillegg" },
                                nynorsk { +"AFP-kronetillegg" },
                                english { +"Contractual pension, NOK supplement" },
                            )
                        }
                        cell { includePhrase(KronerText(brutto)) }
                    }
                }
                ifNotNull(kompensasjonstilleggBrutto) { brutto ->
                    row {
                        cell {
                            text(
                                bokmal { +"AFP kompensasjonstillegg (skattefritt)" },
                                nynorsk { +"AFP-kompensasjonstillegg (skattefritt)" },
                                english { +"Contractual pension, compensation supplement (tax-free)" },
                            )
                        }
                        cell { includePhrase(KronerText(brutto)) }
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { +"Sum AFP før skatt" },
                            nynorsk { +"Sum AFP før skatt" },
                            english { +"Total contractual pension before tax" },
                            BOLD,
                        )
                    }
                    cell { includePhrase(KronerText(totalPensjon, BOLD)) }
                }
            }
        }

        // Levealdersjustering — felles tekst for alle.
        paragraph {
            text(
                bokmal {
                    +"AFP justeres i forhold til forventet levealder ved tidspunkt for uttak. Levealdersjustering " +
                        "er en mekanisme som tar høyde for økt levealder i befolkningen og er innført for å sikre " +
                        "at pensjonssystemet forblir bærekraftig. Du kan lese mer om levealdersjustering på $NAV_URL."
                },
                nynorsk {
                    +"AFP blir justert i forhold til forventa levealder ved tidspunktet for uttak. Levealdersjustering " +
                        "er ein mekanisme som tek høgd for auka levealder i befolkninga, og er innført for å sikre " +
                        "at pensjonssystemet held seg berekraftig. Du kan lese meir om levealdersjustering på $NAV_URL."
                },
                english {
                    +"Contractual pension is adjusted according to your life expectancy on the pension date. " +
                        "Adjusting for life expectancy is a mechanism to cope with the increased age of the " +
                        "population and has been introduced to make sure that the pension system remains " +
                        "sustainable. You can find more information about life expectancy adjustment on $NAV_URL."
                },
            )
        }

        // Per-komponent forklaring (én paragraph per innvilget komponent — null = ikke innvilget).
        ifNotNull(livsvarigBrutto) {
            paragraph {
                text(
                    bokmal { +"AFP livsvarig del" },
                    nynorsk { +"AFP livsvarig del" },
                    english { +"The contractual pension, lifelong amount," },
                    fontType = BOLD,
                )
                text(
                    bokmal {
                        +" er beregnet ut fra opptjeningen din. Grunnlaget er opptjening du har " +
                            "hatt til og med det året du fylte 61 år."
                    },
                    nynorsk {
                        +" er berekna ut frå oppteninga di. Grunnlaget er opptening du har hatt " +
                            "til og med det året du fylte 61 år."
                    },
                    english {
                        +" has been calculated based on your pensionable earnings. The basis is your " +
                            "earnings until the year you turned 61."
                    },
                )
            }
        }
        ifNotNull(kronetilleggBrutto) {
            paragraph {
                text(
                    bokmal { +"AFP kronetillegg" },
                    nynorsk { +"AFP-kronetillegg" },
                    english { +"The contractual pension NOK supplement" },
                    fontType = BOLD,
                )
                text(
                    bokmal {
                        +" utbetales bare til og med den måneden du fyller 67 år. Opphør av " +
                            "kronetillegget har sammenheng med at obligatorisk tjenestepensjon i private " +
                            "arbeidsforhold blir utbetalt fra fylte 67 år."
                    },
                    nynorsk {
                        +" blir berre utbetalt til og med den månaden du fyller 67 år. Opphøyr " +
                            "av kronetillegget har samanheng med at obligatorisk tenestepensjon i private " +
                            "arbeidsforhold blir utbetalt frå fylte 67 år."
                    },
                    english {
                        +" will only be paid until the month that you turn 67. Termination of the NOK " +
                            "supplement is linked to mandatory occupational pension for private employment " +
                            "relationships being paid from the age of 67."
                    },
                )
            }
        }
        ifNotNull(kompensasjonstilleggBrutto) {
            paragraph {
                text(
                    bokmal { +"AFP kompensasjonstillegg" },
                    nynorsk { +"AFP-kompensasjonstillegg" },
                    english { +"The contractual pension compensation supplement" },
                    fontType = BOLD,
                )
                text(
                    bokmal {
                        +" får du fordi du er født før 1963. Personer født før 1963 har " +
                            "ved fortsatt arbeid ikke samme mulighet til å øke grunnlaget for sin alderspensjon " +
                            "fra folketrygden som de som er født senere."
                    },
                    nynorsk {
                        +" får du fordi du er fødd før 1963. Personar som er fødde før " +
                            "1963, har ved vidare arbeid ikkje det same høvet til å auke grunnlaget for " +
                            "alderspensjonen sin frå folketrygda som dei som er fødde seinare."
                    },
                    english {
                        +" has been granted to you because you were born before 1963. People born before " +
                            "1963 who are still working do not have the same opportunities to increase the basis " +
                            "for their national insurance retirement pension as those who were born later."
                    },
                )
            }
        }

        // AFP og alderspensjon fra folketrygden.
        title1 {
            text(
                bokmal { +"AFP i privat sektor og alderspensjon fra folketrygden" },
                nynorsk { +"AFP i privat sektor og alderspensjon frå folketrygda" },
                english { +"Private sector contractual pension and national insurance retirement pension" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"AFP i privat sektor gis sammen med alderspensjonen din. Størrelsen på din AFP er uavhengig " +
                        "av uttaksgraden på alderspensjonen. Utbetaling av AFP vil fortsette også dersom du velger " +
                        "å sette uttaksgraden av alderspensjonen din til null."
                },
                nynorsk {
                    +"AFP i privat sektor blir gitt saman med alderspensjonen din. Storleiken på AFP-en din er " +
                        "uavhengig av uttaksgraden på alderspensjonen. Utbetaling av AFP vil halde fram også " +
                        "dersom du vel å setje uttaksgraden av alderspensjonen din til null."
                },
                english {
                    +"Private sector contractual pension is granted together with your retirement pension. The " +
                        "size of your contractual pension is independent of your retirement pension level. Payment " +
                        "of contractual pension will also continue if you decide to set your retirement pension " +
                        "level at zero."
                },
            )
        }

        // Opptjening etter 61 år (kun for brukere under 70 år).
        showIf(brukerUnder70Aar) {
            paragraph {
                text(
                    bokmal {
                        +"Din AFP er fastsatt på grunnlag av opptjening som du har hatt til og med det året du " +
                            "fylte 61 år. Senere opptjening vil ikke ha noen innvirkning på størrelsen på din AFP. " +
                            "Dersom du har inntekt i årene etter uttak av alderspensjon kan alderspensjonen fra " +
                            "folketrygden øke - dette gjelder til og med det året du fyller 75."
                    },
                    nynorsk {
                        +"AFP-en din er fastsett på grunnlag av opptening som du har hatt til og med det året du " +
                            "fylte 61 år. Seinare opptening vil ikkje ha nokon innverknad på storleiken på AFP-en " +
                            "din. Dersom du har inntekt i åra etter uttak av alderspensjon, kan alderspensjonen " +
                            "frå folketrygda auke - det gjeld til og med det året du fyller 75."
                    },
                    english {
                        +"Your contractual pension has been set based on your earnings up to and including the " +
                            "year you turned 61. Later earnings will not have any effect on the size of your " +
                            "contractual pension. If you have an income during the years after drawing retirement " +
                            "pension, your national insurance retirement pension will increase - this applies up " +
                            "to and including the year you turn 75."
                    },
                )
            }
        }

        paragraph {
            text(
                bokmal {
                    +"Som ved alderspensjon fra folketrygden kan du arbeide så mye du vil uten at din AFP blir " +
                        "redusert."
                },
                nynorsk {
                    +"Som ved alderspensjon frå folketrygda kan du arbeide så mykje du vil, utan at AFP-en din " +
                        "blir redusert."
                },
                english {
                    +"As with your national insurance retirement pension, you can work as much as you want " +
                        "without a reduction in your contractual pension."
                },
            )
        }

        // Utbetaling — overskrift + tekst.
        title1 {
            text(
                bokmal { +"Din AFP utbetales månedlig" },
                nynorsk { +"AFP-en din blir utbetalt månadleg" },
                english { +"Your contractual pension will be paid once a month" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Din AFP blir vanligvis utbetalt den 20. hver måned. Når den 20. er en lørdag eller offentlig " +
                        "fridag blir pensjonen utbetalt senest siste virkedag før den 20. Oversikt over " +
                        "utbetalingsdatoer finner du på $NAV_URL."
                },
                nynorsk {
                    +"AFP-en din blir vanlegvis utbetalt den 20. kvar månad. Når den 20. er ein laurdag eller " +
                        "offentleg fridag, blir pensjonen utbetalt seinast siste yrkedagen før den 20. Oversikt " +
                        "over utbetalingsdatoar finn du på $NAV_URL."
                },
                english {
                    +"Your contractual pension will normally be paid on the 20th of each month. When the 20th is " +
                        "a Saturday or public holiday, your pension will be paid at latest on the last business " +
                        "day before the 20th. You can find a list of payment dates on $NAV_URL."
                },
            )
        }

        // Skattepliktig — bosatt i Norge.
        showIf(bosattINorge) {
            paragraph {
                ifNotNull(kompensasjonstilleggBrutto) {
                    text(
                        bokmal { +"Med unntak av kompensasjonstillegget er din AFP skattepliktig. " },
                        nynorsk { +"Med unntak av kompensasjonstillegget er AFP-en din skattepliktig. " },
                        english { +"Your contractual pension is taxable, except for the compensation supplement. " },
                    )
                }.orShow {
                    text(
                        bokmal { +"Din AFP er skattepliktig. " },
                        nynorsk { +"AFP-en din er skattepliktig. " },
                        english { +"Your contractual pension is taxable. " },
                    )
                }
                text(
                    bokmal {
                        +"Du trenger ikke levere skattekort da skatteopplysningene dine sendes elektronisk fra " +
                            "Skatteetaten til Nav. Ta kontakt med skattekontoret ditt hvis du har spørsmål om " +
                            "skatt og skattekort."
                    },
                    nynorsk {
                        +"Du treng ikkje levere skattekort då skatteopplysningane dine blir sende elektronisk frå " +
                            "Skatteetaten til Nav. Ta kontakt med skattekontoret ditt dersom du har spørsmål om " +
                            "skatt og skattekort."
                    },
                    english {
                        +"You do not need to submit your tax card, as Nav receives your tax information " +
                            "electronically from the Norwegian Tax Administration. Contact your local tax office " +
                            "if you have any questions about tax and tax cards."
                    },
                )
            }
        }

        // Skattepliktig — bosatt i utlandet.
        showIf(not(bosattINorge)) {
            paragraph {
                ifNotNull(kompensasjonstilleggBrutto) {
                    text(
                        bokmal { +"Med unntak av kompensasjonstillegget er din AFP skattepliktig til Norge. " },
                        nynorsk { +"Med unntak av kompensasjonstillegget er AFP-en din skattepliktig til Noreg. " },
                        english {
                            +"Your contractual pension is taxable to Norway, except for the compensation supplement. "
                        },
                    )
                }.orShow {
                    text(
                        bokmal { +"Din AFP er skattepliktig til Norge. " },
                        nynorsk { +"AFP-en din er skattepliktig til Noreg. " },
                        english { +"Your contractual pension is taxable to Norway. " },
                    )
                }
                text(
                    bokmal {
                        +"Spørsmål om skatteplikt til Norge etter flytting til utlandet må rettes til " +
                            "skattekontoret i den kommunen du har flyttet fra. Spørsmål om skatteplikt til det " +
                            "landet du er bosatt i må du selv avklare med skattemyndighetene der."
                    },
                    nynorsk {
                        +"Spørsmål om skatteplikt til Noreg etter flytting til utlandet må rettast til " +
                            "skattekontoret i den kommunen du har flytta frå. Spørsmål om skatteplikt til det " +
                            "landet du er busett i, må du sjølv avklare med skattemyndigheitene der."
                    },
                    english {
                        +"Any questions regarding tax liability to Norway after you have moved abroad must be " +
                            "directed to the tax office in the Norwegian municipality from which you moved. You " +
                            "must yourself clarify issues regarding your tax liability in the country in which " +
                            "you now live with the local tax authorities."
                    },
                )
            }
        }

        paragraph {
            text(
                bokmal {
                    +"På nettjenesten Din pensjon på $NAV_URL kan du se hvilket skattetrekk som er registrert " +
                        "hos Nav og legge inn eventuelt tilleggstrekk om du ønsker det."
                },
                nynorsk {
                    +"På nettenesta Din pensjon på $NAV_URL kan du sjå kva skattetrekk som er registrert hos Nav " +
                        "og leggje inn eventuelt tilleggstrekk om du ønskjer det."
                },
                english {
                    +"The website Din pensjon on $NAV_URL shows the tax deduction rate registered at Nav. Here " +
                        "you can enter any desired supplementary deduction."
                },
            )
        }

        // Dine rettigheter — innsyn.
        title1 {
            text(
                bokmal { +"Dine rettigheter" },
                nynorsk { +"Dine rettar" },
                english { +"Your rights" },
            )
        }
        includePhrase(AfpPrivatFraser.InnsynForvaltningsloven18)

        // Klagerett.
        includePhrase(AfpPrivatFraser.KlagerettFolketrygdloven2112)
    }
}
