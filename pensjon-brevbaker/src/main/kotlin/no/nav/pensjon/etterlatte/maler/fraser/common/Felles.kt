package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object Felles {

    object HjelpFraAndreForvaltningsloven12 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hjelp fra andre - forvaltningsloven § 12",
                    Nynorsk to "Hjelp frå andre – forvaltingslova § 12",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, " +
                            "rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som " +
                            "hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne " +
                            "skjemaet du finner på ${Constants.FULLMAKT_URL}.",
                    Nynorsk to "Du har under heile saksbehandlinga høve til å be om hjelp frå til dømes advokat, " +
                            "rettshjelpar, organisasjonar du er medlem av, eller andre myndige personar. " +
                            "Dersom personen som hjelper deg, ikkje er advokat, " +
                            "må du gi vedkomande ei skriftleg fullmakt. " +
                            "Bruk gjerne skjemaet du finn på ${Constants.FULLMAKT_URL}.",
                    English to "",
                )
            }
        }
    }

}