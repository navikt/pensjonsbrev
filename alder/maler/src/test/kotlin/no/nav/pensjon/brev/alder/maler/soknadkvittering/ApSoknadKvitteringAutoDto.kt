package no.nav.pensjon.brev.fixtures.alder

import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.alder.model.soknad.ApSoknadKvitteringAutoDto

fun createApSoknadKvitteringAutoDto() =
    ApSoknadKvitteringAutoDto(
        innledning = ApSoknadKvitteringAutoDto.Innledning(
            iverksettelsesdato = vilkaarligDato,
            uttaksgrad = 100,
            erNyttRegelverk = true,
        ),
        personopplysninger = ApSoknadKvitteringAutoDto.Personopplysninger(
            navn = "Ola Nordmann",
            foedselsnummer = "12345678901",
            adresselinjer = listOf("Testveien 1", "0001 Oslo"),
            telefon = "99887766",
            statsborgerskap = "Norge",
            erUtenlandsk = false,
            erFlyktning = false,
            kontonummer = "1234.56.78901",
        ),
        familieforhold = ApSoknadKvitteringAutoDto.Familieforhold(
            sivilstand = "Gift",
            omsorgForBarnUnder7 = false,
            eps = ApSoknadKvitteringAutoDto.EpsInfo(
                type = "Ektefelle",
                navn = "Kari Nordmann",
                foedselsnummer = "09876543210",
                mottarPensjon = false,
                harAnnenInntekt = true,
                sumInntekt = 500000,
                arbeidsinntekt = 400000,
                kapitalinntekt = 50000,
                pensjonsinntekt = 50000,
                leverVarigAdskilt = false,
            ),
            avdoed = null,
            samboer = null,
        ),
        utland = ApSoknadKvitteringAutoDto.Utland(
            harBoddArbeidetUtland = true,
            opphold = listOf(
                ApSoknadKvitteringAutoDto.Utenlandsopphold(
                    land = "Sverige",
                    bodd = true,
                    arbeidet = true,
                    startDato = vilkaarligDato.minusYears(10),
                    sluttDato = vilkaarligDato.minusYears(5),
                    pensjonsordning = "Svensk pensjon",
                ),
            ),
        ),
        afpPrivat = ApSoknadKvitteringAutoDto.AfpPrivat(
            soktAfpPrivat = true,
            arbeidsgiverNavn = "Test AS",
            arbeidsgiverOrgnr = "123456789",
            ansattDato = vilkaarligDato.minusYears(20),
            ansattforholdOpphoert = false,
            sisteDagArbeid = null,
            opphoerArsak = null,
            permisjonSiste3Ar = false,
            redusertStillingSiste3Ar = false,
            inntektUtenArbeidsplikt = false,
            naeringsvirkEierandel20 = false,
            stillingUnder20Etter53Ar = false,
            sykemeldtMerEnn26Siste3Ar = false,
            permittertSiste3Ar = false,
            arbeidetUtlandEtter53 = false,
            omsorgForBarnUnder7 = false,
            samtykkeEpost = true,
            epost = "ola@test.no",
        ),
    )
