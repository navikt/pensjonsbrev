package no.nav.pensjon.brev.maler.alder.endring.sivilstand

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaIndividuellInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.minstenivaaPensjonsistParInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.pensjonstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.saertilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.ufoereKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.grunnbelop
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.borSammenMedBruker
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.harInntektOver2G
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.mottarOmstillingsstonad
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.EpsVedVirkSelectors.mottarPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.epsVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.kravAarsak
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.saerskiltSatsErBrukt
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.vedtakEtterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.aarligKontrollEPS
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.beloepEndring
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
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.institusjonsopphold
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.samboereMedFellesBarn
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.samboereTidligereGift
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.BetydningForUtbetaling
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.DuFaarAP
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.EndringYtelseEPS
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.InnvilgetYtelseEPS
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.OmregningGarantiPen
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.OpphoerYtelseEPS
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.OpphoerYtelseEPSOver2G
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.SivilstandHjemler
import no.nav.pensjon.brev.maler.fraser.alderspensjon.ArbeidsinntektOgAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.FeilutbetalingAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.InformasjonOmAlderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.MeldeFraOmEndringer
import no.nav.pensjon.brev.maler.fraser.alderspensjon.UfoereAlder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.VedtakAlderspensjon
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// MF_000102 Vedtaksbrevet dekker alle regelverkstypene.

@TemplateModelHelpers
object EndringAvAlderspensjonSivilstand : RedigerbarTemplate<EndringAvAlderspensjonSivilstandDto> {
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRING_AV_ALDERSPENSJON_SIVILSTAND
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = Sakstype.pensjon

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = EndringAvAlderspensjonSivilstandDto::class,
            languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - Endring av alderspensjon (sivilstand)",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
            val regelverkType = pesysData.regelverkType
            val kravArsakType = pesysData.kravAarsak
            val sivilstand = pesysData.sivilstand
            val sivilstandBestemtStorBokstav = pesysData.sivilstand.bestemtForm(storBokstav = true)
            val sivilstandBestemtLitenBokstav =
                pesysData.sivilstand.bestemtForm(storBokstav = false)
            val harInntektOver2G = pesysData.epsVedVirk.harInntektOver2G
            val mottarPensjon = pesysData.epsVedVirk.mottarPensjon
            val borSammenMedBruker = pesysData.epsVedVirk.borSammenMedBruker
            val mottarOmstillingsstonad = pesysData.epsVedVirk.mottarOmstillingsstonad
            val grunnpensjon =
                pesysData.beregnetPensjonPerManedVedVirk.grunnpensjon.ifNull(then = Kroner(0))
            val garantipensjonInnvilget = pesysData.alderspensjonVedVirk.garantipensjonInnvilget
            val pensjonstilleggInnvilget = pesysData.alderspensjonVedVirk.pensjonstilleggInnvilget
            val minstenivaaIndividuellInnvilget =
                pesysData.alderspensjonVedVirk.minstenivaaIndividuellInnvilget
            val minstenivaaPensjonistParInnvilget =
                pesysData.alderspensjonVedVirk.minstenivaaPensjonsistParInnvilget
            val saertilleggInnvilget = pesysData.alderspensjonVedVirk.saertilleggInnvilget
            val saerskiltSatsErBrukt = pesysData.saerskiltSatsErBrukt
            val uforeKombinertMedAlder = pesysData.alderspensjonVedVirk.ufoereKombinertMedAlder
            val totalPensjon = pesysData.beregnetPensjonPerManedVedVirk.totalPensjon.format()
            val vedtakEtterbetaling = pesysData.vedtakEtterbetaling
            val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad.ifNull(then = (0))
            val grunnbelop = pesysData.beregnetPensjonPerManedVedVirk.grunnbelop
            val innvilgetFor67 = pesysData.alderspensjonVedVirk.innvilgetFor67
            val epsNavn = fritekst("navn")

