package br.com.foxinline.domain.model.query;

import java.util.List;

import br.com.foxinline.domain.model.Interessado;
public class PaginatedSituacaoResult {
    public List<Interessado> content;
    public long totalElements;

    public PaginatedSituacaoResult(List<Interessado> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<Interessado> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
