import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FiltersDataStore(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val SEARCH_QUERY = stringPreferencesKey("search_query")
        private val SELECTED_GENRES = stringSetPreferencesKey("selected_genres")
        private val MIN_RATING = doublePreferencesKey("min_rating")
    }

    suspend fun saveFilters(
        query: String,
        genres: Set<String>,
        minRating: Double
    ) {
        dataStore.edit { preferences ->
            preferences[SEARCH_QUERY] = query
            preferences[SELECTED_GENRES] = genres
            preferences[MIN_RATING] = minRating
        }
    }

    val filtersFlow: Flow<FilterPreferences> = dataStore.data
        .map { preferences ->
            FilterPreferences(
                query = preferences[SEARCH_QUERY] ?: "",
                genres = preferences[SELECTED_GENRES] ?: emptySet(),
                minRating = preferences[MIN_RATING] ?: 0.0
            )
        }
}

data class FilterPreferences(
    val query: String,
    val genres: Set<String>,
    val minRating: Double
)