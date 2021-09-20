package io.oneprofile.signup.twilio

data class SmsMessage(val from: String,
                      val to: String,
                      val content: String) {

    class Builder {
        private lateinit var from: String
        private lateinit var to: String
        private lateinit var content: String

        fun from(from: String) =  apply { this.from = from }
        fun to(to: String) =  apply { this.to = to }
        fun content(content: String) =  apply { this.content = content }

        fun build(): SmsMessage { return SmsMessage(
                from = from,
                to = to,
                content = content
        )}
    }

}