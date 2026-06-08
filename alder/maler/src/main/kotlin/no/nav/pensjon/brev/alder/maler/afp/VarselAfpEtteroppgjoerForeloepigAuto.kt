package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerVarselFraser
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpTilbakekrevingBody
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDto
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDto.Periode
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.formyebetalt
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.forventetInntekt
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.fradragBeregnetArbeidsInntekt
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.fullAfp
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.inntektEtterOpphoer
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.inntektFoerUttak
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.inntektIAfpPerioden
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.korrigertAfp
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.tidligereArbeidsInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.utbetaltAfp
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigAutoDtoSelectors.uttaksdato
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
 * Varsel — foreløpig beregning av AFP-etteroppgjør med mulig tilbakekreving (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_03_100` (Vedtak_afp_EO_fase1). Brevet er
 * fase 1 i etteroppgjøret (offentlig sektor / Statens pensjonskasse): bruker
 * varsles om at den foreløpige beregningen viser for mye utbetalt AFP, og får
 * fire ukers frist til å legge fram nye inntektsopplysninger før det endelige
 * vedtaket fattes — se [VedtakAfpEtteroppgjoerTilbakekrevingAuto] (`PE_AF_04_107`).
 *
 * Det meste av innholdet gjenbrukes fra PE_AF_04-familien gjennom
 * [AfpEtteroppgjoerInnhold] og [AfpTilbakekrevingBody]; de varsel-spesifikke
 * paragrafene ligger i [AfpEtteroppgjoerVarselFraser].
 */
@TemplateModelHelpers
object VarselAfpEtteroppgjoerForeloepigAuto : AutobrevTemplate<VarselAfpEtteroppgjoerForeloepigAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_VARSEL_FORELOEPIG

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - tilbakekreving av for mye utbetalt pensjon - AFP etteroppgjør",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                bokmal {
                    +"Varsel - Foreløpig beregning av etteroppgjøret for avtalefestet pensjon (AFP) for " +
                            oppgjoersAar.format()
                },
                nynorsk {
                    +"Varsel - Førebels berekning av etteroppgjeret for avtalefesta pensjon (AFP) for " +
                            oppgjoersAar.format()
                },
            )
        }

        outline {
            includePhrase(AfpEtteroppgjoerInnhold.EtteroppgjoerIntro)

            includePhrase(
                AfpEtteroppgjoerVarselFraser.ForeloepigBeregningForMye(
                    erHelAfpHeleAaret = periode.equalTo(Periode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = periode.equalTo(Periode.UTTAK_I_AARET),
                    erOpphoerIAaret = periode.equalTo(Periode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = uttaksdato,
                    opphorsdato = opphorsdato,
                    oppgjoersAar = oppgjoersAar,
                    formyebetalt = formyebetalt,
                ),
            )

            includePhrase(AfpEtteroppgjoerInnhold.InnrapporterteInntektsopplysningerIkkeSkiller)
            includePhrase(AfpEtteroppgjoerInnhold.LaverePgiKanGiHoyereAfp)

            includePhrase(AfpEtteroppgjoerVarselFraser.HjemmelForeloepig)

            includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)
            includePhrase(AfpEtteroppgjoerInnhold.InntektUtenforEtteroppgjoerListe)
            includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)
            includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)
            includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmCovidInntekterInnledning)
            includePhrase(AfpEtteroppgjoerInnhold.CovidDokumentasjonskravInntekter)
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmUkrainaUnntak)

            includePhrase(AfpEtteroppgjoerVarselFraser.VedtakOmEndeligResultatSenere)

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))
            includePhrase(
                AfpEtteroppgjoerInnhold.SamletPgiOpplysning(
                    pensjonsgivendeInntekt = pensjonsgivendeInntekt,
                    oppgjoersAar = oppgjoersAar,
                ),
            )

            includePhrase(
                AfpEtteroppgjoerInnhold.InntektFoerUttakInntektEtterOpphoerFordelingPerPeriode(
                    erHelAfpHeleAaret = periode.equalTo(Periode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = periode.equalTo(Periode.UTTAK_I_AARET),
                    erOpphoerIAaret = periode.equalTo(Periode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = uttaksdato,
                    opphorsdato = opphorsdato,
                    oppgjoersAar = oppgjoersAar,
                    inntektFoerUttak = inntektFoerUttak,
                    inntektEtterOpphoer = inntektEtterOpphoer,
                    inntektIAfpPerioden = inntektIAfpPerioden,
                ),
            )

            includePhrase(
                AfpEtteroppgjoerVarselFraser.ToleransebeloepForeloepig(
                    forventetInntekt = forventetInntekt,
                    oppgjoersAar = oppgjoersAar,
                ),
            )

            includePhrase(
                AfpTilbakekrevingBody.NyPensjonsberegningEquation(
                    fullAfp = fullAfp,
                    fradragBeregnetArbeidsInntekt = fradragBeregnetArbeidsInntekt,
                    korrigertAfp = korrigertAfp,
                ),
            )
            includePhrase(
                AfpTilbakekrevingBody.InntektsfradragetFormel(
                    fradragBeregnetArbeidsInntekt = fradragBeregnetArbeidsInntekt,
                    inntektIAfpPerioden = inntektIAfpPerioden,
                    tidligereArbeidsInntektBeregnet = tidligereArbeidsInntektBeregnet,
                    fullAfp = fullAfp,
                ),
            )
            includePhrase(
                AfpTilbakekrevingBody.AfpForMyeEquation(
                    utbetaltAfp = utbetaltAfp,
                    korrigertAfp = korrigertAfp,
                    formyebetalt = formyebetalt,
                ),
            )

            includePhrase(AfpEtteroppgjoerVarselFraser.NaarFristenHarGaattUt(oppgjoersAar))
            includePhrase(AfpEtteroppgjoerVarselFraser.EktefelletilleggForbehold)

            includePhrase(AfpEtteroppgjoerVarselFraser.DineRettigheterOgPlikterInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
            includePhrase(AfpEtteroppgjoerVarselFraser.MeldepliktArbeidsinntekt)
        }
    }
}
