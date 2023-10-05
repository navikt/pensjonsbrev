package no.nav.pensjon.brev.skribenten.services

//TODO add an actual database client
class SkribentenFakeDatabaseService {
    val favourites = HashMap<String, MutableList<String>>()

    fun getFavourites(userId: String): List<String> =
        favourites[userId]?: emptyList()

    fun addFavourite(userId: String, letterId: String) {
        if (favourites.containsKey(userId)) {
            favourites[userId]?.add(letterId)
        } else {
            favourites[userId] = mutableListOf(letterId)
        }
    }

    fun removeFavourite(userId: String, letterId: String) {
        favourites[userId]?.removeIf { it == letterId }
    }
}