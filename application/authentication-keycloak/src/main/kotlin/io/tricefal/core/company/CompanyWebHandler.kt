package io.tricefal.core.company

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
class CompanyWebHandler(val companyService: ICompanyService,
                        val metafileService: IMetafileService,
                        private final val env: Environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val targetEnv = env.getProperty("target.env")!!
    private val dataFilesPath = env.getProperty("data.files.path")!!
    private var backendBaseUrl = env.getProperty("core.baseUrl")!!
    private var emailFrom = env.getProperty("notification.mail.from")!!
    private var emailAdmin = env.getProperty("notification.mail.admin")!!
    private var smsFrom = env.getProperty("notification.sms.twilio.phoneNumber")!!
    private var smsAdmin = env.getProperty("notification.sms.admin")!!

    fun create(company: CompanyModel): CompanyModel {
        val domain = fromModel(company)
        val result = try {
            companyService.create(domain)
        } catch (ex: DuplicateException) {
            throw GlobalConflictException("company already existed with username ${company.nomCommercial} with $company", ex)
        } catch (ex: Throwable) {
            throw CompanyCreationException("Failed to create a company with username ${company.nomCommercial}", ex)
        }
        return toModel(result)
    }

    fun update(name: String, company: CompanyModel): CompanyModel {
        val result = try {
            val domain = fromModel(company)
            companyService.update(name, domain)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $name", ex)
        } catch (ex: Throwable) {
            throw CompanyUpdateException("Failed to update a company with username $name with $company", ex)
        }
        return toModel(result)
    }

    fun patch(name: String, operations: List<PatchOperation>): CompanyModel {
        val result = try {
            companyService.patch(name, operations)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $name", ex)
        } catch (ex: Throwable) {
            throw CompanyUpdateException("Failed to create a company with username $name with operations ${operations.joinToString()}", ex)
        }
        return toModel(result)
    }

    fun findByName(companyName: String): CompanyModel {
        if (companyName.isEmpty()) throw throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "company not found with username $companyName")
        val domain = try {
            this.companyService.findByName(companyName)
        } catch (ex: Throwable) {
            throw GlobalNotFoundException("Failed to find a company with username $companyName", ex)
        }
        return toModel(domain)
    }

    fun completed(companyName: String): CompanyModel {
        val result = try {
            val metaNotification = MetaNotificationDomain(
                targetEnv=targetEnv, baseUrl=backendBaseUrl,
                emailFrom=emailFrom, emailAdmin=emailAdmin,
                smsFrom=smsFrom, smsAdminNumber=smsAdmin)
            companyService.completed(companyName, metaNotification)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $companyName", ex)
        } catch (ex: Throwable) {
            throw CompanyCompletedException("Failed to complete a company with username $companyName", ex)
        }
        return toModel(result)
    }

    fun uploadKbis(companyName: String, file: MultipartFile): CompanyModel {
        val metaFile = fromModel(toMetafile(companyName, file, dataFilesPath, Representation.KBIS))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            companyService.updateOnKbisUploaded(companyName, metaFile.filename, metaFile.creationDate!!)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $companyName", ex)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the kbis document for user $companyName")
            throw CompanyUploadException("Failed to upload the kbis document for user $companyName", ex)
        }
        logger.info("successfully upload the kbis document for user $companyName")
        return toModel(result)
    }

    fun uploadRib(companyName: String, file: MultipartFile): CompanyModel {
        val metaFile = fromModel(toMetafile(companyName, file, dataFilesPath, Representation.RIB))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            companyService.updateOnRibUploaded(companyName, metaFile.filename, metaFile.creationDate!!)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $companyName", ex)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the rib document for user $companyName")
            throw CompanyUploadException("Failed to upload the rib document for user $companyName", ex)
        }
        logger.info("successfully upload the rib document for user $companyName")
        return toModel(result)
    }

    fun uploadRc(companyName: String, file: MultipartFile): CompanyModel {
        val metaFile = fromModel(toMetafile(companyName, file, dataFilesPath, Representation.RC))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            companyService.updateOnRcUploaded(companyName, metaFile.filename, metaFile.creationDate!!)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $companyName", ex)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the rc document for user $companyName")
            throw CompanyUploadException("Failed to upload the rc document for user $companyName", ex)
        }
        logger.info("successfully upload the rc document for user $companyName")
        return toModel(result)
    }

    fun uploadUrssaf(companyName: String, file: MultipartFile): CompanyModel {
        val metaFile = fromModel(toMetafile(companyName, file, dataFilesPath, Representation.URSSAF))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            companyService.updateOnUrssafUploaded(companyName, metaFile.filename, metaFile.creationDate!!)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $companyName", ex)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the urssaaf document for user $companyName")
            throw CompanyUploadException("Failed to upload the urssaaf document for user $companyName", ex)
        }
        logger.info("successfully upload the urssaaf document for user $companyName")
        return toModel(result)
    }

    fun uploadFiscal(companyName: String, file: MultipartFile): CompanyModel {
        val metaFile = fromModel(toMetafile(companyName, file, dataFilesPath, Representation.FISCAL))
        metafileService.save(metaFile, file.inputStream)

        val result = try {
            companyService.updateOnFiscalUploaded(companyName, metaFile.filename, metaFile.creationDate!!)
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $companyName", ex)
        } catch (ex: Throwable) {
            logger.error("Failed to upload the fiscal document for user $companyName")
            throw CompanyUploadException("Failed to upload the fiscal document for user $companyName", ex)
        }
        logger.info("successfully upload the fiscal document for user $companyName")
        return toModel(result)
    }

    fun kbis(companyName: String): MetafileModel {
        return try {
                metafileService.findByUsername(companyName, Representation.KBIS)
                    .map { toModel(it) }
                    .first()
            } catch (ex: NotFoundException) {
                throw GlobalNotFoundException("company not found with username $companyName", ex)
            }
    }

    fun rib(companyName: String): MetafileModel {
        return try {
             metafileService.findByUsername(companyName, Representation.RIB)
                .map { toModel(it) }
                .first()
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $companyName", ex)
        }
    }

    fun rc(companyName: String): MetafileModel {
        return try {
             metafileService.findByUsername(companyName, Representation.RC)
                .map { toModel(it) }
                .first()
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $companyName", ex)
        }
    }

    fun urssaf(companyName: String): MetafileModel {
        return try {
            metafileService.findByUsername(companyName, Representation.URSSAF)
                .map { toModel(it) }
                .first()
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $companyName", ex)
        }
    }

    fun fiscal(companyName: String): MetafileModel {
        return try {
            metafileService.findByUsername(companyName, Representation.FISCAL)
                .map { toModel(it) }
                .first()
        } catch (ex: NotFoundException) {
            throw GlobalNotFoundException("company not found with username $companyName", ex)
        }
    }

    class CompanyCreationException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class CompanyUpdateException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class CompanyUploadException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
    class CompanyCompletedException(val s: String?, val ex: Throwable?) : Throwable(s, ex) {
        constructor(message: String?) : this(message, null)
    }
}