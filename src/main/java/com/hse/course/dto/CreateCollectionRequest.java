package com.hse.course.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class CreateCollectionRequest {
    @NotBlank
    private String name;

    private String description;

    @NotEmpty
    private List<String> interests;

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotEmpty List<String> getInterests() {
        return interests;
    }

    public void setInterests(@NotEmpty List<String> interests) {
        this.interests = interests;
    }
}

