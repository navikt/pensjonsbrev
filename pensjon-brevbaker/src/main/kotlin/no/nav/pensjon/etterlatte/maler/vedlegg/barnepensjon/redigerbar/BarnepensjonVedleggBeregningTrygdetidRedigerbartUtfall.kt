package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.redigerbar

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.Vedlegg
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles

@TemplateModelHelpers
object BarnepensjonVedleggBeregningTrygdetidRedigerbartUtfall : EtterlatteTemplate<ManueltBrevDTO>, Vedlegg {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VEDLEGG_BEREGNING_TRYGDETID_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ManueltBrevDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Beregning av trygdetid",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "",
                Language.Nynorsk to "",
                Language.English to "",
            )
        }
        outline {
            includePhrase(Felles.BlankTekst)
        }
    }
}