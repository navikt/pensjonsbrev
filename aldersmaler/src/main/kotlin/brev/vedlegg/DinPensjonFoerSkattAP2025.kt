package brev.vedlegg

import brev.vedlegg.maanedligPensjonFoerSkatt.TabellMaanedligPensjonFlerePerioderInnledning
import brev.vedlegg.maanedligPensjonFoerSkatt.TabellMaanedligPensjonKap20
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025DtoSelectors.beregnetPensjonPerManedGjeldende
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025DtoSelectors.beregnetPensjonperManed
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattAP2025DtoSelectors.kravVirkFom
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

// V00010 i metaforce
@TemplateModelHelpers
val vedleggMaanedligPensjonFoerSkattAp2025 =
    createAttachment<LangBokmalNynorskEnglish, MaanedligPensjonFoerSkattAP2025Dto>(
        title = newText(
            Bokmal to "Dette er din månedlige pensjon før skatt",
            Nynorsk to "Dette er den månadlege pensjonen din før skatt",
            English to "This is your monthly pension before tax",
        ),
        includeSakspart = false,
    ) {
        includePhrase(TabellMaanedligPensjonKap20(beregnetPensjonPerManedGjeldende))

        showIf(beregnetPensjonperManed.size().greaterThan(1)) {
            includePhrase(TabellMaanedligPensjonFlerePerioderInnledning(kravVirkFom))
            forEach(beregnetPensjonperManed) {
                includePhrase(TabellMaanedligPensjonKap20(it))
            }
        }
    }