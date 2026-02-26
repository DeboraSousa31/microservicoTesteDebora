package br.com.foxinline.domain.model.query;

import java.util.List;

import br.com.foxinline.domain.model.Status;
public class PaginatedSituacaoResult {
    public List<Status> content;
    public long totalElements;

    public PaginatedSituacaoResult(List<Status> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<Status> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
