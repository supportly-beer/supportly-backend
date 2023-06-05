package beer.supportly.backend.config

import com.meilisearch.sdk.Client
import com.meilisearch.sdk.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration class for the search client
 */
@Configuration
class SearchConfig {

    /**
     * Bean for the search client
     *
     * @return Search client with the host and key
     */
    @Bean
    fun searchClient(): Client {
        return Client(Config("http://localhost:7700", "GOChGlk8hDDaZCJ4MnzabhfhH2vJ_Pe18vUTU_wYiSI"))
    }
}