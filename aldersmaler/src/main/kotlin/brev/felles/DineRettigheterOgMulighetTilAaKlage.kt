import brev.vedlegg.VedleggHjelpFraAndreStatisk
import brev.vedlegg.VedleggInnsynSakPensjonStatisk
import brev.vedlegg.VedleggKlagePaaVedtaketStatisk
import brev.vedlegg.VedleggVeiledningStatisk
import no.nav.pensjon.brev.api.model.maler.EmptyVedlegg
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

@TemplateModelHelpers
val dineRettigheterOgMulighetTilAaKlagePensjonStatisk =
    createAttachment<LangBokmalNynorskEnglish, EmptyVedlegg>(
        title = newText(
            Bokmal to "Dine rettigheter og mulighet til å klage",
            Nynorsk to "Rettane dine og høve til å klage",
            English to "Your rights and how to appeal"
        ),
        includeSakspart = false,
    ) {
        includePhrase(VedleggVeiledningStatisk)
        includePhrase(VedleggInnsynSakPensjonStatisk)
        includePhrase(VedleggHjelpFraAndreStatisk)
        includePhrase(VedleggKlagePaaVedtaketStatisk)
    }
