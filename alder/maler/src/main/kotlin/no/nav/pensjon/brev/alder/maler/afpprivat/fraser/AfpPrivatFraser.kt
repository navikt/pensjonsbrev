package no.nav.pensjon.brev.alder.maler.afpprivat.fraser

import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType

/**
 * Felles fraser for AFP privat-brev.
 *
 * Brukes av [no.nav.pensjon.brev.alder.maler.afpprivat.InnvilgelseAvAfpInnhold],
 * [no.nav.pensjon.brev.alder.maler.afpprivat.AvslagAfpPrivat] og
 * [no.nav.pensjon.brev.alder.maler.afpprivat.VedtakAfpPrivatEndring].
 */
object AfpPrivatFraser {

    /**
     * Standard innsyn-paragraf med henvisning til forvaltningsloven § 18.
     * Brukes uten egen tittel — kalleren plasserer den under en passende
     * `title1` («Dine rettigheter» i innvilgelse, «Din rett til innsyn og
     * klage» i avslag).
     */
    object InnsynForvaltningsloven18 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du har som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven " +
                            "paragraf 18."
                    },
                    nynorsk {
                        +"Du har som hovudregel rett til å sjå saksdokumenta etter føresegnene i forvaltingslova " +
                            "paragraf 18."
                    },
                    english {
                        +"As a main rule, you are entitled to see all case documents pursuant to section 18 of the " +
                            "Public Administration Act."
                    },
                )
            }
        }
    }

    /**
     * Standard klagerett-paragraf med henvisning til folketrygdloven § 21-12.
     * Brukes som siste paragraf under «Dine rettigheter» i AFP-vedtak hvor
     * brukeren har klagerett mot vedtaket (innvilgelse av AFP privat og
     * offentlig sektor).
     */
    object KlagerettFolketrygdloven2112 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener at vedtaket ikke er i samsvar med det du har søkt om, kan du klage på vedtaket " +
                            "etter bestemmelsene i folketrygdloven paragraf 21-12. Fristen for å klage er seks uker " +
                            "fra du mottar dette brevet."
                    },
                    nynorsk {
                        +"Dersom du meiner at vedtaket ikkje er i samsvar med det du har søkt om, kan du klage på " +
                            "vedtaket etter føresegnene i folketrygdlova paragraf 21-12. Fristen for å klage er seks " +
                            "veker frå du får dette brevet."
                    },
                    english {
                        +"If you believe that the decision is not in accordance with what you applied for, you can " +
                            "appeal the decision pursuant to section 21-12 of the National Insurance Act. The time " +
                            "limit for filing an appeal is six weeks from the date you received this letter."
                    },
                )
            }
        }
    }

    /**
     * Tabell med AFP-beløp per måned (livsvarig, kronetillegg, kompensasjonstillegg, sum).
     * Inkluderer intro-teksten "Din AFP per måned blir slik:" og selve tabellen.
     * Brukes i vedtak om endring og innvilgelse (unntatt InnvilgelseAvAfpInnhold som har annen
     * tabelloppsett med English).
     */
    data class AfpBeloepPerMaanedTabell(
        private val livsvarig: Expression<BrevbakerType.Kroner?>,
        private val kronetillegg: Expression<BrevbakerType.Kroner?>,
        private val kompensasjonstillegg: Expression<BrevbakerType.Kroner?>,
        private val sumAfpFoerSkatt: Expression<BrevbakerType.Kroner>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Din AFP per måned blir slik:" },
                    nynorsk { +"Din AFP per månad blir slik:" },
                    english { +"Your monthly contractual pension will be:" },
                )
            }

            paragraph {
                table(
                    header = {
                        column {
                            text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"Beløp per måned" },
                                nynorsk { +"Beløp per månad" },
                                english { +"Amount per month" },
                            )
                        }
                    },
                ) {
                    ifNotNull(livsvarig) {
                        row {
                            cell {
                                text(
                                    bokmal { +"AFP livsvarig del" },
                                    nynorsk { +"AFP livsvarig del" },
                                    english { +"Contractual pension, lifelong amount" },
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
                        }
                    }
                    ifNotNull(kronetillegg) {
                        row {
                            cell {
                                text(
                                    bokmal { +"AFP kronetillegg" },
                                    nynorsk { +"AFP-kronetillegg" },
                                    english { +"Contractual pension, NOK supplement" },
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
                        }
                    }
                    ifNotNull(kompensasjonstillegg) {
                        row {
                            cell {
                                text(
                                    bokmal { +"AFP kompensasjonstillegg (skattefritt)" },
                                    nynorsk { +"AFP-kompensasjonstillegg (skattefritt)" },
                                    english { +"Contractual pension, compensation supplement (tax-free)" },
                                )
                            }
                            cell { includePhrase(KronerText(it)) }
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
                        cell { includePhrase(KronerText(sumAfpFoerSkatt, BOLD)) }
                    }
                }
            }
        }
    }

    /**
     * Forklaring av levealdersjustering. Plasseres rett etter beregningstabellen
     * i innvilgelse og endring av AFP i privat sektor.
     */
    object Levealdersjustering : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"AFP justeres i forhold til forventet levealder ved tidspunkt for uttak. Levealdersjustering " +
                            "er en mekanisme som tar høyde for økt levealder i befolkningen og er innført for å sikre " +
                            "at pensjonssystemet forblir bærekraftig. Du kan lese mer om levealdersjustering på ${Constants.NAV_URL}."
                    },
                    nynorsk {
                        +"AFP blir justert i forhold til forventa levealder ved tidspunktet for uttak. Levealdersjustering " +
                            "er ein mekanisme som tek høgd for auka levealder i befolkninga, og er innført for å sikre " +
                            "at pensjonssystemet held seg berekraftig. Du kan lese meir om levealdersjustering på ${Constants.NAV_URL}."
                    },
                    english {
                        +"Contractual pension is adjusted according to your life expectancy on the pension date. " +
                            "Adjusting for life expectancy is a mechanism to cope with the increased age of the " +
                            "population and has been introduced to make sure that the pension system remains " +
                            "sustainable. You can find more information about life expectancy adjustment on ${Constants.NAV_URL}."
                    },
                )
            }
        }
    }

    /**
     * Informasjon til brukeren om muligheten for ny søknad om AFP privat
     * ved eventuell senere rett til alderspensjon. Brukes i avslagsbrev
     * for AFP privat (PE_AF_04_112, PE_AF_04_116).
     */
    object NySoknadVedSenereRettTilAlderspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Hvis du ved eventuell senere rett til alderspensjon fra folketrygden " +
                            "også ønsker å ta ut AFP i privat sektor, må du sende inn ny søknad om " +
                            "AFP. Vi gjør oppmerksom på at du på uttakstidspunktet for AFP må være " +
                            "reell arbeidstaker i en bedrift som omfattes av AFP-ordningen."
                    },
                    nynorsk {
                        +"Dersom du ved eventuell seinare rett til alderspensjon frå folketrygda " +
                            "òg ønskjer å ta ut AFP i privat sektor, må du sende inn ny søknad om " +
                            "AFP. Vi gjer merksam på at du på uttakstidspunktet for AFP må vere " +
                            "reell arbeidstakar i ei verksemd som er omfatta av AFP-ordninga."
                    },
                    english {
                        +"If, in the event of a possible right to national insurance retirement " +
                            "pension, you also want to draw contractual pension in the private " +
                            "sector, you will need to submit a new application for contractual " +
                            "pension. We wish to point out that on the pension date for contractual " +
                            "pension, you must be an actual employee of an enterprise that is " +
                            "covered by the contractual pension scheme."
                    },
                )
            }
        }
    }

    /**
     * Klagerett-paragraf for avslagsbrev med henvisning til folketrygdloven § 21-12.
     * Forskjell fra [KlagerettFolketrygdloven2112]: Denne sier "klage på avslaget" (ikke
     * "klage på vedtaket") og inkluderer setningen om vedlagt orientering om klage-
     * og ankebehandling.
     *
     * Brukes i avslag AFP privat (PE_AF_04_112, PE_AF_04_116).
     */
    object KlagerettAvslagFolketrygdloven2112 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du kan klage på avslaget etter bestemmelsene i folketrygdloven paragraf 21-12. " +
                            "Fristen for å klage er seks uker fra du mottar dette brevet. Vedlagt finner du " +
                            "en orientering om klage- og ankebehandling."
                    },
                    nynorsk {
                        +"Du kan klage på avslaget etter føresegnene i folketrygdlova paragraf 21-12. Fristen " +
                            "for å klage er seks veker frå du får dette brevet. Vedlagt finn du ei " +
                            "orientering om klage- og ankebehandling."
                    },
                    english {
                        +"You can appeal the rejection, pursuant to the terms in section 21-12 of the " +
                            "National Insurance Act. The time limit for filing an appeal is six weeks from " +
                            "the date you received this letter. We enclose information about the appeal process."
                    },
                )
            }
        }
    }

    /**
     * Bold-ledet forklaring av AFP livsvarig del. Rendres bare når
     * [livsvarigBrutto] er satt.
     */
    class KomponentLivsvarig(
        private val livsvarigBrutto: Expression<BrevbakerType.Kroner?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            ifNotNull(livsvarigBrutto) {
                paragraph {
                    text(
                        bokmal { +"AFP livsvarig del" },
                        nynorsk { +"AFP livsvarig del" },
                        english { +"The contractual pension, lifelong amount," },
                        BOLD,
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
        }
    }

    /**
     * Bold-ledet forklaring av AFP kronetillegg. Rendres bare når
     * [kronetilleggBrutto] er satt.
     */
    class KomponentKronetillegg(
        private val kronetilleggBrutto: Expression<BrevbakerType.Kroner?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            ifNotNull(kronetilleggBrutto) {
                paragraph {
                    text(
                        bokmal { +"AFP kronetillegg" },
                        nynorsk { +"AFP-kronetillegg" },
                        english { +"The contractual pension NOK supplement" },
                        BOLD,
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
        }
    }

    /**
     * Bold-ledet forklaring av AFP kompensasjonstillegg. Rendres bare når
     * [kompensasjonstilleggBrutto] er satt.
     */
    class KomponentKompensasjonstillegg(
        private val kompensasjonstilleggBrutto: Expression<BrevbakerType.Kroner?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            ifNotNull(kompensasjonstilleggBrutto) {
                paragraph {
                    text(
                        bokmal { +"AFP kompensasjonstillegg" },
                        nynorsk { +"AFP-kompensasjonstillegg" },
                        english { +"The contractual pension compensation supplement" },
                        BOLD,
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
        }
    }

    /**
     * Overskrift + paragraf om at AFP gis sammen med alderspensjonen og er
     * uavhengig av uttaksgrad.
     */
    object AfpOgAlderspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
        }
    }

    /**
     * Paragraf om at AFP er fastsatt på opptjening til og med året brukeren
     * fylte 61 år, og at senere opptjening kan øke alderspensjonen. Kalleren
     * må selv betinge på alder (under 70 år).
     */
    object OpptjeningEtter61Aar : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
    }

    /**
     * Paragraf om at man kan arbeide så mye man vil uten at AFP blir redusert.
     */
    object ArbeidUtenReduksjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
        }
    }

    /**
     * Overskrift + paragraf om månedlig utbetaling av AFP (den 20. i måneden).
     */
    object MaanedligUtbetaling : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
                            "utbetalingsdatoer finner du på ${Constants.NAV_URL}."
                    },
                    nynorsk {
                        +"AFP-en din blir vanlegvis utbetalt den 20. kvar månad. Når den 20. er ein laurdag eller " +
                            "offentleg fridag, blir pensjonen utbetalt seinast siste yrkedagen før den 20. Oversikt " +
                            "over utbetalingsdatoar finn du på ${Constants.NAV_URL}."
                    },
                    english {
                        +"Your contractual pension will normally be paid on the 20th of each month. When the 20th is " +
                            "a Saturday or public holiday, your pension will be paid at latest on the last business " +
                            "day before the 20th. You can find a list of payment dates on ${Constants.NAV_URL}."
                    },
                )
            }
        }
    }

    /**
     * Paragraf om at AFP er skattepliktig i Norge. Kalleren må selv betinge på
     * bosted (bosatt i Norge). Når [kompensasjonstilleggBrutto] er satt vises
     * et ledende unntak for kompensasjonstillegget.
     */
    class SkattINorge(
        private val kompensasjonstilleggBrutto: Expression<BrevbakerType.Kroner?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
    }

    /**
     * Paragraf om at AFP er skattepliktig til Norge ved bosted i utlandet.
     * Kalleren må selv betinge på bosted (bosatt i utlandet). Når
     * [kompensasjonstilleggBrutto] er satt vises et ledende unntak for
     * kompensasjonstillegget.
     */
    class SkattIUtlandet(
        private val kompensasjonstilleggBrutto: Expression<BrevbakerType.Kroner?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
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
    }

    /**
     * Paragraf om skattetrekk-oppslag i nettjenesten Din pensjon.
     */
    object DinPensjonSkattetrekk : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"På nettjenesten Din pensjon på ${Constants.NAV_URL} kan du se hvilket skattetrekk som er registrert " +
                            "hos Nav og legge inn eventuelt tilleggstrekk om du ønsker det."
                    },
                    nynorsk {
                        +"På nettenesta Din pensjon på ${Constants.NAV_URL} kan du sjå kva skattetrekk som er registrert hos Nav " +
                            "og leggje inn eventuelt tilleggstrekk om du ønskjer det."
                    },
                    english {
                        +"The website Din pensjon on ${Constants.NAV_URL} shows the tax deduction rate registered at Nav. Here " +
                            "you can enter any desired supplementary deduction."
                    },
                )
            }
        }
    }
}