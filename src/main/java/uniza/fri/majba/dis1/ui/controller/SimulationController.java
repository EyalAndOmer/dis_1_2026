package uniza.fri.majba.dis1.ui.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import uniza.fri.majba.dis1.simulation_core.SimulationCore;
import uniza.fri.majba.dis1.traversal_simulation.MonteCarloSimulationCore;
import uniza.fri.majba.dis1.traversal_simulation.TraversalReplication;
import uniza.fri.majba.dis1.ui.CallbackReplication;
import uniza.fri.majba.dis1.ui.callback.ReplicationCallback;

public final class SimulationController implements ReplicationCallback {

    @FXML private Pagination pagination;
    @FXML private VBox contentPanel;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button configButton;
    @FXML private TabPane tabPane;
    @FXML private VBox chartsContainer;
    @FXML private GridPane chartsGrid;

    private static final int DEFAULT_REPLICATIONS = 100_000;

    private SimulationCore simulationCore;
    private CallbackReplication replication;
    private Thread simulationThread;

    @FXML
    void initialize() {
        this.replication = new TraversalReplication();
        this.simulationCore = new MonteCarloSimulationCore(replication);
        replication.setCallback(this);
    }

    @FXML
    void onStartSimulation() {
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }

        contentPanel.setVisible(true);
        contentPanel.setManaged(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                simulationCore.simulate(DEFAULT_REPLICATIONS);
                return null;
            }
        };

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            if (exception != null) {
                exception.printStackTrace();
                System.exit(1);
            }
        });

        task.setOnSucceeded(event -> {
            System.out.println("DONE");
        });

        simulationThread = new Thread(task);
        simulationThread.setDaemon(true);
        simulationThread.setName("simulation-thread");
        simulationThread.start();
    }

    @FXML
    private void onStopSimulation() {
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }
        contentPanel.setVisible(false);
        contentPanel.setManaged(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void onReplicationComplete(double result) {
        System.out.println(result);
    }
}
