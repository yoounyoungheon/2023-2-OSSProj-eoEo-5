package com.eoeo.eoeoservice.dto.core_management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddMajorRequestDto {

    private String name;

    private Long schoolId;

    private Long coreCourseId;

    private Long requiredCourseId;

    private Long selectiveCourseId;

    private Long selectiveCourseCredit;

    private Long totalCredit;

}
