package com.example.tasfri;

public class Alokasi {
    private String allocation, aplikasi, freqStart, freqEnd, freqStartEnd, primarySecondary, satuan, footnote, freqRange;
    private String freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary;

    public Alokasi(){}

    public Alokasi(String allocation, String aplikasi, String freqStart, String freqEnd, String freqStartEnd, String primarySecondary, String satuan, String footnote, String freqRange, String freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary) {
        this.allocation = allocation;
        this.aplikasi = aplikasi;
        this.freqStart = freqStart;
        this.freqEnd = freqEnd;
        this.freqStartEnd = freqStartEnd;
        this.primarySecondary = primarySecondary;
        this.satuan = satuan;
        this.footnote = footnote;
        this.freqRange = freqRange;
        this.freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary = freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary;
    }

    public String getFreqRange() {
        return freqRange;
    }

    public void setFreqRange(String freqRange) {
        this.freqRange = freqRange;
    }

    public String getAllocation() {
        return allocation;
    }

    public void setAllocation(String allocation) {
        this.allocation = allocation;
    }

    public String getAplikasi() {
        return aplikasi;
    }

    public void setAplikasi(String aplikasi) {
        this.aplikasi = aplikasi;
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

    public String getPrimarySecondary() {
        return primarySecondary;
    }

    public void setPrimarySecondary(String primarySecondary) {
        this.primarySecondary = primarySecondary;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getFootnote() {
        return footnote;
    }

    public void setFootnote(String footnote) {
        this.footnote = footnote;
    }

    public String getFreqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary() {
        return freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary;
    }

    public void setFreqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary(String freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary) {
        this.freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary = freqStartEnd_satuan_allocation_aplikasi_footnote_primarySecondary;
    }
}
