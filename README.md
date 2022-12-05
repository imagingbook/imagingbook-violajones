# Java/ImageJ implementation of Viola-Jones face detection #

This Java library implements the face detection algorithm proposed by Viola and Jones 
(partly based on [this implementation](https://code.google.com/archive/p/jviolajones/)).
Part of the [imagingbook project](https://imagingbook.com).

**Author:** Wilhelm Burger

### Project Structure ###

This software is part of the [imagingbook project](https://imagingbook.com).
It is built with Maven and depends on 
[**ImageJ**](https://imagej.nih.gov/ij/) and 
the [**imagingbook-common**](https://github.com/imagingbook/imagingbook-public) library.

This project consists of two sub-projects (Maven modules):
* `imagingbook-violajones-lib`: the face detection library,
* `imagingbook-violajones-data`: a set of test data (images),
* `imagingbook-violajones-plugins`: various ImageJ demo plugins (embedded in a complete ImageJ setup).

### Stand-Alone Installation ###

* Clone this repository.
* Enter `imagingbook-violajones-plugins`.
* Start ImageJ by double-clicking `ImageJ.exe` (Win) or launching `ij.jar` (Mac). This requires an installed Java runtime version 1.8 or higher.
* In ImageJ, use the `Plugins` menu to run the demo plugins.

### Use in an Existing ImageJ Environment ###

* Copy all JAR files from `imagingbook-calibrate-plugins/ImageJ/jars`.
* Copy the plugin `.class` files from `imagingbook-calibrate-plugins/ImageJ/plugins`.


### Documentation (JavaDoc) ###

* **[imagingbook-violajones](https://imagingbook.github.io/imagingbook-violajones/imagingbook-violajones/javadoc/index.html)**

