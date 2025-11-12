// 페이지 로딩이 완료되면 실행
document.addEventListener('DOMContentLoaded', function () {
    const movieListDiv = document.getElementById('movie-list');

    // 백엔드 API에 영화 목록을 요청
    fetch('/movies')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(movies => {
            // 응답받은 영화 데이터로 HTML 요소를 만들어 화면에 추가
            movies.forEach(movie => {
                const movieCard = document.createElement('div');
                movieCard.className = 'movie-card';
                // --- ✨ h3 태그를 a 태그로 감싸줍니다 ---
                movieCard.innerHTML = `
                    <a href="/movie.html?id=${movie.id}">
                        <h3>${movie.title}</h3>
                    </a>
                    <p>감독: ${movie.director}</p>
                    <p>장르: ${movie.genre}</p>
                    <p>상영시간: ${movie.runningTime}분</p>
                    <button class="booking-btn" data-movie-id="${movie.id}">예매하기</button>
                `;
                movieListDiv.appendChild(movieCard);
            });
            // --- ✨ 아래 이벤트 리스너 로직을 새로 추가합니다. ---
            const bookingButtons = document.querySelectorAll('.booking-btn');
            bookingButtons.forEach(button => {
                button.addEventListener('click', (e) => {
                    const token = localStorage.getItem('token');
                    if (!token) {
                        alert('로그인이 필요합니다.');
                        window.location.href = '/login.html';
                        return;
                    }

                    const movieId = e.target.dataset.movieId;
                    // 예매 페이지로 영화 ID를 넘기면서 이동
                    window.location.href = `/booking.html?movieId=${movieId}`;
                });
            });
        })
        .catch(error => {
            console.error('Error fetching movies:', error);
            movieListDiv.textContent = '영화를 불러오는 데 실패했습니다.';
        });
});