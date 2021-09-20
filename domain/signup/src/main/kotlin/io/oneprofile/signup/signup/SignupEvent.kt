package io.oneprofile.signup.signup

// outgoing to profile
class NewSignupEvent(
    val username: String,
    val status: String,
    val firstname: String?,
    val lastname: String?,
    val phoneNumber: String?
)

// outgoing to freelance
class SignupStatusUpdatedEvent(
    val username: String,
    val status: String,
    val firstname: String?,
    val lastname: String?,
    val phoneNumber: String?
) {
    fun isFreelance() = Status.FREELANCE.toString() === status || Status.FREELANCE_WITH_MISSION.toString() === status
    fun isEmployee() = Status.EMPLOYEE.toString() === status
    fun isClient() = Status.CLIENT.toString() === status
}

// outgoing to profile
class SignupStateUpdatedEvent(val username: String, val state: String)
// incoming from company and freelance
class CompanyCompletionEvent(val username: String, val companyName: String)
// outgoing to mission and profile
class SignupResumeUploadedEvent(var username: String, var filename: String)
// outgoing to profile
class SignupResumeLinkedinUploadedEvent(var username: String, var filename: String)
// incoming from profile
class ProfileResumeUploadedEvent(var username: String, var filename: String)
// incoming from profile
class ProfileResumeLinkedinUploadedEvent(var username: String, var filename: String)

class SignupCommentAddedEvent(var targetUsername: String, var comment: CommentDomain)