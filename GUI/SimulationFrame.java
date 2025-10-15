package org.example.GUI;

import org.example.BusinessLogic.SelectionPolicy;
import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {
    private JPanel contentPane;
    private JPanel inputPanel1;
    private JPanel inputPanel2;
    private JPanel buttonPanel;
    private JPanel resultPanel;
    private JTextField textNbOfClients;
    private JTextField textNbOfQueues;
    private JTextField textTimeLimit;
    private JTextField textMaxArrivalTime;
    private JTextField textMinArrivalTime;
    private JTextField textMinServiceTime;
    private JTextField textMaxServiceTime;
    private JButton startButton;
    private JButton clearButton;
    private JButton stopButton;
    private JComboBox<SelectionPolicy> policyJComboBox;
    private JScrollPane testResultScroll;
    private JTextArea testResult;

    public SimulationFrame(String name){
        super(name);
        this.prepareGUI();
    }

    public void prepareGUI(){
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setSize(700, 700);
        this.setResizable(true);
        this.setLocation(350, 0);
        this.contentPane = new JPanel(new GridBagLayout());
        this.prepareInputPanel1();
        this.prepareInputPanel2();
        this.prepareButtonPanel();
        this.prepareResultPanel();
        this.setContentPane(this.contentPane);
    }

    public void prepareInputPanel1(){
        this.inputPanel1 = new JPanel(new GridBagLayout());
        GridBagConstraints c0 = setConstraints(0,0,1,1,1.0,1.0);
        GridBagConstraints c1 = setConstraints(0,0,1,1,1.0,1.0);
        GridBagConstraints c2 = setConstraints(0,1,1,1,1.0,1.0);
        GridBagConstraints c3 = setConstraints(1,0,1,1,1.0,1.0);
        GridBagConstraints c4 = setConstraints(1,1,1,1,1.0,1.0);
        GridBagConstraints c5 = setConstraints(2,0,1,1,1.0,1.0);
        GridBagConstraints c6 = setConstraints(2,1,1,1,1.0,1.0);
        GridBagConstraints c7 = setConstraints(3,0,1,2,1.0,1.0);
        this.inputPanel1.add(new JLabel("Number of clients:"), c1);
        this.textNbOfClients = new JTextField();
        this.inputPanel1.add(this.textNbOfClients, c2);
        this.inputPanel1.add(new JLabel("Number of queues:"), c3);
        this.textNbOfQueues = new JTextField();
        this.inputPanel1.add(this.textNbOfQueues, c4);
        this.inputPanel1.add(new JLabel("Time limit:"), c5);
        this.textTimeLimit = new JTextField();
        this.inputPanel1.add(this.textTimeLimit, c6);
        this.policyJComboBox = new JComboBox<>(new SelectionPolicy[]{SelectionPolicy.SHORTEST_TIME, SelectionPolicy.SHORTEST_QUEUE});
        this.inputPanel1.add(this.policyJComboBox, c7);
        this.contentPane.add(this.inputPanel1, c0);
    }

    public GridBagConstraints setConstraints(int gridx, int gridy, int gridWidth, int gridHeight, double weightx, double weighy) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridWidth;
        c.weightx = weightx;
        c.weighty = weighy;
        c.gridheight = gridHeight;
        c.insets = new Insets(5, 5, 5, 5);
        return c;
    }

    public JTextField getTextNbOfClients(){
        return this.textNbOfClients;
    }

    public JTextField getTextNbOfQueues(){
        return this.textNbOfQueues;
    }

    public JTextField getTextTimeLimit(){
        return this.textTimeLimit;
    }

    public JComboBox<SelectionPolicy> getPolicyJComboBox(){
        return this.policyJComboBox;
    }

    public void prepareInputPanel2(){
        this.inputPanel2 = new JPanel(new GridBagLayout());
        GridBagConstraints c0 = setConstraints(0,1,1,1,1.0,1.0);
        GridBagConstraints c1 = setConstraints(0,0,1,1,1.0,1.0);
        GridBagConstraints c2 = setConstraints(0,1,1,1,1.0,1.0);
        GridBagConstraints c3 = setConstraints(1,0,1,1,1.0,1.0);
        GridBagConstraints c4 = setConstraints(1,1,1,1,1.0,1.0);
        GridBagConstraints c5 = setConstraints(2,0,1,1,1.0,1.0);
        GridBagConstraints c6 = setConstraints(2,1,1,1,1.0,1.0);
        GridBagConstraints c7 = setConstraints(3,0,1,1,1.0,1.0);
        GridBagConstraints c8 = setConstraints(3,1,1,1,1.0,1.0);
        this.inputPanel2.add(new JLabel("Min arrival time:"), c1);
        this.textMinArrivalTime = new JTextField();
        this.inputPanel2.add(this.textMinArrivalTime, c2);
        this.inputPanel2.add(new JLabel("Max arrival time:"), c3);
        this.textMaxArrivalTime = new JTextField();
        this.inputPanel2.add(this.textMaxArrivalTime, c4);
        this.inputPanel2.add(new JLabel("Min service time:"), c5);
        this.textMinServiceTime = new JTextField();
        this.inputPanel2.add(this.textMinServiceTime, c6);
        this.inputPanel2.add(new JLabel("Max service time:"), c7);
        this.textMaxServiceTime = new JTextField();
        this.inputPanel2.add(this.textMaxServiceTime, c8);
        this.contentPane.add(this.inputPanel2, c0);
    }

    public JTextField getTextMinArrivalTime(){
        return this.textMinArrivalTime;
    }

    public JTextField getTextMaxArrivalTime(){
        return  this.textMaxArrivalTime;
    }

    public JTextField getTextMinServiceTime(){
        return this.textMinServiceTime;
    }

    public JTextField getTextMaxServiceTime(){
        return this.textMaxServiceTime;
    }

    public void prepareButtonPanel(){
        this.buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c0 = setConstraints(0,2,1,1,1.0,1.0);
        GridBagConstraints c1 = setConstraints(0,0,1,1,1.0,1.0);
        GridBagConstraints c2 = setConstraints(1,0,1,1,1.0,1.0);
        GridBagConstraints c3 = setConstraints(2,0,1,1,1.0,1.0);
        this.startButton = new JButton("Start");
        this.buttonPanel.add(this.startButton, c2);
        this.clearButton = new JButton("Clear");
        this.clearButton.addActionListener(e -> clearFields());
        this.buttonPanel.add(this.clearButton, c1);
        this.stopButton = new JButton("Stop");
        this.buttonPanel.add(this.stopButton, c3);
        this.contentPane.add(this.buttonPanel, c0);
    }

    public void clearFields(){
        this.textTimeLimit.setText("");
        this.textNbOfQueues.setText("");
        this.textNbOfClients.setText("");
        this.textMinServiceTime.setText("");
        this.textMaxArrivalTime.setText("");
        this.textMinArrivalTime.setText("");
        this.textMaxServiceTime.setText("");
        this.testResult.setText("");
    }

    public JButton getStartButton(){
        return this.startButton;
    }

    public JButton getStopButton(){
        return this.stopButton;
    }

    public void prepareResultPanel(){
        this.resultPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c0 = setConstraints(0,3,1,2,5.0,5.0);
        GridBagConstraints c1 = setConstraints(0,0,1,1,1.0,1.0);
        this.testResult = new JTextArea();
        this.testResultScroll = new JScrollPane(this.testResult);
        this.resultPanel.add(this.testResultScroll, c1);
        this.contentPane.add(this.resultPanel, c0);
    }

    public void addOutput(String s){
        testResult.append(s);
    }
}
