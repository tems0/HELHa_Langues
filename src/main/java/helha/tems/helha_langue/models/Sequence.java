package helha.tems.helha_langue.models;


import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.http.parser.MediaType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "sequence")
public class Sequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Sequence_id")
    private Long sequenceId;

    @Column(name = "Langues")
    private String langues;

    @Column(name = "AudioMP3")
    private String audioMP3;

    @Column(name = "VideoMP4")
    private String videoMP4;

    @Column(name = "Score")
    private Integer score;

    @Column(name = "Timer")
    private String timer;

    @Column(name = "ScoreObtenu")
    private Double scoreObtenu;

    @Column(name = "Completé")
    private Boolean complete;

    @ManyToMany(mappedBy = "sequences")
    private Set<User> Users = new HashSet<>();
}

