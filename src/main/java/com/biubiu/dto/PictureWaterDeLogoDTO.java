package com.biubiu.dto;

import com.biubiu.core.DeLogo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PictureWaterDeLogoDTO {

    private String base64;

    private String type;

    private List<DeLogo> list;

}
