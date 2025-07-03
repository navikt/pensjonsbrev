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
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.PesysDataSelectors.vedtaksBegrunnelse
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagForLiteTrygdetidAPDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagAP2011FolketrygdsakHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagAP2011Under3aar5aarHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagUnder1aarHjemmel
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagUnder1aarTT
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagUnder3aar5aarTTAvtale
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AvslagUnder3aarTT
import no.nav.pensjon.brev.maler.fraser.alderspensjon.OpptjeningstidEOSAvtaleland
import no.nav.pensjon.brev.maler.fraser.alderspensjon.RettTilAPFolketrygdsak
import no.nav.pensjon.brev.maler.fraser.alderspensjon.RettTilAPMedEOSAvtalelandOg3aar5aarTT
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
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
        val erEOSland = pesysData.erEOSland
        val avtaleland = pesysData.avtaleland.ifNull(then = "angi avtaleland")
        val erAvtaleland = pesysData.erAvtaleland
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

            showIf(avslagsBegrunnelse.isNotAnyOf(UNDER_62)) {

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
                    includePhrase(AvslagUnder1aarTT)
                    includePhrase(
                        AvslagUnder1aarHjemmel(
                            avtaleland = avtaleland,
                            erEOSland = erEOSland,
                            regelverkType = pesysData.regelverkType
                        )
                    )
                }

                showIf(avslagsBegrunnelse.isOneOf(UNDER_3_AR_TT)) {

                    showIf(not(erAvtaleland)) { // Mindre enn tre års trygdetid - folketrygdsak:
                        includePhrase(RettTilAPFolketrygdsak(avslagsBegrunnelse))
                        includePhrase(AvslagUnder3aarTT)
                        includePhrase(AvslagAP2011FolketrygdsakHjemmel)

                    }.orShowIf(erEOSland) { // Mindre enn tre års trygdetid - EØSsak:
                        includePhrase(OpptjeningstidEOSAvtaleland(erEOSland))
                        includePhrase(RettTilAPMedEOSAvtalelandOg3aar5aarTT(avslagsBegrunnelse, erEOSland))
                        includePhrase(AvslagAP2011Under3aar5aarHjemmel)

                    }.orShow { // Mindre enn tre års trygdetid - avtalesak:
                        includePhrase(OpptjeningstidEOSAvtaleland(erEOSland))
                        includePhrase(RettTilAPMedEOSAvtalelandOg3aar5aarTT(avslagsBegrunnelse, erAvtaleland))
                        // avslagAP2011Under3aarHjemmelAvtale
                        paragraph {
                            textExpr(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-2 og artikkel ".expr() + fritekst(
                                    "Legg inn aktuelle artikler om sammenlegging og eksport"
                                ) + " i trygdeavtalen med ".expr() + avtaleland + ".",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-2 og artikkel ".expr() + fritekst(
                                    "Legg inn aktuelle artikler om sammenlegging og eksport"
                                ) + " i trygdeavtalen med ".expr() + avtaleland + ".",
                                English to "This decision was made pursuant to the provisions of § 19-2 of the National Insurance Act and Article ".expr() + fritekst(
                                    "Legg inn aktuelle artikler om sammenlegging og eksport"
                                ) + " of the Social Security Agreement with ".expr() + avtaleland + "."
                            )
                        }
                    }

                }.orShowIf(avslagsBegrunnelse.isOneOf(UNDER_5_AR_TT)) {

                    showIf(not(erAvtaleland)) { // Mindre enn fem års trygdetid - folketrygdsak:
                        includePhrase(RettTilAPFolketrygdsak(avslagsBegrunnelse))
                        includePhrase(AvslagUnder3aar5aarTTAvtale)
                        includePhrase(AvslagAP2011FolketrygdsakHjemmel)

                    }.orShowIf(erEOSland) { // Mindre enn fem års trygdetid - EØSsak:
                        includePhrase(OpptjeningstidEOSAvtaleland(erEOSland))
                        includePhrase(RettTilAPMedEOSAvtalelandOg3aar5aarTT(avslagsBegrunnelse, erAvtaleland))
                        includePhrase(AvslagAP2011Under3aar5aarHjemmel)
                    }.orShow { // erAvtaleland
                        includePhrase(OpptjeningstidEOSAvtaleland(erEOSland))
                        includePhrase(RettTilAPMedEOSAvtalelandOg3aar5aarTT(avslagsBegrunnelse, erEOSland))
                        paragraph { // avslagAP2011Under5aarHjemmelAvtaleAuto
                            textExpr(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven § 19-2 og reglene i trygdeavtalen med ".expr() + avtaleland + ".",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 19-2 og reglane i trygdeavtalen med ".expr() + avtaleland + ".",
                                English to "This decision was made pursuant to the provision of § 19-2 of the National Insurance Act and to the provisions of the social security agreement with ".expr() + avtaleland + "."
                            )
                        }
                    }
                }
            }
        }
    }
}