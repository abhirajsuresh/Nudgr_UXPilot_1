package com.nudgr.di;

import com.nudgr.data.local.dao.ImageDao;
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
public final class DatabaseModule_ProvideImageDaoFactory implements Factory<ImageDao> {
  private final Provider<NudgrDatabase> databaseProvider;

  public DatabaseModule_ProvideImageDaoFactory(Provider<NudgrDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ImageDao get() {
    return provideImageDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideImageDaoFactory create(
      Provider<NudgrDatabase> databaseProvider) {
    return new DatabaseModule_ProvideImageDaoFactory(databaseProvider);
  }

  public static ImageDao provideImageDao(NudgrDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideImageDao(database));
  }
}
