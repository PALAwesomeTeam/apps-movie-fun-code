package org.superbiz.moviefun;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean mBean;

    public HomeController(MoviesBean mBean){
        this.mBean = mBean;
    }
    @GetMapping("/")
    public String indexPage(){
        return("index");
    }
    @GetMapping("/setup")
    public String setupPage(Map<String, Object> model){
        setup();
        model.put("movies",mBean.getMovies());
        return("setup");
    }

    private void setup(){
        mBean.addMovie(new Movie("Wedding Crashers", "David Dobkin", "Comedy", 7, 2005));
        mBean.addMovie(new Movie("Starsky & Hutch", "Todd Phillips", "Action", 6, 2004));
        mBean.addMovie(new Movie("Shanghai Knights", "David Dobkin", "Action", 6, 2003));
        mBean.addMovie(new Movie("I-Spy", "Betty Thomas", "Adventure", 5, 2002));
        mBean.addMovie(new Movie("The Royal Tenenbaums", "Wes Anderson", "Comedy", 8, 2001));
        mBean.addMovie(new Movie("Zoolander", "Ben Stiller", "Comedy", 6, 2001));
        mBean.addMovie(new Movie("Shanghai Noon", "Tom Dey", "Comedy", 7, 2000));

    }
}
