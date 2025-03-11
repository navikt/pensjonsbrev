package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata.saksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.SaksbehandlerValgSelectors.revurderingAvRett
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.SaksbehandlerValgSelectors.revurderingReduksjon
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselRevurderingAvPensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselRevurderingAvPensjon : RedigerbarTemplate<VarselRevurderingAvPensjonDto> {

    // MF_000156
    override val kode = Pesysbrevkoder.Redigerbar.PE_VARSEL_REVURDERING_AV_PENSJON
    override val kategori = TemplateDescription.Brevkategori.VARSEL
    override val brevkontekst = TemplateDescription.Brevkontekst.ALLE
    override val sakstyper = Sakstype.pensjon

    override val template = createTemplate(
        name = kode.name,
        letterDataType = VarselRevurderingAvPensjonDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel om revurdering av pensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {

        title {
            showIf(saksbehandlerValg.revurderingAvRett) {
                text(
                    Bokmal to "Vi vurderer om du forsatt har rett til <sakTypeKode>",
                    Nynorsk to "",
                    English to "",
                )
            }
            showIf(saksbehandlerValg.revurderingReduksjon) {
                text(
                    Bokmal to "Vi vurderer om pensjonen din skal reduseres",
                    Nynorsk to "",
                    English to "",
                )
            }
        }

        outline {
            paragraph {


            }
        }
    }
}
