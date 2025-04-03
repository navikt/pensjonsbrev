package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkstype.AP2016
import no.nav.pensjon.brev.api.model.Beregningsmetode.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.erEksportberegnet
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.redusertTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.redusertTrygdetid_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InngangOgEksportVurderingSelectors.eksportBeregnetUtenGarantipensjon_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidSelectors.fom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidSelectors.land
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidSelectors.tom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.beregningsmetode_safe
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class OpplysningerBruktIBeregningenTrygdetidTabeller(
    val trygdetidsdetaljerKap19VedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.TrygdetidsdetaljerKap19VedVirk>,
    val trygdetidsdetaljerKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.TrygdetidsdetaljerKap20VedVirk?>,
    val beregningKap19VedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.BeregningKap19VedVirk>,
    val beregningKap20VedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.BeregningKap20VedVirk?>,
    val alderspensjonVedVirk: Expression<OpplysningerBruktIBeregningenAlderDto.AlderspensjonVedVirk>,
    val inngangOgEksportVurdering: Expression<OpplysningerBruktIBeregningenAlderDto.InngangOgEksportVurdering?>,
    val trygdetidNorge: Expression<List<OpplysningerBruktIBeregningenAlderDto.Trygdetid>>,
    val trygdetidEOS: Expression<List<OpplysningerBruktIBeregningenAlderDto.Trygdetid>>,
    val trygdetidAvtaleland: Expression<List<OpplysningerBruktIBeregningenAlderDto.Trygdetid>>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val regelverkstype = alderspensjonVedVirk.regelverkType
        // TODO en sjekk som kanskje ikke gir mening:
        //kategori = GetValue("fag=aldersovergangKategoriListe=aldersovergangKategori=kategori");
        // if (kategori == "")
        // ReturnValue("3");

        showIf(
            (trygdetidsdetaljerKap19VedVirk.beregningsmetode.notEqualTo(FOLKETRYGD)
                    and trygdetidsdetaljerKap20VedVirk.beregningsmetode_safe.notEqualTo(FOLKETRYGD))
                    or beregningKap19VedVirk.redusertTrygdetid
                    or beregningKap20VedVirk.redusertTrygdetid_safe.ifNull(false)
        ) {
            //trygdetidOverskrift_001
            title1 {
                text(
                    Bokmal to "Trygdetid",
                    Nynorsk to "Trygdetid",
                    English to "Period of national insurance coverage",
                )
            }
            //norskTTInfoGenerell_001
            paragraph {
                text(
                    Bokmal to "Trygdetid er perioder du har vært medlem i folketrygden. Som hovedregel er dette perioder du har bodd eller arbeidet i Norge. Trygdetid har betydning for beregning av pensjonen din. Full trygdetid er 40 år.",
                    Nynorsk to "Trygdetid er periodar du har vore medlem i folketrygda. Som hovudregel er dette periodar du har budd eller arbeidd i Noreg. Trygdetid har betydning for berekninga av pensjonen din. Full trygdetid er 40 år.",
                    English to "The period of national insurance coverage is the periods in which you have been a member of the Norwegian National Insurance Scheme. As a general rule, these are periods when you have been registered as living or working in Norway. The period of national insurance coverage affects the calculation of your pension. The full insurance period is 40 years.",
                )
            }
            //norskTTAP2016_002
            showIf(regelverkstype.isOneOf(AP2016)) {
                paragraph {
                    text(
                        Bokmal to "Reglene for fastsetting av trygdetid er litt ulike i kapittel 19 og kapittel 20 i folketrygdloven. Du kan få trygdetid for perioder fra fylte 16 til 67 år som du har vært medlem i folketrygden. Etter kapittel 19 kan du i tillegg få trygdetid for år med pensjonspoeng, også fra fylte 67 til 75 år. Trygdetiden etter kapittel 19 kan derfor være høyere enn trygdetid etter kapittel 20 i enkelte tilfeller.",
                        Nynorsk to "Reglane for fastsetjing av trygdetid er ulike i kapittel 19 og kapittel 20 i folketrygdlova. Du kan få trygdetid for periodar frå fylte 16 til 67 år, som du har vore medlem i folketrygda. Etter kapittel 19 kan du i tillegg få trygdetid for år med pensjonspoeng, også frå fylte 67 til 75 år. Trygdetida etter kapittel 19 kan derfor være høgare enn trygdetida etter kapittel 20 i enkelte tilfelle.",
                        English to "The provisions pertaining to accumulated pension rights differ in Chapters 19 and 20 in the National Insurance Act. In general, the period of national insurance coverage is the periods in which you have been a member of the Norwegian National Insurance Scheme between the ages of 16 and 67. In addition, you accumulate national insurance coverage by earning pension points, until the year you turn 75, pursuant to Chapter 19. Consequently, national insurance coverage pursuant to Chapter 19 may, in some cases, be higher than years pursuant to Chapter 20.",
                    )
                }
            }
            //norskTTAP2011Botid_001
            showIf(alderspensjonVedVirk.erEksportberegnet and regelverkstype.isOneOf(AP2011)) {
                paragraph {
                    text(
                        Bokmal to "Har du vært medlem i folketrygden i mindre enn 20 år skal trygdetiden etter kapittel 19 fastsettes til antall år med pensjonspoeng. Du har mindre enn 20 års medlemstid og trygdetiden din etter kapittel 19 er derfor fastsatt til antall år med pensjonspoeng.",
                        Nynorsk to "Har du vore medlem i folketrygda i mindre enn 20 år skal trygdetida etter kapittel 19 fastsetjast til antal år med pensjonspoeng. Du har mindre enn 20 års medlemstid og trygdetida di etter kapitel 19 er difor fastsett til antal år med pensjonspoeng.",
                        English to "If you have been a member of the Norwegian National Insurance Scheme for less than 20 years in total, your national insurance coverage pursuant to chapter 19 will be the same as number of years you have accumulated pensionable earnings. You have been a member for less than 20 years in total and your period of national insurance coverage pursuant to chapter 19 is the same as number of years you have accumulated pensionable earnings.",
                    )
                }
            }

            //norskTTAP2016Eksport_001
            showIf(inngangOgEksportVurdering.eksportBeregnetUtenGarantipensjon_safe.ifNull(false)) {
                paragraph {
                    text(
                        Bokmal to "Hvis du har mindre enn 20 års medlemstid, har du ikke rett på garantipensjon når du er bosatt i utlandet.",
                        Nynorsk to "Dersom du har mindre enn 20 års medlemstid, har du ikkje rett på garantipensjon når du bur i utlandet.",
                        English to "If you have been a member of the Norwegian National Insurance Scheme for less than 20 years in total, you are not eligible for any guaranteed pension when you live abroad.",
                    )
                }
            }

            showIf(trygdetidNorge.size().greaterThan(0)) {
                paragraph {
                    text(
                        Bokmal to "Tabellen nedenfor viser perioder vi har registrert at du har bodd og/eller arbeidet i Norge. Disse opplysningene er brukt for å fastsette din norske trygdetid.",
                        Nynorsk to "Tabellen nedanfor viser periodar vi har registrert at du har budd og/eller arbeidd i Noreg. Desse opplysningane er brukte for å fastsetje den norske trygdetida di.",
                        English to "The table below shows the time periods when you have been registered as living and/or working in Norway. This information has been used to establish your Norwegian national insurance coverage.",
                    )
                }
                includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetid(trygdetidNorge))
            }
        }


        showIf(
            trygdetidEOS.size().greaterThan(0)
                    and (trygdetidsdetaljerKap19VedVirk.beregningsmetode.equalTo(EOS)
                    or trygdetidsdetaljerKap20VedVirk.beregningsmetode_safe.equalTo(EOS))
        ) {
            paragraph {
                text(
                    Bokmal to "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i øvrige EØS-land. Disse periodene er brukt i vurderingen av retten til alderspensjon før fylte 67 år.",
                    Nynorsk to "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i øvrige EØS-land. Desse periodane er brukt i vurderinga av retten til alderspensjon før fylte 67 år.",
                    English to "The table below shows your National Insurance coverage in other EEC-countries. These periods have been used to assess whether you are eligible for retirement pension before the age of 67.",
                )
            }

            includePhrase(UtenlandskTrygdetid(trygdetidEOS))
        }

        showIf(
            trygdetidsdetaljerKap19VedVirk.beregningsmetode.isNotAnyOf(EOS, FOLKETRYGD, NORDISK)
                    and trygdetidsdetaljerKap20VedVirk.beregningsmetode_safe.ifNull(EOS)
                .isNotAnyOf(EOS, FOLKETRYGD, NORDISK)
        ) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden din i avtaleland er fastsatt på grunnlag av følgende perioder:",
                    Nynorsk to "Trygdetida di i avtaleland er fastsett på grunnlag av følgjande periodar:",
                    English to "Your period of national insurance coverage in a signatory country is based on the following periods:",
                )
            }
            includePhrase(UtenlandskTrygdetid(trygdetidAvtaleland))
        }
    }

    data class NorskTrygdetid(
        val trygdetidNorge: Expression<List<OpplysningerBruktIBeregningenAlderDto.Trygdetid>>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(trygdetidNorge.size().greaterThan(0)) {
                paragraph {
                    text(
                        Bokmal to "Tabellen nedenfor viser perioder vi har registrert at du har bodd og/eller arbeidet i Norge. Disse opplysningene er brukt for å fastsette din norske trygdetid.",
                        Nynorsk to "Tabellen nedanfor viser periodar vi har registrert at du har budd og/eller arbeidd i Noreg. Desse opplysningane er brukte for å fastsetje den norske trygdetida di.",
                        English to "The table below shows the time periods when you have been registered as living and/or working in Norway. This information has been used to establish your Norwegian national insurance coverage.",
                    )
                    table(
                        {
                            column {
                                text(
                                    Bokmal to "Fra og med",
                                    Nynorsk to "Frå og med",
                                    English to "Start date"
                                )
                            }
                            column {
                                text(
                                    Bokmal to "Til og med",
                                    Nynorsk to "Til og med",
                                    English to "End date",
                                )
                            }
                        }
                    ) {
                        forEach(trygdetidNorge) { trygedtid ->
                            row {
                                cell {
                                    ifNotNull(trygedtid.fom) {
                                        eval(it.format(short = true))
                                    }
                                }
                                cell {
                                    ifNotNull(trygedtid.tom) {
                                        eval(it.format(short = true))
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    data class UtenlandskTrygdetid(
        val trygdetid: Expression<List<OpplysningerBruktIBeregningenAlderDto.Trygdetid>>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                table({
                    column { text(Bokmal to "Land", Nynorsk to "Land", English to "Country") }
                    column {
                        text(
                            Bokmal to "Fra og med",
                            Nynorsk to "Frå og med",
                            English to "Start date"
                        )
                    }
                    column {
                        text(
                            Bokmal to "Til og med",
                            Nynorsk to "Til og med",
                            English to "End date",
                        )
                    }
                }) {
                    forEach(trygdetid) { trygdetid ->
                        row {
                            cell {
                                ifNotNull(trygdetid.land) {
                                    eval(it)
                                }
                            }
                            cell {
                                ifNotNull(trygdetid.fom) {
                                    eval(it.format(short = true))
                                }
                            }
                            cell {
                                ifNotNull(trygdetid.tom) {
                                    eval(it.format(short = true))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
