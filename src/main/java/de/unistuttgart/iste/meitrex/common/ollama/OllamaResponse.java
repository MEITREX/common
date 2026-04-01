package de.unistuttgart.iste.meitrex.common.ollama;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OllamaResponse {

    @JsonProperty("total_duration")
    private long totalDuration;
    @JsonProperty("load_duration")
    private long loadDuration;
    @JsonProperty("prompt_eval_count")
    private long promptEvalCount;
    @JsonProperty("prompt_eval_duration")
    private long promptEvalDuration;
    @JsonProperty("eval_count")
    private long evalCount;
    @JsonProperty("eval_duration")
    private long evalDuration;
    @JsonProperty("model")
    private String model;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("response")
    private String response;
    @JsonProperty("done")
    private boolean done;
    @JsonProperty("done_reason")
    private String doneReason;
    @JsonProperty("context")
    private long[] context;
    @JsonProperty("error")
    private String error;

    /**
     * @return The total duration of the request in milliseconds.
     */
    @JsonIgnore
    public long getTotalDuration() {
        return totalDuration;
    }

    /**
     * @return The duration of the model loading in milliseconds.
     */
    @JsonIgnore
    public long getLoadDuration() {
        return loadDuration;
    }

    /**
     * @return The number of prompt evaluations.
     */
    @JsonIgnore
    public long getPromptEvalCount() {
        return promptEvalCount;
    }

    /**
     * @return The duration of the prompt evaluation in milliseconds.
     */
    @JsonIgnore
    public long getPromptEvalDuration() {
        return promptEvalDuration;
    }

    /**
     * @return The number of evaluations.
     */
    @JsonIgnore
    public long getEvalCount() {
        return evalCount;
    }

    /**
     * @return The duration of the evaluation in milliseconds.
     */
    @JsonIgnore
    public long getEvalDuration() {
        return evalDuration;
    }

    /**
     * @return The model used for the request.
     */
    @JsonIgnore
    public String getModel() {
        return model;
    }

    /**
     * @return The creation time of the request in ISO 8601 format.
     */
    @JsonIgnore
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @return The response from the model.
     */
    @JsonIgnore
    public String getResponse() {
        return response;
    }

    /**
     * @return Whether the request is done or not.
     */
    @JsonIgnore
    public boolean isDone() {
        return done;
    }

    /**
     * @return The reason why the request is done.
     */
    @JsonIgnore
    public String getDoneReason() {
        return doneReason;
    }

    /**
     * @return The context of the request.
     */
    @JsonIgnore
    public long[] getContext() {
        return context;
    }

    @JsonIgnore
    public String getError() {
        return error;
    }
}
