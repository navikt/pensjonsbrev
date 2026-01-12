package no.nav.pensjon.brev.alder.maler.felles

import no.nav.pensjon.brev.alder.maler.vedlegg.VedleggHjelpFraAndreStatisk
import no.nav.pensjon.brev.alder.maler.vedlegg.VedleggInnsynSakPensjonStatisk
import no.nav.pensjon.brev.alder.maler.vedlegg.VedleggKlagePaaVedtaketStatisk
import no.nav.pensjon.brev.alder.maler.vedlegg.VedleggVeiledningStatisk
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val dineRettigheterOgMulighetTilAaKlagePensjonStatisk =
    createAttachment<LangBokmalNynorskEnglish, EmptyVedleggData>(
        title = {
            text(
                bokmal { +"Dine rettigheter og mulighet til å klage" },
                nynorsk { +"Rettane dine og høve til å klage" },
                english { +"Your rights and how to appeal" },
            )
        },
        includeSakspart = false,
    ) {
        includePhrase(VedleggVeiledningStatisk)
        includePhrase(VedleggInnsynSakPensjonStatisk)
        includePhrase(VedleggHjelpFraAndreStatisk)
        includePhrase(VedleggKlagePaaVedtaketStatisk)
    }
