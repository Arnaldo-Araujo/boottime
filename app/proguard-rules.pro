# Mantém as classes de modelo JSON do Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Mantém anotações usadas no Dagger, Gson, Retrofit e Jackson
-keepattributes *Annotation*

# Mantém classes que são serializadas (evita erros no Gson, Jackson, etc.)
-keep class * implements java.io.Serializable { *; }

# Mantém classes da biblioteca AndroidX (previne crashes)
-keep class androidx.** { *; }

# Mantém classes de interfaces Retrofit
-keep interface retrofit2.** { *; }
-keep class com.squareup.** { *; }

# Mantém a classe principal da aplicação
-keep class br.com.i9android.calculadodetempodeserviomilitar.** { *; }

# Ofusca apenas nomes internos e deixa logs mais limpos
-dontwarn