package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype.AP2016
import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.Beregningsmetode.*
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.andelKap19_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.andelKap20_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.erEksportberegnet
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.redusertTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.redusertTrygdetid_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BrukerSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.harInntektOver2G_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.mottarPensjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InngangOgEksportVurderingSelectors.eksportBeregnetUtenGarantipensjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.aldersEllerSykehjem_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.ensligPgaInst_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.epsPaInstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.fengsel_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.helseinstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidSelectors.fom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidSelectors.land
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidSelectors.tom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.beregningsmetode_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregningKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregningKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.epsVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.institusjonsoppholdVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.krav
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.tilleggspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidAvtaleland
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidNorge
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidsdetaljerKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidsdetaljerKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.yrkesskadeDetaljerVedVirk
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregingTabellKap19
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningTabellKap20
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
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
            showIf(regelverkstype.isOneOf(AP2016)) {
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

            showIf(regelverkstype.isOneOf(AP2016)) {
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

            showIf(regelverkstype.isOneOf(AP2016)) {
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
            //kategori = GetValue("fag=aldersovergangKategoriListe=aldersovergangKategori=kategori");
            // if (kategori == "")
            // ReturnValue("3");

            showIf(
                (trygdetidsdetaljerKap19VedVirk.beregningsmetode.notEqualTo(FOLKETRYGD)
                        and trygdetidsdetaljerKap20VedVirk.beregningsmetode_safe.notEqualTo(FOLKETRYGD))
                        or beregningKap19VedVirk.redusertTrygdetid
                        or beregningKap20VedVirk.redusertTrygdetid_safe.ifNull(false)
            ) {
                //trygdetidOverskrift_001
                title1 {
                    text(
                        Bokmal to "Trygdetid",
                        Nynorsk to "Trygdetid",
                        English to "Period of national insurance coverage",
                    )
                }
                //norskTTInfoGenerell_001
                paragraph {
                    text(
                        Bokmal to "Trygdetid er perioder du har vært medlem i folketrygden. Som hovedregel er dette perioder du har bodd eller arbeidet i Norge. Trygdetid har betydning for beregning av pensjonen din. Full trygdetid er 40 år.",
                        Nynorsk to "Trygdetid er periodar du har vore medlem i folketrygda. Som hovudregel er dette periodar du har budd eller arbeidd i Noreg. Trygdetid har betydning for berekninga av pensjonen din. Full trygdetid er 40 år.",
                        English to "The period of national insurance coverage is the periods in which you have been a member of the Norwegian National Insurance Scheme. As a general rule, these are periods when you have been registered as living or working in Norway. The period of national insurance coverage affects the calculation of your pension. The full insurance period is 40 years.",
                    )
                }
                //norskTTAP2016_002
                showIf(regelverkstype.isOneOf(AP2016)) {
                    paragraph {
                        text(
                            Bokmal to "Reglene for fastsetting av trygdetid er litt ulike i kapittel 19 og kapittel 20 i folketrygdloven. Du kan få trygdetid for perioder fra fylte 16 til 67 år som du har vært medlem i folketrygden. Etter kapittel 19 kan du i tillegg få trygdetid for år med pensjonspoeng, også fra fylte 67 til 75 år. Trygdetiden etter kapittel 19 kan derfor være høyere enn trygdetid etter kapittel 20 i enkelte tilfeller.",
                            Nynorsk to "Reglane for fastsetjing av trygdetid er ulike i kapittel 19 og kapittel 20 i folketrygdlova. Du kan få trygdetid for periodar frå fylte 16 til 67 år, som du har vore medlem i folketrygda. Etter kapittel 19 kan du i tillegg få trygdetid for år med pensjonspoeng, også frå fylte 67 til 75 år. Trygdetida etter kapittel 19 kan derfor være høgare enn trygdetida etter kapittel 20 i enkelte tilfelle.",
                            English to "The provisions pertaining to accumulated pension rights differ in Chapters 19 and 20 in the National Insurance Act. In general, the period of national insurance coverage is the periods in which you have been a member of the Norwegian National Insurance Scheme between the ages of 16 and 67. In addition, you accumulate national insurance coverage by earning pension points, until the year you turn 75, pursuant to Chapter 19. Consequently, national insurance coverage pursuant to Chapter 19 may, in some cases, be higher than years pursuant to Chapter 20.",
                        )
                    }
                }
                //norskTTAP2011Botid_001
                showIf(alderspensjonVedVirk.erEksportberegnet and regelverkstype.isOneOf(AP2011)) {
                    paragraph {
                        text(
                            Bokmal to "Har du vært medlem i folketrygden i mindre enn 20 år skal trygdetiden etter kapittel 19 fastsettes til antall år med pensjonspoeng. Du har mindre enn 20 års medlemstid og trygdetiden din etter kapittel 19 er derfor fastsatt til antall år med pensjonspoeng.",
                            Nynorsk to "Har du vore medlem i folketrygda i mindre enn 20 år skal trygdetida etter kapittel 19 fastsetjast til antal år med pensjonspoeng. Du har mindre enn 20 års medlemstid og trygdetida di etter kapitel 19 er difor fastsett til antal år med pensjonspoeng.",
                            English to "If you have been a member of the Norwegian National Insurance Scheme for less than 20 years in total, your national insurance coverage pursuant to chapter 19 will be the same as number of years you have accumulated pensionable earnings. You have been a member for less than 20 years in total and your period of national insurance coverage pursuant to chapter 19 is the same as number of years you have accumulated pensionable earnings.",
                        )
                    }
                }

                //norskTTAP2016Eksport_001
                showIf(inngangOgEksportVurdering.eksportBeregnetUtenGarantipensjon_safe.ifNull(false)) {
                    paragraph {
                        text(
                            Bokmal to "Hvis du har mindre enn 20 års medlemstid, har du ikke rett på garantipensjon når du er bosatt i utlandet.",
                            Nynorsk to "Dersom du har mindre enn 20 års medlemstid, har du ikkje rett på garantipensjon når du bur i utlandet.",
                            English to "If you have been a member of the Norwegian National Insurance Scheme for less than 20 years in total, you are not eligible for any guaranteed pension when you live abroad.",
                        )
                    }
                }

                showIf(trygdetidNorge.size().greaterThan(0)) {
                    paragraph {
                        text(
                            Bokmal to "Tabellen nedenfor viser perioder vi har registrert at du har bodd og/eller arbeidet i Norge. Disse opplysningene er brukt for å fastsette din norske trygdetid.",
                            Nynorsk to "Tabellen nedanfor viser periodar vi har registrert at du har budd og/eller arbeidd i Noreg. Desse opplysningane er brukte for å fastsetje den norske trygdetida di.",
                            English to "The table below shows the time periods when you have been registered as living and/or working in Norway. This information has been used to establish your Norwegian national insurance coverage.",
                        )
                    }
                    includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetid(trygdetidNorge))
                }
            }


            showIf(
                trygdetidEOS.size().greaterThan(0)
                        and (trygdetidsdetaljerKap19VedVirk.beregningsmetode.equalTo(EOS)
                        or trygdetidsdetaljerKap20VedVirk.beregningsmetode_safe.equalTo(EOS))
            ) {
                paragraph {
                    text(
                        Bokmal to "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i øvrige EØS-land. Disse periodene er brukt i vurderingen av retten til alderspensjon før fylte 67 år.",
                        Nynorsk to "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i øvrige EØS-land. Desse periodane er brukt i vurderinga av retten til alderspensjon før fylte 67 år.",
                        English to "The table below shows your National Insurance coverage in other EEC-countries. These periods have been used to assess whether you are eligible for retirement pension before the age of 67.",
                    )
                }

                includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.UtenlandskTrygdetid(trygdetidEOS))
            }

            showIf(
                trygdetidsdetaljerKap19VedVirk.beregningsmetode.isNotAnyOf(EOS, FOLKETRYGD, NORDISK)
                and trygdetidsdetaljerKap20VedVirk.beregningsmetode_safe.ifNull(EOS).isNotAnyOf(EOS, FOLKETRYGD, NORDISK)
            ) {
                paragraph {
                    text(
                        Bokmal to "Trygdetiden din i avtaleland er fastsatt på grunnlag av følgende perioder:",
                        Nynorsk to "Trygdetida di i avtaleland er fastsett på grunnlag av følgjande periodar:",
                        English to "Your period of national insurance coverage in a signatory country is based on the following periods:",
                    )
                }
                includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.UtenlandskTrygdetid(trygdetidAvtaleland))
            }


        }
    )