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
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Doksys redigermal: MF_000066, tvilling autobrev: MF_000177
@TemplateModelHelpers
object AvslagForLiteTrygdetidAP : RedigerbarTemplate<AvslagForLiteTrygdetidAPDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_FOR_LITE_TRYGDETID
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(ALDER)

    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagForLiteTrygdetidAPDto::class,
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
                Bokmal to "Nav har avslått søknaden din om alderspensjon",
                Nynorsk to "Nav har avslått søknaden din om alderspensjon",
                English to "Nav has declined your application for retirement pension",
            )
        }
        outline {
            showIf(avslagsBegrunnelse.equalTo(UNDER_1_AR_TT) and (erAp2011 or erAp2016)) {
                showIf(erAp2011) {
                    //avslagAP2011Under1aar_001
                    paragraph {
                        text(
                            Bokmal to "For å ha rett til alderspensjon må du ha bodd eller arbeidet i Norge i minst ett år. Det har du ikke, og derfor har vi avslått søknaden din.",
                            Nynorsk to "For å ha rett til alderspensjon må du ha budd eller arbeidd i Noreg i minst eitt år. Det har du ikkje, og derfor har vi avslått søknaden din.",
                            English to "To be eligible for retirement pension, you must have been registered as living or working in Norway for at least one year. You do not meet this requirement, therefore we have declined your application.",
                        )
                    }
                }.orShow {
                    // avslagAP2016Under1aar
                    paragraph {
                        text(
                            Bokmal to "For å ha rett til alderspensjon må du ha bodd eller arbeidet i Norge i minst ett år eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                            Nynorsk to "For å ha rett til alderspensjon må du ha budd eller arbeidd i Noreg i minst eitt år eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                            English to "To be eligible for retirement pension, you must have been registered as living in Norway for at least one year or have had a pensionable income. You do not meet any of these requirements, therefore we have declined your application.",
                        )
                    }
                }
                avslagUnder1aar3aar5aarTT()
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-2",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-2",
                        English to "This decision was made pursuant to the provisions of § 19-2",
                    )
                    showIf(erAp2016) {
                        text(
                            Bokmal to ", 20-5, 20-8, 20-10",
                            Nynorsk to ", 20-5, 20-8, 20-10",
                            English to ", 20-5, 20-8, 20-10",
                        )
                    }
                    showIf(erEOSland) {
                        text(
                            Bokmal to " og EØS-avtalens forordning 883/2004 artikkel 57.",
                            Nynorsk to " og EØS-avtalens forordning 883/2004 artikkel 57.",
                            English to " of the National Insurance Act and Article 57 of Regulation (EC) 883/2004.",
                        )
                    }.orShowIf(erAvtaleland) {
                        textExpr(
                            Bokmal to " og reglene i trygdeavtalen med ".expr() + avtaleland + ".",
                            Nynorsk to " og reglane i trygdeavtalen med ".expr() + avtaleland + ".",
                            English to " of the National Insurance Act and to the rules of the Article of the Social Security Agreement with ".expr()
                                    + avtaleland + ".",
                        )
                    }
                }

            }.orShowIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT, UNDER_5_AR_TT) and erAp2011 or erAp2016) {

                showIf(not(erAvtaleland) and not(erEOSland)) {
                    paragraph {
                        text(
                            Bokmal to "For å ha rett til alderspensjon må du ha minst ",
                            Nynorsk to "For å ha rett til alderspensjon må du ha minst ",
                            English to "To be eligible for retirement pension, you must have at least ",
                        )

                        showIf(avslagsBegrunnelse.equalTo(UNDER_3_AR_TT)) {
                            text(Bokmal to "tre", Nynorsk to "tre", English to "three")
                        }.orShow {
                            text(Bokmal to "fem", Nynorsk to "fem", English to "five")
                        }

                        text(
                            Bokmal to " års trygdetid",
                            Nynorsk to " års trygdetid",
                            English to " years of national insurance coverage",
                        )
                        showIf(erAp2016) {
                            text(
                                Bokmal to ", eller ha tjent opp inntektspensjon",
                                Nynorsk to ", eller ha tent opp inntektspensjon",
                                English to ", or have had a pensionable income",
                            )
                        }
                        text(
                            Bokmal to ". Det har du ikke, og derfor har vi avslått søknaden din.",
                            Nynorsk to ". Det har du ikkje, og derfor har vi avslått søknaden din.",
                            English to ". You do not meet this requirement, therefore we have declined your application.",
                        )
                    }

                    avslagUnder1aar3aar5aarTT()

                    showIf(erAp2011) {
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-2.",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-2.",
                                English to "This decision was made pursuant to the provisions of § 19-2 of the National Insurance Act."
                            )
                        }
                    }.orShow {
                        // avslagAP2016Under5aarHjemmel,  avslagAP2016Under3aarHjemmel
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10.",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10.",
                                English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act.",
                            )
                        }
                    }
                }.orShow {
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("Angi antall") + " måneder opptjeningstid i annet ",
                            Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("Angi antall") + " månader oppteningstid i anna ",
                            English to "We have been informed that you have ".expr() + fritekst("Angi antall") + " months of national insurance coverage in "
                        )
                        includePhrase(EOSogEllerAvtaleland(erEOSland, erAvtaleland))
                        text(
                            Bokmal to ". Den samlede trygdetiden din i Norge og ",
                            Nynorsk to ". Den samla trygdetida din i Noreg og ",
                            English to ". Your total national insurance coverage in Norway and "
                        )
                        includePhrase(EOSogEllerAvtaleland(erEOSland, erAvtaleland))
                        textExpr(
                            Bokmal to " er ".expr() + fritekst("Angi samlet trygdetid") + ".",
                            Nynorsk to " er ".expr() + fritekst("Angi samlet trygdetid") + ".",
                            English to " is ".expr() + fritekst("Angi samlet trygdetid") + " years/months of national insurance coverage."
                        )
                    }

                    showIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT, UNDER_5_AR_TT)) {

                        paragraph {
                            text(
                                Bokmal to "For å ha rett til alderspensjon må du til sammen ha minst ",
                                Nynorsk to "For å ha rett til alderspensjon må du til saman ha minst ",
                                English to "To be eligible for retirement pension, you must have in total at least ",
                            )
                            showIf(avslagsBegrunnelse.equalTo(UNDER_3_AR_TT)) {
                                text(Bokmal to "tre", Nynorsk to "tre", English to "three")
                            }.orShowIf(avslagsBegrunnelse.equalTo(UNDER_5_AR_TT)) {
                                text(Bokmal to "fem", Nynorsk to "fem", English to "five")
                            }
                            text(
                                Bokmal to " års trygdetid i Norge og annet ",
                                Nynorsk to " års trygdetid i Noreg og anna ",
                                English to " years of national insurance coverage in Norway and ",
                            )
                            includePhrase(EOSogEllerAvtaleland(erEOSland, erAvtaleland))

                            showIf(erAp2016) {
                                text(
                                    Bokmal to ", eller ha tjent opp inntektspensjon",
                                    Nynorsk to ", eller ha tent opp inntektspensjon",
                                    English to ", or have had a pensionable income",
                                )
                            }

                            text(
                                Bokmal to ". Det har du ikke, og derfor har vi avslått søknaden din.",
                                Nynorsk to ". Det har du ikkje, og derfor har vi avslått søknaden din.",
                                English to ". You do not meet this requirement, therefore we have declined your application.",
                            )
                        }
                    }
                    showIf(erEOSland and not(erAvtaleland)) {
                        showIf(erAp2011) {
                            paragraph {
                                text(
                                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-2 og EØS-avtalens forordning 883/2004 artikkel 6.",
                                    Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-2 og EØS-avtalens forordning 883/2004 artikkel 6.",
                                    English to "This decision was made pursuant to the provisions of § 19-2 of the National Insurance Act and Article 6 of Regulation (EC) 883/2004.",
                                )
                            }
                        }.orShow {
                            // avslagAP2016Under3aarHjemmelEOS,
                            paragraph {
                                text(
                                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6.",
                                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6.",
                                    English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article 6 of Regulation (EC) 883/2004.",
                                )
                            }
                        }
                    }

                    showIf(erAvtaleland) {
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-2",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-2",
                                English to "This decision was made pursuant to the provision of § 19-2",
                            )

                            showIf(erAp2016) {
                                text(
                                    Bokmal to ", 20-5 til 20-8 og 20-10,",
                                    Nynorsk to ", 20-5 til 20-8 og 20-10,",
                                    English to ", 20-5 til 20-8 og 20-10,",
                                )
                            }
                            textExpr(
                                Bokmal to " og reglene i trygdeavtalen med ".expr() + avtaleland,
                                Nynorsk to " og reglane i trygdeavtalen med ".expr() + avtaleland,
                                English to " of the National Insurance Act and to the provisions of the social security agreement with ".expr() + avtaleland,
                            )

                            showIf(erAvtaleland and erEOSland) {
                                text(
                                    Bokmal to ", og EØS-avtalens forordning 883/2004 artikkel 6",
                                    Nynorsk to ", og EØS-avtalens forordning 883/2004 artikkel 6",
                                    English to ", and Article 6 of regulation (EC) 883/200"
                                )
                            }
                            text(Bokmal to ".", Nynorsk to ".", English to ".")
                        }
                    }
                }
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_20_AR_BO) and erAp2011) {
                //avslagAP2011Under20aar
                paragraph {
                    textExpr(
                        Bokmal to "For å få utbetalt alderspensjonen din når du bor i ".expr() + bostedsland + " må du ha vært medlem i folketrygden i minst 20 år eller ha rett til tilleggspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                        Nynorsk to "For å få utbetalt alderspensjonen din når du bur i ".expr() + bostedsland + " må du ha vært medlem i folketrygda i minst 20 år eller ha rett til tilleggspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                        English to "To be eligible for your retirement pension while living in ".expr() + bostedsland + ", you must have been a member of the Norwegian National Insurance Scheme for at least 20 years. You do not meet this requirement, therefore we have declined your application."
                    )
                }
                //avslagAP2011Under20aarHjemmel
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-3.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-3.",
                        English to "This decision was made pursuant to the provisions of § 19-3 of the National Insurance Act.",
                    )
                }
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_20_AR_BO_2016) and erAp2016) {
                // avslagAP2016Under20aar
                paragraph {
                    textExpr(
                        Bokmal to "For å få utbetalt alderspensjonen din når du bor i ".expr()
                                + bostedsland + " må du enten ha vært medlem i folketrygden i minst 20 år, ha rett til tilleggspensjon eller ha tjent opp inntektspensjon."
                                + " Det har du ikke, og derfor har vi avslått søknaden din.",
                        Nynorsk to "For å få utbetalt alderspensjonen din når du bur i ".expr()
                                + bostedsland + " må du enten ha vært medlem i folketrygda i minst 20 år, ha rett til tilleggspensjon eller ha tent opp inntektspensjon."
                                + " Det har du ikkje, og derfor har vi avslått søknaden din.",
                        English to "To be eligible for your retirement pension while living in ".expr()
                                + bostedsland + ", you must either have been a member of the Norwegian National Insurance Scheme for at least 20 years, be entitled to supplementary pension or be entitled to income-based pension."
                                + " You do not meet any of these requirements, therefore we have declined your application."
                    )
                }
                // avslagAP2016Under20aarHjemmel
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-3, 20-5 til 20-8 og 20-10.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-3, 20-5 til 20-8 og 20-10.",
                        English to "This decision was made pursuant to the provisions of § 19-3, 20-5 to 20-8 and 20-10 of the National Insurance Act.",
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
                            Bokmal to "Trygdetid er perioder du har vært medlem i folketrygden. Som hovedregel er dette perioder du har bodd eller arbeidet i Norge. Trygdetid har betydning for beregning av pensjonen din. Full trygdetid er 40 år.",
                            Nynorsk to "Trygdetid er periodar du har vore medlem i folketrygda. Som hovudregel er dette periodar du har budd eller arbeidd i Noreg. Trygdetid har betydning for berekninga av pensjonen din. Full trygdetid er 40 år.",
                            English to "The period of national insurance coverage is the periods in which you have been a member of the Norwegian National Insurance Scheme. As a general rule, these are periods when you have been registered as living or working in Norway. The period of national insurance coverage affects the calculation of your pension. The full insurance period is 40 years.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Tabellen nedenfor viser perioder vi har brukt for å fastsette din norske trygdetid.",
                            Nynorsk to "Tabellen nedanfor viser periodar vi har brukt for å fastsetje den norske trygdetida di.",
                            English to "The table below shows the time periods used to establish your Norwegian national insurance coverage.",
                        )
                    }

                    includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetid(trygdeperioderNorge))
                }

                showIf(trygdeperioderEOSland.isNotEmpty()) {
                    paragraph {
                        text(
                            Bokmal to "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i EØS-land. Disse periodene er brukt i vurderingen av retten til alderspensjon.",
                            Nynorsk to "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i EØS-land. Desse periodane er brukt i vurderinga av retten til alderspensjon.",
                            English to "The table below shows your national insurance coverage in EEA countries. These periods have been used to assess whether you are eligible for retirement pension."
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
                            Bokmal to "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i land som Norge har trygdeavtale med. Disse periodene er brukt i vurderingen av retten til alderspensjon.",
                            Nynorsk to "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i land som Noreg har trygdeavtale med. Desse periodane er brukt i vurderinga av retten til alderspensjon.",
                            English to "The table below shows your national insurance coverage in countries Norway has a social security agreement. These periods have been used to assess whether you are eligible for retirement pension."
                        )
                    }

                    includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.UtenlandskTrygdetid(trygdeperioderAvtaleland))
                }

            }
            showIf(avslagsBegrunnelse.equalTo(UNDER_62)) {
                paragraph {
                    text(
                        Bokmal to "Du har søkt om å ta ut alderspensjon før du fyller 62 år. For å ha rett til alderspensjon må du være 62 år. Derfor har vi avslått søknaden din.",
                        Nynorsk to "For å ha rett til alderspensjon må du vere 62 år. Du har søkt om å ta ut alderspensjon før du fyller 62 år. Derfor har vi avslått søknaden din.",
                        English to "In order to be eligible for retirement pension you have to be 62 years. You have applied for retirement pension from a date prior to having turned 62. Therefore, we have declined your application.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi har ikke vurdert om du oppfyller de andre kravene for å få alderspensjon fra folketrygden.",
                        Nynorsk to "Vi har ikkje vurdert om du fyller dei andre vilkåra for å få alderspensjon frå folketrygda.",
                        English to "We have not assessed whether you meet the other requirements for retirement pension through the National Insurance Act.",
                    )
                }
                // avslagAP2016UttakAlderU62Hjemmel
                showIf(erAp2016) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-4 og 20-2.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-4 og 20-2.",
                            English to "This decision was made pursuant to the provisions of §§ 19-4 and 20-2 of the National Insurance Act.",
                        )
                    }
                }
                showIf(erAp2025) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven § 20-2.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova § 20-2.",
                            English to "This decision was made pursuant to the provisions of § 20-2 of the National Insurance Act.",
                        )
                    }
                }
                title1 {
                    text(
                        Bokmal to "Du kan selv sjekke når du kan få alderspensjon",
                        Nynorsk to "Du kan sjølv sjekke når du kan få alderspensjon",
                        English to "You can find out when you are eligible for retirement pension",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan ta ut alderspensjon før du fyller 67 år hvis du har hatt høy nok pensjonsopptjening. I Din pensjon på $DIN_PENSJON_URL kan du se hva pensjonen din blir, avhengig av når og hvor mye du tar ut. Slik kan du sjekke når du tidligst kan ta ut alderspensjon.",
                        Nynorsk to "Du kan ta ut alderspensjon før du fyller 67 år dersom du har hatt høg nok pensjonsopptening. I Din pensjon på $DIN_PENSJON_URL kan du sjå kva pensjonen din blir, avhengig av når og kor mykje du tek ut. Slik kan du sjekke når du tidlegast kan ta ut alderspensjon.",
                        English to "You may be eligible for retirement pension before the age of 67, provided your accumulated pension capital is sufficiently high. Log on to Din pensjon at $DIN_PENSJON_URL to find out more about your pension payments. You can also see how your payments change depending on when you start drawing a retirement pension and what percentage of retirement pension you choose."
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.",
                        Nynorsk to "Du må sende oss ein ny søknad når du ønskjer å ta ut alderspensjonen. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden.",
                        English to "You have to submit an application when you want to start drawing your retirement pension. Any change will be implemented at the earliest the month after we have received the application."
                    )
                }
            }
            showIf(pesysData.borINorge and avslagsBegrunnelse.isOneOf(UNDER_1_AR_TT, UNDER_3_AR_TT, UNDER_5_AR_TT)) {
                title1 {
                    text(
                        Bokmal to "Supplerende stønad",
                        Nynorsk to "Supplerande stønad",
                        English to "Supplementary benefit"
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du har kort botid i Norge når du fyller 67 år, kan du søke om supplerende stønad. Du kan lese mer om supplerende stønad på vår nettside $SUPPLERENDE_STOENAD_URL.",
                        Nynorsk to "Dersom du har kort butid i Noreg når du fyller 67 år, kan du søke om supplerande stønad. Du kan lese meir om supplerande stønad på vår nettside $SUPPLERENDE_STOENAD_URL.",
                        English to "If you have only lived a short period in Norway before reaching 67 years of age, you can apply for supplementary benefit. You can read more about supplementary benefit at our website $SUPPLERENDE_STOENAD_URL.",
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
            textExpr(
                Bokmal to "Våre opplysninger viser at du har bodd eller arbeidet i Norge i ".expr()
                        + fritekst("X antall dager/måneder") +
                        ". /Våre opplysninger viser at du ikke har bodd eller arbeidet i Norge.",

                Nynorsk to "Våre opplysningar viser at du har budd eller arbeidd i Noreg i ".expr()
                        + fritekst("X antall dager/måneder") +
                        ". /Våre opplysningar viser at du ikkje har budd eller arbeidd i Noreg.",

                English to "We have registered that you have been living or working in Norway ".expr()
                        + fritekst("X days/months") +
                        ". /We have no record of you living or working in Norway.",
            )
        }
    }

    private data class EOSogEllerAvtaleland(val erEOSland: Expression<Boolean>, val erAvtaleland: Expression<Boolean>): TextOnlyPhrase<LangBokmalNynorskEnglish>(){
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(erEOSland and not(erAvtaleland)) {
                text(
                    Bokmal to "EØS-land",
                    Nynorsk to "EØS-land",
                    English to "an other EEA country",
                )
            }.orShowIf(erAvtaleland and not(erEOSland)) {
                text(
                    Bokmal to "avtaleland",
                    Nynorsk to "avtaleland",
                    English to "an other signatory country",
                )
            }.orShowIf(erEOSland and erAvtaleland) {
                text(
                    Bokmal to "EØS- og avtaleland",
                    Nynorsk to "EØS- og avtaleland",
                    English to "other EEA and signatory countries",
                )
            }

        }

    }
}


