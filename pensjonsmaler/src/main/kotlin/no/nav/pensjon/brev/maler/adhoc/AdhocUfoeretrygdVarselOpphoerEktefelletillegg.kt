package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocUfoeretrygdVarselOpphoerEktefelletillegg : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_ADHOC_VARSEL_OPPHOER_EKTEFELLETILLEGG
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel om opphør av ektefelletillegg til uføretrygd",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Varsel om opphør av ektefelletillegg til uføretrygd" }
            )
        }
        outline {
            paragraph {
                text(bokmal { + "Du mottar dette brevet fordi ektefelletillegget til din uføretrygd opphører med virkning fra 1. januar 2025." })
            }
            paragraph {
                text(bokmal { + "Når din uførepensjon ble omregnet til uføretrygd med virkning fra 1. januar 2015, beholdt du ektefelletillegget. I vedtak om omregning av uførepensjon til uføretrygd ble det opplyst at ektefelletillegget skulle opphøre senest 31. desember 2024." })
            }
            paragraph {
                text(bokmal { + "Siden det har gått lang tid siden din uførepensjon ble omregnet til uføretrygd, sender vi nå en påminnelse om at ektefelletillegget opphører." })
            }
            paragraph {
                text(bokmal { + "Det rettslige grunnlaget for opphøret følger av forskrift om omregning av uførepensjon til uføretrygd § 8. Her fremgår det at tillegget gis ut vedtaksperioden for ektefelletillegget til uførepensjonen, men opphører senest 31. desember 2024." })
            }
            paragraph {
                text(bokmal { + "Vi vil opphøre ektefelletillegget automatisk, og du vil få et eget vedtak om dette. Du trenger derfor ikke foreta deg noe." })
            }
            title1 {
                text(bokmal { + "Hvordan du går fram for å uttale deg i saken" })
            }
            paragraph {
                text(bokmal { + "Du har rett til å komme med en uttalelse i saken. Fristen for å uttale seg er 14 dager etter at du har mottatt dette brevet." })
            }
            paragraph {
                text(bokmal { + "Uttalelsen bør være skriftlig. På $KONTAKT_URL kan du chatte eller skrive til oss." })
            }
            paragraph {
                text(bokmal { + "Hvis du ikke finner frem på $NAV_URL kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON, hverdager $NAV_KONTAKTSENTER_AAPNINGSTID." })
            }
            paragraph {
                text(bokmal { + "Hvis vi ikke hører noe fra deg, vil saken bli behandlet med de opplysningene vi har informert om ovenfor." })
            }
        }
    }
}
