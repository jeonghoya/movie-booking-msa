# 1. 기본 이미지로 Apache (httpd)의 경량 버전을 사용합니다.
FROM httpd:alpine

# 2. Apache의 기본 웹 루트 디렉토리(/usr/local/apache2/htdocs/)에
# 현재 폴더(frontend-web)의 모든 파일(js/, css/, *.html 등)을 '복사'합니다.
COPY . /usr/local/apache2/htdocs/

# 3. Apache는 기본적으로 80 포트를 사용합니다.
EXPOSE 80