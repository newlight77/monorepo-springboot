package io.tricefal.core.freelance

import io.tricefal.core.exception.GlobalConflictException
import io.tricefal.core.exception.GlobalNotFoundException
import io.tricefal.core.metafile.*
import io.tricefal.core.notification.MetaNotificationDomain
import io.tricefal.shared.util.json.PatchOperation
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException


@Service
@PropertySource("classpath:application.yml")
class FreelanceWebHandler(val freelanceService: IFreelanceService,
                          val metafileService: IMetafileService,
                          private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val dataFilesPath = env.getProperty("data.files.path")!!
    private var targetEnv = env.getProperty("target.env")!!
    private var backendBaseUrl = env.getProperty("core.baseUrl")!!
    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var emailAdmin = env.getProperty("notification.mail.admin")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!
    private var smsAdmin = env.getProperty("notification.sms.admin")!!

    fun initFreelance(freelance: FreelanceDomain) {
        try {
            freelanceService.create(freelance)
        } catch (ex: DuplicateException) {
            throw FreelanceCreationException("can not initiate the creation of freelance with username ${freelance.username} with $freelance", ex)
        }
    }

    fun create(freelanceModel: FreelanceModel): FreelanceModel {
        val domain = fromModel(freelanceModel)
        val result = try {
            freelanceService.create(domain)
        } catch (ex: DuplicateException) {
            throw GlobalConflictException("freelance already existed with username ${freelanceModel.username} with $freelanceModel", ex)
        } catch (ex: Throwable) {
            throw FreelanceCreationException("Failed to create a freelance profile with username ${freelanceModel.username}", ex)
        }
        return toModel(result)
    }

    fun update(username: String, freelanceModel: FreelanceModel): FreelanceModel {
        val result = try {
            val domain = fromModel(freelanceModel)
            freelanceService.update(username, domain)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        } catch (ex: Throwable) {
            throw FreelanceUpdateException("Failed to update a freelance with username $username with $freelanceModel", ex)
        }
        return toModel(result)
    }

    fun patch(username: String, operations: List<PatchOperation>): FreelanceModel {
        val result = try {
            freelanceService.patch(username, operations)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        } catch (ex: Throwable) {
            throw FreelanceUpdateException("Failed to create a freelance profile with username $username with operations ${operations.joinToString()}", ex)
        }
        return toModel(result)
    }

    fun findByUsername(username: String): FreelanceModel {
        if (username.isEmpty()) throw throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "freelance not found with username $username")
        val domain = try {
            this.freelanceService.findByUsername(username)
        } catch (ex: Throwable) {
            throw GlobalNotFoundException("Failed to find a freelance with username $username", ex)
        }
        return toModel(domain)
    }

    fun findAll(): List<FreelanceModel> {
        return freelanceService.findAll().map { freelanceDomain -> toModel(freelanceDomain) }
    }

    fun availables(): List<FreelanceModel> {
        return freelanceService.availables().map { freelanceDomain -> toModel(freelanceDomain) }
    }

    fun completed(username: String): FreelanceModel {
        val result = try {
            val metaNotification = MetaNotificationDomain(
                targetEnv=targetEnv, baseUrl=backendBaseUrl,
                emailFrom=emailFrom, emailAdmin=emailAdmin,
                smsFrom=smsFrom, smsAdminNumber=smsAdmin)

            freelanceService.completed(username, metaNotification)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        } catch (ex: Throwable) {
            throw FreelanceCompletedException("Failed to complete a freelance with username $username", ex)
        }
        return toModel(result)
    }

    fun uploadKbis(username: String, file: MultipartFile): FreelanceModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.KBIS))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.updateOnKbisUploaded(username, metaFile.filename, metaFile.creationDate!!)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the kbis document for user $username")
            throw FreelanceUploadException("Failed to upload the kbis document for user $username", ex)
        }
        logger.info("successfully upload the kbis document for user $username")
        return toModel(result)
    }

    fun uploadRib(username: String, file: MultipartFile): FreelanceModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.RIB))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.updateOnRibUploaded(username, metaFile.filename, metaFile.creationDate!!)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the rib document for user $username")
            throw FreelanceUploadException("Failed to upload the rib document for user $username", ex)
        }
        logger.info("successfully upload the rib document for user $username")
        return toModel(result)
    }

    fun uploadRc(username: String, file: MultipartFile): FreelanceModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.RC))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.updateOnRcUploaded(username, metaFile.filename, metaFile.creationDate!!)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the rc document for user $username")
            throw FreelanceUploadException("Failed to upload the rc document for user $username", ex)
        }
        logger.info("successfully upload the rc document for user $username")
        return toModel(result)
    }

    fun uploadUrssaf(username: String, file: MultipartFile): FreelanceModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.URSSAF))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.updateOnUrssafUploaded(username, metaFile.filename, metaFile.creationDate!!)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the urssaaf document for user $username")
            throw FreelanceUploadException("Failed to upload the urssaaf document for user $username", ex)
        }
        logger.info("successfully upload the urssaaf document for user $username")
        return toModel(result)
    }

    fun uploadFiscal(username: String, file: MultipartFile): FreelanceModel {
        val metaFile = fromModel(toMetafile(username, file, dataFilesPath, Representation.FISCAL))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            freelanceService.updateOnFiscalUploaded(username, metaFile.filename, metaFile.creationDate!!)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the fiscal document for user $username")
            throw FreelanceUploadException("Failed to upload the fiscal document for user $username", ex)
        }
        logger.info("successfully upload the fiscal document for user $username")
        return toModel(result)
    }

    fun kbis(username: String): MetafileModel {
        return try {
                metafileService.findByUsername(username, Representation.KBIS)
                    .map { toModel(it) }
                    .first()
            } catch (ex: NotFoundException) {
                throw GlobalNotFoundException("freelance not found with username $username", ex)
            }
    }

    fun rib(username: String): MetafileModel {
        return try {
             metafileService.findByUsername(username, Representation.RIB)
                .map { toModel(it) }
                .first()
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        }
    }

    fun rc(username: String): MetafileModel {
        return try {
             metafileService.findByUsername(username, Representation.RC)
                .map { toModel(it) }
                .first()
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        }
    }

    fun urssaf(username: String): MetafileModel {
        return try {
            metafileService.findByUsername(username, Representation.URSSAF)
                .map { toModel(it) }
                .first()
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        }
    }

    fun fiscal(username: String): MetafileModel {
        return try {
            metafileService.findByUsername(username, Representation.FISCAL)
                .map { toModel(it) }
                .first()
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("freelance not found with username $username", ex)
        }
    }

    class FreelanceCreationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class FreelanceUpdateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class FreelanceUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class FreelanceCompletedException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}