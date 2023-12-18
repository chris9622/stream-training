package it.ecubit.java.training.collections.test;

import it.ecubit.java.training.beans.Movie;
import it.ecubit.java.training.loader.tmdb.ImdbLoader;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class JavaCollectionsTest {

    public static void main(String[] args) {
        // Loading of all the top 1000 movies can take up to 10 minutes (needs to call the TMDB APIs for retrieving all the data)
        List<Movie> top1000Movies = ImdbLoader.loadMovies();

        // Exercise 1: Sort the movies by release year (from the most recent to the less recent)
        // and print the results with a counter before the movie info, one for each row
        // (i. e. '1) <MOVIE INFO>'\n'2) <MOVIE INFO>', ...)
        AtomicInteger counter= new AtomicInteger(1);
        top1000Movies.stream()
                .sorted(Comparator.comparingInt(Movie::getYear).reversed())
                        .forEachOrdered(e-> System.out.println(counter.getAndIncrement() + " ) " + e));

        // Exercise 2: Sort the movies lexicographically by title
        // and print the results with a counter before the movie info, one for each row
        // (i. e. '1) <MOVIE INFO>'\n'2) <MOVIE INFO>', ...)
        counter.set(1);
       top1000Movies.stream()
                .sorted(Comparator.comparing(Movie::getTitle))
               .forEachOrdered(e-> System.out.println(counter.getAndIncrement()+ " ) "+ e));

        // Exercise 3: How many movies has been directed by 'Peter Jackson'? Print all of them, one by line.
        top1000Movies.stream()
                .filter(movie -> movie.getDirectors().stream()
                        .anyMatch(director -> director.getName().equals("Peter Jackson")))
                .collect(Collectors.toList()).forEach(System.out::println);
        // Exercise 4: How many movies did 'Orlando Bloom' star in as an actor? Print all of them, one by line.
        top1000Movies.stream()
                .filter(movie -> movie.getActors().stream()
                        .anyMatch(actor -> actor.getName().equals("Orlando Bloom")))
                .collect(Collectors.toList()).forEach(System.out::println);
        // Exercise 5: Sort the movies by rating (ascending, from the less rated to the most rated)
        // and by movie title (lexicographically) as a secondary sort criterion
        // and print the results with a counter before the movie info, one for each row
        counter.set(1);
        top1000Movies.stream()
                .sorted(Comparator.comparing(Movie::getRating)
                        .thenComparing(Comparator.comparing(Movie::getTitle)))
                .forEachOrdered(e -> System.out.println(counter.getAndIncrement() + ") " + e));

        // Exercise 6: Sort the movies by duration (ascending, from the shortest to the longest oned)
        // and by release year (ascending, from the less recent to the most recent one) as a secondary sort criterion
        // and print the results with a counter before the movie info, one for each row

        counter.set(1);
        top1000Movies.stream()
                .sorted(Comparator.comparing(Movie::getDuration)
                        .thenComparing(Comparator.comparing(Movie::getYear)))
                .forEachOrdered(e -> System.out.println(counter.getAndIncrement() + ") " + e));




        // Exercise 7: Group movies by actor, i.e. produce a map with actor name as key and a list of movies as values;
        // the list should contain the films in which the actor starred in (no duplicates)
        // and print the map with a counter before the map entry, one for each row

//        Map<String, List<Movie>> actorMoviesMap = new HashMap<>();
//        int index = 1;
//        top1000Movies.forEach(movie -> {
//            movie.getActors().forEach(actor -> {
//                actorMoviesMap.computeIfAbsent(actor.getName(), k -> {
//                    return new ArrayList<>();
//                }).add(movie);
//            });
//        });
//        for (String key : actorMoviesMap.keySet()) {
//            System.out.println(index++ + ") Attori: " + key + ", Filmografia: " + actorMoviesMap.get(key));
//        }

        counter.set(1);
        Map<String, List<Movie>> actorMoviesMap = new HashMap<>();
        top1000Movies.forEach(movie ->
                movie.getActors().forEach(actor ->
                        actorMoviesMap.computeIfAbsent(actor.getName(), k -> new ArrayList<>())
                                .add(movie)
                )
        );
        actorMoviesMap.forEach((key, value) ->
                System.out.println(counter.getAndIncrement() + ") Attori: " + key + " " + value)
        );

        // Exercise 8: Group movies by director, i.e. produce a map with director name as key and a list of movies as values;
        // the list should contain the films in which the director took care of the direction (no duplicates)
        // and print the map with a counter before the map entry, one for each row

        counter.set(1);
        Map<String, List<Movie>> directorsMoviesMap = new HashMap<>();
        top1000Movies.forEach(movie ->
                movie.getDirectors().forEach(director ->
                        directorsMoviesMap.computeIfAbsent(director.getName(), k -> new ArrayList<>())
                                .add(movie)
                )
        );
        directorsMoviesMap.forEach((key, value) ->
                System.out.println(counter.getAndIncrement() + ") Direttori: " + key + " " + value)
        );

        // Exercise 9: Add the film's box office total income to the movie loading process (field 'Gross' in the CSV)
        // and print the first 20 films who earned most money ever, one for each row, from the first to the 20th

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);

        top1000Movies.stream()
                .sorted((m1, m2) -> {
                    try {
                        Number gross1 = decimalFormat.parse(m1.getGross().isEmpty() ? "0" : m1.getGross());
                        Number gross2 = decimalFormat.parse(m2.getGross().isEmpty() ? "0" : m2.getGross());
                        return Double.compare(gross2.doubleValue(), gross1.doubleValue());
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                })
                .limit(20)
                .forEach(movie ->
                        System.out.println((movie.getTitle() + " - Gross: " + movie.getGross())));


        // Exercise 10: Add the number of votes received on the Social Media for each film (field 'No_of_Votes' in the CSV)
        // and print the first 20 films who received most votes, one for each row, from the first to the 20th

        top1000Movies.stream()
                .sorted(Comparator.comparingLong(Movie::getVotes).reversed())
                .limit(20)
                .forEach(movie -> System.out.println(movie.getTitle() + " " + movie.getVotes()));
























    }
}