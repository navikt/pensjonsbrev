package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.foedselsdatoTilBarnTilleggErInnvilgetFor
import no.nav.pensjon.brev.maler.legacy.ut_tbu501v
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class TBU052V_TBU073V_ForDegSomHarRettTilBarnetillegg(
    val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf((pe.ut_tbu501v())){
            title1 {
                text (
                    bokmal { + "For deg som har rett til barnetillegg" },
                    nynorsk { + "For deg som har rett til barnetillegg" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Du har rett til barnetillegg for barn født: " },
                    nynorsk { + "Du har rett til barnetillegg for barn fødd:" },
                )
            }


            //IF( PE_UT_TBU501V() = true AND (PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarKravlinjeKode(SYS_TableRow) = "bt" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVedtakResultat(SYS_TableRow) = "innv")  ) THEN      INCLUDE ENDIF
            showIf(pe.foedselsdatoTilBarnTilleggErInnvilgetFor().isNotEmpty()){
                forEach(pe.foedselsdatoTilBarnTilleggErInnvilgetFor()){
                    paragraph {
                        text (
                            bokmal { + it.format() },
                            nynorsk { + it.format() },
                        )
                    }
                }
            }
            showIf((pe.ut_tbu501v())){
                paragraph {
                    text (
                        bokmal { + "Barnetillegget kan utgjøre opptil 40 prosent av folketrygdens grunnbeløp for hvert barn du forsørger. Du har rett til barnetillegg så lenge du forsørger barn som er under 18 år. Barnetillegget opphører når barnet fyller 18 år. " },
                        nynorsk { + "Barnetillegget kan utgjere opptil 40 prosent av grunnbeløpet i folketrygda for kvart barn du forsørgjer. Du har rett til barnetillegg så lenge du forsørgjer barn som er under 18 år. Barnetillegget opphøyrer når barnet fyller 18 år." },
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "oppfylt") THEN      INCLUDE ENDIF
                    showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40) and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_yrkesskaderesultat().notEqualTo("oppfylt"))){
                        text (
                            bokmal { + "Hvor mye du får i barnetillegg er også avhengig av trygdetiden din. Fordi trygdetiden din er kortere enn 40 år, blir barnetillegget ditt redusert. " },
                            nynorsk { + " Kor mykje du får i barnetillegg, er også avhengig av trygdetida di. Fordi trygdetida di er kortare enn 40 år, blir barnetillegget ditt redusert." },
                        )
                    }
                }
            }
        }
    }
}