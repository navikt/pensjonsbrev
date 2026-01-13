package no.nav.pensjon.brev.alder.maler.vedlegg.opplysningeromavdodbruktiberegningen

import no.nav.pensjon.brev.alder.maler.felles.AntallAarText
import no.nav.pensjon.brev.alder.maler.felles.AntallMaanederText
import no.nav.pensjon.brev.alder.maler.felles.BroekText
import no.nav.pensjon.brev.alder.maler.felles.Ja
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.alder.model.Beregningsmetode.*
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.tilleggspensjonInnvilget
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.framtidigPoengAr
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.poengAr
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.poengArBroek
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.poengAre91
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.poengArf92
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.sluttpoengtall
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.sluttpoengtallMedOverkomp
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.sluttpoengtallUtenOverkomp
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.faktiskPoengArAvtale
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.faktiskPoengArNorge
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.framtidigPoengAr
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.poengAr
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.poengArBroek
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.poengAre91
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.poengArf92
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.sluttpoengtall
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.sluttpoengtallMedOverkomp
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.sluttpoengtallUtenOverkomp
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedSelectors.avdoedFnr
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.beregningsMetode
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.faktiskTTNordiskKonv
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.framtidigTTBilateral
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.framtidigTTEOS
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.trygdetidEOSBroek
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.anvendtTT
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.beregningsMetode
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.faktiskTTNordiskKonv
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.framtidigTTBilateral
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.framtidigTTEOS
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.proRataBroek
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.trygdetidEOSBroek
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedYrkesskadedetaljerVedVirkSelectors.poengAr
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedYrkesskadedetaljerVedVirkSelectors.poengAre91
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedYrkesskadedetaljerVedVirkSelectors.poengArf92
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedYrkesskadedetaljerVedVirkSelectors.sluttpoengtall
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedYrkesskadedetaljerVedVirkSelectors.yrkesskadeUforegrad
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.avdoedFlyktningstatusErBrukt
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.virkDatoFom
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.TilleggspensjonVedVirkSelectors.kombinertMedAvdoed
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.TilleggspensjonVedVirkSelectors.pgaUngUforeAvdod
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TableHeaderScope
import no.nav.pensjon.brev.template.dsl.TableScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Broek
import no.nav.pensjon.brevbaker.api.model.BroekSelectors.nevner
import no.nav.pensjon.brevbaker.api.model.BroekSelectors.teller
import no.nav.pensjon.brevbaker.api.model.Foedselsnummer
import java.time.LocalDate

