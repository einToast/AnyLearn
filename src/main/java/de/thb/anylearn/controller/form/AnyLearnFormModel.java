package de.thb.anylearn.controller.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnyLearnFormModel {

    private int folderId = 0;
    private int[] categoryId;
}
