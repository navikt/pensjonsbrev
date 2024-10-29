package no.nav.pensjon.brev.maler.adhoc.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.newText

val vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk =
    createAttachment<LangBokmalNynorskEnglish, EmptyBrevdata>(
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
        includePhrase(VedleggKlagePensjon)
    }
