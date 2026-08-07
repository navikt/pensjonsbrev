@file:Suppress("LocalVariableName")

package no.nav.pensjon.brev.maler.legacy.vedlegg


import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.selectors.grunnlag.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.selectors.persongrunnlag.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagbilateral.selectors.trygdetidsgrunnlagListeBilateral.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlageos.selectors.trygdetidsgrunnlagListeEOS.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagnorge.selectors.trygdetidsgrunnlagListeNor.*
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.selectors.vedtaksbrev.*
import no.nav.pensjon.brev.api.model.maler.legacy.personsak.selectors.personSak.*
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.maler.fraser.ufoer.erUforetidspunktMaanedEtterFoedsel
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningUTLegacy =
    createAttachment<LangBokmalNynorsk, PEgruppe10>(
        title = {
            text(
                bokmal { +"Opplysninger om beregningen" },
                nynorsk { +"Opplysningar om utrekninga" },
            )
        },
        includeSakspart = false,
    ) {
        val pe = argument

        val foedselsdato = pe.personsak.foedselsdato
        val erMndEtterFoedsel = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforetidspunkt().safe {
            erUforetidspunktMaanedEtterFoedsel(this, foedselsdato)
        }.ifNull(false)

        title2 {
            text(
                bokmal { + "Opplysninger vi har brukt i beregningen fra " },
                nynorsk { + "Opplysningar vi har brukt i berekninga frå " },
            )
            ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()) { beregningVirkFom ->
                text(
                    bokmal { + beregningVirkFom.format() },
                    nynorsk { + beregningVirkFom.format() },
                )
            }
        }
        paragraph {
            text(
                bokmal { + " Folketrygdens grunnbeløp (G) benyttet i beregningen er " +
                        pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + "." },
                nynorsk { + " Folketrygdas grunnbeløp (G) nytta i berekninga er " +
                        pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + "." },
            )
        }

        includePhrase(TBU010V(pe))
        includePhrase(TBUxx1V(pe))

        // Slik beregner vi uføretrygden din
        showIf (pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()) {
            includePhrase(TBUxx2V(pe))
        }.orShow {
            includePhrase(TBU011V_TBU016V(pe))
        }

        // Minsteytelse
        includePhrase(TBU080V_TBU027V(pe))

        // TODO TBU028V-TBU020V trengs for brev PE_UT_04_300 og PE_UT_14_300

        showIf(
            pe.skalViseTBU034V036V()) {
            includePhrase(TBU034V_036V(pe))
        }

        // Dette er inntektene vi har brukt i beregningen din
        showIf(pe.skalViseInntekterBruktIBeregning()) {
            title1 {
                text(
                    bokmal { +"Dette er inntektene vi har brukt i beregningen din" },
                    nynorsk { +"Dette er inntektene vi har brukt i berekninga di" },
                )
            }
            includePhrase(TBU037V_1(pe))
            includePhrase(TBU037V_2(pe))

            includePhrase(TBU038V_1(pe))
            includePhrase(TBU038V_2(pe))

            includePhrase(TBU037V_3(pe))
            includePhrase(TBU037V_4(pe))

            includePhrase(TBU038V_3(pe))
            includePhrase(TBU038V_4(pe))
        }

        // Dette er trygdetiden din
        showIf(pe.ut_trygdetid()) {
            includePhrase(TBU039V_TBU044V_1(pe, erMndEtterFoedsel))

            showIf(pe.skalViseTrygdetidNorTabell(erMndEtterFoedsel)) {
                ifNotNull(pe.trygdetidNorListe()) { trygdetidsliste ->
                    includePhrase(TrygdetidListeNorTabell(trygdetidsliste))
                }
            }

            showIf(pe.skalViseTrygdetidEOSTabell(erMndEtterFoedsel)){
                ifNotNull(pe.trygdetidEOSListe()){
                    includePhrase(TBU045V_1)
                    includePhrase(TrygdetidsListeEOSTabell(it))
                }
            }
        }

        showIf(pe.skalViseTrygdetidBilateralTabell(erMndEtterFoedsel)){

            ifNotNull(pe.trygdetidBilateralListe()){
                includePhrase(TBU046V_1)
                includePhrase(TrygdetidsListeBilateralTabell(it))
            }
        }

        showIf(pe.ut_trygdetid()
                and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_redusertframtidigtrygdetid()
                and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd")) {
            includePhrase(TBU047V)
        }

        includePhrase(TBU1187(pe))
        includePhrase(TBU1382(pe))
        includePhrase(TBU1384(pe))

        // Slik har vi fastsatt den nye inntektsgrensen din
        showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")){
            includePhrase(TBU500v)
        }

        showIf(pe.skalViseInntektsgrenseOgAvkortning()) {
            includePhrase(TBUxx4v_og_TBU048V_TBU055V(pe))
        }

        // TODO vises kun om brevkode er PE_UT_14_300 or PE_UT_04_300
        //includePhrase(TBU052V_TBU073V_Del_1_InntektenDinFoerDuBleUfoer())

        includePhrase(TBU052V_TBU073V_SlikHarViFastsattKompensasjonsgradenDin(pe))

        includePhrase(TBU052V_TBU073V_SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres(pe))

        includePhrase(TBU052V_TBU073V_SlikBeregnerViReduksjonenAvUfoeretrygden(pe))

        includePhrase(TBU052V_TBU073V_SlikBlirDinUtbetalingFoerSkatt(pe))

        showIf(pe.pe_ut_tbu601v_tbu604v()) {
            includePhrase(TBU052V_TBU073V_SlikRedusererViBarnetilleggetUtFraInntekt(pe))
        }
        includePhrase(TBU052V_TBU073V_ForDegSomHarRettTilBarnetillegg(pe))

        includePhrase(TBU052V_TBU073V_SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(pe))

        includePhrase(TBU052V_TBU073V_SlikBeregnerViGjenlevendetilleggetDitt(pe))

        includePhrase(TBU052V_TBU073V_ForDegSomMottarEktefelletillegg(pe))

        showIf(pe.skalViseEtteroppgjoer()) {
            includePhrase(TBU052V_TBU073V_EtteroppgjoerAvUforetrygdOgBarnetillegg(pe))
        }
    }

