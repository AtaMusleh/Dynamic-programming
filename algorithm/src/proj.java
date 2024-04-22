import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class proj extends Application {
	Stage stage;
	static String result = new String("");
	String ledno;
	TextField t1;
	TextField t2;
	Scene outputScreenScene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setResizable(false);

		Image lightBulbOnImage = new Image("on.png");
		Image lightBulbOffImage = new Image("off.png");
		ImageView lightBulbImageView = new ImageView(lightBulbOffImage);

		lightBulbImageView.setOnMouseEntered(event -> {
			lightBulbImageView.setImage(lightBulbOnImage);

		});

		lightBulbImageView.setOnMouseExited(event -> {
			lightBulbImageView.setImage(lightBulbOffImage);
		});
		Label TopL = new Label("Max Led Lighting");
		TopL.setTextFill(Color.WHITE);
		TopL.setStyle("-fx-font-size: 30px;  -fx-font-weight: bold;");
		Button start = new Button("Start");
		start.setStyle(
				"-fx-background-radius: 15;-fx-min-width: 100px; -fx-min-height: 40px; -fx-font-size: 16px;-fx-font-weight: bold;-fx-background-color: #ffffff;");
		ColorAdjust colorAdjust = new ColorAdjust();

		start.setEffect(colorAdjust);

		start.setOnMouseEntered(event -> {
			colorAdjust.setBrightness(0.3);
		});

		start.setOnMouseExited(event -> {
			colorAdjust.setBrightness(0.0);
		});
		start.setOnAction(e -> {
			inputScreen();
		});
		VBox v = new VBox(lightBulbImageView, start);
		v.setAlignment(Pos.CENTER);
		BorderPane bp = new BorderPane();
		bp.setTop(TopL);
		bp.setCenter(v);
		bp.setAlignment(start, Pos.BOTTOM_CENTER);
		bp.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
		bp.setAlignment(TopL, Pos.CENTER);
		Scene scene = new Scene(bp, 400, 400);
		stage.setTitle("Max Led Lighing");
		stage.setScene(scene);
		stage.show();
	}

	void inputScreen() {
		Label top = new Label("Enter the number of LEDs and\n" + " the permutation of the LEDs");
		top.setTextFill(Color.WHITE);
		top.setStyle("-fx-font-size: 22px;  -fx-font-weight: bold;");
		Label l1 = new Label("LEDs NO.: ");
		l1.setTextFill(Color.WHITE);
		l1.setStyle("-fx-font-size: 14px;  -fx-font-weight: bold;");
		t1 = new TextField();
		Label l2 = new Label("permutation: ");
		l2.setTextFill(Color.WHITE);
		l2.setStyle("-fx-font-size: 14px;  -fx-font-weight: bold;");
		t2 = new TextField();
		t2.setPromptText("Space separated");

		Button con = new Button("Confirm");
		con.setStyle(
				"-fx-background-radius: 15;-fx-min-width: 100px; -fx-min-height: 40px; -fx-font-size: 16px;-fx-font-weight: bold;-fx-background-color: #ffffff;");
		con.setOnAction(e1 -> {
			String t2Text = t2.getText().trim();

			// Check if t2 starts with a number
			if (t2Text.isEmpty() || !Character.isDigit(t2Text.charAt(0))) {
				showAlert("Invalid input", "Permutation must start with a number.");
				return;
			}
			if (!Character.isDigit(t2Text.charAt(0))) {
				showAlert("Invalid input", "Permutation must start with a number.");
				return;
			}

			String[] tokens = t2Text.split("\\s+");

			// Check for duplicates
			Set<String> uniqueTokens = new HashSet<>(Arrays.asList(tokens));
			if (uniqueTokens.size() != tokens.length) {
				showAlert("Invalid input", "Duplicate values found in permutation.");
				return;
			}

			// Check if the number of elements in t2 is equal to t1 value
			try {
				int t1Value = Integer.parseInt(t1.getText());
				if (tokens.length != t1Value) {
					showAlert("Invalid input", "The permutation must contain exactly " + t1Value + " numbers.");
					return;
				}
			} catch (NumberFormatException ex) {
				showAlert("Invalid input", "Please enter a valid number in t1.");
				return;
			}

			// Check if t2 contains only numbers and spaces
			if (!t2Text.matches("[0-9 ]*")) {
				showAlert("Invalid input", "Permutation must only contain numbers and spaces.");
				return;
			}

			// Check if all numbers in t2 are within the range of 1 to t1
			try {
				int t1Value = Integer.parseInt(t1.getText());
				for (String token : tokens) {
					int number = Integer.parseInt(token);
					if (number < 1 || number > t1Value) {
						showAlert("Invalid input", "Numbers in permutation must be between 1 and " + t1Value + ".");
						return;
					}
				}
			} catch (NumberFormatException ex) {
				showAlert("Invalid input", "Please enter valid numeric values in t2.");
				return;
			}

			outputScreen();

		});
		BooleanBinding isButtonDisabled = Bindings.createBooleanBinding(
				() -> t1.getText().trim().isEmpty() || t2.getText().trim().isEmpty(), t1.textProperty(),
				t2.textProperty());

		con.disableProperty().bind(isButtonDisabled);

		t1.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				t1.setText(oldValue);
			}
		});
		t1.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("[0-9]+") && !newValue.isEmpty()) {
				t1.setText(oldValue);
				showAlert("Invalid input", "Please enter a positive integer.");
			}
		});
		Button random = new Button("Random");
		random.setStyle(
				"-fx-background-radius: 15;-fx-min-width: 100px; -fx-min-height: 40px; -fx-font-size: 16px;-fx-font-weight: bold;-fx-background-color: #ffffff;");
		random.setOnAction(e3 -> {
			if (t1.getText() == " " || t1.getText() == null || t1.getText() == "") {
				t1.setText("");
				t2.setText("");
				t1.setText(generateRandomSequence(1, 4000, 1));
				t2.setText(generateRandomSequence(1, Integer.parseInt(t1.getText()), Integer.parseInt(t1.getText())));
			} else if ((t1.getText() != " " || t1.getText() != null || t1.getText() != "") && t2.getText() == null
					|| t2.getText() == "" || t2.getText() == " ") {
				t2.setText("");
				t2.setText(generateRandomSequence(1, Integer.parseInt(t1.getText()), Integer.parseInt(t1.getText())));
			} else if (!t1.getText().isEmpty() && !t2.getText().isEmpty()) {
				// Display confirmation dialog
				boolean confirmed = showConfirmationDialog("Confirmation",
						"Changing the leds permutation to random sequence will overwrite the existing values. Do you want to proceed?");
				if (!confirmed) {

					return; // User canceled the operation
				} else {
					t2.setText(
							generateRandomSequence(1, Integer.parseInt(t1.getText()), Integer.parseInt(t1.getText())));
				}
			}
		});
		Button fileRead = new Button("File");
		fileRead.setStyle(
				"-fx-background-radius: 15;-fx-min-width: 100px; -fx-min-height: 40px; -fx-font-size: 16px;-fx-font-weight: bold;-fx-background-color: #ffffff;");
		fileRead.setOnAction(e4 -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
			File selectedFile = fileChooser.showOpenDialog(stage);

			if (selectedFile != null) {
				try {
					List<String> lines = Files.readAllLines(selectedFile.toPath());

					if (!lines.isEmpty()) {
						// Check if the first line contains a number (LED number)
						String firstLine = lines.get(0).trim();
						if (firstLine.matches("\\d+")) {
							t1.setText(firstLine);

							// Set LEDs sequence from subsequent lines
							StringBuilder sequenceBuilder = new StringBuilder();
							for (int i = 1; i < lines.size(); i++) {
								sequenceBuilder.append(lines.get(i).trim());
								if (i < lines.size() - 1) {
									sequenceBuilder.append(" ");
								}
							}
							t2.setText(sequenceBuilder.toString());
						} else {
							// No LED number in the first line, assume it's only the LED sequence
							t1.setText(String.valueOf(lines.get(0).trim().split("\\s+").length));
							t2.setText(lines.get(0).trim());
						}
					} else {
						showAlert("Invalid file content", "The file is empty.");
					}
				} catch (IOException ex) {
					showAlert("Error", "Error reading the file.");
				}
			}
		});

		VBox v1 = new VBox(25, l1, l2);
		VBox v2 = new VBox(15, t1, t2);
		HBox h1 = new HBox(15, v1, v2);
		HBox h2 = new HBox(15, random, fileRead);
		VBox v = new VBox(20, h1, h2, con);
		v.setAlignment(Pos.CENTER);
		h1.setAlignment(Pos.CENTER);
		v1.setAlignment(Pos.CENTER);
		h2.setAlignment(Pos.CENTER);
		v2.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane();
		root.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
		root.setTop(top);
		root.setCenter(v);
		root.setAlignment(top, Pos.TOP_CENTER);
		root.setAlignment(v, Pos.CENTER);
		Scene s = new Scene(root, 400, 400);
		stage.setScene(s);
		stage.show();
	}

	void outputScreen() {
		int ledNo = Integer.parseInt(t1.getText());
		String[] seq = t2.getText().trim().split("\\s+");
		int[] ledSeq = new int[ledNo];

		try {
			for (int i = 0; i < seq.length; i++) {
				ledSeq[i] = Integer.parseInt(seq[i]);
			}
		} catch (NumberFormatException ex) {
			showAlert("Invalid input", "Please enter valid numeric values in t2.");
			return;
		}

		int[] sourceSequence;
		sourceSequence = new int[ledNo];
		for (int i = 0; i < ledNo; i++) {
			sourceSequence[i] = i + 1;
		}
		int[][] LCSTable = createTable(ledNo + 1);
		// int[][] PathTable = createTable(ledNo + 1);
		Label top = new Label("Result");
		top.setTextFill(Color.WHITE);
		top.setStyle("-fx-font-size: 22px;  -fx-font-weight: bold;");
		Label l1 = new Label("Number of LEDs that will light is: ");
		l1.setTextFill(Color.WHITE);
		l1.setStyle("-fx-font-size: 14px;  -fx-font-weight: bold;");
		Label l1r = new Label();
		l1r.setTextFill(Color.WHITE);
		l1r.setStyle("-fx-font-size: 14px;  -fx-font-weight: bold;");
		l1r.setText("" + maxNum(sourceSequence, ledSeq, LCSTable));
		Label l2 = new Label("The LEDs that will light are: ");
		l2.setTextFill(Color.WHITE);
		l2.setStyle("-fx-font-size: 14px;  -fx-font-weight: bold;");
		Label l2r = new Label();
		l2r.setTextFill(Color.WHITE);
		l2r.setStyle("-fx-font-size: 14px;  -fx-font-weight: bold;");

		print(sourceSequence, ledSeq, sourceSequence.length, ledNo, LCSTable);
		l2r.setText(result);
		Button back = new Button("Back");
		back.setOnAction(e -> {
			result = " ";
			inputScreen();
			l2r.setText(null);
		});
		back.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		Button graph = new Button("Wire connection");
		graph.setStyle(
				"-fx-background-radius: 15;-fx-min-width: 60px; -fx-min-height: 30px; -fx-font-size: 12px;-fx-font-weight: bold;-fx-background-color: #ffffff;");
		graph.setOnAction(e -> {
			ScrollPane sp = new ScrollPane();
			ArrayList leds = new ArrayList();
			ArrayList pwrs = new ArrayList();
			ArrayList<String> on = new ArrayList<>();
			String[] res = result.split(" ");
			for (int i = 0; i < res.length; i++) {
				on.add(res[i]);
			}
			Image onImage = new Image("onS.png");
			Image offImage = new Image("offS.png");
			Image pwr = new Image("pwr.png");

			Pane pane = new Pane();

			int height = 400;
			int width = 0;

			for (int i = 0; i < ledSeq.length; i++) {

				int actualLedNumber = ledSeq[i];
				ImageView imageView = new ImageView(offImage);

				if (on.indexOf(seq[i]) != -1) {
					imageView.setImage(onImage);
				} else {
					imageView.setImage(offImage);
				}

				ImageView imageView2 = new ImageView(pwr);
				Label pwrL = new Label("Power NO. " + (i + 1) + "");
				pwrL.setAlignment(Pos.CENTER);
				pwrL.setStyle("-fx-text-fill:white;-fx-font-size:14px");
				VBox v1 = new VBox(5, pwrL, imageView2);
				v1.setAlignment(Pos.CENTER);
				v1.setId((i + 1) + "");
				v1.setLayoutX(width);
				v1.setPadding(new Insets(0, 0, 0, 5));
				pwrs.add(v1);

				Label ledL = new Label("LED NO." + actualLedNumber);
				ledL.setAlignment(Pos.CENTER);
				ledL.setStyle("-fx-text-fill:white;-fx-font-size:14px");
				VBox v2 = new VBox(5, imageView, ledL);
				v2.setAlignment(Pos.CENTER);
				leds.add(v2);
				v2.setLayoutX(width);
				v2.setLayoutY(height);
				v2.setPadding(new Insets(0, 0, 0, 15));
				if (on.indexOf(seq[i]) != -1) {
					v2.setId(actualLedNumber + " on");
				} else
					v2.setId(actualLedNumber + "");

				pane.getChildren().addAll(v1, v2);

				width += 99;

			}
			int index = 0;
			for (int i = 0; i < ledSeq.length; i++) {
				Node id = (Node) leds.get(i);
				String[] s = id.getId().split(" ");

				if (s.length == 2) {
					Bounds led = id.localToParent(id.getBoundsInLocal());

					Node id2 = (Node) pwrs.get(Integer.parseInt(s[0]) - 1);
					Bounds power = id2.localToParent(id2.getBoundsInLocal());

					Line line = new Line();
					line.setStartX(led.getCenterX() + 22);
					line.setStartY(led.getCenterY() - 35);
					line.setEndX(power.getCenterX() + 9);
					line.setEndY(power.getCenterY() + 50);
					line.setStyle("-fx-stroke-width: 4px; -fx-stroke: red;");
					pane.getChildren().add(line);
				}
			}
			sp.setContent(pane);
			Button esc = new Button("Back");
			esc.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
			esc.setOnAction(e1 -> {
				stage.setScene(outputScreenScene);
			});
			HBox h = new HBox(10, esc);
			h.setAlignment(Pos.TOP_LEFT);
			Label l = new Label("Wires");
			l.setStyle("-fx-font-size: 25px;  -fx-font-weight: bold; -fx-text-fill: white;");
			StackPane st = new StackPane();
			st.getChildren().addAll(h, l);
			st.setAlignment(l, Pos.TOP_CENTER);
			st.setAlignment(h, Pos.TOP_LEFT);
			st.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
			VBox v = new VBox();
			v.getChildren().addAll(st, sp);
			v.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
			v.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
			VBox.setVgrow(pane, Priority.ALWAYS);
			pane.setPadding(new Insets(20, 20, 20, 20));
			pane.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
			sp.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
			h.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
			// Use the root Pane as needed, for example, set it as the content of a new
			// scene

			Scene scene = new Scene(v, 1000, 590);
			scene.setFill(Color.DIMGREY);
			stage.setScene(scene);
			stage.show();
		});
		Button table = new Button("DB Table");
		table.setStyle(
				"-fx-background-radius: 15;-fx-min-width: 60px; -fx-min-height: 30px; -fx-font-size: 12px;-fx-font-weight: bold;-fx-background-color: #ffffff;");

		table.setOnAction(e -> {
			TextArea textArea = new TextArea();
			textArea.setEditable(false);
			textArea.setStyle("-fx-font-size: 16; -fx-control-inner-background: dimgrey; -fx-alignment: center;");

			textArea.appendText("\t0\t");
			for (int k = 0; k < ledSeq.length; k++) {
				textArea.appendText(ledSeq[k] + "\t");
			}
			textArea.appendText("\n");

			textArea.appendText("0\t");
			for (int i = 0; i <= sourceSequence.length; i++) {
				if (i > 0) {

					textArea.appendText(sourceSequence[i - 1] + "\t");
				}
				for (int j = 0; j <= ledSeq.length; j++) {
					textArea.appendText(LCSTable[i][j] + "\t");
				}
				textArea.appendText("\n");
			}

			Button esc = new Button("Back");
			esc.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
			esc.setOnAction(e1 -> {
				stage.setScene(outputScreenScene);
			});

			Label topL = new Label("DB Table");
			topL.setStyle("-fx-font-size: 15px;  -fx-font-weight: bold; -fx-text-fill: white;");
			HBox hbt = new HBox(10);
			hbt.setAlignment(Pos.TOP_LEFT);
			hbt.getChildren().add(esc);
			StackPane st = new StackPane();
			st.getChildren().addAll(hbt, topL);
			st.setAlignment(topL, Pos.TOP_CENTER);
			st.setAlignment(hbt, Pos.TOP_LEFT);
			st.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
			VBox v = new VBox();
			v.getChildren().addAll(st, textArea);
			v.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
			VBox.setVgrow(textArea, Priority.ALWAYS);
			Scene s = new Scene(v, 400, 400);

			stage.setScene(s);
			stage.show();
		});
		HBox hb = new HBox(back);
		hb.setAlignment(Pos.TOP_LEFT);
		StackPane st = new StackPane();
		st.getChildren().addAll(hb, top);
		st.setAlignment(hb, Pos.TOP_LEFT);
		st.setAlignment(top, Pos.TOP_CENTER);
		VBox v1 = new VBox(15, l1, l2);
		VBox v2 = new VBox(15, l1r, l2r);
		HBox h1 = new HBox(15, graph, table);
		HBox h = new HBox(15, v1, v2);
		VBox v = new VBox(30, h, h1);
		v1.setAlignment(Pos.CENTER);
		v2.setAlignment(Pos.CENTER);
		h1.setAlignment(Pos.CENTER);
		h.setAlignment(Pos.CENTER_LEFT);
		v.setAlignment(Pos.CENTER);
		BorderPane bp = new BorderPane();
		bp.setTop(st);
		bp.setCenter(v);
		bp.setBackground(new Background(new BackgroundFill(Color.DIMGREY, null, null)));
		outputScreenScene = new Scene(bp, 400, 400);
		stage.setScene(outputScreenScene);
		stage.show();

	}

	// Method to find the maximum number of lights that can be turned on
	static int maxNum(int[] source, int[] leds, int[][] LongestCS) {
		for (int i = 1; i <= source.length; i++) {
			for (int j = 1; j <= leds.length; j++) {
				if (source[i - 1] == leds[j - 1]) {
					LongestCS[i][j] = LongestCS[i - 1][j - 1] + 1;
				} else {
					LongestCS[i][j] = Math.max(LongestCS[i - 1][j], LongestCS[i][j - 1]);
				}
			}
		}
		return LongestCS[source.length][leds.length];
	}

	// Method to print the LEDs that will light up
	static void print(int[] source, int[] leds, int i, int j, int[][] LongestCS) {
		try {
			if (i == 0 || j == 0) {
				return;
			}

			if (source[i - 1] == leds[j - 1]) {
				print(source, leds, i - 1, j - 1, LongestCS);
				result += source[i - 1] + " ";
			} else if (LongestCS[i - 1][j] >= LongestCS[i][j - 1]) {
				print(source, leds, i - 1, j, LongestCS);
			} else {
				print(source, leds, i, j - 1, LongestCS);
			}
		} catch (StackOverflowError e) {
			result += source[i - 1] + " ";

		}
	}

	public static int[][] createTable(int N) {
		int[][] Table = new int[N][N];
		return Table;
	}

	// Method to display an error alert
	private void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	// Method to generate a random sequence of LEDs
	private static String generateRandomSequence(int min, int max, int count) {
		Random random = new Random();
		IntStream randomNumbers = random.ints(min, max + 1).distinct().limit(count);
		String result = randomNumbers.mapToObj(String::valueOf).collect(Collectors.joining(" "));
		return result;
	}

	// Method to show a confirmation dialog
	private boolean showConfirmationDialog(String title, String content) {
		Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
		confirmation.setTitle(title);
		confirmation.setHeaderText(null);
		confirmation.setContentText(content);

		ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		confirmation.getButtonTypes().setAll(okButton, cancelButton);

		Optional<ButtonType> result = confirmation.showAndWait();
		return result.isPresent() && result.get() == okButton;
	}

}
