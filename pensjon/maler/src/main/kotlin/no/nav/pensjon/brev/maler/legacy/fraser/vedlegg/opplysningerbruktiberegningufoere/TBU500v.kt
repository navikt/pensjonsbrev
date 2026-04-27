package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU500v: OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        title1 {
            text (
                bokmal { + "Slik har vi fastsatt den nye inntektsgrensen din" },
                nynorsk { + "Slik har vi fastsett den nye inntektsgrensa di" },
            )
        }

        paragraph {
            text (
                bokmal { + "Vi har økt inntektsgrensen din for at du skal få riktig utbetaling av uføretrygd. Inntektsgrensen din økes ved at den fastsatte inntekten din før du ble ufør blir endret. " },
                nynorsk { + "Vi har auka inntektsgrensa di for at du skal få riktig utbetaling av uføretrygd. Inntektsgrensa di blir auka ved at den fastsette inntekta di før du blei ufør, blir endra. " },
            )
        }

        paragraph {
            text (
                bokmal { + "Inntekten din før du ble ufør er fastsatt ut fra inntekten i den nåværende stillingsandelen din. Den nye inntekten din er regnet om til en årsinntekt i full stilling. Årsinntekten er deretter justert tilbake til uføretidspunktet ditt og utgjør nå den nye fastsatte inntekten din før du ble ufør." },
                nynorsk { + "Inntekta di før du blei ufør, er fastsett ut frå inntekta i den noverande stillingsdelen din. Den nye inntekta di er rekna om til ei årsinntekt i full stilling. Årsinntekta er deretter justert tilbake til uføretidspunktet ditt og utgjer no den nye fastsette inntekta di før du blei ufør. " },
            )
        }
    }
}