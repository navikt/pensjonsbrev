package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText

// Conditional for showing the attachment is: sakstype = UFOEREP && vedtakResultat = AVSLG

val orienteringOmRettigheterOgPlikterUfoere = createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterUfoereDto>(
    title = newText(
        Bokmal to "Dine rettigheter og plikter",
        Nynorsk to "Dine rettar og plikter",
        English to "Your rights and obligations"
    ),
    includeSakspart = false,
) {
    val bor_i_norge = argument().select(OrienteringOmRettigheterUfoereDto::bruker_borINorge)
    val institusjon_gjeldende = argument().select(OrienteringOmRettigheterUfoereDto::institusjon_gjeldende)
    val sivilstand = argument().select(OrienteringOmRettigheterUfoereDto::avdoed_sivilstand)
    val barnetilleggSaerkullsbarn =
        argument().select(OrienteringOmRettigheterUfoereDto::ufoeretrygdPerMaaned_barnetilleggGjeldende)

    includePhrase(vedleggPlikterUT_001)

    list {
        item { includePhrase(vedleggPlikterUT1_001) }
        item { includePhrase(vedleggPlikterUT2_001) }

        showIf(
            bor_i_norge
                and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
        ) {
            item { includePhrase(vedleggPlikterUT3_001) }
            item { includePhrase(vedleggPlikterUT4_001) }
        }

        item { includePhrase(vedleggPlikterUT5_001) }

        showIf(sivilstand.isOneOf(ENSLIG, ENKE)) {
            item { includePhrase(vedleggPlikterUT6_001) }
        }

        ifNotNull(barnetilleggSaerkullsbarn) { tillegg ->
            showIf(tillegg.map { it > 0 }) {
                item { includePhrase(vedleggPlikterUT7_001) }
            }
        }

        item { includePhrase(vedleggPlikterUT8_001) }
        item { includePhrase(vedleggPlikterUT9_001) }
        item { includePhrase(vedleggPlikterUT10_001) }
        item { includePhrase(vedleggPlikterUT11_001) }
        item { includePhrase(vedleggPlikterUT12_001) }
    }

    includePhrase(vedleggVeiledning_001)
    includePhrase(vedleggInnsynSakUTPesys_001)
    includePhrase(vedleggHjelpFraAndre_001)
    includePhrase(vedleggKlagePensjon_001)
}
