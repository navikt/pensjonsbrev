package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerForklaringer
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.PesysDataSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.PesysDataSelectors.forventetPensjonsgivendeInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.PesysDataSelectors.inntektEtterOpphoer
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.PesysDataSelectors.inntektFoerUttak
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.PesysDataSelectors.inntektIAfpPerioden
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.PesysDataSelectors.medlemAvApotekerordningen
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.PesysDataSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.PesysDataSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.PesysDataSelectors.scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.PesysDataSelectors.toleranseBeloep
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarDtoSelectors.pesysData
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
 * Vedtak — AFP etteroppgjør, ingen endring etter mottatt svar.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_103`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når bruker
 * har lagt fram nye opplysninger om inntekt, og ny beregning fortsatt holder
 * seg innenfor toleransebeløpet — slik at det ikke blir tilbakekreving.
 * Forklaringen til brukeren har fem mulige varianter avhengig av hvilke
 * inntektsfelt (IFU/IEO) som er registrert og når AFP ble tatt ut, se
 * [VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto.Scenario].
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerIngenEndringEtterSvar : RedigerbarTemplate<VedtakAfpEtteroppgjoerIngenEndringEtterSvarDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_ETTEROPPGJOER_INGEN_ENDR_ETTER_SVAR

    override val kategori = Brevkategori.ETTEROPPGJOER
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.AFP)

    override val featureToggle = FeatureToggles.vedtakAfpEtteroppgjoerIngenEndringEtterSvar.toggle

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ingen endring etter mottatt svar - AFP etteroppgjør",
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
            includePhrase(AfpEtteroppgjoerInnhold.HarVaertRiktigIntro(pesysData.oppgjoersAar))
            showIf(pesysData.medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(pesysData.oppgjoersAar))

            showIf(pesysData.scenario.equalTo(Scenario.INGEN_NYE_OPPLYSNINGER)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IngenNyeOpplysningerOmEndretInntektFoerUttak(inntektFoerUttak = pesysData.inntektFoerUttak, oppgjoersAar = pesysData.oppgjoersAar))
            }

            showIf(pesysData.scenario.equalTo(Scenario.IFU_UTTAK_I_AARET)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakIAaret(inntektFoerUttak = pesysData.inntektFoerUttak, oppgjoersAar = pesysData.oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektFoerUttak = pesysData.inntektFoerUttak))
            }

            showIf(pesysData.scenario.equalTo(Scenario.IFU_UTTAK_FOER_AARET)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakFoerAaret(inntektFoerUttak = pesysData.inntektFoerUttak, oppgjoersAar = pesysData.oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektFoerUttak = pesysData.inntektFoerUttak))
            }

            showIf(pesysData.scenario.equalTo(Scenario.IFU_OG_IEO_REGISTRERT)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IfuOgIeoOverstyrt(inntektFoerUttak = pesysData.inntektFoerUttak, inntektEtterOpphoer = pesysData.inntektEtterOpphoer, oppgjoersAar = pesysData.oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenIfuOgIeo(inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektFoerUttak = pesysData.inntektFoerUttak, inntektEtterOpphoer = pesysData.inntektEtterOpphoer))
            }

            showIf(pesysData.scenario.equalTo(Scenario.KUN_IEO_REGISTRERT)) {
                includePhrase(AfpEtteroppgjoerForklaringer.KunIeoOverstyrt(inntektEtterOpphoer = pesysData.inntektEtterOpphoer, oppgjoersAar = pesysData.oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIeo(inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektEtterOpphoer = pesysData.inntektEtterOpphoer))
            }

            paragraph {
                text(
                    bokmal {
                        +"Ved beregningen av pensjonen din for " + pesysData.oppgjoersAar.format() + " la vi til grunn at du ville ha en forventet arbeidsinntekt på " + pesysData.forventetPensjonsgivendeInntektBeregnet.format() + ". Differansen mellom denne tidligere medregnede arbeidsinntekten og den arbeidsinntekten du etter vår nye beregning har hatt i perioden, utgjør " + pesysData.avvik.format() + ". Denne differansen er ikke større enn toleransebeløpet som i " + pesysData.oppgjoersAar.format() + " var " + pesysData.toleranseBeloep.format() + "."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din for " + pesysData.oppgjoersAar.format() + " la vi til grunn at du ville ha ei forventa arbeidsinntekt på " + pesysData.forventetPensjonsgivendeInntektBeregnet.format() + ". Differansen mellom denne tidlegare medrekna arbeidsinntekta og den arbeidsinntekta du etter vår nye berekning har hatt i perioden, utgjer " + pesysData.avvik.format() + ". Denne differansen er ikkje større enn toleransebeløpet som i " + pesysData.oppgjoersAar.format() + " var " + pesysData.toleranseBeloep.format() + "."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.NyBeregningFoererIkkeTilTilbakekreving(pesysData.oppgjoersAar))

            includePhrase(AfpEtteroppgjoerAvslutning)
        }
    }
}
