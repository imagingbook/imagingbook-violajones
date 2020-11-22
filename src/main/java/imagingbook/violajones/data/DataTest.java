package imagingbook.violajones.data;

import java.net.URL;

import imagingbook.lib.util.ResourceUtils;


public class DataTest {
	
	private static final String resourcePath = ""; //"resources/";
	
	public static String getLocalResourcePath(String name) {
		System.out.println("searching for " + resourcePath + name);
		URL url = DataTest.class.getResource(resourcePath + name);
		if (url == null) {
			return null;
		}
		else {
			return url.getPath();
			//return url.toString();
		}
	}
	
	
	
	public static void main(String[] args) {
		System.out.println("Location of resources:" + DataTest.getLocalResourcePath("images/"));
		
		//preferred:
		System.out.println("Location of resources (FileUtils):" + ResourceUtils.getResourcePath(Data.class, "images/"));
		System.out.println("Location of resources (FileUtils):" + ResourceUtils.getResourcePath(Data.class, "images/bishops1.jpg"));
		System.out.println("Location of resources (FileUtils):" + ResourceUtils.getResourcePath(Data.class, "xml/haarcascade_eye.xml"));
		
		{
			System.out.println("Resource test 1 (direct):");
			String filename = "images/bishops1.jpg";
			URL location = DataTest.class.getResource(filename);
			
			if (location == null) {
				System.out.println("Resource not found: " + filename);
			}
			else {
				System.out.println("Class found: " + location.toString());
			}
		}

		{
			System.out.println("Resource test 2 (local method):");
			String filename = "images/bishops1.jpg";
			String location = DataTest.getLocalResourcePath(filename);
			if (location == null) {
				System.out.println("Resource not found: " + filename);
			}
			else {
				System.out.println("Resource found: " + location.toString());
			}
		}
	}

}
