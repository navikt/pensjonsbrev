package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.barnetillegg

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class StoerrelsePaaBarnetillegg_EndringIInntekt(
    val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            paragraph {
                // Åpningssetningen bygges som to språkspesifikke sekvenser (først nynorsk, så bokmål)
                // fordi eigedomspronomenet står i ulik ordstilling: bokmål "inntekten din ... til din
                // {samboar}", nynorsk "inntekta ... til deg eller {samboar} din" (bygd som "di" + "n").
                // Ordstillinga hindrar ein felles per-fragment-struktur. Den felles halen (" blir
                // reduksjonen ...") er slått saman til eitt tospråkleg text()-kall nedst i avsnittet.
                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "Har det vore ei endring i inntekta " },
                    )
                }

                showIf(
                    (pe.ut_tbu605v_eller_til_din() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "til deg eller " },
                    )
                }

                showIf((pe.ut_tbu605v_eller_til_din())) {
                    text(
                        bokmal { + "" },
                        nynorsk { + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " " },
                    )
                }

                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "di" },
                    )
                }

                showIf(
                    (pe.ut_tbu605v_eller_til_din() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "n" },
                    )
                }

                showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endret_inntekt"))) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "," },
                    )
                }

                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "Når inntekta di " },
                    )
                }

                showIf(
                    (pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("endret_inntekt") and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "eller til " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall() + " din " },
                    )
                }

                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "" },
                        nynorsk { + "endrar seg," },
                    )
                }

                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        bokmal { + "Har det vært en endring i inntekten din" },
                        nynorsk { + "" },
                    )
                }

                showIf(
                    ((pe.ut_tbu605v_eller_til_din() and pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget() and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo(
                            "bormed ektefelle"
                        ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert partner") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 3-2"
                    ))))
                ) {
                    text(
                        bokmal { + " eller til din" },
                        nynorsk { + "" },
                    )
                }

                showIf(((pe.ut_tbu605v_eller_til_din()))) {
                    text(
                        bokmal { + " " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + "," },
                        nynorsk { + "" },
                    )
                }

                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "Når inntekten din " },
                        nynorsk { + "" },
                    )
                }

                showIf(
                    (pe.vedtaksdata_kravhode_kravarsaktype()
                        .equalTo("endret_inntekt") and (pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed ektefelle") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed registrert") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo(
                        "bormed 1-5"
                    ) or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt()
                        .equalTo("bormed 1_5") or pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2")))
                ) {
                    text(
                        bokmal { + "eller til din " + pe.sivilstand_ektefelle_partner_samboer_bormed_ut() + " " },
                        nynorsk { + "" },
                    )
                }

                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endret_inntekt")) {
                    text(
                        bokmal { + "endrer seg," },
                        nynorsk { + "" },
                    )
                }
                text(
                    bokmal { + " blir reduksjonen av barnetilleggene vurdert på nytt. " },
                    nynorsk { + " blir reduksjonen av barnetilleggene vurdert på nytt." },
                )
            }
        }

        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_1_3())) {
            paragraph {
                text(
                    bokmal { + "50 prosent av den inntekten som overstiger fribeløpet for " + pe.ut_barnet_barna_felles() + " som bor med begge sine foreldre " },
                    nynorsk { + "50 prosent av inntekta som overstig fribeløpet for " + pe.ut_barnet_barna_felles() + " som bur med begge foreldra " },
                )

                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()) {
                    text(
                        bokmal { + "blir omregnet til et årlig beløp som tilsvarer " },
                        nynorsk { + "sine blir rekna om til et årleg beløp som svarer til " },
                    )
                }

                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        bokmal { + "er " },
                        nynorsk { + "er " },
                    )
                }
                text(
                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                )

                showIf(
                    pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0)
                ) {
                    text(
                        bokmal { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året." },
                        nynorsk { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året." },
                    )
                }
            }
        }

        showIf(
            (pe.ut_tbu613v() and pe.ut_tbu613v_1_3() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                .notEqualTo(
                    0
                ))
        ) {
            paragraph {
                text(
                    bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                    nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor " },
                )

                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(0))
                ) {
                    text(
                        bokmal { + "lagt til" },
                        nynorsk { + "lagt til" },
                    )
                }

                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .lessThan(0))
                ) {
                    text(
                        bokmal { + "trukket fra" },
                        nynorsk { + "trekt frå" },
                    )
                }
                text(
                    bokmal { + " " + pe.barnetilleggfelles_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                    nynorsk { + " " + pe.barnetilleggfelles_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                )
            }
        }

        showIf((pe.ut_tbu613v() and pe.ut_tbu613v_4_5())) {
            paragraph {
                text(
                    bokmal { + "For " + pe.ut_barnet_barna_serkull() + " som ikke bor sammen med begge foreldrene " },
                    nynorsk { + "For " + pe.ut_barnet_barna_serkull() + " som ikkje bur med begge foreldra " },
                )

                showIf(not(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert())) {
                    text(
                        bokmal { + "er 50 prosent av den inntekten som overstiger fribeløpet " },
                        nynorsk { + "er 50 prosent av den inntekta som overstig fribeløpet " },
                    )
                }

                showIf(pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert()) {
                    text(
                        bokmal { + "blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer " },
                        nynorsk { + "blir 50 prosent av den inntekta som overstig fribeløpet regna om til et årleg beløp som svarer til " },

                        )
                }

                text(
                    bokmal { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                    nynorsk { + pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
                        .format() + ". " },
                )

                showIf(
                    pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
                        .equalTo(0)
                ) {
                    text(
                        bokmal { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året. " },
                        nynorsk { + "Dette beløpet bruker vi til å redusere dette barnetillegget for heile året. " },
                    )
                }
            }
        }

        showIf(
            (pe.ut_tbu613v() and pe.ut_tbu613v_4_5() and pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                .notEqualTo(
                    0
                ))
        ) {
            paragraph {
                text(
                    bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                    nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor " },
                )

                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .greaterThan(0))
                ) {
                    text(
                        bokmal { + "lagt til" },
                        nynorsk { + "lagt til" },
                    )
                }

                showIf(
                    (pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
                        .lessThan(0))
                ) {
                    text(
                        bokmal { + "trukket fra" },
                        nynorsk { + "trekt frå" },
                    )
                }
                text(
                    bokmal { + " " + pe.barnetilleggserkull_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                    nynorsk { + " " + pe.barnetilleggserkull_justeringsbelopperarutenminus()
                        .format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                )
            }
        }
    }
}
