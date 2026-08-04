package no.nav.brev

@RequiresOptIn(
    message = """Dette er klasser vi instansierer internt i brevteamets applikasjoner. Ikke bruk denne selv.""",
    level = RequiresOptIn.Level.ERROR,
)
@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
annotation class InternKonstruktoer
