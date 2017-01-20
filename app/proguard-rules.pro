# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in P:\Programok\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class mName to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Add this global rule
-keepattributes Signature
-keepattributes *Annotation*

# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class com.halcyon.ubb.studentlifemanager.model.**{
  *;
}
-keep class com.google.firebase.**
-keep public class com.google.android.gms.**
-keep class org.w3c.dom**
-keep class com.google.firebase.FirebaseApp
-keep class android.util.FloatMath
-dontwarn com.google.android.gms.**
-dontwarn android.support.**
-dontwarn com.github.**
-dontwarn com.squareup.picasso.**
-dontwarn com.etsy.android.grid.**