package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.AfpPeriode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringAutoDto
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.avvik
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.forventetPensjonsgivendeInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.inntektEtterOpphoer
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.inntektFoerUttak
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.inntektIAfpPerioden
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.medlemAvApotekerordningen
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.periode
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.toleranseBeloep
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerIngenEndringAutoDto.uttaksdato
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
 * Vedtak — ingen endring (innenfor toleransebeløpet) — AFP etteroppgjør (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_100`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når forskjellen
 * mellom forventet og faktisk pensjonsgivende inntekt ikke overstiger
 * toleransebeløpet, og pensjonsberegningen derfor ikke
 * skal endres.
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerIngenEndringAuto : AutobrevTemplate<VedtakAfpEtteroppgjoerIngenEndringAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_INGEN_ENDRING_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ingen endring - AFP etteroppgjør",
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
            includePhrase(AfpEtteroppgjoerInnhold.EtteroppgjoerIntro)
            includePhrase(AfpEtteroppgjoerInnhold.IkkeFunnetGrunnlagForAaEndre(oppgjoersAar))

            includePhrase(AfpEtteroppgjoerInnhold.InnrapporterteInntektsopplysningerIkkeSkiller)
            includePhrase(AfpEtteroppgjoerInnhold.LaverePgiKanGiHoyereAfp)

            showIf(medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }

            includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

            includePhrase(AfpEtteroppgjoerInnhold.InntektUtenforEtteroppgjoerListe)

            includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)

            includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)

            includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            includePhrase(AfpEtteroppgjoerInnhold.SamletPgiOpplysning(pensjonsgivendeInntekt = pensjonsgivendeInntekt, oppgjoersAar = oppgjoersAar))

            includePhrase(
                AfpEtteroppgjoerInnhold.InntektFoerUttakInntektEtterOpphoerFordelingPerPeriode(
                    erHelAfpHeleAaret = periode.equalTo(AfpPeriode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = periode.equalTo(AfpPeriode.UTTAK_I_AARET),
                    erOpphoerIAaret = periode.equalTo(AfpPeriode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = periode.equalTo(AfpPeriode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = uttaksdato,
                    opphorsdato = opphorsdato,
                    oppgjoersAar = oppgjoersAar,
                    inntektFoerUttak = inntektFoerUttak,
                    inntektEtterOpphoer = inntektEtterOpphoer,
                    inntektIAfpPerioden = inntektIAfpPerioden,
                ),
            )

            paragraph {
                text(
                    bokmal {
                        +"Ved beregningen av pensjonen din for " + oppgjoersAar.format() + " la vi til grunn at du ville ha en forventet arbeidsinntekt på " + forventetPensjonsgivendeInntektBeregnet.format() + ". Forskjellen mellom den tidligere benyttede arbeidsinntekten og den arbeidsinntekten du etter vår beregning har hatt i perioden, utgjør " + avvik.format() + ". Denne forskjellen er ikke større enn toleransebeløpet som i " + oppgjoersAar.format() + " var på " + toleranseBeloep.format() + "."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din for " + oppgjoersAar.format() + " la vi til grunn at du ville ha ei forventa arbeidsinntekt på " + forventetPensjonsgivendeInntektBeregnet.format() + ". Forskjellen mellom den tidlegare nytta arbeidsinntekta og den arbeidsinntekta du etter vår berekning har hatt i perioden, utgjer " + avvik.format() + ". Denne forskjellen er ikkje større enn toleransebeløpet som i " + oppgjoersAar.format() + " var på " + toleranseBeloep.format() + "."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.PensjonsberegningenBlirIkkeEndret(oppgjoersAar))

            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)

            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageMedDokumentasjonsfrist)

            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}
