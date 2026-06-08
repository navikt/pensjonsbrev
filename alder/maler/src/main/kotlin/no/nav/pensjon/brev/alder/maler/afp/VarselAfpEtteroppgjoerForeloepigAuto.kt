package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerVarselForeloepigInnhold
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
 * Hele brødteksten deles med den redigerbare varianten
 * [VarselAfpEtteroppgjoerForeloepig] (`PE_AF_03_101`) gjennom den felles frasen
 * [AfpEtteroppgjoerVarselForeloepigInnhold].
 */
@TemplateModelHelpers
object VarselAfpEtteroppgjoerForeloepigAuto : AutobrevTemplate<VarselAfpEtteroppgjoerForeloepigAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_VARSEL_FORELOEPIG_AUTO

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
            includePhrase(
                AfpEtteroppgjoerVarselForeloepigInnhold(
                    erHelAfpHeleAaret = periode.equalTo(Periode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = periode.equalTo(Periode.UTTAK_I_AARET),
                    erOpphoerIAaret = periode.equalTo(Periode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = uttaksdato,
                    opphorsdato = opphorsdato,
                    oppgjoersAar = oppgjoersAar,
                    formyebetalt = formyebetalt,
                    pensjonsgivendeInntekt = pensjonsgivendeInntekt,
                    inntektFoerUttak = inntektFoerUttak,
                    inntektEtterOpphoer = inntektEtterOpphoer,
                    inntektIAfpPerioden = inntektIAfpPerioden,
                    forventetInntekt = forventetInntekt,
                    fullAfp = fullAfp,
                    fradragBeregnetArbeidsInntekt = fradragBeregnetArbeidsInntekt,
                    korrigertAfp = korrigertAfp,
                    tidligereArbeidsInntektBeregnet = tidligereArbeidsInntektBeregnet,
                    utbetaltAfp = utbetaltAfp,
                ),
            )
        }
    }
}
