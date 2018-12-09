/**
 * This package contains a Java implementation of the Viola-Jones face detector 
 * [Paul Viola and Michael Jones, "Robust Real-time Face Detection", International Journal 
 * of Computer Vision 57.2 (2004), pp. 137–154] to be used with ImageJ in conjunction with 
 * the 'imagingbook-common' library. It is adapted from the 'JViolaJones' implementation
 * (see https://code.google.com/archive/p/jviolajones/) by unknown authors.
 * Note that this software does not implement the learning part of the face detection 
 * algorithm but reads pre-learned decision trees (trained with OpenCV) and stored in XML 
 * format (in 'resources').
 * Note that this code is experimental and not suitable for production use.
 * 
 * @author Wilhelm Burger
 *
 */
package imagingbook.violajones;