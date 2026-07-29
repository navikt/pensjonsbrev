package no.nav.brev

@RequiresOptIn(
    message = """Dette er dataklasser vi bruker internt i brevteamet, mellom applikasjonene våre. 
        For deg som konsument anbefaler vi at du heller lager og bruke din egen implementasjon av interfacene vi tilbyr. 
        Bruker du likevel våre interne implementasjoner er det på egen risiko, vi endrer disse ved behov uten å annonsere det ut.""",
    level = RequiresOptIn.Level.ERROR
)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InterneDataklasser


@RequiresOptIn(
    message = """Dette er klasser vi instansierer internt i brevteamets applikasjoner. Ikke bruk denne selv.""",
    level = RequiresOptIn.Level.ERROR
)
@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
annotation class InternKonstruktoer
