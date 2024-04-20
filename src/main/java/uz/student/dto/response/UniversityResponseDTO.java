package uz.student.dto.response;

public class UniversityResponseDTO {

    private Long id;

    public UniversityResponseDTO() {
    }

    private String name;

    public UniversityResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
