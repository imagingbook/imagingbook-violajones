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
* Launch ImageJ (requires a Java runtime version 1.8 or higher).
* In ImageJ, use the `Plugins` menu to run the demo plugins.

### Use in an Existing ImageJ Environment ###

* Copy all JAR files from `imagingbook-calibrate-plugins/ImageJ/jars`.
* Copy the plugin `.class` files from `imagingbook-calibrate-plugins/ImageJ/plugins`.

### Use with Maven

To use the ``imagingbook-violajones-lib`` library and ``imagingbook-violajones-data`` resources in your Maven project, 
add the following lines to your ``pom.xml`` file:
````
<repositories>
    <repository>
	<id>imagingbook-maven-repository</id>
    	<url>https://raw.github.com/imagingbook/imagingbook-maven-repository/master</url>
    </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.imagingbook</groupId>
    <artifactId>imagingbook-violajones-lib</artifactId>
    <version>3.0</version>
  </dependency>
  <dependency>
    <groupId>com.imagingbook</groupId>
    <artifactId>imagingbook-violajones-data</artifactId>
    <version>3.0</version>
  </dependency>
  <!-- other dependencies ... -->
</dependencies>
````
The above setup refers to version `3.0`, check the [ImagingBook Maven Repository](https://github.com/imagingbook/imagingbook-maven-repository/tree/master/com/imagingbook/) for the most recent version.
See also [this demo repository](https://github.com/imagingbook/imagingbook-maven-demo-project) for how to set up a simple ImageJ project with Maven.

### Documentation ###

* **JavaDoc: [imagingbook-violajones](https://imagingbook.github.io/imagingbook-violajones/javadoc/index.html?overview-summary.html)**

