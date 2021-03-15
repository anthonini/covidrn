package br.com.anthonini.arquitetura.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {
	
	private static final int QUANTIDADE_PAGINAS = 3;

	private Page<T> page;
	private UriComponentsBuilder uriBuilder;

	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;
		String httpUrl = httpServletRequest.getRequestURL().append(
				httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "")
				.toString().replaceAll("\\+", "%20").replaceAll("excluido", "");
		this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl);
	}
	
	public List<T> getConteudo() {
		return page.getContent();
	}
	
	public boolean isVazia() {
		return page.getContent().isEmpty();
	}
	
	public int getAtual() {
		return page.getNumber();
	}
	
	public int getPrimeiraPagina() {
		return Math.max(1, getAtual()+2 - QUANTIDADE_PAGINAS);
	}
	
	public int getUltimaPagina() {
		return Math.min(getTotal(), getPrimeiraPagina()+QUANTIDADE_PAGINAS-1);
	}
	
	public int getQuantidadePaginas() {
		return QUANTIDADE_PAGINAS;
	}
	
	public boolean isPrimeira() {
		return page.isFirst();
	}
	
	public boolean isUltima() {
		return page.isLast();
	}
	
	public int getTotal() {
		return page.getTotalPages();
	}

	public long getNumeroPrimeiroItemPagina() {
		return (long)page.getNumber()*(long)page.getSize()+1;
	}

	public long getNumeroUltimoItemPagina() {
		return getNumeroPrimeiroItemPagina()+page.getNumberOfElements()-1;
	}

	public long getTotalItens() {
		return page.getTotalElements();
	}
	
	public String urlParaPagina(int pagina) {
		return uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
	}
	
	public String urlOrdenada(String propriedade) {
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder
				.fromUriString(uriBuilder.build(true).encode().toUriString());
		
		String valorSort = String.format("%s,%s", propriedade, inverterDirecao(propriedade));
		
		return uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
	}
	
	public String inverterDirecao(String propriedade) {
		String direcao = "asc";
		
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		if (order != null) {
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
		}
		
		return direcao;
	}
	
	public boolean descendente(String propriedade) {
		return inverterDirecao(propriedade).equals("asc");
	}
	
	public boolean ordenada(String propriedade) {
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null; 
		
		if (order == null) {
			return false;
		}
		
		return page.getSort().getOrderFor(propriedade) != null ? true : false;
	}
	
}

