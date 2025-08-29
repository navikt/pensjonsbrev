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
import no.nav.pensjon.brev.template.dsl.text
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
            bokmal { + "Vi bruker inntektsopplysninger fra Skatteetaten når vi vurderer om du har fått for mye eller for lite i uføretrygd" },
            nynorsk { + "Vi bruker inntektsopplysningar frå Skatteetaten når vi vurderer om du har fått for mykje eller for lite i uføretrygd " },
        )

        //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
        ) {
            text(
                bokmal { + " og barnetillegg" },
                nynorsk { + "og barnetillegg " },
            )
        }
        text(
            bokmal { + " i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + "." },
            nynorsk { + "i " + pe.ut_uforetrygdetteroppgjor_periodefom_year().format() + "." },
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
                bokmal { + "I tabellen(e) under kan du se hva som er din pensjonsgivende inntekt" },
                nynorsk { + "I tabellan(e) under kan du sjå kva som er den pensjonsgivande inntekta di" },
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
            ) {
                text(
                    bokmal { + ", din personinntekt" },
                    nynorsk { + ", personinntekta di" },
                )
            }
            text(
                bokmal { + " og hvilke inntekter som blir brukt til å vurdere om du har fått for mye eller for lite i uføretrygd" },
                nynorsk { + " og kva inntekter vi bruker til å vurdere om du har fått for mykje eller for lite i uføretrygd" },
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0 OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb()
                    .notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))
            ) {
                text(
                    bokmal { + " og barnetillegg" },
                    nynorsk { + " og barnetillegg" },
                )
            }
            text(
                bokmal { + "." },
                nynorsk { + "." },
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
                bokmal { + "Dersom du ikke har rett til barnetillegg hele året er inntektene kortet ned for kun å gjelde den perioden du mottar barnetillegg." },
                nynorsk { + "Dersom du ikkje har rett til barnetillegg heile året, er inntektene korta ned for berre å gjelde den perioden du får barnetillegg." },
            )
        }
    }
    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))) {
        //[Table 6, Table 7, Table 5]

        paragraph {
            text(
                bokmal { +  "Mottar annen forelder uføretrygd eller alderspensjon fra Nav regnes dette også med som personinntekt." },
                nynorsk { +  "Dersom den andre forelderen får uføretrygd eller alderspensjon frå Nav, blir også dette rekna med som personinntekt." },
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
            text(
                bokmal { +  "Folketrygdens grunnbeløp på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()
                    .format() + " er holdt utenfor inntekten til annen forelder." },
                nynorsk { +  "Grunnbeløpet i folketrygda på inntil " + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()
                    .format() + " er halde utanfor inntekta til den andre forelderen." },
            )
        }
    }

    includePhrase(TabellOversiktForskjellBetaling(pe))

    //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopUT <> 0) THEN      INCLUDE ENDIF
    showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloput().notEqualTo(0))) {
        //[Table 2, Table]

        title1 {
            text(
                bokmal { +  "Hvilke inntekter bruker vi i justeringen av uføretrygden?" },
                nynorsk { +  "Kva inntekter bruker vi når vi justerer uføretrygda?" },
            )
        }
        //[Table 2, Table]

        paragraph {
            text(
                bokmal { +  "Uføretrygden blir justert ut fra pensjonsgivende inntekt. Pensjonsgivende inntekt er for eksempel arbeidsinntekt, næringsinntekt og utlandsinntekt. Ytelser fra Nav som erstatter arbeidsinntekt er også pensjonsgivende inntekt. Dette står i § 3-15 i folketrygdloven. " },
                nynorsk { +  "Uføretrygda blir justert ut frå pensjonsgivande inntekt. Pensjonsgivande inntekt er for eksempel arbeidsinntekt, næringsinntekt og utanlandsinntekt. Ytingar frå Nav som erstattar arbeidsinntekt, er også pensjonsgivande inntekt. Dette står i § 3-15 i folketrygdlova. " },
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
                bokmal { +  "Hvilke inntekter bruker vi i fastsettelsen av størrelsen på barnetillegget?" },
                nynorsk { +  "Kva inntekter bruker vi når vi fastset kor stort barnetillegget er?" },
            )
        }
        //[Table 8, Table 9, Table 7]

        paragraph {
            text(
                bokmal { +  "Barnetillegg blir beregnet ut fra personinntekt. Dette står i §12-2 i skatteloven. Personinntekt er den skattepliktige inntekten din før skatt. " },
                nynorsk { +  "Barnetillegg blir berekna ut frå personinntekta. Dette står i §12-2 i skattelova. Personinntekt er den skattepliktige inntekta di før skatt. " },
            )

            //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))) {
                text(
                    bokmal { +  "Bor barnet/barna sammen med begge sine foreldre så regner vi også med personinntekt til annen forelder. " },
                    nynorsk { +  "Bur barnet/barna saman med begge foreldra sine, reknar vi også med personinntekta til den andre forelderen. " },
                )
            }
            text(
                bokmal { +  "Personinntekt er for eksempel: lønn, inkludert bonus og overtid fra arbeidsgiver, næringsinntekt, utlandsinntekt, uføretrygd og andre ytelser fra Nav." },
                nynorsk { +  "Personinntekt er for eksempel: lønn, inkludert bonus og overtid frå arbeidsgivar, næringsinntekt, utanlandsinntekt, uføretrygd og andre ytingar frå Nav." },
            )
        }
        //[Table 8, Table 9, Table 7]

        paragraph {
            text(
                bokmal { +  "Du kan lese mer om personinntekt på $SKATTEETATEN_URL. " },
                nynorsk { +  "Du kan lese meir om personinntekt på $SKATTEETATEN_URL. " },
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
                    bokmal { +  "Vi kan i enkelte tilfeller se bort fra inntekt i etteroppgjøret" },
                    nynorsk { +  "Vi kan i enkelte tilfelle sjå bort frå inntekt i etteroppgjeret" },
                )
            }
        }

        //IF((Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) < 2023) OR  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles = 0 AND  Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)) AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402")  THEN       INCLUDE ENDIF
        showIf(((FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).lessThan(2023)) or (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().equalTo(0) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023))) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {

            paragraph {
                text(
                    bokmal { +  "Dette gjelder kun dersom du hadde en slik inntekt." },
                    nynorsk { +  "Dette gjeld berre dersom du hadde ei slik inntekt." },
                )

                //IF(PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0))) {
                    text(
                        bokmal { +  " Det gjelder ikke for inntekten til annen forelder." },
                        nynorsk { +  " Det gjeld ikkje for inntekta til den andre forelderen." },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +  "Vi kan trekke fra:" },
                    nynorsk { +  "Vi kan trekkje frå:" },
                )

                list {
                    item {
                        //IF(PE_UT_PeriodeFomStorre0101() = true) THEN      INCLUDE ENDIF
                        showIf((pe.ut_periodefomstorre0101())) {
                            text(
                                bokmal { +  "Inntekt før du ble innvilget uføretrygd" },
                                nynorsk { +  "Inntekt før du fekk innvilga uføretrygd" },
                            )
                        }
                    }
                    item {
                        text(
                            bokmal { +  "Erstatning for inntektstap ved erstatningsoppgjør etter" },
                            nynorsk { +  "Erstatning for inntektstap ved erstatningsoppgjer etter" },
                        )
                    }
                    item {
                        text(
                            bokmal { +  "    Skadeerstatningsloven § 3-1" },
                            nynorsk { +  "    Skadeerstatningslova § 3-1" },
                        )
                    }
                    item {
                        text(
                            bokmal { +  "    Yrkesskadeforsikringsloven § 13" },
                            nynorsk { +  "    Yrkesskadeforsikringslova § 13" },
                        )
                    }
                    item {
                        text(
                            bokmal { +  "    Pasientskadeloven § 4 første ledd" },
                            nynorsk { +  "    Pasientskadelova § 4 første ledd" },
                        )
                    }
                    item {
                        showIf((pe.ut_periodetommindre3112())) {
                            text(
                                bokmal { +  "Inntekt etter at uføretrygden din opphørte" },
                                nynorsk { +  "Inntekt etter at uføretrygda di tok slutt" },
                            )
                        }
                    }
                    item {
                        text(
                            bokmal { +  " Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel:" },
                            nynorsk { +  " Inntekt frå arbeid eller verksemd som blei heilt avslutta før du fekk innvilga uføretrygd, for eksempel:" },
                        )
                    }
                    item {
                        text(
                            bokmal { +  "    Utbetalte feriepenger for et arbeidsforhold som er avsluttet" },
                            nynorsk { +  "    Utbetalte feriepengar for et arbeidsforhold som er avslutta" },
                        )
                    }
                    item {
                        text(
                            bokmal { +  "    Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten" },
                            nynorsk { +  "    Inntekter frå sal av produksjonsmiddel i samband med at verksemda blei avslutta" },
                        )
                    }
                    item {
                        text(
                            bokmal { +  "    Produksjonstillegg og andre overføringer til gårdbrukere" },
                            nynorsk { +  "    Produksjonstillegg og andre overføringar til gardbrukarar" },
                        )
                    }
                }
                text(
                    bokmal { +  "Hadde du en slik inntekt i " + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                        .format() + ", må du dokumentere dette innen 3 uker fra " + felles.dokumentDato.format() + ". Du trenger ikke sende inn ny dokumentasjon om vi allerede har trukket dette fra. Inntekter som er trukket fra ser du i tabellen ovenfor." },
                    nynorsk { +  "Hadde du ei slik inntekt i " + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                        .format() + ", må du dokumentere dette innan tre veker frå " + felles.dokumentDato.format() + ". Du treng ikkje sende inn ny dokumentasjon om vi allereie har trekt dette frå. Inntekter som er trekte frå, ser du i tabellen ovanfor." },
                )
            }
        }

        // TODO sjekk at year funksjonen er lik som exstream. (Hypotese-test)
        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles >= 1 AND Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") THEN      INCLUDE ENDIF
        showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThanOrEqual(1) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023)) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {

            paragraph {
                text(
                    bokmal { +  "Dette gjelder kun dersom du hadde en slik inntekt. Det gjelder ikke for inntekten til annen forelder." },
                    nynorsk { +  "Dette gjeld berre dersom du hadde ei slik inntekt. Det gjeld ikkje for inntekta til den andre forelderen." },
                )
            }
            paragraph {
                text(
                    bokmal { +  "Vi kan trekke fra:" },
                    nynorsk { +  "Vi kan trekkje frå:" },
                )
                list {

                    //IF(PE_UT_PeriodeFomStorre0101() = true) AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles >= 1  AND Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") THEN      INCLUDE ENDIF
                    showIf((pe.ut_periodefomstorre0101()) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThanOrEqual(1) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023)) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {

                        item {
                            text(
                                bokmal { +  "Inntekt før du ble innvilget uføretrygd " },
                                nynorsk { +  "Inntekt før du fekk innvilga uføretrygd " },
                            )
                        }
                    }

                    //IF(PE_UT_PeriodeTomMindre3112() = true)  AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles >= 1  AND Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") THEN      INCLUDE ENDIF
                    showIf((pe.ut_periodetommindre3112()) and (pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThanOrEqual(1) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023)) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {

                        item {
                            text(
                                bokmal { +  "Inntekt etter at uføretrygden din opphørte" },
                                nynorsk { +  "Inntekt etter at uføretrygda di tok slutt" },
                            )
                        }
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles >= 1 AND Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThanOrEqual(1) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023)) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {

                        item {
                            text(
                                bokmal { +  "Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel:" },
                                nynorsk { +  "Inntekt frå arbeid eller verksemd som blei heilt avslutta før du fekk innvilga uføretrygd, for eksempel:" },
                            )
                        }

                        item {
                            text(
                                bokmal { +  "    Utbetalte feriepenger for et arbeidsforhold som er avsluttet" },
                                nynorsk { +  "    Utbetalte feriepengar for eit arbeidsforhold som er avslutta" },
                            )
                        }
                        item {
                            text(
                                bokmal { +  "    Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten" },
                                nynorsk { +  "    Inntekter frå sal av produksjonsmiddel i samband med at verksemda blei avslutta" },
                            )
                        }
                        item {
                            text(
                                bokmal { +  "    Produksjonstillegg og andre overføringer til gårdbrukere" },
                                nynorsk { +  "    Produksjonstillegg og andre overføringar til gardbrukarar" },
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles >= 1 AND Year(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_PeriodeFom) >= 2023)  AND PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_EtteroppgjorResultatType = "tilbakekr"  AND (PE_pebrevkode = "PE_UT_23_001"  OR  PE_pebrevkode = "PE_UT_04_402") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_antallbarnfelles().greaterThanOrEqual(1) and FUNKSJON_Year(pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_periodefom()).greaterThanOrEqual(2023)) and pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_etteroppgjorresultattype().equalTo("tilbakekr") and (pe.pebrevkode().equalTo("PE_UT_23_001") or pe.pebrevkode().equalTo("PE_UT_04_402"))) {
                    text(
                        bokmal { +  "I tillegg kan vi se bort fra følgende inntekt for deg, og annen forelder som mottar uføretrygd eller alderspensjon fra Nav:" },
                        nynorsk { +  "Vi kan også sjå bort frå følgjande inntekt for deg og den andre forelderen som mottek uføretrygd eller alderspensjon frå Nav:" },
                    )

                    list {
                        item {
                            text(
                                bokmal { +  "Erstatning for inntektstap ved erstatningsoppgjør etter" },
                                nynorsk { +  "Erstatning for inntektstap ved erstatningsoppgjer etter:" },
                            )
                        }
                        item {
                            text(
                                bokmal { +  "    Skadeerstatningsloven § 3-1" },
                                nynorsk { +  "    Skadeerstatningsloven § 3-1" },
                            )
                        }
                        item {
                            text(
                                bokmal { +  "    Yrkesskadeforsikringsloven § 13" },
                                nynorsk { +  "    Yrkesskadeforsikringsloven § 13" },
                            )
                        }
                        item {
                            text(
                                bokmal { +  "    Pasientskadeloven § 4 første ledd" },
                                nynorsk { +  "    Pasientskadeloven § 4 første ledd" },
                            )
                        }
                    }

                    text(
                        bokmal { +  "Slik inntekt i " + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                            .format() + ", må dokumenteres innen 3 uker fra " + felles.dokumentDato.format() + ". Du trenger ikke sende inn ny dokumentasjon om vi allerede har trukket dette fra. Inntekter som er trukket fra ser du i tabellen ovenfor." },
                        nynorsk { +  "Slik inntekt i " + pe.ut_uforetrygdetteroppgjor_periodefom_year()
                            .format() + ", må du dokumentere innan tre veker frå " + felles.dokumentDato.format() + ". Du treng ikkje sende inn ny dokumentasjon om vi allereie har trekt dette frå. Inntekter som er trekte frå, ser du i tabellen ovanfor." },
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
                    bokmal { +  "Inntekt i perioder før innvilgelse av uføretrygd og etter at uføretrygden opphørte, skal ikke være med i vurderingen om du har fått for mye eller for lite i barnetillegg. " },
                    nynorsk { +  "Inntekt i periodar før du fekk innvilga uføretrygd og etter at uføretrygda tok slutt, skal ikkje vere med i vurderinga av om du har fått for mykje eller for lite i barnetillegg. " },
                )

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb())) {
                    text(
                        bokmal { +  "Dersom du mottar barnetillegg for barn som bor sammen med begge sine foreldre, vil vi også holde slik inntekt utenfor inntekt som er oppgitt for annen forelder. " },
                        nynorsk { +  "Dersom du får barnetillegg for barn som bur saman med begge foreldra sine, held vi også slik inntekt utanfor inntekta som er oppgitt for den andre forelderen. " },
                    )
                }
            }
        }

        //IF( (PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTSB <> 0        OR PE_Vedtaksbrev_Vedtaksdata_EtteroppgjorResultat_AvviksbelopTFB <> 0      ) AND      (PE_UT_PeriodeFomStorre0101() = true      OR PE_UT_PeriodeTomMindre3112() = true)      ) THEN      INCLUDE ENDIF
        showIf(((pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptsb().notEqualTo(0) or pe.vedtaksbrev_vedtaksdata_etteroppgjorresultat_avviksbeloptfb().notEqualTo(0)) and (pe.ut_periodefomstorre0101() or pe.ut_periodetommindre3112()))) {

            paragraph {
                text(
                    bokmal { +  "Vi kan se bort fra næringsinntekt " },
                    nynorsk { +  "Vi kan sjå bort frå næringsinntekt " },
                )

                //IF(PE_Vedtaksbrev_Grunnlag_Persongrunnlagsliste_UforetrygdEtteroppgjor_BarnetilleggFB = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksbrev_grunnlag_persongrunnlagsliste_uforetrygdetteroppgjor_barnetilleggfb())) {
                    text(
                        bokmal { +  "til deg eller annen forelder " },
                        nynorsk { +  "til deg eller den andre forelderen " },
                    )
                }
                text(
                    bokmal { +  "fra en næringsvirksomhet som ble avsluttet før innvilgelse av uføretrygd, eller pensjon fra utland i perioden før uføretrygden ble innvilget eller at den opphørte." },
                    nynorsk { +  "frå ei næringsverksemd som blei avslutta før uføretrygda blei innvilga, eller pensjon frå utlandet i perioden før uføretrygda blei innvilga, eller etter at ho tok slutt." },
                )
            }
        }

        paragraph {
            text(
                bokmal { +  "Hvis vi mottar dokumentasjon fra deg som har betydning for etteroppgjøret, vil vi gjøre et nytt etteroppgjør og du får et nytt brev. Vi vil også gjøre et nytt etteroppgjør dersom Skatteetaten endrer fastsetting av skatten din og det har betydning for uføretrygden din. " },
                nynorsk { +  "Dersom vi får dokumentasjon frå deg som har noko å seie for etteroppgjeret, gjer vi eit nytt etteroppgjer og du får eit nytt brev. Vi gjer også eit nytt etteroppgjer dersom Skatteetaten endrar på fastsettinga av skatten din, og det har betydning for uføretrygda di. " },
            )
        }
    }
}
