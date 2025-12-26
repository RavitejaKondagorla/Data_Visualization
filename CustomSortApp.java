import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class CustomSortApp extends JFrame {
    private int[] array;
    private SortPanel sortPanel;
    private JComboBox<String> algorithmComboBox;
    private JRadioButton ascendingRadioButton;
    private JRadioButton descendingRadioButton;
    private JSlider speedControlSlider; // Use JSlider for speed control
    private JTextField inputArrayField;

    private CustomSortApp(int[] array) {
        this.array = Arrays.copyOf(array, array.length);
        this.sortPanel = new SortPanel();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();

        // Combo box for selecting array input method
        JComboBox<String> inputMethodComboBox = new JComboBox<>(new String[]{"Enter Array", "Random Array"});
        inputMethodComboBox.setSelectedIndex(0); // Default selection
        controlPanel.add(inputMethodComboBox);

        // Text field for entering the array
        inputArrayField = new JTextField(20);
        controlPanel.add(new JLabel("Enter array (comma-separated):"));
        controlPanel.add(inputArrayField);
        inputMethodComboBox.addActionListener(e -> {
            String selectedInputMethod = (String) inputMethodComboBox.getSelectedItem();
            inputArrayField.setEnabled("Enter Array".equals(selectedInputMethod));
        });
        // Combo box for selecting the sorting algorithm
        algorithmComboBox = new JComboBox<>(new String[]{"Bubble Sort", "Insertion Sort", "Selection Sort", "Quick Sort", "Merge Sort", "Heap Sort"});
        algorithmComboBox.setSelectedIndex(0); // Default selection
        controlPanel.add(algorithmComboBox);

        // Radio buttons for selecting the order
        ascendingRadioButton = new JRadioButton("Ascending");
        descendingRadioButton = new JRadioButton("Descending");
        ButtonGroup orderGroup = new ButtonGroup();
        orderGroup.add(ascendingRadioButton);
        orderGroup.add(descendingRadioButton);
        ascendingRadioButton.setSelected(true); // Default selection
        controlPanel.add(ascendingRadioButton);
        controlPanel.add(descendingRadioButton);

        JButton sortButton = new JButton("Sort");
        JButton resetButton = new JButton("Reset");

        sortButton.addActionListener(e -> performSort((String) inputMethodComboBox.getSelectedItem()));
        resetButton.addActionListener(e -> resetArray());

        controlPanel.add(sortButton);
        controlPanel.add(resetButton);

        // Use JSlider for speed control
        speedControlSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 300); // Range from 0 to 1000, initial value 300
        speedControlSlider.setMajorTickSpacing(200);
        speedControlSlider.setPaintTicks(true);
        speedControlSlider.setPaintLabels(true);

        // Move speed control below the "Sort" button
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(speedControlSlider);

        add(controlPanel, BorderLayout.NORTH);
        add(sortPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setVisualizationSpeed(int speed) {
        speedControlSlider.setValue(speed);
    }

    private int getVisualizationSpeed() {
        return speedControlSlider.getValue();
    }

    private void performSort(String inputMethod) {
        if ("Enter Array".equals(inputMethod)) {
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
        } else {
            // Randomly generate the array
            int n = array.length;
            int minVal = 0;
            int maxVal = 100;
            array = generateStartingArray(n, minVal, maxVal);
        }

        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        boolean isAscending = ascendingRadioButton.isSelected();
    
        switch (selectedAlgorithm) {
            case "Bubble Sort":
                performBubbleSort(isAscending);
                break;
            case "Insertion Sort":
                performInsertionSort(isAscending);
                break;
            case "Selection Sort":
                performSelectionSort(isAscending);
                break;
            case "Quick Sort":
                performQuickSort(isAscending);
                break;
            case "Merge Sort":
                performMergeSort(isAscending);
                break;
            case "Heap Sort":
                performHeapSort(isAscending);
                break;
        }
    }

    private void performBubbleSort(boolean isAscending) {
        performSort(() -> {
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = 0; j < array.length - 1 - i; j++) {
                    if ((isAscending && array[j] > array[j + 1]) || (!isAscending && array[j] < array[j + 1])) {
                        swap(j, j + 1);
                    }
                }
            }
        });
    }
    private void performInsertionSort(boolean isAscending) {
        performSort(() -> {
            for (int i = 1; i < array.length; i++) {
                int current = array[i];
                int j = i;

                while (j > 0 && ((isAscending && array[j - 1] > current) || (!isAscending && array[j - 1] < current))) {
                    array[j] = array[j - 1];
                    j--;
                }

                array[j] = current;
            }
        });
    }
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
                    swap(i, minIndex);
                }
            }
        });
    }
    private void performQuickSort(boolean isAscending) {
        performSort(() -> quickSort(0, array.length - 1, isAscending));
    }
    private void quickSort(int low, int high, boolean isAscending) {
        if (low < high) {
            int pivotIndex = partition(low, high, isAscending);
            quickSort(low, pivotIndex - 1, isAscending);
            quickSort(pivotIndex + 1, high, isAscending);
        }
    }
    private int partition(int low, int high, boolean isAscending) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if ((isAscending && array[j] <= pivot) || (!isAscending && array[j] >= pivot)) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        return i + 1;
    }
    private void performMergeSort(boolean isAscending) {
        performSort(() -> mergeSort(0, array.length - 1, isAscending));
    }
    private void mergeSort(int low, int high, boolean isAscending) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(low, mid, isAscending);
            mergeSort(mid + 1, high, isAscending);
            merge(low, mid, high, isAscending);
        }
    }
    private void merge(int low, int mid, int high, boolean isAscending) {
        int[] temp = Arrays.copyOf(array, array.length);
        Color[] tempColors = Arrays.copyOf(sortPanel.barColors, sortPanel.barColors.length);
        int i = low;
        int j = mid + 1;
        int k = low;
        while (i <= mid && j <= high) {
            if ((isAscending && temp[i] <= temp[j]) || (!isAscending && temp[i] >= temp[j])) {
                array[k] = temp[i];
                sortPanel.barColors[k] = tempColors[i];
                k++;
                i++;
            } else {
                array[k] = temp[j];
                sortPanel.barColors[k] = tempColors[j];
                k++;
                j++;
            }
        }
        while (i <= mid) {
            array[k] = temp[i];
            sortPanel.barColors[k] = tempColors[i];
            k++;
            i++;
        }
        sortPanel.repaint();
    }
    private void performHeapSort(boolean isAscending) {
        performSort(() -> {
            int n = array.length;
            for (int i = n / 2 - 1; i >= 0; i--) {
                heapify(n, i, isAscending);
            }
            for (int i = n - 1; i > 0; i--) {
                swap(0, i);
                heapify(i, 0, isAscending);
            }
        });
    }
    private void heapify(int n, int i, boolean isAscending) {
        int largest = i; 
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && ((isAscending && array[left] > array[largest]) || (!isAscending && array[left] < array[largest]))) {
            largest = left;
        }
        if (right < n && ((isAscending && array[right] > array[largest]) || (!isAscending && array[right] < array[largest]))) {
            largest = right;
        }
        if (largest != i) {
            swap(i, largest);
            heapify(n, largest, isAscending);
        }
    }
    private void performSort(Runnable sortingAlgorithm) {
        new Thread(() -> {
            sortingAlgorithm.run();
            Color[] sortedColors = new Color[array.length];
            Arrays.fill(sortedColors, Color.GREEN); // Sorted color
            sortPanel.updateArray(array, sortedColors);
        }).start();
    }
    private void resetArray() {
        array = generateStartingArray(array.length, 0, 100);
        Color[] resetColors = new Color[array.length];
        Arrays.fill(resetColors, Color.RED); // Reset color
        sortPanel.updateArray(array, resetColors);
    }

    private void swap(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;

        Color tempColor = sortPanel.barColors[i];
        sortPanel.barColors[i] = sortPanel.barColors[j];
        sortPanel.barColors[j] = tempColor;
        sortPanel.repaint();

        try {
            Thread.sleep(getVisualizationSpeed());  // Adjust the sleep duration for visualization speed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private class SortPanel extends JPanel {
        private static final int BAR_WIDTH = 50;
        private static final int BAR_SPACING = 20;
        private Color[] barColors;

        private SortPanel() {
            this.barColors = new Color[array.length];
            Arrays.fill(barColors, Color.RED); // Initial color
        }

        private void updateArray(int[] newArray, Color[] newColors) {
            array = Arrays.copyOf(newArray, newArray.length);
            barColors = Arrays.copyOf(newColors, newColors.length);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int x = 0;
            for (int i = 0; i < array.length; i++) {
                int barHeight = array[i] * 5;  // Adjust for visualization
                g2d.setColor(barColors[i]);
                g2d.fillRect(x, getHeight() - barHeight, BAR_WIDTH, barHeight);

                // Display the integer values on top of the bars
                g2d.setColor(Color.BLACK);
                g2d.drawString(Integer.toString(array[i]), x + BAR_WIDTH / 2, getHeight() - barHeight - 5);

                x += BAR_WIDTH + BAR_SPACING;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int n = 20;
            int minVal = 0;
            int maxVal = 100;
            int[] array = generateStartingArray(n, minVal, maxVal);

            CustomSortApp customSortApp = new CustomSortApp(array);
        });
    }

    private static int[] generateStartingArray(int n, int minVal, int maxVal) {
        int[] array = new int[n];
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(maxVal - minVal + 1) + minVal;
        }
        return array;
    }
    
}