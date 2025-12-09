#!/bin/bash

cd "$(dirname "$0")"

export JAVA_HOME=/Users/jason/Library/Java/JavaVirtualMachines/corretto-17.0.14/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

mvn clean compile dependency:copy-dependencies -DoutputDirectory=target/dependency

java -Djava.library.path=target/dependency -cp target/classes:target/dependency/* ShadowDonkeyKong 