package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.redigerbar

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
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
object OmstillingsstoenadVedleggBeregningRedigerbartUtfall : EtterlatteTemplate<ManueltBrevDTO>, Vedlegg {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_VEDLEGG_BEREGNING_UTFALL

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Utfall beregning",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"" },
                nynorsk { +"" },
                english { +"" },
            )
        }
        outline {
            includePhrase(Felles.BlankTekst)
        }
    }


}