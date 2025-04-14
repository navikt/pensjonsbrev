package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.Beregningsmetode.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.faktiskPoengArAvtale_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.faktiskPoengArNorge_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.framtidigPoengAr_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedBeregningKap19VedVirkSelectors.poengArNevner_safe
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
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.framtidigTTBilateral
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.framtidigTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.nevnerProRata
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.tellerProRata
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.tellerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.beregningsMetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.faktiskTTNordiskKonv
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.tellerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedYrkesskadedetaljerVedVirkSelectors.poengAr_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedYrkesskadedetaljerVedVirkSelectors.poengAre91_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedYrkesskadedetaljerVedVirkSelectors.poengArf92_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedYrkesskadedetaljerVedVirkSelectors.sluttpoengtall_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedYrkesskadedetaljerVedVirkSelectors.yrkesskadeUforegrad_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.avdoedFlyktningstatusErBrukt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BrukerSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.TilleggspensjonVedVirkSelectors.kombinertMedAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.TilleggspensjonVedVirkSelectors.pgaUngUforeAvdod
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedYrkesskadedetaljerVedVirk
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
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
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
                            framtidigTTMaanederIfNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTNorsk)

                            //vedleggTabellFaktiskTTBrokNorgeNordisk_001
                            broekIfNotNull(
                                bokmal = "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og annet nordisk land",
                                nynorsk = "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og anna nordisk land",
                                engelsk = "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and other Nordic countries",
                                teller = avdoedTrygdetidsdetaljerKap19VedVirk.tellerTTEOS,
                                nevner = avdoedTrygdetidsdetaljerKap19VedVirk.nevnerTTEOS
                            )
                        }

                        showIf(tillegspensjonKombinertMedAvdod and beregningsmetode.equalTo(FOLKETRYGD)) {
                            //vedleggTabellAvdodKap19Sluttpoengtall_001
                            //vedleggTabellAvdodKap19SluttpoengtallMedOverkomp_001
                            //vedleggTabellAvdodKap19SluttpoengtallUtenOverkomp_001

                            sluttpoengtallKap19()

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
                            aarMed45og42pensjonProsentKap19()

                            //vedleggTabellAvdodKap19FaktiskePoengArNorge_001
                            antallFaktiskePoengAarINorge(avdoedBeregningKap19VedVirk.faktiskPoengArNorge_safe)

                            //vedleggTabellAvdodKap19FramtidigPoengar_001
                            ifNotNull(avdoedBeregningKap19VedVirk.framtidigPoengAr_safe) { framtidigPoengAr ->
                                showIf(
                                    beregningsmetode.isOneOf(FOLKETRYGD)
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
                        }.orShowIf(beregningsmetode.equalTo(EOS)) {
                            //tabellTTNorgeEOS_001
                            antallAarIfNotNull(
                                "Samlet trygdetid i Norge og andre EØS-land",
                                "Samla trygdetid i Noreg og andre EØS-land",
                                "Total national insurance coverage in Norway and other EEA countries",
                                avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT,
                            )

                            //vedleggTabellAvdodFramtidigTT_001
                            framtidigTTMaanederIfNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTEOS)

                            //tabellFaktiskTTBrokNorgeEOS_001
                            broekIfNotNull(
                                bokmal = "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og andre EØS-land",
                                nynorsk = "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og andre EØS-land",
                                engelsk = "The ratio between national insurance coverage in Norway and total insurance coverage in all EEA countries",
                                teller = avdoedTrygdetidsdetaljerKap19VedVirk.tellerTTEOS,
                                nevner = avdoedTrygdetidsdetaljerKap19VedVirk.nevnerTTEOS,
                            )

                            showIf(tillegspensjonKombinertMedAvdod) {
                                //vedleggAvdodTabellAntallPoengArFaktiskNorge_001
                                antallFaktiskePoengAarINorge(avdoedBeregningKap19VedVirk.faktiskPoengArNorge_safe)
                                //vedleggAvdodTabellAntallPoengarEOS_001
                                antallAarIfNotNull(
                                    bokmal = "Antall poengår i andre EØS-land",
                                    nynorsk = "Talet på poengår i andre EØS- land",
                                    engelsk = "Number of point earning years in other EEA country",
                                    aar = avdoedBeregningKap19VedVirk.faktiskPoengArAvtale_safe,
                                )

                                //vedleggTabellKap19SluttpoengtallEOS_001
                                //vedleggTabellAvdodSluttpoengMedOverkompEOS_001
                                //vedleggTabellAvdodSluttpoengUtenOverkompEOS_001

                                sluttpoengtallKap19(
                                    suffixNorsk = " (EØS)",
                                    suffixEngelsk = " (EEA)"
                                )

                                //vedleggTabellKap19PoengArf92EOS_001
                                //vedleggTabellKap19PoengAre91EOS_001
                                aarMed45og42pensjonProsentKap19(
                                    bokmalSuffix = " (EØS)",
                                    nynorskSuffix = " (EØS)",
                                    engelskSuffix = " (EEA)"
                                )
                                //vedleggTabellAvdodFramtidigPoengTeoretisk_001
                                framtidigePoengAarKap19()

                                //tabellPoengArBrokNorgeEOS_001
                                broekIfNotNull(
                                    bokmal = "Forholdet mellom antall poengår i Norge og antall poengår i Norge og annet EØS-land",
                                    nynorsk = "Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og anna EØS-land",
                                    engelsk = "The ratio between point earning years in Norway and total point earning years in all EEA countries",
                                    teller = avdoedBeregningKap19VedVirk.poengArTeller_safe,
                                    nevner = avdoedBeregningKap19VedVirk.poengArNevner_safe,
                                )
                            }
                        }.orShow {
                            //tabellTTNorgeAvtaleland_001
                            antallAarIfNotNull(
                                bokmal = "Samlet trygdetid i Norge og avtaleland",
                                nynorsk = "Samla trygdetid i Noreg og avtaleland",
                                engelsk = "Total period of national insurance coverage in Norway and countries with social security agreement",
                                aar = avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT
                            )

                            //vedleggTabellAvdodFramtidigTTBilateral_001
                            framtidigTTMaanederIfNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTBilateral)

                            //tabellTTBrokNorgeAvtaleland_001
                            broekIfNotNull(
                                bokmal = "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og avtaleland",
                                nynorsk = "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og avtaleland",
                                engelsk = "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and countries with social security agreement",
                                teller = avdoedTrygdetidsdetaljerKap19VedVirk.tellerProRata,
                                nevner = avdoedTrygdetidsdetaljerKap19VedVirk.nevnerProRata,
                            )


                            showIf(tillegspensjonKombinertMedAvdod) {
                                //vedleggAvdodTabellAntallPoengArFaktiskNorge_001
                                antallFaktiskePoengAarINorge(avdoedBeregningKap19VedVirk.faktiskPoengArNorge_safe)

                                //vedleggTabellAvdodPoengAvtleland_001
                                antallAarIfNotNull(
                                    bokmal = "Antall poengår i andre avtaleland",
                                    nynorsk = "Talet på poengår i andre avtaleland",
                                    engelsk = "Number of point earning years in country with social security agreement",
                                    aar = avdoedBeregningKap19VedVirk.faktiskPoengArAvtale_safe
                                )

                                //vedleggTabellKap19SluttpoengtallAvtaleland_001
                                //vedleggTabellAvdodKap19SluttpoengMedOverkompAvtaleland_001
                                //vedleggTabellAvdodKap19SluttpoengUtenOverkompAvtaleland_001
                                sluttpoengtallKap19(
                                    suffixNorsk = " (avtaleland)",
                                    suffixEngelsk = " (earned in countries with social security agreement)"
                                )
                                aarMed45og42pensjonProsentKap19(
                                    bokmalSuffix = " (Norge og avtaleland)",
                                    nynorskSuffix = " (Noreg og avtaleland) ",
                                    engelskSuffix = " (Norway and countries with social security agreement)"
                                )
                                //vedleggTabellAvdodFramtidigPoengTeoretisk_001
                                framtidigePoengAarKap19()

                                broekIfNotNull(
                                    bokmal = "Forholdet mellom antall poengår i Norge og antall poengår i Norge og avtaleland",
                                    nynorsk = "Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og avtaleland",
                                    engelsk = "Ratio between the number of point earning years in Norway and the number of point earning years in Norway and countries with social security agreement",
                                    teller = avdoedBeregningKap19VedVirk.poengArTeller_safe,
                                    nevner = avdoedBeregningKap19VedVirk.poengArNevner_safe,
                                )
                            }
                        }
                    }

                    //tabellUngUfor_002
                    showIf(tilleggspensjonVedVirk.pgaUngUforeAvdod) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Ung ufør",
                                    Nynorsk to "Ung ufør",
                                    English to "Young person with disabilities",
                                )
                            }
                            cell { includePhrase(Ja) }
                        }
                    }
                    //tabellYrkesskadeSluttpoengtall_001
                    ifNotNull(avdoedYrkesskadedetaljerVedVirk.sluttpoengtall_safe) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Sluttpoengtall ved yrkesskade",
                                    Nynorsk to "Sluttpoengtal ved yrkesskade",
                                    English to "Final pension point score on occupational injury",
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
                        aar = avdoedYrkesskadedetaljerVedVirk.poengAr_safe
                    )

                    //tabellPoengArf92Yrkesskade_001
                    antallAarIfNotNull(
                        bokmal = "Antall år med pensjonsprosent 45 benyttet ved yrkesskadeberegning",
                        nynorsk = "Talet på år med pensjonsprosent 45 brukt ved yrkesskadeberekning",
                        engelsk = "Number of years with pension percentage 45 used in the calculation of occupational injury",
                        aar = avdoedYrkesskadedetaljerVedVirk.poengArf92_safe
                    )
                    //tabellPoengAre91Yrkesskade_001
                    antallAarIfNotNull(
                        bokmal = "Antall år med pensjonsprosent 42 benyttet ved yrkesskadeberegning",
                        nynorsk = "Talet på år med pensjonsprosent 42 brukt ved yrkesskadeberekning",
                        engelsk = "Number of years with pension percentage 42 used in the calculation of occupational injury",
                        aar = avdoedYrkesskadedetaljerVedVirk.poengAre91_safe
                    )
                    
                    ifNotNull(avdoedYrkesskadedetaljerVedVirk.yrkesskadeUforegrad_safe) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Yrkesskade uføregrad",
                                    Nynorsk to "Yrkesskade uføregrad",
                                    English to "Occupational injury - degree of disability",
                                )
                            }
                            cell { eval(it.format() + " %")}
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
                    showIf(beregningsmetode.equalTo(NORDISK)) {
                        framtidigTTMaanederIfNotNull(avdoedTrygdetidsdetaljerVedVirkNokkelInfo.framtidigTTNorsk)
                    }

                    //vedleggTabellFaktiskTTBrokNorgeNordiskAP1967_001
                    broekIfNotNull(
                        bokmal = "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og annet nordisk land",
                        nynorsk = "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og anna nordisk land",
                        engelsk = "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and other Nordic countries",
                        teller = avdoedTrygdetidsdetaljerVedVirkNokkelInfo.tellerTTEOS,
                        nevner = avdoedTrygdetidsdetaljerVedVirkNokkelInfo.nevnerTTEOS
                    )
                }
            }
        }

    }

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.framtidigePoengAarKap19() {
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
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.aarMed45og42pensjonProsentKap19(
    bokmalSuffix: String = "",
    nynorskSuffix: String = "",
    engelskSuffix: String = "",
) {
    aarMed45Eller42pensjonsprosent(
        avdoedBeregningKap19VedVirk.poengArf92_safe,
        avdoedBeregningKap19VedVirk.poengAre91_safe,
        bokmalSuffix,
        nynorskSuffix,
        engelskSuffix,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.aarMed45Eller42pensjonsprosent(
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

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.sluttpoengtallKap19(
    suffixNorsk: String = "",
    suffixEngelsk: String = "",
) {
    sluttpoengTall(
        suffixNorsk = suffixNorsk,
        suffixEngelsk = suffixEngelsk,
        sluttpoengtallMedOverkomp = avdoedBeregningKap19VedVirk.sluttpoengtallMedOverkomp_safe,
        sluttpoengtallUtenOverkomp = avdoedBeregningKap19VedVirk.sluttpoengtallUtenOverkomp_safe,
        sluttpoengtall = avdoedBeregningKap19VedVirk.sluttpoengtall_safe
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.sluttpoengTall(
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
                        Bokmal to "Sluttpoengtall$suffixNorsk",
                        Nynorsk to "Sluttpoengtal$suffixNorsk",
                        English to "Final pension point score$suffixEngelsk",
                    )

                }
                cell { eval(it.format()) }
            }
        }

        ifNotNull(sluttpoengtallMedOverkomp) {
            row {
                cell {
                    text(
                        Bokmal to "Sluttpoengtall med overkompensasjon$suffixNorsk",
                        Nynorsk to "Sluttpoengtal med overkompensasjon$suffixNorsk",
                        English to "Final pension point score with over-compensation$suffixEngelsk",
                    )
                }
                cell { eval(it.format()) }
            }
        }

        ifNotNull(sluttpoengtallUtenOverkomp) {
            row {
                cell {
                    text(
                        Bokmal to "Sluttpoengtall uten overkompensasjon$suffixNorsk",
                        Nynorsk to "Sluttpoengtal utan overkompensasjon$suffixNorsk",
                        English to "Final pension point score without over-compensation$suffixEngelsk",
                    )
                }
                cell { eval(it.format()) }
            }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.antallFaktiskePoengAarINorge(
    aar: Expression<Int?>
) {
    antallAarIfNotNull(
        bokmal = "Antall faktiske poengår i Norge",
        nynorsk = "Talet på faktiske poengår i Noreg",
        engelsk = "Number of point earning years in Norway",
        aar = aar
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.framtidigTTMaanederIfNotNull(
    framtidigTTmaaneder: Expression<Int?>
) {
    antallMaanederIfNotNull(
        bokmalTekst = "Framtidig trygdetid",
        nynorskTekst = "Framtidig trygdetid",
        engelskTekst = "Period of future national insurance coverage",
        maaneder = framtidigTTmaaneder
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.broekIfNotNull(
    bokmal: String,
    nynorsk: String,
    engelsk: String,
    teller: Expression<Int?>,
    nevner: Expression<Int?>,
) {
    ifNotNull(teller, nevner) { teller, nevner ->
        row {
            cell { text(Bokmal to bokmal, Nynorsk to nynorsk, English to engelsk) }
            cell { includePhrase(BroekText(teller, nevner)) }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.antallAarIfNotNull(
    bokmal: String, nynorsk: String, engelsk: String, aar: Expression<Int?>
) {
    ifNotNull(aar) {
        row {
            cell {
                text(
                    Bokmal to bokmal,
                    Nynorsk to nynorsk,
                    English to engelsk,
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
