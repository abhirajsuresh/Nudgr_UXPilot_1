package com.nudgr.ui.checklist;

import com.nudgr.data.repository.ImageRepository;
import com.nudgr.data.repository.SessionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class ChecklistViewModel_Factory implements Factory<ChecklistViewModel> {
  private final Provider<ImageRepository> imageRepositoryProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  public ChecklistViewModel_Factory(Provider<ImageRepository> imageRepositoryProvider,
      Provider<SessionRepository> sessionRepositoryProvider) {
    this.imageRepositoryProvider = imageRepositoryProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
  }

  @Override
  public ChecklistViewModel get() {
    return newInstance(imageRepositoryProvider.get(), sessionRepositoryProvider.get());
  }

  public static ChecklistViewModel_Factory create(Provider<ImageRepository> imageRepositoryProvider,
      Provider<SessionRepository> sessionRepositoryProvider) {
    return new ChecklistViewModel_Factory(imageRepositoryProvider, sessionRepositoryProvider);
  }

  public static ChecklistViewModel newInstance(ImageRepository imageRepository,
      SessionRepository sessionRepository) {
    return new ChecklistViewModel(imageRepository, sessionRepository);
  }
}
