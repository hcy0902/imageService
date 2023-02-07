package com.image.Utils.Model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class TagItem {
    Tag tag;
    BigDecimal confidence;
}
