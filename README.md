# EmulatorDetector
=============================================

[![Release](https://jitpack.io/v/mofneko/EmulatorDetector.svg)](https://jitpack.io/#mofneko/EmulatorDetector)

This module help you to emulator detection to your Android project suported Unity.

#### Basic checker
    - BlueStacks
    - Genymotion
    - Android Emulator
    - Nox App Player
    - Koplayer
    - .....

# How to use

##### Java and Kotlin

Users of your library will need add the jitpack.io repository:

```gradle
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```
and:

```gradle
dependencies {
    compile 'com.github.mofneko:EmulatorDetector:1.1.0'
}
```
```java
EmulatorDetector.isEmulator(this);
```

##### C# (Unity)
Create a folder with the structure Assets/Plugins/Android and put [library-release.aar](https://github.com/mofneko/EmulatorDetector/blob/master/aar/library-release.aar) in the Android folder.
```C# (Unity)
AndroidJavaClass unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
AndroidJavaObject context = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity").Call<AndroidJavaObject>("getApplicationContext");
AndroidJavaClass cls = new AndroidJavaClass("com.nekolaboratory.EmulatorDetector");
bool result = cls.CallStatic<bool>("isEmulator", context);
```

# Development

```
$ git clone git@github.com:mofneko/EmulatorDetector.git
$ cd EmulatorDetector
$ gradlew assembleRelease
```

# License

```
MIT License

Copyright (c) 2019 Yusuke Arakawa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
