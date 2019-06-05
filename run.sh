#!/bin/bash
mvn clean install
cd target
mv ./simple-boggle-1.0.jar ../
cd ../
java -jar simple-boggle-1.0.jar
