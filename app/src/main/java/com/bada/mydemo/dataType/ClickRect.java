package com.bada.mydemo.dataType;

import com.baidu.ocr.sdk.model.Word;

import org.opencv.core.Point;

public class ClickRect extends RandomRect {

    public ClickRect(int x, int y, int width, int height, String tag){
        super(x, y, width, height);
        this.tag = tag;
    }

    public ClickRect(int x, int y, int width, int height){
        super(x, y, width, height);
    }

    public ClickRect(Word word){
        this.topX = word.getLocation().getLeft();
        this.topY = word.getLocation().getTop();
        this.width = word.getLocation().getWidth();
        this.height = word.getLocation().getHeight();
    }

    String ocrText;

    String buttonText;

    Point center;
    int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

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
