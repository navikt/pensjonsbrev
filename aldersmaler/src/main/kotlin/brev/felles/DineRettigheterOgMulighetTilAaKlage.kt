import brev.vedlegg.VedleggHjelpFraAndre
import brev.vedlegg.VedleggInnsynSakPensjon
import brev.vedlegg.VedleggKlagePaaVedtaket
import brev.vedlegg.VedleggVeiledning
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

@TemplateModelHelpers
val dineRettigheterOgMulighetTilAaKlagePensjonStatisk =
    createAttachment<LangBokmalNynorskEnglish, Unit>(
        title = newText(
            Bokmal to "Dine rettigheter og mulighet til å klage",
            Nynorsk to "Rettane dine og høve til å klage",
            English to "Your rights and how to appeal"
        ),
        includeSakspart = false,
    ) {
        includePhrase(VedleggVeiledning)
        includePhrase(VedleggInnsynSakPensjon)
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePaaVedtaket)
    }
