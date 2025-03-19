package no.nav.pensjon.brev.model

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.*

@JvmName("formatBormedSivilstandTabell")
fun Expression<BorMedSivilstand>.tableFormat() = format(formatter = FormatBorMedSivilstandTabell)

@JvmName("formatterSakstype")
fun Expression<Sakstype>.format() = format(formatter = FormatterSakstype)

object FormatterSakstype : LocalizedFormatter<Sakstype>() {
    override fun apply(first: Sakstype, second: Language): String {
        return when (first) {
            Sakstype.AFP -> "AFP"
            Sakstype.AFP_PRIVAT -> when (second) {
                Bokmal -> "AFP i privat sektor"
                Nynorsk -> "AFP i privat sektor"
                English -> "contractual pension (AFP) in the private sector"
            }

            Sakstype.ALDER -> when (second) {
                Bokmal -> "alderspensjon"
                Nynorsk -> "alderspensjon"
                English -> "retirement pension"
            }

            Sakstype.BARNEP -> when (second) {
                Bokmal -> "barnepensjon"
                Nynorsk -> "barnepensjon"
                English -> "children’s pension"
            }

            Sakstype.FAM_PL -> when (second) {
                Bokmal ->"ytelse til tidligere familiepleier"
                Nynorsk ->"yting til tidligare familiepleiarar"
                English ->"previous family carers benefits"
            }

            Sakstype.GJENLEV -> when (second) {
                Bokmal -> "gjenlevendepensjon"
                Nynorsk -> "attlevandepensjon"
                English -> "survivor's pension"
            }

            Sakstype.UFOREP -> when (second) {
                Bokmal -> "uføretrygd"
                Nynorsk -> "uføretrygd"
                English -> "disability benefit"
            }

            Sakstype.GAM_YRK -> TODO()
            Sakstype.GENRL -> TODO()
            Sakstype.GRBL -> TODO()
            Sakstype.KRIGSP -> TODO()
            Sakstype.OMSORG -> TODO()
        }
    }

    override fun stableHashCode(): Int = "FormatterSakstype".hashCode()


}

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

@JvmName("formatBorMedSivilstandBestemtForm")
fun Expression<BorMedSivilstand>.bestemtForm() = format(formatter = BorMedSivilstandBestemt)

@JvmName("formatBorMedSivilstandUbestemtForm")
fun Expression<BorMedSivilstand>.ubestemtForm() = format(formatter = BorMedSivilstandUbestemt)

object BorMedSivilstandUbestemt : LocalizedFormatter<BorMedSivilstand>() {
    override fun apply(first: BorMedSivilstand, second: Language): String = borMedSivilstand(first, second, false)
    override fun stableHashCode(): Int = "BorMedSivilstandUbestemt".hashCode()
}

object BorMedSivilstandBestemt : LocalizedFormatter<BorMedSivilstand>() {
    override fun apply(first: BorMedSivilstand, second: Language): String = borMedSivilstand(first, second, true)
    override fun stableHashCode(): Int = "BorMedSivilstandBestemt".hashCode()
}

private fun borMedSivilstand(sivilstand: BorMedSivilstand, language: Language, bestemtForm: Boolean): String =
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
    }