# Z.AI — unofficial Android client for z.ai

<div dir="rtl">

## فارسی

اپلیکیشن اندروید غیررسمی و متن‌باز برای **[z.ai](https://chat.z.ai/)** — همان چت هوش مصنوعی که در مرورگر استفاده می‌کنید، این بار به‌صورت یک اپ مستقل روی گوشی.

### امکانات

- 🚀 نسخه کامل وب z.ai داخل یک اپ بومی (Kotlin + Jetpack Compose)
- 🌐 ورود با همان حساب z.ai — کوکی‌ها و نشست شما روی گوشی باقی می‌ماند
- 🌓 پوسته تیره / روشن / سیستمی
- 🌍 رابط دوزبانه: فارسی (راست‌به‌چپ) و انگلیسی
- 🖥 حالت نسخه دسکتاپ (قابل تغییر در تنظیمات)
- 📤 آپلود فایل و عکس، 🎙 استفاده از میکروفون (چت صوتی)، 📹 ویدئوی تمام‌صفحه
- ⬇️ دانلود فایل‌ها در پوشه Downloads
- 🔙 دکمه بازگشت سخت‌افزاری مثل مرورگر واقعی کار می‌کند

### نصب

1. به صفحه [**Releases**](https://github.com/arjeyproject/Z-AI-Android/releases) بروید و فایل `app-release.apk` آخرین نسخه را دانلود کنید.
2. روی گوشی، نصب از منابع ناشناس را برای مرورگر/فایل‌منیجر خود مجاز کنید (تنظیمات ← امنیت ← نصب برنامه‌های ناشناس).
3. فایل APK را باز کرده و نصب کنید. تمام!

> **نکته درباره ورود:** ورود با «کد تأیید ایمیلی» (Email code) داخل برنامه بدون مشکل کار می‌کند. اگر ورود با گوگل (OAuth) داخل مرورگر داخلی برنامه کامل نشد، از دکمه «بازکردن z.ai در مرورگر» داخل تنظیمات استفاده کنید یا گزینه ورود با ایمیل را انتخاب کنید.

### سوالات متداول

- **آیا به کلید API نیاز دارم؟** خیر. این اپ همان سایت chat.z.ai را داخل خودش اجرا می‌کند؛ با همان حسابی که در مرورگر دارید وارد شوید.
- **گفتگوهای من کجا ذخیره می‌شود؟** روی حساب z.ai خودتان (سرور z.ai) — این اپ فقط یک قاب امن و سریع دور آن است.
- **آیا این اپ رسمی است؟** خیر؛ یک پروژه متن‌باز مستقل است و وابستگی به شرکت Z.ai ندارد.

### ساخت از سورس

با Android Studio باز کنید، یا از خط فرمان:

```bash
./gradlew assembleRelease
```

بیلد خودکار و انتشار در Releases از طریق GitHub Actions انجام می‌شود (`.github/workflows/build.yml`). برای امضای Release، فایل keystore به‌صورت رمزنگاری‌شده در Secrets ریپازیتوری نگهداری می‌شود.

</div>

---

## English

Unofficial, open-source Android app for **[z.ai](https://chat.z.ai/)** — the same AI chat you use in the browser, as a standalone app on your phone.

**Features:** full z.ai web experience inside a native Kotlin + Jetpack Compose shell · persistent sessions (cookies) · dark/light/system theme · bilingual UI (Persian RTL + English) · desktop-site mode · file upload, microphone and fullscreen video support · download manager integration · hardware back-button navigation.

**Install:** grab `app-release.apk` from the [Releases](https://github.com/arjeyproject/Z-AI-Android/releases) page and sideload it (enable "install unknown apps" for your browser or file manager).

> **Sign-in tip:** email verification-code login works in-app. If Google OAuth doesn't complete inside the built-in browser, use the email-code option or tap "Open z.ai in browser" in Settings.

**Build from source:** open in Android Studio or run `./gradlew assembleRelease`. CI builds and publishes APKs to Releases via GitHub Actions.

**Disclaimer:** this is an independent open-source project, not affiliated with or endorsed by Z.ai. Your chats and account live on z.ai's servers; the app is only a fast, secure shell around the web app.

## License

[MIT](LICENSE)
