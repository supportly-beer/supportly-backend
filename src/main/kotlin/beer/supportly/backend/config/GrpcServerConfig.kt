package beer.supportly.backend.config

import beer.supportly.backend.grpc.ChatServiceImpl
import io.grpc.Server
import io.grpc.ServerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcServerConfig {

    @Bean
    fun grpcServerRunner(): Server {
        val server = ServerBuilder.forPort(9090)
            .addService(chatService())
            .build()

        return server.start()
    }

    @Bean
    fun chatService(): ChatServiceImpl {
        return ChatServiceImpl()
    }
}