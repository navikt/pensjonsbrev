package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.andelKap19_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.andelKap20_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BrukerSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.harInntektOver2G_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.mottarPensjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.aldersEllerSykehjem_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.ensligPgaInst_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.epsPaInstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.fengsel_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.helseinstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregningKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregningKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.epsVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.institusjonsoppholdVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.krav
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.tilleggspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidsdetaljerKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidsdetaljerKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.yrkesskadeDetaljerVedVirk
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregingTabellKap19
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningTabellKap20
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningenAlder =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenAlderDto>(
        title = newText(
            Bokmal to "Opplysninger brukt i beregningen",
            Nynorsk to "Opplysningar brukte i berekninga",
            English to "Information about your calculation",
        ),
        includeSakspart = true,
        outline = {

            //vedleggBeregnInnledn_001
            paragraph {
                text(
                    Bokmal to "I dette vedlegget finner du opplysninger om deg og din pensjonsopptjening som vi har brukt i beregningen av pensjonen din. Hvis du mener at opplysningene er feil, må du melde fra til Nav, fordi det kan ha betydning for størrelsen på pensjonen din.",
                    Nynorsk to "I dette vedlegget finn du opplysningar om deg og pensjonsoppteninga di som vi har brukt i berekninga av pensjonen din. Dersom du meiner at opplysningane er feil, må du melde frå til Nav, fordi det kan ha noko å seie for storleiken på pensjonen din.",
                    English to "This appendix contains information about you and your accumulated pension rights, which we have used to calculate your pension. If you think the information is incorrect, you must notify Nav, as this may affect the size of your pension.",
                )
            }

            //sivilstandOverskrift_001
            title1 {
                text(
                    Bokmal to "Sivilstand",
                    Nynorsk to "Sivilstand",
                    English to "Civil status",
                )
            }
            val beregnetSomEnsligPgaInstitsusjon =
                institusjonsoppholdVedVirk.aldersEllerSykehjem_safe.ifNull(false) or
                        institusjonsoppholdVedVirk.ensligPgaInst_safe.ifNull(false) or
                        institusjonsoppholdVedVirk.epsPaInstitusjon_safe.ifNull(false) or
                        institusjonsoppholdVedVirk.fengsel_safe.ifNull(false) or
                        institusjonsoppholdVedVirk.helseinstitusjon_safe.ifNull(false)
            val epsBorSammenMedBruker = epsVedVirk.borSammenMedBruker_safe.ifNull(false)
            val epsMottarPensjon = epsVedVirk.mottarPensjon_safe.ifNull(false)
            val epsHarInntektOver2G = epsVedVirk.harInntektOver2G_safe.ifNull(false)

            val regelverkstype = alderspensjonVedVirk.regelverkType

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

            title1 {
                text(
                    Bokmal to "Opplysninger brukt i beregningen av pensjonen din",
                    Nynorsk to "Opplysningar brukte i berekninga av pensjonen din",
                    English to "Information used to calculate your pension",
                )
            }

            val foedselsaar = bruker.foedselsdato.year
            showIf(regelverkstype.isOneOf(AlderspensjonRegelverkstype.AP2016)) {
                val andelKap19orZero = alderspensjonVedVirk.andelKap19_safe.ifNull(0)
                val andelKap20orZero = alderspensjonVedVirk.andelKap20_safe.ifNull(0)
                //vedleggBelopAP2016Oversikt_001
                paragraph {
                    textExpr(
                        Bokmal to "De som er født i perioden 1954–1962 får en kombinasjon av alderspensjon etter gamle og nye regler i folketrygdloven (kapittel 19 og 20). Fordi du er født i ".expr() +
                                foedselsaar.format() + " får du beregnet " + andelKap19orZero.format() + "/10 av pensjonen etter gamle regler, og "
                                + andelKap20orZero.format() + "/10 etter nye regler.",

                        Nynorsk to "Dei som er fødde i perioden 1954–1962, får ein kombinasjon av alderspensjon etter gamle og nye reglar i folketrygdlova (kapittel 19 og 20). Fordi du er fødd i ".expr() +
                                foedselsaar.format() + ", får du rekna ut " + andelKap19orZero.format() + "/10 av pensjonen etter gamle reglar, og "
                                + andelKap20orZero.format() + "/10 etter nye reglar.",

                        English to "Individuals born between 1954 and 1962 will receive a combination of retirement pension calculated on the basis of both old and new provisions in the National Insurance Act (Chapters 19 and 20). Because you are born in ".expr() +
                                foedselsaar.format() + ", " + andelKap19orZero.format() + "/10 of your pension is calculated on the basis of the old provisions, and "
                                + andelKap20orZero.format() + "/10 is calculated on the basis of new provisions.",
                    )
                }
            }

            paragraph {
                textExpr(
                    Bokmal to "Uttaksgraden for alderspensjonen din er ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent.",
                    Nynorsk to "Uttaksgraden for alderspensjonen din er ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent.",
                    English to "The rate of your retirement pension is ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " percent.",
                )
            }

            showIf(regelverkstype.isOneOf(AlderspensjonRegelverkstype.AP2016)) {
                paragraph {
                    text(
                        Bokmal to "For den delen av pensjonen din som er beregnet etter regler i kapittel 19 har vi brukt disse opplysningene i beregningen vår:",
                        Nynorsk to "For den delen av pensjonen din som er berekna etter reglar i kapittel 19, har vi brukt desse opplysningane i berekninga vår:",
                        English to "We have used the following information to calculate the part of your pension that comes under the provisions of Chapter 19:",
                    )
                }
            }

            includePhrase(
                OpplysningerBruktIBeregingTabellKap19(
                    trygdetidsdetaljerKap19VedVirk = trygdetidsdetaljerKap19VedVirk,
                    tilleggspensjonVedVirk = tilleggspensjonVedVirk,
                    beregnetPensjonPerManedVedVirk = beregnetPensjonPerManedVedVirk,
                    beregningKap19VedVirk = beregningKap19VedVirk,
                    beregningKap20VedVirk = beregningKap20VedVirk,
                    yrkesskadeDetaljerVedVirk = yrkesskadeDetaljerVedVirk,
                    alderspensjonVedVirk = alderspensjonVedVirk,
                )
            )

            showIf(regelverkstype.isOneOf(AlderspensjonRegelverkstype.AP2016)) {
                includePhrase(
                    OpplysningerBruktIBeregningTabellKap20(
                        beregnetPensjonPerManedVedVirk = beregnetPensjonPerManedVedVirk,
                        beregningKap19VedVirk = beregningKap19VedVirk,
                        trygdetidsdetaljerKap20VedVirk = trygdetidsdetaljerKap20VedVirk,
                        beregningKap20VedVirk = beregningKap20VedVirk,
                        krav = krav
                    )
                )
            }

            // TODO en sjekk som kanskje ikke gir mening:
            //


        }
    )