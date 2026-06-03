package application.dto;

import java.util.List;

public class PersonaPageDTO {

    public List<PersonaDTO> content;
    public long totalElements;
    public int totalPages;
    public int currentPage;
    public int size;

    public PersonaPageDTO(List<PersonaDTO> content, long totalElements, int totalPages, int currentPage, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.size = size;
    }
}
