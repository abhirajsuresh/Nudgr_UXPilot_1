package com.nudgr.data.repository;

import com.nudgr.data.local.dao.SessionDao;
import com.nudgr.data.local.dao.SessionEventDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class SessionRepository_Factory implements Factory<SessionRepository> {
  private final Provider<SessionDao> sessionDaoProvider;

  private final Provider<SessionEventDao> sessionEventDaoProvider;

  public SessionRepository_Factory(Provider<SessionDao> sessionDaoProvider,
      Provider<SessionEventDao> sessionEventDaoProvider) {
    this.sessionDaoProvider = sessionDaoProvider;
    this.sessionEventDaoProvider = sessionEventDaoProvider;
  }

  @Override
  public SessionRepository get() {
    return newInstance(sessionDaoProvider.get(), sessionEventDaoProvider.get());
  }

  public static SessionRepository_Factory create(Provider<SessionDao> sessionDaoProvider,
      Provider<SessionEventDao> sessionEventDaoProvider) {
    return new SessionRepository_Factory(sessionDaoProvider, sessionEventDaoProvider);
  }

  public static SessionRepository newInstance(SessionDao sessionDao,
      SessionEventDao sessionEventDao) {
    return new SessionRepository(sessionDao, sessionEventDao);
  }
}
