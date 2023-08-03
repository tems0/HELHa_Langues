package helha.tems.helha_langue.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "QCMQuestion")
public class QCMQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QCMQuestion_id")
    private int qcmQuestionId;

    @Column(name = "QuestionNom")
    private String questionNom;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "sequence_id")
    private Sequence sequence;

    @OneToMany(mappedBy = "qcmQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Response> responses = new HashSet<>();
}

