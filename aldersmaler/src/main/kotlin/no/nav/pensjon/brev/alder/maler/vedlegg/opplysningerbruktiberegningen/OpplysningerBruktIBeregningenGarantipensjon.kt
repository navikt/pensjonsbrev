package no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen

import no.nav.pensjon.brev.alder.maler.felles.GarantipensjonSatsTypeText
import no.nav.pensjon.brev.alder.model.MetaforceSivilstand.*
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025Dto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.brukersSivilstand
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.delingstallLevealder
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.redusertTrygdetid
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.beholdningForForsteUttak
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.EpsVedVirkSelectors.borSammenMedBruker
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.EpsVedVirkSelectors.borSammenMedBruker_safe
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.EpsVedVirkSelectors.harInntektOver2G
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.EpsVedVirkSelectors.mottarPensjon
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.beholdningForForsteUttak
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.delingstalletVed67Ar
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.garantipensjonSatsPerAr
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.nettoUtbetaltPerManed
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.satsType
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

data class OpplysningerBruktIBeregningenGarantipensjon(
    val garantipensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.GarantipensjonVedVirk?>,
    val alderspensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.AlderspensjonVedVirk>,
    val vilkarsVedtak: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.VilkaarsVedtak>,
    val beregningKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.BeregningKap20VedVirk>,
    val beregnetPensjonPerManedVedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.BeregnetPensjonPerManedVedVirk>,
    val epsVedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.EpsVedVirk?>,
    val erBeregnetSomEnsligPgaInstitusjonsopphold: Expression<Boolean>,
    val trygdetidsdetaljerKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderAP2025Dto.TrygdetidsdetaljerKap20VedVirk>,
) : OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val garantipensjonInnvilget = alderspensjonVedVirk.garantipensjonInnvilget

        ifNotNull(garantipensjonVedVirk) { garantipensjonVedVirk ->
            showIf(
                garantipensjonInnvilget
                        and garantipensjonVedVirk.nettoUtbetaltPerManed.greaterThan(0)
            ) {
                title1 {
                    text(
                        bokmal { + "Garantipensjon" },
                        nynorsk { + "Garantipensjon" },
                        english { + "Guaranteed pension" },
                    )
                }
                showIf(alderspensjonVedVirk.uttaksgrad.equalTo(100)) {
                    //vedleggBeregnGarantipensjonUtbetaltFullUttak_001
                    paragraph {
                        text(
                            bokmal { + "Din årlige garantipensjon blir beregnet ved å dele garantipensjonsbeholdningen din på delingstallet ved uttak. Garantipensjonsbeholdningen din er på " +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() + ", og delingstallet ved uttak er " +
                                    beregningKap20VedVirk.delingstallLevealder.format() + "." },

                            nynorsk { + "Den årlege garantipensjonen din blir rekna ut ved å dele garantipensjonsbeholdninga di på delingstalet ved uttak. Garantipensjonsbeholdninga di er på " +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() + ", og delingstalet ved uttak er " +
                                    beregningKap20VedVirk.delingstallLevealder.format() + "." },

                            english { + "Your annual guaranteed pension is calculated by dividing your guaranteed pension capital by the life expectancy divisor at the time of the initial withdrawal. Your guaranteed pension capital is " +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() + ", and the divisor at withdrawal is " +
                                    beregningKap20VedVirk.delingstallLevealder.format() + "." },
                        )
                    }
                }.orShow {
                    //vedleggBeregnGarantipensjonUtbetaltGradertUttak_001
                    paragraph {
                        text(
                            bokmal { + "Din årlige garantipensjon blir beregnet ved å dele garantipensjonsbeholdningen din på delingstallet ved uttak. Garantipensjonsbeholdningen din er på " +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() +
                                    ", og delingstallet ved uttak er " + beregningKap20VedVirk.delingstallLevealder.format() +
                                    ". Siden du ikke tar ut full pensjon, vil du kun få utbetalt " +
                                    alderspensjonVedVirk.uttaksgrad.format() + " prosent av dette beløpet." },

                            nynorsk { + "Den årlege garantipensjonen din blir rekna ut ved å dele garantipensjonsbeholdninga di på delingstalet ved uttak. Garantipensjonsbeholdninga di er på " +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() +
                                    ", og delingstalet ved uttak er " + beregningKap20VedVirk.delingstallLevealder.format() +
                                    ". Sidan du ikkje tek ut full pensjon, vil du berre få utbetalt " +
                                    alderspensjonVedVirk.uttaksgrad.format() + " prosent av dette beløpet." },

                            english { + "Your annual guaranteed pension is calculated by dividing your guaranteed pension capital by the life expectancy divisor at the time of the initial withdrawal. Your guaranteed pension capital is " +
                                    garantipensjonVedVirk.beholdningForForsteUttak.format() +
                                    ", and the life expectancy divisor at withdrawal is " + beregningKap20VedVirk.delingstallLevealder.format() +
                                    ". Since you are not taking out full pension, you will only receive " +
                                    alderspensjonVedVirk.uttaksgrad.format() + " percent of this amount." },
                        )
                    }
                }

                //vedleggBeregnGarantipensjonsbeholdningInnledning_001
                title1 {
                    text(
                        bokmal { + "Garantipensjonsbeholdningen din er beregnet slik:" },
                        nynorsk { + "Garantipensjonsbehaldninga di er rekna ut slik:" },
                        english { + "Your guaranteed pension capital is calculated as follows:" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Garantipensjonen skal sikre deg et visst minstenivå på pensjonen din. Garantipensjonen reduseres med inntektspensjonen. Størrelsen på garantipensjonen fastsettes ut fra en sats som er avhengig av din sivilstand. Satsen blir redusert hvis trygdetiden din er under 40 år." },
                        nynorsk { + "Garantipensjonen skal sikre deg eit visst minstenivå på pensjonen din. Garantipensjonen blir redusert med inntektspensjonen. Storleiken på garantipensjonen blir fastsett ut frå ein sats som er avhengig av sivilstanden din. Satsen blir redusert dersom trygdetida di er under 40 år." },
                        english { + "The guaranteed pension ensures a certain minimum level for your pension. The guaranteed pension is reduced by the income pension. The size of the guaranteed pension is based on a rate that depends on your marital status. The rate is reduced if your national insurance coverage period is less than 40 years." },
                    )
                }
                val brukersSivilstand = beregnetPensjonPerManedVedVirk.brukersSivilstand
                showIf(brukersSivilstand.isOneOf(GIFT, PARTNER)) {
                    val erGift = beregnetPensjonPerManedVedVirk.brukersSivilstand.equalTo(GIFT)
                    paragraph {
                        showIf(erGift) {
                            //vedleggBeregnGift_001
                            text(
                                bokmal { + "Vi har lagt til grunn at du er gift." },
                                nynorsk { + "Vi har lagt til grunn at du er gift." },
                                english { + "We have registered that you have a spouse." },
                            )
                        }.orShow {
                            //vedleggBeregnPartner_001
                            text(
                                bokmal { + "Vi har lagt til grunn at du er partner." },
                                nynorsk { + "Vi har lagt til grunn at du er partnar." },
                                english { + "We have registered that you have a partner." },
                            )
                        }
                    }

                    ifNotNull(epsVedVirk) { epsVedVirk ->
                        paragraph {
                            showIf(erBeregnetSomEnsligPgaInstitusjonsopphold) {
                                showIf(erGift) {
                                    //vedleggBeregnGiftLeverAdskiltInstitusjon_001
                                    text(
                                        bokmal { + "Du og ektefellen din er registrert med forskjellig bosted da en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor " },
                                        nynorsk { + "Du og ektefellen din er registrerte med forskjellig bustad da ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg. Satsen vi brukar er difor " },
                                        english { + "You and your spouse are registered with different residences as one of you is residing in an institution. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the " },
                                    )
                                }.orShow {
                                    //vedleggBeregnPartnerLeverAdskiltInstituton_001
                                    text(
                                        bokmal { + "Du og partneren din er registrert med forskjellig bosted da en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor " },
                                        nynorsk { + "Du og partnaren din er registrerte med forskjellig bustad da en dere bor på institusjon. Pensjonen din er derfor berekna som om du var einsleg. Satsen vi bruker er difor " },
                                        english { + "You and your partner are registered with different residences since one of you resides in an institution. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the " },
                                    )
                                }
                            }.orShowIf(not(epsVedVirk.borSammenMedBruker)) {
                                showIf(erGift) {
                                    //vedleggBeregnGiftLeverAdskilt_002
                                    text(
                                        bokmal { + "Du og ektefellen din er registrert med forskjellig bosted. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor " },
                                        nynorsk { + "Du og ektefellen din er registrerte med forskjellig bustad. Pensjonen din er derfor berekna som om du var einsleg. Satsen vi brukar er difor " },
                                        english { + "You and your spouse are registered with different residences. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the " },
                                    )
                                }.orShow {
                                    //vedleggBeregnPartnerLeverAdskilt_002
                                    text(
                                        bokmal { + "Du og partneren din er registrert med forskjellig bosted. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor " },
                                        nynorsk { + "Du og partnaren din er registrerte med forskjellig bustad. Pensjonen di er difor rekna som om du var einsleg. Satsen vi brukar er difor " },
                                        english { + "You and your partner are registered with different residences. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the " },
                                    )
                                }
                            }.orShowIf(epsVedVirk.mottarPensjon) {
                                showIf(erGift) {
                                    //vedleggBeregnEktefellePensjon_002
                                    text(
                                        bokmal { + "Vi har registrert at ektefellen din mottar uføretrygd, alderspensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for. Satsen vi bruker er derfor " },
                                        nynorsk { + "Vi har registrert at ektefellen din får uføretrygd, alderspensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for. Satsen vi brukar er difor " },
                                        english { + "We have registered that your spouse is receiving disability benefit, a national insurance retirement pension or contractual early retirement pension (AFP) which earns pension points. The rate we use is therefore the " },
                                    )
                                }.orShow {
                                    //vedleggBeregnPartnerPensjon_002
                                    text(
                                        bokmal { + "Vi har registrert at partneren din mottar uføretrygd, alderspensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for. Satsen vi bruker er derfor " },
                                        nynorsk { + "Vi har registrert at partnaren din får uføretrygd, alderspensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for. Satsen vi brukar er difor " },
                                        english { + "We have registered that your partner is receiving disability benefit, a national insurance retirement pension or contractual early retirement pension (AFP) which earns pension points. The rate we use is therefore the " },
                                    )
                                }
                            }.orShowIf(not(epsVedVirk.mottarPensjon)) {
                                showIf(epsVedVirk.harInntektOver2G) {
                                    showIf(erGift) {
                                        //vedleggBeregnEktefelleOver2G_002
                                        text(
                                            bokmal { + "Vi har registrert at ektefellen din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor " },
                                            nynorsk { + "Vi har registrert at ektefellen din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi brukar er difor " },
                                            english { + "We have registered that your spouse has an annual income that exceeds twice the national insurance basic amount (G). The rate we use is therefore the " },
                                        )
                                    }.orShow {
                                        text(
                                            bokmal { + "Vi har registrert at partneren din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor " },
                                            nynorsk { + "Vi har registrert at partnaren din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi brukar er difor " },
                                            english { + "We have registered that your partner has an annual income that exceeds twice the national insurance basic amount (G). The rate we use is therefore the " },
                                        )
                                    }
                                }.orShow {
                                    showIf(erGift) {
                                        text(
                                            bokmal { + "Vi har registrert at ektefellen din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor " },
                                            nynorsk { + "Vi har registrert at ektefellen din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi brukar er difor " },
                                            english { + "We have registered that your spouse has an annual income lower than twice the national insurance basic amount (G). The rate we use is therefore the " },
                                        )
                                    }.orShow {
                                        text(
                                            bokmal { + "Vi har registrert at partneren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor " },
                                            nynorsk { + "Vi har registrert at partnaren din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi brukar er difor " },
                                            english { + "We have registered that your partner has an annual income lower than twice the national insurance basic amount (G). The rate we use is therefore the " },
                                        )
                                    }
                                }
                            }

                            includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                            text(bokmal { + "." }, nynorsk { + "." }, english { + "." })
                        }
                    }
                }.orShowIf(brukersSivilstand.isOneOf(GLAD_EKT, SEPARERT)) {
                    paragraph {
                        //vedleggBeregnGift_001
                        text(
                            bokmal { + "Vi har lagt til grunn at du er gift." },
                            nynorsk { + "Vi har lagt til grunn at du er gift." },
                            english { + "We have registered that you have a spouse." },
                        )
                    }
                    showIf(erBeregnetSomEnsligPgaInstitusjonsopphold) {
                        paragraph {                        //vedleggGiftLeverAdskilt&&Institusjonsopphold_002
                            text(
                                bokmal { + "Du og ektefellen din er registrert med forskjellig bosted da en av dere bor på institusjon. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor " },
                                nynorsk { + "Du og ektefellen din er registrerte med forskjellig bustad da ein av dykk bur på institusjon. Pensjonen din er derfor berekna som om du var einsleg. Satsen vi brukar er difor " },
                                english { + "You and your spouse are registered with different residences as one of you is residing in an institution. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the " },
                            )
                            includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                            text(bokmal { + "." }, nynorsk { + "." }, english { + "." })
                        }
                    }.orShowIf(not(epsVedVirk.borSammenMedBruker_safe.ifNull(false))) {
                        //vedleggBeregnGiftLeverAdskilt_002
                        paragraph {
                            text(
                                bokmal { + "Du og ektefellen din er registrert med forskjellig bosted. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor " },
                                nynorsk { + "Du og ektefellen din er registrerte med forskjellig bustad. Pensjonen din er derfor berekna som om du var einsleg. Satsen vi brukar er difor " },
                                english { + "You and your spouse are registered with different residences. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the " },
                            )
                            includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                            text(bokmal { + "." }, nynorsk { + "." }, english { + "." })
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
                    paragraph {
                        //vedleggBeregnPartnerLeverAdskilt_002
                        text(
                            bokmal { + "Du og partneren din er registrert med forskjellig bosted. Pensjonen din er derfor beregnet som om du var enslig. Satsen vi bruker er derfor " },
                            nynorsk { + "Du og partnaren din er registrerte med forskjellig bustad. Pensjonen di er difor rekna som om du var einsleg. Satsen vi brukar er difor " },
                            english { + "You and your partner are registered with different residences. Therefore, your pension has been calculated as if you were single. The rate we use is therefore the " },
                        )
                        includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                        text(bokmal { + "." }, nynorsk { + "." }, english { + "." })
                    }
                }.orShowIf(brukersSivilstand.isOneOf(SAMBOER_3_2, SAMBOER_1_5)) {
                    showIf(brukersSivilstand.equalTo(SAMBOER_3_2)) {
                        //vedleggBeregnSambo§20-9_001
                        paragraph {
                            text(
                                bokmal { + "Vi har lagt til grunn at du er samboer (jf. folketrygdloven § 20-9)." },
                                nynorsk { + "Vi har lagt til grunn at du er sambuar (jf. folketrygdlova § 20-9)." },
                                english { + "We have registered that you have a cohabitant (cf. § 20-9 of the National Insurance Act)." },
                            )
                        }
                    }.orShow {
                        //vedleggBeregnSambo§1-5_001
                        paragraph {
                            text(
                                bokmal { + "Vi har lagt til grunn at du er samboer (jf. folketrygdloven § 1-5)." },
                                nynorsk { + "Vi har lagt til grunn at du er sambuar (jf. folketrygdlova § 1-5)." },
                                english { + "We have registered that you have a cohabitant (cf. § 1-5 of the National Insurance Act)." },
                            )
                        }
                    }
                    ifNotNull(epsVedVirk) { epsVedVirk ->
                        showIf(
                            epsVedVirk.borSammenMedBruker
                                    and epsVedVirk.mottarPensjon
                                    and not(erBeregnetSomEnsligPgaInstitusjonsopphold)
                        ) {
                            //vedleggBeregnSamboPensjon_001
                            paragraph {
                                text(
                                    bokmal { + "Vi har registrert at samboeren din mottar uføretrygd, pensjon fra folketrygden eller AFP som det godskrives pensjonspoeng for." },
                                    nynorsk { + "Vi har registrert at sambuaren din får uføretrygd, pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for." },
                                    english { + "We have registered that your cohabitant is receiving disability benefit, a national insurance pension or contractual early retirement pension (AFP) which earns pension points." },
                                )
                            }
                        }
                        showIf(
                            epsVedVirk.borSammenMedBruker
                                    and not(epsVedVirk.mottarPensjon)
                                    and not(erBeregnetSomEnsligPgaInstitusjonsopphold)
                        ) {
                            showIf(epsVedVirk.harInntektOver2G) {
                                //vedleggBeregnSamboOver2G_002
                                paragraph {
                                    text(
                                        bokmal { + "Vi har registrert at samboeren din har en inntekt som er høyere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor ordinær sats." },
                                        nynorsk { + "Vi har registrert at sambuaren din har ei inntekt som er høgare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi bruker er difor ordinær sats." },
                                        english { + "We have registered that your cohabitant has an annual income that exceeds twice the national insurance basic amount (G). The rate we use is therefore the ordinary rate." },
                                    )
                                }
                            }.orShow {
                                //vedleggBeregnSamboUnder2G_002
                                paragraph {
                                    text(
                                        bokmal { + "Vi har registrert at samboeren din har en inntekt som er lavere enn to ganger folketrygdens grunnbeløp (G). Satsen vi bruker er derfor høy sats." },
                                        nynorsk { + "Vi har registrert at sambuaren din har ei inntekt som er lågare enn to gonger grunnbeløpet i folketrygda (G). Satsen vi bruker er difor høg sats." },
                                        english { + "We have registered that your cohabitant has an annual income lower than twice the national insurance basic amount (G). The rate we use is therefore the high rate." },
                                    )
                                }
                            }
                        }
                    }
                }.orShowIf(brukersSivilstand.isOneOf(ENSLIG, ENKE, UKJENT)) {
                    paragraph {
                        //vedleggBeregnEnslig_002
                        text(
                            bokmal { + "Vi har lagt til grunn at du er enslig. Satsen vi bruker er derfor " },
                            nynorsk { + "Vi har lagt til grunn at du er einsleg. Satsen vi brukar er difor " },
                            english { + "We have registered that you are single. The rate we use is therefore the " },
                        )
                        includePhrase(GarantipensjonSatsTypeText(garantipensjonVedVirk.satsType))
                        text(bokmal { + "." }, nynorsk { + "." }, english { + "." })
                    }
                }
                showIf(beregningKap20VedVirk.redusertTrygdetid) {
                    //vedleggBeregnSatsRedusertTT_001
                    paragraph {
                        text(
                            bokmal { + "Satsen er redusert fordi trygdetiden din er under 40 år." },
                            nynorsk { + "Satsen er redusert fordi trygdetida di er under 40 år." },
                            english { + "The rate is reduced because your national insurance coverage is less than 40 years." },
                        )
                    }
                }
                paragraph {
                    //vedleggBeregnGarantipensjonDelingstall67_001
                    text(
                        bokmal { + "Vi bruker delingstallet fastsatt for ditt årskull ved 67 år for å regne om denne satsen til en beholdningsstørrelse. Vi trekker deretter fra 80 prosent av pensjonsbeholdningen din ved uttak fra dette beløpet. Summen utgjør da garantipensjonsbeholdningen ved uttak." },
                        nynorsk { + "Vi brukar delingstalet fastsett for årskullet ditt ved 67 år for å rekne om denne satsen til ein behaldningsstorleik. Vi trekkjer deretter frå 80 prosent av pensjonsbehaldninga di ved uttak frå dette beløpet. Summen utgjer då garantipensjonsbehaldninga ved uttak." },
                        english { + "We use the life expectancy divisor set for your cohort at 67 years to convert this rate into a capital balance. We then deduct 80 percent of your accumulated pension capital at withdrawal from this amount. The sum then constitutes the guaranteed pension capital at the time of the initial withdrawal." },
                    )
                }
                showIf(beregningKap20VedVirk.redusertTrygdetid) {
                    paragraph {
                        //vedleggBeregnGarantipensjonsbeholdningRedusertTT_001
                        text(
                            bokmal { + "Sats for garantipensjon x (trygdetid / 40 år full trygdetid) x delingstall ved 67 år - (80% av pensjonsbeholdning ved uttak) = garantipensjonsbeholdning" },
                            nynorsk { + "Sats for garantipensjon x (trygdetid / 40 år full trygdetid) x delingstal ved 67 år - (80% av pensjonsbehaldning ved uttak) = garantipensjonsbehaldning" },
                            english { + "Guaranteed pension rate x (NI coverage / 40 years full NI coverage) x life expectancy adjustment divisor at 67 years - (80% of accumulated pension capital before initial withdrawal) = guaranteed pension capital" },
                        )
                    }
                    //vedleggBeregnGarantipensjonsbeholdningRedusertTT_002
                    paragraph {
                        val norskText = garantipensjonVedVirk.garantipensjonSatsPerAr.format(false) +
                                " kr x (" + trygdetidsdetaljerKap20VedVirk.anvendtTT.format() +
                                " / 40) x " + garantipensjonVedVirk.delingstalletVed67Ar.format() +
                                " - (80% (" + beregningKap20VedVirk.beholdningForForsteUttak.format(false) +
                                " kr)) = " + garantipensjonVedVirk.beholdningForForsteUttak.format(false) + " kr"
                        text(
                            bokmal { + norskText },
                            nynorsk { + norskText },
                            english { + "NOK " + garantipensjonVedVirk.garantipensjonSatsPerAr.format(false) +
                                    " x (" + trygdetidsdetaljerKap20VedVirk.anvendtTT.format() +
                                    " / 40) x " + garantipensjonVedVirk.delingstalletVed67Ar.format() +
                                    " - (80% (NOK " + beregningKap20VedVirk.beholdningForForsteUttak.format(false)
                                + ")) = NOK " + garantipensjonVedVirk.beholdningForForsteUttak.format(false) },
                        )
                    }
                }.orShow {
                    paragraph {
                        //vedleggBeregnGarantipensjonsbeholdningIkkeRedusertTT_001
                        text(
                            bokmal { + "Sats for garantipensjon x delingstall ved 67 år - (80% av pensjonsbeholdning ved uttak) = garantipensjonsbeholdning:" },
                            nynorsk { + "Sats for garantipensjon x delingstal ved 67 år - (80% av pensjonsbehaldning ved uttak) = garantipensjonsbehaldning:" },
                            english { + "Guaranteed pension rate x life expectancy adjustment divisor at 67 years - (80% of accumulated pension capital before initial withdrawal) = guaranteed pension capital:" },
                        )
                    }
                    paragraph {
                        //vedleggBeregnGarantipensjonsbeholdningIkkeRedusertTT_002
                        val norskText = garantipensjonVedVirk.garantipensjonSatsPerAr.format(false) +
                                " kr x " + garantipensjonVedVirk.delingstalletVed67Ar.format() +
                                " - (80% (" + beregningKap20VedVirk.beholdningForForsteUttak.format(false) +
                                " kr)) = " + garantipensjonVedVirk.beholdningForForsteUttak.format(false) + " kr"
                        text(
                            bokmal { + norskText }, nynorsk { + norskText },

                            english { + "NOK " + garantipensjonVedVirk.garantipensjonSatsPerAr.format(false) +
                                    " x " + garantipensjonVedVirk.delingstalletVed67Ar.format() +
                                    " - (80% (NOK " + beregningKap20VedVirk.beholdningForForsteUttak.format(false) +
                                    ")) =  NOK " + garantipensjonVedVirk.beholdningForForsteUttak.format(false) },
                        )
                    }
                }
            }
        }

    }

}