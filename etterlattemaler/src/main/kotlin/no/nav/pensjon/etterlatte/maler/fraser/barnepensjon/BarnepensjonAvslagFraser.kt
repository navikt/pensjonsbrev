package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object BarnepensjonAvslagFraser {
    data class Vedtak(
        val erSluttbehandling: Expression<Boolean>,
        val avdoedNavn: Expression<String>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(erSluttbehandling) {
                paragraph {
                    text(
                        Bokmal to
                            "Du har tidligere fått et foreløpig avslag på søknaden din om barnepensjon fordi du ikke hadde rett på pensjonen kun vurdert etter nasjonale regler. Avslaget var gitt i påvente av opplysninger fra utenlandske trygdemyndigheter.",
                        Nynorsk to
                            "Du har tidlegare fått eit foreløpig avslag på søknaden din om barnepensjon fordi du ikkje hadde rett på pensjonen vurdert berre etter nasjonale regler. Avslaget var gitt i påvente av opplysningar frå utanlandske trygdemyndigheiter.",
                        English to
                            "You previously received a preliminary rejection of your application for children`s pension because you were assessed only according to national rules, which did not entitle you to the pension. The rejection was issued pending information from foreign social security authorities.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to
                            "Vi har nå mottatt opplysninger fra utenlandske trygdemyndigheter, som gjør at du ikke har rett på stønaden vurdert etter EØS/avtalelandreglene.",
                        Nynorsk to
                            "Vi har no mottatt opplysningar frå utanlandske trygdemyndigheiter, som gjer at du ikkje har rett på stønaden vurdert etter EØS/avtalelandreglane.",
                        English to
                            "We have now received information from foreign social security authorities, which means you are not entitled to the allowance under the EEA/agreement country rules.",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Din søknad om barnepensjon etter ".expr() + avdoedNavn + " er derfor endelig avslått.",
                        Nynorsk to "Søknaden din om barnepensjon etter ".expr() + avdoedNavn + " er derfor endeleg avslått.",
                        English to
                            "Your application for children`s pension for the deceased ".expr() + avdoedNavn + " has therefore been finally rejected.",
                    )
                }
            }.orShow {
                paragraph {
                    textExpr(
                        Bokmal to "Din søknad om barnepensjon etter ".expr() + avdoedNavn + " er avslått.",
                        Nynorsk to "Søknaden din om barnepensjon etter ".expr() + avdoedNavn + " er avslått.",
                        English to
                            "Your application for children`s pension for the deceased ".expr() + avdoedNavn + " has been rejected.",
                    )
                }
            }
        }
    }
}
