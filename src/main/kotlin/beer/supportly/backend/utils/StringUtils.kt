package beer.supportly.backend.utils

/**
 * Utility class for string related operations.
 */
class StringUtils {

    companion object {

        /**
         * Generates a random password with the given length.
         *
         * @param length The length of the password.
         *
         * @return The generated password.
         */
        fun generateRandomPassword(length: Int): String {
            val allowedChars = ('A'..'Z') +
                    ('a'..'z') +
                    ('0'..'9') +
                    "!@#$%^&*()_+"

            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }
}