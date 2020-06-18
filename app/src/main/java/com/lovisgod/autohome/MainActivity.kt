package com.lovisgod.autohome

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.*
import kotlin.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var iintent: Intent
    private lateinit var client : MqttAndroidClient
    val publisher = MqttPublisher()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createClient()
        iintent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        iintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        iintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        val testButton = findViewById<ImageView>(R.id.test_mic)
        testButton.setOnClickListener {
            getSpeech()
        }


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)


        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener {})

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener( object : RecognitionListener{
            override fun onReadyForSpeech(params: Bundle?) {

            }

            override fun onRmsChanged(rmsdB: Float) {

            }

            override fun onBufferReceived(buffer: ByteArray?) {

            }

            override fun onPartialResults(partialResults: Bundle?) {

            }

            override fun onEvent(eventType: Int, params: Bundle?) {

            }

            override fun onBeginningOfSpeech() {
                println("this is the beggining")
            }

            override fun onEndOfSpeech() {
              println("Ijkdjfkjkfjk")
            }

            override fun onError(error: Int) {

            }

            override fun onResults(results: Bundle?) {
                val result =results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result !=null) {
                    val string = result.get(0)
                    println(string)
                    if (string.contains("turn on socket")) {
                        textToSpeech.speak(
                            "Ok, I will turn it on now",
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null)
                        publisher.publishSocketOne(client, "1")
                    }
                    if (string.contains("turn off socket")) {
                        textToSpeech.speak(
                            "Ok, I will turn it off now",
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null)
                        publisher.publishSocketOne(client, "0")
                    }
                }
            }

        })


//        speechRecognizer.startListening(iintent)
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getSpeech() {

        if (iintent.resolveActivity(packageManager) != null) {
            println("this is it")
//            startActivityForResult(iintent, 10)
            textToSpeech.speak(
                "Hi, I am Mike Please tell me what you want me to do.",
                TextToSpeech.QUEUE_FLUSH,
                null,
                null)

            try {
                Thread.sleep(3000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            speechRecognizer.startListening(iintent)
        }
        else {
            Toast.makeText(this, "not possible", Toast.LENGTH_LONG).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            10 -> {
                if (resultCode == Activity.RESULT_OK &&  data != null) {
                    val result =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    Toast.makeText(this, result?.get(0), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createClient() {
        val clientId = MqttClient.generateClientId()
        println(clientId)
        client =  MqttAndroidClient(applicationContext, "tcp://io.adafruit.com:1883",  clientId)
        val options = MqttConnectOptions()
        options.keepAliveInterval = 60;//seconds
        options.isCleanSession = true;
        options.isAutomaticReconnect = true;
        val key = "aio_dGro818gXJm3K8mKLDmd6QZ7iTyf"
        options.mqttVersion = MqttConnectOptions.MQTT_VERSION_DEFAULT
        options.userName = "Nsisong"
        options.password = key.toCharArray()

        try {
            val token = client.connect(options)
            println(client.isConnected)
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // We are connected
                    println("success")
                    println(client.isConnected)
                    println(token.topics)
                    Log.d("TAG", "onSuccess")
                }

                override fun onFailure(
                    asyncActionToken: IMqttToken,
                    exception: Throwable
                ) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    println(exception.localizedMessage)
                    println("failure")
                    Log.d("TAG", "onFailure")
                }
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
            e.printStackTrace()
        }
    }
}
