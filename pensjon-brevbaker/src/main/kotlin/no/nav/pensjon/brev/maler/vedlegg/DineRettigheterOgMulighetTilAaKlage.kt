package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDtoSelectors.brukerUnder18Ar
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDtoSelectors.saktype
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

// Conditional for showing the attachment is: vedtakResultat = AVSLG
@TemplateModelHelpers
val dineRettigheterOgMulighetTilAaKlage = createAttachment<LangBokmalNynorskEnglish, DineRettigheterOgMulighetTilAaKlageDto>(
    title = newText(
        Bokmal to "Dine rettigheter og mulighet til å klage",
        Nynorsk to "Rettane dine og høve til å klage",
        English to "Your rights and how to appeal"
    ),
    includeSakspart = false,
) {
    includePhrase(VedleggVeiledning_001)

    showIf(saktype.isNotAnyOf(Sakstype.UFOEREP)) {
        includePhrase(VedleggInnsynSakPensjon_001)
        includePhrase(VedleggInnsynSakUTPesys_001)
    }

    showIf(saktype.isOneOf(Sakstype.BARNEP) and brukerUnder18Ar) {
        includePhrase(VedleggInnsynSakUnder18_001)
    }

    includePhrase(VedleggHjelpFraAndre_001)
    includePhrase(VedleggKlagePensjon_001)
}