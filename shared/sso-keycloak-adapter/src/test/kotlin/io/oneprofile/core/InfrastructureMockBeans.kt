package io.oneprofile.core


import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles

@ComponentScan("io.oneprofile.core")
@ActiveProfiles("test")
class InfrastructureMockBeans {

}