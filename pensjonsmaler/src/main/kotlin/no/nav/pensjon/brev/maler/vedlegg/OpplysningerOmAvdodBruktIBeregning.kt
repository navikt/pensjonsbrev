package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.Beregningsmetode.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.gjenlevenderettAnvendt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.tilleggspensjonInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.framtidigPoengAr_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.poengArNevner_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.poengArTeller_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.poengAr_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.poengAre91_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.poengArf92_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.sluttpoengtallMedOverkomp_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.sluttpoengtallUtenOverkomp_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdodBeregningKap3Selectors.sluttpoengtall_safe
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
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.framtidigTTBilateral
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.framtidigTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.framtidigTTNorsk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.nevnerProRata
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.nevnerTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.tellerProRata
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
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdodBeregningKap3
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
                table(tabellHeadingOpplysningerBruktIBeregningAvdod()) {
                    // vedleggTabellerAvdodFnr_001
                    foedselsnummer()

                    // tabellAvdodFlyktningstatus_001
                    flyktningstatusBrukt()

                    ifNotNull(avdoedTrygdetidsdetaljerKap19VedVirk) { avdoedTrygdetidsdetaljerKap19VedVirk ->
                        val beregningsmetode = avdoedTrygdetidsdetaljerKap19VedVirk.beregningsMetode
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
                                    avdoedTrygdetidsdetaljerKap19VedVirk.tellerTTEOS,
                                    avdoedTrygdetidsdetaljerKap19VedVirk.nevnerTTEOS
                                )
                            }

                            showIf(tillegspensjonKombinertMedAvdod and beregningsmetode.equalTo(FOLKETRYGD)) {
                                //vedleggTabellAvdodKap19Sluttpoengtall_001
                                //vedleggTabellAvdodKap19SluttpoengtallMedOverkomp_001
                                //vedleggTabellAvdodKap19SluttpoengtallUtenOverkomp_001

                                sluttpoengtallKap19()

                                //vedleggTabellKap19PoengAr_001
                                antallPoengAar(avdoedBeregningKap19VedVirk.poengAr_safe)
                            }
                            //vedleggTabellKap19PoengArf92_001
                            showIf(tillegspensjonKombinertMedAvdod) {
                                aarMed45og42pensjonsprosent(
                                    poengAarFoer92 = avdoedBeregningKap19VedVirk.poengArf92_safe,
                                    poengAarEtter91 = avdoedBeregningKap19VedVirk.poengAre91_safe,
                                )

                                //vedleggTabellAvdodKap19FaktiskePoengArNorge_001
                                antallFaktiskePoengAarINorge(avdoedBeregningKap19VedVirk.faktiskPoengArNorge_safe)

                                //vedleggTabellAvdodKap19FramtidigPoengar_001
                                showIf(beregningsmetode.equalTo(FOLKETRYGD)) {
                                    norskeFramtidigePoengAar(avdoedBeregningKap19VedVirk.framtidigPoengAr_safe)
                                }
                            }
                        }.orShowIf(beregningsmetode.equalTo(EOS)) {
                            //tabellTTNorgeEOS_001
                            samletTTNorgeOgEOS(avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT)

                            //vedleggTabellAvdodFramtidigTT_001
                            framtidigTTMaanederIfNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTEOS)

                            //tabellFaktiskTTBrokNorgeEOS_001
                            faktiskTTBroekNorgeEOS(
                                avdoedTrygdetidsdetaljerKap19VedVirk.tellerTTEOS,
                                avdoedTrygdetidsdetaljerKap19VedVirk.nevnerTTEOS
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
                                aarMed45og42pensjonsprosent(
                                    poengAarFoer92 = avdoedBeregningKap19VedVirk.poengArf92_safe,
                                    poengAarEtter91 = avdoedBeregningKap19VedVirk.poengAre91_safe,
                                    bokmalSuffix = " (EØS)",
                                    nynorskSuffix = " (EØS)",
                                    engelskSuffix = " (EEA)"
                                )
                                //vedleggTabellAvdodFramtidigPoengTeoretisk_001
                                framtidigePoengAar(avdoedBeregningKap19VedVirk.framtidigPoengAr_safe)

                                //tabellPoengArBrokNorgeEOS_001
                                poengAarBroekNorgeEOS(
                                    avdoedBeregningKap19VedVirk.poengArTeller_safe,
                                    avdoedBeregningKap19VedVirk.poengArNevner_safe
                                )
                            }
                        }.orShow {
                            //tabellTTNorgeAvtaleland_001
                            samletTrygdetidNorgeAvtaleland(avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT)

                            //vedleggTabellAvdodFramtidigTTBilateral_001
                            framtidigTTMaanederIfNotNull(avdoedTrygdetidsdetaljerKap19VedVirk.framtidigTTBilateral)

                            //tabellTTBrokNorgeAvtaleland_001
                            trygdetidBroekNorgeAvtaleland(
                                avdoedTrygdetidsdetaljerKap19VedVirk.tellerProRata,
                                avdoedTrygdetidsdetaljerKap19VedVirk.nevnerProRata
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
                                aarMed45og42pensjonsprosent(
                                    poengAarFoer92 = avdoedBeregningKap19VedVirk.poengArf92_safe,
                                    poengAarEtter91 = avdoedBeregningKap19VedVirk.poengAre91_safe,
                                    bokmalSuffix = " (Norge og avtaleland)",
                                    nynorskSuffix = " (Noreg og avtaleland)",
                                    engelskSuffix = " (Norway and countries with social security agreement)"
                                )
                                //vedleggTabellAvdodFramtidigPoengTeoretisk_001
                                framtidigePoengAar(avdoedBeregningKap19VedVirk.framtidigPoengAr_safe)

                                //tabellPoengArBrokNorgeAvtaleland_001
                                poengArBrokNorgeOgAvtaleland(
                                    teller = avdoedBeregningKap19VedVirk.poengArTeller_safe,
                                    nevner = avdoedBeregningKap19VedVirk.poengArNevner_safe
                                )
                            }
                        }
                    }

                    //tabellUngUfor_002
                    tilleggspensjonPgaUngUfoer()
                    yrkesskade()
                }
            }

        }.orShowIf(regelverkstypeAlderspensjon.equalTo(AP1967)) {
            paragraph {
                ifNotNull(avdoedTrygdetidsdetaljerVedVirkNokkelInfo) { avdoedNokkelinfo ->
                    val beregningsmetode = avdoedNokkelinfo.beregningsMetode
                    table(tabellHeadingOpplysningerBruktIBeregningAvdod()) {
                        // vedleggTabellerAvdodFnr_001
                        foedselsnummer()

                        // tabellAvdodFlyktningstatus_001
                        flyktningstatusBrukt()

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
                                tabellFaktiskTTBroekNorgeNordisk(
                                    avdoedNokkelinfo.tellerTTEOS,
                                    avdoedNokkelinfo.nevnerTTEOS
                                )
                            }
                            showIf(
                                beregningsmetode.equalTo(FOLKETRYGD)
                                        and alderspensjonVedVirk.tilleggspensjonInnvilget
                                        and alderspensjonVedVirk.gjenlevenderettAnvendt
                            ) {
                                sluttpoengtallKap3()
                                //vedleggTabellKap19PoengArAP1967_001
                                antallPoengAar(avdodBeregningKap3.poengAr_safe)

                                //vedleggTabellKap19PoengArf92AP1967_001
                                //vedleggTabellKap19PoengAre91AP1967_001
                                aarMed45og42pensjonsprosent(
                                    poengAarFoer92 = avdodBeregningKap3.poengArf92_safe,
                                    poengAarEtter91 = avdodBeregningKap3.poengAre91_safe,
                                )

                                //vedleggTabellAvdodKap19FramtidigPoengarAP1967
                                norskeFramtidigePoengAar(avdodBeregningKap3.framtidigPoengAr_safe)
                            }
                        }.orShowIf(beregningsmetode.equalTo(EOS)) {
                            //tabellTTNorgeEOSap1967_001
                            samletTTNorgeOgEOS(avdoedNokkelinfo.anvendtTT)

                            //vedleggTabellAvdodFramtidigTTap1967_001
                            framtidigTTMaanederIfNotNull(avdoedNokkelinfo.framtidigTTEOS)

                            //tabellFaktiskTTBrokNorgeEOSap1967_001
                            faktiskTTBroekNorgeEOS(
                                avdoedNokkelinfo.tellerTTEOS,
                                avdoedNokkelinfo.nevnerTTEOS,
                            )
                            showIf(
                                alderspensjonVedVirk.tilleggspensjonInnvilget
                                        and alderspensjonVedVirk.gjenlevenderettAnvendt
                            ) {
                                //vedleggTabellKap19SluttpoengtallEOSap1967_001
                                //vedleggTabellAvdodSluttpoengMedOverkompEOSap1967_001
                                //vedleggTabellAvdodSluttpoengUtenOverkompEOSap1967_001
                                sluttpoengtallKap3(
                                    suffixNorsk = " (EØS)",
                                    suffixEngelsk = " (EEA)"
                                )

                                //vedleggTabellKap19PoengArf92EOSap1967_001
                                //vedleggTabellKap19PoengAre91EOSap1967_001
                                aarMed45og42pensjonsprosent(
                                    poengAarFoer92 = avdodBeregningKap3.poengArf92_safe,
                                    poengAarEtter91 = avdodBeregningKap3.poengAre91_safe,
                                    bokmalSuffix = " (EØS)",
                                    nynorskSuffix = " (EØS)",
                                    engelskSuffix = " (EEA)"
                                )

                                //vedleggTabellAvdodFramtidigPoengTeoretiskAP1967_001
                                framtidigePoengAar(avdodBeregningKap3.framtidigPoengAr_safe)

                                //tabellPoengArBrokNorgeEOSap1967_001
                                poengAarBroekNorgeEOS(
                                    avdodBeregningKap3.poengArTeller_safe,
                                    avdodBeregningKap3.poengArNevner_safe,
                                )
                            }
                        }.orShow {
                            //tabellTTNorgeAvtalelandAP1967_001
                            samletTrygdetidNorgeAvtaleland(avdoedNokkelinfo.anvendtTT)

                            //vedleggTabellAvdodFramtidigTTBilateralAP1967_001
                            framtidigTTMaanederIfNotNull(avdoedNokkelinfo.framtidigTTBilateral)

                            //tabellTTBrokNorgeAvtalelandAP1967_001
                            trygdetidBroekNorgeAvtaleland(
                                avdoedNokkelinfo.tellerProRata,
                                avdoedNokkelinfo.nevnerProRata,
                            )

                            showIf(
                                alderspensjonVedVirk.tilleggspensjonInnvilget
                                        and alderspensjonVedVirk.gjenlevenderettAnvendt
                            ) {
                                //vedleggTabellKap19SluttpoengtallAvtalelandAP1967_001
                                //vedleggTabellAvdodKap19SluttpoengMedOverkompAvtalelandAP1967_001
                                //vedleggTabellAvdodKap19SluttpoengUtenOverkompAvtalelandAP1967_001

                                // TODO ulik suffix på engelsk. bør avklares. Tror earned... er riktig
                                sluttpoengtallKap3(
                                    suffixNorsk = " (avtaleland)",
                                    suffixEngelsk = " (earned in countries with social security agreement)"
                                )

                                //vedleggTabellKap19PoengArf92AvtalelandAP1967_001
                                //vedleggTabellKap19PoengAre91AvtalelandAP1967_001
                                aarMed45og42pensjonsprosent(
                                    poengAarFoer92 = avdodBeregningKap3.poengArf92_safe,
                                    poengAarEtter91 = avdodBeregningKap3.poengAre91_safe,
                                    bokmalSuffix = " (Norge og avtaleland)",
                                    nynorskSuffix = " (Noreg og avtaleland)",
                                    engelskSuffix = " (Norway and countries with social security agreement)"
                                )

                                //vedleggTabellAvdodFramtidigPoengTeoretiskAP1967_001
                                framtidigePoengAar(avdodBeregningKap3.framtidigPoengAr_safe)

                                //tabellPoengArBrokNorgeAvtalelandAP1967_001
                                poengArBrokNorgeOgAvtaleland(
                                    teller = avdodBeregningKap3.poengArTeller_safe,
                                    nevner = avdodBeregningKap3.poengArNevner_safe
                                )
                            }
                        }
                        tilleggspensjonPgaUngUfoer()
                        yrkesskade()
                    }
                }
            }
        }

    }

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.poengArBrokNorgeOgAvtaleland(
    teller: Expression<Int?>,
    nevner: Expression<Int?>
) {
    broekIfNotNull(
        bokmal = "Forholdet mellom antall poengår i Norge og antall poengår i Norge og avtaleland",
        nynorsk = "Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og avtaleland",
        engelsk = "Ratio between the number of point earning years in Norway and the number of point earning years in Norway and countries with social security agreement",
        teller = teller,
        nevner = nevner,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.trygdetidBroekNorgeAvtaleland(
    teller: Expression<Int?>,
    nevner: Expression<Int?>
) {
    broekIfNotNull(
        bokmal = "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og avtaleland",
        nynorsk = "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og avtaleland",
        engelsk = "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and countries with social security agreement",
        teller = teller,
        nevner = nevner,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.samletTrygdetidNorgeAvtaleland(
    anvendtTT: Expression<Int?>
    ) {
    antallAarIfNotNull(
        bokmal = "Samlet trygdetid i Norge og avtaleland",
        nynorsk = "Samla trygdetid i Noreg og avtaleland",
        engelsk = "Total period of national insurance coverage in Norway and countries with social security agreement",
        aar = anvendtTT
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.poengAarBroekNorgeEOS(
    teller: Expression<Int?>, nevner: Expression<Int?>
) {
    broekIfNotNull(
        bokmal = "Forholdet mellom antall poengår i Norge og antall poengår i Norge og annet EØS-land",
        nynorsk = "Forholdet mellom talet på poengår i Noreg og talet på poengår i Noreg og anna EØS-land",
        engelsk = "The ratio between point earning years in Norway and total point earning years in all EEA countries",
        teller = teller,
        nevner = nevner,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.faktiskTTBroekNorgeEOS(
    teller: Expression<Int?>,
    nevner: Expression<Int?>
) {
    broekIfNotNull(
        bokmal = "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og andre EØS-land",
        nynorsk = "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og andre EØS-land",
        engelsk = "The ratio between national insurance coverage in Norway and total insurance coverage in all EEA countries",
        teller = teller,
        nevner = nevner,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.samletTTNorgeOgEOS(
    anvendtTT: Expression<Int?>
) {
    antallAarIfNotNull(
        "Samlet trygdetid i Norge og andre EØS-land",
        "Samla trygdetid i Noreg og andre EØS-land",
        "Total national insurance coverage in Norway and other EEA countries",
        anvendtTT,
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.norskeFramtidigePoengAar(
    poengAar: Expression<Int?>
) {
    showIf(avdoedBeregningKap19VedVirk.framtidigPoengAr_safe.ifNull(0).greaterThan(0)) {
        antallAarIfNotNull(
            bokmal = "Norske framtidige poengår",
            nynorsk = "Norske framtidige poengår",
            engelsk = "Future point earning years in Norway",
            aar = poengAar
        )
    }
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.antallPoengAar(antallPoengAar: Expression<Int?>) {
    antallAarIfNotNull(
        "Antall poengår",
        "Talet på poengår",
        "Number of pension point earning years",
        antallPoengAar
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.tabellFaktiskTTBroekNorgeNordisk(
    tellerTTEOS: Expression<Int?>,
    nevnerTTEOS: Expression<Int?>
) {
    broekIfNotNull(
        bokmal = "Forholdet mellom faktisk trygdetid i Norge og trygdetid i Norge og annet nordisk land",
        nynorsk = "Forholdet mellom faktisk trygdetid i Noreg og trygdetid i Noreg og anna nordisk land",
        engelsk = "Ratio between actual period of national insurance coverage in Norway and period of national insurance coverage in Norway and other Nordic countries",
        teller = tellerTTEOS,
        nevner = nevnerTTEOS
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.faktiskTTNordiskKonvensjon(
    faktiskTrygdetidNordiskKonvensjon: Expression<Int?>
) {
    antallMaanederIfNotNull(
        "Faktisk trygdetid i annet nordisk land som beregner framtidig trygdetid",
        "Faktisk trygdetid i anna nordisk land som bereknarframtidig trygdetid",
        "Actual period of national insurance coverage in other Nordic countries that calculates future national insurance coverage",
        faktiskTrygdetidNordiskKonvensjon
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.avdodesTrygdetid(
    anvendtTrygdetid: Expression<Int?>
) {
    antallAarIfNotNull(
        "Avdødes trygdetid",
        "Trygdetida til avdøde",
        "Period of national insurance coverage",
        anvendtTrygdetid
    )
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.flyktningstatusBrukt() {
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
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.foedselsnummer() {
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
}

private fun tabellHeadingOpplysningerBruktIBeregningAvdod(): TableHeaderScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.() -> Unit =
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

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.tilleggspensjonPgaUngUfoer() {
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
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.yrkesskade() {
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
    //tabellPoengAre91Yrkesskade_001
    aarMed45og42pensjonsprosent(
        poengAarFoer92 = avdoedYrkesskadedetaljerVedVirk.poengArf92_safe,
        poengAarEtter91 = avdoedYrkesskadedetaljerVedVirk.poengAre91_safe,
        bokmalSuffix = " benyttet ved yrkesskadeberegning",
        nynorskSuffix = " brukt ved yrkesskadeberekning",
        engelskSuffix = " used in the calculation of occupational injury"
    )


    //tabellYrkesskadeUforegrad_001
    ifNotNull(avdoedYrkesskadedetaljerVedVirk.yrkesskadeUforegrad_safe) {
        row {
            cell {
                text(
                    Bokmal to "Yrkesskade uføregrad",
                    Nynorsk to "Yrkesskade uføregrad",
                    English to "Occupational injury - degree of disability",
                )
            }
            cell { eval(it.format() + " %") }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.framtidigePoengAar(
    framtidigePoengAar: Expression<Int?>
) {
    ifNotNull(framtidigePoengAar) {
        showIf(it.greaterThan(0)) {
            row {
                cell {
                    text(
                        Bokmal to "Antall framtidige poengår",
                        Nynorsk to "Talet på framtidige poengår",
                        English to "Future point earning years",
                    )
                }
                cell { includePhrase(AntallAarText(it)) }
            }
        }
    }
}

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.aarMed45og42pensjonsprosent(
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

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>.sluttpoengtallKap3(
    suffixNorsk: String = "",
    suffixEngelsk: String = "",
) {
    sluttpoengTall(
        suffixNorsk = suffixNorsk,
        suffixEngelsk = suffixEngelsk,
        sluttpoengtallMedOverkomp = avdodBeregningKap3.sluttpoengtallMedOverkomp_safe,
        sluttpoengtallUtenOverkomp = avdodBeregningKap3.sluttpoengtallUtenOverkomp_safe,
        sluttpoengtall = avdodBeregningKap3.sluttpoengtall_safe
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
