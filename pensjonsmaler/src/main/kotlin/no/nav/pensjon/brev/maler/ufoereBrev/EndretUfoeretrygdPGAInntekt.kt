package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.arligAvkortningsbelopBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.arligAvkortningsbelopBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.avkortningInntektsgrense
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.avkortningInntektstak
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.avkortningInntektstakBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.avkortningInntektstakBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.avkortningsbelopPerAr
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.belopFratrukketAnnenForeldersInntektBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.belopOkt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.belopRedusert
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.brukerBorINorge
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.brukersInntektBruktIAvkortningAvBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.ettArForVirkningstidspunkt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.forventetInntekt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.fribelopBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.borMed
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.fribelopBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.gammeltBelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.antallBarnBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.antallBarnBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.gammeltBelopBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.gammeltBelopBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.grunnbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.inntektAnnenForelderBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.inntektBruktIAvkortningAvBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.inntektBruktIAvkortningAvBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.innvilgetBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.innvilgetBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.innvilgetET
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.innvilgetGJT
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.instoppholdType
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.justeringsbelopPerArBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.justeringsbelopPerArBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.kravarsaksType
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.nettoAkk
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.nettoBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.nettoBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.nettoPerManed
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.nyttBelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.nyttBelopBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.nyttBelopBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.pe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.periodisertFribelopBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.periodisertFribelopBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.periodisertInntektBTFB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.periodisertInntektBTSB
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.regelverktypeBT
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.totalNetto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.totalNettoForAr
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.uforegrad
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.utbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.vilFylle67IlaVirkningFomAr
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDtoSelectors.virkningFom
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.legacy.fraser.TBU2278_Generated
import no.nav.pensjon.brev.maler.legacy.inkluderopplysningerbruktiberegningen
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.LetterMetadataPensjon
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK
import java.time.LocalDate

@TemplateModelHelpers
object EndretUfoeretrygdPGAInntekt : AutobrevTemplate<EndretUfoeretrygdPGAInntektDto> {

