/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CSSDFarm;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Webby
 */

public class UserInterface extends javax.swing.JFrame {

    //static final Server server = Server.getInstance();
    static Server server = Server.getInstance();
    FieldStation selectedStation;
    Vector<FieldStation> userFieldStations;
    EventTableModel sensorsTable;
    EventTableModel sensorsReportTable;
    EventTableModel sensorsReportSensorDataTable;
    Browser browser = new Browser();
    boolean loadData = false;

    /**
     *
     */
    public UserInterface() {
        initComponents();
        jFrameServer.setVisible(true);
        tblSensorData.getTableHeader().setReorderingAllowed(false);
        loadData();
    }

    /**
     *
     */
    public void serializeData() {
        ObjectOutputStream outstream;
        try {
            outstream = new ObjectOutputStream(new FileOutputStream("data/server.ser"));
            outstream.writeObject(server);
            outstream.close();
        } catch (IOException io) {
            //System.out.println(io);
        }
    }

    /**
     *
     */
    public void loadData() {
        ObjectInputStream instream = null;
        Server loadedServer = null;
        try {
            FileInputStream fileinput = new FileInputStream("data/server.ser");
            instream = new ObjectInputStream(fileinput);
            do {
                try {
                    loadedServer = (Server) instream.readObject();
                    server = Server.getInstance(loadedServer);
                    sliderServerOnOff.setValue((server.getTurnedOn()) ? 1 : 0);

                } catch (ClassNotFoundException ex) {
                    //System.out.println(ex);
                }
            } while (true);
        } catch (IOException io) {
            //System.out.println(io);
            if (server == null) {
                server = Server.getInstance(null);
            }
        }

        if (instream != null) {
            try {
                instream.close();
            } catch (IOException ex) {
                //System.out.println(ex); 
            }
        }
    }

