package com.almightyvats.sensorsafe.model.custom;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SanityCheckCount {
    private SanityCheckType sanityCheckType;
    private int count;
}
