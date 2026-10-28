package no.nav.pensjon.brev.maler.ufore.extensions

import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Grunnlag.Eps
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifElse

fun Expression<Eps>.bokmalUbestemtEntall(): Expression<String> {
    return ifElse(this.equalTo(Eps.EKTEFELLE), "ektefelle".expr(),
        ifElse(this.equalTo(Eps.PARTNER), "partner".expr(),
            ifElse(this.equalTo(Eps.SAMBOER), "samboer".expr(),"".expr())))
}

fun Expression<Eps>.nynorskBestemtEntall(): Expression<String> {
    return ifElse(this.equalTo(Eps.EKTEFELLE), "ektefellen".expr(),
        ifElse(this.equalTo(Eps.PARTNER), "partnaren".expr(),
            ifElse(this.equalTo(Eps.SAMBOER), "sambuaren".expr(),"".expr())))
}

fun Expression<Eps>.engelskUbestemtEntall(): Expression<String> {
    return ifElse(this.equalTo(Eps.EKTEFELLE), "spouse".expr(),
        ifElse(this.equalTo(Eps.PARTNER), "partner".expr(),
            ifElse(this.equalTo(Eps.SAMBOER), "cohabiting partner".expr(),"".expr())))
}