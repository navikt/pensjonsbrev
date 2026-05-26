package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerForklaringer
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDto.NyPensjonsberegningPeriode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.forlitebetalt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.fradragberegnetai
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.fullafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.ieo
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.ifu
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.iiap
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.korrigertafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.pgi
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.tpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.utbetaltafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDtoSelectors.uttaksdato
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — AFP etteroppgjør med etterbetaling (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_105`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når bruker
 * har lagt fram nye dokumenterte opplysninger og avviket mellom forventet
 * og faktisk pensjonsgivende inntekt overstiger toleransebeløpet, slik at
 * det blir **etterbetaling** av for lite utbetalt AFP. Motsatt finansiell
 * retning av [VedtakAfpEtteroppgjoerTilbakekrevingAuto] (PE_AF_04_107).
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAuto : AutobrevTemplate<VedtakAfpEtteroppgjoerEtterbetalingEtterSvarAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_ETTERBETALING_ETTER_SVAR_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - AFP etteroppgjør med etterbetaling",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + oppgjoersAar.format() },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til tidligere brev om etteroppgjør av avtalefestet pensjon (AFP) for " +
                            oppgjoersAar.format() + ". Resultatet av etteroppgjøret viser at du har fått " +
                            forlitebetalt.format() + " for lite utbetalt pensjon."
                    },
                    nynorsk {
                        +"Vi viser til tidlegare brev om etteroppgjer av avtalefesta pensjon (AFP) for " +
                            oppgjoersAar.format() + ". Resultatet av etteroppgjeret viser at du har fått " +
                            forlitebetalt.format() + " for lite utbetalt pensjon."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            // Scenario A — INGEN_OVERSTYRING_UTTAK_I_AARET.
            // Avviker fra [AfpEtteroppgjoerForklaringer.IngenNyeOpplysningerOmEndretInntektFoerUttak]:
            // 105 har "er derfor satt til" der frasen har "blir etter dette
            // satt til", og 105 har en ekstra avsluttende setning om IIAP.
            // Inlinet.
            showIf(scenario.equalTo(Scenario.INGEN_OVERSTYRING_UTTAK_I_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Dokumentasjonen " +
                                "som foreligger gir ikke tilstrekkelig grunnlag for å endre tidligere " +
                                "beregnet arbeidsinntekt før uttak av AFP. Arbeidsinntekten din er derfor " +
                                "satt til " + ifu.format() + " i samsvar med den tidligere beregningen. " +
                                "Dette beløpet skal holdes utenfor etteroppgjøret for " +
                                oppgjoersAar.format() + ". Arbeidsinntekten din i den perioden du har " +
                                "mottatt AFP er som tidligere satt til " + iiap.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Dokumentasjonen " +
                                "som ligg føre, gir ikkje godt nok grunnlag for å endre tidlegare berekna " +
                                "arbeidsinntekt før uttak av AFP. Arbeidsinntekta di er derfor sett til " +
                                ifu.format() + " i samsvar med den tidlegare berekninga. Dette beløpet " +
                                "skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() +
                                ". Arbeidsinntekta di i den perioden du har fått AFP, er, som tidlegare, " +
                                "sett til " + iiap.format() + "."
                        },
                    )
                }
            }

            // Scenario B — IFU_OVERSTYRT_UTTAK_I_AARET.
            // Forklaringen er en eksakt match med fellesfrasen.
            showIf(scenario.equalTo(Scenario.IFU_OVERSTYRT_UTTAK_I_AARET)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakIAaret(ifu, oppgjoersAar))
                // Paret "Den faktiske arbeidsinntekten …" inlines (delt med
                // scenario C i [DenFaktiskeArbeidsinntektenKunIfu105]):
                // [AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu]
                // bruker "differansen" og mangler "er satt til" der 105 har
                // "forskjellen" og "er satt til".
                includePhrase(DenFaktiskeArbeidsinntektenKunIfu105(iiap = iiap, oppgjoersAar = oppgjoersAar, pgi = pgi, ifu = ifu))
            }

            // Scenario C — IFU_OVERSTYRT_HEL_AFP.
            // Forklaringen gjenbrukes; eneste avvik er ett manglende komma i
            // bokmål ("tidligere arbeid er satt" vs. "tidligere arbeid, er
            // satt") som fellesfrasen normaliserer. Paret «Den faktiske …»
            // har derimot flere ordlydsavvik (se Scenario B) og bruker den
            // lokale frasen.
            showIf(scenario.equalTo(Scenario.IFU_OVERSTYRT_HEL_AFP)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakFoerAaret(ifu = ifu, oppgjoersAar = oppgjoersAar))
                includePhrase(DenFaktiskeArbeidsinntektenKunIfu105(iiap = iiap, oppgjoersAar = oppgjoersAar, pgi = pgi, ifu = ifu))
            }

            // Scenario D — IFU_OG_IEO_OVERSTYRT.
            // Forklaringen avviker fra
            // [AfpEtteroppgjoerForklaringer.IfuOgIeoOverstyrt] i ordstilling
            // ("i perioder du ikke har hatt samtidig rett" vs. "i perioder
            // hvor du ikke samtidig har hatt rett") — inlinet. Paret «Den
            // faktiske …» gjenbrukes; eneste avvik er ett manglende komma
            // ("mottatt AFP er" vs. "mottatt AFP, er") som fellesfrasen
            // normaliserer.
            showIf(scenario.equalTo(Scenario.IFU_OG_IEO_OVERSTYRT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                                "opplysninger har du hatt en høyere arbeidsinntekt enn tidligere beregnet " +
                                "i perioder du ikke har hatt samtidig rett til AFP. Arbeidsinntekten din " +
                                "for denne perioden er endret i samsvar med disse opplysningene til " +
                                "henholdsvis " + ifu.format() + " for perioden før uttak av AFP og " +
                                ieo.format() + " for perioden etter opphør av AFP. Disse beløpene skal " +
                                "holdes utenfor etteroppgjøret for " + oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                                "opplysningar har du hatt ei høgare arbeidsinntekt enn tidlegare berekna " +
                                "i periodar der du ikkje har hatt samtidig rett til AFP. Arbeidsinntekta " +
                                "di for denne perioden er endra i samsvar med desse opplysningane til " +
                                "høvesvis " + ifu.format() + " for perioden før uttak av AFP og " +
                                ieo.format() + " for perioden etter at AFP tok slutt. Desse beløpa skal " +
                                "haldast utanfor etteroppgjeret for " + oppgjoersAar.format() + "."
                        },
                    )
                }
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenIfuOgIeo(
                        iiap = iiap, oppgjoersAar = oppgjoersAar, pgi = pgi, ifu = ifu, ieo = ieo,
                    ),
                )
            }

            // Scenario E — KUN_IEO_OVERSTYRT.
            // Forklaringen avviker fra
            // [AfpEtteroppgjoerForklaringer.KunIeoOverstyrt] med "etter
            // opphør av AFP" (105) vs. "etter opphøret av AFP" (frase).
            // Paret «Den faktiske …» bruker "forskjellen" der frasen bruker
            // "differansen", og mangler komma etter "mottatt AFP". Inlinet.
            showIf(scenario.equalTo(Scenario.KUN_IEO_OVERSTYRT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntektsforholdene dine. Ifølge nye " +
                                "opplysninger har du hatt en høyere arbeidsinntekt enn tidligere beregnet " +
                                "etter opphør av AFP. Arbeidsinntekten din for denne perioden er endret " +
                                "i samsvar med disse opplysningene til " + ieo.format() + ". Dette " +
                                "beløpet skal holdes utenfor etteroppgjøret for " + oppgjoersAar.format() +
                                "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntektsforholda dine. Ifølgje nye " +
                                "opplysningar har du hatt ei høgare arbeidsinntekt enn tidlegare berekna " +
                                "etter at AFP tok slutt. Arbeidsinntekta di for denne perioden er endra " +
                                "i samsvar med desse opplysningane til " + ieo.format() + ". Dette " +
                                "beløpet skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() +
                                "."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP er " +
                                iiap.format() + ". Dette beløpet utgjør forskjellen mellom din " +
                                "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekten din etter opphøret av AFP på " +
                                ieo.format() + "."
                        },
                        nynorsk {
                            +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er " +
                                iiap.format() + ". Dette beløpet utgjer forskjellen mellom den " +
                                "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                                pgi.format() + " og arbeidsinntekta di etter at AFP tok slutt, på " +
                                ieo.format() + "."
                        },
                    )
                }
            }

            // Toleransebeløp-paragrafen — 105 sier "lavere" (etterbetaling)
            // der [AfpTilbakekrevingBody.ToleransebeloepOverskrider] sier
            // "høyere" (tilbakekreving), og avslutter med kortere setning.
            // Inlinet.
            paragraph {
                text(
                    bokmal {
                        +"Den arbeidsinntekten du har hatt i perioden med AFP, er " + avvik.format() +
                            " lavere enn den forventede arbeidsinntekten som ble lagt til grunn ved " +
                            "utbetalingen av pensjonen din i det aktuelle tidsrommet. Dette er mer enn " +
                            "toleransebeløpet som i " + oppgjoersAar.format() + " var 15 000 kroner. " +
                            "Vi har derfor beregnet ny pensjon for perioden."
                    },
                    nynorsk {
                        +"Den arbeidsinntekta du har hatt i perioden med AFP, er " + avvik.format() +
                            " lågare enn den forventa arbeidsinntekta som blei lagd til grunn ved " +
                            "utbetalinga av pensjonen din i det aktuelle tidsrommet. Dette er meir enn " +
                            "toleransebeløpet som i " + oppgjoersAar.format() + " var 15 000 kroner. " +
                            "Vi har derfor berekna ny pensjon for perioden."
                    },
                )
            }

            // Hele «Ny pensjonsberegning»-blokken (title1 + intro + tabell +
            // sum). Delt med PE_AF_04_101.
            includePhrase(
                AfpEtteroppgjoerInnhold.NyPensjonsberegningEtterbetalingBlokk(
                    erHeleAaret = periode.equalTo(NyPensjonsberegningPeriode.HELE_AARET),
                    erUttakIAaret = periode.equalTo(NyPensjonsberegningPeriode.UTTAK_I_AARET_LOEPENDE),
                    erOpphoerIAaret = periode.equalTo(NyPensjonsberegningPeriode.UTTAK_FOER_AARET_OPPHOR_I_AARET),
                    erUttakOgOpphoerIAaret = periode.equalTo(NyPensjonsberegningPeriode.UTTAK_OG_OPPHOR_I_AARET),
                    uttaksdato = uttaksdato,
                    opphorsdato = opphorsdato,
                    oppgjoersAar = oppgjoersAar,
                    fullafp = fullafp,
                    fradragberegnetai = fradragberegnetai,
                    iiap = iiap,
                    tpiberegnet = tpiberegnet,
                    korrigertafp = korrigertafp,
                    utbetaltafp = utbetaltafp,
                    forlitebetalt = forlitebetalt,
                ),
            )

            paragraph {
                text(
                    bokmal { +"Det vil bli trukket skatt av etterbetalingsbeløpet." },
                    nynorsk { +"Det blir trekt skatt av etterbetalingsbeløpet." },
                )
            }

            // Forbehold om refusjonskrav — delt med PE_AF_04_101. Harmonisert
            // til "mulige/moglege" (plural).
            includePhrase(AfpEtteroppgjoerInnhold.RefusjonskravForbehold)

            // Avslutning — gjenbrukes fra fellesfrasene. Mindre ordlydsavvik
            // mot Exstream-originalen ("6 uker" → "seks uker" i klage-frasen)
            // harmoniseres mot fellesfrasen, samme valg som i 107.
            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageSeksUker)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}

/**
 * Paret «Den faktiske arbeidsinntekten …»-paragraf for 105-scenariene B og C
 * (`IFU_OVERSTYRT_UTTAK_I_AARET` og `IFU_OVERSTYRT_HEL_AFP`). Avviker fra
 * fellesfrasen [AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu]
 * på to ordlydspunkter ("er satt til" i stedet for "er", og "forskjellen" i
 * stedet for "differansen"), og er derfor lokal for 105.
 */
private data class DenFaktiskeArbeidsinntektenKunIfu105(
    val iiap: Expression<Kroner>,
    val oppgjoersAar: Expression<Year>,
    val pgi: Expression<Kroner>,
    val ifu: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Den faktiske arbeidsinntekten i den perioden du har mottatt AFP er satt til " +
                        iiap.format() + ". Dette beløpet utgjør forskjellen mellom din " +
                        "pensjonsgivende inntekt for " + oppgjoersAar.format() + " på " +
                        pgi.format() + " og arbeidsinntekten din før uttak av AFP på " +
                        ifu.format() + "."
                },
                nynorsk {
                    +"Den faktiske arbeidsinntekta i den perioden du har fått AFP, er sett til " +
                        iiap.format() + ". Dette beløpet utgjer forskjellen mellom den " +
                        "pensjonsgivande inntekta di for " + oppgjoersAar.format() + " på " +
                        pgi.format() + " og arbeidsinntekta di før uttak av AFP på " +
                        ifu.format() + "."
                },
            )
        }
    }
}
