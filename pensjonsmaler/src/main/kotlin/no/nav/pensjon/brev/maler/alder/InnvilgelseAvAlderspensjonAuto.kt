package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDto
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.innvilgetFor67
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.privatAFPErBrukt
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.InngangOgEksportVurderingSelectors.eksportTrygdeavtaleEOS
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.afpPrivatResultatFellesKontoret
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.harFlereBeregningsperioder
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.inngangOgEksportVurdering
import no.nav.pensjon.brev.api.model.maler.alderApi.InnvilgelseAvAlderspensjonAutoDtoSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.maler.fraser.alderspensjon.AfpPrivatErBrukt
import no.nav.pensjon.brev.maler.fraser.alderspensjon.SoktAFPPrivatInfo
import no.nav.pensjon.brev.maler.fraser.alderspensjon.Utbetalingsinformasjon
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV
import java.util.function.Predicate.not

// MF_000098 / AP_INNV_AUTO, AP_INNVILG_AUTO, AP_INNVILG_UTL_AUTO


@TemplateModelHelpers
object InnvilgelseAvAlderspensjonAuto : AutobrevTemplate<InnvilgelseAvAlderspensjonAutoDto> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_AP_INNVILGELSE_AUTO

    override val template = createTemplate(
        name = InfoAldersovergang67AarAuto.kode.name,
        letterDataType = InnvilgelseAvAlderspensjonAutoDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = VEDTAKSBREV,
        ),
    ) {

        title {
            textExpr(
                Bokmal to "Vi har innvilget søknaden din om ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon",
                Nynorsk to "Vi har innvilga søknaden din om ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon",
                English to "We have granted your application for ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " percent retirement pension"
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)

            paragraph {
                textExpr(
                    Bokmal to "Du får ".expr() + alderspensjonVedVirk.totalPensjon.format() + " kroner hver måned før skatt fra ".expr() + kravVirkDatoFom.format(),
                    Nynorsk to "Du får ".expr() + alderspensjonVedVirk.totalPensjon.format() + " kroner kvar månad før skatt frå ".expr() + kravVirkDatoFom.format(),
                    English to "You will receive NOK ".expr() + alderspensjonVedVirk.totalPensjon.format() + " every month before tax from ".expr() + kravVirkDatoFom.format()
                )
                showIf(alderspensjonVedVirk.uforeKombinertMedAlder and alderspensjonVedVirk.innvilgetFor67) {
                    // innvilgelseAPogUTInnledn -> Hvis løpende uføretrygd
                    text(
                        Bokmal to ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                        Nynorsk to ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                        English to ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit."
                    )
                }.orShow {
                    // innvilgelseAPInnledn -> Ingen løpende uføretrygd og ingen gjenlevendetillegg
                    text(
                        Bokmal to " i alderspensjon fra folketrygden.",
                        Nynorsk to " i alderspensjon frå folketrygda.",
                        English to " as retirement pension from the National Insurance Scheme."
                    )
                }
            }

            showIf(alderspensjonVedVirk.privatAFPErBrukt) { includePhrase(AfpPrivatErBrukt(alderspensjonVedVirk.uttaksgrad)) }

            showIf(afpPrivatResultatFellesKontoret.ifNull(then = false)) { includePhrase(SoktAFPPrivatInfo) }

            includePhrase(Utbetalingsinformasjon)

            showIf(harFlereBeregningsperioder and alderspensjonVedVirk.totalPensjon.greaterThan(0)) {
                includePhrase(Felles.FlereBeregningsperioder)
            }

            showIf(alderspensjonVedVirk.uttaksgrad.lessThan(100) and not(alderspensjonVedVirk.uforeKombinertMedAlder)) {
                paragraph {
                    text(
                        Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut mer alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.",
                        Nynorsk to "Du må sende oss ein ny søknad når du ønskjer å ta ut meir alderspensjon. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden.",
                        English to "You have to submit an application when you want to increase your retirement pension. Any change will be implemented at the earliest the month after we have received the application."
                    )
                }
            }

            showIf(inngangOgEksportVurdering.eksportTrygdeavtaleEOS) {
                // hvisFlyttetBosattEØS
                
                }
            }



            // hvisFlyttetBosattAvtaleland

            // eksportAPunder20aar

        }
    }
}