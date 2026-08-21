@file:Suppress("LocalVariableName")

package no.nav.pensjon.brev.maler.legacy.vedlegg


import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.personsak
import no.nav.pensjon.brev.api.model.maler.legacy.personsak.selectors.personSak.foedselsdato
import no.nav.pensjon.brev.maler.fraser.ufoer.erUforetidspunktMaanedEtterFoedsel
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.barnetillegg.*
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning.OpplysningerBruktIBeregningTabell
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning.OpplysningerOmAvdoedTabell
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning.SlikBeregnerViUfoeretrygden
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning.SlikBeregnerViUfoeretrygdenKonvertert
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.etteroppgjoer.EtteroppgjoerAvUforetrygdOgBarnetillegg
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntekt.*
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntektsgrense.InntektsgrenseOgAvkortning
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntektsgrense.NyInntektsgrense
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.kompensasjon.SlikBeregnerViReduksjonenAvUfoeretrygden
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.kompensasjon.SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.kompensasjon.SlikBlirDinUtbetalingFoerSkatt
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.kompensasjon.SlikHarViFastsattKompensasjonsgradenDin
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.minsteytelse.ForDegSomHarRettTilMinsteytelse
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.trygdetid.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
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

        // ============================================================
        // Innledning: virkningsdato og grunnbeløp
        // ============================================================
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

        includePhrase(OpplysningerBruktIBeregningTabell(pe))
        includePhrase(OpplysningerOmAvdoedTabell(pe))

        // ============================================================
        // Slik beregner vi uføretrygden din
        // ============================================================
        showIf (pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()) {
            includePhrase(SlikBeregnerViUfoeretrygdenKonvertert(pe))
        }.orShow {
            includePhrase(SlikBeregnerViUfoeretrygden(pe))
        }

        // Minsteytelse
        includePhrase(ForDegSomHarRettTilMinsteytelse(pe))

        // TODO TBU028V-TBU020V trengs for brev PE_UT_04_300 og PE_UT_14_300

        showIf(
            pe.skalViseGrunnbeloepOgYrkesskadeForklaring()) {
            includePhrase(GrunnbeloepOgYrkesskadeForklaring(pe))
        }

        // ============================================================
        // Dette er inntektene vi har brukt i beregningen din
        // ============================================================
        showIf(pe.skalViseInntekterBruktIBeregning()) {
            title1 {
                text(
                    bokmal { +"Dette er inntektene vi har brukt i beregningen din" },
                    nynorsk { +"Dette er inntektene vi har brukt i berekninga di" },
                )
            }
            includePhrase(InntektsgrunnlagFolketrygd(pe))
            includePhrase(InntektsgrunnlagFolketrygdUthevetNotat(pe))

            includePhrase(InntektsgrunnlagUtland(pe))
            includePhrase(InntektsgrunnlagUtlandUthevetNotat(pe))

            includePhrase(InntektsgrunnlagFolketrygdAvdoed(pe))
            includePhrase(InntektsgrunnlagFolketrygdAvdoedUthevetNotat(pe))

            includePhrase(InntektsgrunnlagUtlandAvdoed(pe))
            includePhrase(InntektsgrunnlagUtlandAvdoedUthevetNotat(pe))
        }

        // ============================================================
        // Dette er trygdetiden din
        // ============================================================
        showIf(pe.ut_trygdetid()) {
            includePhrase(DetteErTrygdetidenDin(pe, erMndEtterFoedsel))

            showIf(pe.skalViseTrygdetidNorTabell(erMndEtterFoedsel)) {
                ifNotNull(pe.trygdetidNorListe()) { trygdetidsliste ->
                    includePhrase(TrygdetidListeNorTabell(trygdetidsliste))
                }
            }

            showIf(pe.skalViseTrygdetidEOSTabell(erMndEtterFoedsel)){
                ifNotNull(pe.trygdetidEOSListe()){
                    includePhrase(TrygdetidEOSInnledning)
                    includePhrase(TrygdetidsListeEOSTabell(it))
                }
            }
        }

        showIf(pe.skalViseTrygdetidBilateralTabell(erMndEtterFoedsel)){

            ifNotNull(pe.trygdetidBilateralListe()){
                includePhrase(TrygdetidBilateralInnledning)
                includePhrase(TrygdetidsListeBilateralTabell(it))
            }
        }

        showIf(pe.ut_trygdetid()
                and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_redusertframtidigtrygdetid()
                and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd")) {
            includePhrase(RedusertFramtidigTrygdetid)
        }

        includePhrase(TrygdetidNorAvdoedInnledning(pe))
        includePhrase(TrygdetidEOSAvdoedInnledning(pe))
        includePhrase(TrygdetidBilateralAvdoedInnledning(pe))

        // ============================================================
        // Slik har vi fastsatt den nye inntektsgrensen din
        // ============================================================
        showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")){
            includePhrase(NyInntektsgrense)
        }

        showIf(pe.skalViseInntektsgrenseOgAvkortning()) {
            includePhrase(InntektsgrenseOgAvkortning(pe))
        }

        // ============================================================
        // Kompensasjonsgrad og utbetaling
        // ============================================================
        // TODO vises kun om brevkode er PE_UT_14_300 or PE_UT_04_300
        //includePhrase(TBU052V_TBU073V_Del_1_InntektenDinFoerDuBleUfoer())

        includePhrase(SlikHarViFastsattKompensasjonsgradenDin(pe))

        includePhrase(SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres(pe))

        includePhrase(SlikBeregnerViReduksjonenAvUfoeretrygden(pe))

        includePhrase(SlikBlirDinUtbetalingFoerSkatt(pe))

        // ============================================================
        // Barnetillegg og andre tillegg
        // ============================================================
        showIf(pe.pe_ut_tbu601v_tbu604v()) {
            includePhrase(SlikRedusererViBarnetilleggetUtFraInntekt(pe))
        }
        includePhrase(ForDegSomHarRettTilBarnetillegg(pe))

        includePhrase(SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(pe))

        includePhrase(SlikBeregnerViGjenlevendetilleggetDitt(pe))

        includePhrase(ForDegSomMottarEktefelletillegg(pe))

        // ============================================================
        // Etteroppgjør av uføretrygd og barnetillegg
        // ============================================================
        showIf(pe.skalViseEtteroppgjoer()) {
            includePhrase(EtteroppgjoerAvUforetrygdOgBarnetillegg(pe))
        }
    }

