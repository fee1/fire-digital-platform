package com.huajie.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PeriodDTO {

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

}
