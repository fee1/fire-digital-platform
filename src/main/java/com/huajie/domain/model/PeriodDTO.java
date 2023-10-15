package com.huajie.domain.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class PeriodDTO {

    private LocalDate startDate;

    private LocalDate endDate;

}
