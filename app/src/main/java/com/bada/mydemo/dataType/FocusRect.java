package com.bada.mydemo.dataType;

public class FocusRect {

    int topX;
    int topY;
    int width;
    int height;

    public FocusRect(int topX, int topY, int width, int height){
        this.topX = topX;
        this.topY = topY;
        this.width = width;
        this.height = height;
    }

    public int getTopX() {
        return topX;
    }

    public int getTopY() {
        return topY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
