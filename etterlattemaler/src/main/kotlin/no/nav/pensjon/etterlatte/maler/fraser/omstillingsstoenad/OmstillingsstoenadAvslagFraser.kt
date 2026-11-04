package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.text
import java.time.LocalDate

object OmstillingsstoenadAvslagFraser {
    data class Vedtak(
        val erSluttbehandling: Expression<Boolean>,
        val tidligereFamiliepleier: Expression<Boolean>,
        val avdoedNavn: Expression<String>,
        val avdoedDoedsdato: Expression<LocalDate?>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            val formatertDoedsdato = avdoedDoedsdato.format().ifNull(
                "<Klarte ikke å finne dødsdato automatisk, du må sette inn her>",
            )

            showIf(erSluttbehandling) {
                paragraph {
                    text(
                        bokmal { +
                            "Du har tidligere fått et foreløpig avslag på søknaden din om omstillingsstønad fordi du ikke hadde rett på stønaden kun vurdert etter nasjonale regler. Avslaget var gitt i påvente av opplysninger fra utenlandske trygdemyndigheter." },
                        nynorsk { +
                            "Du har tidlegare fått eit foreløpig avslag på søknaden din om omstillingsstønad fordi du ikkje hadde rett på stønaden vurdert berre etter nasjonale regler. Avslaget var gitt i påvente av opplysningar frå utanlandske trygdemyndigheiter." },
                        english { +
                            "You previously received a preliminary rejection of your application for adjustment allowance because you were assessed only according to national rules, which did not entitle you to the allowance. The rejection was issued pending information from foreign social security authorities." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +
                            "Vi har nå mottatt opplysninger fra utenlandske trygdemyndigheter, som gjør at du ikke har rett på stønaden vurdert etter EØS-/avtalelandreglene heller." },
                        nynorsk { +
                            "Vi har no mottatt opplysningar frå utanlandske trygdemyndigheiter, som gjer at du ikkje har rett på stønaden vurdert etter EØS-/avtalelandreglane heller." },
                        english { +
                            "We have now received information from foreign social security authorities, which means you are not entitled to the allowance under the EEA/agreement country rules either." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi har behandlet søknaden din om omstillingsstønad etter " + avdoedNavn + ", som døde " + formatertDoedsdato + ". Søknaden er endelig avslått." },
                        nynorsk { +"Vi har behandla søknaden din om omstillingsstønad etter " + avdoedNavn + ", som døydde " + formatertDoedsdato + ". Søknaden er endeleg avslått." },
                        english { +
                            "We have processed your application for adjustment allowance following the death of " + avdoedNavn + ", who died on " + formatertDoedsdato + ". Your application has been finally rejected." },
                    )
                }
            }.orShow {
                showIf(tidligereFamiliepleier) {
                    paragraph {
                        text(
                            bokmal { +"Vi har behandlet søknaden din om omstillingsstønad som tidligere familiepleier for <navn på den som er pleid>. Søknaden er avslått." },
                            nynorsk { +"Vi har behandla søknaden din om omstillingsstønad som tidlegare familiepleiar for <navn på den som er pleid>. Søknaden er avslått." },
                            english { +"We have processed your application for adjustment allowance as a former family caregiver for <navn på den som er pleid>. Your application has been rejected." },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"Vi har behandlet søknaden din om omstillingsstønad etter " + avdoedNavn + ", som døde " + formatertDoedsdato + ". Søknaden er avslått." },
                            nynorsk { +"Vi har behandla søknaden din om omstillingsstønad etter " + avdoedNavn + ", som døydde " + formatertDoedsdato + ". Søknaden er avslått." },
                            english { +
                                    "We have processed your application for adjustment allowance following the death of " + avdoedNavn + ", who died on " + formatertDoedsdato + ". Your application has been rejected." },
                        )
                    }
                }
            }
        }
    }
}
