// The BitOutputStream and BitInputStream classes provide the ability to
// write and read individual bits to a file in a compact form.  One major
// limitation of this approach is that the resulting file will always have
// a number of bits that is a multiple of 8.  In effect, whatever bits are
// output to the file are padded at the end with 0's to make the total
// number of bits a multiple of 8.

import java.io.*;
import java.util.*;

public class BitOutputStream {
	private PrintStream output;
    private List<Integer> buffer;
	private int currentByte; // a buffer used to build up next set of digits
	private int numBits; // how many digits are currently in the buffer
	private boolean debug; // set to true to write ASCII 0s and 1s rather than
							// bits

	private static final int BYTE_SIZE = 8; // digits per byte

	// Creates a BitOutputStream sending output to the given stream. If debug
	// is set to true, bits are printed as ASCII 0s and 1s.
	public BitOutputStream(PrintStream output, boolean debug) {
        this.buffer = new ArrayList<Integer>();
		this.output = output;
		this.debug = debug;
	}

	// Writes given bit to output
	public void write(int bit) {
		if (this.debug) {
			System.out.print(bit);
		} 
        if (bit < 0 || bit > 1) {
            throw new IllegalArgumentException("Illegal bit: " + bit);
        }
        this.currentByte += bit << this.numBits;
        this.numBits++;
        if (this.numBits == BYTE_SIZE) {
            this.buffer.add(this.currentByte);
            this.numBits = 0;
            this.currentByte = 0;
        }
	}

	// post: output is closed
	public void close() {
        int remaining = BYTE_SIZE - this.numBits;

        if (remaining == 8) {
            remaining = 0;
        } 

        /* Flush the last byte (if there is one) */
        if (remaining > 0) {
            this.buffer.add(this.currentByte);
        }

        /* Now that we've received all the output, prepend it with the number
         * of missing bits from the end.
         */
		this.output.write(remaining);
        for (int b : this.buffer) {
            this.output.write(b);
        }
		this.output.close();
	}

	// included to ensure that the stream is closed
	protected void finalize() {
		this.close();
	}
}
