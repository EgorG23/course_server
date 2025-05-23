package com.hse.course.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Data
public class CreateCollectionRequest {
    @NotBlank
    private String name;

    @NotEmpty
    private String interests;

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotEmpty String getInterests() {
        return interests;
    }

    public void setInterests(@NotEmpty String interests) {
        this.interests = interests;
    }
}

