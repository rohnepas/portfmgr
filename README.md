# Crypto Currency Portfolio Manager (portfmgr)
This is a project work from the course CAS OOP (object oriented programming) of the university of applied science Zurich (ZHAW). 

## Actual Status
Minimum viable product V1.0

## What are the possibilities of the App
It tracks your crypto currency transactions, do some statistics and provide an export feature to save the portfolio insights into an Excel sheet.

## Installing and getting started
* Need a API Key from [cryptocompare.com](https://min-api.cryptocompare.com) and add it to the class **OnlineCourseQuery.java** inside the package src/main/java/portfmgr/model
* You could run it in your IDE (e.g Eclipse) directly **or** build an executable .jar with following steps

### Installation guide for executable .jar on MAC
If you want to have a stand-alone .jar File proceed with following steps:

1. Open Termnal on Mac and install [homebrew](https://brew.sh): 

```usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"```

2. Install [gradle](https://stackoverflow.com/questions/28928106/install-upgrade-gradle-on-mac-os-x):

``` brew install gradle ```

3. Now check whether it works or not:

``` gradle -v```

4. Open (another) terminal on Mac and navigate to your project folder *<YOUR PATH>/portfmgr*
5. Build the .jar file (after successful built a new project folder (*../portfmgr/build/libs*) will appear): 
  
  ```gradle build ```
  
6. Install an archiving app (e.g [iUnarchive](http://www.iunarchive.com))
7. Open the built .jar file from *../portfmgr/build/libs* with your archiving app. You will find three folder
* org
* META-INF
* BOOT-INF

Now, its getting a little bit crazy but this issue must be fixed in a newer version.

8. Open Finder on Mac and move the folder **coinlist** (*../portfmgr/src/main/java/coinlist*) into the folder *../portfmgr*
9.  ```gradle clean ```
10.  ```gradle build ```

Now you have an executbale .jar file which could be run stand-alone.

## Credits:
Data under free license from: [cryptocompare.com](https://min-api.cryptocompare.com)

