package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

//  Copy of internal attachment to MF-000154
// Conditional for showing the attachment is: Mandatory showing with adhoc letter UT_2023_INFO_REGLERENDRET_GJT_12_18
@TemplateModelHelpers
val dineRettigheterOgMulighetTilAaKlageAdhoc =
    createAttachment<LangBokmalNynorskEnglish, Unit>(
        title = newText(
            Bokmal to "Dine rettigheter og mulighet til å klage",
            Nynorsk to "Rettane dine og høve til å klage",
            English to "Your rights and how to appeal"
        ),
        includeSakspart = false,
    ) {
        includePhrase(VedleggVeiledning)
        includePhrase(VedleggInnsynSakUfoeretrygdPesys)
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePesys)
    }