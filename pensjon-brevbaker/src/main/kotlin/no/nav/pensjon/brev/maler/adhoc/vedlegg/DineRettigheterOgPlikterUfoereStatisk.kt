package no.nav.pensjon.brev.maler.adhoc.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.newText

val vedleggDineRettigheterOgPlikterUfoereStatisk =
    createAttachment<LangBokmalNynorskEnglish, EmptyBrevdata>(
        title = newText(
            Bokmal to "Dine rettigheter og plikter",
            Nynorsk to "Rettane og pliktene dine",
            English to "Your rights and obligations"
        ),
        includeSakspart = false,
    ) {
        includePhrase(VedleggPlikter)

        paragraph {
            list {
                item { includePhrase(VedleggPlikterUT1) }
                item { includePhrase(VedleggPlikterUT2) }
                item { includePhrase(VedleggPlikterUT3) }
                item { includePhrase(VedleggPlikterUT4) }
                item { includePhrase(VedleggPlikterUT5) }
                item { includePhrase(VedleggPlikterEndretSivilstatus)}
                item { includePhrase(VedleggPlikterUT8) }
                item { includePhrase(VedleggPlikterUT9) }
                item { includePhrase(VedleggPlikterUT10) }
                item { includePhrase(VedleggPlikterUT11) }
                item { includePhrase(VedleggPlikterUT12) }
            }
        }
        includePhrase(VedleggVeiledning)
        includePhrase(VedleggInnsynSakUfoeretrygdPesys)
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePensjon)
    }
