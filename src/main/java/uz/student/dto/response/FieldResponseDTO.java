package uz.student.dto.response;

public class FieldResponseDTO {
    private Long id;
    private String name;
    private String universityName;

    public FieldResponseDTO() {
    }

    public FieldResponseDTO(Long id, String name, String universityName) {
        this.id = id;
        this.name = name;
        this.universityName = universityName;
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

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }
}
