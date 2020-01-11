package com.example.realopencv;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static com.example.realopencv.MainActivity.pause;

public class SpeechGenerator {



    public static final int CONECTION_ERROR     = 0;
    public static final int SUCCESSFUL_DOWNLOAD = 1;
    public static final int BEGIN               = 2;
    public static final int GUIDE               = 3;
    public static final int WAIT                = 4;
    //public static final int


    public static float speechSpeed = 2.5f;
    private static TextToSpeech speechGenerator;


    private static Locate locate;

    public static void playGuide(int msg){

        generateSpeech(locate.getGuideMsg(msg));

    }

    public static void playOnSuccess (){

        generateSpeech(locate.getGuideMsg(SUCCESSFUL_DOWNLOAD) + " " + locate.getGuideMsg(GUIDE));

    }

    public static void setLocate(Locate newLocate, final MainActivity link) {

        locate = newLocate;

        speechGenerator = new TextToSpeech(  link , new TextToSpeech.OnInitListener () {
            @Override
            public void onInit(int status) {

                if (status != TextToSpeech.ERROR) {
                    speechGenerator.setLanguage ( new Locale(locate.getLocateSign()) );

                }
                speechGenerator.setSpeechRate(speechSpeed);

                if(Downloading.notLoaded) {

                    generateSpeech(locate.getGuideMsg(WAIT) + ", " + locate.getGuideMsg(GUIDE));

                    File file = new File(link.getFilesDir() + "/config");

                        if(!file.exists()){
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            pause(6000);

                            //SpeechGenerator.playGuide(SpeechGenerator.GUIDE);


                    }

                }

            }
        } );

    }

    public static void generateSpeech(String str){

        speechGenerator.speak(str, TextToSpeech.QUEUE_FLUSH , null);
        Log.d("VisionAssistant", str);

    }



    public static String getBus(BusSounding.Bus bus){

        return locate.getBus(bus);

    }

    public static Boolean isPlaying(){

        return speechGenerator.isSpeaking();

    }

}
