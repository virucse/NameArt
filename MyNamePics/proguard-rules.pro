
# Add the gson class
-dontwarn com.google.gson**
-keep public class com.google.gson
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.webapi.model.** { *; }
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Add any classes the interact with gson
-dontwarn com.facefilters**
-keep class com.facefilters**{*;}

-dontwarn tv.**
-keep class tv.**{*;}

-dontwarn com.webapi.model**
-keep class com.webapi.model**{*;}
#blur
-dontwarn net.qiujuer.genius.blur**
-keep class net.qiujuer.genius.blur**{*;}
#native gif
-dontwarn com.waynejo.androidndkgif**
-keep class com.waynejo.androidndkgif**{*;}
#UCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

-dontwarn okio.**
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable
-keep class com.appsee.** { *; }
-dontwarn com.appsee.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod
-keep class com.google.ads.mediation.admob.AdMobAdapter {
    *;
}
-keep class com.google.ads.mediation.AdUrlAdapter {
    *;
}

-keep class com.startapp.** {
      *;
}
-dontwarn android.webkit.JavascriptInterface
-dontwarn com.startapp.**

#google
-keep class com.google.** { *; }
-dontwarn com.google.**

#keep enum
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#facebook
#-keep class com.facebook.** {
#   *;
#}

# Mopub
-keep public class com.mopub.**
-keepclassmembers class com.mopub.** { public *; }
-keep class * extends com.mopub.mobileads.CustomEventBanner {}
-keep class * extends com.mopub.mobileads.CustomEventInterstitial {}
-keep class * extends com.mopub.nativeads.CustomEventNative {}
-keep class * extends com.mopub.mobileads.CustomEventRewardedVideo {}
-dontwarn com.mopub.volley.toolbox.**
-dontwarn com.mopub.**

# Adcolony
-keep class com.jirbo.adcolony.** { *;}
-keep class com.immersion.** { *;}
-dontnote com.immersion.**
-dontwarn android.webkit.**
-dontwarn com.jirbo.adcolony.**

#extra
-dontwarn com.webapi.model.**
-keep class com.webapi.model.**{*;}

-dontwarn org.**
-keep class org.**{*;}

#mobfox
-keep class com.mobfox.** { *; }
-keep class com.mobfox.adapter.** {*;}
-keep class com.mobfox.sdk.** {*;}
