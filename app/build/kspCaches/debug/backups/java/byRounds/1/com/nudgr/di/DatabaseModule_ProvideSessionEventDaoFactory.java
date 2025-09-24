package com.nudgr.di;

import com.nudgr.data.local.dao.SessionEventDao;
import com.nudgr.data.local.database.NudgrDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvideSessionEventDaoFactory implements Factory<SessionEventDao> {
  private final Provider<NudgrDatabase> databaseProvider;

  public DatabaseModule_ProvideSessionEventDaoFactory(Provider<NudgrDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SessionEventDao get() {
    return provideSessionEventDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSessionEventDaoFactory create(
      Provider<NudgrDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSessionEventDaoFactory(databaseProvider);
  }

  public static SessionEventDao provideSessionEventDao(NudgrDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSessionEventDao(database));
  }
}
