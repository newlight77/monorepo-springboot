package io.tricefal.core.freelance

import io.tricefal.core.exception.NotAcceptedException
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
import java.lang.Exception
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
        val result = try {
            freelanceService.create(domain)
        } catch (ex: Exception) {
            throw FreelanceCreationException("Failed to create a freelance profile with username ${freelanceModel.username}")
        }
        return toModel(result)
    }

    fun findByUsername(username: String): FreelanceModel {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        return toModel(freelanceService.findByUsername(username))
    }

    fun findAll(): List<FreelanceModel> {
        return freelanceService.findAll().map { freelanceDomain -> toModel(freelanceDomain) }
    }

    fun availables(): List<FreelanceModel> {
        return freelanceService.availables().map { freelanceDomain -> toModel(freelanceDomain) }
    }

    fun uploadKbis(username: String, file: MultipartFile): FreelanceModel {
        val domain = freelanceService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.KBIS))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.kbisUploaded(domain, metaFile)
        } catch (ex: Exception) {
            logger.error("Failed to upload the kbis document for user $username")
            throw FreelanceUploadException("Failed to upload the kbis document for user $username")
        }
        logger.info("successfully upload the kbis document for user $username")
        return toModel(result)
    }

    fun uploadRib(username: String, file: MultipartFile): FreelanceModel {
        val domain = freelanceService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.RIB))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.ribUploaded(domain, metaFile)
        } catch (ex: Exception) {
            logger.error("Failed to upload the rib document for user $username")
            throw FreelanceUploadException("Failed to upload the rib document for user $username")
        }
        logger.info("successfully upload the rib document for user $username")
        return toModel(result)
    }

    fun uploadRc(username: String, file: MultipartFile): FreelanceModel {
        val domain = freelanceService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.RC))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.rcUploaded(domain, metaFile)
        } catch (ex: Exception) {
            logger.error("Failed to upload the rc document for user $username")
            throw FreelanceUploadException("Failed to upload the rc document for user $username")
        }
        logger.info("successfully upload the rc document for user $username")
        return toModel(result)
    }

    fun uploadUrssaf(username: String, file: MultipartFile): FreelanceModel {
        val domain = freelanceService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.URSSAF))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.urssafUploaded(domain, metaFile)
        } catch (ex: Exception) {
            logger.error("Failed to upload the urssaaf document for user $username")
            throw FreelanceUploadException("Failed to upload the urssaaf document for user $username")
        }
        logger.info("successfully upload the urssaaf document for user $username")
        return toModel(result)
    }

    fun uploadFiscal(username: String, file: MultipartFile): FreelanceModel {
        val domain = freelanceService.findByUsername(username)

        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.FISCAL))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.fiscalUploaded(domain, metaFile)
        } catch (ex: Exception) {
            logger.error("Failed to upload the fiscal document for user $username")
            throw FreelanceUploadException("Failed to upload the fiscal document for user $username")
        }
        logger.info("successfully upload the fiscal document for user $username")
        return toModel(result)
    }

    class FreelanceCreationException(private val msg: String) : Throwable(msg) {}
    class FreelanceUploadException(private val msg: String) : Throwable(msg) {}
}