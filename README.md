# Rest GooglePlay API

Rest GooglePlay API is simply a Web Service using VertX framework for searching android applications on GooglePlay, and also downloading them.

Now you can download applications with single click from web to your desktop or every device that can connect to Internet.

This project is available thanks to this project : [Google Play Crawler] (https://github.com/Akdeniz/google-play-crawler)

## Building and Running  

**Note: Make sure you install jvm on your machine on the first place**

### Maven

Install Maven 3 & protobuf compiler (version 3.0!)

`sudo apt-get install maven protobuf-compiler libprotobuf-java`
And build:

`mvn package clean`  

This will pack for you a .jar file.

## Usage

### Personal Usage:
For simplicity, you can use the  RestGooglePlayAPI-1.0-SNAPSHOT-fat.jar file like this:

`java -jar RestGooglePlayAPI-1.0-SNAPSHOT.jar`

The Server will start. Now you can go to the api-docs web page to begin play around.

### Maven Reference

### General  
You can quickly use the API by typing to your browser on the host machine:  

http://localhost:8080/api-docs/

Here you will see a quick guide to use the API. Please note: you can use this page to do everything with the api except download!

Also if you need to change anything for the api-doc, I also include a swagger editor to create your own version:  

http://localhost:8080/swagger/  

## TODO

~~Change the api-docs.~~

## License


