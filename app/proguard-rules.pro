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
-keep class org.w3c.dom**
-keep class com.google.firebase.**
-keep class android.util.FloatMath
-keep class com.firebase.**
-dontwarn com.google.android.gms.**
-dontwarn android.support.**
-dontwarn com.github.**
-dontwarn com.squareup.picasso.**
-dontwarn com.etsy.android.grid.**
-dontwarn com.google.firebase.**
-dontwarn android.util.FloatMath
-dontwarn org.w3c.dom**

#test
-dontwarn org.hamcrest.**
-dontwarn android.test.**
-dontwarn android.support.test.**

-keep class java.beans.**
-keep class org.easymock.** { *; }
-keep class org.jmock.core.Constraint
-keep class junit.runner.BaseTestRunner
-keep class junit.framework.TestCase
-keep class junit.framework.TestSuite
-keep class sun.misc.Unsafe
-keep class org.hamcrest.**
-keep class javax.lang.model.element.Modifier
-keep class android.databinding.DataBindingComponent

-keep class android.databinding.DataBinderMapper
-keep class com.halcyon.ubb.studentlifemanager.databinding.**
-dontwarn com.halcyon.ubb.studentlifemanager.databinding.**
-dontwarn android.databinding.DataBinderMapper

-dontwarn java.beans.**
-dontwarn org.easymock.**
-dontwarn org.jmock.core.Constraint
-dontwarn junit.runner.BaseTestRunner
-dontwarn junit.framework.TestCase
-dontwarn junit.framework.TestSuite
-dontwarn sun.misc.Unsafe
-dontwarn org.hamcrest.**
-dontwarn javax.lang.model.element.Modifier
-dontwarn android.databinding.DataBindingComponent

-keep class org.junit.** { *; }
-dontwarn org.junit.**

-keep class junit.** { *; }
-dontwarn junit.**

-keep class sun.misc.** { *; }
-dontwarn sun.misc.**