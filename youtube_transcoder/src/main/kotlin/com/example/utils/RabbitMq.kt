package com.example.utils

import com.example.Strings.RabbitConsts
import com.example.model.dtos.RabbitDTO
import com.example.services.Transcoder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import io.micronaut.context.annotation.Context
import org.slf4j.LoggerFactory
import ws.schild.jave.info.VideoSize


@Context
class RabbitMq(
    private val transcoder: Transcoder,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private var channel: Channel? = null
    private var deliverCallback: DeliverCallback? = null
    private val objectMapper = ObjectMapper()
    init {
        connect()
        addCallback()
    }
    private fun connect() {
        log.info("RabbitMQ initializing started")
        val factory = ConnectionFactory()
        factory.host = RabbitConsts.RABBIT_HOST
        try {
            val connection = factory.newConnection()
            channel = connection.createChannel()
        } catch (exception: Exception) {
            log.error(exception.message)
            throw Exception("Can't connect to rabbit")
        }
        channel?.queueDeclare(RabbitConsts.QUEUE_NAME, false, false, false, null)
        log.info("RabbitMQ initialized. Channel: ${channel.toString()}")
    }

    private fun addCallback(){
         deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
             val message = String(delivery.body, charset("UTF-8"))
             val messageObject: RabbitDTO = objectMapper.readValue(message)
             log.info("Message from rabbitmq: \n$message")
             transcoder.transcode(messageObject, VideoSize(1920, 1080))
             transcoder.transcode(messageObject, VideoSize(1280 , 720))
             transcoder.transcode(messageObject, VideoSize(640 , 480))
         }
        channel?.basicConsume(RabbitConsts.QUEUE_NAME, true, deliverCallback) { consumerTag: String? -> }
    }
}