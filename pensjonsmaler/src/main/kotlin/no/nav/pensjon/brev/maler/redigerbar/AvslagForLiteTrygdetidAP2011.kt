package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.ALDER
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.VedtaksBegrunnelse.*
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.avtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.bostedsland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.erAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.erEOSland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.trygdeperioderAvtaleland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.trygdeperioderEOSland
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.trygdeperioderNorge
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.vedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagAP2011FolketrygdsakHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagAP2011Under3aar5aarHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagUnder1aar3aar5aarTT
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagUnder1aarHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagUnder3aar5aarHjemmelAvtaleAuto
import no.nav.pensjon.brev.maler.fraser.alderspensjon.OpptjeningstidEOSAvtaleland
import no.nav.pensjon.brev.maler.fraser.alderspensjon.RettTilAPFolketrygdsak
import no.nav.pensjon.brev.maler.fraser.alderspensjon.RettTilAPMedEOSAvtalelandOg3aar5aarTT
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SupplerendeStoenad
import no.nav.pensjon.brev.maler.fraser.alderspensjon.TrygdeperioderAvtalelandTabell
import no.nav.pensjon.brev.maler.fraser.alderspensjon.TrygdeperioderEOSlandTabell
import no.nav.pensjon.brev.maler.fraser.alderspensjon.TrygdeperioderNorgeTabell
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// Doksys redigermal: MF_000066, tvilling autobrev: MF_000177
@TemplateModelHelpers

object AvslagForLiteTrygdetidAP2011 : RedigerbarTemplate<AvslagForLiteTrygdetidAPDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_AVSLAG_FOR_LITE_TRYGDETID_AP2011
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
        val avtaleland = pesysData.avtaleland.ifNull(then = "angi avtaleland")
        val bostedsland = pesysData.bostedsland.ifNull(then = "angi bostedsland")
        val erAvtaleland = pesysData.erAvtaleland
        val erEOSland = pesysData.erEOSland
        val trygdeperioderAvtaleland = pesysData.trygdeperioderAvtaleland
        val trygdeperioderEOSland = pesysData.trygdeperioderEOSland
        val trygdeperioderNorge = pesysData.trygdeperioderNorge


        title {
            text(
                Bokmal to "Nav har avslått søknaden din om alderspensjon",
                Nynorsk to "Nav har avslått søknaden din om alderspensjon",
                English to "Nav has declined your application for retirement pension",
            )
        }
        outline {
            includePhrase(Vedtak.Overskrift)

            // Under ett års medlemstid i folketrygden:
            showIf(avslagsBegrunnelse.isOneOf(UNDER_1_AR_TT)) {
                // avslagAP2011Under1aar
                paragraph {
                    text(
                        Bokmal to "For å ha rett til alderspensjon må du ha bodd eller arbeidet i Norge i minst ett år. Det har du ikke, og derfor har vi avslått søknaden din.",
                        Nynorsk to "For å ha rett til alderspensjon må du ha budd eller arbeidd i Noreg i minst eit år. Det har du ikkje, og derfor har vi avslått søknaden din.",
                        English to "To be eligible for retirement pension, you must have been registered as living or working in Norway for at least one year. You do not meet this requirement, therefore we have declined your application.",
                    )
                }
                includePhrase(AvslagUnder1aar3aar5aarTT)
                includePhrase(AvslagUnder1aarHjemmel(avtaleland, erEOSland, regelverkType = pesysData.regelverkType))

            }.orShowIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT)) {
                showIf(not(erAvtaleland) and not(erEOSland)) { // Mindre enn tre års trygdetid - folketrygdsak:
                    includePhrase(RettTilAPFolketrygdsak(avslagsBegrunnelse, regelverkType = pesysData.regelverkType))
                    includePhrase(AvslagUnder1aar3aar5aarTT)
                    includePhrase(AvslagAP2011FolketrygdsakHjemmel)

                }.orShowIf(erEOSland and not(erAvtaleland)) { // Mindre enn tre års trygdetid - EØSsak:
                    includePhrase(OpptjeningstidEOSAvtaleland(erAvtaleland, erEOSland))
                    includePhrase(
                        RettTilAPMedEOSAvtalelandOg3aar5aarTT(
                            avslagsBegrunnelse,
                            erAvtaleland,
                            erEOSland,
                            regelverkType = pesysData.regelverkType
                        )
                    )
                    includePhrase(AvslagAP2011Under3aar5aarHjemmel)

                }.orShow { // erAvtaleland or (erEOSland and erAvtaleland)
                    includePhrase(OpptjeningstidEOSAvtaleland(erAvtaleland, erEOSland))
                    includePhrase(
                        RettTilAPMedEOSAvtalelandOg3aar5aarTT(
                            avslagsBegrunnelse,
                            erAvtaleland,
                            erEOSland,
                            regelverkType = pesysData.regelverkType
                        )
                    )
                }
            }.orShowIf(avslagsBegrunnelse.isOneOf(UNDER_5_AR_TT)) {

                showIf(not(erAvtaleland) and not(erEOSland)) { // Mindre enn fem års trygdetid - folketrygdsak:
                    includePhrase(RettTilAPFolketrygdsak(avslagsBegrunnelse, regelverkType = pesysData.regelverkType))
                    includePhrase(AvslagUnder1aar3aar5aarTT)
                    includePhrase(AvslagAP2011FolketrygdsakHjemmel)

                }.orShowIf(erEOSland and not(erAvtaleland)) { // Mindre enn fem års trygdetid - EØSsak:
                    includePhrase(OpptjeningstidEOSAvtaleland(erAvtaleland, erEOSland))
                    includePhrase(
                        RettTilAPMedEOSAvtalelandOg3aar5aarTT(
                            avslagsBegrunnelse,
                            erAvtaleland,
                            erEOSland,
                            regelverkType = pesysData.regelverkType
                        )
                    )
                    includePhrase(AvslagAP2011Under3aar5aarHjemmel)
                }.orShow { // avtalelandsak eller (sak med både EØS- og avtaleland)
                    includePhrase(OpptjeningstidEOSAvtaleland(erAvtaleland, erEOSland))
                    includePhrase(
                        RettTilAPMedEOSAvtalelandOg3aar5aarTT(
                            avslagsBegrunnelse,
                            erAvtaleland,
                            erEOSland,
                            regelverkType = pesysData.regelverkType
                        )
                    )
                }
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT, UNDER_5_AR_TT) and erAvtaleland) {
                includePhrase(
                    AvslagUnder3aar5aarHjemmelAvtaleAuto(
                        avtaleland,
                        erAvtaleland,
                        erEOSland,
                        regelverkType = pesysData.regelverkType

                    )
                )
            }

            showIf(avslagsBegrunnelse.isOneOf(UNDER_20_AR_BO)) {
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

            showIf(
                avslagsBegrunnelse.isOneOf(
                    UNDER_20_AR_BO,
                    UNDER_5_AR_TT,
                    UNDER_3_AR_TT,
                    UNDER_1_AR_TT
                )
            ) {
                includePhrase(
                    TrygdeperioderNorgeTabell(
                        trygdeperioderNorge = trygdeperioderNorge
                    )
                )
                includePhrase(
                    TrygdeperioderEOSlandTabell(
                        trygdeperioderEOSland = trygdeperioderEOSland
                    )
                )
                includePhrase(
                    TrygdeperioderAvtalelandTabell(
                        trygdeperioderAvtaleland = trygdeperioderAvtaleland
                    )
                )
            }

            includePhrase(SupplerendeStoenad)
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
