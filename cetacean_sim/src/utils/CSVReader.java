package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormatSymbols;

public class CSVReader {

	/**
	 * Load a csv file and return data. The csv file must have a uniform number of columns. 
	 * @param csvFile - the csv file to load
	 * @return data in csv file. 
	 */
	public static double[][] readCSV(String csvFile) {

		String line = "";
		String cvsSplitBy = ",";

		try {

			BufferedReader br = new BufferedReader(new FileReader(csvFile));


			int row = 0;
			int column=0;   
			String[] dataline;

			System.out.println("Analysing file...");
			//count the number of lines 
			while((line = br.readLine()) != null)
			{
				if (row==0) {
					dataline = line.split(cvsSplitBy);
					column=dataline.length; 
				}
				row++;
			}


			//create an array for the data.
			double[][] data=new double[row][column]; 

			System.out.println("Extracting data from file: Row count" + row+ " Column count:  "+column);

			//reset the reader. 
			br = new BufferedReader(new FileReader(csvFile));
			
			int n=0; 
			while ((line = br.readLine()) != null) {

				// use comma as separator
				dataline = line.split(cvsSplitBy);

				for (int i=0; i<dataline.length; i++){
					if (isStringNumeric(dataline[i])){
						data[n][i]=Double.valueOf(dataline[i]);
					}
					else System.err.println("Not numeric:" + dataline[i]+"b");
				}
				n++; 
			}
			return data;


		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Check whether a string is numeric
	 * @param str - the string ot check
	 * @return true if numeric. 
	 */
	public static boolean isStringNumeric( String str )
	{
	    DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
	    char localeMinusSign = currentLocaleSymbols.getMinusSign();

	    if ( !Character.isDigit( str.charAt( 0 ) ) && str.charAt( 0 ) != localeMinusSign ) return false;

	    boolean isDecimalSeparatorFound = false;
	    char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

	    for ( char c : str.substring( 1 ).toCharArray() )
	    {
	        if ( !Character.isDigit( c ) )
	        {
	            if ( c == localeDecimalSeparator && !isDecimalSeparatorFound )
	            {
	                isDecimalSeparatorFound = true;
	                continue;
	            }
	            return false;
	        }
	    }
	    return true;
	}

}