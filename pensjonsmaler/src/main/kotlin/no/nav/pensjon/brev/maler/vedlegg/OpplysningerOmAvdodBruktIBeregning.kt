package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.Beregningsmetode.FOLKETRYGD
import no.nav.pensjon.brev.api.model.Beregningsmetode.NORDISK
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedSelectors.avdoedFnr
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedSelectors.navn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.beregningsMetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.faktiskTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.sluttpoengtall
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.sluttpoengtallMedOverkomp
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.sluttpoengtallUtenOverkomp
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.tellerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.beregningsMetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.faktiskTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.tellerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.avdoedFlyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BrukerSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.TilleggspensjonVedVirkSelectors.kombinertMedAvdod
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidsdetaljerKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidsdetaljerVedVirkNokkelInfo
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.tilleggspensjonVedVirk
import no.nav.pensjon.brev.maler.fraser.common.AntallAarText
import no.nav.pensjon.brev.maler.fraser.common.AntallMaanederText
import no.nav.pensjon.brev.maler.fraser.common.Ja
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

@TemplateModelHelpers
val opplysningerOmAvdoedBruktIBeregning =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>(
        title = newText(
            Bokmal to "Opplysninger om avdøde brukt i beregningen",
            Nynorsk to "Opplysningar om avdøde brukte i berekninga",
            English to "Information regarding the deceased that provides the basis for the calculation",
        ),
        includeSakspart = false,
    ) {


        val regelverkstypeAlderspensjon = alderspensjonVedVirk.regelverkType

        showIf(
            beregnetPensjonPerManedVedVirk.virkDatoFom.lessThan(LocalDate.of(2024, 1, 1))
                    or bruker.foedselsdato.lessThan(LocalDate.of(1944, 1, 1))
        ) {
            paragraph {
                textExpr(
                    Bokmal to "Som gjenlevende ektefelle har du fått en gunstigere beregning av alderspensjon. I tabellen nedenfor ser du hvilke opplysninger om ".expr() +
                            avdoed.navn + " som vi har lagt til grunn for beregningen.",
                    Nynorsk to "Som attlevande ektefelle har du fått ei gunstigare berekning av alderspensjonen. I tabellen nedanfor ser du kva opplysningar om ".expr() +
                            avdoed.navn + " som vi har lagt til grunn for berekninga.",
                    English to "As a surviving spouse, a more generous formula is used to calculate your retirement pension. The table below shows the information on ".expr() +
                            avdoed.navn + ", which we have used in this calculation.",
                )
            }
        }.orShow {
            paragraph {
                textExpr(
                    Bokmal to "Som gjenlevende ektefelle har du fått beregnet et gjenlevendetillegg i alderspensjonen din. I tabellen nedenfor ser du hvilke opplysninger om ".expr() +
                            avdoed.navn + " som vi har lagt til grunn for beregningen.",
                    Nynorsk to "Som attlevande ektefelle har du fått berekna attlevandetillegg i alderspensjonen. I tabellen nedanfor ser du kva opplysningar om den avdøde ektefellen din ".expr() +
                            avdoed.navn + " som vi har lagt til grunn for berekninga.",
                    English to "As a surviving spouse, a survivor's supplement in retirement pension has been calculated. The table below shows the information on your deceased spouse ".expr() +
                            avdoed.navn + ", which we have used in this calculation.",
                )
            }
        }
        paragraph {
            text(
                Bokmal to "Opplysninger om avdøde som ligger til grunn for beregningen:",
                Nynorsk to "Opplysningar om avdøde som er grunnlag for berekninga:",
                English to "Information regarding the deceased that provides the basis for the calculation:",
            )
        }
        showIf(regelverkstypeAlderspensjon.isOneOf(AP2011, AP2016)) {
            val beregningsmetode = avdoedTrygdetidsdetaljerKap19VedVirk.beregningsMetode
            paragraph {
                table(
                    {
                        column(columnSpan = 5) {
                            val virkFom = beregnetPensjonPerManedVedVirk.virkDatoFom.format(short = true)
                            textExpr(
                                Bokmal to "Opplysninger brukt i beregningen per ".expr() + virkFom,
                                Nynorsk to "Opplysningar brukte i berekninga frå ".expr() + virkFom,
                                English to "Information used to calculate as of ".expr() + virkFom,
                            )
                        }
                        column(columnSpan = 3, alignment = RIGHT) {}
                    }
                ) {
                    // vedleggTabellerAvdodFnr_001
                    row {
                        cell {
                            text(
                                Bokmal to "Fødselsnummer",
                                Nynorsk to "Fødselsnummer",
                                English to "The deceased's personal identification number",
                            )
                        }

                        cell { eval(avdoed.avdoedFnr.format()) }
                    }

                    // tabellAvdodFlyktningstatus_001
                    showIf(beregnetPensjonPerManedVedVirk.avdoedFlyktningstatusErBrukt) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Avdøde er registrert med flyktningestatus",
                                    Nynorsk to "Avdøde er registrert med flyktningestatus",
                                    English to "The deceased is registered with the status of a refugee",
                                )
                            }
                            cell { includePhrase(Ja) }
                        }
                    }
                    //vedleggTabellAvdodTT_001
                    showIf(beregningsmetode.isOneOf(FOLKETRYGD, NORDISK)) {
                        ifNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT) { anvendtTT ->
                            row {
                                cell {
                                    text(
                                        Bokmal to "Avdødes trygdetid",
                                        Nynorsk to "Trygdetida til avdøde",
                                        English to "Period of national insurance coverage",
                                    )
                                }
                                cell { includePhrase(AntallAarText(anvendtTT)) }
                            }
                        }
                    }


                    //vedleggTabellAvdodFaktiskTTnordisk_001
                    showIf(beregningsmetode.equalTo(NORDISK)) {
                        ifNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.faktiskTTNordiskKonv) { faktiskTTNordiskKonv ->
                            row {
                                cell {
                                    text(
                                        Bokmal to "Faktisk trygdetid i annet nordisk land som beregner framtidig trygdetid",
                                        Nynorsk to "Faktisk trygdetid i anna nordisk land som bereknarframtidig trygdetid",
                                        English to "Actual period of national insurance coverage in other Nordic countries that calculates future national insurance coverage",
                                    )
                                }
                                cell {
                                    includePhrase(AntallMaanederText(faktiskTTNordiskKonv))
                                }
                            }
                        }

                        //vedleggTabellAvdodKap19TTnorsk_001
                        ifNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTNorsk) { framtidigTTNorsk ->
                            row {
                                cell {
                                    text(
                                        Bokmal to "Framtidig trygdetid",
                                        Nynorsk to "Framtidig trygdetid",
                                        English to "Period of future national insurance coverage",
                                    )
                                }
                                cell { includePhrase(AntallMaanederText(framtidigTTNorsk)) }
                            }
                        }

                        //vedleggTabellFaktiskTTBrokNorgeNordisk_001
                        ifNotNull(
                            avdoedTrygdetidsdetaljerKap19VedVirk.tellerTTEOS,
                            avdoedTrygdetidsdetaljerKap19VedVirk.nevnerTTEOS
                        ) { teller, nevner ->
                            row {
                                cell {
                                    text(
                                        Bokmal to "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og annet nordisk land",
                                        Nynorsk to "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og anna nordisk land",
                                        English to "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and other Nordic countries",
                                    )
                                }
                                cell { eval(teller.format() + "/" + nevner.format()) }
                            }
                        }
                    }

                    //vedleggTabellAvdodKap19Sluttpoengtall_001

                    showIf(tilleggspensjonVedVirk.kombinertMedAvdod and beregningsmetode.equalTo(FOLKETRYGD)) {
                        showIf(
                            avdoedTrygdetidsdetaljerKap19VedVirk.sluttpoengtallMedOverkomp.isNull()
                                    and avdoedTrygdetidsdetaljerKap19VedVirk.sluttpoengtallUtenOverkomp.isNull()
                        ) {
                            ifNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.sluttpoengtall) { sluttpoengtall ->
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Sluttpoengtall",
                                            Nynorsk to "Sluttpoengtal",
                                            English to "Final pension point score",
                                        )
                                    }
                                    cell { eval(sluttpoengtall.format()) }
                                }
                            }
                        }

                        //vedleggTabellAvdodKap19SluttpoengtallMedOverkomp_001
                        ifNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.sluttpoengtallMedOverkomp) { sluttpoengtallMedOverkomp ->
                            row {
                                cell {
                                    text(
                                        Bokmal to "Sluttpoengtall med overkompensasjon",
                                        Nynorsk to "Sluttpoengtal med overkompensasjon",
                                        English to "Final pension point score with over-compensation",
                                    )
                                }
                                cell { eval(sluttpoengtallMedOverkomp.format()) }
                            }
                        }

                        //vedleggTabellAvdodKap19SluttpoengtallUtenOverkomp_001
                        ifNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.sluttpoengtallUtenOverkomp) { sluttpoengtallUtenOverkomp ->
                            row {
                                cell {
                                    text(
                                        Bokmal to "Sluttpoengtall uten overkompensasjon",
                                        Nynorsk to "Sluttpoengtal utan overkompensasjon",
                                        English to "Final pension point score without over-compensation",
                                    )
                                }
                                cell { eval(sluttpoengtallUtenOverkomp.format()) }
                            }
                        }


                    }


                }
            }


        }.orShowIf(regelverkstypeAlderspensjon.isOneOf(AP1967)) {
            paragraph {
                val beregningsmetode = avdoedTrygdetidsdetaljerVedVirkNokkelInfo.beregningsMetode
                table(
                    {
                        column(columnSpan = 5) {
                            val virkFom = beregnetPensjonPerManedVedVirk.virkDatoFom.format(short = true)
                            textExpr(
                                Bokmal to "Opplysninger brukt i beregningen per ".expr() + virkFom,
                                Nynorsk to "Opplysningar brukte i berekninga frå ".expr() + virkFom,
                                English to "Information used to calculate as of ".expr() + virkFom,
                            )
                        }
                        column(columnSpan = 3, alignment = RIGHT) {}
                    }
                ) {
                    // vedleggTabellerAvdodFnr_001
                    row {
                        cell {
                            text(
                                Bokmal to "Fødselsnummer",
                                Nynorsk to "Fødselsnummer",
                                English to "The deceased's personal identification number",
                            )
                        }

                        cell { eval(avdoed.avdoedFnr.format()) }
                    }

                    // tabellAvdodFlyktningstatus_001
                    showIf(beregnetPensjonPerManedVedVirk.avdoedFlyktningstatusErBrukt) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Avdøde er registrert med flyktningestatus",
                                    Nynorsk to "Avdøde er registrert med flyktningestatus",
                                    English to "The deceased is registered with the status of a refugee",
                                )
                            }
                            cell { includePhrase(Ja) }
                        }
                    }

                    //vedleggTabellAvdodTT1967_001
                    ifNotNull(avdoedTrygdetidsdetaljerVedVirkNokkelInfo.anvendtTT) { anvendtTT ->
                        showIf(
                            beregningsmetode.isOneOf(NORDISK, FOLKETRYGD)
                                    and anvendtTT.greaterThan(0)
                        ) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Avdødes trygdetid",
                                        Nynorsk to "Trygdetida til avdøde",
                                        English to "Period of national insurance coverage",
                                    )
                                }
                                cell { includePhrase(AntallMaanederText(anvendtTT)) }
                            }
                        }
                    }

                    //vedleggTabellAvdodFaktiskTTnordiskAP1967_001
                    ifNotNull(avdoedTrygdetidsdetaljerVedVirkNokkelInfo.faktiskTTNordiskKonv) { faktiskTTNordiskKonv ->
                        showIf(beregningsmetode.equalTo(NORDISK)) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Faktisk trygdetid i annet nordisk land som beregner framtidig trygdetid",
                                        Nynorsk to "Faktisk trygdetid i anna nordisk land som bereknarframtidig trygdetid",
                                        English to "Actual period of national insurance coverage in other Nordic countries that calculates future national insurance coverage",
                                    )
                                }
                                cell {
                                    includePhrase(AntallMaanederText(faktiskTTNordiskKonv))
                                }
                            }
                        }
                    }

                    //vedleggTabellAvdodKap19TTnorskAP1967_001
                    ifNotNull(avdoedTrygdetidsdetaljerVedVirkNokkelInfo.framtidigTTNorsk) { framtidigTTNorsk ->
                        showIf(beregningsmetode.equalTo(NORDISK)) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Framtidig trygdetid",
                                        Nynorsk to "Framtidig trygdetid",
                                        English to "Period of future national insurance coverage",
                                    )
                                }
                                cell { includePhrase(AntallMaanederText(framtidigTTNorsk)) }
                            }
                        }
                    }

                    //vedleggTabellFaktiskTTBrokNorgeNordiskAP1967_001
                    ifNotNull(
                        avdoedTrygdetidsdetaljerVedVirkNokkelInfo.tellerTTEOS,
                        avdoedTrygdetidsdetaljerVedVirkNokkelInfo.nevnerTTEOS
                    ) { teller, nevner ->
                        showIf(beregningsmetode.equalTo(NORDISK)) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og annet nordisk land",
                                        Nynorsk to "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og anna nordisk land",
                                        English to "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and other Nordic countries",
                                    )
                                }
                                cell { eval(teller.format() + "/" + nevner.format()) }
                            }
                        }
                    }
                }
            }
        }
        paragraph {
        }

    }