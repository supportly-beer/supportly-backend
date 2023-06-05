package beer.supportly.backend.config

import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for the blob storage
 */
@Configuration
class BlobConfig {

    /**
     * Creates the blob client
     *
     * @return BlobServiceClient contains the blob client
     */
    @Bean
    fun blobClient(): BlobServiceClient {
        return BlobServiceClientBuilder()
            .endpoint("https://supportly.blob.core.windows.net/")
            .connectionString("DefaultEndpointsProtocol=https;AccountName=supportly;AccountKey=t2bkD2q5Rha7IaGNNRs/Ueex1BG8JGq/wTruZSM91e6q5Wu2qaGfI0djppgXt5nqeMhWEAVDn2qE+AStSlbe1Q==;EndpointSuffix=core.windows.net")
            .buildClient()
    }
}