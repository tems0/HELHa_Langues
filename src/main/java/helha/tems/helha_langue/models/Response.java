package helha.tems.helha_langue.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "Reponse_id")
    private int responseId;

    @Column(name = "Reponse")
    private String response;

    @Column(name = "ReponseCorrect")
    private Boolean responseCorrect;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "qcmquestion_id")
    private QCMQuestion qcmQuestion;

    // Getters and setters, constructors, etc.
}