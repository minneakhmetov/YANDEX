package com.yandex.rest.repository;

import com.yandex.rest.jooq.tables.records.NoteRecord;
import com.yandex.rest.model.NoteModel;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.yandex.rest.jooq.tables.Note.NOTE;

@Repository
public class NoteRepository {

    @Autowired
    private DSLContext dslContext;


    private static final Integer ONE = 1;

    public Optional<NoteModel> getById(Integer id){
        return dslContext.select()
                .from(NOTE)
                .where(NOTE.ID.eq(id))
                .fetchOptionalInto(NoteModel.class);
    }

    public List<NoteModel> getAll(){
        return dslContext.select()
                .from(NOTE)
                .fetchInto(NoteModel.class);
    }

    public boolean deleteById(Integer id){
        return dslContext.delete(NOTE)
                .where(NOTE.ID.eq(id))
                .execute() == ONE;
    }

    public NoteModel save(NoteModel noteModel) {
        return dslContext.insertInto(NOTE)
                .set(NOTE.CONTENT, noteModel.getContent())
                .set(NOTE.TITLE, noteModel.getTitle())
                .returning(NOTE.CONTENT, NOTE.TITLE, NOTE.ID)
                .fetchOne()
                .map(r -> r.into(NOTE).into(NoteModel.class));
    }

    public boolean update(NoteModel noteModel){
        return this.dslContext
                .update(NOTE)
                .set(NOTE.CONTENT, noteModel.getContent())
                .set(NOTE.TITLE, noteModel.getTitle())
                .where(NOTE.ID.eq(noteModel.getId()))
                .execute() == ONE;
    }

    public List<NoteModel> searchBy(String searchQuery, TableField<NoteRecord, String> field){
        return dslContext.select()
                .from(NOTE)
                .where(field.like("%" + searchQuery + "%"))
                .fetchInto(NoteModel.class);

    }

    public List<NoteModel> searchBy(String searchQuery, TableField<NoteRecord, String> field, Set<Integer> notContain){
        return dslContext.select()
                .from(NOTE)
                .where(field.like("%" + searchQuery + "%").and(NOTE.ID.notIn(notContain)))
                .fetchInto(NoteModel.class);

    }





}
