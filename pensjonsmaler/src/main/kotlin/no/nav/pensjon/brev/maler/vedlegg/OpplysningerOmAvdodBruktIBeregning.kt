package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.Beregningsmetode.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.faktiskPoengArAvtale_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.faktiskPoengArNorge_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.framtidigPoengAr_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.poengArTeller_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.poengAr_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.poengAre91_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.poengArf92_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.sluttpoengtallMedOverkomp_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.sluttpoengtallUtenOverkomp_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.sluttpoengtall_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedSelectors.avdoedFnr
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedSelectors.navn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.beregningsMetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.faktiskTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.framtidigTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.nevnerTTEOS
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
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.TilleggspensjonVedVirkSelectors.kombinertMedAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedBeregningKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidsdetaljerKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidsdetaljerVedVirkNokkelInfo
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.tilleggspensjonVedVirk
import no.nav.pensjon.brev.maler.fraser.common.AntallAarText
import no.nav.pensjon.brev.maler.fraser.common.AntallMaanederText
import no.nav.pensjon.brev.maler.fraser.common.BroekText
import no.nav.pensjon.brev.maler.fraser.common.Ja
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.TableScope
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
            val tillegspensjonKombinertMedAvdod = tilleggspensjonVedVirk.kombinertMedAvdoed
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
                    ifNotNull(avdoedTrygdetidsdetaljerKap19VedVirk) { avdoedTrygdetidsdetaljerKap19VedVirk ->
                        val beregningsmetode = avdoedTrygdetidsdetaljerKap19VedVirk.beregningsMetode
                        //vedleggTabellAvdodTT_001
                        showIf(beregningsmetode.isOneOf(FOLKETRYGD, NORDISK)) {
                            antallAarIfNotNull(
                                "Avdødes trygdetid",
                                "Trygdetida til avdøde",
                                "Period of national insurance coverage",
                                avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT
                            )
                        }

                        //vedleggTabellAvdodFaktiskTTnordisk_001
                        showIf(beregningsmetode.equalTo(NORDISK)) {
                            antallMaanederIfNotNull(
                                "Faktisk trygdetid i annet nordisk land som beregner framtidig trygdetid",
                                "Faktisk trygdetid i anna nordisk land som bereknarframtidig trygdetid",
                                "Actual period of national insurance coverage in other Nordic countries that calculates future national insurance coverage",
                                avdoedTrygdetidsdetaljerKap19VedVirk.faktiskTTNordiskKonv
                            )

                            //vedleggTabellAvdodKap19TTnorsk_001
                            antallMaanederIfNotNull(
                                "Framtidig trygdetid",
                                "Framtidig trygdetid",
                                "Period of future national insurance coverage",
                                avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTNorsk
                            )

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
                                    cell { includePhrase(BroekText(teller, nevner)) }
                                }
                            }
                        }

                        //vedleggTabellAvdodKap19Sluttpoengtall_001
                        showIf(tillegspensjonKombinertMedAvdod and beregningsmetode.equalTo(FOLKETRYGD)) {
                            showIf(
                                avdoedBeregningKap19VedVirk.sluttpoengtallMedOverkomp_safe.isNull()
                                        and avdoedBeregningKap19VedVirk.sluttpoengtallUtenOverkomp_safe.isNull()
                            ) {
                                ifNotNull(avdoedBeregningKap19VedVirk.sluttpoengtall_safe) { sluttpoengtall ->
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
                            ifNotNull(avdoedBeregningKap19VedVirk.sluttpoengtallMedOverkomp_safe) { sluttpoengtallMedOverkomp ->
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
                            ifNotNull(avdoedBeregningKap19VedVirk.sluttpoengtallUtenOverkomp_safe) { sluttpoengtallUtenOverkomp ->
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

                            //vedleggTabellKap19PoengAr_001
                            antallAarIfNotNull(
                                "Antall poengår",
                                "Talet på poengår",
                                "Number of pension point earning years",
                                avdoedBeregningKap19VedVirk.poengAr_safe
                            )
                        }
                        //vedleggTabellKap19PoengArf92_001
                        showIf(beregningsmetode.isOneOf(FOLKETRYGD, NORDISK) and tillegspensjonKombinertMedAvdod) {
                            antallAarIfNotNull(
                                "Antall år med pensjonsprosent 45",
                                "Talet på år med pensjonsprosent 45",
                                "Number of years calculated with pension percentage 45",
                                avdoedBeregningKap19VedVirk.poengArf92_safe
                            )

                            //vedleggTabellKap19PoengAre91_001
                            antallAarIfNotNull(
                                "Antall år med pensjonsprosent 42",
                                "Talet på år med pensjonsprosent 42",
                                "Number of years calculated with pension percentage 42",
                                avdoedBeregningKap19VedVirk.poengAre91_safe
                            )

                            //vedleggTabellAvdodKap19FaktiskePoengArNorge_001
                            antallAarIfNotNull(
                                "Antall faktiske poengår i Norge",
                                "Talet på faktiske poengår i Noreg",
                                "Point earning years in Norway",
                                avdoedBeregningKap19VedVirk.faktiskPoengArNorge_safe
                            )
                        }
                        //vedleggTabellAvdodKap19FramtidigPoengar_001
                        ifNotNull(avdoedBeregningKap19VedVirk.framtidigPoengAr_safe) { framtidigPoengAr ->
                            showIf(
                                tillegspensjonKombinertMedAvdod
                                        and beregningsmetode.isOneOf(FOLKETRYGD)
                                        and framtidigPoengAr.ifNull(0).greaterThan(0)
                            ) {
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Norske framtidige poengår",
                                            Nynorsk to "Norske framtidige poengår",
                                            English to "Future point earning years in Norway",
                                        )
                                    }
                                    cell { includePhrase(AntallAarText(framtidigPoengAr)) }
                                }
                            }
                        }


                        //tabellTTNorgeEOS_001
                        showIf(beregningsmetode.equalTo(EOS)) {
                            antallAarIfNotNull(
                                "Samlet trygdetid i Norge og andre EØS-land",
                                "Samla trygdetid i Noreg og andre EØS-land",
                                "Total national insurance coverage in Norway and other EEA countries",
                                avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT,
                            )

                            //vedleggTabellAvdodFramtidigTT_001
                            antallMaanederIfNotNull(
                                "Framtidig trygdetid",
                                "Framtidig trygdetid",
                                "Period of future national insurance cover",
                                avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTEOS
                            )

                            //tabellFaktiskTTBrokNorgeEOS_001
                            ifNotNull(
                                avdoedTrygdetidsdetaljerKap19VedVirk.tellerTTEOS,
                                avdoedTrygdetidsdetaljerKap19VedVirk.nevnerTTEOS
                            ) { teller, nevner ->
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og andre EØS-land",
                                            Nynorsk to "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og andre EØS-land",
                                            English to "The ratio between national insurance coverage in Norway and total insurance coverage in all EEA countries",
                                        )
                                    }
                                    cell { includePhrase(BroekText(teller, nevner)) }
                                }
                            }

                            showIf(tillegspensjonKombinertMedAvdod) {
                                //vedleggAvdodTabellAntallPoengArFaktiskNorge_001
                                antallAarIfNotNull(
                                    bokmalTekst = "Antall faktiske poengår i Norge",
                                    nynorskTekst = "Talet på faktiske poengår i Noreg",
                                    engelskTekst = "Number of point earning years in Norway",
                                    aar = avdoedBeregningKap19VedVirk.faktiskPoengArNorge_safe,
                                )
                                //vedleggAvdodTabellAntallPoengarEOS_001
                                antallAarIfNotNull(
                                    bokmalTekst = "Antall poengår i andre EØS-land",
                                    nynorskTekst = "Talet på poengår i andre EØS- land",
                                    engelskTekst = "Number of point earning years in other EEA country",
                                    aar = avdoedBeregningKap19VedVirk.faktiskPoengArAvtale_safe,
                                )

                                //vedleggTabellKap19SluttpoengtallEOS_001
                                showIf(
                                    avdoedBeregningKap19VedVirk.sluttpoengtallUtenOverkomp_safe.isNull()
                                            and avdoedBeregningKap19VedVirk.sluttpoengtallMedOverkomp_safe.isNull()
                                ) {
                                    ifNotNull(avdoedBeregningKap19VedVirk.sluttpoengtall_safe) {
                                        row {
                                            cell {
                                                text(
                                                    Bokmal to "Sluttpoengtall (EØS)",
                                                    Nynorsk to "Sluttpoengtal (EØS)",
                                                    English to "Final pension point score (EEA)",
                                                )
                                            }
                                            cell { it.format() }
                                        }
                                    }
                                }

                                //vedleggTabellAvdodSluttpoengMedOverkompEOS_001
                                ifNotNull(avdoedBeregningKap19VedVirk.sluttpoengtallMedOverkomp_safe) {
                                    row {
                                        cell {
                                            text(
                                                Bokmal to "Sluttpoengtall med overkompensasjon (EØS)",
                                                Nynorsk to "Sluttpoengtal med overkompensasjon (EØS)",
                                                English to "Final pension point score with over-compensation (EEA)",
                                            )
                                        }
                                        cell { eval(it.format()) }
                                    }
                                }
                                //vedleggTabellAvdodSluttpoengUtenOverkompEOS_001
                                ifNotNull(avdoedBeregningKap19VedVirk.sluttpoengtallUtenOverkomp_safe) {
                                    row {
                                        cell {
                                            text(
                                                Bokmal to "Sluttpoengtall uten overkompensasjon (EØS)",
                                                Nynorsk to "Sluttpoengtal uten overkompensasjon (EØS)",
                                                English to "Final pension point score without over-compensation (EEA)",
                                            )
                                        }
                                        cell { eval(it.format()) }
                                    }
                                }
                                //vedleggTabellKap19PoengArf92EOS_001
                                antallAarIfNotNull(
                                    "Antall år med pensjonsprosent 45 (EØS)",
                                    "Talet på år med pensjonsprosent 45 (EØS)",
                                    "Number of years calculated with pension percentage 45 (EEA)",
                                    avdoedBeregningKap19VedVirk.poengArf92_safe,
                                )
                                //vedleggTabellKap19PoengAre91EOS_001
                                antallAarIfNotNull(
                                    "Antall år med pensjonsprosent 42 (EØS)",
                                    "Talet på år med pensjonsprosent 42 (EØS)",
                                    "Number of years calculated with pension percentage 42 (EEA)",
                                    avdoedBeregningKap19VedVirk.poengAre91_safe,
                                )
                                //vedleggTabellAvdodFramtidigPoengTeoretisk_001
                                ifNotNull(avdoedBeregningKap19VedVirk.framtidigPoengAr_safe) { framtidigPoengAr ->
                                    showIf(framtidigPoengAr.greaterThan(0)) {
                                        row {
                                            cell {
                                                text(
                                                    Bokmal to "Antall framtidige poengår",
                                                    Nynorsk to "Talet på framtidige poengår",
                                                    English to "Future point earning years",
                                                )
                                            }
                                            cell { includePhrase(AntallAarText(framtidigPoengAr)) }
                                        }
                                    }
                                }

                                //ifNotNull(avdoedBeregningKap19VedVirk.poengArTeller)


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
                                cell { includePhrase(BroekText(teller, nevner)) }
                            }
                        }
                    }
                }
            }
        }
        paragraph {
        }

    }

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.antallAarIfNotNull(
    bokmalTekst: String, nynorskTekst: String, engelskTekst: String, aar: Expression<Int?>
) {
    ifNotNull(aar) {
        row {
            cell {
                text(
                    Bokmal to bokmalTekst,
                    Nynorsk to nynorskTekst,
                    English to engelskTekst,
                )
            }
            cell { includePhrase(AntallAarText(it)) }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.antallMaanederIfNotNull(
    bokmalTekst: String, nynorskTekst: String, engelskTekst: String, maaneder: Expression<Int?>
) {
    ifNotNull(maaneder) {
        row {
            cell {
                text(
                    Bokmal to bokmalTekst,
                    Nynorsk to nynorskTekst,
                    English to engelskTekst,
                )
            }
            cell { includePhrase(AntallMaanederText(it)) }
        }
    }
}