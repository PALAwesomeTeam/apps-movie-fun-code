package org.superbiz.moviefun;


import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    // single TransactionTemplate shared amongst all methods in this instance
//    private final PlatformTransactionManager albumsTransactionManager;
//    private final PlatformTransactionManager moviesTransactionManager;

    private final TransactionTemplate albumTransactionTemplate;
    private final TransactionTemplate movieTransactionTemplate;

    // use constructor-injection to supply the PlatformTransactionManager
//    public SimpleService(PlatformTransactionManager transactionManager) {
//        this.transactionTemplate = new TransactionTemplate(transactionManager);
//    transactionManager}

    public HomeController(MoviesBean moviesBean, AlbumsBean albumsBean, MovieFixtures movieFixtures, AlbumFixtures albumFixtures, PlatformTransactionManager albumsTransactionManager, PlatformTransactionManager moviesTransactionManager) {
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
        //this.albumsTransactionManager = albumsTransactionManager;
        //this.moviesTransactionManager = moviesTransactionManager;

        albumTransactionTemplate = new TransactionTemplate(albumsTransactionManager);
        movieTransactionTemplate = new TransactionTemplate(moviesTransactionManager);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")

    public String setup(Map<String, Object> model) {
        for (Movie movie : movieFixtures.load()) {
            movieTransactionTemplate.execute(new TransactionCallbackWithoutResult() {

                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        moviesBean.addMovie(movie);
                    } catch (Exception ex) {
                        status.setRollbackOnly();
                    }
                }
            });
        }

        for (Album album : albumFixtures.load()) {

            albumTransactionTemplate.execute(new TransactionCallbackWithoutResult() {

                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        albumsBean.addAlbum(album);
                    } catch (Exception ex) {
                        status.setRollbackOnly();
                    }
                }
            });
        }



        model.put("movies", moviesBean.getMovies());
        model.put("albums", albumsBean.getAlbums());

        return "setup";
    }
}
