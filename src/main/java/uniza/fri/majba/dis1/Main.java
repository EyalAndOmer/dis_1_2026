package uniza.fri.majba.dis1;

import atlantafx.base.theme.NordDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uniza.fri.majba.dis1.traversal_simulation.MonteCarloSimulationCore;
import uniza.fri.majba.dis1.traversal_simulation.TraversalReplication;

public class Main {

//    @Override
//    public void start(Stage stage) throws Exception {
//        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
//        FXMLLoader loader = new FXMLLoader(
//                getClass().getResource("/chart_view.fxml")
//        );
//
//        Scene scene = new Scene(loader.load());
//
//        scene.getStylesheets().add(
//                getClass().getResource("/chart_styles.css").toExternalForm()
//        );
//
//        stage.setTitle("Simulácia trasy");
//        stage.setScene(scene);
//        stage.setMinWidth(1200);
//        stage.setMinHeight(760);
//        stage.show();
//    }

    public static void main(String[] args) {
        TraversalReplication traversalReplication = new TraversalReplication();
        MonteCarloSimulationCore simulationCore = new MonteCarloSimulationCore(traversalReplication);
        simulationCore.simulate(1);    }
}
