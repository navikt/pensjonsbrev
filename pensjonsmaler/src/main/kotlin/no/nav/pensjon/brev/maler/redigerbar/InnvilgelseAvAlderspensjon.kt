package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.maler.fraser.common.Redigerbar.SaksType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
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
        val uttaksgrad = pesysData.alderspensjonVedVirk
        val regelverkType = pesysData.regelverkType

        title{
            textExpr(
            Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad + " prosent alderspensjon",
            Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad + " prosent alderspensjon",
            English to "We have granted your application for ".expr() + uttaksgrad + " percent retirement pension",
            )
            includePhrase(SaksType(pesysData.sakstype))
        }
        outline {
            paragraph {
                textExpr(
                )

    }

}

