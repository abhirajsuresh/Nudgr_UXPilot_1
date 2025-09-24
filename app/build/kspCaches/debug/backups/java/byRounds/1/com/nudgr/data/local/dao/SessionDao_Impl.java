package com.nudgr.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nudgr.data.local.database.Converters;
import com.nudgr.data.local.entity.Session;
import com.nudgr.data.local.entity.SessionStatus;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SessionDao_Impl implements SessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Session> __insertionAdapterOfSession;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Session> __deletionAdapterOfSession;

  private final EntityDeletionOrUpdateAdapter<Session> __updateAdapterOfSession;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSessionById;

  public SessionDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSession = new EntityInsertionAdapter<Session>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `sessions` (`id`,`plannedDurationMs`,`actualDurationMs`,`reminderIntervalMs`,`startTime`,`endTime`,`status`,`totalUnlocks`,`totalReminders`,`totalSnoozes`,`totalLocks`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Session value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        stmt.bindLong(2, value.getPlannedDurationMs());
        stmt.bindLong(3, value.getActualDurationMs());
        stmt.bindLong(4, value.getReminderIntervalMs());
        final Long _tmp = __converters.dateToTimestamp(value.getStartTime());
        if (_tmp == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(value.getEndTime());
        if (_tmp_1 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp_1);
        }
        if (value.getStatus() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, __SessionStatus_enumToString(value.getStatus()));
        }
        stmt.bindLong(8, value.getTotalUnlocks());
        stmt.bindLong(9, value.getTotalReminders());
        stmt.bindLong(10, value.getTotalSnoozes());
        stmt.bindLong(11, value.getTotalLocks());
      }
    };
    this.__deletionAdapterOfSession = new EntityDeletionOrUpdateAdapter<Session>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `sessions` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Session value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfSession = new EntityDeletionOrUpdateAdapter<Session>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `sessions` SET `id` = ?,`plannedDurationMs` = ?,`actualDurationMs` = ?,`reminderIntervalMs` = ?,`startTime` = ?,`endTime` = ?,`status` = ?,`totalUnlocks` = ?,`totalReminders` = ?,`totalSnoozes` = ?,`totalLocks` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Session value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        stmt.bindLong(2, value.getPlannedDurationMs());
        stmt.bindLong(3, value.getActualDurationMs());
        stmt.bindLong(4, value.getReminderIntervalMs());
        final Long _tmp = __converters.dateToTimestamp(value.getStartTime());
        if (_tmp == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(value.getEndTime());
        if (_tmp_1 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp_1);
        }
        if (value.getStatus() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, __SessionStatus_enumToString(value.getStatus()));
        }
        stmt.bindLong(8, value.getTotalUnlocks());
        stmt.bindLong(9, value.getTotalReminders());
        stmt.bindLong(10, value.getTotalSnoozes());
        stmt.bindLong(11, value.getTotalLocks());
        if (value.getId() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteSessionById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM sessions WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSession(final Session session,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSession.insert(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteSession(final Session session,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSession.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateSession(final Session session,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSession.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteSessionById(final String id, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSessionById.acquire();
        int _argIndex = 1;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteSessionById.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Session>> getAllSessions() {
    final String _sql = "SELECT * FROM sessions ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"sessions"}, new Callable<List<Session>>() {
      @Override
      public List<Session> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlannedDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedDurationMs");
          final int _cursorIndexOfActualDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "actualDurationMs");
          final int _cursorIndexOfReminderIntervalMs = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderIntervalMs");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTotalUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUnlocks");
          final int _cursorIndexOfTotalReminders = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReminders");
          final int _cursorIndexOfTotalSnoozes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSnoozes");
          final int _cursorIndexOfTotalLocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalLocks");
          final List<Session> _result = new ArrayList<Session>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Session _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final long _tmpPlannedDurationMs;
            _tmpPlannedDurationMs = _cursor.getLong(_cursorIndexOfPlannedDurationMs);
            final long _tmpActualDurationMs;
            _tmpActualDurationMs = _cursor.getLong(_cursorIndexOfActualDurationMs);
            final long _tmpReminderIntervalMs;
            _tmpReminderIntervalMs = _cursor.getLong(_cursorIndexOfReminderIntervalMs);
            final Date _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if(_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpStartTime = _tmp_1;
            }
            final Date _tmpEndTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_2);
            final SessionStatus _tmpStatus;
            _tmpStatus = __SessionStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final int _tmpTotalUnlocks;
            _tmpTotalUnlocks = _cursor.getInt(_cursorIndexOfTotalUnlocks);
            final int _tmpTotalReminders;
            _tmpTotalReminders = _cursor.getInt(_cursorIndexOfTotalReminders);
            final int _tmpTotalSnoozes;
            _tmpTotalSnoozes = _cursor.getInt(_cursorIndexOfTotalSnoozes);
            final int _tmpTotalLocks;
            _tmpTotalLocks = _cursor.getInt(_cursorIndexOfTotalLocks);
            _item = new Session(_tmpId,_tmpPlannedDurationMs,_tmpActualDurationMs,_tmpReminderIntervalMs,_tmpStartTime,_tmpEndTime,_tmpStatus,_tmpTotalUnlocks,_tmpTotalReminders,_tmpTotalSnoozes,_tmpTotalLocks);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getSessionById(final String id, final Continuation<? super Session> continuation) {
    final String _sql = "SELECT * FROM sessions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Session>() {
      @Override
      public Session call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlannedDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedDurationMs");
          final int _cursorIndexOfActualDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "actualDurationMs");
          final int _cursorIndexOfReminderIntervalMs = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderIntervalMs");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTotalUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUnlocks");
          final int _cursorIndexOfTotalReminders = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReminders");
          final int _cursorIndexOfTotalSnoozes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSnoozes");
          final int _cursorIndexOfTotalLocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalLocks");
          final Session _result;
          if(_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final long _tmpPlannedDurationMs;
            _tmpPlannedDurationMs = _cursor.getLong(_cursorIndexOfPlannedDurationMs);
            final long _tmpActualDurationMs;
            _tmpActualDurationMs = _cursor.getLong(_cursorIndexOfActualDurationMs);
            final long _tmpReminderIntervalMs;
            _tmpReminderIntervalMs = _cursor.getLong(_cursorIndexOfReminderIntervalMs);
            final Date _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if(_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpStartTime = _tmp_1;
            }
            final Date _tmpEndTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_2);
            final SessionStatus _tmpStatus;
            _tmpStatus = __SessionStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final int _tmpTotalUnlocks;
            _tmpTotalUnlocks = _cursor.getInt(_cursorIndexOfTotalUnlocks);
            final int _tmpTotalReminders;
            _tmpTotalReminders = _cursor.getInt(_cursorIndexOfTotalReminders);
            final int _tmpTotalSnoozes;
            _tmpTotalSnoozes = _cursor.getInt(_cursorIndexOfTotalSnoozes);
            final int _tmpTotalLocks;
            _tmpTotalLocks = _cursor.getInt(_cursorIndexOfTotalLocks);
            _result = new Session(_tmpId,_tmpPlannedDurationMs,_tmpActualDurationMs,_tmpReminderIntervalMs,_tmpStartTime,_tmpEndTime,_tmpStatus,_tmpTotalUnlocks,_tmpTotalReminders,_tmpTotalSnoozes,_tmpTotalLocks);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getLatestSessionByStatus(final SessionStatus status,
      final Continuation<? super Session> continuation) {
    final String _sql = "SELECT * FROM sessions WHERE status = ? ORDER BY startTime DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (status == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, __SessionStatus_enumToString(status));
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Session>() {
      @Override
      public Session call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlannedDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedDurationMs");
          final int _cursorIndexOfActualDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "actualDurationMs");
          final int _cursorIndexOfReminderIntervalMs = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderIntervalMs");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTotalUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUnlocks");
          final int _cursorIndexOfTotalReminders = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReminders");
          final int _cursorIndexOfTotalSnoozes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSnoozes");
          final int _cursorIndexOfTotalLocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalLocks");
          final Session _result;
          if(_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final long _tmpPlannedDurationMs;
            _tmpPlannedDurationMs = _cursor.getLong(_cursorIndexOfPlannedDurationMs);
            final long _tmpActualDurationMs;
            _tmpActualDurationMs = _cursor.getLong(_cursorIndexOfActualDurationMs);
            final long _tmpReminderIntervalMs;
            _tmpReminderIntervalMs = _cursor.getLong(_cursorIndexOfReminderIntervalMs);
            final Date _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if(_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpStartTime = _tmp_1;
            }
            final Date _tmpEndTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_2);
            final SessionStatus _tmpStatus;
            _tmpStatus = __SessionStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final int _tmpTotalUnlocks;
            _tmpTotalUnlocks = _cursor.getInt(_cursorIndexOfTotalUnlocks);
            final int _tmpTotalReminders;
            _tmpTotalReminders = _cursor.getInt(_cursorIndexOfTotalReminders);
            final int _tmpTotalSnoozes;
            _tmpTotalSnoozes = _cursor.getInt(_cursorIndexOfTotalSnoozes);
            final int _tmpTotalLocks;
            _tmpTotalLocks = _cursor.getInt(_cursorIndexOfTotalLocks);
            _result = new Session(_tmpId,_tmpPlannedDurationMs,_tmpActualDurationMs,_tmpReminderIntervalMs,_tmpStartTime,_tmpEndTime,_tmpStatus,_tmpTotalUnlocks,_tmpTotalReminders,_tmpTotalSnoozes,_tmpTotalLocks);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getCurrentSession(final Continuation<? super Session> continuation) {
    final String _sql = "SELECT * FROM sessions WHERE status IN ('RUNNING', 'PAUSED') ORDER BY startTime DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Session>() {
      @Override
      public Session call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlannedDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedDurationMs");
          final int _cursorIndexOfActualDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "actualDurationMs");
          final int _cursorIndexOfReminderIntervalMs = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderIntervalMs");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTotalUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUnlocks");
          final int _cursorIndexOfTotalReminders = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReminders");
          final int _cursorIndexOfTotalSnoozes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSnoozes");
          final int _cursorIndexOfTotalLocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalLocks");
          final Session _result;
          if(_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final long _tmpPlannedDurationMs;
            _tmpPlannedDurationMs = _cursor.getLong(_cursorIndexOfPlannedDurationMs);
            final long _tmpActualDurationMs;
            _tmpActualDurationMs = _cursor.getLong(_cursorIndexOfActualDurationMs);
            final long _tmpReminderIntervalMs;
            _tmpReminderIntervalMs = _cursor.getLong(_cursorIndexOfReminderIntervalMs);
            final Date _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if(_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpStartTime = _tmp_1;
            }
            final Date _tmpEndTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_2);
            final SessionStatus _tmpStatus;
            _tmpStatus = __SessionStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final int _tmpTotalUnlocks;
            _tmpTotalUnlocks = _cursor.getInt(_cursorIndexOfTotalUnlocks);
            final int _tmpTotalReminders;
            _tmpTotalReminders = _cursor.getInt(_cursorIndexOfTotalReminders);
            final int _tmpTotalSnoozes;
            _tmpTotalSnoozes = _cursor.getInt(_cursorIndexOfTotalSnoozes);
            final int _tmpTotalLocks;
            _tmpTotalLocks = _cursor.getInt(_cursorIndexOfTotalLocks);
            _result = new Session(_tmpId,_tmpPlannedDurationMs,_tmpActualDurationMs,_tmpReminderIntervalMs,_tmpStartTime,_tmpEndTime,_tmpStatus,_tmpTotalUnlocks,_tmpTotalReminders,_tmpTotalSnoozes,_tmpTotalLocks);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Session>> getSessionsByDateRange(final Date startDate, final Date endDate) {
    final String _sql = "SELECT * FROM sessions WHERE startTime >= ? AND startTime <= ? ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Long _tmp = __converters.dateToTimestamp(startDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    final Long _tmp_1 = __converters.dateToTimestamp(endDate);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"sessions"}, new Callable<List<Session>>() {
      @Override
      public List<Session> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlannedDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedDurationMs");
          final int _cursorIndexOfActualDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "actualDurationMs");
          final int _cursorIndexOfReminderIntervalMs = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderIntervalMs");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTotalUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUnlocks");
          final int _cursorIndexOfTotalReminders = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReminders");
          final int _cursorIndexOfTotalSnoozes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSnoozes");
          final int _cursorIndexOfTotalLocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalLocks");
          final List<Session> _result = new ArrayList<Session>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Session _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final long _tmpPlannedDurationMs;
            _tmpPlannedDurationMs = _cursor.getLong(_cursorIndexOfPlannedDurationMs);
            final long _tmpActualDurationMs;
            _tmpActualDurationMs = _cursor.getLong(_cursorIndexOfActualDurationMs);
            final long _tmpReminderIntervalMs;
            _tmpReminderIntervalMs = _cursor.getLong(_cursorIndexOfReminderIntervalMs);
            final Date _tmpStartTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfStartTime);
            }
            final Date _tmp_3 = __converters.fromTimestamp(_tmp_2);
            if(_tmp_3 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpStartTime = _tmp_3;
            }
            final Date _tmpEndTime;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_4);
            final SessionStatus _tmpStatus;
            _tmpStatus = __SessionStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final int _tmpTotalUnlocks;
            _tmpTotalUnlocks = _cursor.getInt(_cursorIndexOfTotalUnlocks);
            final int _tmpTotalReminders;
            _tmpTotalReminders = _cursor.getInt(_cursorIndexOfTotalReminders);
            final int _tmpTotalSnoozes;
            _tmpTotalSnoozes = _cursor.getInt(_cursorIndexOfTotalSnoozes);
            final int _tmpTotalLocks;
            _tmpTotalLocks = _cursor.getInt(_cursorIndexOfTotalLocks);
            _item = new Session(_tmpId,_tmpPlannedDurationMs,_tmpActualDurationMs,_tmpReminderIntervalMs,_tmpStartTime,_tmpEndTime,_tmpStatus,_tmpTotalUnlocks,_tmpTotalReminders,_tmpTotalSnoozes,_tmpTotalLocks);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Session>> getSessionsByDate(final Date date) {
    final String _sql = "SELECT * FROM sessions WHERE DATE(startTime/1000, 'unixepoch') = DATE(?/1000, 'unixepoch') ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final Long _tmp = __converters.dateToTimestamp(date);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"sessions"}, new Callable<List<Session>>() {
      @Override
      public List<Session> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlannedDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "plannedDurationMs");
          final int _cursorIndexOfActualDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "actualDurationMs");
          final int _cursorIndexOfReminderIntervalMs = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderIntervalMs");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTotalUnlocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUnlocks");
          final int _cursorIndexOfTotalReminders = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReminders");
          final int _cursorIndexOfTotalSnoozes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSnoozes");
          final int _cursorIndexOfTotalLocks = CursorUtil.getColumnIndexOrThrow(_cursor, "totalLocks");
          final List<Session> _result = new ArrayList<Session>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Session _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final long _tmpPlannedDurationMs;
            _tmpPlannedDurationMs = _cursor.getLong(_cursorIndexOfPlannedDurationMs);
            final long _tmpActualDurationMs;
            _tmpActualDurationMs = _cursor.getLong(_cursorIndexOfActualDurationMs);
            final long _tmpReminderIntervalMs;
            _tmpReminderIntervalMs = _cursor.getLong(_cursorIndexOfReminderIntervalMs);
            final Date _tmpStartTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfStartTime);
            }
            final Date _tmp_2 = __converters.fromTimestamp(_tmp_1);
            if(_tmp_2 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpStartTime = _tmp_2;
            }
            final Date _tmpEndTime;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_3);
            final SessionStatus _tmpStatus;
            _tmpStatus = __SessionStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final int _tmpTotalUnlocks;
            _tmpTotalUnlocks = _cursor.getInt(_cursorIndexOfTotalUnlocks);
            final int _tmpTotalReminders;
            _tmpTotalReminders = _cursor.getInt(_cursorIndexOfTotalReminders);
            final int _tmpTotalSnoozes;
            _tmpTotalSnoozes = _cursor.getInt(_cursorIndexOfTotalSnoozes);
            final int _tmpTotalLocks;
            _tmpTotalLocks = _cursor.getInt(_cursorIndexOfTotalLocks);
            _item = new Session(_tmpId,_tmpPlannedDurationMs,_tmpActualDurationMs,_tmpReminderIntervalMs,_tmpStartTime,_tmpEndTime,_tmpStatus,_tmpTotalUnlocks,_tmpTotalReminders,_tmpTotalSnoozes,_tmpTotalLocks);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __SessionStatus_enumToString(final SessionStatus _value) {
    if (_value == null) {
      return null;
    } switch (_value) {
      case OFF: return "OFF";
      case RUNNING: return "RUNNING";
      case PAUSED: return "PAUSED";
      case COMPLETED: return "COMPLETED";
      case CANCELLED: return "CANCELLED";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private SessionStatus __SessionStatus_stringToEnum(final String _value) {
    if (_value == null) {
      return null;
    } switch (_value) {
      case "OFF": return SessionStatus.OFF;
      case "RUNNING": return SessionStatus.RUNNING;
      case "PAUSED": return SessionStatus.PAUSED;
      case "COMPLETED": return SessionStatus.COMPLETED;
      case "CANCELLED": return SessionStatus.CANCELLED;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
