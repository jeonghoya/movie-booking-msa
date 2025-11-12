package com.example.demo.controller;

import com.example.demo.domain.Movie;
import com.example.demo.dto.MovieRequestDto;
import com.example.demo.dto.ScreeningResponseDto;
import com.example.demo.service.MovieService;
import com.example.demo.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 1. 이 클래스가 REST API를 처리하는 컨트롤러임을 명시
@RequestMapping("/api/core/movies") // 2. "/movies"로 들어오는 모든 요청을 이 컨트롤러가 처리
@RequiredArgsConstructor // 3. final로 선언된 필드의 생성자를 자동으로 만들어 줌 (의존성 주입)
public class MovieController {

    private final MovieService movieService; // 4. 서비스 계층의 기능을 사용하기 위해 주입받음
    private final ScreeningService screeningService;

    // 영화 목록 조회 API
    @GetMapping // 5. HTTP GET 요청을 처리하는 메소드. "/movies" 경로에 해당
    public ResponseEntity<List<Movie>> getAllMovies() {
        // 서비스에 요청을 위임하고, 받은 결과를 ResponseEntity로 감싸서 반환
        return ResponseEntity.ok(movieService.findAllMovies());
    }

    // 영화 상세 정보 조회 API
    @GetMapping("/{movieId}") // 6. "/movies/1" 과 같이 동적인 경로를 처리
    public ResponseEntity<Movie> getMovieById(@PathVariable Long movieId) { // 7. 경로의 {movieId} 값을 파라미터로 받음
        return movieService.findMovieById(movieId)
                .map(ResponseEntity::ok) // 8. 서비스 결과가 있으면(Optional.isPresent), 200 OK 응답
                .orElse(ResponseEntity.notFound().build()); // 9. 결과가 없으면(Optional.empty), 404 Not Found 응답
    }


    // --- ✨ 아래 CUD API들을 새로 추가합니다. ---
    // 이 API들은 나중에 ADMIN 권한이 있는 사용자만 호출할 수 있도록 막을 것입니다.
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody MovieRequestDto requestDto) {
        Movie movie = movieService.createMovie(requestDto);
        return ResponseEntity.ok(movie);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long movieId, @RequestBody MovieRequestDto requestDto) {
        Movie movie = movieService.updateMovie(movieId, requestDto);
        return ResponseEntity.ok(movie);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.ok("영화가 삭제되었습니다.");
    }

    @GetMapping("/{movieId}/screenings")
    public ResponseEntity<List<ScreeningResponseDto>> getScreeningsForMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(screeningService.getScreeningsForMovie(movieId));
    }
}