            title {
                textExpr(
                    Language.Bokmal to "Vi har beregnet alderspensjon din på nytt fra ".expr() + kravVirkDatoFom,
                    Language.Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ".expr() + kravVirkDatoFom,
                    Language.English to "We have recalculated your retirement pension from ".expr() + kravVirkDatoFom,
                )
            }
            outline {
                includePhrase(Vedtak.Overskrift)

                showIf(kravArsakType.isOneOf(KravArsakType.SIVILSTANDSENDRING)) {
                    showIf(sivilstand.isOneOf(MetaforceSivilstand.GIFT) and borSammenMedBruker) {
                        // endringSivilstandGiftUnder2G, endringSisvilstandGiftOver2G, endringSisvilstandGiftYtelse
                        paragraph {
                            text(
                                bokmal { + "Du har giftet deg med " + epsNavn + "," },
                                nynorsk { + "Du har gifta deg med " + epsNavn + "," },
                                english { + "You have married " + epsNavn + "," },
                            )
                            showIf(mottarPensjon) {
                                text(
                                    bokmal { + " som mottar pensjon, uføretrygd eller avtalefestet pensjon." },
                                    nynorsk { + " som får eigen pensjon, uføretrygd eller avtalefesta pensjon." },
                                    english { + 
                                        " who receives a pension, disability benefit or contractual early retirement pension (AFP)." },
                                )
                            }.orShowIf(harInntektOver2G) {
                                text(
                                    bokmal { + " som har en inntekt større enn to ganger grunnbeløpet." },
                                    nynorsk { + " som har ei inntekt større enn to gonger grunnbeløpet." },
                                    english { + " who has an income that is more than twice the national insurance basic amount." },
                                )
                            }.orShow {
                                text(
                                    bokmal { + " som har en inntekt mindre enn to ganger grunnbeløpet." },
                                    nynorsk { + " som har ei inntekt mindre enn to gonger grunnbeløpet." },
                                    english { + " who has an income that is less than twice the national insurance basic amount." },
                                )
                            }
                        }
                    }.orShowIf(sivilstand.isOneOf(MetaforceSivilstand.SAMBOER_3_2)) {
                        // endringSisvilstand3-2samboer
                        paragraph {
                            text(
                                bokmal { + "Du har bodd sammen med " + epsNavn + " i 12 av de siste 18 månedene." },
                                nynorsk { + "Du har budd saman med " + epsNavn + " i 12 av dei siste 18 månadene." },
                                english { + "You have been living with " + epsNavn + " for 12 out of the past 18 months." },
                            )
                        }
                    }.orShowIf(sivilstand.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                        // Radio knapper: Velg type § 1-5 samboer
                        // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.

                        showIf(saksbehandlerValg.samboereMedFellesBarn) {
                            paragraph {
                                // endringSivilstand1-5samboerBarn
                                text(
                                    bokmal { + "Du har flyttet sammen med " + epsNavn + ", og dere har barn sammen." },
                                    nynorsk { + "Du har flytta saman med " + epsNavn + ", og dere har barn saman." },
                                    english { + "You have moved together with " + epsNavn + ", with whom you have children." },
                                )
                            }
                        }
                        // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                        showIf(saksbehandlerValg.samboereTidligereGift) {
                            // endringSivilstand1-5samboerTidlGift
                            paragraph {
                                text(
                                    bokmal { + "Du har flyttet sammen med " + epsNavn + ", og dere har vært gift tidligere." },
                                    nynorsk { + "Du har flytta saman med " + epsNavn + ", og dere har vore gift tidlegare." },
                                    english { + 
                                        "You have moved together with " + epsNavn + ", with whom you were previously married." },
                                )
                            }
                        }
                    }

                    // samboerInntektUnder2G, samboerInntektOver2G, samboerYtelse, samboerOmstillingstonad
                    showIf(
                        sivilstand.isOneOf(
                            MetaforceSivilstand.SAMBOER_3_2,
                            MetaforceSivilstand.SAMBOER_1_5,
                        ) and not(mottarOmstillingsstonad),
                    ) {
                        showIf(mottarPensjon) {
                            paragraph {
                                text(
                                    bokmal { + "Samboeren din mottar pensjon, uføretrygd eller avtalefestet pensjon." },
                                    nynorsk { + "Sambuaren din får pensjon, uføretrygd eller avtalefesta pensjon." },
                                    english { + 
                                        "Your cohabitant receives a pension, disability benefit or contractual early retirement pension (AFP)." },
                                )
                            }
                        }.orShow {
                            paragraph {
                                text(
                                    bokmal { + "Samboeren din har en inntekt " +
                                        ifElse(
                                            harInntektOver2G,
                                            ifTrue = "større",
                                            ifFalse = "mindre",
                                        ) + " enn to ganger grunnbeløpet." },
                                    nynorsk { + "Sambuaren din har ei inntekt " +
                                        ifElse(
                                            harInntektOver2G,
                                            ifTrue = "større",
                                            ifFalse = "mindre",
                                        ) + " enn to gonger grunnbeløpet." },
                                    english { + "Your cohabitant has an income that " +
                                        ifElse(
                                            harInntektOver2G,
                                            ifTrue = "exceeds",
                                            ifFalse = "is less than",
                                        ) + " twice the national insurance basic amount." },
                                )
                            }
                        }
                    }.orShowIf(mottarOmstillingsstonad) {
                        paragraph {
                            text(
                                bokmal { + "Samboeren din mottar omstillingsstønad." },
                                nynorsk { + "Sambuaren din mottek omstillingsstønad." },
                                english { + "Your cohabitant receives adjustment allowance." },
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
                        text(
                            bokmal { + "Inntekten til " +
                                sivilstandBestemtLitenBokstav + " din er " + epsInntektsendringNB + "." },
                            nynorsk { + 
                                "Inntekta til " + sivilstandBestemtLitenBokstav + " din er " + epsInntektsendringNN + "." },
                            english { + 
                                "Your " + sivilstandBestemtLitenBokstav + "'s income has " + epsInntektsendringEN + "." },
                        )
                    }
                }

                showIf(
                    kravArsakType.isOneOf(
                        KravArsakType.EPS_NY_YTELSE,
                        KravArsakType.EPS_NY_YTELSE_UT,
                    )
                        and not(mottarOmstillingsstonad),
                ) {
                    includePhrase(InnvilgetYtelseEPS(pesysData.sivilstand))
                }.orShowIf(kravArsakType.isOneOf(KravArsakType.TILSTOT_ENDR_YTELSE)) {
                    includePhrase(EndringYtelseEPS(pesysData.sivilstand))
                }.orShowIf(
                    kravArsakType.isOneOf(
                        KravArsakType.EPS_OPPH_YTELSE_UT,
                        KravArsakType.TILSTOT_OPPHORT,
                    )
                        and not(harInntektOver2G),
                ) {
                    includePhrase(OpphoerYtelseEPS(pesysData.sivilstand))
                }.orShowIf(
                    kravArsakType.isOneOf(
                        KravArsakType.EPS_OPPH_YTELSE_UT,
                        KravArsakType.TILSTOT_OPPHORT,
                    ) and not(mottarOmstillingsstonad),
                ) {
                    // opphorOmstillingSambo
                    paragraph {
                        text(
                            bokmal { + "Samboeren din mottar ikke lenger omstillingsstønad." },
                            nynorsk { + "Sambuaren din mottek ikkje lenger omstillingsstønad." },
                            english { + "Your cohabitant does not receive adjustment allowance." },
                        )
                    }
                }.orShowIf(
                    kravArsakType.isOneOf(
                        KravArsakType.EPS_OPPH_YTELSE_UT,
                        KravArsakType.TILSTOT_OPPHORT,
                    ) and harInntektOver2G,
                ) {
                    includePhrase(OpphoerYtelseEPSOver2G(pesysData.sivilstand))
                }

                // Radioknapper: Hva er årsaken til sivilstandsendringen?
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(
                    kravArsakType.isOneOf(KravArsakType.SIVILSTANDSENDRING) and
                        not(
                            borSammenMedBruker,
                        ) and saksbehandlerValg.fraFlyttet,
                ) {
                    // flyttetEPS
                    paragraph {
                        text(
                            bokmal { + "Du og " + epsNavn + " bor ikke lenger sammen." },
                            nynorsk { + "Du og " + epsNavn + "bur ikkje lenger saman." },
                            english { + "You and " + epsNavn + "no longer live together." },
                        )
                    }

                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(
                        kravArsakType.isOneOf(KravArsakType.SIVILSTANDSENDRING) and
                            not(
                                borSammenMedBruker,
                            ) and saksbehandlerValg.giftBorIkkeSammen,
                    ) {
                        // endirngSivilstandGiftBorIkkeSammen
                        paragraph {
                            text(
                                bokmal { + 
                                    "Du har giftet deg. Ifølge folkeregisteret er du og ektefellen din registrert bosatt på ulike adresser." },
                                nynorsk { + 
                                    "Du har gifta deg. Ifølgje folkeregisteret er du og ektefellen din registrert busette på ulike adresser." },
                                english { + 
                                    "You have gotten married. According to the national registry you and your spouse are listed at different residential addresses." },
                            )
                        }
                    }
                    // Radioknapper: Alders- og sykehjem eller EPS på annen institusjon
                    // endringSykehjem, endringSykehjemEPS, endringSykkehjemBegge, endringInstitusjonEPS
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(kravArsakType.isOneOf(KravArsakType.INSTOPPHOLD) and saksbehandlerValg.institusjonsopphold) {
                        paragraph {
                            text(
                                bokmal { + 
                                    fritekst("Du/Ektefellen/Partneren/Samboeren/Begge") + " din har flyttet på " +
                                    fritekst(
                                        "sykehjem/institusjon",
                                    ) + "." },
                                nynorsk { + 
                                    fritekst("Du/Ektefellen/Partnaren/Sambuaren/Begge") + " din har flytta på " +
                                    fritekst(
                                        "sjukeheim/institusjon ",
                                    ) + "." },
                                english { + fritekst("You/Your spouse/partner/cohabitant have/has") + " moved into " +
                                    fritekst(
                                        "a nursing home/an institution",
                                    ) + "." },
                            )
                        }
                    }
                }

                showIf(
                    kravArsakType.isNotAnyOf(
                        KravArsakType.ALDERSOVERGANG,
                        KravArsakType.VURDER_SERSKILT_SATS,
                    ) and regelverkType.isNotAnyOf(AlderspensjonRegelverkType.AP2025),
                ) {
                    showIf(grunnpensjon.greaterThan(0)) {
                        showIf(
                            (minstenivaaPensjonistParInnvilget or minstenivaaIndividuellInnvilget)
                                and not(saertilleggInnvilget) and not(pensjonstilleggInnvilget),
                        ) {
                            showIf(garantipensjonInnvilget) {
                                // omregningGP_GarantiPen_MNT
                                paragraph {
                                    text(
                                        bokmal { + "Derfor har vi beregnet grunnpensjonen og garantipensjonen din på nytt." },
                                        nynorsk { + "Derfor har vi berekna grunnpensjonen og garantipensjonen din på nytt." },
                                        english { + "We have therefore recalculated your basic pension and guaranteed pension." },
                                    )
                                }
                            }.orShow {
                                // omregningGP_MNT
                                paragraph {
                                    text(
                                        bokmal { + "Derfor har vi beregnet grunnpensjonen og minstenivåtillegget ditt på nytt." },
                                        nynorsk { + "Derfor har vi berekna grunnpensjonen og minstenivåtillegget ditt på nytt." },
                                        english { + 
                                            "We have therefore recalculated your basic pension and minimum pension supplement." },
                                    )
                                }
                            }
                        }.orShowIf(
                            (minstenivaaPensjonistParInnvilget or minstenivaaIndividuellInnvilget) and pensjonstilleggInnvilget
                                and not(saertilleggInnvilget),
                        ) {
                            // omregningGP_PenT_MNT
                            showIf(garantipensjonInnvilget) {
                                // omregningGP_PenT_Garanti_MNT
                                paragraph {
                                    text(
                                        bokmal { + 
                                            "Derfor har vi beregnet grunnpensjonen, pensjonstillegget, garantipensjonen og minstenivåtillegget ditt på nytt." },
                                        nynorsk { + 
                                            "Derfor har vi berekna grunnpensjonen, pensjonstillegget, garantipensjonen og minstenivåtillegget ditt på nytt." },
                                        english { + 
                                            "We have therefore recalculated your basic pension, supplementary pension, guaranteed pension and minimum pension supplement." },
                                    )
                                }
                            }.orShow {
                                paragraph {
                                    text(
                                        bokmal { + 
                                            "Derfor har vi beregnet grunnpensjonen, pensjonstillegget og minstenivåtillegget ditt på nytt." },
                                        nynorsk { + 
                                            "Derfor har vi berekna grunnpensjonen, pensjonstillegget og minstenivåtillegget ditt på nytt." },
                                        english { + 
                                            "We have therefore recalculated your basic pension, supplementary pension and minimum pension supplement." },
                                    )
                                }
                            }
                        }.orShowIf(
                            not(saertilleggInnvilget) and not(minstenivaaPensjonistParInnvilget)
                                and not(minstenivaaIndividuellInnvilget) and
                                not(
                                    pensjonstilleggInnvilget,
                                ),
                        ) {
                            showIf(garantipensjonInnvilget) {
                                // omregningGP_GarantiPen
                                paragraph {
                                    text(
                                        bokmal { + "Derfor har vi beregnet grunnpensjonen og garantipensjonen din på nytt." },
                                        nynorsk { + "Derfor har vi berekna grunnpensjonen og garantipensjonen din på nytt." },
                                        english { + "We have therefore recalculated your basic pension and guaranteed pension." },
                                    )
                                }
                            }.orShow {
                                // omregningGP
                                paragraph {
                                    text(
                                        bokmal { + "Derfor har vi beregnet grunnpensjonen din på nytt." },
                                        nynorsk { + "Derfor har vi berekna grunnpensjonen din på nytt." },
                                        english { + "We have therefore recalculated your basic pension." },
                                    )
                                }
                            }
                        }.orShowIf(
                            pensjonstilleggInnvilget
                                and not(saertilleggInnvilget) and
                                not(
                                    minstenivaaPensjonistParInnvilget,
                                )
                                and not(minstenivaaIndividuellInnvilget),
                        ) {
                            showIf(garantipensjonInnvilget) {
                                // omregningGP_PenT_GarantiPen_MNT
                                paragraph {
                                    text(
                                        bokmal { + 
                                            "Derfor har vi beregnet grunnpensjonen, pensjonstillegget og garantipensjonen din på nytt." },
                                        nynorsk { + 
                                            "Derfor har vi berekna grunnpensjonen, pensjonstillegget og garantipensjonen din på nytt." },
                                        english { + 
                                            "We have therefore recalculated your basic pension, supplementary pension and guaranteed pension." },
                                    )
                                }
                            }.orShow {
                                // omregningGP_PenT
                                paragraph {
                                    text(
                                        bokmal { + "Derfor har vi beregnet grunnpensjonen og pensjonstillegget ditt på nytt." },
                                        nynorsk { + "Derfor har vi berekna grunnpensjonen og pensjonstillegget ditt på nytt." },
                                        english { + "We have therefore recalculated your basic pension and pension supplement." },
                                    )
                                }
                            }
                        }
                        showIf(
                            regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967) and saertilleggInnvilget,
                        ) {
                            showIf(
                                not(minstenivaaPensjonistParInnvilget) and
                                    not(
                                        minstenivaaPensjonistParInnvilget,
                                    ),
                            ) {
                                // omregningGPST
                                paragraph {
                                    text(
                                        bokmal { + "Derfor har vi beregnet grunnpensjonen og særtillegget ditt på nytt." },
                                        nynorsk { + "Derfor har vi berekna grunnpensjonen og særtillegget ditt på nytt." },
                                        english { + "We have therefore recalculated your basic pension and the special supplement." },
                                    )
                                }
                            }.orShowIf((minstenivaaPensjonistParInnvilget or minstenivaaIndividuellInnvilget)) {
                                // omregningGPSTMNT
                                paragraph {
                                    text(
                                        bokmal { + 
                                            "Derfor har vi beregnet grunnpensjonen, særtillegget og minstenivåtillegget ditt på nytt." },
                                        nynorsk { + 
                                            "Derfor har vi berekna grunnpensjonen, særtillegget og minstenivåtillegget ditt på nytt." },
                                        english { + 
                                            "We have therefore recalculated your basic pension, the special supplement and the minimum level supplement." },
                                    )
                                }
                            }
                        }
                    }
                }

                includePhrase(OmregningGarantiPen(regelverkType))

                // Radioknapper: Forsørger EPS over 60 år. Særskilt sats for minste pensjonsnivå
                showIf(kravArsakType.isOneOf(KravArsakType.VURDER_SERSKILT_SATS)) {
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(saksbehandlerValg.epsIkkeFylt62Aar) {
                        // SaerSatsBruktEpsUnder62
                        paragraph {
                            text(
                                bokmal { + 
                                    sivilstandBestemtStorBokstav + " din du forsørger har en inntekt lavere enn grunnbeløpet " +
                                    grunnbelop.format() +
                                    "." },
                                nynorsk { + 
                                    sivilstandBestemtStorBokstav + " din du forsørgjer har ei inntekt lågare enn grunnbeløpet " +
                                    grunnbelop.format() +
                                    "." },
                                english { + 
                                    "Your " + sivilstandBestemtLitenBokstav +
                                    " you support has an income lower than the basic amount which is " +
                                    grunnbelop.format() +
                                    "." },
                            )
                        }
                    }
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(saksbehandlerValg.epsIkkeRettTilFullAlderspensjon) {
                        // SaerSatsBruktEpsIkkeRettTilAP
                        paragraph {
                            text(
                                bokmal { + 
                                    sivilstandBestemtStorBokstav +
                                    (
                                        " din som du forsørger har ikke rett til full alderspensjon fra" +
                                            " folketrygden og har inntekt lavere enn grunnbeløpet "
                                    ).expr() +
                                    grunnbelop.format() +
                                    "." },
                                nynorsk { + 
                                    sivilstandBestemtStorBokstav +
                                    (
                                        " din som du forsørgjer har ikkje rett til full alderspensjon frå" +
                                            " folketrygda og har inntekt lågare enn grunnbeløpet "
                                    ).expr() +
                                    grunnbelop.format() +
                                    ".",
                                Language.English to
                                    "Your ".expr() + sivilstandBestemtLitenBokstav +
                                    (
                                        " you support does not have rights to full retirement pension through" +
                                            " the National Insurance Act and has income lower than the basic" +
                                            " amount which is "
                                    ).expr() +
                                    grunnbelop.format() +
                                    "." },
                            )
                        }
                    }
                    showIf(saksbehandlerValg.epsAvkallPaaEgenAlderspenspensjon) {
                        // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                        // SaerSatsBruktEpsGittAvkallAP
                        paragraph {
                            text(
                                bokmal { + 
                                    sivilstandBestemtStorBokstav + " din har gitt avkall på sin alderspensjon fra folketrygden." },
                                nynorsk { + 
                                    sivilstandBestemtStorBokstav + " din har gitt avkall på alderspensjon sin frå folketrygda." },
                                english { + 
                                    "Your " + sivilstandBestemtLitenBokstav +
                                    " has renounced their retirement pension through the National Insurance Act." },
                            )
                        }
                    }
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(saksbehandlerValg.epsAvkallPaaEgenUfoeretrygd) {
                        // SaerSatsBruktEpsGittAvkallUT
                        paragraph {
                            textExpr(
                                Language.Bokmal to sivilstandBestemtStorBokstav +
                                    " din har gitt avkall på sin uføretrygd fra folketrygden.",
                                Language.Nynorsk to sivilstandBestemtStorBokstav +
                                    " din har gitt avkall på uføretrygda si frå folketrygda.",
                                Language.English to
                                    "Your ".expr() + sivilstandBestemtLitenBokstav +
                                    " has renounced their disability benefits through the National Insurance Act.",
                            )
                        }
                    }
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt. (brevet kunne vært delt opp basert på kravårsak.
                    showIf(saksbehandlerValg.epsHarInntektOver1G) {
                        // SaerSatsIkkeBruktEpsInntektOver1G, SaerSatsIkkeBruktEpsRettTilFullAP, SaerSatsIkkeBruktEpsMottarAP, SaerSatsIkkeBruktEpsMottarAfp, SaerSatsIkkeBruktEpsMottarUT
                        paragraph {
                            text(
                                bokmal { + 
                                    "Du får ikke beregnet alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav +
                                    " din har inntekt høyere enn grunnbeløpet (" +
                                    grunnpensjon.format() +
                                    ")." },
                                nynorsk { + 
                                    "Du får ikkje berekna alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav +
                                    " din har inntekt høgare enn grunnbeløpet (" +
                                    grunnpensjon.format() +
                                    ")." },
                                english { + 
                                    "Your retirement pension is not recalculated according to a special rate because your " +
                                    sivilstandBestemtLitenBokstav +
                                    " has a higher income than the basic amount which is " +
                                    grunnpensjon.format() +
                                    "." },
                            )
                        }
                    }
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(saksbehandlerValg.epsHarRettTilFullAlderspensjon) {
                        // SaerSatsIkkeBruktEpsRettTilFullAP
                        paragraph {
                            text(
                                bokmal { + 
                                    "Du får ikke beregnet alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav +
                                    " din har rett til full alderspensjon fra folketrygden." },
                                nynorsk { + 
                                    "Du får ikkje berekna alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav +
                                    " din har rett til full alderspensjon frå folketrygda." },
                                english { + 
                                    "Your retirement pension is not recalculated according to a special rate because your " +
                                    sivilstandBestemtLitenBokstav +
                                    " has rights to full retirement pension through the National Insurance Act." },
                            )
                        }
                    }
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(saksbehandlerValg.epsTarUtAlderspensjon) {
                        // SaerSatsIkkeBruktEpsMottarAP
                        paragraph {
                            text(
                                bokmal { + 
                                    "Du får ikke beregnet alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav +
                                    " din mottar alderspensjon fra folketrygden." },
                                nynorsk { + 
                                    "Du får ikkje berekna alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav +
                                    " din mottar alderspensjon frå folketrygda." },
                                english { + 
                                    "Your retirement pension is not recalculated according to a special rate because your " +
                                    sivilstandBestemtLitenBokstav +
                                    " recieves retirement pension through the National Insurance Act." },
                            )
                        }
                    }
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(saksbehandlerValg.epsTarUtAlderspensjonIStatligSektor) {
                        // SaerSatsIkkeBruktEpsMottarAfp
                        paragraph {
                            text(
                                bokmal { + 
                                    "Du får ikke beregnet alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav +
                                    " din mottar AFP i statlig sektor." },
                                nynorsk { + 
                                    "Du får ikkje berekna alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav +
                                    " din mottar AFP i statleg sektor." },
                                english { + 
                                    "Your retirement pension is not recalculated according to a special rate because your " +
                                    sivilstandBestemtLitenBokstav +
                                    " receives contractual retirement pension from the public sector." },
                            )
                        }
                    }
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(saksbehandlerValg.epsTarUtUfoeretrygd) {
                        // SaerSatsIkkeBruktEpsMottarUT
                        paragraph {
                            text(
                                bokmal { + 
                                    "Du får ikke beregnet alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav +
                                    " din mottar uføretrygd fra folketrygden." },
                                nynorsk { + 
                                    "Du får ikkje berekna alderspensjonen din med særskilt sats fordi " +
                                    sivilstandBestemtLitenBokstav +
                                    " din mottar uføretrygd frå folketrygda." },
                                english { + 
                                    "Your retirement pension is not recalculated according to a special rate because your " +
                                    sivilstandBestemtLitenBokstav +
                                    " receives disability benefits through the National Insurance Act." },
                            )
                        }
                    }
                }

                showIf(saerskiltSatsErBrukt and kravArsakType.isOneOf(KravArsakType.VURDER_SERSKILT_SATS)) {
                    paragraph {
                        text(
                            bokmal { + "Derfor har vi beregnet " },
                            nynorsk { + "Derfor har vi berekna " },
                            english { + "We have therefore recalculated your " },
                        )
                        showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967)) {
                            text(
                                bokmal { + "særtillegget" },
                                nynorsk { + "særtillegget" },
                                english { + "special supplement" },
                            )
                        }.orShowIf(
                            regelverkType.isOneOf(
                                AlderspensjonRegelverkType.AP2011,
                                AlderspensjonRegelverkType.AP2016,
                            ),
                        ) {
                            text(
                                bokmal { + "pensjonstillegget" },
                                nynorsk { + "pensjonstillegget" },
                                english { + "basic pension" },
                            )
                        }
                        showIf(minstenivaaIndividuellInnvilget) {
                            text(
                                bokmal { + " og minstenivåtillegget" },
                                nynorsk { + " og minstenivåtillegget" },
                                english { + " and minimum pension supplement" },
                            )
                        }
                        text(
                            bokmal { + " ditt på nytt med særskilt sats." },
                            nynorsk { + " ditt på nytt med særskilt sats." },
                            english { + " according to a special rate." },
                        )
                    }
                }

