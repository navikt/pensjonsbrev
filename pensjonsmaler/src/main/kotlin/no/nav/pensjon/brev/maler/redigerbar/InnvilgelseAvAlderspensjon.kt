package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BarnetilleggVedVirkSelectors.innvilgetFellesbarn
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BarnetilleggVedVirkSelectors.innvilgetFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BarnetilleggVedVirkSelectors.innvilgetSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BarnetilleggVedVirkSelectors.innvilgetSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.barnetilleggFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.barnetilleggSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.EktefelletilleggVedVirkSelectors.innvilgetEktefelletillegg
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.EktefelletilleggVedVirkSelectors.innvilgetEktefelletillegg_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.barnetilleggVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.ektefelletilleggVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Redigerbar.SaksType
import no.nav.pensjon.brev.maler.vedlegg.vedleggPraktiskInformasjonEtteroppgjoerUfoeretrygd
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV


@TemplateModelHelpers
object InnvilgelseAvAlderspensjon : RedigerbarTemplate<InnvilgelseAvAlderspensjonDto> {
    override val kategori: TemplateDescription.Brevkategori = FOERSTEGANGSBEHANDLING
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGELSE
    override val template = createTemplate(
        name = kode.name,
        letterDataType = InnvilgelseAvAlderspensjonDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val uttaksgrad = pesysData.alderspensjonVedVirk.ifNull(then = (0))
        val regelverkType = pesysData.regelverkType
        val innvilgetEktefelletillegg = pesysData.ektefelletilleggVedVirk.innvilgetEktefelletillegg_safe
        val innvilgetSaerkullsbarn = pesysData.barnetilleggVedVirk.innvilgetSaerkullsbarn_safe
        val innvilgetFellesbarn = pesysData.barnetilleggVedVirk.innvilgetFellesbarn_safe
        val ektefelletillegg = pesysData.beregnetPensjonPerManedVedVirk.ifNull(then = (0))
        val barnetilleggFellesbarn = pesysData.beregnetPensjonPerManedVedVirk.barnetilleggFellesbarn_safe.ifNull(then = (0))
        val barnetilleggSaerkullsbarn = pesysData.beregnetPensjonPerManedVedVirk.barnetilleggSaerkullsbarn_safe.ifNull(then = (0))

        title {
            textExpr(
                Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad + " prosent alderspensjon",
                Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad + " prosent alderspensjon",
                English to "We have granted your application for ".expr() + uttaksgrad + " percent retirement pension",
            )
            includePhrase(SaksType(pesysData.sakstype))
        }

        outline {

            showIf(
                innvilgetEktefelletillegg and ektefelletillegg.notEqualTo(0) and not(innvilgetFellesbarn) and not(
                    innvilgetSaerkullsbarn)
            ) {
                // innvETAP
                paragraph {
                    val navn = fritekst("navn")
                    val beloep = fritekst("beløp")
                    textExpr(
                        Bokmal to "I tillegg til alderspensjonen får du også ektefelletillegg for ".expr() + navn + "." +
                                " Hvis den samlede inntekten din blir høyere enn ".expr() + beloep + " kroner vil vi redusere dette tillegget.",
                        Nynorsk to "I tillegg til alderspensjonen får du også ektefelletillegg for ".expr() + navn + "." +
                                " Dersom den samla inntekta di blir høgare enn ".expr() + beloep + " kroner vil vi redusere dette tillegget.",
                        English to "In addition to retirement pension, you are also entitled to a spouse supplement for ".expr() + navn + "." +
                                " If your total income exceeds NOK ".expr() + beloep + ", this supplement will be reduced."
                    )
                }
            }
            showIf(
                innvilgetFellesbarn and
            )

        }
    }
}

