package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.EndringAvUttaksgradAutoDtoSelectors.virkDatoFom
import no.nav.pensjon.brev.maler.fraser.alderspensjon.UfoereAlder
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
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
                    bokmal { +"Vi har innvilget søknaden din om ".expr() + alderspensjonVedVirk.uttaksgrad + " prosent alderspensjon." },
                    nynorsk { +"Vi har innvilga søknaden din om ".expr() + alderspensjonVedVirk.uttaksgrad + " prosent alderspensjon." },
                    english { +"We have granted your application for ".expr() + alderspensjonVedVirk.uttaksgrad + " percent retirement pension." }
                )
            }
            outline {
                includePhrase(Vedtak.Overskrift)

                showIf(alderspensjonVedVirk.uforeKombinertMedAlder) {
                    // innvilgelseAPogUTInnledn
                    includePhrase(UfoereAlder.DuFaar(alderspensjonVedVirk.totalPensjon, virkDatoFom))
                }   .orShow {
                    includePhrase()
                }
            }
        }
}