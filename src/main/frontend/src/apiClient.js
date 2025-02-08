import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:3000', // 백엔드 API의 기본 URL로 변경
    headers: {
        'Content-Type': 'application/json',
    },
});

let isRefreshing = false; // 토큰 재발급 요청 중 여부
let refreshSubscribers = []; // 재발급 후 대기 중인 요청들

const onRrefreshed = (newAccessToken) => {
    // 재발급이 완료되면 대기 중인 요청들을 처리
    refreshSubscribers.forEach((callback) => callback(newAccessToken));
    refreshSubscribers = [];
};

const addRefreshSubscriber = (callback) => {
    refreshSubscribers.push(callback);
};

// 요청 인터셉터: Authorization 헤더에 토큰 추가
apiClient.interceptors.request.use((config) => {
    const accessToken = sessionStorage.getItem('accessToken');
    if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
});

// 응답 인터셉터: 401 에러 처리
apiClient.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
            const accessToken = sessionStorage.getItem('accessToken');
            if (!accessToken) {
                return Promise.reject(error);
            }
            originalRequest._retry = true; // 재시도 플래그 설정

            if (!isRefreshing) {
                isRefreshing = true;

                try {
                    const { data } = await axios.post('/api/auth/refresh', {}, {
                        withCredentials: true, // 리프레쉬 토큰 포함
                    });
                    const newAccessToken = data.accessToken;

                    sessionStorage.setItem('accessToken', newAccessToken);
                    onRrefreshed(newAccessToken); // 대기 중인 요청들에 토큰 전달
                    isRefreshing = false;

                    // 요청 재전송
                    originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                    return apiClient(originalRequest);
                } catch (refreshError) {
                    isRefreshing = false;
                    refreshSubscribers = []; // 대기 중인 요청 초기화
                    return Promise.reject(refreshError); // 실패 처리
                }
            }

            // 다른 요청이 리프레쉬 중인 경우 대기
            return new Promise((resolve) => {
                addRefreshSubscriber((newAccessToken) => {
                    originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                    resolve(apiClient(originalRequest)); // 대기 후 재전송
                });
            });
        }

        return Promise.reject(error);
    }
);

export default apiClient;