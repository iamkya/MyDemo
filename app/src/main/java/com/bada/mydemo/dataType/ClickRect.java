package com.bada.mydemo.dataType;

public class ClickRect extends RandomRect {

    public ClickRect(int x, int y, int width, int height, String tag){
        super(x, y, width, height);
        this.tag = tag;
    }

    String buttonText;

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
}
