package com.yandex.rest.controller;

import com.yandex.rest.form.NoteForm;
import com.yandex.rest.jooq.tables.Note;
import com.yandex.rest.model.NoteModel;
import com.yandex.rest.repository.NoteRepository;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yandex.rest.jooq.tables.Note.NOTE;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @Value("${yandex.content.cut.limit}")
    private Integer limit;

    @GetMapping
    public ResponseEntity<List<NoteModel>> getAll(@RequestParam(required = false) String query) {
        if (Objects.isNull(query))
            return ResponseEntity.ok(noteRepository.getAll());
        else {
            List<NoteModel> byTitle = noteRepository.searchBy(query, NOTE.TITLE);
            Set<Integer> ids = byTitle.stream()
                    .map(NoteModel::getId)
                    .collect(Collectors.toSet());
            List<NoteModel> byContent = noteRepository
                    .searchBy(query, NOTE.CONTENT, ids)
                    .stream()
                    .peek(noteModel -> noteModel.setContent(noteModel.getContent().substring(0, noteModel.getContent().length() > limit ? limit : noteModel.getContent().length() - 1)))
                    .collect(Collectors.toList());
            byTitle.addAll(byContent);
            return ResponseEntity.ok(byTitle);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteModel> getById(@PathVariable Integer id) {
        return noteRepository.getById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NoteModel> save(@RequestBody NoteForm noteForm) {
        return ResponseEntity.ok(noteRepository.save(noteForm.toNoteModel()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Integer id) {
        return noteRepository.deleteById(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteModel> update(@PathVariable Integer id, @RequestBody NoteForm noteForm) {
        Optional<NoteModel> existing = noteRepository.getById(id);
        if (!existing.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        NoteModel toDb = NoteModel.builder()
                .id(id)
                .content(Objects.nonNull(noteForm.getContent()) ? noteForm.getContent() : existing.get().getContent())
                .title(Objects.nonNull(noteForm.getTitle()) ? noteForm.getTitle() : existing.get().getTitle())
                .build();
        if (noteRepository.update(toDb)) {
            return ResponseEntity.ok(toDb);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


}
