package br.com.foxinline.domain.model.query;

import java.util.List;
import br.com.foxinline.domain.model.TipoComodo;


public class PaginatedSituacaoResult {
    public List<TipoComodo> content;
    public long totalElements;

    public PaginatedSituacaoResult(List<TipoComodo> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<TipoComodo> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
