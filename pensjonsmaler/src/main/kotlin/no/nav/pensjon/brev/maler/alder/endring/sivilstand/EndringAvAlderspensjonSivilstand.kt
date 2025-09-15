package no.nav.pensjon.brev.maler.alder.endring.sivilstand

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravArsakType.*
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
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.PesysDataSelectors.vedtakEtterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.beloepEndring
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.endringPensjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.epsInntektOekningReduksjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.etterbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.feilutbetaling
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.fraFlyttet
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.giftBorIkkeSammen
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.institusjonsopphold
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.samboereMedFellesBarn
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.SaksbehandlerValgSelectors.samboereTidligereGift
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.EndringAvAlderspensjonSivilstandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser.*
import no.nav.pensjon.brev.maler.fraser.alderspensjon.*
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
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

            val alderspensjonVedVirk = pesysData.alderspensjonVedVirk
            val garantipensjonInnvilget = alderspensjonVedVirk.garantipensjonInnvilget
            val pensjonstilleggInnvilget = alderspensjonVedVirk.pensjonstilleggInnvilget
            val minstenivaaIndividuellInnvilget = alderspensjonVedVirk.minstenivaaIndividuellInnvilget
            val minstenivaaPensjonistParInnvilget = alderspensjonVedVirk.minstenivaaPensjonsistParInnvilget
            val saertilleggInnvilget = alderspensjonVedVirk.saertilleggInnvilget
            val uforeKombinertMedAlder = alderspensjonVedVirk.ufoereKombinertMedAlder
            val uttaksgrad = alderspensjonVedVirk.uttaksgrad.ifNull(then = (0))
            val innvilgetFor67 = alderspensjonVedVirk.innvilgetFor67

            val epsVedVirk = pesysData.epsVedVirk
            val harInntektOver2G = epsVedVirk.harInntektOver2G
            val mottarPensjon = epsVedVirk.mottarPensjon
            val borSammenMedBruker = epsVedVirk.borSammenMedBruker
            val mottarOmstillingsstonad = epsVedVirk.mottarOmstillingsstonad

            val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()
            val regelverkType = pesysData.regelverkType
            val kravArsakType = pesysData.kravAarsak
            val sivilstand = pesysData.sivilstand

            val grunnpensjon = pesysData.beregnetPensjonPerManedVedVirk.grunnpensjon.ifNull(then = Kroner(0))

            val vedtakEtterbetaling = pesysData.vedtakEtterbetaling
            val epsNavn = fritekst("navn")

            val beloepEndring = saksbehandlerValg.beloepEndring

            title {
                text(
                    bokmal { +"Vi har beregnet alderspensjon din på nytt fra " + kravVirkDatoFom },
                    nynorsk { +"Vi har berekna alderspensjonen din på nytt frå " + kravVirkDatoFom },
                    english { +"We have recalculated your retirement pension from " + kravVirkDatoFom },
                )
            }
            outline {
                includePhrase(Vedtak.Overskrift)

                showIf(kravArsakType.isOneOf(SIVILSTANDSENDRING)) {
                    showIf(sivilstand.isOneOf(MetaforceSivilstand.GIFT) and borSammenMedBruker) {
                        // endringSivilstandGiftUnder2G, endringSisvilstandGiftOver2G, endringSisvilstandGiftYtelse
                        paragraph {
                            text(
                                bokmal { +"Du har giftet deg med " + epsNavn + "," },
                                nynorsk { +"Du har gifta deg med " + epsNavn + "," },
                                english { +"You have married " + epsNavn + "," },
                            )
                            showIf(mottarPensjon) {
                                text(
                                    bokmal { +" som mottar pensjon, uføretrygd eller avtalefestet pensjon." },
                                    nynorsk { +" som får eigen pensjon, uføretrygd eller avtalefesta pensjon." },
                                    english { +" who receives a pension, disability benefit or contractual early retirement pension (AFP)." })
                            }.orShowIf(harInntektOver2G) {
                                text(
                                    bokmal { +" som har en inntekt større enn to ganger grunnbeløpet." },
                                    nynorsk { +" som har ei inntekt større enn to gonger grunnbeløpet." },
                                    english { +" who has an income that is more than twice the national insurance basic amount." },
                                )
                            }.orShow {
                                text(
                                    bokmal { +" som har en inntekt mindre enn to ganger grunnbeløpet." },
                                    nynorsk { +" som har ei inntekt mindre enn to gonger grunnbeløpet." },
                                    english { +" who has an income that is less than twice the national insurance basic amount." },
                                )
                            }
                        }
                    }.orShowIf(sivilstand.isOneOf(MetaforceSivilstand.SAMBOER_3_2)) {
                        // endringSisvilstand3-2samboer
                        paragraph {
                            text(
                                bokmal { +"Du har bodd sammen med " + epsNavn + " i 12 av de siste 18 månedene." },
                                nynorsk { +"Du har budd saman med " + epsNavn + " i 12 av dei siste 18 månadene." },
                                english { +"You have been living with " + epsNavn + " for 12 out of the past 18 months." },
                            )
                        }
                    }.orShowIf(sivilstand.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                        // Radio knapper: Velg type § 1-5 samboer
                        // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.

                        showIf(saksbehandlerValg.samboereMedFellesBarn) {
                            paragraph {
                                // endringSivilstand1-5samboerBarn
                                text(
                                    bokmal { +"Du har flyttet sammen med " + epsNavn + ", og dere har barn sammen." },
                                    nynorsk { +"Du har flytta saman med " + epsNavn + ", og dere har barn saman." },
                                    english { +"You have moved together with " + epsNavn + ", with whom you have children." },
                                )
                            }
                        }
                        // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                        showIf(saksbehandlerValg.samboereTidligereGift) {
                            // endringSivilstand1-5samboerTidlGift
                            paragraph {
                                text(
                                    bokmal { +"Du har flyttet sammen med " + epsNavn + ", og dere har vært gift tidligere." },
                                    nynorsk { +"Du har flytta saman med " + epsNavn + ", og dere har vore gift tidlegare." },
                                    english { +"You have moved together with " + epsNavn + ", with whom you were previously married." },
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
                                    bokmal { +"Samboeren din mottar pensjon, uføretrygd eller avtalefestet pensjon." },
                                    nynorsk { +"Sambuaren din får pensjon, uføretrygd eller avtalefesta pensjon." },
                                    english {
                                        +"Your cohabitant receives a pension, disability benefit or contractual" +
                                                " early retirement pension (AFP)."
                                    },
                                )
                            }
                        }.orShow {
                            paragraph {
                                text(
                                    bokmal {
                                        +"Samboeren din har en inntekt " +
                                                ifElse(
                                                    harInntektOver2G,
                                                    ifTrue = "større",
                                                    ifFalse = "mindre",
                                                ) + " enn to ganger grunnbeløpet."
                                    },
                                    nynorsk {
                                        +"Sambuaren din har ei inntekt " +
                                                ifElse(
                                                    harInntektOver2G,
                                                    ifTrue = "større",
                                                    ifFalse = "mindre",
                                                ) + " enn to gonger grunnbeløpet."
                                    },
                                    english {
                                        +"Your cohabitant has an income that " +
                                                ifElse(
                                                    harInntektOver2G,
                                                    ifTrue = "exceeds",
                                                    ifFalse = "is less than",
                                                ) + " twice the national insurance basic amount."
                                    },
                                )
                            }
                        }
                    }.orShowIf(mottarOmstillingsstonad) {
                        paragraph {
                            text(
                                bokmal { +"Samboeren din mottar omstillingsstønad." },
                                nynorsk { +"Sambuaren din mottek omstillingsstønad." },
                                english { +"Your cohabitant receives adjustment allowance." },
                            )
                        }
                    }
                } // END of kravArsakType.SIVILSTANDSENDRING

                // Radioknapper: Velg endring i EPS inntekt
                // endringInntektOktEPS, endringInntektRedusertEPS
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(kravArsakType.isOneOf(EPS_ENDRET_INNTEKT) and saksbehandlerValg.epsInntektOekningReduksjon) {
                    paragraph {
                        text(
                            bokmal {
                                +"Inntekten til " +
                                        sivilstand.bestemtForm(storBokstav = false) + " din er " +
                                        fritekst("økt/redusert") + "."
                            },
                            nynorsk {
                                +"Inntekta til " + sivilstand.bestemtForm(storBokstav = false) + " din er " +
                                        fritekst("auka/redusert") + "."
                            },
                            english {
                                +"Your " + sivilstand.bestemtForm(storBokstav = false) + "'s income has " +
                                        fritekst("increased/been reduced") + "."
                            },
                        )
                    }
                }

                showIf(
                    kravArsakType.isOneOf(EPS_NY_YTELSE, EPS_NY_YTELSE_UT)
                            and not(mottarOmstillingsstonad),
                ) {
                    includePhrase(InnvilgetYtelseEPS(pesysData.sivilstand))
                }.orShowIf(kravArsakType.isOneOf(TILSTOT_ENDR_YTELSE)) {
                    includePhrase(EndringYtelseEPS(pesysData.sivilstand))
                }.orShowIf(
                    kravArsakType.isOneOf(EPS_OPPH_YTELSE_UT, TILSTOT_OPPHORT)
                            and not(harInntektOver2G),
                ) {
                    includePhrase(OpphoerYtelseEPS(pesysData.sivilstand))
                }.orShowIf(
                    kravArsakType.isOneOf(EPS_OPPH_YTELSE_UT, TILSTOT_OPPHORT)
                            and not(mottarOmstillingsstonad),
                ) {
                    // opphorOmstillingSambo
                    paragraph {
                        text(
                            bokmal { +"Samboeren din mottar ikke lenger omstillingsstønad." },
                            nynorsk { +"Sambuaren din mottek ikkje lenger omstillingsstønad." },
                            english { +"Your cohabitant does not receive adjustment allowance." },
                        )
                    }
                }.orShowIf(
                    kravArsakType.isOneOf(EPS_OPPH_YTELSE_UT, TILSTOT_OPPHORT)
                            and harInntektOver2G,
                ) {
                    includePhrase(OpphoerYtelseEPSOver2G(pesysData.sivilstand))
                }

                // Radioknapper: Hva er årsaken til sivilstandsendringen?
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(
                    kravArsakType.isOneOf(SIVILSTANDSENDRING) and
                            not(borSammenMedBruker) and saksbehandlerValg.fraFlyttet
                ) {
                    // flyttetEPS
                    paragraph {
                        text(
                            bokmal { +"Du og " + epsNavn + " bor ikke lenger sammen." },
                            nynorsk { +"Du og " + epsNavn + "bur ikkje lenger saman." },
                            english { +"You and " + epsNavn + "no longer live together." },
                        )
                    }

                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    showIf(saksbehandlerValg.giftBorIkkeSammen) {
                        // endirngSivilstandGiftBorIkkeSammen
                        paragraph {
                            text(
                                bokmal {
                                    +"Du har giftet deg. Ifølge folkeregisteret er du og ektefellen din" +
                                            " registrert bosatt på ulike adresser."
                                },
                                nynorsk {
                                    +"Du har gifta deg. Ifølgje folkeregisteret er du og ektefellen din" +
                                            " registrert busette på ulike adresser."
                                },
                                english {
                                    +"You have gotten married. According to the national registry you" +
                                            " and your spouse are listed at different residential addresses."
                                },
                            )
                        }
                    }
                }
                // Radioknapper: Alders- og sykehjem eller EPS på annen institusjon
                // endringSykehjem, endringSykehjemEPS, endringSykkehjemBegge, endringInstitusjonEPS
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(kravArsakType.isOneOf(INSTOPPHOLD) and saksbehandlerValg.institusjonsopphold) {
                    paragraph {
                        text(
                            bokmal {
                                +fritekst("Du/Ektefellen/Partneren/Samboeren/Begge") + " din har flyttet på " +
                                        fritekst("sykehjem/institusjon") + "."
                            },
                            nynorsk {
                                +fritekst("Du/Ektefellen/Partnaren/Sambuaren/Begge") + " din har flytta på " +
                                        fritekst("sjukeheim/institusjon ") + "."
                            },
                            english {
                                +fritekst("You/Your spouse/partner/cohabitant have/has") + " moved into " +
                                        fritekst("a nursing home/an institution") + "."
                            },
                        )
                    }
                }

                showIf(
                    regelverkType.isNotAnyOf(AlderspensjonRegelverkType.AP2025)
                            or grunnpensjon.greaterThan(0)
                ) {
                    showIf(
                        (minstenivaaPensjonistParInnvilget or minstenivaaIndividuellInnvilget)
                                and not(saertilleggInnvilget)
                                and not(pensjonstilleggInnvilget),
                    ) {
                        showIf(garantipensjonInnvilget) {
                            // omregningGP_GarantiPen_MNT
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen og garantipensjonen din på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen og garantipensjonen din på nytt." },
                                    english { +"We have therefore recalculated your basic pension and guaranteed pension." },
                                )
                            }
                        }.orShow {
                            // omregningGP_MNT
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen og minstenivåtillegget ditt på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen og minstenivåtillegget ditt på nytt." },
                                    english { +"We have therefore recalculated your basic pension and minimum pension supplement." },
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
                                    bokmal {
                                        +"Derfor har vi beregnet grunnpensjonen, pensjonstillegget," +
                                                " garantipensjonen og minstenivåtillegget ditt på nytt."
                                    },
                                    nynorsk {
                                        +"Derfor har vi berekna grunnpensjonen, pensjonstillegget," +
                                                " garantipensjonen og minstenivåtillegget ditt på nytt."
                                    },
                                    english {
                                        +"We have therefore recalculated your basic pension," +
                                                " supplementary pension, guaranteed pension and minimum pension supplement."
                                    },
                                )
                            }
                        }.orShow {
                            paragraph {
                                text(
                                    bokmal {
                                        +"Derfor har vi beregnet grunnpensjonen, pensjonstillegget" +
                                                " og minstenivåtillegget ditt på nytt."
                                    },
                                    nynorsk {
                                        +"Derfor har vi berekna grunnpensjonen, pensjonstillegget" +
                                                " og minstenivåtillegget ditt på nytt."
                                    },
                                    english {
                                        +"We have therefore recalculated your basic pension," +
                                                " supplementary pension and minimum pension supplement."
                                    },
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
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen og garantipensjonen din på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen og garantipensjonen din på nytt." },
                                    english { +"We have therefore recalculated your basic pension and guaranteed pension." },
                                )
                            }
                        }.orShow {
                            // omregningGP
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen din på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen din på nytt." },
                                    english { +"We have therefore recalculated your basic pension." },
                                )
                            }
                        }
                    }.orShowIf(
                        pensjonstilleggInnvilget
                                and not(saertilleggInnvilget)
                                and not(minstenivaaPensjonistParInnvilget)
                                and not(minstenivaaIndividuellInnvilget),
                    ) {
                        showIf(garantipensjonInnvilget) {
                            // omregningGP_PenT_GarantiPen_MNT
                            paragraph {
                                text(
                                    bokmal {
                                        +"Derfor har vi beregnet grunnpensjonen, pensjonstillegget og garantipensjonen din på nytt."
                                    },
                                    nynorsk {
                                        +"Derfor har vi berekna grunnpensjonen, pensjonstillegget og garantipensjonen din på nytt."
                                    },
                                    english {
                                        +"We have therefore recalculated your basic pension," +
                                                " supplementary pension and guaranteed pension."
                                    },
                                )
                            }
                        }.orShow {
                            // omregningGP_PenT
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen og pensjonstillegget ditt på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen og pensjonstillegget ditt på nytt." },
                                    english { +"We have therefore recalculated your basic pension and pension supplement." },
                                )
                            }
                        }
                    }
                    showIf(
                        regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967) and saertilleggInnvilget,
                    ) {
                        showIf(
                            not(minstenivaaPensjonistParInnvilget) and
                                    not(minstenivaaPensjonistParInnvilget)
                        ) {
                            // omregningGPST
                            paragraph {
                                text(
                                    bokmal { +"Derfor har vi beregnet grunnpensjonen og særtillegget ditt på nytt." },
                                    nynorsk { +"Derfor har vi berekna grunnpensjonen og særtillegget ditt på nytt." },
                                    english { +"We have therefore recalculated your basic pension and the special supplement." },
                                )
                            }
                        }.orShowIf((minstenivaaPensjonistParInnvilget or minstenivaaIndividuellInnvilget)) {
                            // omregningGPSTMNT
                            paragraph {
                                text(
                                    bokmal {
                                        +"Derfor har vi beregnet grunnpensjonen, særtillegget og minstenivåtillegget ditt på nytt."
                                    },
                                    nynorsk {
                                        +"Derfor har vi berekna grunnpensjonen, særtillegget og minstenivåtillegget ditt på nytt."
                                    },
                                    english {
                                        +"We have therefore recalculated your basic pension, the" +
                                                " special supplement and the minimum level supplement."
                                    },
                                )
                            }
                        }
                    }
                }

                includePhrase(OmregningGarantiPen(regelverkType))

                includePhrase(BetydningForUtbetaling(regelverkType, beloepEndring))

                showIf(uforeKombinertMedAlder) {
                    // innvilgelseAPogUTInnledn
                    includePhrase(
                        UfoereAlder.DuFaar(
                            pesysData.beregnetPensjonPerManedVedVirk.totalPensjon,
                            pesysData.kravVirkDatoFom,
                        ),
                    )
                }.orShow {
                    includePhrase(
                        DuFaarAP(
                            pesysData.kravVirkDatoFom,
                            pesysData.beregnetPensjonPerManedVedVirk.totalPensjon
                        )
                    )
                }

                includePhrase(Utbetalingsinformasjon)

                showIf(regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) {
                    // hjemmelSivilstandAP2025
                    paragraph {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 20-9, 20-17 femte avsnitt og 22-12." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 20-9, 20-17 femte avsnitt og 22-12." },
                            english { +
                            "This decision was made pursuant to the provisions of §§ 20-9, 20-17 fifth paragraph, and 22-12 of the National Insurance Act." },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ " },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ " },
                            english { + "This decision was made pursuant to the provisions of §§ " },
                        )
                        showIf(sivilstand.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                            text(
                                bokmal { + "1-5, " },
                                nynorsk { + "1-5, " },
                                english { + "1-5, " },
                            )
                        }
                        text(
                            bokmal { + "3-2" },
                            nynorsk { + "3-2" },
                            english { + "3-2" },
                        )

                        showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967) and saertilleggInnvilget) {
                            text(
                                bokmal { + ", 3-3" },
                                nynorsk { + ", 3-3" },
                                english { + ", 3-3" },
                            )
                        }
                        showIf(pensjonstilleggInnvilget or minstenivaaIndividuellInnvilget or minstenivaaPensjonistParInnvilget) {
                            text(
                                bokmal { + ", 19-8" },
                                nynorsk { + ", 19-8" },
                                english { + ", 19-8" },
                            )
                        }
                        showIf(pensjonstilleggInnvilget) {
                            text(
                                bokmal { + ", 19-9" },
                                nynorsk { + ", 19-9" },
                                english { + ", 19-9" },
                            )
                        }
                        showIf(garantipensjonInnvilget) {
                            text(
                                bokmal { + ", 20-9" },
                                nynorsk { + ", 20-9" },
                                english { + ", 20-9" },
                            )
                        }
                        text(
                            bokmal { + " og 22-12." },
                            nynorsk { + " og 22-12." },
                            english { + " and 22-12." },
                        )
                    }
                }

                // Selectable - Hvis reduksjon tilbake i tid - feilutbetalingAP
                showIf(saksbehandlerValg.feilutbetaling) {
                    includePhrase(FeilutbetalingAP)
                }

                // Hvis endring i pensjonen (Selectable) - skattAPendring
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
                            kravArsakType.isNotAnyOf(INSTOPPHOLD)
                ) {
                    includePhrase(
                        ArbeidsinntektOgAlderspensjon(
                            innvilgetFor67 = innvilgetFor67,
                            uttaksgrad = uttaksgrad.ifNull(0),
                            uforeKombinertMedAlder = uforeKombinertMedAlder,
                        ),
                    )
                }

                includePhrase(InformasjonOmAlderspensjon)
                includePhrase(MeldeFraOmEndringer)
                includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
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
