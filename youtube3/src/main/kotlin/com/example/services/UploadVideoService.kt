package com.example.services

import com.example.Strings.FilesPaths
import com.example.model.tables.OriginalVideoFileEntity
import com.example.model.dtos.RabbitDTO
import com.example.repositories.OriginalVideoFileDAO
import com.example.utils.RabbitMqUtil
import com.google.common.io.Files
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.multipart.PartData
import io.micronaut.http.multipart.StreamingFileUpload
import jakarta.inject.Singleton
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import java.io.File
import java.io.FileOutputStream
import io.micronaut.transaction.SynchronousTransactionManager
import java.sql.Connection

// https://www.bookstack.cn/read/micronaut-2.1.0-en/spilt.44.spilt.6.42816bd0e8e8c4ea.md
@Singleton
class UploadVideoService(
    private val rabbitMqUtil: RabbitMqUtil,
    private val originalVideoFileDAO: OriginalVideoFileDAO,
    //why we need thin value? we don't use it
    private val transactionManager: SynchronousTransactionManager<Connection>

) {
    private val log = LoggerFactory.getLogger(javaClass)

    private fun createFile(file: StreamingFileUpload): Pair<OriginalVideoFileEntity, File> {
        //instead of using Google @Beta unstable function it's easily can do by kotlin :
        //file.filename.substringBeforeLast(".")
        val entity = OriginalVideoFileEntity(Files.getNameWithoutExtension(file.filename),
            Files.getFileExtension(file.filename))
        originalVideoFileDAO.save(entity)
        val diskFile = File("${FilesPaths.PATH_TO_ORIGIN_FILES}/${entity.id}.${entity.extension}")
        return Pair(entity, diskFile)
    }
    fun uploadVideo(file: StreamingFileUpload): Mono<HttpResponse<String>> {
        log.info("file from video: ${file.filename}; size: ${file.size}")
        val (entity, diskFile) = createFile(file)
        val outStream = FileOutputStream(diskFile)
        //I think this pattern isn't necessary in this context
        //We need may be just use method from Files: copy()
        return Mono.create {
                emitter ->
            file.subscribe(object : Subscriber<PartData> {
                private lateinit var s: Subscription
                override fun onSubscribe(s: Subscription?) {
                    log.info("Subscription added")
                    this.s = s ?: throw Exception("Subscription failed")
                    s.request(1)
                }

                override fun onError(t: Throwable?) {
                    log.info("onError exception.")
                    outStream.close()
                    diskFile.delete()
                    originalVideoFileDAO.delete(entity)
                }

                override fun onComplete() {
                    log.info("onComplete: file ${file.filename}")
                    emitter.success(HttpResponse.status<String>(HttpStatus.OK).body("Uploaded"))
                    outStream.close()
                    //--
                    rabbitMqUtil.send(RabbitDTO(entity))
                }

                override fun onNext(partData: PartData?) {
//                    log.info("Processing part ${partCounter++}")
                    outStream.write(partData?.bytes ?: throw Exception("onNext failed"))
                    s.request(1)
                }
            })
        }
    }
}