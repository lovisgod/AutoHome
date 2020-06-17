package com.lovisgod.autohome

import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
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
            client.publish(topic, message).actionCallback  = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // We are connected
                    Log.d("TAG", "publish successful")
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    println(exception.localizedMessage)
                    Log.d("TAG", "publish failed")
                }
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}