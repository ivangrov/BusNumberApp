package com.example.realopencv;

import org.opencv.core.Rect;

import java.util.ArrayList;

public class RUS extends Locate {

    private final static String RU = "ru";

    private final static int CENTER = 0, LEFT = 1, RIGHT = 2;
    private final static String[] directions = { "перед вами", "слева", "справа" };


    private static String getDirectionByX(int direction) {

        if (direction > 75)
            return directions[RIGHT];

        if (direction < 25)
            return directions[LEFT];

        return directions[CENTER];

    }


    private String getDoorPlace(int x, Rect rect) {

        x = (x - rect.x) * 100 / rect.width;

        if(x > 75){

            return "с правой стороны";

        }

        if (x < 25) {

            return "с левой стороны";

        }

        return "по центру";

    }

    private String getDoors(BusSounding.Bus bus) {

        String str = "";

        for(int i = 0; i < bus.doorsClose.size(); i ++){

            str += getDoorPlace(bus.doorsClose.get(i), bus.rect) + " закрытая дверь, ";

        }

        for(int i = 0; i < bus.doorsOpen.size(); i ++){

            str += getDoorPlace(bus.doorsOpen.get(i), bus.rect) + " открытая дверь, ";

        }

        return str;

    }



    public static String getNum(ArrayList<String> numArr){

        String str = "";

            if(numArr.size() > 0){
                for (int i = 0; i < numArr.size(); i++) {

                    str += (i != 0) ? "или" : "";

                    str += numArr.get(i);
                }
                str = "под номером " + str;
            }



        return str;
    }

    @Override
    public String getBus(BusSounding.Bus bus) {

        return getDirectionByX ((bus.rect.x + bus.rect.width / 2) * 100 / MainActivity.width) + " автобус" +
                getNum(bus.numberWay) + ". " +
                getDoors(bus);

    }

    @Override
    public String getGuideMsg(int guideMsg) {

        String str = "";

        switch (guideMsg){

            case SpeechGenerator.CONECTION_ERROR : str = "для того что-бы модули загрузились, " +
            "необходимо подключение к интернету";
            break;

            case SpeechGenerator.SUCCESSFUL_DOWNLOAD : str = "дополнительные модули загружены. Подождите ещё не много, мы запускаем приложение";
            break;

            case SpeechGenerator.BEGIN : str = "начнём!";
            break;

            case SpeechGenerator.GUIDE : str = "Для корректной работы приложения держите ваш смартфон горизонтально, и направляйте камеру в сторону потенциальных автобусов. Приложение с помощью камеры будет в реальном времени искать автобусы, их номера и открытые или закрытые двери. После озвучит эту информацию. Если приложение найдет два номера на одном автобусе оно назовёт их через или. Система неидеальна, поэтому прежде всего всегда доверяйте своим чувствам и интуиции и используйте приложение лишь как дополнительный источник информации, всегда оставляя минимум одно ухо без наушника. ";
            break;

            case SpeechGenerator.WAIT : str = " Сейчас мы загрузим дополнительные модули для работы приложения, включите интернет" +
                    "это займёт 100 Мегабайт памяти, если у вас мало места, то просто закройте приложение";
            break;

        }

        return str;
    }

    @Override
    public String getLocateSign() {
        return RU;
    }


}