                showIf(saerskiltSatsErBrukt) {
                    includePhrase(
                        BetydningForUtbetaling(
                            regelverkType,
                            saksbehandlerValg.beloepEndring,
                        ),
                    )
                }

                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(kravArsakType.isOneOf(KravArsakType.VURDER_SERSKILT_SATS) and saksbehandlerValg.aarligKontrollEPS) {
                    // SaerSatsInfoAarligKontrollEps
                    paragraph {
                        text(
                            bokmal { + 
                                "Fram til " + sivilstandBestemtLitenBokstav +
                                " din fyller 67 år, har vi en årlig kontroll om " +
                                sivilstandBestemtLitenBokstav +
                                " din har rett til full alderpensjon. Du får nytt vedtak hvis dette fører til at alderspensjonen din blir omregnet." },
                            nynorsk { + 
                                "Fram til " + sivilstandBestemtLitenBokstav +
                                " din fyller 67 år, har vi ein årleg kontroll av " +
                                sivilstandBestemtLitenBokstav +
                                " si rett til full alderpensjon. Du får nytt vedtak hvis dette fører til at alderspensjonen din blir omrekna." },
                            english { + 
                                "Until your " + sivilstandBestemtLitenBokstav +
                                " turns 67 years of age, we have an annual control of their rights to a full retirement pension. " +
                                "You will receive a new decision if this results in your retirement pension being recalculated." },
                        )
                    }
                }

