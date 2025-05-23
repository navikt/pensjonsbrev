package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP1967
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDto
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDtoSelectors.AlderspensjonGjeldendeSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDtoSelectors.alderspensjonGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDtoSelectors.alderspensjonPerManed
import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAlderspensjonDtoSelectors.krav
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.TabellMaanedligPensjonKap19
import no.nav.pensjon.brev.maler.fraser.vedlegg.maanedligPensjonFoerSkatt.TabellMaanedligPensjonKap19og20
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.textExpr

// V00007 i doksys: Din månedlige pensjon før skatt (alderspensjon)
@TemplateModelHelpers
val maanedligPensjonFoerSkattAlderspensjon =
    createAttachment<LangBokmalNynorskEnglish, MaanedligPensjonFoerSkattAlderspensjonDto>(
        title = {
            textExpr(
                Bokmal to "Oversikt over pensjonen fra ".expr() + krav.virkDatoFom.format(),
                Nynorsk to "Oversikt over pensjonen frå ".expr() + krav.virkDatoFom.format(),
                English to "Pension specifications as of".expr() + krav.virkDatoFom.format(),
            )
        },
        includeSakspart = false,
        outline = {
            showIf(alderspensjonGjeldende.regelverkType.isOneOf(AP1967, AP2011)) {
                forEach(alderspensjonPerManed) {
                    this.includePhrase(TabellMaanedligPensjonKap19(it))
                }
            }.orShowIf(alderspensjonGjeldende.regelverkType.equalTo(AP2016)) {
                forEach(alderspensjonPerManed) {
                    this.includePhrase(TabellMaanedligPensjonKap19og20(it))
                }
            }
        }
)