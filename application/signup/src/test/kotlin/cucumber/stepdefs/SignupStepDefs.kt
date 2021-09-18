package cucumber.stepdefs

import io.cucumber.datatable.DataTable
import io.cucumber.java8.En
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import io.oneprofile.core.signup.SignupJpaRepository
import io.oneprofile.core.signup.SignupModel
import org.hamcrest.core.Is
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import java.time.Instant

class SignupStepDefs : En {

    @LocalServerPort
    var port: Int = 0

    val baseUrl = "http://localhost:"
    lateinit var signupModel: SignupModel
    lateinit var response: ValidatableResponse

    @MockBean
    lateinit var repository: SignupJpaRepository


    init {
        DataTableType { entry: Map<String, String> ->
            SignupModel.Builder("email")
                    .password(entry["password"])
                    .lastname(entry["lastname"])
                    .firstname(entry["firstname"])
                    .phoneNumber(entry["phoneNumber"])
                    .signupDate(Instant.now())
                    .build()
        }

        Given("^a user with the given details$") { signupTable: DataTable ->
            val signups: List<SignupModel> = signupTable.asList(SignupModel::class.java)
            signupModel = signups[0]
        }

        When("^he subscribes on the platform$") {
            val url = "$baseUrl$port/api/signup"

            val response = RestAssured.given().log()
                    .all()
                    .`when`()
                    .contentType(ContentType.JSON)
                    .body(signupModel)
                    .post(url)
                    .andReturn()

            this.response = response.then()
        }

        Then("^an account is created$") {
            this.response.statusCode(201)
            this.response.body("created", Is.`is`(true))
        }

        And("^and notification is send by email with a message$") {
            this.response.body("emailSent", Is.`is`(true))
        }

        And("^an activation code is send via SMS to the phone number$") {
            this.response.body("smsSent", Is.`is`(true))
        }
    }

}
