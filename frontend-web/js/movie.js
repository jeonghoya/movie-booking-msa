document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const movieId = params.get('id');
    //const token = localStorage.getItem('token');
    const isLoggedIn = localStorage.getItem('isLoggedIn');

    const movieDetailsDiv = document.getElementById('movie-details');
    const reviewListDiv = document.getElementById('review-list');
    const reviewFormContainer = document.getElementById('review-form-container');
    const reviewForm = document.getElementById('review-form');
    // --- ✨ 여기에 누락된 변수 선언을 추가합니다 ---
    const screeningListDiv = document.getElementById('screening-list');

    if (!movieId) {
        alert('영화 정보가 올바르지 않습니다.');
        window.location.href = '/';
        return;
    }

    fetch(`/api/core/movies/${movieId}`)
        .then(res => res.json())
        .then(movie => {
            movieDetailsDiv.innerHTML = `
                <h2>${movie.title}</h2>
                <p><strong>감독:</strong> ${movie.director}</p>
                <p><strong>장르:</strong> ${movie.genre}</p>
                <p><strong>상영시간:</strong> ${movie.runningTime}분</p>
            `;
        });


    // ✨ --- 상영 시간표 불러오기 로직 추가 ---
    fetch(`/api/core/movies/${movieId}/screenings`)
        .then(res => res.json())
        .then(screenings => {
            if (screenings.length === 0) {
                screeningListDiv.innerHTML = '<p>현재 상영 정보가 없습니다.</p>';
                return;
            }
            screenings.forEach(screening => {
                const screeningLink = document.createElement('a');
                screeningLink.className = 'screening-item';
                // 클릭하면 screeningId를 가지고 booking.html로 이동
                screeningLink.href = `/booking.html?screeningId=${screening.screeningId}`;
                screeningLink.innerHTML = `
                    <strong>${screening.screeningHallName}</strong>
                    <span>${new Date(screening.screeningTime).toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })}</span>
                `;
                screeningListDiv.appendChild(screeningLink);
            });
        });


    // 리뷰 목록 불러오기 함수
    const fetchReviews = () => {
        fetch(`/api/core/movies/${movieId}/reviews`)
            .then(res => res.json())
            .then(reviews => {
                reviewListDiv.innerHTML = ''; // 기존 목록 초기화
                if (reviews.length === 0) {
                    reviewListDiv.innerHTML = '<p>작성된 리뷰가 없습니다.</p>';
                    return;
                }
                reviews.forEach(review => {
                    const reviewEl = document.createElement('div');
                    reviewEl.className = 'review-card'; // CSS용 클래스
                    reviewEl.innerHTML = `
                        <strong>${review.username}</strong> (별점: ${review.rating})
                        <p>${review.content}</p>
                    `;
                    reviewListDiv.appendChild(reviewEl);
                });
            });
    };

    // 로그인 상태이면 리뷰 작성 폼 보여주기
    if (isLoggedIn) {
        reviewFormContainer.style.display = 'block';
    }

    // 리뷰 작성 폼 제출 이벤트
    reviewForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const content = document.getElementById('content').value;
        const rating = parseInt(document.getElementById('rating').value, 10);

        const response = await fetch(`/api/core/movies/${movieId}/reviews`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify({ content, rating })
        });

        if (response.ok) {
            alert('리뷰가 등록되었습니다.');
            reviewForm.reset();
            fetchReviews(); // 리뷰 목록 새로고침
        } else {
            alert('리뷰 등록에 실패했습니다.');
        }
    });

    // 페이지 로드 시 리뷰 목록 최초 호출
    fetchReviews();
});