package com.example.tasfri;

public class Assignment {
    private String FreqStart, FreqEnd, FreqStartEnd, Aplikasi, Instansi, StartDate, EndDate, PrimarySecondary, Satuan, Keterangan;
    private String freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan;

    public Assignment(){}

    public Assignment(String freqStart, String freqEnd, String freqStartEnd, String aplikasi, String instansi, String startDate, String endDate, String primarySecondary, String satuan, String keterangan, String freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan) {
        this.FreqStart = freqStart;
        this.FreqEnd = freqEnd;
        this.FreqStartEnd = freqStartEnd;
        this.Aplikasi = aplikasi;
        this.Instansi = instansi;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.PrimarySecondary = primarySecondary;
        this.Satuan = satuan;
        this.Keterangan = keterangan;
        this.freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan = freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan;
    }

    public String getFreqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan() {
        return freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan;
    }

    public void setFreqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan(String freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan) {
        this.freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan = freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan;
    }

    public Assignment(String freqStartEnd, String aplikasi, String instansi, String startDate, String endDate, String primarySecondary, String satuan, String keterangan, String freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan) {
        this.FreqStartEnd = freqStartEnd;
        this.Aplikasi = aplikasi;
        this.Instansi = instansi;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.PrimarySecondary = primarySecondary;
        this.Satuan = satuan;
        this.Keterangan = keterangan;
        this.freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan = freqStartEnd_satuan_aplikasi_instansi_startDate_endDate_primarySecondary_keterangan;
    }

    public String getFreqStart() {
        return FreqStart;
    }

    public void setFreqStart(String freqStart) {
        this.FreqStart = freqStart;
    }

    public String getFreqEnd() {
        return FreqEnd;
    }

    public void setFreqEnd(String freqEnd) {
        this.FreqEnd = freqEnd;
    }

    public String getFreqStartEnd() {
        return FreqStartEnd;
    }

    public void setFreqStartEnd(String freqStartEnd) {
        this.FreqStartEnd = freqStartEnd;
    }

    public String getAplikasi() {
        return Aplikasi;
    }

    public void setAplikasi(String aplikasi) {
        this.Aplikasi = aplikasi;
    }

    public String getInstansi() {
        return Instansi;
    }

    public void setInstansi(String instansi) {
        this.Instansi = instansi;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        this.StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        this.EndDate = endDate;
    }

    public String getPrimarySecondary() {
        return PrimarySecondary;
    }

    public void setPrimarySecondary(String primarySecondary) {
        this.PrimarySecondary = primarySecondary;
    }

    public String getSatuan() {
        return Satuan;
    }

    public void setSatuan(String satuan) {
        this.Satuan = satuan;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.Keterangan = keterangan;
    }
}
