package cj.software.genetics.schedule.entity.setup;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class Tasks implements Serializable, Comparable<Tasks> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1)
    private Integer durationSeconds;

    @NotNull
    @Min(0)
    private Integer numberTasks;

    private Tasks() {
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public Integer getNumberTasks() {
        return numberTasks;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("duration", durationSeconds)
                .append("number", numberTasks);
        String result = builder.build();
        return result;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
                .append(durationSeconds);
        int result = builder.build();
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean result;
        if (otherObject instanceof Tasks other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(durationSeconds, other.durationSeconds);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int compareTo(Tasks other) {
        CompareToBuilder builder = new CompareToBuilder()
                .append(this.durationSeconds, other.durationSeconds);
        int result = builder.build();
        return result;
    }

    public static class Builder {
        protected Tasks instance;

        protected Builder() {
            instance = new Tasks();
        }

        public Tasks build() {
            Tasks result = instance;
            instance = null;
            return result;
        }

        public Builder withDurationSeconds(Integer durationSeconds) {
            instance.durationSeconds = durationSeconds;
            return this;
        }

        public Builder withNumberTasks(Integer numberTasks) {
            instance.numberTasks = numberTasks;
            return this;
        }
    }
}