package br.com.foxinline.domain.model.query;

import java.util.List;

import br.com.foxinline.domain.model.Estado;
public class PaginatedSituacaoResult {
    public List<Estado> content;
    public long totalElements;

    public PaginatedSituacaoResult(List<Estado> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<Estado> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
