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
    compile 'com.github.mofneko:EmulatorDetector:1.0.0'
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
Copyright 2018 Yusuke Arakawa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
