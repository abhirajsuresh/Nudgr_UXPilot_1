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
import com.nudgr.data.local.entity.SessionEvent;
import com.nudgr.data.local.entity.SessionEventType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.Integer;
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
public final class SessionEventDao_Impl implements SessionEventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SessionEvent> __insertionAdapterOfSessionEvent;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<SessionEvent> __deletionAdapterOfSessionEvent;

  private final SharedSQLiteStatement __preparedStmtOfDeleteEventsBySessionId;

  public SessionEventDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSessionEvent = new EntityInsertionAdapter<SessionEvent>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `session_events` (`id`,`sessionId`,`eventType`,`timestamp`,`data`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SessionEvent value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getSessionId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSessionId());
        }
        if (value.getEventType() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, __SessionEventType_enumToString(value.getEventType()));
        }
        final Long _tmp = __converters.dateToTimestamp(value.getTimestamp());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.getData() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getData());
        }
      }
    };
    this.__deletionAdapterOfSessionEvent = new EntityDeletionOrUpdateAdapter<SessionEvent>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `session_events` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SessionEvent value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteEventsBySessionId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM session_events WHERE sessionId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertEvent(final SessionEvent event,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSessionEvent.insert(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertEvents(final List<SessionEvent> events,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSessionEvent.insert(events);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEvent(final SessionEvent event,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSessionEvent.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteEventsBySessionId(final String sessionId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteEventsBySessionId.acquire();
        int _argIndex = 1;
        if (sessionId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, sessionId);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteEventsBySessionId.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<SessionEvent>> getEventsBySessionId(final String sessionId) {
    final String _sql = "SELECT * FROM session_events WHERE sessionId = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (sessionId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sessionId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"session_events"}, new Callable<List<SessionEvent>>() {
      @Override
      public List<SessionEvent> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
          final List<SessionEvent> _result = new ArrayList<SessionEvent>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final SessionEvent _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpSessionId;
            if (_cursor.isNull(_cursorIndexOfSessionId)) {
              _tmpSessionId = null;
            } else {
              _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
            }
            final SessionEventType _tmpEventType;
            _tmpEventType = __SessionEventType_stringToEnum(_cursor.getString(_cursorIndexOfEventType));
            final Date _tmpTimestamp;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if(_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpTimestamp = _tmp_1;
            }
            final String _tmpData;
            if (_cursor.isNull(_cursorIndexOfData)) {
              _tmpData = null;
            } else {
              _tmpData = _cursor.getString(_cursorIndexOfData);
            }
            _item = new SessionEvent(_tmpId,_tmpSessionId,_tmpEventType,_tmpTimestamp,_tmpData);
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
  public Flow<List<SessionEvent>> getEventsBySessionAndType(final String sessionId,
      final SessionEventType eventType) {
    final String _sql = "SELECT * FROM session_events WHERE sessionId = ? AND eventType = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (sessionId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sessionId);
    }
    _argIndex = 2;
    if (eventType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, __SessionEventType_enumToString(eventType));
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"session_events"}, new Callable<List<SessionEvent>>() {
      @Override
      public List<SessionEvent> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
          final List<SessionEvent> _result = new ArrayList<SessionEvent>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final SessionEvent _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpSessionId;
            if (_cursor.isNull(_cursorIndexOfSessionId)) {
              _tmpSessionId = null;
            } else {
              _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
            }
            final SessionEventType _tmpEventType;
            _tmpEventType = __SessionEventType_stringToEnum(_cursor.getString(_cursorIndexOfEventType));
            final Date _tmpTimestamp;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if(_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpTimestamp = _tmp_1;
            }
            final String _tmpData;
            if (_cursor.isNull(_cursorIndexOfData)) {
              _tmpData = null;
            } else {
              _tmpData = _cursor.getString(_cursorIndexOfData);
            }
            _item = new SessionEvent(_tmpId,_tmpSessionId,_tmpEventType,_tmpTimestamp,_tmpData);
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
  public Object getLatestEventBySessionAndType(final String sessionId,
      final SessionEventType eventType, final Continuation<? super SessionEvent> continuation) {
    final String _sql = "SELECT * FROM session_events WHERE sessionId = ? AND eventType = ? ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (sessionId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sessionId);
    }
    _argIndex = 2;
    if (eventType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, __SessionEventType_enumToString(eventType));
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SessionEvent>() {
      @Override
      public SessionEvent call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
          final SessionEvent _result;
          if(_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpSessionId;
            if (_cursor.isNull(_cursorIndexOfSessionId)) {
              _tmpSessionId = null;
            } else {
              _tmpSessionId = _cursor.getString(_cursorIndexOfSessionId);
            }
            final SessionEventType _tmpEventType;
            _tmpEventType = __SessionEventType_stringToEnum(_cursor.getString(_cursorIndexOfEventType));
            final Date _tmpTimestamp;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfTimestamp);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if(_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpTimestamp = _tmp_1;
            }
            final String _tmpData;
            if (_cursor.isNull(_cursorIndexOfData)) {
              _tmpData = null;
            } else {
              _tmpData = _cursor.getString(_cursorIndexOfData);
            }
            _result = new SessionEvent(_tmpId,_tmpSessionId,_tmpEventType,_tmpTimestamp,_tmpData);
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
  public Object getEventCountBySessionAndType(final String sessionId,
      final SessionEventType eventType, final Continuation<? super Integer> continuation) {
    final String _sql = "SELECT COUNT(*) FROM session_events WHERE sessionId = ? AND eventType = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (sessionId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sessionId);
    }
    _argIndex = 2;
    if (eventType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, __SessionEventType_enumToString(eventType));
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if(_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __SessionEventType_enumToString(final SessionEventType _value) {
    if (_value == null) {
      return null;
    } switch (_value) {
      case SESSION_STARTED: return "SESSION_STARTED";
      case SESSION_PAUSED: return "SESSION_PAUSED";
      case SESSION_RESUMED: return "SESSION_RESUMED";
      case SESSION_ENDED: return "SESSION_ENDED";
      case SESSION_CANCELLED: return "SESSION_CANCELLED";
      case SCREEN_UNLOCKED: return "SCREEN_UNLOCKED";
      case SCREEN_LOCKED: return "SCREEN_LOCKED";
      case REMINDER_SHOWN: return "REMINDER_SHOWN";
      case ACTION_LOCK: return "ACTION_LOCK";
      case ACTION_SNOOZE: return "ACTION_SNOOZE";
      case ACTION_EXTEND: return "ACTION_EXTEND";
      case IMAGE_SELECTED: return "IMAGE_SELECTED";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private SessionEventType __SessionEventType_stringToEnum(final String _value) {
    if (_value == null) {
      return null;
    } switch (_value) {
      case "SESSION_STARTED": return SessionEventType.SESSION_STARTED;
      case "SESSION_PAUSED": return SessionEventType.SESSION_PAUSED;
      case "SESSION_RESUMED": return SessionEventType.SESSION_RESUMED;
      case "SESSION_ENDED": return SessionEventType.SESSION_ENDED;
      case "SESSION_CANCELLED": return SessionEventType.SESSION_CANCELLED;
      case "SCREEN_UNLOCKED": return SessionEventType.SCREEN_UNLOCKED;
      case "SCREEN_LOCKED": return SessionEventType.SCREEN_LOCKED;
      case "REMINDER_SHOWN": return SessionEventType.REMINDER_SHOWN;
      case "ACTION_LOCK": return SessionEventType.ACTION_LOCK;
      case "ACTION_SNOOZE": return SessionEventType.ACTION_SNOOZE;
      case "ACTION_EXTEND": return SessionEventType.ACTION_EXTEND;
      case "IMAGE_SELECTED": return SessionEventType.IMAGE_SELECTED;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
