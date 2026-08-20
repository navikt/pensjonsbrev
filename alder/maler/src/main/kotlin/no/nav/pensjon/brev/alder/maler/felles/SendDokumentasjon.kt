package no.nav.pensjon.brev.alder.maler.felles

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.alder.model.aldersovergang.Ytelse
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.expression.equalTo

object SendDokumentasjon : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        includePhrase(SendDokumentasjonFelles)
        paragraph {
            text(
                bokmal { +"Alderspensjonen din blir vurdert på nytt etter at vi har fått dokumentasjonen." },
            )
        }

    }
}

data class SendDokumentasjonYtelse(val ytelse: Expression<Ytelse>) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        includePhrase(SendDokumentasjonFelles)
        paragraph {
            showIf(ytelse.equalTo(Ytelse.ALDER)) {
                text(
                    bokmal { +"Alderspensjonen din blir vurdert på nytt etter at vi har fått dokumentasjonen." },
                )
                }.orShow {
                    text(
                        bokmal { +"AFP-en din blir vurdert på nytt etter at vi har fått dokumentasjonen." },
                    )
            }
        }
    }
}

private object SendDokumentasjonFelles : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Vi ber deg sende dokumentasjonen innen 14 dager fra du får dette brevet:" +
                        "Nav familie- og pensjonsytelser" +
                        "PB 6600 Etterstad" +
                        "0607 OSLO" +
                        "Du kan også sende dokumentasjonen digitalt på nav.no/ettersende." },
            )
        }
    }
}
