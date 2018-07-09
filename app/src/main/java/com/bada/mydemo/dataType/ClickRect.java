package com.bada.mydemo.dataType;

import org.opencv.core.Point;

public class ClickRect extends RandomRect {

    public ClickRect(int x, int y, int width, int height, String tag){
        super(x, y, width, height);
        this.tag = tag;
    }

    public ClickRect(int x, int y, int width, int height){
        super(x, y, width, height);
    }

    String ocrText;

    String buttonText;
    Point center;

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getOcrText() {
        return ocrText;
    }

    public void setOcrText(String ocrText) {
        this.ocrText = ocrText;
    }
}
