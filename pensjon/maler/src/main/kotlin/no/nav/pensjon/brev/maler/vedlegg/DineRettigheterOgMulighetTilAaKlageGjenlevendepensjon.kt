package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerFellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.BrevbakerFellesSelectors.NavEnhetSelectors.telefonnummer


val vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon =
    createAttachment<LangBokmalNynorskEnglish, EmptyVedleggData>(
        title = {
            text(
                bokmal { +"Dine rettigheter og mulighet til å klage" },
                nynorsk { +"Rettane dine og høve til å klage" },
                english { +"Your rights and how to appeal" }
            )
        },
        includeSakspart = false,
    ) {
        includePhrase(VedleggVeiledning)
        includePhrase(VedleggInnsynSakGjenlevendepensjon)
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePaaVedtaket(felles.avsenderEnhet.telefonnummer))
    }