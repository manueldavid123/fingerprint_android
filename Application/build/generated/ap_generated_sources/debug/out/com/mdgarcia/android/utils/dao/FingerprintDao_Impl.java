package com.mdgarcia.android.utils.dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import com.mdgarcia.android.utils.model.Fingerprint;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;

@SuppressWarnings("unchecked")
public class FingerprintDao_Impl implements FingerprintDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfFingerprint;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfFingerprint;

  public FingerprintDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFingerprint = new EntityInsertionAdapter<Fingerprint>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `fingerprints`(`id`,`accepted`,`valid`,`read`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Fingerprint value) {
        stmt.bindLong(1, value.getId());
        final int _tmp;
        _tmp = value.isAccepted() ? 1 : 0;
        stmt.bindLong(2, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isValid() ? 1 : 0;
        stmt.bindLong(3, _tmp_1);
        final int _tmp_2;
        _tmp_2 = value.isRead() ? 1 : 0;
        stmt.bindLong(4, _tmp_2);
        if (value.getTimestamp() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getTimestamp());
        }
      }
    };
    this.__deletionAdapterOfFingerprint = new EntityDeletionOrUpdateAdapter<Fingerprint>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `fingerprints` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Fingerprint value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public void insertFingerprints(Fingerprint... fingerprints) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfFingerprint.insert(fingerprints);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteFingerprints(Fingerprint... fingerprints) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfFingerprint.handleMultiple(fingerprints);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Fingerprint[] getAll() {
    final String _sql = "SELECT * FROM fingerprints";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAccepted = _cursor.getColumnIndexOrThrow("accepted");
      final int _cursorIndexOfValid = _cursor.getColumnIndexOrThrow("valid");
      final int _cursorIndexOfRead = _cursor.getColumnIndexOrThrow("read");
      final int _cursorIndexOfTimestamp = _cursor.getColumnIndexOrThrow("timestamp");
      final Fingerprint[] _result = new Fingerprint[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final Fingerprint _item;
        final boolean _tmpAccepted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfAccepted);
        _tmpAccepted = _tmp != 0;
        final boolean _tmpValid;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfValid);
        _tmpValid = _tmp_1 != 0;
        final boolean _tmpRead;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfRead);
        _tmpRead = _tmp_2 != 0;
        _item = new Fingerprint(_tmpAccepted,_tmpValid,_tmpRead);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final Long _tmpTimestamp;
        if (_cursor.isNull(_cursorIndexOfTimestamp)) {
          _tmpTimestamp = null;
        } else {
          _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        }
        _item.setTimestamp(_tmpTimestamp);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Fingerprint[] getAccepted() {
    final String _sql = "SELECT * FROM fingerprints where accepted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAccepted = _cursor.getColumnIndexOrThrow("accepted");
      final int _cursorIndexOfValid = _cursor.getColumnIndexOrThrow("valid");
      final int _cursorIndexOfRead = _cursor.getColumnIndexOrThrow("read");
      final int _cursorIndexOfTimestamp = _cursor.getColumnIndexOrThrow("timestamp");
      final Fingerprint[] _result = new Fingerprint[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final Fingerprint _item;
        final boolean _tmpAccepted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfAccepted);
        _tmpAccepted = _tmp != 0;
        final boolean _tmpValid;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfValid);
        _tmpValid = _tmp_1 != 0;
        final boolean _tmpRead;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfRead);
        _tmpRead = _tmp_2 != 0;
        _item = new Fingerprint(_tmpAccepted,_tmpValid,_tmpRead);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final Long _tmpTimestamp;
        if (_cursor.isNull(_cursorIndexOfTimestamp)) {
          _tmpTimestamp = null;
        } else {
          _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        }
        _item.setTimestamp(_tmpTimestamp);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Fingerprint[] getNotAccepted() {
    final String _sql = "SELECT * FROM fingerprints where accepted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAccepted = _cursor.getColumnIndexOrThrow("accepted");
      final int _cursorIndexOfValid = _cursor.getColumnIndexOrThrow("valid");
      final int _cursorIndexOfRead = _cursor.getColumnIndexOrThrow("read");
      final int _cursorIndexOfTimestamp = _cursor.getColumnIndexOrThrow("timestamp");
      final Fingerprint[] _result = new Fingerprint[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final Fingerprint _item;
        final boolean _tmpAccepted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfAccepted);
        _tmpAccepted = _tmp != 0;
        final boolean _tmpValid;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfValid);
        _tmpValid = _tmp_1 != 0;
        final boolean _tmpRead;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfRead);
        _tmpRead = _tmp_2 != 0;
        _item = new Fingerprint(_tmpAccepted,_tmpValid,_tmpRead);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final Long _tmpTimestamp;
        if (_cursor.isNull(_cursorIndexOfTimestamp)) {
          _tmpTimestamp = null;
        } else {
          _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        }
        _item.setTimestamp(_tmpTimestamp);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Fingerprint[] getAcceptedValid() {
    final String _sql = "SELECT * FROM fingerprints where accepted = 1 and valid = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAccepted = _cursor.getColumnIndexOrThrow("accepted");
      final int _cursorIndexOfValid = _cursor.getColumnIndexOrThrow("valid");
      final int _cursorIndexOfRead = _cursor.getColumnIndexOrThrow("read");
      final int _cursorIndexOfTimestamp = _cursor.getColumnIndexOrThrow("timestamp");
      final Fingerprint[] _result = new Fingerprint[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final Fingerprint _item;
        final boolean _tmpAccepted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfAccepted);
        _tmpAccepted = _tmp != 0;
        final boolean _tmpValid;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfValid);
        _tmpValid = _tmp_1 != 0;
        final boolean _tmpRead;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfRead);
        _tmpRead = _tmp_2 != 0;
        _item = new Fingerprint(_tmpAccepted,_tmpValid,_tmpRead);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final Long _tmpTimestamp;
        if (_cursor.isNull(_cursorIndexOfTimestamp)) {
          _tmpTimestamp = null;
        } else {
          _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        }
        _item.setTimestamp(_tmpTimestamp);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Fingerprint[] getAcceptedNotValid() {
    final String _sql = "SELECT * FROM fingerprints where accepted = 1 and valid = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAccepted = _cursor.getColumnIndexOrThrow("accepted");
      final int _cursorIndexOfValid = _cursor.getColumnIndexOrThrow("valid");
      final int _cursorIndexOfRead = _cursor.getColumnIndexOrThrow("read");
      final int _cursorIndexOfTimestamp = _cursor.getColumnIndexOrThrow("timestamp");
      final Fingerprint[] _result = new Fingerprint[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final Fingerprint _item;
        final boolean _tmpAccepted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfAccepted);
        _tmpAccepted = _tmp != 0;
        final boolean _tmpValid;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfValid);
        _tmpValid = _tmp_1 != 0;
        final boolean _tmpRead;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfRead);
        _tmpRead = _tmp_2 != 0;
        _item = new Fingerprint(_tmpAccepted,_tmpValid,_tmpRead);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final Long _tmpTimestamp;
        if (_cursor.isNull(_cursorIndexOfTimestamp)) {
          _tmpTimestamp = null;
        } else {
          _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        }
        _item.setTimestamp(_tmpTimestamp);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Fingerprint[] getNotAcceptedNotValid() {
    final String _sql = "SELECT * FROM fingerprints where accepted = 0 and valid = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAccepted = _cursor.getColumnIndexOrThrow("accepted");
      final int _cursorIndexOfValid = _cursor.getColumnIndexOrThrow("valid");
      final int _cursorIndexOfRead = _cursor.getColumnIndexOrThrow("read");
      final int _cursorIndexOfTimestamp = _cursor.getColumnIndexOrThrow("timestamp");
      final Fingerprint[] _result = new Fingerprint[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final Fingerprint _item;
        final boolean _tmpAccepted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfAccepted);
        _tmpAccepted = _tmp != 0;
        final boolean _tmpValid;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfValid);
        _tmpValid = _tmp_1 != 0;
        final boolean _tmpRead;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfRead);
        _tmpRead = _tmp_2 != 0;
        _item = new Fingerprint(_tmpAccepted,_tmpValid,_tmpRead);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final Long _tmpTimestamp;
        if (_cursor.isNull(_cursorIndexOfTimestamp)) {
          _tmpTimestamp = null;
        } else {
          _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        }
        _item.setTimestamp(_tmpTimestamp);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Fingerprint[] getNotAcceptedValid() {
    final String _sql = "SELECT * FROM fingerprints where accepted = 0 and valid = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAccepted = _cursor.getColumnIndexOrThrow("accepted");
      final int _cursorIndexOfValid = _cursor.getColumnIndexOrThrow("valid");
      final int _cursorIndexOfRead = _cursor.getColumnIndexOrThrow("read");
      final int _cursorIndexOfTimestamp = _cursor.getColumnIndexOrThrow("timestamp");
      final Fingerprint[] _result = new Fingerprint[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final Fingerprint _item;
        final boolean _tmpAccepted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfAccepted);
        _tmpAccepted = _tmp != 0;
        final boolean _tmpValid;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfValid);
        _tmpValid = _tmp_1 != 0;
        final boolean _tmpRead;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfRead);
        _tmpRead = _tmp_2 != 0;
        _item = new Fingerprint(_tmpAccepted,_tmpValid,_tmpRead);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final Long _tmpTimestamp;
        if (_cursor.isNull(_cursorIndexOfTimestamp)) {
          _tmpTimestamp = null;
        } else {
          _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        }
        _item.setTimestamp(_tmpTimestamp);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Fingerprint[] getNotRead() {
    final String _sql = "SELECT * FROM fingerprints where read = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAccepted = _cursor.getColumnIndexOrThrow("accepted");
      final int _cursorIndexOfValid = _cursor.getColumnIndexOrThrow("valid");
      final int _cursorIndexOfRead = _cursor.getColumnIndexOrThrow("read");
      final int _cursorIndexOfTimestamp = _cursor.getColumnIndexOrThrow("timestamp");
      final Fingerprint[] _result = new Fingerprint[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final Fingerprint _item;
        final boolean _tmpAccepted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfAccepted);
        _tmpAccepted = _tmp != 0;
        final boolean _tmpValid;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfValid);
        _tmpValid = _tmp_1 != 0;
        final boolean _tmpRead;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfRead);
        _tmpRead = _tmp_2 != 0;
        _item = new Fingerprint(_tmpAccepted,_tmpValid,_tmpRead);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final Long _tmpTimestamp;
        if (_cursor.isNull(_cursorIndexOfTimestamp)) {
          _tmpTimestamp = null;
        } else {
          _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        }
        _item.setTimestamp(_tmpTimestamp);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
