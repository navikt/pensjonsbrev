package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.borSammenMedBruker
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.harInntektOver2G
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.mottarOmstillingsstonad
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.mottarPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.epsVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.garantitillegg_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.kravAarsak
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.endringIEPSInntekt
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.institusjonsopphold
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.samboer15
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// MF_000102 med krav.arsak = ALDERSOVERGANG
// Brevet gjelder for AP2016/AP2025 brukere når garantitillegg er innvilget etter kapittel 20 i ny alderspensjon

@TemplateModelHelpers
object EndringAvAlderspensjonSivilstand : RedigerbarTemplate<EndringAvAlderspensjonSivilstandDto> {
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_AV_ALDERSPENSJON_SIVILSTAND
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndringAvAlderspensjonSivilstandDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Endring av alderspensjon (sivilstand)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val kravVirkDatoFom = pesysData.kravVirkDatoFom
        val garantitillegg = pesysData.garantitillegg_safe
        val regelverkType = pesysData.regelverkType
        val kravArsakType = pesysData.kravAarsak
        val brukersSivilstand = pesysData.brukersSivilstand // trenger bestemtform og ubestemtform
        val harInntektOver2G = pesysData.epsVedVirk.harInntektOver2G
        val mottarPensjon = pesysData.epsVedVirk.mottarPensjon
        val borSammenMedBruker = pesysData.epsVedVirk.borSammenMedBruker
        val mottarOmstillingsstonad = pesysData.epsVedVirk.mottarOmstillingsstonad



