package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sivilstand.ENKE
import no.nav.pensjon.brev.api.model.Sivilstand.ENSLIG
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAfpDto
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.newText

// Conditional for showing the attachment is: sakstype = AFP && vedtakResultat = INNVL

val orienteringOmRettigheterOgPlikterAFP = createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterAfpDto>(
    title = newText(
        Bokmal to "Dine rettigheter og plikter",
        Nynorsk to "Dine rettar og plikter",
        English to "Your rights and obligations"
    ),
    includeSakspart = false,
) {
    val bor_i_norge = argument().select(OrienteringOmRettigheterAfpDto::bruker_borINorge)
    val institusjon_gjeldende = argument().select(OrienteringOmRettigheterAfpDto::institusjon_gjeldende)
    val sivilstand = argument().select(OrienteringOmRettigheterAfpDto::bruker_sivilstand)


    includePhrase(VedleggPlikterAFP_001)
    list {
        item { includePhrase(vedleggPlikterAFP1_001) }
        showIf(sivilstand.isOneOf(ENSLIG, ENKE)) {
            item { includePhrase(vedleggPlikterAFP2_001) }
        }

        showIf(not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))) {
            showIf(bor_i_norge) {
                item { includePhrase(vedleggPlikterAFP3_001) }
            }.orShow {
                item { includePhrase(vedleggPlikterAFP4_001) }
            }
        }
    }

    includePhrase(VedleggVeiledning_001)
    includePhrase(VedleggInnsynSakPensjon_001)
    includePhrase(VedleggHjelpFraAndre_001)
    includePhrase(VedleggKlagePensjon_001)
}
