package beer.supportly.backend.mail

import beer.supportly.backend.database.entities.UserEntity
import beer.supportly.backend.security.service.JwtService
import org.apache.catalina.util.URLEncoder
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Service to send emails to users.
 *
 * @property javaMailSender The mail sender to use.
 */
@Service
class MailService(
    private val javaMailSender: JavaMailSender,
    private val jwtService: JwtService
) {

    /**
     * Sends the forgot password email for a user.
     *
     * @param userEntity The user entity.
     */
    fun sendForgotPassword(userEntity: UserEntity) {
        this.sendMail(
            userEntity.email, "Reset your password!", this.getForgotPasswordTemplate(
                userEntity.firstName,
                userEntity.lastName,
                jwtService.generateToken(
                    mapOf("type" to "resetPassword"),
                    userEntity,
                    Date(System.currentTimeMillis() + (1000 * 60 * 10))
                ),
                userEntity.email
            )
        )
    }

    /**
     * Sends the email validation email for a user.
     *
     * @param userEntity The user entity.
     */
    fun sendEmailValidation(userEntity: UserEntity) {
        this.sendMail(
            userEntity.email, "Welcome to Supportly!", this.getValidateEmailTemplate(
                userEntity.firstName,
                userEntity.lastName,
                jwtService.generateToken(
                    mapOf("type" to "validateEmail"),
                    userEntity,
                    Date(System.currentTimeMillis() + (1000 * 60 * 10))
                ),
                userEntity.email
            )
        )
    }

    /**
     * Sends an email to the given email address.
     *
     * @param email The email address to send the email to.
     * @param title The title of the email.
     * @param message The message of the email.
     */
    fun sendMail(email: String, title: String, message: String) {
        val mimeMessage = javaMailSender.createMimeMessage()
        val mimeMessageHelper = MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name())

        mimeMessageHelper.setFrom("noreply@supportly.beer")
        mimeMessageHelper.setTo(email)
        mimeMessageHelper.setSubject(title)
        mimeMessageHelper.setText(message, true)

        javaMailSender.send(mimeMessageHelper.mimeMessage)
    }

    /**
     * Get the email template for the email validation
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param token The token to validate the email.
     * @param email The email of the user.
     *
     * @return The email template.
     */
    fun getValidateEmailTemplate(firstName: String, lastName: String, token: String, email: String): String {
        val verifyUrl =
            "http://localhost:4200/auth/validate-email/$token/${
                URLEncoder.DEFAULT.encode(
                    email,
                    StandardCharsets.UTF_8
                )
            }"
        return this.getHtmlTemplate(
            firstName,
            lastName,
            "Please verify your email by clicking the button down below. Please note, when you now confirm your email, your account will be automatically removed after 30 days.",
            "Confirm Email",
            "If you did not register on our site and you received this email, feel free to ignore it. Also check all your email accounts and get in touch with our support. Someone might try to impersonate you!",
            verifyUrl
        )
    }

    /**
     * Get the email template for the forgot password email.
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param token The token to reset the password.
     * @param email The email of the user.
     *
     * @return The password template.
     */
    fun getForgotPasswordTemplate(firstName: String, lastName: String, token: String, email: String): String {
        val forgotUrl =
            "http://localhost:4200/auth/reset-password/$token/${
                URLEncoder.DEFAULT.encode(
                    email,
                    StandardCharsets.UTF_8
                )
            }"
        return this.getHtmlTemplate(
            firstName,
            lastName,
            "Here is the link you requested to reset your password. We would suggest that you save your passwords in a password manager of your choice!",
            "Reset Password",
            "If you did not issue this password reset, we would highly recommend, you change your password and contact support about this incident. Someone might try to impersonate you!",
            forgotUrl
        )
    }

    /**
     * Get the email general email template.
     *
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param chapterOne The first chapter of the email.
     * @param buttonText The text of the button.
     * @param chapterTwo The second chapter of the email.
     * @param url The url to redirect to.
     *
     * @return The email template.
     */
    fun getHtmlTemplate(
        firstName: String,
        lastName: String,
        chapterOne: String,
        buttonText: String,
        chapterTwo: String,
        url: String
    ): String {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                "<head>\n" +
                "<!--[if gte mso 9]>\n" +
                "<xml>\n" +
                "  <o:OfficeDocumentSettings>\n" +
                "    <o:AllowPNG/>\n" +
                "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "  </o:OfficeDocumentSettings>\n" +
                "</xml>\n" +
                "<![endif]-->\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "  <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\n" +
                "  <title></title>\n" +
                "  \n" +
                "    <style type=\"text/css\">\n" +
                "      @media only screen and (min-width: 620px) {\n" +
                "  .u-row {\n" +
                "    width: 600px !important;\n" +
                "  }\n" +
                "  .u-row .u-col {\n" +
                "    vertical-align: top;\n" +
                "  }\n" +
                "\n" +
                "  .u-row .u-col-100 {\n" +
                "    width: 600px !important;\n" +
                "  }\n" +
                "\n" +
                "}\n" +
                "\n" +
                "@media (max-width: 620px) {\n" +
                "  .u-row-container {\n" +
                "    max-width: 100% !important;\n" +
                "    padding-left: 0px !important;\n" +
                "    padding-right: 0px !important;\n" +
                "  }\n" +
                "  .u-row .u-col {\n" +
                "    min-width: 320px !important;\n" +
                "    max-width: 100% !important;\n" +
                "    display: block !important;\n" +
                "  }\n" +
                "  .u-row {\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "  .u-col {\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "  .u-col > div {\n" +
                "    margin: 0 auto;\n" +
                "  }\n" +
                "}\n" +
                "body {\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "}\n" +
                "\n" +
                "table,\n" +
                "tr,\n" +
                "td {\n" +
                "  vertical-align: top;\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "\n" +
                "p {\n" +
                "  margin: 0;\n" +
                "}\n" +
                "\n" +
                ".ie-container table,\n" +
                ".mso-container table {\n" +
                "  table-layout: fixed;\n" +
                "}\n" +
                "\n" +
                "* {\n" +
                "  line-height: inherit;\n" +
                "}\n" +
                "\n" +
                "a[x-apple-data-detectors='true'] {\n" +
                "  color: inherit !important;\n" +
                "  text-decoration: none !important;\n" +
                "}\n" +
                "\n" +
                "table, td { color: #000000; } #u_body a { color: #0000ee; text-decoration: underline; } @media (max-width: 480px) { #u_content_heading_1 .v-container-padding-padding { padding: 50px 10px 0px 20px !important; } #u_content_heading_1 .v-font-size { font-size: 34px !important; } #u_content_text_1 .v-container-padding-padding { padding: 20px !important; } #u_content_text_1 .v-text-align { text-align: justify !important; } #u_content_button_1 .v-container-padding-padding { padding: 10px 20px 50px !important; } #u_content_text_3 .v-container-padding-padding { padding: 20px !important; } #u_content_text_3 .v-text-align { text-align: justify !important; } #u_content_text_3 .v-line-height { line-height: 190% !important; } #u_content_text_2 .v-line-height { line-height: 200% !important; } }\n" +
                "    </style>\n" +
                "  \n" +
                "  \n" +
                "\n" +
                "<!--[if !mso]><!--><link href=\"https://fonts.googleapis.com/css?family=Cabin:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><link href=\"https://fonts.googleapis.com/css?family=Montserrat:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]-->\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #e7e7e7;color: #000000\">\n" +
                "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\n" +
                "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
                "  <table id=\"u_body\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #e7e7e7;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "  <tbody>\n" +
                "  <tr style=\"vertical-align: top\">\n" +
                "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
                "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #e7e7e7;\"><![endif]-->\n" +
                "    \n" +
                "\n" +
                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #f1f7fb\">\n" +
                "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #f1f7fb;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                "      \n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table style=\"font-family:'Montserrat',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 0px 0px;font-family:'Montserrat',sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tr>\n" +
                "    <td class=\"v-text-align\" style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
                "      \n" +
                "      <img align=\"center\" border=\"0\" src=\"https://supportly.blob.core.windows.net/branding/logo.png\" alt=\"image\" title=\"image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 600px;\" width=\"600\"/>\n" +
                "      \n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #1e2655\">\n" +
                "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #1e2655;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                "      \n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 1px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 1px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table style=\"font-family:'Montserrat',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:0px;font-family:'Montserrat',sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 3px solid #1e2655;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "    <tbody>\n" +
                "      <tr style=\"vertical-align: top\">\n" +
                "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "          <span>&#160;</span>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #ffffff\">\n" +
                "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #ffffff;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                "      \n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table id=\"u_content_heading_1\" style=\"font-family:'Montserrat',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:60px 10px 0px;font-family:'Montserrat',sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <h1 class=\"v-text-align v-line-height v-font-size\" style=\"margin: 0px; color: #1e2655; line-height: 140%; text-align: left; word-wrap: break-word; font-family: arial black,AvenirNext-Heavy,avant garde,arial; font-size: 45px; font-weight: 400;\">Attention</h1>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table id=\"u_content_text_1\" style=\"font-family:'Montserrat',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 10px 20px;font-family:'Montserrat',sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <div class=\"v-text-align v-line-height v-font-size\" style=\"font-size: 14px; line-height: 190%; text-align: justify; word-wrap: break-word;\">\n" +
                "    <p style=\"font-size: 14px; line-height: 190%;\"><strong><span style=\"font-size: 18px; line-height: 34.2px; color: #1e2655; font-family: Cabin, sans-serif;\">Hi $firstName $lastName!</span></strong><br />$chapterOne</p>\n" +
                "  </div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table id=\"u_content_button_1\" style=\"font-family:'Montserrat',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 60px;font-family:'Montserrat',sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <!--[if mso]><style>.v-button {background: transparent !important;}</style><![endif]-->\n" +
                "<div class=\"v-text-align\" align=\"left\">\n" +
                "  <!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"$url\" style=\"height:37px; v-text-anchor:middle; width:141px;\" arcsize=\"11%\"  stroke=\"f\" fillcolor=\"#1e2655\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:'Montserrat',sans-serif;\"><![endif]-->  \n" +
                "    <a href=\"$url\" target=\"_blank\" class=\"v-button v-font-size\" style=\"box-sizing: border-box;display: inline-block;font-family:'Montserrat',sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #1e2655; border-radius: 4px;-webkit-border-radius: 4px; -moz-border-radius: 4px; width:auto; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;font-size: 14px;\">\n" +
                "      <span class=\"v-line-height\" style=\"display:block;padding:10px 20px;line-height:120%;\"><span style=\"font-size: 14px; line-height: 16.8px;\">$buttonText</span></span>\n" +
                "    </a>\n" +
                "  <!--[if mso]></center></v:roundrect><![endif]-->\n" +
                "</div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:'Montserrat',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:0px;font-family:'Montserrat',sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "    <tbody>\n" +
                "      <tr style=\"vertical-align: top\">\n" +
                "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "          <span>&#160;</span>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table id=\"u_content_text_3\" style=\"font-family:'Montserrat',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 10px 40px;font-family:'Montserrat',sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <div class=\"v-text-align v-line-height v-font-size\" style=\"font-size: 14px; line-height: 190%; text-align: justify; word-wrap: break-word;\">\n" +
                "    <p style=\"font-size: 14px; line-height: 190%;\">$chapterTwo</p>\n" +
                "  </div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #1e2655\">\n" +
                "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #1e2655;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                "      \n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table id=\"u_content_text_2\" style=\"font-family:'Montserrat',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 10px;font-family:'Montserrat',sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <div class=\"v-text-align v-line-height v-font-size\" style=\"font-size: 14px; color: #ecf0f1; line-height: 190%; text-align: center; word-wrap: break-word;\">\n" +
                "    <p style=\"font-size: 14px; line-height: 190%;\">© Supportly  |  We love to help people.</p>\n" +
                "<p style=\"font-size: 14px; line-height: 190%;\">If you have any quetions please contact us <span style=\"text-decoration: underline; font-size: 14px; line-height: 26.6px;\"><strong>help@supportly.beer</strong></span>.</p>\n" +
                "  </div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "  </tbody>\n" +
                "  </table>\n" +
                "  <!--[if mso]></div><![endif]-->\n" +
                "  <!--[if IE]></div><![endif]-->\n" +
                "</body>\n" +
                "\n" +
                "</html>\n"
    }
}