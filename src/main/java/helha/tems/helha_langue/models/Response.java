package helha.tems.helha_langue.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.CascadeType;

@Entity
@Getter
@Setter
@Table(name = "Reponses")
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Question_id")
    private Long questionId;

    @Column(name = "Reponse")
    private String reponse;

    @Column(name = "ReponseCorrect")
    private Boolean reponseCorrect;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "QCMQuestion_id", referencedColumnName = "QCMQuestion_id")
    private QCMQuestion qcmQuestion;

    // Getters and setters, constructors, etc.
}