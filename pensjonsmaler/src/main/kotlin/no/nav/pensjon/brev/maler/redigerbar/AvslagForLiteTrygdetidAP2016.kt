package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.avtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.bostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.erAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.erEOSland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.vedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagUnder1aarTT
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagUnder3aarTT
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SupplerendeStoenad
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
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
        val avtaleland = pesysData.avtaleland
        val erAvtaleland = pesysData.erAvtaleland
        val bostedsland = pesysData.bostedsland

        title {
            text(
                Bokmal to "Nav har avslått søknaden din om alderspensjon",
                Nynorsk to "Nav har avslått søknaden din om alderspensjon",
                English to "Nav has declined your application for retirement pension",
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            showIf(avslagsBegrunnelse.isNotAnyOf(UNDER_62)) {
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

                    // avslagAP2016Under1aarHjemmelAvtale, avslagAP2016Under1aarHjemmelEOS
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova",
                            English to "This decision was made pursuant to the provisions of",
                        )
                        showIf(erEOSland) {
                            text(
                                Bokmal to " §§ 19-2, 20-5 til 20-8, 20-10 og EØS-avtalens forordning 883/2004 artikkel 57.",
                                Nynorsk to " §§ 19-2, 20-5 til 20-8, 20-10 og EØS-avtalens forordning 883/2004 artikkel 57.",
                                English to "§§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article 57 of Regulation (EC) 883/2004.",
                            )
                        }.orShow {
                            textExpr(
                                Bokmal to " §§ 19-2, 20-5 til 20-8, 20-10 og artikkel ".expr()
                                        + fritekst("Legg inn aktuelle artikler om sammenlegging og eksport") + " i trygdeavtalen med ".expr()
                                        + avtaleland + ".",
                                Nynorsk to " §§ 19-2, 20-5 til 20-8, 20-10 og artikkel ".expr()
                                        + fritekst("Legg inn aktuelle artikler om sammenlegging og eksport") + " i trygdeavtalen med ".expr()
                                        + avtaleland + ".",
                                English to " §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article ".expr()
                                        + fritekst("Legg inn aktuelle artikler om sammenlegging og eksport") + " of the Social Security Agreement with ".expr()
                                        + avtaleland + "."
                            )
                        }
                    }
                }

                showIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT)) {
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
                        // avslagAP2016Under3aar
                        paragraph {
                            text(
                                Bokmal to "For å ha rett til alderspensjon må du ha minst tre års trygdetid eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                                Nynorsk to "For å ha rett til alderspensjon må du ha minst tre års trygdetid eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                                English to "To be eligible for retirement pension, you must have at least three years of national insurance coverage or have had a pensionable income. "
                                        + "You do not meet these requirements, therefore we have declined your application.",
                            )
                        }
                        includePhrase(AvslagUnder3aarTT)
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10.",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10.",
                                English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act.",
                            )
                        }
                    }
                }

                showIf(avslagsBegrunnelse.isOneOf(UNDER_5_AR_TT)) {
                    showIf(erEOSland) {
                        // avslagUnder5aarTTEOS
                        paragraph {
                            textExpr(
                                Bokmal to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " måneder opptjeningstid i annet EØS-land. Den samlede trygdetiden din i Norge og annet EØS-land er ".expr()
                                        + fritekst("angi samlet trygdetid i Norge og EØS-land") + ".",
                                Nynorsk to "Vi har fått opplyst at du har ".expr() + fritekst("angi antall") + " månader oppteningstid i anna avtaleland. Den samla trygdetida din i Noreg og anna avtaleland er ".expr()
                                        + fritekst("angi samlet trygdetid i Norge og EØS-land") + ".",
                                English to "We have been informed that you have <FRITEKST: angi antall> months of national insurance coverage in an other EEA country. Your total national insurance coverage in Norway and an other EEA country is ".expr()
                                        + fritekst("angi samlet trygdetid i Norge og EEA-country") + "."
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
                    }.orShowIf(erAvtaleland and not(erEOSland)) {
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
                                        + avtaleland + ".",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10, og reglane i trygdeavtalen med ".expr()
                                        + avtaleland + ".",
                                English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act, and to the provisions of the social security agreement with ".expr()
                                        + avtaleland + ".",
                            )
                        }
                    }.orShow {
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
                        // avslagAP2016Under5aar
                        paragraph {
                            text(
                                Bokmal to "For å ha rett til alderspensjon må du til sammen ha minst fem års trygdetid eller ha tjent opp inntektspensjon. Det har du ikke, og derfor har vi avslått søknaden din.",
                                Nynorsk to "For å ha rett til alderspensjon må du til saman ha minst fem års trygdetid eller ha tent opp inntektspensjon. Det har du ikkje, og derfor har vi avslått søknaden din.",
                                English to "To be eligible for retirement pension, you must have in total at least five years of national insurance coverage or have had a pensionable income. You do not meet these requirements, therefore we have declined your application.",
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

                showIf(avslagsBegrunnelse.isOneOf(UNDER_20_AR_BO_2016)) {
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
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-3, 20-5 til 20-8 og 20-10.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-3, 20-5 til 20-8 og 20-10.",
                            English to "This decision was made pursuant to the provisions of § 19-3, 20-5 to 20-8 and 20-10 of the National Insurance Act.",
                        )
                    }
                }
            }.orShowIf(avslagsBegrunnelse.isOneOf(UNDER_62)) {
                // avslagAPUttakAlderU62Begrunn
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
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-4 og 20-2.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-4 og 20-2.",
                        English to "This decision was made pursuant to the provisions of §§ 19-4 and 20-2 of the National Insurance Act.",
                    )
                }
                // avslagAPUttakAlderU62Info
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
                // nySoknadAPInfo
                paragraph {
                    text(
                        Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.2",
                        Nynorsk to "Du må sende oss ein ny søknad når du ønskjer å ta ut alderspensjonen. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden.",
                        English to "You have to submit an application when you want to start drawing your retirement pension. Any change will be implemented at the earliest the month after we have received the application."
                    )
                }
                includePhrase(SupplerendeStoenad)
                includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
                includePhrase(Felles.HarDuSpoersmaal.alder)
            }
        }
    }
}

