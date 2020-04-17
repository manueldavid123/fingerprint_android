package com.mdgarcia.android.utils.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "fingerprints")
public class Fingerprint {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "accepted")
    private boolean accepted;
    @ColumnInfo(name = "timestamp")
    private Long timestamp;

    public Fingerprint(boolean accepted) {
        this.accepted = accepted;
        this.timestamp = new Date().getTime();
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

    @Override
    public String toString() {
        return "Fingerprint{" +
                "id=" + id +
                ", accepted=" + accepted +
                ", timestamp=" + timestamp +
                '}';
    }
}
