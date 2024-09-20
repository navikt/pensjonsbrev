package no.nav.pensjon.brev.maler.legacy.vedlegg

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_URL
import no.nav.pensjon.brev.maler.legacy.FUNKSJON_Year
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerometteroppgjoret.OpplysningerOmEtteroppgjoretLegacy.TabellBeloepFratrukketInntektAnnenForelder
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerometteroppgjoret.OpplysningerOmEtteroppgjoretLegacy.TabellInntektenDin
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerometteroppgjoret.OpplysningerOmEtteroppgjoretLegacy.TabellInntekterEPS
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerometteroppgjoret.OpplysningerOmEtteroppgjoretLegacy.TabellOversiktForskjellBetaling
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerometteroppgjoret.OpplysningerOmEtteroppgjoretLegacy.TabellTrukketFraInntekt
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.ut_periodefomstorre0101
import no.nav.pensjon.brev.maler.legacy.ut_periodetommindre3112
import no.nav.pensjon.brev.maler.legacy.ut_uforetrygdetteroppgjor_periodefom_year
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_sumfratrekkbt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput
import no.nav.pensjon.brev.maler.legacy.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.dokumentDato

@TemplateModelHelpers
val opplysningerOmETteroppgjoeretUTLegacy = createAttachment<LangBokmalNynorsk, PE>(
    title = newText(
        Bokmal to "Opplysninger om etteroppgjøret",
        Nynorsk to "Opplysningar om etteroppgjeret",
    ),
    includeSakspart = false,
) {
    val pe = argument
    //[PE_UT_orientering_TBU076V_veiledning]

    paragraph {
        text(
            Bokmal to "Vi bruker inntektsopplysninger fra Skatteetaten når vi vurderer om du har fått for mye eller for lite i uføretrygd",
            Nynorsk to "Vi bruker inntektsopplysningar frå Skatteetaten når vi vurderer om du har fått for mykje eller for lite i uføretrygd ",
        )

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
        ) {
            text(
                Bokmal to " og barnetillegg",
                Nynorsk to "og barnetillegg ",
            )
        }
        textExpr(
            Bokmal to " i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ".",
            Nynorsk to "i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + ".",
        )
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf(
        (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput()
            .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
            .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
    ) {
        //[PE_UT_orientering_TBU076V_veiledning]

        paragraph {
            text(
                Bokmal to "I tabellen(e) under kan du se hva som er din pensjonsgivende inntekt",
                Nynorsk to "I tabellan(e) under kan du sjå kva som er den pensjonsgivande inntekta di",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
            ) {
                text(
                    Bokmal to ", din personinntekt",
                    Nynorsk to ", personinntekta di",
                )
            }
            text(
                Bokmal to " og hvilke inntekter som blir brukt til å vurdere om du har fått for mye eller for lite i uføretrygd",
                Nynorsk to " og kva inntekter vi bruker til å vurdere om du har fått for mykje eller for lite i uføretrygd",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
            ) {
                text(
                    Bokmal to " og barnetillegg",
                    Nynorsk to " og barnetillegg",
                )
            }
            text(
                Bokmal to ".",
                Nynorsk to ".",
            )
        }
    }

    includePhrase(TabellInntektenDin(pe))
    includePhrase(TabellTrukketFraInntekt(pe))
    includePhrase(TabellInntekterEPS(pe))
    includePhrase(TabellBeloepFratrukketInntektAnnenForelder(pe))

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf(
        (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
            .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
    ) {
        //[Table 5, Table 6, Table 4]

        paragraph {
            text(
                Bokmal to "Dersom du ikke har rett til barnetillegg hele året er inntektene kortet ned for kun å gjelde den perioden du mottar barnetillegg.",
                Nynorsk to "Dersom du ikkje har rett til barnetillegg heile året, er inntektene korta ned for berre å gjelde den perioden du får barnetillegg.",
            )
        }
    }
    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))) {
        //[Table 6, Table 7, Table 5]

        paragraph {
            text(
                Bokmal to "Mottar annen forelder uføretrygd eller alderspensjon fra NAV regnes dette også med som personinntekt.",
                Nynorsk to "Dersom den andre forelderen får uføretrygd eller alderspensjon frå NAV, blir også dette rekna med som personinntekt.",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0 AND PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_SumInntekterBT > PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_UforetrygdEtteroppgjorDetaljEPS_SumFratrekkBT) THEN      INCLUDE ENDIF
    showIf(
        (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb()
            .notEqualTo(0) and pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_suminntekterbt()
            .greaterThan(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_uforetrygdetteroppgjordetaljeps_sumfratrekkbt()))
    ) {
        //[Table 5, Table 6, Table 4]

        paragraph {
            textExpr(
                Bokmal to "Folketrygdens grunnbeløp på inntil ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()
                    .format() + " kroner er holdt utenfor inntekten til annen forelder.",
                Nynorsk to "Grunnbeløpet i folketrygda på inntil ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()
                    .format() + " kroner er halde utanfor inntekta til den andre forelderen.",
            )
        }
    }

    includePhrase(TabellOversiktForskjellBetaling(pe))

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))) {
        //[Table 2, Table]

        title1 {
            text(
                Bokmal to "Hvilke inntekter bruker vi i justeringen av uføretrygden?",
                Nynorsk to "Kva inntekter bruker vi når vi justerer uføretrygda?",
            )
        }
        //[Table 2, Table]

        paragraph {
            text(
                Bokmal to "Uføretrygden blir justert ut fra pensjonsgivende inntekt. Pensjonsgivende inntekt er for eksempel arbeidsinntekt, næringsinntekt og utlandsinntekt. Ytelser fra NAV som erstatter arbeidsinntekt er også pensjonsgivende inntekt. Dette står i § 3-15 i folketrygdloven. ",
                Nynorsk to "Uføretrygda blir justert ut frå pensjonsgivande inntekt. Pensjonsgivande inntekt er for eksempel arbeidsinntekt, næringsinntekt og utanlandsinntekt. Ytingar frå NAV som erstattar arbeidsinntekt, er også pensjonsgivande inntekt. Dette står i § 3-15 i folketrygdlova. ",
            )
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf(
        (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
            .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
    ) {
        //[Table 8, Table 9, Table 7]

        title1 {
            text(
                Bokmal to "Hvilke inntekter bruker vi i fastsettelsen av størrelsen på barnetillegget?",
                Nynorsk to "Kva inntekter bruker vi når vi fastset kor stort barnetillegget er?",
            )
        }
        //[Table 8, Table 9, Table 7]

        paragraph {
            text(
                Bokmal to "Barnetillegg blir beregnet ut fra personinntekt. Dette står i §12-2 i skatteloven. Personinntekt er den skattepliktige inntekten din før skatt. ",
                Nynorsk to "Barnetillegg blir berekna ut frå personinntekta. Dette står i §12-2 i skattelova. Personinntekt er den skattepliktige inntekta di før skatt. ",
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))) {
                text(
                    Bokmal to "Bor barnet/barna sammen med begge sine foreldre så regner vi også med personinntekt til annen forelder. ",
                    Nynorsk to "Bur barnet/barna saman med begge foreldra sine, reknar vi også med personinntekta til den andre forelderen. ",
                )
            }
            text(
                Bokmal to "Personinntekt er for eksempel: lønn, inkludert bonus og overtid fra arbeidsgiver, næringsinntekt, utlandsinntekt, uføretrygd og andre ytelser fra NAV.",
                Nynorsk to "Personinntekt er for eksempel: lønn, inkludert bonus og overtid frå arbeidsgivar, næringsinntekt, utanlandsinntekt, uføretrygd og andre ytingar frå NAV.",
            )
        }
        //[Table 8, Table 9, Table 7]

        paragraph {
            text(
                Bokmal to "Du kan lese mer om personinntekt på $SKATTEETATEN_URL. ",
                Nynorsk to "Du kan lese meir om personinntekt på $SKATTEETATEN_URL. ",
            )
        }
    }

    //IF( PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND  (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402")  ) THEN      INCLUDE ENDIF
    showIf(
        (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode()
            .equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402")))
    ) {
        //[Table 2, Table 3, Table]

        //IF( PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") ) THEN       INCLUDE ENDIF
        showIf(
            (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode()
                .equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402")))
        ) {

            title1 {
                text(
                    Bokmal to "Vi kan i enkelte tilfeller se bort fra inntekt i etteroppgjøret",
                    Nynorsk to "Vi kan i enkelte tilfelle sjå bort frå inntekt i etteroppgjeret",
                )
            }
        }

        //IF((Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) < 2023) OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles = 0 AND  Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)) AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402")  THEN       INCLUDE ENDIF
        showIf(((FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).lessThan(2023)) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().equalTo(0) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023))) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {

            paragraph {
                text(
                    Bokmal to "Dette gjelder kun dersom du hadde en slik inntekt.",
                    Nynorsk to "Dette gjeld berre dersom du hadde ei slik inntekt.",
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))) {
                    text(
                        Bokmal to " Det gjelder ikke for inntekten til annen forelder.",
                        Nynorsk to " Det gjeld ikkje for inntekta til den andre forelderen.",
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Vi kan trekke fra:",
                    Nynorsk to "Vi kan trekkje frå:",
                )

                list {
                    item {
                        //IF(PE_UT_PeriodeFomStorre0101() = true) THEN      INCLUDE ENDIF
                        showIf((pe.ut_periodefomstorre0101())) {
                            text(
                                Bokmal to "Inntekt før du ble innvilget uføretrygd",
                                Nynorsk to "Inntekt før du fekk innvilga uføretrygd",
                            )
                        }
                    }
                    item {
                        text(
                            Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør etter",
                            Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer etter",
                        )
                    }
                    item {
                        text(
                            Bokmal to "    Skadeerstatningsloven § 3-1",
                            Nynorsk to "    Skadeerstatningslova § 3-1",
                        )
                    }
                    item {
                        text(
                            Bokmal to "    Yrkesskadeforsikringsloven § 13",
                            Nynorsk to "    Yrkesskadeforsikringslova § 13",
                        )
                    }
                    item {
                        text(
                            Bokmal to "    Pasientskadeloven § 4 første ledd",
                            Nynorsk to "    Pasientskadelova § 4 første ledd",
                        )
                    }
                    item {
                        showIf((pe.ut_periodetommindre3112())) {
                            text(
                                Bokmal to "Inntekt etter at uføretrygden din opphørte",
                                Nynorsk to "Inntekt etter at uføretrygda di tok slutt",
                            )
                        }
                    }
                    item {
                        text(
                            Bokmal to " Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel:",
                            Nynorsk to " Inntekt frå arbeid eller verksemd som blei heilt avslutta før du fekk innvilga uføretrygd, for eksempel:",
                        )
                    }
                    item {
                        text(
                            Bokmal to "    Utbetalte feriepenger for et arbeidsforhold som er avsluttet",
                            Nynorsk to "    Utbetalte feriepengar for et arbeidsforhold som er avslutta",
                        )
                    }
                    item {
                        text(
                            Bokmal to "    Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten",
                            Nynorsk to "    Inntekter frå sal av produksjonsmiddel i samband med at verksemda blei avslutta",
                        )
                    }
                    item {
                        text(
                            Bokmal to "    Produksjonstillegg og andre overføringer til gårdbrukere",
                            Nynorsk to "    Produksjonstillegg og andre overføringar til gardbrukarar",
                        )
                    }
                }
                textExpr(
                    Bokmal to "Hadde du en slik inntekt i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                        .format() + ", må du dokumentere dette innen 3 uker fra " + felles.dokumentDato.format() + ". Du trenger ikke sende inn ny dokumentasjon om vi allerede har trukket dette fra. Inntekter som er trukket fra ser du i tabellen ovenfor.",
                    Nynorsk to "Hadde du ei slik inntekt i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                        .format() + ", må du dokumentere dette innan tre veker frå " + felles.dokumentDato.format() + ". Du treng ikkje sende inn ny dokumentasjon om vi allereie har trekt dette frå. Inntekter som er trekte frå, ser du i tabellen ovanfor.",
                )
            }
        }

        // TODO sjekk at year funksjonen er lik som exstream. (Hypotese-test)
        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles >= 1 AND Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") THEN      INCLUDE ENDIF
        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThanOrEqual(1) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023)) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {

            paragraph {
                text(
                    Bokmal to "Dette gjelder kun dersom du hadde en slik inntekt. Det gjelder ikke for inntekten til annen forelder.",
                    Nynorsk to "Dette gjeld berre dersom du hadde ei slik inntekt. Det gjeld ikkje for inntekta til den andre forelderen.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi kan trekke fra:",
                    Nynorsk to "Vi kan trekkje frå:",
                )
                list {

                    //IF(PE_UT_PeriodeFomStorre0101() = true) AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles >= 1  AND Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") THEN      INCLUDE ENDIF
                    showIf((pe.ut_periodefomstorre0101()) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThanOrEqual(1) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023)) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {

                        item {
                            text(
                                Bokmal to "Inntekt før du ble innvilget uføretrygd ",
                                Nynorsk to "Inntekt før du fekk innvilga uføretrygd ",
                            )
                        }
                    }

                    //IF(PE_UT_PeriodeTomMindre3112() = true)  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles >= 1  AND Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") THEN      INCLUDE ENDIF
                    showIf((pe.ut_periodetommindre3112()) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThanOrEqual(1) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023)) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {

                        item {
                            text(
                                Bokmal to "Inntekt etter at uføretrygden din opphørte",
                                Nynorsk to "Inntekt etter at uføretrygda di tok slutt",
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles >= 1 AND Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThanOrEqual(1) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023)) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {

                        item {
                            text(
                                Bokmal to "Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel:",
                                Nynorsk to "Inntekt frå arbeid eller verksemd som blei heilt avslutta før du fekk innvilga uføretrygd, for eksempel:",
                            )
                        }

                        item {
                            text(
                                Bokmal to "    Utbetalte feriepenger for et arbeidsforhold som er avsluttet",
                                Nynorsk to "    Utbetalte feriepengar for eit arbeidsforhold som er avslutta",
                            )
                        }
                        item {
                            text(
                                Bokmal to "    Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten",
                                Nynorsk to "    Inntekter frå sal av produksjonsmiddel i samband med at verksemda blei avslutta",
                            )
                        }
                        item {
                            text(
                                Bokmal to "    Produksjonstillegg og andre overføringer til gårdbrukere",
                                Nynorsk to "    Produksjonstillegg og andre overføringar til gardbrukarar",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles >= 1 AND Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThanOrEqual(1) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023)) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {
                    text(
                        Bokmal to "I tillegg kan vi se bort fra følgende inntekt for deg, og annen forelder som mottar uføretrygd eller alderspensjon fra NAV:",
                        Nynorsk to "Vi kan også sjå bort frå følgjande inntekt for deg og den andre forelderen som mottek uføretrygd eller alderspensjon frå NAV:",
                    )

                    list {
                        item {
                            text(
                                Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør etter",
                                Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer etter:",
                            )
                        }
                        item {
                            text(
                                Bokmal to "    Skadeerstatningsloven § 3-1",
                                Nynorsk to "    Skadeerstatningsloven § 3-1",
                            )
                        }
                        item {
                            text(
                                Bokmal to "    Yrkesskadeforsikringsloven § 13",
                                Nynorsk to "    Yrkesskadeforsikringsloven § 13",
                            )
                        }
                        item {
                            text(
                                Bokmal to "    Pasientskadeloven § 4 første ledd",
                                Nynorsk to "    Pasientskadeloven § 4 første ledd",
                            )
                        }
                    }

                    textExpr(
                        Bokmal to "Slik inntekt i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                            .format() + ", må dokumenteres innen 3 uker fra " + felles.dokumentDato.format() + ". Du trenger ikke sende inn ny dokumentasjon om vi allerede har trukket dette fra. Inntekter som er trukket fra ser du i tabellen ovenfor.",
                        Nynorsk to "Slik inntekt i ".expr() + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                            .format() + ", må du dokumentere innan tre veker frå " + felles.dokumentDato.format() + ". Du treng ikkje sende inn ny dokumentasjon om vi allereie har trekt dette frå. Inntekter som er trekte frå, ser du i tabellen ovanfor.",
                    )
                }
            }
        }
    }

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = 'tilbakekr'  AND (PE_pebrevkode = 'PE_UT_23_001' OR PE_pebrevkode = 'PE_UT_04_402')) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402")))) {

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))) {

            paragraph {
                text(
                    Bokmal to "Inntekt i perioder før innvilgelse av uføretrygd og etter at uføretrygden opphørte, skal ikke være med i vurderingen om du har fått for mye eller for lite i barnetillegg. ",
                    Nynorsk to "Inntekt i periodar før du fekk innvilga uføretrygd og etter at uføretrygda tok slutt, skal ikkje vere med i vurderinga av om du har fått for mykje eller for lite i barnetillegg. ",
                )

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb())) {
                    text(
                        Bokmal to "Dersom du mottar barnetillegg for barn som bor sammen med begge sine foreldre, vil vi også holde slik inntekt utenfor inntekt som er oppgitt for annen forelder. ",
                        Nynorsk to "Dersom du får barnetillegg for barn som bur saman med begge foreldra sine, held vi også slik inntekt utanfor inntekta som er oppgitt for den andre forelderen. ",
                    )
                }
            }
        }

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))) {

            paragraph {
                text(
                    Bokmal to "Vi kan se bort fra næringsinntekt ",
                    Nynorsk to "Vi kan sjå bort frå næringsinntekt ",
                )

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb())) {
                    text(
                        Bokmal to "til deg eller annen forelder ",
                        Nynorsk to "til deg eller den andre forelderen ",
                    )
                }
                text(
                    Bokmal to "fra en næringsvirksomhet som ble avsluttet før innvilgelse av uføretrygd, eller pensjon fra utland i perioden før uføretrygden ble innvilget eller at den opphørte.",
                    Nynorsk to "frå ei næringsverksemd som blei avslutta før uføretrygda blei innvilga, eller pensjon frå utlandet i perioden før uføretrygda blei innvilga, eller etter at ho tok slutt.",
                )
            }
        }

        paragraph {
            text(
                Bokmal to "Hvis vi mottar dokumentasjon fra deg som har betydning for etteroppgjøret, vil vi gjøre et nytt etteroppgjør og du får et nytt brev. Vi vil også gjøre et nytt etteroppgjør dersom Skatteetaten endrer fastsetting av skatten din og det har betydning for uføretrygden din. ",
                Nynorsk to "Dersom vi får dokumentasjon frå deg som har noko å seie for etteroppgjeret, gjer vi eit nytt etteroppgjer og du får eit nytt brev. Vi gjer også eit nytt etteroppgjer dersom Skatteetaten endrar på fastsettinga av skatten din, og det har betydning for uføretrygda di. ",
            )
        }
    }
}