data class OpplysningerOmAvdodTabell(
    val alderspensjonVedVirk: Expression<OpplysningerOmAvdoedBruktIBeregningDto.AlderspensjonVedVirk>,
    val tilleggspensjonVedVirk: Expression<OpplysningerOmAvdoedBruktIBeregningDto.TilleggspensjonVedVirk?>,
    val avdoedTrygdetidsdetaljerKap19VedVirk: Expression<OpplysningerOmAvdoedBruktIBeregningDto.AvdoedTrygdetidsdetaljerKap19VedVirk?>,
    val avdodBeregningKap3: Expression<OpplysningerOmAvdoedBruktIBeregningDto.AvdodBeregningKap3?>,
    val avdoedTrygdetidsdetaljerVedVirkNokkelInfo: Expression<OpplysningerOmAvdoedBruktIBeregningDto.AvdoedTrygdetidsdetaljerVedVirkNokkelInfo?>,
    val avdoedBeregningKap19VedVirk: Expression<OpplysningerOmAvdoedBruktIBeregningDto.AvdoedBeregningKap19VedVirk?>,
    val beregnetPensjonPerManedVedVirk: Expression<OpplysningerOmAvdoedBruktIBeregningDto.BeregnetPensjonPerManedVedVirk>,
    val avdoed: Expression<OpplysningerOmAvdoedBruktIBeregningDto.Avdoed>,
    val avdoedYrkesskadedetaljerVedVirk: Expression<OpplysningerOmAvdoedBruktIBeregningDto.AvdoedYrkesskadedetaljerVedVirk?>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {

    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val regelverkstype = alderspensjonVedVirk.regelverkType
        paragraph {
            text(
                bokmal { +"Opplysninger om avdøde som ligger til grunn for beregningen:" },
                nynorsk { +"Opplysningar om avdøde som er grunnlag for berekninga:" },
                english { +"Information regarding the deceased that provides the basis for the calculation:" },
            )
        }
        showIf(regelverkstype.isOneOf(AP2011, AP2016)) {
            val tillegspensjonKombinertMedAvdod = tilleggspensjonVedVirk.safe { kombinertMedAvdoed }.ifNull(false)
            paragraph {
                table(tabellHeadingOpplysningerBruktIBeregningAvdod(beregnetPensjonPerManedVedVirk.virkDatoFom)) {
                    // vedleggTabellerAvdodFnr_001
                    foedselsnummer(avdoed.avdoedFnr)

                    // tabellAvdodFlyktningstatus_001
                    flyktningstatusBrukt(beregnetPensjonPerManedVedVirk.avdoedFlyktningstatusErBrukt)

                    ifNotNull(avdoedTrygdetidsdetaljerKap19VedVirk) { avdoedTrygdetidsdetaljerKap19VedVirk ->
                        ifNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.beregningsMetode) { beregningsmetode ->
                            //vedleggTabellAvdodTT_001
                            showIf(beregningsmetode.isOneOf(FOLKETRYGD, NORDISK)) {
                                avdodesTrygdetid(avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT)


                                //vedleggTabellAvdodFaktiskTTnordisk_001
                                showIf(beregningsmetode.equalTo(NORDISK)) {
                                    faktiskTTNordiskKonvensjon(avdoedTrygdetidsdetaljerKap19VedVirk.faktiskTTNordiskKonv)

                                    //vedleggTabellAvdodKap19TTnorsk_001
                                    framtidigTTMaanederIfNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTNorsk)

                                    //vedleggTabellFaktiskTTBrokNorgeNordisk_001
                                    tabellFaktiskTTBroekNorgeNordisk(
                                        avdoedTrygdetidsdetaljerKap19VedVirk.trygdetidEOSBroek,
                                    )
                                }

                                showIf(tillegspensjonKombinertMedAvdod and beregningsmetode.equalTo(FOLKETRYGD)) {
                                    //vedleggTabellAvdodKap19Sluttpoengtall_001
                                    //vedleggTabellAvdodKap19SluttpoengtallMedOverkomp_001
                                    //vedleggTabellAvdodKap19SluttpoengtallUtenOverkomp_001

                                    sluttpoengTall(
                                        sluttpoengtallMedOverkomp = avdoedBeregningKap19VedVirk.safe { sluttpoengtallMedOverkomp },
                                        sluttpoengtallUtenOverkomp = avdoedBeregningKap19VedVirk.safe { sluttpoengtallUtenOverkomp },
                                        sluttpoengtall = avdoedBeregningKap19VedVirk.safe { sluttpoengtall },
                                    )

                                    //vedleggTabellKap19PoengAr_001
                                    antallPoengAar(avdoedBeregningKap19VedVirk.safe { poengAr })
                                }
                                //vedleggTabellKap19PoengArf92_001
                                showIf(tillegspensjonKombinertMedAvdod) {
                                    aarMed45og42pensjonsprosent(
                                        poengAarFoer92 = avdoedBeregningKap19VedVirk.safe { poengArf92 },
                                        poengAarEtter91 = avdoedBeregningKap19VedVirk.safe { poengAre91 },
                                    )

                                    //vedleggTabellAvdodKap19FaktiskePoengArNorge_001
                                    showIf(beregningsmetode.equalTo(NORDISK)) {
                                        antallFaktiskePoengAarINorge(avdoedBeregningKap19VedVirk.safe { faktiskPoengArNorge })
                                    }

                                    //vedleggTabellAvdodKap19FramtidigPoengar_001
                                    showIf(beregningsmetode.equalTo(FOLKETRYGD)) {
                                        norskeFramtidigePoengAar(avdoedBeregningKap19VedVirk.safe { framtidigPoengAr })
                                    }
                                }
                            }.orShowIf(beregningsmetode.equalTo(EOS)) {
                                //tabellTTNorgeEOS_001
                                samletTTNorgeOgEOS(avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT)

                                //vedleggTabellAvdodFramtidigTT_001
                                framtidigTTMaanederIfNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTEOS)

                                //tabellFaktiskTTBrokNorgeEOS_001
                                faktiskTTBroekNorgeEOS(avdoedTrygdetidsdetaljerKap19VedVirk.trygdetidEOSBroek)

                                showIf(tillegspensjonKombinertMedAvdod) {
                                    //vedleggAvdodTabellAntallPoengArFaktiskNorge_001
                                    antallFaktiskePoengAarINorge(avdoedBeregningKap19VedVirk.safe { faktiskPoengArNorge })
                                    //vedleggAvdodTabellAntallPoengarEOS_001
                                    antallAarIfNotNull(
                                        bokmal = "Antall poengår i andre EØS-land",
                                        nynorsk = "Talet på poengår i andre EØS- land",
                                        engelsk = "Number of point earning years in other EEA country",
                                        aar = avdoedBeregningKap19VedVirk.safe { faktiskPoengArAvtale },
                                    )

                                    //vedleggTabellKap19SluttpoengtallEOS_001
                                    //vedleggTabellAvdodSluttpoengMedOverkompEOS_001
                                    //vedleggTabellAvdodSluttpoengUtenOverkompEOS_001

                                    sluttpoengTall(
                                        suffixNorsk = " (EØS)",
                                        suffixEngelsk = " (EEA)",
                                        sluttpoengtallMedOverkomp = avdoedBeregningKap19VedVirk.safe { sluttpoengtallMedOverkomp },
                                        sluttpoengtallUtenOverkomp = avdoedBeregningKap19VedVirk.safe { sluttpoengtallUtenOverkomp },
                                        sluttpoengtall = avdoedBeregningKap19VedVirk.safe { sluttpoengtall }
                                    )

                                    //vedleggTabellKap19PoengArf92EOS_001
                                    //vedleggTabellKap19PoengAre91EOS_001
                                    aarMed45og42pensjonsprosent(
                                        poengAarFoer92 = avdoedBeregningKap19VedVirk.safe { poengArf92 },
                                        poengAarEtter91 = avdoedBeregningKap19VedVirk.safe { poengAre91 },
                                        bokmalSuffix = " (EØS)",
                                        nynorskSuffix = " (EØS)",
                                        engelskSuffix = " (EEA)"
                                    )
                                    //vedleggTabellAvdodFramtidigPoengTeoretisk_001
                                    framtidigePoengAar(avdoedBeregningKap19VedVirk.safe { framtidigPoengAr })

                                    //tabellPoengArBrokNorgeEOS_001
                                    poengAarBroekNorgeEOS(avdoedBeregningKap19VedVirk.safe { poengArBroek })
                                }
                            }.orShow {
                                //tabellTTNorgeAvtaleland_001
                                samletTrygdetidNorgeAvtaleland(avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT)

                                //vedleggTabellAvdodFramtidigTTBilateral_001
                                framtidigTTMaanederIfNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTBilateral)

                                //tabellTTBrokNorgeAvtaleland_001
                                trygdetidBroekNorgeAvtaleland(avdoedTrygdetidsdetaljerKap19VedVirk.trygdetidEOSBroek)


                                showIf(tillegspensjonKombinertMedAvdod) {
                                    //vedleggAvdodTabellAntallPoengArFaktiskNorge_001
                                    antallFaktiskePoengAarINorge(avdoedBeregningKap19VedVirk.safe { faktiskPoengArNorge })

                                    //vedleggTabellAvdodPoengAvtleland_001
                                    antallAarIfNotNull(
                                        bokmal = "Antall poengår i andre avtaleland",
                                        nynorsk = "Talet på poengår i andre avtaleland",
                                        engelsk = "Number of point earning years in country with social security agreement",
                                        aar = avdoedBeregningKap19VedVirk.safe { faktiskPoengArAvtale }
                                    )

                                    //vedleggTabellKap19SluttpoengtallAvtaleland_001
                                    //vedleggTabellAvdodKap19SluttpoengMedOverkompAvtaleland_001
                                    //vedleggTabellAvdodKap19SluttpoengUtenOverkompAvtaleland_001
                                    sluttpoengTall(
                                        suffixNorsk = " (avtaleland)",
                                        suffixEngelsk = " (earned in countries with social security agreement)",
                                        sluttpoengtallMedOverkomp = avdoedBeregningKap19VedVirk.safe { sluttpoengtallMedOverkomp },
                                        sluttpoengtallUtenOverkomp = avdoedBeregningKap19VedVirk.safe { sluttpoengtallUtenOverkomp },
                                        sluttpoengtall = avdoedBeregningKap19VedVirk.safe { sluttpoengtall }
                                    )
                                    aarMed45og42pensjonsprosent(
                                        poengAarFoer92 = avdoedBeregningKap19VedVirk.safe { poengArf92 },
                                        poengAarEtter91 = avdoedBeregningKap19VedVirk.safe { poengAre91 },
                                        bokmalSuffix = " (Norge og avtaleland)",
                                        nynorskSuffix = " (Noreg og avtaleland)",
                                        engelskSuffix = " (Norway and countries with social security agreement)"
                                    )
                                    //vedleggTabellAvdodFramtidigPoengTeoretisk_001
                                    framtidigePoengAar(avdoedBeregningKap19VedVirk.safe { framtidigPoengAr })

                                    //tabellPoengArBrokNorgeAvtaleland_001
                                    poengArBrokNorgeOgAvtaleland(avdoedBeregningKap19VedVirk.safe { poengArBroek })
                                }
                            }
                        }
                    }

                    //tabellUngUfor_002
                    tilleggspensjonPgaUngUfoer(tilleggspensjonVedVirk.safe { pgaUngUforeAvdod }.ifNull(false))
                    yrkesskade(avdoedYrkesskadedetaljerVedVirk)
                }
            }

        }.orShowIf(regelverkstype.equalTo(AP1967)) {
            paragraph {
                ifNotNull(avdoedTrygdetidsdetaljerVedVirkNokkelInfo) { avdoedNokkelinfo ->
                    val beregningsmetode = avdoedNokkelinfo.beregningsMetode
                    table(tabellHeadingOpplysningerBruktIBeregningAvdod(beregnetPensjonPerManedVedVirk.virkDatoFom)) {
                        // vedleggTabellerAvdodFnr_001
                        foedselsnummer(avdoed.avdoedFnr)

                        // tabellAvdodFlyktningstatus_001
                        flyktningstatusBrukt(beregnetPensjonPerManedVedVirk.avdoedFlyktningstatusErBrukt)

                        //vedleggTabellAvdodTT1967_001
                        showIf(beregningsmetode.isOneOf(NORDISK, FOLKETRYGD)) {

                            showIf(avdoedNokkelinfo.anvendtTT.ifNull(0).greaterThan(0)) {
                                avdodesTrygdetid(avdoedNokkelinfo.anvendtTT)
                            }

                            showIf(beregningsmetode.equalTo(NORDISK)) {
                                //vedleggTabellAvdodFaktiskTTnordiskAP1967_001
                                faktiskTTNordiskKonvensjon(avdoedNokkelinfo.faktiskTTNordiskKonv)

                                //vedleggTabellAvdodKap19TTnorskAP1967_001
                                framtidigTTMaanederIfNotNull(avdoedNokkelinfo.framtidigTTNorsk)

                                //vedleggTabellFaktiskTTBrokNorgeNordiskAP1967_001
                                tabellFaktiskTTBroekNorgeNordisk(avdoedNokkelinfo.trygdetidEOSBroek)
                            }
                            showIf(
                                beregningsmetode.equalTo(FOLKETRYGD)
                                        and alderspensjonVedVirk.tilleggspensjonInnvilget
                                        and alderspensjonVedVirk.gjenlevenderettAnvendt
                            ) {
                                sluttpoengTall(
                                    sluttpoengtallMedOverkomp = avdodBeregningKap3.safe { sluttpoengtallMedOverkomp },
                                    sluttpoengtallUtenOverkomp = avdodBeregningKap3.safe { sluttpoengtallUtenOverkomp },
                                    sluttpoengtall = avdodBeregningKap3.safe { sluttpoengtall }
                                )
                                //vedleggTabellKap19PoengArAP1967_001
                                antallPoengAar(avdodBeregningKap3.safe { poengAr })

                                //vedleggTabellKap19PoengArf92AP1967_001
                                //vedleggTabellKap19PoengAre91AP1967_001
                                aarMed45og42pensjonsprosent(
                                    poengAarFoer92 = avdodBeregningKap3.safe { poengArf92 },
                                    poengAarEtter91 = avdodBeregningKap3.safe { poengAre91 },
                                )

                                //vedleggTabellAvdodKap19FramtidigPoengarAP1967
                                norskeFramtidigePoengAar(avdodBeregningKap3.safe { framtidigPoengAr })
                            }
                        }.orShowIf(beregningsmetode.equalTo(EOS)) {
                            //tabellTTNorgeEOSap1967_001
                            samletTTNorgeOgEOS(avdoedNokkelinfo.anvendtTT)

                            //vedleggTabellAvdodFramtidigTTap1967_001
                            framtidigTTMaanederIfNotNull(avdoedNokkelinfo.framtidigTTEOS)

                            //tabellFaktiskTTBrokNorgeEOSap1967_001
                            faktiskTTBroekNorgeEOS(avdoedNokkelinfo.trygdetidEOSBroek)
                            showIf(
                                alderspensjonVedVirk.tilleggspensjonInnvilget
                                        and alderspensjonVedVirk.gjenlevenderettAnvendt
                            ) {
                                //vedleggTabellKap19SluttpoengtallEOSap1967_001
                                //vedleggTabellAvdodSluttpoengMedOverkompEOSap1967_001
                                //vedleggTabellAvdodSluttpoengUtenOverkompEOSap1967_001
                                sluttpoengTall(
                                    suffixNorsk = " (EØS)",
                                    suffixEngelsk = " (EEA)",
                                    sluttpoengtallMedOverkomp = avdodBeregningKap3.safe { sluttpoengtallMedOverkomp },
                                    sluttpoengtallUtenOverkomp = avdodBeregningKap3.safe { sluttpoengtallUtenOverkomp },
                                    sluttpoengtall = avdodBeregningKap3.safe { sluttpoengtall },
                                )

                                //vedleggTabellKap19PoengArf92EOSap1967_001
                                //vedleggTabellKap19PoengAre91EOSap1967_001
                                aarMed45og42pensjonsprosent(
                                    poengAarFoer92 = avdodBeregningKap3.safe { poengArf92 },
                                    poengAarEtter91 = avdodBeregningKap3.safe { poengAre91 },
                                    bokmalSuffix = " (EØS)",
                                    nynorskSuffix = " (EØS)",
                                    engelskSuffix = " (EEA)"
                                )

                                //vedleggTabellAvdodFramtidigPoengTeoretiskAP1967_001
                                framtidigePoengAar(avdodBeregningKap3.safe { framtidigPoengAr })

                                //tabellPoengArBrokNorgeEOSap1967_001
                                poengAarBroekNorgeEOS(avdodBeregningKap3.safe { poengArBroek })
                            }
                        }.orShow {
                            //tabellTTNorgeAvtalelandAP1967_001
                            samletTrygdetidNorgeAvtaleland(avdoedNokkelinfo.anvendtTT)

                            //vedleggTabellAvdodFramtidigTTBilateralAP1967_001
                            framtidigTTMaanederIfNotNull(avdoedNokkelinfo.framtidigTTBilateral)

                            //tabellTTBrokNorgeAvtalelandAP1967_001
                            trygdetidBroekNorgeAvtaleland(avdoedNokkelinfo.proRataBroek)

                            showIf(
                                alderspensjonVedVirk.tilleggspensjonInnvilget
                                        and alderspensjonVedVirk.gjenlevenderettAnvendt
                            ) {
                                //vedleggTabellKap19SluttpoengtallAvtalelandAP1967_001
                                //vedleggTabellAvdodKap19SluttpoengMedOverkompAvtalelandAP1967_001
                                //vedleggTabellAvdodKap19SluttpoengUtenOverkompAvtalelandAP1967_001

                                sluttpoengTall(
                                    suffixNorsk = " (avtaleland)",
                                    suffixEngelsk = " (earned in countries with social security agreement)",
                                    sluttpoengtallMedOverkomp = avdodBeregningKap3.safe { sluttpoengtallMedOverkomp },
                                    sluttpoengtallUtenOverkomp = avdodBeregningKap3.safe { sluttpoengtallUtenOverkomp },
                                    sluttpoengtall = avdodBeregningKap3.safe { sluttpoengtall },
                                )

                                //vedleggTabellKap19PoengArf92AvtalelandAP1967_001
                                //vedleggTabellKap19PoengAre91AvtalelandAP1967_001
                                aarMed45og42pensjonsprosent(
                                    poengAarFoer92 = avdodBeregningKap3.safe { poengArf92 },
                                    poengAarEtter91 = avdodBeregningKap3.safe { poengAre91 },
                                    bokmalSuffix = " (Norge og avtaleland)",
                                    nynorskSuffix = " (Noreg og avtaleland)",
                                    engelskSuffix = " (Norway and countries with social security agreement)"
                                )

                                //vedleggTabellAvdodFramtidigPoengTeoretiskAP1967_001
                                framtidigePoengAar(avdodBeregningKap3.safe { framtidigPoengAr })

                                //tabellPoengArBrokNorgeAvtalelandAP1967_001
                                poengArBrokNorgeOgAvtaleland(avdodBeregningKap3.safe { poengArBroek })
                            }
                        }
                        tilleggspensjonPgaUngUfoer(tilleggspensjonVedVirk.safe { pgaUngUforeAvdod }.ifNull(false))
                        yrkesskade(avdoedYrkesskadedetaljerVedVirk)
                    }
                }
            }
        }
    }

}


