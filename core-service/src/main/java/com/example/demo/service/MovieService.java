package com.example.demo.service;

import com.example.demo.domain.Movie;
import com.example.demo.dto.MovieRequestDto;
import com.example.demo.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // 1. 이 클래스가 비즈니스 로직을 처리하는 서비스 계층임을 명시
@RequiredArgsConstructor // 2. final 필드에 대한 생성자 자동 주입
public class MovieService {

    private final MovieRepository movieRepository; // 3. 데이터베이스 작업을 위해 Repository를 주입받음


    // 모든 영화를 조회하는 로직
    public List<Movie> findAllMovies() {
        return movieRepository.findAll(); // 4. Repository에게 모든 영화 데이터를 요청
    }

    // ID로 특정 영화를 조회하는 로직
    public Optional<Movie> findMovieById(Long id) {
        return movieRepository.findById(id); // 5. Repository에게 특정 ID의 영화 데이터를 요청
    }

    // (추가) 나중에 영화를 등록하고, 수정하고, 삭제하는 등의 복잡한 규칙이 포함된 로직이 여기에 들어갑니다.
    // 영화 등록
    public Movie createMovie(MovieRequestDto requestDto) {
        Movie movie = new Movie();
        movie.setTitle(requestDto.getTitle());
        movie.setDirector(requestDto.getDirector());
        movie.setGenre(requestDto.getGenre());
        movie.setRunningTime(requestDto.getRunningTime());
        return movieRepository.save(movie);
    }

    // 영화 정보 수정
    @Transactional
    public Movie updateMovie(Long id, MovieRequestDto requestDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화를 찾을 수 없습니다."));

        movie.setTitle(requestDto.getTitle());
        movie.setDirector(requestDto.getDirector());
        movie.setGenre(requestDto.getGenre());
        movie.setRunningTime(requestDto.getRunningTime());
        return movie; // @Transactional에 의해 메소드 종료 시 자동으로 DB에 업데이트됨
    }

    // 영화 삭제
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
