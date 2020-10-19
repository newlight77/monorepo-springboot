package io.tricefal.core.freelance

import io.tricefal.core.exception.NotAcceptedException
import io.tricefal.core.exception.NotFoundException
import io.tricefal.core.metafile.IMetafileService
import io.tricefal.core.metafile.Representation
import io.tricefal.core.metafile.fromModel
import io.tricefal.core.metafile.toMetafile
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*


@Service
@PropertySource("classpath:application.yml")
class FreelanceWebHandler(val freelanceService: IFreelanceService,
                          val metafileService: IMetafileService,
                          private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val dataFilesPath = env.getProperty("data.files.path")!!

    val locale: Locale = LocaleContextHolder.getLocale()

    fun create(freelanceModel: FreelanceModel): FreelanceModel {
        val domain = fromModel(freelanceModel)
        val result = freelanceService.create(domain)
        return toModel(result)
    }

    fun findByUsername(username: String): FreelanceModel {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        return toModel(freelanceService.findByUsername(username))
    }

    fun findAll(): List<FreelanceModel> {
        return freelanceService.findAll().map { freelanceDomain -> toModel(freelanceDomain) }
    }

    fun uploadKbis(username: String, file: MultipartFile): FreelanceModel {
        val domain = freelanceService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.KBIS))
        metafileService.save(metaFile, file.inputStream)

        return toModel(freelanceService.kbisUploaded(domain, metaFile))
    }

    fun uploadRib(username: String, file: MultipartFile): FreelanceModel {
        val domain = freelanceService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.RIB))
        metafileService.save(metaFile, file.inputStream)

        return toModel(freelanceService.ribUploaded(domain, metaFile))
    }

    fun uploadRc(username: String, file: MultipartFile): FreelanceModel {
        val domain = freelanceService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.RC))
        metafileService.save(metaFile, file.inputStream)

        return toModel(freelanceService.rcUploaded(domain, metaFile))
    }

    fun uploadUrssaf(username: String, file: MultipartFile): FreelanceModel {
        val domain = freelanceService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.URSSAF))
        metafileService.save(metaFile, file.inputStream)

        return toModel(freelanceService.urssafUploaded(domain, metaFile))
    }

    fun uploadFiscal(username: String, file: MultipartFile): FreelanceModel {
        val domain = freelanceService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.FISCAL))
        metafileService.save(metaFile, file.inputStream)

        return toModel(freelanceService.urssafUploaded(domain, metaFile))
    }


}