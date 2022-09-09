# Contributing to Dyson4J
Thank you for being interested in contributing! Dyson4J is a small project for a very niche audience of developers that want to write applications for their Dyson fans, so if you feel there's a missing feature in the library that would be useful to you, do not hesitate to create a [Pull Request](https://github.com/2008Choco/Dyson4J/pulls).

This file contains information about how to contribute and what you can expect when creating a Pull Request or an Issue in this repository. Not all information detailed in this document are strict rules. Most are just guidelines that we strongly recommend you follow. Additionally, these contribution guidelines are available for edit should you choose to adjust them.

## API Compatibility
Dyson4J operates on a stable release basis, not rolling release/snapshots. While it is strongly advised to retain binary compatibility between versions, this is not enforced. You are allowed to create breaking changes so long as the reason behind the change is compelling enough to warrant it. Where possible, deprecation is preferred so developers may adapt to changes in upcoming releases without being blind-sided by abrupt changes in API.

### What Is Binary Compatibility?
Binary compatibility is specified by [Oracle's language specification, Chapter 13](https://docs.oracle.com/javase/specs/jls/se7/html/jls-13.html) whereby changes made to the library should not require dependent programs to be recompiled. In general, avoid the following:

- Deleting undeprecated API classes, methods, or constants (or those not scheduled for removal)
  * This includes renaming methods and constants, or renaming or repackaging classes
- Changing the return type of methods
- Changing a class' descriptor from class to interface, or vice versa, or changing its abstract modifier

### What Is Considered API?
API is explicitly documented under a package's `package-info.java` file. If a class or package is **not** API, its documentation will state it clearly. As a general rule of thumb, if a dependent is expected to use a method, field, or class as part of public-facing functionality, it is considered API.

## Checkstyle & Formatting
This project makes use of [Checkstyle](https://checkstyle.sourceforge.io) to enforce a specific programming style. This is strongly enforced and all pull requests must abide by this programming style. If the standard is broken, the Maven build will fail. For the most part, this project abides by [Google's Java styling guide](https://google.github.io/styleguide/javaguide.html) with minor alterations.

To check whether or not your changes respect this project's Checkstyle configuration, you may run `mvn checkstyle:check` to validate the build, or simply run a build with `mvn clean package` as the Checkstyle phase is run automatically during build validation. Additionally, most modern Java IDEs (such as Eclipse, IntelliJ, and NetBeans) support Checkstyle integration with plugins and can raise warnings where applicable.

- **Eclipse IDE:** https://checkstyle.org/eclipse-cs
- **IntelliJ IDEA:** https://plugins.jetbrains.com/plugin/1065-checkstyle-idea
- **NetBeans:** https://github.com/awatry/netbeans-checkstyle
