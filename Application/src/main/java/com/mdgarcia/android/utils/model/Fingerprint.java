package com.mdgarcia.android.utils.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "fingerprints")
public class Fingerprint {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "accepted")
    private boolean accepted;
    @ColumnInfo(name = "valid")
    private boolean valid;
    @ColumnInfo(name = "read")
    private boolean read;
    @ColumnInfo(name = "timestamp")
    private Long timestamp;

    public Fingerprint(boolean accepted, boolean valid, boolean read) {
        this.accepted = accepted;
        this.valid = valid;
        this.timestamp = new Date().getTime();
        this.read = read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        Date date = new Date(timestamp);

        return "Fecha: " + date.toString() + "\nAceptada: " + accepted + "\nValida: " + valid + "\nLeída: " + read;
    }
}
