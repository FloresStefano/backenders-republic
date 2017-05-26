package it.addvalue.demo;

public class FitnesseSampleTableRecord {

    private Long id;
    private String word;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "FitnesseSampleTableRecord{id=" + id + ", word='" + word + "\'}";
    }
}
