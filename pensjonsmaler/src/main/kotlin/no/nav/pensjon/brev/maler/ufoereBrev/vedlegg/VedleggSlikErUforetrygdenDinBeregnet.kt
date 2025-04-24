package no.nav.pensjon.brev.maler.ufoereBrev.vedlegg

import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDto
//import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.VedleggSlikErUforetrygdenDinBeregnetDtoSelectors.virkningFom

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

fun  createVedleggSlikErUforetrygdenDinBeregnet()
    = createAttachment<LangBokmalNynorsk, VedleggSlikErUforetrygdenDinBeregnetDto> (
        title = newText(
            Bokmal to "Slik er uføretrygden din beregnet ",
            Nynorsk to "",
        ),
        includeSakspart = false,
    ) {

        paragraph {
            list {
                item {
                    textExpr(
                        Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr(), // + virkningFom.format() + ".",
                        Nynorsk to "".expr(),
                    )
                }
            }
        }
    }
