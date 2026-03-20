package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseGrunnpensjonUtlandDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseGrunnpensjonUtlandDtoSelectors.PesysDataSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseGrunnpensjonUtlandDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.legacy.pegruppe2.*
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.greaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object InnvilgelseGrunnpensjonUtland : RedigerbarTemplate<InnvilgelseGrunnpensjonUtlandDto> {

    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_GP_INNVILGELSE_UTLAND

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uttaksgrad",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
        letterDataType = InnvilgelseGrunnpensjonUtlandDto::class
    ) {
        val pe = pesysData.pe
        title {
            // TODO
            text(
                bokmal { +"test" },
                english { +"test" },
            )
        }
        outline {
            //[PE_GP_04_024_overskrift]

            paragraph {
                text(
                    bokmal { +"Gjenlevendepensjon - melding om endelig vedtak" },
                    english { +"Survivor's pension - notification of a final decision" },
                )
            }
            //[PE_GP_04_024_tabell1]

            paragraph {
                text(
                    bokmal { +"Nav viser til din søknad om gjenlevendepensjon mottatt " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". Dette er et endelig vedtak. Det inneholder endringer i forhold til det foreløpige vedtaket du tidligere har mottatt. Du får " + pe.vedtaksdata_beregningsdata_beregning_netto().format(denominator = false) + " kroner hver måned før skatt fra " + pe.vedtaksdata_virkningfom().format() + "." },
                    english { +"Nav makes reference to your application for a survivor's pension, received on " + pe.vedtaksdata_kravhode_kravmottatdato().format() + ". This is a final decision. It entails changes compared with the preliminary decision you have previously received. You will receive " + pe.vedtaksdata_beregningsdata_beregning_netto().format(denominator = false) + " Norwegian kroner each month, before tax, as of " + pe.vedtaksdata_virkningfom().format() + "." },
                )
            }
            //[Table]

            paragraph {
                text(
                    bokmal { +"<FRITEKST: VELG ETT AV ALTERNATIVENE FOR GENERELLE VILKÅR UNDER, ELLER FYLL INN EGEN TEKST SELV DERSOM FORSLAGENE IKKE ER DEKKENDE. OVERSKRIFT OG ALLE ANDRE ALTERNATIVER FJERNES. " },
                    english { +"<FRITEKST: VELG ETT AV ALTERNATIVE FOR GENERELLE VILKÅR UNDER, ELLER FYLL INN EGEN TEKST SELV DERSOM FORSLAGENE IKKE ER DEKKENDE. OVERSKRIFT OG ALLE ANDRE ALTERNATIVER FJERNES. " },
                )
            }
            //[Table]

            paragraph {
                text(
                    bokmal { +"ALT. 1 EKTESKAP (§ 17-5 BOKSTAV A))Pensjon kan gis til gjenlevende ektefelle dersom ekteskapet varte i minst fem år. Avdøde må også ha vært medlem i folketrygden, eller mottatt pensjon eller uføretrygd fra folketrygden de siste <FRITEKST: tre/fem> årene før dødsfallet. " },
                    english { +"ALT. 1 EKTESKAP (§ 17-5 BOKSTAV A))A pension may be granted to a surviving spouse if the marriage lasted for a minimum of five years. The deceased must also have been a member of the National Insurance Scheme or have received a pension from the National Insurance Scheme in the <FRITEKST: three/five> years prior to death. " },
                )
            }
            //[Table]

            paragraph {
                text(
                    bokmal { +"ALT. 2 GIFT, FELLES BARN (§ 17-5 BOKSTAV B))Pensjon kan gis til gjenlevende ektefelle dersom den gjenlevende hadde felles barn med avdøde. Avdøde må også ha vært medlem i folketrygden, eller mottatt pensjon eller uføretrygd fra folketrygden de siste <FRITEKST: tre/fem> årene før dødsfallet. " },
                    english { +"ALT. 2 GIFT, FELLES BARN (§ 17-5 BOKSTAV B))A pension may be granted to a surviving spouse if the surviving spouse has children with the deceased. The deceased must also have been a member of the National Insurance Scheme or have received a pension from the National Insurance Scheme in the <FRITEKST: three/five> years prior to death. " },
                )
            }
            //[Table]

            paragraph {
                text(
                    bokmal { +"ALT. 3 SAMBOER, FELLES BARN (§ 17-5 BOKSTAV B))Pensjon kan gis til gjenlevende samboer dersom den gjenlevende hadde felles barn med avdøde. Avdøde må de siste <FRITEKST: tre/fem> årene før dødsfallet ha vært medlem i folketrygden, eller mottatt pensjon eller uføretrygd fra folketrygden. " },
                    english { +"ALT. 3 SAMBOER, FELLES BARN (§ 17-5 BOKSTAV B))A pension may be granted to a surviving cohabitant if the surviving cohabitant has children with the deceased. It is a requirement that the deceased was a member of the National Insurance Scheme or received a pension from the National Insurance Scheme in the <FRITEKST: three/five> years prior to death. " },
                )
            }
            //[Table]

            paragraph {
                text(
                    bokmal { +"ALT. 4 SKILT GJENLEVENDE (§ 17-10 FØRSTE LEDD - EKTESKAPET VART I MINST 15 ÅR OG FELLES BARN)Pensjon til en gjenlevende kan gis dersom den gjenlevende og avdøde var gift i minst 15 år, hadde barn sammen, og skilsmissen ikke er lengre tilbake i tid enn fem år før dødsfallet. Avdøde må også ha vært medlem i folketrygden, eller mottatt pensjon eller uføretrygd fra folketrygden de siste <FRITEKST: tre/fem> årene før dødsfallet. " },
                    english { +"ALT. 4 SKILT GJENLEVENDE (§ 17-10 FØRSTE LEDD - EKTESKAPET VART I MINST 15 ÅR OG FELLES BARN)A pension may be granted to a survivor if the survivor and the deceased were married for at least 15 years, had children together, and the divorce occurred no more than five years prior to death. The deceased must also have been a member of the National Insurance Scheme or have received a pension from the National Insurance Scheme in the <FRITEKST: three/five> years prior to death. " },
                )
            }
            //[Table]

            paragraph {
                text(
                    bokmal { +"ALT. 5 SKILT GJENLEVENDE (§ 17-10 FØRSTE LEDD - EKTESKAPET VART I MINST 25 ÅR)Pensjon kan gis til en gjenlevende dersom den gjenlevende og avdøde var gift i minst 25 år, og skilsmissen ikke er lenger tilbake enn fem år før dødsfallet. Avdøde må også ha vært medlem i folketrygden, eller mottatt pensjon eller uføretrygd fra folketrygden de siste <FRITEKST: tre/fem> årene før dødsfallet. " },
                    english { +"ALT. 5 SKILT GJENLEVENDE (§ 17-10 FØRSTE LEDD - EKTESKAPET VART I MINST 25 ÅR)A pension may be granted to a survivor if the survivor and the deceased were married for at least 25 years and the divorce occurred no more than five years prior to death. The deceased must also have been a member of the National Insurance Scheme or have received a pension from the National Insurance Scheme in the <FRITEKST: three/five> years prior to death. " },
                )
            }
            //[Table]

            paragraph {
                text(
                    bokmal { +"ALT. 6 DØDSFALLET SKYLDES YRKESSKADE (§ 17-12)<FRITEKST: Beskriv hvordan yrkesskade har vært avgjørende som unntaksbestemmelse for inngangsvilkår:a)	unntak fra forutgående medlemskapb)	unntak fra ekteskapets varighet eller omsorg for barnc)	unntak fra vilkår om ekteskap/partnerskap>" },
                    english { +"ALT. 6 DØDSFALLET SKYLDES YRKESSKADE (§ 17-12)<FRITEKST: Beskriv hvordan yrkesskade har vært avgjørende som unntaksbestemmelse for inngangsvilkår:a)	unntak fra forutgående medlemskapb)	unntak fra ekteskapets varighet eller omsorg for barnc)	unntak fra vilkår om ekteskap/partnerskap>" },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2 = "folketrygd" OR PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_MinstTreArsFMNorge = true OR PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_MinstEttArFMNorge = true OR PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_UnntakFraForutgaendeMedlemskap = true OR PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_OppfyltEtterGamleRegler = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().equalTo("folketrygd") or pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minsttrearsfmnorge() or pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minstettarfmnorge() or pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_unntakfraforutgaendemedlemskap() or pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_oppfyltettergamleregler())) {

                //IF(PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportforbud = true AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportunntak = "flykt_gjenlev") THEN      INCLUDE ENDIF
                showIf((pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportforbud() and pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportunntak().equalTo("flykt_gjenlev"))) {
                    //[Table, Table 2]

                    paragraph {
                        text(
                            bokmal { +"Det gjøres unntak fra det siste vilkåret dersom avdøde var flyktning." },
                            english { +"An exemption is made to the last requirement if the deceased was a refugee." },
                        )
                    }
                }
                //[Table, Table 2]

                paragraph {
                    text(
                        bokmal { +" Du får pensjon etter unntaksbestemmelsen fordi " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                        english { +" You have been granted a pension in accordance with this exemption because " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningsRelasjoner_AvdodMellomnavn <> "") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn().notEqualTo(""))) {
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                            english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                        )
                    }
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " hadde status som flyktning. Dersom du flytter til utlandet vil retten til gjenlevendepensjon falle bort." },
                        english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " had refugee status. If you move abroad you will no longer have the right to a survivor's pension." },
                    )
                }

                //IF(PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportforbud = true AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportunntak = "dod26_gjenlev") THEN      INCLUDE ENDIF
                showIf((pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportforbud() and pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportunntak().equalTo("dod26_gjenlev"))) {
                    //[Table, Table 2]

                    paragraph {
                        text(
                            bokmal { +" Det gjøres unntak fra dette vilkåret dersom avdøde var medlem av folketrygden ved dødsfallet og ikke hadde fylt 26 år." },
                            english {
                                +" An exemption to this requirement is made if the deceased was a member of the National Insurance Scheme at the time of death and had not yet turned 26."
                            },
                        )
                    }
                }
                //[Table, Table 2]

                paragraph {
                    text(
                        bokmal { +" Du har rett til pensjon etter unntaksbestemmelsen fordi " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                        english { +" You have the right to a pension in accordance with this exemption because " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningsRelasjoner_AvdodMellomnavn <> "") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn().notEqualTo(""))) {
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                            english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() },
                        )
                    }
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " var medlem av folketrygden og ikke hadde fylt 26 år ved dødsfallet. Dersom du flytter til utlandet vil retten til gjenlevendepensjon falle bort." },
                        english { +" " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " was a member of the National Insurance Scheme and had not turned 26 at the time of death. If you move abroad you will no longer have the right to a survivor's pension." },
                    )
                }

                //IF(PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportforbud = true AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportunntak = "mindre5ar_gjenlev") THEN      INCLUDE ENDIF
                showIf((pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportforbud() and pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportunntak().equalTo("mindre5ar_gjenlev"))) {
                    //[Table, Table 2]

                    paragraph {
                        text(
                            bokmal { +" Det gjøres unntak fra det siste vilkåret dersom avdøde ikke har vært uten medlemskap i folketrygden i over fem år etter fylte 16 år." },
                            english { +" An exemption to the last requirement is made if the deceased had not been without a membership in the National Insurance Scheme for more than five years after turning 16." },
                        )
                    }
                }
                //[Table, Table 2]

                paragraph {
                    text(
                        bokmal { +" Du har rett til pensjon etter unntaksbestemmelsen fordi " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                        english { +" You have the right to a pension in accordance with this exemption because " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningsRelasjoner_AvdodMellomnavn <> "") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn().notEqualTo(""))) {
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                            english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() },
                        )
                    }
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " ikke har vært uten medlemskap i folketrygden i over fem år etter fylte 16 år. Dersom du flytter til utlandet vil retten til gjenlevendepensjon falle bort." },
                        english { +" " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " had not been without a membership in the National Insurance Scheme for more than five years after turning 16. If you move abroad you will no longer have the right to a survivor's pension." },
                    )
                }

                //IF(PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportunntak = "halv_minstep_gjenlev") THEN      INCLUDE ENDIF
                showIf((pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportunntak().equalTo("halv_minstep_gjenlev"))) {
                    //[Table, Table 2]

                    paragraph {
                        text(
                            bokmal { +" Det gjøres unntak fra dette vilkåret dersom den avdøde var medlem i folketrygden ved dødsfallet og hadde tidligere opptjening som tilsvarte minst halvparten av full minsteytelse." },
                            english { +" An exemption to this requirement is made if the deceased was a member of the National Insurance Scheme at the time of death and had an earned pension corresponding to or exceeding half of the minimum pension level." },
                        )
                    }
                }

                //IF(PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportforbud = false AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportunntak = "tpungufor_gjenlev") THEN      INCLUDE ENDIF
                showIf((not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportforbud()) and pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportunntak().equalTo("tpungufor_gjenlev"))) {
                    //[Table, Table 2]

                    paragraph {
                        text(
                            bokmal { +" Du får gjenlevendepensjon etter denne unntaksbestemmelsen fordi " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                            english { +" You have the right to a pension in accordance with this exemption because " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningsRelasjoner_AvdodMellomnavn <> "") THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn().notEqualTo(""))) {
                            text(
                                bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                                english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                            )
                        }
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " hadde tidligere opptjening som tilsvarte minst halvparten av full minsteytelse. Hvis du flytter til et annet land vil retten til pensjon bli vurdert på nytt." },
                            english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " had an earned pension corresponding to or exceeding half of the minimum pension level. If you move abroad your right to a survivor's pension will be reassessed." },
                        )
                    }
                }

                //IF(PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportforbud = false OR PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_Eksportunntak = "tpungufor_gjenlev") THEN      INCLUDE ENDIF
                showIf((not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportforbud()) or pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_eksportunntak().equalTo("tpungufor_gjenlev"))) {
                    //[Table, Table 2]

                    paragraph {
                        text(
                            bokmal { +" Disse vilkårene er oppfylt, og du har rett til pensjon som gjenlevende etter " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                            english { +" These requirements are met and you have the right to a pension as the survivor of " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningsRelasjoner_AvdodMellomnavn <> "") THEN      INCLUDE ENDIF
                        showIf((pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn().notEqualTo(""))) {
                            text(
                                bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                                english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                            )
                        }
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + "." },
                            english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + "." },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_Netto               = 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_netto().equalTo(0))) {
                    //[Table, Table 2]

                    paragraph {
                        text(
                            bokmal { +" Selv om du er innvilget gjenlevendepensjon kommer denne ikke til utbetaling fordi den forventede inntekten din er for høy." },
                            english { +" Even though you have been granted a survivor's pension, the pension will not be paid out because your expected income is too high." },
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_Kravhode_VurdereTrygdeavtale = true  AND     PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdeavtaler_Avtaletype = "eos_nor" AND     (      PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_MinstTreArsFMNorge  = true    OR      PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_MinstEttArFMNorge = true    OR      PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_UnntakFraForutgaendeMedlemskap = true    OR       PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_OppfyltEtterGamleRegler  = true) ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_kravhode_vurderetrygdeavtale() and pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaletype().equalTo("eos_nor") and (pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minsttrearsfmnorge() or pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minstettarfmnorge() or pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_unntakfraforutgaendemedlemskap() or pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_oppfyltettergamleregler()))) {

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2 = "folketrygd" AND PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdeavtaledetaljer_Art10BruktGP = false AND PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_FaTTEOS >= 12) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().equalTo("folketrygd") and not(pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaledetaljer_art10bruktgp()) and pe.grunnlag_persongrunnlagslisteavdod_trygdetid_fatteos().greaterThanOrEqual(12))) {
                    //[Table, Table 4]

                    paragraph {
                        text(
                            bokmal { +"Du har rett til gjenlevendepensjon både etter folketrygdlovens regler og etter reglene i EØS-avtalens bestemmelser om trygd. Pensjonen din er beregnet etter folketrygdlovens regler, fordi det gir en høyere pensjon enn en beregning etter EØS-avtalens bestemmelser. " },
                            english { +"You are entitled to a survivor's pension in accordance with the National Insurance Act and the social security regulations in the EEA agreement. Your pension has been calculated in accordance with the regulations in the National Insurance Act because this provides a higher pension than a calculation in accordance with the regulations of the EEA agreement. " },
                        )
                    }
                }
                //[Table, Table 4]

                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 17 og EØS-avtalens bestemmelser om trygd." },
                        english { +"This decision has been made in accordance with the regulations in Chapter 17 of the National Insurance Act and the regulations on social security in the EEA agreement. " },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2 = "eos" AND PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdeavtaledetaljer_Art10BruktGP = false AND PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_FaTTEOS >= 12) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().equalTo("eos") and not(pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaledetaljer_art10bruktgp()) and pe.grunnlag_persongrunnlagslisteavdod_trygdetid_fatteos().greaterThanOrEqual(12))) {
                    //[Table, Table 4]

                    paragraph {
                        text(
                            bokmal { +"Du har rett til gjenlevendepensjon både etter folketrygdlovens regler og etter reglene i EØS-avtalens bestemmelser om trygd. Pensjonen din er beregnet etter EØS-avtalens regler, fordi det gir en høyere pensjon enn en beregning etter folketrygdlovens regler. " },
                            english { +"You are entitled to a survivor's pension in accordance with the National Insurance Act and the social security regulations in the EEA agreement. Your pension has been calculated in accordance with the social security regulations in the EEA agreement because this provides a higher pension than a calculation in accordance with the regulations of the National Insurance Act. " },
                        )
                    }
                }
                //[Table, Table 4]

                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 17 og EØS-avtalens bestemmelser om trygd." },
                        english { +"This decision has been made pursuant to the regulations in Chapter 17 of the National Insurance Act and the regulations on social security in the EEA agreement. " },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2 = "nordisk" AND PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdeavtaledetaljer_Art10BruktGP = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().equalTo("nordisk") and pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaledetaljer_art10bruktgp())) {
                    //[Table, Table 4]

                    paragraph {
                        text(
                            bokmal { +"Du har rett til gjenlevendepensjon både etter folketrygdlovens regler, nordisk konvensjon om trygd og reglene i EØS-avtalens bestemmelser om trygd. Pensjonen din er beregnet etter folketrygdlovens regler og nordisk konvensjon om trygd, fordi det gir en høyere pensjon enn en beregning etter EØS-avtalens bestemmelser. " },
                            english { +"You are entitled to a survivor's pension in accordance with the National Insurance Act, the Nordic Convention of Social Security and the social security regulations in the EEA agreement. Your pension has been calculated in accordance with the regulations in the National Insurance Act and the Nordic Convention on Social Security, because this provides a higher pension than a calculation in accordance with the regulations of the EEA agreement. " },
                        )
                    }
                }
                //[Table, Table 4]

                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 17, nordisk konvensjon om trygd og EØS-avtalens bestemmelser om trygd." },
                        english { +"This decision has been made pursuant to the regulations in Chapter 17 of the National Insurance Act, the Nordic Convention on Social Security and the regulations on social security in the EEA agreement. " },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2 = "eos" AND PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdeavtaledetaljer_Art10BruktGP = true AND PE_Grunnlag_PersongrunnlagslisteAvdod_Trygdetid_FaTTEOS >= 12) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().equalTo("eos") and pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaledetaljer_art10bruktgp() and pe.grunnlag_persongrunnlagslisteavdod_trygdetid_fatteos().greaterThanOrEqual(12))) {
                    //[Table, Table 4]

                    paragraph {
                        text(
                            bokmal { +"Du har rett til gjenlevendepensjon både etter folketrygdlovens regler, nordisk konvensjon om trygd og reglene i EØS-avtalens bestemmelser om trygd. Pensjonen din er beregnet etter EØS-avtalens bestemmelser, fordi det gir en høyere pensjon enn en beregning etter folketrygdlovens regler og nordisk konvensjon om trygd. " },
                            english { +"You are entitled to a survivor's pension in accordance with the National Insurance Act, the Nordic Convention of Social Security and the social security regulations in the EEA agreement. Your pension has been calculated in accordance with the social security regulations in the EEA agreement because this provides a higher pension than a calculation in accordance with the regulations of the National Insurance Act and the Nordic Convention on Social Security. " },
                        )
                    }
                }
                //[Table, Table 4]

                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 17, nordisk konvensjon om trygd og EØS-avtalens bestemmelser om trygd. " },
                        english { +"This decision has been made pursuant to the regulations in Chapter 17 of the National Insurance Act, the Nordic Convention on Social Security and the regulations on social security in the EEA agreement. " },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2  = "eos"  AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_OppfyltVedSammenlegging  = true  AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_MinstTreArsFMNorge = false  AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_MinstEttArFMNorge = false  AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_UnntakFraForutgaendeMedlemskap  = false AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_OppfyltEtterGamleRegler = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().equalTo("eos") and pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_oppfyltvedsammenlegging() and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minsttrearsfmnorge()) and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minstettarfmnorge()) and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_unntakfraforutgaendemedlemskap()) and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_oppfyltettergamleregler()))) {
                //[Table, Table 5]

                paragraph {
                    text(
                        bokmal { +"Etter bestemmelsene i EØS-avtalen om trygd kan opptjeningstid i et annet EØS-land legges til norsk opptjeningstid for å oppfylle det siste vilkåret " },
                        english { +"According to the social security regulations in the EEA agreement, pension rights earned in another EEA country may be added to pension rights earned in Norway in order to meet the last requirement. " },
                    )
                }
                //[Table, Table 5]

                paragraph {
                    text(
                        bokmal { +"<FRITEKST: Velg et av alternativene og tilpass. Fjern overskrifter:" },
                        english { +"<FRITEKST: Velg et av alternativene og tilpass. Fjern overskrifter:" },
                    )
                }
                //[Table, Table 5]

                paragraph {
                    text(
                        bokmal { +" Alt 1" },
                        english { +" Alt 1" },
                    )
                }
                //[Table, Table 5]

                paragraph {
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                        english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningsRelasjoner_AvdodMellomnavn <> "") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn().notEqualTo(""))) {
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                            english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                        )
                    }
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " var de siste tre årene før dødsfallet ikke medlem i folketrygden. Du får likevel pensjon etter unntaksbestemmelsene fordi avdøde oppfyller vilkåret om forutgående medlemskap gjennom medregning av perioder med medlemskap i andre EØS-land. Vi har lagt til grunn at avdøde de siste <FRITEKST: tre/fem> årene fram til dødsfallet har følgende medlemsperioder i folketrygden: <FRITEKST: FOM-TOM>." },
                        english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " was not a member of the National Insurance Scheme the <FRITEKST: three/five> years prior to death. You will nevertheless receive a pension in accordance with the regulations governing exemptions, because the deceased met the membership requirement when their periods of national insurance coverage in other EEA countries are included in the calculation. We have based our decision on our finding that during the <FRITEKST: three/five> years prior to death the deceased was a member of the National Insurance Scheme during the following periods: <FRITEKST: FOM-TOM>." },
                    )
                }
                //[Table, Table 5]

                paragraph {
                    text(
                        bokmal { +" Alt 2" },
                        english { +" Alt 2" },
                    )
                }
                //[Table, Table 5]

                paragraph {
                    text(
                        bokmal { +"Du får pensjon som gjenlevende etter " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                        english { +"You will receive a pension as a survivor of " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningsRelasjoner_AvdodMellomnavn <> "") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn().notEqualTo(""))) {
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                            english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                        )
                    }
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " fordi avdøde mottok <FRITEKST: uføretrygd/pensjon> fra folketrygden de siste <FRITEKST: tre/fem> årene før dødsfallet. Avdøde hadde rett til <FRITEKST: uføretrygd/pensjon> etter bestemmelsene i EØS-avtalen om trygd. Gjenlevendepensjonen beregnes derfor etter reglene i denne avtalen." },
                        english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " because the deceased received <FRITEKST: a pension/ a time-limited disability benefit> from the National Insurance Scheme the <FRITEKST: three/five> years prior to death. The deceased was entitled to <FRITEKST: a pension/ a time-limited disability benefit> in accordance with the social security regulations in the EEA agreement. The survivor's pension will therefore be calculated based on the regulations in this agreement." },
                    )
                }
                //[Table, Table 5]

                paragraph {
                    text(
                        bokmal { +" Alt 3" },
                        english { +" Alt 3" },
                    )
                }
                //[Table, Table 5]

                paragraph {
                    text(
                        bokmal { +"Redegjør for inngangsvilkår dersom verken alt 1 eller alt 2 passer. >" },
                        english { +"Redegjør for inngangsvilkår dersom verken alt 1 eller alt 2 passer. >" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_Netto = 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_netto().equalTo(0))) {
                    //[Table, Table 5]

                    paragraph {
                        text(
                            bokmal { +" Selv om du er innvilget gjenlevendepensjon kommer denne ikke til utbetaling fordi den forventede inntekten din er for høy." },
                            english { +" Even though you have been granted a survivor's pension, the pension will not be paid out because your expected income is too high." },
                        )
                    }
                }
                //[Table, Table 5]

                paragraph {
                    text(
                        bokmal { +" Vedtaket er gjort etter bestemmelsene om gjenlevendepensjon i folketrygdloven kapittel 17 og EØS-avtalens bestemmelser om trygd. " },
                        english { +" This decision has been made pursuant to the regulations for a survivor's pension in Chapter 17 of the National Insurance Act and the EEA agreement's regulations on social security." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2   <>  "eos"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2   <>  "nordisk"  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2   <>  "folketrygd"  AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_OppfyltVedSammenlegging = true  AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_MinstTreArsFMNorge  = false  AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_MinstEttArFMNorge  = false  AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_UnntakFraForutgaendeMedlemskap  = false AND PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_OppfyltEtterGamleRegler = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().notEqualTo("eos") and pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().notEqualTo("nordisk") and pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().notEqualTo("folketrygd") and pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_oppfyltvedsammenlegging() and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minsttrearsfmnorge()) and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minstettarfmnorge()) and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_unntakfraforutgaendemedlemskap()) and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_oppfyltettergamleregler()))) {
                //[Table, Table 6]

                paragraph {
                    text(
                        bokmal { +"Etter bestemmelsene i trygdeavtalen mellom Norge og " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + " kan opptjeningstid i " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + " legges til norsk opptjeningstid for å oppfylle det siste vilkåret. " },
                        english { +"According to the rules of the social security agreement between Norway and " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + ", pension rights earned in " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + " may be added to pension rights earned in Norway in order to meet the last requirement. " },
                    )
                }
                //[Table, Table 6]

                paragraph {
                    text(
                        bokmal { +"<FRITEKST: Velg et av alternativene og tilpass. Fjern overskrifter." },
                        english { +"<FRITEKST: Velg et av alternativene og tilpass. Fjern overskrifter." },
                    )
                }
                //[Table, Table 6]

                paragraph {
                    text(
                        bokmal { +" Alt 1" },
                        english { +" Alt 1" },
                    )
                }
                //[Table, Table 6]

                paragraph {
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                        english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningsRelasjoner_AvdodMellomnavn <> "") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn().notEqualTo(""))) {
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                            english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                        )
                    }
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " var de siste <FRITEKST: tre/fem> årene før dødsfallet ikke medlem i folketrygden. Du får likevel pensjon etter unntaksbestemmelsene fordi avdøde oppfyller vilkåret om forutgående medlemskap gjennom medregning av perioder med medlemskap i " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + ". Vi har lagt til grunn at avdøde de siste tre årene fram til dødsfallet har følgende medlemsperioder i folketrygden: <FRITEKST: FOM-TOM>." },
                        english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " was not a member of the National Insurance Scheme the <FRITEKST: three/five> years prior to death. You will nevertheless receive a pension in accordance with the regulations governing exemptions, because the deceased met the membership requirement when their periods of national insurance coverage in " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + " are included in the calculation. We have based our decision on our finding that during the <FRITEKST: three/five> years prior to death the deceased was a member of the National Insurance Scheme during the following periods: <FRITEKST: FOM-TOM>." },
                    )
                }
                //[Table, Table 6]

                paragraph {
                    text(
                        bokmal { +" Alt 2" },
                        english { +" Alt 2" },
                    )
                }
                //[Table, Table 6]

                paragraph {
                    text(
                        bokmal { +"Du får pensjon som gjenlevende etter " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                        english { +"You will receive a pension as a survivor of " + pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodfornavn_initcap() + " " },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningsRelasjoner_AvdodMellomnavn <> "") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn().notEqualTo(""))) {
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                            english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodmellomnavn_initcap() + " " },
                        )
                    }
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " fordi avdøde mottok <FRITEKST: pensjon/tidsbegrenset uførestønad> fra folketrygden de siste <FRITEKST: tre/fem> årene før dødsfallet. Avdøde hadde rett til <FRITEKST: pensjon/tidsbegrenset uførestønad> etter bestemmelsene i trygdeavtalen mellom Norge og " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + ". Gjenlevendepensjonen beregnes derfor etter reglene i denne avtalen." },
                        english { +pe.vedtaksdata_beregningsdata_beregningsrelasjoner_avdodetternavn_initcap() + " because the deceased received <FRITEKST: a pension/ a time-limited disability benefit> from the National Insurance Scheme the <FRITEKST: three/five> years prior to death. The deceased was entitled to <FRITEKST: a pension/a time-limited disability benefit> in accordance with the rules of the social security agreement between Norway and " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + ". The survivor's pension will therefore be calculated based on the regulations in this agreement." },
                    )
                }
                //[Table, Table 6]

                paragraph {
                    text(
                        bokmal { +" Alt 3" },
                        english { +" Alt 3" },
                    )
                }
                //[Table, Table 6]

                paragraph {
                    text(
                        bokmal { +"Redegjør for inngangsvilkår dersom verken alt 1 eller alt 2 passer. >" },
                        english { +"Redegjør for inngangsvilkår dersom verken alt 1 eller alt 2 passer. >" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_Netto = 0) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_netto().equalTo(0))) {
                    //[Table, Table 6]

                    paragraph {
                        text(
                            bokmal { +" Selv om du er innvilget gjenlevendepensjon kommer denne ikke til utbetaling fordi den forventede inntekten din er for høy." },
                            english { +" Even though you have been granted a survivor's pension, the pension will not be paid out because your expected income is too high." },
                        )
                    }
                }
                //[Table, Table 6]

                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene om gjenlevendepensjon i folketrygdloven kapittel 17 og bestemmelsene i trygdeavtalen mellom Norge og " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + "." },
                        english { +"This decision has been made pursuant to the regulations for a survivor's pension in Chapter 17 of the National Insurance Act and the regulations of the social security agreement between Norway and " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + "." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_Brutto <> PE_Vedtaksdata_BeregningsData_Beregning_Netto) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_brutto().notEqualTo(pe.vedtaksdata_beregningsdata_beregning_netto()))) {
                //[PE_GP_tabellA1_utland 2]

                paragraph {
                    text(
                        bokmal { +"Din månedlige gjenlevendepensjon fra " + pe.vedtaksdata_beregningsdata_beregning_virkdatofom().format() + "Folketrygdens grunnbeløp (G) benyttet i beregningen er " + pe.vedtaksdata_beregningsdata_beregning_grunnbelop().format(denominator = false) + " kroner.Framtidig årlig inntekt benyttet i beregningen er " + pe.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi().format() + " kroner." },
                        english { +"Your survivor's pension from " + pe.vedtaksdata_beregningsdata_beregning_virkdatofom().format() + "The national insurance basic amount (G) used in the calculation is " + pe.vedtaksdata_beregningsdata_beregning_grunnbelop().format(denominator = false) + " NOK.Expected future earned income used in the calculation is " + pe.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi().format() + " NOK." },
                    )
                }
                //[PE_GP_tabellA1_utland 2]

                paragraph {
                    text(
                        bokmal { +"Pensjon per måned før fradrag for inntekt" },
                        english { +"Pension per month before reduction due to income" },
                    )
                    text(
                        bokmal { +"Pensjon per måned etter fradrag for inntekt" },
                        english { +"Pension per month after reduction due to income" },
                    )
                }
                //[PE_GP_tabellA1_utland 2]

                paragraph {
                    text(
                        bokmal { +"Grunnpensjon" },
                        english { +"Basic pension" },
                    )
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_grunnpensjon_gpbrutto().format(denominator = false) + " kr" },
                        english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_grunnpensjon_gpbrutto().format(denominator = false) + " NOK" },
                    )
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_grunnpensjon_gpnetto().format(denominator = false) + " kr" },
                        english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_grunnpensjon_gpnetto().format(denominator = false) + " NOK" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_TPinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpinnvilget())) {
                    //[PE_GP_tabellA1_utland 2]

                    paragraph {
                        text(
                            bokmal { +"Tilleggspensjon" },
                            english { +"Supplementary pension" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpbrutto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpbrutto().format(denominator = false) + " NOK" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpnetto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpnetto().format(denominator = false) + " NOK" },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Sertillegg_STinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_sertillegg_stinnvilget())) {
                    //[PE_GP_tabellA1_utland 2]

                    paragraph {
                        text(
                            bokmal { +"Særtillegg" },
                            english { +"Special supplement" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelseomp_sertillegg_stbrutto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelseomp_sertillegg_stbrutto().format(denominator = false) + " NOK" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_sertillegg_stnetto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_sertillegg_stnetto().format(denominator = false) + " NOK" },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_FasteUtgifter_FasteUtgifterInnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifterinnvilget())) {
                    //[PE_GP_tabellA1_utland 2]

                    paragraph {
                        text(
                            bokmal { +"Faste utgifter ved institusjonsopphold" },
                            english { +"Fixed costs when institutionalised" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifterbrutto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifterbrutto().format(denominator = false) + " NOK" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifternetto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifternetto().format(denominator = false) + " NOK" },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_FamileTillegg_FamilieTilleggInnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietillegginnvilget())) {
                    //[PE_GP_tabellA1_utland 2]

                    paragraph {
                        text(
                            bokmal { +"Familietillegg" },
                            english { +"Family supplement" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietilleggbrutto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietilleggbrutto().format(denominator = false) + " NOK" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietilleggnetto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietilleggnetto().format(denominator = false) + " NOK" },
                        )
                    }
                }
                //[PE_GP_tabellA1_utland 2]

                paragraph {
                    text(
                        bokmal { +"Sum pensjon før skatt" },
                        english { +"Total pension before tax" },
                    )
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregning_brutto().format(denominator = false) + " kr" },
                        english { +pe.vedtaksdata_beregningsdata_beregning_brutto().format(denominator = false) + " NOK" },
                    )
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregning_netto().format(denominator = false) + " kr" },
                        english { +pe.vedtaksdata_beregningsdata_beregning_netto().format(denominator = false) + " NOK" },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_Brutto = PE_Vedtaksdata_BeregningsData_Beregning_Netto) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_brutto().equalTo(pe.vedtaksdata_beregningsdata_beregning_netto()))) {
                //[PE_GP_tabellA2_utland]

                paragraph {
                    text(
                        bokmal { +"Din månedlige gjenlevendepensjon fra " + pe.vedtaksdata_beregningsdata_beregning_virkdatofom().format() + "Folketrygdens grunnbeløp (G) benyttet i beregningen er " + pe.vedtaksdata_beregningsdata_beregning_grunnbelop().format(denominator = false) + " kroner.Framtidig årlig inntekt benyttet i beregningen er " + pe.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi().format() + " kroner." },
                        english { +"Your survivor's pension from " + pe.vedtaksdata_beregningsdata_beregning_virkdatofom().format() + "The national insurance basic amount (G) used in the calculation is " + pe.vedtaksdata_beregningsdata_beregning_grunnbelop().format(denominator = false) + " NOK.Expected future earned income used in the calculation is " + pe.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi().format() + " NOK." },
                    )
                }
                //[PE_GP_tabellA2_utland]

                paragraph {
                    text(
                        bokmal { +"Pensjon per måned" },
                        english { +"Pension per month" },
                    )
                }
                //[PE_GP_tabellA2_utland]

                paragraph {
                    text(
                        bokmal { +"Grunnpensjon" },
                        english { +"Basic pension" },
                    )
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_grunnpensjon_gpnetto().format(denominator = false) + " kr" },
                        english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_grunnpensjon_gpnetto().format(denominator = false) + " NOK" },
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_TPinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpinnvilget())) {
                    //[PE_GP_tabellA2_utland]

                    paragraph {
                        text(
                            bokmal { +"Tilleggspensjon" },
                            english { +"Supplementary pension" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpnetto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpnetto().format(denominator = false) + " NOK " },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Sertillegg_STinnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_sertillegg_stinnvilget())) {
                    //[PE_GP_tabellA2_utland]

                    paragraph {
                        text(
                            bokmal { +"Særtillegg" },
                            english { +"Special supplement" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_sertillegg_stnetto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_sertillegg_stnetto().format(denominator = false) + " NOK" },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_FasteUtgifter_FasteUtgifterInnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifterinnvilget())) {
                    //[PE_GP_tabellA2_utland]

                    paragraph {
                        text(
                            bokmal { +"Faste utgifter ved institusjonsopphold" },
                            english { +"Fixed costs when institutionalised" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifternetto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_fasteutgifter_fasteutgifternetto().format(denominator = false) + " NOK" },
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_FamileTillegg_FamilieTilleggInnvilget = true) THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietillegginnvilget())) {
                    //[PE_GP_tabellA2_utland]

                    paragraph {
                        text(
                            bokmal { +"Familietillegg" },
                            english { +"Family supplement" },
                        )
                        text(
                            bokmal { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietilleggnetto().format(denominator = false) + " kr" },
                            english { +pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_familetillegg_familietilleggnetto().format(denominator = false) + " NOK" },
                        )
                    }
                }
                //[PE_GP_tabellA2_utland]

                paragraph {
                    text(
                        bokmal { +"Sum pensjon før skatt" },
                        english { +"Total pension before tax" },
                    )
                    text(
                        bokmal { +pe.vedtaksdata_beregningsdata_beregning_netto().format(denominator = false) + " kr" },
                        english { +pe.vedtaksdata_beregningsdata_beregning_netto().format(denominator = false) + " NOK" },
                    )
                }
            }
            //[PE_GP_04_024_forventet_inntekt]

            paragraph {
                text(
                    bokmal { +"Grunnpensjon fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er " + pe.vedtaksdata_beregningsdata_beregning_grunnbelop().format(denominator = false) + " kr. For at du skal få full grunnpensjon må avdødes trygdetid være minst 40 år. Trygdetiden tilsvarer det antall år den avdøde har vært medlem i folketrygden etter fylte 16 år. Dersom avdøde var under 67 år på tidspunktet for dødsfallet, blir det beregnet framtidig trygdetid. Den regnes vanligvis fram til og med det året avdøde ville ha fylt 66 år. Ved mindre enn 40 års trygdetid blir grunnpensjonen tilsvarende redusert. Oversikt over trygdetiden er gitt i vedlegg til dette vedtaket." },
                    english { +"The basic pension is calculated on the basis of the national insurance basic amount, which currently is " + pe.vedtaksdata_beregningsdata_beregning_grunnbelop().format(denominator = false) + " NOK. In order for you to receive a full basic pension, the deceased's period of national insurance cover must be at least 40 years. The period of national insurance cover is equivalent to the years the deceased had been a member of the National Insurance Scheme since turning 16. If the deceased was under 67 years of age at the time of death, credit is also given for their future period of national insurance cover. This period is usually calculated to include the years up to and including the year the deceased would have turned 66. If the deceased had less than 40 years of national insurance cover, the basic pension is reduced proportionately. An overview of the period of national insurance cover is enclosed with this decision." },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodFlyktning  = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdodflyktning())) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Pensjonen er beregnet etter full trygdetid (40 år) fordi avdøde var flyktning. " },
                        english { +" The pension has been calculated based on a full period of national insurance cover (40 years) because the deceased was a refugee." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodDodsfallSkyldesYrkesskade  = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdoddodsfallskyldesyrkesskade())) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Når dødsfallet skyldes en yrkesskade blir ikke grunnpensjonen avkortet på grunn av manglende trygdetid." },
                        english { +" If the death occurred as the result of a workplace injury, the basic pension is not reduced even if the deceased's period of national insurance cover was less than 40 years." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningsInformasjonKapittel19_Yug  <> false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodDodsfallSkyldesYrkesskade  = false) THEN      INCLUDE ENDIF
            showIf((not(pe.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdoddodsfallskyldesyrkesskade()))) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Når avdøde mottok en pensjon beregnet helt eller delvis etter regler som gjelder for yrkesskade, blir ikke denne delen av grunnpensjonen avkortet på grunn av manglende trygdetid." },
                        english { +" If the deceased received a pension based entirely or in part on the regulations for workplace injuries, this part of the basic pension is not reduced even if the deceased's period of national insurance cover was less than 40 years." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleMottarPensjon  = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleInntektOver2g  = false) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningektefellemottarpensjon() and not(pe.vedtaksdata_beregningsdata_beregning_beregningektefelleinntektover2g()))) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Grunnpensjonen er justert til 90 prosent av beløpet fordi din samboer mottar pensjon eller uføretrygd fra folketrygden." },
                        english { +" The basic pension has been adjusted to 90 per cent of the total amount, because your cohabitant receives a national insurance pension. " },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningEktefelleInntektOver2g  = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningektefelleinntektover2g())) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Grunnpensjonen er justert til 90 prosent av beløpet fordi din samboer har inntekt over to ganger grunnbeløpet." },
                        english { +" The basic pension has been adjusted to 90 per cent of the total amount, because your cohabitant has an income that exceeds twice the national insurance basic amount." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_TPinnvilget  = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpinnvilget())) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Tilleggspensjonen avhenger av antall år med pensjonspoeng avdøde har opptjent og størrelsen på pensjonspoengene. Det gis pensjonspoeng for år med inntekt over folketrygdens grunnbeløp. Det kreves 40 år med pensjonspoeng for å få full tilleggspensjon. På visse vilkår kan det medregnes framtidige poengår fra dødsåret til og med det året avdøde ville ha fylt 66 år. Tilleggspensjonen din utgjør 55 prosent av den tilleggspensjonen avdøde hadde opptjent rett til. Oversikt over poengopptjeningen er gitt i vedlegg til dette vedtaket." },
                        english { +" Your supplementary pension depends on the number of years the deceased earned pension points and on how many pension points were earned. You receive pension points for years in which you have an income greater than the national insurance basic amount. 40 years of pension points are required to receive a full supplementary pension. In some cases credit may be given for future years of earning pension points. The future years of earning pension points usually include the years between death and up to and including the year the deceased would have turned 66. Your supplementary pension is 55 per cent of the supplementary pension the deceased had accumulated. An overview of the accumulated points is enclosed with this decision." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_TPinnvilget  = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodDodsfallSkyldesYrkesskade  = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdoddodsfallskyldesyrkesskade())) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Når dødsfallet skyldes en yrkesskade gjelder det spesielle regler for beregning av tilleggspensjon." },
                        english { +" When the death is caused by a workplace injury, particular regulations for the calculation of supplementary pensions apply." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_TPinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_Yug > 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpinnvilget() and pe.vedtaksdata_beregningsdata_beregning_yug().greaterThan(0))) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Når avdøde mottok en pensjon beregnet helt eller delvis etter regler som gjelder for yrkesskade, gjelder det spesielle regler for beregning av tilleggspensjon." },
                        english { +" If the deceased received a pension calculated in whole or in part in accordance with the regulations for workplace injuries, particular regulations for the calculation of a supplementary pension apply." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Tilleggspensjon_TPinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodUngUforFodtFor1941 = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningsSammendragAvdod_AvdodUngUforFodtEtter1940 = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_tilleggspensjon_tpinnvilget() and pe.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdodunguforfodtfor1941() and pe.vedtaksdata_beregningsdata_beregning_beregningssammendragavdod_avdodunguforfodtetter1940())) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Tilleggspensjonen er beregnet med utgangspunkt i at avdøde tidligere har mottatt pensjon beregnet etter særbestemmelsene for unge uføre. Dette grunnlaget er fortsatt benyttet, ettersom dette gir den høyeste tilleggspensjonen. Vær oppmerksom på at tilleggspensjonen etter særbestemmelsene for unge uføre vil falle bort dersom du velger å flytte fra Norge." },
                        english { +" The supplementary pension has been calculated on the basis that the deceased previously received a pension based on the particular regulations for young disabled people. This basis has still been used, as it gives the highest supplementary pension. Please note that your supplementary pension based on the regulations for young disabled people will be terminated if you choose to move away from Norway." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Sertillegg_STinnvilget = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_sertillegg_stinnvilget())) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Særtillegget er gitt for å sikre et minste pensjonsnivå til de som har liten eller ingen opptjening av tilleggspensjon." },
                        english { +" A special supplement is granted to ensure a minimum pension level for people who are not eligible for a supplementary pension or only qualify for a small supplementary pension." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2  = "eos") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().equalTo("eos"))) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Beregning etter trygdeavtalen med EØS-avtalenPensjonen skal beregnes etter bestemmelsene i EØS-avtalen når vilkårene for rett til pensjon er oppfylt ved sammenlegging av opptjeningstid i flere land, eller når slik beregning gir høyere beløp enn beregning kun etter reglene i norsk folketrygd. Etter EØS-avtalen er pensjonen din først beregnet etter samlet opptjening i Norge og andre EØS-land. Deretter er pensjonen multiplisert med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjening i Norge og andre EØS-land. I vedlegget ”Opplysninger om din beregning” kan du se hvilke tall som er brukt ved beregning av pensjonen din." },
                        english {
                            +" Calculation according to the social security regulations of the EEA agreement The pension is to be calculated in accordance with the regulations of the EEA agreement when the requirements for a right to a pension are met by combining pension earning years in two or more countries, or when this calculation is more advantageous to you than the calculation done only in accordance with the regulations of the Norwegian National Insurance Act. In accordance with the EEA agreement, your pension is first calculated based on the combined pension you have accumulated in Norway and other EEA countries. The pension is then multiplied by a proportion figure that indicates the ratio between the actual length of pension earning in Norway and the combined actual length of pension earning in Norway and other EEA countries. In the enclosed \"Information about your pension calculation\" you can see the figures that have been used to calculate your pension."
                        },
                    )
                }
            }

            //IF(      (PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2 <> "eos"   OR       PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2 <> "nordisk"   OR     PE_Vedtaksdata_BeregningsData_Beregning_BeregningNokkelinfo_BeregningNokkelinfo2_Beregningsmetode2 <> "folketrygd" ) AND     (      PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_OppfyltVedSammenlegging  = true    AND      PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_MinstTreArsFMNorge  = false    AND      PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_MinstEttArFMNorge = false    AND      PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_UnntakFraForutgaendeMedlemskap = false    AND       PE_Grunnlag_PersongrunnlagslisteAvdod_InngangOgEksportGrunnlag_OppfyltEtterGamleRegler  = false) ) THEN      INCLUDE ENDIF
            showIf(((pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().notEqualTo("eos") or pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().notEqualTo("nordisk") or pe.vedtaksdata_beregningsdata_beregning_beregningnokkelinfo_beregningnokkelinfo2_beregningsmetode2().notEqualTo("folketrygd")) and (pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_oppfyltvedsammenlegging() and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minsttrearsfmnorge()) and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_minstettarfmnorge()) and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_unntakfraforutgaendemedlemskap()) and not(pe.grunnlag_persongrunnlagslisteavdod_inngangogeksportgrunnlag_oppfyltettergamleregler())))) {
                //[PE_GP_04_024_forventet_inntekt]

                paragraph {
                    text(
                        bokmal { +" Beregning etter trygdeavtalen med " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + "Pensjonen skal beregnes etter bestemmelsene i trygdeavtalen når vilkårene for rett til pensjon er oppfylt ved sammenlegging av opptjeningstid i " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + ", eller når slik beregning gir høyere beløp enn beregning kun etter reglene i norsk folketrygd. Etter trygdeavtalen er pensjonen din først beregnet etter samlet opptjening i Norge og " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + ". Deretter er pensjonen multiplisert med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjening i Norge og " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + ". I vedlegget ”Opplysninger om din beregning” kan du se hvilke tall som er brukt ved beregning av pensjonen din. " },
                        english {
                            +" Calculation in accordance with the social security agreement with " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + "The pension is calculated in accordance with the regulations of the social security agreement when the pension criteria are met by combining pension earning periods in " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + ", or when such a calculation provides a higher pension than a calculation only in accordance with the regulations of the Norwegian National Insurance Act. In accordance with the social security agreement your pension is first calculated based on your combined pension earned in Norway and " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + ". The pension is then multiplied by a proportion figure that indicates the ratio between the actual length of pension earning in Norway and " + pe.grunnlag_persongrunnlagslisteavdod_trygdeavtaler_avtaleland() + ". In the enclosed \"Information about your pension calculation\" you can see which figures have been used to calculate your pension."
                        },
                    )
                }
            }
            //[PE_GP_04_024_forventet_inntekt]

            paragraph {
                text(
                    bokmal { +" Forventet inntektStørrelsen på pensjonen er avhengig av hvilken arbeidsinntekt du har eller kan forventes å få ved siden av pensjonen. Det er arbeidsinntekt før skatt som legges til grunn. " },
                    english { +" Expected incomeThe size of your pension depends on your earned income level or the income level you are expected to receive in addition to your pension. Your pre-tax earned income level forms the basis for the calculation of your pension. " },
                )
            }
            //[PE_GP_04_024_forventet_inntekt]

            paragraph {
                text(
                    bokmal { +"Med arbeidsinntekt menes personinntekt fra arbeid eller næringsvirksomhet som er pensjonsgivende, eller andre ytelser fra folketrygden som likestilles med arbeidsinntekt. Slike ytelser er dagpenger under arbeidsledighet, sykepenger, pleiepenger, svangerskapspenger, foreldrepenger og arbeidsavklaringspenger. " },
                    english { +"\"Earned income\" means personal income from pensionable work or business activities, or other benefits from the National Insurance Scheme that are considered equivalent to earned income. Such benefits include unemployment benefit (dagpenger), sickness benefit (sykepenger), attendance allowance (pleiepenger), pregnancy allowance (svangerskapspenger), parental benefit (foreldrepenger) and work assessment allowance (arbeidsavklaringspenger). " },
                )
            }
            //[PE_GP_04_024_forventet_inntekt]

            paragraph {
                text(
                    bokmal { +"<FRITEKST: VELG ET ALTERNATIV, ELLER FYLL INN TEKST. GI EVENTUELL NÆRMERE BEGRUNNELSE FOR HVORFOR INNTEKTEN ER HYPOTETISK FASTSATT, ELLER ANDRE VURDERINGER GJORT I FORBINDELSE MED FASTSETTELSEN AV FORVENTET INNTEKT. OVERSKRIFTEN SKAL FJERNES. " },
                    english { +"<FRITEKST: VELG ET ALTERNATIV, ELLER FYLL INN TEKST. GI EVENTUELL NÆRMERE BEGRUNNELSE FOR HVORFOR INNTEKTEN ER HYPOTETISK FASTSATT, ELLER ANDRE VURDERINGER GJORT I FORBINDELSE MED FASTSETTELSEN AV FORVENTET INNTEKT. OVERSKRIFTEN SKAL FJERNES. " },
                )
            }
            //[PE_GP_04_024_forventet_inntekt]

            paragraph {
                text(
                    bokmal { +"ALT. 1. LAGT TIL GRUNN INNTEKT UNDER 1/2 G:Vi har lagt til grunn at du har en inntekt tilsvarende " + pe.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi().format() + " kroner. Pensjonen din er beregnet uten reduksjon for arbeidsinntekt, siden inntekten din er under halvparten av folketrygdens grunnbeløp. Hvis inntekten din overstiger halvparten av grunnbeløpet er det viktig at du varsler Nav om dette. " },
                    english { +"ALT. 1. LAGT TIL GRUNN INNTEKT UNDER 1/2 G:The pension calculation has been adjusted to your reported income being " + pe.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi().format() + " NOK. Your pension has not been reduced due to registered employment/taxable income, because your income is less than half of the national insurance basic amount. It is important that you notify Nav if your income increases to more than half the national insurance basic amount. " },
                )
            }
            //[PE_GP_04_024_forventet_inntekt]

            paragraph {
                text(
                    bokmal { +"ALT. 2. LAGT TIL GRUNN INNTEKT OVER 1/2 G:Pensjonen er redusert etter en forventet inntekt tilsvarende " + pe.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi().format() + " kroner. Hvis inntekten din endrer seg er det viktig at du varsler Nav om dette. " },
                    english { +"ALT. 2. LAGT TIL GRUNN INNTEKT OVER 1/2 G:Your pension has been reduced because you have an expected income of " + pe.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi().format() + " NOK. It is important that you notify Nav if your income changes. " },
                )
            }
            //[PE_GP_04_024_forventet_inntekt]

            paragraph {
                text(
                    bokmal { +"ALT. 3. SOM FØLGE AV INNTEKT, ER PENSJONEN REDUSERT TIL NULL KRONERPensjonen er redusert etter en forventet inntekt tilsvarende " + pe.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi().format() + " kroner. Du får derfor ikke utbetalt noen pensjon per i dag. Det er imidlertid viktig at du melder fra til Nav om endringer av inntekten din, slik at vi kan vurdere om endringen kan medføre at du likevel vil få utbetalt pensjon.> " },
                    english { +"ALT. 3. SOM FØLGE AV INNTEKT, ER PENSJONEN REDUSERT TIL NULL KRONERYour pension has been reduced because you have an expected income of " + pe.vedtaksdata_beregningsdata_beregning_beregningssammendragbruker_brukerfpi().format() + " NOK. You have consequently not been granted a pension. However, it is important that you notify Nav if your income changes, so that we can assess whether the changes mean that you will receive a pension. " },
                )
            }
            //[PE_GP_04_024_forventet_inntekt]

            paragraph {
                text(
                    bokmal { +"FRITEKST: FORSLAG TEKST – BEHOV FOR OPPFØLGINGNav har som en av sine hovedoppgaver å bistå mottakere av trygdeytelser til å komme i arbeid og aktivitet. Har du behov for hjelp til å komme i arbeid, forbedre din kompetanse, eller øke din stillingsprosent, kan du ta kontakt med ditt lokale Nav kontor.>" },
                    english { +"FRITEKST: FORSLAG TEKST – BEHOV FOR OPPFØLGINGOne of the main tasks of Nav is to assist recipients of national insurance benefits in getting work or starting work-related activities. Contact your local Nav office if you need help in finding work, improving your skills or increasing your labour market participation.> " },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_Netto > 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_netto().greaterThan(0))) {
                //[PE_GP_04_024_utbetaling]

                paragraph {
                    text(
                        bokmal { +"Utbetaling og skattPensjonen din blir vanligvis utbetalt den 20. hver måned. Når den 20. er en lørdag eller offentlig fridag blir pensjonen utbetalt senest siste virkedag før den 20. Oversikt over utbetalingsdatoer finner du på vår nettside $NAV_URL." },
                        english { +"Payment and taxYour pension will normally be paid on the 20th of each month. When the 20th is a Saturday or public holiday, your pension will be paid at the latest on the last business day before the 20th. You can find a list of payment dates on $NAV_URL." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_Netto > 0) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_netto().greaterThan(0))) {
                //[PE_GP_04_024_utbetaling]

                paragraph {
                    text(
                        bokmal { +" Skattereglene for pensjonsinntekt er ikke de samme som for arbeidsinntekt. Derfor bør du vurdere å søke om nytt skattekort når du starter uttak av pensjon. Endring av skattekort gjøres enklest på Skatteetatens nettsider www.skatteetaten.no. Har du spørsmål kan du ringe Skatteetaten på telefon 800 80 000. Du trenger ikke levere skattekortet til Nav fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten." },
                        english { +" Tax regulations for pensions are different than for work related income. You should therefore consider applying for a new tax card when you start receiving your pension from Nav. Changing your tax card can easily be done on the Norwegian Tax Authority's internet pages at www.skatteetaten.no. If you have any questions, call the Norwegian Tax Authority on 800 80 000. You are not required to submit your tax card to Nav, because your tax details are sent electronically to Nav from the Norwegian Tax Authority." },
                    )
                }
            }

            //IF(PE_VedtaksData_Etterbetaling = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_etterbetaling())) {
                //[PE_GP_04_024_utbetaling]

                paragraph {
                    text(
                        bokmal { +" Du får etterbetalt pensjon fra " + pe.vedtaksdata_virkningfom().format() + ". Etterbetalingen vil vanligvis bli utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i etterbetalingen for skatt, ytelser du har mottatt fra Nav eller andre, som for eksempel tjenestepensjonsordninger. Hvis skattekontor eller andre ordninger har krav i etterbetalingen kan denne bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen." },
                        english { +" You will receive retroactive pension payment from " + pe.vedtaksdata_virkningfom().format() + ". The transfer of funds will normally be done within seven working days. A deduction for tax may be made from the retroactive payment, benefits you have received from Nav or others, like for example service pension schemes. If the tax assessment office or other organisations have claims on retroactive payments, this may be delayed. Deductions from retroactive payments will be shown on the payment notice." },
                    )
                }
            }

            //IF(PE_VedtaksData_Etterbetaling = true) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_etterbetaling())) {
                //[PE_GP_04_024_utbetaling]

                paragraph {
                    text(
                        bokmal { +" Ved etterbetalinger som gjelder tidligere år, vil Nav trekke 30 prosent som en standardsats. Dersom du krever at skatteetaten forut for utbetalingen reberegner skatten for de tidligere årene, så må du gi beskjed om dette til Nav innen sju dager etter dato for dette brevet" },
                        english { +" For refunds for previous years, Nav will deduct 30 per cent as a standard rate. If you demand in advance of the payment that the Norwegian Tax Administration recalculate taxes for the previous years, you must notify Nav of this within seven days after the date of this letter." },
                    )
                }
            }
            //[PE_GP_04_024_dine_plikter]

            paragraph {
                text(
                    bokmal { +"Dine rettigheterDu har som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven paragraf 18. " },
                    english { +"Your rightsAs a main rule you have the right to see the case documents, in accordance with paragraph 18 of the Public Administration Act. " },
                )
            }
            //[PE_GP_04_024_dine_plikter]

            paragraph {
                text(
                    bokmal { +"Hvis du ikke er enig i vedtaket kan du klage. Fristen for å klage er seks uker fra du mottar dette brevet.   " },
                    english { +"If you disagree with the decision, you have the right to appeal. The time limit for filing an appeal is six weeks from the date you receive this letter." },
                )
            }

            //IF(FF_GetArrayElement_String(PE_Grunnlag_Persongrunnlagsliste_Trygdeavtaler_Avtaletype, 1) = "eos_nor") THEN      INCLUDE ENDIF
            showIf(pe.grunnlag_persongrunnlagsliste_trygdeavtaler_avtaletype().equalTo("eos_nor")) {
                //[PE_GP_04_024_dine_plikter]

                paragraph {
                    text(
                        bokmal { +" Vedlagt følger blanketten ”Samlet melding om vedtakene” (E 211N). Kopi av blankett E 211 og vedtak om norsk pensjon er sendt utenlandske trygdemyndigheter. Når det gjelder klageadgang overfor utenlandske trygdemyndigheter, viser vi til vedtaksbrev fra det enkelte land. " },
                        english { +" Please find enclosed the form \"Samlet melding om vedtakene” (Joint notification of the decisions, E 211N). A copy of form E 211 and the decision to grant you a Norwegian pension has been sent to the foreign social security authorities. For information on your right to appeal to foreign pension authorities, please see the letter of decision from the authorities in the relevant country." },
                    )
                }
            }
            //[PE_GP_04_024_dine_plikter]

            paragraph {
                text(
                    bokmal { +" Dine plikterVi gjør oppmerksom på at du har plikt til å melde fra til NAV om endringer som har betydning for størrelsen på pensjonen din, eller for retten til pensjon. Du må alltid melde fra dersom " },
                    english { +" Your obligations We wish to remind you that you have a duty to notify Nav of any changes that will have an impact on the amount of retirement pension you receive, or on your rights to a pension. You must always notify Nav if: " },
                )
            }
            //[PE_GP_04_024_dine_plikter]

            paragraph {
                text(
                    bokmal { +"arbeidsinntekten din endrer seg" },
                    english { +"your earned income changes " },
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_enslig) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("enslig"))) {
                //[PE_GP_04_024_dine_plikter]

                paragraph {
                    text(
                        bokmal { +"du gifter deg, inngår partnerskap eller samboerskap" },
                        english { +"you marry, enter a partnership, or begin cohabiting" },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_SivilstandAnvendt_bormed_3_2) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregning_beregningsivilstandanvendt().equalTo("bormed 3-2"))) {
                //[PE_GP_04_024_dine_plikter]

                paragraph {
                    text(
                        bokmal { +"samboerens inntektsforhold endrer seg. Dette gjelder både endringer i arbeidsinntekt, pensjonsinntekter og kapitalinntektdu gifter deg eller inngår partnerskapdu får barn med samboeren av dere får et varig opphold i institusjondu og samboeren din flytter fra hverandresamboeren din dør" },
                        english { +"there are changes to your cohabitant's income. This regards any changes in your earned income, your pension, or your capital incomeyou marry or enter a partnershipyou have children with your cohabitantyou or your spouse/partner/cohabitant are permanently institutionalizedyou and your cohabitant move apart your cohabitant dies " },
                    )
                }
            }
            //[PE_GP_04_024_dine_plikter]

            paragraph {
                text(
                    bokmal { +"du skal oppholde deg utenfor Norge i en lengre periode eller skal flytte til et annet land " },
                    english { +"you are resident somewhere other than Norway for an extended period of time or you are moving to another country " },
                )
            }
            //[PE_GP_04_024_dine_plikter]

            paragraph {
                text(
                    bokmal { +"Hvis du ikke melder fra om endringer og får utbetalt for mye pensjon, kan pensjon som er utbetalt feil kreves tilbake. " },
                    english { +"If you do not notify us of changes to your situation and you pension payments subsequently are too high, you may have to repay the pension that has been paid to you in error. " },
                )
            }
        }
    }
}


