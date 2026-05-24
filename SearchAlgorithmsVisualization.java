import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class SearchAlgorithmsVisualization extends JFrame {
    private int[] array;
    private SearchPanel searchPanel;
    private JTextField inputArrayField;
    private JTextField targetField;
    private JComboBox<String> algorithmComboBox;
    private JButton searchButton;
    private JSlider speedControlSlider; // Added JSlider for visualization speed
    private int target;

    private SearchAlgorithmsVisualization() {
        this.array = new int[0];
        this.searchPanel = new SearchPanel();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();

        // Text field for entering the array
        inputArrayField = new JTextField(20);
        controlPanel.add(new JLabel("Enter array (comma-separated):"));
        controlPanel.add(inputArrayField);

        // Text field for entering the target value
        targetField = new JTextField(5);
        controlPanel.add(new JLabel("Enter target value:"));
        controlPanel.add(targetField);

        // Combo box for selecting the search algorithm
        algorithmComboBox = new JComboBox<>(new String[]{"Linear Search", "Binary Search"});
        controlPanel.add(new JLabel("Select search algorithm:"));
        controlPanel.add(algorithmComboBox);

        // Search button
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        controlPanel.add(searchButton);

        // JSlider for visualization speed
        speedControlSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500); // Range from 0 to 1000, initial value 500
        speedControlSlider.setMajorTickSpacing(200);
        speedControlSlider.setPaintTicks(true);
        speedControlSlider.setPaintLabels(true);
        controlPanel.add(new JLabel("Visualization Speed:"));
        controlPanel.add(speedControlSlider);

        add(controlPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void performSearch() {
        // Parse the user input to get the array
        String[] inputArrayStrings = inputArrayField.getText().split(",");
        try {
            array = Arrays.stream(inputArrayStrings)
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid array input. Please enter integers separated by commas.");
            return;
        }

        // Parse the user input to get the target value
        try {
            target = Integer.parseInt(targetField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid target value. Please enter an integer.");
            return;
        }

        searchButton.setEnabled(false); // Disable the search button during visualization

        new Thread(() -> {
            switch ((String) algorithmComboBox.getSelectedItem()) {
                case "Linear Search":
                    linearSearchVisualization();
                    break;
                case "Binary Search":
                    binarySearchVisualization();
                    break;
                // Add more cases for additional search algorithms
            }

            searchButton.setEnabled(true); // Enable the search button after visualization
        }).start();
    }

    private void linearSearchVisualization() {
        int speed = speedControlSlider.getValue();

        for (int i = 0; i < array.length; i++) {
            searchPanel.setCurrentIndex(i);
            searchPanel.repaint();

            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            if (array[i] == target) {
                JOptionPane.showMessageDialog(this, "Target found at index: " + i);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Target not found in the array.");
    }

    private void binarySearchVisualization() {
        Arrays.sort(array); // Binary search requires a sorted array

        int low = 0;
        int high = array.length - 1;
        int speed = speedControlSlider.getValue();

        while (low <= high) {
            int mid = (low + high) / 2;

            searchPanel.setCurrentIndex(mid);
            searchPanel.repaint();

            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            if (array[mid] == target) {
                JOptionPane.showMessageDialog(this, "Target found at index: " + mid);
                return;
            } else if (array[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        JOptionPane.showMessageDialog(this, "Target not found in the array.");
    }

    private class SearchPanel extends JPanel {
        private static final int BAR_WIDTH = 50;
        private static final int BAR_SPACING = 20;
        private int currentIndex = -1;

        private void setCurrentIndex(int index) {
            this.currentIndex = index;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int x = 0;
            for (int i = 0; i < array.length; i++) {
                int barHeight = array[i] * 5;
                g2d.setColor(Color.BLUE);
                g2d.fillRect(x, getHeight() - barHeight, BAR_WIDTH, barHeight);

                if (i == currentIndex) {
                    g2d.setColor(Color.RED);
                    g2d.fillRect(x, getHeight() - barHeight, BAR_WIDTH, barHeight);
                }

                // Display the integer values on top of the bars
                g2d.setColor(Color.BLACK);
                g2d.drawString(Integer.toString(array[i]), x + BAR_WIDTH / 2, getHeight() - barHeight - 5);

                x += BAR_WIDTH + BAR_SPACING;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SearchAlgorithmsVisualization::new);
    }
}
