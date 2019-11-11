package com.example.tasfri;

public class Aplikasi {

    private String application;
    private String freqStart;
    private String freqEnd;
    private String freqStartEnd;
    private String freqRange;
    private String satuan;
    private String footnote;
    private String freqStartEnd_satuan_application_footnote;

    public Aplikasi(){}

    public Aplikasi(String application, String freqStart, String freqEnd, String freqStartEnd, String freqRange, String satuan, String footnote, String freqStartEnd_satuan_application_footnote) {
        this.application = application;
        this.freqStart = freqStart;
        this.freqEnd = freqEnd;
        this.freqStartEnd = freqStartEnd;
        this.freqRange = freqRange;
        this.satuan = satuan;
        this.footnote = footnote;
        this.freqStartEnd_satuan_application_footnote = freqStartEnd_satuan_application_footnote;
    }

    public String getFreqRange() {
        return freqRange;
    }

    public void setFreqRange(String freqRange) {
        this.freqRange = freqRange;
    }

    public String getFreqStartEnd_satuan_application_footnote() {
        return freqStartEnd_satuan_application_footnote;
    }

    public void setFreqStartEnd_satuan_application_footnote(String freqStartEnd_satuan_application_footnote) {
        this.freqStartEnd_satuan_application_footnote = freqStartEnd_satuan_application_footnote;
    }

    public String getFootnote() {
        return footnote;
    }

    public void setFootnote(String footnote) {
        this.footnote = footnote;
    }
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getFreqStart() {
        return freqStart;
    }

    public void setFreqStart(String freqStart) {
        this.freqStart = freqStart;
    }

    public String getFreqEnd() {
        return freqEnd;
    }

    public void setFreqEnd(String freqEnd) {
        this.freqEnd = freqEnd;
    }

    public String getFreqStartEnd() {
        return freqStartEnd;
    }

    public void setFreqStartEnd(String freqStartEnd) {
        this.freqStartEnd = freqStartEnd;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}