                showIf(uforeKombinertMedAlder) {
                    // innvilgelseAPogUTInnledn
                    includePhrase(
                        UfoereAlder.DuFaar(
                            pesysData.beregnetPensjonPerManedVedVirk.totalPensjon,
                            pesysData.kravVirkDatoFom,
                        ),
                    )
                }.orShow {
                    includePhrase(DuFaarAP(kravVirkDatoFom, totalPensjon))
                }

                includePhrase(Utbetalingsinformasjon)

                includePhrase(
                    SivilstandHjemler(
                        regelverkType = regelverkType,
                        kravArsakType = kravArsakType,
                        sivilstand = sivilstand,
                        saertilleggInnvilget = saertilleggInnvilget,
                        pensjonstilleggInnvilget = pensjonstilleggInnvilget,
                        minstenivaaIndividuellInnvilget = minstenivaaIndividuellInnvilget,
                        minstenivaaPensjonistParInnvilget = minstenivaaPensjonistParInnvilget,
                        garantipensjonInnvilget = garantipensjonInnvilget,
                        saerskiltSatsErBrukt = saerskiltSatsErBrukt,
                    ),
                )

                // Selectable - Hvis reduksjon tilbake i tid - feilutbetalingAP
                showIf(saksbehandlerValg.feilutbetaling) {
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    includePhrase(FeilutbetalingAP)
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
                    regelverkType.isNotAnyOf(AlderspensjonRegelverkType.AP1967) and
                        kravArsakType.isNotAnyOf(
                            KravArsakType.INSTOPPHOLD,
                        ),
                ) {
                    includePhrase(
                        ArbeidsinntektOgAlderspensjon(
                            innvilgetFor67 = innvilgetFor67,
                            uttaksgrad = uttaksgrad.ifNull(then = (0)),
                            uforeKombinertMedAlder = uforeKombinertMedAlder,
                        ),
                    )
                }

                includePhrase(InformasjonOmAlderspensjon)
                includePhrase(MeldeFraOmEndringer)
                includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.HarDuSpoersmaal.alder)
            }
            includeAttachment(
                vedleggOrienteringOmRettigheterOgPlikter,
                pesysData.orienteringOmRettigheterOgPlikterDto,
            )
            includeAttachmentIfNotNull(
                vedleggMaanedligPensjonFoerSkatt,
                pesysData.maanedligPensjonFoerSkattDto,
            )
            includeAttachmentIfNotNull(
                vedleggMaanedligPensjonFoerSkattAp2025,
                pesysData.maanedligPensjonFoerSkattAP2025Dto,
            )
        }
}
