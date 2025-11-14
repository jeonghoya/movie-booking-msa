// static/js/my_bookings.js

document.addEventListener('DOMContentLoaded', () => {
    const bookingsListDiv = document.getElementById('bookings-list');
    //const token = localStorage.getItem('token');
    const isLoggedIn = localStorage.getItem('isLoggedIn');

    if (!isLoggedIn) {
        alert('로그인이 필요합니다.');
        window.location.href = '/login.html';
        return;
    }

    // 1. 내 예매 내역 API 호출
    fetch('/api/core/bookings', {credentials: 'include'})
    .then(response => response.json())
    .then(bookings => {
        if (bookings.length === 0) {
            bookingsListDiv.textContent = '예매 내역이 없습니다.';
            return;
        }

        bookings.forEach(booking => {
            const bookingCard = document.createElement('div');
            bookingCard.className = 'booking-card';
            // ✨ 이 부분을 수정합니다
            bookingCard.innerHTML = `
                <div class="booking-info">
                    <h4>${booking.movieTitle}</h4>
                    <p><strong>상영관:</strong> ${booking.screeningHallName}</p>
                    <p><strong>상영일:</strong> ${new Date(booking.screeningTime).toLocaleString('ko-KR')}</p>
                    <p><strong>좌석:</strong> ${booking.seats.join(', ')}</p>
                    <p><strong>예매일:</strong> ${new Date(booking.bookingTime).toLocaleString('ko-KR')}</p>
                </div>
                <button class="cancel-btn" data-booking-id="${booking.bookingId}">예매 취소</button>
            `;
            bookingsListDiv.appendChild(bookingCard);
        });
    });

    // 2. 예매 취소 버튼 이벤트 처리 (이벤트 위임 사용)
    bookingsListDiv.addEventListener('click', async (e) => {
        if (e.target && e.target.matches('.cancel-btn')) {
            const bookingId = e.target.dataset.bookingId;

            if (confirm('정말로 이 예매를 취소하시겠습니까?')) {
                try {
                    const response = await fetch(`/api/core/bookings/${bookingId}`, {
                        method: 'DELETE',
                        credentials: 'include'
                    });

                    if (response.ok) {
                        alert('예매가 성공적으로 취소되었습니다.');
                        window.location.reload(); // 페이지 새로고침으로 목록 갱신
                    } else {
                        alert('예매 취소에 실패했습니다.');
                    }
                } catch (error) {
                    console.error('Cancellation error:', error);
                    alert('예매 취소 중 오류가 발생했습니다.');
                }
            }
        }
    });
});