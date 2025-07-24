package no.nav.pensjon.brev.model

import no.nav.pensjon.brev.api.model.BorMedSivilstand
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.safe

@JvmName("formatBormedSivilstandTabell")
fun Expression<BorMedSivilstand>.tableFormat() = format(formatter = FormatBorMedSivilstandTabell)

object FormatBorMedSivilstandTabell : LocalizedFormatter<BorMedSivilstand>() {
    override fun apply(first: BorMedSivilstand, second: Language): String =
        when (first) {
            BorMedSivilstand.GIFT_LEVER_ADSKILT,
            BorMedSivilstand.EKTEFELLE -> when (second) {
                Bokmal, Nynorsk -> "Gift"
                English -> "Married"
            }

            BorMedSivilstand.PARTNER, BorMedSivilstand.PARTNER_LEVER_ADSKILT -> when (second) {
                Bokmal -> "Partner"
                Nynorsk -> "Partnar"
                English -> "Partnership"
            }

            BorMedSivilstand.SAMBOER1_5 -> when (second) {
                Bokmal -> "Samboer (jf. Folketrygdloven § 1-5)"
                Nynorsk -> "Sambuar (jf. folketrygdlova § 1-5)"
                English -> "Cohabitation (cf. Section 1-5 of the National Insurance Act)"
            }

            BorMedSivilstand.SAMBOER3_2 -> when (second) {
                Bokmal -> "Samboer (jf. Folketrygdloven § 12-13)"
                Nynorsk -> "Sambuar (jf. Folketrygdlova § 12-13)"
                English -> "Cohabitation (cf. Section 12-13 of the National Insurance Act)"
            }
        }

    override fun stableHashCode(): Int = "FormatBorMedSivilstandTabell".hashCode()
}
@JvmName("formatMetaforceSivilstandBestemtForm")
fun Expression<MetaforceSivilstand>.bestemtForm(storBokstav: Boolean = false) = format(formatter = MetaforceSivilstandBestemt(storBokstav))

@JvmName("formatMetaforceSivilstandUbestemtForm")
fun Expression<MetaforceSivilstand>.ubestemtForm(storBokstav: Boolean = false) = format(formatter = MetaforceSivilstandUbestemt(storBokstav))

@JvmName("formatBorMedSivilstandBestemtForm")
fun Expression<BorMedSivilstand>.bestemtForm(storBokstav: Boolean = false) = format(formatter = BorMedSivilstandBestemt(storBokstav))

@JvmName("formatBorMedSivilstandUbestemtForm")
fun Expression<BorMedSivilstand>.ubestemtForm(storBokstav: Boolean = false) = format(formatter = BorMedSivilstandUbestemt(storBokstav))

class BorMedSivilstandUbestemt(private val storBokstav: Boolean) : LocalizedFormatter<BorMedSivilstand>() {
    override fun apply(first: BorMedSivilstand, second: Language): String = borMedSivilstand(first, second, false, storBokstav)
    override fun stableHashCode(): Int = "BorMedSivilstandUbestemt($storBokstav)".hashCode()
}

class MetaforceSivilstandBestemt(private val storBokstav: Boolean) : LocalizedFormatter<MetaforceSivilstand>() {
    override fun apply(first: MetaforceSivilstand, second: Language): String = metaforceBorMedSivilstand(first, second, true, storBokstav)
    override fun stableHashCode(): Int = "MetaforceSivilstandBestemt($storBokstav)".hashCode()
}

class MetaforceSivilstandUbestemt(private val storBokstav: Boolean) : LocalizedFormatter<MetaforceSivilstand>() {
    override fun apply(first: MetaforceSivilstand, second: Language): String = metaforceBorMedSivilstand(first, second, false, storBokstav)
    override fun stableHashCode(): Int = "MetaforceSivilstandUbestemt($storBokstav)".hashCode()
}


class BorMedSivilstandBestemt(private val storBokstav: Boolean) : LocalizedFormatter<BorMedSivilstand>() {
    override fun apply(first: BorMedSivilstand, second: Language): String = borMedSivilstand(first, second, true, storBokstav)
    override fun stableHashCode(): Int = "BorMedSivilstandBestemt($storBokstav)".hashCode()
}

