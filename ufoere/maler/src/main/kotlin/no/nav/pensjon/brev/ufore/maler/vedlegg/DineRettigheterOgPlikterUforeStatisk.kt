package no.nav.pensjon.brev.ufore.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevFellesSelectors.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.BrevFellesSelectors.NavEnhetSelectors.telefonnummer

val vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk =
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
        includePhrase(VedleggInnsynSakUfoeretrygdPesys)
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePaaVedtaket(felles.avsenderEnhet.telefonnummer))
    }
