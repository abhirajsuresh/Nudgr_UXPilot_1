# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ================================================================================================
# Firebase ProGuard/R8 Rules
# ================================================================================================
# Official Firebase keep rules to preserve Analytics, Crashlytics, Auth, Firestore, and Storage
# classes and generated code during minification.
# See: https://firebase.google.com/docs/android/setup#proguard

# Firebase Authentication
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Firebase Crashlytics
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**

# Firebase Analytics
-keep class com.google.android.gms.measurement.** { *; }
-keep class com.google.firebase.analytics.** { *; }
-dontwarn com.google.android.gms.measurement.**

# Firebase Firestore
-keep class com.google.firebase.firestore.** { *; }
-keep class com.google.android.gms.internal.firebase-firestore.** { *; }
-dontwarn com.google.firebase.firestore.**

# Keep all model classes used with Firestore (adjust package name to match your models)
-keep class com.nudgr.data.model.** { *; }
-keep class com.nudgr.data.entities.** { *; }

# Firebase Storage
-keep class com.google.firebase.storage.** { *; }
-dontwarn com.google.firebase.storage.**

# Firebase Core
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Protobuf (required for Firestore and other Firebase services)
-keep class com.google.protobuf.** { *; }
-dontwarn com.google.protobuf.**

# Keep Kotlin Metadata for reflection (Firestore needs this)
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

# Keep Kotlin metadata for Firestore serialization
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }
-dontwarn kotlin.reflect.**

# Preserve generic signatures for Firestore
-keepattributes Signature

# ================================================================================================
# Hilt (already in your project)
# ================================================================================================
-dontwarn com.google.errorprone.annotations.**

# ================================================================================================
# Compose
# ================================================================================================
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

