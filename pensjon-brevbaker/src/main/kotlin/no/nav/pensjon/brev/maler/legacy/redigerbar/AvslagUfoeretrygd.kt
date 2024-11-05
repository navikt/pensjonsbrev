package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.AvslagUfoeretrygdDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.adhoc.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.*
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object AvslagUfoeretrygd : RedigerbarTemplate<AvslagUfoeretrygdDto> {

    // PE_UT_04_104
    override val kode: Brevkode.Redigerbar = Brevkode.Redigerbar.UT_AVSLAG_UFOERETRYGD
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        name = kode.name, letterDataType = AvslagUfoeretrygdDto::class, languages = languages(Bokmal, Nynorsk, English), letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på uføretrygd",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Nav har avslått søknaden din om uføretrygd",
                Nynorsk to "Nav har avslått søknaden din om uføretrygd ",
                English to "Nav has denied your application for disability benefit",
            )
        }
        outline {
            val pe = pesysData.pe

            //IF(PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND FF_GetArrayElement_String(PE_Vedtaksdata_Kravhode_Kravlinjeliste_Kravlinje_Kravlinjetype) = "ut" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_okn_ug") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_uu") and pe.vedtaksdata_kravhode_kravgjelder()
                    .notEqualTo("sok_ys") and pe.vedtaksdata_kravhode_kravarsaktype()
                    .notEqualTo("endring_ifu") and pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype().equalTo(
                    "ut"
                ) and pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_okn_ug"))
            ) {
                includePhrase(TBU2384_Generated(pe))
            }

            //PE_Vedtaksdata_Kravhode_KravGjelder = "sok_okn_ug"
            showIf(pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_okn_ug")) {
                //[TBU2385NN, TBU2385EN, TBU2385]

                paragraph {
                    textExpr(
                        Bokmal to "Vi har avslått søknaden din om økt uføretrygd som vi mottok ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                        Nynorsk to "Vi har avslått søknaden din om auka uføretrygd som vi fekk ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                        English to "We have denied your application for increased disability benefit, which we received on ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                    )
                }
            }

            //PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys"
            showIf(pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys")) {
                //[TBU2386NN, TBU2386EN, TBU2386]

                paragraph {
                    textExpr(
                        Bokmal to "Vi har avslått søknaden din om særbestemmelser for yrkesskade eller yrkessykdom i uføretrygden, som vi mottok ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                        Nynorsk to "Vi har avslått søknaden din om særreglar for yrkesskade eller yrkessjukdom i uføretrygda som vi fekk ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                        English to "We have denied your application for special rules pertaining to occupational injury or occupational illness to be applied to your disability benefit, which we received on ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                    )
                }
            }

            //PE_Vedtaksdata_Kravhode_KravGjelder = "sok_uu"
            showIf(pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu")) {
                //[TBU2387NN, TBU2387EN, TBU2387]

                paragraph {
                    textExpr(
                        Bokmal to "Vi har avslått søknaden din om rettighet som ung ufør som vi mottok ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                        Nynorsk to "Vi har avslått søknaden din om rett som ung ufør som vi fekk ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                        English to "We have denied your application to be granted rights as a young disabled person, which we received on ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                    )
                }
            }

            //PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu"
            showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")) {
                //[TBU2388NN, TBU2388EN, TBU2388]

                paragraph {
                    textExpr(
                        Bokmal to "Vi har avslått søknaden din om å endre den fastsatte inntekten din før du ble ufør, som vi mottok ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                        Nynorsk to "Vi har avslått søknaden din om å endre den fastsette inntekta di før du blei ufør, som vi fekk ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                        English to "We have denied your application to change your determined income prior to your disability, which we received on ".expr() + pe.vedtaksdata_kravhode_kravmottatdato()
                            .format() + ".",
                    )
                }
            }
            includePhrase(TBU1092_Generated)

            //IF( (PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu"  AND  PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys"  AND  PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu")  AND (  (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "ikke_oppfylt"  OR  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "ikke_oppfylt"  OR  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_SykdomSkadeLyteResultat) = "ikke_oppfylt" OR  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat) = "ikke_oppfylt")  OR   (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_ForutgaendeMedlemskapResultat) = "ikke_oppfylt"  AND  PE_Vedtaksdata_Kravhode_VurdereTrygdeavtale = false)  )  AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) <> "stdbegr_12_7_2_i_4" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingBegrunnelse) <> "stdbegr_12_5_1_i_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakBegrunnelse) <> "stdbegr_12_5_2_i_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_SykdomSkadeLyteBegrunnelse) <> "stdbegr_12_6_1_i_3"  ) THEN      INCLUDE ENDIF
            showIf(
                ((pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_uu") and pe.vedtaksdata_kravhode_kravgjelder()
                    .notEqualTo("sok_ys") and pe.vedtaksdata_kravhode_kravarsaktype()
                    .notEqualTo("endring_ifu")) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat().equalTo(
                    "ikke_oppfylt"
                ) or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat().equalTo(
                    "ikke_oppfylt"
                ) or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_sykdomskadelyteresultat()
                    .equalTo("ikke_oppfylt") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()
                    .equalTo("ikke_oppfylt")) or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskapresultat().equalTo(
                    "ikke_oppfylt"
                ) and not(pe.vedtaksdata_kravhode_vurderetrygdeavtale()))) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                    .notEqualTo(
                        "stdbegr_12_7_2_i_4"
                    ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingbegrunnelse().notEqualTo(
                    "stdbegr_12_5_1_i_3"
                ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakbegrunnelse().notEqualTo(
                    "stdbegr_12_5_2_i_3"
                ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_sykdomskadelytebegrunnelse().notEqualTo("stdbegr_12_6_1_i_3"))
            ) {
                //[TBU2389NN, TBU2389EN, TBU2389]

                paragraph {
                    text(
                        Bokmal to "For å ha rett til uføretrygd må du oppfylle alle disse vilkårene:",
                        Nynorsk to "For å ha rett til uføretrygd må du oppfylle alle desse vilkåra:",
                        English to "In order to be entitled to disability benefit, you must meet all of the below requirements:",
                    )
                    list {
                        item {
                            text(
                                Bokmal to "Du må være mellom 18 og 67 år.",
                                Nynorsk to "Du må vere mellom 18 og 67 år.",
                                English to "You must be between the ages of 18 and 67.",
                            )
                        }
                        item {
                            textExpr(
                                Bokmal to "Du må ha vært medlem av folketrygden i de siste ".expr() + pe.aars_trygdetid() + " årene fram til uføretidspunktet, eller oppfylle en av unntaksreglene for medlemskap.",
                                Nynorsk to "Du må ha vore medlem av folketrygda i dei siste ".expr() + pe.aars_trygdetid() + " åra fram til uføretidspunktet eller oppfylle ein av unntaksreglane for medlemskap.",
                                English to "You must have had national insurance coverage for the last ".expr() + pe.aars_trygdetid() + " years prior to your disability, or you must meet one of the exemption requirements for coverage.",
                            )
                        }
                        item {
                            text(
                                Bokmal to "Inntektsevnen din må være varig nedsatt med minst 50 prosent på grunn av sykdom og/eller skade, eller du må oppfylle en av unntaksreglene.",
                                Nynorsk to "Inntektsevna di må vere varig sett ned med minst 50 prosent på grunn av sjukdom og/eller skade, eller du må oppfylle ein av unntaksreglane.",
                                English to "Your earning ability must be permanently reduced by no less than 50 percent due to illness and/or injury, or you must meet one of the exemption requirements.",
                            )
                        }
                        item {
                            text(
                                Bokmal to "Sykdommen eller skaden din må være hovedårsak til din nedsatte inntektsevne.",
                                Nynorsk to "Sjukdommen eller skaden din må vere hovudårsaka til at inntektsevna di er nedsett.",
                                English to "Your illness or injury must be the primary cause behind your reduced earning ability.",
                            )
                        }
                        item {
                            text(
                                Bokmal to "Du må ha gjennomført all hensiktsmessig behandling og arbeidsrettede tiltak, som kan bedre inntektsevnen din.",
                                Nynorsk to "Du må ha gjennomført all formålstenleg behandling og alle arbeidsretta tiltak som kan betre inntektsevna di.",
                                English to "You must have tried all relevant treatments and employment schemes in an attempt to improve your earning ability.",
                            )
                        }
                    }
                }
            }

            //IF( (PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu"  AND  PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys"  AND  PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu")  AND (  (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "ikke_oppfylt"  OR  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "ikke_oppfylt"  OR  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_SykdomSkadeLyteResultat) = "ikke_oppfylt" OR  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat) = "ikke_oppfylt")  OR   (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_ForutgaendeMedlemskapResultat) = "ikke_oppfylt"  AND  PE_Vedtaksdata_Kravhode_VurdereTrygdeavtale = false)  )  AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) <> "stdbegr_12_7_2_i_4" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingBegrunnelse) <> "stdbegr_12_5_1_i_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakBegrunnelse) <> "stdbegr_12_5_2_i_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_SykdomSkadeLyteBegrunnelse) <> "stdbegr_12_6_1_i_3"  ) THEN      INCLUDE ENDIF
            showIf(
                ((pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_uu") and pe.vedtaksdata_kravhode_kravgjelder()
                    .notEqualTo("sok_ys") and pe.vedtaksdata_kravhode_kravarsaktype()
                    .notEqualTo("endring_ifu")) and ((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat().equalTo(
                    "ikke_oppfylt"
                ) or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat().equalTo(
                    "ikke_oppfylt"
                ) or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_sykdomskadelyteresultat()
                    .equalTo("ikke_oppfylt") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()
                    .equalTo("ikke_oppfylt")) or (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskapresultat().equalTo(
                    "ikke_oppfylt"
                ) and not(pe.vedtaksdata_kravhode_vurderetrygdeavtale()))) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                    .notEqualTo(
                        "stdbegr_12_7_2_i_4"
                    ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingbegrunnelse().notEqualTo(
                    "stdbegr_12_5_1_i_3"
                ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakbegrunnelse().notEqualTo(
                    "stdbegr_12_5_2_i_3"
                ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_sykdomskadelytebegrunnelse().notEqualTo("stdbegr_12_6_1_i_3"))
            ) {
                //[TBU2390NN, TBU2390EN, TBU2390]

                paragraph {
                    text(
                        Bokmal to "Vi har kommet fram til at du ikke oppfyller alle disse vilkårene.",
                        Nynorsk to "Vi har kome fram til at du ikkje oppfyller alle desse vilkåra.",
                        English to "We have concluded that you do not meet all these requirements.",
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingBegrunnelse) = "stdbegr_12_5_2_i_3" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakBegrunnelse) = "stdbegr_12_5_2_i_3" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_SykdomSkadeLyteBegrunnelse) = "stdbegr_12_5_2_i_3") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingbegrunnelse()
                    .equalTo("stdbegr_12_5_2_i_3") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakbegrunnelse()
                    .equalTo("stdbegr_12_5_2_i_3") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_sykdomskadelytebegrunnelse().equalTo(
                    "stdbegr_12_5_2_i_3"
                ))
            ) {
                //[TBU2391NN, TBU2391EN, TBU2391]

                paragraph {
                    text(
                        Bokmal to "For at vi skal kunne ta stilling til søknaden din om uføretrygd, må du gi oss de opplysningene vi trenger. Vi sendte deg et brev <FRITEKST: dato> der vi ba deg sende oss dokumentene som manglet, og varslet deg om at søknaden din ville bli avslått dersom vi ikke fikk dem innen fristen. ",
                        Nynorsk to "For at vi skal kunne ta stilling til søknaden din om uføretrygd, må du gi oss dei opplysningane vi treng. Vi sende deg eit brev <FRITEKST: dato> der vi bad deg sende oss dei dokumenta som mangla, og der vi varsla deg om at søknaden din ville bli avslått dersom vi ikkje fekk dokumenta innan fristen.",
                        English to "In order for us to be able to process your application for disability benefit, you must provide all the information we need. We sent you a letter on <FRITEKST: dato>, where we asked you to provide the documentation that was missing, and notified you that your application would be denied if said documentation was not provided within the date specified.",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "<FRITEKST: Forklar nærmere hvilken dokumentasjon vi ba om, og hvorfor vi ikke kan behandle søknaden uten disse opplysningene.>",
                        Nynorsk to "<FRITEKST: Forklar nærmere hvilken dokumentasjon vi ba om, og hvorfor vi ikke kan behandle søknaden uten disse opplysningene.>",
                        English to "<FRITEKST: Forklar nærmere hvilken dokumentasjon vi ba om, og hvorfor vi ikke kan behandle søknaden uten disse opplysningene.>",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Vi har ikke mottatt disse dokumentene og avslår derfor søknaden din om uføretrygd.",
                        Nynorsk to "Vi har ikkje fått desse dokumenta og avslår derfor søknaden din om uføretrygd.",
                        English to "We have not received the documentation we need, and your application for disability benefit is thus denied.",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 21-3 <FRITEKST: Vurdere om det skal henvises til bestemmelser i kap 12, og hvis 21-7 er brukt, må du angi hvilken bokstav som er vurdert>. ",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 21-3 <FRITEKST: Vurdere om det skal henvises til bestemmelser i kap 12, og hvis 21-7 er brukt, må du angi hvilken bokstav som er vurdert>.",
                        English to "This decision is made pursuant to Sections 21-3 <FRITEKST: Vurdere om det skal henvises til bestemmelser i kap 12, og hvis 21-7 er brukt, må du angi hvilken bokstav som er vurdert> of the National Insurance Act.",
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_ForutgaendeMedlemskapResultat) = "ikke_oppfylt" AND PE_Vedtaksdata_Kravhode_VurdereTrygdeavtale = false) THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskapresultat().equalTo("ikke_oppfylt") and not(
                    pe.vedtaksdata_kravhode_vurderetrygdeavtale()
                ))
            ) {

                //IF(PE_aarstall_trygdetid <= 2020) THEN      INCLUDE ENDIF
                showIf((pe.aarstall_trygdetid().lessThanOrEqual(2020))) {
                    //[TBU2392NN, TBU2392EN, TBU2392]

                    paragraph {
                        text(
                            Bokmal to "For å ha rett til uføretrygd, må du ha vært medlem av folketrygden i de siste tre årene fram til uføretidspunktet. Vi kan gjøre unntak fra hovedregelen dersom:",
                            Nynorsk to "For å ha rett til uføretrygd må du ha vore medlem av folketrygda i dei siste tre åra fram til uføretidspunktet. Vi kan gjere unntak frå hovudregelen dersom:",
                            English to "In order to be entitled to disability benefit, you must have had national insurance coverage for the last three years prior to your disability. An exception to this main requirement can be made, if:",
                        )
                        list {
                            item {
                                text(
                                    Bokmal to "du er registrert med flyktningstatus fra Utlendingsdirektoratet, eller",
                                    Nynorsk to "du er registrert med flyktningstatus frå Utlendingsdirektoratet, eller",
                                    English to "you have been granted refugee status by the Directorate of Immigration, or",
                                )
                            }
                            item {
                                text(
                                    Bokmal to "uførheten din skyldes en godkjent yrkesskade eller yrkessykdom",
                                    Nynorsk to "uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom",
                                    English to "your disability is due to a certified occupational injury or occupational illness.",
                                )

                            }
                        }
                    }
                }

                //IF(PE_aarstall_trygdetid >= 2021) THEN      INCLUDE ENDIF
                showIf((pe.aarstall_trygdetid().greaterThanOrEqual(2021))) {
                    //[TBU5018_NN, TBU5018_EN, TBU5018]

                    paragraph {
                        text(
                            Bokmal to "For å ha rett til uføretrygd, må du ha vært medlem av folketrygden i de siste fem årene fram til uføretidspunktet. Vi kan gjøre unntak fra hovedregelen dersom uførheten din skyldes en godkjent yrkesskade eller yrkessykdom.",
                            Nynorsk to "For å ha rett til uføretrygd må du ha vore medlem av folketrygda i dei siste fem åra fram til uføretidspunktet. Vi kan gjere unntak frå hovudregelen dersom uførleiken din kjem av ein godkjend yrkesskade eller yrkessjukdom.",
                            English to "In order to be entitled to disability benefit, you must have had national insurance coverage for the last five years prior to your disability. An exception to this main requirement can be made, if your disability is due to a certified occupational injury or occupational illness. ",
                        )
                    }
                }
                //[TBU2393NN, TBU2393EN, TBU2393]

                paragraph {
                    text(
                        Bokmal to "Vi kan også gjøre unntak dersom du var medlem av folketrygden på uføretidspunktet, og:",
                        Nynorsk to "Vi kan også gjere unntak dersom du var medlem av folketrygda på uføretidspunktet, og:",
                        English to "We can also make an exception if you had national insurance coverage at the time of your disability, and:",
                    )
                    list {
                        item {
                            text(
                                Bokmal to "ble ufør før du fylte 26 år,",
                                Nynorsk to "blei ufør før du fylte 26 år,",
                                English to "you became disabled before the age of 26,",
                            )
                        }
                        item {
                            text(
                                Bokmal to "ikke har vært utmeldt av folketrygden i mer enn fem år før dette tidspunktet, eller",
                                Nynorsk to "ikkje har vore utmeld av folketrygda i meir enn fem år før dette tidspunktet, eller",
                                English to "and you had not been without national insurance coverage for more than five years before that time, or",
                            )
                        }
                        item {
                            text(
                                Bokmal to "har tjent opp rett til minst halvparten av minsteytelsen for uføretrygd",
                                Nynorsk to "har tent opp rett til minst halvparten av minsteytinga for uføretrygd.",
                                English to "you have earned the right to no less than half of the minimum disability benefit.",
                            )
                        }
                    }
                }

                //[TBU2394]
                paragraph {
                    textExpr(
                        Bokmal to "Du flyttet til Norge <FRIKTEKST: siste innflyttingsdato til Norge>, og ble da medlem av folketrygden. Vi har fastsatt uføretidspunktet ditt til ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()
                            .format() + ". Da ble inntektsevnen din varig nedsatt med minst ",
                        Nynorsk to "Du flytta til Noreg <FRITEKST: siste innflyttingsdato til Norge>, og blei då medlem av folketrygda. Vi har fastsett uføretidspunktet ditt til ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()
                            .format() + ". Då blei inntektsevna di varig sett ned med minst ",
                        English to "You moved to Norway on <FRITEKST: siste innflyttingsdato til Norge>, and you have national insurance coverage from that date. We have determined your date of disability to be ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()
                            .format() + ". Your earning ability then became permanently reduced by at least ",
                    )

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_o_1") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_o_1"))) {
                        text(
                            Bokmal to "halvparten",
                            Nynorsk to "halvparten",
                            English to "half",
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_o_2") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_o_2"))) {
                        text(
                            Bokmal to "40 prosent",
                            Nynorsk to "40 prosent",
                            English to "40 percent",
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_112_7_2_o_3") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_o_3"))) {
                        text(
                            Bokmal to "30 prosent",
                            Nynorsk to "30 prosent",
                            English to "30 percent",
                        )
                    }
                    text(
                        Bokmal to ".",
                        Nynorsk to ".",
                        English to ".",
                    )
                }

                //[TBU2395NN, TBU2395EN, TBU2395]

                paragraph {
                    textExpr(
                        Bokmal to "Du var ikke medlem av folketrygden i ".expr() + pe.aars_trygdetid() + " år før du ble ufør. Du oppfyller heller ingen av unntaksreglene. Vi avslår derfor søknaden din om uføretrygd. ",
                        Nynorsk to "Du var ikkje medlem av folketrygda i ".expr() + pe.aars_trygdetid() + " år før du blei ufør. Du oppfyller heller ingen av unntaksreglane. Vi avslår derfor søknaden din om uføretrygd.",
                        English to "You did not have national insurance coverage for the ".expr() + pe.aars_trygdetid() + " years leading up to the date of disability. Nor do you meet any of the exemption requirements. We thus deny your application for disability benefit.",
                    )
                }
                //[TBU2396NN, TBU2396EN, TBU2396]

                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-2.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-2.",
                        English to "This decision is made pursuant to Sections 12-2 of the National Insurance Act.",
                    )
                }

                //IF(PE_aars_trygdetid = "fem" AND PE_Vedtaksdata_Kravhode_KravGjelder = "forsteg_bh") THEN      INCLUDE ENDIF
                showIf((pe.aars_trygdetid().equalTo("fem") and pe.vedtaksdata_kravhode_kravgjelder().equalTo("forsteg_bh"))) {
                    //[TBU5017_NN, TBU5017_EN, TBU5017]

                    title2 {
                        text(
                            Bokmal to "Supplerende stønad til uføre flyktninger",
                            Nynorsk to "Supplerande stønad til uføre flyktningar",
                            English to "Supplementary benefit as a disabled refugee",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Hvis du har godkjent flyktningstatus etter § 28 i utlendingsloven gitt i vedtak fra Utlendingsnemnda (UDI), kan du søke om supplerende stønad til uføre flyktninger. Stønaden er behovsprøvd og all inntekt/formue fra Norge og utlandet blir regnet med. Inntekten/formuen til eventuell ektefelle, samboer eller registrert partner blir også regnet med. Du kan lese mer om supplerende stønad til uføre flyktninger på vår nettside ${Constants.NAV_URL}.",
                            Nynorsk to "Dersom du har godkjend flyktningstatus etter § 28 i utlendingslova gitt i vedtak frå Utlendingsnemnda (UDI), kan du søkje om supplerande stønad til uføre flyktningar. Stønaden er behovsprøvd, og all inntekt/formue frå Noreg og utlandet blir rekna med. Inntekta/formuen til eventuell ektefelle, sambuar eller registrert partnar skal også reknast med. Du kan lese meir om supplerande stønad til uføre flyktningar på nettsida vår ${Constants.NAV_URL}.",
                            English to "If you have been granted refugee status by the Directorate of Immigration (UDI), according to the provisions of § 28 of the Immigration Act, you can apply for supplementary benefit as a disabled refugee. The benefit is means-tested and your total income/fortune from Norway and abroad is taken into account. The income/fortune of any spouse, cohabitant or registered partner will also be taken into account. You can read more about supplementary benefit as a disabled refugee at our website ${Constants.NAV_URL}.",
                        )
                    }
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_FortsattMedlemsskapResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_ForutgaendeMedlemskapResultat) <> "ikke_oppfylt") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_fortsattmedlemsskapresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskapresultat()
                    .notEqualTo("ikke_oppfylt"))
            ) {
                //[TBU2397NN, TBU2397EN, TBU2397]

                paragraph {
                    text(
                        Bokmal to "Du bor i <FRITEKST: land>. Dette er et land Norge ikke har trygdeavtale med.",
                        Nynorsk to "Du bur i <FRITEKST: land>. Dette er eit land Noreg ikkje har trygdeavtale med.",
                        English to "You live in <FRITEKST: land>. This is a country with which Norway does not have a national insurance agreement.",
                    )
                }
                //[TBU2398NN, TBU2398EN, TBU2398]

                paragraph {
                    text(
                        Bokmal to "For å ha rett til uføretrygd må du derfor ha jobbet i Norge i minst ett år før du ble ufør, og hatt pensjonsgivende inntekt på minst folketrygdens grunnbeløp.",
                        Nynorsk to "For å ha rett til uføretrygd må du derfor ha arbeidd i Noreg i minst eitt år før du blei ufør, og hatt pensjonsgivande inntekt som svarer til grunnbeløpet i folketrygda.",
                        English to "In order to be entitled to disability benefit, you must therefore have worked in Norway for no less than one year prior to your disability, and you must have had pensionable income equivalent to no less than the National Insurance basic amount.",
                    )
                }
                //[TBU2399NN, TBU2399EN, TBU2399]

                paragraph {
                    textExpr(
                        Bokmal to "Vi har satt uføretidspunktet ditt til ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()
                            .format() + ". Da ble inntektsevnen din varig nedsatt med minst ",
                        Nynorsk to "Vi har sett uføretidspunktet ditt til ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()
                            .format() + ". Då blei inntektsevna di varig sett ned med minst ",
                        English to "We have determined your date of disability to be ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()
                            .format() + ". Your earning ability then became permanently reduced by at least ",
                    )

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_o_1") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_o_1"))) {
                        text(
                            Bokmal to "halvparten",
                            Nynorsk to "halvparten",
                            English to "half",
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_o_2") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_o_2"))) {
                        text(
                            Bokmal to "40 prosent",
                            Nynorsk to "40 prosent",
                            English to "40 percent",
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_o_3") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_o_3"))) {
                        text(
                            Bokmal to "30 prosent",
                            Nynorsk to "30 prosent",
                            English to "30 percent",
                        )
                    }
                    text(
                        Bokmal to ". Ut fra opplysningene i saken din <FRITEKST: arbeid og inntekt i Norge>. Du har ikke jobbet i Norge i minst ett år før uføretidspunktet ditt. Du oppfyller derfor ikke dette kravet.",
                        Nynorsk to ". Ut frå opplysningane i saka di <FRITEKST: arbeid og inntekt i Norge> har du ikkje arbeidd i Noreg i minst eitt år før uføretidspunktet ditt. Du oppfyller derfor ikkje dette kravet.",
                        English to ". Given the information available in your case, <FRITEKST: arbeid og inntekt i Norge>. You have not worked in Norway for one year or more before your date of disability. You do not meet this requirement.",
                    )
                }
                //[TBU2400NN, TBU2400EN, TBU2400]

                paragraph {
                    text(
                        Bokmal to "Du kan også ha rett til uføretrygd hvis du har bodd i Norge i 20 år før uføretidspunktet ditt. Du må da ha vært medlem av folketrygden i denne perioden.",
                        Nynorsk to "Du kan også ha rett til uføretrygd dersom du har budd i Noreg i 20 år før uføretidspunktet ditt. Du må då ha vore medlem av folketrygda i denne perioden.",
                        English to "You may also be entitled to disability benefit if you have lived in Norway for no less than 20 years prior to your date of disability. You must have had national insurance coverage for this period.",
                    )
                }
                //[TBU2401NN, TBU2401EN, TBU2401]

                paragraph {
                    text(
                        Bokmal to "Du bodde i Norge og var medlem av folketrygden fra <FRITEKST: FOMmedlemsperiode Norge> til <FRITEKST: TOMmedlemsperiode Norge>. Du har ikke vært medlem av folketrygden i 20 år, og oppfyller heller ikke dette kravet. Vi avslår derfor søknaden din om uføretrygd.",
                        Nynorsk to "Du budde i Noreg og var medlem av folketrygda frå <FRITEKST: FOMmedlemsperiode Norge> til <FRITEKST: TOMmedlemsperiode Norge>. Du har ikkje vore medlem av folketrygda i 20 år før uføretidspunktet ditt, og oppfyller heller ikkje dette kravet. Vi avslår derfor søknaden din om uføretrygd.",
                        English to "You lived in Norway, and had national insurance coverage from <FRITEKST: FOMmedlemsperiode Norge> to <FRITEKST: TOMmedlemsperiode Norge>. You have not had national insurance coverage for 20 years prior to your disability, and, consequently, you do not meet this requirement either. We therefore deny your application for disability benefit.",
                    )
                }
                //[TBU2402NN, TBU2402EN, TBU2402]

                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-3.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-3.",
                        English to "This decision is made pursuant to Section 12-3 of the National Insurance Act.",
                    )
                }
            }

            //IF(  PE_Vedtaksdata_Kravhode_VurdereTrygdeavtale = true  AND (PE_Vedtaksdata_Kravhode_KravGjelder = "f_bh_bo_utland"  OR PE_Vedtaksdata_Kravhode_KravGjelder = "mellombh")   AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_ForutgaendeMedlemskapResultat) = "ikke_oppfylt" ) THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_kravhode_vurderetrygdeavtale() and (pe.vedtaksdata_kravhode_kravgjelder()
                    .equalTo("f_bh_bo_utland") or pe.vedtaksdata_kravhode_kravgjelder()
                    .equalTo("mellombh")) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskapresultat().equalTo(
                    "ikke_oppfylt"
                ))
            ) {
                //[TBU2403NN, TBU2403EN, TBU2403]

                paragraph {
                    text(
                        Bokmal to "For å ha rett til uføretrygd må du ha vært medlem av folketrygden i de siste tre årene fram til uføretidspunktet. Fordi du har bodd i et land Norge har trygdeavtale med, kan vi gjøre unntak fra denne regelen. Vi gjør dette ved å legge sammen periodene dine med medlemskap i folketrygden og medlemskap i den <FRITEKST: nasjonalitet> trygdeordningen.",
                        Nynorsk to "For å ha rett til uføretrygd må du ha vore medlem av folketrygda i dei siste tre åra fram til uføretidspunktet. Fordi du har budd i eit land Noreg har trygdeavtale med, kan vi gjere unntak frå denne regelen. Vi gjer dette ved å leggje saman periodane dine med medlemskap i folketrygda og medlemskap i den <FRITEKST: nasjonalitet> trygdeordninga.",
                        English to "In order to be entitled to disability benefit, you must have had national insurance coverage for the last three years prior to your disability. Because you have lived in a country with which Norway has a national insurance agreement, we can make an exception to this rule. This is done by adding up your periods of national insurance coverage in Norway and your periods of coverage by <FRITEKST: nasjonalitet> national insurance.",
                    )
                }
                //[TBU2404NN, TBU2404EN, TBU2404]

                paragraph {
                    text(
                        Bokmal to "Uføretidspunktet ditt er satt til <FRITEKST: uføretidspunkt>. Da ble inntektsevnen din varig nedsatt med minst ",
                        Nynorsk to "Uføretidspunktet ditt er sett til <FRITEKST: uføretidspunkt>. Då blei inntektsevna di varig sett ned med minst ",
                        English to "Your date of disability has been determined to <FRITEKST: uføretidspunkt>. Your earning ability then became permanently reduced by at least ",
                    )

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_o_1") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_o_1"))) {
                        text(
                            Bokmal to "halvparten",
                            Nynorsk to "halvparten",
                            English to "half",
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_o_2") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_o_2"))) {
                        text(
                            Bokmal to "40 prosent",
                            Nynorsk to "40 prosent",
                            English to "40 percent",
                        )
                    }

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_o_3") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_o_3"))) {
                        text(
                            Bokmal to "30 prosent",
                            Nynorsk to "30 prosent",
                            English to "30 percent",
                        )
                    }
                    text(
                        Bokmal to ".",
                        Nynorsk to ".",
                        English to ".",
                    )
                }
                //[TBU2405NN, TBU2405EN, TBU2405]

                paragraph {
                    text(
                        Bokmal to "Du var medlem av den norske folketrygden: ",
                        Nynorsk to "Du var medlem av den norske folketrygda: ",
                        English to "You had national insurance coverage in Norway: ",
                    )
                    text(
                        Bokmal to "<FRITEKST: FOMmedlemskap Norge> til <FRITEKST: TOMmedlemskap Norge>.",
                        Nynorsk to "<FRITEKST: FOMmedlemskap Norge> til <FRITEKST: TOMmedlemskap Norge>.",
                        English to "<FRITEKST: FOMmedlemskap Norge> to <FRITEKST: TOMmedlemskap Norge>.",
                    )
                }
                //[TBU2406NN, TBU2406EN, TBU2406]

                paragraph {
                    text(
                        Bokmal to "Du var medlem av den <FRITEKST: nasjonalitet> trygdeordningen:",
                        Nynorsk to "Du var medlem av den <FRITEKST: nasjonalitet> trygdeordninga:",
                        English to "You had national insurance coverage in <FRITEKST: nasjonalitet>:",
                    )
                    text(
                        Bokmal to "<FRITEKST: FOMutenlandsk medlemskap> til <FRITEKST: TOMutenlandsk medlemskap>.",
                        Nynorsk to "<FRITEKST: FOMutenlandsk medlemskap> til <FRITEKST: TOMutenlandsk medlemskap>.",
                        English to "<FRITEKST: FOMutenlandsk medlemskap> to <FRITEKST: TOMutenlandsk medlemskap>.",
                    )
                }
                //[TBU2407NN, TBU2407EN, TBU2407]

                paragraph {
                    text(
                        Bokmal to "Du har ikke vært medlem av folketrygden og den <FRITEKST: nasjonalitet> trygdeordningen sammenhengende i minst tre år fram til uføretidspunktet ditt. Vi avslår derfor søknaden din om uføretrygd.",
                        Nynorsk to "Du har ikkje vore medlem av folketrygda og den <FRITEKST: nasjonalitet> trygdeordninga samanhengande i minst tre år fram til uføretidspunktet ditt. Vi avslår derfor søknaden din om uføretrygd.",
                        English to "You did not have continuous coverage by Norwegian national insurance and the <FRITEKST: land> national insurance for three years or more prior to your disability. We thus deny your application for disability benefit.",
                    )
                }
                //[TBU2408NN, TBU2408EN, TBU2408]

                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter <FRITEKST: trygdeavtale brukt for inngang med artikkel> avtalen artikkel og folketrygdloven § 12-2.",
                        Nynorsk to "Vedtaket er gjort etter <FRITEKST: trygdeavtale brukt for inngang med artikkel> avtalen artikkel og folketrygdlova § 12-2.",
                        English to "This decision is made pursuant to <FRITEKST: trygdeavtale brukt for inngang med artikkel> and Section 12-2 of the National Insurance Act.",
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_AlderResultat) = "ikke_oppfylt") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_alderresultat().equalTo("ikke_oppfylt"))) {

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_AlderBegrunnelse) = "stdbegr_12_4_1_i_1") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_alderbegrunnelse().equalTo("stdbegr_12_4_1_i_1"))) {
                    //[TBU2409NN, TBU2409EN, TBU2409]

                    paragraph {
                        textExpr(
                            Bokmal to "Du er ".expr() + pe.ut_vilkargjelderpersonalder().format() + " år.",
                            Nynorsk to "Du er ".expr() + pe.ut_vilkargjelderpersonalder().format() + " år.",
                            English to "You are ".expr() + pe.ut_vilkargjelderpersonalder().format() + " years old.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_AlderBegrunnelse) = "stdbegr_12_4_1_i_2") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_alderbegrunnelse().equalTo("stdbegr_12_4_1_i_2"))) {
                    //[TBU2410NN, TBU2410EN, TBU2410]

                    paragraph {
                        textExpr(
                            Bokmal to "Når du søker om uføretrygd mellom fylte 62 og 67 år, må din pensjonsgivende inntekt ha vært minst folketrygdens grunnbeløp i året før uføretidspunktet. Hvis du ikke oppfyller dette vilkåret, må du ha tjent minst tre ganger folketrygdens grunnbeløp i løpet av de tre siste årene før dette tidspunktet. Grunnbeløpet utgjør ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()
                                .format() + " kroner. I tillegg kan du ikke motta gjenlevendepensjon, eller ha rett til å ta ut hel alderspensjon.",
                            Nynorsk to "Når du søkjer om uføretrygd mellom fylte 62 og 67 år, må den pensjonsgivande inntekta di ha vore minst grunnbeløpet i folketrygda i året før uføretidspunktet. Dersom du ikkje oppfyller dette vilkåret, må du ha tent minst tre gonger grunnbeløpet i folketrygda gjennom dei tre siste åra før dette tidspunktet. Grunnbeløpet utgjer ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()
                                .format() + " kroner. I tillegg kan du ikkje få attlevandepensjon eller ha rett til å ta ut heil alderspensjon.",
                            English to "When you apply for disability benefit between the ages of 62 and 67, your pensionable income the year prior to your disability must be no less than the National Insurance basic amount. If you do not meet this requirement, your income must be no less than three times the National Insurance basic amount for the last three years before this date. The National Insurance basic amount is NOK ".expr() + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()
                                .format() + ". In addition, you cannot be receiving survivor's pension or be entitled to a full retirement pension.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_AlderBegrunnelse) = "stdbegr_12_4_1_i_2") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_alderbegrunnelse().equalTo("stdbegr_12_4_1_i_2"))) {
                    //[TBU2411NN, TBU2411EN, TBU2411]

                    paragraph {
                        text(
                            Bokmal to "De pensjonsgivende inntektene dine er mindre enn dette.",
                            Nynorsk to "Dei pensjonsgivande inntektene dine er mindre enn dette.",
                            English to "Your pensionable income is less than the required total.",
                        )
                    }
                }
                includePhrase(TBU2412_Generated)
                //[TBU2413NN, TBU2413EN, TBU2413]

                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-4.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-4.",
                        English to "This decision is made pursuant to Section 12-4 of the National Insurance Act.",
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingBegrunnelse) = "stdbegr_12_5_1_i_1" AND FF_GetArrayElement_String(PE_Vedtaksdata_Kravhode_Kravlinjeliste_Kravlinje_Kravlinjetype) = "ut" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_okn_ug") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingbegrunnelse().equalTo(
                    "stdbegr_12_5_1_i_1"
                ) and pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype().equalTo("ut") and pe.vedtaksdata_kravhode_kravgjelder()
                    .notEqualTo("sok_okn_ug"))
            ) {
                //[TBU2414NN, TBU2414EN, TBU2414]

                paragraph {
                    text(
                        Bokmal to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>.",
                        Nynorsk to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>.",
                        English to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>.",
                    )
                    text(
                        Bokmal to "Vi har ut fra sakens opplysninger vurdert at du ikke har gjennomført all hensiktsmessig behandling. Du har heller ikke gjennomført arbeidsrettede tiltak, som kan bedre dine inntektsmuligheter i arbeidslivet.",
                        Nynorsk to "Vi har ut frå opplysningane i saka vurdert det slik at du ikkje har gjennomført all formålstenleg behandling. Du har heller ikkje gjennomført arbeidsretta tiltak som kan betre moglegheitene dine for deg å skaffe deg inntekter i arbeidslivet.",
                        English to "Given the information available in this case, we have concluded that you have not completed all relevant treatment. Nor have you completed relevant employment schemes, which could improve your chances of earning an income.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi kan derfor ikke vurdere om sykdom eller skade har ført til at inntektsevnen din er varig nedsatt.",
                        Nynorsk to "Vi kan derfor ikkje vurdere om sjukdom eller skade har ført til at inntektsevna di er varig sett ned.",
                        English to "Therefore, we are unable to assess whether illness or injury has caused your earning ability to be permanently reduced.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd.",
                        Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd.",
                        English to "You do not meet the requirements, and your application for disability benefit is thus denied.",
                    )
                }
                includePhrase(TBU2422_Generated)
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingBegrunnelse) = "stdbegr_12_5_1_i_1" AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_okn_ug") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingbegrunnelse().equalTo(
                    "stdbegr_12_5_1_i_1"
                ) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_okn_ug"))
            ) {
                //[TBU2415NN, TBU2415EN, TBU2415]

                paragraph {
                    text(
                        Bokmal to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        Nynorsk to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        English to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                    )
                    text(
                        Bokmal to "Vi har ut fra sakens opplysninger vurdert at du ikke har gjennomført all hensiktsmessig behandling. Du har heller ikke gjennomført de arbeidsrettede tiltakene som kan bedre dine inntektsmuligheter i arbeidslivet.",
                        Nynorsk to "Vi har ut frå opplysningane i saka vurdert det slik at du ikkje har gjennomført all formålstenleg behandling. Du har heller ikkje gjennomført dei arbeidsretta tiltaka som kan betre moglegheitene dine for å skaffe deg inntekter i arbeidslivet.",
                        English to "Given the information available in this case, we have concluded that you have not completed all relevant treatment. Nor have you completed relevant employment schemes, which could improve your earning opportunities in the job market.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Fordi behandling og arbeidsrettede tiltak ikke er gjennomført, kan vi ikke ta stilling til om inntektsevnen din er varig nedsatt med mer enn <FRITEKST: Nåværende uføregrad> prosent.",
                        Nynorsk to "Fordi du ikkje har gjennomført behandling og arbeidsretta tiltak, kan vi ikkje ta stilling til om inntektsevna di er varig nedsett med meir enn <FRITEKST: Nåværende uføregrad> prosent.",
                        English to "Because relevant treatments and employment schemes have not been tried, we cannot conclude whether your earning ability is permanently reduced by more than <FRITEKST: Nåværende uføregrad> percent.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføretrygd.",
                        Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om auka uføretrygd.",
                        English to "You do not meet the requirements, and your application for disability benefit is thus denied.",
                    )
                }
                includePhrase(TBU2423_Generated)
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_Kravhode_Kravlinjeliste_Kravlinje_Kravlinjetype) = "ut" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingBegrunnelse) = "stdbegr_12_5_1_i_2" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_okn_ug") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype()
                    .equalTo("ut") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingbegrunnelse()
                    .equalTo("stdbegr_12_5_1_i_2") and pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_okn_ug"))
            ) {
                //[TBU2416NN, TBU2416EN, TBU2416]

                paragraph {
                    text(
                        Bokmal to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        Nynorsk to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        English to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                    )
                    text(
                        Bokmal to "Ut fra opplysninger i saken din, er du fortsatt i behandling. Vi har derfor vurdert at du ikke har gjennomført all hensiktsmessig behandling som kan bedre funksjonsevnen din. Du har heller ikke gjennomført arbeidsrettede tiltak eller forsøkt annet arbeid som kan bedre dine inntektsmuligheter i arbeidslivet. Det er sannsynlig at du senere vil kunne være i aktivitet og gjennomføre arbeidsrettede tiltak. ",
                        Nynorsk to "Ut frå opplysningar i saka di er du framleis i behandling. Vi har derfor vurdert det slik at du ikkje har gjennomført all formålstenleg behandling som kan betre funksjonsevna di. Du har heller ikkje gjennomført arbeidsretta tiltak eller forsøkt anna arbeid som kan betre moglegheitene dine for å skaffe deg inntekter i arbeidslivet. Det er sannsynleg at du seinare vil kunne vere i aktivitet og gjennomføre arbeidsretta tiltak.",
                        English to "Given the information available in your case, you are still receiving treatment. We have thus concluded that you have not yet completed all relevant treatment, which could improve your functional ability. You have also not completed relevant employment schemes, which could improve your chances of earning an income in the job market. It is likely that you, in the future, will be able to be more active and complete employment schemes.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Det er derfor for tidlig å ta stilling til om sykdom eller skade har ført til at inntektsevnen din er varig nedsatt. ",
                        Nynorsk to "Det er derfor for tidleg å ta stilling til om sjukdom eller skade har ført til at inntektsevna di er varig sett ned.",
                        English to "It is therefore too early to conclude whether illness or injury has caused your earning ability to be permanently reduced.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd.",
                        Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd.",
                        English to "You do not meet the requirements, and your application for disability benefit is thus denied.",
                    )
                }
                includePhrase(TBU2422_Generated)
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingBegrunnelse) = "stdbegr_12_5_1_i_2" AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_okn_ug") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingbegrunnelse().equalTo(
                    "stdbegr_12_5_1_i_2"
                ) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_okn_ug"))
            ) {
                //[TBU2417NN, TBU2417EN, TBU2417]

                paragraph {
                    text(
                        Bokmal to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        Nynorsk to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        English to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                    )
                    text(
                        Bokmal to "Ut fra opplysninger i saken din, er du fortsatt i behandling. Vi har derfor vurdert at du ikke har gjennomført all hensiktsmessig behandling som kan bedre funksjonsevnen din. Du har heller ikke gjennomført arbeidsrettede tiltak eller forsøkt annet arbeid som kan bedre dine inntektsmuligheter i arbeidslivet.",
                        Nynorsk to "Ut frå opplysningar i saka di er du framleis i behandling. Vi har derfor vurdert det slik at du ikkje har gjennomført all formålstenleg behandling som kan betre funksjonsevna di. Du har heller ikkje gjennomført arbeidsretta tiltak, eller forsøkt anna arbeid som kan betre moglegheitene dine for å skaffe deg inntekter i arbeidslivet.",
                        English to "Given the information available in your case, you are still receiving treatment. We have thus concluded that you have not yet completed all relevant treatment, which could improve your functional ability. You have also not completed relevant employment schemes, which could improve your chances of earning an income in the job market.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Det er sannsynlig at du senere vil kunne være i aktivitet og gjennomføre arbeidsrettede tiltak. Vi kan derfor ikke ta stilling til om inntektsevnen din er varig nedsatt med mer enn <FRITEKST: Nåværende uføregrad> prosent.",
                        Nynorsk to "Det er sannsynleg at du seinare vil kunne vere i aktivitet og gjennomføre arbeidsretta tiltak. Vi kan derfor ikkje ta stilling til om inntektsevna di er varig sett ned med meir enn <FRITEKST: Nåværende uføregrad> prosent.",
                        English to "It is likely that you, in the future, can be more active and complete employment schemes. We can therefore not conclude whether your earning ability is permanently reduced by more than <FRITEKST: Nåværende uføregrad> percent.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføretrygd.",
                        Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om auka uføretrygd.",
                        English to "You do not meet the requirements, and your application for increased disability benefit is thus denied.",
                    )
                }
                includePhrase(TBU2423_Generated)
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_Kravhode_Kravlinjeliste_Kravlinje_Kravlinjetype) = "ut" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakBegrunnelse) = "stdbegr_12_5_2_i_1" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_okn_ug") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat()
                    .equalTo("oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype()
                    .equalTo("ut") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakbegrunnelse()
                    .equalTo("stdbegr_12_5_2_i_1") and pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_okn_ug"))
            ) {
                //[TBU2418NN, TBU2418EN, TBU2418]

                paragraph {
                    text(
                        Bokmal to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        Nynorsk to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        English to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                    )
                    text(
                        Bokmal to "Vi har ut fra sakens opplysninger vurdert at du har gjennomført hensiktsmessig behandling. Du har imidlertid ikke gjennomført arbeidsrettede tiltak eller forsøkt annet arbeid som kan bedre dine inntektsmuligheter i arbeidslivet. Det er ingen åpenbare grunner til at du ikke kan gjennomføre arbeidsrettede tiltak.",
                        Nynorsk to "Vi har ut frå opplysningane i saka vurdert det slik at du har gjennomført formålstenleg behandling. Du har likevel ikkje gjennomført arbeidsretta tiltak eller forsøkt anna arbeid som kan betre moglegheitene dine for å få inntekter i arbeidslivet. Det er ingen openberre grunnar til at du ikkje kan gjennomføre arbeidsretta tiltak.",
                        English to "Given the information available in this case, we have concluded that you have completed all relevant treatment. However, you have not completed relevant employment schemes, nor tried another line of work, which could improve your changes of earning an income in the job market. There are no obvious reasons why you cannot complete employment schemes.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Det er derfor for tidlig å ta stilling til i hvor stor grad inntektsevnen din er varig nedsatt, og om dette skyldes sykdom eller skade.",
                        Nynorsk to "Det er derfor for tidleg å ta stilling til i kor stor grad inntektsevna di er varig sett ned, og om dette kjem av sjukdom eller skade.",
                        English to "It is therefore too early to conclude on the degree to which your earning ability has been permanently reduced, and whether the reduction is due to illness or injury.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd.",
                        Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd.",
                        English to "You do not meet the requirements, and your application for disability benefit is thus denied.",
                    )
                }
                includePhrase(TBU2422_Generated)
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "ikke_oppfylt" AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_okn_ug" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakBegrunnelse) = "stdbegr_12_5_2_i_1") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat()
                    .equalTo("oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_kravhode_kravgjelder()
                    .equalTo("sok_okn_ug") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakbegrunnelse().equalTo(
                    "stdbegr_12_5_2_i_1"
                ))
            ) {
                //[TBU2419NN, TBU2419EN, TBU2419]

                paragraph {
                    text(
                        Bokmal to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        Nynorsk to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        English to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                    )
                    text(
                        Bokmal to "Vi har ut fra sakens opplysninger vurdert at du har gjennomført hensiktsmessig behandling. Du har imidlertid ikke gjennomført arbeidsrettede tiltak eller forsøkt annet arbeid som kan bedre dine inntektsmuligheter i arbeidslivet.",
                        Nynorsk to "Vi har ut frå opplysningane i saka vurdert det slik at du har gjennomført formålstenleg behandling. Du har likevel ikkje gjennomført arbeidsretta tiltak eller forsøkt anna arbeid som kan betre inntektsmoglegheitene dine i arbeidslivet.",
                        English to "Given the information available in this case, we have concluded that you have completed all relevant treatment. However, you have not completed relevant employment schemes, nor tried another line of work, which could improve your chances of earning an income in the job market.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Det er ingen åpenbare grunner til at du ikke kan gjennomføre arbeidsrettede tiltak. Vi kan derfor ikke ta stilling til om inntektsevnen din er varig nedsatt med mer enn <FRITEKST: Nåværende uføregrad> prosent. ",
                        Nynorsk to "Det er ingen openberre grunnar til at du ikkje kan gjennomføre arbeidsretta tiltak. Vi kan derfor ikkje ta stilling til om inntektsevna di er varig sett ned med meir enn <FRITEKST: Nåværende uføregrad> prosent.",
                        English to "There are no obvious reasons why you cannot complete employment schemes. We can therefore not conclude whether your earning ability is permanently reduced by more than <FRITEKST: Nåværende uføregrad> percent.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføretrygd.",
                        Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om auka uføretrygd.",
                        English to "You do not meet the requirements, and your application for increased disability benefit is thus denied.",
                    )
                }
                includePhrase(TBU2423_Generated)
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakBegrunnelse) = "stdbegr_12_5_2_i_2" AND FF_GetArrayElement_String(PE_Vedtaksdata_Kravhode_Kravlinjeliste_Kravlinje_Kravlinjetype) = "ut" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_okn_ug") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat()
                    .equalTo("oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakbegrunnelse().equalTo(
                    "stdbegr_12_5_2_i_2"
                ) and pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype().equalTo("ut") and pe.vedtaksdata_kravhode_kravgjelder()
                    .notEqualTo("sok_okn_ug"))
            ) {
                //[TBU2420NN, TBU2420EN, TBU2420]

                paragraph {
                    text(
                        Bokmal to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        Nynorsk to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        English to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                    )
                    text(
                        Bokmal to "Vi har ut fra sakens opplysninger vurdert at du har gjennomført hensiktsmessig behandling. Du har imidlertid ikke gjennomført alle arbeidsrettede tiltak eller forsøkt annet arbeid som kan bedre dine inntektsmuligheter i arbeidslivet. Det er ingen åpenbare grunner til at du ikke kan delta på flere arbeidsrettede tiltak.",
                        Nynorsk to "Vi har ut frå opplysningane i saka vurdert det slik at du har gjennomført formålstenleg behandling. Du har likevel ikkje gjennomført alle arbeidsretta tiltak eller forsøkt anna arbeid som kan betre inntektsmoglegheitene dine i arbeidslivet. Det er ingen openberre grunnar til at du ikkje kan delta på fleire arbeidsretta tiltak.",
                        English to "Given the information available in this case, we have concluded that you have completed all relevant treatment. However, you have not completed all relevant employment schemes, nor tried another line of work, which could improve your chances of earning an income in the job market. There are no obvious reasons why you cannot participate in more employment schemes.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Det er derfor for tidlig å ta stilling til i hvor stor grad inntektsevnen din er varig nedsatt, og om dette skyldes sykdom eller skade.",
                        Nynorsk to "Det er derfor for tidleg å ta stilling til i kor stor grad inntektsevna di er varig sett ned, og om dette kjem av sjukdom eller skade.",
                        English to "It is therefore too early to conclude on the degree to which your earning ability has been permanently reduced, and whether the reduction is due to illness or injury.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd.",
                        Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd.",
                        English to "You do not meet the requirements, and your application for disability benefit is thus denied.",
                    )
                }
                includePhrase(TBU2422_Generated)
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakBegrunnelse) = "stdbegr_12_5_2_i_2" AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_okn_ug") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat()
                    .equalTo("oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat()
                    .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakbegrunnelse().equalTo(
                    "stdbegr_12_5_2_i_2"
                ) and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_okn_ug"))
            ) {
                //[TBU2421NN, TBU2421EN, TBU2421]

                paragraph {
                    text(
                        Bokmal to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        Nynorsk to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        English to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                    )
                    text(
                        Bokmal to "Vi har ut fra sakens opplysninger vurdert at du har gjennomført hensiktsmessig behandling. Du har imidlertid ikke gjennomført alle arbeidsrettede tiltak eller forsøkt annet arbeid som kan bedre dine inntektsmuligheter i arbeidslivet. Det er ingen åpenbare grunner til at du ikke kan delta på flere arbeidsrettede tiltak.",
                        Nynorsk to "Vi har ut frå opplysningane i saka vurdert det slik at du har gjennomført formålstenleg behandling. Du har likevel ikkje gjennomført alle arbeidsretta tiltak eller forsøkt anna arbeid som kan betre inntektsmoglegheitene dine i arbeidslivet. Det er ingen openberre grunnar til at du ikkje kan delta på fleire arbeidsretta tiltak.",
                        English to "Given the information available in this case, we have concluded that you have completed all relevant treatment. However, you have not completed all relevant employment schemes, nor tried another line of work, which could improve your chances of earning an income in the job market. There are no obvious reasons why you cannot participate in more employment schemes.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi kan derfor ikke ta stilling til om inntektsevnen din er varig nedsatt med mer enn <FRITEKST: Nåværende uføregrad> prosent.",
                        Nynorsk to "Vi kan derfor ikkje ta stilling til om inntektsevna di er varig sett ned med meir enn <FRITEKST: Nåværende uføregrad> prosent.",
                        English to "We can therefore not conclude whether your earning ability is permanently reduced by more than <FRITEKST: Nåværende uføregrad> percent.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om økt uføretrygd.",
                        Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om auka uføretrygd.",
                        English to "You do not meet the requirements, and your application for increased disability benefit is thus denied.",
                    )
                }
                includePhrase(TBU2423_Generated)
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) <> "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) <> "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_SykdomSkadeLyteResultat) = "ikke_oppfylt") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat().notEqualTo(
                    "ikke_oppfylt"
                ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat()
                    .notEqualTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_sykdomskadelyteresultat().equalTo("ikke_oppfylt"))
            ) {

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_SykdomSkadeLyteBegrunnelse) = "stdbegr_12_6_1_i_1" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_Kravhode_Kravlinjeliste_Kravlinje_Kravlinjetype) = "ut") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_sykdomskadelytebegrunnelse()
                        .equalTo("stdbegr_12_6_1_i_1") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat()
                        .equalTo("oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat().equalTo(
                        "oppfylt"
                    ) and pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype().equalTo("ut"))
                ) {
                    //[TBU2424NN, TBU2424EN, TBU2424]

                    paragraph {
                        text(
                            Bokmal to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                            Nynorsk to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                            English to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        )
                        text(
                            Bokmal to "Vi har ut fra sakens opplysninger vurdert at sykdom har ført til at funksjonsevnen din er nedsatt. Det er imidlertid ikke dokumentert at du har varig sykdom.",
                            Nynorsk to "Vi har ut frå opplysningane i saka vurdert det slik at sjukdom har ført til at funksjonsevna di er sett ned. Det er likevel ikkje dokumentert at du har varig sjukdom.",
                            English to "Given the information available in this case, we have concluded that you have an illness that has caused your functional ability to be reduced. However, it has not been documented that your condition is permanent.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Vi kan derfor ikke ta stilling til i hvor stor grad inntektsevnen din er varig nedsatt.",
                            Nynorsk to "Vi kan derfor ikkje ta stilling til i kor stor grad inntektsevna di er varig sett ned.",
                            English to "Therefore, we cannot conclude on the degree to which your earning ability has been permanently reduced.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd.",
                            Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd.",
                            English to "You do not meet the requirements, and your application for disability benefit is thus denied.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_SykdomSkadeLyteBegrunnelse) = "stdbegr_12_6_1_i_2" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigArbeidsrettedeTiltakResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_HensiktsmessigBehandlingResultat) = "oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_Kravhode_Kravlinjeliste_Kravlinje_Kravlinjetype) = "ut") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_sykdomskadelytebegrunnelse()
                        .equalTo("stdbegr_12_6_1_i_2") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigarbeidsrettedetiltakresultat()
                        .equalTo("oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_hensiktsmessigbehandlingresultat().equalTo(
                        "oppfylt"
                    ) and pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype().equalTo("ut"))
                ) {
                    //[TBU2425NN, TBU2425EN, TBU2425]

                    paragraph {
                        text(
                            Bokmal to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                            Nynorsk to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                            English to "<FRITEKST: legg inn konkret begrunnelse der det er nødvendig>. ",
                        )
                        text(
                            Bokmal to "Vi har ut fra sakens opplysninger vurdert at sykdom har ført til at funksjonsevnen din er nedsatt. Det er imidlertid ikke dokumentert at sykdom er hovedårsak til din nedsatte funksjonsevne.",
                            Nynorsk to "Vi har ut frå opplysningane i saka vurdert det slik at du har ein sjukdom som har ført til at funksjonsevna di er sett ned. Det er likevel ikkje dokumentert at sjukdommen er hovudårsaka til den nedsette funksjonsevna di.",
                            English to "Given the information available in this case, we have concluded that you have an illness that has caused your functional ability to be reduced. However, it has not been documented that your illness is the primary cause behind your reduced functional ability.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Vi kan derfor ikke ta stilling til i hvor stor grad inntektsevnen din er varig nedsatt.",
                            Nynorsk to "Vi kan derfor ikkje ta stilling til i kor stor grad inntektsevna di er varig sett ned.",
                            English to "Therefore, we cannot conclude on the degree to which your earning ability has been permanently reduced.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din om uføretrygd.",
                            Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din om uføretrygd.",
                            English to "You do not meet the requirements, and your application for disability benefit is thus denied.",
                        )
                    }
                }
                //[TBU2426NN, TBU2426EN, TBU2426]

                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-6 og 12-7.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-6 og 12-7.",
                        English to "This decision is made pursuant to Sections 12-6 and 12-7 of the National Insurance Act.",
                    )
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_FortsattMedlemsskapResultat) <> "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_ForutgaendeMedlemskapResultat) <> "ikke_oppfylt") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_fortsattmedlemsskapresultat()
                    .notEqualTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskapresultat()
                    .notEqualTo("ikke_oppfylt"))
            ) {

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_i_2" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_i_3" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) <> "") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .equalTo("stdbegr_12_7_2_i_2") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .equalTo("stdbegr_12_7_2_i_3") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().notEqualTo(
                        ""
                    ))
                ) {
                    includePhrase(TBU2427_Generated)
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) <> "" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) <> "stdbegr_12_17_1_i_1" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse(1) <> "stdbegr_12_7_2_i_4") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .notEqualTo("") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_uu") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse().notEqualTo(
                        "stdbegr_12_17_1_i_1"
                    ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().notEqualTo("stdbegr_12_7_2_i_4"))
                ) {
                    //[TBU2428NN, TBU2428EN, TBU2428]

                    paragraph {
                        text(
                            Bokmal to "Inntektsevnen din må som hovedregel være varig nedsatt med minst 50 prosent.",
                            Nynorsk to "Inntektsevna di må som hovudregel vere varig sett ned med minst 50 prosent.",
                            English to "As a general rule, your earning ability must be permanently reduced by no less than 50 percent.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_i_2" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .equalTo("stdbegr_12_7_2_i_2") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("endring_ifu") and pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_uu") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_ys"))
                ) {
                    //[TBU2429NN, TBU2429EN, TBU2429]

                    paragraph {
                        text(
                            Bokmal to "Hvis du mottar arbeidsavklaringspenger når du søker om uføretrygd er det tilstrekkelig at inntektsevnen din er varig nedsatt med minst 40 prosent.",
                            Nynorsk to "Dersom du får arbeidsavklaringspengar når du søkjer om uføretrygd er det tilstrekkeleg at inntektsevna di er varig sett ned med minst 40 prosent. ",
                            English to "If you are receiving a work assessment allowance at the time you submit an application for disability benefit, it is sufficient that your earning ability has been permanently reduced by no less than 40 percent.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_i_3" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) <> "stdbegr_12_17_1_i_1") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .equalTo("stdbegr_12_7_2_i_3") and pe.vedtaksdata_kravhode_kravarsaktype()
                        .notEqualTo("endring_ifu") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_uu") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse().notEqualTo(
                        "stdbegr_12_17_1_i_1"
                    ))
                ) {
                    //[TBU2430NN, TBU2430EN, TBU2430]

                    paragraph {
                        text(
                            Bokmal to "Har du en godkjent yrkesskade eller yrkessykdom som er årsak til den nedsatte inntektsevnen din, er det tilstrekkelig at inntektsevnen din er varig nedsatt med minst 30 prosent.",
                            Nynorsk to "Har du ein godkjend yrkesskade eller yrkessjukdom som er årsaka til den nedsette inntektsevna di, er det tilstrekkeleg at inntektsevna di er varig sett ned med minst 30 prosent.",
                            English to "If a certified occupational injury or occupational illness is the cause behind your reduced earning ability, it is sufficient that your earning ability has been permanently reduced by at least 30 percent.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_Kravhode_KravGjelder = "sok_okn_ug"  AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_1_i_2" OR PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse(1) = "stdbegr_12_7_2_i_4" )) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravgjelder()
                        .equalTo("sok_okn_ug") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo(
                        "stdbegr_12_7_1_i_2"
                    ) or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_i_4")))
                ) {
                    //[TBU2431NN, TBU2431EN, TBU2431]

                    paragraph {
                        text(
                            Bokmal to "Du mottar <FRITEKST: Nåværende Uforegrad> prosent uføretrygd. Uføregraden kan økes dersom inntektsevnen blir varig nedsatt med mer enn dette, på grunn av sykdom eller skade.",
                            Nynorsk to "Du får <FRITEKST: Nåværende Uforegrad> prosent uføretrygd. Uføregraden kan aukast dersom inntektsevna blir varig sett ned med meir enn dette på grunn av sjukdom eller skade.",
                            English to "You are receiving <FRITEKST: Nåværende Uforegrad> percent disability benefit. Your degree of disability can be increased if your earning ability is further reduced due to illness or injury, and the reduction is permanent.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) <> "" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_AlderResultat) = "oppfylt") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .notEqualTo("") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_uu") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_ys") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_alderresultat().equalTo(
                        "oppfylt"
                    ))
                ) {
                    //[TBU2039NN, TBU2039EN, TBU2039]

                    paragraph {
                        text(
                            Bokmal to "Vi har sammenliknet inntektsmulighetene dine før og etter at du ble ufør, og har vurdert hvor mye inntektsevnen din er varig nedsatt.",
                            Nynorsk to "Vi har samanlikna inntektsmoglegheitene dine før og etter at du blei ufør, og vurdert kor mykje inntektsevna di er varig nedsett.",
                            English to "We have compared your earning capacity before and after your disability, and we have assessed how much your earning ability has been reduced.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) <> "" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) <> "stdbegr_12_17_1_i_1" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) = "stdbegr_12_8_1_2") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .notEqualTo("") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()
                        .notEqualTo("stdbegr_12_17_1_i_1") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse().equalTo(
                        "stdbegr_12_8_1_2"
                    ))
                ) {
                    //[TBU2432NN, TBU2432EN, TBU2432]

                    paragraph {
                        textExpr(
                            Bokmal to "Inntekten din før du ble ufør er fastsatt til ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
                                .format() + " kroner. <FRITEKST: begrunnelse for fastsatt IFU>. Oppjustert til dagens verdi tilsvarer dette en inntekt på <FRITEKST: Oppjustert ifu> kroner. Du har en inntekt på " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()
                                .format() + " kroner, og vi har derfor fastsatt din nedsatte inntektsevne til <FRITEKST: sett inn fastsatt uføregrad før avrunding> prosent.",
                            Nynorsk to "Inntekta di før du blei ufør, er fastsett til ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
                                .format() + " kroner. <FRITEKST: begrunnelse for fastsatt IFU>. Oppjustert til dagens verdi svarer dette til ei inntekt på <FRITEKST: Oppjustert ifu> kroner. Du har ei inntekt på " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()
                                .format() + " kroner, og vi har derfor fastsett den nedsette inntektsevna di til <FRITEKST: sett inn fastsatt uføregrad før avrunding> prosent.",
                            English to "Your income prior to your disability was determined as NOK ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
                                .format() + ". <FRITEKST: begrunnelse for fastsatt IFU>. Adjusted to current value, this is equivalent to an income of NOK <FRITEKST: Oppjustert ifu>. Your current income is NOK " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()
                                .format() + ", and we have thus determined your reduction in earning ability to <FRITEKST: sett inn fastsatt uføregrad før avrunding> percent.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse) = "stdbegr_12_8_1_3" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "mellombh") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse()
                        .equalTo("stdbegr_12_8_1_3") and pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("mellombh"))
                ) {
                    //[TBU2433NN, TBU2433EN, TBU2433]

                    paragraph {
                        textExpr(
                            Bokmal to "Inntekten din før du ble ufør er fastsatt til ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
                                .format() + " kroner. <FRITEKST: begrunnelse for fastsatt IFU>. Oppjustert til dagens verdi tilsvarer dette en inntekt på <FRITEKST: Oppjustert ifu> kroner. Det er dokumentert at du har inntektsmuligheter som du ikke benytter, og disse tar vi med når vi fastsetter inntekten din etter at du ble ufør. Inntekten din etter at du ble ufør er fastsatt til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()
                                .format() + " kroner, og din nedsatte inntektsevne er derfor fastsatt til <FRITEKST: sett inn fastsatt uføregrad før avrunding> prosent.",
                            Nynorsk to "Inntekta di før du blei ufør, er fastsett til ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
                                .format() + " kroner. <FRITEKST: begrunnelse for fastsatt IFU>. Oppjustert til dagens verdi svarer dette til ei inntekt på <FRITEKST: Oppjustert ifu> kroner. Det er dokumentert at du har moglegheiter for å skaffe inntekter som du ikkje nyttar, og desse tek vi med når vi fastset inntekta di etter at du blei ufør. Inntekta di etter at du blei ufør, er fastsett til " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()
                                .format() + " kroner, og den nedsette inntektsevna di er derfor fastsett til <FRITEKST: sett inn fastsatt uføregrad før avrunding> prosent.",
                            English to "Your income prior to your disability was established as NOK ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifuinntekt()
                                .format() + ". <FRITEKST: begrunnelse for fastsatt IFU>. Adjusted to current value, this is equivalent to an income of NOK <FRITEKST: oppjustert ifu>. It has been documented that you have options for gainful employment that you are not taking advantage of, and these will be included when your income after your disability is calculated. Your income after your disability was determined as NOK " + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieuinntekt()
                                .format() + ", and your reduction in earning ability has been determined as <FRITEKST: sett inn fastsatt uføregrad før avrunding> percent.",
                        )
                    }
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_FortsattMedlemsskapResultat) <> "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_ForutgaendeMedlemskapResultat) <> "ikke_oppfylt") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_fortsattmedlemsskapresultat()
                    .notEqualTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskapresultat()
                    .notEqualTo("ikke_oppfylt"))
            ) {

                //IF((PE_Vedtaksdata_Kravhode_KravGjelder = "sok_okn_ug"  OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Kravlinje_Kravlinjetype) = "ut")  AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_i_2"  OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_i_1")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_okn_ug") or pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype()
                        .equalTo("ut")) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo(
                        "stdbegr_12_7_2_i_2"
                    ) or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo(
                        "stdbegr_12_7_2_i_1"
                    ))
                ) {
                    //[TBU2434NN, TBU2434EN, TBU2434]

                    paragraph {
                        text(
                            Bokmal to "Du mottok ikke arbeidsavklaringspenger da du søkte om uføretrygd. Inntektsevnen din må derfor være varig nedsatt med minst halvparten. Vi har kommet fram til at inntektsevnen din ikke er tilstrekkelig varig nedsatt.",
                            Nynorsk to "Du fekk ikkje arbeidsavklaringspengar då du søkte om uføretrygd. Inntektsevna di må derfor vere varig sett ned med minst halvparten. Vi har kome fram til at inntektsevna di ikkje er tilstrekkeleg varig sett ned.",
                            English to "You were not receiving a work assessment allowance at the time you submitted your application, and as a consequence, your earning ability must be permanently reduced by no less than half. We have concluded that your earning ability has not been sufficiently reduced.",
                        )
                    }
                }

                //IF(  (PE_Vedtaksdata_Kravhode_KravGjelder = "sok_okn_ug"   OR  FF_GetArrayElement_String(PE_Vedtaksdata_Kravhode_Kravlinjeliste_Kravlinje_Kravlinjetype) = "ut")   AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_i_2" ) THEN      INCLUDE ENDIF
                showIf(
                    ((pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_okn_ug") or pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype()
                        .equalTo("ut")) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .equalTo("stdbegr_12_7_2_i_2"))
                ) {
                    //[TBU2435NN, TBU2435EN, TBU2435]

                    paragraph {
                        text(
                            Bokmal to "Du mottok arbeidsavklaringspenger da du søkte om uføretrygd, men vi har kommet fram til at inntektsevnen din ikke er varig nedsatt med minst 40 prosent. ",
                            Nynorsk to "Du fekk arbeidsavklaringspengar då du søkte om uføretrygd, men vi har kome fram til at inntektsevna di ikkje er varig sett ned med minst 40 prosent.",
                            English to "You were receiving a work assessment allowance at the time you submitted your application or disability benefit, and we have concluded that your earning ability has not been permanently reduced by at least 40 percent.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_i_1") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse().equalTo("stdbegr_12_7_2_i_1"))) {
                    //[TBU2436NN, TBU2436EN, TBU2436]

                    paragraph {
                        text(
                            Bokmal to "Inntektsevnen din er ikke varig nedsatt med minst halvparten.",
                            Nynorsk to "Inntektsevna di er ikkje varig sett ned med minst halvparten.",
                            English to "Your earning ability has not been reduced by at least half.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_Kravhode_Kravlinjeliste_Kravlinje_Kravlinjetype) = "ut"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) <> ""  AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu"  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu"  AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_okn_ug"  AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_AlderResultat) = "oppfylt"  AND (FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "ikke_vurdert"  OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype()
                        .equalTo("ut") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .notEqualTo("") and pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_ys") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_uu") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_okn_ug") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_alderresultat().equalTo(
                        "oppfylt"
                    ) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
                        .equalTo("ikke_vurdert") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("")))
                ) {
                    includePhrase(TBU2412_Generated)
                }

                //IF( PE_Vedtaksdata_Kravhode_KravGjelder = "sok_okn_ug"  AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse(1) <> ""  AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys"   AND  (PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "ikke_vurdert"  OR PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "")  ) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravgjelder()
                        .equalTo("sok_okn_ug") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .notEqualTo("") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_ys") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
                        .equalTo("ikke_vurdert") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("")))
                ) {
                    includePhrase(TBU2443_Generated)
                }

                //IF( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) <> ""  AND  FF_GetArrayElement_String(PE_Vedtaksdata_Kravhode_Kravlinjeliste_Kravlinje_Kravlinjetype) = "ut"  AND  PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys"  AND  PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_uu"  AND  PE_Vedtaksdata_Kravhode_KravArsakType <> "endring_ifu"  AND  PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_okn_ug"  AND  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_AlderResultat) = "oppfylt"   AND  ( PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "ikke_vurdert"  OR PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "" )  ) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .notEqualTo("") and pe.vedtaksdata_kravhode_kravlinjeliste_kravlinje_kravlinjetype()
                        .equalTo("ut") and pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_ys") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_uu") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_okn_ug") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_alderresultat().equalTo(
                        "oppfylt"
                    ) and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
                        .equalTo("ikke_vurdert") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("")))
                ) {
                    //[TBU2437NN, TBU2437EN, TBU2437]

                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-7.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-7.",
                            English to "This decision is made pursuant to Section 12-7 of the National Insurance Act.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) <> ""  AND PE_Vedtaksdata_Kravhode_KravGjelder = "sok_okn_ug"  AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys" AND (PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "ikke_vurdert"  OR PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) = "")) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .notEqualTo("") and pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_okn_ug") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_ys") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
                        .equalTo("ikke_vurdert") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().equalTo("")))
                ) {
                    //[TBU2438NN, TBU2438EN, TBU2438]

                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-7 og 12-10.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-7 og 12-10.",
                            English to "This decision is made pursuant to Sections 12-7 and 12-10 of the National Insurance Act.",
                        )
                    }
                }

                //PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys"
                showIf(pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys")) {
                    includePhrase(TBU2427_Generated)
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneBegrunnelse) = "stdbegr_12_7_2_i_3" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "oppfylt" AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < 30 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) < 30 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse(1) = "stdbegr_12_17_1_o_1") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevnebegrunnelse()
                        .equalTo("stdbegr_12_7_2_i_3") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
                        .equalTo("oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()
                        .lessThan(30) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()
                        .greaterThan(0) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforegrad()
                        .lessThan(30) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse().equalTo("stdbegr_12_17_1_o_1"))
                ) {
                    //[TBU2439NN, TBU2439EN, TBU2439]

                    paragraph {
                        text(
                            Bokmal to "Du har en godkjent yrkesskade eller yrkessykdom, som er årsak til den nedsatte inntektsevnen din. Inntektsevnen din er imidlertid ikke tilstrekkelig varig nedsatt.",
                            Nynorsk to "Du har ein godkjend yrkesskade eller yrkessjukdom som er årsak til den nedsette inntektsevna di. Inntektsevna di er likevel ikkje tilstrekkeleg varig sett ned.",
                            English to "You have a certified occupational injury or occupational illness, which is the cause behind your reduced earning ability. However, the permanent reduction in your earning ability is not sufficient.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_i_2") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse().equalTo("stdbegr_12_17_1_i_2"))) {
                    //[TBU2440NN, TBU2440EN, TBU2440]

                    paragraph {
                        text(
                            Bokmal to "Du har en godkjent yrkesskade eller yrkessykdom. Vi har vurdert at den nedsatte inntektsevnen din ikke skyldes den godkjente yrkesskaden eller yrkessykdommen din. Det er andre sykdomsforhold som er årsaken til den nedsatte inntektsevnen din.",
                            Nynorsk to "Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har vurdert det slik at den nedsette inntektsevna di ikkje kjem av den godkjende yrkesskaden eller yrkessjukdommen din. Det er andre sjukdomsforhold som er årsaka til den nedsette inntektsevna di.",
                            English to "You have a certified occupational injury or occupational illness. We have concluded that your reduction in earning ability was not caused by the certified occupational injury or occupational illness. Other illnesses are the cause behind your reduced earning ability.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat) = "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_2" AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < 30 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUforePeriode_Uforetrygdberegning_Uforegrad < 30 AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()
                        .equalTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()
                        .equalTo("stdbegr_12_17_1_o_2") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().lessThan(
                        30
                    ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()
                        .greaterThan(0) and pe.vedtaksdata_beregningsdata_beregninguforeperiode_uforetrygdberegning_uforegrad()
                        .lessThan(30) and pe.vedtaksdata_kravhode_kravgjelder().notEqualTo("sok_ys"))
                ) {
                    //[TBU2441NN, TBU2441EN, TBU2441]

                    paragraph {
                        textExpr(
                            Bokmal to "Du har en godkjent yrkesskade eller yrkessykdom. Vi har vurdert at ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()
                                .format() + " prosent av den nedsatte inntektsevnen din skyldes den godkjente yrkesskaden eller yrkessykdommen din. I tillegg er det andre sykdomsforhold som er årsaken til den nedsatte inntektsevnen din.",
                            Nynorsk to "Du har ein godkjend yrkesskade eller yrkessjukdom. Vi har vurdert det slik at ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()
                                .format() + " prosent av den nedsette inntektsevna di kjem av den godkjende yrkesskaden eller yrkessjukdommen din. I tillegg er det andre sjukdomsforhold som er årsaka til den nedsette inntektsevna di.",
                            English to "You have a certified occupational injury or occupational illness. We have concluded that ".expr() + pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()
                                .format() + " percent of your reduction in earning ability was caused by the certified occupational injury or occupational illness. In addition, other illnesses contribute to your reduced earning ability.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat) = "ikke_oppfylt" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_o_2" AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) < 30 AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_YrkesskadeGrad) > 0) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()
                        .equalTo("ikke_oppfylt") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_ys") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse().equalTo(
                        "stdbegr_12_17_1_o_2"
                    ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad()
                        .lessThan(30) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_yrkesskadegrad().greaterThan(0))
                ) {
                    //[TBU2442NN, TBU2442EN, TBU2442]

                    paragraph {
                        text(
                            Bokmal to "Inntektsevnen din er imidlertid ikke tilstrekkelig varig nedsatt med minst <FRITEKST: prosentsats 30/40/50> prosent.",
                            Nynorsk to "Inntektsevna di er likevel ikkje tilstrekkeleg varig sett ned med minst <FRITEKST: prosentsats 30/40/50> prosent.",
                            English to "However, your earning ability is not permanently reduced by at least <FRITEKST: prosentsats 30/40/50> percent.",
                        )
                    }
                }
            }

            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_FortsattMedlemsskapResultat) <> "ikke_oppfylt" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_ForutgaendeMedlemskapResultat) <> "ikke_oppfylt") THEN      INCLUDE ENDIF
            showIf(
                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_fortsattmedlemsskapresultat()
                    .notEqualTo("ikke_oppfylt") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_forutgaendemedlemskapresultat()
                    .notEqualTo("ikke_oppfylt"))
            ) {

                //IF(PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeBegrunnelse) = "stdbegr_12_17_1_i_1") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskadebegrunnelse()
                        .equalTo(
                            "stdbegr_12_17_1_i_1"
                        ))
                ) {
                    //[TBU2444NN, TBU2444EN, TBU2444]

                    paragraph {
                        text(
                            Bokmal to "For å ha rett til uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom, må uførheten din skyldes en godkjent yrkesskade eller yrkessykdom.",
                            Nynorsk to "For å ha rett til uføretrygd etter særreglar for yrkesskade eller yrkessjukdom må uførleiken din kome av ein godkjend yrkesskade eller yrkessjukdom.",
                            English to "In order to be entitled to disability benefit in accordance with special rules for occupational injury or occupational illness, the cause behind your disability must be a certified occupational injury or occupational illness.",
                        )
                    }
                    //[TBU2445NN, TBU2445EN, TBU2445]

                    paragraph {
                        text(
                            Bokmal to "Du har ikke en godkjent yrkesskade eller yrkessykdom.",
                            Nynorsk to "Du har ikkje ein godkjend yrkesskade eller yrkessjukdom.",
                            English to "You do not have a certified occupational injury or occupational illness.",
                        )
                    }
                }

                //IF(  FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat) = "ikke_oppfylt" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys"  AND (PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "ikke_vurdert" AND  PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "")  ) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()
                        .equalTo("ikke_oppfylt") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_ys") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
                        .notEqualTo("ikke_vurdert") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().notEqualTo("")))
                ) {
                    includePhrase(TBU2412_Generated)
                }

                //IF( FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_NedsattInntektsevneResultat) = "ikke_oppfylt" AND PE_Vedtaksdata_Kravhode_KravGjelder <> "sok_ys"  AND (PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "ikke_vurdert" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "")  ) THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_nedsattinntektsevneresultat()
                        .equalTo("ikke_oppfylt") and pe.vedtaksdata_kravhode_kravgjelder()
                        .notEqualTo("sok_ys") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
                        .notEqualTo("ikke_vurdert") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().notEqualTo("")))
                ) {
                    //[TBU2446NN, TBU2446EN, TBU2446]

                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-7 og 12-17.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-7 og 12-17.",
                            English to "This decision is made pursuant to Sections 12-7 and 12-17 of the National Insurance Act.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "ikke_oppfylt") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
                        .equalTo(
                            "ikke_oppfylt"
                        ))
                ) {
                    //[TBU3704NN, TBU3704EN, TBU3704]

                    paragraph {
                        text(
                            Bokmal to "Du oppfyller ikke vilkårene, og vi avslår derfor søknaden din.",
                            Nynorsk to "Du oppfyller ikkje vilkåra, og vi avslår derfor søknaden din.",
                            English to "You do not meet the requirements, and your application is thus denied.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_Kravhode_KravGjelder = "sok_ys" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat) = "ikke_oppfylt") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_ys") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat()
                        .equalTo(
                            "ikke_oppfylt"
                        ))
                ) {
                    //[TBU3705NN, TBU3705EN, TBU3705]

                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-17.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-17.",
                            English to "This decision is made pursuant to Sections 12-7 of the National Insurance Act.",
                        )
                    }
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforBegrunnelse) = "stdbegr_12_13_1_i_1") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforbegrunnelse().equalTo("stdbegr_12_13_1_i_1"))) {
                    includePhrase(TBU1151_Generated)
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforBegrunnelse) = "stdbegr_12_13_1_i_2") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforbegrunnelse().equalTo("stdbegr_12_13_1_i_2"))) {
                    includePhrase(TBU1152_Generated)
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforBegrunnelse) = "stdbegr_12_13_1_i_2") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforbegrunnelse().equalTo("stdbegr_12_13_1_i_2"))) {
                    includePhrase(TBU1153_Generated)
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforBegrunnelse) = "stdbegr_12_13_1_i_3") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforbegrunnelse().equalTo("stdbegr_12_13_1_i_3"))) {
                    includePhrase(TBU1154_Generated)
                }

                //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforBegrunnelse) = "stdbegr_12_13_1_i_3") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforbegrunnelse().equalTo("stdbegr_12_13_1_i_3"))) {
                    includePhrase(TBU1155_Generated)
                }

                //IF(PE_Vedtaksdata_Kravhode_KravGjelder = "sok_uu" AND FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforBegrunnelse) <> "" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat(1) = "ikke_oppfylt") THEN      INCLUDE ENDIF
                showIf(
                    (pe.vedtaksdata_kravhode_kravgjelder().equalTo("sok_uu") and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforbegrunnelse()
                        .notEqualTo(
                            ""
                        ) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat().equalTo("ikke_oppfylt"))
                ) {
                    //[TBU2447NN, TBU2447EN, TBU2447]

                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-13 tredje ledd.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-13 tredje ledd.",
                            English to "This decision is made pursuant to Section 12-13, Subsection 3 of the National Insurance Act.",
                        )
                    }
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")) {
                    //[TBU2448NN, TBU2448EN, TBU2448]

                    paragraph {
                        text(
                            Bokmal to "For å ha rett til å endre den fastsatte inntekten din før du ble ufør, må du ha hatt en varig inntektsøkning uten at stillingsandelen din har økt. Inntekt før uførhet kan bare endres dersom du mottar gradert uføretrygd.",
                            Nynorsk to "For å ha rett til å endre den fastsette inntekta di før du blei ufør, må du ha hatt ei varig inntektsauke utan at stillingsdelen din har auka. Inntekta di før du blei ufør kan berre endrast dersom du får gradert uføretrygd.",
                            English to "In order for you to have the right to amend your determined income prior to your disability, you must have had a permanent increase in income without an increase in your percentage of full-time equivalent. Income prior to disability may only be amended if you are receiving a graduated disability benefit.",
                        )
                    }
                }

                //PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu"
                showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")) {

                    //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_10" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_11") THEN      INCLUDE ENDIF
                    showIf(
                        (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
                            .equalTo("stdbegr_12_8_2_10") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
                            .equalTo("stdbegr_12_8_2_11"))
                    ) {
                        //[TBU2449NN, TBU2449EN, TBU2449]

                        paragraph {

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_10") THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().equalTo(
                                    "stdbegr_12_8_2_10"
                                ))
                            ) {
                                text(
                                    Bokmal to "Du har ikke hatt en varig inntektsøkning i din stilling på <FRITEKST: prosentandel> prosent. ",
                                    Nynorsk to "Du har ikkje hatt ei varig inntektsauke i stillinga di på <FRITEKST: prosentandel> prosent. ",
                                    English to "You have not had a permanent increase in income from your position of <FRITEKST: prosentandel> percent of full-time equivalent. ",
                                )
                            }

                            //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_11") THEN      INCLUDE ENDIF
                            showIf(
                                (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().equalTo(
                                    "stdbegr_12_8_2_11"
                                ))
                            ) {
                                text(
                                    Bokmal to "Du har hatt en varig inntektsøkning, men det er fordi du har økt stillingsandelen din.",
                                    Nynorsk to "Du har hatt ei varig inntektsauke, men det er fordi du har auka stillingsdelen din.",
                                    English to "You have had a permanent increase in income, but this increase was brought on by you increasing your percentage of full-time equivalent.",
                                )
                            }
                        }
                    }

                    //PE_Vedtaksdata_Kravhode_KravArsakType = "endring_ifu"
                    showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")) {
                        //[TBU2450NN, TBU2450EN, TBU2450]

                        paragraph {
                            text(
                                Bokmal to "Du oppfyller ikke vilkåret, og vi avslår derfor søknaden din om endring av inntekten din før uførhet.",
                                Nynorsk to "Du oppfyller ikkje vilkåret, og vi avslår derfor søknaden din om endring av inntekta di før du blei ufør.",
                                English to "You do not meet the requirement, and your application for a change in your income level before disability is thus denied.",
                            )
                        }
                    }
                    //[TBU2451NN, TBU2451EN, TBU2451]

                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-9 og forskrift om uføretrygd fra folketrygden § 2-2.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-9 og uføreforskrifta § 2-2.",
                            English to "This decision is made pursuant to Sections 12-9 of the National Insurance Act, as well as the Regulations pertaining to disability benefit pursuant to Section 2-2 of the National Insurance Act.",
                        )
                    }
                }
            }

            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}

