package com.nudgr.di;

import com.nudgr.data.local.dao.SessionDao;
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
public final class DatabaseModule_ProvideSessionDaoFactory implements Factory<SessionDao> {
  private final Provider<NudgrDatabase> databaseProvider;

  public DatabaseModule_ProvideSessionDaoFactory(Provider<NudgrDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SessionDao get() {
    return provideSessionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSessionDaoFactory create(
      Provider<NudgrDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSessionDaoFactory(databaseProvider);
  }

  public static SessionDao provideSessionDao(NudgrDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSessionDao(database));
  }
}
