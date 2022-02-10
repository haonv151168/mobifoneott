# MobifoneOTT

This repository contains sample applications for Android which use the [MobifoneOTT] . They are organized
as follows:


## Android

* Android Example, Android SDK written in Kotlin

## NOTE: 

* Server socket : https://ott.mobifone.ai

## How to use ?

* Step 1 : Install <br />
<br />
Download folder "mobifone-meet-sdk" : https://github.com/haonv151168/mobifone-meet-sdk <br/>
Download folder "mobifonecall" <br />
<br/>

#### Gradle

In build.gradle (project level) add : </br>
```
allprojects {
    repositories {
        // Other dependencies
        maven { url 'https://jitpack.io' }
        maven { url "file:xxx/mobifone-meet-sdk/releases" }
        // xxx : is path to mobifone-meet-sdk
    }
}
```
<br />

In build.gradle (app level) :
```
implementation project(path: ':mobifonecall')
```
#### Setup in code
Implement MobifoneHelperListener and CallListener
```
ContextHolder.context = applicationContext
MobifoneClient.mobifoneHelperListener = this
MobifoneClient.callListener = this

if (!MobifoneClient.isConnected()) {
   MobifoneClient.connectServer()
}
```



[MobifoneOTTHelper]

Contact : Phòng Phần Mềm Khách Hàng - TT CNTT Mobifone <br />
