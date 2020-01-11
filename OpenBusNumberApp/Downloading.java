package com.example.realopencv;

import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;




public class Downloading extends Thread {


    private static int  percent = 0;
    private static long totalSize = 0;

    public static MainActivity link;


    private final static String[] fileNames = { "BUSNUM.pb", "busNet.cfg", "busNet.weights"};

    private final static long[] fileSize = { 93477633, 2028, 34732716};

    private final static String[] filePath = {
            "https://drive.google.com/uc?export=download&id=10egK9kMxIUxjAQAkxnVrkQw_RF1bidck",
            "https://drive.google.com/uc?export=download&id=1notSQVi-QRv10IihK47GNKp_h_PqtJIi",
            "https://drive.google.com/uc?export=download&id=1XiWxu3y0Kja9rKnD-RkiZw-k9hFpLs7J"
    };

    private static File loadFile;
    private static boolean[] exist;
    private static long loaded = 0;
    public static boolean notLoaded = false;


    public static void checkFiles(){

        exist = new boolean[fileNames.length];

        for(int i = 0; i < fileNames.length; i++){


            File file = new File(link.getFilesDir() + "/" + fileNames[i]);

            if(!file.exists()){

                    totalSize = totalSize + fileSize[i];
                    exist[i] = true;
                    notLoaded = true;


            }else{
                if(file.length() != fileSize[i]){

                    totalSize = totalSize + fileSize[i];
                    exist[i] = true;
                    notLoaded = true;

                }
            }

        }

        if(notLoaded){
            new Downloading().start();
        }

    }

    private static void getStateLoading(){

        MainActivity.sayToLog("getStateLoading();");

        if (loadFile != null) {
            int currentPercent = (int) ((loadFile.length() + loaded) * 100 / totalSize);

            if (currentPercent != percent) {

                percent = currentPercent;
                if(!SpeechGenerator.isPlaying())
                    SpeechGenerator.generateSpeech(percent + "%");
            }
        }

    }

    public static void startLoging(){

        Thread myThready = new Thread(new Runnable() {
            public void run() //Этот метод будет выполняться в побочном потоке
            {
                while (notLoaded) {

                    getStateLoading();
                    MainActivity.pause(5000);

                }
            }
        });

        myThready.start();

    }

    private static void onDownloadComplete(boolean success) {
        if(success) {

            SpeechGenerator.playOnSuccess();
            notLoaded = false;
            loadFile = null;
            link.loadNets();

        } else {

            SpeechGenerator.playGuide(SpeechGenerator.CONECTION_ERROR);
            checkFiles();
            MainActivity.pause(5000);

        }

    }

    @Override
    public void run() {

        try {


            for(int i = 0; i < fileNames.length; i++){

                if(exist[i]){

                    String destFileName = fileNames[i];
                    String src = filePath[i];
                    loadFile = new File(link.getFilesDir() + "/" + destFileName);

                    FileUtils.copyURLToFile(new URL(src), loadFile);
                    loaded = loaded + loadFile.length();
                }

            }



            onDownloadComplete(true);


        } catch (IOException e) {
            e.printStackTrace();
            onDownloadComplete(false);

        }
    }
}