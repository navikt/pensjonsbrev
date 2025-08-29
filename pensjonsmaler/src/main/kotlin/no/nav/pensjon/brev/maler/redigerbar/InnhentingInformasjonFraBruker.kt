package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.amerikanskSocialSecurityNumber
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.bankopplysninger
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.boOgArbeidsperioder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.bosattIEoesLandSedErEoesBlanketter
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.eps60aarOgInntektUnder1g
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.eps62aarOgInntektUnder1gBoddArbeidUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.epsInntektUnder2g
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.forsoergerEpsBosattIUtlandet
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.inntektsopplysninger
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.manglendeOpptjening
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.registreringAvSivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.SaksbehandlerValgSelectors.tidspunktForUttak
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnhentingInformasjonFraBrukerDtoSelectors.saksbehandlerValg
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
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InnhentingInformasjonFraBruker : RedigerbarTemplate<InnhentingInformasjonFraBrukerDto> {
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNHENTING_INFORMASJON_FRA_BRUKER     //MF_000133
    override val template: LetterTemplate<*, InnhentingInformasjonFraBrukerDto> = createTemplate(
        name = kode.name,
        letterDataType = InnhentingInformasjonFraBrukerDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhenting av opplysninger/dokumentasjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    ) {
        title {
            text(
                bokmal { + "Du må sende oss flere opplysninger" },
                nynorsk { + "Du må sende oss fleire opplysningar" },
                english { + "We need more information from you" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi har mottatt " +
                            fritekst("søknad/blankett/brev/henvendelse")
                            + " fra deg " + fritekst("dato") + "." },

                    nynorsk { + "Vi har fått " +
                            fritekst("søknad/blankett/brev/henvendelse")
                            + " frå deg " + fritekst("dato") + "." },

                    english { + "We have received " +
                            fritekst("an application/a form/a letter/an inquiry")
                            + " from you " + fritekst("dato") + "." },
                )
            }
            paragraph {
                val fritekst = fritekst("Utfyllende informasjon til bruker om årsak til brevet.")
                text(
                    bokmal { + fritekst },
                    nynorsk { + fritekst },
                    english { + fritekst },
                )
            }
            paragraph {
                text(
                    bokmal { + "For at vi skal kunne behandle saken din ferdig, må du sende oss følgende opplysninger/dokumentasjon:" },
                    nynorsk { + "For at vi skal kunne behandle saka di ferdig, må du sende oss følgande opplysningar/dokumentasjon:" },
                    english { + "In order for us to be able to finish processing your case, you need to send us following information and/or documentation:" },
                )

                list {
                    item {
                        val opplysning1 = fritekst("Opplysning 1")
                        text(bokmal { + opplysning1 }, nynorsk { + opplysning1 }, english { + opplysning1 })
                    }
                    item {
                        val opplysning2 = fritekst("Opplysning 2")
                        text(bokmal { + opplysning2 }, nynorsk { + opplysning2 }, english { + opplysning2 })
                    }
                    item {
                        val opplysning3 = fritekst("Opplysning 3")
                        text(bokmal { + opplysning3 }, nynorsk { + opplysning3 }, english { + opplysning3 })
                    }
                }
            }

            showIf(saksbehandlerValg.inntektsopplysninger) {
                title1 {
                    text(
                        bokmal { + "Inntektsopplysninger" },
                        nynorsk { + "Inntektsopplysningar" },
                        english { + "Income information" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Som dokumentasjon for inntekten, både person- og kapitalinntekten, kan du for eksempel sende kopi av ligningen, kopier av lønnsslipper fra de tre siste månedene, bekreftelse fra arbeidsgiveren, kopier av lønns- og trekkoppgaver, bekreftelse fra regnskapsfører eller årsoppgave fra banken." },
                        nynorsk { + "Som dokumentasjon på inntekta, både person- og kapitalinntekta, kan du for eksempel sende kopi av likninga, kopiar av lønnsslippar frå dei tre siste månadene, stadfesting frå arbeidsgivaren, kopiar av lønns- og trekkoppgåver, stadfesting frå rekneskapsførar eller årsoppgåve frå banken." },
                        english { + "Examples of documentation of your income (both your personal income and capital income) include your annual tax assessment, pay slips from the last three months, confirmation from your employer, certificates of pay and tax deducted, confirmation from your accountant and year-end statements from your bank." },
                    )
                }
            }

            showIf(saksbehandlerValg.bankopplysninger) {
                title1 {
                    text(
                        bokmal { + "Bankopplysninger" },
                        nynorsk { + "Bankopplysningar" },
                        english { + "Bank details" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vi har behov for bankopplysninger for å kunne utbetale pensjonen din." },
                        nynorsk { + "Vi treng bankopplysningar for å kunne betale ut pensjonen din." },
                        english { + "We need your bank details in order to be able to pay you your pension." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "I Din pensjon på $DIN_PENSJON_URL kan du registrere nødvendige bankopplysninger. Gå inn på Din Profil og velg " + quoted("Bankkonto for utbetaling") +". Du kan logge inn med BankID, Buypass, MinID eller Commfides." },
                        nynorsk { + "I Din pensjon på $DIN_PENSJON_URL kan du registrere nødvendige bankopplysningar. Gå inn på Din Profil og vel " + quoted("Bankkonto for utbetaling") +". Du kan logge inn med BankID, Buypass, MinID eller Commfides." },
                        english { + "You can register the necessary bank details in the " + quoted("Din pensjon") +" portal at $DIN_PENSJON_URL. Log in on your personal page " + quoted("Din Profil") + " and select " + quoted("Bank account for payment") +". You can log in using BankID, Buypass, MinID or Commfides." },
                    )
                }
            }

            showIf(saksbehandlerValg.amerikanskSocialSecurityNumber) {
                title1 {
                    text(
                        bokmal { + "Amerikansk social security-nummer" },
                        nynorsk { + "Amerikansk social security-nummer" },
                        english { + "American social security number" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vi har behov for ditt amerikanske social security-nummer. (Det vil bli behandlet konfidensielt og er utelukkende for innhenting av informasjon fra amerikanske trygdemyndigheter om eventuell trygdetid i USA.)" },
                        nynorsk { + "Vi treng ditt amerikanske social security-nummer. (Det vil bli handsama løynleg, men er nødvendig for å hente inn informasjon frå amerikanske trygdemyndigheiter om eventuell trygdetid i USA.)" },
                        english { + "We need your American social security number. (Will be treated confidentially and is solely to be used for the purpose of requesting information from US authorities about possible insurance coverage in the United States.)" },
                    )
                }
            }



            showIf(saksbehandlerValg.registreringAvSivilstand) {
                title1 {
                    text(
                        bokmal { + "Personnummer/ID for " + fritekst("ektefellen/partneren/samboeren") + " din" },
                        nynorsk { + "Personnummer/ID for " + fritekst("ektefellen/partnaren/sambuaren") + " din" },
                        english { + "Personal identity number for your" + fritekst("spouse/partner/cohabiting partner") },
                    )
                }

                paragraph {
                    text(
                        bokmal { + "Vi mangler det nasjonale personnummeret eller identifikasjonsnummeret til " +
                                fritekst("ektefellen/partneren/samboeren") + " din. Dokumentasjon på dette kan være kopi av pass eller annen fotoidentifikasjon med nasjonalt ID-nummer " +
                                fritekst("samt kopi av eventuell vigselsattest") + "." },

                        nynorsk { + "Vi manglar det nasjonale personnummeret eller identifikasjonsnummeret til " +
                                fritekst("ektefellen/partnaren/sambuaren") + " din. Dokumentasjon på dette kan vere kopi av pass eller annan fotoidentifikasjon med nasjonalt ID-nummer " +
                                fritekst(" og også kopi av eventuell vigselsattest") + "." },

                        english { + "We do not have your " +
                                fritekst("spouse/partner/cohabitant") +
                                "’s national personal identity number. Documentation of this could be a copy of the passport or other photographic proof of identity with the national ID number " +
                                fritekst("and/or a copy of your marriage certificate, if you are married") + "." },
                    )
                }
            }

            showIf(saksbehandlerValg.eps60aarOgInntektUnder1g) {
                title1 {
                    text(
                        bokmal { + "Dokumentasjon på din " + fritekst("ektefelle/partner/samboer") + " sin inntekt" },
                        nynorsk { + "Dokumentasjon på din " + fritekst("ektefelle/partnar/sambuar") + " si inntekt" },
                        english { + "Documentation of your " + fritekst("spouse/partner/cohabitant") + "'s income" },
                    )
                }
                paragraph {
                    val beloep = fritekst("beløp")
                    text(
                        bokmal { + "Du har opplyst at " + fritekst("ektefellen/partneren/samboeren") +
                                " din har en inntekt som ikke overstiger gjeldende grunnbeløp i folketrygden på " +
                                beloep + " kroner." },

                        nynorsk { + "Du har opplyst at " + fritekst("ektefellen/partnaren/sambuaren") +
                                " din har ei inntekt som ikkje overstig gjeldande grunnbeløp i folketrygda på " +
                                beloep + " kroner." },

                        english { + "You have informed us that your " + fritekst("spouse/partner/cohabitant") +
                                " has an annual income which does not exceed the national insurance basic amount (1G), NOK " +
                                beloep + "." },
                    )
                }

                paragraph {
                    text(
                        bokmal { + "Du kan ha rett til å få pensjonen beregnet med en særskilt sats for minste pensjonsnivå dersom du forsørger " +
                                fritekst("ektefelle/partner/samboer") + " over 60 år. Dette skal vi nå vurdere." },

                        nynorsk { + "Du kan ha rett til å få pensjonen berekna med ein særskilt sats for minste pensjonsnivå dersom du forsørgjer " +
                                fritekst("ektefelle/partnar/sambuar") + " over 60 år. Dette skal vi nå vurdere." },

                        english { + "You may be entitled to the minimum pension level according to a special rate, if you provide for your " +
                                fritekst("spouse/partner/cohabitant") + " who is over the age of 60. We are now going to assess this." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vi mangler dokumentasjon på din " + fritekst("ektefelles/partners/samboers")
                                + " samlede inntekt. Med inntekt menes:" },
                        nynorsk { + "Vi manglar dokumentasjon på den samla inntekta til din " + fritekst("ektefelle/partnar/sambuar")
                                + ". Med inntekt meinast:" },
                        english { + "We do not have documentation of your " + fritekst("spouse/partner/cohabitant")
                                + "’s total income. As income we mean:" },
                    )
                }

                paragraph {
                    list {
                        item {
                            text(
                                bokmal { + "arbeidsinntekt i Norge, og eventuelt andre land" },
                                nynorsk { + "arbeidsinntekt i Noreg og eventuelt andre land" },
                                english { + "employment income in Norway and other countries" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "inntekt fra andre private og offentlige pensjonsordninger" },
                                nynorsk { + "inntekt frå andre private og offentlege pensjonsordningar" },
                                english { + "occupational pension from private or public schemes" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "ytelser og pensjoner fra andre land" },
                                nynorsk { + "ytingar og pensjonar frå andre land" },
                                english { + "benefits and pensions from other countries" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "ytelser fra Nav, blant annet sykepenger og arbeidsavklaringspenger (AAP)" },
                                nynorsk { + "ytingar frå Nav, blant anna sjukepengar og arbeidsavklaringspengar (AAP)" },
                                english { + "other national insurance benefits, for example sickness benefits and work assessment allowance (AAP)" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "kapitalinntekt" },
                                nynorsk { + "kapitalinntekt" },
                                english { + "capital income" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "livrente" },
                                nynorsk { + "livrente" },
                                english { + "annuity income" },
                            )
                        }
                    }
                    text(
                        bokmal { + "Som dokumentasjon kan du sende kopi av skatteoppgjøret for siste år. Vi godtar også bekreftelse fra regnskapsfører, årsoppgave fra bank eller kopier av lønns- og trekkoppgaver." },
                        nynorsk { + "Som dokumentasjon kan du sende kopi av skatteoppgjeret for det siste året. Vi godtar også stadfesting frå rekneskapsførar, årsoppgåve frå bank eller kopiar av lønns- og trekkoppgåver." },
                        english { + "As documentation, you can submit a copy of the tax settlement for the previous year. Alternatively, you can submit a confirmation from an accountant, a year-end bank statement or copies of certificates of pay and tax deducted." },
                    )
                }
            }

            showIf(saksbehandlerValg.eps62aarOgInntektUnder1gBoddArbeidUtland) {
                title1 {
                    text(
                        bokmal { + "Opplysninger om din ektefelles/partners/samboers opphold i utlandet" },
                        nynorsk { + "Opplysningar om din ektefelles/partnars/sambuars opphald i utlandet" },
                        english { + "Information about your spouse/partner/cohabitant’s stays abroad" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Alderspensjonen din er beregnet med en særskilt sats for minste pensjonsnivå fordi du forsørger " + fritekst(
                            "ektefelle/partner/samboer"
                        ) + " over 60 år. " },
                        nynorsk { + "Alderspensjonen din er berekna med ein særskilt sats for minste pensjonsnivå fordi du forsørgjer " + fritekst(
                            "ektefelle/partnar/sambuar"
                        ) + " over 60 år. " },
                        english { + "Your retirement pension is calculated according to a special rate which gives you the minimum pension level, because you provide for your " + fritekst(
                            "spouse/partner/cohabitant"
                        ) + " who is over the age of 60. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Hvis ektefellen/partneren/samboeren din kan ta ut full alderspensjon fra folketrygden fra måneden etter fylte 62 år vil du ikke lenger får pensjonen beregnet etter den særskilte satsen." },
                        nynorsk { + "Dersom ektefellen/partnaren/sambuaren din kan ta ut full alderspensjon frå folketrygda frå månaden etter fylte 62 år vil du ikkje lenger får pensjonen berekna etter den særskilte satsen." },
                        english { + "If your spouse/partner/cohabitant is entitled to draw a 100 percent retirement pension at the age of 62, you are no longer entitled to the minimum pension level according to the special rate." },
                    )
                }

                paragraph {
                    text(
                        bokmal { + "Vi mangler opplysninger om din " + fritekst("ektefelle/partner/samboer")
                                + " har bodd og/eller arbeidet i andre land enn Norge. " },

                        nynorsk { + "Vi manglar opplysningar om din " + fritekst("ektefelle/partnar/sambuar")
                                + " har budd og/eller arbeida i andre land enn Noreg." },

                        english { + "We do not have any information on whether your " + fritekst("spouse/partner/cohabitant")
                                + " has lived and/or worked in other countries than Norway. " },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Du må sende oss disse opplysningene:" },
                        nynorsk { + "Du må sende oss desse opplysningane:" },
                        english { + "You must send us the following information:" },
                    )
                    list {
                        item {
                            text(
                                bokmal { + "hvilke land ektefellen/partneren/samboeren din bodde og/eller arbeidet i" },
                                nynorsk { + "kva land ektefellen/partnaren/sambuaren din har budd og/eller arbeidd i" },
                                english { + "which countries your spouse/partner/cohabitant has lived and/or worked in" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "hvilke perioder dette var" },
                                nynorsk { + "kva periodar dette var" },
                                english { + "what time periods were these" },
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { + "Retten til alderspensjon avhenger blant annet av trygdetid, som er tid med medlemskap i folketrygden etter fylte 16 år. Hvis du ikke sender inn opplysninger, forutsetter vi at ektefellen/partneren/samboeren din har full trygdetid (40 år) i Norge." },
                        nynorsk { + "Retten til alderspensjon avheng blant anna av trygdetid, som er tida med medlemskap i folketrygda etter fylte 16 år. Dersom du ikkje sender inn opplysningar, forutsett vi at ektefellen/partnaren/sambuaren din har full trygdetid (40 år) i Noreg." },
                        english { + "The right for retirement pension depends, among other factors, on how long a person has been a member of the National Insurance Scheme, after the age of 16. If you do not send us any information, we will assume that your spouse/partner/cohabitant has a full period of national insurance coverage (40 years) in Norway." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "På $DIN_PENSJON_URL kan ektefellen/partneren/samboeren din sjekke sin rett til å ta ut full alderspensjon fra måneden etter fylte 62 år." },
                        nynorsk { + "På $DIN_PENSJON_URL kan ektefellen/partnaren/sambuaren din sjekke sin rett til å ta ut full alderspensjon frå månaden etter fylte 62 år." },
                        english { + "Your spouse/partner/cohabitant can log on to $DIN_PENSJON_URL to find out if he/she is entitled to draw a 100 percent retirement pension at the age of 62." },
                    )
                }
            }

            showIf(saksbehandlerValg.epsInntektUnder2g) {
                title1 {
                    text(
                        bokmal { + "Dokumentasjon på din ektefelle/partner/samboer sin inntekt" },
                        nynorsk { + "Dokumentasjon på din ektefelle/partnar/sambuar si inntekt" },
                        english { + "Documentation of your spouse/partner/cohabitant's income" },
                    )
                }
                paragraph {
                    val beloep = fritekst("beløp")
                    text(
                        bokmal { + "Vi mangler dokumentasjon på at din " +
                                fritekst("ektefelles/partners/samboers") +
                                " samlede inntekt er under to ganger folketrygdens grunnbeløp (2G), " +
                                beloep + " kroner. Med inntekt menes:" },

                        nynorsk { + "Vi manglar dokumentasjon på at din " +
                                fritekst("ektefelle/partnar/sambuar") +
                                " si samla inntekt er under to gonger grunnbeløpet i folketrygda (2G), " +
                                beloep + " kroner. Med inntekt meinast:" },

                        english { + "We do not have documentation that your " +
                                fritekst("spouse/partner/cohabitant") +
                                "’s total income is lower than twice the national insurance basic amount (2G), NOK " +
                                beloep + ". As income we mean:" },
                    )
                    list {
                        item {
                            text(
                                bokmal { + "arbeidsinntekt i Norge og eventuelt andre land" },
                                nynorsk { + "arbeidsinntekt i Noreg og eventuelt andre land" },
                                english { + "employment income in Norway and other countries" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "inntekt fra andre private og offentlige pensjonsordninger" },
                                nynorsk { + "inntekt frå andre private og offentlege pensjonsordningar" },
                                english { + "occupational pension from private or public schemes" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "ytelser og pensjoner fra andre land" },
                                nynorsk { + "ytingar og pensjonar frå andre land" },
                                english { + "benefits and pensions from other countries" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "ytelser fra Nav, blant annet sykepenger og arbeidsavklaringspenger (AAP)" },
                                nynorsk { + "ytingar frå Nav, blant anna sjukepengar og arbeidsavklaringspengar (AAP)" },
                                english { + "other national insurance benefits, for example sickness benefits and work assessment allowance (AAP)" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "kapitalinntekt" },
                                nynorsk { + "kapitalinntekt" },
                                english { + "capital income" },
                            )
                        }
                        item {
                            text(
                                bokmal { + "livrente" },
                                nynorsk { + "livrente" },
                                english { + "annuity income" },
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { + "Som dokumentasjon kan du sende kopi av skatteoppgjøret for siste år. Vi godtar også bekreftelse fra regnskapsfører, årsoppgave fra bank eller kopier av lønns- og trekkoppgaver." },
                        nynorsk { + "Som dokumentasjon kan du sende kopi av skatteoppgjeret for det siste året. Vi godtar også stadfesting frå rekneskapsførar, årsoppgåve frå bank eller kopiar av lønns- og trekkoppgåver." },
                        english { + "As documentation, you can submit a copy of the tax settlement for the previous year. Alternatively, you can submit a confirmation from an accountant, a year-end bank statement or copies of certificates of pay and tax deducted." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Inntektsopplysningene er nødvendige for å kunne beregne pensjonen din korrekt." },
                        nynorsk { + "Inntektsopplysningane er nødvendige for å kunne berekne pensjonen din korrekt." },
                        english { + "The income information is necessary to calculate your pension correctly." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "For å ha rett til full grunnpensjon, må du dokumentere at inntekten til " +
                                fritekst("ektefellen/partneren/samboeren") + " din er under 2G." },

                        nynorsk { + "For å ha rett til full grunnpensjon må du dokumentere at inntekta til " +
                                fritekst("ektefellen/partnaren/sambuaren") + " din er under 2G." },

                        english { + "In order to be entitled to a 100 percent basic pension, you must document that your " +
                                fritekst("spouse/partner/cohabitant") + "’s income is less than 2G." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +  "Dersom vi ikke får dokumentasjon på at inntekten til " +
                                fritekst("ektefellen/partneren/samboeren") + " din er under 2G vil du kun få en grunnpensjon på 90 prosent av grunnbeløpet." },

                        nynorsk { +  "Dersom vi ikkje får dokumentasjon på at inntekta til " +
                                fritekst("ektefellen/partnaren/sambuaren") + " din er under 2G, vil du berre få ein grunnpensjon på 90 prosent av grunnbeløpet." },

                        english { +  "If we do not receive documentation that your " +
                                fritekst("spouse/partner/cohabitant") + "’s income is below 2G, you will only receive a basic pension of 90 percent of the basic amount." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +  "Du trenger ikke legge ved dokumentasjon dersom inntekten er over 2G. Du kan bare gi oss beskjed om dette." },
                        nynorsk { +  "Du treng ikkje leggje ved dokumentasjon dersom inntekta er over 2G. Du kan berre gi oss beskjed om dette." },
                        english { +  "You do not need to provide documentation if the income is over 2G. Simply notify us about this." },
                    )
                }
            }

            showIf(saksbehandlerValg.forsoergerEpsBosattIUtlandet) {
                title1 {
                    text(
                        bokmal { +  "Dokumentasjon på at du forsørger " + fritekst("ektefellen/partneren/samboeren") + " din" },
                        nynorsk { +  "Dokumentasjon på at du forsørgjer " + fritekst("ektefellen/partnaren/sambuaren") + " din" },
                        english { +  "Documentation that you provide for your " + fritekst("spouse/partner/cohabitant") },
                    )
                }
                paragraph {
                    text(
                        bokmal { +  "Vi skal vurdere om du har rett til pensjon beregnet etter særskilt sats. Dette kan gi deg en høyere pensjon. For å få særskilt sats må du dokumentere at du forsørger din ektefelle/partner/samboer. Eksempler på dokumentasjon er bankoverføringer. Det må dokumenteres minst tre overføringer, du må stå som avsender og din ektefelle/partner/samboer som mottaker." },
                        nynorsk { +  "Vi skal vurdere om du har rett til pensjon berekna etter særskilt sats. Dette kan gi deg ein høgare pensjon. For å få særskilt sats må du dokumentere at du forsørgjer din ektefelle/partnar/sambuar. Eksempel på dokumentasjon er bankoverføringar. Det må dokumenterast minst tre overføringar, du må stå som avsendar og din ektefelle/partnar/sambuar som mottakar." },
                        english { +  "We will assess your eligibility for retirement pension according to a special rate. This may give you a higher pension. To get this special rate you must document that you provide for your spouse/partner/cohabitant. An example of such documentation is bank transfers, of which there must be at least three. You must be the person who transfers the money, and your spouse/partner/cohabitant must be the receiver." },
                    )
                }
            }

            showIf(saksbehandlerValg.manglendeOpptjening) {
                title1 {

                    text(
                        bokmal { +  "Manglende opptjening" },
                        nynorsk { +  "Manglande opptening" },
                        english { +  "Period(-s) with no pension earnings" },
                    )

                }

                paragraph {
                    text(
                        bokmal { +  "Ifølge opplysningene vi har mottatt, har du ikke hatt pensjonsopptjening i " +
                                fritekst("året/årene") + ". Vi ber om at du kort forklarer årsaken til dette." },

                        nynorsk { +  "Ifølgje opplysningane vi har fått, har du ikkje hatt pensjonsopptening i " +
                                fritekst("året/åra") + ". Vi ber om at du kort forklarar årsaka til dette." },

                        english { +  "According to the information we have received, you have no pension earnings for " +
                                fritekst("the year/the years") + ". We ask you to provide a brief account of the cause for this." },
                    )
                }
            }

            showIf(saksbehandlerValg.boOgArbeidsperioder) {
                title1 {
                    text(
                        bokmal { +  "Bo- og arbeidsperioder i Norge" },
                        nynorsk { +  "Bu- og arbeidsperiodar  i Noreg" },
                        english { +  "Periods in Norway" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +  "Du må oppgi hvor og når du var bosatt og/eller arbeidet i Norge. For å sikre at vi har riktige opplysninger om dine arbeidsperioder ber vi deg om å dokumentere disse. Dette kan være kopier av attester, lønnslipper eller skattemelding." },
                        nynorsk { +  "Du må gi opp kvar og når du var busett og/eller arbeidde i Noreg. For å sikre at vi har riktige opplysningar om arbeidsperiodane dine, ber vi deg om å dokumentere desse. Det kan vere kopiar av attestar, lønnsslippar eller skattemelding." },
                        english { +  "You must state where and when you lived and/or worked in Norway. To ensure that we have the correct information about your work periods, we ask you to provide documentation, such as copies of references, pay slips or tax returns." },
                    )
                }
            }

            showIf(saksbehandlerValg.bosattIEoesLandSedErEoesBlanketter) {
                title1 {
                    text(
                        bokmal { +  "Søknad fra EØS-land" },
                        nynorsk { +  "Søknad frå EØS-land" },
                        english { +  "Application from an EEA country " },
                    )
                }
                paragraph {
                    text(
                        bokmal { +  "Dersom man er bosatt i et EØS-land, og har opparbeidet seg pensjonsrettigheter i dette landet, følger det av EØS-reglementet at man skal søke om pensjon fra annet EØS-land gjennom bostedslandets nasjonale myndigheter. Vi ber derfor om at du kontakter " + fritekst(" ") + " trygdemyndigheter for oversendelse av SED-dokumentene P2000, P5000 og P4000, eventuelt EØS-blankettene E202, E205 og E207." },
                        nynorsk { +  "Dersom man bur i eit EØS-land, og har opparbeida seg pensjonsrettighetar i dette landet, følgjer det av EØS-reglementet at man skal søkje om pensjon frå anna EØS-land gjennom bustadslandet sine nasjonale myndigheiter. Vi ber derfor om at du kontaktar " + fritekst(" ") + " trygdemyndigheiter for oversendelse av SED-dokumenta P2000, P5000 og P4000, eventuelt EØS-blankettane E202, E205 og E207." },
                        english { +  "According to the EEA agreement, a resident of an EEA country has to forward his/her claim for benefits in another EEA country through the national authorities in the country of residence. In order for us to process your claim for retirement pension from the Norwegian National Insurance Scheme, you must contact your local authorities." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +  "Din søknad kan ikke bli behandlet før vi mottar disse blankettene." },
                        nynorsk { +  "Din søknad kan ikkje bli behandla før vi mottar desse blankettane." },
                        english { +  "No decision will be made until the SED-documents P2000, P5000 and P4000, or the forms E202, E205 and E207 are provided." },
                    )
                }
            }
            showIf(saksbehandlerValg.tidspunktForUttak) {
                title1 {
                    text(
                        bokmal { +  "Tidspunkt for uttak/ønsket uttaksgrad" },
                        nynorsk { +  "Tidspunkt for uttak/ønska uttaksgrad " },
                        english { +  "Statement for your pension " },
                    )
                }
                paragraph {
                    val dato = fritekst("dato")
                    text(
                        bokmal { +  "Du har ikke oppgitt når du ønsker å ta ut pensjon. Du må derfor fylle ut skjemaet du får sammen med dette brevet. Dersom du ikke sender oss informasjon om når du ønsker å ta ut pensjon, tar vi utgangspunkt i datoen du sendte søknaden til " +
                                fritekst("oss/trygdemyndighetene i bostedslandet ditt") + ". Det vil si " + dato },
                        nynorsk { +  "Du har ikkje oppgitt når du ønskjer å ta ut pensjon. Du må derfor fylle ut skjemaet du får saman med dette brevet. Dersom du ikkje sender oss informasjon om når du ønskjer å ta ut pensjon, vil vi ta utgangspunkt i datoen då du sende søknaden til " +
                                fritekst("oss/trygdeorganet i bustadslandet ditt") + ". Det vil seie " + dato },
                        english { +  "You have not stated when you want to start drawing your pension. Therefore, you must fill out the form enclosed with this letter. If you do not send us information about when you want to start drawing your pension, we will use the date you submitted the application to " +
                                fritekst("us,/the National Insurance authorities in your country of residence,") + " i.e. " + dato },
                    )
                }
                paragraph {
                    text(
                        bokmal { +  "Du har ikke oppgitt hvilken uttaksgrad av alderspensjon du ønsker. " +
                                fritekst("Vi kan ikke behandle søknaden din før vi får svar fra deg om dette./Dersom du ikke sender oss informasjon om uttaksgraden, setter vi den til 100 prosent.") },

                        nynorsk { +  "Du har ikkje oppgitt kva uttaksgrad av alderspensjon du ønskjer å ta ut. " +
                                fritekst("Vi kan ikkje behandle søknaden din før vi får svar frå deg om dette./Dersom du ikkje sender oss informasjon om uttaksgraden, set vi den til 100 prosent.") },

                        english { +  "You have not stated what percentage of your retirement pension you want to draw. " +
                                fritekst("We cannot process your application until we receive a response from you./If you do not send us information about what withdrawal percentage you want, the percentage will be set to 100 percent.") },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +  "Du kan gi tilbakemelding via Nav sin nettside $DITT_NAV. Velg " + quoted("Send beskjed til Nav") + ", tema " + quoted("Beskjed – pensjon") + "." },
                    nynorsk { +  "Du kan gi tilbakemelding via Nav si nettside $DITT_NAV. Vel " + quoted("Send beskjed til Nav") +", tema " + quoted("Beskjed – pensjon") + "." },
                    english { +  "Feel free to contact us and give us the required information at Nav’s online service $DITT_NAV. Select " + quoted("Send beskjed til Nav") +" and the theme " + quoted("Beskjed – pensjon") + "." },
                )
            }
            paragraph {
                val svarfristDato = fritekst("dato svarfrist")
                text(
                    bokmal { +  "Skriftlig tilbakemelding per post og eventuell dokumentasjon må du sende innen "
                            + svarfristDato + " til følgende adresse:" },
                    nynorsk { +  "Skriftleg tilbakemelding per post og eventuell dokumentasjon må du sende innan "
                            + svarfristDato + " til følgande adresse:" },
                    english { +  "Please send the required documentation within "
                            + svarfristDato + " to the address below:" },
                )
            }
            includePhrase(Alderspensjon.Returadresse)
            paragraph {
                text(
                    bokmal { +  "Dersom vi ikke mottar nødvendig dokumentasjonen innen fristen, vil saken bli avgjort med de opplysninger som foreligger. Hvis vi ikke har nok opplysninger til å behandle søknaden din, kan saken bli avslått." },
                    nynorsk { +  "Dersom vi ikkje mottar nødvendig dokumentasjon innan fristen, vil saka bli avgjord med de opplysningane som foreligg. Hvis vi ikkje har nok opplysningar til å behandle saka di, kan saka di bli avslått." },
                    english { +  "If we do not receive the necessary documentation or an explanation within the deadline, your application will either be processed using the information available to us or postponed until we have received the information we need. If we do not receive the necessary information, we will not have enough information to process your case, and your application may be declined." },
                )
            }
            paragraph {
                text(
                    bokmal { +  "Dette går fram av folketrygdloven §§ 21-3 og 21-7." },
                    nynorsk { +  "Dette går fram av folketrygdlova §§ 21-3 og 21-7." },
                    english { +  "This follows from sections 21-3 and 21-7 of the National Insurance Act." },
                )
            }
            paragraph {
                text(
                    bokmal { +  "Du kan finne mer informasjon om regelverket på $NAV_URL" },
                    nynorsk { +  "Du kan finne meir informasjon om regelverket på $NAV_URL" },
                    english { +  "You can find more information about the regulations at $NAV_URL" },
                )
            }
            title1 {
                text(
                    bokmal { +  "Svartid" },
                    nynorsk { +  "Svartid" },
                    english { +  "Response time" },
                )
            }
            paragraph {
                text(
                    bokmal { +  "Saken vil bli behandlet innen " + fritekst("antall dager/uker/måneder") + " etter at vi har mottatt nødvendige opplysninger/dokumentasjon. Hvis saken ikke blir avgjort i løpet av denne tiden, vil du høre nærmere fra oss." },
                    nynorsk { +  "Saka vil bli behandla innan " + fritekst("talet på dagar/veker/månader") + " etter at vi har fått nødvendige opplysningar/dokumentasjon. Dersom saka ikkje blir avgjord i løpet av denne tida, vil du høyre nærare frå oss." },
                    english { +  "Your case will be processed within " + fritekst("antall days/weeks/months") + " after we have received the necessary information and/or documentation. If your case has not been decided within this time, you will hear from us." },
                )
            }

            includePhrase(Felles.MeldeFraEndringer)

            paragraph {
                text(
                    bokmal { +  "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav." },
                    nynorsk { +  "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav." },
                    english { +  "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to Nav." },
                )
            }

            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }

}