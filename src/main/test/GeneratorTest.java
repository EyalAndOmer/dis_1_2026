import uniza.fri.majba.dis1.simulation_core.generators.ContinuousEmpiricGenerator;
import uniza.fri.majba.dis1.simulation_core.generators.DiscreteEmpiricGenerator;
import uniza.fri.majba.dis1.simulation_core.generators.EmpiricGeneratorConfiguration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class GeneratorTest {
    Random seedGenerator = new Random(12345);

    List<EmpiricGeneratorConfiguration> continuousEmpiricGeneratorConfiguration = List.of(
            new EmpiricGeneratorConfiguration(10, 20, 0.1),
            new EmpiricGeneratorConfiguration(20, 32, 0.5),
            new EmpiricGeneratorConfiguration(32, 45, 0.2),
            new EmpiricGeneratorConfiguration(45, 75, 0.15),
            new EmpiricGeneratorConfiguration(75, 85, 0.05)
    );

    ContinuousEmpiricGenerator continuousEmpiricGenerator = new ContinuousEmpiricGenerator(continuousEmpiricGeneratorConfiguration, seedGenerator);

    List<EmpiricGeneratorConfiguration> discreteEmpiricGeneratorConfiguration = List.of(
            new EmpiricGeneratorConfiguration(15, 28, 0.2),
            new EmpiricGeneratorConfiguration(29, 44, 0.4),
            new EmpiricGeneratorConfiguration(45, 65, 0.4)
    );    Random seedGenerator = new Random(12345);


    DiscreteEmpiricGenerator discreteEmpiricGenerator = new DiscreteEmpiricGenerator(discreteEmpiricGeneratorConfiguration, seedGenerator);

    void discreteEmpiricGeneratorTest() {
        String fileName = "discrete_empiric_generator_output.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            writer.write("index,value");
            writer.newLine();

            for (int i = 0; i < 1_000_000; i++) {

                double value = discreteEmpiricGenerator.generate();

                writer.write(i + "," + value);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void continuousEmpiricGeneratorTest() {
        String fileName = "continuous_empiric_generator_output.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            writer.write("value");
            writer.newLine();

            for (int i = 0; i < 1_000_000; i++) {

                double value = continuousEmpiricGenerator.generate();

                writer.write(String.valueOf(value));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void main() {
        GeneratorTest generatorTest = new GeneratorTest();
        generatorTest.continuousEmpiricGeneratorTest();
        generatorTest.discreteEmpiricGeneratorTest();
    }
}
