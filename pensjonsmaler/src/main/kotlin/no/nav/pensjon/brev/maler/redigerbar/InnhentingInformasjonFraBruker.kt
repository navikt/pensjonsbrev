package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.amerikansSocialSecurityNumber
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.bankOpplysninger
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.boOgArbeidsperioder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.bosattEOSLandSedEOSBlanketter
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.eps60aarOgInntektUnder1G
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.eps62aarOgInntektUnder1GBoddArbeidUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.epsInntektUnder2G
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.forsorgerEpsBosattIUtlandet
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.inntektsopplysninger
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.manglendeOpptjening
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.registreringAvSivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.SaksbehandlerValgSelectors.tidspunktForUttak
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingOpplysningerFraBrukerAlderDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Alderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InnhentingInformasjonFraBruker : RedigerbarTemplate<InnhentingOpplysningerFraBrukerAlderDto> {
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNHENTING_INFORMASJON_FRA_BRUKER
    override val template: LetterTemplate<*, InnhentingOpplysningerFraBrukerAlderDto> = createTemplate(
        name = kode.name,
        letterDataType = InnhentingOpplysningerFraBrukerAlderDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhenting av opplysninger/dokumentasjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    ) {
        title {
            textExpr(
                Bokmal to "Vi har mottatt ".expr() +
                        fritekst("søknad/blankett/brev/henvendelse")
                        + " fra deg " + fritekst("dato") + ".",

                Nynorsk to "Vi har fått ".expr() +
                        fritekst("søknad/blankett/brev/henvendelse")
                        + " frå deg " + fritekst("dato") + ".",

                English to "We have received ".expr() +
                        fritekst("an application/a form/a letter/an inquiry")
                        + " from you " + fritekst("dato") + ".",
            )
        }
        outline {
            paragraph {
                val fritekst = fritekst("Utfyllende informasjon til bruker om årsak til brevet.")
                textExpr(
                    Bokmal to fritekst,
                    Nynorsk to fritekst,
                    English to fritekst,
                )
            }
            paragraph {
                text(
                    Bokmal to "For at vi skal kunne behandle saken din ferdig, må du sende oss følgende opplysninger/ dokumentasjon:",
                    Nynorsk to "For at vi skal kunne behandle saka di ferdig, må du sende oss følgande opplysningar / dokumentasjon:",
                    English to "In order for us to be able to finish processing your case, you need to send us following information and/or documentation:",
                )

                list {
                    item {
                        val opplysning1 = fritekst("Opplysning 1")
                        textExpr(Bokmal to opplysning1, Nynorsk to opplysning1, English to opplysning1)
                    }
                    item {
                        val opplysning2 = fritekst("Opplysning 2")
                        textExpr(Bokmal to opplysning2, Nynorsk to opplysning2, English to opplysning2)
                    }
                    item {
                        val opplysning3 = fritekst("Opplysning 3")
                        textExpr(Bokmal to opplysning3, Nynorsk to opplysning3, English to opplysning3)
                    }
                }
            }

            showIf(saksbehandlerValg.inntektsopplysninger) {
                title1 {
                    text(
                        Bokmal to "Inntektsopplysninger",
                        Nynorsk to "Inntektsopplysningar ",
                        English to "Income information",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Som dokumentasjon på inntekta, både person- og kapitalinntekta, kan du for eksempel sende kopi av likninga, kopiar av lønnsslippar frå dei tre siste månadene, stadfesting frå arbeidsgivaren, kopiar av lønns- og trekkoppgåver, stadfesting frå rekneskapsførar eller årsoppgåve frå banken.",
                        Nynorsk to "Som dokumentasjon på inntekta, både person- og kapitalinntekta, kan du for eksempel sende kopi av likninga, kopiar av lønnsslippar frå dei tre siste månadene, stadfesting frå arbeidsgivaren, kopiar av lønns- og trekkoppgåver, stadfesting frå rekneskapsførar eller årsoppgåve frå banken.",
                        English to "Examples of documentation of your income (both your personal income and capital income) include your annual tax assessment, pay slips from the last three months, confirmation from your employer, certificates of pay and tax deducted, confirmation from your accountant and year-end statements from your bank.",
                    )
                }
            }

            showIf(saksbehandlerValg.bankOpplysninger) {
                title1 {
                    text(
                        Bokmal to "Bankopplysninger",
                        Nynorsk to "Bankopplysningar",
                        English to "Bank details",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi har behov for bankopplysninger for å kunne utbetale pensjonen din.",
                        Nynorsk to "Vi treng bankopplysningar for å kunne betale ut pensjonen din.",
                        English to "We need your bank details in order to be able to pay you your pension.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "I Din pensjon på $DIN_PENSJON_URL kan du registrere nødvendige bankopplysninger. Gå inn på Din Profil og velg «Bankkonto for utbetaling». Du kan logge inn med BankID, Buypass, MinID eller Commfides.",
                        Nynorsk to "I Din pensjon på $DIN_PENSJON_URL kan du registrere nødvendige bankopplysningar. Gå inn på Din Profil og vel «Bankkonto for utbetaling». Du kan logge inn med BankID, Buypass, MinID eller Commfides.",
                        English to "You can register the necessary bank details in the “Din pensjon” portal at $DIN_PENSJON_URL. Log in on your personal page “Din Profil” and select “Bank account for payment”. You can log in using BankID, Buypass, MinID or Commfides.",
                    )
                }
            }

            showIf(saksbehandlerValg.amerikansSocialSecurityNumber) {
                title1 {
                    text(
                        Bokmal to "Amerikansk social security-nummer",
                        Nynorsk to "Amerikansk social security-nummer",
                        English to "American social security number",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi har behov for ditt amerikanske social security-nummer. (Det vil bli behandlet konfidensielt og er utelukkende for innhenting av informasjon fra amerikanske trygdemyndigheter om eventuell trygdetid i USA.)",
                        Nynorsk to "Vi treng ditt amerikanske social security-nummer. (Det vil bli handsama løynleg, men er nødvendig for å hente inn informasjon frå amerikanske trygdemyndigheiter om eventuell trygdetid i USA.)",
                        English to "We need your American social security number. (Will be treated confidentially and is solely to be used for the purpose of requesting information from US authorities about possible insurance coverage in the United States.)",
                    )
                }
            }



            showIf(saksbehandlerValg.registreringAvSivilstand) {
                paragraph {
                    textExpr(
                        Bokmal to "Personnummer/ID for ".expr() + fritekst("ektefellen/partneren/samboeren") + " din",
                        Nynorsk to "Personnummer/ID for ".expr() + fritekst("ektefellen/partnaren/sambuaren") + " din",
                        English to "Personal identity number".expr(),
                    )
                }

                paragraph {
                    textExpr(
                        Bokmal to "Vi mangler det nasjonale personnummeret eller identifikasjonsnummeret til ".expr() +
                                fritekst("ektefellen/partneren/samboeren") + " din. Dokumentasjon på dette kan være kopi av pass eller annen fotoidentifikasjon med nasjonalt ID-nummer " +
                                fritekst("samt kopi av eventuell vigselsattest") + ".",

                        Nynorsk to "Vi manglar det nasjonale personnummeret eller identifikasjonsnummeret til ".expr() +
                                fritekst("ektefellen/partnaren/sambuaren") + " din. Dokumentasjon på dette kan vere kopi av pass eller annan fotoidentifikasjon med nasjonalt ID-nummer " +
                                fritekst(" og også kopi av eventuell vigselsattest") + ".",

                        English to "We do not have your ".expr() +
                                fritekst("spouse/partner/cohabitant") +
                                "’s national personal identity number. Documentation of this could be a copy of the passport or other photographic proof of identity with the national ID number " +
                                fritekst("and/or a copy of your marriage certificate, if you are married") + ".",
                    )
                }
            }

            showIf(saksbehandlerValg.eps60aarOgInntektUnder1G) {
                title1 {
                    textExpr(
                        Bokmal to "Dokumentasjon på din ".expr() + fritekst("ektefelle/partner/samboer") + " sin inntekt",
                        Nynorsk to "Dokumentasjon på din ".expr() + fritekst("ektefelle/partnar/sambuar") + " si inntekt",
                        English to "Documentation of your ".expr() + fritekst("spouse/partner/cohabitant") + "`s income",
                    )
                }
                paragraph {
                    val beloep = fritekst("beløp")
                    textExpr(
                        Bokmal to "Du har opplyst at ".expr() + fritekst("ektefellen/partneren/samboeren") +
                                " din har en inntekt som ikke overstiger gjeldende grunnbeløp i folketrygden på " +
                                beloep + " kroner.",

                        Nynorsk to "Du har opplyst at ".expr() + fritekst("ektefellen/partnaren/sambuaren") +
                                " din har ei inntekt som ikkje overstig gjeldande grunnbeløp i folketrygda på " +
                                beloep + " kroner.",

                        English to "You have informed us that your ".expr() + fritekst("spouse/partner/cohabitant") +
                                " has an annual income which does not exceed the national insurance basic amount (1G), NOK " +
                                beloep + ".",
                    )
                }

                paragraph {
                    textExpr(
                        Bokmal to "Du kan ha rett til å få pensjonen beregnet med en særskilt sats for minste pensjonsnivå dersom du forsørger ".expr() +
                                fritekst("ektefelle/partner/samboer") + " over 60 år. Dette skal vi nå vurdere.",

                        Nynorsk to "Du kan ha rett til å få pensjonen berekna med ein særskilt sats for minste pensjonsnivå dersom du forsørgjer ".expr() +
                                fritekst("ektefelle/partnar/sambuar") + " over 60 år. Dette skal vi nå vurdere.",

                        English to "You may be entitled to the minimum pension level according to a special rate, if you provide for your ".expr() +
                                fritekst("spouse/partner/cohabitant") + " who is over the age of 60. We are now going to assess this.",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Vi mangler dokumentasjon på din ".expr() + fritekst("ektefelles/partners/samboers")
                                + " samlede inntekt. Med inntekt menes:",
                        Nynorsk to "Vi manglar dokumentasjon på den samla inntekta til din ".expr() + fritekst("ektefelle/partnar/sambuar")
                                + ". Med inntekt meinast:",
                        English to "We do not have documentation of your ".expr() + fritekst("spouse/partner/cohabitant")
                                + "’s total income. As income we mean:",
                    )
                }

                paragraph {
                    list {
                        item {
                            text(
                                Bokmal to "arbeidsinntekt i Norge, og eventuelt andre land",
                                Nynorsk to "arbeidsinntekt i Noreg og eventuelt andre land",
                                English to "employment income in Norway and other countries",
                            )
                        }
                        item {
                            text(
                                Bokmal to "inntekt fra andre private og offentlige pensjonsordninger",
                                Nynorsk to "inntekt frå andre private og offentlege pensjonsordningar",
                                English to "occupational pension from private or public schemes",
                            )
                        }
                        item {
                            text(
                                Bokmal to "ytelser og pensjoner fra andre land",
                                Nynorsk to "ytingar og pensjonar frå andre land",
                                English to "benefits and pensions from other countries",
                            )
                        }
                        item {
                            text(
                                Bokmal to "ytelser fra NAV, blant annet sykepenger og arbeidsavklaringspenger (APP)",
                                Nynorsk to "ytingar frå NAV, blant anna sjukepengar og arbeidsavklaringspengar (APP)",
                                English to "other national insurance benefits, for example sickness benefits and work assessment allowance (AAP)",
                            )
                        }
                        item {
                            text(
                                Bokmal to "kapitalinntekt",
                                Nynorsk to "kapitalinntekt",
                                English to "capital income",
                            )
                        }
                        item {
                            text(
                                Bokmal to "livrente",
                                Nynorsk to "livrente",
                                English to "annuity income",
                            )
                        }
                    }
                    text(
                        Bokmal to "Som dokumentasjon kan du sende kopi av skatteoppgjøret for siste år. Vi godtar også bekreftelse fra regnskapsfører, årsoppgave fra bank eller kopier av lønns- og trekkoppgaver.",
                        Nynorsk to "Som dokumentasjon kan du sende kopi av skatteoppgjeret for det siste året. Vi godtar også stadfesting frå rekneskapsførar, årsoppgåve frå bank eller kopiar av lønns- og trekkoppgåver.",
                        English to "As documentation, you can submit a copy of the tax settlement for the previous year. Alternatively, you can submit a confirmation from an accountant, a year-end bank statement or copies of certificates of pay and tax deducted.",
                    )
                }
            }

            showIf(saksbehandlerValg.eps62aarOgInntektUnder1GBoddArbeidUtland) {
                title1 {
                    text(
                        Bokmal to "Opplysninger om din ektefelles/partners/samboers opphold i utlandet",
                        Nynorsk to "Opplysningar om din ektefelles/partnars/sambuars opphald i utlandet",
                        English to "Information about your spouse/partner/cohabitant’s stays abroad",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Alderspensjonen din er beregnet med en særskilt sats for minste pensjonsnivå fordi du forsørger ".expr() + fritekst(
                            "ektefelle/partner/samboer"
                        ) + " over 60 år. ",
                        Nynorsk to "Alderspensjonen din er berekna med ein særskilt sats for minste pensjonsnivå fordi du forsørgjer ".expr() + fritekst(
                            "ektefelle/partnar/sambuar"
                        ) + " over 60 år. ",
                        English to "Your retirement pension is calculated according to a special rate which gives you the minimum pension level, because you provide for your ".expr() + fritekst(
                            "spouse/partner/cohabitant"
                        ) + " who is over the age of 60. ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis ektefellen/partneren/samboeren din kan ta ut full alderspensjon fra folketrygden fra måneden etter fylte 62 år vil du ikke lenger får pensjonen beregnet etter den særskilte satsen.",
                        Nynorsk to "Dersom ektefellen/partnaren/sambuaren din kan ta ut full alderspensjon frå folketrygda frå månaden etter fylte 62 år vil du ikkje lenger får pensjonen berekna etter den særskilte satsen.",
                        English to "If your spouse/partner/cohabitant is entitled to draw a 100 percent retirement pension at the age of 62, you are no longer entitled to the minimum pension level according to the special rate.",
                    )
                }

                paragraph {
                    textExpr(
                        Bokmal to "Vi mangler opplysninger om din ".expr() + fritekst("ektefelle / partner / samboer")
                                + " har bodd og/eller arbeidet i andre land enn Norge. ",

                        Nynorsk to "Vi manglar opplysningar om din ".expr() + fritekst("ektefelle / partnar / sambuar")
                                + " har budd og/eller arbeida i andre land enn Noreg.",

                        English to "We do not have any information on whether your ".expr() + fritekst("spouse / partner / cohabitant")
                                + " has lived and/or worked in other countries than Norway. ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du må sende oss disse opplysningene:",
                        Nynorsk to "Du må sende oss desse opplysningane:",
                        English to "You must send us the following information:",
                    )
                    list {
                        item {
                            text(
                                Bokmal to "hvilke land ektefellen/partneren/samboeren din bodde og/eller arbeidet i",
                                Nynorsk to "kva land ektefellen/partnaren/sambuaren din har budd og/eller arbeidd i",
                                English to "which countries your spouse/partner/cohabitant has lived and/or worked in",
                            )
                        }
                        item {
                            text(
                                Bokmal to "hvilke perioder dette var",
                                Nynorsk to "kva periodar dette var",
                                English to "what time periods were these",
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        Bokmal to "Retten til alderspensjon avhenger blant annet av trygdetid, som er tid med medlemskap i folketrygden etter fylte 16 år. Hvis du ikke sender inn opplysninger, forutsetter vi at ektefellen/partneren/samboeren din har full trygdetid (40 år) i Norge.",
                        Nynorsk to "Retten til alderspensjon avheng blant anna av trygdetid, som er tida med medlemskap i folketrygda etter fylte 16 år. Dersom du ikkje sender inn opplysningar, forutsett vi at ektefellen/partnaren/sambuaren din har full trygdetid (40 år) i Noreg.",
                        English to "The right for retirement pension depends, among other factors, on how long a person has been a member of the National Insurance Scheme, after the age of 16. If you do not send us any information, we will assume that your spouse/partner/cohabitant has a full period of national insurance coverage (40 years) in Norway.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "På $DIN_PENSJON_URL kan ektefellen/partneren/samboeren din sjekke sin rett til å ta ut full alderspensjon fra måneden etter fylte 62 år.",
                        Nynorsk to "På $DIN_PENSJON_URL kan ektefellen/partnaren/sambuaren din sjekke sin rett til å ta ut full alderspensjon frå månaden etter fylte 62 år.",
                        English to "Your spouse/partner/cohabitant can log on to $DIN_PENSJON_URL to find out if he/she is entitled to draw a 100 percent retirement pension at the age of 62.",
                    )
                }
            }

            showIf(saksbehandlerValg.epsInntektUnder2G) {
                title1 {
                    text(
                        Bokmal to "Dokumentasjon på din ektefelle/partner/samboer sin inntekt",
                        Nynorsk to "Dokumentasjon på din ektefelle/partnar/sambuar si inntekt",
                        English to "Documentation of your spouse/partner/cohabitant`s income",
                    )
                }
                paragraph {
                    val beloep = fritekst("beløp")
                    textExpr(
                        Bokmal to "Vi mangler dokumentasjon på at din ".expr() +
                                fritekst("ektefelles/partners/samboers") +
                                " samlede inntekt er under to ganger folketrygdens grunnbeløp (2G), " +
                                beloep + " kroner. Med inntekt menes:",

                        Nynorsk to "Vi manglar dokumentasjon på at din ".expr() +
                                fritekst("ektefelle/partnar/sambuar") +
                                " si samla inntekt er under to gonger grunnbeløpet i folketrygda (2G), " +
                                beloep + " kroner. Med inntekt meinast:",

                        English to "We do not have documentation that your ".expr() +
                                fritekst("spouse/partner/cohabitant") +
                                "’s total income is lower than twice the national insurance basic amount (2G), NOK " +
                                beloep + ". As income we mean:",
                    )
                    list {
                        item {
                            text(
                                Bokmal to "arbeidsinntekt i Norge og eventuelt andre land",
                                Nynorsk to "arbeidsinntekt i Noreg og eventuelt andre land",
                                English to "employment income in Norway and other countries",
                            )
                        }
                        item {
                            text(
                                Bokmal to "inntekt fra andre private og offentlige pensjonsordninger",
                                Nynorsk to "inntekt frå andre private og offentlege pensjonsordningar",
                                English to "occupational pension from private or public schemes",
                            )
                        }
                        item {
                            text(
                                Bokmal to "ytelser og pensjoner fra andre land",
                                Nynorsk to "ytingar og pensjonar frå andre land",
                                English to "benefits and pensions from other countries",
                            )
                        }
                        item {
                            text(
                                Bokmal to "ytelser fra NAV, blant annet sykepenger og arbeidsavklaringspenger (APP)",
                                Nynorsk to "ytingar frå NAV, blant anna sjukepengar og arbeidsavklaringspengar (APP)",
                                English to "other national insurance benefits, for example sickness benefits and work assessment allowance (AAP)",
                            )
                        }
                        item {
                            text(
                                Bokmal to "kapitalinntekt",
                                Nynorsk to "kapitalinntekt",
                                English to "capital income",
                            )
                        }
                        item {
                            text(
                                Bokmal to "livrente",
                                Nynorsk to "livrente",
                                English to "annuity income",
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        Bokmal to "Som dokumentasjon kan du sende kopi av skatteoppgjøret for siste år. Vi godtar også bekreftelse fra regnskapsfører, årsoppgave fra bank eller kopier av lønns- og trekkoppgaver.",
                        Nynorsk to "Som dokumentasjon kan du sende kopi av skatteoppgjeret for det siste året. Vi godtar også stadfesting frå rekneskapsførar, årsoppgåve frå bank eller kopiar av lønns- og trekkoppgåver.",
                        English to "As documentation, you can submit a copy of the tax settlement for the previous year. Alternatively, you can submit a confirmation from an accountant, a year-end bank statement or copies of certificates of pay and tax deducted.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Inntektsopplysningene er nødvendige for å kunne beregne pensjonen din korrekt.",
                        Nynorsk to "Inntektsopplysningane er nødvendige for å kunne berekne pensjonen din korrekt.",
                        English to "The income information is necessary to calculate your pension correctly.",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "For å ha rett til full grunnpensjon, må du dokumentere at inntekten til ".expr() +
                                fritekst("ektefellen/partneren/samboeren") + " din er under 2G.",

                        Nynorsk to "For å ha rett til full grunnpensjon må du dokumentere at inntekta til ".expr() +
                                fritekst("ektefellen/partnaren/sambuaren") + " din er under 2G.",

                        English to "In order to be entitled to a 100 percent basic pension, you must document that your ".expr() +
                                fritekst("spouse/partner/cohabitant") + "’s income is less than 2G.",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Dersom vi ikke får dokumentasjon på at inntekten til ".expr() +
                                fritekst("ektefellen/partneren/samboeren") + " din er under 2G vil du kun få en grunnpensjon på 90 prosent av grunnbeløpet.",

                        Nynorsk to "Dersom vi ikkje får dokumentasjon på at inntekta til ".expr() +
                                fritekst("ektefellen/partnaren/sambuaren") + " din er under 2G, vil du berre få ein grunnpensjon på 90 prosent av grunnbeløpet.",

                        English to "If we do not receive documentation that your ".expr() +
                                fritekst("spouse/partner/cohabitant") + "’s income is below 2G, you will only receive a basic pension of 90 percent of the basic amount.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du trenger ikke legge ved dokumentasjon dersom inntekten er over 2G. Du kan bare gi oss beskjed om dette.",
                        Nynorsk to "Du treng ikkje leggje ved dokumentasjon dersom inntekta er over 2G. Du kan berre gi oss beskjed om dette.",
                        English to "You do not need to provide documentation if the income is over 2G. Simply notify us about this.",
                    )
                }
            }

            showIf(saksbehandlerValg.forsorgerEpsBosattIUtlandet) {
                title1 {
                    textExpr(
                        Bokmal to "Dokumentasjon på at du forsørger ".expr() + fritekst("ektefellen/partneren/samboeren") + " din",
                        Nynorsk to "Dokumentasjon på at du forsørgjer ".expr() + fritekst("ektefellen/partnaren/sambuaren") + " din",
                        English to "Documentation that you provide for your ".expr() + fritekst("spouse/partner/cohabitant"),
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi skal vurdere om du har rett til pensjon beregnet etter særskilt sats. Dette kan gi deg en høyere pensjon. For å få særskilt sats må du dokumentere at du forsørger din ektefelle/partner/samboer. Eksempler på dokumentasjon er bankoverføringer. Det må dokumenteres minst tre overføringer, du må stå som avsender og din ektefelle/partner/samboer som mottaker.",
                        Nynorsk to "Vi skal vurdere om du har rett til pensjon berekna etter særskilt sats. Dette kan gi deg ein høgare pensjon. For å få særskilt sats må du dokumentere at du forsørgjer din ektefelle/partnar/sambuar. Eksempel på dokumentasjon er bankoverføringar. Det må dokumenterast minst tre overføringar, du må stå som avsendar og din ektefelle/partnar/sambuar som mottakar.",
                        English to "We will assess your eligibility for retirement pension according to a special rate. This may give you a higher pension. To get this special rate you must document that you provide for your spouse/partner/cohabitant. An example of such documentation is bank transfers, of which there must be at least three. You must be the person who transfers the money, and your spouse/partner/cohabitant must be the receiver.",
                    )
                }
            }

            showIf(saksbehandlerValg.manglendeOpptjening) {
                title1 {

                    text(
                        Bokmal to "Manglende opptjening",
                        Nynorsk to "Manglande opptening",
                        English to "Period(-s) with no pension earnings",
                    )

                }

                paragraph {
                    textExpr(
                        Bokmal to "Ifølge opplysningene vi har mottatt, har du ikke hatt pensjonsopptjening i ".expr() +
                                fritekst("året/årene") + ". Vi ber om at du kort forklarer årsaken til dette.",

                        Nynorsk to "Ifølgje opplysningane vi har fått, har du ikkje hatt pensjonsopptening i ".expr() +
                                fritekst("året/åra") + ". Vi ber om at du kort forklarar årsaka til dette.",

                        English to "According to the information we have received, you have no pension earnings for ".expr() +
                                fritekst("the year/the years") + ". We ask you to provide a brief account of the cause for this.",
                    )
                }
            }

            showIf(saksbehandlerValg.boOgArbeidsperioder) {
                title1 {
                    text(
                        Bokmal to "Bo- og arbeidsperioder i Norge",
                        Nynorsk to "Bu- og arbeidsperiodar  i Noreg",
                        English to "Periods in Norway",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du må oppgi hvor og når du var bosatt og/eller arbeidet i Norge. For å sikre at vi har riktige opplysninger om dine arbeidsperioder ber vi deg om å dokumentere disse. Dette kan være kopier av attester, lønnslipper eller skattemelding.",
                        Nynorsk to "Du må gi opp kvar og når du var busett og/eller arbeidde i Noreg. For å sikre at vi har riktige opplysningar om arbeidsperiodane dine, ber vi deg om å dokumentere desse. Det kan vere kopiar av attestar, lønnsslippar eller skattemelding.",
                        English to "You must state where and when you lived and/or worked in Norway. To ensure that we have the correct information about your work periods, we ask you to provide documentation, such as copies of references, pay slips or tax returns.",
                    )
                }
            }

            showIf(saksbehandlerValg.bosattEOSLandSedEOSBlanketter) {
                title1 {
                    text(
                        Bokmal to "Søknad fra EØS-land",
                        Nynorsk to "Søknad frå EØS-land",
                        English to "Application from an EEA country ",
                    )
                }
                paragraph {
                    // TODO rar tekst. Samsvarer ikke på engelsk. + hva gjør jeg med tomt fritekst felt? kan vi kalle den for noe?
                    textExpr(
                        Bokmal to "Dersom man er bosatt i et EØS-land, og har opparbeidet seg pensjonsrettigheter i dette landet, følger det av EØS-reglementet at man skal søke om pensjon fra annet EØS-land gjennom bostedslandets nasjonale myndigheter. Vi ber derfor om at du kontakter ".expr() + fritekst(
                            "trygdemyndighet"
                        ) + " trygdemyndigheter for oversendelse av SED-dokumentene P2000, P5000 og P4000, eventuelt EØS-blankettene E202, E205 og E207.",
                        Nynorsk to "Dersom man bur i eit EØS-land, og har opparbeida seg pensjonsrettighetar i dette landet, følgjer det av EØS-reglementet at man skal søkje om pensjon frå anna EØS-land gjennom bustadslandet sine nasjonale myndigheiter. Vi ber derfor om at du kontaktar ".expr() + fritekst(
                            "trygdemyndighet"
                        ) + " trygdemyndigheiter for oversendelse av SED-dokumenta P2000, P5000 og P4000, eventuelt EØS-blankettane E202, E205 og E207.",
                        English to "According to the EEA agreement, a resident of an EEA country has to forward his/her claim for benefits in another EEA country through the national authorities in the country of residence. In order for us to process your claim for retirement pension from the Norwegian National Insurance Scheme, you must contact your local authorities.".expr(),
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Din søknad kan ikke bli behandlet før vi mottar disse blankettene.",
                        Nynorsk to "Din søknad kan ikkje bli behandla før vi mottar desse blankettane.",
                        English to "No decision will be made until the SED-documents P2000, P5000 and P4000, or the forms E202, E205 and E207 are provided.",
                    )
                }
            }
            showIf(saksbehandlerValg.tidspunktForUttak) {
                title1 {
                    text(
                        Bokmal to "Tidspunkt for uttak / ønsket uttaksgrad",
                        Nynorsk to "Tidspunkt for uttak/ønska uttaksgrad ",
                        English to "Statement for your pension ",
                    )
                }
                paragraph {
                    val dato = fritekst("dato")
                    textExpr(
                        Bokmal to "Du har ikke oppgitt når du ønsker å ta ut pensjon. Du må derfor fylle ut skjemaet du får sammen med dette brevet. Dersom du ikke sender oss informasjon om når du ønsker å ta ut pensjon, tar vi utgangspunkt i datoen du sendte søknaden til ".expr() +
                                fritekst("oss / trygdemyndighetene i bostedslandet ditt") + ". Det vil si " + dato,
                        Nynorsk to "Du har ikkje oppgitt når du ønskjer å ta ut pensjon. Du må derfor fylle ut skjemaet du får saman med dette brevet. Dersom du ikkje sender oss informasjon om når du ønskjer å ta ut pensjon, vil vi ta utgangspunkt i datoen då du sende søknaden til ".expr() +
                                fritekst("oss / trygdeorganet i bustadslandet ditt") + ". Det vil seie " + dato,
                        English to "You have not stated when you want to start drawing your pension. Therefore, you must fill out the form enclosed with this letter. If you do not send us information about when you want to start drawing your pension, we will use the date you submitted the application to ".expr() +
                                fritekst("us, / the National Insurance authorities in your country of residence,") + " i.e. " + dato,
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du har ikke oppgitt hvilken uttaksgrad av alderspensjon du ønsker. " +
                                fritekst("Vi kan ikke behandle søknaden din før vi får svar fra deg om dette. / Dersom du ikke sender oss informasjon om uttaksgraden, setter vi den til 100 prosent."),

                        Nynorsk to "Du har ikkje oppgitt kva uttaksgrad av alderspensjon du ønskjer å ta ut. " +
                                fritekst("Vi kan ikkje behandle søknaden din før vi får svar frå deg om dette. / Dersom du ikkje sender oss informasjon om uttaksgraden, set vi den til 100 prosent."),

                        English to "You have not stated what percentage of your retirement pension you want to draw. " +
                                fritekst("We cannot process your application until we receive a response from you. / If you do not send us information about what withdrawal percentage you want, the percentage will be set to 100 percent."),
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Du kan gi tilbakemelding via nav sin nettside $DITT_NAV. Velg «Send beskjed til nav», tema «Beskjed – pensjon».",
                    Nynorsk to "Du kan gi tilbakemelding via nav si nettside $DITT_NAV. Vel «Send beskjed til nav», tema «Beskjed – pensjon».",
                    English to "Feel free to contact us and give us the required information at nav’s online service $DITT_NAV. Select «Send beskjed til nav» and the theme «Beskjed – pensjon».",
                )
            }
            paragraph {
                val svarfristDato = fritekst("dato svarfrist")
                textExpr(
                    Bokmal to "Skriftlig tilbakemelding per post og eventuell dokumentasjon må du sende innen ".expr()
                            + svarfristDato + " til følgende adresse:",
                    Nynorsk to "Skriftleg tilbakemelding per post og eventuell dokumentasjon må du sende innan ".expr()
                            + svarfristDato + " til følgande adresse:",
                    English to "Please send the required documentation within ".expr()
                            + svarfristDato + " to the address below:",
                )
            }
            includePhrase(Alderspensjon.Returadresse)
            paragraph {
                text(
                    Bokmal to "Dersom vi ikke mottar nødvendig dokumentasjonen innen fristen, vil saken bli avgjort med de opplysninger som foreligger. Hvis vi ikke har nok opplysninger til å behandle søknaden din, kan saken bli avslått.",
                    Nynorsk to "Dersom vi ikkje mottar nødvendig dokumentasjon innan fristen, vil saka bli avgjord med de opplysningane som foreligg. Hvis vi ikkje har nok opplysningar til å behandle saka di, kan saka di bli avslått.",
                    English to "If we do not receive the necessary documentation or an explanation within the deadline, your application will either be processed using the information available to us or postponed until we have received the information we need. If we do not receive the necessary information, we will not have enough information to process your case, and your application may be declined.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Dette går fram av folketrygdloven §§ 21-3 og 21-7.",
                    Nynorsk to "Dette går fram av folketrygdlova §§ 21-3 og 21-7.",
                    English to "This follows from sections 21-3 and 21-7 of the National Insurance Act.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan finne mer informasjon om regelverket på $NAV_URL",
                    Nynorsk to "Du kan finne meir informasjon om regelverket på $NAV_URL",
                    English to "You can find more information about the regulations at $NAV_URL",
                )
            }
            title1 {
                text(
                    Bokmal to "Svartid",
                    Nynorsk to "Svartid",
                    English to "Response time",
                )
            }
            paragraph {
                text(
                    Bokmal to "Saken vil bli behandlet innen " + fritekst("antall dager/uker/måneder") + " etter at vi har mottatt nødvendige opplysninger/dokumentasjon. Hvis saken ikke blir avgjort i løpet av denne tiden, vil du høre nærmere fra oss.",
                    Nynorsk to "Saka vil bli behandla innan " + fritekst("talet på dagar / veker / månader") + " etter at vi har fått nødvendige opplysningar/dokumentasjon. Dersom saka ikkje blir avgjord i løpet av denne tida, vil du høyre nærare frå oss.",
                    English to "Your case will be processed within " + fritekst("antall days / weeks / months") + " after we have received the necessary information and/or documentation. If your case has not been decided within this time, you will hear from us.",
                )
            }


            includePhrase(Felles.MeldeFraEndringer)

            paragraph {
                text(
                    Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til [_Value NAV_].",
                    Nynorsk to "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til [_Value NAV_].",
                    English to "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to [_Value NAV_].",
                )
            }

            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }

}