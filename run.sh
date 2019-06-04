#!/bin/bash
mvn clean install
cd target
java -jar simple-boggle-1.0.jar
