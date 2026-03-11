package uniza.fri.majba.dis1.ui.chart;

import javafx.util.StringConverter;

import java.text.DecimalFormat;


/**
 * Custom number axis converter, that makes the displayed numbers on the axes more readable.
 * The converter uses the symbol K for thousands and M for millions
 */
public class NumberAxisConverter extends StringConverter<Number> {
    private final DecimalFormat formatter;

    public NumberAxisConverter(String decimalFormat) {
        this.formatter = new DecimalFormat(decimalFormat);
    }

    @Override
    public Number fromString(String string) {
        return null;
    }

    @Override
    public String toString(Number value) {
        double doubleValue = value.doubleValue();
        if (doubleValue < 1_000) {
            return formatter.format(doubleValue);
        } else if (doubleValue < 1_000_000) {
            return formatter.format(doubleValue / 1_000) + "K";
        } else {
            return formatter.format(doubleValue / 1_000_000) + "M";
        }
    }
}