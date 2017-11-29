import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author geetanjli Logger class to get the console logs output in the file
 */
public class Logger {
	String out_filename;// = "output_file.txt";

	public Logger(String out_filename) {
		// deleting already exiting file if exits with the same name as output
		// filename
		File f = new File(out_filename);
		f.delete();
		this.out_filename = out_filename;
	}

	/**
	 * Method to append to the output file
	 * 
	 * @param content
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public void writeToFile(String content) throws IOException, UnsupportedEncodingException, FileNotFoundException {
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(out_filename, true), "utf-8"))) {
			writer.write(content);
			writer.newLine();
		}
	}

}
