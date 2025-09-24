package com.nudgr.service;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SessionController_Factory implements Factory<SessionController> {
  private final Provider<Context> contextProvider;

  public SessionController_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SessionController get() {
    return newInstance(contextProvider.get());
  }

  public static SessionController_Factory create(Provider<Context> contextProvider) {
    return new SessionController_Factory(contextProvider);
  }

  public static SessionController newInstance(Context context) {
    return new SessionController(context);
  }
}
