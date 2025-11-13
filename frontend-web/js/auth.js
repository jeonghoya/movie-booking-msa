// auth.js

// ✨ 파일 상단 또는 DOMContentLoaded 리스너 바깥에 JWT 해석 함수 추가
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // === 네비게이션 메뉴 상태 업데이트 로직 (모든 페이지에서 실행) ===
    const token = localStorage.getItem('token');
    const loginLink = document.getElementById('login-link');
    const signupLink = document.getElementById('signup-link');
    const myBookingsLink = document.getElementById('my-bookings-link');
    const myInfoLink = document.getElementById('my-info-link'); // ✨ 변수 추가
    const adminLink = document.getElementById('admin-link'); // ✨ 변수 추가
    const logoutLink = document.getElementById('logout-link');

    // ✨ 각 링크가 존재하는 경우에만 스타일을 변경하도록 수정
    if (token) {
        // 로그인 상태일 때
        const decodedToken = parseJwt(token); // ✨ 토큰 해석
        const userRole = decodedToken ? decodedToken.role : null;

        if (loginLink) loginLink.style.display = 'none';
        if (signupLink) signupLink.style.display = 'none';
        if (myBookingsLink) myBookingsLink.style.display = 'inline';
        if (myInfoLink) myInfoLink.style.display = 'inline'; // ✨ '내 정보' 링크 보이기
        if (logoutLink) logoutLink.style.display = 'inline';

        // ✨ ADMIN 역할일 경우 관리자 페이지 링크 보이기
        if (userRole === 'ADMIN' && adminLink) {
            adminLink.style.display = 'inline';
        }

        if (logoutLink) {
            logoutLink.addEventListener('click', async (e) => {
                e.preventDefault();
                await fetch('/api/user/logout', {
                    method: 'POST',
                    headers: { 'Authorization': `Bearer ${token}` }
                });
                localStorage.removeItem('token');
                alert('로그아웃 되었습니다.');
                window.location.href = '/';
            });
        }

    } else {
        // 로그아웃 상태일 때
        if (loginLink) loginLink.style.display = 'inline';
        if (signupLink) signupLink.style.display = 'inline';
        if (myBookingsLink) myBookingsLink.style.display = 'none';
        if (myInfoLink) myInfoLink.style.display = 'none'; // ✨ '내 정보' 링크 숨기기
        if (adminLink) adminLink.style.display = 'none'; // ✨ 관리자 링크 숨기기
        if (logoutLink) logoutLink.style.display = 'none';
    }

    // 회원가입 페이지 로직
    if (document.getElementById('signup-form')) {
        const signupForm = document.getElementById('signup-form');

        signupForm.addEventListener('submit', async (e) => {
            e.preventDefault(); // 폼의 기본 제출 동작(새로고침)을 막음

            const email = document.getElementById('email').value;
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            try {
                const response = await fetch('/api/user/signup', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ email, username, password }),
                });

                if (response.ok) {
                    alert('회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.');
                    window.location.href = '/login.html'; // 로그인 페이지로 리디렉션
                } else {
                    const errorData = await response.json(); // 에러 메시지가 JSON 형태일 경우
                    alert('회원가입 실패: ' + (errorData.message || '다시 시도해주세요.'));
                }
            } catch (error) {
                console.error('Signup error:', error);
                alert('오류가 발생했습니다.');
            }
        });
    }

    // 로그인 페이지 로직
    if (document.getElementById('login-form')) {
        const loginForm = document.getElementById('login-form');

        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            try {
                const response = await fetch('/api/user/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ email, password }),
                });

                if (response.ok) {
                    const token = await response.text(); // JWT는 일반 텍스트로 응답됨
                    localStorage.setItem('token', token); // 브라우저의 localStorage에 토큰 저장
                    alert('로그인 성공!');
                    window.location.href = '/'; // 메인 페이지로 리디렉션
                } else {
                    alert('로그인 실패: 이메일 또는 비밀번호를 확인해주세요.');
                }
            } catch (error) {
                console.error('Login error:', error);
                alert('오류가 발생했습니다.');
            }
        });
    }
});