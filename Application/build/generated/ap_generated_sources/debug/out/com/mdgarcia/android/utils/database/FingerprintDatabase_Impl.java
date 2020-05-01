package com.mdgarcia.android.utils.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import com.mdgarcia.android.utils.dao.FingerprintDao;
import com.mdgarcia.android.utils.dao.FingerprintDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class FingerprintDatabase_Impl extends FingerprintDatabase {
  private volatile FingerprintDao _fingerprintDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `fingerprints` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `accepted` INTEGER NOT NULL, `valid` INTEGER NOT NULL, `read` INTEGER NOT NULL, `timestamp` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"e12e35c1d8005f6cd3332574ccb52f23\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `fingerprints`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsFingerprints = new HashMap<String, TableInfo.Column>(5);
        _columnsFingerprints.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsFingerprints.put("accepted", new TableInfo.Column("accepted", "INTEGER", true, 0));
        _columnsFingerprints.put("valid", new TableInfo.Column("valid", "INTEGER", true, 0));
        _columnsFingerprints.put("read", new TableInfo.Column("read", "INTEGER", true, 0));
        _columnsFingerprints.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFingerprints = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFingerprints = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFingerprints = new TableInfo("fingerprints", _columnsFingerprints, _foreignKeysFingerprints, _indicesFingerprints);
        final TableInfo _existingFingerprints = TableInfo.read(_db, "fingerprints");
        if (! _infoFingerprints.equals(_existingFingerprints)) {
          throw new IllegalStateException("Migration didn't properly handle fingerprints(com.mdgarcia.android.utils.model.Fingerprint).\n"
                  + " Expected:\n" + _infoFingerprints + "\n"
                  + " Found:\n" + _existingFingerprints);
        }
      }
    }, "e12e35c1d8005f6cd3332574ccb52f23", "104cba0312abbcf245549c9eb7260c94");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "fingerprints");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `fingerprints`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public FingerprintDao fingerprintDao() {
    if (_fingerprintDao != null) {
      return _fingerprintDao;
    } else {
      synchronized(this) {
        if(_fingerprintDao == null) {
          _fingerprintDao = new FingerprintDao_Impl(this);
        }
        return _fingerprintDao;
      }
    }
  }
}
