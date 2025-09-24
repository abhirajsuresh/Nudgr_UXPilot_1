package com.nudgr.ui.images;

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
public final class ImageLibraryViewModel_Factory implements Factory<ImageLibraryViewModel> {
  private final Provider<ImageRepository> imageRepositoryProvider;

  private final Provider<Context> contextProvider;

  public ImageLibraryViewModel_Factory(Provider<ImageRepository> imageRepositoryProvider,
      Provider<Context> contextProvider) {
    this.imageRepositoryProvider = imageRepositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ImageLibraryViewModel get() {
    return newInstance(imageRepositoryProvider.get(), contextProvider.get());
  }

  public static ImageLibraryViewModel_Factory create(
      Provider<ImageRepository> imageRepositoryProvider, Provider<Context> contextProvider) {
    return new ImageLibraryViewModel_Factory(imageRepositoryProvider, contextProvider);
  }

  public static ImageLibraryViewModel newInstance(ImageRepository imageRepository,
      Context context) {
    return new ImageLibraryViewModel(imageRepository, context);
  }
}
