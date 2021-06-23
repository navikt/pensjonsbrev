package no.nav.pensjon.brev.pdfbygger

open class Field(val type: FieldType, val content: String) {

}

enum class FieldType(val typeKey: String) {
    //It is important that the typekey never changes.
    //the typekey must be all lowercase and a-z due to latex command restrictions
    // Feel free to rename the enum names
    FORNAVN_MOTTAKER("fornavnmottaker"),
    ETTERNAVN_MOTTAKER("etternavnmottaker"),
    GATENAVN_MOTTAKER("gatenavnmottaker"),
    HUSNUMMER_MOTTAKER("husnummermottaker"),
    POSTNUMMER_MOTTAKER("postnummermottaker"),
    POSTSTED_MOTTAKER("poststedmottaker"),
    ALDERSPENSJON_MAANEDLIG_BELOEP("alderspensjonmaanedligbeloep"),
    TESTBREV_OPPGITTPROSENT("oppgittprosent"),
    TESTBREV2_NYTTFELT("nyttfelt"),
}
