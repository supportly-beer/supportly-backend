package beer.supportly.backend.config

import com.meilisearch.sdk.Client
import com.meilisearch.sdk.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

/**
 * Configuration class for the search client
 */
@Configuration
class SearchConfig(
    private val environment: Environment
) {

    /**
     * Bean for the search client
     *
     * @return Search client with the host and key
     */
    @Bean
    fun searchClient(): Client {
        return Client(
            Config(
                environment.getProperty("SUPPORTLY_SEARCH_API_URL"),
                environment.getProperty("MEILI_MASTER_KEY")
            )
        )
    }
}