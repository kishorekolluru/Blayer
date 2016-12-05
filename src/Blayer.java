import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Blayer {
    static Set<Integer> maskSet = new HashSet<Integer>();
    InetAddress address;
    Socket socket;
    static OutputStream out;

    public static void main(String[] args) {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err
                    .println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        // Construct the example object and initialze native hook.
        GlobalScreen.getInstance().addNativeKeyListener(
                new NativeKeyListener() {

                    @Override
                    public void nativeKeyPressed(NativeKeyEvent nke) {
                        if (nke.getKeyCode() == NativeKeyEvent.VK_ESCAPE) {
                            GlobalScreen.unregisterNativeHook();
                            System.exit(0);
                        }
                        maskSet.add(nke.getKeyCode());
                        System.out.println("pressed");
                    }

                    @Override
                    public void nativeKeyReleased(NativeKeyEvent nke) {
                        if (maskSet.contains(nke.getKeyCode())) {
                            System.out.println("removed " + nke.getKeyCode());
                            //System.out.println(NativeKeyEvent.VK_DEAD_TILDE);
                            maskSet.remove(nke.getKeyCode());
                        }
                    }

                    @Override
                    public void nativeKeyTyped(NativeKeyEvent nke) {
                        if (!maskSet.isEmpty()
                                && maskSet
                                .contains(192)) {//for `
                            System.out.println("relrese");
                            if (nke.getKeyChar() == 'r' || nke.getKeyChar() == 'R') {
                                issueCommand("pause\n".getBytes());
                            }
                        }

                    }
                });
        try {
            new Blayer().startProcess();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void issueCommand(byte[] commd) {
        try {
            out.write(commd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Process startProcess() throws IOException {
        String user = System.getProperty("user.name");
        JFileChooser chooser = new JFileChooser();

        chooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Mp3 files", "mp3");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(chooser);
        chooser.grabFocus();
        File[] files = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            files = chooser.getSelectedFiles();
        }

        Process vlcProcess = new ProcessBuilder(
                "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe", "--extraintf",
                "rc",// required for vlc to take commands from remote( in this
                // instance from this program)
                "--rc-host", "localhost:8080").start();// ,"file:///C:\\Users\\"+user+"\\Music\\new songs").start();

        address = InetAddress.getByName("localhost");
        socket = new Socket(address, 8080);
        final BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = socket.getOutputStream();

        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i].getAbsolutePath());
            out.write(("enqueue " + files[i] + "\n").getBytes());
        }
        out.write("play\n".getBytes());
        for (int i = 0; i < files.length; i++) {
            out.write(("enqueue " + files[i] + "\n").getBytes());
        }

        char ch;
        do {
            System.out.println(
                    "1. Next\n2.Previous\n3.Pause\n4.Quit\n5.Volume up\n6.Volume Down");
            System.out.println("Choose One");
            Scanner ob = new Scanner(System.in);
            ch = ob.next().charAt(0);
            switch (ch) {
                case '1':
                    out.write("next\n".getBytes());
                    break;
                case '2':
                    out.write("prev\n".getBytes());
                    break;
                case '3':
                    out.write("pause\n".getBytes());
                    break;
                case '4':
                    out.write("quit\n".getBytes());
                case '5':
                    out.write("volup 1\n".getBytes());
                    break;
                case '6':
                    out.rite("voldown 1\n".getBytes());
                    break;
                case '7':
                    JFileChooser
                            chooser = new JFileChooser();

                    chooser.setMultiSelectionEnabled(true);
                    FileNameExtensionFilter
                            filter = new FileNameExtensionFilter("Mp3 files", "mp3");
                    chooser.setFileFilter(filter);
                    int returnVal =
                            chooser.showOpenDialog(chooser);
                    chooser.grabFocus();
                    File[]
                            files = null;
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        files = chooser.getSelectedFiles();
                    }
                    for (int i = 0; i < files.length;
                         i++) {
                        System.out.println(files[i].getAbsolutePath());
                        out.write(("enqueue " + files[i] + "\n").getBytes());
                    }
                    out.write("play\n".getBytes());
                    break;
            } //
            out.write("play\n".getBytes()); // Thread.sleep(9000); //
            out.write("volup 90\n".getBytes());
        } while (ch > 0);
        return null;
    }

}
