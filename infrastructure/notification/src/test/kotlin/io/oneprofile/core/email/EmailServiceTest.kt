package io.oneprofile.core.email

import com.icegreen.greenmail.util.GreenMail
import com.icegreen.greenmail.util.GreenMailUtil
import com.icegreen.greenmail.util.ServerSetup
import freemarker.cache.FileTemplateLoader
import freemarker.template.Configuration
import org.junit.jupiter.api.*
import org.junit.jupiter.api.io.TempDir
import org.mockito.Mockito
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.io.File
import java.io.FileInputStream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmailServiceTest {

    private val config = Configuration(Configuration.VERSION_2_3_30)
    private val sender = Mockito.spy(JavaMailSenderImpl::class.java)

    @TempDir
    lateinit var tmpDir: File

    private val greenMail = GreenMail(ServerSetup(2525, null, "smtp"))

    @BeforeEach
    internal fun beforeEach() {
        greenMail.reset()
    }

    @BeforeAll
    internal fun before() {
        val path = DefaultResourceLoader().getResource("classpath:/templates/").file
        config.templateLoader = FileTemplateLoader(path)
        sender.port = 2525;
        greenMail.start()
    }

    @AfterAll
    internal fun after() {
        greenMail.stop()
    }

    @Test
    fun `should send an email`() {
        // arrange
        val message = EmailMessage.Builder()
                .from("from@mail.com")
                .to("to@mail.com")
                .subject("my subject")
                .content("my content")
                .build()

        // act
        EmailService(sender, config).send(message)

        // assert
        val emails = greenMail.receivedMessages
        Assertions.assertEquals(1, emails.size)
        Assertions.assertEquals("my subject", emails[0].subject)
        val messageContent = GreenMailUtil.getBody(emails[0])
        Assertions.assertTrue(messageContent.contains("my content"))
    }

    @Test
    fun `should send an email with attachment`() {
        // arrange
//        val attachment = ByteArrayInputStream("empty".toByteArray())
        val file = File(tmpDir, "test.txt")
        file.createNewFile()
        val attachment = FileInputStream(file)

        val message = EmailMessage.Builder()
                .from("from@mail.com")
                .to("to@mail.com")
                .subject("my subject")
                .content("my content")
                .attachment(attachment)
                .build()

        // act
        EmailService(sender, config).send(message)

        // assert
        val emails = greenMail.receivedMessages
        Assertions.assertEquals(1, emails.size)
        Assertions.assertEquals("my subject", emails[0].subject)
        val messageContent = GreenMailUtil.getBody(emails[0])
        Assertions.assertTrue(messageContent.contains("Content-Type: multipart/related;"))
        Assertions.assertTrue(messageContent.contains("Content-Type: text/plain; charset=UTF-8"))
        Assertions.assertTrue(messageContent.contains("Content-Type: application/octet-stream; name=file"))
        Assertions.assertTrue(messageContent.contains("Content-Disposition: attachment; filename=file"))
        Assertions.assertTrue(messageContent.contains("my content"))
    }

    @Test
    fun `should send an email from a template`() {
        // arrange
        val model = hashMapOf<String, String>()
        model.put("greeting", "Kong")
        model.put("content", "my content as a long string")
        val message = EmailMessage.Builder()
                .from("from@mail.com")
                .to("to@mail.com")
                .subject("my subject")
                .content("content")
                .emailTemplate(EmailTemplate.SIGNUP)
                .model(model)
                .build()

        // act
        EmailService(sender, config).send(message)

        // assert
        val emails = greenMail.receivedMessages
        Assertions.assertEquals(1, emails.size)
        Assertions.assertEquals("my subject", emails[0].subject)
        val messageContent = GreenMailUtil.getBody(emails[0])
        Assertions.assertTrue(messageContent.contains("my content as a long string"))
    }
}