# XRD Data Analysis Utilities

An application (in JavaFX) for viewing MAR Tiff data, tracking changes between samples, and calculating X-Ray Diffraction patterns for selected groups.

This project has three purposes:

1.  To create effective, modern software that will replace antiquated tools currently in use by the mineralogical community.
2.  To construct a set of tools to help me execute the goals of my Master's Thesis project.
3.  To learn Java and the patterns recommended by Oracle and the Java developer community for use with JavaFX applications in a production environment.

Though the tool is in a useful state right now, a myriad of features (and documentation) will be added over the course of the next few weeks.

# Notes

This project was initially scaffolded with the JavaFX application packager in IntelliJ 13. Neither ANT nor Maven have been configured in a manner tht successfully instantiates the JavaFX Runtime and allows the application to run properly when cloned to other machines/environments. Work is being done to remedy this, and a solution for building without a configuration attached directly to IntelliJ or any other development tool will be available soon.

# Requirements

In order to begin development with this repository, the JDK and the latest release for Java 8 should be installed on your machine.

If the JDK and/or Java 8 is/are not installed on your machine:

1.  Go to the [Java SE Development Kit 8 page](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) on Oracle's website
2.  Read/accept the [Oracle binary Code License Agreement for Java SE](http://www.oracle.com/technetwork/java/javase/terms/license/index.html)
3.  Select the version that best suits your OS/development environment and click on the file link to download it
4.  Open the installer you downloaded and follow all instructions/prompts until Java 8 and the JDK are installed on your machine

Your development environment is now ready for Java.

As of JDK 8, JavaFX runtime jars are available to the Java Runtime Environment without the need for explicitly defining JavaFX as a dependency in any build file or dependencies list..

# Dependencies

Note that this project currently has four module dependencies:

1. [footplate](https://github.com/quantumjockey/footplate) - For managing application files and system properties.
2. [gradientswatch](https://github.com/quantumjockey/gradientswatch) - For data visualization (graphics).
3. [melyaframework](https://github.com/quantumjockey/melyaframework) - For application scaffolding, GUI management, and utilization of JavaFX features.
4. [xrdfileoperations](https://github.com/quantumjockey/xrdfileoperations) - For reading crystallographic file formats and analyzing the data contained therein.

Be sure to clone these repositories and reconfigure the application's project structure prior to working with the code.

These modules are still not complete from a production standpoint and, though usable, require much more testing before being posted publicly on Maven or an organizational GitHub profile.

Stay tuned for updates!
