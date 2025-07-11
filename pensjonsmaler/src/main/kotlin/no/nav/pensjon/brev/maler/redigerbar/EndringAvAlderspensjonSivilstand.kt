package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP1967
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2025
import no.nav.pensjon.brev.api.model.KravArsakType.ALDERSOVERGANG
import no.nav.pensjon.brev.api.model.KravArsakType.VURDER_SERSKILT_SATS
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.GIFT
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.SAMBOER_1_5
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.SAMBOER_3_2
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaPensjonsistParInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.saertilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.ufoereKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.garantitillegg_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.grunnbelop
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.borSammenMedBruker
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.harInntektOver2G
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.mottarOmstillingsstonad
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.mottarPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.epsVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.kravAarsak
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.saerskiltSatsErBrukt
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.vedtakEtterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.aarligKontrollEPS
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.endringPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsAvkallPaaEgenAlderspenspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsAvkallPaaEgenUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsHarInntektOver1G
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsHarRettTilFullAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsIkkeFylt62Aar
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsIkkeRettTilFullAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsInntektOekningReduksjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsTarUtAlderspensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsTarUtAlderspensjonIStatligSektor
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsTarUtUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.feilutbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.fraFlyttet
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.giftBorIkkeSammen
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.ingenBetydning
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.institusjonsopphold
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.pensjonenOeker
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.pensjonenRedusert
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.samboereMedFellesBarn
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.samboereTidligereGift
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.*
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


