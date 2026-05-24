// Import required packages
import javax.swing.*;         // For GUI components (JFrame, JPanel, JButton, JComboBox, etc.)
import java.awt.*;            // For graphics and layout management
import java.util.Arrays;      // For array manipulation (copyOf, stream, etc.)
import java.util.Random;      // For random array generation

// Main class extending JFrame to create a window for the sorting visualization app
public class Practice extends JFrame {
    // Instance variables
    private int[] array;                          // Array to be sorted
    private SortPanel sortPanel;                  // Custom JPanel to draw bars representing array values
    private JComboBox<String> algorithmComboBox;  // Dropdown for selecting sorting algorithm
    private JRadioButton ascendingRadioButton;    // Radio button for ascending order
    private JRadioButton descendingRadioButton;   // Radio button for descending order
    private JSlider speedControlSlider;           // Slider to control visualization speed
    private JTextField inputArrayField;           // Input field to accept custom array values

    // Constructor initializes the array and sets up UI
    private Practice(int[] array) {
        this.array = Arrays.copyOf(array, array.length);  // Copy input array to avoid direct modification
        this.sortPanel = new SortPanel();                 // Create new SortPanel to visualize array
        initializeUI();                                   // Call method to build and display UI
    }

    // Method to build the GUI
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Exit application when window is closed
        setLayout(new BorderLayout());                    // Use BorderLayout to arrange components

        JPanel controlPanel = new JPanel();               // Panel at top to hold controls

        // Dropdown to choose array input method (user enters manually or randomly generated)
        JComboBox<String> inputMethodComboBox = new JComboBox<>(new String[]{"Enter Array", "Random Array"});
        inputMethodComboBox.setSelectedIndex(0);          // Default is "Enter Array"
        controlPanel.add(inputMethodComboBox);

        // Text field for entering array manually
        inputArrayField = new JTextField(20);
        controlPanel.add(new JLabel("Enter array (comma-separated):"));  // Label for text field
        controlPanel.add(inputArrayField);

        // Enable/disable text field depending on selected input method
        inputMethodComboBox.addActionListener(e -> {
            String selectedInputMethod = (String) inputMethodComboBox.getSelectedItem();
            inputArrayField.setEnabled("Enter Array".equals(selectedInputMethod));
        });

        // Dropdown for choosing sorting algorithm
        algorithmComboBox = new JComboBox<>(new String[]{
            "Bubble Sort", "Insertion Sort", "Selection Sort", "Quick Sort", "Merge Sort", "Heap Sort"});
        algorithmComboBox.setSelectedIndex(0);            // Default: Bubble Sort
        controlPanel.add(algorithmComboBox);

        // Radio buttons for ascending or descending order
        ascendingRadioButton = new JRadioButton("Ascending");
        descendingRadioButton = new JRadioButton("Descending");
        ButtonGroup orderGroup = new ButtonGroup();       // Group ensures only one radio button can be selected
        orderGroup.add(ascendingRadioButton);
        orderGroup.add(descendingRadioButton);
        ascendingRadioButton.setSelected(true);           // Default: Ascending
        controlPanel.add(ascendingRadioButton);
        controlPanel.add(descendingRadioButton);

        // Sort and Reset buttons
        JButton sortButton = new JButton("Sort");
        JButton resetButton = new JButton("Reset");

        // When "Sort" is clicked, sorting starts
        sortButton.addActionListener(e -> performSort((String) inputMethodComboBox.getSelectedItem()));
        // When "Reset" is clicked, array is regenerated
        resetButton.addActionListener(e -> resetArray());

        controlPanel.add(sortButton);
        controlPanel.add(resetButton);

