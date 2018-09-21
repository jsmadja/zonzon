#!/bin/bash
export JAVA_HOME=/home/jsmadja/Perso/Development/jdk1.6.0_45
export PATH=$JAVA_HOME/bin:$PATH
rm -rf zonzon
rm zonzon.zip
mkdir zonzon
/home/jsmadja/Perso/Development/apache-maven-3.0.4/bin/mvn clean package
cp target/zonzon-1.0-jar-with-dependencies.jar zonzon/
mv zonzon/zonzon-1.0-jar-with-dependencies.jar zonzon/zonzon.jar
cp Clic\ Obligatoire.bat zonzon/
cp -rf semantic zonzon/
cp Donnees\ Brutes.xlsx zonzon/
cp jquery-3.1.1.min.js zonzon
mkdir -p zonzon/src/main/resources
cp src/main/resources/template.html zonzon/src/main/resources
zip -r zonzon.zip zonzon