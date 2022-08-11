package db;

public class PatternInstance {
    private String pattern;

    public PatternInstance(String pattern) {
        this.pattern = pattern;
    }

    public boolean contains(char symbol) {
        return this.pattern.contains(String.valueOf(symbol));
    }

    public boolean intersects(PatternInstance that) {
        return this.pattern.contains(that.getPattern());
    }

    public String encrypt(String sequence) {
        StringBuilder message = new StringBuilder();
        for (char symbol : sequence.toCharArray()) {
            if (symbol == '0' || this.pattern.indexOf(symbol) != -1) {
                message.append(symbol);
            } else {
                message.append('*');
            }
        }
        return message.toString();
    }

    public String getPattern() {
        return this.pattern;
    }

    @Override
    public String toString() {
        return "PatternInstance{" +
                "pattern=" + pattern +
                '}';
    }
}
