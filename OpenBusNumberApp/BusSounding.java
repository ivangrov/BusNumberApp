package com.example.realopencv;

import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;

public class BusSounding {

    private final static int CLOSE_DOOR = 0, OPEN_DOOR = 1, BUS = 2, NUM = 3;

    private static class Number {

        public String num = "0";
        public Rect rect;

    }

    public static class Bus {

        public Rect rect ;
        public ArrayList<String> numberWay = new ArrayList<>();
        public ArrayList<Integer> doorsOpen = new ArrayList<>();
        public ArrayList<Integer> doorsClose = new ArrayList<>();

    }

    private static ArrayList <Rect> rectBuses = new ArrayList<>();
    private static ArrayList <Rect> rectOpenDoors = new ArrayList<>();
    private static ArrayList <Rect> rectCloseDoors = new ArrayList<>();
    private static ArrayList <Number> nums = new ArrayList<>();

    private static ArrayList <Bus> buses = new ArrayList<>();










    private static Point getPointByRect(Rect rect){

        Point point = new Point();

        point.x = rect.x + rect.width / 2;
        point.y = rect.y + rect.height / 2;

        return point;

    }

    private static boolean crossRects(Rect rect, Point point){

        return (
            point.x > rect.x &&
            point.x < rect.x + rect.width &&
            point.y > rect.y &&
            point.y < rect.y + rect.height
        );

    }

    private static void sortBus() {

        while (rectBuses.size() != 0) {
            Bus bus = new Bus();

            bus.rect = rectBuses.get(0);

            for (int i = 0; i < rectOpenDoors.size(); i++) {

                Point objectPoint = getPointByRect(rectOpenDoors.get(i));

                if(crossRects(rectBuses.get(0), objectPoint)){

                    //при нахождении объекта внутри коробки
                    bus.doorsOpen.add((int)objectPoint.x);

                }

            }

            for (int i = 0; i < rectCloseDoors.size(); i++) {

                Point objectPoint = getPointByRect(rectCloseDoors.get(i));

                if(crossRects(rectBuses.get(0), objectPoint)){

                    //при нахождении объекта внутри коробки
                    bus.doorsClose.add((int)objectPoint.x);

                }

            }

            for (int i = 0; i < nums.size(); i++) {

                Point objectPoint = getPointByRect(nums.get(i).rect);

                if(crossRects(rectBuses.get(0), objectPoint)){

                    //при нахождении объекта внутри коробки

                    bus.numberWay.add(nums.get(i).num);

                }

            }


            buses.add(bus);
            rectBuses.remove(0);

        }

    }




    public static void playBuses(){

        //ScSystem.IsNetPrepared = false;
        String str = "";

        sortBus();

        while (buses.size() > 0){

            str += SpeechGenerator.getBus(buses.get(0));

            buses.remove(0);

        }

        buses.clear();
        rectCloseDoors.clear();
        rectOpenDoors.clear();
        nums.clear();
        rectBuses.clear();

        SpeechGenerator.generateSpeech(str);

    }



    public static void addElement(Rect box , int id) {

        switch (id){

            case BUS :
                rectBuses.add(box);
                break;

            case OPEN_DOOR :
                rectOpenDoors.add(box);
                break;

            case CLOSE_DOOR :
                rectCloseDoors.add(box);
                
        }

    }

    public static void addNum(Rect box, String number){

        Number numberBox = new Number();

        numberBox.num = number;

        numberBox.rect = box;

        nums.add(numberBox);

    }

}
