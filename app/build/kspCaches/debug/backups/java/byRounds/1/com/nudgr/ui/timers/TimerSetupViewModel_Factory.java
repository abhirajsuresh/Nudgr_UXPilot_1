package com.nudgr.ui.timers;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class TimerSetupViewModel_Factory implements Factory<TimerSetupViewModel> {
  @Override
  public TimerSetupViewModel get() {
    return newInstance();
  }

  public static TimerSetupViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TimerSetupViewModel newInstance() {
    return new TimerSetupViewModel();
  }

  private static final class InstanceHolder {
    private static final TimerSetupViewModel_Factory INSTANCE = new TimerSetupViewModel_Factory();
  }
}
