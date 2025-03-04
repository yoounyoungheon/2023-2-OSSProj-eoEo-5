package com.eoeo.eoeoservice.dto.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CoreCourseCreditResponseDto {

    private long coreLectureTypeId;

    private String coreLectureTypeName;

    private long coreLectureCredit;

}
