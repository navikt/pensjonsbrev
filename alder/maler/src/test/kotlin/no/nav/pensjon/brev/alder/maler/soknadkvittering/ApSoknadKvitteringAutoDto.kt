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
            erUtenlandsk = false,
            statsborgerskapLand = null,
            erFlyktning = false,
            kontonummer = "1234 56 78903",
        ),
        familieforhold = ApSoknadKvitteringAutoDto.Familieforhold(
            sivilstand = "Gift",
            omsorgForBarnUnder7 = false,
            avdoed = null,
            samboer = null,
            harSamboerSpoersmaal = null,
            eps = ApSoknadKvitteringAutoDto.Eps(
                betegnelse = "ektefelle",
                betegnelseGenitiv = "ektefelles",
                betegnelseGenitivStor = "Ektefelles",
                navnOgFoedselsnummer = ApSoknadKvitteringAutoDto.EpsNavnOgFoedselsnummer(
                    navn = "Kari Nordmann",
                    foedselsnummer = "21098765432",
                ),
                samboerFraDato = null,
                giftOgBarn = null,
                leverVarigAdskilt = false,
                pensjonOgInntekt = ApSoknadKvitteringAutoDto.PensjonOgInntekt(
                    mottarAfp = false,
                    harAnnenInntekt = true,
                    sumInntekt = 500000,
                ),
            ),
        ),
        utland = ApSoknadKvitteringAutoDto.Utland(
            harBoddArbeidetUtland = true,
            opphold = listOf(
                ApSoknadKvitteringAutoDto.Utenlandsopphold(
                    land = "Sverige",
                    bodd = true,
                    arbeidet = true,
                    startDato = vilkaarligDato,
                    sluttDato = vilkaarligDato.plusYears(2),
                    pensjonsordning = "Svensk pensjon",
                    utlandsId = "SE-12345",
                    tilleggsinformasjon = "Ingen tilleggsinformasjon",
                ),
            ),
        ),
        afpPrivat = ApSoknadKvitteringAutoDto.AfpPrivat(
            soektAfpPrivat = true,
            detaljer = ApSoknadKvitteringAutoDto.AfpPrivatDetaljer(
                arbeidsgiverNavn = "Testbedrift AS",
                arbeidsgiverAdresse = listOf("Storgata 1", "0001 Oslo"),
                arbeidsgiverOrgnr = "123456789",
                omsorgForBarnUnder7 = false,
                ansattDato = vilkaarligDato,
                ansattforholdOpphoert = false,
                opphoer = null,
                ansattType = "Fast ansatt",
                redusertStillingSiste3Ar = false,
                stillingUnder20Etter53Ar = false,
                sykemeldtMerEnn26Siste3Ar = false,
                permittertSiste3Ar = false,
                permisjonSiste3Ar = false,
                inntektUtenArbeidsplikt = false,
                naeringsvirkEierandel20 = false,
                arbeidetUtlandEtter53 = false,
                samtykkeEpost = true,
                epost = "ola.nordmann@example.com",
            ),
        ),
    )
