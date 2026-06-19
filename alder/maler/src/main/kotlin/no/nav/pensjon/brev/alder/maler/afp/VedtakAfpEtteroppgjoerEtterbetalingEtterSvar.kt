package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerForklaringer
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold.NyPensjonsberegningEtterbetalingBlokk
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto.NyPensjonsberegningPeriode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.forlitebetalt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.fradragBeregnetArbeidsInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.fullAfp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.inntektEtterOpphoer
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.inntektFoerUttak
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.inntektIAfpPerioden
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.korrigertAfp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.medlemAvApotekerordningen
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.tidligereArbeidsInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.toleranseBeloep
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.utbetaltAfp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.PesysDataSelectors.uttaksdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
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
object VedtakAfpEtteroppgjoerEtterbetalingEtterSvar : RedigerbarTemplate<VedtakAfpEtteroppgjoerEtterbetalingEtterSvarDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_ETTEROPPGJOER_ETTERBETALING_ETTER_SVAR

    override val kategori = Brevkategori.ETTEROPPGJOER
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.AFP)

    override val featureToggle = FeatureToggles.vedtakAfpEtteroppgjoerEtterbetalingEtterSvar.toggle

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - AFP etteroppgjør med etterbetaling etter mottatt svar",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + pesysData.oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + pesysData.oppgjoersAar.format() },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til tidligere brev om etteroppgjør for avtalefestet pensjon (AFP) for " + pesysData.oppgjoersAar.format() + ". Resultatet av etteroppgjøret viser at du har fått " + pesysData.forlitebetalt.format() + " for lite utbetalt pensjon."
                    },
                    nynorsk {
                        +"Vi viser til tidlegare brev om etteroppgjer for avtalefesta pensjon (AFP) for " + pesysData.oppgjoersAar.format() + ". Resultatet av etteroppgjeret viser at du har fått " + pesysData.forlitebetalt.format() + " for lite utbetalt pensjon."
                    },
                )
            }

            showIf(pesysData.medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(pesysData.oppgjoersAar))

            showIf(pesysData.scenario.equalTo(Scenario.INGEN_OVERSTYRING_UTTAK_I_AARET)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IngenNyeOpplysningerOmEndretInntektFoerUttak(inntektFoerUttak = pesysData.inntektFoerUttak, oppgjoersAar = pesysData.oppgjoersAar))
                paragraph {
                    text(
                        bokmal {
                            +"Arbeidsinntekten din i den perioden du har mottatt AFP er som tidligere satt til " + pesysData.inntektIAfpPerioden.format() + "."
                        },
                        nynorsk {
                            +"Arbeidsinntekta di i den perioden du har fått AFP er som tidlegare sett til " + pesysData.inntektIAfpPerioden.format() + "."
                        },
                    )
                }

            }.orShowIf(pesysData.scenario.equalTo(Scenario.IFU_OVERSTYRT_UTTAK_I_AARET)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakIAaret(inntektFoerUttak = pesysData.inntektFoerUttak, oppgjoersAar = pesysData.oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektFoerUttak = pesysData.inntektFoerUttak))

            }.orShowIf(pesysData.scenario.equalTo(Scenario.IFU_OVERSTYRT_HEL_AFP)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakFoerAaret(inntektFoerUttak = pesysData.inntektFoerUttak, oppgjoersAar = pesysData.oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektFoerUttak = pesysData.inntektFoerUttak))

            }.orShowIf(pesysData.scenario.equalTo(Scenario.IFU_OG_IEO_OVERSTYRT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntekten din. Ifølge nye opplysninger har du hatt en høyere arbeidsinntekt enn tidligere beregnet i perioder du ikke har hatt rett til AFP. Arbeidsinntekten din for denne perioden er endret i samsvar med disse opplysningene til " + pesysData.inntektFoerUttak.format() + " for perioden før uttak av AFP og " + pesysData.inntektEtterOpphoer.format() + " for perioden etter opphør av AFP. Disse beløpene skal holdes utenfor etteroppgjøret for " + pesysData.oppgjoersAar.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntekta di. Ifølgje nye opplysningar har du hatt ei høgare arbeidsinntekt enn tidlegare berekna i periodar der du ikkje har hatt rett til AFP. Arbeidsinntekta di for denne perioden er endra i samsvar med desse opplysningane til " + pesysData.inntektFoerUttak.format() + " for perioden før uttak av AFP og " + pesysData.inntektEtterOpphoer.format() + " for perioden etter at AFP tok slutt. Desse beløpa skal haldast utanfor etteroppgjeret for " + pesysData.oppgjoersAar.format() + "."
                        },
                    )
                }
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenIfuOgIeo(
                        inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektFoerUttak = pesysData.inntektFoerUttak, inntektEtterOpphoer = pesysData.inntektEtterOpphoer,
                    ),
                )

            }.orShowIf(pesysData.scenario.equalTo(Scenario.KUN_IEO_OVERSTYRT)) {
                includePhrase(AfpEtteroppgjoerForklaringer.KunIeoOverstyrt(inntektEtterOpphoer = pesysData.inntektEtterOpphoer, oppgjoersAar = pesysData.oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIeo(inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektEtterOpphoer = pesysData.inntektEtterOpphoer))
            }

            paragraph {
                text(
                    bokmal {
                        +"Den arbeidsinntekten du har hatt i perioden med AFP er " + pesysData.avvik.format() + " lavere enn den forventede arbeidsinntekten som ble lagt til grunn ved utbetalingen av pensjonen din i det aktuelle tidsrommet. Dette er mer enn toleransebeløpet som i " + pesysData.oppgjoersAar.format() + " var " + pesysData.toleranseBeloep.format() + ". Vi har derfor beregnet ny pensjon for perioden."
                    },
                    nynorsk {
                        +"Den arbeidsinntekta du har hatt i perioden med AFP er " + pesysData.avvik.format() + " lågare enn den forventa arbeidsinntekta som blei lagd til grunn ved utbetalinga av pensjonen din i det aktuelle tidsrommet. Dette er meir enn toleransebeløpet som i " + pesysData.oppgjoersAar.format() + " var " + pesysData.toleranseBeloep.format() + ". Vi har derfor berekna ny pensjon for perioden."
                    },
                )
            }

            includePhrase(
                NyPensjonsberegningEtterbetalingBlokk(
                    erHeleAaret = pesysData.periode.equalTo(NyPensjonsberegningPeriode.HELE_AARET),
                    erUttakIAaret = pesysData.periode.equalTo(NyPensjonsberegningPeriode.UTTAK_I_AARET_LOEPENDE),
                    erOpphoerIAaret = pesysData.periode.equalTo(NyPensjonsberegningPeriode.UTTAK_FOER_AARET_OPPHOR_I_AARET),
                    erUttakOgOpphoerIAaret = pesysData.periode.equalTo(NyPensjonsberegningPeriode.UTTAK_OG_OPPHOR_I_AARET),
                    uttaksdato = pesysData.uttaksdato,
                    opphorsdato = pesysData.opphorsdato,
                    oppgjoersAar = pesysData.oppgjoersAar,
                    fullAfp = pesysData.fullAfp,
                    fradragBeregnetArbeidsInntekt = pesysData.fradragBeregnetArbeidsInntekt,
                    inntektIAfpPerioden = pesysData.inntektIAfpPerioden,
                    tidligereArbeidsInntektBeregnet = pesysData.tidligereArbeidsInntektBeregnet,
                    korrigertAfp = pesysData.korrigertAfp,
                    utbetaltAfp = pesysData.utbetaltAfp,
                    forlitebetalt = pesysData.forlitebetalt,
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

            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageSeksUkerMedLenke)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}
