/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter16;

/**
 *
 * @author Carmen_Lucia3
 */

import java.io.*;

public abstract class CollectionWrapper {
	private String pathToFile;

	public boolean initializeObjects(String pathToFile, boolean primary) {
		this.pathToFile = pathToFile;
		String line = null;
		BufferedReader bIn = null;
		boolean outcome = true;

		try {
			// Open the file. 
			bIn = new BufferedReader(new FileReader(pathToFile));

		    	line = bIn.readLine();
		    	while (line != null) {
			        if (primary) parseData(line);
			        else parseData2(line);
		    		line = bIn.readLine();
			}

			bIn.close();
		}
		catch (FileNotFoundException f) {
			outcome = false;
		}
		catch (IOException i) {
			outcome = false;
		}

		return outcome;
	}

	public abstract void parseData(String line);
	public abstract void parseData2(String line);
}
