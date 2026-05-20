package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerForklaringer
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.fpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.ieo
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.ifu
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.iiap
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.pgi
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDtoSelectors.scenario
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — AFP etteroppgjør, ingen endring etter mottatt svar (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_103`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når bruker
 * har lagt fram nye opplysninger om inntekt, og ny beregning fortsatt holder
 * seg innenfor toleransebeløpet — slik at det ikke blir tilbakekreving.
 * Forklaringen til brukeren har fem mulige varianter avhengig av hvilke
 * inntektsfelt (IFU/IEO) som er registrert og når AFP ble tatt ut, se
 * [VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto.Scenario].
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerIngenEndringEtterSvarAuto :
    AutobrevTemplate<VedtakAfpEtteroppgjoerIngenEndringEtterSvarAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_INGEN_ENDR_ETTER_SVAR_AUTO

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
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + oppgjoersAar.format() },
            )
        }

        outline {
            includePhrase(AfpEtteroppgjoerInnhold.HarVaertRiktigIntro(oppgjoersAar))
            includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            showIf(scenario.equalTo(Scenario.INGEN_NYE_OPPLYSNINGER)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IngenNyeOpplysningerOmEndretInntektFoerUttak(ifu, oppgjoersAar))
            }

            showIf(scenario.equalTo(Scenario.IFU_UTTAK_I_AARET)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakIAaret(ifu, oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(iiap, oppgjoersAar, pgi, ifu))
            }

            showIf(scenario.equalTo(Scenario.IFU_UTTAK_FOER_AARET)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakFoerAaret(ifu, oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(iiap, oppgjoersAar, pgi, ifu))
            }

            showIf(scenario.equalTo(Scenario.IFU_OG_IEO_REGISTRERT)) {
                includePhrase(AfpEtteroppgjoerForklaringer.IfuOgIeoOverstyrt(ifu, ieo, oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenIfuOgIeo(iiap, oppgjoersAar, pgi, ifu, ieo))
            }

            showIf(scenario.equalTo(Scenario.KUN_IEO_REGISTRERT)) {
                includePhrase(AfpEtteroppgjoerForklaringer.KunIeoOverstyrt(ieo, oppgjoersAar))
                includePhrase(AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIeo(iiap, oppgjoersAar, pgi, ieo))
            }

            paragraph {
                text(
                    bokmal {
                        +"Ved beregningen av pensjonen din for " + oppgjoersAar.format() + " la vi til " +
                            "grunn at du ville ha en forventet arbeidsinntekt på " + fpiberegnet.format() +
                            ". Differansen mellom denne tidligere medregnede arbeidsinntekten og den " +
                            "arbeidsinntekten du etter vår nye beregning har hatt i perioden, utgjør " +
                            avvik.format() + ". Denne differansen er ikke større enn toleransebeløpet " +
                            "som i " + oppgjoersAar.format() + " var 15 000 kroner."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din for " + oppgjoersAar.format() + " la vi til " +
                            "grunn at du ville ha ei forventa arbeidsinntekt på " + fpiberegnet.format() +
                            ". Differansen mellom denne tidlegare medrekna arbeidsinntekta og den " +
                            "arbeidsinntekta du etter vår nye berekning har hatt i perioden, utgjer " +
                            avvik.format() + ". Denne differansen er ikkje større enn toleransebeløpet " +
                            "som i " + oppgjoersAar.format() + " var 15 000 kroner."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.NyBeregningFoererIkkeTilTilbakekreving(oppgjoersAar))

            // Avslutning — rettigheter, plikter og kontaktinformasjon (alltid).
            includePhrase(AfpEtteroppgjoerAvslutning)
        }
    }
}
