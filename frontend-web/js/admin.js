// static/js/admin.js

document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    // 간단한 JWT 해석 함수 (auth.js에도 동일한 함수가 있어야 함)
    function parseJwt(token) { try { return JSON.parse(atob(token.split('.')[1])); } catch (e) { return null; } }

    // 관리자가 아니면 페이지 접근 차단
    if (!token || !parseJwt(token) || parseJwt(token).role !== 'ADMIN') {
        alert('관리자만 접근할 수 있습니다.');
        window.location.href = '/';
        return;
    }

    // --- 1. ✨ 새로운 변수들을 선언합니다 ---
    const movieForm = document.getElementById('movie-form');
    const movieListBody = document.getElementById('admin-movie-list');
    const reviewListBody = document.getElementById('admin-review-list');

    const screeningForm = document.getElementById('screening-form');
    const movieSelect = document.getElementById('movie-select');
    const hallSelect = document.getElementById('hall-select');
    const screeningListBody = document.getElementById('admin-screening-list');

    const movieIdInput = document.getElementById('movieId');
    const cancelEditBtn = document.getElementById('cancel-edit-btn');

    // 영화 목록 불러오기 함수
    const fetchMovies = async () => {
        const response = await fetch('/api/core/movies');
        const movies = await response.json();
        movieListBody.innerHTML = ''; // 기존 목록 초기화
        movies.forEach(movie => {
            const row = `
                <tr>
                    <td>${movie.id}</td>
                    <td>${movie.title}</td>
                    <td>${movie.director}</td>
                    <td>
                        <button class="edit-btn" data-id="${movie.id}">수정</button>
                        <button class="delete-btn" data-id="${movie.id}">삭제</button>
                    </td>
                </tr>
            `;
            movieListBody.insertAdjacentHTML('beforeend', row);
        });
    };

    // ✨ 전체 리뷰 목록 불러오기 함수 추가
    // 전체 리뷰 목록 불러오기 함수 수정
    const fetchAllReviews = async () => {
        const response = await fetch('/api/core/admin/reviews', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const reviews = await response.json();
        reviewListBody.innerHTML = '';
        reviews.forEach(review => {
            // ✨ 이 row 생성 부분을 수정합니다
            const row = `
                <tr>
                    <td>${review.id}</td>
                    <td>${review.movieInfo ? review.movieInfo.title : 'N/A'}</td>
                    <td>${review.username}</td>
                    <td>${review.content}</td>
                    <td>
                        <button class="review-delete-btn" data-id="${review.id}">삭제</button>
                    </td>
                </tr>
            `;
            reviewListBody.insertAdjacentHTML('beforeend', row);
        });
    };

    // 영화 목록 불러와서 드롭다운 채우기
    const populateMovieSelect = async () => {
        const response = await fetch('/api/core/movies');
        const movies = await response.json();
        movies.forEach(movie => {
            const option = `<option value="${movie.id}">${movie.title}</option>`;
            movieSelect.insertAdjacentHTML('beforeend', option);
        });
    };

    // 상영관 목록 불러와서 드롭다운 채우기
    const populateHallSelect = async () => {
        const response = await fetch('/api/core/screening-halls'); // 백엔드에 GET /screening-halls API 필요
        const halls = await response.json();
        halls.forEach(hall => {
            const option = `<option value="${hall.id}">${hall.name}</option>`;
            hallSelect.insertAdjacentHTML('beforeend', option);
        });
    };

    // 등록된 모든 상영 목록 불러오기
    const fetchAllScreenings = async () => {
        // ✨ GET /screenings API는 모든 상영 정보를 반환합니다.
        const response = await fetch('/api/core/screenings');
        const screenings = await response.json();
        screeningListBody.innerHTML = '';
        screenings.forEach(s => {
            // ✨ --- 이 row 생성 부분을 DTO에 맞게 수정합니다 ---
            const row = `
                <tr>
                    <td>${s.screeningId}</td>
                    <td>${s.movieTitle}</td>
                    <td>${s.screeningHallName}</td>
                    <td>${new Date(s.screeningTime).toLocaleString('ko-KR')}</td>
                    <td>
                        <button class="screening-delete-btn" data-id="${s.screeningId}">삭제</button>
                    </td>
                </tr>
            `;
            // --- 여기까지 ---
            screeningListBody.insertAdjacentHTML('beforeend', row);
        });
    };

    // 폼 제출 (영화 등록/수정) 이벤트
    movieForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = movieIdInput.value;
        const movieData = {
            title: document.getElementById('title').value,
            director: document.getElementById('director').value,
            genre: document.getElementById('genre').value,
            runningTime: parseInt(document.getElementById('runningTime').value, 10),
        };

        const method = id ? 'PUT' : 'POST';
        const url = id ? `/movies/${id}` : '/movies';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(movieData)
        });

        if (response.ok) {
            alert(`영화가 성공적으로 ${id ? '수정' : '등록'}되었습니다.`);
            resetForm();
            fetchMovies();
        } else {
            alert('오류가 발생했습니다.');
        }
    });

    // --- 3. ✨ 상영 정보 추가 폼 제출 이벤트를 추가합니다 ---
    screeningForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const screeningData = {
            movieId: movieSelect.value,
            screeningHallId: hallSelect.value,
            screeningTime: document.getElementById('screening-time').value
        };

        try {
            const response = await fetch('/api/core/screenings', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(screeningData)
            });

            if (response.ok) {
                alert('상영 정보가 성공적으로 추가되었습니다.');
                screeningForm.reset();
                fetchAllScreenings(); // 목록 새로고침
            } else {
                const error = await response.json();
                alert(`추가 실패: ${error.message}`); // 중복 시간 오류 등 서버 메시지 표시
            }
        } catch (err) {
            console.error('Screening creation failed:', err);
        }
    });

    // ✨ 목록 테이블 내 버튼 클릭 이벤트 수정 (영화/리뷰 모두 처리)
    document.querySelector('main').addEventListener('click', async (e) => {
        const id = e.target.dataset.id;

        // --- 기존 영화 수정/삭제 로직 ---
        // 수정 버튼 클릭 시
        if (e.target.matches('.edit-btn')) {
            const response = await fetch(`/api/core/movies/${id}`);
            const movie = await response.json();

            movieIdInput.value = movie.id;
            document.getElementById('title').value = movie.title;
            document.getElementById('director').value = movie.director;
            document.getElementById('genre').value = movie.genre;
            document.getElementById('runningTime').value = movie.runningTime;
            cancelEditBtn.style.display = 'inline-block';
        }
        // 삭제 버튼 클릭 시
        if (e.target.matches('.delete-btn')) {
            if (confirm(`정말로 ID ${id} 영화를 삭제하시겠습니까?`)) {
                const response = await fetch(`/api/core/movies/${id}`, {
                    method: 'DELETE',
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                if (response.ok) {
                    alert('영화가 삭제되었습니다.');
                    fetchMovies();
                } else {
                    alert('삭제에 실패했습니다.');
                }
            }
        }

        // --- ✨ 리뷰 삭제 버튼 클릭 시 ---
        if (e.target.matches('.review-delete-btn')) {
            if (confirm(`정말로 ID ${id} 리뷰를 삭제하시겠습니까?`)) {
                const response = await fetch(`/api/core/reviews/${id}`, {
                    method: 'DELETE',
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                if (response.ok) {
                    alert('리뷰가 삭제되었습니다.');
                    fetchAllReviews(); // 리뷰 목록 새로고침
                } else {
                    alert('리뷰 삭제에 실패했습니다.');
                }
            }
        }

        // 상영 정보 삭제 버튼 클릭 시
        if (e.target.matches('.screening-delete-btn')) {
            if (confirm(`정말로 ID ${id} 상영 정보를 삭제하시겠습니까?`)) {
                const response = await fetch(`/api/core/screenings/${id}`, {
                    method: 'DELETE',
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                if (response.ok) {
                    alert('상영 정보가 삭제되었습니다.');
                    fetchAllScreenings(); // 목록 새로고침
                } else {
                    alert('삭제에 실패했습니다.');
                }
            }
        }
    });

    // 수정 취소 버튼
    cancelEditBtn.addEventListener('click', resetForm);

    function resetForm() {
        movieForm.reset();
        movieIdInput.value = '';
        cancelEditBtn.style.display = 'none';
    }

    // --- 5. ✨ 페이지 로드 시 실행할 함수들을 호출합니다 ---
    function initializePage() {
        fetchMovies();
        fetchAllReviews();
        populateMovieSelect();
        populateHallSelect();
        fetchAllScreenings();
    }
    initializePage();
});