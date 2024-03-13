
-dontwarn javax.inject.**
    # Keep file R and Manifest
    -keep class **.R
    -keep class **.R$* {*;}
    -keep class **.BuildConfig {*;}
    -keep class **.Manifest {*;}

    -keep class androidx.constraintlayout.motion.widget.KeyAttributes { *; }


    # Keep Dependency Injection Framework related classes and methods
    -keep class dagger.** { *; }
    -keep class *Hilt_* {*;}
    -keep class dagger.hilt.** { *; }
    -keep class javax.inject.** { *; }
    -keep class javax.annotation.** { *; }

    # Keep Model and Repository Classes
    -keep class com.example.core.data.model.** { *; }
    -keep class com.example.core.data.repo.** { *; }

    # Keep ViewModels
    -keep class * extends androidx.lifecycle.ViewModel

    # Keep Parcelable Classes
    -keep class * implements android.os.Parcelable {
        public static final android.os.Parcelable$Creator *;
    }

    # Keep UI-related classes
    -keep class com.example.ui.activity.** { *; }
    -keep class com.example.ui.fragment.** { *; }
    -keep class com.example.service.** { *; }

    # Menjaga semua kelas dalam paket tertentu
    -keep class com.example.package.** { *; }

    # Menjaga semua kelas yang disebutkan dalam file tertentu
    #-keep @com.example.rules.txt


    # Menjaga kelas model
    -keep class com.example.model.** { *; }

    # Menjaga kelas service
    -keep class com.example.service.** { *; }

    # Menjaga kelas-kelas yang diperlukan untuk penanganan SSL/TLS
    -keep class okhttp3.internal.platform.ConscryptPlatform {*;}
    -keep class okhttp3.internal.platform.OpenJSSEPlatform {*;}
    -keep class org.bouncycastle.** {*;}
    -keep class org.conscrypt.** {*;}
    -keep class org.openjsse.** {*;}


    # Keep classes required by OpenJSSE
    -keep class sun.security.x509.** { *; }
    -keep class sun.util.logging.** { *; }

    -keep class com.example.projectone.GithubUserApplication { *; }
    -keep class com.example.projectone.ui.main.MainActivity { *; }
    -keep class com.example.projectone.ui.userdetail.DetailUserActivity { *; }

# Keep classes generated by Hilt
    -keep class com.example.projectone.*Hilt* {
        *;
    }
    -keepclasseswithmembernames class com.example.projectone.*Hilt* {
        *;
    }
    -keepclasseswithmembernames class com.example.projectone.*Hilt_* {
        *;
    }

-keepattributes *Annotation*
-keep @dagger.hilt.annotation.* class * { *; }


    -keepclassmembers class * {
        @dagger.hilt.* *;
    }
    -keepclassmembers class * {
        @javax.inject.* *;
    }
    -keepclasseswithmembers class * {
        @dagger.hilt.* <methods>;
    }
    -keep class kotlin.Metadata { *; }

# Keep Hilt generated classes
-keep class com.example.projectone.Hilt_* {*;}
-keep,allowobfuscation,allowshrinking @dagger.hilt.EntryPoint class *
-keep,allowobfuscation,allowshrinking @dagger.hilt.android.EarlyEntryPoint class *
-keep,allowobfuscation,allowshrinking @dagger.hilt.internal.ComponentEntryPoint class *
-keep,allowobfuscation,allowshrinking @dagger.hilt.internal.GeneratedEntryPoint class *

-keep class kotlin.**
-keep class javax.** { *; }



# Keep Hilt generated classes
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep the main class along with its main method (entry point of your application)
#-keep class com.example.projectone.GithubUserApplication {
#    public static void main(java.lang.String[]);
#}

# Keep all publicly accessible elements of the class (fields and methods)
-keepclassmembers class com.example.projectone.GithubUserApplication {
    public *;
}

# Optionally, you can also keep the constructor to ensure proper instantiation
-keepclassmembers class com.example.projectone.GithubUserApplication


