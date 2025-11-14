package no.nav.pensjon.brev.alder.maler.felles

import no.nav.pensjon.brev.alder.maler.vedlegg.VedleggHjelpFraAndreStatisk
import no.nav.pensjon.brev.alder.maler.vedlegg.VedleggInnsynSakPensjonStatisk
import no.nav.pensjon.brev.alder.maler.vedlegg.VedleggKlagePaaVedtaketStatisk
import no.nav.pensjon.brev.alder.maler.vedlegg.VedleggVeiledningStatisk
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

@TemplateModelHelpers
val dineRettigheterOgMulighetTilAaKlagePensjonStatisk =
    createAttachment<LangBokmalNynorskEnglish, EmptyVedleggData>(
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
