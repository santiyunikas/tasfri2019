package com.example.tasfri;

public interface ItemClickListener {

    void OnItemClick(int position, Assignment assData);

    void OnItemClick(int position, Aplikasi appData);

    void OnItemClick(int position, Alokasi alloData);
}
