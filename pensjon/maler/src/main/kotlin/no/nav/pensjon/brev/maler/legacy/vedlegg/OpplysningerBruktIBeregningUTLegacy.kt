@file:Suppress("LocalVariableName")

package no.nav.pensjon.brev.maler.legacy.vedlegg


import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.selectors.pEgruppe10.personsak
import no.nav.pensjon.brev.api.model.maler.legacy.personsak.selectors.personSak.foedselsdato
import no.nav.pensjon.brev.maler.ufore.fraser.erUforetidspunktMaanedEtterFoedsel
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning.SlikBeregnerViUPTilUT
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.barnetillegg.*
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning.OpplysningerBruktIBeregningTabell
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning.OpplysningerOmAvdoedTabell
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntekt.ReduksjonsprosentOgUtbetalingUPTilUT
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning.SlikBeregnerViUfoeretrygden
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.beregning.SlikBeregnerViUfoeretrygdenKonvertert
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.etteroppgjoer.EtteroppgjoerAvUforetrygdOgBarnetillegg
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntekt.*
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntektsgrense.InntektsgrenseOgAvkortning
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntektsgrense.NyInntektsgrense
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.kompensasjon.SlikBeregnerViReduksjonenAvUfoeretrygden
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.kompensasjon.SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.kompensasjon.SlikBlirDinUtbetalingFoerSkatt
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.kompensasjon.SlikHarViFastsattReduksjonsprosentenDin
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.minsteytelse.ForDegSomHarRettTilMinsteytelse
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.trygdetid.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

const val OPPLYSNINGER_OM_BEREGNINGEN_BOKMAL = "Opplysninger om beregningen"
const val OPPLYSNINGER_OM_BEREGNINGEN_NYNORSK = "Opplysningar om utrekninga"

private val vedleggPerProfil: Map<OpplysningerBruktIBeregningUTProfil, AttachmentTemplate<LangBokmalNynorsk, PEgruppe10>> =
    OpplysningerBruktIBeregningUTProfil.entries.associateWith { lagVedlegg(it) }

fun vedleggOpplysningerBruktIBeregningUTLegacy(profil: OpplysningerBruktIBeregningUTProfil): AttachmentTemplate<LangBokmalNynorsk, PEgruppe10> =
    vedleggPerProfil.getValue(profil)

