package no.nav.pensjon.brev.maler.klageOgAnke.tekstNAY

import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
object OversendelsesbrevTilKlageinstansTekst {

    object Generisk : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 { text(bokmal { +"Hva klagesaken gjelder" }) }
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til klagen av "
                        +fritekst("dato") + " på vedtak av "
                        +fritekst("dato") + ",der "
                        +"<beskrive kort hva avslaget gjelder/vedtaket gjelder. Eks: (...), der klager fikk avslag på sitt krav om uføretrygd>"
                        +". Klage fristen er overholdt."
                    }
                )
            }
            paragraph { text(bokmal { +"Klagen behandles etter folketrygdloven § " + fritekst("aktuell hjemmel/hjemler") + "." }) }
            paragraph { text(bokmal { +"Vi har vurdert klagen, men ikke funnet grunnlag for å gjøre om det påklagede vedtaket." }) }

            title1 { text(bokmal { +"Klagers anførsler" }) }
            paragraph { text(bokmal { +"Det anføres i klagen <gjengi hovedinnholdet i klagers anførsler i tekst eller punktvis>" }) }

            title1 { text(bokmal { +"Vurdering av klagen" }) }
            paragraph {
                text(
                    bokmal {
                        +"<Gjør rede for problemstillingen i saken. "
                        +"Det skal komme frem hvilke lovbestemmelser som gjøres gjeldende i saken / hva sier loven. "
                        +"Det skal skrives en begrunnelse for hvorfor klager ikke fyller vilkårene. "
                        +"Klagers anførsler skal kommenteres / imøtegås. "
                        +"Drøftelsen skal avsluttes med en konklusjon.>"
                    }
                )
            }
            paragraph{ text(bokmal { +"<Klipp inn fra vedtak eller vilkårsvurdering og svar ut anførslene konkret>" }, BOLD) }
        }
    }
}