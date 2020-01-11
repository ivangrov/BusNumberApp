package com.example.realopencv;

import org.opencv.core.Rect;

import java.util.ArrayList;

public class ENG extends Locate {

    private final static String ENG = "en";

    private final static int CENTER = 0, LEFT = 1, RIGHT = 2;
    private final static String[] directions = {"in front of you", "on the left", "on the right"};

    @Override
    public String getLocateSign() {
        return ENG;
    }

    private static String getDirectionByX(int direction) {

        if(direction > 75)
            return directions[RIGHT];

        if(direction < 25)
            return directions[LEFT];

        return directions[CENTER];

    }

    public static String getNum(ArrayList<String> numArr){

        String str = "";

        if(numArr.size() > 0){
            for (int i = 0; i < numArr.size(); i++) {

                str += (i != 0) ? "or" : "";

                str += numArr.get(i);
            }
            str = "number " + str;
        }



        return str;
    }

    private String getDoorPlace(int x, Rect rect) {

        x = (x - rect.x) * 100 / rect.width;

        if(x > 75){

            return "on the right side";

        }

        if (x < 25) {

            return "on the left side";

        }

        return "in the center";

    }

    private String getDoors(BusSounding.Bus bus) {

        String str = "";

        for(int i = 0; i < bus.doorsClose.size(); i ++){

            str += getDoorPlace(bus.doorsClose.get(i), bus.rect) + " are closed doors, ";

        }

        for(int i = 0; i < bus.doorsOpen.size(); i ++){

            str += getDoorPlace(bus.doorsOpen.get(i), bus.rect) + " are opened doors, ";

        }

        return str;

    }

    @Override
    public String getBus(BusSounding.Bus bus) {
        return getDirectionByX((bus.rect.x + bus.rect.width / 2) * 100 / MainActivity.width) + " is the bus " +
                getNum(bus.numberWay) + ". " +
                getDoors(bus);
    }

    @Override
    public String getGuideMsg(int guideMsg) {
        String str = "";

        switch (guideMsg){

            case SpeechGenerator.CONECTION_ERROR : str = "In order for the modules to download your smartphone needs to be connected to the Internet";
            break;

            case SpeechGenerator.SUCCESSFUL_DOWNLOAD : str = "The additional modules have been downloaded. Please, wait a little longer while we're launching the app";
            break;

            case SpeechGenerator.BEGIN : str = "Let's get started!";
            break;

            case SpeechGenerator.GUIDE: str = "In order for the app to work properly, hold your smartphone horizontally, and point the camera towards the potential buses. The app will use the camera in real-time to find buses, their numbers, and opened or closed doors, voicing the information back to you. If the app finds two numbers in one bus, it will voice them both. The system is not perfect, so above everything else always trust your senses and intuition first, and use Bus Number App only as a source of additional information, always leaving at least one ear without the headphones.";
            break;

            case SpeechGenerator.WAIT : str = "Now the app is going to download the additional modules that are necessary for allowing the app to work. It will take the additional 100 megabytes of storage space on your smartphone. If 100 megabytes of storage space are not available on your smartphone, you can just close the app. Make sure that your device is connected to the Internet while we're downloading the modules";
            break;
        }

        return str;
    }
}
