# appupgrade
Library to check Android app upgrades

Steps for installing appupgrade to the app

### 1. Add this for gradle dependency
	dependencies {
		...
		compile 'com.hummingwave.appupgrade:appupgrade:1.1.0'
	}

### 2. Add this for maven dependency
        <dependency>
        <groupId>com.hummingwave.appupgrade</groupId>
        <artifactId>appupgrade</artifactId>
        <version>1.1.0</version>
        <type>pom</type>
        </dependency>

### 3. Add these permissions in AndroidManifest.xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

### 4. Add this line in your launcher activity eg: SplashActivity
          Upgrade.init(this);


This checks whether the current version of the app is less than latest version of the existing app in playstore.
- If less, then pop-up is shown with
1. Update Now : redirects the app to playstore
2. Update Later : dismisses the pop-up

- If equal or greater, nothing happens as the app is up to date.

We can make forceUpdate if the latest version in the playstore contains **"F"** in the version name.






