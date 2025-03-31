package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype
import no.nav.pensjon.brev.api.model.Beregningsmetode.*
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.flyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonPerManedSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.andelKap19_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.andelKap20_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.forholdstallLevealder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.poengAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.poengArTeller
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.poengAre91
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.poengArf92
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.poengarNevner
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.redusertTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.sluttpoengtall
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.redusertTrygdetid_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BrukerSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.harInntektOver2G_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.EPSvedVirkSelectors.mottarPensjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.aldersEllerSykehjem_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.ensligPgaInst_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.epsPaInstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.fengsel_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InstitusjonsoppholdVedVirkSelectors.helseinstitusjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.nevnerProRata
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.tellerProRata
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.tellerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregningKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.beregningKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.epsVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.institusjonsoppholdVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.tilleggspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.trygdetidsdetaljerKap19VedVirk
import no.nav.pensjon.brev.maler.fraser.common.AntallAarText
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
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
            title1 {
            }

            paragraph {
                val harTilleggspensjon = tilleggspensjonVedVirk.notNull()
                table(
                    header = {
                        column(columnSpan = 4) {
                            textExpr(
                                Bokmal to "Opplysninger brukt i beregningen per ".expr() + beregnetPensjonPerManedVedVirk.virkDatoFom.format(),
                                Nynorsk to "Opplysningar brukte i berekninga frå ".expr() + beregnetPensjonPerManedVedVirk.virkDatoFom.format(),
                                English to "Information used to calculate as of ".expr() + beregnetPensjonPerManedVedVirk.virkDatoFom.format(),
                            )
                        }
                        column(alignment = RIGHT) { }
                    }
                ) {

                    showIf(
                        beregnetPensjonPerManedVedVirk.flyktningstatusErBrukt
                                and not(beregningKap19VedVirk.redusertTrygdetid)
                                and not(beregningKap20VedVirk.redusertTrygdetid_safe.ifNull(false))
                    ) {
                        //tabellFlyktningstatus_002
                        row {
                            cell {
                                text(
                                    Bokmal to "Du er innvilget flyktningstatus fra UDI",
                                    Nynorsk to "Du er innvilga flyktningstatus frå UDI",
                                    English to "You are registered with the status of a refugee granted by the UDI",
                                )
                            }
                            cell { text(Bokmal to "Ja", Nynorsk to "Ja", English to "Yes") }
                        }
                    }

                    val beregningsmetodeKap19 = trygdetidsdetaljerKap19VedVirk.beregningsmetode
                    showIf(beregningsmetodeKap19.isOneOf(FOLKETRYGD, NORDISK)) {
                        //tabellTT_002
                        row {
                            cell {
                                text(
                                    Bokmal to "Trygdetid",
                                    Nynorsk to "Trygdetid",
                                    English to "National insurance coverage",
                                )
                            }
                            cell { includePhrase(AntallAarText(trygdetidsdetaljerKap19VedVirk.anvendtTT)) }
                        }

                        showIf(tilleggspensjonVedVirk.notNull()) {
                            //vedleggTabellKap19Sluttpoengtall_001
                            ifNotNull(beregningKap19VedVirk.sluttpoengtall) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Sluttpoengtall",
                                            Nynorsk to "Sluttpoengtall",
                                            English to "Final pension point score",
                                        )
                                    }
                                    cell { eval(it.formatTwoDecimals()) }
                                }
                            }

                            //vedleggTabellKap19PoengAr_001
                            showIf(beregningsmetodeKap19.isOneOf(FOLKETRYGD)) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Antall poengår",
                                            Nynorsk to "Talet på poengår",
                                            English to "Number of pension point earning years",
                                        )
                                    }
                                    cell { includePhrase(AntallAarText(beregningKap19VedVirk.poengAar)) }
                                }
                            }

                            //vedleggTabellKap19PoengArf92_001
                            ifNotNull(beregningKap19VedVirk.poengArf92) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Antall år med pensjonsprosent 45",
                                            Nynorsk to "Talet på år med pensjonsprosent 45",
                                            English to "Number of years calculated with pension percentage 45",
                                        )
                                    }
                                    cell {
                                        includePhrase(AntallAarText(it))
                                    }
                                }
                            }

                            ifNotNull(beregningKap19VedVirk.poengAre91) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Antall år med pensjonsprosent 42",
                                            Nynorsk to "Talet på år med pensjonsprosent 42",
                                            English to "Number of years calculated with pension percentage 42",
                                        )
                                    }
                                    cell {
                                        includePhrase(AntallAarText(it))
                                    }
                                }
                            }
                        }
                    }.orShowIf(beregningsmetodeKap19.isOneOf(EOS)) {
                        //tabellTTNorgeEOS_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet trygdetid i Norge og andre EØS-land",
                                    Nynorsk to "Samla trygdetid i Noreg og andre EØS-land",
                                    English to "Total national insurance coverage in Norway and other EEA countries",
                                )
                            }
                            cell { includePhrase(AntallAarText(trygdetidsdetaljerKap19VedVirk.anvendtTT)) }
                        }

                        //tabellFaktiskTTBrokNorgeEOS_001
                        ifNotNull(
                            trygdetidsdetaljerKap19VedVirk.nevnerTTEOS,
                            trygdetidsdetaljerKap19VedVirk.tellerTTEOS
                        ) { teller, nevner ->

                            row {
                                cell {
                                    text(
                                        Bokmal to "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og andre EØS-land",
                                        Nynorsk to "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og andre EØS-land",
                                        English to "The ratio between national insurance coverage in Norway and total insurance coverage in all EEA countries",
                                    )
                                }
                                cell { eval(teller.format() + "/" + nevner.format()) }
                            }
                        }

                        //vedleggTabellKap19SluttpoengtallEOS_001
                        showIf(harTilleggspensjon) {
                            ifNotNull(beregningKap19VedVirk.sluttpoengtall) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Sluttpoengtall (EØS)",
                                            Nynorsk to "Sluttpoengtal (EØS)",
                                            English to "Final pension point score (EEA)",
                                        )
                                    }
                                    cell { eval(it.formatTwoDecimals()) }
                                }
                            }
                        }

                        //vedleggTabellKap19PoengArf92EOS_001
                        ifNotNull(beregningKap19VedVirk.poengArf92) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Antall år med pensjonsprosent 45 (EØS)",
                                        Nynorsk to "Talet på år med pensjonsprosent 45 (EØS)",
                                        English to "Number of years calculated with pension percentage 45 (EEA)",
                                    )
                                }
                                cell { includePhrase(AntallAarText(it))}
                            }
                        }

                        //vedleggTabellKap19PoengAre91EOS_001
                        ifNotNull(beregningKap19VedVirk.poengAre91) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Antall år med pensjonsprosent 42 (EØS)",
                                        Nynorsk to "Talet på år med pensjonsprosent 42 (EØS)",
                                        English to "Number of years calculated with pension percentage 42 (EEA)",
                                    )
                                }
                                cell { includePhrase(AntallAarText(it))}
                            }
                        }

                        //tabellPoengArBrokNorgeEOS_001
                        ifNotNull(beregningKap19VedVirk.poengArTeller, beregningKap19VedVirk.poengarNevner) {
                            teller, nevner ->
                            row {
                                cell {
                                    text(
                                        Bokmal to "Forholdet mellom antall poengår i Norge og antall poengår i Norge og annet EØS-land",
                                        Nynorsk to "Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og anna EØS-land",
                                        English to "The ratio between point earning years in Norway and total point earning years in all EEA countries",
                                    )
                                }
                                cell { eval(teller.format() + "/" + nevner.format()) }
                            }
                        }
                    }.orShow {
                        row {
                            cell {
                                text(
                                    Bokmal to "Samlet trygdetid i Norge og avtaleland",
                                    Nynorsk to "Samla trygdetid i Noreg og avtaleland",
                                    English to "Total period of national insurance coverage in Norway and countries with social security agreement",
                                )
                            }
                            cell {
                                includePhrase(AntallAarText(trygdetidsdetaljerKap19VedVirk.anvendtTT))
                            }
                        }

                        ifNotNull(trygdetidsdetaljerKap19VedVirk.tellerProRata, trygdetidsdetaljerKap19VedVirk.nevnerProRata) {
                                teller, nevner ->
                            row {
                                cell {
                                    text(
                                        Bokmal to "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og avtaleland",
                                        Nynorsk to "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og avtaleland",
                                        English to "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and countries with social security agreement",
                                    )
                                }
                                cell { eval(teller.format() + "/" + nevner.format()) }
                            }
                        }

                        showIf(harTilleggspensjon) {
                            //vedleggTabellKap19Sluttpoengtall Avtaleland_001
                            ifNotNull(beregningKap19VedVirk.sluttpoengtall) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Sluttpoengtall (avtaleland)",
                                            Nynorsk to "Sluttpoengtal (avtaleland)",
                                            English to "Final pension point score (Norway and countries with social security agreement)",
                                        )
                                    }
                                    cell {
                                        eval(it.formatTwoDecimals())
                                    }
                                }
                            }

                            //vedleggTabellKap19PoengArf92Avtaleland_001
                            ifNotNull(beregningKap19VedVirk.poengArf92) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Antall år med pensjonsprosent 45 (Norge og avtaleland)",
                                            Nynorsk to "Talet på år med pensjonsprosent 45 (Noreg og avtaleland) ",
                                            English to "Number of years calculated with pension percentage 45 (Norway and countries with social security agreement)",
                                        )
                                    }
                                    cell {
                                        includePhrase(AntallAarText(it))
                                    }
                                }
                            }

                            //vedleggTabellKap19PoengAre91Avtaleland_001
                            ifNotNull(beregningKap19VedVirk.poengAre91) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Antall år med pensjonsprosent 42 (Norge og avtaleland)",
                                            Nynorsk to "Talet på år med pensjonsprosent 42 (Noreg og avtaleland)",
                                            English to "Number of years calculated with pension percentage 42 (Norway and countries with social security agreement)",
                                        )
                                    }
                                    cell {
                                        includePhrase(AntallAarText(it))
                                    }
                                }
                            }

                            //tabellPoengArBrokNorgeAvtaleland_001
                            ifNotNull(beregningKap19VedVirk.poengArTeller, beregningKap19VedVirk.poengarNevner) {
                                    teller, nevner ->
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Forholdet mellom antall poengår i Norge og antall poengår i Norge og avtaleland",
                                            Nynorsk to "Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og avtaleland ",
                                            English to "Ratio between the number of point earning years in Norway and the number of point earning years in Norway and countries with social security agreement",
                                        )
                                    }
                                    cell { eval(teller.format() + "/" + nevner.format()) }
                                }
                            }
                        }
                    }

                    //vedleggTabellKap19Forholdstall_001
                    showIf(beregningKap19VedVirk.forholdstallLevealder.greaterThan(0.0)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Forholdstall ved levealdersjustering",
                                    Nynorsk to "Forholdstal ved levealdersjustering",
                                    English to "Ratio for life expectancy adjustment",
                                )
                            }
                            cell { eval (beregningKap19VedVirk.forholdstallLevealder.formatThreeDecimals())}
                        }
                    }
                }
            }


        }
    )