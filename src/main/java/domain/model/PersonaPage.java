package domain.model;

import java.util.List;

public class PersonaPage {

    public List<Persona> content;
    public long totalElements;
    public int totalPages;
    public int currentPage;
    public int size;

    public PersonaPage(List<Persona> content, long totalElements, int totalPages, int currentPage, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.size = size;
    }
}
