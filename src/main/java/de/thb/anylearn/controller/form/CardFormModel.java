package de.thb.anylearn.controller.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardFormModel {

    private int id;
    private String question;
    private String answer;
    private int folderId;
    private int[] categoryId;
    private int[] userId;
    private int ownerId;
}

