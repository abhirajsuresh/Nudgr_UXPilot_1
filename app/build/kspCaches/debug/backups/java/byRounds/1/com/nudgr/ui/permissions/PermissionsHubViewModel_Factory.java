package com.nudgr.ui.permissions;

import android.content.Context;
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
public final class PermissionsHubViewModel_Factory implements Factory<PermissionsHubViewModel> {
  private final Provider<Context> contextProvider;

  public PermissionsHubViewModel_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PermissionsHubViewModel get() {
    return newInstance(contextProvider.get());
  }

  public static PermissionsHubViewModel_Factory create(Provider<Context> contextProvider) {
    return new PermissionsHubViewModel_Factory(contextProvider);
  }

  public static PermissionsHubViewModel newInstance(Context context) {
    return new PermissionsHubViewModel(context);
  }
}
