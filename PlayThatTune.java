import java.io.*;
import java.util.*;
import javax.swing.*;

public class PlayThatTune {
    public static void main(String[] args) throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser(new File("./play"));
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            Scanner input = new Scanner(chooser.getSelectedFile());
            Guitar g = new Guitar37();
            while (input.hasNextInt()) {
                int pitch = input.nextInt();
                double duration = input.nextDouble();
                g.playNote(pitch);
                advance(duration, g);
            }
        }
    }

    public static void advance(double duration, Guitar g) {
        int tics = (int) Math.round(duration * StdAudio.SAMPLE_RATE);
        for (int i = 0; i < tics; i++) {
            StdAudio.play(g.sample());
            g.tic();
        }
    }
}
