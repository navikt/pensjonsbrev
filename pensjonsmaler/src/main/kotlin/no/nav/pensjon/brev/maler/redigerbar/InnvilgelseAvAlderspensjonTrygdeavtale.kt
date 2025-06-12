package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erMellombehandling
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.erSluttbehandlingNorgeUtland
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.innvilgelseAPellerOektUttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.SaksbehandlerValgSelectors.nyBeregningAvInnvilgetAP
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonTrygdeavtaleDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Redigerbar.SaksType
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

// MF_000097 / AP_INNV_AVT_MAN
@TemplateModelHelpers
object InnvilgelseAvAlderspensjonTrygdeavtale : RedigerbarTemplate<InnvilgelseAvAlderspensjonTrygdeavtaleDto> {
    override val kategori: TemplateDescription.Brevkategori = FOERSTEGANGSBEHANDLING
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGELSE_TRYGDEAVTALE
    override val template = createTemplate(
        name = kode.name,
        letterDataType = InnvilgelseAvAlderspensjonTrygdeavtaleDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon (trygdeavtale)",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad
        val erMellombehandling = pesysData.erMellombehandling
        val erSluttbehandlingNorgeUtland = pesysData.erSluttbehandlingNorgeUtland
        val kravVirkDatoFom = pesysData.kravVirkDatoFom.format()

        title {
            showIf(erMellombehandling) {
                textExpr(
                    Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    English to "We have granted your application for ".expr() + uttaksgrad.format() + " percent retirement pension"
                )
            }.orShowIf(saksbehandlerValg.nyBeregningAvInnvilgetAP and erSluttbehandlingNorgeUtland or (not(erSluttbehandlingNorgeUtland) and not(erMellombehandling))) {
                textExpr(
                    Bokmal to "Vi har beregnet alderspensjonen din på nytt fra ".expr() + kravVirkDatoFom,
                    Nynorsk to "Vi har berekna alderspensjonen din på nytt frå ".expr() + kravVirkDatoFom,
                    English to "We have recalculated your retirement pension from ".expr() + kravVirkDatoFom
                )
            }.orShowIf(saksbehandlerValg.innvilgelseAPellerOektUttaksgrad and erSluttbehandlingNorgeUtland or (not(erSluttbehandlingNorgeUtland) and not(erMellombehandling))) {
                textExpr(
                    Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad.format() + " prosent alderspensjon",
                    English to "We have granted your application for ".expr() + uttaksgrad.format() + " percent retirement pension"
                )
            }
            includePhrase(SaksType(pesysData.sakstype))
        }

        outline {
        }
    }
}