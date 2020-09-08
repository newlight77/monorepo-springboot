package io.tricefal.core.profile

import io.tricefal.core.exception.NotAcceptedException
import io.tricefal.core.exception.NotFoundException
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.fromModel
import io.tricefal.core.metafile.toMetafile
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
@PropertySource("classpath:application.yml")
class ProfileWebHandler(val profileService: IProfileService,
                        val metafileService: IMetafileService,
                        private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun uploadPortrait(username: String, file: MultipartFile): ProfileModel {

        val profile = findProfile(username)

        val resumeMetaFile = fromModel(toMetafile(username, file))
        metafileService.save(resumeMetaFile, file.inputStream)

        return toModel(profileService.portraitUploaded(profile, resumeMetaFile))
    }

    fun uploadResume(username: String, file: MultipartFile): ProfileModel {

        val profile = findProfile(username)

        val resumeMetaFile = fromModel(toMetafile(username, file))
        metafileService.save(resumeMetaFile, file.inputStream)

        return toModel(profileService.resumeUploaded(profile, resumeMetaFile))
    }

    fun uploadRef(username: String, file: MultipartFile): ProfileModel {

        val profile = findProfile(username)

        val metaFile = fromModel(toMetafile(username, file))
        metafileService.save(metaFile, file.inputStream)

        return toModel(profileService.refUploaded(profile, metaFile))
    }

    private fun findProfile(username: String): ProfileDomain {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        val profile = this.profileService.findByUsername(username)
                .orElseThrow { NotFoundException("username $username not found") }
        return profile
    }

}