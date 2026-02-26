package br.com.foxinline.domain.model.query;

import java.util.List;
import br.com.foxinline.domain.model.Atualizacao;


public class PaginatedSituacaoResult {
    public List<Atualizacao> content;
    public long totalElements;

    public PaginatedSituacaoResult(List<Atualizacao> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<Atualizacao> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }
}
