package io.tricefal.core.profile

import io.tricefal.core.exception.NotAcceptedException
import io.tricefal.core.exception.NotFoundException
import io.tricefal.core.metafile.*
import io.tricefal.core.signup.ISignupService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.nio.file.Paths


@Service
@PropertySource("classpath:application.yml")
class ProfileWebHandler(val profileService: IProfileService,
                        val metafileService: IMetafileService,
                        private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val dataFilesPath = env.getProperty("data.files.path")!!

    fun find(username: String): ProfileModel {
         return toModel(findProfile(username))
    }

    fun uploadPortrait(username: String, file: MultipartFile): ProfileModel {

        val profile = findProfile(username)

        val resumeMetaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.PORTRAIT))
        metafileService.save(resumeMetaFile, file.inputStream)

        return toModel(profileService.portraitUploaded(profile, resumeMetaFile))
    }

    fun uploadResume(username: String, file: MultipartFile): ProfileModel {

        val profile = findProfile(username)

        val resumeMetaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.CV))
        metafileService.save(resumeMetaFile, file.inputStream)

        return toModel(profileService.resumeUploaded(profile, resumeMetaFile))
    }

    fun uploadRef(username: String, file: MultipartFile): ProfileModel {

        val profile = findProfile(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.REF))
        metafileService.save(metaFile, file.inputStream)

        return toModel(profileService.refUploaded(profile, metaFile))
    }

    private fun findProfile(username: String): ProfileDomain {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        val profile = this.profileService.findByUsername(username)
                .orElseThrow { NotFoundException("username $username not found") }
        return profile
    }

    fun portrait(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.PORTRAIT }
    }

    fun cv(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.CV }
    }

    fun ref(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.REF }
    }

}