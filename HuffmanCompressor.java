import java.io.*;
import java.util.*;

public class HuffmanCompressor {
    private String filename;

    public static final int CHAR_MAX = 255; // max char value
    public static final int MAKE_CODE = 1;
    public static final int COMPRESS = 2;
    public static final int DECOMPRESS = 3;
    public static final int ROUND_TRIP = 4;

    public HuffmanCompressor(String filename) {
        if (!filename.endsWith(".txt")) {
            throw new IllegalArgumentException(
                    "This compressor only works on text files!");
        }
        this.filename = filename.split(".txt")[0];
    }

    public void makeCode() throws IOException {
        System.out.println("I am about to make the Huffman code for "
                + filename + ".txt...");
        FileInputStream input = new FileInputStream(filename + ".txt");
        int[] count = new int[CHAR_MAX];
        int n = input.read();
        while (n != -1) {
            count[n]++;
            n = input.read();
        }

        // Build the code; open the output file; save the code
        System.out.println("\tI built up a frequency table of the " +
                           "characters in your file.");
        System.out.println("\tNow, I'm going to call your HuffmanCode(int[]) " +
                           "constructor using that frequency table.");
        HuffmanCode t = new HuffmanCode(count);

        System.out.println("\tOkay!  Now, I am going to save the code (using " +
                           "your save method) to the file " + filename +
                           ".code!");
        PrintStream output = new PrintStream(new File(filename + ".code"));
        t.save(output);
        System.out.println("...I am done making the Huffman Code!");
    }

    public void compress(boolean debug) throws IOException {
        System.out.println("I am about to attempt to COMPRESS "
                + filename + ".txt:");
        /* We must make the code before we can compress... */
        this.makeCode();
        System.out.println("\tNow that I have the Huffman Code, I am going " +
                           "to use the huffman code file created by");
        System.out.println("\tyour save() method to compress the contents " +
                           "into " + filename + ".short!");

        String[] codes = new String[CHAR_MAX];
        Scanner codeInput = new Scanner(new File(filename + ".code"));
        while (codeInput.hasNextLine()) {
            int n = Integer.parseInt(codeInput.nextLine());
            codes[n] = codeInput.nextLine();
        }

        // Open file to be compressed; open output file
        FileInputStream input = new FileInputStream(this.filename + ".txt");
        BitOutputStream output = new BitOutputStream(
                new PrintStream(this.filename + ".short"), debug);

        // Do the compression
        int n = input.read();
        while (n != -1) {
            if (codes[n] == null) {
                System.out.println("Your code file has no code for " + n +
                                   " (the character '" + (char) n + "')");
                System.out.println("exiting...");
                System.exit(1);
            }
            for (int i = 0; i < codes[n].length(); i++) {
                output.write(codes[n].charAt(i) - '0');
            }
            n = input.read();
        }
        input.close();
        output.close();
        System.out.println("...I am done compressing the file");
    }

    public void decompress(boolean printToConsole) throws IOException {
        System.out.println("I am about to attempt to DECOMPRESS " + 
                           filename + ".short:");

        System.out.println("\tTo do this, I must first read in the huffman " +
                           "code used to compress the file.");
        System.out.println("\tI will use your HuffmanCode(Scanner) " +
                           "constructor!");
        // Open code file and construct tree
        Scanner codeInput = new Scanner(new File(this.filename + ".code"));
        HuffmanCode t = new HuffmanCode(codeInput);

        // Open compressed file; open output
        BitInputStream input = new BitInputStream(this.filename + ".short");
        PrintStream output = System.out;
        if (!printToConsole) {
            System.out.println("\tNow, I will decompress the file using your " +
                               "translate() method and save");
            System.out.println("\tthe output into " + this.filename + ".new");
            output = new PrintStream(new File(this.filename + ".new"));
        }
        else {
            System.out.println("\tNow, I will decompress the file using your " +
                               "translate() method and display it on the console");
        }

        // Decompress the file
        t.translate(input, output);
        output.close();
        System.out.println("...I am done decompressing the file");
    }

    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);

        System.out.println("Welcome to the CSE 143 Huffman Compressor!");
        System.out.println();

        String filename;

        do {
            System.out.print("Which file would you like to work with (it " +
                             "must be a txt file)? ");
            filename = console.nextLine().trim();
        } while (!filename.endsWith(".txt"));

        System.out.println();

        int choice = -1;

        while (choice == -1) {
            System.out.println("Would you like to:");
            System.out.println("\t(1) make a huffman code,");
            System.out.println("\t(2) compress a file,");
            System.out.println("\t(3) decompress a file, or");
            System.out.println(
                    "\t(4) do a compression followed by a decompression");
            System.out.print("1-4? ");
            String choiceStr = console.nextLine().trim();
            try {
                choice = Integer.parseInt(choiceStr);
                if (choice < 1 || choice > 4) {
                    choice = -1;
                }
            } catch(NumberFormatException e) {
                /* Don't change choice; so, the loop will repeat. */
            }
        }

        boolean debug = false;
        if (choice == COMPRESS) {
            debug = prompt(
                    console,
                    "Would you like to debug the compressed file (y/n)? ");
        }

        boolean toConsole = false;
        if (choice == DECOMPRESS || choice == ROUND_TRIP) {
            toConsole = !prompt(
                    console,
                    "Would you like to print the result to a file (y/n)? ");
        }

        HuffmanCompressor huffman = new HuffmanCompressor(filename);
        switch (choice) {
            case 1: huffman.makeCode(); break;
            case 2: huffman.compress(debug); break;
            case 3: huffman.decompress(toConsole); break;
            case 4: 
                huffman.compress(false);
                huffman.decompress(toConsole); 
            break;
        }
	}

    public static boolean prompt(Scanner console, String message) {
        System.out.print(message);
        return console.nextLine().trim().toLowerCase().startsWith("y");

    }
}
