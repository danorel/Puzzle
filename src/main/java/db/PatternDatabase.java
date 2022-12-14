package db;

import java.util.*;
import java.util.stream.IntStream;

import entities.Action;
import entities.Agent;
import entities.World;
import org.apache.commons.math3.util.Pair;

public class PatternDatabase {

    private PatternDatabase instance;

    private final HashMap<Pattern, HashMap<String, Integer>> index;

    public PatternDatabase() {
        this.index = new HashMap<>();
    }

    public PatternDatabase getInstance() {
        if (instance == null) {
            return new PatternDatabase();
        }
        return instance;
    }

    public PatternDatabase addDefaultPatterns(int k) {
        int batchSize = k - 1;
        int batchLength = k + 1;

        String[] sequences = new String[batchSize];
        Arrays.fill(sequences, "");

        for (int i = 0; i < batchSize; ++i) {
            for (int j = 0; j < batchLength; ++j) {
                sequences[i] += (i * batchLength + (j + 1)) + ",";
            }
            this.addCustomPattern(sequences[i]);
        }

        return this;
    }

    public PatternDatabase addCustomPattern(String sequence) {
        Pattern targetPattern = new Pattern(sequence);
        for (Pattern existingPattern : this.index.keySet()) {
            if (existingPattern.intersects(targetPattern)) {
                throw new RuntimeException("Patterns cannot overlap!");
            }
        }
        this.index.put(new Pattern(sequence), new HashMap<>());
        return this;
    }

    public PatternDatabase compute(Agent initialAgent, World initialWorld) {
        index.forEach(((pattern, patternIndex) -> {
            Agent patternAgent = new Agent(initialAgent);
            World patternWorld = new World(initialWorld, pattern);
            patternIndex.put(patternWorld.getSerialization(), patternWorld.evaluate());

            Queue<Pair<Agent, World>> frontier = new LinkedList<>();
            frontier.add(new Pair<>(patternAgent, patternWorld));

            while (!frontier.isEmpty()) {
                Pair<Agent, World> front = frontier.poll();

                Agent currentAgent = front.getFirst();
                World currentWorld = front.getSecond();

                for (Action action : Action.values()) {
                    Agent nextAgent = currentAgent.transition(action);
                    if (nextAgent == null) {
                        continue;
                    }
                    if (nextAgent.belongsTo(currentWorld)) {
                        World nextWorld = currentWorld.transition(currentAgent, action);
                        if (nextWorld == null) {
                            continue;
                        }
                        String nextSerialization = nextWorld.getSerialization();
                        if (!patternIndex.containsKey(nextSerialization)) {
                            frontier.add(new Pair<>(nextAgent, nextWorld));
                            patternIndex.put(nextSerialization, nextWorld.evaluate());
                        }
                    }
                }
            }
        }));

        return this;
    }

    public int evaluate(World world) {
        String serialization = world.getSerialization();

        IntStream patternScores = index.entrySet().stream().map(((patternEntry) -> {
            Pattern pattern = patternEntry.getKey();
            HashMap<String, Integer> patternIndex = patternEntry.getValue();

            String serializationPattern = pattern.encrypt(serialization);

            Integer patternScore = patternIndex.get(serializationPattern);

            return patternScore == null ? 0 : patternScore;
        })).mapToInt(Integer::valueOf);

        return patternScores.sum();
    }
}
