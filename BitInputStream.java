// The BitOutputStream and BitInputStream classes provide the ability to
// write and read individual bits to a file in a compact form.  One major
// limitation of this approach is that the resulting file will always have
// a number of bits that is a multiple of 8.  In effect, whatever bits are
// output to the file are padded at the end with 0's to make the total
// number of bits a multiple of 8.
//
// BitInputStream has the following public methods:
//     public BitInputStream(String file)
//         opens an input stream with the given file name
//     public int nextBit()
//         reads the next bit from input (throws -1 if at end of file)
//     public boolean hasNextBit()
//         returns true if there's another bit in the input stream to be read
//     public void close()
//         closes the input

import java.io.*;
import java.util.*;

public class BitInputStream {
    private FileInputStream input;
    private int currentByte;     // current set of bits (buffer)
    private int nextByte;        // next set of bits (buffer)
    private int numBits;         // how many bits from buffer have been used
    private int remainingAtEnd;  // how many bits will be remaining at the end
                                 // after we're done

    private static final int BYTE_SIZE = 8;  // bits per byte

    // pre : given file name is legal
    // post: creates a BitInputStream reading input from the file
    public BitInputStream(String file) {
        try {
            this.input = new FileInputStream(file);
            
            // Read in the number of remaining bits at the end
            this.remainingAtEnd = this.input.read();

            // Set up the nextByte field.
            this.nextByte = this.input.read();
        } catch (IOException ex) {
            throw new RuntimeException(ex.toString());
        }

        this.nextByte();
    }

    public boolean hasNextBit() {
        boolean atEnd = this.currentByte == -1;
        boolean onlyRemaining = this.nextByte == -1 
                && BYTE_SIZE - this.numBits == this.remainingAtEnd;
        return !atEnd && !onlyRemaining;
    }

    // post: reads next bit from input (-1 if at end of file)
    //       throws NoSuchElementException if there is no bit to return
    public int nextBit() {
        // if at eof, throw exception
        if (!this.hasNextBit()) {
            throw new NoSuchElementException();
        }
        int result = this.currentByte % 2;
        this.currentByte /= 2;
        this.numBits++;
        if (this.numBits == BYTE_SIZE) {
            this.nextByte();
        }
        return result;
    }

    // post: refreshes the internal buffer with the next BYTE_SIZE bits
    private void nextByte() {
        this.currentByte = this.nextByte;
        if (this.currentByte != -1) {
            try {
                this.nextByte = this.input.read();
            } catch (IOException e) {
                throw new RuntimeException(e.toString());
            }
        }

        this.numBits = 0;
    }

    // post: input is closed
    public void close() {
        try {
            this.input.close();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    // included to ensure that the stream is closed
    protected void finalize() {
        this.close();
    }
}
