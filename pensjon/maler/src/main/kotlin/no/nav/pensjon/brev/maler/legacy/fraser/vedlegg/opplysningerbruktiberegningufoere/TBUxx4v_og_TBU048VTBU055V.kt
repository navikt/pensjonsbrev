package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class TBUxx4v_og_TBU048V_TBU055V(
    val pe: Expression<PEgruppe10>,
): OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        //[TBUxx4 og TBU048V-TBU055V]

        title1 {
            text (
                bokmal { + "Slik fastsetter vi inntekten din før du ble ufør" },
                nynorsk { + "Slik fastset vi inntekta di før du blei ufør" },
            )
        }
        //[TBUxx4 og TBU048V-TBU055V]

        paragraph {
            text (
                bokmal { + "Inntekten før du ble ufør har betydning for hvordan vi fastsetter uføregraden din, og eventuell justering av uføretrygd ut fra inntekt. Inntekten før du ble ufør har ingen sammenheng med hvordan vi beregner uføretrygden din." },
                nynorsk { + "Inntekta di før du blei ufør har betydning for korleis vi fastset uføregraden din, og ei eventuell justering av uføretrygda ut frå inntekt. Inntekta di før du blei ufør har ingen samanheng med korleis vi bereknar uføretrygda di." },
            )
        }
        //[TBUxx4 og TBU048V-TBU055V]

        paragraph {
            text (
                bokmal { + "Når vi fastsetter inntekten din før du ble ufør tar vi utgangspunkt i den normale inntektssituasjonen din før du ble ufør. Dersom du ikke har arbeidet i full stilling, vil inntekten din bli regnet om til en årsinntekt i full stilling. Inntekt før du ble ufør fastsettes forskjellig for arbeidstakere, selvstendig næringsdrivende og personer uten inntekt." },
                nynorsk { + "Når vi fastset inntekta di før du blei ufør tek vi utgangspunkt i den normale inntektssituasjonen din før du blei ufør. Dersom du ikkje har arbeidd i full stilling, vil inntekta di bli rekna om til ein årsinntekt i full stilling. Inntekt før du blei ufør blir fastsett forskjellig for arbeidstakare, sjølvstendig næringsdrivande og personar utan inntekt." },
            )
        }
        //[TBUxx4 og TBU048V-TBU055V]

        paragraph {
            text (
                bokmal { + "For deg som er arbeidstaker" },
                nynorsk { + "For deg som er arbeidstakar" },
            )
        }
        //[TBUxx4 og TBU048V-TBU055V]

        paragraph {
            text (
                bokmal { + "Inntekten din før du ble ufør skal som hovedregel tilsvare de inntektsmulighetene du hadde eller kunne hatt før uføretidspunktet. I de fleste tilfeller vil vi bruke den pensjonsgivende inntekten du hadde året før du ble ufør eller sykepengegrunnlaget ditt." },
                nynorsk { + "Inntekta di før du blei ufør skal som hovudregel tilsvare dei inntektsmoglegheitane du hadde eller kunne hatt før uføretidspunktet. I dei fleste tilfeller vil vi bruke den pensjonsgivande inntekta du hadde året før du blei ufør eller sjukepengegrunnlaget ditt." },
            )
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) THEN      INCLUDE ENDIF
        showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().lessThan(100))){
            //[TBUxx4 og TBU048V-TBU055V]

            paragraph {
                text (
                    bokmal { + "Hvis du er i arbeid kan vi fastsette inntekten din før du ble ufør ut fra din nåværende stillingsandel. Inntekten du har skal regnes om til en årsinntekt i full stilling. Årsinntekten blir deretter justert tilbake til uføretidspunktet ditt og vil tilsvare inntekten din før du ble ufør. Dette gjør vi for at du skal få riktig uføregrad." },
                    nynorsk { + "Dersom du er i arbeid kan vi fastsetje inntekta di før du blei ufør ut frå den nåværande stillingsdelen din. Inntekta skal reknas om til ei årsinntekt i full stilling. Årsinntekta blir deretter justert tilbake til uføretidspunktet ditt og vil tilsvare inntekta di før du blei ufør. Dette gjer vi for at du skal få riktig uføregrad." },
                )
            }
        }
        //[TBUxx4 og TBU048V-TBU055V]

        paragraph {
            text (
                bokmal { + "For deg som er selvstendig næringsdrivende" },
                nynorsk { + "For deg som er sjølvstendig næringsdrivande" },
            )
        }
        //[TBUxx4 og TBU048V-TBU055V]

        paragraph {
            text (
                bokmal { + "Inntekten din før du ble ufør skal tilsvare de inntektsmulighetene du hadde før uføretidspunktet. Vi bruker gjennomsnittet av din pensjonsgivende inntekt for de tre siste årene før du ble ufør. Alle tre årene skal være med, selv om inntektene har variert mye.  Det kan gjøres unntak fra denne regelen, dersom sykdom eller skade har redusert inntektsmulighetene dine over flere år. " },
                nynorsk { + "Inntekta di før du blei ufør skal svare til dei inntektsmoglegheitene du hadde før uføretidspunktet. Vi bruker gjennomsnittet av den pensjonsgivande inntekta di for dei tre siste åra før du blei ufør. Alle tre åra skal vere med, sjølv om inntektene har variert mykje.  Det kan gjerast unntak frå denne regelen, dersom sjukdom eller skade har redusert inntektsmoglegheitene dine over fleire år." },
            )
        }


        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_3" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_4" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_5" OR PE_pebrevkode = "PE_UT_04_500") THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
                .equalTo("stdbegr_12_8_2_3") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
                .equalTo("stdbegr_12_8_2_4") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().equalTo(
                "stdbegr_12_8_2_5"
            ) or pe.pebrevkode().equalTo("PE_UT_04_500"))){
            //[TBUxx4 og TBU048V-TBU055V]

            paragraph {
                text (
                    bokmal { + "Minstenivå på inntekt før uførhet" },
                    nynorsk { + "Minstenivå på inntekt før du blei ufør" },
                )
            }
        }

        //IF(FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_3" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_4" OR FF_GetArrayElement_String(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse) = "stdbegr_12_8_2_5" OR PE_pebrevkode = "PE_UT_04_500") THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
                .equalTo("stdbegr_12_8_2_3") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse()
                .equalTo("stdbegr_12_8_2_4") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().equalTo(
                "stdbegr_12_8_2_5"
            ) or pe.pebrevkode().equalTo("PE_UT_04_500"))){
            //[TBUxx4 og TBU048V-TBU055V]

            paragraph {
                text (
                    bokmal { + "Inntekten din før du ble ufør skal ikke settes lavere enn:" },
                    nynorsk { + "Inntekta di før du blei ufør skal ikkje setjas lågare enn:" },
                )
                list {
                    item {
                        text(
                            bokmal { + "3,3 ganger grunnbeløpet dersom du lever sammen med ektefelle/partner/samboer. Samboerforholdet ditt må ha vart i minst 12 av de siste 18 månedene." },
                            nynorsk { + "3,3 gonger grunnbeløpet dersom du lever saman med ektefelle/partner/sambuar. Sambuarforholdet ditt må ha vart i minst 12 av dei siste 18 månadene." },
                        )
                    }
                    item {
                        text(
                            bokmal { + "3,5 ganger grunnbeløpet dersom du er enslig." },
                            nynorsk { + "3,5 gonger grunnbeløpet dersom du er einsleg." },
                        )
                    }
                    item {
                        text(
                            bokmal { + "4,5 ganger grunnbeløpet dersom du har rett til uføretrygd med rettighet som ung ufør." },
                            nynorsk { + "4,5 gonger grunnbeløpet dersom du har rett til uføretrygd med rett som ung ufør." },
                        )
                    }
                }
            }
        }


        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0) THEN      INCLUDE ENDIF
        showIf(
            (pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
                .lessThan(100) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad().greaterThan(0))){
            //[TBUxx4 og TBU048V-TBU055V]

            paragraph {
                text (
                    bokmal { + "Endring av inntekt før du ble ufør" },
                    nynorsk { + "Endring av inntekt før du blei ufør" },
                )
            }

            paragraph {
                text (
                    bokmal { + "For å ha rett til å endre den fastsatte inntekten din før du ble ufør, må du ha hatt en varig inntektsøkning uten at stillingsandelen din har økt. Inntekt før uførhet kan bare endres dersom du mottar gradert uføretrygd." },
                    nynorsk { + "For å ha rett til å endre den fastsette inntekta di før du blei ufør, må du ha hatt ein varig inntektsaukning utan at stillingsdelen din har blitt auka. Inntekta før du blei ufør kan bare endras dersom du får gradert uføretrygd." },
                )
            }
        }

        //IF(PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109") THEN      INCLUDE ENDIF
        showIf(pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109")){
            //[TBUxx4 og TBU048V-TBU055V]

            title1 {
                text (
                    bokmal { + "Slik fastsetter vi inntekten din etter at du ble ufør" },
                    nynorsk { + "Slik fastset vi inntekta di etter at du blei ufør" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Når vi fastsetter inntekten din etter at du ble ufør, tar vi utgangspunkt i den framtidige pensjonsgivende inntekten din. Er det dokumentert at du har inntektsmuligheter som du ikke benytter, skal også disse tas med ved fastsettelsen av inntekten din etter at du ble ufør." },
                    nynorsk { + "Når vi fastset inntekta di etter at du blei ufør, tek vi utgangspunkt i den framtidige pensjonsgivande inntekta di. Er det dokumentert at du har inntektsmoglegheiter som du ikkje nyttar, skal også desse takast med når inntekta di etter at du blei ufør, skal fastsetjast." },
                )
            }

            paragraph {
                text (
                    bokmal { + "Folketrygdens grunnbeløp endres hvert år, og inntekten din før og etter at du ble ufør blir justert ut fra dette." },
                    nynorsk { + "Grunnbeløpet i folketrygda blir endra kvart år, og inntekta di før og etter at du blei ufør, blir justert ut frå dette." },
                )
            }

            title1 {
                text (
                    bokmal { + "Slik fastsetter vi uføregraden din" },
                    nynorsk { + "Slik fastset vi uføregraden din" },
                )
            }

            paragraph {
                text (
                    bokmal { + "Uføregraden din fastsetter vi ved å sammenligne inntekten din før og etter at du ble ufør. Har du ikke inntektsevne, setter vi uføregraden til 100 prosent. Har du fortsatt inntektsevne, vil uføregraden tilsvare inntektsevnen din som du har tapt. Uføregraden din graderes i trinn på fem prosentpoeng. Vi vurderer alltid om uføregraden skal settes lavere enn 100 prosent." },
                    nynorsk { + "Uføregraden din fastset vi ved å samanlikne inntekta di før og etter at du blei ufør. Dersom du ikkje har inntektsevne, set vi uføregraden til 100 prosent. Har du framleis inntektsevne, tilsvarer uføregraden den inntektsevna di som du har tapt. Uføregraden din blir gradert i trinn på fem prosentpoeng. Vi vurderer alltid om uføregraden skal setjast lågare enn 100 prosent." },
                )
            }
        }
    }
}
