package br.com.foxinline.domain.model.query;

import java.util.List;

import br.com.foxinline.domain.model.TipoOrgao;

public class PaginatedTipoOrgaoResult {
    public List<TipoOrgao> content;
    public long totalElements;

    public PaginatedTipoOrgaoResult(List<TipoOrgao> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<TipoOrgao> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
