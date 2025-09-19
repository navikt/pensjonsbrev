package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.avtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.borINorge
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.bostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.erEOSland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.trygdeperioderAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.trygdeperioderEOSland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.trygdeperioderNorge
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.vedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SUPPLERENDE_STOENAD_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Doksys redigermal: MF_000066, tvilling autobrev: MF_000177
@TemplateModelHelpers
object AvslagForLiteTrygdetidAP : RedigerbarTemplate<AvslagForLiteTrygdetidAPDto> {

    override val featureToggle = FeatureToggles.avslagForLiteTrygdetidAP.toggle

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_FOR_LITE_TRYGDETID
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(ALDER)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på søknad om alderspensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val avslagsBegrunnelse = pesysData.vedtaksBegrunnelse
        val avtaleland = pesysData.avtaleland.ifNull(fritekst("angi avtaleland"))
        val bostedsland = pesysData.bostedsland.ifNull(fritekst("angi bostedsland"))
        val erAvtaleland = pesysData.avtaleland.notNull()
        val erEOSland = pesysData.erEOSland
        val trygdeperioderAvtaleland = pesysData.trygdeperioderAvtaleland
        val trygdeperioderEOSland = pesysData.trygdeperioderEOSland
        val trygdeperioderNorge = pesysData.trygdeperioderNorge
        val regelverkType = pesysData.regelverkType
        val erAp2011 = regelverkType.equalTo(AP2011)
        val erAp2016 = regelverkType.equalTo(AP2016)
        val erAp2025 = regelverkType.equalTo(AP2025)