        title {
            showIf(kravArsakType.isNotAnyOf(KravArsakType.ALDERSOVERGANG)) {
                textExpr(
                    Bokmal to "Vi har beregnet alderspensjon din på nytt fra ".expr() + kravVirkDatoFom.format(),
                    Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ".expr() + kravVirkDatoFom.format(),
                    English to "We have recalculated your retirement pension from ".expr() + kravVirkDatoFom.format()
                )
            }.orShow {
                textExpr(
                    Bokmal to "Du har fått innvilget garantitillegg fra ".expr() + kravVirkDatoFom.format(),
                    Nynorsk to "Du har fått innvilga garantitillegg frå ".expr() + kravVirkDatoFom.format(),
                    English to "You have been granted a guarantee supplement for accumulated pension capital rights from ".expr() + kravVirkDatoFom.format()
                )
            }
        }
        outline {
            showIf(kravArsakType.isOneOf(KravArsakType.SIVILSTANDSENDRING)) {
                showIf(brukersSivilstand.isOneOf(MetaforceSivilstand.GIFT) and borSammenMedBruker) {
                    paragraph {
                        val navn = fritekst("navn")
                        textExpr(
                            Bokmal to "Du har giftet deg med ".expr() + navn + ",",
                            Nynorsk to "Du har gifta deg med ".expr() + navn + ",",
                            English to "You have married ".expr() + navn + ","
                        )
                        showIf(not(harInntektOver2G) and not(mottarPensjon)) {
                            text(
                                Bokmal to " som har en inntekt mindre enn to ganger grunnbeløpet.",
                                Nynorsk to " som har ei inntekt mindre enn to gonger grunnbeløpet.",
                                English to " who has an income that is less than twice the national insurance basic amount.",
                            )
                        }.orShowIf(harInntektOver2G and not(mottarPensjon)) {
                            text(
                                Bokmal to " som har en inntekt større enn to ganger grunnbeløpet.",
                                Nynorsk to " som har ei inntekt større enn to gonger grunnbeløpet.",
                                English to " who has an annual income that exceeds twice the national insurance basic amount."
                            )
                        }.orShowIf(harInntektOver2G and mottarPensjon) {
                            text(
                                Bokmal to " som mottar pensjon, uføretrygd eller avtalefestet pensjon.",
                                Nynorsk to " som får eigen pensjon, uføretrygd eller avtalefesta pensjon.",
                                English to " who receives a pension, disability benefit or contractual early retirement pension (AFP)."
                            )
                        }
                    }
                }.orShowIf(brukersSivilstand.isOneOf(MetaforceSivilstand.SAMBOER_3_2)) {
                    paragraph {
                        val navn = fritekst("navn")
                        textExpr(
                            Bokmal to "Du har bodd sammen med ".expr() + navn + " i 12 av de siste 18 månedene.",
                            Nynorsk to "Du har budd saman med ".expr() + navn + " i 12 av dei siste 18 månadene.",
                            English to "You have been living with ".expr() + navn + " for 12 out of the past 18 months."
                        )
                    }
                    // Radio knapper: Velg type § 1-5 samboer
                }.orShowIf(saksbehandlerValg.samboer15.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                    paragraph {
                        val navn = fritekst("navn")
                        val textValgNB = fritekst("barn sammen/vært gift tidligere")
                        val textValgNN = fritekst("barn saman/vore gift tidlegare")
                        val textValgEN = fritekst("with whom you have children/to whom you were previously married")
                        textExpr(
                            Bokmal to "Du har flyttet sammen med ".expr() + navn + ", og dere har ".expr() + textValgNB + ".",
                            Nynorsk to "Du har flytta saman med ".expr() + navn + ", og dere har ".expr() + textValgNN + ".",
                            English to "You have moved together with ".expr() + navn + ", ".expr() + textValgEN + ".",
                        )
                    }
                }

                showIf(
                    brukersSivilstand.isOneOf(
                        MetaforceSivilstand.SAMBOER_3_2,
                        MetaforceSivilstand.SAMBOER_1_5
                    ) and not(mottarOmstillingsstonad)
                ) {
                    showIf(not(harInntektOver2G) and not(mottarPensjon)) {
                        paragraph {
                            text(
                                Bokmal to "Samboeren din har en inntekt " + ifElse(
                                    harInntektOver2G,
                                    ifTrue = "mindre",
                                    ifFalse = "større"
                                ) + " enn to ganger grunnbeløpet.",
                                Nynorsk to "Sambuaren din har ei inntekt " + ifElse(
                                    harInntektOver2G,
                                    ifTrue = "mindre",
                                    ifFalse = "større"
                                ) + " enn to gonger grunnbeløpet.",
                                English to "Your cohabitant has an income that " + ifElse(
                                    harInntektOver2G,
                                    ifTrue = " is less than",
                                    ifFalse = "exceeds"
                                ) + " twice the national insurance basic amount.",
                            )
                        }
                    }.orShowIf(mottarPensjon) {
                        paragraph {
                            text(
                                Bokmal to "Samboeren din mottar pensjon, uføretrygd eller avtalefestet pensjon.",
                                Nynorsk to "Sambuaren din får pensjon, uføretrygd eller avtalefesta pensjon.",
                                English to "Your cohabitant receives a pension, disability benefit or contractual early retirement pension (AFP)."
                            )
                        }
                    }
                }.orShowIf(mottarOmstillingsstonad) {
                    paragraph {
                        text(
                            Bokmal to "Samboeren din mottar omstillingsstønad.",
                            Nynorsk to "Sambuaren din mottek omstillingsstønad.",
                            English to "Your cohabitant receives adjustment allowance."
                        )
                    }
                }
            }
            // Radio knapper: Velg endring i EPS inntekt
            showIf(saksbehandlerValg.endringIEPSInntekt.isOneOf(KravArsakType.EPS_ENDRET_INNTEKT)) {
                paragraph {
                    // brukersSivilstand = bestemtform liten bokstav
                    val epsInntektsendringNB = fritekst("økt/redusert")
                    val epsInntektsendringNN = fritekst("auka/redusert")
                    val epsInntektsendringEN = fritekst("increased/been reduced")
                    textExpr(
                        Bokmal to "Inntekten til ".expr() + brukersSivilstand + " din er ".expr() + epsInntektsendringNB + ".",
                        Nynorsk to "Inntekta til ".expr() + brukersSivilstand + " din er ".expr() + epsInntektsendringNN + ".",
                        English to "Your ".expr() + brukersSivilstand + "'s income has ".expr() + epsInntektsendringEN + ".",
                    )
                }
            }

            showIf(
                kravArsakType.isOneOf(KravArsakType.EPS_NY_YTELSE, KravArsakType.EPS_NY_YTELSE_UT) and not(
                    mottarOmstillingsstonad
                )
            ) {
                // brukersSivilstand = bestemtform stor bokstav, og liten bokstav
                paragraph {
                    textExpr(
                        Bokmal to brukersSivilstand + " din har fått innvilget egen pensjon eller uføretrygd.",
                        Nynorsk to brukersSivilstand + " din har fått innvilga eigen pensjon eller eiga uføretrygd.",
                        English to "Your ".expr() + brukersSivilstand + " has been granted a pension or disability benefit."
                    )
                }
            }.orShowIf(kravArsakType.isOneOf(KravArsakType.TILSTOT_ENDR_YTELSE)) {
                // brukersSivilstand = bestemtform liten bokstav
                paragraph {
                    textExpr(
                        Bokmal to "Pensjonen eller uføretrygden til ".expr() + brukersSivilstand + " din er endret.",
                        Nynorsk to "Pensjonen eller uføretrygda til ".expr() + brukersSivilstand + " din er endra.",
                        English to "Your ".expr() + brukersSivilstand + "'s pension or disability benefit been changed."
                    )
                }
            }.orShowIf(
                kravArsakType.isOneOf(KravArsakType.EPS_OPPH_YTELSE_UT, KravArsakType.TILSTOT_OPPHORT)
                        and not(harInntektOver2G)
            )
            // brukersSivilstand = bestemtform stor bokstav og liten bokstav
            {
                paragraph {
                    textExpr(
                        Bokmal to brukersSivilstand + " din mottar ikke lenger egen pensjon eller uføretrygd.",
                        Nynorsk to brukersSivilstand + " din får ikkje lenger eigen pensjon eller eiga uføretrygd.",
                        English to "Your ".expr() + brukersSivilstand + " no longer receives a pension or disability benefit."
                    )
                }
            }.orShowIf(
                kravArsakType.isOneOf(
                    KravArsakType.EPS_OPPH_YTELSE_UT,
                    KravArsakType.TILSTOT_OPPHORT
                ) and harInntektOver2G
            ) {
                // brukersSivilstand = bestemtform stor bokstav og liten bokstav
                paragraph {
                    textExpr(
                        Bokmal to brukersSivilstand + " din mottat  ikke lenger egen pensjon eller uføretrygd, men har fortsatt en inntekt større enn to ganger grunnbeløpet.",
                        Nynorsk to brukersSivilstand + " din får ikkje lenger eigen pensjon eller eiga uføretrygd, men har framleis ei inntekt som er større enn to gonger grunnbeløpet.",
                        English to "Your ".expr() + brukersSivilstand + " no longer receives a pension or disability benefit, but still has an annual income that exceeds twice the national insurance basic amount."
                    )
                }
            }

            showIf(kravArsakType.isOneOf(KravArsakType.SIVILSTANDSENDRING) and not(borSammenMedBruker)) {
                paragraph {
                       val navn = fritekst("navn")
                    textExpr(
                        Bokmal to "Du og ".expr() + navn + " bor ikke lenger sammen.",
                        Nynorsk to "Du og ".expr() + navn + "bur ikkje lenger saman.",
                        English to "You and ".expr() + navn + "no longer live together.",
                    )
                    text(
                        Bokmal to "Du har giftet deg. Ifølge folkeregisteret er du og ektefellen din registrert bosatt på ulike adresser.",
                        Nynorsk to "Du har gifta deg. Ifølgje folkeregisteret er du og ektefellen din registrert busette på ulike adresser.",
                        English to "You have gotten married. According to the national registry you and your spouse are listed at different residential addresses."
                    )
                }
            }

            showIf(saksbehandlerValg.institusjonsopphold.isOneOf(KravArsakType.INSTOPPHOLD)) {
                paragraph {
                    val hvemPaaInstitusjonNB = fritekst("Du/Ektefellen/Partneren/Samboeren/Begge")
                    val hvemPaaInstitusjonNN = fritekst("Du/Ektefellen/Partnaren/Sambuaren/Begge")
                    val hvemPaaInstitusjonEN = fritekst("You/Your spouse/partner/cohabitant have/has")
                    val typeInstitusjonNB = fritekst("sykehjem/institusjon")
                    val typeInstitusjonNN = fritekst("sjukeheim/institusjon ")
                    val typeInstitusjonNEN = fritekst("a nursing home/an institution")
                    textExpr(
                        Bokmal to hvemPaaInstitusjonNB + " din har flyttet på ".expr() + typeInstitusjonNB + ".",
                        Nynorsk to hvemPaaInstitusjonNN + " din har flytta på ".expr() + typeInstitusjonNN + ".",
                        English to hvemPaaInstitusjonEN + " moved into ".expr() + typeInstitusjonNEN + ".",
                    )
                }
            }


            paragraph {
                text(
                    Bokmal to "Garantitillegget skal sikre at du får en alderspensjon som tilsvarer den pensjonen du hadde tjent opp før pensjonsreformen i 2010.",
                    Nynorsk to "Garantitillegget skal sikre at du får ein alderspensjon ved 67 år som svarer til den pensjonen du hadde tent opp før pensjonsreforma i 2010.",
                    English to "The guarantee supplement for accumulated pension capital rights is to ensure that you receive a retirement pension at age 67 that corresponds " +
                            "to the pension you had earned before the pension reform in 2010.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Tillegget utbetales sammen med alderspensjonen og kan tidligst utbetales fra måneden etter du fyller 67 år.",
                    Nynorsk to "Tillegget blir betalt ut samen med alderspensjonen og kan tidlegast betalast ut frå månaden etter du fyller 67 år.",
                    English to "The supplement will be paid in addition to your retirement pension and can at the earliest be paid from the month after you turn 67 years of age.",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Garantitillegget utgjør ".expr() + garantitillegg.format() + " kroner per måned før skatt fra ".expr() + kravVirkDatoFom.format(),
                    Nynorsk to "Garantitillegget utgjer ".expr() + garantitillegg.format() + " kroner per månad før skatt frå ".expr() + kravVirkDatoFom.format(),
                    English to "Your monthly guarantee supplement for accumulated pension capital rights will be NOK "
                        .expr() + garantitillegg.format() + " before tax from ".expr() + kravVirkDatoFom.format()
                )
            }

            includePhrase(Utbetalingsinformasjon)

            showIf(regelverkType.isNotAnyOf(AlderspensjonRegelverkType.AP2025)) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven § 20-20.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova § 20-20.",
                        English to "This decision was made pursuant to the provisions of § 20-20 of the National Insurance Act."
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-9, 20-17 femte avsnitt og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-9, 20-17 femte avsnitt og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 20-9, 20-17 fifth paragraph, and 22-12 of the National Insurance Act."
                    )
                }
            }
        }
    }
}