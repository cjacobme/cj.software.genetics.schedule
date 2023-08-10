package cj.software.genetics.schedule.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Min;
import java.io.Serial;
import java.io.Serializable;

public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Min(0)
    private int identifier;

    @Min(1)
    private int durationSeconds;

    private Task() {
    }

    public int getIdentifier() {
        return identifier;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
                .append(identifier);
        int result = builder.build();
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean result;
        if (otherObject instanceof Task other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(this.identifier, other.identifier);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("identifier", identifier)
                .append("duration", durationSeconds);
        String result = builder.build();
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected Task instance;

        protected Builder() {
            instance = new Task();
        }

        public Task build() {
            Task result = instance;
            instance = null;
            return result;
        }

        public Builder withIdentifier(int identifier) {
            instance.identifier = identifier;
            return this;
        }

        public Builder withDurationSeconds(int durationSeconds) {
            instance.durationSeconds = durationSeconds;
            return this;
        }
    }
}