    // PE_UT_05_100
    override val kode = Pesysbrevkoder.AutoBrev.UT_ENDRET_PGA_INNTEKT

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndretUfoeretrygdPGAInntektDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadataPensjon(
            displayTitle = "Vedtak - endring av uføretrygd på grunn av inntekt (automatisk)",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val virkningFomErForsteDagIAaret = virkningFom.month.equalTo(1) and virkningFom.day.equalTo(1)
        val endretUT = gammeltBelop.ifNull(0).notEqualTo(nyttBelop.ifNull(0))
        val endretBT = gammeltBelopBTFB.notEqualTo(nyttBelopBTFB) or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
        val endretBTFB = gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
        val endretBTSB = gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
        val reduksjonBT = gammeltBelopBTFB.ifNull(0).greaterThan(nyttBelopBTFB.ifNull(0)) or gammeltBelopBTSB.ifNull(0).greaterThan(nyttBelopBTSB.ifNull(0))
        val nyBTStorreEnnNull = nyttBelopBTFB.ifNull(0).greaterThan(0) or nyttBelopBTSB.ifNull(0).greaterThan(0)

        title {
            showIf(endretUT and not(endretBT)) {
                text(
                    Bokmal to "Nav har endret utbetalingen av uføretrygden din",
                    Nynorsk to "Nav har endra utbetalinga av uføretrygda di",
                )

            }.orShowIf(endretUT and endretBT) {
                showIf(innvilgetBTSB and innvilgetBTFB) {
                    text(
                        Bokmal to "Nav har endret utbetalingen av uføretrygden din og barnetilleggene dine",
                        Nynorsk to "Nav har endra utbetalinga av uføretrygda di og barnetillegga dine",
                    )
                }.orShow {
                    text(
                        Bokmal to "Nav har endret utbetalingen av uføretrygden din og barnetillegget ditt",
                        Nynorsk to "Nav har endret utbetalingen av uføretrygden din og barnetillegget ditt",
                    )
                }
            }.orShow {
                showIf(innvilgetBTSB and innvilgetBTFB) {
                    text(
                        Bokmal to "Nav har endret utbetalingen av barnetilleggene i uføretrygden din",
                        Nynorsk to "Nav har endra utbetalinga av barnetillegga i uføretrygda di",
                    )
                }.orShow {
                    text(
                        Bokmal to "Nav har endret utbetalingen av barnetillegget i uføretrygden din",
                        Nynorsk to "Nav har endret utbetalingen av barnetillegget i uføretrygda di",
                    )
                }
            }
        }

        outline {
            showIf(not(virkningFomErForsteDagIAaret)) {
                paragraph {
                    showIf(not(innvilgetBTFB)) {
                        text(
                            Bokmal to "Vi har mottatt nye opplysninger om inntekten din. ",
                            Nynorsk to "Vi har fått nye opplysningar om inntekta di. ",
                        )
                    }.orShow {
                        ifNotNull(borMed) { borMed ->
                            showIf(innvilgetBTFB and innvilgetBTSB) {
                                textExpr(
                                    Bokmal to "Vi har mottatt nye opplysninger om inntekten til deg eller din ".expr() + epsTypeUbestemt(borMed) + ". Inntekten til din " + epsTypeUbestemt(borMed) + " har kun betydning for størrelsen på barnetillegget for " + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre. ",
                                    Nynorsk to "Vi har fått nye opplysningar om inntekta til deg eller ".expr() + epsTypeUbestemt(borMed) + " din. Inntekta til " + epsTypeUbestemt(borMed) + " din har berre betydning for storleiken på barnetillegget for " + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge foreldra sine. ",
                                )
                            }.orShow {
                                textExpr(
                                    Bokmal to "Vi har mottatt nye opplysninger om inntekten til deg eller din ".expr() + epsTypeUbestemt(borMed) + ". Inntekten til din " + epsTypeUbestemt(borMed) + " har kun betydning for størrelsen på barnetillegget ditt. ",
                                    Nynorsk to "Vi har fått nye opplysningar om inntekta til deg eller ".expr() + epsTypeUbestemt(borMed) + " din. Inntekta til " + epsTypeUbestemt(borMed) + " din har berre betydning for storleiken på barnetillegget ditt. ",
                                )
                            }
                        }
                    }

                    showIf(endretUT and not(endretBT)) {
                        textExpr(
                            Bokmal to "Utbetalingen av uføretrygden din er derfor endret fra ".expr() + virkningFom.format() + ". ",
                            Nynorsk to "Utbetalinga av uføretrygda di er derfor endra frå ".expr() + virkningFom.format() + ". ",
                        )
                    }.orShow {
                        showIf(innvilgetBTFB and innvilgetBTSB) {
                            textExpr(
                                Bokmal to "Utbetalingen av uføretrygden din og barnetilleggene dine er derfor endret fra ".expr() + virkningFom.format() + ". ",
                                Nynorsk to "Utbetalinga av uføretrygda di og barnetillegga dine er derfor endra frå ".expr() + virkningFom.format() + ". ",
                            )
                        }.orShow {
                            textExpr(
                                Bokmal to "Utbetalingen av uføretrygden din og barnetillegget ditt er derfor endret fra ".expr() + virkningFom.format() + ". ",
                                Nynorsk to "Utbetalinga av uføretrygda di og barnetillegget ditt er derfor endra frå ".expr() + virkningFom.format() + ". ",
                            )
                        }

                    }
                }
            }.orShow {
                showIf(endretUT) {
                    ifNotNull(forventetInntekt)  { forventetInntekt ->
                        paragraph {
                            textExpr(
                                Bokmal to "Vi vil bruke en inntekt på ".expr() + forventetInntekt.format() + " kroner når vi reduserer uføretrygden din for " + virkningFom.year.format() + ". " +
                                        "Har du ikke meldt inn ny inntekt for " + virkningFom.year.format() + ", er inntekten justert opp til dagens verdi. ",
                                Nynorsk to "Vi vil bruke ei inntekt på ".expr() + forventetInntekt.format() + " kroner når vi reduserer uføretrygda di for " + virkningFom.year.format() + ". " +
                                        "Har du ikkje meldt inn ny inntekt for " + virkningFom.year.format() + ", er inntekta justert opp til dagens verdi. ",
                            )
                        }
                    }

                    showIf(not(utbetalingsgrad.equalTo(uforegrad))) {
                        paragraph {
                            textExpr(
                                Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + ettArForVirkningstidspunkt.format() + ", er inntekten justert opp slik at den gjelder for hele " + virkningFom.year.format() + ". ",
                                Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + ettArForVirkningstidspunkt.format() + ", er inntekta også justert opp slik at den gjeld for heile " + virkningFom.year.format() + ". ",
                            )
                        }
                    }

                    showIf(vilFylle67IlaVirkningFomAr) {
                        paragraph {
                            textExpr(
                                Bokmal to "Fordi du fyller 67 år i ".expr() + virkningFom.year.format() + ", er inntekten justert i forhold til antall måneder du mottar uføretrygd. ",
                                Nynorsk to "Fordi du fyljer 67 år i ".expr() + virkningFom.year.format() + ", er inntekta justert ut frå talet på månadar du får uføretrygd. ",
                            )
                        }
                    }
                }

                showIf(endretUT and endretBT and innvilgetBTSB and not(innvilgetBTFB)) {
                    ifNotNull(inntektBruktIAvkortningAvBTSB) { inntektBruktIAvkortningAvBTSB ->
                        paragraph {
                            textExpr(
                                Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                                Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr() + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                            )
                        }
                    }
                }

                showIf(endretUT and endretBT and innvilgetBTFB) {
                    paragraph {
                        showIf(innvilgetBTFB and innvilgetBTSB) {
                            ifNotNull(inntektBruktIAvkortningAvBTFB, inntektBruktIAvkortningAvBTSB) { inntektBruktIAvkortningAvBTFB, inntektBruktIAvkortningAvBTSB ->
                                textExpr(
                                    Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner for " + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre. " +
                                            "For " + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                                    Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur med begge sine foreldra. " +
                                            "For " + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikkje bur saman med begge foreldrea vil vi bruke ei inntekt på " + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                                )
                            }
                        }.orShow {
                            ifNotNull(inntektBruktIAvkortningAvBTFB) { inntektBruktIAvkortningAvBTFB ->
                                textExpr(
                                    Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner. ",
                                    Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner. ",
                                )
                            }
                        }
                    }
                }.orShowIf(endretUT and endretBTFB) {
                    paragraph {
                        showIf(innvilgetBTFB and innvilgetBTSB) {
                            ifNotNull(inntektBruktIAvkortningAvBTFB, inntektBruktIAvkortningAvBTSB) { inntektBruktIAvkortningAvBTFB, inntektBruktIAvkortningAvBTSB ->
                                textExpr(
                                    Bokmal to "Vi vil bruke en inntekt på ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner når vi reduserer barnetillegget for " + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre for " + virkningFom.year.format() + ". " +
                                            "For " + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                                    Nynorsk to "Vi vil bruke ei inntekt på ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner når vi reduserer barnetillegget for " + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge foreldra sine for " + virkningFom.year.format() + ". " +
                                            "For " + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikkje bur saman med begge foreldra vil vi bruke ei inntekt på " + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                                )
                            }
                        }.orShow {
                            ifNotNull(inntektBruktIAvkortningAvBTFB) { inntektBruktIAvkortningAvBTFB ->
                                textExpr(
                                    Bokmal to "Vi vil bruke en inntekt på ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner når vi reduserer barnetillegget ditt. ",
                                    Nynorsk to "Vi vil bruke ei inntekt på ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner når vi reduserer barnetillegget ditt. ",
                                )
                            }
                        }

                        showIf(innvilgetBTFB) {
                            textExpr(
                                Bokmal to "Har du ikke meldt inn nye inntekter for ".expr() + virkningFom.year.format() + ", er inntektene justert opp til dagens verdi. ",
                                Nynorsk to "Har du ikkje meldt inn nye inntekter for ".expr() + virkningFom.year.format() + ", er inntektene justert opp til dagens verdi. ",
                            )
                        }.orShow {
                            textExpr(
                                Bokmal to "Har du ikke meldt inn ny inntekt for ".expr() + virkningFom.year.format() + ", er inntekten justert opp til dagens verdi. ",
                                Nynorsk to "Har du ikke meldt inn ny inntekt for ".expr() + virkningFom.year.format() + ", er inntekta justert opp til dagens verdi. ",
                            )
                        }

                    }
                }.orShowIf(not(endretBTFB) and endretBTSB and not(endretUT)) {
                    ifNotNull(inntektBruktIAvkortningAvBTSB) { inntektBruktIAvkortningAvBTSB ->
                        paragraph {
                            textExpr(
                                Bokmal to "Vi vil bruke en inntekt på ".expr() + inntektBruktIAvkortningAvBTSB.format() + " kroner når vi reduserer barnetillegget ditt. " +
                                        "Har du ikke meldt inn ny inntekt for " + virkningFom.year.format() + ", er inntekten justert opp til dagens verdi. ",
                                Nynorsk to "Vi vil bruke ei inntekt på ".expr() + inntektBruktIAvkortningAvBTSB.format() + " kroner når vi reduserer barnetillegget ditt. " +
                                        "Har du ikkje meldt inn ny inntekt for " + virkningFom.year.format() + ", er inntekta justert opp til dagens verdi. ",
                            )
                        }
                    }
                }

                showIf(utbetalingsgrad.equalTo(uforegrad) and endretBT) {
                    paragraph {
                        textExpr(
                            Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + ettArForVirkningstidspunkt.format() + ", er inntekten justert opp slik at den gjelder for hele " + virkningFom.year.format() + ". ",
                            Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + ettArForVirkningstidspunkt.format() + ", er inntekta justert opp slik at den gjeld for heile " + virkningFom.year.format() + ". ",
                        )
                    }
                }

                showIf(
                    reduksjonBT
                            and nyBTStorreEnnNull
                            and (periodisertInntektBTFB
                            or periodisertInntektBTSB
                            or periodisertFribelopBTFB
                            or periodisertFribelopBTSB)
                ) {
                    paragraph {
                        showIf(innvilgetBTFB and innvilgetBTSB) {
                            // TODO: her burde vi heller sende inn grunnlagsdata for barnetillegget
                            showIf((not(periodisertInntektBTFB) and periodisertInntektBTSB) or (not(periodisertFribelopBTFB) and periodisertFribelopBTSB)) {
                                showIf(periodisertInntektBTSB and periodisertFribelopBTSB) {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() +  barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikke bor med begge sine foreldre hele året " +
                                                "er inntektene og fribeløpet justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() +  barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikkje bur saman med begge foreldra hele året " +
                                                "er inntektene og fribeløpet justert slik at dei berre gjelde for den perioden du får barnetillegg. ",
                                    )
                                }.orShowIf(periodisertFribelopBTSB) {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikke bor med begge sine foreldre hele året " +
                                                "er fribeløpet justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikkje bur saman med begge foreldra hele året " +
                                                "er fribeløpet justert slik at dei berre gjelde for den perioden du får barnetillegg. ",
                                    )
                                }.orShow {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikke bor med begge sine foreldre hele året " +
                                                "er inntektene justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikkje bur saman med begge foreldra hele året " +
                                                "er inntektene justert slik at dei berre gjelde for den perioden du får barnetillegg. ",
                                    )
                                }
                            }.orShow{
                                showIf(periodisertInntektBTFB and periodisertFribelopBTFB) {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre hele året " +
                                                "er inntektene og fribeløpet justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge foreldra hele året " +
                                                "er inntektene og fribeløpet justert slik at dei berre gjelde for den perioden du får barnetillegg. ",
                                    )
                                }.orShowIf(periodisertFribelopBTFB) {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre hele året " +
                                                "er fribeløpet justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge foreldra hele året " +
                                                "er fribeløpet justert slik at dei berre gjelde for den perioden du får barnetillegg. ",
                                    )
                                }.orShow {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre hele året " +
                                                "er inntektene justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge foreldra hele året " +
                                                "er inntektene justert slik at dei berre gjelde for den perioden du får barnetillegg. ",
                                    )
                                }
                            }
                        }.orShow {
                            showIf((periodisertInntektBTFB or periodisertInntektBTSB) and (periodisertFribelopBTFB or periodisertFribelopBTSB)) {
                                text(
                                    Bokmal to "Fordi du ikke har barnetillegg hele året er inntektene og fribeløpet justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                    Nynorsk to "Fordi du ikkje har barnetillegg hele året er inntektene og fribeløpet justert slik at dei berre gjelde for den perioden du får barnetillegg. ",
                                )
                            }.orShowIf(periodisertFribelopBTFB or periodisertFribelopBTSB) {
                                text(
                                    Bokmal to "Fordi du ikke har barnetillegg hele året er fribeløpet justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                    Nynorsk to "Fordi du ikkje har barnetillegg hele året er fribeløpet justert slik at dei berre gjelde for den perioden du får barnetillegg. ",
                                )
                            }.orShow {
                                text(
                                    Bokmal to "Fordi du ikke har barnetillegg for hele året er inntektene justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                    Nynorsk to "Fordi du ikkje har barnetillegg for hele året er inntektene justert slik at dei berre gjelde for den perioden du får barnetillegg. ",
                                )
                            }
                        }
                    }
                }

                paragraph {
                    showIf(innvilgetBTFB) {
                        ifNotNull(borMed) { borMed ->
                            textExpr(
                                Bokmal to "Forventer du og din ".expr() + epsTypeUbestemt(borMed) + " å tjene noe annet i " + virkningFom.year.format() + " er det viktig at du melder inn ny forventet inntekt. Dette kan du gjøre på $NAV_URL. ",
                                Nynorsk to "Forventar du og ".expr() + epsTypeUbestemt(borMed) + " din å tene noko anna i " + virkningFom.year.format() + " er det viktig at du melder inn ei ny forventa inntekt. Dette kan du gjere på $NAV_URL. ",
                            )
                        }
                    }.orShow {
                        textExpr(
                            Bokmal to "Forventer du å tjene noe annet i ".expr() + virkningFom.year.format() + " er det viktig at du melder inn ny forventet inntekt. Dette kan du gjøre på $NAV_URL. ",
                            Nynorsk to "Forventar du å tene noko anna i ".expr() + virkningFom.year.format() + " er det viktig at du melder inn ei ny forventa inntekt. Dette kan du gjere på $NAV_URL. ",
                        )
                    }

                }
            }

            showIf(not(innvilgetBTSB) and not(innvilgetBTFB)) {
                showIf(not(innvilgetET) and not(innvilgetGJT) and instoppholdType.equalTo("")) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd per måned før skatt. ",
                            Nynorsk to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd per månad før skatt. ",
                        )
                    }
                }

                showIf(not(innvilgetGJT) and innvilgetET) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd og ektefelletillegg per måned før skatt. ",
                            Nynorsk to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd og ektefelletillegg per månad før skatt. ",
                        )
                    }
                }

                showIf(innvilgetGJT and not(innvilgetET) and instoppholdType.equalTo("")) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt. ",
                            Nynorsk to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd og attlevandetillegg per månad før skatt. ",
                        )
                    }
                }
            }

            showIf(innvilgetBTSB or innvilgetBTFB) {
                showIf(not(innvilgetET) and not(innvilgetGJT) and instoppholdType.equalTo("")) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd og barnetillegg per måned før skatt. ",
                            Nynorsk to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd og barnetillegg per månad før skatt. ",
                        )
                    }
                }

                showIf(not(innvilgetGJT) and innvilgetET) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd, barne- og ektefelletillegg per måned før skatt. ",
                            Nynorsk to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd, barne- og ektefelletillegg per månad før skatt. ",
                        )
                    }

                }

                showIf(innvilgetGJT and not(innvilgetET)) {
                    paragraph {
                        textExpr(
                            Bokmal to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt. ",
                            Nynorsk to "Du får ".expr() + totalNetto.format() + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt. ",
                        )
                    }
                }

            }

            showIf((totalNetto.greaterThan(0))) {
                showIf(totalNetto.greaterThan(0)) {
                    paragraph {
                        text(
                            Bokmal to "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned. ",
                            Nynorsk to "Uføretrygda blir framleis utbetalt seinast den 20. i kvar månad. ",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "I dette brevet forklarer vi hvilke rettigheter og plikter du har. Det er derfor viktig at du leser hele brevet. ",
                    Nynorsk to "I dette brevet forklarer vi kva rettar og plikter du har. Det er derfor viktig at du les heile brevet. ",
                )
            }

            title1 {
                text(
                    Bokmal to "Begrunnelse for vedtaket",
                    Nynorsk to "Grunngiving for vedtaket",
                )
            }

            showIf(endretUT) {
                showIf(avkortningInntektsgrense.lessThan(avkortningInntektstak)) {
                    paragraph {
                        text(
                            Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden. Det er bare den delen av inntekten din som overstiger inntektsgrensen som vil gi en reduksjon av uføretrygden. ",
                            Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda. Det er berre den delen av inntekta di som kjem over inntektsgrensa som gir ein reduksjon av uføretrygda di. ",
                        )
                    }

                    paragraph {
                        ifNotNull(forventetInntekt) { forventetInntekt ->
                            showIf(nyttBelop.ifNull(0).greaterThan(0)) {
                                showIf(forventetInntekt.greaterThan(avkortningInntektsgrense)) {
                                    showIf(gammeltBelop.ifNull(0).greaterThan(nyttBelop.ifNull(0))) {
                                        text(
                                            Bokmal to "Uføretrygden din reduseres fordi du tjener over inntektsgrensen din. Selv om du får en reduksjon lønner det seg likevel å jobbe ved siden av uføretrygden. ",
                                            Nynorsk to "Uføretrygda di blir redusert fordi du tener over inntektsgrensa di. Sjølv om du får ein reduksjon, lønner det seg likevel å jobbe ved sida av uføretrygda. ",
                                        )
                                    }
                                    showIf(utbetalingsgrad.equalTo(uforegrad) and gammeltBelop.ifNull(0).lessThan(nyttBelop.ifNull(0))) {
                                        text(
                                            Bokmal to "Endringen i inntekten din gjør at uføretrygden ikke lenger er redusert. ",
                                            Nynorsk to "Endringa i inntekta di gjer at uføretrygda ikkje lenger er redusert. ",
                                        )
                                    }
                                }.orShowIf(utbetalingsgrad.equalTo(uforegrad)) {
                                    text(
                                        Bokmal to "Utbetalingen av uføretrygden din økes fordi du tjener under inntektsgrensen din. Det betyr at uføretrygden ikke lenger er redusert. ",
                                        Nynorsk to "Utbetalinga av uføretrygda aukar fordi du tener under inntektsgrensa di. Det betyr at uføretrygda ikkje lenger er redusert. ",
                                    )
                                }

                            }.orShowIf(forventetInntekt.lessThanOrEqual(avkortningInntektstak)) {
                                text(
                                    Bokmal to "Endring i inntekten din gjør at du ikke får utbetalt uføretrygd for resten av året. ",
                                    Nynorsk to "Endring i inntekta di gjer at du ikkje får utbetalt uføretrygd for resten av året. ",
                                )
                            }
                        }
                    }
                }

                showIf(avkortningInntektsgrense.greaterThanOrEqual(avkortningInntektstak)) {
                    paragraph {
                        text (
                            Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden. ",
                            Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda. ",
                        )
                        showIf(belopRedusert and forventetInntekt.ifNull(0).greaterThan(avkortningInntektsgrense)) {
                            text(
                                Bokmal to " Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene. ",
                                Nynorsk to " Det lønner seg likevel å jobbe fordi inntekt og uføretrygd vil alltid vere høgare enn uføretrygd åleine. ",
                            )
                        }
                    }
                }

                ifNotNull( avkortningsbelopPerAr, forventetInntekt) { avkortningsbelopPerAr, forventetInntekt ->
                    showIf(forventetInntekt.greaterThan(avkortningInntektsgrense) and nyttBelop.ifNull(0).greaterThan(0)) {
                        paragraph {
                            showIf(virkningFomErForsteDagIAaret) {
                                textExpr(
                                    Bokmal to "Siden du har en inntekt på ".expr() + forventetInntekt.format() + " kroner trekker vi " + avkortningsbelopPerAr.format() + " kroner fra uføretrygden for neste år. ",
                                    Nynorsk to "Fordi du har ei inntekt på ".expr() + forventetInntekt.format() + " kroner trekkjer vi " + avkortningsbelopPerAr.format() + " kroner frå uføretrygda for neste år. ",
                                )
                            }.orShow {
                                textExpr(
                                    Bokmal to "Siden du har en inntekt på ".expr() + forventetInntekt.format() + " kroner trekker vi " + avkortningsbelopPerAr.format() + " kroner fra uføretrygden i år. ",
                                    Nynorsk to "Fordi du har ei inntekt på ".expr() + forventetInntekt.format() + " kroner trekkjer vi " + avkortningsbelopPerAr.format() + " kroner frå uføretrygda i år. ",
                                )
                            }
                        }
                    }
                }

                ifNotNull( forventetInntekt) { forventetInntekt ->
                    showIf(forventetInntekt.greaterThan(avkortningInntektstak) and nyttBelop.ifNull(0).equalTo(0)) {
                        showIf(avkortningInntektsgrense.notEqualTo(avkortningInntektstak)) {
                            paragraph {
                                textExpr(
                                    Bokmal to "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + avkortningInntektstak.format() + " kroner. " +
                                            "Inntekten vi har brukt er " + forventetInntekt.format() + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året. ",
                                    Nynorsk to "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie ".expr() + avkortningInntektstak.format() + " kroner. " +
                                            "Inntekta vi har brukt er " + forventetInntekt.format() + " kroner og du vil ikkje få utbetalt uføretrygd resten av året. ",
                                )
                            }
                        }.orShow {
                            paragraph {
                                textExpr(
                                    Bokmal to "Det utbetales ikke uføretrygd når inntekten din utgjør mer enn inntektsgrensen, det vil si ".expr() + avkortningInntektsgrense.format() + " kroner. " +
                                            "Inntekten vi har brukt er " + forventetInntekt.format() + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året. ",
                                    Nynorsk to "Det blir ikkje utbetalt uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil seie ".expr() + avkortningInntektsgrense.format() + " kroner. " +
                                            "Inntekta vi har brukt er " + forventetInntekt.format() + " kroner og du vil derfor ikkje få utbetalt uføretrygd resten av året. ",
                                )
                            }
                        }
                    }
                }
            }

            showIf(endretBT) {
                showIf(innvilgetBTFB or innvilgetBTSB) {
                    paragraph {
                        text (
                            Bokmal to "Inntekten din har også betydning for hva du får utbetalt i barnetillegg. ",
                            Nynorsk to "Inntekta di har også betydning for kva du får utbetalt i barnetillegg. ",
                        )

                        ifNotNull (borMed) { borMed ->
                            showIf(innvilgetBTFB and not(innvilgetBTSB)) {
                                textExpr(
                                    Bokmal to "Inntekten din har også betydning for hva du får utbetalt i barnetillegg. Fordi ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " bor med begge sine foreldre, bruker vi i tillegg din " + epsTypeUbestemt(borMed) + "s inntekt når vi fastsetter størrelsen på barnetillegget ditt. ",
                                    Nynorsk to "Inntekta di har også betydning for kva du får utbetalt i barnetillegg. Fordi ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " bur saman med begge foreldra sine, bruker vi i tillegg " +  epsTypeUbestemt(borMed) + " din si inntekt når vi fastset storleiken på barnetillegget ditt. ",
                                )
                            }.orShowIf(innvilgetBTSB and innvilgetBTFB) {
                                textExpr(
                                    Bokmal to "Inntekten din har også betydning for hva du får utbetalt i barnetillegg. For".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre, bruker vi i tillegg din " + epsTypeUbestemt(borMed) + "s inntekt når vi fastsetter størrelsen på barnetillegget ditt. " +
                                            "For ".expr() + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikke bor sammen med begge foreldrene bruker vi kun inntekten din. ",
                                    Nynorsk to "Inntekta di har også betydning for kva du får utbetalt i barnetillegg. For".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge foreldra sine, bruker vi i tillegg " + epsTypeUbestemt(borMed) + " din si inntekt når vi fastset storleiken på barnetillegget ditt. " +
                                            "For ".expr() + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikkje bur saman med begge foreldra bruker vi berre inntekta di. ",
                                )
                            }
                        }.orShow {
                            text(
                                Bokmal to "Inntekten din har også betydning for hva du får utbetalt i barnetillegg. ",
                                Nynorsk to "Inntekta di har også betydning for kva du får utbetalt i barnetillegg. ",
                            )
                        }

                        showIf(innvilgetGJT) {
                            text(
                                Bokmal to "Uføretrygden og gjenlevendetillegget ditt regnes med som inntekt. ",
                                Nynorsk to "Uføretrygda og attlevandetillegget ditt er rekna med som inntekt. ",
                            )
                        }.orShow {
                            text(
                                Bokmal to "Uføretrygden din regnes med som inntekt. ",
                                Nynorsk to "Uføretrygda di er rekna med som inntekt. ",
                            )
                        }

                    }
                }

                showIf(innvilgetBTFB) {
                    paragraph {
                        ifNotNull(brukersInntektBruktIAvkortningAvBTFB, inntektAnnenForelderBTFB, borMed) { brukersInntektBruktIAvkortningAvBTFB, inntektAnnenForelderBTFB, borMed ->
                            textExpr(
                                Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + brukersInntektBruktIAvkortningAvBTFB.format() + " kroner og inntekten til din " + epsTypeUbestemt(borMed) + " på " + inntektAnnenForelderBTFB.format() + " kroner. ",
                                Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + brukersInntektBruktIAvkortningAvBTFB.format() + " kroner og inntekta til " + epsTypeUbestemt(borMed) + " din på " + inntektAnnenForelderBTFB.format() + " kroner. ",
                            )
                        }
                        ifNotNull(belopFratrukketAnnenForeldersInntektBTFB, borMed) { belopFratrukketAnnenForeldersInntektBTFB, borMed ->
                            showIf((belopFratrukketAnnenForeldersInntektBTFB.greaterThan(0))) {
                                textExpr(
                                    Bokmal to "Folketrygdens grunnbeløp på inntil ".expr() + grunnbelop.format() + " kroner er holdt utenfor din " + epsTypeUbestemt(borMed) + "s inntekt. ",
                                    Nynorsk to "Grunnbeløpet i folketrygda på inntil ".expr() + grunnbelop.format() + " kroner er held utanfor inntekta til " + epsTypeUbestemt(borMed) + " din. ",
                                )
                            }
                        }
                        ifNotNull(inntektBruktIAvkortningAvBTFB) { inntektBruktIAvkortningAvBTFB ->
                            textExpr(
                                Bokmal to "Til sammen utgjør disse inntektene ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner. ",
                                Nynorsk to "Til saman utgjer desse inntektene ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner. ",
                            )
                        }
                        ifNotNull(fribelopBTFB, justeringsbelopPerArBTFB, nettoBTFB) { fribelopBTFB, justeringsbelopPerArBTFB, nettoBTFB ->
                            showIf(justeringsbelopPerArBTFB.equalTo(0) and nettoBTFB.greaterThan(0))  {
                                showIf(inntektBruktIAvkortningAvBTFB.ifNull(0).greaterThan(fribelopBTFB)) {
                                    showIf(innvilgetBTFB and innvilgetBTSB and nettoBTSB.ifNull(0).greaterThan(0)) {
                                        textExpr(
                                            Bokmal to "Dette beløpet er høyere enn fribeløpsgrensen på ".expr() + fribelopBTFB.format() + " kroner. " +
                                                    "Derfor er barnetillegget for " + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre redusert. ",
                                            Nynorsk to "Dette beløpet er høgare enn fribeløpet på ".expr() + fribelopBTFB.format() + " kroner. " +
                                                    "Derfor er barnetillegget for " + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge sine foreldra redusert. ",
                                        )
                                    }.orShow {
                                        textExpr(
                                            Bokmal to "Dette beløpet er høyere enn fribeløpsgrensen på ".expr() + fribelopBTFB.format() + " kroner. Derfor er barnetillegget redusert. ",
                                            Nynorsk to "Dette beløpet er høgare enn fribeløpet på ".expr() + fribelopBTFB.format() + " kroner. Derfor er barnetillegget redusert. ",
                                        )
                                    }

                                }.orShow {
                                    showIf(innvilgetBTFB and innvilgetBTSB and nettoBTSB.ifNull(0).greaterThan(0)) {
                                        textExpr(
                                            Bokmal to "Dette beløpet er lavere enn fribeløpsgrensen på ".expr() + fribelopBTFB.format() + " kroner. " +
                                                    "Derfor er barnetillegget for " + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre ikke lenger redusert. ",
                                            Nynorsk to "Dette beløpet er lågare enn fribeløpet på ".expr() + fribelopBTFB.format() + " kroner. " +
                                                    "Derfor er barnetillegget for " + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge sine foreldra ikkje lenger redusert. ",
                                        )
                                    }.orShow {
                                        textExpr(
                                            Bokmal to "Dette beløpet er lavere enn fribeløpsgrensen på ".expr() + fribelopBTFB.format() + " kroner. Derfor er barnetillegget redusert. ",
                                            Nynorsk to "Dette beløpet er lågare enn fribeløpet på ".expr() + fribelopBTFB.format() + " kroner. Derfor er barnetillegget redusert. ",
                                        )
                                    }
                                }
                            }
                        }

                        showIf(justeringsbelopPerArBTFB.notEqualTo(0)) {
                            showIf(innvilgetBTSB and nettoBTSB.ifNull(0).greaterThan(0)) {
                                textExpr(
                                    Bokmal to ("Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. " +
                                            "Dette ble tatt hensyn til da vi endret barnetillegget for ").expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre. ",
                                    Nynorsk to ("Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. " +
                                            "Dette har vi teke omsyn til når vi endra barnetillegget for ").expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge foreldra sine. ",
                                )
                            }.orShow {
                                text(
                                    Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. ",
                                    Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. ",
                                )
                            }

                            showIf(nettoBTFB.equalTo(0)) {
                                text(
                                    Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. ",
                                    Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. ",
                                )
                            }
                        }
                    }
                }

                showIf(innvilgetBTSB and innvilgetBTFB) {
                    paragraph {
                        ifNotNull(inntektBruktIAvkortningAvBTSB) { inntektBruktIAvkortningAvBTSB ->
                            textExpr(
                                Bokmal to "Barnetillegget for ".expr() + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikke bor sammen med begge foreldrene er beregnet ut fra inntekten din på " + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                                Nynorsk to "Barnetillegget for ".expr() + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikkje bur saman med begge foreldra er berekna ut frå inntekta di på " + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                            )
                        }
                        ifNotNull(fribelopBTSB, justeringsbelopPerArBTSB, nettoBTSB) { fribelopBTSB, justeringsbelopPerArBTSB, nettoBTSB ->
                            showIf(justeringsbelopPerArBTSB.equalTo(0) and nettoBTSB.greaterThan(0)) {
                                showIf(inntektBruktIAvkortningAvBTSB.ifNull(0).greaterThan(fribelopBTSB)) {
                                    showIf(gammeltBelopBTFB.ifNull(0).greaterThan(nyttBelopBTFB.ifNull(0)) and gammeltBelopBTSB.ifNull(0).greaterThan(nyttBelopBTSB.ifNull(0))) {
                                        textExpr(
                                            Bokmal to "Dette er høyere enn fribeløpsgrensen på ".expr() + fribelopBTSB.format() + " kroner. Dette barnetillegget er derfor også redusert. ",
                                            Nynorsk to "Dette er høgare enn fribeløpet på ".expr() + fribelopBTSB.format() + " kroner. Derfor er barnetillegget også redusert. ",
                                        )
                                    }.orShow {
                                        textExpr(
                                            Bokmal to "Dette er høyere enn fribeløpsgrensen på ".expr() + fribelopBTSB.format() + " kroner. Dette barnetillegget er derfor redusert. ",
                                            Nynorsk to "Dette er høgare enn fribeløpet på ".expr() + fribelopBTSB.format() + " kroner. Derfor er barnetillegget redusert. ",
                                        )
                                    }
                                }.orShow {
                                    showIf(gammeltBelopBTFB.ifNull(0).greaterThan(nyttBelopBTFB.ifNull(0)) and gammeltBelopBTSB.ifNull(0).greaterThan(nyttBelopBTSB.ifNull(0))) {
                                        textExpr(
                                            Bokmal to "Dette er lavere enn fribeløpsgrensen på ".expr() + fribelopBTSB.format() + " kroner. Dette barnetillegget er derfor ikke lenger også redusert. ",
                                            Nynorsk to "Dette er lågare enn fribeløpet på ".expr() + fribelopBTSB.format() + " kroner. Derfor er barnetillegget ikkje lenger også redusert. ",
                                        )
                                    }.orShow {
                                        textExpr(
                                            Bokmal to "Dette er lavere enn fribeløpsgrensen på ".expr() + fribelopBTSB.format() + " kroner. Dette barnetillegget er derfor ikke lenger redusert. ",
                                            Nynorsk to "Dette er lågare enn fribeløpet på ".expr() + fribelopBTSB.format() + " kroner. Derfor er barnetillegget ikkje lenger redusert. ",
                                        )
                                    }
                                }
                            }
                        }

                        showIf(justeringsbelopPerArBTSB.notEqualTo(0)) {
                            showIf(nettoBTSB.ifNull(0).greaterThan(0)) {
                                textExpr(
                                    Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. ".expr() +
                                            "Dette ble tatt hensyn til da vi endret barnetillegget for " + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikke bor sammen med begge foreldrene. ",
                                    Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. ".expr() +
                                            "Dette har vi teke omsyn til når vi endra barnetillegget for " + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikkje bur saman med begge foreldra. ",
                                )
                            }.orShow {
                                text(
                                    Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. ",
                                    Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. ",
                                )
                            }
                        }
                    }
                }

                showIf(innvilgetBTSB and not(innvilgetBTFB)) {
                    paragraph {
                        ifNotNull(inntektBruktIAvkortningAvBTSB) { inntektBruktIAvkortningAvBTSB ->
                            textExpr(
                                Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                                Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                            )
                        }

                        showIf(justeringsbelopPerArBTSB.equalTo(0) and nettoBTSB.ifNull(0).greaterThan(0)) {
                            ifNotNull(fribelopBTSB) { fribelopBTSB ->
                                showIf(inntektBruktIAvkortningAvBTSB.ifNull(0).greaterThan(fribelopBTSB)) {
                                    textExpr(
                                        Bokmal to "Dette er høyere enn fribeløpsgrensen på ".expr() + fribelopBTSB.format() + " kroner. Barnetillegget er derfor redusert. ",
                                        Nynorsk to "Dette beløpet er høgare enn fribeløpet på ".expr() + fribelopBTSB.format() + " kroner. Derfor er barnetillegget redusert. ",
                                    )
                                }.orShow {
                                    textExpr(
                                        Bokmal to "Dette er lavere enn fribeløpsgrensen på ".expr() + fribelopBTSB.format() + " kroner. Barnetillegget er derfor ikke lenger redusert. ",
                                        Nynorsk to "Dette beløpet er lågare enn fribeløpet på ".expr() + fribelopBTSB.format() + " kroner. Derfor er barnetillegget ikkje lenger redusert. ",
                                    )
                                }
                            }
                        }.orShow {
                            showIf(nettoBTSB.ifNull(0).greaterThan(0)) {
                                text(
                                    Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. ",
                                    Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. ",
                                )
                            }.orShow {
                                text(
                                    Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. ",
                                    Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. ",
                                )
                            }
                        }
                    }
                }

                showIf(
                    nettoBTFB.equalTo(0)
                            and innvilgetBTFB
                            and justeringsbelopPerArBTFB.equalTo(0)
                ) {
                    paragraph {
                        ifNotNull(avkortningInntektstakBTFB, borMed) { avkortningInntektstakBTFB, borMed ->
                            showIf(innvilgetBTSB and innvilgetBTFB) {
                                textExpr(
                                    Bokmal to "Barnetillegget for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre blir ikke utbetalt fordi den samlede inntekten til deg og din " + epsTypeUbestemt(borMed) + " er høyere enn " + avkortningInntektstakBTFB.format() + " kroner. ",
                                    Nynorsk to "Barnetillegget for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge forelda sine blir ikkje utbetalt fordi den samla inntekta til deg og " + epsTypeUbestemt(borMed) + " din er høgare enn " + avkortningInntektstakBTFB.format() + " kroner. ",
                                )
                            }.orShow {
                                textExpr(
                                    Bokmal to "Barnetillegget blir ikke utbetalt fordi den samlede inntekten til deg og din ".expr() + epsTypeUbestemt(borMed) + " er høyere enn " + avkortningInntektstakBTFB.format() + " kroner. ",
                                    Nynorsk to "Barnetillegget blir ikkje utbetalt fordi den samla inntekta til deg og ".expr() + epsTypeUbestemt(borMed) + " din er høgare enn " + avkortningInntektstakBTFB.format() + " kroner. ",
                                )
                            }
                        }
                    }
                }

                showIf(nettoBTSB.equalTo(0) and innvilgetBTSB and justeringsbelopPerArBTSB.equalTo(0)) {
                    paragraph {
                        ifNotNull(avkortningInntektstakBTSB) { avkortningInntektstakBTSB ->
                            showIf(innvilgetBTSB and innvilgetBTFB) {
                                textExpr(
                                    Bokmal to "Barnetillegget for ".expr() + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikke bor sammen med begge foreldrene blir ikke utbetalt fordi inntekten din er høyere enn ".expr() + avkortningInntektstakBTSB.format() + " kroner. ",
                                    Nynorsk to "Barnetillegget for ".expr() + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikkje bur saman med begge foreldra blir ikkje utbetalt fordi inntekta di er høgare enn ".expr() + avkortningInntektstakBTSB.format() + " kroner. ",
                                )
                            }.orShow {
                                textExpr(
                                    Bokmal to "Barnetillegget blir ikke utbetalt fordi inntekten din er høyere enn ".expr() + avkortningInntektstakBTSB.format() + " kroner. ",
                                    Nynorsk to "Barnetillegget blir ikkje utbetalt fordi inntekta di er høgare enn ".expr() + avkortningInntektstakBTSB.format() + " kroner. ",
                                )
                            }
                        }
                    }
                }

                showIf(
                    not(virkningFomErForsteDagIAaret)
                            and (
                            periodisertInntektBTFB
                                    or periodisertInntektBTSB
                                    or periodisertFribelopBTFB
                                    or periodisertFribelopBTSB
                            )
                ) {
                    paragraph {
                        // TODO: er det rett å bruke periodisertinntekt her?
                        showIf((periodisertInntektBTFB or periodisertInntektBTSB) and (periodisertFribelopBTFB or periodisertFribelopBTSB)) {
                            showIf(innvilgetBTFB and innvilgetBTSB) {
                                showIf((not(periodisertInntektBTFB) and periodisertInntektBTSB) or (not(periodisertFribelopBTFB) and periodisertFribelopBTSB)) {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikke bor med begge sine foreldre hele året er inntektene og fribeløpet justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikkje bur saman med begge foreldra sine heile året, er inntektene og fribeløpet justert slik at dei berre gjeld for den perioden du får barnetillegg. ",
                                    )
                                }.orShow {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre hele året er inntektene og fribeløpet justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge foreldra sine heile året, er inntektene og fribeløpet justert slik at dei berre gjeld for den perioden du får barnetillegg. ",
                                    )
                                }
                            }.orShow {
                                text(
                                    Bokmal to "Fordi du ikke har barnetillegg hele året er inntektene og fribeløpet justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                    Nynorsk to "Fordi du ikkje har barnetillegg heile året, er inntektene og fribeløpet justert slik at dei berre gjeld for den perioden du får barnetillegg. ",
                                )
                            }
                            showIf(kravarsaksType.equalTo("sivilstandsendring")) {
                                text(
                                    Bokmal to "Fordi sivilstanden din har endret seg er inntektene og fribeløpet justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg. ",
                                    Nynorsk to "Fordi sivilstanden din har endra seg, er inntektene og fribeløpet justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg. ",
                                )
                            }
                        }.orShowIf(periodisertFribelopBTFB or periodisertFribelopBTSB) {
                            showIf(innvilgetBTFB and innvilgetBTSB) {
                                showIf((not(periodisertInntektBTFB) and periodisertInntektBTSB) or (not(periodisertFribelopBTFB) and periodisertFribelopBTSB)) {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikke bor med begge sine foreldre hele året er fribeløpet justert slik at det kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikkje bur saman med begge foreldra sine heile året, er fribeløpet justert slik at det berre gjeld for den perioden du får barnetillegg. ",
                                    )
                                }.orShow {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre hele året er fribeløpet justert slik at det kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge foreldra sine heile året, er fribeløpet justert slik at det berre gjeld for den perioden du får barnetillegg. ",
                                    )
                                }
                            }.orShow {
                                text(
                                    Bokmal to "Fordi du ikke har barnetillegg hele året er fribeløpet justert slik at det kun gjelder for den perioden du mottar barnetillegg. ",
                                    Nynorsk to "Fordi du ikkje har barnetillegg heile året, er fribeløpet justert slik at det berre gjeld for den perioden du får barnetillegg. ",
                                )
                            }
                            showIf(kravarsaksType.equalTo("sivilstandsendring")) {
                                text(
                                    Bokmal to "Fordi sivilstanden din har endret seg er fribeløpet justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg. ",
                                    Nynorsk to "Fordi sivilstanden din har endra seg, er fribeløpet justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg. ",
                                )
                            }
                        }.orShow {
                            showIf(innvilgetBTFB and innvilgetBTSB) {
                                showIf((not(periodisertInntektBTFB) and periodisertInntektBTSB) or (not(periodisertFribelopBTFB) and periodisertFribelopBTSB)) {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikke bor med begge sine foreldre hele året er inntektene justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som ikkje bur saman med begge foreldra sine heile året, er inntektene justert slik at dei berre gjeld for den perioden du får barnetillegg. ",
                                    )
                                }.orShow {
                                    textExpr(
                                        Bokmal to "Fordi du ikke har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre hele året er inntektene justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                        Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur saman med begge foreldra sine heile året, er inntektene justert slik at dei berre gjeld for den perioden du får barnetillegg. ",
                                    )
                                }
                            }.orShow {
                                text(
                                    Bokmal to "Fordi du ikke har barnetillegg hele året er inntektene justert slik at de kun gjelder for den perioden du mottar barnetillegg. ",
                                    Nynorsk to "Fordi du ikkje har barnetillegg heile året, er inntektene justert slik at dei berre gjeld for den perioden du får barnetillegg. ",
                                )
                            }
                            showIf(kravarsaksType.equalTo("sivilstandsendring")) {
                                text(
                                    Bokmal to "Fordi sivilstanden din har endret seg er inntektene justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg. ",
                                    Nynorsk to "Fordi sivilstanden din har endra seg, er inntektene justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg. ",
                                )
                            }
                        }
                    }
                }
            }

            showIf(avkortningInntektsgrense.lessThan(avkortningInntektstak)) {
                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om dette i vedlegget «Opplysninger om beregningen». ",
                        Nynorsk to "Du kan lese meir om dette i vedlegget «Opplysningar om berekninga». ",
                    )
                }
            }

            showIf(not(innvilgetBTSB) and not(innvilgetBTFB)) {
                showIf(not(innvilgetGJT) and not(innvilgetET)) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12. ",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12. ",
                        )
                    }
                }

                showIf(innvilgetGJT and not(innvilgetET)) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12- 18 og 22-12. ",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12. ",
                        )
                    }
                }

                showIf(not(innvilgetGJT) and innvilgetET) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12 og overgangsforskriften § 8. ",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12 og overgangsforskrifta § 8. ",
                        )
                    }
                }

            }.orShowIf(innvilgetBTSB or innvilgetBTFB) {
                showIf(not(innvilgetGJT) and not(innvilgetET) and gammeltBelop.ifNull(0).notEqualTo(nyttBelop.ifNull(0))) {
                    paragraph {
                        showIf(regelverktypeBT.equalTo("overgangsregler_2016")) {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden. ",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygda. ",
                            )
                        }.orShow {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12. ",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12. ",
                            )
                        }
                    }
                }

                showIf(not(innvilgetGJT) and innvilgetET) {
                    paragraph {
                        showIf(regelverktypeBT.equalTo("overgangsregler_2016")) {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og overgangsforskriften §8 og forskrift om overgangsregler for barnetillegg i uføretrygden. ",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og overgangsforskrifta § 8 og forskrift om overgangsregler for barnetillegg i uføretrygda. ",
                            )
                        }.orShow {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og overgangsforskriften §8. ",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og overgangsforskrifta § 8. ",
                            )
                        }
                    }
                }
                showIf(innvilgetGJT and not(innvilgetET)) {
                    paragraph {
                        showIf(regelverktypeBT.equalTo("overgangsregler_2016")) {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden. ",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12 og forskrift om overgangsreglar for barnetillegg i uføretrygda. ",
                            )
                        }.orShow {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12. ",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12. ",
                            )
                        }
                    }
                }

                showIf(not(innvilgetGJT) and not(innvilgetET) and not(endretUT)) {
                    paragraph {
                        showIf(regelverktypeBT.equalTo("overgangsregler_2016")) {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden. ",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12 og forskrift om overgangsreglar for barnetillegg i uføretrygda. ",
                            )
                        }.orShow {
                            text(
                                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12. ",
                                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12. ",
                            )
                        }
                    }
                }
            }

            showIf(endretUT and nyttBelop.ifNull(0).greaterThan(0)) {
                title1 {
                    text (
                        Bokmal to "Hva får du i uføretrygd framover?",
                        Nynorsk to "Kva får du i uføretrygd framover?",
                    )
                }

                showIf(utbetalingsgrad.notNull() and utbetalingsgrad.ifNull(0).lessThan(uforegrad) and avkortningInntektstak.greaterThan(avkortningInntektsgrense)) {
                    paragraph {
                        ifNotNull(totalNettoForAr) { totalNettoForAr ->
                            textExpr(
                                Bokmal to "Ut fra den årlige inntekten din vil uføretrygden utgjøre ".expr() + totalNettoForAr.format() + " kroner. ",
                                Nynorsk to "Ut frå den årlege inntekta di vil uføretrygda utgjere ".expr() + totalNettoForAr.format() + " kroner. ",
                            )
                        }

                        showIf(not(virkningFomErForsteDagIAaret)) {
                            ifNotNull(nettoAkk) { nettoAkk ->
                                textExpr(
                                    Bokmal to "Hittil i år har du fått utbetalt ".expr() + nettoAkk.format() + " kroner. ",
                                    Nynorsk to "Hittil i år har du fått utbetalt ".expr() + nettoAkk.format() + " kroner. ",
                                )
                            }
                        }
                        textExpr (
                            Bokmal to "Du har derfor rett til en utbetaling av uføretrygd på ".expr() + nettoPerManed.format() + " kroner per måned for resten av året. ",
                            Nynorsk to "Du har derfor rett til ei utbetaling av uføretrygd på ".expr() + nettoPerManed.format() + " kroner per månad for resten av året. ",
                        )
                    }
                }

                showIf(forventetInntekt.ifNull(0).greaterThan(avkortningInntektsgrense)) {
                    paragraph {
                        textExpr (
                            Bokmal to "Selv om uføretrygden din er redusert på grunn av inntekt beholder du likevel uføregraden din på ".expr() + uforegrad.format() + " prosent. " +
                                    "Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din. ",
                            Nynorsk to "Sjølv om uføretrygda di er redusert på grunn av inntekt beheld du likevel uføregraden din på ".expr() + uforegrad.format() + " prosent. " +
                                    "Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di. ",
                        )
                    }
                }
            }

            showIf(
                (endretBTFB and nyttBelopBTFB.ifNull(0).greaterThan(0))
                        or (endretBTSB and nyttBelopBTSB.ifNull(0).greaterThan(0))
            ) {
                title1 {
                    text (
                        Bokmal to "Hva får du i barnetillegg framover?",
                        Nynorsk to "Kva får du i barnetillegg framover?",
                    )
                }
                showIf(endretBTFB and nyttBelopBTFB.notEqualTo(0)) {
                    paragraph {
                        ifNotNull(arligAvkortningsbelopBTFB, borMed) { arligAvkortningsbelopBTFB, borMed ->
                            textExpr(
                                Bokmal to "Ut fra den samlede inntekten til deg og din ".expr() + epsTypeUbestemt(borMed) + " er barnetillegget vurdert på nytt. " +
                                        "Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er " + arligAvkortningsbelopBTFB.format() + " kroner. ",
                                Nynorsk to "Ut frå dei samla inntektene til deg og ".expr() + epsTypeUbestemt(borMed) + " din er barnetillegget blitt vurdert på nytt. " +
                                        "Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er " + arligAvkortningsbelopBTFB.format() + " kroner. ",
                            )
                        }

                        showIf(justeringsbelopPerArBTFB.notEqualTo(0)) {
                            ifNotNull(justeringsbelopPerArBTFB) { justeringsbelopPerArBTFB ->
                                showIf(justeringsbelopPerArBTFB.greaterThan(0)) {
                                    textExpr(
                                        Bokmal to "Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor lagt til ".expr() + justeringsbelopPerArBTFB.absoluteValue().format() + " kroner i det vi reduserer barnetillegget med for resten av året. ",
                                        Nynorsk to "Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor lagt til ".expr() + justeringsbelopPerArBTFB.absoluteValue().format() + " kroner i det vi har redusert barnetillegget med for resten av året. ",
                                    )
                                }.orShow {
                                    textExpr(
                                        Bokmal to "Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor trukket fra ".expr() + justeringsbelopPerArBTFB.absoluteValue().format() + " kroner i det vi reduserer barnetillegget med for resten av året. ",
                                        Nynorsk to "Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor trekt frå ".expr() + justeringsbelopPerArBTFB.absoluteValue().format() + " kroner i det vi har redusert barnetillegget med for resten av året. ",
                                    )
                                }
                            }
                        }
                        ifNotNull(nettoBTFB) { nettoBTFB ->
                            textExpr(
                                Bokmal to " Du har derfor rett til en utbetaling av barnetillegg på ".expr() + nettoBTFB.format() + " kroner per måned for resten av året. ",
                                Nynorsk to " Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + nettoBTFB.format() + " kroner per månad for resten av året. ",
                            )
                        }
                    }
                }

                showIf(endretBTSB and nyttBelopBTSB.notEqualTo(0)) {
                    paragraph {
                        ifNotNull(arligAvkortningsbelopBTSB) { arligAvkortningsbelopBTSB ->
                            textExpr(
                                Bokmal to ("Ut fra den samlede inntekten din er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. " +
                                        "Den årlige reduksjonen av barnetillegget er ").expr() + arligAvkortningsbelopBTSB.format() + " kroner. ",
                                Nynorsk to ("Ut frå den samla inntekta di er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. " +
                                        "Den årlege reduksjonen av barnetillegget er ").expr() + arligAvkortningsbelopBTSB.format() + " kroner. ",
                            )
                        }

                        showIf(justeringsbelopPerArBTSB.notEqualTo(0)) {
                            text(
                                Bokmal to "Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor ",
                                Nynorsk to "Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor ",
                            )
                            ifNotNull(justeringsbelopPerArBTSB) { justeringsbelopPerArBTSB ->
                                showIf(justeringsbelopPerArBTSB.greaterThan(0)) {
                                    textExpr(
                                        Bokmal to "Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor lagt til ".expr() + justeringsbelopPerArBTSB.absoluteValue().format() + " kroner i det vi reduserer barnetillegget med for resten av året. ",
                                        Nynorsk to "Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor lagt til ".expr() + justeringsbelopPerArBTSB.absoluteValue().format() + " kroner i det vi reduserte barnetillegget med for resten av året. ",
                                    )
                                }.orShow {
                                    textExpr(
                                        Bokmal to "Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor trukket fra ".expr() + justeringsbelopPerArBTSB.absoluteValue().format() + " kroner i det vi reduserer barnetillegget med for resten av året. ",
                                        Nynorsk to "Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor trekt frå ".expr() + justeringsbelopPerArBTSB.absoluteValue().format() + " kroner i det vi reduserte barnetillegget med for resten av året. ",
                                    )
                                }
                            }
                        }

                        ifNotNull(nettoBTSB) { nettoBTSB ->
                            textExpr(
                                Bokmal to " Du har derfor rett til en utbetaling av barnetillegg på ".expr() + nettoBTSB.format() + " kroner per måned for resten av året. ",
                                Nynorsk to " Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + nettoBTSB.format() + " kroner per månad for resten av året. ",
                            )
                        }
                    }
                }

                paragraph {
                    showIf(
                        nettoBTFB.equalTo(0)
                                and innvilgetBTFB
                                and (
                                not(innvilgetBTSB)
                                        or (innvilgetBTSB and nettoBTSB.notEqualTo(0))
                                )
                    ) {
                        textExpr(
                            Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bor med begge sine foreldre. ",
                            Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() + barnetEllerBarnaBestemt(antallBarnBTFB) + " som bur med begge foreldra. ",
                        )
                    }

                    showIf(
                        nettoBTSB.equalTo(0)
                                and innvilgetBTSB
                                and (
                                not(innvilgetBTFB)
                                        or (innvilgetBTFB and nettoBTFB.notEqualTo(0))
                                )
                    ) {
                        textExpr(
                            Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikke bor sammen med begge foreldrene. ",
                            Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() + barnetEllerBarnaBestemt(antallBarnBTSB) + " som ikkje bur saman med begge foreldra. ",
                        )
                    }
                }
            }

            showIf(innvilgetGJT) {
                title1 {
                    text(
                        Bokmal to "For deg som mottar gjenlevendetillegg",
                        Nynorsk to "For deg som mottar tillegg for attlevande ektefelle",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Du mottar gjenlevendetillegg i uføretrygden din. Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med. ",
                        Nynorsk to "Du får attlevandetillegg i uføretrygda di. Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosenten som vi reduserer uføretrygda di med. ",
                    )
                }

                showIf(belopRedusert and innvilgetGJT) {
                    paragraph {
                        text(
                            Bokmal to "Gjenlevendetillegget er redusert ut fra den innmeldte inntekten. ",
                            Nynorsk to "Attlevandetillegget er redusert ut frå den innmelde inntekta. ",
                        )
                    }
                }

                showIf(belopOkt and innvilgetGJT) {
                    paragraph {
                        text(
                            Bokmal to "Gjenlevendetillegget er økt ut fra den innmeldte inntekten. ",
                            Nynorsk to "Attlevandetillegget er auka ut frå den innmelde inntekta. ",
                        )
                    }
                }

                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om dette i vedlegget «Opplysninger om beregningen». ",
                        Nynorsk to "Du kan lese meir om dette i vedlegget «Opplysningar om berekninga». ",
                    )
                }
            }

            showIf(innvilgetET) {
                title1 {
                    text(
                        Bokmal to "For deg som mottar ektefelletillegg",
                        Nynorsk to "For deg som får ektefelletillegg",
                    )
                }
            }

            showIf(innvilgetET) {
                paragraph {
                    text(
                        Bokmal to "Du mottar ektefelletillegg i uføretrygden din. Dette tillegget blir ikke endret som følge av inntektsendringer. ",
                        Nynorsk to "Du får ektefelletillegg i uføretrygda di. Dette tillegget blir ikkje endra som følgje av inntektsendringar. ",
                    )
                }
            }

            title1 {
                text (
                    Bokmal to "Du må melde fra om endringer i inntekt",
                    Nynorsk to "Du må melde frå om endringar i inntekt",
                )
            }
            includePhrase(TBU2278_Generated(pe))

            paragraph {
                showIf(innvilgetBTSB or innvilgetBTFB) {
                    text(
                        Bokmal to "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden og barnetillegget blir så riktig som mulig. " +
                                "Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL. ",
                        Nynorsk to "Det er viktig at du melder frå om inntektsendringar, slik at uføretrygda og barnetillegget blir så riktig som mogleg. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» når du loggar deg inn på $NAV_URL. ",
                    )
                }.orShow {
                    text(
                        Bokmal to "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden blir så riktig som mulig. " +
                                "Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL. ",
                        Nynorsk to "Det er viktig at du melder frå om inntektsendringar, slik at uføretrygda blir så riktig som mogleg. " +
                                "Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» når du loggar deg inn på $NAV_URL. ",
                    )
                }
            }

            showIf(forventetInntekt.ifNull(0).lessThan(avkortningInntektstak)) {
                showIf(avkortningInntektstak.greaterThan(avkortningInntektsgrense)) {
                    paragraph {
                        textExpr(
                            Bokmal to "Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr()
                                    + avkortningInntektstak.format() + " kroner per år. Inntekten er justert opp til dagens verdi. ",
                            Nynorsk to "Vi gjer merksam på at det ikkje blir utbetalt uføretrygd når inntekta di utgjer meir enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie ".expr()
                                    + avkortningInntektstak.format() + " kroner per år. Inntekta er justert opp til dagens verdi. ",
                        )
                    }
                }.orShow {
                    paragraph {
                        textExpr(
                            Bokmal to "Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn inntektsgrensen din, det vil si ".expr()
                                    + avkortningInntektsgrense.format() + " kroner per år. ",
                            Nynorsk to "Vi gjer merksam på at det ikkje blir utbetalt uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil seie ".expr()
                                    + avkortningInntektsgrense.format() + " kroner per år. ",
                        )
                    }
                }
            }

            paragraph {
                showIf(innvilgetBTSB or innvilgetBTFB) {
                    text(
                        Bokmal to "Vi vil foreta et etteroppgjør hvis du har fått utbetalt for mye eller for lite uføretrygd og barnetillegg i løpet av året. " +
                                "Dette gjør vi når likningen fra Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd og barnetillegg, vil vi utbetale dette beløpet til deg. " +
                                "Hvis du har fått utbetalt for mye i uføretrygd og barnetillegg, må du betale dette tilbake. ",
                        Nynorsk to "Vi gjer eit etteroppgjer dersom du har fått utbetalt for mykje eller for lite uføretrygd og barnetillegg i løpet av året. " +
                                "Dette gjer vi når likninga frå Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd og barnetillegg, vil vi betale deg dette beløpet. " +
                                "Dersom du har fått utbetalt for mykje i uføretrygd og barnetillegg, må du betale dette tilbake. ",
                    )
                }.orShow {
                    text(
                        Bokmal to "Vi vil foreta et etteroppgjør hvis du har fått utbetalt for mye eller for lite uføretrygd i løpet av året. " +
                                "Dette gjør vi når likningen fra Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd, vil vi utbetale dette beløpet til deg. " +
                                "Hvis du har fått utbetalt for mye i uføretrygd, må du betale dette tilbake. ",
                        Nynorsk to "Vi gjer eit etteroppgjer dersom du har fått utbetalt for mykje eller for lite uføretrygd i løpet av året. " +
                                "Dette gjer vi når likninga frå Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd, vil vi betale deg dette beløpet. " +
                                "Dersom du har fått utbetalt for mykje i uføretrygd, må du betale dette tilbake. ",
                    )
                }
            }

            title1 {
                text (
                    Bokmal to "Inntekter som ikke skal føre til reduksjon av uføretrygden",
                    Nynorsk to "Inntekter som ikkje skal føra til reduksjon av uføretrygda",
                )
            }
            paragraph {
                text(
                    Bokmal to "Det kan gjøres unntak for enkelte inntektstyper som ikke skal føre til reduksjon av uføretrygden. Dette kan gjelde følgende: ",
                    Nynorsk to "Det kan gjerast unntak for enkelte inntektstypar som ikkje skal føra til reduksjon av uføretrygda. Dette kan gjelda følgjande: ",
                )
            }
            paragraph {
                text (
                    Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør etter ",
                    Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer etter ",
                )
                list {
                    item {
                        text(
                            Bokmal to "Skadeerstatningsloven § 3-1 ",
                            Nynorsk to "Skadeerstatningsloven § 3-1 ",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Yrkesskadeforsikringsloven § 13 ",
                            Nynorsk to "Yrkesskadeforsikringsloven § 13 ",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Pasientskadeloven § 4 første ledd ",
                            Nynorsk to "Pasientskadeloven § 4 første ledd ",
                        )
                    }
                }
                text (
                    Bokmal to "Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel: ",
                    Nynorsk to "Inntekt frå arbeid eller verksemd som vart heilt avslutta før du fekk innvilga uføretrygd, til dømes: ",
                )

                list {
                    item {
                        text(
                            Bokmal to "Utbetalte feriepenger for et arbeidsforhold som er avsluttet ",
                            Nynorsk to "Utbetalte feriepengar for eit arbeidsforhold som er avslutta ",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten ",
                            Nynorsk to "Inntekter frå sal av produksjonsmiddel i samband med opphøyr av verksemda ",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Produksjonstillegg og andre overføringer til gårdbrukere ",
                            Nynorsk to "Produksjonstillegg og andre overføringar til gardbrukarar ",
                        )
                    }
                }

                // Når feature toggelen slettes skal resten av if-en bli igjen, også det i orShow
                showIf(FeatureToggles.pl7822EndretInntekt.expr().enabled() and virkningFom.legacyGreaterThanOrEqual(LocalDate.of(2023, 1, 1)) and innvilgetBTFB) {
                    text(
                        Bokmal to "Hva kan holdes utenfor personinntekten til den andre forelderen?",
                        Nynorsk to "Kva kan haldast utanfor personinntekta til den andre forelderen?",
                    )

                    list {
                        item {
                            text(
                                Bokmal to "Erstatningsoppgjør for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon fra Nav",
                                Nynorsk to "Erstatningsoppgjer for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon frå Nav",
                            )
                        }
                    }
                    text(
                        Bokmal to "Dersom vi mottar dokumentasjon fra deg som bekrefter slik inntekt, kan vi gjøre en ny beregning.",
                        Nynorsk to "Dersom vi mottek dokumentasjon frå deg som stadfestar slik inntekt, kan vi gjera ei ny berekning.",
                    )
                }.orShow {
                    text(
                        Bokmal to "Dersom vi mottar dokumentasjon fra deg som bekrefter at du har slik inntekt, kan vi gjøre en ny beregning av uføretrygden din. ",
                        Nynorsk to "Dersom vi mottek dokumentasjon frå deg som stadfestar at du har slik inntekt, kan vi gjera ei ny berekning av uføretrygda di. ",
                    )
                }
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(brukerBorINorge))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe, pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}