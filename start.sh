#!/bin/bash

# To run (not as a daemon) use
#  mvn package appassembler:assemble
# and
#  sh target/appassembler/bin/app1

export REPO_DIR=/home/paul/Projects/jt-open-source/code.google.com/javathinking-simplejob/target/appassembler/repo
chmod u+x target/generated-resources/appassembler/jsw/JTBatch/bin/*
mkdir u+x target/generated-resources/appassembler/jsw/JTBatch/logs
./target/generated-resources/appassembler/jsw/JTBatch/bin/JTBatch start