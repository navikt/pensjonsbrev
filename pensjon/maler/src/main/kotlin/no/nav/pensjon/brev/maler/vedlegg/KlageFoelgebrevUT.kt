package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.maler.fraser.common.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KLAGEINSTANS
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text


@TemplateModelHelpers
val vedleggFoelgebrevKlageinstansUT = createAttachment<LangBokmal, EmptyVedleggData>(
    title = { text(bokmal { +"Klage - Uføretrygd" }) },
    includeSakspart = true,
) {
    paragraph { text(bokmal { +"Vi viser til din klage av (dato) på vedtak av <dato>." }) }
    paragraph { text(bokmal { +"Vi har vurdert vedtaket vårt på nytt, men har ikke endret det. " }) }
    paragraph { text(bokmal { +"Klagesaken er derfor oversendt til $NAV_KLAGEINSTANS for behandling. Kopi av innstillingen vår er vedlagt." }) }
    paragraph {
        text(bokmal {
            +"Klageinstansen vurderer alle sider av saken på selvstendig grunnlag. "
            +"Resultatet av klagebehandlingen kan bli at vårt vedtak ikke blir endret, eller at det blir endret helt eller delvis. "
            +"Klageinstansen kan også oppheve vedtaket vårt, og sende saken tilbake til oss for helt eller delvis ny behandling."
        })
    }
    paragraph { text(bokmal { +"Du får melding fra $NAV_KLAGEINSTANS når de har mottatt saken." }) }
    paragraph {
        text(bokmal {
            +"Du finner oversikt over saksbehandlingstidene på $NAV_URL/saksbehandlingstider. "
            +"Du får beskjed fra $NAV_KLAGEINSTANS, dersom de trenger mer tid."
        })
    }

    paragraph {
        text(bokmal {
            +"Du kan sende merknader og dokumentasjon til $NAV_KLAGEINSTANS. "
            +"Du kan logge deg inn på $KONTAKT_URL og sende skriftlig melding der. "
            +"Hvis du ønsker å ettersende dokumentasjon, kan du gå til $KLAGE_URL og trykke på " + quoted("Ettersend dokumentasjon") + " for det saken gjelder."
        })
    }
    paragraph{ text(bokmal { +"Har du spørsmål? Du finner mer informasjon på $NAV_URL." }) }
    paragraph{ text(bokmal { +"På $KONTAKT_URL kan du chatte eller skrive til oss." }) }
    paragraph{text(bokmal { +"Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON, hverdager $NAV_KONTAKTSENTER_AAPNINGSTID." })}
}