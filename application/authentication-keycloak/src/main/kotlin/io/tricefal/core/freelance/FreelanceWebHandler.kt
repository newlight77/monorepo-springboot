package io.tricefal.core.freelance

import io.tricefal.core.exception.NotAcceptedException
import io.tricefal.core.exception.NotFoundException
import io.tricefal.core.metafile.*
import io.tricefal.core.profile.ProfileDomain
import io.tricefal.shared.util.PatchOperation
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

    fun create(freelanceModel: FreelanceModel): FreelanceModel {
        val domain = fromModel(freelanceModel)
        val result = try {
            freelanceService.create(domain)
        } catch (ex: Throwable) {
            throw FreelanceCreationException("Failed to create a freelance profile with username ${freelanceModel.username}")
        }
        return toModel(result)
    }

    fun update(username: String, freelanceModel: FreelanceModel): FreelanceModel {
        val result = try {
            val domain = fromModel(freelanceModel)
            freelanceService.update(username, domain)
        } catch (ex: Throwable) {
            throw FreelanceCreationException("Failed to update a freelance with username $username with $freelanceModel")
        }
        return toModel(result)
    }

    fun patch(username: String, operations: List<PatchOperation>): FreelanceModel {
        val result = try {
            freelanceService.patch(username, operations)
        } catch (ex: Throwable) {
            throw FreelanceCreationException("Failed to create a freelance profile with username $username with operations ${operations.joinToString()}")
        }
        return toModel(result)
    }

    fun findByUsername(username: String): FreelanceModel {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        return toModel(find(username))
    }

    fun findAll(): List<FreelanceModel> {
        return freelanceService.findAll().map { freelanceDomain -> toModel(freelanceDomain) }
    }

    fun availables(): List<FreelanceModel> {
        return freelanceService.availables().map { freelanceDomain -> toModel(freelanceDomain) }
    }

    fun uploadKbis(username: String, file: MultipartFile): FreelanceModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.KBIS))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.updateOnKbisUploaded(username, metaFile.filename)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the kbis document for user $username")
            throw FreelanceUploadException("Failed to upload the kbis document for user $username")
        }
        logger.info("successfully upload the kbis document for user $username")
        return toModel(result)
    }

    fun uploadRib(username: String, file: MultipartFile): FreelanceModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.RIB))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.updateOnRibUploaded(username, metaFile.filename)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the rib document for user $username")
            throw FreelanceUploadException("Failed to upload the rib document for user $username")
        }
        logger.info("successfully upload the rib document for user $username")
        return toModel(result)
    }

    fun uploadRc(username: String, file: MultipartFile): FreelanceModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.RC))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.updateOnRcUploaded(username, metaFile.filename)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the rc document for user $username")
            throw FreelanceUploadException("Failed to upload the rc document for user $username")
        }
        logger.info("successfully upload the rc document for user $username")
        return toModel(result)
    }

    fun uploadUrssaf(username: String, file: MultipartFile): FreelanceModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.URSSAF))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.updateOnUrssafUploaded(username, metaFile.filename)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the urssaaf document for user $username")
            throw FreelanceUploadException("Failed to upload the urssaaf document for user $username")
        }
        logger.info("successfully upload the urssaaf document for user $username")
        return toModel(result)
    }

    fun uploadFiscal(username: String, file: MultipartFile): FreelanceModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.FISCAL))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.updateOnFiscalUploaded(username, metaFile.filename)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the fiscal document for user $username")
            throw FreelanceUploadException("Failed to upload the fiscal document for user $username")
        }
        logger.info("successfully upload the fiscal document for user $username")
        return toModel(result)
    }

    fun kbis(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.KBIS }
    }

    fun rib(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.RIB }
    }

    fun rc(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.RC }
    }

    fun urssaf(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.URSSAF }
    }

    fun fiscal(username: String): MetafileModel {
        return metafileService.findByUsername(username)
                .map { toModel(it) }
                .first { it.representation == Representation.FISCAL }
    }

    private fun find(username: String): FreelanceDomain {
        if (username.isEmpty()) throw NotAcceptedException("username is $username")
        return this.freelanceService.findByUsername(username)
                .orElseThrow { NotFoundException("username $username not found") }
    }

    class FreelanceCreationException(private val msg: String) : Throwable(msg) {}
    class FreelanceUploadException(private val msg: String) : Throwable(msg) {}
}