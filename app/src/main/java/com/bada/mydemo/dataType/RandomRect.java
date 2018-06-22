package com.bada.mydemo.dataType;

import java.util.Random;

public class RandomRect extends FocusRect{

    public RandomRect(){
        super(0, 0, 0, 0);
    }

    public RandomRect(int topX, int topY, int width, int height) {
        super(topX, topY, width, height);
    }

    public String getClickPoint() {
        Random rand = new Random();

        int x = rand.nextInt(width) + topX;
        int y = rand.nextInt(height) + topY;

        return x + " " + y;
    }


}
