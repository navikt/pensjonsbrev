# Brevbaker

## Lokal kjøring mot q2
Kjør brevbaker lokalt mot pdf-bygger som kjører i q2. Autentisering/autorisering er deaktivert, og FakeUnleash er satt opp til å svare med `enabled` for
alle feature toggles. Man kan overstyre FakeUnleash med konfigurasjonsfeltet `brevbaker.unleash.fakeUnleashEnableAll`. 

For å kjøre brevbaker lokalt mot q2 må development mode aktiveres og man må overstyre konfigurasjonsfil til `application-local.conf`. Kjører man fra intellij så vil 
development mode være aktivert som standardvalg, om ikke må man angi dette som et VM-option `-Dio.ktor.development=true`. For å velge konfigurasjonsfil angir
man `-config=application-local.conf` som program argument.
