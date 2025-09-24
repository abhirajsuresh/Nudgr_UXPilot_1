package com.nudgr.data.repository;

import com.nudgr.data.local.dao.ImageDao;
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
public final class ImageRepository_Factory implements Factory<ImageRepository> {
  private final Provider<ImageDao> imageDaoProvider;

  public ImageRepository_Factory(Provider<ImageDao> imageDaoProvider) {
    this.imageDaoProvider = imageDaoProvider;
  }

  @Override
  public ImageRepository get() {
    return newInstance(imageDaoProvider.get());
  }

  public static ImageRepository_Factory create(Provider<ImageDao> imageDaoProvider) {
    return new ImageRepository_Factory(imageDaoProvider);
  }

  public static ImageRepository newInstance(ImageDao imageDao) {
    return new ImageRepository(imageDao);
  }
}
