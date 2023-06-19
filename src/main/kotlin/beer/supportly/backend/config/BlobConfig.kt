package beer.supportly.backend.config

import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

/**
 * Configuration for the blob storage
 *
 * @property environment the environment variables
 */
@Configuration
class BlobConfig(
    private val environment: Environment
) {

    /**
     * Creates the blob client
     *
     * @return BlobServiceClient contains the blob client
     */
    @Bean
    fun blobClient(): BlobServiceClient {
        return BlobServiceClientBuilder()
            .endpoint("https://supportly.blob.core.windows.net/")
            .connectionString(environment.getProperty("SUPPORTLY_AZURE_CONNECTION_STRING"))
            .buildClient()
    }
}