        title {
            text(
                bokmal { + "Nav har avslått søknaden din om alderspensjon" },
                nynorsk { + "Nav har avslått søknaden din om alderspensjon" },
                english { + "Nav has declined your application for retirement pension" },
            )
        }
        outline {
            showIf(avslagsBegrunnelse.equalTo(UNDER_1_AR_TT) and (erAp2011 or erAp2016)) {
                showIf(erAp2011) {
                    //avslagAP2011Under1aar_001
                    paragraph {
                        text(
                            bokmal { + "For å ha rett til alderspensjon må du ha bodd eller arbeidet i Norge i minst ett år. Det har du ikke, og derfor har vi avslått søknaden din." },
                            nynorsk { + "For å ha rett til alderspensjon må du ha budd eller arbeidd i Noreg i minst eitt år. Det har du ikkje, og derfor har vi avslått søknaden din." },
                            english { + "To be eligible for retirement pension, you must have been registered as living or working in Norway for at least one year. You do not meet this requirement, therefore we have declined your application." },
                        )
                    }
                }.orShow {
                    // avslagAP2016Under1aar
                    paragraph {
                        text(
                            bokmal { + "For å ha rett til alderspensjon må du ha bodd eller arbeidet i Norge i minst ett år eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din." },
                            nynorsk { + "For å ha rett til alderspensjon må du ha budd eller arbeidd i Noreg i minst eitt år eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din." },
                            english { + "To be eligible for retirement pension, you must have been registered as living in Norway for at least one year or have had a pensionable income. You do not meet any of these requirements, therefore we have declined your application." },
                        )
                    }
                }
                avslagUnder1aar3aar5aarTT()
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven § 19-2" },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova § 19-2" },
                        english { + "This decision was made pursuant to the provisions of § 19-2" },
                    )
                    showIf(erAp2016) {
                        text(
                            bokmal { + ", 20-5, 20-8, 20-10" },
                            nynorsk { + ", 20-5, 20-8, 20-10" },
                            english { + ", 20-5, 20-8, 20-10" },
                        )
                    }
                    showIf(erEOSland) {
                        text(
                            bokmal { + " og EØS-avtalens forordning 883/2004 artikkel 57." },
                            nynorsk { + " og EØS-avtalens forordning 883/2004 artikkel 57." },
                            english { + " of the National Insurance Act and Article 57 of Regulation (EC) 883/2004." },
                        )
                    }.orShowIf(erAvtaleland) {
                        text(
                            bokmal { + " og reglene i trygdeavtalen med " + avtaleland + "." },
                            nynorsk { + " og reglane i trygdeavtalen med " + avtaleland + "." },
                            english { + " of the National Insurance Act and to the rules of the Article of the Social Security Agreement with "
                                    + avtaleland + "." },
                        )
                    }
                }

            }.orShowIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT, UNDER_5_AR_TT) and erAp2011 or erAp2016) {

                showIf(not(erAvtaleland) and not(erEOSland)) {
                    paragraph {
                        text(
                            bokmal { + "For å ha rett til alderspensjon må du ha minst " },
                            nynorsk { + "For å ha rett til alderspensjon må du ha minst " },
                            english { + "To be eligible for retirement pension, you must have at least " },
                        )

                        showIf(avslagsBegrunnelse.equalTo(UNDER_3_AR_TT)) {
                            text(bokmal { + "tre" }, nynorsk { + "tre" }, english { + "three" })
                        }.orShow {
                            text(bokmal { + "fem" }, nynorsk { + "fem" }, english { + "five" })
                        }

                        text(
                            bokmal { + " års trygdetid" },
                            nynorsk { + " års trygdetid" },
                            english { + " years of national insurance coverage" },
                        )
                        showIf(erAp2016) {
                            text(
                                bokmal { + ", eller ha tjent opp inntektspensjon" },
                                nynorsk { + ", eller ha tent opp inntektspensjon" },
                                english { + ", or have had a pensionable income" },
                            )
                        }
                        text(
                            bokmal { + ". Det har du ikke, og derfor har vi avslått søknaden din." },
                            nynorsk { + ". Det har du ikkje, og derfor har vi avslått søknaden din." },
                            english { + ". You do not meet this requirement, therefore we have declined your application." },
                        )
                    }

                    avslagUnder1aar3aar5aarTT()

                    showIf(erAp2011) {
                        paragraph {
                            text(
                                bokmal { + "Vedtaket er gjort etter folketrygdloven § 19-2." },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova § 19-2." },
                                english { + "This decision was made pursuant to the provisions of § 19-2 of the National Insurance Act." }
                            )
                        }
                    }.orShow {
                        // avslagAP2016Under5aarHjemmel,  avslagAP2016Under3aarHjemmel
                        paragraph {
                            text(
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10." },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10." },
                                english { + "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act." },
                            )
                        }
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { + "Vi har fått opplyst at du har " + fritekst("Angi antall") + " måneder opptjeningstid i annet " },
                            nynorsk { + "Vi har fått opplyst at du har " + fritekst("Angi antall") + " månader oppteningstid i anna " },
                            english { + "We have been informed that you have " + fritekst("Angi antall") + " months of national insurance coverage in " }
                        )
                        includePhrase(EOSogEllerAvtaleland(erEOSland, erAvtaleland))
                        text(
                            bokmal { + ". Den samlede trygdetiden din i Norge og " },
                            nynorsk { + ". Den samla trygdetida din i Noreg og " },
                            english { + ". Your total national insurance coverage in Norway and " }
                        )
                        includePhrase(EOSogEllerAvtaleland(erEOSland, erAvtaleland))
                        text(
                            bokmal { + " er " + fritekst("Angi samlet trygdetid") + "." },
                            nynorsk { + " er " + fritekst("Angi samlet trygdetid") + "." },
                            english { + " is " + fritekst("Angi samlet trygdetid") + " years/months of national insurance coverage." }
                        )
                    }

                    showIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT, UNDER_5_AR_TT)) {

                        paragraph {
                            text(
                                bokmal { + "For å ha rett til alderspensjon må du til sammen ha minst " },
                                nynorsk { + "For å ha rett til alderspensjon må du til saman ha minst " },
                                english { + "To be eligible for retirement pension, you must have in total at least " },
                            )
                            showIf(avslagsBegrunnelse.equalTo(UNDER_3_AR_TT)) {
                                text(bokmal { + "tre" }, nynorsk { + "tre" }, english { + "three" })
                            }.orShowIf(avslagsBegrunnelse.equalTo(UNDER_5_AR_TT)) {
                                text(bokmal { + "fem" }, nynorsk { + "fem" }, english { + "five" })
                            }
                            text(
                                bokmal { + " års trygdetid i Norge og annet " },
                                nynorsk { + " års trygdetid i Noreg og anna " },
                                english { + " years of national insurance coverage in Norway and " },
                            )
                            includePhrase(EOSogEllerAvtaleland(erEOSland, erAvtaleland))

                            showIf(erAp2016) {
                                text(
                                    bokmal { + ", eller ha tjent opp inntektspensjon" },
                                    nynorsk { + ", eller ha tent opp inntektspensjon" },
                                    english { + ", or have had a pensionable income" },
                                )
                            }

                            text(
                                bokmal { + ". Det har du ikke, og derfor har vi avslått søknaden din." },
                                nynorsk { + ". Det har du ikkje, og derfor har vi avslått søknaden din." },
                                english { + ". You do not meet this requirement, therefore we have declined your application." },
                            )
                        }
                    }
                    showIf(erEOSland and not(erAvtaleland)) {
                        showIf(erAp2011) {
                            paragraph {
                                text(
                                    bokmal { + "Vedtaket er gjort etter folketrygdloven § 19-2 og EØS-avtalens forordning 883/2004 artikkel 6." },
                                    nynorsk { + "Vedtaket er gjort etter folketrygdlova § 19-2 og EØS-avtalens forordning 883/2004 artikkel 6." },
                                    english { + "This decision was made pursuant to the provisions of § 19-2 of the National Insurance Act and Article 6 of Regulation (EC) 883/2004." },
                                )
                            }
                        }.orShow {
                            // avslagAP2016Under3aarHjemmelEOS,
                            paragraph {
                                text(
                                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6." },
                                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6." },
                                    english { + "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article 6 of Regulation (EC) 883/2004." },
                                )
                            }
                        }
                    }

                    showIf(erAvtaleland) {
                        paragraph {
                            text(
                                bokmal { + "Vedtaket er gjort etter folketrygdloven § 19-2" },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova § 19-2" },
                                english { + "This decision was made pursuant to the provision of § 19-2" },
                            )

                            showIf(erAp2016) {
                                text(
                                    bokmal { + ", 20-5 til 20-8 og 20-10," },
                                    nynorsk { + ", 20-5 til 20-8 og 20-10," },
                                    english { + ", 20-5 til 20-8 og 20-10," },
                                )
                            }
                            showIf( erEOSland) {
                                text(
                                    bokmal { + ", og EØS-avtalens forordning 883/2004 artikkel 6" },
                                    nynorsk { + ", og EØS-avtalens forordning 883/2004 artikkel 6" },
                                    english { + ", and Article 6 of regulation (EC) 883/200" }
                                )
                            }.orShow {
                                text(
                                    bokmal { + " og reglene i trygdeavtalen med " + avtaleland },
                                    nynorsk { + " og reglane i trygdeavtalen med " + avtaleland },
                                    english { + " of the National Insurance Act and to the provisions of the social security agreement with " + avtaleland },
                                )
                            }
                            text(bokmal { + "." }, nynorsk { + "." }, english { + "." })
                        }
                    }
                }
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_20_AR_BO) and erAp2011) {
                //avslagAP2011Under20aar
                paragraph {
                    text(
                        bokmal { + "For å få utbetalt alderspensjonen din når du bor i " + bostedsland + " må du ha vært medlem i folketrygden i minst 20 år eller ha rett til tilleggspensjon. Det har du ikke, og derfor har vi avslått søknaden din." },
                        nynorsk { + "For å få utbetalt alderspensjonen din når du bur i " + bostedsland + " må du ha vært medlem i folketrygda i minst 20 år eller ha rett til tilleggspensjon. Det har du ikkje, og derfor har vi avslått søknaden din." },
                        english { + "To be eligible for your retirement pension while living in " + bostedsland + ", you must have been a member of the Norwegian National Insurance Scheme for at least 20 years. You do not meet this requirement, therefore we have declined your application." }
                    )
                }
                //avslagAP2011Under20aarHjemmel
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven § 19-3." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova § 19-3." },
                        english { + "This decision was made pursuant to the provisions of § 19-3 of the National Insurance Act." },
                    )
                }
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_20_AR_BO_2016) and erAp2016) {
                // avslagAP2016Under20aar
                paragraph {
                    text(
                        bokmal { + "For å få utbetalt alderspensjonen din når du bor i "
                                + bostedsland + " må du enten ha vært medlem i folketrygden i minst 20 år, ha rett til tilleggspensjon eller ha tjent opp inntektspensjon."
                                + " Det har du ikke, og derfor har vi avslått søknaden din." },
                        nynorsk { + "For å få utbetalt alderspensjonen din når du bur i "
                                + bostedsland + " må du enten ha vært medlem i folketrygda i minst 20 år, ha rett til tilleggspensjon eller ha tent opp inntektspensjon."
                                + " Det har du ikkje, og derfor har vi avslått søknaden din." },
                        english { + "To be eligible for your retirement pension while living in "
                                + bostedsland + ", you must either have been a member of the Norwegian National Insurance Scheme for at least 20 years, be entitled to supplementary pension or be entitled to income-based pension."
                                + " You do not meet any of these requirements, therefore we have declined your application." }
                    )
                }
                // avslagAP2016Under20aarHjemmel
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven § 19-3, 20-5 til 20-8 og 20-10." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova § 19-3, 20-5 til 20-8 og 20-10." },
                        english { + "This decision was made pursuant to the provisions of § 19-3, 20-5 to 20-8 and 20-10 of the National Insurance Act." },
                    )
                }
            }
            showIf(
                avslagsBegrunnelse.isOneOf(
                    UNDER_20_AR_BO,
                    UNDER_20_AR_BO_2016,
                    UNDER_5_AR_TT,
                    UNDER_3_AR_TT,
                    UNDER_1_AR_TT
                )
            ) {
                showIf(trygdeperioderNorge.isNotEmpty()) {
                    //trygdetidOverskrift
                    includePhrase(Vedtak.TrygdetidOverskrift)

                    // TODO skal vi ha denne?, isåfall, hvorfor ikke EØS og avtaleland varianten?
                    //norskTTInfoGenerell_001
                    paragraph {
                        text(
                            bokmal { + "Trygdetid er perioder du har vært medlem i folketrygden. Som hovedregel er dette perioder du har bodd eller arbeidet i Norge. Trygdetid har betydning for beregning av pensjonen din. Full trygdetid er 40 år." },
                            nynorsk { + "Trygdetid er periodar du har vore medlem i folketrygda. Som hovudregel er dette periodar du har budd eller arbeidd i Noreg. Trygdetid har betydning for berekninga av pensjonen din. Full trygdetid er 40 år." },
                            english { + "The period of national insurance coverage is the periods in which you have been a member of the Norwegian National Insurance Scheme. As a general rule, these are periods when you have been registered as living or working in Norway. The period of national insurance coverage affects the calculation of your pension. The full insurance period is 40 years." },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "Tabellen nedenfor viser perioder vi har brukt for å fastsette din norske trygdetid." },
                            nynorsk { + "Tabellen nedanfor viser periodar vi har brukt for å fastsetje den norske trygdetida di." },
                            english { + "The table below shows the time periods used to establish your Norwegian national insurance coverage." },
                        )
                    }

                    includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetid(trygdeperioderNorge))
                }

                showIf(trygdeperioderEOSland.isNotEmpty()) {
                    paragraph {
                        text(
                            bokmal { + "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i EØS-land. Disse periodene er brukt i vurderingen av retten til alderspensjon." },
                            nynorsk { + "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i EØS-land. Desse periodane er brukt i vurderinga av retten til alderspensjon." },
                            english { + "The table below shows your national insurance coverage in EEA countries. These periods have been used to assess whether you are eligible for retirement pension." }
                        )
                    }

                    includePhrase(
                        OpplysningerBruktIBeregningenTrygdetidTabeller.UtenlandskTrygdetid(
                            trygdeperioderEOSland
                        )
                    )

                }

                showIf(trygdeperioderAvtaleland.isNotEmpty()) {
                    paragraph {
                        text(
                            bokmal { + "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i land som Norge har trygdeavtale med. Disse periodene er brukt i vurderingen av retten til alderspensjon." },
                            nynorsk { + "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i land som Noreg har trygdeavtale med. Desse periodane er brukt i vurderinga av retten til alderspensjon." },
                            english { + "The table below shows your national insurance coverage in countries Norway has a social security agreement. These periods have been used to assess whether you are eligible for retirement pension." }
                        )
                    }

                    includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.UtenlandskTrygdetid(trygdeperioderAvtaleland))
                }

            }
            showIf(avslagsBegrunnelse.equalTo(UNDER_62)) {
                paragraph {
                    text(
                        bokmal { + "Du har søkt om å ta ut alderspensjon før du fyller 62 år. For å ha rett til alderspensjon må du være 62 år. Derfor har vi avslått søknaden din." },
                        nynorsk { + "For å ha rett til alderspensjon må du vere 62 år. Du har søkt om å ta ut alderspensjon før du fyller 62 år. Derfor har vi avslått søknaden din." },
                        english { + "In order to be eligible for retirement pension you have to be 62 years. You have applied for retirement pension from a date prior to having turned 62. Therefore, we have declined your application." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vi har ikke vurdert om du oppfyller de andre kravene for å få alderspensjon fra folketrygden." },
                        nynorsk { + "Vi har ikkje vurdert om du fyller dei andre vilkåra for å få alderspensjon frå folketrygda." },
                        english { + "We have not assessed whether you meet the other requirements for retirement pension through the National Insurance Act." },
                    )
                }
                // avslagAP2016UttakAlderU62Hjemmel
                showIf(erAp2016) {
                    paragraph {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-4 og 20-2." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-4 og 20-2." },
                            english { + "This decision was made pursuant to the provisions of §§ 19-4 and 20-2 of the National Insurance Act." },
                        )
                    }
                }
                showIf(erAp2025) {
                    paragraph {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven § 20-2." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova § 20-2." },
                            english { + "This decision was made pursuant to the provisions of § 20-2 of the National Insurance Act." },
                        )
                    }
                }
                title1 {
                    text(
                        bokmal { + "Du kan selv sjekke når du kan få alderspensjon" },
                        nynorsk { + "Du kan sjølv sjekke når du kan få alderspensjon" },
                        english { + "You can find out when you are eligible for retirement pension" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Du kan ta ut alderspensjon før du fyller 67 år hvis du har hatt høy nok pensjonsopptjening. I Din pensjon på $DIN_PENSJON_URL kan du se hva pensjonen din blir, avhengig av når og hvor mye du tar ut. Slik kan du sjekke når du tidligst kan ta ut alderspensjon." },
                        nynorsk { + "Du kan ta ut alderspensjon før du fyller 67 år dersom du har hatt høg nok pensjonsopptening. I Din pensjon på $DIN_PENSJON_URL kan du sjå kva pensjonen din blir, avhengig av når og kor mykje du tek ut. Slik kan du sjekke når du tidlegast kan ta ut alderspensjon." },
                        english { + "You may be eligible for retirement pension before the age of 67, provided your accumulated pension capital is sufficiently high. Log on to Din pensjon at $DIN_PENSJON_URL to find out more about your pension payments. You can also see how your payments change depending on when you start drawing a retirement pension and what percentage of retirement pension you choose." }
                    )
                }

                paragraph {
                    text(
                        bokmal { + "Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden." },
                        nynorsk { + "Du må sende oss ein ny søknad når du ønskjer å ta ut alderspensjonen. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden." },
                        english { + "You have to submit an application when you want to start drawing your retirement pension. Any change will be implemented at the earliest the month after we have received the application." }
                    )
                }
            }
            showIf(pesysData.borINorge and avslagsBegrunnelse.isOneOf(UNDER_1_AR_TT, UNDER_3_AR_TT, UNDER_5_AR_TT)) {
                title1 {
                    text(
                        bokmal { + "Supplerende stønad" },
                        nynorsk { + "Supplerande stønad" },
                        english { + "Supplementary benefit" }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Hvis du har kort botid i Norge når du fyller 67 år, kan du søke om supplerende stønad. Du kan lese mer om supplerende stønad på vår nettside $SUPPLERENDE_STOENAD_URL." },
                        nynorsk { + "Dersom du har kort butid i Noreg når du fyller 67 år, kan du søke om supplerande stønad. Du kan lese meir om supplerande stønad på vår nettside $SUPPLERENDE_STOENAD_URL." },
                        english { + "If you have only lived a short period in Norway before reaching 67 years of age, you can apply for supplementary benefit. You can read more about supplementary benefit at our website $SUPPLERENDE_STOENAD_URL." },
                    )
                }
            }

            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(
            vedleggDineRettigheterOgMulighetTilAaKlage,
            pesysData.dineRettigheterOgMulighetTilAaKlageDto
        )
    }

    private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, AvslagForLiteTrygdetidAPDto>.avslagUnder1aar3aar5aarTT() {
        paragraph {
            text(
                bokmal { + "Våre opplysninger viser at du har bodd eller arbeidet i Norge i "
                        + fritekst("X antall dager/måneder") +
                        ". /Våre opplysninger viser at du ikke har bodd eller arbeidet i Norge." },

                nynorsk { + "Våre opplysningar viser at du har budd eller arbeidd i Noreg i "
                        + fritekst("X antall dager/måneder") +
                        ". /Våre opplysningar viser at du ikkje har budd eller arbeidd i Noreg." },

                english { + "We have registered that you have been living or working in Norway "
                        + fritekst("X days/months") +
                        ". /We have no record of you living or working in Norway." },
            )
        }
    }

    private data class EOSogEllerAvtaleland(val erEOSland: Expression<Boolean>, val erAvtaleland: Expression<Boolean>): TextOnlyPhrase<LangBokmalNynorskEnglish>(){
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(erEOSland and not(erAvtaleland)) {
                text(
                    bokmal { + "EØS-land" },
                    nynorsk { + "EØS-land" },
                    english { + "an other EEA country" },
                )
            }.orShowIf(erAvtaleland and not(erEOSland)) {
                text(
                    bokmal { + "avtaleland" },
                    nynorsk { + "avtaleland" },
                    english { + "an other signatory country" },
                )
            }.orShowIf(erEOSland and erAvtaleland) {
                text(
                    bokmal { + "EØS- og avtaleland" },
                    nynorsk { + "EØS- og avtaleland" },
                    english { + "other EEA and signatory countries" },
                )
            }

        }

    }
}


