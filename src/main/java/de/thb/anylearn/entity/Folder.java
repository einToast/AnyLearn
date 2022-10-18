package de.thb.anylearn.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    private String name;

    @OneToMany(mappedBy = "folderId", fetch = FetchType.EAGER)
    private List<Card> cardId;
}
