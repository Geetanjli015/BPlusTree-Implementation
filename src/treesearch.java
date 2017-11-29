import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * tree search utlity takes the input file, parses and the directs to
 * appropriate methods of b plus tree
 * 
 * @author geetanjli
 *
 */
public class treesearch {

	public static void main(String[] args) {
		String fileName = args[0];//"/Users/geetanjli/Documents/adsworkspace/bplustree/src/in.txt";
		FileReader fr = null;
		BufferedReader br = null;
		BPTree<Double, String> b = null;
		try {
			// Reading the input file
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String sCurrentLine;
			treesearch t = new treesearch();
			while ((sCurrentLine = br.readLine()) != null) {
				// System.out.println(sCurrentLine);
				b = t.parseInput(sCurrentLine, b);
			}
			// Printing final tree
			// printTree(b.root); removed this method as not required in final
			// submission
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method used for parsing input file and directing to appropriate bptree
	 * mathod
	 * 
	 * @param currLine
	 * @param b
	 * @return
	 */
	private BPTree<Double, String> parseInput(String currLine, BPTree<Double, String> b) {
		currLine = currLine.trim().replace(" ", "");
		if (currLine.equals("") || currLine == null)
			return b;
		// If the command has insert then call insert method of bptree
		if (currLine.contains("Insert")) {
			// Double as key and string as value
			String key = currLine.substring(currLine.indexOf("(") + 1, currLine.indexOf(","));
			String value = currLine.substring(currLine.indexOf(",") + 1, currLine.indexOf(")"));
			b.insertKeyValuePair(Double.parseDouble(key), value);
			// If the command has search then call search method of bptree
		} else if (currLine.contains("Search")) {
			// extracting keys from braces and segregating two search cases that
			// is searching single key and range search
			String key = currLine.substring(currLine.indexOf("(") + 1, currLine.indexOf(")"));
			int noOfParam = 1;
			if (key.contains(",")) {
				noOfParam = key.split(",").length;
			}
			if (noOfParam == 1) {
				b.search(Double.parseDouble(key));
			} else if (noOfParam == 2) {
				// If two arguments the direct to range search
				b.search(Double.parseDouble(key.split(",")[0]), Double.parseDouble(key.split(",")[1]));
			}

		} else {
			// If instruction doesn't contain insert or search it should be
			// degree only if current line has all digits regex
			if (currLine.matches("\\d+"))
				b = new BPTree<Double, String>(Integer.parseInt(currLine));
			else
				System.out.println("Input file contains invalid input. Please check again! ");
		}

		return b;
	}

}
