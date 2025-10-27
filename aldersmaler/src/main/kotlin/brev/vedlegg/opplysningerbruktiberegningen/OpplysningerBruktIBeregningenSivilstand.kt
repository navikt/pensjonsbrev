package brev.vedlegg.opplysningerbruktiberegningen

import no.nav.pensjon.brev.api.model.maler.MetaforceSivilstand.*
import no.nav.pensjon.brev.model.alder.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.maler.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.maler.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.api.model.maler.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.harInntektOver2G_safe
import no.nav.pensjon.brev.api.model.maler.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.mottarPensjon_safe
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.text


data class OpplysningerBruktIBeregningenSivilstand(
    val beregnetSomEnsligPgaInstitusjon : Expression<Boolean>,
    val epsVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.EPSvedVirk?>,
    val alderspensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.AlderspensjonVedVirk>,
    val beregnetPensjonPerManedVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.AlderspensjonPerManed>
): OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        //sivilstandOverskrift_001
        title1 {
            text(
                bokmal { + "Sivilstand" },
                nynorsk { + "Sivilstand" },
                english { + "Civil status" },
            )
        }

        val epsBorSammenMedBruker = epsVedVirk.borSammenMedBruker_safe.ifNull(false)
        val epsMottarPensjon = epsVedVirk.mottarPensjon_safe.ifNull(false)
        val epsHarInntektOver2G = epsVedVirk.harInntektOver2G_safe.ifNull(false)


        //vedleggBeregnGift_001
        val brukersSivilstand = beregnetPensjonPerManedVedVirk.brukersSivilstand
        showIf(brukersSivilstand.isOneOf(GIFT)) {
            paragraph {
                text(
                    bokmal { + "Vi har lagt til grunn at du er gift." },
                    nynorsk { + "Vi har lagt til grunn at du er gift." },
                    english { + "We have registered that you have a spouse." },
                )
            }

            //vedleggBeregnGiftLeverAdskilt_001
            showIf(beregnetSomEnsligPgaInstitusjon ) {
                paragraph {
                    text(
                        bokmal { + "Du og ektefellen din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig." },
                        nynorsk { + "Du og ektefellen din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg." },
                        english { + "You and your spouse are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single." },
                    )
                }
            }.orShowIf(epsBorSammenMedBruker) {
                //vedleggBeregnEktefellePensjon_001
                showIf(epsMottarPensjon) {
                    paragraph {
                        text(
                            bokmal { + "Vi har registrert at ektefellen din mottar uføretrygd, pensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for." },
                            nynorsk { + "Vi har registrert at ektefellen din får uføretrygd, pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for." },
                            english { + "We have registered that your spouse is receiving disability benefit, a national insurance pension or contractual early retirement pension (AFP) which earns pension points." },
                        )
                    }
                }

                showIf(not(epsMottarPensjon)) {
                    showIf(epsHarInntektOver2G) {
                        //vedleggBeregnEktefelleOver2G_001
                        paragraph {
                            text(
                                bokmal { + "Vi har registrert at ektefellen din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G)." },
                                nynorsk { + "Vi har registrert at ektefellen din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G)." },
                                english { + "We have registered that your spouse has an annual income that exceeds twice the national insurance basic amount (G)." },
                            )
                        }
                    }.orShow {
                        //vedleggBeregnEktefelleUnder2G_001
                        paragraph {
                            text(
                                bokmal { + "Vi har registrert at ektefellen din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G)." },
                                nynorsk { + "Vi har registrert at ektefellen din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G)." },
                                english { + "We have registered that your spouse has an annual income lower than twice the national insurance basic amount (G)." },
                            )
                        }
                    }
                }
            }
        }.orShowIf(brukersSivilstand.isOneOf(GLAD_EKT, SEPARERT)) {
            //vedleggBeregnGift_001
            paragraph {
                text(
                    bokmal { + "Vi har lagt til grunn at du er gift." },
                    nynorsk { + "Vi har lagt til grunn at du er gift." },
                    english { + "We have registered that you have a spouse." },
                )
            }

            //vedleggBeregnGiftLeverAdskilt_001
            paragraph {
                text(
                    bokmal { + "Du og ektefellen din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig." },
                    nynorsk { + "Du og ektefellen din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg." },
                    english { + "You and your spouse are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single." },
                )
            }
        }.orShowIf(brukersSivilstand.isOneOf(PARTNER)) {
            //vedleggBeregnParner_001
            paragraph {
                text(
                    bokmal { + "Vi har lagt til grunn at du er partner." },
                    nynorsk { + "Vi har lagt til grunn at du er partner." },
                    english { + "We have registered that you have a partner." },
                )
            }
            showIf(beregnetSomEnsligPgaInstitusjon ) {
                //vedleggBeregnPartnerLeerAdskilt_001
                paragraph {
                    text(
                        bokmal { + "Du og partneren din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig." },
                        nynorsk { + "Du og partnaren din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg." },
                        english { + "You and your partner are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single." },
                    )
                }
            }.orShowIf(epsBorSammenMedBruker) {
                //vedleggBeregnPartnerPensjon_001
                showIf(epsMottarPensjon) {
                    paragraph {
                        text(
                            bokmal { + "Vi har registrert at partneren din mottar uføretrygd, pensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for." },
                            nynorsk { + "Vi har registrert at partnaren din får uføretrygd, pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for." },
                            english { + "We have registered that your partner is receiving disability benefit, a national insurance pension or contractual early retirement pension (AFP) which earns pension points." },
                        )
                    }
                }.orShow {
                    //vedleggBeregnParnerOver2G_001
                    showIf(epsHarInntektOver2G) {
                        paragraph {
                            text(
                                bokmal { + "Vi har registrert at partneren din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G)." },
                                nynorsk { + "Vi har registrert at partnaren din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G)." },
                                english { + "We have registered that your partner has an annual income that exceeds twice the national insurance basic amount (G)." },
                            )
                        }
                    }.orShow {
                        //vedleggBeregnParnerUnder2G
                        paragraph {
                            text(
                                bokmal { + "Vi har registrert at partneren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G)." },
                                nynorsk { + "Vi har registrert at partnaren din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G)." },
                                english { + "We have registered that your partner has an annual income lower than twice the national insurance basic amount (G)." },
                            )
                        }
                    }
                }

            }
        }.orShowIf(brukersSivilstand.isOneOf(GLAD_PART, SEPARERT_PARTNER)) {
            //vedleggBeregnPartner_001
            paragraph {
                text(
                    bokmal { + "Vi har lagt til grunn at du er partner." },
                    nynorsk { + "Vi har lagt til grunn at du er partnar." },
                    english { + "We have registered that you have a partner." },
                )
            }
            //vedleggBeregnPartnerLeverAdskilt_001
            paragraph {
                text(
                    bokmal { + "Du og partneren din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig." },
                    nynorsk { + "Du og partnaren din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg." },
                    english { + "You and your partner are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single." },
                )
            }
        }.orShowIf(brukersSivilstand.isOneOf(SAMBOER_3_2, SAMBOER_1_5)) {
            showIf(brukersSivilstand.isOneOf(SAMBOER_3_2)) {
                //vedleggBeregnSambo§3-2_001
                paragraph {
                    text(
                        bokmal { + "Vi har lagt til grunn at du er samboer (jf. folketrygdloven § 3-2 femte ledd)." },
                        nynorsk { + "Vi har lagt til grunn at du er sambuar (jf. folketrygdlova § 3-2 femte ledd)." },
                        english { + "We have registered that you have a cohabitant (cf. § 3-2 section 5 of the National Insurance Act)." },
                    )
                }
            }
            //vedleggBeregnSambo§1-5_001
            showIf(brukersSivilstand.isOneOf(SAMBOER_1_5)) {
                paragraph {
                    text(
                        bokmal { + "Vi har lagt til grunn at du er samboer (jf. folketrygdloven § 1-5)." },
                        nynorsk { + "Vi har lagt til grunn at du er sambuar (jf. folketrygdlova § 1-5)." },
                        english { + "We have registered that you have a cohabitant (cf. § 1-5 of the National Insurance Act)." },
                    )
                }
            }
            showIf(not(beregnetSomEnsligPgaInstitusjon ) and epsBorSammenMedBruker) {
                showIf(epsMottarPensjon) {
                    //vedleggBeregnSamboPensjon_001
                    paragraph {
                        text(
                            bokmal { + "Vi har registrert at samboeren din mottar uføretrygd, pensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for." },
                            nynorsk { + "Vi har registrert at sambuaren din får uføretrygd, pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for." },
                            english { + "We have registered that your cohabitant is receiving disability benefit, a national insurance pension or contractual early retirement pension (AFP) which earns pension points." },
                        )
                    }
                }.orShow {
                    showIf(epsHarInntektOver2G) {
                        //vedleggBeregnSamboOver2G_001
                        paragraph {
                            text(
                                bokmal { + "Vi har registrert at samboeren din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G)." },
                                nynorsk { + "Vi har registrert at sambuaren din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G)." },
                                english { + "We have registered that your cohabitant has an annual income that exceeds twice the national insurance basic amount (G)." },
                            )
                        }
                    }.orShow {
                        //vedleggBeregnSamboUnder2G_001
                        paragraph {
                            text(
                                bokmal { + "Vi har registrert at samboeren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G)." },
                                nynorsk { + "Vi har registrert at sambuaren din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G)." },
                                english { + "We have registered that your cohabitant has an annual income lower than twice the national insurance basic amount (G)." },
                            )
                        }
                    }
                }
            }
        }.orShowIf(brukersSivilstand.isOneOf(ENSLIG, ENKE)) {
            paragraph {
                text(
                    bokmal { + "Vi har lagt til grunn at du er enslig." },
                    nynorsk { + "Vi har lagt til grunn at du er einsleg." },
                    english { + "We have registered that you have a civil status as a single person." },
                )
            }
        }

        paragraph {
            text(
                bokmal { + "Dette har betydning for størrelsen på alderspensjonen din." },
                nynorsk { + "Dette har betydning for storleiken på alderspensjonen din." },
                english { + "This will affect the size of your retirement pension." },
            )
        }
    }
}