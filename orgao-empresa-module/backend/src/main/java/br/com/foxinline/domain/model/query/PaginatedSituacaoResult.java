package br.com.foxinline.domain.model.query;

import java.util.List;

import br.com.foxinline.domain.model.Orgao;
public class PaginatedSituacaoResult {
    public List<Orgao> content;
    public long totalElements;

    public PaginatedSituacaoResult(List<Orgao> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<Orgao> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
