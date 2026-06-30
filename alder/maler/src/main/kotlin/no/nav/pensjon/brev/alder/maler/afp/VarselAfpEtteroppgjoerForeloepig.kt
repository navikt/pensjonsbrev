package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerVarselForeloepigInnhold
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.AfpPeriode
import no.nav.pensjon.brev.alder.model.afp.VarselAfpEtteroppgjoerForeloepigDto
import no.nav.pensjon.brev.alder.model.afp.selectors.varselAfpEtteroppgjoerForeloepigDto.pesysData.*
import no.nav.pensjon.brev.alder.model.afp.selectors.varselAfpEtteroppgjoerForeloepigDto.*
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
 * Varsel — foreløpig beregning av AFP-etteroppgjør med mulig tilbakekreving (redigerbar).
 *
 * Konvertert fra Exstream-malen `PE_AF_03_101` (Vedtak_afp_EO_fase1). Brevet er
 * den redigerbare tvillingen til autobrevet [VarselAfpEtteroppgjoerForeloepigAuto]
 * (`PE_AF_03_100`): innholdet er identisk, men saksbehandler kan redigere teksten
 * i Skribenten. Hele brødteksten deles med autobrevet gjennom den felles frasen
 * [AfpEtteroppgjoerVarselForeloepigInnhold].
 */
@TemplateModelHelpers
object VarselAfpEtteroppgjoerForeloepig : RedigerbarTemplate<VarselAfpEtteroppgjoerForeloepigDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_ETTEROPPGJOER_VARSEL_FORELOEPIG

    override val featureToggle = FeatureToggles.varselAfpEtteroppgjoerForeloepig.toggle

    override val kategori = Brevkategori.ETTEROPPGJOER

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP)

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
                            pesysData.oppgjoersAar.format()
                },
                nynorsk {
                    +"Varsel - Førebels berekning av etteroppgjeret for avtalefesta pensjon (AFP) for " +
                            pesysData.oppgjoersAar.format()
                },
            )
        }

        outline {
            includePhrase(
                AfpEtteroppgjoerVarselForeloepigInnhold(
                    erHelAfpHeleAaret = pesysData.periode.equalTo(AfpPeriode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = pesysData.periode.equalTo(AfpPeriode.UTTAK_I_AARET),
                    erOpphoerIAaret = pesysData.periode.equalTo(AfpPeriode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = pesysData.periode.equalTo(AfpPeriode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = pesysData.uttaksdato,
                    opphorsdato = pesysData.opphorsdato,
                    oppgjoersAar = pesysData.oppgjoersAar,
                    formyebetalt = pesysData.formyebetalt,
                    pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt,
                    inntektFoerUttak = pesysData.inntektFoerUttak,
                    inntektEtterOpphoer = pesysData.inntektEtterOpphoer,
                    inntektIAfpPerioden = pesysData.inntektIAfpPerioden,
                    forventetInntekt = pesysData.forventetInntekt,
                    fullAfp = pesysData.fullAfp,
                    fradragBeregnetArbeidsInntekt = pesysData.fradragBeregnetArbeidsInntekt,
                    korrigertAfp = pesysData.korrigertAfp,
                    tidligereArbeidsInntektBeregnet = pesysData.tidligereArbeidsInntektBeregnet,
                    utbetaltAfp = pesysData.utbetaltAfp,
                    toleranseBeloep = pesysData.toleranseBeloep,
                ),
            )
        }
    }
}
