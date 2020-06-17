package com.lovisgod.autohome

import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttException

import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.UnsupportedEncodingException


class MqttPublisher {

    fun publishSocketOne (client: MqttAndroidClient, event: String) {
        val topic = "socket1"
        val encodedPayload: ByteArray
        try {
            encodedPayload = event.toByteArray(charset("UTF-8"))
            val message = MqttMessage(encodedPayload)
            message.isRetained = true
            client.publish(topic, message)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}