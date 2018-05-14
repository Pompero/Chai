import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.fazecast.jSerialComm.SerialPort;

import java.awt.event.*;
import java.io.PrintWriter;
import java.util.Scanner;
import java.awt.*;

public class GraphTest {
	
static SerialPort chosenPort;
	
//Ints for Sliders and such
static int throttle1int = 0;
static int throttle2int = 0;
static int throttle3int = 0;
static int RPM1int = 0;
static int RPM2int = 0;
static int RPM3int = 0;
static int current1int = 0;
static int current2int = 0;
static int current3int = 0;
static int voltageint = 0;

//Ints for the sliders
static int alittle = -50;
static int amiddle = 0;
static int alot = 50;
static int fifty = 50;

//Floats for printing.
static float motor1int = (float)0.5;
static float motor2int = (float)0.5;
static float bensinint = (float)0.5;
static float randomfloat = 1;

public static void main(String[] args) {
	
// Creating and Configuring window.
JFrame window = new JFrame();
window.setTitle("Elhybriddrivlina");
window.setSize(1600, 1080);
window.setLayout(new BorderLayout());
window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//Creating components
JComboBox<String> portList = new JComboBox<String>();
JButton connectButton = new JButton("Connect");
JButton stopButton = new JButton("Stop");
JButton clearButton = new JButton("Clear");
JPanel centerPanel = new JPanel();

//Top panel
JPanel topPanel = new JPanel();
JPanel topPanelw = new JPanel();
JPanel bigtopPanel = new JPanel();
JPanel topPanele = new JPanel();

//Sliders for motorkontroller
JSlider bensin = new JSlider(JSlider.HORIZONTAL, alittle, alot, amiddle);
JSlider motor1 = new JSlider(JSlider.HORIZONTAL, alittle, alot, amiddle);
JSlider motor2 = new JSlider(JSlider.HORIZONTAL, alittle, alot, amiddle);

//motor1
motor1.setMajorTickSpacing(50);
motor1.setMinorTickSpacing(10);
motor1.setPaintTicks(true);
motor1.setPaintLabels(true);

//motor2
motor2.setMajorTickSpacing(50);
motor2.setMinorTickSpacing(10);
motor2.setPaintTicks(true);
motor2.setPaintLabels(true);

//bensin
bensin.setMajorTickSpacing(25);
bensin.setMinorTickSpacing(5);
bensin.setPaintTicks(true);
bensin.setPaintLabels(true);

//Get Value from sliders (temporary)
String motor1value = Integer.toString(motor1.getValue());
String motor2value = Integer.toString(motor2.getValue());
String bensinvalue = Integer.toString(bensin.getValue());

motor1.addChangeListener(new ChangeListener() {

@Override
public void stateChanged(ChangeEvent arg0) {
JSlider motor1 = (JSlider)arg0.getSource();
if(!motor1.getValueIsAdjusting()) {
motor1int = (float)0.5 - (((float)motor1.getValue())/100);			
}
}
});

motor2.addChangeListener(new ChangeListener() {
@Override
public void stateChanged(ChangeEvent arg1) {
JSlider motor2 = (JSlider)arg1.getSource();
if(!motor2.getValueIsAdjusting()) {
motor2int = (float)0.5 - (((float)motor2.getValue())/100);	
}
}
});

bensin.addChangeListener(new ChangeListener() {
@Override
public void stateChanged(ChangeEvent arg2) {
JSlider bensin = (JSlider)arg2.getSource();
if(!bensin.getValueIsAdjusting()) {
bensinint = (float)0.5 - (((float)bensin.getValue())/100);		
}
}
});

topPanele.setLayout(new BoxLayout(topPanele, BoxLayout.Y_AXIS));
topPanele.add(motor1);
topPanele.add(motor2);
topPanele.add(bensin);
topPanele.setBorder(BorderFactory.createTitledBorder("1. MG 1, 2. MG 2, 3. ICE"));
topPanele.setPreferredSize(new Dimension(600, 150));
bigtopPanel.add(topPanele);

//Text panel in the center (debug)
JLabel console = new JLabel();

//Side panels
JPanel westPanel = new JPanel();
JPanel eastPanel = new JPanel();
JPanel textPaneltop = new JPanel();

// Adding to the top panel.
topPanel.add(portList);
topPanel.add(connectButton);
topPanel.add(stopButton);
topPanel.add(clearButton);
//topPanel.add(console);
console.setText("<html>Throttle: ------------ <br>RPM: ------------ <br>Current: ------------<br>Voltage: ------------ </html>");
topPanel.setVisible(true);
topPanel.setBorder(BorderFactory.createTitledBorder("Arduino options"));
textPaneltop.add(console);
textPaneltop.setBorder(BorderFactory.createTitledBorder("Arduino input"));

// TopPanel west
String[] presets = {"Default", "Preset 2", "Preset 3"};
JComboBox presetBox = new JComboBox(presets);
topPanelw.add(presetBox);
topPanelw.setBorder(BorderFactory.createTitledBorder("Settings"));

// Adding to big top panel
bigtopPanel.add(topPanelw);
bigtopPanel.add(topPanel);
bigtopPanel.add(textPaneltop);

// Add the top JPanel with the buttons and the drop-down box.
window.add(bigtopPanel, BorderLayout.NORTH);
window.add(centerPanel, BorderLayout.CENTER);

// Populate Drop-down box
SerialPort[] portNames = SerialPort.getCommPorts();
for(int i = 0; i < portNames.length; i++)
portList.addItem(portNames[i].getSystemPortName());

// Create line graph - Current
XYSeries current1 = new XYSeries("MG 1");
XYSeries current2 = new XYSeries("MG 2");
XYSeries current3 = new XYSeries("ICE");
XYSeriesCollection dataset = new XYSeriesCollection();
dataset.addSeries(current1);
dataset.addSeries(current2);
dataset.addSeries(current3);
JFreeChart chart1 = ChartFactory.createXYLineChart("Effect", "Time", "Current", dataset);
XYPlot currentPlot = (XYPlot) chart1.getPlot();

// Create line graph - RPM
XYSeries RPM1 = new XYSeries("MG 2");
XYSeries RPM2 = new XYSeries("ICE");
XYSeries RPM3 = new XYSeries("MG 1");
XYSeriesCollection dataset2 = new XYSeriesCollection();
dataset2.addSeries(RPM3);
dataset2.addSeries(RPM1);
dataset2.addSeries(RPM2);
JFreeChart chart2 = ChartFactory.createXYLineChart("RPM", "Time", "RPM", dataset2);

// Create line graph 3 - Voltage and throttle
XYSeries voltage1 = new XYSeries("Voltage3");
XYSeries throttleseries = new XYSeries("MG 1 Throttle");
XYSeries throttle2series = new XYSeries("ICE Throttle");
XYSeries throttle3series = new XYSeries("MG 2Throttle");
XYSeriesCollection dataset3 = new XYSeriesCollection();
dataset3.addSeries(throttleseries);
dataset3.addSeries(throttle3series);
dataset3.addSeries(throttle2series);
dataset3.addSeries(voltage1);
JFreeChart chart3 = ChartFactory.createXYLineChart("Gas", "Time", "Throttle", dataset3);

// Add the chart in the middle.
centerPanel.add(new ChartPanel(chart1));
centerPanel.add(new ChartPanel(chart2));
centerPanel.add(new ChartPanel(chart3));
centerPanel.setBorder(BorderFactory.createTitledBorder("Measurements"));

//Configure the connect button and use another thread to listen to data
connectButton.addActionListener(new ActionListener() {

@Override
public void actionPerformed(ActionEvent arg0) {
if(connectButton.getText().equals("Connect")) {
chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
chosenPort.setBaudRate(115200);
if(chosenPort.openPort()) {
connectButton.setText("Disconnect");
portList.setEnabled(false);
}

//new thread that listen for incoming text and populates the graph
Thread thread = new Thread() {
@Override public void run() {
//Scanner
Scanner scanner = new Scanner(chosenPort.getInputStream());
while(scanner.hasNextLine()) {
try {
	scanner.useDelimiter("\\s[a-z]\\D");
//System.out.println(scanner.nextLine());
//Reading throttle
System.out.println(scanner.nextLine());
String throttle = scanner.nextLine();
//System.out.println(throttle);
float throttlenumber = Float.parseFloat(throttle);
throttleseries.add(throttle1int++, throttlenumber);
String throttle2 = scanner.nextLine();
//System.out.println(throttle);
float throttle2number = Float.parseFloat(throttle2);
throttle2series.add(throttle2int++, throttle2number);
String throttle3 = scanner.nextLine();
//System.out.println(throttle);
float throttle3number = Float.parseFloat(throttle3);
throttle3series.add(throttle3int++, throttle3number);

//Reading RPM
System.out.println(scanner.nextLine());
String RPM1line = scanner.nextLine();
float RPM1number = Float.parseFloat(RPM1line);
//System.out.println(RPM1line);
RPM1.add(RPM1int++, RPM1number);
String RPM2line = scanner.nextLine();
//System.out.println(RPM2line);
float RPM2number = Float.parseFloat(RPM2line);
RPM2.add(RPM2int++, RPM2number);
String RPM3line = scanner.nextLine();
float RPM3number = Float.parseFloat(RPM3line);
//System.out.println(RPM3line);
RPM3.add(RPM3int++, RPM3number);

//Reading Current
System.out.println(scanner.nextLine());
String current1line = scanner.nextLine();
float current1number = Float.parseFloat(current1line);
current1.add(current1int++, current1number);
String current2line = scanner.nextLine();
float current2number = Float.parseFloat(current2line);
current2.add(current2int++, current2number);
String current3line = scanner.nextLine();
float current3number = Float.parseFloat(current3line);
current3.add(current3int++, current3number);

//Reading voltage
System.out.println(scanner.nextLine());
String voltageline = scanner.nextLine();
float voltagenumber = Float.parseFloat(voltageline);
console.setText("<html>Throttle: " + throttle + " " + throttle2 + " " + throttle3 +
"<br>RPM: " + RPM1line + " " + RPM2line + " " + RPM3line +
"<br>Current: " + current1line + " " + current2line + " " + current3line + 
"<br>Voltage: " + voltageline +"</html>");
window.repaint();
} catch(Exception e) {}
} 
scanner.close();
// Scanner close //
try {Thread.sleep(2000);} catch(Exception e){}

PrintWriter output = new PrintWriter(chosenPort.getOutputStream());

while(true) {
output.print(motor1int + "," + bensinint + "," + motor2int);
output.flush();
try {Thread.sleep(2000);} catch(Exception e){}
}
}
};
thread.start();
}
else {
chosenPort.closePort();
portList.setEnabled(true);
connectButton.setText("Connect");				
}
}
});

stopButton.addActionListener(new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {	
}
});

clearButton.addActionListener(new ActionListener(){
@Override
public void actionPerformed(ActionEvent arg0) {
throttleseries.clear();
throttle2series.clear();
throttle3series.clear();
RPM1.clear();
RPM2.clear();
RPM3.clear();
current1.clear();
current2.clear();
current3.clear();
voltage1.clear();
throttle1int = 0;
throttle2int = 0;
throttle3int = 0;
RPM1int = 0;
RPM2int = 0;
RPM3int = 0;
current1int = 0;
current2int = 0;
current3int = 0;
voltageint = 0;
}
});
//Apparently this needs to be far down.
window.setVisible(true);
	}
}