// MF_000102 Vedtaksbrevet dekker alle regelverkstypene.

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
        val garantitillegg = pesysData.beregnetPensjonPerManedVedVirk.garantitillegg_safe.ifNull(then = Kroner(0))
        val regelverkType = pesysData.regelverkType
        val kravArsakType = pesysData.kravAarsak
        val sivilstand = pesysData.sivilstand
        val sivilstandBestemtStorBokstav = pesysData.sivilstand.bestemtForm(storBokstav = true)
        val sivilstandBestemtLitenBokstav = pesysData.sivilstand.bestemtForm(storBokstav = false)
        val harInntektOver2G = pesysData.epsVedVirk.harInntektOver2G
        val mottarPensjon = pesysData.epsVedVirk.mottarPensjon
        val borSammenMedBruker = pesysData.epsVedVirk.borSammenMedBruker
        val mottarOmstillingsstonad = pesysData.epsVedVirk.mottarOmstillingsstonad
        val grunnpensjon = pesysData.beregnetPensjonPerManedVedVirk.grunnpensjon.ifNull(then = Kroner(0))
        val garantipensjonInnvilget = pesysData.alderspensjonVedVirk.garantipensjonInnvilget
        val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
        val minstenivaaIndividuellInnvilget = pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget
        val minstenivaaPensjonistParInnvilget = pesysData.alderspensjonVedVirk.minstenivaaPensjonsistParInnvilget
        val saertilleggInnvilget = pesysData.alderspensjonVedVirk.saertilleggInnvilget
        val saerskiltSatsErBrukt = pesysData.saerskiltSatsErBrukt
        val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.ufoereKombinertMedAlder
        val totalPensjon = pesysData.beregnetPensjonPerManedVedVirk.totalPensjon.format()
        val vedtakEtterbetaling = pesysData.vedtakEtterbetaling
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad.ifNull(then = (0))
        val grunnbelop = pesysData.beregnetPensjonPerManedVedVirk.grunnbelop



        title {
            showIf(kravArsakType.isNotAnyOf(ALDERSOVERGANG)) {
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
            includePhrase(Vedtak.Overskrift)

            showIf(kravArsakType.isOneOf(KravArsakType.SIVILSTANDSENDRING)) {
                showIf(sivilstand.isOneOf(GIFT) and borSammenMedBruker) {
                    // endringSivilstandGiftUnder2G, endringSisvilstandGiftOver2G, endringSisvilstandGiftYtelse
                    paragraph {
                        val navn = fritekst("navn")
                        textExpr(
                            Bokmal to "Du har giftet deg med ".expr() + navn + ",",
                            Nynorsk to "Du har gifta deg med ".expr() + navn + ",",
                            English to "You have married ".expr() + navn + ","
                        )
                        showIf(mottarPensjon) {
                            text(
                                Bokmal to " som mottar pensjon, uføretrygd eller avtalefestet pensjon.",
                                Nynorsk to " som får eigen pensjon, uføretrygd eller avtalefesta pensjon.",
                                English to " who receives a pension, disability benefit or contractual early retirement pension (AFP)."
                            )
                        }.orShowIf(harInntektOver2G) {
                            text(
                                Bokmal to " som har en inntekt større enn to ganger grunnbeløpet.",
                                Nynorsk to " som har ei inntekt større enn to gonger grunnbeløpet.",
                                English to " who has an income that is more than twice the national insurance basic amount.",
                            )
                        }.orShow {
                            text(
                                Bokmal to " som har en inntekt mindre enn to ganger grunnbeløpet.",
                                Nynorsk to " som har ei inntekt mindre enn to gonger grunnbeløpet.",
                                English to " who has an income that is less than twice the national insurance basic amount.",
                            )
                        }
                    }
                }.orShowIf(sivilstand.isOneOf(SAMBOER_3_2)) {
                    // endringSisvilstand3-2samboer
                    paragraph {
                        val navn = fritekst("navn")
                        textExpr(
                            Bokmal to "Du har bodd sammen med ".expr() + navn + " i 12 av de siste 18 månedene.",
                            Nynorsk to "Du har budd saman med ".expr() + navn + " i 12 av dei siste 18 månadene.",
                            English to "You have been living with ".expr() + navn + " for 12 out of the past 18 months."
                        )
                    }
                }.orShowIf(sivilstand.isOneOf(SAMBOER_1_5)) {
                    // Radio knapper: Velg type § 1-5 samboer
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(saksbehandlerValg.samboereMedFellesBarn) {
                        paragraph {
                            // endringSivilstand1-5samboerBarn
                            val navn = fritekst("navn")
                            textExpr(
                                Bokmal to "Du har flyttet sammen med ".expr() + navn + ", og dere har barn sammen.",
                                Nynorsk to "Du har flytta saman med ".expr() + navn + ", og dere har barn saman.",
                                English to "You have moved together with ".expr() + navn + ", with whom you have children."

                            )
                        }
                    }
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(saksbehandlerValg.samboereTidligereGift) {
                        // endringSivilstand1-5samboerTidlGift
                        paragraph {
                            val navn = fritekst("navn")
                            textExpr(
                                Bokmal to "Du har flyttet sammen med ".expr() + navn + ", og dere har vært tidligere.",
                                Nynorsk to "Du har flytta saman med ".expr() + navn + ", og dere har vore gift tidlegare.",
                                English to "You have moved together with ".expr() + navn + ", with whom you were previously married."
                            )
                        }
                    }
                }

                // samboerInntektUnder2G, samboerInntektOver2G, samboerYtelse, samboerOmstillingstonad
                showIf(
                    sivilstand.isOneOf(
                        SAMBOER_3_2,
                        SAMBOER_1_5
                    ) and not(mottarOmstillingsstonad)
                ) {
                    showIf(mottarPensjon) {
                        paragraph {
                            text(
                                Bokmal to "Samboeren din mottar pensjon, uføretrygd eller avtalefestet pensjon.",
                                Nynorsk to "Sambuaren din får pensjon, uføretrygd eller avtalefesta pensjon.",
                                English to "Your cohabitant receives a pension, disability benefit or contractual early retirement pension (AFP)."
                            )
                        }
                    }.orShow {
                        paragraph {
                            textExpr(
                                Bokmal to "Samboeren din har en inntekt ".expr() + ifElse(
                                    harInntektOver2G,
                                    ifTrue = "større",
                                    ifFalse = "mindre"
                                ) + " enn to ganger grunnbeløpet.",
                                Nynorsk to "Sambuaren din har ei inntekt ".expr() + ifElse(
                                    harInntektOver2G,
                                    ifTrue = "større",
                                    ifFalse = "mindre"
                                ) + " enn to gonger grunnbeløpet.",
                                English to "Your cohabitant has an income that ".expr() + ifElse(
                                    harInntektOver2G,
                                    ifTrue = "exceeds",
                                    ifFalse = "is less than"
                                ) + " twice the national insurance basic amount.",
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
            } // END of kravArsakType.SIVILSTANDSENDRING

            // Radioknapper: Velg endring i EPS inntekt
            // endringInntektOktEPS, endringInntektRedusertEPS
            // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
            showIf(kravArsakType.isOneOf(KravArsakType.EPS_ENDRET_INNTEKT) and saksbehandlerValg.epsInntektOekningReduksjon) {
                paragraph {
                    val epsInntektsendringNB = fritekst("økt/redusert")
                    val epsInntektsendringNN = fritekst("auka/redusert")
                    val epsInntektsendringEN = fritekst("increased/been reduced")
                    textExpr(
                        Bokmal to "Inntekten til ".expr()
                                + sivilstandBestemtLitenBokstav + " din er ".expr() + epsInntektsendringNB + ".",
                        Nynorsk to "Inntekta til ".expr() + sivilstandBestemtLitenBokstav + " din er ".expr() + epsInntektsendringNN + ".",
                        English to "Your ".expr() + sivilstandBestemtLitenBokstav + "'s income has ".expr() + epsInntektsendringEN + ".",
                    )
                }
            }

            showIf(
                kravArsakType.isOneOf(KravArsakType.EPS_NY_YTELSE, KravArsakType.EPS_NY_YTELSE_UT)
                        and not(mottarOmstillingsstonad)
            ) {
                // innvilgetYtelseEPS
                paragraph {
                    textExpr(
                        Bokmal to sivilstandBestemtStorBokstav + " din har fått innvilget egen pensjon eller uføretrygd.",
                        Nynorsk to sivilstandBestemtStorBokstav + " din har fått innvilga eigen pensjon eller eiga uføretrygd.",
                        English to "Your ".expr() + sivilstandBestemtLitenBokstav + " has been granted a pension or disability benefit."
                    )
                }
            }.orShowIf(kravArsakType.isOneOf(KravArsakType.TILSTOT_ENDR_YTELSE)) {
                // endringYtelseEPS
                paragraph {
                    textExpr(
                        Bokmal to "Pensjonen eller uføretrygden til ".expr() + sivilstandBestemtLitenBokstav + " din er endret.",
                        Nynorsk to "Pensjonen eller uføretrygda til ".expr() + sivilstandBestemtLitenBokstav + " din er endra.",
                        English to "Your ".expr() + sivilstandBestemtLitenBokstav + "'s pension or disability benefit has been changed."
                    )
                }
            }.orShowIf(
                kravArsakType.isOneOf(KravArsakType.EPS_OPPH_YTELSE_UT, KravArsakType.TILSTOT_OPPHORT)
                        and not(harInntektOver2G)
            ) {
                // opphorYtelseEPS
                paragraph {
                    textExpr(
                        Bokmal to sivilstandBestemtStorBokstav + " din mottar ikke lenger egen pensjon eller uføretrygd.",
                        Nynorsk to sivilstandBestemtStorBokstav + " din får ikkje lenger eigen pensjon eller eiga uføretrygd.",
                        English to "Your ".expr() + sivilstandBestemtLitenBokstav + " no longer receives a pension or disability benefit."
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
                        Bokmal to sivilstandBestemtStorBokstav + " din mottat  ikke lenger egen pensjon eller uføretrygd, men har fortsatt en inntekt større enn to ganger grunnbeløpet.",
                        Nynorsk to sivilstandBestemtStorBokstav + " din får ikkje lenger eigen pensjon eller eiga uføretrygd, men har framleis ei inntekt som er større enn to gonger grunnbeløpet.",
                        English to "Your ".expr() + sivilstandBestemtLitenBokstav + " no longer receives a pension or disability benefit, but still has an annual income that exceeds twice the national insurance basic amount."
                    )
                }
            }

            // Radioknapper: Hva er årsaken til sivilstandsendringen?
            // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
            showIf(
                kravArsakType.isOneOf(KravArsakType.SIVILSTANDSENDRING) and not(borSammenMedBruker) and saksbehandlerValg.fraFlyttet
            ) {
                // flyttetEPS
                paragraph {
                    val navn = fritekst("navn")
                    textExpr(
                        Bokmal to "Du og ".expr() + navn + " bor ikke lenger sammen.",
                        Nynorsk to "Du og ".expr() + navn + "bur ikkje lenger saman.",
                        English to "You and ".expr() + navn + "no longer live together."
                    )
                }

                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(kravArsakType.isOneOf(KravArsakType.SIVILSTANDSENDRING) and not(borSammenMedBruker) and saksbehandlerValg.giftBorIkkeSammen) {
                    // endirngSivilstandGiftBorIkkeSammen
                    paragraph {
                        text(
                            Bokmal to "Du har giftet deg. Ifølge folkeregisteret er du og ektefellen din registrert bosatt på ulike adresser.",
                            Nynorsk to "Du har gifta deg. Ifølgje folkeregisteret er du og ektefellen din registrert busette på ulike adresser.",
                            English to "You have gotten married. According to the national registry you and your spouse are listed at different residential addresses."
                        )
                    }
                }
                // Radioknapper: Alders- og sykehjem eller EPS på annen institusjon
                // endringSykehjem, endringSykehjemEPS, endringSykkehjemBegge, endringInstitusjonEPS
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(kravArsakType.isOneOf(KravArsakType.INSTOPPHOLD) and saksbehandlerValg.institusjonsopphold) {
                    paragraph {
                        textExpr(
                            Bokmal to fritekst("Du/Ektefellen/Partneren/Samboeren/Begge") + " din har flyttet på ".expr() + fritekst(
                                "sykehjem/institusjon"
                            ) + ".",
                            Nynorsk to fritekst("Du/Ektefellen/Partnaren/Sambuaren/Begge") + " din har flytta på ".expr() + fritekst(
                                "sjukeheim/institusjon "
                            ) + ".",
                            English to fritekst("You/Your spouse/partner/cohabitant have/has") + " moved into ".expr() + fritekst(
                                "a nursing home/an institution"
                            ) + "."
                        )
                    }
                }
            }

            showIf(
                kravArsakType.isNotAnyOf(
                    ALDERSOVERGANG,
                    VURDER_SERSKILT_SATS
                ) and regelverkType.isNotAnyOf(AP2025)
            ) {
                showIf(grunnpensjon.greaterThan(0)) {
                    showIf(
                        (minstenivaaPensjonistParInnvilget or minstenivaaIndividuellInnvilget)
                                and not(saertilleggInnvilget) and not(pensjonstilleggInnvilget)
                    ) {
                        showIf(garantipensjonInnvilget) {
                            // omregningGP_GarantiPen_MNT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen og garantipensjonen din på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen og garantipensjonen din på nytt.",
                                    English to "We have therefore recalculated your basic pension and guaranteed pension."
                                )
                            }
                        }.orShow {
                            // omregningGP_MNT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen og minstenivåtillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen og minstenivåtillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension and minimum pension supplement."
                                )
                            }
                        }
                    }.orShowIf(
                        (minstenivaaPensjonistParInnvilget or minstenivaaIndividuellInnvilget) and pensjonstilleggInnvilget
                                and not(saertilleggInnvilget)
                    ) {
                        // omregningGP_PenT_MNT
                        showIf(garantipensjonInnvilget) {
                            // omregningGP_PenT_Garanti_MNT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen, pensjonstillegget, garantipensjonen og minstenivåtillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen, pensjonstillegget, garantipensjonen og minstenivåtillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension, supplementary pension, guaranteed pension and minimum pension supplement."
                                )
                            }
                        }.orShow {
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen, pensjonstillegget og minstenivåtillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen, pensjonstillegget og minstenivåtillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension, supplementary pension and minimum pension supplement."
                                )
                            }
                        }
                    }.orShowIf(
                        not(saertilleggInnvilget) and not(minstenivaaPensjonistParInnvilget)
                                and not(minstenivaaIndividuellInnvilget) and not(pensjonstilleggInnvilget)
                    ) {
                        showIf(garantipensjonInnvilget) {
                            // omregningGP_GarantiPen
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen og garantipensjonen din på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen og garantipensjonen din på nytt.",
                                    English to "We have therefore recalculated your basic pension and guaranteed pension."
                                )
                            }
                        }.orShow {
                            // omregningGP
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen din på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen din på nytt.",
                                    English to "Derfor har vi berekna grunnpensjonen din på nytt."
                                )
                            }
                        }
                    }.orShowIf(
                        pensjonstilleggInnvilget
                                and not(saertilleggInnvilget) and not(minstenivaaPensjonistParInnvilget)
                                and not(minstenivaaIndividuellInnvilget)
                    ) {
                        showIf(garantipensjonInnvilget) {
                            // omregningGP_PenT_GarantiPen_MNT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen, pensjonstillegget, garantipensjonen og minstenivåtillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen, pensjonstillegget, garantipensjonen og minstenivåtillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension, supplementary pension, guaranteed pension and minimum pension supplement."
                                )
                            }
                        }.orShow {
                            // omregningGP_PenT
                            paragraph {
                                text(
                                    Bokmal to "Derfor har vi beregnet grunnpensjonen og pensjonstillegget ditt på nytt.",
                                    Nynorsk to "Derfor har vi berekna grunnpensjonen og pensjonstillegget ditt på nytt.",
                                    English to "We have therefore recalculated your basic pension and pension supplement."
                                )
                            }
                        }
                    }
                    showIf(
                        regelverkType.isOneOf(AP1967) and saertilleggInnvilget
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
            }

            showIf(regelverkType.isOneOf(AP2025)) {
                // omregning_GarantiPen
                paragraph {
                    text(
                        Bokmal to "Derfor har vi vurdert garantipensjonen din på nytt.",
                        Nynorsk to "Derfor har vi vurdert garantipensjonen din på nytt.",
                        English to "We have therefore recalculated your guaranteed pension."
                    )
                }
            }

            showIf(kravArsakType.isOneOf(ALDERSOVERGANG)) {
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
            showIf(kravArsakType.isOneOf(VURDER_SERSKILT_SATS)) {
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(saksbehandlerValg.epsIkkeFylt62Aar) {
                    // SaerSatsBruktEpsUnder62
                    paragraph {
                        textExpr(
                            Bokmal to sivilstandBestemtStorBokstav + " din du forsørger har en inntekt lavere enn grunnbeløpet ".expr() + grunnbelop.format() + " kroner.",
                            Nynorsk to sivilstandBestemtStorBokstav + " din du forsørgjer har ei inntekt lågare enn grunnbeløpet ".expr() + grunnbelop.format() + " kroner.",
                            English to "Your ".expr() + sivilstandBestemtLitenBokstav + " you support has an income lower than the basic amount which is NOK ".expr() + grunnbelop.format() + ".",
                        )
                    }
                }
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(saksbehandlerValg.epsIkkeRettTilFullAlderspensjon) {
                    // SaerSatsBruktEpsIkkeRettTilAP
                    paragraph {
                        textExpr(
                            Bokmal to sivilstandBestemtStorBokstav + " din som du forsørger har ikke rett til full alderspensjon fra folketrygden og har inntekt lavere enn grunnbeløpet ".expr() + grunnbelop.format() + " kroner.",
                            Nynorsk to sivilstandBestemtStorBokstav + " din som du forsørgjer har ikkje rett til full alderspensjon frå folketrygda og har inntekt lågare enn grunnbeløpet ".expr() + grunnbelop.format() + " kroner.",
                            English to "Your ".expr() + sivilstandBestemtLitenBokstav + " you support does not have rights to full retirement pension through the National Insurance Act and has income lower than the basic amount which is NOK ".expr() + grunnbelop.format() + "."
                        )
                    }
                }
                showIf(saksbehandlerValg.epsAvkallPaaEgenAlderspenspensjon) {
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    // SaerSatsBruktEpsGittAvkallAP
                    paragraph {
                        textExpr(
                            Bokmal to sivilstandBestemtStorBokstav + " din har gitt avkall på sin alderspensjon fra folketrygden.",
                            Nynorsk to sivilstandBestemtStorBokstav + " din har gitt avkall på alderspensjon sin frå folketrygda.",
                            English to "Your ".expr() + sivilstandBestemtLitenBokstav + " has renounced their retirement pension through the National Insurance Act."
                        )
                    }
                }
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(saksbehandlerValg.epsAvkallPaaEgenUfoeretrygd) {
                    // SaerSatsBruktEpsGittAvkallUT
                    paragraph {
                        textExpr(
                            Bokmal to sivilstandBestemtStorBokstav + " din har gitt avkall på sin uføretrygd fra folketrygden.",
                            Nynorsk to sivilstandBestemtStorBokstav + " din har gitt avkall på uføretrygda si frå folketrygda.",
                            English to "Your ".expr() + sivilstandBestemtLitenBokstav + " has renounced their disability benefits through the National Insurance Act."
                        )
                    }
                }
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt. (brevet kunne vært delt opp basert på kravårsak.
                showIf(saksbehandlerValg.epsHarInntektOver1G) {
                    // SaerSatsIkkeBruktEpsInntektOver1G, SaerSatsIkkeBruktEpsRettTilFullAP, SaerSatsIkkeBruktEpsMottarAP, SaerSatsIkkeBruktEpsMottarAfp, SaerSatsIkkeBruktEpsMottarUT
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + sivilstandBestemtLitenBokstav + " din har inntekt høyere enn grunnbeløpet (".expr() + grunnpensjon.format() + " kroner).",
                            Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + sivilstandBestemtLitenBokstav + " din har inntekt høgare enn grunnbeløpet (".expr() + grunnpensjon.format() + " kroner).",
                            English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + sivilstandBestemtLitenBokstav + " has a higher income than the basic amount which is NOK ".expr() + grunnpensjon.format() + "."
                        )
                    }
                }
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(saksbehandlerValg.epsHarRettTilFullAlderspensjon) {
                    // SaerSatsIkkeBruktEpsRettTilFullAP
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + sivilstandBestemtLitenBokstav + " din har rett til full alderspensjon fra folketrygden.",
                            Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + sivilstandBestemtLitenBokstav + " din har rett til full alderspensjon frå folketrygda.",
                            English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + sivilstandBestemtLitenBokstav + " has rights to full retirement pension through the National Insurance Act."
                        )
                    }
                }
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(saksbehandlerValg.epsTarUtAlderspensjon) {
                    // SaerSatsIkkeBruktEpsMottarAP
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + sivilstandBestemtLitenBokstav + " din mottar alderspensjon fra folketrygden.",
                            Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + sivilstandBestemtLitenBokstav + " din mottar alderspensjon frå folketrygda.",
                            English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + sivilstandBestemtLitenBokstav + " recieves retirement pension through the National Insurance Act."
                        )
                    }
                }
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(saksbehandlerValg.epsTarUtAlderspensjonIStatligSektor) {
                    // SaerSatsIkkeBruktEpsMottarAfp
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + sivilstandBestemtLitenBokstav + " din mottar AFP i statlig sektor.",
                            Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + sivilstandBestemtLitenBokstav + " din mottar AFP i statleg sektor.",
                            English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + sivilstandBestemtLitenBokstav + " receives contractual retirement pension from the public sector."
                        )
                    }
                }
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(saksbehandlerValg.epsTarUtUfoeretrygd) {
                    // SaerSatsIkkeBruktEpsMottarUT
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ikke beregnet alderspensjonen din med særskilt sats fordi ".expr() + sivilstandBestemtLitenBokstav + " din mottar uføretrygd fra folketrygden.",
                            Nynorsk to "Du får ikkje berekna alderspensjonen din med særskilt sats fordi ".expr() + sivilstandBestemtLitenBokstav + " din mottar uføretrygd frå folketrygda.",
                            English to "Your retirement pension is not recalculated according to a special rate because your ".expr() + sivilstandBestemtLitenBokstav + " receives disability benefits through the National Insurance Act."
                        )
                    }
                }
            }

            showIf(saerskiltSatsErBrukt and kravArsakType.isOneOf(VURDER_SERSKILT_SATS)) {
                paragraph {
                    text(
                        Bokmal to "Derfor har vi beregnet ",
                        Nynorsk to "Derfor har vi berekna ",
                        English to "We have therefore recalculated your "
                    )
                    showIf(regelverkType.isOneOf(AP1967)) {
                        text(Bokmal to "særtillegget", Nynorsk to "særtillegget", English to "special supplement")
                    }.orShowIf(regelverkType.isOneOf(AP2011, AP2016)) {
                        text(Bokmal to "pensjonstillegget", Nynorsk to "pensjonstillegget", English to "basic pension")
                    }
                    showIf(minstenivaaIndividuellInnvilget) {
                        text(
                            Bokmal to " og minstenivåtillegget",
                            Nynorsk to " og minstenivåtillegget",
                            English to " and minimum pension supplement"
                        )
                    }
                    text(
                        Bokmal to " ditt på nytt med særskilt sats.",
                        Nynorsk to " ditt på nytt med særskilt sats.",
                        English to " according to a special rate."
                    )
                }
            }

            // Radioknapper: Betydning for pensjons utbetaling?
            showIf(
                regelverkType.isOneOf(AP2011, AP2016)
                        and saerskiltSatsErBrukt
            ) {
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
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
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
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
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
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

            // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
            showIf(kravArsakType.isOneOf(VURDER_SERSKILT_SATS) and saksbehandlerValg.aarligKontrollEPS) {
                // SaerSatsInfoAarligKontrollEps
                paragraph {
                    textExpr(
                        Bokmal to "Fram til ".expr() + sivilstandBestemtLitenBokstav + " din fyller 67 år, har vi en årlig kontroll om ".expr() +
                                sivilstandBestemtLitenBokstav + " din har rett til full alderpensjon. Du får nytt vedtak hvis dette fører til at alderspensjonen din blir omregnet.",
                        Nynorsk to "Fram til ".expr() + sivilstandBestemtLitenBokstav + " din fyller 67 år, har vi ein årleg kontroll av ".expr() +
                                sivilstandBestemtLitenBokstav + " si rett til full alderpensjon. Du får nytt vedtak hvis dette fører til at alderspensjonen din blir omrekna.",
                        English to "Until your ".expr() + sivilstandBestemtLitenBokstav + " turns 67 years of age, we have an annual control of their rights to a full retirement pension. " +
                                "You will receive a new decision if this results in your retirement pension being recalculated."
                    )
                }
            }

            showIf(uforeKombinertMedAlder) {
                // innvilgelseAPogUTInnledn
                includePhrase(UfoereAlder.DuFaar(pesysData.beregnetPensjonPerManedVedVirk.totalPensjon, pesysData.kravVirkDatoFom))
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

            showIf(
                kravArsakType.isNotAnyOf(ALDERSOVERGANG, VURDER_SERSKILT_SATS)
                        and regelverkType.isNotAnyOf(AP2025)
            ) {
                // hjemmelSivilstandAlleRegelverkstyper
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ ",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ ",
                        English to "This decision was made pursuant to the provisions of §§ ",
                    )
                    showIf(sivilstand.isOneOf(SAMBOER_1_5)) {
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
                    showIf(regelverkType.isOneOf(AP1967) and saertilleggInnvilget) {
                        text(
                            Bokmal to ", 3-3",
                            Nynorsk to ", 3-3",
                            English to ", 3-3",
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
                    kravArsakType.isOneOf(ALDERSOVERGANG) and regelverkType.isNotAnyOf(AP2025)
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
                kravArsakType.isOneOf(VURDER_SERSKILT_SATS) and saerskiltSatsErBrukt
            ) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ ",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ ",
                        English to "This decision was made pursuant to the provisions of §§ ",
                    )
                    showIf(regelverkType.isOneOf(AP1967)) {
                        showIf(sivilstand.isOneOf(SAMBOER_1_5)) {
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
                        regelverkType.isOneOf(AP2011, AP2016)
                    ) {
                        showIf(sivilstand.isOneOf(SAMBOER_1_5)) {
                            text(
                                Bokmal to "1-5, ",
                                Nynorsk to "1-5, ",
                                English to "1-5, "
                            )
                        }
                        text(
                            Bokmal to "19-8, 19-9 og 22-12.",
                            Nynorsk to "19-8, 19-9 og 22-12.",
                            English to "19-8, 19-9 and 22-12 of the National Insurance Act."
                        )
                    }
                }

                showIf(regelverkType.isOneOf(AP2025)) {
                    // hjemmelSivilstandAP2025
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-9, 20-17 femte avsnitt og 22-12.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-9, 20-17 femte avsnitt og 22-12.",
                            English to "This decision was made pursuant to the provisions of §§ 20-9, 20-17 fifth paragraph, and 22-12 of the National Insurance Act."
                        )
                    }
                }

                showIf(kravArsakType.isOneOf(ALDERSOVERGANG)) {
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

                // Selectable - Hvis reduksjon tilbake i tid - feilutbetalingAP
                showIf(saksbehandlerValg.feilutbetaling) {
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
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
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(saksbehandlerValg.endringPensjon) {
                    includePhrase(VedtakAlderspensjon.EndringKanHaBetydningForSkatt)
                }

                // Hvis etterbetaling (Selectable) - etterbetalingAP_002
                showIf(saksbehandlerValg.etterbetaling or vedtakEtterbetaling) {
                    includePhrase(Vedtak.Etterbetaling(pesysData.kravVirkDatoFom))
                }

                // Arbeidsinntekt og pensjon
                showIf(
                    regelverkType.isNotAnyOf(AP1967) and kravArsakType.isNotAnyOf(KravArsakType.INSTOPPHOLD)
                ) {
                    includePhrase(
                        ArbeidsinntektOgAlderspensjon(
                            uttaksgrad = uttaksgrad.ifNull(then = (0)),
                            uforeKombinertMedAlder = uforeKombinertMedAlder
                        )
                    )
                }
            }

            includePhrase(InformasjonOmAlderspensjon)
            includePhrase(MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(
            vedleggDineRettigheterOgMulighetTilAaKlage,
            pesysData.dineRettigheterOgMulighetTilAaKlageDto
        )
        includeAttachmentIfNotNull(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto)
        includeAttachmentIfNotNull(
            vedleggMaanedligPensjonFoerSkattAp2025,
            pesysData.maanedligPensjonFoerSkattAP2025Dto
        )
    }
}








