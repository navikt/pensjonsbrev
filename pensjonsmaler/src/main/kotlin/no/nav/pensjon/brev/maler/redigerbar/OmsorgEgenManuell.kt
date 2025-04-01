package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDtoSelectors.SaksbehandlerValgSelectors.aarEgenerklaringOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDtoSelectors.SaksbehandlerValgSelectors.aarInnvilgetOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.OmsorgEgenerklaeringOutline
import no.nav.pensjon.brev.maler.fraser.OmsorgEgenerklaeringTittel
import no.nav.pensjon.brev.maler.vedlegg.egenerklaeringPleieOgOmsorgsarbeidManuell
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object OmsorgEgenManuell : RedigerbarTemplate<OmsorgEgenManuellDto> {

    override val kode = Pesysbrevkoder.Redigerbar.PE_OMSORG_EGEN_MANUELL

    override val kategori = TemplateDescription.Brevkategori.BREV_MED_SKJEMA
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.OMSORG)
    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmsorgEgenManuellDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Innhenting av egenerklæring om pleie- og omsorgsarbeid (omsorgsopptjening)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            includePhrase(OmsorgEgenerklaeringTittel)
        }
        outline {
            includePhrase(
                OmsorgEgenerklaeringOutline(
                    aarEgenerklaringOmsorgspoeng = saksbehandlerValg.aarEgenerklaringOmsorgspoeng.format(),
                    aarInnvilgetOmsorgspoeng = saksbehandlerValg.aarInnvilgetOmsorgspoeng.format(),
                )
            )
        }
        includeAttachment(egenerklaeringPleieOgOmsorgsarbeidManuell, argument)
    }
}