package com.bada.mydemo.dataType;

import java.io.Serializable;

public class FocusRect implements Serializable{
    static final long serialVersionUID = 1L;

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
