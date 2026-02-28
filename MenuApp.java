import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class MenuApp extends JFrame {

    private final JTextArea textArea = new JTextArea();
    private final JPanel mainPanel = new JPanel(new BorderLayout());

    // Option #3: one random green hue per program execution
    private Color greenColor = null;
    private final JMenuItem randomGreenItem = new JMenuItem("3) Random Green Hue");

    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MenuApp() {
        super("Menu Demo (Java Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Main panel (this is what we recolor)
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        mainPanel.setBackground(new Color(0xF2, 0xF2, 0xF2));

        // Text box (scrollable)
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setJMenuBar(buildMenuBar());

        setContentPane(mainPanel);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem printDateTime = new JMenuItem(new AbstractAction("1) Print Date/Time") {
            @Override
            public void actionPerformed(ActionEvent e) {
                printDateTime();
            }
        });

        JMenuItem saveToLog = new JMenuItem(new AbstractAction("2) Save Text to log.txt") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToLog();
            }
        });

        randomGreenItem.addActionListener(e -> applyRandomGreen());

        JMenuItem exit = new JMenuItem(new AbstractAction("4) Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // clean exit
                dispose();
                System.exit(0);
            }
        });

        menu.add(printDateTime);
        menu.add(saveToLog);
        menu.add(randomGreenItem);
        menu.addSeparator();
        menu.add(exit);

        menuBar.add(menu);
        return menuBar;
    }

    // 1) When selected, print date/time in text area
    private void printDateTime() {
        String now = LocalDateTime.now().format(DT_FMT);
        textArea.append(now + System.lineSeparator());
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    // 2) When selected, write text area contents to "log.txt"
    private void saveToLog() {
        String content = textArea.getText();
        Path path = Paths.get("log.txt");
        try {
            Files.write(path, content.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            JOptionPane.showMessageDialog(this,
                    "Wrote text box contents to \"log.txt\".",
                    "Saved",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not write log.txt:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // 3) Change frame background to a random green hue.
    //    Requirement: show the initial random hue each time selected for a single run.
    //    Implementation: generate ONCE per run, reuse thereafter, and update menu text to show the hex.
    private void applyRandomGreen() {
        if (greenColor == null) {
            Random rand = new Random();

            // green-ish: keep R and B low, G higher
            int r = rand.nextInt(81);           // 0..80
            int g = 120 + rand.nextInt(136);    // 120..255
            int b = rand.nextInt(81);           // 0..80

            greenColor = new Color(r, g, b);

            String hex = String.format("#%02x%02x%02x", r, g, b);
            randomGreenItem.setText("3) Random Green Hue (" + hex + ")");
        }

        mainPanel.setBackground(greenColor);
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        // Use system look and feel (optional, but nice)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MenuApp app = new MenuApp();
            app.setVisible(true);
        });
    }
}