private fun lagVedlegg(profil: OpplysningerBruktIBeregningUTProfil) =
    createAttachment<LangBokmalNynorsk, PEgruppe10>(
        title = {
            text(
                bokmal { +OPPLYSNINGER_OM_BEREGNINGEN_BOKMAL },
                nynorsk { +OPPLYSNINGER_OM_BEREGNINGEN_NYNORSK },
            )
        },
        includeSakspart = false,
    ) {
        val pe = argument
        val utelatVedDoedsfall = profil.utelatVedDoedsfall.expr()
        val erOmregningUPtilUT = profil.erOmregningUPtilUT.expr()
        val erIkkeInntektsendring = (!profil.erInntektsendring).expr()
        val erIkkeBarnetilleggsbrev = (!profil.erBarnetilleggsbrev).expr()
        val erIkkeNyBeregningAvBarnetillegg = (!profil.erNyBeregningAvBarnetillegg).expr()
        val erIkkeBarnetilleggsbrevUtenEndring = (!profil.erBarnetilleggsbrevUtenEndring).expr()
        val erBeregningsendring = profil.erBeregningsendring.expr()

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

        includePhrase(
            OpplysningerBruktIBeregningTabell(
                pe = pe,
                visSivilstatusUansett = erOmregningUPtilUT,
            )
        )
        showIf(erIkkeInntektsendring and erIkkeBarnetilleggsbrev) {
            includePhrase(
                OpplysningerOmAvdoedTabell(
                    pe = pe,
                    utelatVedDoedsfall = utelatVedDoedsfall,
                )
            )
        }

        // ============================================================
        // Slik beregner vi uføretrygden din
        // ============================================================
        showIf (pe.vedtaksbrev_vedtaksdata_kravhode_brukerkonvertertup()) {
            showIf(pe.erFulltBeregningsbrev(profil)) {
                includePhrase(
                    SlikBeregnerViUfoeretrygdenKonvertert(
                        pe = pe,
                        visForklaringVedEndring = profil.erEndring.expr(),
                        visForklaringVedOektUfoeregrad = profil.erOekningUfoeregrad.expr(),
                    )
                )
            }
        }.orShow {
            showIf(pe.erFulltBeregningsbrev(profil)) {
                includePhrase(SlikBeregnerViUfoeretrygden(pe))
            }
        }

        // Minsteytelse
        showIf(profil.visMinsteytelse.expr()) {
            includePhrase(
                ForDegSomHarRettTilMinsteytelse(
                    pe = pe,
                    visUtlandsforklaringUansett = profil.visUtlandsforklaringUansett.expr(),
                )
            )
        }

        showIf(erOmregningUPtilUT){
            includePhrase(SlikBeregnerViUPTilUT(pe))
        }

        showIf(pe.skalViseGrunnbeloepOgYrkesskadeForklaring(profil)) {
            showIf(erIkkeInntektsendring) {
                includePhrase(Grunnbeloep)
            }
            includePhrase(YrkesskadeForklaring(pe))
        }

        // ============================================================
        // Dette er inntektene vi har brukt i beregningen din
        // ============================================================
        showIf(pe.skalViseInntekterBruktIBeregning(profil)) {
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

            showIf(
                erIkkeInntektsendring and
                    (!profil.erOekningUfoeregrad).expr() and
                    erIkkeBarnetilleggsbrev
            ) {
                includePhrase(
                    InntektsgrunnlagFolketrygdAvdoed(
                        pe = pe,
                        utelatVedDoedsfall = utelatVedDoedsfall,
                    )
                )
                includePhrase(
                    InntektsgrunnlagFolketrygdAvdoedUthevetNotat(
                        pe = pe,
                        utelatVedDoedsfall = utelatVedDoedsfall,
                    )
                )

                includePhrase(
                    InntektsgrunnlagUtlandAvdoed(
                        pe = pe,
                        utelatVedDoedsfall = utelatVedDoedsfall,
                    )
                )
                includePhrase(
                    InntektsgrunnlagUtlandAvdoedUthevetNotat(
                        pe = pe,
                        utelatVedDoedsfall = utelatVedDoedsfall,
                    )
                )
            }
        }

        // ============================================================
        // Dette er trygdetiden din
        // ============================================================
        showIf(pe.ut_trygdetid(profil)) {
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

        showIf(pe.skalViseTrygdetidBilateralTabell(erMndEtterFoedsel, profil)){

            ifNotNull(pe.trygdetidBilateralListe()){
                includePhrase(TrygdetidBilateralInnledning)
                includePhrase(TrygdetidsListeBilateralTabell(it))
            }
        }

        showIf(pe.ut_trygdetid(profil)
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

        showIf(pe.skalViseInntektsgrenseOgAvkortning(profil)) {
            includePhrase(
                InntektsgrenseOgAvkortning(
                    pe = pe,
                    visForklaringOmInntektEtterUfoerhet = erIkkeNyBeregningAvBarnetillegg,
                )
            )
        }

        // ============================================================
        // Reduksjonsprosent og utbetaling
        // ============================================================
        showIf(erOmregningUPtilUT) {
            includePhrase(
                ReduksjonsprosentOgUtbetalingUPTilUT(
                    pe = pe,
                    erBeregningsendring = erBeregningsendring,
                    visMinstepensjonsforklaring = profil.visMinstepensjonsforklaring.expr(),
                    visForklaringOmRettTilBarnetillegg = profil.visForklaringOmRettTilBarnetillegg.expr(),
                    visBarnetilleggsberegning = profil.visBarnetilleggsberegning.expr(),
                    utelatVedDoedsfall = utelatVedDoedsfall,
                )
            )
        }

        includePhrase(
            SlikHarViFastsattReduksjonsprosentenDin(
                pe = pe,
                erBeregningsendring = erBeregningsendring,
                visDetaljertReduksjonsberegning = (!profil.erEndringEllerOekning).expr() and
                    erIkkeInntektsendring,
            )
        )

        showIf(erIkkeBarnetilleggsbrevUtenEndring) {
            includePhrase(
                SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres(
                    pe = pe,
                    utelatVedDoedsfall = utelatVedDoedsfall,
                )
            )
        }

        showIf(erIkkeNyBeregningAvBarnetillegg) {
            includePhrase(SlikBeregnerViReduksjonenAvUfoeretrygden(pe))
        }

        includePhrase(
            SlikBlirDinUtbetalingFoerSkatt(
                pe = pe,
                visOrdinaereUtbetalingsavsnitt = erIkkeBarnetilleggsbrev,
            )
        )

        // ============================================================
        // Barnetillegg og andre tillegg
        // ============================================================
        showIf(pe.erInntektsendringMedEndretBarnetillegg()) {
            includePhrase(SlikRedusererViBarnetilleggetUtFraInntekt(pe))
        }
        includePhrase(
            ForDegSomHarRettTilBarnetillegg(
                pe = pe,
                visForklaringForBrevtype = profil.visForklaringOmRettTilBarnetillegg.expr(),
                utelatVedDoedsfall = utelatVedDoedsfall,
            )
        )

        includePhrase(
            SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(
                pe = pe,
                visForklaringForBrevtype = profil.visBarnetilleggsberegning.expr(),
                utelatVedDoedsfall = utelatVedDoedsfall,
            )
        )

        includePhrase(
            SlikBeregnerViGjenlevendetilleggetDitt(
                pe = pe,
                visOmregningsforklaring = erOmregningUPtilUT,
                visGenerellForklaring = erIkkeInntektsendring and
                    erIkkeBarnetilleggsbrevUtenEndring,
            )
        )

        includePhrase(
            ForDegSomMottarEktefelletillegg(
                pe = pe,
                visGenerellForklaring = (!profil.erInnvilgelse).expr() and
                    erIkkeBarnetilleggsbrev,
                visOmregningsforklaring = erOmregningUPtilUT,
                visMinstepensjonsforklaring = profil.visMinstepensjonsforklaring.expr(),
                utelatVedDoedsfall = utelatVedDoedsfall,
            )
        )

        // ============================================================
        // Etteroppgjør av uføretrygd og barnetillegg
        // ============================================================
        showIf(pe.skalViseEtteroppgjoer(profil)) {
            includePhrase(EtteroppgjoerAvUforetrygdOgBarnetillegg(pe))
        }
    }
