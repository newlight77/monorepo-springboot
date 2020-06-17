package io.tricefal.core.okta

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.PropertySource

@ConfigurationProperties(prefix = "okta")
@PropertySource("okta.yml")
class OktaProperties(
        val baseUrl: String
)