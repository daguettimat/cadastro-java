
package br.com.global5.showcase.model;

public class Movie {
    
    private String movie;
    private String directedBy;
    private String genres;
    private int runTime;
    
    public Movie() {}
    
    public Movie(String movie, String directedBy, String genres, int runTime) {
        this.movie = movie;
        this.directedBy = directedBy;
        this.genres = genres;
        this.runTime = runTime;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getDirectedBy() {
        return directedBy;
    }

    public void setDirectedBy(String directedBy) {
        this.directedBy = directedBy;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }
}
