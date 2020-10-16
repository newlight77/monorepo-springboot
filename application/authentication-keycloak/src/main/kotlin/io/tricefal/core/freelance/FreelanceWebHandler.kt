package io.tricefal.core.freelance

import io.tricefal.core.exception.NotAcceptedException
import io.tricefal.core.exception.NotFoundException
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.Representation
import io.tricefal.core.metafile.fromModel
import io.tricefal.core.metafile.toMetafile
import io.tricefal.core.signup.SignupModel
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.annotation.PropertySource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.security.SecureRandom
import java.util.*


@Service
@PropertySource("classpath:application.yml")
class FreelanceWebHandler(val freelanceService: IFreelanceService,
                          val metafileService: IMetafileService,
                          private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val dataFilesPath = env.getProperty("data.files.path")!!

    val locale: Locale = LocaleContextHolder.getLocale()

    fun signup(freelanceModel: FreelanceModel): FreelanceModel {
        val domain = fromModel(freelanceModel)
        val result = freelanceService.create(domain)
        return toModel(result)
    }

    fun findByUsername(username: String): Optional<FreelanceModel> {
        return freelanceService.findByUsername(username).map { freelanceDomain -> toModel(freelanceDomain) }
    }

    fun findAll(): List<FreelanceModel> {
        return freelanceService.findAll().map { freelanceDomain -> toModel(freelanceDomain) }
    }

    fun uploadResume(username: String, file: MultipartFile): FreelanceModel {
        val signup = findSignup(username)

        val resumeMetaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV))
        metafileService.save(resumeMetaFile, file.inputStream)

        return toModel(freelanceService.resumeUploaded(signup, resumeMetaFile))
    }

    private fun findSignup(username: String): FreelanceDomain {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        val freelanceDomain = this.freelanceService.findByUsername(username)
                .orElseThrow { NotFoundException("username $username not found") }
        return freelanceDomain
    }

}