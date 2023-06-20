package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sivilstand.ENKE
import no.nav.pensjon.brev.api.model.Sivilstand.ENSLIG
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.bruker_borINorge
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.harInnvilgetBarnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.harTilleggForFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.institusjon_gjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.sivilstand
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.newText

// Conditional for showing the attachment is: sakstype = UFOEREP && vedtakResultat = AVSLG

val vedleggDineRettigheterOgPlikterUfoere =
    createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterUfoereDto>(
        title = newText(
            Bokmal to "Dine rettigheter og plikter",
            Nynorsk to "Dine rettar og plikter",
            English to "Your rights and obligations"
        ),
        includeSakspart = false,
    ) {
        includePhrase(VedleggPlikter)

        paragraph {
            list {
                item { includePhrase(VedleggPlikterUT1) }
                item { includePhrase(VedleggPlikterUT2) }

                showIf(
                    bruker_borINorge
                            and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                ) {
                    item { includePhrase(VedleggPlikterUT3) }
                    item { includePhrase(VedleggPlikterUT4) }
                }

                item { includePhrase(VedleggPlikterUT5) }

                showIf(sivilstand.isOneOf(ENSLIG, ENKE)) {
                    item { includePhrase(VedleggPlikterUT6) }
                }

                showIf(harInnvilgetBarnetillegg) {
                    item { includePhrase(VedleggPlikterUT7(harTilleggForFlereBarn)) }
                    item { includePhrase(VedleggPlikterUT13(harTilleggForFlereBarn)) }
                    item { includePhrase(VedleggPlikterUT14(harTilleggForFlereBarn)) }
                }

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
