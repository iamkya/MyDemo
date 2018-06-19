package com.bada.mydemo.rect;

import java.util.Random;

public class RandomRect extends FocusRect{

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
