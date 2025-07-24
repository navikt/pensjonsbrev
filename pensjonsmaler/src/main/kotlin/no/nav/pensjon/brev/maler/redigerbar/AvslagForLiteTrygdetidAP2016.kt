package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016Dto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016DtoSelectors.PesysDataSelectors.avtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016DtoSelectors.PesysDataSelectors.bostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016DtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016DtoSelectors.PesysDataSelectors.erAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016DtoSelectors.PesysDataSelectors.erEOSland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016DtoSelectors.PesysDataSelectors.trygdeperioderAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016DtoSelectors.PesysDataSelectors.trygdeperioderEOSland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016DtoSelectors.PesysDataSelectors.trygdeperioderNorge
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016DtoSelectors.PesysDataSelectors.vedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAP2016DtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagForLiteTrygdetidAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagForLiteTrygdetidAP.TrygdeperioderAvtalelandTabell
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagForLiteTrygdetidAP.TrygdeperioderEOSlandTabell
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagForLiteTrygdetidAP.TrygdeperioderNorgeTabell
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Doksys redigermal: MF_000066, tvilling autobrev: MF_000177
@TemplateModelHelpers

object AvslagForLiteTrygdetidAP2016 : RedigerbarTemplate<AvslagForLiteTrygdetidAP2016Dto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_FOR_LITE_TRYGDETID_AP2016
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val template = createTemplate(
        name = kode.name,
        letterDataType = AvslagForLiteTrygdetidAP2016Dto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på søknad om alderspensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val avslagsBegrunnelse = pesysData.vedtaksBegrunnelse
        val avtaleland = pesysData.avtaleland
        val bostedsland = pesysData.bostedsland
        val erAvtaleland = pesysData.erAvtaleland
        val erEOSland = pesysData.erEOSland
        val trygdeperioderAvtaleland = pesysData.trygdeperioderAvtaleland
        val trygdeperioderEOSland = pesysData.trygdeperioderEOSland
        val trygdeperioderNorge = pesysData.trygdeperioderNorge
        val regelverkType = AP2016.expr()

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
                    includePhrase(AvslagForLiteTrygdetidAP.AvslagUnder1aar3aar5aarTT)
                    includePhrase(
                        AvslagForLiteTrygdetidAP.AvslagUnder1aarHjemmel(
                            avtaleland = avtaleland,
                            erEOSland = erEOSland,
                            regelverkType = regelverkType
                        )
                    )

                }.orShowIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT)) {

                    showIf(not(erAvtaleland) and not(erEOSland)) { // Mindre enn tre års trygdetid - folketrygdsak:
                        includePhrase(
                            AvslagForLiteTrygdetidAP.RettTilAPFolketrygdsak(UNDER_3_AR_TT, AP2016)
                        )
                        includePhrase(AvslagForLiteTrygdetidAP.AvslagUnder1aar3aar5aarTT)

                    }.orShowIf(erEOSland and not(erAvtaleland)) { // Mindre enn tre års trygdetid - EØSsak:
                        includePhrase(AvslagForLiteTrygdetidAP.OpptjeningstidEOSAvtaleland(erAvtaleland, erEOSland))
                        includePhrase(
                            AvslagForLiteTrygdetidAP.RettTilAPMedEOSAvtalelandOg3aar5aarTT(
                                avslagsbegrunnelse = avslagsBegrunnelse,
                                erAvtaleland = erAvtaleland,
                                erEOSland = erEOSland,
                                regelverkType = regelverkType
                            ),
                        )
                        // avslagAP2016Under3aarHjemmelEOS
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6.",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6.",
                                English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article 6 of Regulation (EC) 883/2004",
                            )
                        }
                    }.orShow { // Mindre enn tre års avtalesak, eller både avtaleland og EØSland
                        includePhrase(AvslagForLiteTrygdetidAP.OpptjeningstidEOSAvtaleland(erAvtaleland, erEOSland))
                        includePhrase(
                            AvslagForLiteTrygdetidAP.RettTilAPMedEOSAvtalelandOg3aar5aarTT(
                                avslagsbegrunnelse = avslagsBegrunnelse,
                                erAvtaleland = erAvtaleland,
                                erEOSland = erEOSland,
                                regelverkType = regelverkType
                            )
                        )
                    }
                }.orShowIf(avslagsBegrunnelse.isOneOf(UNDER_5_AR_TT)) {

                    showIf(not(erAvtaleland) and not(erEOSland)) { // Mindre enn fem års trygdetid - folketrygdsak:

                        includePhrase(AvslagForLiteTrygdetidAP.RettTilAPFolketrygdsak(UNDER_5_AR_TT, AP2016))
                        includePhrase(AvslagForLiteTrygdetidAP.AvslagUnder1aar3aar5aarTT)

                        // avslagAP2016Under5aarHjemmel
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10.",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10.",
                                English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act",
                            )
                        }
                    }.orShowIf(erEOSland and not(erAvtaleland)) { // Mindre enn fem års trygdetid - EØSsak:
                        includePhrase(AvslagForLiteTrygdetidAP.OpptjeningstidEOSAvtaleland(erAvtaleland, erEOSland))
                        includePhrase(
                            AvslagForLiteTrygdetidAP.RettTilAPMedEOSAvtalelandOg3aar5aarTT(
                                avslagsbegrunnelse = avslagsBegrunnelse,
                                erAvtaleland = erAvtaleland,
                                erEOSland = erEOSland,
                                regelverkType = regelverkType
                            )
                        )
                        // avslagAP2016Under5aarHjemmelEOS
                        paragraph {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6.",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2, 20-5 til 20-8 og 20-10 og EØS-avtalens forordning 883/2004 artikkel 6.",
                                English to "This decision was made pursuant to the provisions of §§ 19-2, 20-5 to 20-8 and 20-10 of the National Insurance Act and Article 6 of Regulation (EC) 883/2004.",
                            )
                        }
                    }.orShow { // avtalelandsak eller (sak med både EØS- og avtaleland)
                        includePhrase(AvslagForLiteTrygdetidAP.OpptjeningstidEOSAvtaleland(erAvtaleland, erEOSland))
                        includePhrase(
                            AvslagForLiteTrygdetidAP.RettTilAPMedEOSAvtalelandOg3aar5aarTT(
                                avslagsbegrunnelse = avslagsBegrunnelse,
                                erAvtaleland = erAvtaleland,
                                erEOSland = erEOSland,
                                regelverkType = regelverkType
                            )
                        )
                    }
                }

                showIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT, UNDER_5_AR_TT) and erAvtaleland) {
                    includePhrase(
                        AvslagForLiteTrygdetidAP.AvslagUnder3aar5aarHjemmelAvtaleAuto(
                            avtaleland = avtaleland,
                            erAvtaleland = erAvtaleland,
                            erEOSland = erEOSland,
                            regelverkType = regelverkType
                        )
                    )
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
                showIf(
                    avslagsBegrunnelse.isOneOf(
                        UNDER_20_AR_BO_2016,
                        UNDER_5_AR_TT,
                        UNDER_3_AR_TT,
                        UNDER_1_AR_TT
                    )
                ) {
                    includePhrase(TrygdeperioderNorgeTabell(trygdeperioderNorge))
                    includePhrase(TrygdeperioderEOSlandTabell(trygdeperioderEOSland))
                    includePhrase(TrygdeperioderAvtalelandTabell(trygdeperioderAvtaleland))
                }
            }.orShowIf(avslagsBegrunnelse.isOneOf(UNDER_62)) {
                includePhrase(AvslagForLiteTrygdetidAP.AvslagAPUttakAlderU62Begrunn)
                // avslagAP2016UttakAlderU62Hjemmel
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-4 og 20-2.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-4 og 20-2.",
                        English to "This decision was made pursuant to the provisions of §§ 19-4 and 20-2 of the National Insurance Act.",
                    )
                }
                includePhrase(AvslagForLiteTrygdetidAP.AvslagAPUttakAlderU62Info)
                includePhrase(AvslagForLiteTrygdetidAP.NysoknadAPInfo)
            }
            includePhrase(AvslagForLiteTrygdetidAP.SupplerendeStoenad)
            includePhrase(Felles.RettTilAAKlage(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.RettTilInnsyn(vedlegg = vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
        includeAttachment(
            vedleggDineRettigheterOgMulighetTilAaKlage,
            pesysData.dineRettigheterOgMulighetTilAaKlageDto
        )
    }
}

