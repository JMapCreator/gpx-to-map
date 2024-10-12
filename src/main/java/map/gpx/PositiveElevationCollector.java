package map.gpx;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Execute a reduce operation on a collection of integer to
 * sum positive difference between two following points.
 */
public class PositiveElevationCollector implements Collector<Integer, List<Integer>, Integer> {
    private Integer positiveElevation = 0;

    @Override
    public Supplier<List<Integer>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<Integer>, Integer> accumulator() {
        return (accumulator, current) -> {
            if (!accumulator.isEmpty() && current > accumulator.getLast()) {
                positiveElevation += current - accumulator.getLast();
            }
            accumulator.add(current);
        };
    }

    @Override
    public BinaryOperator<List<Integer>> combiner() {
        return (a, b) -> a;
    }

    @Override
    public Function<List<Integer>, Integer> finisher() {
        return (accumulator) -> positiveElevation;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }
}
