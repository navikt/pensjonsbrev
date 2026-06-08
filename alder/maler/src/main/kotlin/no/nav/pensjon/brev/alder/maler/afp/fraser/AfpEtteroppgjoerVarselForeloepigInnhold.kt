package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

/**
 * Hele brødteksten i varselet «Foreløpig beregning av AFP-etteroppgjør» (fase 1,
 * offentlig sektor / Statens pensjonskasse), delt mellom autobrevet
 * [no.nav.pensjon.brev.alder.maler.afp.VarselAfpEtteroppgjoerForeloepigAuto]
 * (`PE_AF_03_100`) og den redigerbare varianten
 * [no.nav.pensjon.brev.alder.maler.afp.VarselAfpEtteroppgjoerForeloepig]
 * (`PE_AF_03_101`). De to Exstream-malene har identisk innhold; eneste forskjell
 * er at 101 kan redigeres av saksbehandler i Skribenten.
 *
 * Periodevarianten uttrykkes som fire gjensidig utelukkende boolske flagg
 * ([erHelAfpHeleAaret], [erUttakIAaret], [erOpphoerIAaret],
 * [erUttakOgOpphoerIAaret]) slik at frasen kan gjenbrukes på tvers av de to
 * malenes egne `Periode`-enum-typer.
 */
data class AfpEtteroppgjoerVarselForeloepigInnhold(
    val erHelAfpHeleAaret: Expression<Boolean>,
    val erUttakIAaret: Expression<Boolean>,
    val erOpphoerIAaret: Expression<Boolean>,
    val erUttakOgOpphoerIAaret: Expression<Boolean>,
    val uttaksdato: Expression<LocalDate>,
    val opphorsdato: Expression<LocalDate?>,
    val oppgjoersAar: Expression<Year>,
    val formyebetalt: Expression<Kroner>,
    val pensjonsgivendeInntekt: Expression<Kroner>,
    val inntektFoerUttak: Expression<Kroner>,
    val inntektEtterOpphoer: Expression<Kroner>,
    val inntektIAfpPerioden: Expression<Kroner>,
    val forventetInntekt: Expression<Kroner>,
    val fullAfp: Expression<Kroner>,
    val fradragBeregnetArbeidsInntekt: Expression<Kroner>,
    val korrigertAfp: Expression<Kroner>,
    val tidligereArbeidsInntektBeregnet: Expression<Kroner>,
    val utbetaltAfp: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        includePhrase(AfpEtteroppgjoerInnhold.EtteroppgjoerIntro)

        includePhrase(
            AfpEtteroppgjoerVarselFraser.ForeloepigBeregningForMye(
                erHelAfpHeleAaret = erHelAfpHeleAaret,
                erUttakIAaret = erUttakIAaret,
                erOpphoerIAaret = erOpphoerIAaret,
                erUttakOgOpphoerIAaret = erUttakOgOpphoerIAaret,
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
                erHelAfpHeleAaret = erHelAfpHeleAaret,
                erUttakIAaret = erUttakIAaret,
                erOpphoerIAaret = erOpphoerIAaret,
                erUttakOgOpphoerIAaret = erUttakOgOpphoerIAaret,
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
