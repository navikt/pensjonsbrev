package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder

import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.harInntektOver2G_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.mottarPensjon_safe
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class OpplysningerBruktIBeregningenSivilstand(
    val beregnetSomEnsligPgaInstitsusjon: Expression<Boolean>,
    val epsVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.EPSvedVirk?>,
    val alderspensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.AlderspensjonVedVirk>,
    val beregnetPensjonPerManedVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.AlderspensjonPerManed>
): OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        //sivilstandOverskrift_001
        title1 {
            text(
                Bokmal to "Sivilstand",
                Nynorsk to "Sivilstand",
                English to "Civil status",
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
                    Bokmal to "Vi har lagt til grunn at du er gift.",
                    Nynorsk to "Vi har lagt til grunn at du er gift.",
                    English to "We have registered that you have a spouse.",
                )
            }

            //vedleggBeregnGiftLeverAdskilt_001
            showIf(beregnetSomEnsligPgaInstitsusjon) {
                paragraph {
                    text(
                        Bokmal to "Du og ektefellen din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig.",
                        Nynorsk to "Du og ektefellen din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg.",
                        English to "You and your spouse are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single.",
                    )
                }
            }.orShowIf(epsBorSammenMedBruker) {
                //vedleggBeregnEktefellePensjon_001
                showIf(epsMottarPensjon) {
                    paragraph {
                        text(
                            Bokmal to "Vi har registrert at ektefellen din mottar uføretrygd, pensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for.",
                            Nynorsk to "Vi har registrert at ektefellen din får uføretrygd, pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for.",
                            English to "We have registered that your spouse is receiving disability benefit, a national insurance pension or contractual early retirement pension (AFP) which earns pension points.",
                        )
                    }
                }

                //vedleggBeregnEktefelleOver2G_001
                showIf(epsHarInntektOver2G and epsMottarPensjon) {
                    paragraph {
                        text(
                            Bokmal to "Vi har registrert at ektefellen din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G).",
                            Nynorsk to "Vi har registrert at ektefellen din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G).",
                            English to "We have registered that your spouse has an annual income that exceeds twice the national insurance basic amount (G).",
                        )
                    }
                }

                //vedleggBeregnEktefelleUnder2G_001
                showIf(not(epsHarInntektOver2G) and not(epsMottarPensjon)) {
                    paragraph {
                        text(
                            Bokmal to "Vi har registrert at ektefellen din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G).",
                            Nynorsk to "Vi har registrert at ektefellen din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G).",
                            English to "We have registered that your spouse has an annual income lower than twice the national insurance basic amount (G).",
                        )
                    }
                }
            }
        }.orShowIf(brukersSivilstand.isOneOf(GLAD_EKT, SEPARERT)) {
            //vedleggBeregnGift_001
            paragraph {
                text(
                    Bokmal to "Vi har lagt til grunn at du er gift.",
                    Nynorsk to "Vi har lagt til grunn at du er gift.",
                    English to "We have registered that you have a spouse.",
                )
            }

            //vedleggBeregnGiftLeverAdskilt_001
            paragraph {
                text(
                    Bokmal to "Du og ektefellen din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig.",
                    Nynorsk to "Du og ektefellen din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg.",
                    English to "You and your spouse are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single.",
                )
            }
        }.orShowIf(brukersSivilstand.isOneOf(PARTNER)) {
            //vedleggBeregnParner_001
            paragraph {
                text(
                    Bokmal to "Vi har lagt til grunn at du er partner.",
                    Nynorsk to "Vi har lagt til grunn at du er partner.",
                    English to "We have registered that you have a partner.",
                )
            }
            showIf(beregnetSomEnsligPgaInstitsusjon) {
                //vedleggBeregnPartnerLeerAdskilt_001
                paragraph {
                    text(
                        Bokmal to "Du og partneren din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig.",
                        Nynorsk to "Du og partnaren din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg.",
                        English to "You and your partner are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single.",
                    )
                }
            }.orShowIf(epsBorSammenMedBruker) {
                //vedleggBeregnPartnerPensjon_001
                showIf(epsMottarPensjon) {
                    paragraph {
                        text(
                            Bokmal to "Vi har registrert at partneren din mottar uføretrygd, pensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for.",
                            Nynorsk to "Vi har registrert at partnaren din får uføretrygd, pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for.",
                            English to "We have registered that your partner is receiving disability benefit, a national insurance pension or contractual early retirement pension (AFP) which earns pension points.",
                        )
                    }
                }.orShow {
                    //vedleggBeregnParnerOver2G_001
                    showIf(epsHarInntektOver2G) {
                        paragraph {
                            text(
                                Bokmal to "Vi har registrert at partneren din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G).",
                                Nynorsk to "Vi har registrert at partnaren din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G).",
                                English to "We have registered that your partner has an annual income that exceeds twice the national insurance basic amount (G).",
                            )
                        }
                    }.orShow {
                        //vedleggBeregnParnerUnder2G
                        paragraph {
                            text(
                                Bokmal to "Vi har registrert at partneren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G).",
                                Nynorsk to "Vi har registrert at partnaren din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G).",
                                English to "We have registered that your partner has an annual income lower than twice the national insurance basic amount (G).",
                            )
                        }
                    }
                }

            }
        }.orShowIf(brukersSivilstand.isOneOf(GLAD_PART, SEPARERT_PARTNER)) {
            //vedleggBeregnPartner_001
            paragraph {
                text(
                    Bokmal to "Vi har lagt til grunn at du er partner.",
                    Nynorsk to "Vi har lagt til grunn at du er partnar.",
                    English to "We have registered that you have a partner.",
                )
            }
            //vedleggBeregnPartnerLeverAdskilt_001
            paragraph {
                text(
                    Bokmal to "Du og partneren din er registrert med forskjellig bosted, eller en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig.",
                    Nynorsk to "Du og partnaren din er registrerte med forskjellig bustad, eller ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg.",
                    English to "You and your partner are registered with different residences, or one of you is living in an institution. Therefore, your pension has been calculated as if you were single.",
                )
            }
        }.orShowIf(brukersSivilstand.isOneOf(SAMBOER_3_2, SAMBOER_1_5)) {
            showIf(brukersSivilstand.isOneOf(SAMBOER_3_2)) {
                //vedleggBeregnSambo§3-2_001
                paragraph {
                    text(
                        Bokmal to "Vi har lagt til grunn at du er samboer (jf. folketrygdloven § 3-2 femte ledd).",
                        Nynorsk to "Vi har lagt til grunn at du er sambuar (jf. folketrygdlova § 3-2 femte ledd).",
                        English to "We have registered that you have a cohabitant (cf. § 3-2 section 5 of the National Insurance Act).",
                    )
                }
            }
            //vedleggBeregnSambo§1-5_001
            showIf(brukersSivilstand.isOneOf(SAMBOER_1_5)) {
                paragraph {
                    text(
                        Bokmal to "Vi har lagt til grunn at du er samboer (jf. folketrygdloven § 1-5).",
                        Nynorsk to "Vi har lagt til grunn at du er sambuar (jf. folketrygdlova § 1-5).",
                        English to "We have registered that you have a cohabitant (cf. § 1-5 of the National Insurance Act).",
                    )
                }
            }
            showIf(not(beregnetSomEnsligPgaInstitsusjon) and epsBorSammenMedBruker) {
                showIf(epsMottarPensjon) {
                    //vedleggBeregnSamboPensjon_001
                    paragraph {
                        text(
                            Bokmal to "Vi har registrert at samboeren din mottar uføretrygd, pensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for.",
                            Nynorsk to "Vi har registrert at sambuaren din får uføretrygd, pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for.",
                            English to "We have registered that your cohabitant is receiving disability benefit, a national insurance pension or contractual early retirement pension (AFP) which earns pension points.",
                        )
                    }
                }.orShow {
                    showIf(epsHarInntektOver2G) {
                        //vedleggBeregnSamboOver2G_001
                        paragraph {
                            text(
                                Bokmal to "Vi har registrert at samboeren din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G).",
                                Nynorsk to "Vi har registrert at sambuaren din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G).",
                                English to "We have registered that your cohabitant has an annual income that exceeds twice the national insurance basic amount (G).",
                            )
                        }
                    }.orShow {
                        //vedleggBeregnSamboUnder2G_001
                        paragraph {
                            text(
                                Bokmal to "Vi har registrert at samboeren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G).",
                                Nynorsk to "Vi har registrert at sambuaren din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G).",
                                English to "We have registered that your cohabitant has an annual income lower than twice the national insurance basic amount (G).",
                            )
                        }
                    }
                }
            }
        }.orShowIf(brukersSivilstand.isOneOf(ENSLIG, ENKE)) {
            paragraph {
                text(
                    Bokmal to "Vi har lagt til grunn at du er enslig.",
                    Nynorsk to "Vi har lagt til grunn at du er einsleg.",
                    English to "We have registered that you have a civil status as a single person.",
                )
            }
        }

        paragraph {
            text(
                Bokmal to "Dette har betydning for størrelsen på alderspensjonen din.",
                Nynorsk to "Dette har betydning for storleiken på alderspensjonen din.",
                English to "This will affect the size of your retirement pension.",
            )
        }
    }
}