import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SuperCalculator extends Application {

    private TextField display = new TextField();
    private StringBuilder currentInput = new StringBuilder();
    private double lastResult = 0;
    private String lastOperator = "";
    private boolean scientificMode = false;
    private GridPane buttonGrid = new GridPane();
    private VBox root = new VBox(10);
    private Label modeLabel = new Label("Standard Mode");

    // Colors
    private Color backgroundColor = Color.web("#2b2b2b");
    private Color displayColor = Color.web("#1e1e1e");
    private Color numberButtonColor = Color.web("#3c3c3c");
    private Color functionButtonColor = Color.web("#5c5c5c");
    private Color operatorButtonColor = Color.web("#ff9500"); // oranžová
    private Color buttonTextColor = Color.web("#ffffff");

    private VBox modeDropdown = new VBox(5);
    private boolean dropdownVisible = false;

    private VBox settingsPanel = new VBox(10);
    private boolean settingsVisible = false;

    @Override
    public void start(Stage stage) {

        display.setEditable(false);
        display.setPrefHeight(60);
        display.setStyle("-fx-font-size: 24px; -fx-alignment: center-right; -fx-background-radius: 10;" +
                "-fx-background-color: " + toRgbString(displayColor) + "; -fx-text-fill: " + toRgbString(buttonTextColor));

        modeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 5px;");

        root.setPadding(new Insets(15));
        root.getChildren().addAll(createTopBar(), modeLabel, display, buttonGrid, createSettingsPanel());
        root.setStyle("-fx-background-color: " + toRgbString(backgroundColor) + "; -fx-background-radius: 15;");

        createCalculatorButtons();

        Scene scene = new Scene(root, 550, 650);
        stage.setScene(scene);
        stage.setTitle("Super Calculator");
        stage.show();
    }

    private HBox createTopBar() {
        Button menuBtn = new Button("≡");
        menuBtn.setStyle("-fx-font-size: 20px; -fx-background-color: transparent; -fx-text-fill: white;");

        modeDropdown.setStyle("-fx-background-color: #3c3c3c; -fx-padding: 10; -fx-background-radius: 10;");
        modeDropdown.setVisible(false);

        Button standardBtn = new Button("Standard Mode");
        Button scientificBtn = new Button("Scientific Mode");
        for (Button b : new Button[]{standardBtn, scientificBtn}) {
            b.setPrefWidth(150);
            b.setStyle("-fx-background-color: #5c5c5c; -fx-text-fill: white; -fx-background-radius: 10;");
        }
        standardBtn.setOnAction(e -> { scientificMode = false; modeLabel.setText("Standard Mode"); createCalculatorButtons(); });
        scientificBtn.setOnAction(e -> { scientificMode = true; modeLabel.setText("Scientific Mode"); createCalculatorButtons(); });

        modeDropdown.getChildren().addAll(standardBtn, scientificBtn);
        modeDropdown.setTranslateX(-200);

        menuBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> toggleModeDropdown());

        StackPane menuStack = new StackPane();
        menuStack.getChildren().addAll(menuBtn, modeDropdown);
        StackPane.setAlignment(menuBtn, Pos.CENTER_LEFT);
        StackPane.setAlignment(modeDropdown, Pos.TOP_LEFT);

        HBox topBar = new HBox(10, menuStack);
        topBar.setAlignment(Pos.CENTER_LEFT);
        return topBar;
    }

    private VBox createSettingsPanel() {
        Button gearBtn = new Button("⚙");
        gearBtn.setStyle("-fx-font-size:20px; -fx-background-color: transparent; -fx-text-fill:white;");

        settingsPanel.setStyle("-fx-background-color: #3c3c3c; -fx-padding: 10; -fx-background-radius: 10;");
        settingsPanel.setVisible(false);

        ToggleButton darkLightToggle = new ToggleButton("🌙");
        darkLightToggle.setOnAction(e -> {
            if(darkLightToggle.isSelected()) { darkLightToggle.setText("☀"); applyLightMode(); }
            else { darkLightToggle.setText("🌙"); applyDarkMode(); }
        });
        darkLightToggle.setPrefWidth(120);

        Button customSettingsBtn = new Button("Custom Settings");
        customSettingsBtn.setOnAction(e -> openCustomSettings());
        customSettingsBtn.setPrefWidth(120);

        settingsPanel.getChildren().addAll(darkLightToggle, customSettingsBtn);
        settingsPanel.setTranslateY(-150);

        gearBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> toggleSettingsPanel());

        VBox container = new VBox(5, gearBtn, settingsPanel);
        container.setAlignment(Pos.CENTER_RIGHT);
        return container;
    }

    private void toggleModeDropdown() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), modeDropdown);
        if(!dropdownVisible){
            modeDropdown.setVisible(true);
            tt.setFromX(-200); tt.setToX(0); tt.play();
            dropdownVisible = true;
        } else {
            tt.setFromX(0); tt.setToX(-200);
            tt.setOnFinished(e -> modeDropdown.setVisible(false));
            tt.play();
            dropdownVisible = false;
        }
    }

    private void toggleSettingsPanel() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), settingsPanel);
        if(!settingsVisible){
            settingsPanel.setVisible(true);
            tt.setFromY(-150); tt.setToY(0); tt.play();
            settingsVisible = true;
        } else {
            tt.setFromY(0); tt.setToY(-150);
            tt.setOnFinished(e -> settingsPanel.setVisible(false));
            tt.play();
            settingsVisible = false;
        }
    }

    private void createCalculatorButtons() {
        buttonGrid.getChildren().clear();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.setAlignment(Pos.CENTER);

        String[] buttons = {
                "C","CE","%","÷",
                "7","8","9","×",
                "4","5","6","−",
                "1","2","3","+",
                "0",".","^","="
        };

        int row=0, col=0;
        for (String b : buttons) {
            Button btn = createButton(b);
            buttonGrid.add(btn, col, row);
            col++;
            if(col>3){ col=0; row++; }
        }

        if(scientificMode){
            String[] sci = {"√","sin","cos","tan","log","ln","π","e"};
            int colSci=4, rowSci=0;
            for(String b:sci){
                Button btn = createButton(b);
                buttonGrid.add(btn, colSci, rowSci);
                rowSci++;
            }
        }
    }

    private Button createButton(String text){
        Button btn = new Button(text);
        btn.setPrefSize(70,70);
        styleButton(btn);
        btn.setOnAction(e -> { handleButton(text); animateButton(btn); });
        return btn;
    }

    private void animateButton(Button btn){
        ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
        st.setFromX(1); st.setFromY(1);
        st.setToX(0.9); st.setToY(0.9);
        st.setAutoReverse(true); st.setCycleCount(2);
        st.play();
    }

    private void styleButton(Button btn){
        Color bg;
        if(btn.getText().matches("[0-9\\.]")) bg = numberButtonColor;
        else if(btn.getText().matches("[+\\-×÷=^%√]")) bg = operatorButtonColor;
        else bg = functionButtonColor;

        btn.setStyle(
                "-fx-background-color: "+toRgbString(bg)+";"+
                        "-fx-text-fill: "+toRgbString(buttonTextColor)+";"+
                        "-fx-font-size:18px;"+
                        "-fx-background-radius:15;"+
                        "-fx-border-color:#555555;"+
                        "-fx-border-radius:15;"+
                        "-fx-border-width:1;"+
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4,0,2,2);"
        );
    }

    private void handleButton(String text){
        try{
            switch(text){
                case "C": currentInput.setLength(0); lastResult=0; lastOperator=""; display.setText(""); break;
                case "CE": currentInput.setLength(0); display.setText(""); break;
                case "=": calculate(); break;
                case "+": case "−": case "×": case "÷": case "^": case "%": case "√": setOperator(text); break;
                case "sin": case "cos": case "tan": case "log": case "ln": case "π": case "e": applyScientificFunction(text); break;
                default: addNumber(text); break;
            }
        }catch(Exception e){ display.setText("Error"); currentInput.setLength(0); }
    }

    private void addNumber(String num){ currentInput.append(num); display.setText(currentInput.toString()); }

    private void setOperator(String op){
        if(currentInput.length()>0) lastResult=parseInput(currentInput.toString());
        currentInput.setLength(0); lastOperator=op;
    }

    private void calculate(){
        if(currentInput.length()==0 && !lastOperator.equals("√")) return;
        double current = currentInput.length()>0? parseInput(currentInput.toString()):0;
        switch(lastOperator){
            case "+": lastResult+=current; break;
            case "−": lastResult-=current; break;
            case "×": lastResult*=current; break;
            case "÷": lastResult/=current; break;
            case "^": lastResult=Math.pow(lastResult,current); break;
            case "%": lastResult=lastResult*current/100; break;
            case "√": lastResult=Math.sqrt(lastResult); break;
        }
        display.setText(String.valueOf(lastResult));
        currentInput.setLength(0); lastOperator="";
    }

    private void applyScientificFunction(String func){
        double val=0;
        if(func.equals("π")) val=Math.PI;
        else if(func.equals("e")) val=Math.E;
        else { if(currentInput.length()==0) return; val=parseInput(currentInput.toString()); }
        double res=0;
        switch(func){
            case "sin": res=Math.sin(Math.toRadians(val)); break;
            case "cos": res=Math.cos(Math.toRadians(val)); break;
            case "tan": res=Math.tan(Math.toRadians(val)); break;
            case "√": res=Math.sqrt(val); break;
            case "log": res=Math.log10(val); break;
            case "ln": res=Math.log(val); break;
            case "π": case "e": res=val; break;
        }
        display.setText(String.valueOf(res));
        currentInput.setLength(0);
    }

    private double parseInput(String s){ return Double.parseDouble(s.replace("−","-").replace("×","*").replace("÷","/")); }

    private void applyDarkMode(){ backgroundColor=Color.web("#2b2b2b"); displayColor=Color.web("#1e1e1e"); numberButtonColor=Color.web("#3c3c3c"); functionButtonColor=Color.web("#5c5c5c"); applyTheme(); }

    private void applyLightMode(){ backgroundColor=Color.web("#e0e0e0"); displayColor=Color.web("#ffffff"); numberButtonColor=Color.web("#d0d0d0"); functionButtonColor=Color.web("#b0b0b0"); applyTheme(); }

    private void openCustomSettings(){
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Custom Settings");

        ColorPicker bgPicker = new ColorPicker(backgroundColor);
        ColorPicker displayPicker = new ColorPicker(displayColor);
        ColorPicker numberPicker = new ColorPicker(numberButtonColor);
        ColorPicker functionPicker = new ColorPicker(functionButtonColor);

        Button apply = new Button("Apply");
        apply.setOnAction(e -> {
            backgroundColor=bgPicker.getValue(); displayColor=displayPicker.getValue();
            numberButtonColor=numberPicker.getValue(); functionButtonColor=functionPicker.getValue();
            applyTheme(); settingsStage.close();
        });

        VBox layout = new VBox(10,
                new Label("Background:"), bgPicker,
                new Label("Display:"), displayPicker,
                new Label("Number buttons:"), numberPicker,
                new Label("Function buttons:"), functionPicker,
                apply);
        layout.setPadding(new Insets(10)); layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 250, 300);
        settingsStage.setScene(scene);
        settingsStage.show();
    }

    private void applyTheme(){
        root.setStyle("-fx-background-color: "+toRgbString(backgroundColor)+"; -fx-background-radius: 15;");
        display.setStyle("-fx-background-color: "+toRgbString(displayColor)+"; -fx-text-fill:"+toRgbString(buttonTextColor)+"; -fx-font-size:24px; -fx-alignment:center-right; -fx-background-radius:10;");
        for(var node: buttonGrid.getChildren()) if(node instanceof Button b) styleButton(b);
    }

    private String toRgbString(Color c){ return String.format("rgb(%d,%d,%d)",(int)(c.getRed()*255),(int)(c.getGreen()*255),(int)(c.getBlue()*255)); }

    public static void main(String[] args){ launch(); }
}