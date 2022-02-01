package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.maler.fraser.Constants
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.text

object Felles {

    /**
     * TBU1223, TBU1224
     */
    val meldEndringerPesys_001 = createPhrase<LangBokmal, Unit> {
        title1 {
            text(
                Language.Bokmal to "Du må melde fra om endringer"
            )
        }
        paragraph {
            text(
                Language.Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget ser du hvilke endringer du må si fra om."
            )
        }

        paragraph {
            text(
                Language.Bokmal to "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. " +
                        "Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til NAV."
            )
        }
    }

    /**
     * TBU1100
     */
    val rettTilKlagePesys_001 = createPhrase<LangBokmal, Unit> {
        title1 {
            text(
                Language.Bokmal to "Du har rett til å klage"
            )
        }

        paragraph {
            text(
                Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}."
            )
        }

        paragraph {
            text(
                Language.Bokmal to "I vedlegget får du vite mer om hvordan du går fram."
            )
        }
    }

    /**
     * TBU1074, TBU1075
     */
    val rettTilInnsynPesys_001 = createPhrase<LangBokmal, Unit> {
        title1 {
            text(
                Language.Bokmal to "Du har rett til innsyn"
            )
        }

        paragraph {
            text(
                Language.Bokmal to "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram."
            )
        }
    }
}