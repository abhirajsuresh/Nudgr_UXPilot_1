package com.nudgr.receiver;

import com.nudgr.service.SessionController;
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
public final class ScreenStateReceiver_MembersInjector implements MembersInjector<ScreenStateReceiver> {
  private final Provider<SessionController> sessionControllerProvider;

  public ScreenStateReceiver_MembersInjector(
      Provider<SessionController> sessionControllerProvider) {
    this.sessionControllerProvider = sessionControllerProvider;
  }

  public static MembersInjector<ScreenStateReceiver> create(
      Provider<SessionController> sessionControllerProvider) {
    return new ScreenStateReceiver_MembersInjector(sessionControllerProvider);
  }

  @Override
  public void injectMembers(ScreenStateReceiver instance) {
    injectSessionController(instance, sessionControllerProvider.get());
  }

  @InjectedFieldSignature("com.nudgr.receiver.ScreenStateReceiver.sessionController")
  public static void injectSessionController(ScreenStateReceiver instance,
      SessionController sessionController) {
    instance.sessionController = sessionController;
  }
}
