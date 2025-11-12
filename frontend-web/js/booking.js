// static/js/booking.js
document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const screeningId = params.get('screeningId');
    const token = localStorage.getItem('token');
    // --- ✨ 아래 변수 선언들을 추가합니다 ---
    const summaryMovieTitle = document.getElementById('summary-movie-title');
    const summaryHallName = document.getElementById('summary-hall-name');
    const summaryScreeningTime = document.getElementById('summary-screening-time');
    const summaryTotalPrice = document.getElementById('summary-total-price');

    if (!token || !screeningId) {
        alert('잘못된 접근입니다.');
        window.location.href = '/';
        return;
    }

    const seatMap = document.getElementById('seat-map');
    const summarySeats = document.getElementById('summary-seats');
    const confirmBtn = document.getElementById('confirm-booking-btn');

    let screeningDetails = null;
    let selectedSeats = [];
    const TICKET_PRICE = 15000; // 티켓 가격

    // 상영 정보와 예매된 좌석 정보를 동시에 가져옴
        Promise.all([
            fetch(`/screenings/${screeningId}`).then(res => res.json()),
            fetch(`/screenings/${screeningId}/booked-seats`).then(res => res.json())
        ]).then(([screeningData, bookedSeats]) => {
            screeningDetails = screeningData;

            // ✨ 변경된 DTO 구조에 맞게 수정합니다.
            summaryMovieTitle.textContent = screeningDetails.movieTitle;
            summaryHallName.textContent = screeningDetails.screeningHall.name;
            summaryScreeningTime.textContent = new Date(screeningDetails.screeningTime).toLocaleString('ko-KR');

            // ✨ 이제 screeningHall 정보가 screeningData 안에 포함되어 있습니다.
            generateSeatMap(screeningDetails.screeningHall, bookedSeats);
        }).catch(err => {
            console.error("Error fetching data:", err)
            alert("예매 정보를 불러오는 데 실패했습니다. 다시 시도해주세요.");
        });

    function generateSeatMap(hall, bookedSeats) {
        seatMap.style.gridTemplateColumns = `repeat(${hall.totalColumns}, 1fr)`;
        for (let r = 0; r < hall.totalRows; r++) {
            for (let c = 0; c < hall.totalColumns; c++) {
                const seat = document.createElement('div');
                const seatId = `${String.fromCharCode(65 + r)}${c + 1}`;
                seat.className = 'seat';
                seat.dataset.seatId = seatId;

                if (bookedSeats.includes(seatId)) {
                    seat.classList.add('booked');
                } else {
                    seat.classList.add('available');
                }
                seatMap.appendChild(seat);
            }
        }
    }

    seatMap.addEventListener('click', e => {
        if (e.target.matches('.seat.available')) {
            e.target.classList.toggle('selected');
            updateSelection(e.target.dataset.seatId);
        }
    });

    function updateSelection(seatId) {
        const index = selectedSeats.indexOf(seatId);
        if (index > -1) {
            selectedSeats.splice(index, 1);
        } else {
            selectedSeats.push(seatId);
        }

        summarySeats.textContent = selectedSeats.length > 0 ? selectedSeats.sort().join(', ') : '없음';
        summaryTotalPrice.textContent = (selectedSeats.length * TICKET_PRICE).toLocaleString(); // ✨ 변수 이름 수정
    }

    confirmBtn.addEventListener('click', async () => {
        if (selectedSeats.length === 0) {
            alert('좌석을 선택해주세요.');
            return;
        }

        try {
            const response = await fetch('/bookings', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ screeningId: screeningId, seats: selectedSeats })
            });
            if (response.ok) {
                alert('예매가 성공적으로 완료되었습니다.');
                window.location.href = '/my_bookings.html';
            } else {
                const errorData = await response.json();
                alert(`예매 실패: ${errorData.message}`);
            }
        } catch (err) {
            console.error('Booking failed:', err);
        }
    });
});