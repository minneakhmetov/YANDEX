package com.yandex.rest.form;

import com.yandex.rest.model.NoteModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NoteForm {
    private String title;
    private String content;

    public NoteModel toNoteModel(){
        return NoteModel.builder()
                .content(content)
                .title(title)
                .build();
    }
}
