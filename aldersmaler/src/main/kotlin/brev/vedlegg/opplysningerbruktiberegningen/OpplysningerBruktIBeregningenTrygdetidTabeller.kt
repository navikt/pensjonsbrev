package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder


import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.model.alder.Beregningsmetode.*
import no.nav.pensjon.brev.model.alder.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.model.alder.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.erEksportberegnet
import no.nav.pensjon.brev.model.alder.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.model.alder.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap19VedVirkSelectors.redusertTrygdetid
import no.nav.pensjon.brev.model.alder.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.BeregningKap20VedVirkSelectors.redusertTrygdetid_safe
import no.nav.pensjon.brev.model.alder.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.InngangOgEksportVurderingSelectors.eksportBeregnetUtenGarantipensjon_safe
import no.nav.pensjon.brev.model.alder.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.beregningsmetode
import no.nav.pensjon.brev.model.alder.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.beregningsmetode_safe
import no.nav.pensjon.brev.api.model.maler.vedlegg.Trygdetid
import no.nav.pensjon.brev.api.model.maler.vedlegg.TrygdetidSelectors.fom
import no.nav.pensjon.brev.api.model.maler.vedlegg.TrygdetidSelectors.land
import no.nav.pensjon.brev.api.model.maler.vedlegg.TrygdetidSelectors.tom
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
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
    val trygdetidNorge: Expression<List<Trygdetid>>,
    val trygdetidEOS: Expression<List<Trygdetid>>,
    val trygdetidAvtaleland: Expression<List<Trygdetid>>,
    val skalSkjuleTrygdetidstabellerPgaAldersovergang: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val regelverkstype = alderspensjonVedVirk.regelverkType

        showIf(
            not(skalSkjuleTrygdetidstabellerPgaAldersovergang) and
                    ((trygdetidsdetaljerKap19VedVirk.beregningsmetode.notEqualTo(FOLKETRYGD)
                            and trygdetidsdetaljerKap20VedVirk.beregningsmetode_safe.notEqualTo(FOLKETRYGD))
                            or beregningKap19VedVirk.redusertTrygdetid
                            or beregningKap20VedVirk.redusertTrygdetid_safe.ifNull(false)
                            )
        ) {
            //trygdetidOverskrift_001
            title1 {
                text(
                    bokmal { + "Trygdetid" },
                    nynorsk { + "Trygdetid" },
                    english { + "Period of national insurance coverage" },
                )
            }
            //norskTTInfoGenerell_001
            paragraph {
                text(
                    bokmal { + "Trygdetid er perioder du har vært medlem i folketrygden. Som hovedregel er dette perioder du har bodd eller arbeidet i Norge. Trygdetid har betydning for beregning av pensjonen din. Full trygdetid er 40 år." },
                    nynorsk { + "Trygdetid er periodar du har vore medlem i folketrygda. Som hovudregel er dette periodar du har budd eller arbeidd i Noreg. Trygdetid har betydning for berekninga av pensjonen din. Full trygdetid er 40 år." },
                    english { + "The period of national insurance coverage is the periods in which you have been a member of the Norwegian National Insurance Scheme. As a general rule, these are periods when you have been registered as living or working in Norway. The period of national insurance coverage affects the calculation of your pension. The full insurance period is 40 years." },
                )
            }
            //norskTTAP2016_002
            showIf(regelverkstype.isOneOf(AP2016)) {
                paragraph {
                    text(
                        bokmal { + "Reglene for fastsetting av trygdetid er litt ulike i kapittel 19 og kapittel 20 i folketrygdloven. Du kan få trygdetid for perioder fra fylte 16 til 67 år som du har vært medlem i folketrygden. Etter kapittel 19 kan du i tillegg få trygdetid for år med pensjonspoeng, også fra fylte 67 til 75 år. Trygdetiden etter kapittel 19 kan derfor være høyere enn trygdetid etter kapittel 20 i enkelte tilfeller." },
                        nynorsk { + "Reglane for fastsetjing av trygdetid er ulike i kapittel 19 og kapittel 20 i folketrygdlova. Du kan få trygdetid for periodar frå fylte 16 til 67 år, som du har vore medlem i folketrygda. Etter kapittel 19 kan du i tillegg få trygdetid for år med pensjonspoeng, også frå fylte 67 til 75 år. Trygdetida etter kapittel 19 kan derfor være høgare enn trygdetida etter kapittel 20 i enkelte tilfelle." },
                        english { + "The provisions pertaining to accumulated pension rights differ in Chapters 19 and 20 in the National Insurance Act. In general, the period of national insurance coverage is the periods in which you have been a member of the Norwegian National Insurance Scheme between the ages of 16 and 67. In addition, you accumulate national insurance coverage by earning pension points, until the year you turn 75, pursuant to Chapter 19. Consequently, national insurance coverage pursuant to Chapter 19 may, in some cases, be higher than years pursuant to Chapter 20." },
                    )
                }
            }
            //norskTTAP2011Botid_001
            showIf(alderspensjonVedVirk.erEksportberegnet and regelverkstype.isOneOf(AP2011)) {
                paragraph {
                    text(
                        bokmal { + "Har du vært medlem i folketrygden i mindre enn 20 år skal trygdetiden etter kapittel 19 fastsettes til antall år med pensjonspoeng. Du har mindre enn 20 års medlemstid og trygdetiden din etter kapittel 19 er derfor fastsatt til antall år med pensjonspoeng." },
                        nynorsk { + "Har du vore medlem i folketrygda i mindre enn 20 år skal trygdetida etter kapittel 19 fastsetjast til antal år med pensjonspoeng. Du har mindre enn 20 års medlemstid og trygdetida di etter kapitel 19 er difor fastsett til antal år med pensjonspoeng." },
                        english { + "If you have been a member of the Norwegian National Insurance Scheme for less than 20 years in total, your national insurance coverage pursuant to chapter 19 will be the same as number of years you have accumulated pensionable earnings. You have been a member for less than 20 years in total and your period of national insurance coverage pursuant to chapter 19 is the same as number of years you have accumulated pensionable earnings." },
                    )
                }
            }

            //norskTTAP2016Eksport_001
            showIf(inngangOgEksportVurdering.eksportBeregnetUtenGarantipensjon_safe.ifNull(false)) {
                paragraph {
                    text(
                        bokmal { + "Hvis du har mindre enn 20 års medlemstid, har du ikke rett på garantipensjon når du er bosatt i utlandet." },
                        nynorsk { + "Dersom du har mindre enn 20 års medlemstid, har du ikkje rett på garantipensjon når du bur i utlandet." },
                        english { + "If you have been a member of the Norwegian National Insurance Scheme for less than 20 years in total, you are not eligible for any guaranteed pension when you live abroad." },
                    )
                }
            }

            showIf(trygdetidNorge.size().greaterThan(0)) {
                includePhrase(NorskTrygdetidInnledning)
                includePhrase(NorskTrygdetid(trygdetidNorge))
            }
        }


        showIf(
            trygdetidEOS.size().greaterThan(0)
                    and (trygdetidsdetaljerKap19VedVirk.beregningsmetode.equalTo(EOS)
                    or trygdetidsdetaljerKap20VedVirk.beregningsmetode_safe.equalTo(EOS))
        ) {
            paragraph {
                text(
                    bokmal { + "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i øvrige EØS-land. Disse periodene er brukt i vurderingen av retten til alderspensjon før fylte 67 år." },
                    nynorsk { + "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i øvrige EØS-land. Desse periodane er brukt i vurderinga av retten til alderspensjon før fylte 67 år." },
                    english { + "The table below shows your National Insurance coverage in other EEA-countries. These periods have been used to assess whether you are eligible for retirement pension before the age of 67." },
                )
            }

            includePhrase(UtenlandskTrygdetid(trygdetidEOS))
        }

        showIf(
            trygdetidsdetaljerKap19VedVirk.beregningsmetode.isNotAnyOf(EOS, FOLKETRYGD, NORDISK)
                    and trygdetidsdetaljerKap20VedVirk.beregningsmetode_safe.ifNull(EOS)
                .isNotAnyOf(EOS, FOLKETRYGD, NORDISK)
                    and trygdetidAvtaleland.size().greaterThan(0)
        ) {
            paragraph {
                text(
                    bokmal { + "Trygdetiden din i avtaleland er fastsatt på grunnlag av følgende perioder:" },
                    nynorsk { + "Trygdetida di i avtaleland er fastsett på grunnlag av følgjande periodar:" },
                    english { + "Your period of national insurance coverage in a signatory country is based on the following periods:" },
                )
            }
            includePhrase(UtenlandskTrygdetid(trygdetidAvtaleland))
        }
    }

    object NorskTrygdetidInnledning : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { + "Tabellen nedenfor viser perioder vi har brukt for å fastsette din norske trygdetid." },
                    nynorsk { + "Tabellen nedanfor viser periodar vi har brukt for å fastsetje den norske trygdetida di." },
                    english { + "The table below shows the time periods used to establish your Norwegian national insurance coverage." },
                )
            }

        }

    }

    data class NorskTrygdetid(
        val trygdetidNorge: Expression<List<Trygdetid>>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(trygdetidNorge.isNotEmpty()) {
                paragraph {
                    table(
                        {
                            column {
                                text(
                                    bokmal { + "Fra og med" },
                                    nynorsk { + "Frå og med" },
                                    english { + "Start date" }
                                )
                            }
                            column {
                                text(
                                    bokmal { + "Til og med" },
                                    nynorsk { + "Til og med" },
                                    english { + "End date" },
                                )
                            }
                        }
                    ) {
                        forEach(trygdetidNorge) { trygdetid ->
                            row {
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

    data class UtenlandskTrygdetid(
        val trygdetid: Expression<List<Trygdetid>>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                table({
                    column { text(bokmal { + "Land" }, nynorsk { + "Land" }, english { + "Country" }) }
                    column {
                        text(
                            bokmal { + "Fra og med" },
                            nynorsk { + "Frå og med" },
                            english { + "Start date" }
                        )
                    }
                    column {
                        text(
                            bokmal { + "Til og med" },
                            nynorsk { + "Til og med" },
                            english { + "End date" },
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
