package cj.software.genetics.schedule.entity.setup;

import javafx.scene.paint.Color;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class Priority implements Serializable, Comparable<Priority> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(0)
    private Integer value;

    @NotNull
    @Min(1)
    private Integer numSlots;

    @NotNull
    @SuppressWarnings("java:S1948") // ignore not Serializable
    private Color foreground;

    @NotNull
    @SuppressWarnings("java:S1948") // ignore not Serializable
    private Color background;

    @NotEmpty
    private final SortedSet<Tasks> tasks = new TreeSet<>();

    private Priority() {
    }

    public Integer getValue() {
        return value;
    }

    public Integer getNumSlots() {
        return numSlots;
    }

    public Color getForeground() {
        return foreground;
    }

    public Color getBackground() {
        return background;
    }

    public SortedSet<Tasks> getTasks() {
        return Collections.unmodifiableSortedSet(tasks);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int compareTo(Priority other) {
        CompareToBuilder builder = new CompareToBuilder()
                .append(this.value, other.value);
        int result = builder.build();
        return result;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
                .append(value);
        int result = builder.build();
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean result;

        if (otherObject instanceof Priority other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(this.value, other.value);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    public static class Builder {
        protected Priority instance;

        protected Builder() {
            instance = new Priority();
        }

        public Priority build() {
            Priority result = instance;
            instance = null;
            return result;
        }

        public Builder withValue(Integer value) {
            instance.value = value;
            return this;
        }

        public Builder withNumSlots(Integer numSlots) {
            instance.numSlots = numSlots;
            return this;
        }

        public Builder withForeground(Color foreground) {
            instance.foreground = foreground;
            return this;
        }

        public Builder withBackground(Color background) {
            instance.background = background;
            return this;
        }

        public Builder withTasks(Collection<Tasks> tasks) {
            instance.tasks.clear();
            if (tasks != null) {
                instance.tasks.addAll(tasks);
            }
            return this;
        }
    }
}