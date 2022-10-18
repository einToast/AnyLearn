package de.thb.anylearn.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String answer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date nextTime;

    @ManyToOne
    private Folder folder;
}
