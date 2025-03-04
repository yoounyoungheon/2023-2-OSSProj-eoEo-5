package com.eoeo.eoeoservice.domain.substitute_lecture;

import com.eoeo.eoeoservice.domain.BaseEntity;
import com.eoeo.eoeoservice.domain.lecture.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@SQLDelete(sql = "UPDATE substitute_lecture SET is_deleted = true WHERE id = ?")
public class SubstituteLecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_lecture_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Lecture originalLecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substitute_lecture_id", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Lecture substituteLecture;

}
