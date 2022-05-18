package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAfpDto
import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText

val orienteringOmRettigheterOgPlikterAFP = createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterAfpDto>(
    title = newText(
        Bokmal to "Dine rettigheter og plikter",
        Nynorsk to "Dine rettar og plikter",
        English to "Your rights and obligations"
    ),
    includeSakspart = true,
) {
    val bor_i_norge = argument().select(OrienteringOmRettigheterAfpDto::bruker_borINorge)
    val institusjon_gjeldende = argument().select(OrienteringOmRettigheterAfpDto::institusjon_gjeldende)
    val sivilstand = argument().select(OrienteringOmRettigheterAfpDto::bruker_sivilstand)
    val saktype = argument().select(OrienteringOmRettigheterAfpDto::saktype)


    showIf(saktype.isOneOf(Sakstype.AFP)) {
        includePhrase(vedleggPlikterAFP_001)
        list {
            item { includePhrase(vedleggPlikterAFP1_001) }
            showIf(
                sivilstand.isOneOf(ENSLIG, ENKE)
            ) {
                item { includePhrase(vedleggPlikterAFP2_001) }
            }
            showIf(
                bor_i_norge
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterAFP3_001) }
            }
            showIf(
                not(bor_i_norge)
                    and not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))
            ) {
                item { includePhrase(vedleggPlikterAFP4_001) }
            }
        }
    }

    showIf(saktype.isOneOf(Sakstype.AFP)) {
        includePhrase(vedleggVeiledning_001)
        includePhrase(vedleggInnsynSakPensjon_001)
        includePhrase(vedleggHjelpFraAndre_001)
        includePhrase(vedleggKlagePensjon_001)
    }
}
