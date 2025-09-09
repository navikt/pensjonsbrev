package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.privatAFPerBrukt
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.harFlereBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AfpPrivatErBrukt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.FlereBeregningsperioder
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV

/* MF_000099 / AP_ENDR_GRAD_AUTO: Brevet produseres ved innvilget søknad om endring av uttaksgrad i selvbetjeningsløsningen.
Malen har 2 deler:
Endring i uttaksgrad -> når bruker endrer uttaksgrad til en uttaksgrad større enn null
Stans av alderspensjon -> når bruker endrer uttaksgrad til null */

@TemplateModelHelpers
object EndringUttaksgradAuto : AutobrevTemplate<EndringAvUttaksgradAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_ENDRING_UTTAKSGRAD_AUTO
    override val template =
        createTemplate(
            name = InfoAldersovergang67AarAuto.kode.name,
            letterDataType = EndringAvUttaksgradAutoDto::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - endring av uttaksgrad (auto)",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = VEDTAKSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Vi har innvilget søknaden din om ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon." },
                    nynorsk { +"Vi har innvilga søknaden din om ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon." },
                    english { +"We have granted your application for ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " percent retirement pension." }
                )
            }
            outline {
                includePhrase(Vedtak.Overskrift)

                paragraph {
                    text(
                        bokmal { +"Du får ".expr() + alderspensjonVedVirk.totalPensjon.format() + " hver måned før skatt fra ".expr() + kravVirkDatoFom.format() },
                        nynorsk { +"Du får ".expr() + alderspensjonVedVirk.totalPensjon.format() + " kvar månad før skatt frå ".expr() + kravVirkDatoFom.format() },
                        english { +"You will receive ".expr() + alderspensjonVedVirk.totalPensjon.format() + " every month before tax from ".expr() + kravVirkDatoFom.format() }
                    )
                    showIf(alderspensjonVedVirk.uforeKombinertMedAlder) {
                        // innvilgelseAPogUTInnledn -> Hvis løpende uføretrygd
                        text(
                            bokmal { +". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din." },
                            nynorsk { +". Du får alderspensjon frå folketrygda ved sida av uføretrygda di." },
                            english { +". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit." }
                        )
                    }.orShow {
                        // innvilgelseAPInnledn -> Ingen løpende uføretrygd
                        text(
                            bokmal { +" i alderspensjon fra folketrygden." },
                            nynorsk { +" i alderspensjon frå folketrygda." },
                            english { +" as retirement pension from the National Insurance Scheme." }
                        )
                    }
                }

                showIf(alderspensjonVedVirk.privatAFPerBrukt) {
                    includePhrase(AfpPrivatErBrukt(uttaksgrad = alderspensjonVedVirk.uttaksgrad))
                }

                includePhrase(Utbetalingsinformasjon)

                showIf(harFlereBeregningsperioder) {
                                     includePhrase(FlereBeregningsperioder)
                }
            }
        }
}