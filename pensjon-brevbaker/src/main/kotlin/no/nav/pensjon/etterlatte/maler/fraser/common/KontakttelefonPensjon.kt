package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.KONTAKTTELEFON_PENSJON
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE

fun ParagraphOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, out Any>.kontakttelefonPensjon(utland: Expression<Boolean>) =
    textExpr(
        Bokmal to kontakttelefonPensjonExpr(utland),
        Nynorsk to kontakttelefonPensjonExpr(utland),
        English to kontakttelefonPensjonExpr(utland))

fun kontakttelefonPensjonExpr(utland: Expression<Boolean>): Expression<String> =
    ifElse(utland,
        KONTAKTTELEFON_PENSJON_MED_LANDKODE,
        KONTAKTTELEFON_PENSJON)
