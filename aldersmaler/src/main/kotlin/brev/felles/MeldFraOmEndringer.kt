package brev.felles

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object MeldeFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                bokmal { + "Du må melde fra om endringer" },
                nynorsk { + "Du må melde frå om endringar" },
                english { + "You must notify Nav if anything changes" },
            )
        }
        paragraph {
            text(
                bokmal { + "Hvis du får endringer i familiesituasjon, planlegger opphold i utlandet," +
                        " eller ektefellen eller samboeren din får endringer i inntekten, kan det ha betydning for beløpet du får utbetalt fra Nav." +
                        " I slike tilfeller må du derfor straks melde fra til oss." +
                        " I vedlegget ser du hvilke endringer du må si fra om." },
                nynorsk { + "Dersom du får endringar i familiesituasjonen, planlegg opphald i utlandet," +
                        " eller ektefellen, partnaren eller sambuaren din får endringar i inntekta, kan det få noko å seie for beløpet du får utbetalt frå Nav." +
                        " I slike tilfelle må du derfor straks melde frå til oss. I vedlegget ser du kva endringar du må seie frå om." },
                english { + "If there are changes in your family situation or you are planning a long-term stay abroad," +
                        " or there are changes in the income of your spouse or co-habiting partner, these might affect the payments you receive from Nav." +
                        " In such cases, you must notify Nav immediately. The appendix specifies which changes you are obligated to notify us of." },
            )
        }
        paragraph {
            text(
                bokmal { + "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene." +
                        " Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav." },
                nynorsk { +
                "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane." +
                        " Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav." },
                english { + "If your payments have been too high as a result of you failing to notify us of a change," +
                        " the incorrect payment must normally be repaid. It is your responsibility to keep yourself informed of movements in your account," +
                        " and you are obligated to report any and all errors to Nav." },
            )
        }
    }
}