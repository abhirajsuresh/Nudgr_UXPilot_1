package com.nudgr.di;

import android.content.Context;
import com.nudgr.data.local.database.NudgrDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideNudgrDatabaseFactory implements Factory<NudgrDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideNudgrDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public NudgrDatabase get() {
    return provideNudgrDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideNudgrDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideNudgrDatabaseFactory(contextProvider);
  }

  public static NudgrDatabase provideNudgrDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideNudgrDatabase(context));
  }
}
