package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sivilstand.ENKE
import no.nav.pensjon.brev.api.model.Sivilstand.ENSLIG
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.avdoed_sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.bruker_borINorge
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.harInnvilgetBarnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.harTilleggForFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.institusjon_gjeldende
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.newText

// Conditional for showing the attachment is: sakstype = UFOEREP && vedtakResultat = AVSLG

val vedleggOrienteringOmRettigheterOgPlikterUfoere =
    createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterUfoereDto>(
        title = newText(
            Bokmal to "Dine rettigheter og plikter",
            Nynorsk to "Dine rettar og plikter",
            English to "Your rights and obligations"
        ),
        includeSakspart = false,
    ) {
        includePhrase(VedleggPlikterUT_001)

        list {
            item { includePhrase(VedleggPlikterUT1_001) }
            item { includePhrase(VedleggPlikterUT2_001) }

            showIf(
                bruker_borINorge
                    and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
            ) {
                item { includePhrase(VedleggPlikterUT3_001) }
                item { includePhrase(VedleggPlikterUT4_001) }
            }

            item { includePhrase(VedleggPlikterUT5_001) }

            showIf(avdoed_sivilstand.isOneOf(ENSLIG, ENKE)) {
                item { includePhrase(VedleggPlikterUT6_001) }
            }

            showIf(harInnvilgetBarnetillegg) {
                item { includePhrase(VedleggPlikterAP7_001) }
                item { includePhrase(VedleggPlikterUT7_001(harTilleggForFlereBarn)) }
                item { includePhrase(VedleggPlikterUT13_001(harTilleggForFlereBarn)) }
                item { includePhrase(VedleggPlikterUT14_001(harTilleggForFlereBarn)) }
            }

            item { includePhrase(VedleggPlikterUT8_001) }
            item { includePhrase(VedleggPlikterUT9_001) }
            item { includePhrase(VedleggPlikterUT10_001) }
            item { includePhrase(VedleggPlikterUT11_001) }
            item { includePhrase(VedleggPlikterUT12_001) }
        }

        includePhrase(VedleggVeiledning)
        includePhrase(VedleggInnsynSakUfoeretrygdPesys)
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePensjon)
    }
