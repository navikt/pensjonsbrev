package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaPensjonsistParInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.saertilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.borSammenMedBruker
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.harInntektOver2G
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.mottarOmstillingsstonad
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.mottarPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.epsVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.garantitillegg_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.grunnpensjon_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.kravAarsak
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.saerskiltSatsErBrukt
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.ufoereKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.vedtakEtterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.aarligKontrollEPS
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.endringIEPSInntekt
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.endringPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.feilutbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.forsoergerEPSOver60AarBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.forsoergerEPSOver60AarIkkeBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.ingenBetydning
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.institusjonsopphold
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.pensjonenOeker
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.pensjonenRedusert
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.samboer15
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.sivilstandsendringAarsak
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.*
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dateFormatter
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

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
        val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
        val garantitillegg = pesysData.garantitillegg_safe.ifNull(then = Kroner(0))
        val regelverkType = pesysData.regelverkType
        val kravArsakType = pesysData.kravAarsak
        val brukersSivilstand = pesysData.brukersSivilstand
        val brukersSivilstandBestemt = pesysData.brukersSivilstand.bestemtForm(storBokstav = true)
        val harInntektOver2G = pesysData.epsVedVirk.harInntektOver2G
        val mottarPensjon = pesysData.epsVedVirk.mottarPensjon
        val borSammenMedBruker = pesysData.epsVedVirk.borSammenMedBruker
        val mottarOmstillingsstonad = pesysData.epsVedVirk.mottarOmstillingsstonad
        val grunnpensjon = pesysData.grunnpensjon_safe.ifNull(then = Kroner(0))
        val garantipensjonInnvilget = pesysData.alderspensjonVedVirk.garantipensjonInnvilget
        val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
        val minstenivaaIndividuellInnvilget = pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget
        val minstenivaaPensjonistParInnvilget = pesysData.alderspensjonVedVirk.minstenivaaPensjonsistParInnvilget
        val saertilleggInnvilget = pesysData.alderspensjonVedVirk.saertilleggInnvilget
        val saerskiltSatsErBrukt = pesysData.saerskiltSatsErBrukt
        val uforeKombinertMedAlder = pesysData.ufoereKombinertMedAlder
        val totalPensjon = pesysData.totalPensjon.format()
        val vedtakEtterbetaling = pesysData.vedtakEtterbetaling
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad.ifNull(then = (0))



        title {
            showIf(kravArsakType.isNotAnyOf(KravArsakType.ALDERSOVERGANG)) {
                // nyBeregningAPTittel
                textExpr(
                    Bokmal to "Vi har beregnet alderspensjon din på nytt fra ".expr() + kravVirkDatoFom,
                    Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ".expr() + kravVirkDatoFom,
                    English to "We have recalculated your retirement pension from ".expr() + kravVirkDatoFom
                )
            }.orShow {
                // innvilgetGarantitilleggTittel
                textExpr(
                    Bokmal to "Du har fått innvilget garantitillegg fra ".expr() + kravVirkDatoFom,
                    Nynorsk to "Du har fått innvilga garantitillegg frå ".expr() + kravVirkDatoFom,
                    English to "You have been granted a guarantee supplement for accumulated pension capital rights from ".expr() + kravVirkDatoFom
                )
            }
        }
        outline {
            showIf(kravArsakType.isOneOf(KravArsakType.SIVILSTANDSENDRING)) {
                showIf(brukersSivilstand.isOneOf(MetaforceSivilstand.GIFT) and borSammenMedBruker) {
                    // endringSivilstandGiftUnder2G, endringSisvilstandGiftOver2G, endringSisvilstandGiftYtelse
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
                    // endringSisvilstand3-2samboer
                    paragraph {
                        val navn = fritekst("navn")
                        textExpr(
                            Bokmal to "Du har bodd sammen med ".expr() + navn + " i 12 av de siste 18 månedene.",
                            Nynorsk to "Du har budd saman med ".expr() + navn + " i 12 av dei siste 18 månadene.",
                            English to "You have been living with ".expr() + navn + " for 12 out of the past 18 months."
                        )
                    }
                }.orShowIf(saksbehandlerValg.samboer15.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                    // Radio knapper: Velg type § 1-5 samboer
                    // endringSivilstand1-5samboerBarn, endringSivilstand1-5samboerTidlGift
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

                // samboerInntektUnder2G, samboerInntektOver2G, samboerYtelse, samboerOmstillingstonad
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
            // endringInntektOktEPS, endringInntektRedusertEPS
            showIf(saksbehandlerValg.endringIEPSInntekt.isOneOf(KravArsakType.EPS_ENDRET_INNTEKT)) {
                paragraph {
                    val epsInntektsendringNB = fritekst("økt/redusert")
                    val epsInntektsendringNN = fritekst("auka/redusert")
                    val epsInntektsendringEN = fritekst("increased/been reduced")
                    textExpr(
                        Bokmal to "Inntekten til ".expr()
                                + brukersSivilstandBestemt + " din er ".expr() + epsInntektsendringNB + ".",
                        Nynorsk to "Inntekta til ".expr() + brukersSivilstandBestemt + " din er ".expr() + epsInntektsendringNN + ".",
                        English to "Your ".expr() + brukersSivilstandBestemt + "'s income has ".expr() + epsInntektsendringEN + ".",
                    )
                }
            }

            showIf(
                kravArsakType.isOneOf(KravArsakType.EPS_NY_YTELSE, KravArsakType.EPS_NY_YTELSE_UT) and not(
                    mottarOmstillingsstonad
                )
            ) {
                // innvilgetYtelseEPS
                paragraph {
                    textExpr(
                        Bokmal to brukersSivilstandBestemt + " din har fått innvilget egen pensjon eller uføretrygd.",
                        Nynorsk to brukersSivilstandBestemt + " din har fått innvilga eigen pensjon eller eiga uføretrygd.",
                        English to "Your ".expr() + brukersSivilstandBestemt + " has been granted a pension or disability benefit."
                    )
                }
            }.orShowIf(kravArsakType.isOneOf(KravArsakType.TILSTOT_ENDR_YTELSE)) {
                // endringYtelseEPS
                paragraph {
                    textExpr(
                        Bokmal to "Pensjonen eller uføretrygden til ".expr() + brukersSivilstandBestemt + " din er endret.",
                        Nynorsk to "Pensjonen eller uføretrygda til ".expr() + brukersSivilstandBestemt + " din er endra.",
                        English to "Your ".expr() + brukersSivilstandBestemt + "'s pension or disability benefit been changed."
                    )
                }
            }.orShowIf(
                kravArsakType.isOneOf(KravArsakType.EPS_OPPH_YTELSE_UT, KravArsakType.TILSTOT_OPPHORT)
                        and not(harInntektOver2G)
            )
            {
                // opphorYtelseEPS
                paragraph {
                    textExpr(
                        Bokmal to brukersSivilstandBestemt + " din mottar ikke lenger egen pensjon eller uføretrygd.",
                        Nynorsk to brukersSivilstandBestemt + " din får ikkje lenger eigen pensjon eller eiga uføretrygd.",
                        English to "Your ".expr() + brukersSivilstandBestemt + " no longer receives a pension or disability benefit."
                    )
                }
            }.orShowIf(
                kravArsakType.isOneOf(
                    KravArsakType.EPS_OPPH_YTELSE_UT,
                    KravArsakType.TILSTOT_OPPHORT
                ) and not(mottarOmstillingsstonad)
            ) {
                // opphorOmstillingSambo
                paragraph {
                    text(
                        Bokmal to "Samboeren din mottar ikke lenger omstillingsstønad.",
                        Nynorsk to "Sambuaren din mottek ikkje lenger omstillingsstønad.",
                        English to "Your cohabitant does not receive adjustment allowance."
                    )
                }
            }.orShowIf(
                kravArsakType.isOneOf(
                    KravArsakType.EPS_OPPH_YTELSE_UT,
                    KravArsakType.TILSTOT_OPPHORT
                ) and harInntektOver2G
            ) {
                // opphorYtelseEPSOver2G
                paragraph {
                    textExpr(
                        Bokmal to brukersSivilstandBestemt + " din mottat  ikke lenger egen pensjon eller uføretrygd, men har fortsatt en inntekt større enn to ganger grunnbeløpet.",
                        Nynorsk to brukersSivilstandBestemt + " din får ikkje lenger eigen pensjon eller eiga uføretrygd, men har framleis ei inntekt som er større enn to gonger grunnbeløpet.",
                        English to "Your ".expr() + brukersSivilstandBestemt + " no longer receives a pension or disability benefit, but still has an annual income that exceeds twice the national insurance basic amount."
                    )
                }
            }

            // Radioknapper: Hva er årsaken til sivilstandsendringen?
            showIf(kravArsakType.isOneOf(KravArsakType.SIVILSTANDSENDRING) and not(borSammenMedBruker)) {
                showIf(saksbehandlerValg.sivilstandsendringAarsak) {
                    // flyttetEPS, endringSivilstandGiftBorIkkeSammen
                    paragraph {
                        val navn = fritekst("navn")
                        textExpr(
                            Bokmal to "Du og ".expr() + navn + " bor ikke lenger sammen. // Du har giftet deg. Ifølge folkeregisteret er du og ektefellen din registrert bosatt på ulike adresser.",
                            Nynorsk to "Du og ".expr() + navn + "bur ikkje lenger saman. // Du har gifta deg. Ifølgje folkeregisteret er du og ektefellen din registrert busette på ulike adresser.",
                            English to "You and ".expr() + navn + "no longer live together. // You have gotten married. According to the national registry you and your spouse are listed at different residential addresses."
                        )
                    }
                }
            }

            // Radioknapper: Alders- og sykehjem eller EPS på annen institusjon
            // endringSykehjem, endringSykehjemEPS, endringSykkehjemBegge, endringInstitusjonEPS
            showIf(kravArsakType.isOneOf(KravArsakType.INSTOPPHOLD)) {
                showIf(saksbehandlerValg.institusjonsopphold) {
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
                            English to hvemPaaInstitusjonEN + " moved into ".expr() + typeInstitusjonNEN + "."
                        )
                    }
                }
            }

            showIf(
                kravArsakType.isNotAnyOf(
                    KravArsakType.ALDERSOVERGANG,
                    KravArsakType.VURDER_SERSKILT_SATS
                ) and regelverkType.isNotAnyOf(AlderspensjonRegelverkType.AP2025)
            ) {
                showIf(grunnpensjon.greaterThan(0)) {
                    showIf(
                        not(saertilleggInnvilget) and (minstenivaaPensjonistParInnvilget or minstenivaaIndividuellInnvilget) and not(
                            pensjonstilleggInnvilget
                        )
                    ) {
                        showIf(not(garantipensjonInnvilget)) {
                            // omregningGP_MNT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen og minstenivåtillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen og minstenivåtillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension and minimum pension supplement."
                                )
                            }
                        }.orShow {
                            // omregningGP_GarantiPen_MNT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen og garantipensjonen din på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen og garantipensjonen din på nytt.",
                                    English to "We have therefore recalculated your basic pension and guaranteed pension."
                                )
                            }
                        }
                    }
                    showIf(not(saertilleggInnvilget) and (minstenivaaPensjonistParInnvilget or minstenivaaIndividuellInnvilget) and pensjonstilleggInnvilget) {
                        // omregningGP_PenT_MNT
                        showIf(not(garantipensjonInnvilget)) {
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen, pensjonstillegget og minstenivåtillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen, pensjonstillegget og minstenivåtillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension, supplementary pension and minimum pension supplement."
                                )
                            }
                        }.orShow {
                            // omregningGP_PenT_Garanti_MNT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen, pensjonstillegget, garantipensjonen og minstenivåtillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen, pensjonstillegget, garantipensjonen og minstenivåtillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension, supplementary pension, guaranteed pension and minimum pension supplement."
                                )
                            }
                        }
                    }
                    showIf(
                        not(saertilleggInnvilget) and not(minstenivaaPensjonistParInnvilget) and not(
                            minstenivaaIndividuellInnvilget
                        ) and not(pensjonstilleggInnvilget)
                    ) {
                        showIf(not(garantipensjonInnvilget)) {
                            // omregningGP
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen din på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen din på nytt.",
                                    English to "Derfor har vi berekna grunnpensjonen din på nytt."
                                )
                            }
                        }.orShow {
                            // omregningGP_GarantiPen
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen og garantipensjonen din på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen og garantipensjonen din på nytt.",
                                    English to "We have therefore recalculated your basic pension and guaranteed pension."
                                )
                            }
                        }
                    }
                    showIf(
                        not(saertilleggInnvilget) and not(minstenivaaPensjonistParInnvilget) and not(
                            minstenivaaIndividuellInnvilget
                        ) and pensjonstilleggInnvilget
                    ) {
                        showIf(not(garantipensjonInnvilget)) {
                            // omregningGP_PenT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen og pensjonstillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen og pensjonstillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension and pension supplement."
                                )
                            }
                        }.orShow {
                            // omregningGP_PenT_GarantiPen_MNT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen, pensjonstillegget, garantipensjonen og minstenivåtillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen, pensjonstillegget, garantipensjonen og minstenivåtillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension, supplementary pension, guaranteed pension and minimum pension supplement."
                                )
                            }
                        }
                    }

                    showIf(
                        regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967) and saertilleggInnvilget
                    ) {
                        showIf(not(minstenivaaPensjonistParInnvilget) and not(minstenivaaPensjonistParInnvilget)) {
                            // omregningGPST
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen og særtillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen og særtillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension and the special supplement."
                                )
                            }
                        }.orShowIf((minstenivaaPensjonistParInnvilget or minstenivaaIndividuellInnvilget)) {
                            // omregningGPSTMNT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen, særtillegget og minstenivåtillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen, særtillegget og minstenivåtillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension, the special supplement and the minimum level supplement."
                                )
                            }
                        }
                    }
                }

                showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2025)) {
                    // omregning_GarantiPen
                    paragraph {
                        text(
                            Bokmal to "Derfor har vi vurdert garantipensjonen din på nytt.",
                            Nynorsk to "Derfor har vi vurdert garantipensjonen din på nytt.",
                            English to "We have therefore recalculated your guaranteed pension."
                        )
                    }
                }

                showIf(kravArsakType.isOneOf(KravArsakType.ALDERSOVERGANG)) {
                    // innvilgetGarantitilleggKap20
                    paragraph {
                        text(
                            Bokmal to "Garantitillegget skal sikre at du får en alderspensjon som tilsvarer den pensjonen du hadde tjent opp før pensjonsreformen i 2010.",
                            Nynorsk to
                                    "Garantitillegget skal sikre at du får ein alderspensjon ved 67 år som svarer til den pensjonen du hadde tent opp før pensjonsreforma i 2010.",
                            English to
                                    "The guarantee supplement for accumulated pension capital rights is to ensure that you receive a retirement pension at age 67 that corresponds to the pension you had earned before the pension reform in 2010."
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Tillegget utbetales sammen med alderspensjonen og kan tidligst utbetales fra måneden etter du fyller 67 år.",
                            Nynorsk to "Tillegget blir betalt ut samen med alderspensjonen og kan tidlegast betalast ut frå månaden etter du fyller 67 år.",
                            English to "The supplement will be paid in addition to your retirement pension and can at the earliest be paid from the month after you turn 67 years of age."
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Garantitillegget utgjør ".expr() + garantitillegg.format() + " kroner per måned før skatt fra ".expr() + kravVirkDatoFom + ".",
                            Nynorsk to "Garantitillegget utgjer ".expr() + garantitillegg.format() + " kroner per månad før skatt frå ".expr() + kravVirkDatoFom + ".",
                            English to "Your monthly guarantee supplement for accumulated pension capital rights will be NOK ".expr() +
                                    garantitillegg.format() + " before tax from ".expr() + kravVirkDatoFom + ".",
                        )
                    }
                }

                // Radioknapper: Forsørger EPS over 60 år. Særskilt sats for minste pensjonsnivå
                showIf(kravArsakType.isOneOf(KravArsakType.VURDER_SERSKILT_SATS)) {
                    showIf(saksbehandlerValg.forsoergerEPSOver60AarBruktIBeregningen) {
                        // SaerSatsBruktEpsUnder62
                        paragraph {
                            textExpr(
                                Bokmal to brukersSivilstandBestemt + " din du forsørger har en inntekt lavere enn grunnbeløpet ".expr() + grunnpensjon.format() + " kroner.",
                                Nynorsk to brukersSivilstandBestemt + " din du forsørgjer har ei inntekt lågare enn grunnbeløpet ".expr() + grunnpensjon.format() + " kroner.",
                                English to "Your ".expr() + brukersSivilstandBestemt + " you support has an income lower than the basic amount which is NOK ".expr() + grunnpensjon.format() + ".",
                            )
                        }
                        // SaerSatsBruktEpsIkkeRettTilAP
                        paragraph {
                            textExpr(
                                Bokmal to brukersSivilstandBestemt + " din som du forsørger har ikke rett til full alderspensjon fra folketrygden og har inntekt lavere enn grunnbeløpet ".expr() + grunnpensjon.format() + " kroner.",
                                Nynorsk to brukersSivilstandBestemt + " din som du forsørgjer har ikkje rett til full alderspensjon frå folketrygda og har inntekt lågare enn grunnbeløpet ".expr() + grunnpensjon.format() + " kroner.",
                                English to "Your ".expr() + brukersSivilstandBestemt + " you support does not have rights to full retirement pension through the National Insurance Act and has income lower than the basic amount which is NOK ".expr() + grunnpensjon.format() + "."
                            )
                        }
                        // SaerSatsBruktEpsGittAvkallAP
                        paragraph {
                            textExpr(
                                Bokmal to brukersSivilstandBestemt + " din har gitt avkall på sin alderspensjon fra folketrygden.",
                                Nynorsk to brukersSivilstandBestemt + " din har gitt avkall på alderspensjon sin frå folketrygda.",
                                English to brukersSivilstandBestemt + " has renounced their retirement pension through the National Insurance Act."
                            )
                        }
                        // SaerSatsBruktEpsGittAvkallUT
                        paragraph {
                            textExpr(
                                Bokmal to brukersSivilstandBestemt + " din har gitt avkall på sin uføretrygd fra folketrygden.",
                                Nynorsk to brukersSivilstandBestemt + " din har gitt avkall på uføretrygda si frå folketrygda.",
                                English to brukersSivilstandBestemt + " has renounced their disability benefits through the National Insurance Act."
                            )
                        }
                    }
                    showIf(saksbehandlerValg.forsoergerEPSOver60AarIkkeBruktIBeregningen) {
                        // SaerSatsIkkeBruktEpsInntektOver1G, SaerSatsIkkeBruktEpsRettTilFullAP, SaerSatsIkkeBruktEpsMottarAP, SaerSatsIkkeBruktEpsMottarAfp, SaerSatsIkkeBruktEpsMottarUT
                        paragraph {
                            textExpr(
                                Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din",
                                Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din",
                                English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + brukersSivilstandBestemt + " has"
                            )
                        }
                        paragraph {
                            textExpr(
                                Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din har inntekt høyere enn grunnbeløpet (".expr() + grunnpensjon.format() + " kroner).",
                                Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din har inntekt høgare enn grunnbeløpet (".expr() + grunnpensjon.format() + " kroner).",
                                English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + brukersSivilstandBestemt + " has a higher income than the basic amount which is NOK ".expr() + grunnpensjon.format() + "."
                            )
                        }
                        // SaerSatsIkkeBruktEpsRettTilFullAP
                        paragraph {
                            textExpr(
                                Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din har rett til full alderspensjon fra folketrygden.",
                                Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din har rett til full alderspensjon frå folketrygda.",
                                English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + brukersSivilstandBestemt + " has rights to full retirement pension through the National Insurance Act."
                            )
                        }
                        // SaerSatsIkkeBruktEpsMottarAP
                        paragraph {
                            textExpr(
                                Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din mottar alderspensjon fra folketrygden.",
                                Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din mottar alderspensjon frå folketrygda.",
                                English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + brukersSivilstandBestemt + " recieves retirement pension through the National Insurance Act."
                            )
                        }
                        // SaerSatsIkkeBruktEpsMottarAfp
                        paragraph {
                            textExpr(
                                Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din mottar AFP i statlig sektor.",
                                Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din mottar AFP i statleg sektor.",
                                English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + brukersSivilstandBestemt + " receives contractual retirement pension from the public sector."
                            )
                        }
                        // SaerSatsIkkeBruktEpsMottarUT
                        paragraph {
                            textExpr(
                                Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din mottar uføretrygd fra folketrygden.",
                                Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + brukersSivilstandBestemt + " din mottar uføretrygd frå folketrygda.",
                                English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + brukersSivilstandBestemt + " receives disability benefits through the National Insurance Act."
                            )
                        }
                    }


                    // omregningSaerskilSats_AP1967, _AP2011AP2016
                    showIf(saerskiltSatsErBrukt) {
                        paragraph {
                            text(
                                Bokmal to "Derfor har vi beregnet ",
                                Nynorsk to "Derfor har vi berekna ",
                                English to "We have therefore recalculated your "
                            )
                            showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967)) {
                                text(
                                    Bokmal to +ifElse(
                                        minstenivaaIndividuellInnvilget,
                                        ifTrue = "særtillegget og minstenivåtillegget",
                                        ifFalse = "særtillegget"
                                    ),
                                    Nynorsk to +ifElse(
                                        minstenivaaIndividuellInnvilget,
                                        ifTrue = "særtillegget og minstenivåtillegget",
                                        ifFalse = "særtillegget"
                                    ),
                                    English to +ifElse(
                                        minstenivaaIndividuellInnvilget,
                                        ifTrue = "special supplement and minimum pension supplement",
                                        ifFalse = "special supplement"
                                    ),
                                )
                            }.orShowIf(
                                regelverkType.isOneOf(
                                    AlderspensjonRegelverkType.AP2011,
                                    AlderspensjonRegelverkType.AP2016
                                )
                            ) {
                                text(
                                    Bokmal to +ifElse(
                                        minstenivaaIndividuellInnvilget,
                                        ifTrue = "pensjonstillegget og minstenivåtillegget",
                                        ifFalse = "pensjonstillegget"
                                    ),
                                    Nynorsk to +ifElse(
                                        minstenivaaIndividuellInnvilget,
                                        ifTrue = "pensjonstillegget og minstenivåtillegget",
                                        ifFalse = "pensjonstillegget"
                                    ),
                                    English to +ifElse(
                                        minstenivaaIndividuellInnvilget,
                                        ifTrue = "basic pension and minimum pension supplement",
                                        ifFalse = "basic pension"
                                    ),
                                )
                            }
                            text(
                                Bokmal to " ditt på nytt med særskilt sats.",
                                Nynorsk to " ditt på nytt med særskilt sats.",
                                English to " according to a special rate."
                            )
                        }
                    }

                    paragraph {
                        val fritekst = fritekst("Opplysninger/forhold du vil informere bruker om i saken.")
                        textExpr(
                            Bokmal to fritekst,
                            Nynorsk to fritekst,
                            English to fritekst,
                        )
                    }
                }
                showIf(
                    regelverkType.isOneOf(
                        AlderspensjonRegelverkType.AP2011,
                        AlderspensjonRegelverkType.AP2016
                    ) and saerskiltSatsErBrukt
                ) {
                    showIf(saksbehandlerValg.ingenBetydning) {
                        // ingenEndringBelop
                        paragraph {
                            text(
                                Bokmal to "Dette får ingen betydning for utbetalingen din.",
                                Nynorsk to "Dette får ingen følgjer for utbetalinga di.",
                                English to "This does not affect the amount you will receive."
                            )
                        }

                    }
                    showIf(saksbehandlerValg.pensjonenOeker) {
                        // nyBeregningAPØkning
                        paragraph {
                            text(
                                Bokmal to "Dette fører til at pensjonen din øker.",
                                Nynorsk to "Dette fører til at pensjonen din aukar.",
                                English to "This leads to an increase in your retirement pension."
                            )
                        }
                    }
                    showIf(saksbehandlerValg.pensjonenRedusert) {
                        // nyBeregningAPReduksjon
                        paragraph {
                            text(
                                Bokmal to "Dette fører til at pensjonen din blir redusert.",
                                Nynorsk to "Dette fører til at pensjonen din blir redusert.",
                                English to "This leads to a reduction in your retirement pension."
                            )
                        }
                    }
                }

                showIf(kravArsakType.isOneOf(KravArsakType.VURDER_SERSKILT_SATS)) {
                    showIf(saksbehandlerValg.aarligKontrollEPS) {
                        // SaerSatsInfoAarligKontrollEps
                        paragraph {
                            textExpr(
                                Bokmal to "Fram til ".expr() + brukersSivilstand + " din fyller 67 år, har vi en årlig kontroll om ".expr() +
                                        brukersSivilstand + " din har rett til full alderpensjon. Du får nytt vedtak hvis dette fører til at alderspensjonen din blir omregnet.",
                                Nynorsk to "Fram til ".expr() + brukersSivilstand + " din fyller 67 år, har vi ein årleg kontroll av ".expr() +
                                        brukersSivilstand + " si rett til full alderpensjon. Du får nytt vedtak hvis dette fører til at alderspensjonen din blir omrekna.",
                                English to "Until your ".expr() + brukersSivilstand + "  turns 67 years of age, we have an annual control of their rights to a full retirement pension. " +
                                        "You will receive a new decision if this results in your retirement pension being recalculated."
                            )
                        }
                    }
                }

                showIf(uforeKombinertMedAlder) {
                    // innvilgelseAPogUTInnledn
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + totalPensjon + " kroner hver måned før skatt fra ".expr() + kravVirkDatoFom + "." +
                                    " Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                            Nynorsk to "Du får ".expr() + totalPensjon + " kroner kvar månad før skatt frå ".expr() + kravVirkDatoFom + "." +
                                    " Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                            English to "You will receive NOK ".expr() + totalPensjon + " every month before tax from ".expr() + kravVirkDatoFom + "." +
                                    " You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit."
                        )
                    }
                }.orShow {
                    paragraph {
                        // innvilgelseAPInnledn
                        textExpr(
                            Bokmal to "Du får ".expr() + totalPensjon + " kroner hver måned før skatt fra ".expr() + kravVirkDatoFom + " i alderspensjon fra folketrygden.",
                            Nynorsk to "Du får ".expr() + totalPensjon + " kroner kvar månad før skatt frå ".expr() + kravVirkDatoFom + " i alderspensjon frå folketrygda.",
                            English to "You will receive NOK ".expr() + totalPensjon + " every month before tax from ".expr() + kravVirkDatoFom + " as retirement pension from the National Insurance Scheme."
                        )
                    }
                }

                includePhrase(Utbetalingsinformasjon)

                paragraph {
                    val fritekst = fritekst("Opplysninger/forhold du vil informere bruker om i saken.")
                    textExpr(
                        Bokmal to fritekst,
                        Nynorsk to fritekst,
                        English to fritekst,
                    )
                }

                showIf(
                    kravArsakType.isNotAnyOf(
                        KravArsakType.ALDERSOVERGANG,
                        KravArsakType.VURDER_SERSKILT_SATS
                    ) and regelverkType.isNotAnyOf(AlderspensjonRegelverkType.AP2025)
                ) {
                    // hjemmelSivilstandAlleRegelverkstyper
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ ",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ ",
                            English to "This decision was made pursuant to the provisions of §§ ",
                        )
                        showIf(brukersSivilstand.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                            text(
                                Bokmal to "1-5, ",
                                Nynorsk to "1-5, ",
                                English to "1-5, "
                            )
                        }
                        text(
                            Bokmal to "3-2",
                            Nynorsk to "3-2",
                            English to "3-2",
                        )
                        showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967) and saertilleggInnvilget) {
                            text(
                                Bokmal to ", 3-3, ",
                                Nynorsk to ", 3-3, ",
                                English to ", 3-3, ",
                            )
                        }
                        showIf(pensjonstilleggInnvilget or minstenivaaIndividuellInnvilget or minstenivaaPensjonistParInnvilget) {
                            text(
                                Bokmal to ", 19-8",
                                Nynorsk to ", 19-8",
                                English to ", 19-8",
                            )
                        }
                        showIf(pensjonstilleggInnvilget) {
                            text(
                                Bokmal to ", 19-9",
                                Nynorsk to ", 19-9",
                                English to ", 19-9",
                            )
                        }
                        showIf(garantipensjonInnvilget) {
                            text(
                                Bokmal to ", 20-9",
                                Nynorsk to ", 20-9",
                                English to ", 20-9",
                            )
                        }
                        text(
                            Bokmal to " og 22-12.",
                            Nynorsk to " og 22-12.",
                            English to " and 22-12.",
                        )
                    }

                    showIf(
                        kravArsakType.isOneOf(KravArsakType.ALDERSOVERGANG) and regelverkType.isNotAnyOf(
                            AlderspensjonRegelverkType.AP2025
                        )
                    ) {
                        // hjemmelGarantitillegg§20-20
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven § 20-20.",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 20-20.",
                                English to "This decision was made pursuant to the provisions of § 20-20 of the National Insurance Act.",
                            )
                        }
                    }
                }

                showIf(
                    kravArsakType.isOneOf(KravArsakType.VURDER_SERSKILT_SATS) and saerskiltSatsErBrukt
                ) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ ",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ ",
                            English to "This decision was made pursuant to the provisions of §§ ",
                        )
                        showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967)) {
                            showIf(brukersSivilstand.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                                text(
                                    Bokmal to "1-5, ",
                                    Nynorsk to "1-5, ",
                                    English to "1-5, "
                                )
                            }
                            text(
                                Bokmal to "3-2",
                                Nynorsk to "3-2",
                                English to "3-2"
                            )
                            showIf(saertilleggInnvilget) {
                                text(
                                    Bokmal to ", 3-3",
                                    Nynorsk to ", 3-3",
                                    English to ", 3-3"
                                )
                            }
                            text(
                                Bokmal to ", 19-8 og 22-12.",
                                Nynorsk to ", 19-8 og 22-12.",
                                English to ", 19-8 and 22-12 of the National Insurance Act."
                            )
                        }.orShowIf(
                            regelverkType.isOneOf(
                                AlderspensjonRegelverkType.AP2011,
                                AlderspensjonRegelverkType.AP2016
                            )
                        ) {
                            showIf(brukersSivilstand.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                                text(
                                    Bokmal to "1-5, ",
                                    Nynorsk to "1-5, ",
                                    English to "1-5, "
                                )
                            }
                        }.orShow {
                            text(
                                Bokmal to "19-8, 19-9 og 22-12.",
                                Nynorsk to "19-8, 19-9 og 22-12.",
                                English to "19-8, 19-9 and 22-12 of the National Insurance Act."
                            )
                        }
                    }
                }

                showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2025)) {
                    // hjemmelSivilstandAP2025
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-9, 20-17 femte avsnitt og 22-12.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-9, 20-17 femte avsnitt og 22-12.",
                            English to "This decision was made pursuant to the provisions of §§ 20-9, 20-17 fifth paragraph, and 22-12 of the National Insurance Act."
                        )
                    }
                }

                showIf(kravArsakType.isOneOf(KravArsakType.ALDERSOVERGANG)) {
                    // vedleggBeregnPensjonsOpptjeningOverskrift
                    title1 {
                        text(
                            Bokmal to "Pensjonsopptjeningen din",
                            Nynorsk to "Pensjonsoppteninga di",
                            English to "Your accumulated pension capital",
                        )
                    }
                    // vedleggBeregnPensjonsOpptjening
                    paragraph {
                        text(
                            Bokmal to "I nettjenesten Din pensjon på $DIN_PENSJON_URL kan du få oversikt over pensjonsopptjeningen din for hvert enkelt år. Der vil du kunne se hvilke andre typer pensjonsopptjening som er registrert på deg.",
                            Nynorsk to "I nettenesta Din pensjon på $DIN_PENSJON_URL kan du få oversikt over pensjonsoppteninga di for kvart enkelt år. Der kan du sjå kva andre typar pensjonsopptening som er registrert på deg.",
                            English to "Our online service 'Din pensjon' at $DIN_PENSJON_URL provides details on your accumulated rights for each year. Here you will be able to see your other types of pension rights we have registered."
                        )
                    }
                }

                // Hvis reduksjon tilbake i tid (Selectable) - feilutbetalingAP
                showIf(saksbehandlerValg.feilutbetaling) {
                    title1 {
                        text(
                            Bokmal to "Feilutbetaling",
                            Nynorsk to "Feilutbetaling",
                            English to "Incorrect payment"
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Vi har redusert pensjonen din tilbake i tid. Derfor har du fått for mye utbetalt. Vi vil sende deg et eget varselbrev om en eventuell tilbakebetaling.",
                            Nynorsk to "Vi har redusert pensjonen din tilbake i tid. Derfor har du fått for mykje utbetalt. Vi vil sende deg eit eige varselbrev om ei eventuell tilbakebetaling.",
                            English to "We have reduced your retirement pension for a previous period. You have therefore been paid too much. We will send you a separate notice letter concerning possible repayment.",
                        )
                    }
                }

                // Hvis endring i pensjonen (Selectable) - skattAPendring
                showIf(saksbehandlerValg.endringPensjon) {
                    title1 {
                        text(
                            Bokmal to "Endring av alderspensjon kan ha betydning for skatt",
                            Nynorsk to "Endring av alderspensjon kan ha betyding for skatt",
                            English to "The change in your retirement pension may affect how much tax you pay"
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endret. Dette kan du gjøre selv på $SKATTEETATEN_PENSJONIST_URL. Der får du også mer informasjon om skattekort for pensjonister.",
                            Nynorsk to "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endra. Dette kan du gjere sjølv på $SKATTEETATEN_PENSJONIST_URL. Der får du også meir informasjon om skattekort for pensjonistar.",
                            English to "When your retirement pension has been changed, you should check if your tax deduction card is correctly calculated. You can change your tax card by logging on to $SKATTEETATEN_PENSJONIST_URL. There you will find more information regarding tax deduction card for pensioners.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "På $DIN_PENSJON_URL får du vite hva du betaler i skatt. Her kan du også legge inn ekstra skattetrekk om du ønsker det. Dersom du endrer skattetrekket vil dette gjelde fra måneden etter at vi har fått beskjed.",
                            Nynorsk to "På $DIN_PENSJON_URL får du vite kva du betaler i skatt. Her kan du også leggje inn tilleggsskatt om du ønskjer det. Dersom du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed.",
                            English to "At $DIN_PENSJON_URL you can see how much tax you are paying. Here you can also add surtax, if you want. If you change your income tax rate, this will be applied from the month after we have been notified of the change.",
                        )
                    }
                }

                showIf(vedtakEtterbetaling) {
                    // etterbetalingAP_002
                    title1 {
                        text(
                            Bokmal to "Etterbetaling",
                            Nynorsk to "Etterbetaling",
                            English to "Retroactive payment",
                        )
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "Du får etterbetalt pensjon fra ".expr() + kravVirkDatoFom + ". Etterbetalingen vil vanligvis bli utbetalt i løpet av syv virkedager. Vi kan trekke fra skatt og ytelser du har fått fra for eksempel Nav eller tjenestepensjonsordninger. Derfor kan etterbetalingen din bli forsinket. Tjenestepensjonsordninger har ni ukers frist på å kreve trekk i etterbetalingen. Du kan sjekke eventuelle fradrag i utbetalingsmeldingen på $DITT_NAV.",
                            Nynorsk to "Du får etterbetalt pensjon frå ".expr() + kravVirkDatoFom + ". Etterbetalinga blir vanlegvis betalt ut i løpet av sju yrkedagar. Vi kan trekke frå skatt og ytingar du har fått frå for eksempel Nav eller tenestepensjonsordningar. Derfor kan etterbetalinga di bli forsinka. Tenestepensjonsordninga har ni veker frist på å krevje trekk i etterbetalinga. Du kan sjekke eventuelle frådrag i utbetalingsmeldinga på $DITT_NAV.",
                            English to "You will receive retroactive pension payments from ".expr() + kravVirkDatoFom + ". The retroactive payments will normally be made in the course of seven working days. We can make deductions for tax and benefits you have received, for example, from Nav or occupational pension schemes. Therefore, your retroactive payment may be delayed. Occupational pension schemes have a deadline of nine weeks to demand a deduction from the retroactive payments. You can check if there are any deductions from the payment notice at $DITT_NAV.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Hvis etterbetalingen gjelder tidligere år, trekker vi skatt etter skatteetatens standardsatser.",
                            Nynorsk to "Dersom etterbetalinga gjeld tidlegare år, vil vi trekkje skatt etter standardsatsane til skatteetaten.",
                            English to "If the retroactive payment refers to earlier years, we will deduct tax at the Tax Administration's standard rates.",
                        )
                    }
                }

                // Hvis etterbetaling (Selectable) - etterbetalingAP_002
                showIf(not(vedtakEtterbetaling)) {
                    showIf(saksbehandlerValg.etterbetaling) {
                        // etterbetalingAP_002
                        title1 {
                            text(
                                Bokmal to "Etterbetaling",
                                Nynorsk to "Etterbetaling",
                                English to "Retroactive payment",
                            )
                        }
                        paragraph {
                            textExpr(
                                Bokmal to "Du får etterbetalt pensjon fra ".expr() + kravVirkDatoFom + ". Etterbetalingen vil vanligvis bli utbetalt i løpet av syv virkedager. Vi kan trekke fra skatt og ytelser du har fått fra for eksempel Nav eller tjenestepensjonsordninger. Derfor kan etterbetalingen din bli forsinket. Tjenestepensjonsordninger har ni ukers frist på å kreve trekk i etterbetalingen. Du kan sjekke eventuelle fradrag i utbetalingsmeldingen på $DITT_NAV.",
                                Nynorsk to "Du får etterbetalt pensjon frå ".expr() + kravVirkDatoFom + ". Etterbetalinga blir vanlegvis betalt ut i løpet av sju yrkedagar. Vi kan trekke frå skatt og ytingar du har fått frå for eksempel Nav eller tenestepensjonsordningar. Derfor kan etterbetalinga di bli forsinka. Tenestepensjonsordninga har ni veker frist på å krevje trekk i etterbetalinga. Du kan sjekke eventuelle frådrag i utbetalingsmeldinga på $DITT_NAV.",
                                English to "You will receive retroactive pension payments from ".expr() + kravVirkDatoFom + ". The retroactive payments will normally be made in the course of seven working days. We can make deductions for tax and benefits you have received, for example, from Nav or occupational pension schemes. Therefore, your retroactive payment may be delayed. Occupational pension schemes have a deadline of nine weeks to demand a deduction from the retroactive payments. You can check if there are any deductions from the payment notice at $DITT_NAV.",
                            )
                        }
                        paragraph {
                            text(
                                Bokmal to "Hvis etterbetalingen gjelder tidligere år, trekker vi skatt etter skatteetatens standardsatser.",
                                Nynorsk to "Dersom etterbetalinga gjeld tidlegare år, vil vi trekkje skatt etter standardsatsane til skatteetaten.",
                                English to "If the retroactive payment refers to earlier years, we will deduct tax at the Tax Administration's standard rates.",
                            )
                        }
                    }
                }

                // Arbeidsinntekt og pensjon
                showIf(
                    regelverkType.isNotAnyOf(AlderspensjonRegelverkType.AP1967) and kravArsakType.isNotAnyOf(
                        KravArsakType.INSTOPPHOLD
                    )
                ) {
                    // arbInntektAPOverskrift
                    title1 {
                        text(
                            Bokmal to "Arbeidsinntekt og alderspensjon",
                            Nynorsk to "Arbeidsinntekt og alderspensjon",
                            English to "Earned income and retirement pension",
                        )
                    }
                    // arbInntektAP
                    paragraph {
                        text(
                            Bokmal to "Du kan arbeide så mye du vil uten at alderspensjonen din blir redusert. Det kan føre til at pensjonen din øker.",
                            Nynorsk to "Du kan arbeide så mykje du vil utan at alderspensjonen din blir redusert. Det kan føre til at pensjonen din aukar.",
                            English to "You can work as much as you want without your retirement pension being reduced. This may lead to an increase in your pension.",
                        )
                    }
                    // nyOpptjeningHelAP
                    showIf(uttaksgrad.equalTo(100)) {
                        paragraph {
                            text(
                                Bokmal to "Hvis du har 100 prosent alderspensjon, gjelder økningen fra 1. januar året etter at skatteoppgjøret ditt er ferdig.",
                                Nynorsk to "Dersom du har 100 prosent alderspensjon, gjeld auken frå 1. januar året etter at skatteoppgjeret ditt er ferdig.",
                                English to "If you are receiving a full (100 percent) retirement pension, the increase will come into effect from 1 January the year after your final tax settlement has been completed.",
                            )
                        }
                    }.orShow {
                        // nyOpptjeningGradertAP
                        paragraph {
                            text(
                                Bokmal to "Hvis du har lavere enn 100 prosent alderspensjon, blir økningen lagt til hvis du søker om endret grad eller ny beregning av den graden du har nå.",
                                Nynorsk to "Dersom du har lågare enn 100 prosent alderspensjon, blir auken lagd til dersom du søkjer om endra grad eller ny berekning av den graden du har no.",
                                English to "If you are receiving retirement pension at a reduced rate (lower than 100 percent), the increase will come into effect if you apply to have the rate changed or have your current rate recalculated.",
                            )
                        }
                    }
                    // arbInntektAPogUT
                    showIf(uforeKombinertMedAlder) {
                        paragraph {
                            text(
                                Bokmal to "Uføretrygden din kan fortsatt bli redusert på grunn av inntekt. Du finner informasjon om inntektsgrensen i vedtak om uføretrygd.",
                                Nynorsk to "Uføretrygda di kan framleis bli redusert på grunn av inntekt. Du finn informasjon om inntektsgrensa i vedtak om uføretrygd.",
                                English to "Your disability benefit may still be reduced as a result of income. You can find information on the income limit in the decision on disability benefit.",
                            )
                        }
                    }
                }
            }
        }
    }



