Creating a Java 7-compatible AMI for Elastic Beanstalk
======================================================

Add the following to a *.config file in .ebextensions for Java 7 support:

```
packages:
  yum:
    java-1.7.0-openjdk: []
    java-1.7.0-openjdk-devel: []

commands:
  use_java7:
    command: alternatives --set java /usr/lib/jvm/jre-1.7.0-openjdk.x86_64/bin/java
```