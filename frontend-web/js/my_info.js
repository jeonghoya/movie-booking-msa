// static/js/my_info.js

document.addEventListener('DOMContentLoaded', () => {
    //const token = localStorage.getItem('token');
    const isLoggedIn = localStorage.getItem('isLoggedIn'); // ✨ 수정

    if (!isLoggedIn) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login.html';
        return;
    }

    // ✨ --- 1. 누락되었던 사용자 정보 조회 fetch 코드 --- ✨
    fetch('/api/user/me')
    .then(response => response.json())
    .then(userInfo => {
        // 받아온 정보로 HTML 요소의 내용을 채움
        document.getElementById('user-id').textContent = userInfo.id;
        document.getElementById('user-email').textContent = userInfo.email;
        document.getElementById('user-username').textContent = userInfo.username;
    })
    .catch(error => {
        console.error('Error fetching user info:', error);
        alert('사용자 정보를 불러오는 데 실패했습니다.');
    });


    // ✨ --- 2. 기존에 있던 내 리뷰 목록 조회 fetch 코드 --- ✨
    const myReviewListDiv = document.getElementById('my-review-list');
    fetch('/api/core/users/me/reviews')
    .then(res => res.json())
    .then(reviews => {
        myReviewListDiv.innerHTML = '';

        if (reviews.length === 0) {
            myReviewListDiv.innerHTML = '<p>작성된 리뷰가 없습니다.</p>';
            return;
        }

        reviews.forEach(review => {
            const reviewCard = document.createElement('div');
            reviewCard.className = 'review-card';
            reviewCard.innerHTML = `
                <strong>${review.movieInfo.title}</strong> (별점: ${review.rating})
                <p>${review.content}</p>
            `;
            myReviewListDiv.appendChild(reviewCard);
        });
    })
    .catch(error => {
        console.error('Error fetching my reviews:', error);
        myReviewListDiv.innerHTML = '<p>리뷰를 불러오는 데 실패했습니다.</p>';
    });
});