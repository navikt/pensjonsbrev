package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringDto.Periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerIngenEndringDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.forventetPensjonsgivendeInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.inntektEtterOpphoer
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.inntektFoerUttak
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.inntektIAfpPerioden
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.medlemAvApotekerordningen
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.toleranseBeloep
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.PesysDataSelectors.uttaksdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
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
 * Redigerbart vedtak — AFP etteroppgjør (SPK), ingen endring innenfor toleransebeløpet.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_108`. Auto-varianten er
 * [VedtakAfpEtteroppgjoerIngenEndringAutoDto] (`PE_AF_04_100`). Innhold er
 * tilnærmet identisk; saksbehandler kan tilpasse teksten ved behov.
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerIngenEndring : RedigerbarTemplate<VedtakAfpEtteroppgjoerIngenEndringDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_ETTEROPPGJOER_INGEN_ENDRING

    override val featureToggle = FeatureToggles.vedtakAfpEtteroppgjoerToleransebeloep.toggle

    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ingen endring (innenfor toleransebeløp) - AFP etteroppgjør",
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
            includePhrase(AfpEtteroppgjoerInnhold.EtteroppgjoerIntro)
            includePhrase(AfpEtteroppgjoerInnhold.IkkeFunnetGrunnlagForAaEndre(pesysData.oppgjoersAar))

            includePhrase(AfpEtteroppgjoerInnhold.InnrapporterteInntektsopplysningerIkkeSkiller)
            includePhrase(AfpEtteroppgjoerInnhold.LaverePgiKanGiHoyereAfp)

            showIf(pesysData.medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }

            includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

            includePhrase(AfpEtteroppgjoerInnhold.InntektUtenforEtteroppgjoerListe)

            includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)
            includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)
            includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(pesysData.oppgjoersAar))
            includePhrase(AfpEtteroppgjoerInnhold.SamletPgiOpplysning(pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, oppgjoersAar = pesysData.oppgjoersAar))

            includePhrase(
                AfpEtteroppgjoerInnhold.InntektFoerUttakInntektEtterOpphoerFordelingPerPeriode(
                    erHelAfpHeleAaret = pesysData.periode.equalTo(Periode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = pesysData.periode.equalTo(Periode.UTTAK_I_AARET),
                    erOpphoerIAaret = pesysData.periode.equalTo(Periode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = pesysData.periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = pesysData.uttaksdato,
                    opphorsdato = pesysData.opphorsdato,
                    oppgjoersAar = pesysData.oppgjoersAar,
                    inntektFoerUttak = pesysData.inntektFoerUttak,
                    inntektEtterOpphoer = pesysData.inntektEtterOpphoer,
                    inntektIAfpPerioden = pesysData.inntektIAfpPerioden,
                ),
            )

            paragraph {
                text(
                    bokmal {
                        +"Ved beregningen av pensjonen din for " + pesysData.oppgjoersAar.format() + " la vi til grunn at du ville ha en forventet arbeidsinntekt på " + pesysData.forventetPensjonsgivendeInntektBeregnet.format() + ". Forskjellen mellom den tidligere benyttede arbeidsinntekten og den arbeidsinntekten du etter vår beregning har hatt i perioden, utgjør " + pesysData.avvik.format() + ". Denne forskjellen er ikke større enn toleransebeløpet som i " + pesysData.oppgjoersAar.format() + " var på " + pesysData.toleranseBeloep.format() + "."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din for " + pesysData.oppgjoersAar.format() + " la vi til grunn at du ville ha ei forventa arbeidsinntekt på " + pesysData.forventetPensjonsgivendeInntektBeregnet.format() + ". Forskjellen mellom den tidlegare nytta arbeidsinntekta og den arbeidsinntekta du etter vår berekning har hatt i perioden, utgjer " + pesysData.avvik.format() + ". Denne forskjellen er ikkje større enn toleransebeløpet som i " + pesysData.oppgjoersAar.format() + " var på " + pesysData.toleranseBeloep.format() + "."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.PensjonsberegningenBlirIkkeEndret(pesysData.oppgjoersAar))

            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageMedDokumentasjonsfrist)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}
