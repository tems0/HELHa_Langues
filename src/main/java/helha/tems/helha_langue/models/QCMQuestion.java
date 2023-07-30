package helha.tems.helha_langue.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "QCMQuestion")
public class QCMQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QCMQuestion_id")
    private Long qcmQuestionId;

    @Column(name = "QuestionNom")
    private String questionNom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Sequence_id", referencedColumnName = "sequence_id")
    private Sequence sequence;

    // Getters and setters, constructors, etc.
}

