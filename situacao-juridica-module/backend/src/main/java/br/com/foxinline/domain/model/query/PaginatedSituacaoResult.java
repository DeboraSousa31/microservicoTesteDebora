package br.com.foxinline.domain.model.query;

import java.util.List;

import br.com.foxinline.domain.model.SituacaoJuridica;
public class PaginatedSituacaoResult {
    public List<SituacaoJuridica> content;
    public long totalElements;

    public PaginatedSituacaoResult(List<SituacaoJuridica> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<SituacaoJuridica> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
