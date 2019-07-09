// This is a sample class that implements the Guitar interface.  It is not well
// documented.

public class GuitarLite implements Guitar {
    private GuitarString stringA;
    private GuitarString stringC;

    // create two guitar strings, for concert A and C
    public GuitarLite() {
        double CONCERT_A = 440.0;
        double CONCERT_C = CONCERT_A * Math.pow(2, 3.0/12.0);  
        stringA = new GuitarString(CONCERT_A);
        stringC = new GuitarString(CONCERT_C);
    }

    public void playNote(int pitch) {
        if (pitch == 0) {
            stringA.pluck();
        } else if (pitch == 3) {
            stringC.pluck();
        }
    }

    public boolean hasString(char string) {
        return (string == 'a' || string == 'c');
    }
    
    public void pluck(char string) {
        if (string == 'a') {
            stringA.pluck();
        } else if (string == 'c') {
            stringC.pluck();
        }
    }

    public double sample() {
        return stringA.sample() + stringC.sample();
    }

    public void tic() {
        stringA.tic();
        stringC.tic();
    }

    public int time() {
        return -1;  // not implemented
    }
}
