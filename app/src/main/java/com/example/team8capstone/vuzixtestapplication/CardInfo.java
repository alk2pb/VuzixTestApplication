package com.example.team8capstone.vuzixtestapplication;

/**
 * Class that holds all the information required to build a card
 * Field names are self explanatory
 */
public class CardInfo {
    public int slideNumber;
    public int xmlLayout;
    public int imageResource;
    public int audioResource;
    public int videoResource;
    public boolean hasImage = false;
    public boolean hasAudio = false;
    public boolean hasVideo = false;
    public boolean hasHeader = false;
    public boolean hasText = false;
    public String header;
    public int headerTextSize = 30;
    public int textSize = 25;
    public String text;


    public CardInfo(int _slideNumber, int _xmlLayout) {
        slideNumber = _slideNumber;
        xmlLayout = _xmlLayout;

    }

    public CardInfo setImageResource(int _imageResource){
        imageResource = _imageResource;
        hasImage = true;
        return this;
    }

    public CardInfo setAudioResource(int _audioResource){
        audioResource = _audioResource;
        hasAudio = true;
        return this;
    }

    public CardInfo setVideoResource(int _videoResource){
        videoResource = _videoResource;
        hasVideo = true;
        return this;
    }

    public CardInfo setHeader(String _header){
        header = _header;
        hasHeader = true;
        return this;
    }

    public CardInfo setText(String _text){
        text = _text;
        hasText = true;
        return this;
    }

    public CardInfo setTextSize(int _textSize){
        textSize = _textSize;
        return this;
    }

}