        // Slider for controlling speed of visualization
        speedControlSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 300); // Range: 0–1000 ms delay
        speedControlSlider.setMajorTickSpacing(200);     // Major ticks every 200 units
        speedControlSlider.setPaintTicks(true);          // Show tick marks
        speedControlSlider.setPaintLabels(true);         // Show numeric labels
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(speedControlSlider);

        // Add panels to frame
        add(controlPanel, BorderLayout.NORTH);           // Control panel at top
        add(sortPanel, BorderLayout.CENTER);             // Sorting visualization in center
        pack();                                          // Adjust window size to fit components
        setLocationRelativeTo(null);                     // Center window on screen
        setVisible(true);                                // Show window
    }

    // Helper: set slider programmatically
    private void setVisualizationSpeed(int speed) {
        speedControlSlider.setValue(speed);
    }

    // Helper: get current speed value from slider
    private int getVisualizationSpeed() {
        return speedControlSlider.getValue();
    }

    // Method to handle sorting when user presses "Sort"
    private void performSort(String inputMethod) {
        if ("Enter Array".equals(inputMethod)) {
            // Parse user input string into integer array
            String[] inputArrayStrings = inputArrayField.getText().split(",");
            try {
                array = Arrays.stream(inputArrayStrings)
                        .map(String::trim)              // Remove spaces
                        .mapToInt(Integer::parseInt)    // Convert to int
                        .toArray();
            } catch (NumberFormatException e) {
                // Show error if invalid input
                JOptionPane.showMessageDialog(this, "Invalid array input. Please enter integers separated by commas.");
                return;
            }
        } else {
            // Generate random array if user chose "Random Array"
            int n = array.length;
            int minVal = 0;
            int maxVal = 100;
            array = generateStartingArray(n, minVal, maxVal);
        }

        // Detect which algorithm user selected
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        boolean isAscending = ascendingRadioButton.isSelected(); // Check sort order

        // Execute corresponding sorting algorithm
        switch (selectedAlgorithm) {
            case "Bubble Sort": performBubbleSort(isAscending); break;
            case "Insertion Sort": performInsertionSort(isAscending); break;
            case "Selection Sort": performSelectionSort(isAscending); break;
            case "Quick Sort": performQuickSort(isAscending); break;
            case "Merge Sort": performMergeSort(isAscending); break;
            case "Heap Sort": performHeapSort(isAscending); break;
        }
    }

    // Bubble Sort implementation with visualization
    private void performBubbleSort(boolean isAscending) {
        performSort(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = 0; j < array.length - 1 - i; j++) {
                    if ((isAscending && array[j] > array[j + 1]) || (!isAscending && array[j] < array[j + 1])) {
                        swap(j, j + 1); // Swap elements and update GUI
                    }
                }
            }
        });
    }

    // Insertion Sort
    private void performInsertionSort(boolean isAscending) {
        performSort(() -> {
            for (int i = 1; i < array.length; i++) {
                int current = array[i];
                int j = i;
                while (j > 0 && ((isAscending && array[j - 1] > current) || (!isAscending && array[j - 1] < current))) {
                    array[j] = array[j - 1]; // Shift element
                    j--;
                }
                array[j] = current;
            }
        });
    }

    // Selection Sort
    private void performSelectionSort(boolean isAscending) {
        performSort(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < array.length; j++) {
                    if ((isAscending && array[j] < array[minIndex]) || (!isAscending && array[j] > array[minIndex])) {
                        minIndex = j;
                    }
                }
                if (minIndex != i) {
                    swap(i, minIndex); // Swap found min/max with current
                }
            }
        });
    }

    // Quick Sort
    private void performQuickSort(boolean isAscending) {
        performSort(() -> quickSort(0, array.length - 1, isAscending));
    }
    private void quickSort(int low, int high, boolean isAscending) {
        if (low < high) {
            int pivotIndex = partition(low, high, isAscending); // Partition array
            quickSort(low, pivotIndex - 1, isAscending);        // Sort left
            quickSort(pivotIndex + 1, high, isAscending);       // Sort right
        }
    }
    private int partition(int low, int high, boolean isAscending) {
        int pivot = array[high]; // Choose last element as pivot
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if ((isAscending && array[j] <= pivot) || (!isAscending && array[j] >= pivot)) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high); // Place pivot in correct position
        return i + 1;
    }

    // Merge Sort
    private void performMergeSort(boolean isAscending) {
        performSort(() -> mergeSort(0, array.length - 1, isAscending));
    }
    private void mergeSort(int low, int high, boolean isAscending) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(low, mid, isAscending);       // Left half
            mergeSort(mid + 1, high, isAscending);  // Right half
            merge(low, mid, high, isAscending);     // Merge
        }
    }
    private void merge(int low, int mid, int high, boolean isAscending) {
        int[] temp = Arrays.copyOf(array, array.length); // Copy of array
        Color[] tempColors = Arrays.copyOf(sortPanel.barColors, sortPanel.barColors.length);
        int i = low, j = mid + 1, k = low;
        while (i <= mid && j <= high) {
            if ((isAscending && temp[i] <= temp[j]) || (!isAscending && temp[i] >= temp[j])) {
                array[k] = temp[i];
                sortPanel.barColors[k] = tempColors[i];
                i++;
            } else {
                array[k] = temp[j];
                sortPanel.barColors[k] = tempColors[j];
                j++;
            }
            k++;
        }
        while (i <= mid) { // Copy remaining elements
            array[k] = temp[i];
            sortPanel.barColors[k] = tempColors[i];
            i++; k++;
        }
        sortPanel.repaint(); // Refresh panel
    }

    // Heap Sort
    private void performHeapSort(boolean isAscending) {
        performSort(() -> {
            int n = array.length;
            // Build heap
            for (int i = n / 2 - 1; i >= 0; i--) {
                heapify(n, i, isAscending);
            }
            // Extract elements one by one
            for (int i = n - 1; i > 0; i--) {
                swap(0, i); // Move current root to end
                heapify(i, 0, isAscending);
            }
        });
    }
    private void heapify(int n, int i, boolean isAscending) {
        int largest = i; // Assume current as largest
        int left = 2 * i + 1, right = 2 * i + 2;
        if (left < n && ((isAscending && array[left] > array[largest]) || (!isAscending && array[left] < array[largest]))) {
            largest = left;
        }
        if (right < n && ((isAscending && array[right] > array[largest]) || (!isAscending && array[right] < array[largest]))) {
            largest = right;
        }
        if (largest != i) {
            swap(i, largest); // Swap and continue heapify
            heapify(n, largest, isAscending);
        }
    }

    // Common sorting execution method: runs in a new thread
    private void performSort(Runnable sortingAlgorithm) {
        new Thread(() -> {
            sortingAlgorithm.run();                        // Run sorting algorithm
            Color[] sortedColors = new Color[array.length];
            Arrays.fill(sortedColors, Color.GREEN);        // Mark all as sorted
            sortPanel.updateArray(array, sortedColors);    // Update panel
        }).start();
    }

    // Reset array to random values
    private void resetArray() {
        array = generateStartingArray(array.length, 0, 100);
        Color[] resetColors = new Color[array.length];
        Arrays.fill(resetColors, Color.RED);              // Reset bars to red
        sortPanel.updateArray(array, resetColors);
    }

    // Swap two elements in array and update visualization
    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;

        // Swap colors too
        Color tempColor = sortPanel.barColors[i];
        sortPanel.barColors[i] = sortPanel.barColors[j];
        sortPanel.barColors[j] = tempColor;
        sortPanel.repaint(); // Redraw bars

        try {
            Thread.sleep(getVisualizationSpeed());        // Delay for visualization
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Inner class for visualization panel
    private class SortPanel extends JPanel {
        private static final int BAR_WIDTH = 50;          // Fixed width of each bar
        private static final int BAR_SPACING = 20;        // Space between bars
        private Color[] barColors;                        // Store color of each bar

        // Constructor initializes all bars as red
        private SortPanel() {
            this.barColors = new Color[array.length];
            Arrays.fill(barColors, Color.RED);
        }

        // Update array and bar colors, then repaint
        private void updateArray(int[] newArray, Color[] newColors) {
            array = Arrays.copyOf(newArray, newArray.length);
            barColors = Arrays.copyOf(newColors, newColors.length);
            repaint();
        }

        // Custom drawing of bars
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);                       // Clear panel before repaint
            Graphics2D g2d = (Graphics2D) g;

            int x = 0;
            for (int i = 0; i < array.length; i++) {
                int barHeight = array[i] * 5;              // Scale values for visualization
                g2d.setColor(barColors[i]);                // Set bar color
                g2d.fillRect(x, getHeight() - barHeight, BAR_WIDTH, barHeight);

                // Draw number above each bar
                g2d.setColor(Color.BLACK);
                g2d.drawString(Integer.toString(array[i]), x + BAR_WIDTH / 2, getHeight() - barHeight - 5);

                x += BAR_WIDTH + BAR_SPACING;              // Move to next bar position
            }
        }
    }

    // Main method: entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {                 // Run on EDT (thread-safe for Swing)
            int n = 20;                                    // Default array size
            int minVal = 0, maxVal = 100;                  // Value range
            int[] array = generateStartingArray(n, minVal, maxVal);
            new Practice(array);                      // Launch app
        });
    }

    // Generate random array
    private static int[] generateStartingArray(int n, int minVal, int maxVal) {
        int[] array = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(maxVal - minVal + 1) + minVal;
        }
        return array;
    }
}
