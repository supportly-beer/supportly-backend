package beer.supportly.backend.config

import beer.supportly.backend.grpc.ChatServiceImpl
import io.grpc.Server
import io.grpc.ServerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration class for the gRPC server
 *
 * @property chatServiceImpl the chat service implementation
 *
 * @see beer.supportly.backend.grpc.ChatServiceImpl
 */
@Configuration
class GrpcServerConfig(
    private val chatServiceImpl: ChatServiceImpl
) {

    /**
     * Bean for the gRPC server
     *
     * @return Server started on port 9090 and with the chat service implementation
     *
     * @see beer.supportly.backend.grpc.ChatServiceImpl
     */
    @Bean
    fun grpcServerRunner(): Server {
        val server = ServerBuilder.forPort(9090)
            .addService(chatServiceImpl)
            .build()

        return server.start()
    }
}