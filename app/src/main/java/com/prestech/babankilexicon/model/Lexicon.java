package com.prestech.babankilexicon.model;

public class Lexicon {

    private String kejomWord;
    private String englishWord;
    private String partOfSpeech;
    private String pluralForm;
    private String variant;
    private String examplePhrase;
    private String pronunciation;
    private int lexiconId;

    public String getKejomWord() {
        return kejomWord;
    }

    public void setKejomWord(String kejomWord) {
        this.kejomWord = kejomWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getPluralForm() {
        return pluralForm;
    }

    public void setPluralForm(String pluralForm) {
        this.pluralForm = pluralForm;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getExamplePhrase() {
        return examplePhrase;
    }

    public void setExamplePhrase(String examplePhrase) {
        this.examplePhrase = examplePhrase;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public int getLexiconId() {
        return lexiconId;
    }

    public void setLexiconId(int lexiconId) {
        this.lexiconId = lexiconId;
    }

    @Override
    public String toString() {
        /*return "\nLexicon [kejomWord=" + kejomWord + ", englishWord=" + englishWord + ", partOfSpeech=" + partOfSpeech
                + ", pluralForm=" + pluralForm + ", variant=" + variant + ", examplePhrase=" + examplePhrase
                + ", pronunciation=" + pronunciation + ", lexiconId=" + lexiconId + "]";*/

        return "\n[ lexiconId:" + lexiconId + "| Kejom word:"+kejomWord+ "]";
    }

}
