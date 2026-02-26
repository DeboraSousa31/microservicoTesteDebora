package br.com.foxinline.domain.model.query;

import java.util.List;

import br.com.foxinline.domain.model.SituacaoProprietario;

public class PaginatedSituacaoResult {
    public List<SituacaoProprietario> content;
    public long totalElements;

    public PaginatedSituacaoResult(List<SituacaoProprietario> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<SituacaoProprietario> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