private fun TableScope<LangBokmalNynorskEnglish, Unit>.poengArBrokNorgeOgAvtaleland(broek: Expression<Broek?>) {
    broekIfNotNull(
        bokmal = "Forholdet mellom antall poengår i Norge og antall poengår i Norge og avtaleland",
        nynorsk = "Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og avtaleland",
        engelsk = "Ratio between the number of point earning years in Norway and the number of point earning years in Norway and countries with social security agreement",
        broek = broek,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.trygdetidBroekNorgeAvtaleland(broek: Expression<Broek?>) {
    broekIfNotNull(
        bokmal = "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og avtaleland",
        nynorsk = "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og avtaleland",
        engelsk = "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and countries with social security agreement",
        broek = broek,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.samletTrygdetidNorgeAvtaleland(anvendtTT: Expression<Int?>) {
    antallAarIfNotNull(
        bokmal = "Samlet trygdetid i Norge og avtaleland",
        nynorsk = "Samla trygdetid i Noreg og avtaleland",
        engelsk = "Total period of national insurance coverage in Norway and countries with social security agreement",
        aar = anvendtTT
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.poengAarBroekNorgeEOS(broek: Expression<Broek?>) {
    broekIfNotNull(
        bokmal = "Forholdet mellom antall poengår i Norge og antall poengår i Norge og annet EØS-land",
        nynorsk = "Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og anna EØS-land",
        engelsk = "The ratio between point earning years in Norway and total point earning years in all EEA countries",
        broek = broek,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.faktiskTTBroekNorgeEOS(
    broek: Expression<Broek?>
) {
    broekIfNotNull(
        bokmal = "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og andre EØS-land",
        nynorsk = "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og andre EØS-land",
        engelsk = "The ratio between national insurance coverage in Norway and total insurance coverage in all EEA countries",
        broek = broek,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.samletTTNorgeOgEOS(
    anvendtTT: Expression<Int?>
) {
    antallAarIfNotNull(
        "Samlet trygdetid i Norge og andre EØS-land",
        "Samla trygdetid i Noreg og andre EØS-land",
        "Total national insurance coverage in Norway and other EEA countries",
        anvendtTT,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.norskeFramtidigePoengAar(
    poengAar: Expression<Int?>
) {
    showIf(poengAar.ifNull(0).greaterThan(0)) {
        antallAarIfNotNull(
            bokmal = "Norske framtidige poengår",
            nynorsk = "Norske framtidige poengår",
            engelsk = "Future point earning years in Norway",
            aar = poengAar
        )
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.antallPoengAar(antallPoengAar: Expression<Int?>) {
    antallAarIfNotNull(
        "Antall poengår",
        "Talet på poengår",
        "Number of pension point earning years",
        antallPoengAar
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.tabellFaktiskTTBroekNorgeNordisk(
    broek: Expression<Broek?>,
) {
    broekIfNotNull(
        bokmal = "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og annet nordisk land",
        nynorsk = "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og anna nordisk land",
        engelsk = "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and other Nordic countries",
        broek = broek,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.faktiskTTNordiskKonvensjon(
    faktiskTrygdetidNordiskKonvensjon: Expression<Int?>
) {
    antallMaanederIfNotNull(
        "Faktisk trygdetid i annet nordisk land som beregner framtidig trygdetid",
        "Faktisk trygdetid i anna nordisk land som bereknarframtidig trygdetid",
        "Actual period of national insurance coverage in other Nordic countries that calculates future national insurance coverage",
        faktiskTrygdetidNordiskKonvensjon
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.avdodesTrygdetid(
    anvendtTrygdetid: Expression<Int?>
) {
    antallAarIfNotNull(
        "Avdødes trygdetid",
        "Trygdetida til avdøde",
        "Period of national insurance coverage",
        anvendtTrygdetid
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.flyktningstatusBrukt(flyktningstatusErBrukt: Expression<Boolean>) {
    showIf(flyktningstatusErBrukt) {
        row {
            cell {
                text(
                    bokmal { +"Avdøde er registrert med flyktningestatus" },
                    nynorsk { +"Avdøde er registrert med flyktningestatus" },
                    english { +"The deceased is registered with the status of a refugee" },
                )
            }
            cell { includePhrase(Ja) }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.foedselsnummer(avdodFnr: Expression<Foedselsnummer>) {
    row {
        cell {
            text(
                bokmal { +"Fødselsnummer" },
                nynorsk { +"Fødselsnummer" },
                english { +"The deceased's personal identification number" },
            )
        }

        cell { eval(avdodFnr.format()) }
    }
}

private fun tabellHeadingOpplysningerBruktIBeregningAvdod(virkFom: Expression<LocalDate>): TableHeaderScope<LangBokmalNynorskEnglish, Unit>.() -> Unit =
    {
        column(columnSpan = 5) {
            text(
                bokmal { +"Opplysninger brukt i beregningen per " + virkFom.format(short = true) },
                nynorsk { +"Opplysningar brukte i berekninga frå " + virkFom.format(short = true) },
                english { +"Information used to calculate as of " + virkFom.format(short = true) },
            )
        }
        column(columnSpan = 3, alignment = RIGHT) {}
    }

private fun TableScope<LangBokmalNynorskEnglish, Unit>.tilleggspensjonPgaUngUfoer(pgaUngUforeAvdod: Expression<Boolean>) {
    showIf(pgaUngUforeAvdod) {
        row {
            cell {
                text(
                    bokmal { +"Ung ufør" },
                    nynorsk { +"Ung ufør" },
                    english { +"Young person with disabilities" },
                )
            }
            cell { includePhrase(Ja) }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.yrkesskade(
    avdoedYrkesskadedetaljerVedVirk: Expression<OpplysningerOmAvdoedBruktIBeregningDto.AvdoedYrkesskadedetaljerVedVirk?>
) {
    //tabellYrkesskadeSluttpoengtall_001
    ifNotNull(avdoedYrkesskadedetaljerVedVirk.safe { sluttpoengtall }) {
        row {
            cell {
                text(
                    bokmal { +"Sluttpoengtall ved yrkesskade" },
                    nynorsk { +"Sluttpoengtal ved yrkesskade" },
                    english { +"Final pension point score on occupational injury" },
                )
            }
            cell { eval(it.format()) }
        }
    }
    //tabellYrkesskadePoengAr_001
    antallAarIfNotNull(
        bokmal = "Antall poengår benyttet ved yrkesskadeberegningen",
        nynorsk = "Talet på poengår benyttet ved yrkesskadeberekning",
        engelsk = "Number of pension point earning years used in the calculation of occupational injury",
        aar = avdoedYrkesskadedetaljerVedVirk.safe { poengAr }
    )

    //tabellPoengArf92Yrkesskade_001
    //tabellPoengAre91Yrkesskade_001
    aarMed45og42pensjonsprosent(
        poengAarFoer92 = avdoedYrkesskadedetaljerVedVirk.safe { poengArf92 },
        poengAarEtter91 = avdoedYrkesskadedetaljerVedVirk.safe { poengAre91 },
        bokmalSuffix = " benyttet ved yrkesskadeberegning",
        nynorskSuffix = " brukt ved yrkesskadeberekning",
        engelskSuffix = " used in the calculation of occupational injury"
    )


    //tabellYrkesskadeUforegrad_001
    ifNotNull(avdoedYrkesskadedetaljerVedVirk.safe { yrkesskadeUforegrad }) {
        row {
            cell {
                text(
                    bokmal { +"Yrkesskade uføregrad" },
                    nynorsk { +"Yrkesskade uføregrad" },
                    english { +"Occupational injury - degree of disability" },
                )
            }
            cell { eval(it.format() + " %") }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.framtidigePoengAar(
    framtidigePoengAar: Expression<Int?>
) {
    ifNotNull(framtidigePoengAar) {
        showIf(it.greaterThan(0)) {
            row {
                cell {
                    text(
                        bokmal { +"Antall framtidige poengår" },
                        nynorsk { +"Talet på framtidige poengår" },
                        english { +"Future point earning years" },
                    )
                }
                cell { includePhrase(AntallAarText(it)) }
            }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.aarMed45og42pensjonsprosent(
    poengAarFoer92: Expression<Int?>,
    poengAarEtter91: Expression<Int?>,
    bokmalSuffix: String = "",
    nynorskSuffix: String = "",
    engelskSuffix: String = "",
) {
    antallAarIfNotNull(
        "Antall år med pensjonsprosent 45$bokmalSuffix",
        "Talet på år med pensjonsprosent 45$nynorskSuffix",
        "Number of years calculated with pension percentage 45$engelskSuffix",
        poengAarFoer92
    )


    //vedleggTabellKap19PoengAre91_001
    antallAarIfNotNull(
        "Antall år med pensjonsprosent 42$bokmalSuffix",
        "Talet på år med pensjonsprosent 42$nynorskSuffix",
        "Number of years calculated with pension percentage 42$engelskSuffix",
        poengAarEtter91
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.sluttpoengTall(
    suffixNorsk: String = "",
    suffixEngelsk: String = "",
    sluttpoengtallMedOverkomp: Expression<Double?>,
    sluttpoengtallUtenOverkomp: Expression<Double?>,
    sluttpoengtall: Expression<Double?>
) {
    showIf(sluttpoengtallMedOverkomp.isNull() and sluttpoengtallUtenOverkomp.isNull()) {
        ifNotNull(sluttpoengtall) {
            row {
                cell {
                    text(
                        bokmal { +"Sluttpoengtall$suffixNorsk" },
                        nynorsk { +"Sluttpoengtal$suffixNorsk" },
                        english { +"Final pension point score$suffixEngelsk" },
                    )

                }
                cell { eval(it.format()) }
            }
        }

        ifNotNull(sluttpoengtallMedOverkomp) {
            row {
                cell {
                    text(
                        bokmal { +"Sluttpoengtall med overkompensasjon$suffixNorsk" },
                        nynorsk { +"Sluttpoengtal med overkompensasjon$suffixNorsk" },
                        english { +"Final pension point score with over-compensation$suffixEngelsk" },
                    )
                }
                cell { eval(it.format()) }
            }
        }

        ifNotNull(sluttpoengtallUtenOverkomp) {
            row {
                cell {
                    text(
                        bokmal { +"Sluttpoengtall uten overkompensasjon$suffixNorsk" },
                        nynorsk { +"Sluttpoengtal utan overkompensasjon$suffixNorsk" },
                        english { +"Final pension point score without over-compensation$suffixEngelsk" },
                    )
                }
                cell { eval(it.format()) }
            }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.antallFaktiskePoengAarINorge(
    aar: Expression<Int?>
) {
    antallAarIfNotNull(
        bokmal = "Antall faktiske poengår i Norge",
        nynorsk = "Talet på faktiske poengår i Noreg",
        engelsk = "Number of point earning years in Norway",
        aar = aar
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.framtidigTTMaanederIfNotNull(
    framtidigTTmaaneder: Expression<Int?>
) {
    antallMaanederIfNotNull(
        bokmalTekst = "Framtidig trygdetid",
        nynorskTekst = "Framtidig trygdetid",
        engelskTekst = "Period of future national insurance coverage",
        maaneder = framtidigTTmaaneder
    )
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.broekIfNotNull(
    bokmal: String,
    nynorsk: String,
    engelsk: String,
    broek: Expression<Broek?>,
) {
    ifNotNull(broek) {
        row {
            cell { text(bokmal { +bokmal }, nynorsk { +nynorsk }, english { +engelsk }) }
            cell { includePhrase(BroekText(it.teller, it.nevner)) }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.antallAarIfNotNull(
    bokmal: String, nynorsk: String, engelsk: String, aar: Expression<Int?>
) {
    ifNotNull(aar) {
        row {
            cell {
                text(
                    bokmal { +bokmal },
                    nynorsk { +nynorsk },
                    english { +engelsk },
                )
            }
            cell { includePhrase(AntallAarText(it)) }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, Unit>.antallMaanederIfNotNull(
    bokmalTekst: String, nynorskTekst: String, engelskTekst: String, maaneder: Expression<Int?>
) {
    ifNotNull(maaneder) {
        row {
            cell {
                text(
                    bokmal { +bokmalTekst },
                    nynorsk { +nynorskTekst },
                    english { +engelskTekst },
                )
            }
            cell { includePhrase(AntallMaanederText(it)) }
        }
    }
}