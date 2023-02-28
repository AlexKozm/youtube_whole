package com.example.utils


import com.example.Strings.RabbitConsts
import com.example.model.dtos.RabbitDTO
import com.fasterxml.jackson.databind.ObjectMapper
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import io.micronaut.context.annotation.Context
import org.slf4j.LoggerFactory

@Context
class RabbitMqUtil {
    private val log = LoggerFactory.getLogger(javaClass)
    private var channel: Channel? = null

    init {
        connect()
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
        log.info("RabbitMQ initialized")
    }

    fun send(rabbitDTO: RabbitDTO) {
        if (channel == null) {
            connect()
        }
        log.info("Sending message to rabbitmq")
//        val message =  Json.encodeToString(rabbitDTO)
        val message = ObjectMapper().writeValueAsString(rabbitDTO)
        log.info("Message to rabbit: \n $message")
        channel?.basicPublish("", RabbitConsts.QUEUE_NAME, null, message.toByteArray())
        log.info("Message sent to rabbitmq")
    }


}