private fun metaforceBorMedSivilstand(sivilstand: MetaforceSivilstand, language: Language, bestemtForm: Boolean, storBokstav: Boolean = false): String {
    val borMedSivilstand = when (sivilstand) {
        MetaforceSivilstand.EKTEFELLE -> BorMedSivilstand.EKTEFELLE
        MetaforceSivilstand.GIFT -> BorMedSivilstand.EKTEFELLE
        MetaforceSivilstand.SEPARERT -> BorMedSivilstand.EKTEFELLE
        MetaforceSivilstand.GLAD_EKT -> BorMedSivilstand.GIFT_LEVER_ADSKILT
        MetaforceSivilstand.GLAD_PART -> BorMedSivilstand.PARTNER_LEVER_ADSKILT
        MetaforceSivilstand.PARTNER -> BorMedSivilstand.PARTNER
        MetaforceSivilstand.SAMBOER -> BorMedSivilstand.SAMBOER1_5 //formatteres likt uansett
        MetaforceSivilstand.SAMBOER_1_5 -> BorMedSivilstand.SAMBOER1_5
        MetaforceSivilstand.SAMBOER_3_2 -> BorMedSivilstand.SAMBOER3_2
        MetaforceSivilstand.SEPARERT_PARTNER -> BorMedSivilstand.PARTNER
        MetaforceSivilstand.UKJENT,
        MetaforceSivilstand.ENKE,
        MetaforceSivilstand.ENSLIG,
        MetaforceSivilstand.FELLES_BARN,
        MetaforceSivilstand.FORELDER -> null
    }
    return if(borMedSivilstand != null) {
        borMedSivilstand(borMedSivilstand, language, bestemtForm, storBokstav)
    } else ""
}

private fun borMedSivilstand(sivilstand: BorMedSivilstand, language: Language, bestemtForm: Boolean, storBokstav: Boolean): String =
    when (sivilstand) {
        BorMedSivilstand.EKTEFELLE,
        BorMedSivilstand.GIFT_LEVER_ADSKILT -> when (language) {
            Bokmal, Nynorsk -> if (bestemtForm) "ektefellen" else "ektefelle"
            English -> "spouse"
        }

        BorMedSivilstand.PARTNER,
        BorMedSivilstand.PARTNER_LEVER_ADSKILT -> when (language) {
            Bokmal -> if (bestemtForm) "partneren" else "partner"
            Nynorsk -> if (bestemtForm) "partnaren" else "partnar"
            English -> "partner"
        }

        BorMedSivilstand.SAMBOER1_5, BorMedSivilstand.SAMBOER3_2 -> when (language) {
            Bokmal -> if (bestemtForm) "samboeren" else "samboer"
            Nynorsk -> if (bestemtForm) "sambuaren" else "sambuar"
            English -> "cohabitant"
        }
    }.apply {
        return if(storBokstav) {
            replaceFirstChar { it.uppercase() }
        } else this
    }

fun Expression<Sakstype>.format(): Expression<String?> = SakstypeNavn(this, Expression.FromScope.Language)

@JvmName("formatSakstypeNullable")
fun Expression<Sakstype?>.format(): Expression<String?> = safe(SakstypeNavn, Expression.FromScope.Language)

object SakstypeNavn : BinaryOperation<Sakstype, Language, String?>() {
    override fun apply(first: Sakstype, second: Language): String? = sakstype(first, second)
    override fun stableHashCode(): Int = "SakstypeNavn".hashCode()

    private fun sakstype(sakstype: Sakstype, language: Language): String? =
        when(sakstype) {
            Sakstype.AFP -> "AFP"
            Sakstype.AFP_PRIVAT -> when(language) {
                Bokmal -> "AFP i privat sektor"
                Nynorsk -> "AFP i privat sektor"
                English -> "contractual pension (AFP) in the private sector"
            }
            Sakstype.ALDER -> when(language) {
                Bokmal -> "alderspensjon"
                Nynorsk ->  "alderspensjon"
                English -> "retirement pension"
            }
            Sakstype.BARNEP -> when(language) {
                Bokmal -> "barnepensjon"
                Nynorsk ->  "barnepensjon"
                English -> "children’s pension"
            }
            Sakstype.FAM_PL -> when(language) {
                Bokmal -> "ytelse til tidligere familiepleier"
                Nynorsk ->  "yting til tidligare familiepleiarar"
                English -> "previous family carers benefits"
            }
            Sakstype.GJENLEV -> when(language) {
                Bokmal -> "gjenlevendepensjon"
                Nynorsk ->  "attlevandepensjon"
                English -> "survivor's pension"
            }
            Sakstype.UFOREP -> when(language) {
                Bokmal -> "uføretrygd"
                Nynorsk ->  "uføretrygd"
                English -> "disability benefit"
            }

            Sakstype.GAM_YRK,
            Sakstype.GENRL,
            Sakstype.GRBL,
            Sakstype.KRIGSP,
            Sakstype.OMSORG -> null
        }
}