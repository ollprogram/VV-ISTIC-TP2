# Use an official OpenJDK base image
FROM openjdk:23-jdk-slim

# Install necessary dependencies (e.g., X11 libraries for GUI)
RUN apt-get update && apt-get install -y \
    libx11-dev \
    libxext-dev \
    libxrender-dev \
    libxtst-dev \
    libxi6 \
    xauth \
    libx11-xcb1 \
    libxrandr2 \
    libxrender1 \
    libxxf86vm1 \  
    libgtk-3-0 \    
    libgl1-mesa-glx \
    wget \
    unzip \
    && apt-get clean

# Download and install OpenJFX 23.0.1 SDK for Linux
RUN wget https://download2.gluonhq.com/openjfx/23.0.1/openjfx-23.0.1_linux-x64_bin-sdk.zip -O /tmp/openjfx.zip \
    && mkdir -p /opt/openjfx \
    && unzip /tmp/openjfx.zip -d /opt/openjfx \
    && rm /tmp/openjfx.zip

# Download and install PMD 7.7.0
RUN wget https://github.com/pmd/pmd/releases/download/pmd_releases%2F7.7.0/pmd-dist-7.7.0-bin.zip -O /tmp/pmd.zip \
    && mkdir -p /opt/pmd \
    && unzip /tmp/pmd.zip -d /opt/pmd \
    && rm /tmp/pmd.zip

# Set environment variables for Java, OpenJFX, and PMD
ENV JAVA_HOME=/usr/lib/jvm/java-23-openjdk
ENV PATH=$JAVA_HOME/bin:$PATH
ENV JAVAFX_HOME=/opt/openjfx/javafx-sdk-23.0.1
ENV PMD_HOME=/opt/pmd/pmd-bin-7.7.0

# Expose necessary ports for the X server (if running on a host machine)
EXPOSE 6000

# Command to run PMD Designer with OpenJFX 23.0.1
CMD ["/opt/pmd/pmd-bin-7.7.0/bin/pmd", "designer"]