package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.avdoed_sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.bruker_borINorge
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.institusjon_gjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.ufoeretrygdPerMaaned_barnetilleggGjeldende
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText

// Conditional for showing the attachment is: sakstype = UFOEREP && vedtakResultat = AVSLG

val vedleggOrienteringOmRettigheterOgPlikterUfoere = createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterUfoereDto>(
    title = newText(
        Bokmal to "Dine rettigheter og plikter",
        Nynorsk to "Dine rettar og plikter",
        English to "Your rights and obligations"
    ),
    includeSakspart = false,
) {
    includePhrase(VedleggPlikterUT_001)

    list {
        item { includePhrase(vedleggPlikterUT1_001) }
        item { includePhrase(vedleggPlikterUT2_001) }

        showIf(
            bruker_borINorge
                    and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
        ) {
            item { includePhrase(vedleggPlikterUT3_001) }
            item { includePhrase(vedleggPlikterUT4_001) }
        }

        item { includePhrase(vedleggPlikterUT5_001) }

        showIf(avdoed_sivilstand.isOneOf(ENSLIG, ENKE)) {
            item { includePhrase(vedleggPlikterUT6_001) }
        }

        ifNotNull(ufoeretrygdPerMaaned_barnetilleggGjeldende) { tillegg ->
            showIf(tillegg.greaterThan(0)) {
                item { includePhrase(vedleggPlikterUT7_001) }
            }
        }

        item { includePhrase(vedleggPlikterUT8_001) }
        item { includePhrase(vedleggPlikterUT9_001) }
        item { includePhrase(vedleggPlikterUT10_001) }
        item { includePhrase(vedleggPlikterUT11_001) }
        item { includePhrase(vedleggPlikterUT12_001) }
    }

    includePhrase(VedleggVeiledning_001)
    includePhrase(VedleggInnsynSakUTPesys_001)
    includePhrase(VedleggHjelpFraAndre_001)
    includePhrase(VedleggKlagePensjon_001)
}
