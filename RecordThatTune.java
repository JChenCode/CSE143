// This program can be used to record a song played using a Guitar37 object.
// Start the program and play whatever notes you want and type the letter "s"
// to stop recording.  If you don't type "s", it won't properly record the last
// note that you play.

import java.io.*;
import java.util.*;

public class RecordThatTune {
    public static void main(String[] args) throws FileNotFoundException {
        giveIntro();
        Scanner console = new Scanner(System.in);
        System.out.print("output file name? ");
        String fileName = console.nextLine();
        PrintStream output = new PrintStream(new File(fileName));

        Guitar g = new Guitar37();
        boolean done = false;
        int oldTime = -1;  // special value to indicate no note played
        char oldKey = 'a';
        while (!done) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (g.hasString(key)) {
                    g.pluck(key);
                    recordNote(output, g, oldTime, oldKey);
                    oldKey = key;
                    oldTime = g.time();
                } else if (key == 's') {
                    done = true;
                } else {
                    System.out.println("bad key: " + key);
                }
            }
            // send the result to the sound card
            StdAudio.play(g.sample());
            g.tic();
        }
        recordNote(output, g, oldTime, oldKey);
    }

    // introduces the program to the user
    public static void giveIntro() {
        System.out.println("This program allows you to record notes");
        System.out.println("on a Guitar object and store it in an output");
        System.out.println("file.  Hit the 's' key to stop recording and");
        System.out.println("then quit the application.");
        System.out.println();
    }

    // records the last note played (if any) and writes the information to the
    // given output file; assumes oldTime is -1 if there was no previous note
    public static void recordNote(PrintStream output, Guitar g, int oldTime,
                                  int oldKey) {
        if (oldTime != -1) {
            int pitch = Guitar37.KEYBOARD.indexOf(oldKey) - 24;
            int tics = g.time() - oldTime;
            double duration = (double) tics / StdAudio.SAMPLE_RATE;
            output.println(pitch + " " + duration);
        }
    }
}
