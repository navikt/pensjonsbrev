package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocRegelendretGjenlevendetillegg:AutobrevTemplate<Unit> {
    override val kode = Brevkode.AutoBrev.UT_2023_INFO_REGLERENDRET_GJT_12_18
    override val template: LetterTemplate<*, Unit> = createTemplate(
        name = kode.name,
        letterDataType = Unit::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - forlengelse av gjenlevendetillegg i uføretrygden din",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vi forlenger gjenlevendetillegget i uføretrygden din",
                Nynorsk to "Vi forlengjer attlevandetillegget i uføretrygda di",
                English to "TEST"
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Stortinget har vedtatt nye regler for gjenlevendetillegg til uføretrygden fra 1. januar 2024.",
                    Nynorsk to "Stortinget har vedtatt nye reglar for attlevandetillegg til uføretrygda frå 1. januar 2024.",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har hatt tidsbegrenset gjenlevendetillegg i uføretrygden din. Gjenlevendetillegget skal ikke lenger være tidsbegrenset.",
                    Nynorsk to "Du har hatt tidsavgrensa attlevandetillegg i uføretrygda di. Attlevandetillegget skal ikkje lenger vere tidsavgrensa.",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-18.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova § 12-18.",
                    English to ""
                )
            }
            title2 {
                text(
                    Bokmal to "Hvilke andre endringer får betydning for deg?",
                    Nynorsk to "Kva andre endringar får noko å seie for deg?",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du får fortsatt gjenlevendetillegg til uføretrygden din. Du beholder gjenlevendetillegget du har rett til i desember 2023.",
                    Nynorsk to "Du får framleis attlevandetillegg til uføretrygda di. Du beheld attlevandetillegget du har rett til i desember 2023.",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Gjenlevendetillegget vil ikke lenger bli regulert årlig ved endringer i grunnbeløpet. Uføretrygden vil fortsatt bli regulert årlig.",
                    Nynorsk to "Attlevandetillegget vil ikkje lenger bli regulert årleg ved endringar i grunnbeløpet. Uføretrygda vil framleis bli regulert årleg.",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Beløpet du får utbetalt vil fortsatt bli redusert hvis du har inntekt over inntektsgrensen for uføretrygden din.",
                    Nynorsk to "Beløpet du får utbetalt vil framleis bli redusert viss du har inntekt over inntektsgrensa for uføretrygda di.",
                    English to ""
                )
            }
            title2 {
                text(
                    Bokmal to "Lurer du på hva du har utbetalt?",
                    Nynorsk to "Lurar du på kva du har utbetalt?",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan logge deg på nav.no/dinuføretrygd for å se utbetalingene dine.",
                    Nynorsk to "Du kan logge deg på nav.no/dinuføretrygd for å sjå utbetalingane dine.",
                    English to ""
                )
            }
            title2 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du mister gjenlevendetillegget hvis du gifter deg igjen eller får barn med en samboer.",
                    Nynorsk to "Du mistar attlevandetillegget viss du giftar deg att eller får barn med ein sambuar.",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger opphold i et annet land, kan det påvirke utbetalingen din. I slike tilfeller må du derfor straks melde fra til NAV. Se hva du må melde fra om på nav.no/uforetrygd-endring.",
                    Nynorsk to "Viss du får endringar i inntekt, familiesituasjon, jobbsituasjon eller planlegg opphald i eit anna land, kan det verke inn på utbetalinga di. I slike tilfelle må du derfor straks melde frå til NAV. Sjå kva du må melde frå om på nav.no/uforetrygd-endring.",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan oppdaterer inntekten din på nav.no/inntektsplanleggeren.",
                    Nynorsk to "Du kan oppdaterer inntekta di på nav.no/inntektsplanleggeren.",
                    English to ""
                )
            }
            title2 {
                text(
                    Bokmal to "Du har rett til å klage ",
                    Nynorsk to "Du har rett til å klage ",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på nav.no/klage.",
                    Nynorsk to "Viss du meiner vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. Klagen skal vere skriftleg. Du finn skjema og informasjon på nav.no/klage.",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "I vedlegget får du vite mer om hvordan du går fram.",
                    Nynorsk to "I vedlegget får du vite meir om korleis du går fram.",
                    English to ""
                )
            }
            title2 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "Du har rett til innsyn",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram.",
                    Nynorsk to "Du har rett til å sjå dokumenta i saka di. I vedlegget får du vite korleis du går fram.",
                    English to ""
                )
            }
            title2 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Do you have questions?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på ${Constants.GJENLEVENDETILLEGG_URL}.",
                    Nynorsk to "Du finn meir informasjon på ${Constants.GJENLEVENDETILLEGG_URL}.",
                    English to "You can find more information at ${Constants.GJENLEVENDETILLEGG_URL}.",
                )
                newline()
                text(
                    Bokmal to "På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss.",
                    Nynorsk to "På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss.",
                    English to "At ${Constants.KONTAKT_URL} you can chat or write to us.",
                )
                newline()
                text(
                    Bokmal to "Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}, hverdager ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    Nynorsk to "Om du ikkje finn svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}, kvardagar ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                    English to "If you do not find the answer at ${Constants.NAV_URL}, you can call us at: +47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}, weekdays ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}.",
                )
            }
        }
       // includeAttachment(dineRettigheterOgPlikterGjenlevendetillegg)
    }
}