    /**
     *
     */
    public void displayManagerScreen() {
        //remove old panel details
        if (panelReport.isVisible()) {
            comboReportFieldStations.setModel(new DefaultComboBoxModel());
        }

        panelReport.setVisible(false);
        panelManager.setVisible(true);
        userFieldStations = server.loadData();

        listUserStations.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof FieldStation) {
                    // Here value will be of the Type 'FieldStation'
                    ((JLabel) renderer).setText(((FieldStation) value).getName());
                }
                return renderer;
            }
        });

        if (server.getUsersRole() == 1) {
            btnAddSensor.setVisible(false);
            btnRemoveSensor.setVisible(false);
            btnAddFieldStation.setVisible(false);
            btnRemoveFieldStation.setVisible(false);
        }

        panelManager.setVisible(true);

        int pos = loadUserData("data/userSettings.ser");
        try {
            listUserStations.setSelectedIndex(pos);
        } catch (Exception eX) {

        }

    }

    /**
     * Switch panel to add sensor panel 
     */
    public void displayAddSensorPanel() {
        comboIntervalMinutes.setSelectedIndex(5);
        panelManager.setVisible(false);
        panelAddSensor.setVisible(true);
        lblFieldStationName2.setText(selectedStation.getName());
    }

    /**
     * 
     */
    public void displayReportScreen() {
        loadData = false;
        panelManager.setVisible(false);
        panelReport.setVisible(true);
        dpReportCalendar.getEditor().setEditable(false);
        comboReportSensorType.setLightWeightPopupEnabled(false);
        dpReportCalendar.setLightWeightPopupEnabled(false);
        userFieldStations = server.loadData();
        comboReportFieldStations.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof FieldStation) {
                    FieldStation station = (FieldStation) value;
                    setText(station.getName());
                }
                return this;
            }
        });
        for (FieldStation station : userFieldStations) {
            comboReportFieldStations.addItem(station);
        }
        Date date = new Date();
        dpReportCalendar.setDate(date);
        int pos = loadUserData("data/userData.ser");
        try {
            comboReportFieldStations.setSelectedIndex(pos);
        } catch (Exception eX) {

        }

    }

    /**
     *
     */
    public void displayReportSensorDataScreen() {
        if (panelReport.isVisible()) {
            panelReport.setVisible(false);
        }
        panelReportSensorData.setVisible(true);

        if (dpReportSensorDataDate.getDate() == null) {
            Date date = dpReportCalendar.getDate();
            dpReportSensorDataDate.setDate(date);
        }

        int index = tblSensorData.getSelectedRow();
        SensorData selectedSensorData = (SensorData) sensorsReportTable.getElementAt(index);

        FieldStation fieldStation = (FieldStation) comboReportFieldStations.getSelectedItem();
        Sensor sensor = fieldStation.getSetOfSensors().getSensor(selectedSensorData.getId());

        lblReportSensorDataFieldStationName.setText(fieldStation.getName());
        lblReportSensorDataSensorName.setText(sensor.getId());
        lblReportSensorDataSensorType.setText(sensor.getType());
        lblReportSensorDataSensorUnits.setText(sensor.getUnits());
        lblReportSensorDataNextIntervalDate.setText(sensor.getNextIntervalTime());

        updateReportSensorData();
    }

    /**
     * Verify field station and add it to the server. Load updated list of 
     * field stations and refresh the jList to show new field station
     * @param id
     * @param name
     */
    public void addFieldStation(String id, String name) {
        if (server.verifyFieldStation(id)) {
            server.addFieldStation(id, name);
            Vector<FieldStation> userFieldStations = server.loadData();
            listUserStations.setListData(userFieldStations); 
        }
    }

    /**
     * Remove field station with id from the server. Update jList to reflect the change.
     * If jList is empty set selectedStation to null, else select the first field station.
     * @param id
     */
    public void removeFieldStation(String id) {
        server.removeFieldStation(id);
        Vector<FieldStation> userFieldStations = server.loadData();
        listUserStations.setListData(userFieldStations);

        if (listUserStations.getModel().getSize() > 0) {
            listUserStations.setSelectedIndex(0);
        } else {
            selectedStation = null;
            lblFieldStationName.setText(" ");
        }
    }

    /**
     * Remove sensor with id from the currently selected field station
     * @param id
     */
    public void removeSensor(String id) {
        FieldStation station = server.getFieldStation(selectedStation.getId());
        station.removeSensor(id);
        server.removeSensor(station.getId(), id);
        displayManagerScreen();
        changeSelectedFieldStation(selectedStation);
    }

    /**
     *
     */
    public void updateReport() {
        FieldStation fieldStation = (FieldStation) comboReportFieldStations.getSelectedItem();
        if (fieldStation != null) {
            String sensorType = (String) comboReportSensorType.getSelectedItem();
            DateFormat inputformatter = new SimpleDateFormat("dd/MM/yyyy");
            String newDate = inputformatter.format(dpReportCalendar.getDate());
            Date date = new Date();

            try {
                date = inputformatter.parse(newDate);
            } catch (ParseException ex) {

            }

            //Vector<Sensor> sensors = fieldStation.getSetOfSensors().getByType(sensorType);
            EventList<SensorData> eventList = new BasicEventList<SensorData>();
            Report report = server.compileReport(fieldStation.getId());
            Vector<SensorData> sensorData = report.getDataByTypeAndDate(sensorType, date);

            eventList.clear();
            for (SensorData sensor : sensorData) {
                //sensor.collectData();
                eventList.add(sensor);

            }

            sensorsReportTable = new EventTableModel(eventList, new SensorDataTableFormat());

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    int indexFs = comboReportFieldStations.getSelectedIndex();
                    SetOfSensors setOfSensors = server.getUserFieldStation().get(indexFs).getSetOfSensors();
                    tblSensorData.setModel(sensorsReportTable);

                    CustomTableRendererColour cellRenderer = null;
                    try {
                        cellRenderer = new CustomTableRendererColour(indexFs, setOfSensors);
                    } catch (Exception eX) {
                        //System.out.println(eX);
                    }

                    int lastRowIndex = tblSensorData.getModel().getRowCount() - 1;
                    if (lastRowIndex >= 0) {
                        tblSensorData.setRowSelectionInterval(lastRowIndex, lastRowIndex);
                    }
                    TableColumn column = null;
                    for (int i = 0; i < tblSensorData.getColumnModel().getColumnCount(); i++) {
                        column = tblSensorData.getColumnModel().getColumn(i);
                        if (i == 4) {
                            column.setPreferredWidth(250);
                        } else if (i == 3) {
                            column.setPreferredWidth(200);
                        } else {
                            column.setPreferredWidth(50);
                        }
                        if (i == 2) {
                            tblSensorData.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
                        }
                    }
                    //pass setOfSensors until a better way to get threshold value
                    if (panelHeatmap.isVisible()) {
                        updateHeatmap(sensorData, setOfSensors);
                    }
                }
            });

        }
    }

    //Custom DefaultTableCellRenderer

    /**
     *
     */
        public static class CustomTableRendererColour extends DefaultTableCellRenderer {

        //used as class is static so need a way to get the correct FieldStation

        int id;
        SetOfSensors setOfSensors;

        private CustomTableRendererColour(int indexFs, SetOfSensors setOfSensors) {
            id = indexFs;
            this.setOfSensors = setOfSensors;
        }

        private CustomTableRendererColour() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        // You should override getTableCellRendererComponent

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);

            try {
                String sensorID = table.getModel().getValueAt(row, 0).toString();
                Sensor sensor = setOfSensors.getSensor(sensorID);
                float thresh = sensor.getThreshold();
                boolean threshIsUpperLimit = sensor.getThresholdIsUpperLimit();
                float val = Float.parseFloat(value.toString());

                if (threshIsUpperLimit) {
                    if (val >= thresh) {
                        c.setForeground(new Color(0xC91F37));
                    } else {
                        c.setForeground(new Color(0x10ce00));
                    }
                } else if (!threshIsUpperLimit) {
                    if (val <= thresh) {
                        c.setForeground(new Color(0xC91F37));
                    } else {
                        c.setForeground(new Color(0x10ce00));
                    }
                }
            } catch (Exception eX) {
                //System.out.println(eX);
            }
            return c;
        }
    }

    /**
     *
     * @return
     */
    public String getWebName() {
        return comboReportFieldStations.getSelectedItem().toString();
    }

    /**
     *
     */
    public void updateReportSensorData() {
        int index = tblSensorData.getSelectedRow();
        SensorData selectedSensorData = (SensorData) sensorsReportTable.getElementAt(index);

        FieldStation fieldStation = (FieldStation) comboReportFieldStations.getSelectedItem();
        Sensor sensor = fieldStation.getSetOfSensors().getSensor(selectedSensorData.getId());

        DateFormat inputformatter = new SimpleDateFormat("dd/MM/yyyy");
        String newDate = inputformatter.format(dpReportSensorDataDate.getDate());
        Date date = new Date();

        try {
            date = inputformatter.parse(newDate);
        } catch (ParseException ex) {

        }

        Report report = server.compileReport(fieldStation.getId());
        Vector<SensorData> sensorData = report.getDataForSensorOnDate(selectedSensorData.getId(), date);

        EventList<SensorData> eventList = new BasicEventList<SensorData>();

        eventList.clear();
        for (SensorData sensorItem : sensorData) {
            //sensor.collectData();
            eventList.add(sensorItem);
        }

        sensorsReportSensorDataTable = new EventTableModel(eventList, new SensorDataTableFormat());

        tblReportSensorData.setModel(sensorsReportSensorDataTable);

        int lastRowIndex = tblReportSensorData.getModel().getRowCount() - 1;
        if (lastRowIndex >= 0) {
            tblReportSensorData.setRowSelectionInterval(lastRowIndex, lastRowIndex);
        }
    }

    /**
     *
     */
    public void changeReportView() {

    }

    /**
     * Clear all values from sensor screen
     */
    public void clearSensorScreen() {
        txtSensorId.setText("");
        comboIntervalSeconds.setSelectedIndex(0);
        comboIntervalMinutes.setSelectedIndex(0);
        comboIntervalHours.setSelectedIndex(0);
        comboIntervalDays.setSelectedIndex(0);
        comboSensorType.setSelectedIndex(0);
        comboSensorUnits.setSelectedIndex(0);
        txtThreshold.setText("");
    }

    /**
     * Configure JxBrowser to be displayed. Load map.html from file to string data, load
     * this data into JxBrowser and add the browser to the panel.
     */
    public void displayHeatmap() {
        LoggerProvider.getChromiumProcessLogger().setLevel(Level.OFF);
        LoggerProvider.getIPCLogger().setLevel(Level.OFF);
        LoggerProvider.getBrowserLogger().setLevel(Level.OFF);
        BrowserView view = new BrowserView(browser);
        JPanel toolBar = new JPanel();

        Dimension dimension = panelHeatmap.getSize();

        JInternalFrame internalFrame = new JInternalFrame();
        internalFrame.add(view, BorderLayout.CENTER);
        internalFrame.add(toolBar, BorderLayout.NORTH);
        internalFrame.setSize(dimension);
        internalFrame.setLocation(0, 0);
        internalFrame.setBorder(null);
        internalFrame.setVisible(true);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) internalFrame.getUI()).setNorthPane(null);

        URL url = getClass().getResource("map.html");
        File html = new File(url.getPath());

        String htmlString = null;
        try {
            htmlString = FileUtils.readFileToString(html);
        } catch (IOException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        browser.loadHTML(htmlString);

        panelHeatmap.add(internalFrame);
    }

    /**
     * Populate ArrayLists with sensor data by looping through all the sensorData vector.
     * Use JxBrowser's 'executeJavascript' functionality to call a javascript function, 'addMyData', that consumes 
     * the data inside the ArrayLists and loads it into the map inside the browser.
     * @param sensorData
     * @param setOfSensors
     */
    public void updateHeatmap(Vector<SensorData> sensorData, SetOfSensors setOfSensors) {
        ArrayList stringData = new ArrayList();
        ArrayList stringReadingData = new ArrayList();
        ArrayList stringSensorName = new ArrayList();
        ArrayList stringSensorUnit = new ArrayList();
        ArrayList stringSensorDate = new ArrayList();
        ArrayList stringSensorWeight = new ArrayList();

        for (SensorData sensor : sensorData) {
            stringData.add(sensor.getLocation().toString());
            stringReadingData.add(sensor.getValue());
            stringSensorName.add(stringToIntConverter(sensor.getId()));
            int unitType = 0;
            switch (sensor.getUnit()) {
                case "LUX":
                    unitType = 1;
                    break;
                case "PH":
                    unitType = 2;
                    break;
                case "C":
                    unitType = 3;
                    break;
                case "F":
                    unitType = 4;
                    break;
                case "%":
                    unitType = 5;
                    break;
            }
            stringSensorUnit.add(unitType);

            Sensor sensor1 = setOfSensors.getSensor(sensor.getId());
            float thresh = sensor1.getThreshold();
            float val = sensor.getValue();
            int weight = Math.round((val * 100) / thresh);
            stringSensorWeight.add(weight);
            stringSensorDate.add(sensor.getDate().toString());
        }

        if (sensorData != null) {
            browser.executeJavaScript("addMyData("
                    + stringData + ", " + stringReadingData + ", " + stringSensorUnit + ", " + stringSensorWeight
                    + ")");
        }
    }

    private int stringToIntConverter(String string) {
        int total = string.length();
        int x = 0;
        for (int i = 0; i < total; i++) {
            if (i == 0) {
                x = Character.getNumericValue(string.charAt(i));
            } else {
                x += Character.getNumericValue(string.charAt(i));
            }
        }
        return x;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrameServer = new javax.swing.JFrame();
        sliderServerOnOff = new javax.swing.JSlider();
        txtServerOff = new javax.swing.JLabel();
        txtServerOn = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panelLogIn = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        buttonRegister = new javax.swing.JButton();
        panelManager = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listUserStations = new javax.swing.JList();
        btnAddFieldStation = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblFieldStationName = new javax.swing.JLabel();
        lblSensorList = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSensorTable = new javax.swing.JTable();
        btnReport = new javax.swing.JButton();
        lblFieldStationManager = new javax.swing.JLabel();
        btnRemoveFieldStation = new javax.swing.JButton();
        btnFieldStationDetails = new javax.swing.JButton();
        btnAddSensor = new javax.swing.JButton();
        btnRemoveSensor = new javax.swing.JButton();
        btnSensorDetails = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        panelAddSensor = new javax.swing.JPanel();
        lblAddNewSensor = new javax.swing.JLabel();
        lblFieldStationName2 = new javax.swing.JLabel();
        lblSensorType = new javax.swing.JLabel();
        lblSensorId = new javax.swing.JLabel();
        lblSensorInterval = new javax.swing.JLabel();
        lblSensorUnits = new javax.swing.JLabel();
        txtSensorId = new javax.swing.JTextField();
        comboSensorType = new javax.swing.JComboBox();
        comboSensorUnits = new javax.swing.JComboBox();
        lblIntervalMinutes = new javax.swing.JLabel();
        lblIntervalDays = new javax.swing.JLabel();
        lblIntervalHours = new javax.swing.JLabel();
        lblIntervalSeconds = new javax.swing.JLabel();
        comboIntervalDays = new javax.swing.JComboBox();
        comboIntervalHours = new javax.swing.JComboBox();
        comboIntervalMinutes = new javax.swing.JComboBox();
        comboIntervalSeconds = new javax.swing.JComboBox();
        btnSaveSensor = new javax.swing.JButton();
        btnCancelSensor = new javax.swing.JButton();
        btnClearSensor = new javax.swing.JButton();
        txtThreshold = new javax.swing.JTextField();
        lblThreshold = new javax.swing.JLabel();
        checkIsUpperLimit = new javax.swing.JCheckBox();
        panelReport = new javax.swing.JPanel();
        lblReportTitle = new javax.swing.JLabel();
        btnManager = new javax.swing.JButton();
        comboReportFieldStations = new javax.swing.JComboBox();
        lblReportTitle1 = new javax.swing.JLabel();
        sliderView = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblReportTitle2 = new javax.swing.JLabel();
        comboReportSensorType = new javax.swing.JComboBox();
        lblReportTitle3 = new javax.swing.JLabel();
        dpReportCalendar = new org.jdesktop.swingx.JXDatePicker();
        panelSwitcher = new javax.swing.JPanel();
        panelReportTable = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSensorData = new javax.swing.JTable();
        panelHeatmap = new javax.swing.JPanel();
        panelReportSensorData = new javax.swing.JPanel();
        btnBackReportSensorData = new javax.swing.JButton();
        lblReportTitle4 = new javax.swing.JLabel();
        lblReportSensorDataFieldStationName = new javax.swing.JLabel();
        lblReportSensorDataSensorName = new javax.swing.JLabel();
        lblReportSensorDataSensorType = new javax.swing.JLabel();
        lblReportSensorDataSensorUnits = new javax.swing.JLabel();
        lblReportSensorDataDate = new javax.swing.JLabel();
        dpReportSensorDataDate = new org.jdesktop.swingx.JXDatePicker();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblReportSensorData = new javax.swing.JTable();
        lblReportSensorDataNextIntervalDate = new javax.swing.JLabel();
        lblReportSensorDataNextInterval = new javax.swing.JLabel();
        panelRegister = new javax.swing.JPanel();
        btnRegister = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblUsername = new javax.swing.JLabel();
        txtUsernameReg = new javax.swing.JTextField();
        lblUsername1 = new javax.swing.JLabel();
        comboUserRole = new javax.swing.JComboBox();
        lblUserRole = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        txtPasswordReg = new javax.swing.JPasswordField();

        jFrameServer.setMinimumSize(new java.awt.Dimension(360, 200));

        sliderServerOnOff.setMaximum(1);
        sliderServerOnOff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sliderServerOnOffMouseReleased(evt);
            }
        });
        sliderServerOnOff.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sliderServerOnOffKeyReleased(evt);
            }
        });

        txtServerOff.setText("OFF");

        txtServerOn.setText("ON");

        jLabel3.setText("Server");

        javax.swing.GroupLayout jFrameServerLayout = new javax.swing.GroupLayout(jFrameServer.getContentPane());
        jFrameServer.getContentPane().setLayout(jFrameServerLayout);
        jFrameServerLayout.setHorizontalGroup(
            jFrameServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFrameServerLayout.createSequentialGroup()
                .addGroup(jFrameServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jFrameServerLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(txtServerOff)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sliderServerOnOff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtServerOn))
                    .addGroup(jFrameServerLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(jLabel3)))
                .addGap(0, 42, Short.MAX_VALUE))
        );
        jFrameServerLayout.setVerticalGroup(
            jFrameServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFrameServerLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addGroup(jFrameServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtServerOff, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jFrameServerLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jFrameServerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtServerOn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sliderServerOnOff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(62, 62, 62))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.CardLayout());

        jButton1.setText("Log in");
        jButton1.setName("btnLogIn"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });

        txtUsername.setText("John");
        txtUsername.setName("txtUsername"); // NOI18N

        txtPassword.setText("Password");

        buttonRegister.setText("Register");
        buttonRegister.setName("btnLogIn"); // NOI18N
        buttonRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRegisterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLogInLayout = new javax.swing.GroupLayout(panelLogIn);
        panelLogIn.setLayout(panelLogInLayout);
        panelLogInLayout.setHorizontalGroup(
            panelLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogInLayout.createSequentialGroup()
                .addGap(0, 196, Short.MAX_VALUE)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(247, 247, 247))
            .addGroup(panelLogInLayout.createSequentialGroup()
                .addGap(281, 281, 281)
                .addGroup(panelLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buttonRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelLogInLayout.setVerticalGroup(
            panelLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogInLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addGroup(panelLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(89, 89, 89)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(112, Short.MAX_VALUE))
        );

        getContentPane().add(panelLogIn, "card5");

        listUserStations.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        listUserStations.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listUserStationsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listUserStations);

        btnAddFieldStation.setText("Add Field Station");
        btnAddFieldStation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFieldStationActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblFieldStationName.setFont(new java.awt.Font("Calibri Light", 0, 14)); // NOI18N
        lblFieldStationName.setText("Field Station Manager");

        lblSensorList.setText("Sensor List");

        tblSensorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Type", "Unit", "GPS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblSensorTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblFieldStationName, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(217, 217, 217))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(lblSensorList)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFieldStationName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSensorList)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnReport.setText("Report");
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });

        lblFieldStationManager.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblFieldStationManager.setText("Field Station Manager");

        btnRemoveFieldStation.setText("Remove Field Station");
        btnRemoveFieldStation.setEnabled(false);
        btnRemoveFieldStation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveFieldStationActionPerformed(evt);
            }
        });

        btnFieldStationDetails.setText("Field Station Details");
        btnFieldStationDetails.setEnabled(false);
        btnFieldStationDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFieldStationDetailsActionPerformed(evt);
            }
        });

        btnAddSensor.setText("Add Sensor");
        btnAddSensor.setEnabled(false);
        btnAddSensor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSensorActionPerformed(evt);
            }
        });

        btnRemoveSensor.setText("Remove Sensor");
        btnRemoveSensor.setEnabled(false);
        btnRemoveSensor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveSensorActionPerformed(evt);
            }
        });

        btnSensorDetails.setText("Sensor Details");
        btnSensorDetails.setEnabled(false);
        btnSensorDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSensorDetailsActionPerformed(evt);
            }
        });

        jPanel4.setMinimumSize(new java.awt.Dimension(80, 90));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelManagerLayout = new javax.swing.GroupLayout(panelManager);
        panelManager.setLayout(panelManagerLayout);
        panelManagerLayout.setHorizontalGroup(
            panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelManagerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAddFieldStation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFieldStationDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRemoveFieldStation, javax.swing.GroupLayout.PREFERRED_SIZE, 143, Short.MAX_VALUE)
                    .addComponent(btnReport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelManagerLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAddSensor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRemoveSensor, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                            .addComponent(btnSensorDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelManagerLayout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addComponent(lblFieldStationManager, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelManagerLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );
        panelManagerLayout.setVerticalGroup(
            panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelManagerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReport)
                    .addComponent(lblFieldStationManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelManagerLayout.createSequentialGroup()
                        .addGroup(panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddFieldStation)
                            .addComponent(btnAddSensor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRemoveFieldStation)
                            .addComponent(btnRemoveSensor))
                        .addGap(7, 7, 7)
                        .addGroup(panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFieldStationDetails)
                            .addComponent(btnSensorDetails)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getContentPane().add(panelManager, "card5");

        lblAddNewSensor.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblAddNewSensor.setText("Add New Sensor");

        lblFieldStationName2.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblFieldStationName2.setText("jLabel1");

        lblSensorType.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblSensorType.setText("Sensor Type:");

        lblSensorId.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblSensorId.setText("Sensor Id:");

        lblSensorInterval.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblSensorInterval.setText("Measurement Interval:");

        lblSensorUnits.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblSensorUnits.setText("Units:");

        txtSensorId.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        txtSensorId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSensorIdKeyReleased(evt);
            }
        });

        comboSensorType.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        comboSensorType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Soil Moisture", "Soil Temperature", "Air Temperature", "Soil Acidity", "Light Intensity" }));
        comboSensorType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboSensorTypeActionPerformed(evt);
            }
        });

        comboSensorUnits.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        comboSensorUnits.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "mm", "%" }));

        lblIntervalMinutes.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblIntervalMinutes.setText("Minutes:");

        lblIntervalDays.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblIntervalDays.setText("Days:");

        lblIntervalHours.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblIntervalHours.setText("Hours:");

        lblIntervalSeconds.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblIntervalSeconds.setText("Seconds:");

        comboIntervalDays.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        comboIntervalDays.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        comboIntervalDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboIntervalDaysActionPerformed(evt);
            }
        });

        comboIntervalHours.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        comboIntervalHours.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" }));
        comboIntervalHours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboIntervalHoursActionPerformed(evt);
            }
        });

        comboIntervalMinutes.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        comboIntervalMinutes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60" }));
        comboIntervalMinutes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboIntervalMinutesActionPerformed(evt);
            }
        });

        comboIntervalSeconds.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        comboIntervalSeconds.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60" }));
        comboIntervalSeconds.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboIntervalSecondsActionPerformed(evt);
            }
        });

        btnSaveSensor.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        btnSaveSensor.setText("Save");
        btnSaveSensor.setEnabled(false);
        btnSaveSensor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSensorActionPerformed(evt);
            }
        });

        btnCancelSensor.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        btnCancelSensor.setText("Cancel");
        btnCancelSensor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelSensorActionPerformed(evt);
            }
        });

        btnClearSensor.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        btnClearSensor.setText("Clear");
        btnClearSensor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSensorActionPerformed(evt);
            }
        });

        txtThreshold.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        txtThreshold.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtThresholdKeyReleased(evt);
            }
        });

        lblThreshold.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblThreshold.setText("Threshold:");

        checkIsUpperLimit.setSelected(true);
        checkIsUpperLimit.setText("Upper Limit?");

        javax.swing.GroupLayout panelAddSensorLayout = new javax.swing.GroupLayout(panelAddSensor);
        panelAddSensor.setLayout(panelAddSensorLayout);
        panelAddSensorLayout.setHorizontalGroup(
            panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAddSensorLayout.createSequentialGroup()
                .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelAddSensorLayout.createSequentialGroup()
                        .addGap(343, 343, 343)
                        .addComponent(lblAddNewSensor))
                    .addGroup(panelAddSensorLayout.createSequentialGroup()
                        .addGap(372, 372, 372)
                        .addComponent(lblFieldStationName2))
                    .addGroup(panelAddSensorLayout.createSequentialGroup()
                        .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelAddSensorLayout.createSequentialGroup()
                                .addGap(157, 157, 157)
                                .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblSensorId, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSensorType, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSensorUnits, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblSensorInterval, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelAddSensorLayout.createSequentialGroup()
                                .addGap(245, 245, 245)
                                .addComponent(lblThreshold, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAddSensorLayout.createSequentialGroup()
                                .addComponent(txtThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkIsUpperLimit))
                            .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtSensorId)
                                .addComponent(comboSensorType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(comboSensorUnits, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelAddSensorLayout.createSequentialGroup()
                                    .addGap(22, 22, 22)
                                    .addComponent(lblIntervalDays, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboIntervalDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(31, 31, 31)
                                    .addComponent(lblIntervalHours, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(comboIntervalHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAddSensorLayout.createSequentialGroup()
                                    .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnSaveSensor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(panelAddSensorLayout.createSequentialGroup()
                                            .addComponent(lblIntervalMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(comboIntervalMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(panelAddSensorLayout.createSequentialGroup()
                                            .addGap(14, 14, 14)
                                            .addComponent(lblIntervalSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(comboIntervalSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelAddSensorLayout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(btnCancelSensor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))))
                    .addGroup(panelAddSensorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnClearSensor)))
                .addGap(70, 212, Short.MAX_VALUE))
        );
        panelAddSensorLayout.setVerticalGroup(
            panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAddSensorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAddNewSensor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFieldStationName2)
                .addGap(50, 50, 50)
                .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelAddSensorLayout.createSequentialGroup()
                        .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSensorId)
                            .addComponent(txtSensorId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSensorType)
                            .addComponent(comboSensorType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboSensorUnits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSensorUnits))
                        .addGap(18, 18, 18)
                        .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(comboIntervalHours, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblIntervalHours))
                            .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(comboIntervalDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblIntervalDays)
                                .addComponent(lblSensorInterval)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(comboIntervalMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblIntervalMinutes))
                            .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(comboIntervalSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblIntervalSeconds)))
                        .addGap(18, 18, 18)
                        .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblThreshold)))
                    .addGroup(panelAddSensorLayout.createSequentialGroup()
                        .addComponent(checkIsUpperLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(panelAddSensorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveSensor)
                    .addComponent(btnCancelSensor))
                .addGap(4, 4, 4)
                .addComponent(btnClearSensor)
                .addContainerGap())
        );

        getContentPane().add(panelAddSensor, "card5");

        panelReport.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                panelReportCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        lblReportTitle.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportTitle.setText("Report");

        btnManager.setText("Manager");
        btnManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManagerActionPerformed(evt);
            }
        });

        comboReportFieldStations.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboReportFieldStationsItemStateChanged(evt);
            }
        });

        lblReportTitle1.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportTitle1.setText("View:");

        sliderView.setMaximum(1);
        sliderView.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderViewStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        jLabel1.setText("GPS");

        jLabel2.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        jLabel2.setText("Table");

        lblReportTitle2.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportTitle2.setText("Sensor Type:");

        comboReportSensorType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Soil Moisture", "Soil Temperature", "Air Temperature", "Soil Acidity", "Light Intensity" }));
        comboReportSensorType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboReportSensorTypeItemStateChanged(evt);
            }
        });

        lblReportTitle3.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportTitle3.setText("Date:");

        dpReportCalendar.setDate(new Date());
        dpReportCalendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dpReportCalendarActionPerformed(evt);
            }
        });

        panelSwitcher.setLayout(new java.awt.CardLayout());

        tblSensorData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSensorData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSensorDataMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblSensorData);

        javax.swing.GroupLayout panelReportTableLayout = new javax.swing.GroupLayout(panelReportTable);
        panelReportTable.setLayout(panelReportTableLayout);
        panelReportTableLayout.setHorizontalGroup(
            panelReportTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportTableLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 863, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelReportTableLayout.setVerticalGroup(
            panelReportTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportTableLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelSwitcher.add(panelReportTable, "card3");

        javax.swing.GroupLayout panelHeatmapLayout = new javax.swing.GroupLayout(panelHeatmap);
        panelHeatmap.setLayout(panelHeatmapLayout);
        panelHeatmapLayout.setHorizontalGroup(
            panelHeatmapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 863, Short.MAX_VALUE)
        );
        panelHeatmapLayout.setVerticalGroup(
            panelHeatmapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 302, Short.MAX_VALUE)
        );

        panelSwitcher.add(panelHeatmap, "card3");

        javax.swing.GroupLayout panelReportLayout = new javax.swing.GroupLayout(panelReport);
        panelReport.setLayout(panelReportLayout);
        panelReportLayout.setHorizontalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportLayout.createSequentialGroup()
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(panelReportLayout.createSequentialGroup()
                            .addGap(187, 187, 187)
                            .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblReportTitle2)
                                .addComponent(lblReportTitle1)
                                .addComponent(lblReportTitle3))
                            .addGap(37, 37, 37)
                            .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(panelReportLayout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(sliderView, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel2))
                                .addComponent(comboReportSensorType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(dpReportCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelReportLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(btnManager, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblReportTitle)
                            .addGap(18, 18, 18)
                            .addComponent(comboReportFieldStations, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelReportLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelSwitcher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        panelReportLayout.setVerticalGroup(
            panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReportTitle)
                    .addComponent(btnManager)
                    .addComponent(comboReportFieldStations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblReportTitle1)
                        .addComponent(jLabel1))
                    .addComponent(sliderView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblReportTitle2, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(comboReportSensorType, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReportTitle3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dpReportCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSwitcher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(panelReport, "card5");

        btnBackReportSensorData.setText("Back");
        btnBackReportSensorData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackReportSensorDataActionPerformed(evt);
            }
        });

        lblReportTitle4.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportTitle4.setText("Sensor Data");

        lblReportSensorDataFieldStationName.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportSensorDataFieldStationName.setText("Field Station Name");

        lblReportSensorDataSensorName.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportSensorDataSensorName.setText("Sensor Name");

        lblReportSensorDataSensorType.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportSensorDataSensorType.setText("(Sensor Type)");

        lblReportSensorDataSensorUnits.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportSensorDataSensorUnits.setText("(Units)");

        lblReportSensorDataDate.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportSensorDataDate.setText("Date:");

        dpReportSensorDataDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dpReportSensorDataDateActionPerformed(evt);
            }
        });

        tblReportSensorData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblReportSensorData);

        lblReportSensorDataNextIntervalDate.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportSensorDataNextIntervalDate.setText("29/02/2016 16:27");

        lblReportSensorDataNextInterval.setFont(new java.awt.Font("Calibri Light", 0, 18)); // NOI18N
        lblReportSensorDataNextInterval.setText("Next interval:");

        javax.swing.GroupLayout panelReportSensorDataLayout = new javax.swing.GroupLayout(panelReportSensorData);
        panelReportSensorData.setLayout(panelReportSensorDataLayout);
        panelReportSensorDataLayout.setHorizontalGroup(
            panelReportSensorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportSensorDataLayout.createSequentialGroup()
                .addGroup(panelReportSensorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelReportSensorDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBackReportSensorData)
                        .addGap(261, 261, 261)
                        .addComponent(lblReportTitle4))
                    .addGroup(panelReportSensorDataLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(171, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelReportSensorDataLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelReportSensorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelReportSensorDataLayout.createSequentialGroup()
                        .addComponent(lblReportSensorDataNextInterval)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblReportSensorDataNextIntervalDate))
                    .addComponent(lblReportSensorDataSensorUnits)
                    .addComponent(lblReportSensorDataSensorType)
                    .addComponent(lblReportSensorDataSensorName)
                    .addComponent(lblReportSensorDataFieldStationName))
                .addGap(111, 111, 111)
                .addComponent(lblReportSensorDataDate)
                .addGap(18, 18, 18)
                .addComponent(dpReportSensorDataDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(223, 223, 223))
        );
        panelReportSensorDataLayout.setVerticalGroup(
            panelReportSensorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelReportSensorDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelReportSensorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBackReportSensorData)
                    .addComponent(lblReportTitle4))
                .addGap(18, 18, 18)
                .addGroup(panelReportSensorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReportSensorDataFieldStationName)
                    .addComponent(lblReportSensorDataDate)
                    .addComponent(dpReportSensorDataDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblReportSensorDataSensorName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblReportSensorDataSensorType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblReportSensorDataSensorUnits)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelReportSensorDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReportSensorDataNextIntervalDate)
                    .addComponent(lblReportSensorDataNextInterval))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        getContentPane().add(panelReportSensorData, "card6");

        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        btnCancel.setText("< Back to Login");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        lblUsername.setText("Username:");

        lblUsername1.setText("Password:");

        comboUserRole.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Farmer", "Food Processing Manager" }));

        lblUserRole.setText("User Role:");

        javax.swing.GroupLayout panelRegisterLayout = new javax.swing.GroupLayout(panelRegister);
        panelRegister.setLayout(panelRegisterLayout);
        panelRegisterLayout.setHorizontalGroup(
            panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRegisterLayout.createSequentialGroup()
                .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panelRegisterLayout.createSequentialGroup()
                        .addGap(297, 297, 297)
                        .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblUsername)
                            .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblUserRole)
                                .addComponent(lblUsername1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUsernameReg)
                            .addComponent(comboUserRole, 0, 164, Short.MAX_VALUE)
                            .addComponent(txtPasswordReg)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelRegisterLayout.createSequentialGroup()
                        .addGap(298, 298, 298)
                        .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(349, 349, 349))
            .addGroup(panelRegisterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelRegisterLayout.setVerticalGroup(
            panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRegisterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCancel)
                .addGap(29, 29, 29)
                .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsername)
                    .addComponent(txtUsernameReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsername1)
                    .addComponent(txtPasswordReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboUserRole)
                    .addComponent(lblUserRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnRegister)
                .addGap(18, 18, 18)
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(216, Short.MAX_VALUE))
        );

        getContentPane().add(panelRegister, "card7");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        logIn();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void logIn() {
        String name = txtUsername.getText();
        String password = txtPassword.getText();
        boolean authenticateUser = server.authenticateUser(name, password);
        if (authenticateUser) {
            panelLogIn.setVisible(false);
            if (server.getUsersRole() == 1) {
                displayManagerScreen();
            } else {
                displayReportScreen();
            }
            listUserStations.setListData(userFieldStations);
        }
    }

    private void btnAddFieldStationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFieldStationActionPerformed
        JTextField id = new JTextField();
        JTextField name = new JTextField();
        //Space is needed to expand dialog 
        JLabel verified = new JLabel(" ");
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        okButton.setEnabled(false);
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String idText = id.getText();
                String nameText = name.getText();
                addFieldStation(id.getText(), name.getText());
                JOptionPane.getRootFrame().dispose();
                listUserStations.setSelectedValue(server.getFieldStation(idText), false);
            }
        });

        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.getRootFrame().dispose();
            }
        });

        id.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent key) {
                boolean theid = id.getText().equals("");
                boolean thename = name.getText().equals("");
                if (server.verifyFieldStation(id.getText()) && !theid && !thename) {
                    verified.setText("Verified");
                    verified.setForeground(new Color(0, 102, 0));
                    okButton.setEnabled(true);
                } else {
                    verified.setText("Not Verified");
                    verified.setForeground(Color.RED);
                    okButton.setEnabled(false);
                }
            }
        });
        name.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent key) {
                boolean theid = id.getText().equals("");
                boolean thename = name.getText().equals("");
                if (server.verifyFieldStation(id.getText()) && !theid && !thename) {
                    verified.setText("Verified");
                    verified.setForeground(new Color(0, 102, 0));
                    okButton.setEnabled(true);
                } else {
                    verified.setText("Not Verified");
                    verified.setForeground(Color.RED);
                    okButton.setEnabled(false);
                }
            }
        });

        Object[] message = {
            "ID:", id,
            "Name:", name,
            verified
        };
        int inputFields = JOptionPane.showOptionDialog(null, message, "Add Field Station", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{okButton, cancelButton}, null);
        if (inputFields == JOptionPane.OK_OPTION) {
            System.out.print("Added Field Station!");
        }

    }//GEN-LAST:event_btnAddFieldStationActionPerformed

    private void changeSelectedFieldStation(FieldStation selectedStation) {
        SetOfSensors stationSensors = selectedStation.getSetOfSensors();

        EventList<Sensor> eventList = new BasicEventList<Sensor>();

        eventList.clear();
        for (Sensor sensor : stationSensors.getSensors()) {
            eventList.add(sensor);
        }

        sensorsTable = new EventTableModel(eventList, new SensorTableFormat());

        tblSensorTable.setModel(sensorsTable);
        int lastRowIndex = tblSensorTable.getModel().getRowCount() - 1;
        if (lastRowIndex >= 0) {
            tblSensorTable.setRowSelectionInterval(lastRowIndex, lastRowIndex);
            btnRemoveSensor.setEnabled(true);
            btnSensorDetails.setEnabled(true);
        }

        lblFieldStationName.setText(selectedStation.getName());
    }

    private void listUserStationsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listUserStationsValueChanged
        if (!evt.getValueIsAdjusting()) {//prevents double events Eg: from mouse down and mouse up
            FieldStation selected = (FieldStation) listUserStations.getSelectedValue();
            if (selected != null) {
                btnAddSensor.setEnabled(true);
                btnRemoveFieldStation.setEnabled(true);
                btnFieldStationDetails.setEnabled(true);
                selectedStation = server.getFieldStation(selected.getId());
                changeSelectedFieldStation(selectedStation);

                saveUserData("data/userSettings.ser", listUserStations.getSelectedIndex());
            } else {
                btnAddSensor.setEnabled(false);
                btnRemoveFieldStation.setEnabled(false);
                btnFieldStationDetails.setEnabled(false);
            }
        }
    }//GEN-LAST:event_listUserStationsValueChanged

    private void saveUserData(String filePath, int item) {
        int userSettings;
        ObjectOutputStream outstream;
        try {
            outstream = new ObjectOutputStream(new FileOutputStream(filePath));
            userSettings = item;
            outstream.writeInt(userSettings);
            outstream.close();
        } catch (IOException io) {
            //System.out.println(io);
        }
    }

    private int loadUserData(String filePath) {
        //test load
        ObjectInputStream instream = null;
        Integer loadedUserSettings = null;
        try {
            FileInputStream fileinput = new FileInputStream(filePath);
            instream = new ObjectInputStream(fileinput);
            boolean loop = true;
            try {
                loadedUserSettings = instream.readInt();
                //set selected based on loaderUserSettings

            } catch (Exception ex) {
                //System.out.println("EX:" + ex);
            }
        } catch (IOException io) {
            //System.out.println(io);
        }

        if (instream != null) {
            try {
                instream.close();
            } catch (IOException ex) {
                //System.out.println(ex); 
            }
        }
        if (loadedUserSettings == null) {
            return 0;
        } else {
            return loadedUserSettings;
        }
    }

    private void btnAddSensorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSensorActionPerformed
        displayAddSensorPanel();
    }//GEN-LAST:event_btnAddSensorActionPerformed

    private void btnSaveSensorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSensorActionPerformed

        int secondsSeconds = Integer.parseInt(comboIntervalSeconds.getSelectedItem().toString());
        int minutesSeconds = Integer.parseInt(comboIntervalMinutes.getSelectedItem().toString()) * 60;
        int hoursSeconds = Integer.parseInt(comboIntervalHours.getSelectedItem().toString()) * 60 * 60;
        int daysSeconds = Integer.parseInt(comboIntervalDays.getSelectedItem().toString()) * 60 * 60 * 24;
        int interval = secondsSeconds + minutesSeconds + hoursSeconds + daysSeconds;
        int threshold = -1;
        if (!txtThreshold.getText().equals(""));
        {
            threshold = Integer.parseInt(txtThreshold.getText());
        }
        boolean upperlimit = checkIsUpperLimit.isSelected();
        server.addSensor(selectedStation.getId(), txtSensorId.getText(), (String) comboSensorType.getSelectedItem(), (String) comboSensorUnits.getSelectedItem(), interval, threshold, upperlimit);

        panelAddSensor.setVisible(false);
        displayManagerScreen();
        changeSelectedFieldStation(selectedStation);
        clearSensorScreen();
    }//GEN-LAST:event_btnSaveSensorActionPerformed

    private void btnCancelSensorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelSensorActionPerformed
        panelAddSensor.setVisible(false);
        clearSensorScreen();
        displayManagerScreen();
    }//GEN-LAST:event_btnCancelSensorActionPerformed

    private void btnRemoveFieldStationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveFieldStationActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove field station: " + selectedStation.getId(), "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            removeFieldStation(selectedStation.getId());
        }
    }//GEN-LAST:event_btnRemoveFieldStationActionPerformed

    private void btnRemoveSensorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveSensorActionPerformed
        int index = tblSensorTable.getSelectedRow();
        Sensor sensor = (Sensor) sensorsTable.getElementAt(index);
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove sensor: " + sensor.getId(), "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            removeSensor(sensor.getId());
        }
    }//GEN-LAST:event_btnRemoveSensorActionPerformed

    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        // TODO add your handling code here:
        displayReportScreen();
    }//GEN-LAST:event_btnReportActionPerformed

    private void comboReportSensorTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboReportSensorTypeItemStateChanged
        // TODO add your handling code here:
        updateReport();
    }//GEN-LAST:event_comboReportSensorTypeItemStateChanged

    private void dpReportCalendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dpReportCalendarActionPerformed
        // TODO add your handling code here:
        updateReport();
    }//GEN-LAST:event_dpReportCalendarActionPerformed

    private void panelReportCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_panelReportCaretPositionChanged
        // TODO add your handling code here:
        changeReportView();
    }//GEN-LAST:event_panelReportCaretPositionChanged

    private void btnManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManagerActionPerformed
        // TODO add your handling code here:
        displayManagerScreen();
    }//GEN-LAST:event_btnManagerActionPerformed

    private void tblSensorDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSensorDataMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            displayReportSensorDataScreen();
        }
    }//GEN-LAST:event_tblSensorDataMouseClicked

    private void btnBackReportSensorDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackReportSensorDataActionPerformed
        // TODO add your handling code here:
        panelReport.setVisible(true);
        panelReportSensorData.setVisible(false);
    }//GEN-LAST:event_btnBackReportSensorDataActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        serializeData();
    }//GEN-LAST:event_formWindowClosing

    private void btnClearSensorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSensorActionPerformed
        // TODO add your handling code here:
        clearSensorScreen();
    }//GEN-LAST:event_btnClearSensorActionPerformed

    private boolean isIntervalSelected() {
        if (comboIntervalSeconds.getSelectedIndex() == 0 && comboIntervalMinutes.getSelectedIndex() == 0
                && comboIntervalHours.getSelectedIndex() == 0 && comboIntervalDays.getSelectedIndex() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void txtSensorIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSensorIdKeyReleased
        validateAndSetSensorButtons();
    }//GEN-LAST:event_txtSensorIdKeyReleased

    private void validateAndSetSensorButtons() {
        String sensorID = txtSensorId.getText();
        //trim() removes whitespaces and ensures that it contains text other than spaces
        boolean isEmpty = sensorID.trim().isEmpty();
        if (!isEmpty) {
            boolean intervalSelected = false;
            intervalSelected = isIntervalSelected();
            //also check that sensor with this name doesn't already exist.
            Sensor foundSensor = selectedStation.getSetOfSensors().getSensor(sensorID);
            //if null means it doesn't already exist in the collection
            if (foundSensor == null && intervalSelected != false) {
                btnSaveSensor.setEnabled(true);
            } else {
                btnSaveSensor.setEnabled(false);
            }
        } else {
            btnSaveSensor.setEnabled(false);
        }
    }

    private void comboIntervalDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboIntervalDaysActionPerformed
        validateAndSetSensorButtons();
    }//GEN-LAST:event_comboIntervalDaysActionPerformed

    private void comboIntervalMinutesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboIntervalMinutesActionPerformed
        validateAndSetSensorButtons();
    }//GEN-LAST:event_comboIntervalMinutesActionPerformed

    private void comboIntervalHoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboIntervalHoursActionPerformed
        validateAndSetSensorButtons();
    }//GEN-LAST:event_comboIntervalHoursActionPerformed

    private void comboIntervalSecondsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboIntervalSecondsActionPerformed
        validateAndSetSensorButtons();
    }//GEN-LAST:event_comboIntervalSecondsActionPerformed

    private void txtThresholdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtThresholdKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtThresholdKeyReleased

    private void dpReportSensorDataDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dpReportSensorDataDateActionPerformed
        // TODO add your handling code here:
        displayReportSensorDataScreen();
    }//GEN-LAST:event_dpReportSensorDataDateActionPerformed


    private void btnFieldStationDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFieldStationDetailsActionPerformed
        // TODO add your handling code here:
        Object[] message = {
            "ID: " + selectedStation.getId(),
            "Name: " + selectedStation.getName()
        };
        JOptionPane.showMessageDialog(null, message, "Sensor Details", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnFieldStationDetailsActionPerformed

    private void btnSensorDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSensorDetailsActionPerformed
        // TODO add your handling code here:
        int index = tblSensorTable.getSelectedRow();
        Sensor sensor = (Sensor) sensorsTable.getElementAt(index);

        GPSData location = sensor.getGps();

        Object[] message = {
            "ID: " + sensor.getId(),
            "Type: " + sensor.getType(),
            "Units: " + sensor.getUnits(),
            "Location: " + location.GPStoString(),
            (sensor.getThresholdIsUpperLimit() ? "Threshold Upper Limit: " : "Threshold Lower Limit: ") + sensor.getThreshold()
        };

        JOptionPane.showMessageDialog(null, message, "Sensor Details", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnSensorDetailsActionPerformed

    private void sliderServerOnOffMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sliderServerOnOffMouseReleased
        JSlider source = (JSlider) evt.getSource();
        server.togglePower(source.getValue());

    }//GEN-LAST:event_sliderServerOnOffMouseReleased

    private void sliderViewStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderViewStateChanged
        // TODO add your handling code here:
        JSlider source = (JSlider) evt.getSource();
        if (!source.getValueIsAdjusting()) {
            if (source.getValue() == 0) {
                panelReportTable.setVisible(false);
                panelHeatmap.setVisible(true);
                displayHeatmap();
            } else {
                panelReportTable.setVisible(true);
                panelHeatmap.setVisible(false);
            }

        }
    }//GEN-LAST:event_sliderViewStateChanged

    private void buttonRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRegisterActionPerformed
        panelRegister.setVisible(true);
        panelLogIn.setVisible(false);
    }//GEN-LAST:event_buttonRegisterActionPerformed

    private void sliderServerOnOffKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sliderServerOnOffKeyReleased
        // TODO add your handling code here:
        JSlider source = (JSlider) evt.getSource();
        server.togglePower(source.getValue());
    }//GEN-LAST:event_sliderServerOnOffKeyReleased

    private void comboSensorTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboSensorTypeActionPerformed
        //When user changes the sensor type via dropdown, update the sensor units to the relevant options
        String sensorType = comboSensorType.getSelectedItem().toString();
        switch (sensorType) {
            case "Soil Moisture":
                comboSensorUnits.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"mm", "%"}));
                break;
            case "Soil Temperature":
                comboSensorUnits.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"°C", "°F"}));
                break;
            case "Air Temperature":
                comboSensorUnits.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"°C", "°F"}));
                break;
            case "Soil Acidity":
                comboSensorUnits.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"pH"}));
                break;
            case "Light Intensity":
                comboSensorUnits.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Lux"}));
                break;

        }


    }//GEN-LAST:event_comboSensorTypeActionPerformed

    private void comboReportFieldStationsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboReportFieldStationsItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            if (loadData == true) {
                updateReport();
                saveUserData("data/userData.ser", comboReportFieldStations.getSelectedIndex());
            }
            loadData = true;

        }
    }//GEN-LAST:event_comboReportFieldStationsItemStateChanged

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Hello");
        }
        logIn();
    }//GEN-LAST:event_jButton1KeyPressed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        txtUsernameReg.setText("");
        txtPasswordReg.setText("");
        panelRegister.setVisible(false);
        panelLogIn.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed

        int userRole = comboUserRole.getSelectedIndex();
        String username = txtUsernameReg.getText();
        String password = txtPasswordReg.getText();
        lblStatus.setForeground(Color.red);

        if (username.length() == 0 || password.length() == 0) {
            lblStatus.setText("Information missing!");
        } else {

            try {
                server.createUserAccount(username, password, userRole);
                lblStatus.setForeground(Color.green);
                lblStatus.setText("Success.");
            } catch (Exception e) {
                lblStatus.setText("An error occured" + e.getMessage());
            }
        }

    }//GEN-LAST:event_btnRegisterActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        UserInterface userint = new UserInterface();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                userint.setVisible(true);
            }
        });

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                //ADD INTERVALL CALLS HERE
                Vector<FieldStation> fieldStations = server.loadData();
                if (fieldStations != null) {
                    for (FieldStation aFieldStations : fieldStations) {

                        for (Sensor aSensor : aFieldStations.getSetOfSensors().getSensors()) {
                            if (aSensor.onInterval()) {
                                if (server.getTurnedOn()) {
                                    userint.updateReport();
                                }
                            }
                        }
                    }
                }
            }
        }, 1000, 1000);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAddFieldStation;
    public javax.swing.JButton btnAddSensor;
    private javax.swing.JButton btnBackReportSensorData;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancelSensor;
    private javax.swing.JButton btnClearSensor;
    private javax.swing.JButton btnFieldStationDetails;
    public javax.swing.JButton btnManager;
    private javax.swing.JButton btnRegister;
    public javax.swing.JButton btnRemoveFieldStation;
    private javax.swing.JButton btnRemoveSensor;
    public javax.swing.JButton btnReport;
    public javax.swing.JButton btnSaveSensor;
    private javax.swing.JButton btnSensorDetails;
    private javax.swing.JButton buttonRegister;
    private javax.swing.JCheckBox checkIsUpperLimit;
    private javax.swing.JComboBox comboIntervalDays;
    private javax.swing.JComboBox comboIntervalHours;
    private javax.swing.JComboBox comboIntervalMinutes;
    private javax.swing.JComboBox comboIntervalSeconds;
    private javax.swing.JComboBox comboReportFieldStations;
    private javax.swing.JComboBox comboReportSensorType;
    public javax.swing.JComboBox comboSensorType;
    public javax.swing.JComboBox comboSensorUnits;
    private javax.swing.JComboBox comboUserRole;
    private org.jdesktop.swingx.JXDatePicker dpReportCalendar;
    private org.jdesktop.swingx.JXDatePicker dpReportSensorDataDate;
    private javax.swing.JButton jButton1;
    private javax.swing.JFrame jFrameServer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblAddNewSensor;
    private javax.swing.JLabel lblFieldStationManager;
    private javax.swing.JLabel lblFieldStationName;
    private javax.swing.JLabel lblFieldStationName2;
    private javax.swing.JLabel lblIntervalDays;
    private javax.swing.JLabel lblIntervalHours;
    private javax.swing.JLabel lblIntervalMinutes;
    private javax.swing.JLabel lblIntervalSeconds;
    private javax.swing.JLabel lblReportSensorDataDate;
    private javax.swing.JLabel lblReportSensorDataFieldStationName;
    private javax.swing.JLabel lblReportSensorDataNextInterval;
    private javax.swing.JLabel lblReportSensorDataNextIntervalDate;
    private javax.swing.JLabel lblReportSensorDataSensorName;
    private javax.swing.JLabel lblReportSensorDataSensorType;
    private javax.swing.JLabel lblReportSensorDataSensorUnits;
    private javax.swing.JLabel lblReportTitle;
    private javax.swing.JLabel lblReportTitle1;
    private javax.swing.JLabel lblReportTitle2;
    private javax.swing.JLabel lblReportTitle3;
    private javax.swing.JLabel lblReportTitle4;
    private javax.swing.JLabel lblSensorId;
    private javax.swing.JLabel lblSensorInterval;
    private javax.swing.JLabel lblSensorList;
    private javax.swing.JLabel lblSensorType;
    private javax.swing.JLabel lblSensorUnits;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblThreshold;
    private javax.swing.JLabel lblUserRole;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsername1;
    public javax.swing.JList listUserStations;
    private javax.swing.JPanel panelAddSensor;
    private javax.swing.JPanel panelHeatmap;
    private javax.swing.JPanel panelLogIn;
    private javax.swing.JPanel panelManager;
    private javax.swing.JPanel panelRegister;
    private javax.swing.JPanel panelReport;
    private javax.swing.JPanel panelReportSensorData;
    private javax.swing.JPanel panelReportTable;
    private javax.swing.JPanel panelSwitcher;
    private javax.swing.JSlider sliderServerOnOff;
    private javax.swing.JSlider sliderView;
    private javax.swing.JTable tblReportSensorData;
    private javax.swing.JTable tblSensorData;
    public javax.swing.JTable tblSensorTable;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JPasswordField txtPasswordReg;
    public javax.swing.JTextField txtSensorId;
    private javax.swing.JLabel txtServerOff;
    private javax.swing.JLabel txtServerOn;
    private javax.swing.JTextField txtThreshold;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JTextField txtUsernameReg;
    // End of variables declaration//GEN-END:variables
}
