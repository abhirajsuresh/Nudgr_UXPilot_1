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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<SessionController> sessionControllerProvider;

  public BootReceiver_MembersInjector(Provider<SessionController> sessionControllerProvider) {
    this.sessionControllerProvider = sessionControllerProvider;
  }

  public static MembersInjector<BootReceiver> create(
      Provider<SessionController> sessionControllerProvider) {
    return new BootReceiver_MembersInjector(sessionControllerProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectSessionController(instance, sessionControllerProvider.get());
  }

  @InjectedFieldSignature("com.nudgr.receiver.BootReceiver.sessionController")
  public static void injectSessionController(BootReceiver instance,
      SessionController sessionController) {
    instance.sessionController = sessionController;
  }
}
