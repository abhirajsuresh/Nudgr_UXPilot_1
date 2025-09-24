package com.nudgr.ui.reminder;

import android.content.Context;
import com.nudgr.data.repository.ImageRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ReminderViewModel_Factory implements Factory<ReminderViewModel> {
  private final Provider<ImageRepository> imageRepositoryProvider;

  private final Provider<Context> contextProvider;

  public ReminderViewModel_Factory(Provider<ImageRepository> imageRepositoryProvider,
      Provider<Context> contextProvider) {
    this.imageRepositoryProvider = imageRepositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ReminderViewModel get() {
    return newInstance(imageRepositoryProvider.get(), contextProvider.get());
  }

  public static ReminderViewModel_Factory create(Provider<ImageRepository> imageRepositoryProvider,
      Provider<Context> contextProvider) {
    return new ReminderViewModel_Factory(imageRepositoryProvider, contextProvider);
  }

  public static ReminderViewModel newInstance(ImageRepository imageRepository, Context context) {
    return new ReminderViewModel(imageRepository, context);
  }
}
