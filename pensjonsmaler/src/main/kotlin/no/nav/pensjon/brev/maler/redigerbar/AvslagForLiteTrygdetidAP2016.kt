package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.avtalelandNavn
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.bostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.erEOSland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.harAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.vedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Alderspensjon
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagUnder1aarTT
import no.nav.pensjon.brev.maler.fraser.alderspensjon.avslagUnder1aarTT
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Doksys redigermal: MF_000066, tvilling autobrev: MF_000177
@TemplateModelHelpers
object AvslagForLiteTrygdetidAP2016 : RedigerbarTemplate<AvslagForLiteTrygdetidAPDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_FOR_LITE_TRYGDETID_AP2016
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
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val avslagsBegrunnelse = pesysData.vedtaksBegrunnelse
        val erEOSland = pesysData.erEOSland
        val avtalelandNavn = pesysData.avtalelandNavn.ifNull(then = "angi avtaleland")
        val harAvtaleland = pesysData.harAvtaleland
        val bostedsland = pesysData.bostedsland.ifNull(then = "angi bostedsland")

        title {
            text(
                Bokmal to "Nav har avslått søknaden din om alderspensjon",
                Nynorsk to "Nav har avslått søknaden din om alderspensjon",
                English to "Nav has declined your application for retirement pension",
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            showIf(avslagsBegrunnelse.isOneOf(UNDER_1_AR_TT)) {
                // avslagAP2016Under1aar
                paragraph {
                    text(
                        Bokmal to "For å ha rett til alderspensjon må du ha bodd eller arbeidet i Norge i minst ett år eller ha tjent opp inntektspensjon. "
                                + "Det har du ikke, og derfor har vi avslått søknaden din.",
                        Nynorsk to "For å ha rett til alderspensjon må du ha budd eller arbeidd i Noreg i minst eit år eller ha tent opp inntektspensjon. "
                                + "Det har du ikkje, og derfor har vi avslått søknaden din.",
                        English to "To be eligible for retirement pension, you must have been registered as living in Norway for at least one year or have had a pensionable income. "
                                + "You do not meet any of these requirements, therefore we have declined your application.",
                    )
                }
                includePhrase(AvslagUnder1aarTT)

                showIf(erEOSland) {
                    // avslagAP2016Under1aarHjemmelEOS
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8, 20-10 og EØS-avtalens forordning 883/2004 artikkel 57.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8, 20-10 og EØS-avtalens forordning 883/2004 artikkel 57.",
                            English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article 57 of Regulation (EC) 883/2004.",
                        )
                    }
                }.orShow {
                    // avslagAP2016Under1aarHjemmelAvtale
                    paragraph {
                        textExpr(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8, 20-10 og artikkel ".expr()
                                    + fritekst("Legg in aktuelle artikler om sammenlegging og eksport") + " i trygdeavtalen med ".expr()
                                    + avtalelandNavn + ".",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8, 20-10 og artikkel ".expr()
                                    + fritekst("Legg in aktuelle artikler om sammenlegging og eksport") + " i trygdeavtalen med ".expr()
                                    + avtalelandNavn + ".",
                            English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article ".expr()
                                    + fritekst("Legg in aktuelle artikler om sammenlegging og eksport") + " of the Social Security Agreement with ".expr()
                                    + avtalelandNavn + "."
                        )
                    }
                }
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT) and not(harAvtaleland)) {
                // avslagAP2016Under3aar
                paragraph {
                    text(
                        Bokmal to "For å ha rett til alderspensjon må du ha minst tre års trygdetid eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                        Nynorsk to "For å ha rett til alderspensjon må du ha minst tre års trygdetid eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                        English to "To be eligible for retirement pension, you must have at least three years of national insurance coverage or have had a pensionable income. "
                                + "You do not meet these requirements, therefore we have declined your application.",
                    )
                }
                // avslagUnder3aarTT
                paragraph {
                    textExpr(
                        Bokmal to "Våre opplysninger viser at du har bodd eller arbeidet i Norge i ".expr() + fritekst("angi trygdetid") + ". Våre opplysninger viser at du ikke har bodd eller arbeidet i Norge.",
                        Nynorsk to "Våre opplysningar viser at du har budd eller arbeidd i Noreg i ".expr() + fritekst("angi trygdetid") + ". I følgje våre opplysningar har du ikkje budd eller arbeidd i Noreg.",
                        English to "We have registered that you have been living or working in Norway for ".expr() + fritekst(
                            "angi trygdetid"
                        ) + ". We have no record of you living or working in Norway.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10.",
                        English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act.",
                    )
                }
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT)) {
                // avslagUnder3aarTTEOS
                showIf(erEOSland) {
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " måneder opptjeningstid i annet EØS-land. Den samlede trygdetiden din i Norge og annet EØS-land er ".expr()
                                    + fritekst("anggi samlet trygdetid i Norge og avtaleland") + ".",
                            Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " månader oppteningstid i anna EØS-land. Den samla trygdetid di i Noreg og anna EØS-land er ".expr()
                                    + fritekst("angi samlet trygdetid i Norge og avtaleland") + ".",
                            English to "We have been informed that you have ".expr() + fritekst("angi antall") + " months of national insurance coverage in an other EEA country. Your total national insurance coverage in Norway and an other EEA country is ".expr()
                                    + fritekst("angi antall") + ".",
                        )
                    }
                    // avslagAP2016Under3aarEOS
                    paragraph {
                        text(
                            Bokmal to "For å ha rett til alderspensjon må du til sammen ha minst tre års trygdetid i Norge og annet EØS-land, eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                            Nynorsk to "For å ha rett til alderspensjon må du til saman ha minst tre års trygdetid i Noreg og anna EØS-land, eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                            English to "To be eligible for retirement pension, you must have in total at least three years of national insurance coverage in Norway and an other EEA country, or have had a pensionable income. You do not meet any of these requirements, therefore we have declined your application.",
                        )
                    }
                    // avslagAP2016Under3aarHjemmelEOS
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6.",
                            English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article 6 of Regulation (EC) 883/2004",
                        )
                    }
                }.orShow {
                    // avslagUnder3aarTTAvtale
                    paragraph {
                        textExpr(
                            Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " måneder opptjeningstid i annet avtaleland. Den samlede trygdetiden din i Norge og annet avtaleland er ".expr()
                                    + fritekst("angi samlet trygdetid i Norge og avtaleland") + ".",
                            Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " månader oppteningstid i anna avtaleland. Den samla trygdetida din i Noreg og anna avtaleland er ".expr()
                                    + fritekst("angi samlet trygdetid i Norge og avtaleland") + ".",
                            English to "We have been informed that you have ".expr() + fritekst("angi antall") + " months of national insurance coverage in an other signatory country. Your total national insurance coverage in Norway and an other signatory country is ".expr()
                                    + fritekst("angi samlet trygdetid i Norge og avtaleland") + "."
                        )
                    }
                    // avslagAP2016Under3aarAvtale
                    paragraph {
                        text(
                            Bokmal to "For å ha rett til alderspensjon må du til sammen ha minst tre års trygdetid i Norge og annet avtaleland, eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                            Nynorsk to "For å ha rett til alderspensjon må du til saman ha minst tre års trygdetid i Noreg og anna avtaleland, eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                            English to "To be eligible for retirement pension, you must have in total at least three years of national insurance coverage in Norway and an other signatory country, or have had a pensionable income. You do not meet these requirements, therefore we have declined your application.",
                        )
                    }
                    // avslagAP2016Under3aarHjemmelAvtale
                    paragraph {
                        textExpr(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10 og artikkel ".expr() + fritekst(
                                "Legg inn aktuelle artikler om sammenlegging og eksport"
                            ) + " i trygdeavtalen med ".expr() + avtalelandNavn + ".",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10 og artikkel ".expr() + fritekst(
                                "Legg inn aktuelle artikler om sammenlegging og eksport"
                            ) + " i trygdeavtalen med ".expr() + avtalelandNavn + ".",
                            English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article ".expr() + fritekst(
                                "Legg inn aktuelle artikler om sammenlegging og eksport"
                            ) + " of the Social Security Agreement with ".expr() + avtalelandNavn + ".",
                        )
                    }
                }
            }
            showIf(avslagsBegrunnelse.isOneOf(UNDER_20_AR_BO_2016)) {
                // avslagAP2016Under20aar
                paragraph {
                    textExpr(
                        Bokmal to "For å få utbetalt alderspensjonen din når du bor i ".expr() + bostedsland + " må du enten ha vært medlem i folketrygden i minst 20 år, ha rett til tilleggspensjon eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                        Nynorsk to "For å få utbetalt alderspensjonen din når du bur i ".expr() + bostedsland + " må du enten ha vært medlem i folketrygda i minst 20 år, ha rett til tilleggspensjon eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                        English to "To be eligible for your retirement pension while living in ".expr() + bostedsland + ", you must either have been a member of the Norwegian National Insurance Scheme for at least 20 years, be entitled to supplementary pension or be entitled to income-based pension. You do not meet any of these requirements, therefore we have declined your application."
                    )
                }
                // avslagAP2016Under20aarHjemmel
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-3, 20-5 til 20-8 og 20-10.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-3, 20-5 til 20-8 og 20-10.",
                        English to "This decision was made pursuant to the provisions of § 19-3, 20-5 to 20-8 and 20-10 of the National Insurance Act.",
                    )
                }
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_5_AR_TT)) {
                showIf(harAvtaleland) {
                    showIf(erEOSland) {
                        // avslagUnder5aarTTEOS
                        paragraph {
                            textExpr(
                                Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " måneder opptjeningstid i annet EØS-land. Den samlede trygdetiden din i Norge og annet EØS-land er ".expr() + fritekst(
                                    "angi samlet trygdetid i Norge og avtaleland"
                                ) + " år.",
                                Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " månader oppteningstid i anna EØS-land. Den samla trygdetida di i Noreg og anna EØS-land er ".expr() + fritekst(
                                    "angi samlet trygdetid i Norge og avtaleland"
                                ) + " år.",
                                English to "We have been informed that you have ".expr() + fritekst("ang antall") + " months of national insurance coverage in an other EEA country. Your total national insurance coverage in Norway and an other EEA country is ".expr() + fritekst(
                                    "angi samlet trygdetid i Norge og avtaleland"
                                ) + " years."
                            )
                        }
                        // avslagAP2016Under5aarEOS
                        paragraph {
                            text(
                                Bokmal to "For å ha rett til alderspensjon må du til sammen ha minst fem års trygdetid i Norge og annet EØS-land, eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                                Nynorsk to "For å ha rett til alderspensjon må du til saman ha minst fem års trygdetid i Noreg og anna EØS-land, eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                                English to "To be eligible for retirement pension, you must have in total at least five years of national insurance coverage in Norway and an other EEA country, or have had a pensionable income. You do not meet any of these requirements, therefore we have declined your application.",
                            )
                        }
                        // avslagAP2016Under5aarHjemmelEOS
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6.",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6.",
                                English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article 6 of Regulation (EC) 883/2004.",
                            )
                        }
                    }.orShow {
                        // avslagUnder5aarTTAvtale
                        paragraph {
                            textExpr(
                                Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " måneder opptjeningstid i annet avtaleland. Den samlede trygdetiden din i Norge og annet avtaleland er ".expr()
                                        + fritekst("angi samlet trygdetid i Norge og avtaleland") + ".",
                                Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " månader oppteningstid i anna avtaleland. Den samla trygdetida din i Noreg og anna avtaleland er ".expr()
                                        + fritekst("angi samlet trygdetid i Norge og avtaleland") + ".",
                                English to "We have been informed that you have <FRITEKST: angi antall> months of national insurance coverage in an other signatory country. Your total national insurance coverage in Norway and an other signatory country is ".expr()
                                        + fritekst("angi samlet trygdetid i Norge og avtaleland") + "."
                            )
                        }
                        // avslagAP2016Under5aarAvtale
                        paragraph {
                            text(
                                Bokmal to "For å ha rett til alderspensjon må du til sammen ha minst fem års trygdetid i Norge og annet avtaleland, eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                                Nynorsk to "For å ha rett til alderspensjon må du til saman ha minst fem års trygdetid i Noreg og anna avtaleland, eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                                English to "To be eligible for retirement pension, you must have in total at least five years of national insurance coverage in Norway and an other signatory country, or have had a pensionable income. You do not meet these requirements, therefore we have declined your application.",
                            )
                        }
                        // avslagAP2016Under5aarHjemmelAvtaleAuto
                        paragraph {
                            textExpr(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10, og reglene i trygdeavtalen med ".expr()
                                        + avtalelandNavn + ".",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10, og reglane i trygdeavtalen med ".expr()
                                        + avtalelandNavn + ".",
                                English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act, and to the provisions of the social security agreement with ".expr()
                                        + avtalelandNavn + ".",
                            )
                        }
                    }
                }.orShow {
                    // avslagAP2016Under5aar
                    paragraph {
                        text(
                            Bokmal to "For å ha rett til alderspensjon må du ha minst fem års trygdetid eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                            Nynorsk to "For å ha rett til alderspensjon må du ha minst fem års trygdetid eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                            English to "To be eligible for retirement pension, you must have at least five years of national insurance coverage or have had a pensionable income. You do not meet these requirements, therefore we have declined your application.",
                        )
                    }
                    // avslagUnder5aarTT
                    paragraph {
                        textExpr(
                            Bokmal to "Våre opplysninger viser at du har bodd eller arbeidet i Norge i ".expr() + fritekst(
                                "angi antall dager/måneder"
                            ) + ". Vi har ikke registrert at du har bodd eller arbeidet i Norge.",
                            Nynorsk to "Ifølgje våre opplysningar har du budd eller arbeidd i Noreg i ".expr() + fritekst(
                                "angi antall dager/måneder"
                            ) + ". I følgje våre opplysningar har du ikkje budd eller arbeidd i Noreg.",
                            English to "We have registered that you have been living or working in Norway for ".expr() + fritekst(
                                "angi antall dager/måneder"
                            ) + ". We have no record of you living or working in Norway."
                        )
                    }
                    // avslagAP2016Under5aarHjemmel
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10.",
                            English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act",
                        )
                    }
                }
            }
        }
    }
}

