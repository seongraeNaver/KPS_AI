package com.kimparksin.meatplus.dto;

import com.kimparksin.meatplus.entity.Image;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageDto {
    private Long id;
    private String inputImg;
    private String outputImg;
    private LocalDateTime inputTime;
    private LocalDateTime outputTime;
    private Long projectCode;

    public ImageDto(Image image) {
        this.id = image.getId();
        this.inputImg = image.getInputImg();
        this.outputImg = image.getOutputImg();
        this.inputTime = image.getInputTime();
        this.outputTime = image.getOutputTime();
        this.projectCode = image.getProject().getProjectCode();
    }
}
