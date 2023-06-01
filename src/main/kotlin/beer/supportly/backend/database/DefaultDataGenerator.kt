package beer.supportly.backend.database

import beer.supportly.backend.database.entities.RoleEntity
import beer.supportly.backend.database.entities.UserEntity
import beer.supportly.backend.database.repositories.RoleRepository
import beer.supportly.backend.service.UserService
import beer.supportly.backend.utils.StringUtils
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Default data generator for the database
 */
@Component
class DefaultDataGenerator(
    private val userService: UserService,
    private val roleRepository: RoleRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(DefaultDataGenerator::class.java)

    /**
     * Generate the default data for the database
     */
    @PostConstruct
    fun onPostConstruct() {
        val shouldCreateRoles = roleRepository.count() == 0L

        if (shouldCreateRoles) {
            roleRepository.saveAll(
                listOf(
                    RoleEntity("ROLE_USER"),
                    RoleEntity("ROLE_AGENT"),
                    RoleEntity("ROLE_ADMINISTRATOR"),
                )
            )

            logger.info("Generated required roles... [ROLE_USER, ROLE_MODERATOR, ROLE_ADMINISTRATOR]")
        }

        val shouldCreateAdminUser = userService.getUserCount() == 0L

        if (shouldCreateAdminUser) {
            val adminRole = roleRepository.findByName("ROLE_ADMINISTRATOR").get()

            val email = "admin@example.com"
            val randomPassword = StringUtils.generateRandomPassword(15)

            val adminUser = UserEntity(
                email,
                "max",
                "mustermann",
                randomPassword,
                "https://media.istockphoto.com/id/1200677760/photo/portrait-of-handsome-smiling-young-man-with-crossed-arms.jpg?s=612x612&w=0&k=20&c=g_ZmKDpK9VEEzWw4vJ6O577ENGLTOcrvYeiLxi8mVuo=",
                "not_set",
                false,
                true,
                adminRole
            )

            userService.saveUser(adminUser)

            logger.info("########################################################################")
            logger.info("                                                                        ")
            logger.info("Created new default user with super administrator permissions!")
            logger.info("                                                                        ")
            logger.info("    Email: $email")
            logger.info("    Password: $randomPassword")
            logger.info("                                                                        ")
            logger.info("Please save these credentials! You will never see them again!")
            logger.info("                                                                        ")
            logger.info("########################################################################")
        }
    }
}