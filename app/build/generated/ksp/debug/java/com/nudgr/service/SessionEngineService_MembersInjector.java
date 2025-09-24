package com.nudgr.service;

import com.nudgr.data.repository.ImageRepository;
import com.nudgr.data.repository.SessionRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class SessionEngineService_MembersInjector implements MembersInjector<SessionEngineService> {
  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<ImageRepository> imageRepositoryProvider;

  public SessionEngineService_MembersInjector(Provider<SessionRepository> sessionRepositoryProvider,
      Provider<ImageRepository> imageRepositoryProvider) {
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.imageRepositoryProvider = imageRepositoryProvider;
  }

  public static MembersInjector<SessionEngineService> create(
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<ImageRepository> imageRepositoryProvider) {
    return new SessionEngineService_MembersInjector(sessionRepositoryProvider, imageRepositoryProvider);
  }

  @Override
  public void injectMembers(SessionEngineService instance) {
    injectSessionRepository(instance, sessionRepositoryProvider.get());
    injectImageRepository(instance, imageRepositoryProvider.get());
  }

  @InjectedFieldSignature("com.nudgr.service.SessionEngineService.sessionRepository")
  public static void injectSessionRepository(SessionEngineService instance,
      SessionRepository sessionRepository) {
    instance.sessionRepository = sessionRepository;
  }

  @InjectedFieldSignature("com.nudgr.service.SessionEngineService.imageRepository")
  public static void injectImageRepository(SessionEngineService instance,
      ImageRepository imageRepository) {
    instance.imageRepository = imageRepository;
  }
}
