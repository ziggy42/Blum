# Twitter4j
-dontwarn twitter4j.**
-keep  class twitter4j.conf.PropertyConfigurationFactory
-keep class twitter4j.** { *; }

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

# Support Library
-keep class android.support.v7.widget.SearchView { *; }

# jsoup
-keeppackagenames org.jsoup.nodes