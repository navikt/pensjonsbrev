package no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.instopphreduksjonsperiode

data class InstOpphReduksjonsPeriodeListe(
    val instopphreduksjonsperiode: List<InstOpphReduksjonsPeriode>
) {
    val forsorgeransvaralle: Boolean =
        instopphreduksjonsperiode.all { it.forsorgeransvar }

    val forsorgeransvaringen: Boolean =
        instopphreduksjonsperiode.none { it.forsorgeransvar }

    val forsorgeransvarsiste: Boolean = instopphreduksjonsperiode.maxBy {it.fomDato}.forsorgeransvar

}
