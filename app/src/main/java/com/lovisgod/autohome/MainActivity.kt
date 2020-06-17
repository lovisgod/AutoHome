package com.lovisgod.autohome

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var iintent: Intent

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iintent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        iintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        iintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        val testButton = findViewById<ImageView>(R.id.test_mic)
        testButton.setOnClickListener {
            getSpeech()
        }


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)


        textToSpeech = TextToSpeech(this, TextToSpeech.OnInitListener {})

//        textToSpeech.speak(
//            "Hi I am Mike Please tell me wha you want me to do",
//            TextToSpeech.QUEUE_FLUSH,
//            null,
//            null)
//        getSpeech()
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
                    if (string.contains("turn on")) {
                        textToSpeech.speak(
                            "Ok, I will turn it on now",
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null)

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
                "Hi I am Mike Please tell me wha you want me to do",
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
}
