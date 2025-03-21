package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloep.fritekst
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text

// felles fraser som kun kan brukes i redigerbare brev
object Redigerbar {
    data class SaksType(val sakstype: Expression<Sakstype>) : PlainTextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun PlainTextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(sakstype.isOneOf(Sakstype.AFP)) {
                text(
                    Bokmal to "AFP",
                    Nynorsk to "AFP",
                    English to "AFP",
                )
            }.orShowIf(sakstype.isOneOf(Sakstype.AFP_PRIVAT)) {
                text(
                    Bokmal to "AFP i privat sektor",
                    Nynorsk to "AFP i privat sektor",
                    English to "contractual pension (AFP) in the private) sector",
                )
            }.orShowIf(sakstype.isOneOf(Sakstype.ALDER)) {
                text(
                    Bokmal to "alderspensjon",
                    Nynorsk to "alderspensjon",
                    English to "retirement pension",
                )
            }.orShowIf(sakstype.isOneOf(Sakstype.BARNEP)) {
                text(
                    Bokmal to "barnepensjon",
                    Nynorsk to "barnepensjon",
                    English to "children’s pension",
                )
            }.orShowIf(sakstype.isOneOf(Sakstype.FAM_PL)) {
                text(
                    Bokmal to "ytelse til tidligere familiepleier",
                    Nynorsk to "yting til tidligare familiepleiarar",
                    English to "previous family carers benefits",
                )
            }.orShowIf(sakstype.isOneOf(Sakstype.GJENLEV)) {
                text(
                    Bokmal to "gjenlevendepensjon",
                    Nynorsk to "attlevandepensjon",
                    English to "survivor's pension",
                )
            }.orShowIf(sakstype.isOneOf(Sakstype.UFOREP)) {
                text(
                    Bokmal to "uføretrygd",
                    Nynorsk to "uføretrygd",
                    English to "disability benefit",
                )
            }.orShow {
                eval(fritekst("ytelse"))
            }
        }
    }
}
