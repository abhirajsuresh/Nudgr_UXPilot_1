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
public final class DeviceAdminReceiver_MembersInjector implements MembersInjector<DeviceAdminReceiver> {
  private final Provider<SessionController> sessionControllerProvider;

  public DeviceAdminReceiver_MembersInjector(
      Provider<SessionController> sessionControllerProvider) {
    this.sessionControllerProvider = sessionControllerProvider;
  }

  public static MembersInjector<DeviceAdminReceiver> create(
      Provider<SessionController> sessionControllerProvider) {
    return new DeviceAdminReceiver_MembersInjector(sessionControllerProvider);
  }

  @Override
  public void injectMembers(DeviceAdminReceiver instance) {
    injectSessionController(instance, sessionControllerProvider.get());
  }

  @InjectedFieldSignature("com.nudgr.receiver.DeviceAdminReceiver.sessionController")
  public static void injectSessionController(DeviceAdminReceiver instance,
      SessionController sessionController) {
    instance.sessionController = sessionController;
  }
}
