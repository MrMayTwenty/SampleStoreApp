# Design Decisions & Challenges

## Design Decisions

### 1. **Jetpack Compose for UI**

- Used **Jetpack Compose** to build a modern, declarative UI, making UI code more readable and maintainable.
- Eliminated the need for XML layouts, improving performance and reducing boilerplate code.

### 2. **Room Database for Local Storage**

- Chose **Room** as the local database to manage shopping cart and wishlist data efficiently.
- Provides an abstraction over SQLite with built-in support for LiveData and Kotlin Coroutines.

### 3. **State Management with ViewModel & StateFlow**

- Used **ViewModel** to separate UI logic from composables, ensuring better maintainability.
- Employed **StateFlow** instead of LiveData for reactive UI updates, improving performance.

### 4. **Dependency Injection with Dagger-Hilt**

- Integrated **Dagger-Hilt** for dependency injection to improve testability and manage dependencies efficiently.
- Reduced boilerplate for repository and database initialization.

### 5. **Multi-Language Support**

- Implemented translations for **English, German, Spanish, Italian, and Portuguese**.
- Followed Android's best practices for localization using **string resources**.

### 6. **Dark Mode and Light Mode**

- Used Material 3’s **dynamic theming** to support both light and dark modes.
- Ensured all UI components adapt correctly with appropriate color schemes.

### 7. **Payment Integration with PayPal**

- Since Google Pay is not available in our country yet, I opted to use **PayPal** instead.
- Chose **PayPal Sandbox** for testing secure transactions.
- Used **PayPal Card Payment** library to allow manual card entry.

---

## Challenges & Solutions

### **1. Managing State in Jetpack Compose**

- **Challenge:** Handling UI state properly without excessive recompositions.
- **Solution:** Used **StateFlow** in ViewModel to manage UI state efficiently and prevent unnecessary recompositions.

### **2. Debugging Payment Integration Issues**

- **Challenge:** PayPal Sandbox API inconsistencies caused failed transactions. Outdated documentation on the Android SDK, deprecated methods, and refactored code made integration difficult.
- **Solution:** Debugged using **API logs** and adjusted **callback handling**.
    - Read updated documentation and cross-referenced it with the **[PayPal GitHub repository](https://github.com/paypal/paypal-android)** .
    - Followed the **PayPal Migration Guide**, but it was not optimized for Jetpack Compose as it relied on `MainActivity` and XML-based views.
      [PayPal Native Payments Migration Guide](https://github.com/paypal/paypal-android/blob/main/v2_MIGRATION_GUIDE.md#paypal-native-payments).
    - Adapted the implementation to work seamlessly with Jetpack Compose while ensuring compatibility with PayPal's updated SDK.
---
